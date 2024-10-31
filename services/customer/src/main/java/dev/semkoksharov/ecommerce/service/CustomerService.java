package dev.semkoksharov.ecommerce.service;

import dev.semkoksharov.ecommerce.dto.CustomerRequestDTO;
import dev.semkoksharov.ecommerce.dto.CustomerResponse;
import dev.semkoksharov.ecommerce.exception.CustomerNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import dev.semkoksharov.ecommerce.model.Customer;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import dev.semkoksharov.ecommerce.repository.CustomerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final ModelMapper modelMapper;


    public CustomerResponse createCustomer(CustomerRequestDTO request) {
        var customer = customerRepository.save(modelMapper.map(request, Customer.class));
        return modelMapper.map(customer, CustomerResponse.class);
    }

    public void updateCustomer(@Valid CustomerRequestDTO request) {
        var customer = customerRepository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(String.format("[Cannot update customer] :: No customer found with the provided ID: %s", request.id())));
        mergerCustomer(customer, request);
        customerRepository.save(customer);
    }

    private void mergerCustomer(Customer customer, CustomerRequestDTO request) {
        if (StringUtils.isNotBlank(request.name())) {
            customer.setName(request.name());
        }
        if (StringUtils.isNotBlank(request.surname())) {
            customer.setName(request.surname());
        }
        if (StringUtils.isNotBlank(request.email())) {
            customer.setName(request.email());
        }
        if (request.address() != null) {
            customer.setAddress(request.address());
        }
    }

    public List<CustomerResponse> findAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> modelMapper.map(customer, CustomerResponse.class))
                .toList();
    }

    public Boolean existsById(String customerId) {
        return customerRepository.existsById(customerId);
    }

    public CustomerResponse findById(String customerId) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("[Retrieving error] No customer found with the provided ID: %s", customerId)
                ));

        return modelMapper.map(customer, CustomerResponse.class);
    }

    public CustomerResponse deleteById(String customerId) {
        var customerToDelete = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Customer with ID '%s' is not exists", customerId)));

        customerRepository.deleteById(customerId);

        return  modelMapper.map(customerToDelete, CustomerResponse.class);
    }
}
