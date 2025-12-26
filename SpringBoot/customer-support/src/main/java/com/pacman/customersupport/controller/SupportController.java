package com.pacman.customersupport.controller;

import com.pacman.customersupport.dto.SupportRequest;
import com.pacman.customersupport.dto.SupportResponse;
import com.pacman.customersupport.service.AICustomerSupportService;
import com.pacman.customersupport.service.VectorStoreService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/support")
@CrossOrigin(origins = "*")
public class SupportController {
    private final AICustomerSupportService aiCustomerSupportService;
    private final VectorStoreService vectorStoreService;

    public SupportController(AICustomerSupportService aiCustomerSupportService, VectorStoreService vectorStoreService) {
        this.aiCustomerSupportService = aiCustomerSupportService;
        this.vectorStoreService = vectorStoreService;
    }

    @GetMapping("/health")
    public String health() {
        return "Customer Support Service is running with Ollama!";
    }

    @GetMapping("/test-ollama")
    public String testOllama() {
        try {
            String testResponse = aiCustomerSupportService
                    .generateResponse("Hello, can you tell me about your return policy?");
            return "Ollama is working! Response: " + testResponse;
        } catch (Exception e) {
            return "Ollama test failed: " + e.getMessage();
        }
    }

    @PostMapping("/load-documents")
    public String loadDocuments() {
        try {
            vectorStoreService.loadInitialDocuments();
            return "Documents loaded successfully!";
        } catch (Exception e) {
            return "Failed to load documents: " + e.getMessage();
        }
    }

    @PostMapping("/load-extended-kb")
    public String loadExtendedKnowledgeBase() {
        try {
            vectorStoreService.loadExtendedKnowledgeBase();
            return "Extended knowledge base loaded successfully!";
        } catch (Exception e) {
            return "Failed to load extended knowledge base: " + e.getMessage();
        }
    }

    @GetMapping("/knowledge-stats")
    public String getKnowledgeStats() {
        try {
            return vectorStoreService.getKnowledgeBaseStats();
        } catch (Exception e) {
            return "Failed to get knowledge base stats: " + e.getMessage();
        }
    }

    @GetMapping("/database-documents")
    public String getDatabaseDocuments() {
        try {
            return vectorStoreService.getDatabaseDocumentsList();
        } catch (Exception e) {
            return "Failed to get database documents: " + e.getMessage();
        }
    }

    @PostMapping("/clear-database")
    public String clearDatabase() {
        try {
            vectorStoreService.clearDatabase();
            return "Database cleared successfully!";
        } catch (Exception e) {
            return "Failed to clear database: " + e.getMessage();
        }
    }

    @PostMapping("/ask")
    public SupportResponse ask(@RequestBody SupportRequest request) {
        try {
            String answer = aiCustomerSupportService.generateResponse(request.getQuestion());
            return new SupportResponse(answer);
        } catch (Exception e) {
            return new SupportResponse(
                    "Sorry, I'm currently unable to process your request. Please check your API quota and try again later. Error: "
                            + e.getMessage());
        }
    }
}
