package com.hogan.framework.permission;


import com.hogan.common.base.BaseEntity;
import com.hogan.framework.menu.Menu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * ClassName:Permission
 * Description:Permission
 * User:dada
 * Date:2018/07/18
 */
@Entity
@Table(name = "fw_permission")
public class Permission extends BaseEntity {
		
    /**
	 * menu_id
	 */
	@Column(name = "menu_id")
	private String menuId;
	
    /**
	 * permission_name
	 */
	@Column(name = "permission_name")
	private String permissionName;
	
    /**
	 * permission_url
	 */
	@Column(name = "permission_url")
	private String permissionUrl;
	
    /**
	 * permission_code
	 */
	@Column(name = "permission_code")
	private String permissionCode;
	
    /**
	 * permission_desc
	 */
	@Column(name = "permission_desc")
	private String permissionDesc;

	@Column(name = "is_necessary")
	private Boolean isNecessary;

	@Transient
	private Menu menu;

	public Boolean getIsNecessary() {
		return isNecessary;
	}

	public void setIsNecessary(Boolean isNecessary) {
		this.isNecessary = isNecessary;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	
	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	
	public String getPermissionUrl() {
		return permissionUrl;
	}

	public void setPermissionUrl(String permissionUrl) {
		this.permissionUrl = permissionUrl;
	}
	
	public String getPermissionCode() {
		return permissionCode;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}
	
	public String getPermissionDesc() {
		return permissionDesc;
	}

	public void setPermissionDesc(String permissionDesc) {
		this.permissionDesc = permissionDesc;
	}
	
}