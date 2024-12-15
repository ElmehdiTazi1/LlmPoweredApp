package org.mql.llm.services;

import org.mql.llm.models.Comment;
import org.mql.llm.repositories.CommentRepository;
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
import java.util.Optional;

@Service
public class CommentService {

    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String GROQ_API_KEY = "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";

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Optional<Comment> findCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> findAllComments() {
        return commentRepository.findAll();
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public Map<String, String> analyzeComment(String commentContent) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + GROQ_API_KEY);

        Map<String, Object> payload = new HashMap<>();
        payload.put("messages", List.of(Map.of(
                "role", "user",
                "content", "Je vous donne un commentaire. Analysez-le et fournissez-moi uniquement et directement pas de reponse donc juste json les résultats sous forme de JSON avec les clés suivantes : 'sentiment', 'nom de la marque', et 'nom du produit'. Voici le commentaire : \"" + commentContent + "\""
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

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");

            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

                if (message != null) {
                    Object content = message.get("content");
                    if (content instanceof String) {
                        String responseContent = (String) content;

                        ObjectMapper objectMapper = new ObjectMapper();
                        return objectMapper.readValue(responseContent, new TypeReference<Map<String, String>>() {
                        });
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

    public Comment processAndSaveComment(String commentContent) {
        Map<String, String> analysisResult = analyzeComment(commentContent);

        Comment comment = new Comment();
        comment.setContent(commentContent);
        comment.setSentiment(analysisResult.get("sentiment"));
        comment.setBrand(analysisResult.get("nom de la marque"));
        comment.setProduct(analysisResult.get("nom du produit"));

        // Notify admin if sentiment is negative
        if ("négatif".equalsIgnoreCase(comment.getSentiment())) {
            notifyAdmin(comment);
        }

        return saveComment(comment);
    }

    private void notifyAdmin(Comment comment) {
        // Logique de notification pour l'administrateur (e.g., via WebSocket, email, etc.)
        System.out.println("Notification admin : Commentaire négatif - " + comment.getContent());
    }
}
