package com.hogan.common.base;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * ClassName:BaseEntity
 * Description:BaseEntity
 * User:hogan.li
 * Date:2017/7/27 21:41
 */
@MappedSuperclass
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    protected String id;                //id

    @Basic
    @Column(name = "create_date")
    private String createDate;          //创建时间

    @Basic
    @Column(name = "create_by")
    private String createBy;            //创建者

    @Basic
    @Column(name = "update_date")
    private String updateDate;          //更新时间

    @Basic
    @Column(name = "update_by")
    private String updateBy;            //更新者

    @Basic
    @Column(name = "delete_sign", columnDefinition = "tinyint")
    private Boolean deleteSign = false; //删除标识

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Boolean getDeleteSign() {
        return deleteSign;
    }

    public void setDeleteSign(Boolean deleteSign) {
        this.deleteSign = deleteSign;
    }
}
