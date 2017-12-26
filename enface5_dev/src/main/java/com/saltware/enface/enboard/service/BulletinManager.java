package com.saltware.enface.enboard.service;

import java.util.Map;

import com.saltware.enview.exception.BaseException;

public interface BulletinManager {
	
	/**  
	 * 처리내용 : 게시물 관련 Cach정보를 초기화 한다.
	 * @date : 2015. 11. 13.
	 * @author : 권은총(솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일       작성자              변경내용
	 *	------------ ------------------- ---------------------------------------
	 *	2015. 11. 13. 권은총(솔트웨어)    최초작성
	 *	-----------------------------------------------------------------------
	 * @exception BaseException
	 */
	public void invalidate();
	
	
	
	/**  
	 * 처리내용 : 게시물 전체 건수를 Return 한다. 
	 * <br/> boardId를 넘길 경우 해당 게시판의 게시물 건수를 조회한다.
	 * @date : 2015. 11. 13.
	 * @author : 가나다(솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일       작성자              변경내용
	 *	------------ ------------------- ---------------------------------------
	 *	2015. 11. 13. 가나다(솔트웨어)    최초작성
	 *	-----------------------------------------------------------------------
	 * @param 
	 * @return 게시물건수
	 * @exception BaseException
	 */
	@SuppressWarnings("rawtypes")
	public int getBulletinCount(Map paramMap) throws BaseException;
	
	public int getBulletinCount() throws BaseException;
	
	public int getVisionBoardUserCount() throws BaseException;
}
