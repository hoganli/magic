CREATE TABLE `m_project` (
  `id` varchar(36) NOT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  `price` decimal(6,2) DEFAULT NULL,
  `dis_price` decimal(6,2) DEFAULT NULL,
  `used` tinyint(4) DEFAULT '1',
  `delete_sign` tinyint(4) DEFAULT '0',
  `create_by` varchar(36) DEFAULT NULL,
  `create_date` varchar(26) DEFAULT NULL,
  `update_by` varchar(36) DEFAULT NULL,
  `update_date` varchar(26) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `m_member` (
  `id` varchar(36) NOT NULL,
  `nick_name` varchar(20) DEFAULT NULL,
  `mobile_phone` varchar(20) DEFAULT NULL,
  `sexual` tinyint(4) DEFAULT NULL,
  `level` int(2) DEFAULT '1',
  `integral` int(11) DEFAULT '0',
  `delete_sign` tinyint(4) DEFAULT '0',
  `create_by` varchar(36) DEFAULT NULL,
  `create_date` varchar(26) DEFAULT NULL,
  `update_by` varchar(36) DEFAULT NULL,
  `update_date` varchar(26) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `m_book` (
  `id` varchar(36) NOT NULL,
  `member_id` varchar(36) DEFAULT NULL,
  `project_id` varchar(36) DEFAULT NULL,
  `b_date` varchar(36) DEFAULT NULL,
  `b_time` varchar(36) DEFAULT NULL,
  `delete_sign` tinyint(4) DEFAULT '0',
  `create_by` varchar(36) DEFAULT NULL,
  `create_date` varchar(26) DEFAULT NULL,
  `update_by` varchar(36) DEFAULT NULL,
  `update_date` varchar(26) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;