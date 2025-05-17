package org.mql.llm.models;

import java.time.LocalDateTime;

/**
 * Modèle représentant une conversion JSP vers Thymeleaf
 */
public class ConversionResult {
    private String jspCode;
    private String thymeleafCode;
    private LocalDateTime timestamp;
    private boolean success;
    private String error;

    /**
     * Constructeur par défaut.
     * Initialise un nouveau résultat de conversion avec la date actuelle.
     */
    public ConversionResult() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructeur pour une conversion réussie.
     * 
     * @param jspCode Code JSP d'origine
     * @param thymeleafCode Code Thymeleaf généré
     */
    public ConversionResult(String jspCode, String thymeleafCode) {
        this();
        this.jspCode = jspCode;
        this.thymeleafCode = thymeleafCode;
        this.success = true;
    }

    /**
     * Constructeur pour une conversion avec erreur potentielle.
     * 
     * @param jspCode Code JSP d'origine
     * @param error Message d'erreur éventuel
     * @param success Statut de la conversion
     */
    public ConversionResult(String jspCode, String error, boolean success) {
        this();
        this.jspCode = jspCode;
        this.error = error;
        this.success = success;
    }    /**
     * Récupère le code JSP d'origine.
     * 
     * @return Le code JSP soumis pour conversion
     */
    public String getJspCode() {
        return jspCode;
    }

    /**
     * Définit le code JSP d'origine.
     * 
     * @param jspCode Le code JSP à définir
     */
    public void setJspCode(String jspCode) {
        this.jspCode = jspCode;
    }

    /**
     * Récupère le code Thymeleaf généré.
     * 
     * @return Le code Thymeleaf résultant de la conversion
     */
    public String getThymeleafCode() {
        return thymeleafCode;
    }

    /**
     * Définit le code Thymeleaf généré.
     * 
     * @param thymeleafCode Le code Thymeleaf à définir
     */
    public void setThymeleafCode(String thymeleafCode) {
        this.thymeleafCode = thymeleafCode;
    }    /**
     * Récupère l'horodatage de la conversion.
     * 
     * @return La date et l'heure de la conversion
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Définit l'horodatage de la conversion.
     * 
     * @param timestamp La date et l'heure à définir
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Indique si la conversion a réussi.
     * 
     * @return true si la conversion a réussi, false sinon
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Définit le statut de réussite de la conversion.
     * 
     * @param success Le statut à définir
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Récupère le message d'erreur éventuel.
     * 
     * @return Le message d'erreur ou null si pas d'erreur
     */
    public String getError() {
        return error;
    }

    /**
     * Définit le message d'erreur.
     * 
     * @param error Le message d'erreur à définir
     */
    public void setError(String error) {
        this.error = error;
    }
}
