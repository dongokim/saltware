package com.saltware.enface.enboard.service.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.saltware.enface.enboard.service.BoardMenuManager;
import com.saltware.enface.enboard.service.BoardMenuService;
import com.saltware.enface.enboard.vo.BoardMenuVo;
import com.saltware.enview.Enview;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.security.EVSubject;
import com.saltware.enview.security.UserInfo;

/**  
 * 개요 : 게시판 메뉴 매니저 구현(캐시사용) 
 * @date: 2015.08.06
 * @author: 나상모(솔트웨어)
 * @history : 
 *	-----------------------------------------------------------------------
 *	변경일				작성자						변경내용
 *	---------- ------------------- ---------------------------------------
 * 2015.08.06 	나상모(솔트웨어) 				최초작성
 * 2015.08.31 	나상모(솔트웨어) 				getBoardMenuList 추가
 *	-----------------------------------------------------------------------
 */
public class BoardMenuManagerImpl implements BoardMenuManager {
	/**
	 * 로그
	 */
	private final Log log = LogFactory.getLog(getClass());
	
	/**
	 * 메뉴를 세션에 저장할때 사용하는 접두사. 뒤에는 언어 코드가 붙는다. 
	 */
	private final String MENU_SESSION_KEY_PREFIX = "BoardMenu.";

	/**
	 * 게시판 메뉴 조회 서비스
	 */
	private BoardMenuService boardMenuService = null;
	
	
	/**  
	 * 처리내용 : 게시판 메뉴를 DB에서 불러온다. 
	 * <br> 대분류와 중분류 게시판을 묶어 3단계메뉴로 만든다.
	 * <br> 로그인 정보가 없는 경우 [로그인전 누구나] 권한이 있는 게시판만 보여준다. 
	 * <br> 로그인 정보가 있는 경우 [로그인전 누구나], [로그인후 누구나], 그리고 권한이 있는 게시판을  보여준다.
	 * <br> 하위메뉴가 없는 카테고리 메뉴는 삭제한다.  
	 * @date: 2015.08.06
	 * @author: 나상모(솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일				작성자						변경내용
	 *	---------- ------------------- ---------------------------------------
	 * 2015.08.06 	나상모(솔트웨어) 				최초작성
	 *	-----------------------------------------------------------------------
	 * @param request HttpRequest
	 * @param langKnd 언어
	 * @exception BaseException
	 */
	private Map loadMenu( String linkYns, String langKnd) throws BaseException {
		log.debug("*** loadMenu. linkYns=" + linkYns + ",langKnd=" + langKnd);
		
		// 최상위메뉴를 만들어 넣는다.
		BoardMenuVo topMenu = new BoardMenuVo();
		topMenu.setPageId("root");
		topMenu.setTitle("Root");
		topMenu.setShortTitle("Root");
		topMenu.setMenuType("C");
		Map menuMap = new HashMap<String, BoardMenuVo>();
		menuMap.put( topMenu.getPageId(), topMenu);
		Map portalMenuMap = new HashMap<String, BoardMenuVo>();
		portalMenuMap.put( topMenu.getPageId(), topMenu);
		
		// 사용자의 principal 목록을 읽어 LIST로 만든다.
		List idList = new ArrayList();
		UserInfo userInfo = EVSubject.getUserInfo();
		boolean isAdmin = false;
		if( userInfo != null) {
			// 사용자 PID
			idList.add( userInfo.getInt("principalId", 0));
			// 그룹 PID
			idList.add( userInfo.getInt("groupPrincipalId", 0));
			// 역할 PID
			List roleIdList = (List)userInfo.getUserInfoMap().get("roleIdList");
			for (int i = 0; i < roleIdList.size(); i++) {
				Map map = (Map)roleIdList.get(i);
				idList.add(map.get("id"));
			}
			// 그룹리스트
			List groupList = (List)userInfo.getUserInfoMap().get("groupList");
			if( groupList!=null) {
				for (int i = 0; i < groupList.size(); i++) {
					Map map = (Map)groupList.get(i);
					idList.add(map.get("principal_id"));
				}
			}
			// 부서리스트
			List orgList = (List)userInfo.getUserInfoMap().get("orgList");
			if( orgList!=null) { 
				for (int i = 0; i < orgList.size(); i++) {
					Map map = (Map)orgList.get(i);
					idList.add(map.get("principal_id"));
				}
			}
			
			isAdmin = userInfo.getHasAdminRole();
		}
		log.debug("idList=" + idList);
		String rootCateId = Enview.getConfiguration().getString("board.rootCateId", "11");
		Map paramMap = new HashMap();
		paramMap.put("isAdmin", isAdmin);
		paramMap.put("idList", idList);
		paramMap.put("rootCateId", rootCateId);
		paramMap.put("langKnd", langKnd);
		paramMap.put("linkYns", linkYns);
		List menuList = boardMenuService.selecList(paramMap);
		
		BoardMenuVo parentMenu = null, menu = null, menu0;
		for (int i = 0; i < menuList.size(); i++) {
			menu = (BoardMenuVo)menuList.get(i);
			
			menu0 = (BoardMenuVo)menuMap.get( menu.getPageId());
			// 중복된경우 권한만 OR 시키고 건너띈다 
			if( menu0!=null) {
				log.debug("duplicated menu. merge action mask " + menu0.getActionMask() + " | " + menu.getActionMask() + " = " + (menu0.getActionMask() | menu.getActionMask()));
				menu0.setActionMask( menu0.getActionMask() | menu.getActionMask());
				continue;
			}
			
			menuMap.put( menu.getPageId(), menu);
			parentMenu = (BoardMenuVo)menuMap.get(menu.getParentId());
//			log.debug("parentMenu=" + parentMenu);
			
			if( parentMenu==null) {
				log.debug( "Menu has no parent : " + menu.toString());
				continue;
			}
//			log.debug( menu.toString() + "->" + parentMenu.getPageId());
			menu.setParentMenu( parentMenu);
			parentMenu.addElement(menu);
		}
		// 하위메뉴가 없는 카테고리메뉴(MenuType=C)를 제거한다.
		cleanMenu( topMenu);
		
		return menuMap;
	}

	
	/**  
	 * 처리내용 : 언어코드를 읽어 온다.
	 * @date: 2015.08.06
	 * @author: 나상모(솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일				작성자						변경내용
	 *	---------- ------------------- ---------------------------------------
	 * 2015.08.06 	나상모(솔트웨어) 				최초작성
	 *	-----------------------------------------------------------------------
	 * @param request HttpRequest
	 * @return 언어코드
	 * @exception BaseException
	 */
    public String getLangKnd( HttpServletRequest request) {
    	String sessionKey = "langKnd";
    	
    	String langKnd = (String)request.getSession().getAttribute( sessionKey);
    	if( langKnd==null) {
			langKnd = "ko";
			request.getSession().setAttribute(sessionKey, langKnd);
    	}
    	return langKnd;
    }
    
	/**  
	 * 처리내용: 세션에서 게시판 메뉴 맵을 읽어온다. 만약 존재하지 않으면 DB에서 읽어온다.
	 * @date: 2015.08.06
	 * @author: 나상모(솔트웨어)
	 * @history: 
	 *	-----------------------------------------------------------------------
	 *	변경일				작성자						변경내용
	 *	---------- ------------------- ---------------------------------------
	 * 2015.08.06 	나상모(솔트웨어) 				최초작성
	 *	-----------------------------------------------------------------------
	 * @param request HttpRequest
	 * @param langKnd 언어
	 * @exception BaseException
	 */
    private Map getMenuMap( HttpServletRequest request, String linkYns, String langKnd) throws BaseException{
		log.debug("*** getMenuMap. linkYns=" + linkYns + ",langKnd=" + langKnd);
		String key= MENU_SESSION_KEY_PREFIX + linkYns + "." +  langKnd;
		log.debug("*** getMenuMap. key=" + key);
		
		Map menuMap = (Map)request.getSession().getAttribute(key);
		if( menuMap == null) {
			menuMap = loadMenu( linkYns, langKnd);
			request.getSession().setAttribute(MENU_SESSION_KEY_PREFIX + linkYns + "." + langKnd, menuMap);
		}
		return menuMap;
    }

    /**  
	 * 처리내용 : 게시판 최상위 메뉴를 조회한다.
	 * @date: 2015.08.06
	 * @author: 나상모(솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일				작성자						변경내용
	 *	---------- ------------------- ---------------------------------------
	 * 2015.08.06 	나상모(솔트웨어) 				최초작성
	 *	-----------------------------------------------------------------------
	 * @param request
	 * @return 게시판메뉴
	 * @exception BaseException
	 */
	public BoardMenuVo getTopMenu() throws BaseException {
		return getMenu("root");
		
	}
	
    /**  
	 * 처리내용 : 포탈용 게시판 최상위 메뉴를 조회한다.
	 * @date: 2015.08.06
	 * @author: 나상모(솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일				작성자						변경내용
	 *	---------- ------------------- ---------------------------------------
	 * 2015.08.06 	나상모(솔트웨어) 				최초작성
	 *	-----------------------------------------------------------------------
	 * @param request
	 * @return 게시판메뉴
	 * @exception BaseException
	 */
	public BoardMenuVo getTopMenuForPortal() throws BaseException {
		return getMenu("root", "Y");
		
	}
	
	
	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder
		        .getRequestAttributes()).getRequest();
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
	 * @param request
	 * @param menuId - 메뉴ID
	 * @return 게시판메뉴
	 * @exception BaseException
	 */
	public BoardMenuVo getMenu(String menuId, String linkYns) throws BaseException {
		HttpServletRequest request = getRequest();
		String langKnd = getLangKnd( request);
		Map menuMap = getMenuMap(request, linkYns, langKnd);
		return (BoardMenuVo)menuMap.get( menuId);
	}

	/**  
	 * 처리내용 : 게시판 메뉴 서비스를 설정한다.
	 * @date: 2015.08.06
	 * @author: 나상모(솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일				작성자						변경내용
	 *	---------- ------------------- ---------------------------------------
	 * 2015.08.06 	나상모(솔트웨어) 				최초작성
	 *	-----------------------------------------------------------------------
	 * @param boardMenuService 게시판메뉴서비스
	 * @return Data Map Collection
	 * @exception BaseException
	 */
	public void setBoardMenuService(BoardMenuService boardMenuService) {
		this.boardMenuService = boardMenuService;
	}
	
	/**  
	 * 처리내용 : 게시판 메뉴 캐시를 재설정한다.
	 * <br> 로그인, 로그아웃, 언어변경, 사용자변경 재절정해야한다. 
	 * @date: 2015.08.06
	 * @author: 나상모(솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일				작성자						변경내용
	 *	---------- ------------------- ---------------------------------------
	 * 2015.08.06 	나상모(솔트웨어) 				최초작성
	 *	-----------------------------------------------------------------------
	 * @exception BaseException
	 */
	public void invalidate() {
		HttpSession session = getRequest().getSession();
		Enumeration en = session.getAttributeNames();
		while( en.hasMoreElements()) {
			String name = (String)en.nextElement();
			if( name.startsWith( MENU_SESSION_KEY_PREFIX) ) {
				session.removeAttribute(name);
			}
		}
	}
	
	/**  
	 * 처리내용 : 하위메뉴가 없는 카테고리메뉴(MenuType=C)를 제거한다.
	 * @date: 2015.08.06
	 * @author: 나상모(솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일				작성자						변경내용
	 *	---------- ------------------- ---------------------------------------
	 * 2015.08.06 	나상모(솔트웨어) 				최초작성
	 *	-----------------------------------------------------------------------
	 * @param request HttpRequest
	 * @exception BaseException
	 * 
	 * todo 20161117 서울대의 경우 root-1depth 구조라 수정이 필요함. 
	 */
	private void cleanMenu( BoardMenuVo parentMenu) {
		// 카테고리타입이 아니면 아무일도 안하고 리턴!
		if( !parentMenu.getMenuType().equals("C")) {
			return;
		}
		BoardMenuVo menu;
		// 하위로 재귀호출
		for (int i = 0; i < parentMenu.getElements().size(); i++) {
			menu = (BoardMenuVo)parentMenu.getElements().get(i);
			cleanMenu( menu);
		}
		// 하위메뉴중 카테고리 타입이고 하위메뉴가 없는 메뉴  제거 
		int index = 0;
		while( index < parentMenu.getElements().size()) {
			menu = (BoardMenuVo)parentMenu.getElements().get(index);
			if( menu.getMenuType().equals("C") && menu.getElements().size()==0) {
				parentMenu.getElements().remove(index);
			} else {
				index++;
			}
		}
	}

	/**  
	 * 처리내용 : 권한에 따른 게시판 메뉴를 조회한다.(내부 재귀호출용, 카테고리 제외)
	 * @date: 2015.08.31
	 * @author: 나상모(솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일				작성자						변경내용
	 *	---------- ------------------- ---------------------------------------
	 * 2015.08.31 	나상모(솔트웨어) 				최초작성
	 *	-----------------------------------------------------------------------
	 * @param auth 카테고리
	 * @param boardList 게시판 메뉴리스트
	 * @param auth 권한
	 * @exception BaseException
	 */
	private void getBoardList( BoardMenuVo cate, List boardList, int auth) {
		List subList = cate.getElements();
		BoardMenuVo sub = null;
		log.debug("auth=" + auth);
		for (int i = 0; i < subList.size(); i++) {
			sub = (BoardMenuVo)subList.get(i);
			log.debug( sub.getMenuId() + "," + sub.getShortTitle() + "," + sub.getActionMask() + "," + ( sub.getActionMask() & auth) );
			if( sub.getMenuType().equals("C")) {
				getBoardList( sub, boardList, auth);
			} else if( ( sub.getActionMask() & auth ) > 0) {
				boardList.add( sub);	
			}
		}
	}
	
	/**  
	 * 처리내용 : 권한에 따른 게시판 메뉴를 조회한다. (카테고리 제외)
	 * @date: 2015.08.31
	 * @author: 나상모(솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일				작성자						변경내용
	 *	---------- ------------------- ---------------------------------------
	 * 2015.08.31 	나상모(솔트웨어) 				최초작성
	 *	-----------------------------------------------------------------------
	 * @param auth 권한
	 *  @return 게시판 메뉴리스트
	 * @exception BaseException
	 */
	public List getBoardMenuList( int auth) {
		List boardList = new ArrayList();
		try {
			BoardMenuVo topMenu = getTopMenu();
			getBoardList( topMenu, boardList, auth);
		} catch (Exception e) {
		}
		
		return boardList;
	}
	
	/**  
	 * 처리내용 : 게시판 갯수를 얻는다. 
	 * @date: 2015.09.14
	 * @author: 나상모(솔트웨어)
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일				작성자						변경내용
	 *	---------- ------------------- ---------------------------------------
	 * 2015.09.14 	나상모(솔트웨어) 				최초작성
	 *	-----------------------------------------------------------------------
	 *  @return 게시판 갯수
	 * @exception BaseException
	 */
	public int getBoardCount() throws BaseException {
		HttpSession session = getRequest().getSession();
		Integer  count = (Integer)session.getAttribute( MENU_SESSION_KEY_PREFIX + "allCount");
		if( count==null) {
			Map paramMap = new HashMap();
			count = boardMenuService.selectAllCount(paramMap);
			session.setAttribute(MENU_SESSION_KEY_PREFIX + "allCount", count);
		}
		
		return count;
	}


	@Override
	public BoardMenuVo getMenu(String menuId) throws BaseException {
		log.debug("*** getMenu " + menuId);
		return getMenu( menuId, "");
	}

	@Override
	public BoardMenuVo getMenuForPortal(String menuId) throws BaseException {
		log.debug("*** getMenuForPortal " + menuId);
		return getMenu( menuId, "Y");
	}
	
}
