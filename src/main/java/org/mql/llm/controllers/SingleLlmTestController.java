package org.mql.llm.controllers;

import org.mql.llm.services.GeminiService;
import org.mql.llm.services.LlamaService;
import org.mql.llm.services.MistralService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/test/single")
@CrossOrigin(origins = "*")
public class SingleLlmTestController {
    private final GeminiService geminiService;
    private final MistralService mistralService;
    private final LlamaService llamaService;

    public SingleLlmTestController(GeminiService geminiService, MistralService mistralService, LlamaService llamaService) {
        this.geminiService = geminiService;
        this.mistralService = mistralService;
        this.llamaService = llamaService;
    }

    @PostMapping("/gemini")
    public ResponseEntity<?> analyzeWithGemini(@RequestBody Map<String, String> request) {
        if (!request.containsKey("comment")) {
            return ResponseEntity.badRequest().build();
        }
        
        String comment = request.get("comment");
        try {
            Map<String, String> result = geminiService.analyzeComment(comment);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/mistral")
    public ResponseEntity<?> analyzeWithMistral(@RequestBody Map<String, String> request) {
        if (!request.containsKey("comment")) {
            return ResponseEntity.badRequest().build();
        }
        
        String comment = request.get("comment");
        try {
            Map<String, String> result = mistralService.analyzeComment(comment);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/llama")
    public ResponseEntity<?> analyzeWithLlama(@RequestBody Map<String, String> request) {
        if (!request.containsKey("comment")) {
            return ResponseEntity.badRequest().build();
        }
        
        String comment = request.get("comment");
        try {
            Map<String, String> result = llamaService.analyzeComment(comment);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("error", e.getMessage()));
        }
    }
}
