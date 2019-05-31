package com.hogan.app.book;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
 * @ClassName MBookDaoImpl
 * @Description 预约DaoImpl自定义接口实现类
 * @author hogan
 * @date 2019-05-24 17:04:56
 */
public class MBookDaoImpl implements MBookDaoCustom {

    @Autowired
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<MBook> findMBookByCriteria(Map<String, Object> paramMap) throws Exception {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MBook> cq = cb.createQuery(MBook.class);
        Root<MBook> root = cq.from(MBook.class);

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
            String id = (String) paramMap.get("id");
            String bDate = (String) paramMap.get("bDate");
            // 动态拼装查询条件
            if (StringUtils.isNotBlank(id)) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (StringUtils.isNotBlank(bDate)) {
                predicates.add(cb.equal(root.get("bDate"), bDate));
            }
        }

        //固定筛选条件
        predicates.add(cb.equal(root.get("deleteSign"),false));

        // 设置查询条件列表
        cq.where(predicates.toArray(new Predicate[predicates.size()]));

        // 设置排序
        String sortProperty = "bDate";
        cq.orderBy(cb.desc(root.get(sortProperty)));

        // 获取查询实例（查询list和count）
        TypedQuery<MBook> typedQueryList = em.createQuery(cq);
        TypedQuery<MBook> typedQueryCount = em.createQuery(cq);

        // 设置分页
        Pageable pageable = null;
        if( start != null && limit != null && page != null){
            Sort sort = new Sort(Sort.Direction.DESC, sortProperty);
            pageable = new PageRequest(page -1 , limit, sort);
            typedQueryList.setFirstResult(start);
            typedQueryList.setMaxResults(limit);
        }

        // 构建返回对象
        return new PageImpl<MBook>(typedQueryList.getResultList(), pageable, typedQueryCount.getResultList().size());
    }

    @Override
    public Page<MBook> findMBookByJpql(Map<String, Object> paramMap) throws Exception {

      //复杂查询的Jpql语句设置
        StringBuilder sb = new StringBuilder("select t from MBook t where 1 = 1 ");

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
        TypedQuery<MBook> typedQueryList = em.createQuery(jpql, MBook.class);
        TypedQuery<MBook> typedQueryCount = em.createQuery(jpql, MBook.class);

        // 设置分页
        Pageable pageable = null;
        if(start != null && limit != null && page != null){
            Sort sort = new Sort(Sort.Direction.DESC, sortProperty);
            pageable= new PageRequest(page -1 , limit, sort);
            typedQueryList.setFirstResult(start);
            typedQueryList.setMaxResults(limit);
        }

        // 构建返回对象
        return new PageImpl<MBook>(typedQueryList.getResultList(), pageable,  typedQueryCount.getResultList().size());
    }

    @Override
    public  Page<MBook> findMBookBySql(Map<String, Object> paramMap) throws Exception {

        //复杂查询的SQL语句设置
        StringBuilder sb = new StringBuilder("select * from m_book t where 1 = 1 ");

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
        Query queryList = em.createNativeQuery(sql, MBook.class);
        Query queryCount = em.createNativeQuery(sql, MBook.class);

        // 设置分页
        Pageable pageable = null;
        if(start != null && limit != null && page != null){
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            pageable = new PageRequest(page -1 , limit , sort);
            queryList.setFirstResult(start);
            queryList.setMaxResults(limit);
        }

        // 构建返回对象
        return new PageImpl<MBook>(queryList.getResultList(), pageable, queryCount.getResultList().size());
    }
}