
package com.saltware.enface.enboard.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.saltware.enview.security.EnviewMenu;



/**  
 * 개요 : 게시판 메뉴 Data Access Object
 * @date: 2015.08.06
 * @author: 나상모(솔트웨어)
 * @history : 
 *	-----------------------------------------------------------------------
 *	변경일				작성자						변경내용
 *	---------- ------------------- ---------------------------------------
 * 2015.08.06 	나상모(솔트웨어) 				최초작성
 *	-----------------------------------------------------------------------
 */
public class BoardMenuDao extends SqlMapClientDaoSupport {
	
	/**  
	 * 처리내용 : 게시판 메뉴를 조회한다.
	 * @date: 2015.08.06
	 * @author: 나상모(솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일				작성자						변경내용
	 *	---------- ------------------- ---------------------------------------
	 * 2015.08.06 	나상모(솔트웨어) 				최초작성
	 *	-----------------------------------------------------------------------
	 * @param paramMap - 조회조건
	 * @return Data Map List
	 * @exception DataAccessException
	 */
	public List selectList(Map paramMap) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("boardMenu.selectList", paramMap);
	}

	/**  
	 * 처리내용 : 게시판 전체 갯수룰 조회한다.
	 * @date: 2015.09.14
	 * @author: 나상모(솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일				작성자						변경내용
	 *	---------- ------------------- ---------------------------------------
	 * 2015.09.14 	나상모(솔트웨어) 				최초작성
	 *	-----------------------------------------------------------------------
	 * @param paramMap - 조회조건
	 * @return 게시판 갯수
	 * @exception DataAccessException
	 */
	public int selectAllCount(Map paramMap) throws DataAccessException {
		return (Integer)getSqlMapClientTemplate().queryForObject("boardMenu.selectAllCount", paramMap);
	}
	
}
