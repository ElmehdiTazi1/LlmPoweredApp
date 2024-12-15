package org.mql.llm.repositories;

import org.mql.llm.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
public interface CommentRepository extends JpaRepository<Comment, Long> {
}

