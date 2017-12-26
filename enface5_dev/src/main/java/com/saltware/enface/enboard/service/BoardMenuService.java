
package com.saltware.enface.enboard.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;

import com.saltware.enview.exception.BaseException;
import com.saltware.enview.security.CommonUserMenu;
import com.saltware.enview.security.EnviewMenu;


/**  
 * 개요 : 게시판 메뉴 Service
 * @date: 2015.08.06
 * @author: 나상모(솔트웨어)
 * @history : 
 *	-----------------------------------------------------------------------
 *	변경일				작성자						변경내용
 *	---------- ------------------- ---------------------------------------
 * 2015.08.06 	나상모(솔트웨어) 				최초작성
 *	-----------------------------------------------------------------------
 */
public interface BoardMenuService{
	
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
	 * @exception BaseException
	 */
	public List selecList(Map paramMap) throws BaseException;
	
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
	public int selectAllCount(Map paramMap) throws BaseException;

}
