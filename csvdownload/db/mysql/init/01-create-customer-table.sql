CREATE TABLE IF NOT EXISTS customer (
    customer_id BIGINT NOT NULL AUTO_INCREMENT,
    customer_name VARCHAR(255) NOT NULL,
    branch_no INT NOT NULL,
    PRIMARY KEY (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET SESSION cte_max_recursion_depth = 10000000;

INSERT INTO customer (customer_name, branch_no)
WITH RECURSIVE seq AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM seq WHERE n < 5000000
)
SELECT CONCAT('customer_', LPAD(n, 5, '0')), 100
FROM seq;
