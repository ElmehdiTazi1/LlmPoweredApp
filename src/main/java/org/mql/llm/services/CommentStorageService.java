package org.mql.llm.services;

import org.mql.llm.models.Comment;
import org.mql.llm.models.CommentAnalysis;
import org.mql.llm.repositories.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentStorageService {
    private final CommentRepository commentRepository;
    private final CommentService commentService;

    public CommentStorageService(CommentRepository commentRepository, CommentService commentService) {
        this.commentRepository = commentRepository;
        this.commentService = commentService;
    }

    @Transactional
    public Comment saveCommentWithAnalysis(String commentContent) {
        Comment comment = new Comment();
        comment.setContent(commentContent);

        Map<String, Map<String, String>> analyses = commentService.analyzeCommentWithAllServices(commentContent);

        analyses.forEach((llmName, result) -> {
            CommentAnalysis analysis = new CommentAnalysis();
            analysis.setLlmName(llmName);

            if (result.containsKey("error")) {
                analysis.setErrorMessage(result.get("error"));
            } else {
                analysis.setSentiment(result.get("sentiment"));
                analysis.setBrandName(result.get("nom de la marque"));
                analysis.setProductName(result.get("nom du produit"));
            }

            comment.addAnalysis(analysis);
        });

        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<Comment> getAllCommentsWithAnalyses() {
        return commentRepository.findAllWithAnalyses();
    }

    @Transactional(readOnly = true)
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Transactional
    public boolean deleteComment(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getCommentStats() {
        List<Comment> comments = getAllCommentsWithAnalyses();

        long totalComments = comments.size();
        long totalAnalyses = comments.stream()
                .mapToLong(c -> c.getAnalyses().size())
                .sum();

        Map<String, Long> sentimentDistribution = comments.stream()
                .flatMap(c -> c.getAnalyses().stream())
                .filter(a -> a.getSentiment() != null)
                .collect(Collectors.groupingBy(
                        CommentAnalysis::getSentiment,
                        Collectors.counting()
                ));

        Map<String, Long> brandDistribution = comments.stream()
                .flatMap(c -> c.getAnalyses().stream())
                .filter(a -> a.getBrandName() != null)
                .collect(Collectors.groupingBy(
                        CommentAnalysis::getBrandName,
                        Collectors.counting()
                ));

        return Map.of(
                "totalComments", totalComments,
                "totalAnalyses", totalAnalyses,
                "sentimentDistribution", sentimentDistribution,
                "brandDistribution", brandDistribution
        );
    }
}