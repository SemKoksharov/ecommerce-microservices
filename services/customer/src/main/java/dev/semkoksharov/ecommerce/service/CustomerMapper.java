package dev.semkoksharov.ecommerce.service;

import dev.semkoksharov.ecommerce.dto.CustomerRequestDTO;
import dev.semkoksharov.ecommerce.model.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public Customer toCustomer(CustomerRequestDTO request) {
        if (request == null) {
            return null;
        }

        return Customer.builder()
                .id(request.id())
                .name(request.name())
                .surname(request.surname())
                .email(request.email())
                .address(request.address())
                .build();
    }
}
