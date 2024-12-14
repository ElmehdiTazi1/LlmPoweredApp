package org.mql.llm.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class GroqCloudService {

    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String GROQ_API_KEY = "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"; // Remplacez par votre clé API réelle

    public Map<String, String> analyzeComment(String comment) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + GROQ_API_KEY);

        Map<String, Object> payload = new HashMap<>();
        payload.put("messages", List.of(Map.of(
        	    "role", "user", 
        	    "content", "Je vous donne un commentaire. Analysez-le et fournissez-moi uniquement et directement pas de reponse donc juste json les résultats sous forme de JSON avec les clés suivantes : 'sentiment', 'nom de la marque', et 'nom du produit', je veut resultat directement json .le commentaire : \"" + comment + "\""
        	)));
        payload.put("model", "llama3-8b-8192");
        payload.put("temperature", 1);
        payload.put("max_tokens", 512);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                GROQ_API_URL,
                HttpMethod.POST,
                request,
                Map.class
            );

            // Assurez-vous que la réponse contient bien une liste sous "choices"
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");

            if (choices != null && !choices.isEmpty()) {
                // Extraire l'élément message et vérifier sa structure
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

                if (message != null) {
                    // Vérifier que "content" existe et est de type String
                    Object content = message.get("content");
                    if (content instanceof String) {
                        String responseContent = (String) content;
                        
                        // Convertir le contenu en JSON, en supposant qu'il est bien formaté
                        ObjectMapper objectMapper = new ObjectMapper();
                        return objectMapper.readValue(responseContent, new TypeReference<Map<String, String>>() {});
                    } else {
                        throw new RuntimeException("Le champ 'content' n'est pas de type String");
                    }
                } else {
                    throw new RuntimeException("Message est null ou mal formé");
                }
            }

            throw new RuntimeException("Invalid response format from API");
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to call GroqCloud API: " + e.getStatusCode() + " " + e.getStatusText());
        } catch (Exception e) {
            throw new RuntimeException("Error parsing response from GroqCloud API: " + e.getMessage(), e);
        }
    }


}

