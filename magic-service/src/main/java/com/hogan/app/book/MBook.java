package com.hogan.app.book;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hogan.app.member.MMember;
import com.hogan.app.project.MProject;
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

	@Column(name = "confirm", columnDefinition = "tinyint")
	private Boolean confirm;

	@Column(name = "finish", columnDefinition = "tinyint")
	private Boolean finish;

	@Transient
	private MMember member;

	@Transient
	private MProject project;

	public Boolean getFinish() {
		return finish;
	}

	public void setFinish(Boolean finish) {
		this.finish = finish;
	}

	public MMember getMember() {
		return member;
	}

	public void setMember(MMember member) {
		this.member = member;
	}

	public MProject getProject() {
		return project;
	}

	public void setProject(MProject project) {
		this.project = project;
	}

	public String getbDate() {
		return bDate;
	}

	public void setbDate(String bDate) {
		this.bDate = bDate;
	}

	public String getbTime() {
		return bTime;
	}

	public void setbTime(String bTime) {
		this.bTime = bTime;
	}

	public Boolean getConfirm() {
		return confirm;
	}

	public void setConfirm(Boolean confirm) {
		this.confirm = confirm;
	}

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
	
}