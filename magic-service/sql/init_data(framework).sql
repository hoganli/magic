-- ----------------------------
-- records of fw_user
-- ----------------------------
--                         (id,  unit_id, account,    password,                                                            user_name,                         office_phone                            state,           admin_sign,   password_update_date，  五个基本字段)
insert into fw_user values ('1', '',      'admin',    '51bb551c76a48a1587b4aa49de0b87fff02eaa77eaa5eec9bb8d7f4c3fca9074',  '管理员', null, null, null, null,  '0756-8660888', null, null, null, null, '1', null, null, '1',          '',                     0, 'system', CURRENT_TIMESTAMP(3), '', '');
insert into fw_user values ('2', '',      'auditor',  'eabc85303025e77582b4cbd296bbea7d837940d2ed045cfcefb02f346e7d96dd',  '审计员', null, null, null, null,  '0756-8660888', null, null, null, null, '1', null, null, '0',          '',                     0, 'system', CURRENT_TIMESTAMP(3), '', '');

-- ----------------------------
-- records of fw_menu
-- ----------------------------
--                         (id,            parent_id,menu_name,        menu_code,                is_folder,五个基本字段)
insert into fw_menu values ('001',         '',       '系统管理',       '',                       '1',      0, 'system', CURRENT_TIMESTAMP(3), '', '');
-- insert into fw_menu values ('001001',      '001',    '机构管理',       'unit',                   '0',      0, 'system', CURRENT_TIMESTAMP(3), '', '');
insert into fw_menu values ('001002',      '001',    '用户管理',       'user',                   '0',      0, 'system', CURRENT_TIMESTAMP(3), '', '');
insert into fw_menu values ('001003',      '001',    '角色管理',       'role',                   '0',      0, 'system', CURRENT_TIMESTAMP(3), '', '');
-- insert into fw_menu values ('001004',      '001',    '权限管理',       'permission',             '0',      0, 'system', CURRENT_TIMESTAMP(3), '', '');
insert into fw_menu values ('001005',      '001',    '日志管理',       'log',                    '0',      0, 'system', CURRENT_TIMESTAMP(3), '', '');

-- ----------------------------
-- records of fw_permission
-- ----------------------------
--                               (id,           menu_id,     permission_name,     permission_url,   permission_code,                        permission_desc,is_necessary,五个基本字段)
-- insert into fw_permission values (UUID_SHORT(), '001001',    '列表',              '',               'unit:list',                         '', '1', 0, 'system', CURRENT_TIMESTAMP(3), '', '');
                                                                                                                                            
insert into fw_permission values ('001002001', '001002',    '列表',              '',               'user:list',                            '', '1', 0, 'system', CURRENT_TIMESTAMP(3), '', '');
insert into fw_permission values ('001002002', '001002',    '同步',              '',               'user:sync',                            '', '0', 0, 'system', CURRENT_TIMESTAMP(3), '', '');
insert into fw_permission values ('001002003', '001002',    '编辑',              '',               'user:update',                          '', '0', 0, 'system', CURRENT_TIMESTAMP(3), '', '');
insert into fw_permission values ('001002004', '001002',    '重置',              '',               'user:reset',                           '', '0', 0, 'system', CURRENT_TIMESTAMP(3), '', '');
insert into fw_permission values ('001002005', '001002',    '删除',              '',               'user:delete',                          '', '0', 0, 'system', CURRENT_TIMESTAMP(3), '', '');
                                                                                                                                           
insert into fw_permission values ('001003001', '001003',    '列表',              '',               'role:list',                            '', '1', 0, 'system', CURRENT_TIMESTAMP(3), '', '');
insert into fw_permission values ('001003002', '001003',    '新增',              '',               'role:add',                             '', '0', 0, 'system', CURRENT_TIMESTAMP(3), '', '');
insert into fw_permission values ('001003003', '001003',    '编辑',              '',               'role:update',                          '', '0', 0, 'system', CURRENT_TIMESTAMP(3), '', '');
insert into fw_permission values ('001003004', '001003',    '删除',              '',               'role:delete',                          '', '0', 0, 'system', CURRENT_TIMESTAMP(3), '', '');
insert into fw_permission values ('001003005', '001003',    '授权',              '',               'rolePermission:update',                '', '0', 0, 'system', CURRENT_TIMESTAMP(3), '', '');
                                                                                                                                            
insert into fw_permission values ('001005001', '001005',    '列表',              '',               'log:list',                             '', '1', 0, 'system', CURRENT_TIMESTAMP(3), '', '');
insert into fw_permission values ('001005002', '001005',    '导出',              '',               'log:export',                           '', '0', 0, 'system', CURRENT_TIMESTAMP(3), '', '');

-- ----------------------------
-- records of fw_role
-- ----------------------------
--                         (id,    role_name,    act_group,  remark,         五个基本字段);
insert into fw_role values ('1',   '审计角色',   '',         '审计权限',     '0', 'system', CURRENT_TIMESTAMP(3), '', '');

-- ----------------------------
-- records of fw_role_permission
-- ----------------------------
--                                    (id,       role_id,    permission_id,  五个基本字段);
insert into fw_role_permission values ('1',      '1',        '001005001',    '0', 'system', CURRENT_TIMESTAMP(3), '', '');
insert into fw_role_permission values ('2',      '1',        '001005002',    '0', 'system', CURRENT_TIMESTAMP(3), '', '');

-- ----------------------------
-- records of fw_user_role
-- ----------------------------
--                              (id,   user_id,   role_id,   五个基本字段)
insert into fw_user_role values ('1',  '2',       '1',       0, 'system', CURRENT_TIMESTAMP(3), '', '');