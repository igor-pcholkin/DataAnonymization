
use test;
CREATE TABLE IF NOT EXISTS /*!32312 IF NOT EXISTS*/ USERS(id INTEGER, name VARCHAR(256), email VARCHAR(256), address VARCHAR(256), birthdate DATE,
    socialNumber CHAR(12), ccard CHAR(16), post_code VARCHAR(20))   ;

CREATE TABLE IF NOT EXISTS `COMPANIES`(`company_id` INTEGER, `name` VARCHAR(256), `address` VARCHAR(256));

CREATE TABLE IF NOT EXISTS `LOGS`(`id` INTEGER, `user_id` INTEGER, `ipAddress` VARCHAR(39));