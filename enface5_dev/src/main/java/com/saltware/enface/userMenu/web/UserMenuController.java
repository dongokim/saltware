package com.saltware.enface.userMenu.web;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.saltware.enface.userMenu.service.UserMenuService;
import com.saltware.enface.userMenu.service.impl.MenuSessionContext;
import com.saltware.enview.Enview;
import com.saltware.enview.code.EnviewCodeManager;
import com.saltware.enview.codebase.CodeBundle;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.login.LoginConstants;
import com.saltware.enview.multiresource.EnviewMultiResourceManager;
import com.saltware.enview.multiresource.MultiResourceBundle;
import com.saltware.enview.security.EnviewMenu;
import com.saltware.enview.session.SessionManager;
import com.saltware.enview.sso.EnviewSSOManager;
import com.saltware.enview.util.EnviewLocale;
import com.saltware.enview.util.StringUtil;

/**
 * 사용자메뉴관리 Controller<br>
 * 사용자메뉴는 마이메뉴라고도 불리운다. @
 * 
 * @author kevin
 * @since 2011.07.26 13:2:324
 * @version 1.0
 * @see Copyright (C) by Saltware All right reserved.
 */
public class UserMenuController extends MultiActionController {
	private final Log log = LogFactory.getLog(getClass());

	private UserMenuService userMenuService;
	private SessionManager enviewSessionManager;

	/**
	 * 사용자메뉴 서비스를 설정한다.
	 * @param userMenuService 사용자메뉴서비스
	 */
	public void setUserMenuService(UserMenuService userMenuService) {
		this.userMenuService = userMenuService;
	}

	/**
	 * 사용자 메뉴 Controller  
	 */
	public UserMenuController() {
		this.enviewSessionManager = (SessionManager) Enview.getComponentManager().getComponent("com.saltware.enview.session.SessionManager");
	}

	/**
	 * Retrieve user My Menu list and response with json format
	 */
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response, UserMenuForm formData) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		String forwardPath = null;

		try {
			String path = request.getParameter("path");
			String orientation = request.getParameter("orientation");
			if ("horizontal".equals(orientation)) {
				forwardPath = "userMenu/topMenu";
			} else if ("vertical".equals(orientation)) {
				forwardPath = "userMenu/leftMenu";
			} else {
				forwardPath = "userMenu/treeMenu";
			}

			Collection results = userMenuService.findByPath(request, path);

			request.setAttribute("results", results);
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String msgKey = be.getMessageKey();
			String errorMessage = be.getMessage();
			if (msgKey != null) {
				errorMessage = enviewMessages.getString(msgKey);
			}
			request.setAttribute("errorMessage", "[" + be.getClass() + "] " + errorMessage);
			formData.setResultStatus("fail");
			formData.setFailureReason("[" + be.getClass() + "] " + errorMessage);
			request.setAttribute("inform", formData);
		}

		return new ModelAndView(forwardPath);
	}

	/**
	 * Retrieve user My Menu list and response with json format
	 */
	public ModelAndView listForAjax(HttpServletRequest request, HttpServletResponse response, UserMenuForm formData) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		String forwardPath = null;

		try {
			String path = request.getParameter("path");
			String orientation = request.getParameter("orientation");
			if ("horizontal".equals(orientation)) {
				forwardPath = "userMenu/topMenu";
			} else if ("vertical".equals(orientation)) {
				forwardPath = "userMenu/leftMenu";
			} else {
				forwardPath = "userMenu/treeMenu";
			}

			Collection results = userMenuService.findByPath(request, path);

			request.setAttribute("results", results);
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String msgKey = be.getMessageKey();
			String errorMessage = be.getMessage();
			if (msgKey != null) {
				errorMessage = enviewMessages.getString(msgKey);
			}
			request.setAttribute("errorMessage", "[" + be.getClass() + "] " + errorMessage);
			formData.setResultStatus("fail");
			formData.setFailureReason("[" + be.getClass() + "] " + errorMessage);
			request.setAttribute("inform", formData);
		}

		return new ModelAndView(forwardPath);
	}

	/**
	 * Retrieve user My Menu list for chooser and response with json format
	 */
	public ModelAndView listForChooser(HttpServletRequest request, HttpServletResponse response, UserMenuForm formData) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		String forwardPath = null;

		try {
			String path = request.getParameter("path");
			String isTree = request.getParameter("tree");
			if ("false".equals(isTree)) {
				forwardPath = "userMenu/menuChooser";
			} else {
				forwardPath = "userMenu/treeMenuChooser";
			}

			log.info("**** path=" + path + ", isTree=" + isTree + ", forwardPath=" + forwardPath);
			Set myMenuSet = new HashSet();
			Collection myMenuList = userMenuService.findUserByType(request, 1);
			for (Iterator it = myMenuList.iterator(); it.hasNext();) {
				myMenuSet.add(((EnviewMenu) it.next()).getPageId());
			}

			Collection results = userMenuService.findByPath(request, path);
			log.info("**** results=" + results.size());

			request.setAttribute("myMenuSet", myMenuSet);
			request.setAttribute("results", results);
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String msgKey = be.getMessageKey();
			String errorMessage = be.getMessage();
			if (msgKey != null) {
				errorMessage = enviewMessages.getString(msgKey);
			}
			request.setAttribute("errorMessage", "[" + be.getClass() + "] " + errorMessage);
			formData.setResultStatus("fail");
			formData.setFailureReason("[" + be.getClass() + "] " + errorMessage);
			request.setAttribute("inform", formData);
		}

		return new ModelAndView(forwardPath);
	}

	/**
	 * Retrieve page children for tree and response with json format
	 */
	public ModelAndView retrieveTreeForAjax(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
		String langKnd = (String) userInfoMap.get("lang_knd");
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		StringBuffer buffer = new StringBuffer();
		String userSite = (String) userInfoMap.get("site_name");

		try {
			String id = request.getParameter("id");
			if (id == null || id.length() == 0) {
				id = userSite;
			}
			id = com.saltware.enface.util.StringUtil.toId( id);
			

			Collection results = userMenuService.findById(request, id);

			Set myMenuMap = new HashSet();
			Collection myMenus = userMenuService.findUserByType(request, MenuSessionContext.MENUTYPE_MY_MENU);
			if (myMenus != null) {
				for (Iterator it = myMenus.iterator(); it.hasNext();) {
					EnviewMenu menu = (EnviewMenu) it.next();
					myMenuMap.add(menu.getPageId());
				}
			}

			request.setAttribute("id", id);
			request.setAttribute("results", results);

			if (id.equals(userSite)) {
				buffer.append("<tree id=\"0\">");
				buffer.append("<item id=\"/\" im0=\"_folder.gif\" im1=\"_folder_open.gif\" im2=\"folder.gif\" open=\"1\" call=\"1\" select=\"1\"><![CDATA[/]]>");
			} else {
				buffer.append("<tree id=\"").append(id).append("\">");
			}
			for (Iterator it = results.iterator(); it.hasNext();) {
				EnviewMenu menu = (EnviewMenu) it.next();
				String isChild = "0";
				String isChecked = "";
				if (menu.getElements() != null && menu.getElements().size() > 0) {
					isChild = "1";
				}
				if (myMenuMap.contains(menu.getPageId())) {
					// isChecked = "checked=\"true\"";
					continue;
				}

				buffer.append("<item child=\"").append(isChild).append("\" id=\"").append(menu.getPath()).append("\" ").append(isChecked)
						.append(" im0=\"page.gif\" im1=\"page.gif\" im2=\"page.gif\" userData=\"").append(menu.getPageId()).append("\"><![CDATA[")
						.append(menu.getShortTitle()).append("]]></item>");
			}
			if (id.equals(userSite)) {
				buffer.append("</item>");
			}
			buffer.append("</tree>");
			log.debug("*** result=" + buffer.toString());

			response.setContentType("text/xml;charset=UTF-8");
			response.getWriter().print(buffer.toString());

		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			throw be;
		}

		return null;
	}

	/**
	 * Retrieve user Menu list and response with json format
	 */
	public ModelAndView listMenuForAjax(HttpServletRequest request, HttpServletResponse response, UserMenuForm formData) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		CodeBundle enviewCodeBundle = EnviewCodeManager.getInstance().getBundle(langKnd);

		try {
			int menuType = MenuSessionContext.MENUTYPE_MY_MENU;
			String menuTypeStr = request.getParameter("menuType");
			if (menuTypeStr != null && menuTypeStr.length() > 0) {
				menuType = Integer.parseInt(menuTypeStr);
			}
			Collection results = userMenuService.findUserByType(request, menuType);

			formData.setResultStatus("success");
			formData.setFailureReason("");
			formData.setTotalSize(results.size());
			formData.setResultSize(results.size());

			request.setAttribute("inform", formData);
			request.setAttribute("results", results);
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String msgKey = be.getMessageKey();
			String errorMessage = be.getMessage();
			if (msgKey != null) {
				errorMessage = enviewMessages.getString(msgKey);
				// log.debug("*** errorMessage=" + errorMessage);
			}
			request.setAttribute("errorMessage", "[" + be.getClass() + "] " + errorMessage);
			formData.setResultStatus("fail");
			formData.setFailureReason("[" + be.getClass() + "] " + errorMessage);
			request.setAttribute("inform", formData);
		}

		return new ModelAndView("userMenu/retrieveForAjax");
	}

	/**
	 * Add user Menu and response with json format
	 */
	public ModelAndView addMenuForAjax(HttpServletRequest request, HttpServletResponse response, UserMenuForm formData) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);

		try {
			int menuType = MenuSessionContext.MENUTYPE_MY_MENU;
			String menuTypeStr = request.getParameter("menuType");
			if (menuTypeStr != null && menuTypeStr.length() > 0) {
				menuType = Integer.parseInt(menuTypeStr);
			}

			String principalType = request.getParameter("principalType");
			if (principalType == null || principalType.length() == 0) {
				principalType = "U";
			}

			String pageIds = request.getParameter("pageIds");
			//userMenuService.insert(request, principalType, menuType, pageIds);
			userMenuService.setMyMenu(request, principalType, menuType, pageIds);

			Collection myMenuList = userMenuService.findUserByType(request, 1);
			log.info("**** myMenuList=" + myMenuList);

			request.setAttribute("results", myMenuList);
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
				errorMessage = enviewMessages.getString(msgKey);
			}
		}

		return new ModelAndView("userMenu/listMyMenu");
	}

	/**
	 * Change user My Menu order information and response with json format
	 */
	public ModelAndView changeMenuOrderForAjax(HttpServletRequest request, HttpServletResponse response, UserMenuForm formData) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);

		try {
			int menuType = MenuSessionContext.MENUTYPE_MY_MENU;
			String menuTypeStr = request.getParameter("menuType");
			if (menuTypeStr != null && menuTypeStr.length() > 0) {
				menuType = Integer.parseInt(menuTypeStr);
			}
			String toDownStr = request.getParameter("toDown");
			boolean toDown = true;
			if (toDownStr != null && toDownStr.length() > 0) {
				toDown = Boolean.parseBoolean(toDownStr);
			}
			String principalType = request.getParameter("principalType");
			if (principalType == null || principalType.length() == 0) {
				principalType = "U";
			}

			String pageId = request.getParameter("pageId");

			userMenuService.changeOrder(request, principalType, menuType, pageId, toDown);

			Collection menuList = userMenuService.findUserByType(request, menuType);

			request.setAttribute("results", menuList);
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
				errorMessage = enviewMessages.getString(msgKey);
			}
		}

		return new ModelAndView("userMenu/listMyMenu");
	}

	/**
	 * Change user My Page order information and response with json format
	 */
	public ModelAndView changeMyPageOrderForAjax(HttpServletRequest request, HttpServletResponse response, UserMenuForm formData) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);

		try {
			String toDownStr = request.getParameter("toDown");
			boolean toDown = true;
			if (toDownStr != null && toDownStr.length() > 0) {
				toDown = Boolean.parseBoolean(toDownStr);
			}

			String pageId = request.getParameter("pageId");

			userMenuService.changeMyPageOrder(request, pageId, toDown);

			Collection myPageList = (List) this.userMenuService.findMyPageList(request);

			request.setAttribute("myPageList", myPageList);
			request.setAttribute("logoutUrl", Enview.getConfiguration().getString(LoginConstants.SSO_LOGOUT_DESTINATION));
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
				errorMessage = enviewMessages.getString(msgKey);
			}
		}

		return new ModelAndView("userMenu/listMyPage");
	}

	/**
	 * Remove user My Menu information and response with json format
	 */
	public ModelAndView removeMenuForAjax(HttpServletRequest request, HttpServletResponse response, UserMenuForm formData) throws Exception {

		try {
			int menuType = MenuSessionContext.MENUTYPE_MY_MENU;
			String menuTypeStr = request.getParameter("menuType");
			if (menuTypeStr != null && menuTypeStr.length() > 0) {
				menuType = Integer.parseInt(menuTypeStr);
			}
			String principalType = request.getParameter("principalType");
			if (principalType == null || principalType.length() == 0) {
				principalType = "U";
			}

			userMenuService.delete(request, principalType, menuType, formData.getPageId(), formData.getDomainId());

			Collection menuList = userMenuService.findUserByType(request, menuType);

			request.setAttribute("results", menuList);
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
				MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
				errorMessage = enviewMessages.getString(msgKey);
			}
		}

		return new ModelAndView("userMenu/listMyMenu");
	}
}
