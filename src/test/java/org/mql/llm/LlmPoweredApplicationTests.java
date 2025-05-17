package org.mql.llm;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires simples pour la bibliothèque Jsp2ThymeleafConverter
 */
class LlmPoweredApplicationTests {

    @Test
    void shouldCreateConverterInstance() {
        // When
        Jsp2ThymeleafConverter converter = new Jsp2ThymeleafConverter();
        
        // Then
        assertNotNull(converter);
    }
    
    @Test
    void shouldHandleSimpleJspCode() {
        // Given
        Jsp2ThymeleafConverter converter = new Jsp2ThymeleafConverter();
        String simpleJsp = "<h1>Hello world</h1>";
        
        // When
        String result = converter.convert(simpleJsp);
        
        // Then
        assertNotNull(result);
        assertFalse(result.startsWith("Erreur:"));
    }
}
