CREATE TABLE `copies`
(
    `id`     varchar(36)    NOT NULL,
    `author` varchar(64)    NOT NULL,
    `title`  varchar(128)   NOT NULL,
    `copies`  INTEGER NOT NULL,
    FOREIGN KEY (`id`) REFERENCES BOOKS(`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;