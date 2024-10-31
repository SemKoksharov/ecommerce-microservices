package dev.semkoksharov.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import dev.semkoksharov.ecommerce.model.Address;

public record CustomerRequestDTO(
        String id,
        @NotNull(message = "Customer name field is required")
        String name,
        @NotNull(message = "Customer surname field is required")
        String surname,
        @NotNull(message = "Customer email field is required")
        @Email(message = "Customer email is not a valid email address")
        String email,
        Address address) {
}
