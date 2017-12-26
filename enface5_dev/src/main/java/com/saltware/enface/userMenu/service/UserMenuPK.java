
package com.saltware.enface.userMenu.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.saltware.enview.util.DateUtil;

/**  
 * @Class Name : UserMenuPK.java
 * @Description : 사용자메뉴관리 Primary Key Object
 * @
 * @author kevin
 * @since 2011.07.26 13:2:324
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by Saltware All right reserved.
 */
public class UserMenuPK
{
	// primary key
	
	private String principalId = "0";
	private String pageId = "0";
	private String menuType = "1";
	private String domainId = "0";

	public UserMenuPK()
	{
	
    }
	
    public UserMenuPK(String principalId, String pageId, String menuType, String domainId)
	{
		this();
		this.principalId = principalId;
		this.pageId = pageId;
		this.menuType = menuType;
		this.domainId = domainId;
    }
	
	public String getPrincipalId(){ return principalId; }
    public void setPrincipalId(String principalId){ this.principalId=principalId; }			
	public String getPageId(){ return pageId; }
    public void setPageId(String pageId){ this.pageId=pageId; }			
    public String getMenuType() { return menuType; }
	public void setMenuType(String menuType) { this.menuType = menuType; }
	public String getDomainId(){ return this.domainId; }
	public void setDomainId(String domainId) { this.domainId = domainId; }

	// master key
	

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("[UserMenu] ");
		
		buffer.append(" PrincipalId=\"").append( this.principalId ).append("\"");
		buffer.append(" PageId=\"").append( this.pageId ).append("\"");
		buffer.append(" menuType=\"").append( this.menuType ).append("\"");
		buffer.append(" domainId=\"").append( this.domainId ).append("\"");
		return buffer.toString();      
	}
}
