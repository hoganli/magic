package com.hogan.common.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * ClassName:BaseDaoFactoryBean
 * Description:工厂bean用于创建工厂
 * User:dada
 * Date:2017/7/27 21:26
 */
public class BaseDaoFactoryBean<R extends JpaRepository<S, ID>, S, ID extends Serializable> extends JpaRepositoryFactoryBean<R, S, ID> {

    public BaseDaoFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    /**
     * 重写createRepositoryFactory方法，用自定义的BaseDaoFactory类型创建repository实例
     */
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new BaseDaoFactory(entityManager);
    }
}
