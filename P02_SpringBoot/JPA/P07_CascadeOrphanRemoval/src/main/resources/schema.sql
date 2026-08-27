CREATE TABLE IF NOT EXISTS db_orders (
    id BIGSERIAL PRIMARY KEY,
    order_code VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS db_order_items (
    id BIGSERIAL PRIMARY KEY,
    product_name VARCHAR(150) NOT NULL,
    order_id BIGINT NOT NULL,
    CONSTRAINT fk_db_order_items_order
        FOREIGN KEY (order_id) REFERENCES db_orders(id) ON DELETE CASCADE
);
