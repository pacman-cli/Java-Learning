package com.pacman.customersupport.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.ai.chat.client.ChatClient;


import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

@Service
public class AICustomerSupportService {
    // This class can be used to orchestrate calls between the VectorStoreService
    // and other services like email, chat, etc.
    private final ChatClient chatClient;
    private final VectorStoreService vectorStoreService;

    public AICustomerSupportService(ChatClient chatClient, VectorStoreService vectorStoreService) {
        this.chatClient = chatClient;
        this.vectorStoreService = vectorStoreService;
    }

    // Add methods to handle customer queries, fetch relevant documents from
    // vector store, and generate AI responses.
    public String generateResponse(String question) {
        // Step 1: Retrieve relevant context
        List<Document> relevantDocs = vectorStoreService.similaritySearch(question);

        String context = relevantDocs.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n---\n"));

        // Step 2: Use chatClient to generate a response based on the documents and
        // query <-> Build prompt with context

        String prompt = """
                You are a friendly customer support agent.
                    Use the following context to answer the question.
                    If you don't know, say "I don't know based on provided info."

                    CONTEXT:
                    %s

                    QUESTION:
                    %s

                    ANSWER:
                    """.formatted(context, question);

        // Step 3: Call LLM using the fluent API
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
