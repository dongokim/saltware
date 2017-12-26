package com.saltware.enface.enboard.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.saltware.enview.security.EnviewMenu;
import com.saltware.enview.util.SystemUtil;

/**
 * 개요 : 게시판 분류와 게시판으로 이루어진 메뉴 Vo
 * 
 * @date: 2015. 8. 10.
 * @author: 나상모(솔트웨어)
 * @history : ----------------------------------------------------------------------- 변경일 작성자 변경내용 ----------- ------------------- --------------------------------------- 2015. 8. 10. 나상모(솔트웨어) 최초작성
 *          -----------------------------------------------------------------------
 */
public class BoardMenuVo implements EnviewMenu, Cloneable, Serializable {

	/**
	 * 상위메뉴
	 */
	private EnviewMenu parentMenu;

	/**
	 * 하위메뉴목록
	 */
	private List elements;

	private String pageId;
	private String parentId;
	private String name;
	private String url;
	private String path;
	private int depth;
	private String title;
	private String shortTitle;
	private String target = "_self";
	private String skin;
	private boolean hidden;
	private boolean quick;
	private String parameter;
	private String defaultPage;
	private String pageInfo01;
	private String pageInfo02;
	private String pageInfo03;

	private String contextPath;
	private String principal_id;
	private String menuType;
	private int menuOrder;
	private String langKnd;
	private String domainId;
	private String deviceType;
	
	int actionMask = 0;

	public BoardMenuVo() {
		this.contextPath = SystemUtil.getInstance().getContextPath();
		this.elements = new ArrayList();
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
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getFullUrl() {
		if ("/".equals(this.contextPath)) {
			return url;
		} else {
			return this.contextPath + url;
		}
	}

	public String getMenuId() {
		return pageId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isQuick() {
		return quick;
	}

	public void setQuick(boolean quick) {
		this.quick = quick;
	}

	public String getParameter() {
		return (parameter != null) ? parameter : "";
	}

	public void setParameter(String parameter) {
		if (parameter != null) {
			if (parameter.indexOf("?") == -1) {
				parameter = "?" + parameter;
			}
		}
		this.parameter = parameter;
	}

	public String getDefaultPage() {
		return defaultPage;
	}

	public void setDefaultPage(String defaultPage) {
		this.defaultPage = defaultPage;
	}

	public String getPageInfo01() {
		return pageInfo01;
	}

	public void setPageInfo01(String pageInfo01) {
		this.pageInfo01 = pageInfo01;
	}

	public String getPageInfo02() {
		return pageInfo02;
	}

	public void setPageInfo02(String pageInfo02) {
		this.pageInfo02 = pageInfo02;
	}

	public String getPageInfo03() {
		return pageInfo03;
	}

	public void setPageInfo03(String pageInfo03) {
		this.pageInfo03 = pageInfo03;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

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
		return langKnd;
	}

	public void setLangKnd(String langKnd) {
		this.langKnd = langKnd;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[BoardMenu] ");

		buffer.append(" PageId=\"").append(this.pageId).append("\"");
		buffer.append(" ParentId=\"").append(this.parentId).append("\"");
		buffer.append(" MenuType=\"").append(this.menuType).append("\"");
		buffer.append(" ShortTitle=\"").append(this.shortTitle).append("\"");
		buffer.append(" Title=\"").append(this.title).append("\"");
		buffer.append(" Url=\"").append(this.url).append("\"");
		buffer.append(" ActionMask=\"").append(this.actionMask).append("\"");
		return buffer.toString();
	}

	public int getActionMask() {
		return actionMask;
	}

	public void setActionMask(int actionMask) {
		this.actionMask = actionMask;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	@Override
	public String getMenuUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultPagePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefaultPagePath(String defaultPagePath) {
		// TODO Auto-generated method stub
		
	}
}
