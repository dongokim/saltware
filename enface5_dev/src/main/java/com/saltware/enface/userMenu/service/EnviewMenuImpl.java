package com.saltware.enface.userMenu.service;

import java.util.ArrayList;
import java.util.List;

import com.saltware.enhancer.util.StringUtil;
import com.saltware.enview.security.EnviewMenu;
import com.saltware.enview.util.SystemUtil;

public class EnviewMenuImpl implements EnviewMenu, Cloneable
{
    /** parentMenu - parent menu implementation */
    private EnviewMenu parentMenu;

    /** elements - ordered list of menu elements */
    private List elements;

    private String pageId;
    private String parentId;
    private String name;
	private String url;
    private String path;
    private String title;
    private String shortTitle;
    private String target;
    private String skin;
    private boolean hidden;
    private boolean quick;
	private String parameter;
    private String defaultPage;
    private String defaultPagePath;
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
    
	public EnviewMenuImpl()
    {
    	this.contextPath = SystemUtil.getInstance().getContextPath();
    	this.elements = new ArrayList();
    }

	public EnviewMenu getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(EnviewMenu parentMenu) {
		this.parentMenu = parentMenu;
	}
	
	public void addElement(EnviewMenu menu)
	{
		this.elements.add( menu );
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
	
	public String getFullUrl() {
		if( "/".equals(this.contextPath) ) {
			return "/portal" + path + ".page" + getParameter();
		}
		else {
			return this.contextPath + "/portal" + path + ".page" + getParameter();
		}
	}
	
	public String getMenuId() {
		return path.replaceAll("/", "_");
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
		if( parameter != null ) {
			if( parameter.indexOf("?") == -1 ) {
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

	public String getContextPath() { return contextPath; }
	public void setContextPath(String contextPath) { this.contextPath = contextPath; }
	public String getPrincipalId() { return principal_id; }
	public void setPrincipalId(String principal_id) { this.principal_id = principal_id; }
	public String getMenuType() { return menuType; }
	public void setMenuType(String menuType) { this.menuType = menuType; }
	public int getMenuOrder() { return menuOrder; }
	public void setMenuOrder(int menuOrder) { this.menuOrder = menuOrder; }
    public String getLangKnd() { return langKnd; }
	public void setLangKnd(String langKnd) { this.langKnd = langKnd; }
	public String getDomainId() { return domainId; }
	public void setDomainId(String domainId) { this.domainId = domainId; }
	
	
	public String getMenuUrl() {
		if( !StringUtil.isEmpty( url)) {
			return url + getParameter();
		}
		if( !StringUtil.isEmpty( defaultPagePath)) {
			
			return (contextPath.equals("/") ? "" : contextPath) + "/portal/" + defaultPagePath + ".page"  + getParameter(); 
		}
		return getFullUrl();
	}
	
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("[UserMenu] ");
		
		buffer.append(" PrincipalId=\"").append( this.principal_id ).append("\"");
		buffer.append(" PageId=\"").append( this.pageId ).append("\"");
		buffer.append(" ParentId=\"").append( this.parentId ).append("\"");
		buffer.append(" MenuType=\"").append( this.menuType ).append("\"");
		buffer.append(" MenuOrder=\"").append( this.menuOrder ).append("\"");
		buffer.append(" DomainId=\"").append( this.domainId ).append("\"");
		buffer.append(" DeviceType=\"").append( this.deviceType ).append("\"");
		return buffer.toString();      
	}

	public String getDefaultPagePath() {
		return defaultPagePath;
	}

	public void setDefaultPagePath(String defaultPagePath) {
		this.defaultPagePath = defaultPagePath;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
}
