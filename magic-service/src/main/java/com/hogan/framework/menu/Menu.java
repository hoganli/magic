package com.hogan.framework.menu;


import com.hogan.common.base.BaseEntity;
import com.hogan.framework.permission.Permission;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;


/**
 * ClassName:Menu
 * Description:Menu
 * User:hogan.li
 * Date:2018/07/18
 */
@Entity
@Table(name = "fw_menu")
public class Menu extends BaseEntity {
		
    /**
	 * parent_id
	 */
	@Column(name = "parent_id")
	private String parentId;
	
    /**
	 * menu_name
	 */
	@Column(name = "menu_name")
	private String menuName;
	
    /**
	 * menu_code
	 */
	@Column(name = "menu_code")
	private String menuCode;

	/**
	 * is_floder
	 */
	@Column(name = "is_folder")
	private Boolean isFolder;

	@Transient
	private List<Menu> subMenus;

	@Transient
	private List<Permission> permissions;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public Boolean getFolder() {
		return isFolder;
	}

	public void setFolder(Boolean folder) {
		isFolder = folder;
	}

	public List<Menu> getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(List<Menu> subMenus) {
		this.subMenus = subMenus;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
}