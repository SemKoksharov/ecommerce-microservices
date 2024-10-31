package dev.semkoksharov.ecommerce.dto;

import dev.semkoksharov.ecommerce.model.Address;

public record CustomerResponse(String id,
                               String name,
                               String surname,
                               String email,
                               Address address) {

}
