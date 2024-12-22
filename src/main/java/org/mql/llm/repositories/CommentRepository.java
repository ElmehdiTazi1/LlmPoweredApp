package org.mql.llm.repositories;

import org.mql.llm.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.analyses ORDER BY c.createdAt DESC")
    List<Comment> findAllWithAnalyses();
}
