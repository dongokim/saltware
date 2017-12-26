package com.saltware.enface.enboard.dao;

import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * <pre>  
 * 개요 : 게시물과 관련된 DAO 
 * @date : 2015. 11. 13.
 * @author : (솔트웨어)
 * @history : 
 *	-----------------------------------------------------------------------
 *	변경일       작성자              변경내용
 *	------------ ------------------- ---------------------------------------
 *	2015. 11. 13. (솔트웨어)    최초작성
 *	-----------------------------------------------------------------------
 *	</pre>
 */
public class BulletinDao extends SqlMapClientDaoSupport {
	/**  
	 * 처리내용 : 지정된 게시판의 전체 게시물 건수를 조회한다. 
	 * @date : 2015. 11. 13.
	 * @author : (솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일       작성자              변경내용
	 *	------------ ------------------- ---------------------------------------
	 *	2015. 11. 13. (솔트웨어)    최초작성
	 *	-----------------------------------------------------------------------
	 * @param 
	 * @return 
	 * @exception 
	 */
	public int selectAllCount(Map paramMap) throws Exception {
		return (Integer) getSqlMapClientTemplate().queryForObject("bulletinDao.selectAllCount", paramMap);
	}
	
	public int selectVisionBoardUserCount(Map paramMap) throws Exception {
		return (Integer) getSqlMapClientTemplate().queryForObject("bulletinDao.selectVisionBoardUser", paramMap);
	}
}
