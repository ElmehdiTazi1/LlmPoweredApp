package org.mql.llm.controllers;

import org.mql.llm.services.GroqCloudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final GroqCloudService groqCloudService;

    public CommentController(GroqCloudService groqCloudService) {
        this.groqCloudService = groqCloudService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, String>> analyzeComment(@RequestBody Map<String, String> request) {
        try {
            String comment = request.get("comment");
            Map<String, String> analysisResult = groqCloudService.analyzeComment(comment);
            return ResponseEntity.ok(analysisResult);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

}
