package com.example.P07_CascadeOrphanRemoval.alldemo.repository;

import com.example.P07_CascadeOrphanRemoval.alldemo.entity.AllOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllOrderRepository extends JpaRepository<AllOrder, Long> {
}
