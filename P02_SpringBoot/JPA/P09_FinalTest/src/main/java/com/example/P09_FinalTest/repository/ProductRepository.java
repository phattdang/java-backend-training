package com.example.P09_FinalTest.repository;

import com.example.P09_FinalTest.entity.Product;
import com.example.P09_FinalTest.entity.enums.ProductStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByProductCode(String productCode);

    Optional<Product> findByProductCode(String productCode);

    List<Product> findByStatus(ProductStatus status);

    List<Product> findByNameContainingIgnoreCase(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdForUpdate(@Param("id") Long id);

    @Query(
            value = "select * from products p where p.stock <= :threshold order by p.stock asc",
            nativeQuery = true
    )
    List<Product> findLowStockProductsNative(@Param("threshold") int threshold);
}
