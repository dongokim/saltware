
package com.saltware.enface.userMenu.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.saltware.enface.userMenu.service.EnviewMenuImpl;
import com.saltware.enface.userMenu.service.UserMenuPK;
import com.saltware.enview.security.EnviewMenu;

/**  
 * @Class Name : UserMenuForm.java
 * @Description : 사용자메뉴관리 Form
 * @
 * @author kevin
 * @since 2011.07.26 13:2:324
 * @version 1.0
 * @see 
 * 
 *  Copyright (C) by Saltware All right reserved.
 */
public class UserMenuForm
{
	// primary key
	
	private String principalId = "0";
	private String pageId = "0";
	private String domainId = null;
	
	// fields
	
	private String parentId = "0";
	private String menuType = null;
	private Integer menuOrder = 0;
	
    public UserMenuForm()
	{
	
    }
	
	
	public String getPrincipalId(){ return principalId; }
    public void setPrincipalId(String principalId){ this.principalId=principalId; }			
	public String getPageId(){ return pageId; }
    public void setPageId(String pageId){ this.pageId=pageId; }
    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }
	public String getParentId(){ return parentId; }
    public void setParentId(String parentId){ this.parentId=parentId; }			
	public String getMenuType(){ return menuType; }
    public void setMenuType(String menuType){ if(menuType != null && menuType.length() > 0) this.menuType=menuType; }			
	public Integer getMenuOrder(){ return menuOrder; }
    public void setMenuOrder(Integer menuOrder){ this.menuOrder=menuOrder; }			
	// reference field
	
	
	// search conds
	private int pageNo = 1;
	private int pageSize = 10;
	private String sortColumn = null;
	private String sortMethod = "ASC";

	public Integer getPageNo() { return pageNo; }
	public void setPageNo(Integer pageNo) { if( pageNo != null ) this.pageNo = pageNo.intValue(); }
	public Integer getPageSize() { return pageSize; }
	public void setPageSize(Integer pageSize) { if( pageSize != null ) this.pageSize = pageSize.intValue(); }
	public String getSortColumn() { return sortColumn; }
	public void setSortColumn(String sortColumn) { if( sortColumn != null && sortColumn.length() > 0) this.sortColumn = sortColumn; }
	public String getSortMethod() { return this.sortMethod; }
	public void setSortMethod(String sortMethod) { this.sortMethod = sortMethod; }
	public int getStartRow() { return (pageNo-1) * pageSize + 1; }
	public int getEndRow() { return pageNo * pageSize; }
	
	
	// reference objects
	
	
	// for result information
	private String resultStatus = null;
	private String failureReason = null;
	private int totalSize = 0;
	private int resultSize = 0;

	public String getResultStatus() { return resultStatus; }
	public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }
	public String getFailureReason() { return failureReason; }
	public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
	public int getTotalSize() { return totalSize; }
	public void setTotalSize(int totalSize) { this.totalSize = totalSize; }
	public int getResultSize() { return resultSize; }
	public void setResultSize(int resultSize) { this.resultSize = resultSize; }
	
	// for detail information
	public UserMenuPK getPrimaryKey()
	{
		return new UserMenuPK(this.principalId, this.pageId, this.menuType, this.domainId);
	}
	
	// for retrieve list information
	public Map getSearchCondition()
	{
		Map condition = new HashMap();
		condition.put("pageNo", String.valueOf(this.pageNo));
		condition.put("pageSize", String.valueOf(this.pageSize));
		condition.put("sortColumn", this.sortColumn);
		condition.put("sortMethod", this.sortMethod);
		condition.put("startRow", (this.pageNo-1) * this.pageSize + 1);
		condition.put("endRow", this.pageNo * this.pageSize);
		
		if(domainId != null) condition.put("domainId", domainId);
		
		
	
		return condition;
	}

	// for update or insert information
	public EnviewMenu getUpdateObject()
	{
		EnviewMenu enviewMenu = new EnviewMenuImpl();
		enviewMenu.setPrincipalId( this.principalId );		
		enviewMenu.setPageId( this.pageId );		
		enviewMenu.setParentId( this.parentId );		
		enviewMenu.setMenuType( this.menuType );		
		enviewMenu.setMenuOrder( this.menuOrder );		
		
		return enviewMenu;
	}
	
	/*
	// for remove information
//	private String[] pks;
	private List updateKeyList = null;

	public List getRemoveKeyList() { return (updateKeyList != null) ? updateKeyList : new ArrayList(); }
	public void setPks(String[] pks) {
		if( pks != null && pks.length > 0 ) {
			this.pks = pks;
			this.updateKeyList = new ArrayList();
			for(int i=0; i<pks.length; i++) {
				if( pks[i]!=null && pks[i].length()>0 ) {
					String[] pk = pks[i].split(":");
					UserMenuPK userMenuPK = new UserMenuPK(pk[0], pk[1], pk[2], pk[3]);
        			this.updateKeyList.add(userMenuPK);
				}
			}
		}
	}
    
	public List getUpdateKeyList() { return (updateKeyList != null) ? updateKeyList : new ArrayList(); }
	*/
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("[UserMenu] ");
		
		buffer.append(" PrincipalId=\"").append( this.principalId ).append("\"");
		buffer.append(" PageId=\"").append( this.pageId ).append("\"");
		buffer.append(" ParentId=\"").append( this.parentId ).append("\"");
		buffer.append(" MenuType=\"").append( this.menuType ).append("\"");
		buffer.append(" MenuOrder=\"").append( this.menuOrder ).append("\"");

		return buffer.toString();      
	}
}
