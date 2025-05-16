package org.mql.llm.services;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${conversion.model:gemini-1.5-flash}")
    private String model;

    @Value("${conversion.temperature:0.2}")
    private double temperature;

    @Value("${conversion.max-tokens:8192}")
    private int maxTokens;

    /**
     * Convertit le code JSP en Thymeleaf en utilisant le modèle Gemini.
     * 
     * @param prompt Le prompt complet avec les instructions et le code JSP
     * @return Map contenant le résultat de la conversion ou une erreur
     */
    public Map<String, String> processJspConversion(String prompt) {
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key="
                + geminiApiKey;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(part));

        Map<String, Object> payload = new HashMap<>();
        payload.put("contents", List.of(content));

        // Configuration spécifique pour la génération de code
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", temperature); // Température plus basse pour la génération de code
        generationConfig.put("maxOutputTokens", maxTokens); // Plus de tokens pour gérer des fichiers plus grands
        payload.put("generationConfig", generationConfig);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    apiUrl, // Use apiUrl instead of GEMINI_API_URL
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null) {
                return Map.of("error", "Réponse vide de l'API Gemini");
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");

            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> candidate = candidates.get(0);

                @SuppressWarnings("unchecked")
                Map<String, Object> contents = (Map<String, Object>) candidate.get("content");

                @SuppressWarnings("unchecked")
                List<Map<String, Object>> parts = (List<Map<String, Object>>) contents.get("parts");

                String text = (String) parts.get(0).get("text");

                Map<String, String> result = new HashMap<>();
                result.put("conversion", text);
                return result;
            }

            return Map.of("error", "Aucun résultat retourné par l'API Gemini");
        } catch (Exception e) {
            return Map.of("error", "Error calling Gemini API: " + e.getMessage());
        }
    }
}
