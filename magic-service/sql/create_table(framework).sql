-- ----------------------------
-- table structure for fw_unit
-- ----------------------------
drop table if exists fw_unit;

create table fw_unit(
   id                   varchar(36) not null,
   name                 varchar(100) not null,
   principal            varchar(50),
   phone                varchar(50),
   fax                  varchar(50),
   email                varchar(100),
   postcode             varchar(6),
   address              varchar(200),
   website              varchar(100),
   intro                varchar(255),
   remark               varchar(255),
   parent_id            varchar(36),
   delete_sign          tinyint  default 0,      -- 删除标识
   create_by            varchar(36),             -- 创建者
   create_date          varchar(26),             -- 创建时间
   update_by            varchar(36),             -- 更新者
   update_date          varchar(26),             -- 更新时间
   primary key (id)
);

-- ----------------------------
-- table structure for fw_user
-- ----------------------------
drop table if exists fw_user;

create table fw_user(
   id                   varchar(36) not null,
   unit_id              varchar(36) not null,
   account              varchar(255) not null,
   password             varchar(200) null,
   user_name            varchar(100) not null,
   gender               tinyint,
   birthday             varchar(10),
   position             varchar(100),
   work_code            varchar(50),
   office_phone         varchar(50),
   mobile_phone         varchar(50),
   home_phone           varchar(50),
   email                varchar(50),
   other_contact        varchar(200),
   state                tinyint default 1,
   remark               varchar(500),
   user_type            varchar(1),
   admin_sign           tinyint default 0,
   password_update_date varchar(26),             -- 密码更新时间
   delete_sign          tinyint  default 0,      -- 删除标识
   create_by            varchar(36),             -- 创建者
   create_date          varchar(26),             -- 创建时间
   update_by            varchar(36),             -- 更新者
   update_date          varchar(26),             -- 更新时间
   primary key (id)
);

-- ----------------------------
-- table structure for fw_role
-- ----------------------------
drop table if exists fw_role;

create table fw_role(
   id                   varchar(36) not null,
   role_name            varchar(100) not null,
   act_group            varchar(100),
   remark               varchar(500),
   delete_sign          tinyint  default 0,      -- 删除标识
   create_by            varchar(36),             -- 创建者
   create_date          varchar(26),             -- 创建时间
   update_by            varchar(36),             -- 更新者
   update_date          varchar(26),             -- 更新时间
   primary key (id)
);

-- ----------------------------
-- table structure for fw_user_role
-- ----------------------------
drop table if exists fw_user_role;

create table fw_user_role(
   id                   varchar(36) not null,
   user_id              varchar(36) not null,
   role_id              varchar(36) not null,
   delete_sign          tinyint  default 0,      -- 删除标识
   create_by            varchar(36),             -- 创建者
   create_date          varchar(26),             -- 创建时间
   update_by            varchar(36),             -- 更新者
   update_date          varchar(26),             -- 更新时间
   primary key (id)
);

-- ------------------------------
-- table structure for fw_menu  菜单表
-- ------------------------------
drop table if exists fw_menu; 

create table fw_menu (
   id                  varchar(36) not null,
   parent_id           varchar(36) not null,    -- 上级id
   menu_name           varchar(100) not null,   -- 权限名称
   menu_code           varchar(100) not null,   -- 权限url：页面
   is_folder           tinyint  default 0,      -- 是否文件夹
   delete_sign         tinyint  default 0,      -- 删除标识
   create_by           varchar(36),             -- 创建者
   create_date         varchar(26),             -- 创建时间
   update_by           varchar(36),             -- 更新者
   update_date         varchar(26),             -- 更新时间
   primary key (id)
);

-- ------------------------------
-- table structure for fw_permission  权限表
-- ------------------------------
drop table if exists fw_permission; 

create table fw_permission (
   id                  varchar(36) not null,
   menu_id             varchar(36) not null,    -- 权限对应的菜单id
   permission_name     varchar(100) not null,   -- 权限名称
   permission_url      varchar(100) not null,   -- 权限url：页面
   permission_code     varchar(100) not null,   -- 权限代码
   permission_desc     varchar(200) null,
   is_necessary        tinyint  default 0,      -- 是否必选
   delete_sign         tinyint  default 0,      -- 删除标识
   create_by           varchar(36),             -- 创建者
   create_date         varchar(26),             -- 创建时间
   update_by           varchar(36),             -- 更新者
   update_date         varchar(26),             -- 更新时间
   primary key (id)
);

-- ----------------------------------------------------
-- table structure for fw_role_permission	角色权限表
-- ----------------------------------------------------
drop table if exists fw_role_permission;

create table fw_role_permission(
   id                   varchar(36) not null,
   role_id              varchar(36) not null,
   permission_id        varchar(36) not null,
   delete_sign          tinyint  default 0,      -- 删除标识
   create_by            varchar(36),             -- 创建者
   create_date          varchar(26),             -- 创建时间
   update_by            varchar(36),             -- 更新者
   update_date          varchar(26),             -- 更新时间
   primary key (id)
);

-- ----------------------------
-- table structure for fw_log
-- ----------------------------
drop table if exists fw_log;

create table fw_log(
   id                  varchar(36) not null,
   user_id             varchar(36),
   user_account        varchar(255),
   user_name           varchar(100),
   op_type             varchar(20),             -- 操作类型：登录、退出、新增、修改、删除
   op_method           varchar(100),
   op_args             varchar(2000),
   op_result           boolean,
   op_ip               varchar(255),
   op_host             varchar(255),
   delete_sign         tinyint  default 0,      -- 删除标识
   create_by           varchar(36),             -- 创建者
   create_date         varchar(26),             -- 创建时间
   update_by           varchar(36),             -- 更新者
   update_date         varchar(26),             -- 更新时间
   primary key (id)
);