package com.hogan.app.member;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hogan.common.base.BaseEntity;


/**
 * memberEntity
 * @author hoagn
 * @date 2019-05-24 17:15:32
 */
@Entity
@Table(name = "m_member")
public class MMember extends BaseEntity {
		
    /**
	 * nick_name
	 */
	@Column(name = "nick_name")
	private String nickName;

	/**
	 * nick_name
	 */
	@Column(name = "user_name")
	private String userName;
	
    /**
	 * mobile_phone
	 */
	@Column(name = "mobile_phone")
	private String mobilePhone;
	
    /**
	 * sexual
	 */
	@Column(name = "sexual")
	private Integer sexual;
	
    /**
	 * level
	 */
	@Column(name = "level")
	private Integer level;
	
    /**
	 * integral
	 */
	@Column(name = "integral")
	private Integer integral;

	@Transient
	private String date;

	@Transient
	private String time;

	@Transient
	private String projectId;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	public Integer getSexual() {
		return sexual;
	}

	public void setSexual(Integer sexual) {
		this.sexual = sexual;
	}
	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	
}