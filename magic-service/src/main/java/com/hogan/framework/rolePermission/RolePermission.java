package com.hogan.framework.rolePermission;

import com.hogan.common.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * ClassName:RolePermission
 * Description:RolePermission
 * User:hogan.li
 * Date:2018/07/18
 */
@Entity
@Table(name = "fw_role_permission")
public class RolePermission extends BaseEntity{

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "permission_id")
    private String permissionId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }
}
