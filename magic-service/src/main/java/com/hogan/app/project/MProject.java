package com.hogan.app.project;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hogan.common.base.BaseEntity;

import java.math.BigDecimal;


/**
 * projectEntity
 * @author hogan
 * @date 2019-05-24 17:16:09
 */
@Entity
@Table(name = "m_project")
public class MProject extends BaseEntity {
		
    /**
	 * project_name
	 */
	@Column(name = "project_name")
	private String projectName;
	
    /**
	 * price
	 */
	@Column(name = "price")
	private BigDecimal price;
	
    /**
	 * dis_price
	 */
	@Column(name = "dis_price")
	private BigDecimal disPrice;
	
    /**
	 * used
	 */
	@Column(name = "used")
	private Integer used;
	
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public BigDecimal getDisPrice() {
		return disPrice;
	}

	public void setDisPrice(BigDecimal disPrice) {
		this.disPrice = disPrice;
	}
	
	public Integer getUsed() {
		return used;
	}

	public void setUsed(Integer used) {
		this.used = used;
	}
	
}