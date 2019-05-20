package com.hogan.framework.user;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Map;


/**
 * ClassName:UserDaoImpl
 * Description:UserDaoImpl
 * User:dada
 * Date:2018/07/18
 */
public class UserDaoImpl implements UserDaoCustom {

    @Autowired
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<User> findUserByCriteria(Map<String, Object> paramMap) throws Exception {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);

        Integer start = null;
        Integer limit = null;
        Integer page = null;

        // 创建查询条件列表
        ArrayList<Predicate> predicates = new ArrayList<Predicate>();

        if (paramMap != null && paramMap.size() > 0) {

            // 获取分页查询参数
            start = (Integer) paramMap.get("start");
            limit = (Integer) paramMap.get("limit");
            page = (Integer) paramMap.get("page");

            // 获取动态查询条件
//            String id = (String) paramMap.get("id");
            String unitId = (String) paramMap.get("unitId");
            String account = (String) paramMap.get("account");
            String userName = (String) paramMap.get("userName");
            String keyword = (String) paramMap.get("keyword");

            // 动态拼装查询条件
//            if (StringUtils.isNotBlank(id)) {
//                predicates.add(cb.equal(root.get("id"), id));
//            }

            if (StringUtils.isNotBlank(unitId)) {
                predicates.add(cb.equal(root.get("unitId"), unitId));
            }

            if (StringUtils.isNotBlank(account)) {
                predicates.add(cb.like(root.<String>get("account"), "%" + account + "%"));
            }

            if (StringUtils.isNotBlank(userName)) {
                predicates.add(cb.like(root.<String>get("userName"), "%" + userName + "%"));
            }

            if (StringUtils.isNotBlank(keyword)) {
                predicates.add(
                        cb.or(
                                cb.like(root.<String>get("account"), "%" + keyword + "%"),
                                cb.like(root.<String>get("userName"), "%" + keyword + "%"),
                                cb.like(root.<String>get("email"), "%" + keyword + "%"),
                                cb.like(root.<String>get("mobilePhone"), "%" + keyword + "%"),
                                cb.like(root.<String>get("workCode"), "%" + keyword + "%")
                        )
                );
            }
        }

        //固定筛选条件
        predicates.add(cb.equal(root.get("adminSign"), false));
        predicates.add(cb.equal(root.get("deleteSign"),false));

        // 设置查询条件列表
        cq.where(predicates.toArray(new Predicate[predicates.size()]));

        // 设置排序
        String sortProperty = "createDate";
        cq.orderBy(cb.desc(root.get(sortProperty)));

        // 获取查询实例（查询list和count）
        TypedQuery<User> typedQueryList = em.createQuery(cq);
        TypedQuery<User> typedQueryCount = em.createQuery(cq);

        // 设置分页
        Pageable pageable = null;
        if( start != null && limit != null && page != null){
            Sort sort = new Sort(Sort.Direction.DESC, sortProperty);
            pageable = new PageRequest(page -1 , limit, sort);
            typedQueryList.setFirstResult(start);
            typedQueryList.setMaxResults(limit);
        }

        // 构建返回对象
        return new PageImpl<User>(typedQueryList.getResultList(), pageable, typedQueryCount.getResultList().size());
    }

    @Override
    public Page<User> findUserByJpql(Map<String, Object> paramMap) throws Exception {

      //复杂查询的Jpql语句设置
        StringBuilder sb = new StringBuilder("select t from User t where 1 = 1 ");

        Integer start = null;
        Integer limit = null;
        Integer page = null;

        // 创建查询条件列表
        if(paramMap != null && paramMap.size() > 0 ){

            // 获取分页查询参数
            start = (Integer) paramMap.get("start");
            limit = (Integer) paramMap.get("limit");
            page = (Integer) paramMap.get("page");

            // 获取动态查询条件
            String id = (String) paramMap.get("id");

            // 拼装动态查询条件
            if(StringUtils.isNotBlank(id)){
                sb.append(" and t.id = '" + id + "'");
            }
        }

        // 设置固定查询条件
        sb.append(" and t.deleteSign = " + false);

        // 设置排序
        String sortProperty = "createDate";
        sb.append(" order by " + sortProperty + " desc");

        // 获取查询实例（查询list和count）
        String jpql = sb.toString();
        TypedQuery<User> typedQueryList = em.createQuery(jpql, User.class);
        TypedQuery<User> typedQueryCount = em.createQuery(jpql, User.class);

        // 设置分页
        Pageable pageable = null;
        if(start != null && limit != null && page != null){
            Sort sort = new Sort(Sort.Direction.DESC, sortProperty);
            pageable= new PageRequest(page -1 , limit, sort);
            typedQueryList.setFirstResult(start);
            typedQueryList.setMaxResults(limit);
        }

        // 构建返回对象
        return new PageImpl<User>(typedQueryList.getResultList(), pageable,  typedQueryCount.getResultList().size());
    }

    @Override
    public Page<User> findUserBySql(Map<String, Object> paramMap) throws Exception {

        //复杂查询的SQL语句设置
        StringBuilder sb = new StringBuilder("select * from fw_user t where 1 = 1 ");

        Integer start = null;
        Integer limit = null;
        Integer page = null;

        // 创建查询条件列表
        if(paramMap != null && paramMap.size() > 0 ){

            // 获取分页查询参数
            start = (Integer) paramMap.get("start");
            limit = (Integer) paramMap.get("limit");
            page = (Integer) paramMap.get("page");

            // 获取动态查询条件
            String id = (String) paramMap.get("id");

            // 拼装动态查询条件
            if(StringUtils.isNotBlank(id)){
                sb.append(" and t.id = '" + id + "'");
            }
        }

        // 设置固定查询条件
        sb.append(" and t.delete_sign = " + false);

        // 设置排序
        sb.append(" order by " + "create_date" + " desc");

        // 获取查询实例（查询list和count）
        String sql = sb.toString();
        Query queryList = em.createNativeQuery(sql, User.class);
        Query queryCount = em.createNativeQuery(sql, User.class);

        // 设置分页
        Pageable pageable = null;
        if(start != null && limit != null && page != null){
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            pageable = new PageRequest(page -1 , limit , sort);
            queryList.setFirstResult(start);
            queryList.setMaxResults(limit);
        }

        // 构建返回对象
        return new PageImpl<User>(queryList.getResultList(), pageable, queryCount.getResultList().size());
    }
}