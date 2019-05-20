package com.hogan.framework.user;


import com.hogan.common.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;


/**
 * ClassName:User
 * Description:User
 * User:dada
 * Date:2018/07/18
 */
@Entity
@Table(name = "fw_user")
public class User extends BaseEntity {

    /**
     * unit_id
     */
    @Column(name = "unit_id")
    private String unitId;

    /**
     * account
     */
    @Column(name = "account")
    private String account;

    /**
     * password
     */
    @Column(name = "password")
    private String password;

    /**
     * user_name
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * gender
     */
    @Column(name = "gender", columnDefinition = "tinyint")
    private Integer gender;

    /**
     * birthday
     */
    @Column(name = "birthday")
    private String birthday;

    /**
     * position
     */
    @Column(name = "position")
    private String position;

    /**
     * work_code
     */
    @Column(name = "work_code")
    private String workCode;

    /**
     * office_phone
     */
    @Column(name = "office_phone")
    private String officePhone;

    /**
     * mobile_phone
     */
    @Column(name = "mobile_phone")
    private String mobilePhone;

    /**
     * home_phone
     */
    @Column(name = "home_phone")
    private String homePhone;

    /**
     * email
     */
    @Column(name = "email")
    private String email;

    /**
     * other_contact
     */
    @Column(name = "other_contact")
    private String otherContact;

    /**
     * state
     */
    @Column(name = "state", columnDefinition = "tinyint")
    private Boolean state;

    /**
     * remark
     */
    @Column(name = "remark")
    private String remark;

    /**
     * user_type
     */
    @Column(name = "user_type")
    private String userType;

    /**
     * admin_sign
     */
    @Column(name = "admin_sign", columnDefinition = "tinyint")
    private Boolean adminSign = false;

    /**
     * password_update_date
     */
    @Column(name = "password_update_date")
    private String passwordUpdateDate;

    @Transient
    private List<String> menus;

    @Transient
    private List<String> permissions;

    @Transient
    private List<String> roleIds;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtherContact() {
        return otherContact;
    }

    public void setOtherContact(String otherContact) {
        this.otherContact = otherContact;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Boolean getAdminSign() {
        return adminSign;
    }

    public void setAdminSign(Boolean adminSign) {
        this.adminSign = adminSign;
    }

    public String getPasswordUpdateDate() {
        return passwordUpdateDate;
    }

    public void setPasswordUpdateDate(String passwordUpdateDate) {
        this.passwordUpdateDate = passwordUpdateDate;
    }

    public List<String> getMenus() {
        return menus;
    }

    public void setMenus(List<String> menus) {
        this.menus = menus;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}