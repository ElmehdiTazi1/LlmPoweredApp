package org.mql.llm.controllers;

import org.mql.llm.services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class LlmTestController {
    private final CommentService commentService;

    public LlmTestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Map<String, String>>> analyzeComment(@RequestBody Map<String, String> request) {
        if (!request.containsKey("comment")) {
            return ResponseEntity.badRequest().build();
        }
        
        String comment = request.get("comment");
        Map<String, Map<String, String>> results = commentService.analyzeCommentWithAllServices(comment);
        return ResponseEntity.ok(results);
    }
}
