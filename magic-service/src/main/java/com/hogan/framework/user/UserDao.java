package com.hogan.framework.user;


import com.hogan.common.base.BaseDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * ClassName:UserDao
 * Description:UserDao
 * User:dada
 * Date:2018/07/18
 */
public interface UserDao extends BaseDao<User, String>, UserDaoCustom {
	
	//此处定义模块jpa规范扩展接口(无需实现)

    public User findByAccount(String account) throws Exception;

    public List<User> findByAccountAndIdNot(String account, String id) throws Exception;

    public User findByAccountAndPassword(String account, String password) throws Exception;

    public List<User> findByDeleteSignAndAdminSign(Boolean deleteSign, Boolean adminSign) throws Exception;

    public void deleteByUnitId(String unitId) throws Exception;

    public User findByIdAndDeleteSign(String id, Boolean deleteSign) throws Exception;

    @Modifying
    @Query("update User u set u.password = ?1 where u.account = ?2")
    public int setPasswordFor(String password, String account) throws Exception;

    List<User> findByAccountNotIn(List<String> accounts);
}