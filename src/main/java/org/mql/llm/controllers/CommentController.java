package org.mql.llm.controllers;

import org.mql.llm.models.Comment;
import org.mql.llm.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService groqCloudService) {
        this.commentService = groqCloudService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, String>> analyzeComment(@RequestBody Map<String, String> request) {
        try {
            String comment = request.get("comment");
            Comment analysisResult = commentService.processAndSaveComment(comment);
            Map<String,String> result = new HashMap<>();
            result.put(analysisResult.getSentiment().toString(),analysisResult.getSentiment());
            result.put(analysisResult.getBrand().toString(),analysisResult.getBrand());
            result.put(analysisResult.getProduct().toString(),analysisResult.getProduct());

            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/all")
    public List<Comment> getAllComments(){
    	return commentService.findAllComments();
    }

}
