package org.mql.llm.services;

import com.fasterxml.jackson.core.JsonParser;
import org.mql.llm.services.LLMService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LlamaService implements LLMService {

    private static final String LLAMA_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String LLAMA_API_KEY = "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";

    @Override
    public String getName() {
        return "Llama";
    }

    @Override
    public Map<String, String> analyzeComment(String commentContent) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + LLAMA_API_KEY);

        // Modifier le prompt pour être plus explicite sur le format attendu
        String prompt = "Analysez ce commentaire et retournez uniquement un objet JSON valide avec exactement ces trois clés : 'sentiment', 'nom de la marque', et 'nom du produit'. Ne pas inclure de backticks, de marqueurs de code ou de texte supplémentaire. Retournez uniquement le JSON brut. Le commentaire est : \"" + commentContent + "\"";

        Map<String, Object> payload = new HashMap<>();
        payload.put("messages", List.of(Map.of(
                "role", "user",
                "content", prompt
        )));
        payload.put("model", "llama3-8b-8192");
        payload.put("temperature", 1);
        payload.put("max_tokens", 512);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            Map<String, Object> response = restTemplate.postForObject(LLAMA_API_URL, request, Map.class);

            // Extraire les données spécifiques
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                if (message != null) {
                    String content = (String) message.get("content");

                    // Nettoyer la réponse des backticks et marqueurs markdown
                    content = content.replaceAll("```json", "")
                            .replaceAll("```", "")
                            .trim();

                    // Debug log
                    System.out.println("Llama Response content: " + content);

                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        // Configure ObjectMapper pour être plus permissif
                        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
                        return objectMapper.readValue(content, new TypeReference<Map<String, String>>() {});
                    } catch (Exception e) {
                        System.err.println("Failed to parse Llama JSON content: " + content);
                        throw new RuntimeException("Failed to parse Llama response as JSON: " + e.getMessage());
                    }
                }
            }
            throw new RuntimeException("Invalid response format from Llama API");
        } catch (Exception e) {
            throw new RuntimeException("Error calling Llama API: " + e.getMessage(), e);
        }
    }
}
