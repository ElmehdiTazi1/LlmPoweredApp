package org.mql.llm.services;

import org.mql.llm.config.ApiKeyProvider;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service responsable de l'interaction avec l'API Gemini pour la génération de contenu.
 */
public class GeminiService {
    private final ApiKeyProvider apiKeyProvider;
    private final String model;
    private final double temperature;
    private final int maxTokens;
    
    /**
     * Constructeur avec tous les paramètres pour une configuration complète.
     * 
     * @param apiKeyProvider Fournisseur de clé API
     * @param model Le modèle d'IA à utiliser
     * @param temperature La température pour la génération
     * @param maxTokens Nombre maximum de tokens pour la sortie
     */    public GeminiService(ApiKeyProvider apiKeyProvider, String model, double temperature, int maxTokens) {
        this.apiKeyProvider = apiKeyProvider;
        this.model = model;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
    }
    
    /**
     * Constructeur avec API key directe et paramètres de configuration.
     *
     * @param apiKey Clé API Gemini
     * @param model Le modèle d'IA à utiliser
     * @param temperature La température pour la génération
     * @param maxTokens Nombre maximum de tokens pour la sortie
     */
    public GeminiService(String apiKey, String model, double temperature, int maxTokens) {
        this.apiKeyProvider = new ApiKeyProvider(apiKey);
        this.model = model;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
    }
    
    /**
     * Constructeur avec API key directe et paramètres par défaut.
     *
     * @param apiKey Clé API Gemini
     */
    public GeminiService(String apiKey) {
        this(apiKey, "gemini-1.5-flash", 0.2, 8192);
    }
    
    /**
     * Constructeur avec ApiKeyProvider et paramètres par défaut pour le modèle.
     * 
     * @param apiKeyProvider Fournisseur de clé API
     */
    public GeminiService(ApiKeyProvider apiKeyProvider) {
        this(apiKeyProvider, "gemini-1.5-flash", 0.2, 8192);
    }
    
    /**
     * Constructeur par défaut avec valeurs par défaut.
     * Utilise seulement dans un contexte Spring avec @Service.
     */
    public GeminiService() {
        this(new ApiKeyProvider());
    }

    /**
     * Convertit le code JSP en Thymeleaf en utilisant le modèle Gemini.
     * 
     * @param prompt Le prompt complet avec les instructions et le code JSP
     * @return Map contenant le résultat de la conversion ou une erreur
     */    public Map<String, String> processJspConversion(String prompt) {
        String apiKey = apiKeyProvider.getApiKey();
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key="
                + apiKey;

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
