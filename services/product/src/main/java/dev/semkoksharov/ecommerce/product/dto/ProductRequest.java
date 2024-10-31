package dev.semkoksharov.ecommerce.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(

        Long id,
        @NotNull(message = "Product name is required")
        @NotBlank(message = "Product name cannot be blank/empty")
        String name,
        @NotNull(message = "Product description is required")
        @NotBlank(message = "Product description cannot be blank/empty")
        String description,

        @Positive(message = "Available quantity should be a positive number")
        double availableQuantity,
        @Positive(message = "Price should be a positive number")
        BigDecimal price,

        @Positive(message = "Category ID should be a positive number")
        Long catId

) {
}
