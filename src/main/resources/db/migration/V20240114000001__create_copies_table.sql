CREATE TABLE `copies`
(
    `id`     INTEGER  NOT NULL,
    `isbn`   varchar(36)    NOT NULL,
    `author` varchar(64)    NOT NULL,
    `title`  varchar(128)   NOT NULL,
    `copies`  INTEGER NOT NULL,
    PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;