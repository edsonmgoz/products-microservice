package dev.edsonmm.products.exception.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de error genérica")
public record ErrorResponse(

        @Schema(description = "Código HTTP", example = "404")
        int status,

        @Schema(description = "Mensaje de error", example = "No se encontró el producto con id 1")
        String message,

        @Schema(description = "Ruta de la petición", example = "/api/products/1")
        String path
) {}
