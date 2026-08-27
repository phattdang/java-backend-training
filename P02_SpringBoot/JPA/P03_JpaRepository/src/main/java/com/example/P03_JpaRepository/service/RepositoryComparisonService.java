package com.example.P03_JpaRepository.service;

import com.example.P03_JpaRepository.dto.RepositoryOperationResult;
import com.example.P03_JpaRepository.repository.UserCrudRepository;
import com.example.P03_JpaRepository.repository.UserPagingRepository;
import com.example.P03_JpaRepository.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class RepositoryComparisonService {

    private static final Logger log = LoggerFactory.getLogger(RepositoryComparisonService.class);

    private final UserCrudRepository crudRepository;
    private final UserPagingRepository pagingRepository;
    private final UserRepository jpaRepository;

    public RepositoryComparisonService(
            UserCrudRepository crudRepository,
            UserPagingRepository pagingRepository,
            UserRepository jpaRepository) {
        this.crudRepository = crudRepository;
        this.pagingRepository = pagingRepository;
        this.jpaRepository = jpaRepository;
    }

    @PostConstruct
    void logRuntimeImplementations() {
        log.info("UserCrudRepository runtime class: {}", crudRepository.getClass().getName());
        log.info("UserPagingRepository runtime class: {}", pagingRepository.getClass().getName());
        log.info("UserRepository runtime class: {}", jpaRepository.getClass().getName());
    }

    public RepositoryOperationResult compareRepositories() {
        Map<String, Object> comparison = new LinkedHashMap<>();
        comparison.put("CrudRepository", Map.of(
                "purpose", "Basic CRUD operations",
                "runtimeClass", crudRepository.getClass().getName()
        ));
        comparison.put("PagingAndSortingRepository", Map.of(
                "purpose", "Paging and sorting abstraction (not executed in Part 4)",
                "runtimeClass", pagingRepository.getClass().getName()
        ));
        comparison.put("JpaRepository", Map.of(
                "purpose", "Broader JPA repository API, including flush() and saveAndFlush()",
                "runtimeClass", jpaRepository.getClass().getName()
        ));

        return new RepositoryOperationResult(
                "Repository abstraction and runtime implementation",
                "Spring Data scans each interface and registers a runtime proxy bean. JpaRepository is usually enough in normal JPA projects.",
                comparison
        );
    }
}
