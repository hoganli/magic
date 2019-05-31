package com.hogan.app.book;


import com.hogan.app.member.MMember;
import com.hogan.app.member.MMemberDao;
import com.hogan.app.project.MProjectDao;
import com.hogan.common.base.BaseServiceImpl;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;


/**
 * @ClassName MBookService
 * @Description 预约Service实现类
 * @author hogan
 * @date 2019-05-24 17:04:56
 */
@Service
@Transactional
public class MBookService extends BaseServiceImpl<MBook, String> {

	@Autowired
	private MBookDao mBookDao;

	@Autowired
    private MMemberDao mMemberDao;

	@Autowired
    private MProjectDao mProjectDao;
	
	/**
     * 根据条件查询MBook列表（实现方式三选一）
     */
    public Page<MBook> findMBookByCondition(Map<String, Object> paramMap) throws Exception {
        Page<MBook> page = mBookDao.findMBookByCriteria(paramMap);
        fillDatas(page.getContent());
        return page;
//        return mBookDao.findMBookByJpql(paramMap);
//        return mBookDao.findMBookBySql(paramMap);
    }

    private void fillDatas(List<MBook> content) throws Exception {
        if(CollectionUtils.isEmpty(content)) {
            return;
        }
        for(MBook book : content) {
            book.setMember(mMemberDao.findById(book.getMemberId()).get());
            book.setProject(mProjectDao.findById(book.getProjectId()).get());
        }
    }

    @Transactional
    public void createBook(MMember member) {
        // 根据手机号查询用户
        MMember memberInDb = mMemberDao.findByMobilePhone(member.getMobilePhone());
        // 新建预约
        MBook book = new MBook();
        if(memberInDb == null) {
            // 新建用户
            member.setLevel(1);
            member.setIntegral(0);
            member = mMemberDao.save(member);
            book.setMemberId(member.getId());
        } else {
            memberInDb.setNickName(member.getNickName());
            book.setMemberId(memberInDb.getId());
            mMemberDao.save(memberInDb);
        }

        book.setbDate(member.getDate());
        book.setbTime(member.getTime());
        book.setProjectId(member.getProjectId());
        mBookDao.save(book);
    }
}