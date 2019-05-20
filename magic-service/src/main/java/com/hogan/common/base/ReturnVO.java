package com.hogan.common.base;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName:ReturnVO
 * Description:ReturnVO
 * User:hogan.li
 * Date:2017/8/9 15:06
 */
public class ReturnVO implements Serializable{

    private boolean success = true;     // 成功标识
    private String message;             // 错误信息
    private List dataList;              // 数据列表
    private Object data;                // 数据对象
    private Long totalProperty;         // 分页总数
    private Page page;                  // 分页对象

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getTotalProperty() {
        return totalProperty;
    }

    public void setTotalProperty(Long totalProperty) {
        this.totalProperty = totalProperty;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
