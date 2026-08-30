CREATE TABLE IF NOT EXISTS customer (
    customer_id BIGINT NOT NULL AUTO_INCREMENT,
    customer_name VARCHAR(255) NOT NULL,
    branch_no INT NOT NULL,
    PRIMARY KEY (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO customer (customer_name, branch_no) VALUES
    ('customer_001', 1),
    ('customer_002', 2),
    ('customer_003', 1),
    ('customer_004', 3),
    ('customer_005', 2);
