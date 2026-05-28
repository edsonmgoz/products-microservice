package dev.edsonmm.products.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Datos para crear o actualizar un producto")
public class ProductRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre del producto", example = "Laptop gamer", minLength = 2, maxLength = 100)
    private String name;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    @Schema(description = "Descripción opcional", example = "Laptop con GPU dedicada", maxLength = 500)
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio debe ser mayor o igual a cero")
    @Schema(description = "Precio unitario", example = "1500.00", minimum = "0")
    private BigDecimal price;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock debe ser mayor o igual a cero")
    @Schema(description = "Unidades disponibles", example = "10", minimum = "0")
    private Integer stock;

    @Schema(description = "Si el producto está activo", example = "true", defaultValue = "true")
    private boolean active = true;
}
