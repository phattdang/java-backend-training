package com.example.P07_CascadeOrphanRemoval.dbcascadedemo;

import com.example.P07_CascadeOrphanRemoval.dto.CreateOrderRequest;
import com.example.P07_CascadeOrphanRemoval.dto.DatabaseCascadeResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseCascadeDemoService {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public DatabaseCascadeResult directSqlDelete(CreateOrderRequest request) {
        Long parentId = jdbcTemplate.queryForObject(
                "insert into db_orders(order_code) values (?) returning id",
                Long.class, request.orderCode());
        request.items().forEach(item -> jdbcTemplate.update(
                "insert into db_order_items(product_name, order_id) values (?, ?)",
                item.productName(), parentId));
        Integer before = jdbcTemplate.queryForObject(
                "select count(*) from db_order_items where order_id = ?", Integer.class, parentId);
        log.info("=== DIRECT SQL DELETE: HIBERNATE CASCADE IS NOT INVOLVED ===");
        int deletedParents = jdbcTemplate.update("delete from db_orders where id = ?", parentId);
        Integer after = jdbcTemplate.queryForObject(
                "select count(*) from db_order_items where order_id = ?", Integer.class, parentId);
        return new DatabaseCascadeResult(parentId, before, deletedParents, after,
                "PostgreSQL FK ON DELETE CASCADE deleted the children; no JPA entity operation ran.");
    }
}
