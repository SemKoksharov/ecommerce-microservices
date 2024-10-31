package dev.semkoksharov.ecommerce.product.dto;

import java.math.BigDecimal;

public record ProductPurchaseResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {
}
