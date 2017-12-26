package com.saltware.enface.userMenu.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enhancer.util.StringUtil;
import com.saltware.enview.Enview;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.om.page.Page;
import com.saltware.enview.page.PageManager;
import com.saltware.enview.security.EnviewMenu;
import com.saltware.enview.sso.EnviewSSOManager;
import com.saltware.enview.util.SystemUtil;

/**
 * EnviewMenu 경량화 구현체 기존 Enview메뉴가 메뉴 당 900바이트 내외의 메모리를 점유하여 400바이트 내외의 메모리를 사용하는 경량구현체를 새로 제작
 * 
 * @author smna
 * 
 */

public class EnviewMenuLightWeightImpl implements EnviewMenu, Cloneable {

	private static PageManager pageManager = null;
	private static Method getPageMethod = null;
	private static boolean getPageById = false;
	private final Log log = LogFactory.getLog(getClass());

	private static PageManager getPageManager() {
		if (pageManager == null) {
			pageManager = (PageManager) Enview.getComponentManager().getComponent("com.saltware.enview.page.PageManager");
		}
		return pageManager;
	}

	/** parentMenu - parent menu implementation */
	private EnviewMenu parentMenu;

	/** elements - ordered list of menu elements */
	private List elements;

	private int pageId;
//	private String path;
//	//
	private String principal_id;
	private String menuType;
	private int menuOrder;
	private String domainId;
	private String deviceType;
	private Locale locale;

	public EnviewMenuLightWeightImpl() {
		this.elements = new ArrayList();
	}

	public EnviewMenuLightWeightImpl(EnviewMenu menu) {
		this();
		// init PageManager
		// getPage();

		pageId = Integer.parseInt(menu.getPageId());

		principal_id = menu.getPrincipalId();
		menuType = menu.getMenuType();
		menuOrder = menu.getMenuOrder();
		locale = new Locale(menu.getLangKnd());
		domainId = menu.getDomainId();
		deviceType = menu.getDeviceType();
	}

	private Page getPage() {
		try {
			if (pageManager == null) {
				pageManager = (PageManager) Enview.getComponentManager().getComponent("com.saltware.enview.page.PageManager");
			}
			return pageManager.getPage(pageId);
		} catch (BaseException e) {
			log.error("Failed to getPage " + pageId);
//			log.error( e,e);
			return null;
		}
	}

	private Locale getLocale() {
		return locale;
	}

	public EnviewMenu getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(EnviewMenu parentMenu) {
		this.parentMenu = parentMenu;
	}

	public void addElement(EnviewMenu menu) {
		this.elements.add(menu);
	}

	public List getElements() {
		return elements;
	}

	public void setElements(List elements) {
		this.elements = elements;
	}

	public String getPageId() {
		return String.valueOf(pageId);
	}

	public void setPageId(String pageId) {
		// this.pageId = pageId;
	}

	public String getParentId() {
		return getPage().getParentId();
	}

	public void setParentId(String parentId) {
		// this.parentId = parentId;
	}

	public String getName() {
		return getPage().getName();
	}

	public void setName(String name) {
		// this.name = name;
	}

	public String getUrl() {
		return getPage().getTargetUrl();
	}

	public void setUrl(String url) {
		// this.url = url;
	}

	public String getPath() {
		return getPage().getPath();
	}

	public void setPath(String path) {
		//this.path = path;
	}

	public String getFullUrl() {
		String contextPath = SystemUtil.getInstance().getContextPath();
		if ("/".equals(contextPath)) {
			return "/portal" + getPath() + ".page" + getParameter();
		} else {
			return contextPath + "/portal" + getPath() + ".page" + getParameter();
		}
	}

	public String getMenuId() {
		return getPath().replaceAll("/", "_");
	}

	public String getTitle() {
		return getPage().getTitle(getLocale());
	}

	public void setTitle(String title) {
		// this.title = title;
	}

	public String getShortTitle() {
		return getPage().getShortTitle(getLocale());
	}

	public void setShortTitle(String shortTitle) {
		// this.shortTitle = shortTitle;
	}

	public String getTarget() {
		return getPage().getTarget();
	}

	public void setTarget(String target) {
		// this.target = target;
	}

	public String getSkin() {
		return getPage().getSkin();
	}

	public void setSkin(String skin) {
		// this.skin = skin;
	}

	public boolean isHidden() {
		return getPage().isHidden();
	}

	public void setHidden(boolean hidden) {
		// this.hidden = hidden;
	}

	public boolean isQuick() {
		return getPage().isQuickMenu();
	}

	public void setQuick(boolean quick) {
		// this.quick = quick;
	}

	public String getParameter() {
		return StringUtil.isNull2(getPage().getParameter(), "");
	}

	public void setParameter(String parameter) {
		// if( parameter != null ) {
		// if( parameter.indexOf("?") == -1 ) {
		// parameter = "?" + parameter;
		// }
		// }
		// this.parameter = parameter;
	}

	public String getDefaultPage() {
		return getPage().getDefaultPage();
	}

	public void setDefaultPage(String defaultPage) {
		// this.defaultPage = defaultPage;
	}

	public String getPageInfo01() {
		return getPage().getPageInfo01();
	}

	public void setPageInfo01(String pageInfo01) {
		// this.pageInfo01 = pageInfo01;
	}

	public String getPageInfo02() {
		return getPage().getPageInfo02();
	}

	public void setPageInfo02(String pageInfo02) {
		// this.pageInfo02 = pageInfo02;
	}

	public String getPageInfo03() {
		return getPage().getPageInfo03();
	}

	public void setPageInfo03(String pageInfo03) {
		// this.pageInfo03 = pageInfo03;
	}

	// public String getContextPath() { return contextPath; }
	// public void setContextPath(String contextPath) { this.contextPath = contextPath; }
	public String getPrincipalId() {
		return principal_id;
	}

	public void setPrincipalId(String principal_id) {
		this.principal_id = principal_id;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public int getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(int menuOrder) {
		this.menuOrder = menuOrder;
	}

	public String getLangKnd() {
		return locale.getLanguage();
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getMenuUrl() {
		String url = getUrl();
		if (!StringUtil.isEmpty(url)) {
			return url + getParameter();
		}

		String contextPath  = SystemUtil.getInstance().getContextPath();
		String defaultPagePath = getDefaultPagePath();
		if (!StringUtil.isEmpty(defaultPagePath)) {
			return (contextPath.equals("/") ? "" : contextPath) + "/portal/" + defaultPagePath + ".page" + getParameter();
		}
		return getFullUrl();
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[UserMenu] ");

		buffer.append(" PrincipalId=\"").append(this.principal_id).append("\"");
		buffer.append(" PageId=\"").append(this.pageId).append("\"");
		buffer.append(" ParentId=\"").append(getPageId()).append("\"");
		buffer.append(" MenuType=\"").append(this.menuType).append("\"");
		buffer.append(" MenuOrder=\"").append(this.menuOrder).append("\"");
		buffer.append(" DomainId=\"").append(this.domainId).append("\"");
		buffer.append(" DeviceType=\"").append(this.deviceType).append("\"");
		return buffer.toString();
	}

	public String getDefaultPagePath() {
		return getPage().getDefaultPagePath();
	}

	public void setDefaultPagePath(String defaultPagePath) {
		// this.defaultPagePath = defaultPagePath;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public void setLangKnd(String langKnd) {
		locale = new Locale(langKnd);
	}
}
