package org.mql.llm.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService implements LLMService {
    private static final String GEMINI_API_KEY = "AIzaSyBm-y3Jrqr2V-nA4oUGBlWVaaJBOyoPObA"; // Replace with your actual API key
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + GEMINI_API_KEY;

    @Override
    public String getName() {
        return "Gemini";
    }


    @Override
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
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");

            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> candidate = candidates.get(0);
                Map<String, Object> contents = (Map<String, Object>) candidate.get("content");
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
