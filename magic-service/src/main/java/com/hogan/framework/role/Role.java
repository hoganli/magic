package com.hogan.framework.role;



import com.hogan.common.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;


/**
 * ClassName:Role
 * Description:Role
 * User:hogan.li
 * Date:2018/07/18
 */
@Entity
@Table(name = "fw_role")
public class Role extends BaseEntity {
		
    /**
	 * role_name
	 */
	@Column(name = "role_name")
	private String roleName;
	
    /**
	 * act_group
	 */
	@Column(name = "act_group")
	private String actGroup;
	
    /**
	 * remark
	 */
	@Column(name = "remark")
	private String remark;

	@Transient
	private List<String> permissionIds;

	@Transient
	private Boolean canDelete;

	public Boolean getCanDelete() {
		return canDelete;
	}

	public void setCanDelete(Boolean canDelete) {
		this.canDelete = canDelete;
	}

	public List<String> getPermissionIds() {
		return permissionIds;
	}

	public void setPermissionIds(List<String> permissionIds) {
		this.permissionIds = permissionIds;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public String getActGroup() {
		return actGroup;
	}

	public void setActGroup(String actGroup) {
		this.actGroup = actGroup;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}