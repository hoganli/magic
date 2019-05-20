package com.hogan.common.base;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * ClassName:BaseDaoFactory
 * Description:工厂通过两个方法来确定具体的实现类，也就是Spring Data Jpa实例化接口的时候需要创建的实现类
 * User:hogan.li
 * Date:2017/7/27 21:18
 */
public class BaseDaoFactory<S, ID extends Serializable> extends JpaRepositoryFactory {

    private final EntityManager em;

    public BaseDaoFactory(EntityManager em) {
        super(em);
        this.em = em;
    }

    /**
     * 重写getTargetRepository方法，获取自定义的repository实现
     */
    @Override
    protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
        return new BaseDaoImpl<T, ID>((Class<T>) information.getDomainType(), em);
    }

    /**
     * 重写getRepositoryBaseClass方法，获取自定义repository实现的类型
     */
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return BaseDaoImpl.class;
    }

}
