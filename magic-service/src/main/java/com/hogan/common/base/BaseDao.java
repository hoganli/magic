package com.hogan.common.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * ClassName:BaseDao
 * Description:BaseDao接口（jpa接口+base自定义接口）
 * User:hogan.li
 * Date:2017/2/23-17:46
 */
@NoRepositoryBean
public interface BaseDao<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    //此处定义BaseDao自定义方法（jpa没有提供的，需要用于全局的方法）


    //删除相关方法（硬删除）

    public void deleteAllById(List<ID> ids) throws Exception;


    //删除相关方法（软删除——设置deleteSign为1）

    public void deleteSoftById(ID id) throws Exception;

    public void deleteSoftAllById(List<ID> ids) throws Exception;

    public void deleteSoftAll(List<T> list) throws Exception;

    public void deleteSoftAll() throws Exception;

}
