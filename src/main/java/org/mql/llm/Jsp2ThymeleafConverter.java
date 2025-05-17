package org.mql.llm;

import org.mql.llm.config.ApiKeyProvider;
import org.mql.llm.services.GeminiService;
import org.mql.llm.services.JspToThymeleafService;

import java.util.Map;

/**
 * Façade principale pour la conversion de JSP vers Thymeleaf.
 * Cette classe est le point d'entrée principal pour utiliser la bibliothèque.
 */
public class Jsp2ThymeleafConverter {
    
    private final JspToThymeleafService jspToThymeleafService;
    
    /**
     * Constructeur avec une clé API personnalisée.
     * 
     * @param apiKey Clé API Gemini personnalisée
     */
    public Jsp2ThymeleafConverter(String apiKey) {
        ApiKeyProvider apiKeyProvider = new ApiKeyProvider(apiKey);
        GeminiService geminiService = new GeminiService(apiKeyProvider);
        this.jspToThymeleafService = new JspToThymeleafService(geminiService);
    }
    
    /**
     * Constructeur par défaut utilisant la clé API intégrée.
     */
    public Jsp2ThymeleafConverter() {
        ApiKeyProvider apiKeyProvider = new ApiKeyProvider();
        GeminiService geminiService = new GeminiService(apiKeyProvider);
        this.jspToThymeleafService = new JspToThymeleafService(geminiService);
    }
    
    /**
     * Constructeur avec personnalisation complète des services.
     * 
     * @param apiKeyProvider Fournisseur de clé API
     * @param model Modèle d'IA à utiliser
     * @param temperature Température pour la génération
     * @param maxTokens Nombre maximum de tokens
     */
    public Jsp2ThymeleafConverter(ApiKeyProvider apiKeyProvider, String model, double temperature, int maxTokens) {
        GeminiService geminiService = new GeminiService(apiKeyProvider, model, temperature, maxTokens);
        this.jspToThymeleafService = new JspToThymeleafService(geminiService);
    }
    
    /**
     * Convertit le code JSP en Thymeleaf.
     * 
     * @param jspCode Code JSP à convertir
     * @return Code Thymeleaf résultant
     */
    public String convert(String jspCode) {
        return jspToThymeleafService.convertJspToThymeleaf(jspCode);
    }
    
    /**
     * Convertit le code JSP en Thymeleaf et retourne le résultat détaillé.
     * 
     * @param jspCode Code JSP à convertir
     * @param includeDetails Si true, inclut des détails supplémentaires sur la conversion
     * @return Résultat détaillé de la conversion
     */
    public Map<String, Object> convertWithDetails(String jspCode, boolean includeDetails) {
        String thymeleafCode = jspToThymeleafService.convertJspToThymeleaf(jspCode);
        
        Map<String, Object> result = Map.of(
            "thymeleafCode", thymeleafCode,
            "success", !thymeleafCode.startsWith("Erreur:")
        );
        
        return result;
    }
}
