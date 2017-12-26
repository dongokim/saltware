package com.saltware.enface.enboard.service.impl;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.saltware.enface.enboard.service.BulletinManager;
import com.saltware.enface.enboard.service.BulletinService;
import com.saltware.enview.Enview;
import com.saltware.enview.exception.BaseException;

public class BulletinManagerImpl implements BulletinManager {
	private final Log log = LogFactory.getLog(getClass());
	private final String BLTN_SESSION_KEY_PREFIX = "Bltn.";

	private BulletinService bulletinService;

	public void setBulletinService(BulletinService bulletinService) {
		this.bulletinService = bulletinService;
	}

	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * @override 처리내용 : 캐쉬된 게시물 관련 정보를 초기화 한다.
	 * @date : 2015. 11. 13.
	 * @author : 권은총(솔트웨어)
	 * @history : ----------------------------------------------------------------------- 변경일 작성자 변경내용 ------------ ------------------- --------------------------------------- 2015. 11. 13. 권은총(솔트웨어) 최초작성
	 *          -----------------------------------------------------------------------
	 * @param
	 * @return
	 * @exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void invalidate() {
		HttpSession session = getRequest().getSession();
		Enumeration en = session.getAttributeNames();
		while (en.hasMoreElements()) {
			String name = (String) en.nextElement();
			if (name.startsWith(BLTN_SESSION_KEY_PREFIX)) {
				session.removeAttribute(name);
			}
		}
		log.info("Bulletin Cache Invalidate.");
	}

	@Override
	public int getBulletinCount() throws BaseException {
		Map paramMap = new HashMap();
		paramMap.put("boardId", "");
		return getBulletinCount(paramMap);
	}

	/**
	 * @override 처리내용 : 전체 게시물 수를 카운트 한다.
	 * @date : 2015. 11. 13.
	 * @author : 권은총(솔트웨어)
	 * @history : ----------------------------------------------------------------------- 변경일 작성자 변경내용 ------------ ------------------- --------------------------------------- 2015. 11. 13. 권은총(솔트웨어) 최초작성
	 *          -----------------------------------------------------------------------
	 * @param
	 * @return
	 * @exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public int getBulletinCount(Map paramMap) throws BaseException {
		HttpSession session = getRequest().getSession();
		Integer count = 0;

		try {
			String boardId = "";
			if (paramMap.get("boardId") != null) {
				boardId = (String) paramMap.get("boardId") + ".";
			}

			count = (Integer) session.getAttribute(BLTN_SESSION_KEY_PREFIX + boardId + "allCount");
			if (count == null) {
				count = bulletinService.selectAllCount(paramMap);
				session.setAttribute(BLTN_SESSION_KEY_PREFIX + boardId + "allCount", count);
			}
			log.debug("Bulletin All Count : " + count);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				e.printStackTrace();
			}
			log.error("getBulletinCount Error Message : " + e.getMessage());

			throw new BaseException(e.getMessage());
		}

		return count;
	}

	@Override
	public int getVisionBoardUserCount() throws BaseException {
		HttpSession session = getRequest().getSession();
		Integer count = 0;

		try {
			String boardId = Enview.getConfiguration().getString("pt.vision.board.id", "privateBoard");
			HashMap<String, String> paramMap = new HashMap<String, String>();

			count = (Integer) session.getAttribute(BLTN_SESSION_KEY_PREFIX + boardId + "visionBoardUser");
			if (count == null) {
				paramMap.put("boardId", boardId);
				count = bulletinService.selectVisionBoardUserCount(paramMap);
				session.setAttribute(BLTN_SESSION_KEY_PREFIX + boardId + "visionBoardUser", count);
			}
			log.debug("Bulletin visionBoardUser Count : " + count);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				e.printStackTrace();
			}
			log.error("getVisionBoardUserCount Error Message : " + e.getMessage());

			throw new BaseException(e.getMessage());
		}

		return count;
	}

}
