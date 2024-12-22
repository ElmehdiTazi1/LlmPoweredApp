package org.mql.llm.services;


import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LlmManagerService {

    private final Map<String, LLMService> llmServices = new HashMap<>();

    public LlmManagerService(LlamaService llamaService, MistralService mistralService, GeminiService geminiService) {
        llmServices.put("llama", llamaService);
        llmServices.put("mistral", mistralService);
        llmServices.put("gemini", geminiService);
    }

    public Map<String, String> analyzeWithAllLlm(String commentContent) {
        Map<String, String> results = new HashMap<>();
        llmServices.forEach((name, service) -> {
            results.put(name, service.analyzeComment(commentContent).get("sentiment"));
        });
        return results;
    }
}
