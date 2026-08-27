package com.example.P07_CascadeOrphanRemoval.removedemo.repository;

import com.example.P07_CascadeOrphanRemoval.removedemo.entity.RemoveOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RemoveOrderRepository extends JpaRepository<RemoveOrder, Long> {
}
