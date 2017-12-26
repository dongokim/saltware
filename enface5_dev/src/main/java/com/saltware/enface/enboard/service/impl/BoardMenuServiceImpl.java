package com.saltware.enface.enboard.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.saltware.enface.enboard.dao.BoardMenuDao;
import com.saltware.enface.enboard.service.BoardMenuService;
import com.saltware.enview.exception.BaseException;

/**  
 * 개요 : 게시판 메뉴 Service 구현
 * @date: 2015.08.06
 * @author: 나상모(솔트웨어)
 * @history : 
 *	-----------------------------------------------------------------------
 *	변경일				작성자						변경내용
 *	---------- ------------------- ---------------------------------------
 * 2015.08.06 	나상모(솔트웨어) 				최초작성
 *	-----------------------------------------------------------------------
 */
public class BoardMenuServiceImpl implements BoardMenuService {
	
	/**
	 * 게시판 메뉴 DAO
	 */
	private BoardMenuDao boardMenuDao;
	
	/**
	 * 처리내용 : 게시판 멘 DAO를 설정한다.
	 * @date: 2015.08.06
	 * @author: 나상모(솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일				작성자						변경내용
	 *	---------- ------------------- ---------------------------------------
	 * 2015.08.06 	나상모(솔트웨어) 				최초작성
	 *	-----------------------------------------------------------------------
	 * @param boardMenuDAO 게시판 메뉴 DAO
	 */
	public void setBoardMenuDAO(BoardMenuDao boardMenuDao) {
		this.boardMenuDao = boardMenuDao;
	}	 

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
	public List selecList(Map paramMap) throws BaseException {
		return boardMenuDao.selectList(paramMap);
	}

	/**  
	 * 처리내용 : 게시판 메뉴 DAO를 설정한다.
	 * @date: 2015.08.06
	 * @author: 나상모(솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일				작성자						변경내용
	 *	---------- ------------------- ---------------------------------------
	 * 2015.08.06 	나상모(솔트웨어) 				최초작성
	 *	-----------------------------------------------------------------------
	 * @param boardMenuDao 게시판메뉴DAO
	 */
	public void setBoardMenuDao(BoardMenuDao boardMenuDao) {
		this.boardMenuDao = boardMenuDao;
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
	public int selectAllCount(Map paramMap) throws BaseException {
		return this.boardMenuDao.selectAllCount(paramMap);
	}

}
