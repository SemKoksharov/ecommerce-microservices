package dev.semkoksharov.ecommerce.product.dto;

import java.math.BigDecimal;

public record ProductResponse(

        Long id,
        String name,
        String description,
        double availableQuantity,
        BigDecimal price,
        Long catId,
        String catName,
        String catDescription
) {
}
