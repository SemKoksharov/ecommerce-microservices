package dev.semkoksharov.ecommerce.product.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductPurchaseRequest(
        @NotNull(message = "Product is mandatory")
        @Positive
        Long productId,

        @NotNull(message = "Quantity is mandatory")
        @Positive
        Double quantity
) {
}
