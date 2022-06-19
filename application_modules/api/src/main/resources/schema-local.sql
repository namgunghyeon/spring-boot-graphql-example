CREATE TABLE IF NOT EXISTS account
(
    `id`                BIGINT NOT NULL auto_increment,
    `name`              VARCHAR (100) NOT NULL comment '이름',
    `email`             VARCHAR (100) NOT NULL comment '이메일',
    `password`          VARCHAR (255) NOT NULL comment '패스워드',
    `created_at`        TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY ( id )
);

ALTER TABLE account ADD CONSTRAINT account_email_UNIQUE UNIQUE(email);