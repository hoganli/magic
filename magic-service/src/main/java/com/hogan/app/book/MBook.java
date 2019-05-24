package com.hogan.app.book;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hogan.common.base.BaseEntity;


/**
 * 预约Entity
 * @author hogan
 * @date 2019-05-24 17:04:56
 */
@Entity
@Table(name = "m_book")
public class MBook extends BaseEntity {
		
    /**
	 * member_id
	 */
	@Column(name = "member_id")
	private String memberId;
	
    /**
	 * project_id
	 */
	@Column(name = "project_id")
	private String projectId;
	
    /**
	 * b_date
	 */
	@Column(name = "b_date")
	private String bDate;
	
    /**
	 * b_time
	 */
	@Column(name = "b_time")
	private String bTime;
	
	
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	public String getBDate() {
		return bDate;
	}

	public void setBDate(String bDate) {
		this.bDate = bDate;
	}
	
	public String getBTime() {
		return bTime;
	}

	public void setBTime(String bTime) {
		this.bTime = bTime;
	}
	
}