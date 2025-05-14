package org.mql.llm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.mql.llm.repositories.CommentRepository;
import org.mockito.Mockito;

@Configuration
public class TestConfig {
    
    @Bean
    @Primary
    public CommentRepository mockCommentRepository() {
        return Mockito.mock(CommentRepository.class);
    }
}
