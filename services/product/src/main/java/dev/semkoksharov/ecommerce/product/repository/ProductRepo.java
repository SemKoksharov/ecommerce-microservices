package dev.semkoksharov.ecommerce.product.repository;

import dev.semkoksharov.ecommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findAllByIdInOrderByIdAsc(List<Long> productIds);
}
