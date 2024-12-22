package org.mql.llm.controllers;

import org.mql.llm.models.Comment;
import org.mql.llm.services.CommentStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comment Analysis", description = "API endpoints for comment analysis with multiple LLM services")
@CrossOrigin(origins = "*")
public class CommentController {
    private final CommentStorageService commentStorageService;

    public CommentController(CommentStorageService commentStorageService) {
        this.commentStorageService = commentStorageService;
    }

    @PostMapping
    @Operation(summary = "Analyze and save a new comment",
            description = "Analyzes the comment using multiple LLM services and saves both the comment and analyses")
    @ApiResponse(responseCode = "201", description = "Comment successfully analyzed and saved")
    public ResponseEntity<Comment> createComment(@RequestBody Map<String, String> request) {
        if (!request.containsKey("comment")) {
            return ResponseEntity.badRequest().build();
        }
        String comment = request.get("comment");
        System.out.println(comment);
        Comment savedComment = commentStorageService.saveCommentWithAnalysis(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @GetMapping
    @Operation(summary = "Get all comments with their analyses",
            description = "Retrieves all comments along with their LLM analyses")
    public ResponseEntity<List<Comment>> getAllComments(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        List<Comment> comments = commentStorageService.getAllCommentsWithAnalyses();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific comment by ID",
            description = "Retrieves a single comment and its analyses by ID")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        return commentStorageService.getCommentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a comment",
            description = "Deletes a comment and all its associated analyses")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        if (commentStorageService.deleteComment(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/stats")
    @Operation(summary = "Get comment analysis statistics",
            description = "Retrieves statistics about comments and LLM analyses")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = commentStorageService.getCommentStats();
        return ResponseEntity.ok(stats);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        Map<String, String> error = Map.of(
                "error", e.getMessage(),
                "type", e.getClass().getSimpleName()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}