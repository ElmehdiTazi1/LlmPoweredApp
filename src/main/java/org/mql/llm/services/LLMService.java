package org.mql.llm.services;


import java.util.Map;

public interface LLMService {
    String getName();
    Map<String, String> analyzeComment(String commentContent);
}