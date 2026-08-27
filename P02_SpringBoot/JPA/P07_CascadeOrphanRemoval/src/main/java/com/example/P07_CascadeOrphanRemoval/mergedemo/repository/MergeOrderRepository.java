package com.example.P07_CascadeOrphanRemoval.mergedemo.repository;

import com.example.P07_CascadeOrphanRemoval.mergedemo.entity.MergeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MergeOrderRepository extends JpaRepository<MergeOrder, Long> {
}
