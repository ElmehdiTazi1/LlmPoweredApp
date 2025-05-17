package org.mql.llm.examples;

import org.mql.llm.Jsp2ThymeleafConverter;
import org.mql.llm.config.ApiKeyProvider;

/**
 * Exemple d'utilisation de la bibliothèque Jsp2ThymeleafConverter.
 * Cette classe montre les différentes façons d'initialiser et d'utiliser le convertisseur.
 */
public class ConversionExample {

    private static final String JSP_EXAMPLE = "<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>\n" +
                                           "<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\" %>\n" +
                                           "<!DOCTYPE html>\n" +
                                           "<html>\n" +
                                           "<head>\n" +
                                           "    <title>Hello JSP</title>\n" +
                                           "</head>\n" +
                                           "<body>\n" +
                                           "    <h1>Hello, ${user.name}!</h1>\n" +
                                           "    <c:if test=\"${user.admin}\">\n" +
                                           "        <p>You are an admin.</p>\n" +
                                           "    </c:if>\n" +
                                           "    <ul>\n" +
                                           "        <c:forEach var=\"item\" items=\"${items}\">\n" +
                                           "            <li>${item.name} - ${item.price}</li>\n" +
                                           "        </c:forEach>\n" +
                                           "    </ul>\n" +
                                           "</body>\n" +
                                           "</html>";

    public static void main(String[] args) {
        // Exemple 1: Utilisation avec la clé API par défaut
        usingDefaultApiKey();
        
        // Exemple 2: Utilisation avec une clé API personnalisée
        usingCustomApiKey("YOUR_CUSTOM_API_KEY_HERE");
        
        // Exemple 3: Utilisation avec configuration avancée
        usingAdvancedConfiguration();
    }
    
    /**
     * Exemple d'utilisation du convertisseur avec la clé API par défaut
     */
    private static void usingDefaultApiKey() {
        System.out.println("\n=== Exemple 1: Utilisation avec la clé API par défaut ===\n");
        
        // Création du convertisseur avec la clé API par défaut
        Jsp2ThymeleafConverter converter = new Jsp2ThymeleafConverter();
        
        // Conversion
        String thymeleafCode = converter.convert(JSP_EXAMPLE);
        
        System.out.println("Résultat de la conversion:");
        System.out.println(thymeleafCode);
    }
    
    /**
     * Exemple d'utilisation du convertisseur avec une clé API personnalisée
     */
    private static void usingCustomApiKey(String apiKey) {
        System.out.println("\n=== Exemple 2: Utilisation avec une clé API personnalisée ===\n");
        
        // Création du convertisseur avec une clé API personnalisée
        Jsp2ThymeleafConverter converter = new Jsp2ThymeleafConverter(apiKey);
        
        // Conversion
        String thymeleafCode = converter.convert(JSP_EXAMPLE);
        
        System.out.println("Résultat de la conversion:");
        System.out.println(thymeleafCode);
    }
    
    /**
     * Exemple d'utilisation du convertisseur avec configuration avancée
     */
    private static void usingAdvancedConfiguration() {
        System.out.println("\n=== Exemple 3: Utilisation avec configuration avancée ===\n");
        
        // Création d'un fournisseur de clé API
        ApiKeyProvider apiKeyProvider = new ApiKeyProvider(); // Utilise la clé par défaut
        
        // Configuration personnalisée
        Jsp2ThymeleafConverter converter = new Jsp2ThymeleafConverter(
            apiKeyProvider,    // Fournisseur de clé API 
            "gemini-1.5-pro",  // Modèle différent
            0.3,               // Température plus élevée
            16384              // Plus de tokens max
        );
        
        // Conversion
        String thymeleafCode = converter.convert(JSP_EXAMPLE);
        
        System.out.println("Résultat de la conversion avec configuration avancée:");
        System.out.println(thymeleafCode);
    }
}
