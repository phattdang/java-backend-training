package com.example.P09_FinalTest.controller;

import com.example.P09_FinalTest.dto.product.CreateProductRequest;
import com.example.P09_FinalTest.dto.product.ProductResponse;
import com.example.P09_FinalTest.dto.product.UpdateProductRequest;
import com.example.P09_FinalTest.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest request) {
        return productService.createProduct(request);
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @GetMapping
    public Page<ProductResponse> getProducts(Pageable pageable) {
        return productService.getProducts(pageable);
    }

    @PatchMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
        return productService.updateProduct(id, request);
    }
}
