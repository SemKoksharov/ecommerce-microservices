package dev.semkoksharov.ecommerce.controller;

import dev.semkoksharov.ecommerce.dto.CustomerRequestDTO;
import dev.semkoksharov.ecommerce.dto.CustomerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dev.semkoksharov.ecommerce.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @RequestBody @Valid CustomerRequestDTO request
    ) {
        return ResponseEntity.ok(customerService.createCustomer(request));
    }

    @PutMapping
    public ResponseEntity<?> updateCustomer(
            @RequestBody @Valid CustomerRequestDTO request
    ) {
        customerService.updateCustomer(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAllCustomers() {
        System.out.println("[LOG] Request ro DB to find all customers");
        var allCustomers = customerService.findAllCustomers();
        System.out.printf("[LOG] Customer found: %d", allCustomers.size());

        return ResponseEntity.ok(allCustomers);
    }

    @GetMapping("/exists/{customer-id}")
    public ResponseEntity<Boolean> existsById(@PathVariable("customer-id") String customerId) {
        return ResponseEntity.ok(customerService.existsById(customerId));
    }

    @GetMapping("/{customer-id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable("customer-id") String customerId) {
        return ResponseEntity.ok(customerService.findById(customerId));
    }

    @DeleteMapping("/{customer-id}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable("customer-id") String customerId) {
        var customerResponse = customerService.deleteById(customerId);
        var deleteMessage = String.format("Customer was deleted%n Customer's data:%n%n%s", customerResponse);

        return ResponseEntity.status(HttpStatus.OK).body(deleteMessage);
    }
}
