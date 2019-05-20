package com.hogan.framework.userRole;

import com.hogan.common.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * ClassName:UserRole
 * Description:UserRole
 * User:dada
 * Date:2018/07/18
 */
@Entity
@Table(name = "fw_user_role")
public class UserRole extends BaseEntity{

    @Column(name = "user_id")
    private String userId;

    @Column(name = "role_id")
    private String roleId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
