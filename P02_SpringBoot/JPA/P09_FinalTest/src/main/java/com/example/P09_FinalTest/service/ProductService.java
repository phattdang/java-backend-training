package com.example.P09_FinalTest.service;

import com.example.P09_FinalTest.dto.product.CreateProductRequest;
import com.example.P09_FinalTest.dto.product.ProductResponse;
import com.example.P09_FinalTest.dto.product.UpdateProductRequest;
import com.example.P09_FinalTest.entity.Product;
import com.example.P09_FinalTest.entity.enums.ProductStatus;
import com.example.P09_FinalTest.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        if (productRepository.existsByProductCode(request.productCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product code already exists");
        }

        Product product = new Product();
        product.setProductCode(request.productCode());
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setStatus(resolveProductStatus(request.status(), request.stock()));

        return toResponse(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        return toResponse(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::toResponse);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        if (request.name() != null) {
            product.setName(request.name());
        }
        if (request.description() != null) {
            product.setDescription(request.description());
        }
        if (request.price() != null) {
            product.setPrice(request.price());
        }
        if (request.stock() != null) {
            product.setStock(request.stock());
        }
        if (request.status() != null || request.stock() != null) {
            product.setStatus(resolveProductStatus(request.status(), product.getStock()));
        }

        return toResponse(product);
    }

    private ProductStatus resolveProductStatus(ProductStatus requestedStatus, Integer stock) {
        if (stock != null && stock == 0) {
            return ProductStatus.OUT_OF_STOCK;
        }
        if (requestedStatus != null) {
            return requestedStatus;
        }
        return ProductStatus.ACTIVE;
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getProductCode(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getStatus(),
                product.getVersion(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
