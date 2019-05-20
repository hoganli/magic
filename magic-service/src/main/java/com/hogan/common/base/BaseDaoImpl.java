package com.hogan.common.base;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * ClassName:BaseDaoImpl
 * Description:BaseDAO实现类（jpa继承实现+base自定义实现）
 * User:hogan.li
 * Date:2017/7/27 21:41
 */
public class BaseDaoImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseDao<T, ID> {

    private final EntityManager em;

    private final Class<T> domainClass;

    public BaseDaoImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.domainClass = domainClass;
        this.em = em;
    }

    @Override
    public void deleteAllById(List<ID> ids) throws Exception {

        // 创建jpql语句
        String jpql = "delete from " + domainClass.getSimpleName() + " domain where domain.id in :ids";

        // 创建query对象
        Query query = em.createQuery(jpql);
        query.setParameter("ids", ids);
        query.executeUpdate();
    }

    @Override
    public void deleteSoftById(ID id) throws Exception {
        // 创建jpql语句
        String jpql = "update " + domainClass.getSimpleName() + " domain set domain.deleteSign = true where domain.id = :id";

        // 创建query对象
        Query query = em.createQuery(jpql);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void deleteSoftAllById(List<ID> ids) throws Exception {
        // 创建jpql语句
        String jpql = "update " + domainClass.getSimpleName() + " domain set domain.deleteSign = true where domain.id in :ids";

        // 创建query对象
        Query query = em.createQuery(jpql);
        query.setParameter("ids", ids);
        query.executeUpdate();
    }

    @Override
    public void deleteSoftAll(List<T> list) throws Exception {
        throw new RuntimeException("此方法暂未实现，如需使用，请先实现！");

    }

    @Override
    public void deleteSoftAll() throws Exception {
        // 创建jpql语句
        String jpql = "update " + domainClass.getSimpleName() + " domain set domain.deleteSign = true";

        // 创建query对象
        Query query = em.createQuery(jpql);
        query.executeUpdate();
    }

}
