package com.pacman.customersupport.service;

import com.pacman.customersupport.entity.DocumentChunk;
import com.pacman.customersupport.repository.DocumentChunkRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VectorStoreService {

    private final VectorStore vectorStore;
    private final DocumentChunkRepository documentChunkRepository;

    public VectorStoreService(EmbeddingModel embeddingModel, DocumentChunkRepository documentChunkRepository) {
        // Use the constructor instead of the builder
        this.vectorStore = new SimpleVectorStore(embeddingModel); //save and load embeddings to/from a database
        this.documentChunkRepository = documentChunkRepository;
        // Don't load documents automatically to avoid API calls during startup
    }

    @PostConstruct
    public void init() {
        List<DocumentChunk> chunks = documentChunkRepository.findAll();
        List<Document> documents = chunks.stream()
                .map(c -> new Document(c.getContent(), Map.of("source", c.getSourceDocument())))
                .collect(Collectors.toList());
        // Add documents to vector store only if they exist in DB and API is available
        if (!documents.isEmpty()) {
            try {
                vectorStore.add(documents);
            } catch (Exception e) {
                System.out.println("Could not load documents from database: " + e.getMessage());
            }
        }
    }

    // Make this public so it can be called manually when needed
    public void loadInitialDocuments() {
        List<Document> documents = new ArrayList<>();

        // Sample knowledge base (you can load from files or DB)
        documents.add(new Document("""
                Our return policy allows returns within 30 days of purchase.
                Item must be unused and in original packaging.
                Refunds are processed within 5-7 business days.
                """, Map.of("source", "return_policy.txt")));

        documents.add(new Document("""
                We offer 24/7 customer support via chat and email.
                Phone support is available 9AM-6PM EST.
                """, Map.of("source", "support_hours.txt")));

        documents.add(new Document("""
                Standard shipping takes 3-5 business days.
                Express shipping takes 1-2 business days.
                Free shipping on orders over $50.
                """, Map.of("source", "shipping_policy.txt")));

        try {
            // Add to vector store
            vectorStore.add(documents);
            
            // Save to database
            saveDocumentsToDatabase(documents);
            
            System.out.println("Initial documents loaded successfully and saved to database!");
        } catch (Exception e) {
            System.out.println("Could not load initial documents: " + e.getMessage());
            System.out.println("This is likely due to API quota limits. You can try again later.");
        }
    }

    public List<Document> similaritySearch(String query) {
        return vectorStore.similaritySearch(query);
    }

    // Load extended knowledge base with more comprehensive information
    public void loadExtendedKnowledgeBase() {
        List<Document> documents = new ArrayList<>();

        // Product Information
        documents.add(new Document("""
                Our product catalog includes:
                - Electronics: Smartphones, Laptops, Tablets
                - Clothing: Men's, Women's, Children's apparel
                - Home & Garden: Furniture, Appliances, Decor
                - Books: Fiction, Non-fiction, Educational materials
                All products come with manufacturer warranty and quality guarantee.
                """, Map.of("source", "product_catalog.txt")));

        // Payment Information
        documents.add(new Document("""
                Accepted payment methods:
                - Credit Cards: Visa, MasterCard, American Express
                - Digital Payments: PayPal, Apple Pay, Google Pay
                - Buy Now Pay Later: Klarna, Afterpay
                - Bank Transfer for orders over $500
                All transactions are secured with SSL encryption.
                """, Map.of("source", "payment_methods.txt")));

        // Account Information
        documents.add(new Document("""
                Account Management:
                - Create account for faster checkout and order tracking
                - Password reset available via email verification
                - Update personal information and preferences anytime
                - View order history and download invoices
                - Manage newsletter subscriptions and notifications
                """, Map.of("source", "account_management.txt")));

        // Warranty & Support
        documents.add(new Document("""
                Warranty and Support:
                - 1-year manufacturer warranty on all electronics
                - 30-day return policy on clothing and accessories
                - Technical support available for electronics
                - Live chat support available 24/7
                - Email support with 24-hour response time
                - Phone support: Monday-Friday 9AM-6PM EST
                """, Map.of("source", "warranty_support.txt")));

        try {
            // Add to vector store
            vectorStore.add(documents);
            
            // Save to database
            saveDocumentsToDatabase(documents);
            
            System.out.println("Extended knowledge base loaded successfully and saved to database!");
        } catch (Exception e) {
            System.out.println("Could not load extended knowledge base: " + e.getMessage());
        }
    }

    // Helper method to save documents to database
    private void saveDocumentsToDatabase(List<Document> documents) {
        for (Document doc : documents) {
            String sourceDocument = (String) doc.getMetadata().get("source");
            
            // Check if document already exists to avoid duplicates
            if (!documentExists(doc.getContent(), sourceDocument)) {
                DocumentChunk chunk = DocumentChunk.builder()
                        .content(doc.getContent())
                        .sourceDocument(sourceDocument)
                        .embedding("") // We'll store embedding as empty string for now
                        .build();
                
                documentChunkRepository.save(chunk);
                System.out.println("Saved document to database: " + sourceDocument);
            } else {
                System.out.println("Document already exists in database: " + sourceDocument);
            }
        }
    }
    
    // Helper method to check if document already exists
    private boolean documentExists(String content, String sourceDocument) {
        List<DocumentChunk> existingChunks = documentChunkRepository.findAll();
        return existingChunks.stream()
                .anyMatch(chunk -> chunk.getContent().equals(content) && 
                         chunk.getSourceDocument().equals(sourceDocument));
    }

    // Get knowledge base statistics
    public String getKnowledgeBaseStats() {
        try {
            // Test if knowledge base has content by performing a search
            List<Document> testSearch = vectorStore.similaritySearch("policy");
            long dbCount = documentChunkRepository.count();
            
            if (testSearch.isEmpty() && dbCount == 0) {
                return "Knowledge base appears to be empty. Consider loading documents first.";
            } else {
                return String.format("Knowledge base status:\n" +
                    "- Vector store: Found %d relevant documents\n" +
                    "- Database: Contains %d document chunks\n" +
                    "- Status: Ready for queries", 
                    testSearch.size(), dbCount);
            }
        } catch (Exception e) {
            return "Knowledge base status unknown: " + e.getMessage();
        }
    }

    // Get list of documents in database
    public String getDatabaseDocumentsList() {
        List<DocumentChunk> chunks = documentChunkRepository.findAll();
        if (chunks.isEmpty()) {
            return "No documents found in database.";
        }
        
        StringBuilder result = new StringBuilder();
        result.append(String.format("Found %d documents in database:\n\n", chunks.size()));
        
        for (int i = 0; i < chunks.size(); i++) {
            DocumentChunk chunk = chunks.get(i);
            result.append(String.format("%d. Source: %s\n", i + 1, chunk.getSourceDocument()));
            result.append(String.format("   Content preview: %s...\n", 
                chunk.getContent().length() > 100 ? 
                chunk.getContent().substring(0, 100) : chunk.getContent()));
            result.append("\n");
        }
        
        return result.toString();
    }

    // Clear database
    public void clearDatabase() {
        documentChunkRepository.deleteAll();
        System.out.println("Database cleared successfully!");
    }
}