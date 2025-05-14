package org.mql.llm.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService implements LLMService {
    private static final String GEMINI_API_KEY = "AIzaSyDrd6yle9q8tW70cMc1jkxRkjGUSDnfu3s"; // Clé API mise à jour
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + GEMINI_API_KEY;

    @Override
    public String getName() {
        return "Gemini";
    }

    /**
     * Convertit le code JSP en Thymeleaf en utilisant le modèle Gemini.
     * 
     * @param prompt Le prompt complet avec les instructions et le code JSP
     * @return Map contenant le résultat de la conversion ou une erreur
     */    public Map<String, String> processJspConversion(String prompt) {
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
        generationConfig.put("temperature", 0.2); // Température plus basse pour la génération de code
        generationConfig.put("maxOutputTokens", 8192); // Plus de tokens pour gérer des fichiers plus grands
        payload.put("generationConfig", generationConfig);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    GEMINI_API_URL,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

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
                
                // Extraire le code converti du texte
                Map<String, String> result = new HashMap<>();
                result.put("conversion", text);
                return result;
            }
            
            return Map.of("error", "Aucun résultat retourné par l'API Gemini");
        } catch (Exception e) {
            return Map.of("error", "Error calling Gemini API: " + e.getMessage());
        }
    }    @Override
    public Map<String, String> analyzeComment(String commentContent) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String prompt = "Je vous donne un commentaire. Analysez-le et fournissez-moi uniquement et directement pas de reponse donc juste json les résultats sous forme de JSON avec les clés suivantes : 'sentiment', 'nom de la marque', et 'nom du produit', je veut resultat directement json .le commentaire : \"" + commentContent + "\"";
        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(part));

        Map<String, Object> payload = new HashMap<>();
        payload.put("contents", List.of(content));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + GEMINI_API_KEY;
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null) {
                throw new RuntimeException("Réponse vide de l'API Gemini");
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

                text = text.replaceAll("```json", "")
                        .replaceAll("```", "")
                        .trim();

                System.out.println("Response text: " + text);

                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readValue(text, new TypeReference<Map<String, String>>() {});
                } catch (Exception e) {
                    System.err.println("Failed to parse JSON response: " + text);
                    throw new RuntimeException("Failed to parse Gemini response as JSON: " + e.getMessage());
                }
            }

            throw new RuntimeException("Invalid response format from Gemini API");
        } catch (Exception e) {
            throw new RuntimeException("Error calling Gemini API: " + e.getMessage(), e);
        }
    }
}
