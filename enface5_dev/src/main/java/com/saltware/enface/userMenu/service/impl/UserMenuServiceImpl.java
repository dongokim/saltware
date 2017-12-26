package com.saltware.enface.userMenu.service.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

import com.saltware.enface.userMenu.service.UserMenuService;
import com.saltware.enview.Enview;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.idgenerator.IdGenerator;
import com.saltware.enview.login.LoginConstants;
import com.saltware.enview.security.EnviewMenu;
import com.saltware.enview.security.UserInfo;
import com.saltware.enview.session.SessionConstants;
import com.saltware.enview.session.SessionManager;
import com.saltware.enview.sso.EnviewSSOManager;

/**
 * @Class Name : UserMenuServiceImpl.java
 * @Description : 사용자메뉴관리 Service Implementation @
 * @author kevin
 * @since 2011.07.26 13:2:324
 * @version 1.0
 * @see Copyright (C) by Saltware All right reserved.
 */
public class UserMenuServiceImpl implements UserMenuService {
	private final Log log = LogFactory.getLog(getClass());
	private IdGenerator idGenerator = null;
	private SessionManager enviewSessionManager;

	MenuSessionContext guestMenuSessionContext = null;

	public UserMenuServiceImpl() {
		this.idGenerator = (IdGenerator) Enview.getComponentManager().getComponent("IdGenerator");
		this.enviewSessionManager = (SessionManager) Enview.getComponentManager().getComponent("com.saltware.enview.session.SessionManager");
	}

	private UserMenuDAO userMenuDAO;

	public void setUserMenuDAO(UserMenuDAO userMenuDAO) {
		this.userMenuDAO = userMenuDAO;
	}

	private MenuSessionContext getMenuSessionContext(HttpServletRequest request) throws BaseException {
		MenuSessionContext menuSessionContext = null;

		try {
			HttpSession session = request.getSession(false);
			if (session != null) {
				String userId = (String) session.getAttribute(LoginConstants.SSO_LOGIN_ID);
				if (userId == null) {
					if (guestMenuSessionContext != null) {
						menuSessionContext = guestMenuSessionContext;
					} else {
						guestMenuSessionContext = menuSessionContext = new MenuSessionContext(this.userMenuDAO);
						menuSessionContext.setUserId("guest");
						menuSessionContext.setDomainId( EnviewSSOManager.getDomainInfo(request).getDomainId());
						menuSessionContext.setLangKnd( EnviewSSOManager.getLangKnd(request));
						menuSessionContext.invalidateCache();
					}
				} else {
					menuSessionContext = (MenuSessionContext) session.getAttribute(SessionConstants.USERMENU_CONTEXT);
					if (menuSessionContext == null) {
						menuSessionContext = new MenuSessionContext(this.userMenuDAO);
						Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
						if (userInfoMap != null) {
							menuSessionContext.setUserId((String) userInfoMap.get("user_id"));
							menuSessionContext.setPrincipalId((String) userInfoMap.get("principal_id"));
							menuSessionContext.setGroupPrincipalId((String) userInfoMap.get("groupPrincipalId"));
							menuSessionContext.setGroupId((String) userInfoMap.get("groupId"));
							menuSessionContext.setDomainId((String) userInfoMap.get("domainId"));
							menuSessionContext.setLangKnd((String) userInfoMap.get("lang_knd"));
							menuSessionContext.setAdmin( ((List)userInfoMap.get("roles")).contains(UserInfo.ADMIN_ROLE));
							menuSessionContext.invalidateCache();
							session.setAttribute(SessionConstants.USERMENU_CONTEXT, menuSessionContext);
						} else {
							throw new BaseException("UserInfoMap is null !!!");
						}
					}
				}
			} else {
				throw new BaseException("Session is expired !!!");
			}
		} catch (BaseException e) {
			throw e;
		}

		return menuSessionContext;
	}

	/**
	 * Guest의 메뉴캐시 정보를 다시 초기화 한다.
	 * 
	 * @return void
	 * @exception BaseException
	 */
	public void invalidateCache() throws BaseException {
		// if( guestMenuSessionContext != null ) {
		//
		// }
	}

	/**
	 * Guest의 메뉴캐시 정보를 다시 초기화 한다.
	 * 
	 * @return void
	 * @exception BaseException
	 */
	public void invalidateCache(HttpServletRequest request) throws BaseException {
		getMenuSessionContext(request).invalidateCache();
	}

	/**
	 * Retrieve child user Menu list by pageId
	 * 
	 * @param request
	 *            - HttpServletRequest
	 * @param pageId
	 *            - Page ID
	 * @return Data Map Collection
	 * @exception BaseException
	 */
	public Collection findById(HttpServletRequest request, String pageId) throws BaseException {
		return getMenuSessionContext(request).getChildrenById(pageId);
	}

	/**
	 * Retrieve child user Menu list by path
	 * 
	 * @param request
	 *            - HttpServletRequest
	 * @param pagePath
	 *            - Page path
	 * @return Data Map Collection
	 * @exception BaseException
	 */
	public Collection findByPath(HttpServletRequest request, String pagePath) throws BaseException {
		return getMenuSessionContext(request).getChildrenByPath(pagePath);
	}

	/**
	 * Retrieve user Menu list by menuType
	 * 
	 * @param request
	 *            - HttpServletRequest
	 * @param menuType
	 *            - Menu Type (mainMenu : 0, myMenu : 1, myQuickMenu : 2)
	 * @return Data Map Collection
	 * @exception BaseException
	 */
	public Collection findUserByType(HttpServletRequest request, int menuType) throws BaseException {
		return getMenuSessionContext(request).getMenuList("U", menuType);
	}

	/**
	 * Retrieve group Menu list by menuType
	 * 
	 * @param request
	 *            - HttpServletRequest
	 * @param menuType
	 *            - Menu Type (mainMenu : 0, myMenu : 1, myQuickMenu : 2)
	 * @return Data Map Collection
	 * @exception BaseException
	 */
	public Collection findGroupByType(HttpServletRequest request, int menuType) throws BaseException {
		return getMenuSessionContext(request).getMenuList("G", menuType);
	}

	/**
	 * 마이페이지 목록을 얻는다.
	 * 
	 * @param request
	 *            - HttpServletRequest
	 * @return List (EnviewMenu)
	 * @exception BaseException
	 */
	public List findMyPageList(HttpServletRequest request) throws BaseException {
		return getMenuSessionContext(request).getMyPageList();
	}

	/**
	 * 그룹페이지 목록을 얻는다.
	 * 
	 * @param request
	 *            - HttpServletRequest
	 * @return List (EnviewMenu)
	 * @exception BaseException
	 */
	public List findGroupPageList(HttpServletRequest request) throws BaseException {
		return getMenuSessionContext(request).getGroupPageList();
	}

	/**
	 * Get root menu
	 * 
	 * @param request
	 *            - HttpServletRequest
	 * @return EnviewMenu
	 * @exception BaseException
	 */
	public EnviewMenu getRootMenu(HttpServletRequest request) throws BaseException {
		return getMenuSessionContext(request).getRootMenu();
	}

	/**
	 * Get quick menu list
	 * 
	 * @param request
	 *            - HttpServletRequest
	 * @return EnviewMenu
	 * @exception BaseException
	 */
	public List getQuickMenuList(HttpServletRequest request) throws BaseException {
		return getMenuSessionContext(request).getQuickMenuList();
	}

	/**
	 * Check exist userMenu by name
	 * 
	 * @param VO
	 *            - modify data EnviewMenu
	 * @return boolean
	 * @exception DataAccessException
	 */
	public boolean exist(EnviewMenu enviewMenu) throws BaseException {
		return userMenuDAO.exist(enviewMenu);
	}

	/**
	 * Add new user Menu list information
	 * 
	 * @param paramMap
	 *            - paramMap(principal_id, domainId)
	 * @param pageList
	 *            - page to add userMenus
	 * @return void
	 * @exception DataAccessException
	 */
	public void insert(Map paramMap, List pageList) throws BaseException {
		try {
			userMenuDAO.insertList(paramMap, pageList);
		} catch (SQLException e) {
			throw new BaseException(e);
		}
	}

	/**
	 * Remove all user Menu and add new user Menu list information
	 * 
	 * @param request
	 *            - HttpServletRequest
	 * @param menuType
	 *            - Menu Type (mainMenu : 0, myMenu : 1, myQuickMenu : 2)
	 * @param pageIds
	 *            - pageId string with comma
	 * @return void
	 * @exception DataAccessException
	 */
	public void insert(HttpServletRequest request, String principalType, int menuType, String pageIds) throws BaseException {
		getMenuSessionContext(request).addMenuList(principalType, menuType, pageIds);
	}

	/**
	 * Remove all user Menu and add new user Menu list information
	 * 
	 * @param request
	 *            - HttpServletRequest
	 * @param menuType
	 *            - Menu Type (mainMenu : 0, myMenu : 1, myQuickMenu : 2)
	 * @param pageIds
	 *            - pageId string with comma
	 * @return void
	 * @exception DataAccessException
	 */
	public void setMyMenu(HttpServletRequest request, String principalType, int menuType, String pageIds) throws BaseException {
		getMenuSessionContext(request).setMenuList(principalType, menuType, pageIds);
	}

	/**
	 * 해당하는 메뉴를 주어진 위치로 이동 시키고 전체를 다시 재 정렬 한다.
	 * 
	 * @param request
	 *            - HttpServletRequest
	 * @param menuType
	 *            - Menu Type (mainMenu : 0, myMenu : 1, myQuickMenu : 2)
	 * @param pageId
	 *            - pageId string
	 * @param toDown
	 *            - Sorting direction
	 * @return void
	 * @exception DataAccessBaseException
	 */
	public void changeOrder(HttpServletRequest request, String principalType, int menuType, String pageId, boolean toDown) throws BaseException {
		getMenuSessionContext(request).changeOrder(principalType, menuType, pageId, toDown);
	}

	/**
	 * 마이페이지 목록 중 주어진 위치로 이동 시키고 전체를 다시 재 정렬 한다.
	 * 
	 * @param request
	 *            - HttpServletRequest
	 * @param pageId
	 *            - pageId string
	 * @param toDown
	 *            - Sorting direction
	 * @return void
	 * @exception DataAccessBaseException
	 */
	public void changeMyPageOrder(HttpServletRequest request, String pageId, boolean toDown) throws BaseException {
		getMenuSessionContext(request).changeMyPageOrder(pageId, toDown);
	}

	/**
	 * Remove user Menu information
	 * 
	 * @param request
	 *            - HttpServletRequest
	 * @param menuType
	 *            - Menu Type (mainMenu : 0, myMenu : 1, myQuickMenu : 2)
	 * @param pageIds
	 *            - pageId string with comma
	 * @return void
	 * @exception BaseException
	 */
	public void delete(HttpServletRequest request, String principalType, int menuType, String pageId, String domainId) throws BaseException {
		getMenuSessionContext(request).deleteMenuList(principalType, menuType, pageId, domainId);
	}

	/**
	 * rootPath까지 페이지 경로에 해당하는 EnviewMenu목록을 얻는다.
	 * 
	 * @param request
	 *            - HttpServletRequest
	 * @param rootPath
	 *            - 최종 위치
	 * @param requestPath
	 *            - 현재 페이지위치
	 * @return List (EnviewMenu)
	 * @exception BaseException
	 */
	public List getNavigation(HttpServletRequest request, String principalType, String rootPath, String requestPath) throws BaseException {
		return getMenuSessionContext(request).getNavigation(principalType, rootPath, requestPath);
	}

}
