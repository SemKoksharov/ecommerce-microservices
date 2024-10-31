package dev.semkoksharov.ecommerce.product.service;

import dev.semkoksharov.ecommerce.product.dto.ProductPurchaseRequest;
import dev.semkoksharov.ecommerce.product.dto.ProductPurchaseResponse;
import dev.semkoksharov.ecommerce.product.dto.ProductRequest;
import dev.semkoksharov.ecommerce.product.dto.ProductResponse;
import dev.semkoksharov.ecommerce.product.exception.ProductPurchaseException;
import dev.semkoksharov.ecommerce.product.model.Category;
import dev.semkoksharov.ecommerce.product.model.Product;
import dev.semkoksharov.ecommerce.product.repository.ProductRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private ModelMapper modelMapper;

    public Long createProduct(ProductRequest request) {
        log.info(":: Creating product ::");
        log.info(":: Product data:\n {} ::", request.toString());
        var mapped = mapAndSetCategory(request);
        var newSavedProduct = productRepo.saveAndFlush(mapped);
        Long id = newSavedProduct.getId();
        log.info(":: Product was saved successfully with ID: {} ::", id);
        return id;
    }

    private Product mapAndSetCategory(ProductRequest request) {
        var mapped = modelMapper.map(request, Product.class);
        mapped.setCategory(Category.builder()
                .id(request.id())
                .build());
        return mapped;
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        var productIds = request.stream()
                .map(ProductPurchaseRequest::productId)
                .toList();

        var storedProducts = productRepo.findAllByIdInOrderByIdAsc(productIds);

        if (productIds.size() != storedProducts.size()) {
            log.error(":::: [Purchase error] ::::");
            log.error(":::: [Will be threw an exception] ::::");

            var sb = new StringBuilder(
                    "[Purchase error] One or more products dose not exists\nAvailable products from the list:\n"
            );

            storedProducts.forEach(product -> sb.append(
                    String.format(
                            "Product name: %s%nAvailable quantity: %.2f%n",
                            product.getName(), product.getAvailableQuantity()
                    )));

            var exMessage = sb.toString();
            throw new ProductPurchaseException(exMessage);
        }

        var storesRequest = request.stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
        for (int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = storesRequest.get(i);
            var availableQuantity = product.getAvailableQuantity();
            var requestedQuantity = product.getAvailableQuantity();

            if (availableQuantity < requestedQuantity) {
                throw new ProductPurchaseException(
                        String.format("[Purchase error] Insufficient stock quantity for product with ID %d(%s)%nAvailable quantity: %.2f",
                                productRequest.productId(), product.getName(), availableQuantity)
                );
            }

            var newAvailableQuantity = availableQuantity - requestedQuantity;
            product.setAvailableQuantity(newAvailableQuantity);
            purchasedProducts.add(this.mapToProductPurchaseResponse(product, requestedQuantity));
        }

        return purchasedProducts;
    }


    public ProductResponse findProductById(Long productId) {
        var productFound = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("[Retrieving error] Product with ID %d is not found in the database", productId)
                ));
        mapToProductResponse(productFound);
        return mapToProductResponse(productFound);
    }


    public List<ProductResponse> findAllProducts() {
        return productRepo.findAll()
                .stream()
                .map(this::mapToProductResponse)
                .toList();
    }


    private ProductPurchaseResponse mapToProductPurchaseResponse(Product product, double quantity) {
        return new ProductPurchaseResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                quantity
        );
    }
    private ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAvailableQuantity(),
                product.getPrice(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCategory().getDescription());
    }

}
