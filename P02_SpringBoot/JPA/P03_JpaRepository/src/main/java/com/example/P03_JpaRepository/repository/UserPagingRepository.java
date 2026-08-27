package com.example.P03_JpaRepository.repository;

import com.example.P03_JpaRepository.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Learning-only repository showing the paging and sorting abstraction.
 * No paging or sorting is executed in Part 4.
 */
public interface UserPagingRepository extends PagingAndSortingRepository<User, Long> {
}
