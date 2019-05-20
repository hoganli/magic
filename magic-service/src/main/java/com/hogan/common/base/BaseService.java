package com.hogan.common.base;

import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * ClassName:BaseService
 * Description:BaseService接口（定义公共的service方法）
 * User:hogan.li
 * Date:2017/2/23-17:46
 */
public interface BaseService<T, ID> {

    //新增相关方法

    public T save(T t) throws Exception;

    public <S extends T> List<S> saveAll(List<S> list) throws Exception;


    //删除相关方法（硬删除）

    public void delete(T t) throws Exception;

    public void deleteById(ID id) throws Exception;

    public void deleteAllById(List<ID> ids) throws Exception;

    public void deleteAll(List<T> list) throws Exception;

    public void deleteAll() throws Exception;


    //删除相关方法（软删除——设置deleteSign为1）

    public void deleteSoftById(ID id) throws Exception;

    public void deleteSoftAllById(List<ID> ids) throws Exception;

    public void deleteSoftAll(List<T> list) throws Exception;

    public void deleteSoftAll() throws Exception;


    //查询相关方法

    public T findById(ID id) throws Exception;

    public Boolean existsById(ID id) throws Exception;

    public <S extends T> Boolean exists(S s) throws Exception;

    public List<T> findAllById(List<ID> ids) throws Exception;

    public List<T> findAll() throws Exception;

    public List<T> findAll(Sort sort) throws Exception;


    //更新相关方法

    public T update(T t) throws Exception;

}
