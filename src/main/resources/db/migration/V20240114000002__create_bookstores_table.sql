CREATE TABLE `bookstores`
(
    `store_code`     char(8)    NOT NULL unique,
    `address`  varchar(64)      NOT NULL,
    `country_code` varchar(3)   NOT NULL,
    `city` varchar(10)          NOT NULL

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;