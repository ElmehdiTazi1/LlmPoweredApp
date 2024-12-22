package org.mql.llm.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {

    private final List<LLMService> llmServices;

    public CommentService(List<LLMService> llmServices) {
        this.llmServices = llmServices;
    }

    public Map<String, Map<String, String>> analyzeCommentWithAllServices(String commentContent) {
        Map<String, Map<String, String>> analysisResults = new HashMap<>();

        for (LLMService llmService : llmServices) {
            try {
                Map<String, String> result = llmService.analyzeComment(commentContent);
                analysisResults.put(llmService.getName(), result);
            } catch (Exception e) {
                System.err.println("Erreur avec " + llmService.getName() + ": " + e.getMessage());
                analysisResults.put(llmService.getName(), Map.of("error", e.getMessage()));
            }
        }

        return analysisResults;
    }
}
