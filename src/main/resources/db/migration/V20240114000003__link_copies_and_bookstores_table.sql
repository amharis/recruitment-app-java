ALTER TABLE `copies`
(
    ADD bookstore_code char(8) not null DEFAULT '';
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;