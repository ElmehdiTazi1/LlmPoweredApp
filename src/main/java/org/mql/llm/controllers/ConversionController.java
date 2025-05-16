package org.mql.llm.controllers;

import org.mql.llm.models.ConversionResult;
import org.mql.llm.services.JspToThymeleafService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.Map;

/**
 * Contrôleur REST exposant les services de conversion JSP vers Thymeleaf
 */
@RestController
@RequestMapping("/api/conversion")
@Tag(name = "JSP to Thymeleaf Conversion", description = "API endpoints for converting JSP to Thymeleaf")
@CrossOrigin(origins = "*")
public class ConversionController {
    private final JspToThymeleafService conversionService;

    public ConversionController(JspToThymeleafService conversionService) {
        this.conversionService = conversionService;
    }    /**
     * Convertit le code JSP en Thymeleaf et retourne le résultat au format JSON
     * 
     * @param request Requête contenant le code JSP à convertir
     * @return Code Thymeleaf généré au format JSON
     */
    @PostMapping("/jsp-to-thymeleaf")
    @Operation(
        summary = "Convert JSP to Thymeleaf (JSON response)", 
        description = "Converts JSP code to equivalent Thymeleaf code using AI and returns JSON"
    )
    @ApiResponse(responseCode = "200", description = "Conversion successful")
    @ApiResponse(responseCode = "400", description = "Bad request - JSP code is missing")
    public ResponseEntity<?> convertJspToThymeleaf(@RequestBody Map<String, String> request) {
        if (!request.containsKey("jspCode")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Le code JSP est requis"));
        }
        
        String jspCode = request.get("jspCode");
        
        try {
            String thymeleafCode = conversionService.convertJspToThymeleaf(jspCode);
            ConversionResult result = new ConversionResult(jspCode, thymeleafCode);
            
            return ResponseEntity.ok(Map.of(
                "thymeleafCode", thymeleafCode,
                "timestamp", result.getTimestamp().toString(),
                "success", Boolean.toString(result.isSuccess())
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Erreur lors de la conversion: " + e.getMessage(),
                "success", "false"
            ));
        }
    }
      /**
     * Convertit le code JSP en Thymeleaf et retourne directement le HTML
     * 
     * @param request Requête contenant le code JSP à convertir
     * @return Code Thymeleaf généré en HTML brut
     */    @PostMapping(value = "/jsp-to-thymeleaf/raw")
    @Operation(
        summary = "Convert JSP to Thymeleaf (Raw HTML Response)", 
        description = "Converts JSP code to equivalent Thymeleaf code using AI and returns raw HTML content based on Accept header"
    )
    @ApiResponse(responseCode = "200", description = "Conversion successful")
    @ApiResponse(responseCode = "400", description = "Bad request - JSP code is missing")
    public ResponseEntity<?> convertJspToThymeleafRaw(
            @RequestBody Map<String, String> request,
            @RequestHeader(value = "Accept", defaultValue = MediaType.TEXT_HTML_VALUE) String acceptHeader) {
        
        if (!request.containsKey("jspCode")) {
            if (acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE)) {
                return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "Le code JSP est requis", "success", "false"));
            } else {
                return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Erreur: Le code JSP est requis");
            }
        }
        
        String jspCode = request.get("jspCode");
        
        try {
            String thymeleafCode = conversionService.convertJspToThymeleaf(jspCode);
            ConversionResult result = new ConversionResult(jspCode, thymeleafCode);
            
            // Return response based on the Accept header
            if (acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE)) {
                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                        "thymeleafCode", thymeleafCode,
                        "timestamp", result.getTimestamp().toString(),
                        "success", Boolean.toString(result.isSuccess()),
                        "htmlContent", thymeleafCode
                    ));
            } else {
                // Return raw HTML content
                return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(thymeleafCode);
            }
        } catch (Exception e) {
            if (acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE)) {
                return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                        "error", "Erreur lors de la conversion: " + e.getMessage(),
                        "success", "false"
                    ));
            } else {
                return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Erreur lors de la conversion: " + e.getMessage());
            }
        }
    }
}
