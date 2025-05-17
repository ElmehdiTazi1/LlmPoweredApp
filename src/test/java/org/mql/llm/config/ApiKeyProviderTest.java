package org.mql.llm.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiKeyProviderTest {
    
    private static final String CUSTOM_API_KEY = "custom-api-key-for-test";
    private static final String ENV_VAR_NAME = "GEMINI_API_KEY";
    private static final String SYSTEM_PROPERTY_NAME = "gemini.api.key";
    
    private String originalEnvVar;
    private String originalSystemProperty;
    
    @BeforeEach
    void setUp() {
        // Sauvegarde des valeurs originales si elles existent
        originalEnvVar = System.getenv(ENV_VAR_NAME);
        originalSystemProperty = System.getProperty(SYSTEM_PROPERTY_NAME);
    }
    
    @AfterEach
    void tearDown() {
        // Restauration ou nettoyage
        System.clearProperty(SYSTEM_PROPERTY_NAME);
    }
    
    @Test
    void shouldReturnDefaultApiKeyWhenNoOtherKeyProvided() {
        // Given
        ApiKeyProvider provider = new ApiKeyProvider();
        
        // When
        String apiKey = provider.getApiKey();
        
        // Then
        assertNotNull(apiKey);
        assertFalse(apiKey.isEmpty());
    }
    
    @Test
    void shouldReturnConfiguredApiKeyWhenProvided() {
        // Given
        ApiKeyProvider provider = new ApiKeyProvider(CUSTOM_API_KEY);
        
        // When
        String apiKey = provider.getApiKey();
        
        // Then
        assertEquals(CUSTOM_API_KEY, apiKey);
    }
    
    @Test
    void shouldReturnSystemPropertyKeyWhenProvided() {
        // Given
        System.setProperty(SYSTEM_PROPERTY_NAME, "system-property-api-key");
        ApiKeyProvider provider = new ApiKeyProvider(CUSTOM_API_KEY);
        
        // When
        String apiKey = provider.getApiKey();
        
        // Then
        assertEquals("system-property-api-key", apiKey);
    }
    
    @Test
    void shouldAllowChangingConfiguredApiKey() {
        // Given
        ApiKeyProvider provider = new ApiKeyProvider("initial-api-key");
        
        // When
        provider.setConfiguredApiKey("updated-api-key");
        String apiKey = provider.getApiKey();
        
        // Then
        assertEquals("updated-api-key", apiKey);
    }
}
