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

    public ConversionResult() {
        this.timestamp = LocalDateTime.now();
    }

    public ConversionResult(String jspCode, String thymeleafCode) {
        this();
        this.jspCode = jspCode;
        this.thymeleafCode = thymeleafCode;
        this.success = true;
    }

    public ConversionResult(String jspCode, String error, boolean success) {
        this();
        this.jspCode = jspCode;
        this.error = error;
        this.success = success;
    }

    public String getJspCode() {
        return jspCode;
    }

    public void setJspCode(String jspCode) {
        this.jspCode = jspCode;
    }

    public String getThymeleafCode() {
        return thymeleafCode;
    }

    public void setThymeleafCode(String thymeleafCode) {
        this.thymeleafCode = thymeleafCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
