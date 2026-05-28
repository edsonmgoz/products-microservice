package dev.edsonmm.products.exception.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Respuesta de error de validación")
public record ValidationErrorResponse(

        @Schema(description = "Código HTTP", example = "400")
        int status,

        @Schema(description = "Mapa de campo → mensaje de error")
        Map<String, String> errors
) {}
