package com.hogan.framework.unit;


import com.hogan.common.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * ClassName:Unit
 * Description:Unit
 * User:dada
 * Date:2018/07/18
 */
@Entity
@Table(name = "fw_unit")
public class Unit extends BaseEntity {
		
    /**
	 * name
	 */
	@Column(name = "name")
	private String name;
	
    /**
	 * principal
	 */
	@Column(name = "principal")
	private String principal;
	
    /**
	 * phone
	 */
	@Column(name = "phone")
	private String phone;
	
    /**
	 * fax
	 */
	@Column(name = "fax")
	private String fax;
	
    /**
	 * email
	 */
	@Column(name = "email")
	private String email;
	
    /**
	 * postcode
	 */
	@Column(name = "postcode")
	private String postcode;
	
    /**
	 * address
	 */
	@Column(name = "address")
	private String address;
	
    /**
	 * website
	 */
	@Column(name = "website")
	private String website;
	
    /**
	 * intro
	 */
	@Column(name = "intro")
	private String intro;
	
    /**
	 * remark
	 */
	@Column(name = "remark")
	private String remark;
	
    /**
	 * parent_id
	 */
	@Column(name = "parent_id")
	private String parentId;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
	
	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
}