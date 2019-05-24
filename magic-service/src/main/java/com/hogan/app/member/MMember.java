package com.hogan.app.member;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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