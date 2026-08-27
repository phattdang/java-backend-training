package com.example.P07_CascadeOrphanRemoval.orphandemo.repository;

import com.example.P07_CascadeOrphanRemoval.orphandemo.entity.OrphanOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrphanOrderRepository extends JpaRepository<OrphanOrder, Long> {
}
