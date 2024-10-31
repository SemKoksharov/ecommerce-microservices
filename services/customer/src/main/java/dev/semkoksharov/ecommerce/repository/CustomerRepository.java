package dev.semkoksharov.ecommerce.repository;

import dev.semkoksharov.ecommerce.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
}
