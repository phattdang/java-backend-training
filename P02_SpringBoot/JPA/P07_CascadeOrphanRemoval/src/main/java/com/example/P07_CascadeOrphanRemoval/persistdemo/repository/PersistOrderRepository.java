package com.example.P07_CascadeOrphanRemoval.persistdemo.repository;

import com.example.P07_CascadeOrphanRemoval.persistdemo.entity.PersistOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersistOrderRepository extends JpaRepository<PersistOrder, Long> {
}
