package com.hogan.common.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * ClassName:BaseService
 * Description:BaseService实现类
 * User:dada
 * Date:2019/1/30 12:22
 */
public class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

    //BaseDao需要根据不同的T类型来赋值，以实现通用
    @Autowired
    protected BaseDao<T, ID> baseDao;

    @Override
    public T save(T t) throws Exception {
        return baseDao.save(t);
    }

    @Override
    public <S extends T> List<S> saveAll(List<S> list) throws Exception {
        return baseDao.saveAll(list);
    }

    @Override
    public void delete(T t) throws Exception {
        baseDao.delete(t);
    }

    @Override
    public void deleteById(ID id) throws Exception {
        baseDao.deleteById(id);
    }

    @Override
    public void deleteAllById(List<ID> ids) throws Exception {
        baseDao.deleteAllById(ids);
    }

    @Override
    public void deleteAll(List<T> list) throws Exception {
        baseDao.deleteAll(list);
    }

    @Override
    public void deleteAll() throws Exception {
        baseDao.deleteAll();
    }

    @Override
    public void deleteSoftById(ID id) throws Exception {
        baseDao.deleteSoftById(id);
    }

    @Override
    public void deleteSoftAllById(List<ID> ids) throws Exception {
        baseDao.deleteSoftAllById(ids);
    }

    @Override
    public void deleteSoftAll(List<T> list) throws Exception {
        baseDao.deleteSoftAll(list);
    }

    @Override
    public void deleteSoftAll() throws Exception {
        baseDao.deleteSoftAll();
    }

    @Override
    public T findById(ID id) throws Exception {
        return baseDao.getOne(id);
    }

    @Override
    public Boolean existsById(ID id) throws Exception {
        return baseDao.existsById(id);
    }

    @Override
    public <S extends T> Boolean exists(S s) throws Exception {
        return baseDao.exists(Example.of(s));
    }

    @Override
    public List<T> findAllById(List<ID> ids) throws Exception {
        return baseDao.findAllById(ids);
    }

    @Override
    public List<T> findAll() throws Exception {
        return baseDao.findAll();
    }

    @Override
    public List<T> findAll(Sort sort) throws Exception {
        return baseDao.findAll(sort);
    }

    @Override
    public T update(T t) throws Exception {
        return baseDao.save(t);
    }
}
