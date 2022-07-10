create database if not exists short_link;

CREATE TABLE if not exists link_info (
  id bigint(20) DEFAULT NULL,
  short_link varchar(100) NOT NULL,
  origin_link varchar(256) NOT NULL,
  status tinyint(4) NOT NULL DEFAULT '1' COMMENT 'status: 0-disable,1-enable',
  expire_time datetime DEFAULT NULL COMMENT 'null means never expire',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;