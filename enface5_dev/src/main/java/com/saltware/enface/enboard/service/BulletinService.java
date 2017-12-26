package com.saltware.enface.enboard.service;

import java.util.Map;


/**
 * <pre>  
 * 개요 : 개시물 Service Interface
 * @date : 2015. 11. 13.
 * @author : 권은총(솔트웨어)
 * @history : 
 *	-----------------------------------------------------------------------
 *	변경일       작성자              변경내용
 *	------------ ------------------- ---------------------------------------
 *	2015. 11. 13. 권은총(솔트웨어)    최초작성
 *	-----------------------------------------------------------------------
 *	</pre>
 */
public interface BulletinService {
	
	/**  
	 * 처리내용 : 게시물의 전체 건수를 들고 온다.
	 * @date : 2015. 11. 13.
	 * @author : 권은총(솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일       작성자              변경내용
	 *	------------ ------------------- ---------------------------------------
	 *	2015. 11. 13. 권은총(솔트웨어)    최초작성
	 *	-----------------------------------------------------------------------
	 * @param 
	 * @return 
	 * @exception 
	 */
	public int selectAllCount(Map paramMap) throws Exception;
	
	public int selectVisionBoardUserCount(Map paramMap) throws Exception;
}
