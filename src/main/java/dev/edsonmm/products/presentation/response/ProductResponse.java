package dev.edsonmm.products.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Datos del producto devueltos por la API")
public record ProductResponse(

        @Schema(description = "Identificador único", example = "1")
        Long id,

        @Schema(description = "Nombre del producto", example = "Laptop gamer")
        String name,

        @Schema(description = "Descripción opcional", example = "Laptop con GPU dedicada")
        String description,

        @Schema(description = "Precio unitario", example = "1500.00")
        BigDecimal price,

        @Schema(description = "Unidades disponibles", example = "10")
        Integer stock,

        @Schema(description = "Si el producto está activo", example = "true")
        boolean active,

        @Schema(description = "Fecha de creación")
        LocalDateTime createdAt,

        @Schema(description = "Última actualización")
        LocalDateTime updatedAt
) {}
