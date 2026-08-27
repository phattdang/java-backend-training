package com.example.P07_CascadeOrphanRemoval.nocascadedemo.repository;

import com.example.P07_CascadeOrphanRemoval.nocascadedemo.entity.NoCascadeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoCascadeOrderRepository extends JpaRepository<NoCascadeOrder, Long> {
}
