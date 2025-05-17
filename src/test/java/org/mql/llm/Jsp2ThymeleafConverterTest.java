package org.mql.llm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mql.llm.config.ApiKeyProvider;
import org.mql.llm.services.GeminiService;
import org.mql.llm.services.JspToThymeleafService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class Jsp2ThymeleafConverterTest {

    @Mock
    private GeminiService geminiService;
    
    @Mock
    private JspToThymeleafService jspToThymeleafService;
    
    private Jsp2ThymeleafConverter converter;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Configuration du mock JspToThymeleafService
        when(jspToThymeleafService.convertJspToThymeleaf(anyString()))
            .thenReturn("<html xmlns:th=\"http://www.thymeleaf.org\"><body><p th:text=\"${message}\">Hello</p></body></html>");
        
        // Création de l'instance à tester
        converter = new Jsp2ThymeleafConverter("test-api-key");
    }
    
    @Test
    void shouldCreateInstanceWithDefaultApiKey() {
        // When
        Jsp2ThymeleafConverter defaultConverter = new Jsp2ThymeleafConverter();
        
        // Then
        assertNotNull(defaultConverter);
    }
    
    @Test
    void shouldCreateInstanceWithCustomApiKey() {
        // When
        Jsp2ThymeleafConverter customConverter = new Jsp2ThymeleafConverter("custom-api-key");
        
        // Then
        assertNotNull(customConverter);
    }
    
    @Test
    void shouldCreateInstanceWithAdvancedConfiguration() {
        // Given
        ApiKeyProvider apiKeyProvider = new ApiKeyProvider("advanced-api-key");
        
        // When
        Jsp2ThymeleafConverter advancedConverter = new Jsp2ThymeleafConverter(
            apiKeyProvider, "gemini-pro", 0.3, 16384);
        
        // Then
        assertNotNull(advancedConverter);
    }
}
