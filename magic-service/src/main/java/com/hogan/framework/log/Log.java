package com.hogan.framework.log;


import com.hogan.common.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * ClassName:Log
 * Description:Log
 * User:dada
 * Date:2018/07/18
 */
@Entity
@Table(name = "fw_log")
public class Log extends BaseEntity {
		
    /**
	 * user_id
	 */
	@Column(name = "user_id")
	private String userId;
	
    /**
	 * user_account
	 */
	@Column(name = "user_account")
	private String userAccount;
	
    /**
	 * user_name
	 */
	@Column(name = "user_name")
	private String userName;
	
    /**
	 * op_type
	 */
	@Column(name = "op_type")
	private String opType;
	
    /**
	 * op_method
	 */
	@Column(name = "op_method")
	private String opMethod;
	
    /**
	 * op_args
	 */
	@Column(name = "op_args")
	private String opArgs;
	
    /**
	 * op_result
	 */
	@Column(name = "op_result")
	private Boolean opResult;
	
    /**
	 * op_ip
	 */
	@Column(name = "op_ip")
	private String opIp;
	
    /**
	 * op_host
	 */
	@Column(name = "op_host")
	private String opHost;
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}
	
	public String getOpMethod() {
		return opMethod;
	}

	public void setOpMethod(String opMethod) {
		this.opMethod = opMethod;
	}
	
	public String getOpArgs() {
		return opArgs;
	}

	public void setOpArgs(String opArgs) {
		this.opArgs = opArgs;
	}

	public Boolean getOpResult() {
		return opResult;
	}

	public void setOpResult(Boolean opResult) {
		this.opResult = opResult;
	}

	public String getOpIp() {
		return opIp;
	}

	public void setOpIp(String opIp) {
		this.opIp = opIp;
	}
	
	public String getOpHost() {
		return opHost;
	}

	public void setOpHost(String opHost) {
		this.opHost = opHost;
	}
	
}