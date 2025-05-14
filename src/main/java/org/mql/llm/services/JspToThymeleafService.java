package org.mql.llm.services;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service spécialisé pour la conversion de code JSP vers Thymeleaf
 * utilisant les modèles LLM pour transformer intelligemment le code.
 */
@Service
public class JspToThymeleafService {
    
    private final GeminiService geminiService;
    private final String promptTemplate;
    
    public JspToThymeleafService(GeminiService geminiService) {
        this.geminiService = geminiService;
        // Escape les expressions Thymeleaf pour éviter que Spring ne les interprète comme des placeholders
        this.promptTemplate = "Tu es un expert en développement Java et en frameworks web, spécialisé dans la migration de JSP vers Thymeleaf. "
            + "Ta mission est de convertir le code JSP que je vais te soumettre en code Thymeleaf équivalent selon les spécifications suivantes:"
            + "\n\n# Règles de conversion JSP vers Thymeleaf:"
            + "\n\n## Éléments à convertir systématiquement:"
            + "\n- Remplacer `<%= expression %>` par `th:text=\"$" + "{expression}\"`"
            + "\n- Convertir `$" + "{pageContext.request.contextPath}` en `@{/}`"
            + "\n- Transformer `<c:if test=\"condition\">` en `th:if=\"$" + "{condition}\"`"
            + "\n- Transformer `<c:forEach var=\"item\" items=\"collection\">` en `th:each=\"item : $" + "{collection}\"`"
            + "\n- Remplacer `<c:out value=\"expression\"/>` par `th:text=\"$" + "{expression}\"`"
            + "\n- Convertir `<c:choose>`, `<c:when>`, `<c:otherwise>` en utilisant `th:if`, `th:unless`, `th:switch`, et `th:case`"
            + "\n- Transformer les inclusions `<jsp:include page=\"...\"/>` en `th:insert` ou `th:replace`"
            + "\n- Remplacer les EL expressions comme `$" + "{variable}` en maintenant la même syntaxe `$" + "{variable}` (Thymeleaf utilise la même syntaxe)"
            + "\n\n## Éléments impossibles à convertir automatiquement:"
            + "\nPour chaque élément impossible à convertir automatiquement, encadre-le avec des commentaires HTML dans le format suivant:"
            + "\n<!--"
            + "\n❌ **[Type d'élément]** → nécessite réécriture manuelle❌"
            + "\n[Code JSP original]"
            + "\n-->"
            + "\n\nExemples d'éléments impossibles à convertir:"
            + "\n- Scriptlets JSP (`<% code %>`)"
            + "\n- Déclarations JSP (`<%! code %>`)"
            + "\n- Directives JSP complexes (`<%@ directive %>`)"
            + "\n- Code Java intégré dans des scriptlets"
            + "\n- Expressions EL complexes qui dépendent de fonctions JSP personnalisées"
            + "\n- Tags personnalisés spécifiques à votre application"
            + "\n\n## Format de sortie:"
            + "\n1. Commence par une déclaration HTML5 et inclus les attributs Thymeleaf nécessaires dans la balise HTML: `<html xmlns:th=\"http://www.thymeleaf.org\">`"
            + "\n2. Ajoute les namespaces supplémentaires si nécessaire (layout, etc.)"
            + "\n3. Assure-toi que toutes les balises sont correctement fermées (Thymeleaf est plus strict que JSP)"
            + "\n4. Fournir le code converti avec une indentation propre et cohérente"
            + "\n5. Ajouter en commentaire les explications pour les transformations complexes"
            + "\n\n## Note importante:"
            + "\nSi tu rencontres des constructions JSP complexes (comme des taglibs spécifiques) que tu ne peux pas convertir avec certitude, signale-les clairement dans les commentaires selon le format spécifié."
            + "\n\n## INSTRUCTIONS DE FORMATAGE IMPORTANTES:"
            + "\nRetourne uniquement le code HTML/Thymeleaf converti sans aucun autre commentaire, texte d'explication ou balise de formatage Markdown (comme ```html ou ```)."
            + "\n\nTransforme maintenant le code JSP suivant en Thymeleaf équivalent:";
    }
    
    /**
     * Convertit le code JSP en Thymeleaf équivalent.
     * 
     * @param jspCode Code JSP à convertir
     * @return Code Thymeleaf résultant
     */
    public String convertJspToThymeleaf(String jspCode) {
        String prompt = buildPrompt(jspCode);
        String conversion = processConversion(prompt);
        return cleanConvertedHtml(conversion);
    }
    
    /**
     * Construit le prompt complet avec les instructions et le code JSP.
     * 
     * @param jspCode Code JSP à inclure dans le prompt
     * @return Prompt complet
     */
    private String buildPrompt(String jspCode) {
        return promptTemplate + "\n\n" + jspCode;
    }
    
    /**
     * Traite la demande de conversion via le service LLM.
     * 
     * @param prompt Prompt complet pour le LLM
     * @return Résultat de la conversion
     */
    private String processConversion(String prompt) {
        try {
            Map<String, String> response = geminiService.processJspConversion(prompt);
            if (response.containsKey("error")) {
                return "Erreur: " + response.get("error");
            }
            return response.getOrDefault("conversion", "Erreur: Pas de résultat de conversion");
        } catch (Exception e) {
            return "Erreur lors de la conversion: " + e.getMessage();
        }
    }
    
    /**
     * Nettoie le code HTML/Thymeleaf des balises de formatage Markdown et autres artefacts.
     * 
     * @param html Code HTML/Thymeleaf potentiellement avec des balises de formatage
     * @return Code HTML/Thymeleaf nettoyé
     */
    private String cleanConvertedHtml(String html) {
        if (html == null || html.isEmpty()) {
            return html;
        }
        
        // Supprimer les blocs de code Markdown (```html et ```)
        String cleaned = html.replaceAll("```html", "").replaceAll("```", "");
        
        // Extraire le HTML du bloc de code si présent
        Pattern pattern = Pattern.compile("(?s).*?(<html.*?>.*</html>).*?", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(cleaned);
        if (matcher.matches()) {
            cleaned = matcher.group(1);
        }
        
        // Enlever les espaces/sauts de ligne au début et à la fin
        cleaned = cleaned.trim();
        
        return cleaned;
    }
}
