
package com.saltware.enface.page.web;

import java.util.HashMap;
import java.util.Map;

/**  
 * 사용자 페이지관리 Form
 * @author kevin
 * @since 2011.07.26 13:2:324
 * @version 1.0
 */
public class PageForm
{
	private Integer principalId = 0;
	private Integer pageId = 0;
	private Integer pageOrder = 0;
	private String path = null;
	private String title = null;
	private String theme = null;
	private String templatePath = null;
	private String pageLayout = null;

	/**
	 * 사용자 페이지관리 Form 생성자
	 */
	public PageForm()
	{
	
    }

	/**
	 * principal id를 리턴한다
	 * @return principal id
	 */
	public Integer getPrincipalId(){ return principalId; }

	/**
	 * pricipal id를 설정한다.
	 * @param principalId
	 */
    public void setPrincipalId(Integer principalId){ this.principalId=principalId; }
    
    /**
     * 페이지 id를 리턴한다.
     * @return 페이지 id
     */
	public Integer getPageId(){ return pageId; }
	
	/**
	 * 페이지 id를 설정한다.
	 * @param pageId 페이지 id
	 */
    public void setPageId(Integer pageId){ this.pageId=pageId; }
    
    /**
     * 페이지 순서를 리턴한다.
     * @return 페이지 순서
     */
	public Integer getPageOrder() { return pageOrder; }
	
	/**
	 * 페이지 순서를 설정한다.
	 * @param pageOrder 페이지순서
	 */
	public void setPageOrder(Integer pageOrder) { this.pageOrder = pageOrder; }
	
	/**
	 * 경로를 리턴한다. 
	 * @return 경로
	 */
	public String getPath() { return path; }
	
	/**
	 * 경로를 설정한다.
	 * @param path
	 */
	public void setPath(String path) { this.path = path; }
	
	/**
	 * 제목을 리턴한다.
	 * @return 제목
	 */
	public String getTitle() { return title; }
	
	/**
	 * 제목을 설정한다.
	 * @param title 제목
	 */
	public void setTitle(String title) { this.title = title; }
	
	/**
	 * 테마를 리턴한다.
	 * @return 테마
	 */
	public String getTheme() { return theme; }
	
	/**
	 * 테마를 설정한다.
	 * @param theme 테마
	 */
	public void setTheme(String theme) { this.theme = theme; }
	
	/**
	 * 템플릿 경로를 리턴한다.
	 * @return 템플릿 경로
	 */
	public String getTemplatePath() { return templatePath; }
	
	/**
	 * 템플릿 경로를 설정한다 
	 * @param templatePath 템플릿 경로
	 */
	public void setTemplatePath(String templatePath) { this.templatePath = templatePath; }
	
	/**
	 * 페이지 레이아웃을 리턴한다.
	 * @return 페이지 레이아웃
	 */
	public String getPageLayout() { return pageLayout; }
	
	/**
	 * 페이지 레이아웃을 설정한다.
	 * @param pageLayout
	 */
	public void setPageLayout(String pageLayout) { this.pageLayout = pageLayout; }

	private int pageNo = 1;
	private int pageSize = 10;
	private String sortColumn = null;
	private String sortMethod = "ASC";
	private String title0Cond = null;
	private String selectPortletIds = null;
	private String portletCategory = null;

	/**
	 * 검색조건 : 페이지번호를 리턴한다.(1부터 시작)
	 * @return 페이지 번호
	 */
	public Integer getPageNo() { return pageNo; }
	
	/**
	 * 검색조건 : 페이지 번호를 설정한다. (1부터 시작)
	 * @param pageNo 페이지 번호
	 */
	public void setPageNo(Integer pageNo) { if( pageNo != null ) this.pageNo = pageNo.intValue(); }
	
	/**
	 * 검색조건 : 페이지 크기를 리턴한다.<br> 
	 * 페이지 크기는 한페이지에 표시되는 데이터 갯수를 의미한다.
	 * @return 페이지 크기
	 */
	public Integer getPageSize() { return pageSize; }
	
	/**
	 * 검색조건 : 페이지 크기를 설정한다.<br>
	 * 페이지 크기는 한페이지에 표시되는 데이터 갯수를 의미한다.
	 * @param pageSize 페이지 크기
	 */
	public void setPageSize(Integer pageSize) { if( pageSize != null ) this.pageSize = pageSize.intValue(); }
	
	/**
	 * 검색조건 : 정렬 컬럼을 리턴한다.
	 * @return 정렬 컬럼
	 */
	public String getSortColumn() { return sortColumn; }
	
	/**
	 * 검색조건 : 정렬 컬럼을 설정한다. 
	 * @param sortColumn
	 */
	public void setSortColumn(String sortColumn) { if( sortColumn != null && sortColumn.length() > 0) this.sortColumn = sortColumn; }
	
	/**
	 * 검색조건 : 정렬 방법을 리턴한다(ASC/DESC)
	 * @return 정렬 방법
	 */
	public String getSortMethod() { return this.sortMethod; }
	
	/**
	 * 검색조건 : 정렬 방법을 설정한다(ASC/DESC)
	 * @param sortMethod 정렬 방법
	 */
	public void setSortMethod(String sortMethod) { this.sortMethod = sortMethod; }
	
	/**
	 * 검색조건 : 시작 ROW번호를 리턴한다.<br> 
	 * ( 페이지 번호 - 1 ) * 페이지 크기 + 1로 게산된다.
	 * @return 시작 ROW번호
	 */
	public int getStartRow() { return (pageNo-1) * pageSize + 1; }
	
	
	/**
	 * 검색조건 : 끝 ROW번호를 리턴한다.<br> 
	 * 페이지 번호 * 페이지 크기로 게산된다.
	 * @return 끝 ROW번호
	 */
	public int getEndRow() { return pageNo * pageSize; }
	
	/**
	 * 검색조건 : 타이틀 검색 조건 값을 리턴한다.
	 * @return 타이틀 검색 조건 값
	 */
    public String getTitle0Cond() { return title0Cond; }
    /**
	 * 검색조건 : 타이틀 검색 조건 값을 설정한다.
     * @param title0Cond 타이틀 검색 조건 값 
     */
	public void setTitle0Cond(String title0Cond) { this.title0Cond = title0Cond; }
	
	/**
	 * 검색조건 : 선택된 포틀릿 ID리스트를 리턴한다.
	 * @return 선택된 포틀릿 ID리스트
	 */
	public String getSelectPortletIds() { return selectPortletIds; }
	
	/**
	 * 검색조건 : 선택된 포틀릿 ID리스트를 설정한다.
	 * @param selectPortletIds 선택된 포틀릿 ID리스트
	 */
	public void setSelectPortletIds(String selectPortletIds) { this.selectPortletIds = selectPortletIds; }
	
	/**
	 * 검색조건 : 포틀릿 카테고리를 리턴한다.
	 * @return 포틀릿 카테고리
	 */
	public String getPortletCategory() { return portletCategory; }
	

	/**
	 * 검색조건 : 포틀릿 카테고리를 설정한다.
	 * @param portletCategory 포틀릿 카테고리
	 */
	public void setPortletCategory(String portletCategory) { this.portletCategory = portletCategory; }


	/**
	 */
	private String resultStatus = null;
	private String failureReason = null;
	private int totalSize = 0;
	private int resultSize = 0;

	/**
	 * 결과 : 결과상태를 리턴한다.
	 * @return 결과상태
	 */
	public String getResultStatus() { return resultStatus; }
	
	/**
	 * 결과 : 결과상태를 설정한다.
	 * @param resultStatus 결과상태
	 */
	public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }
	
	/**
	 * 결과 : 실패사유를 리턴한다. 
	 * @return 실패사유
	 */
	public String getFailureReason() { return failureReason; }
	
	/**
	 * 결과 : 실패사유를 설정한다.
	 * @param failureReason  실패사유
	 */
	public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
	
	/**
	 * 결과 : 조회 조건에 맞는전체 크기를 리턴한다.
	 * @return 전체 크기
	 */
	public int getTotalSize() { return totalSize; }
	
	/**
	 * 결과 : 조건에 맞는전체 크기를 설정한다.
	 * @param totalSize 전체 크기
	 */
	public void setTotalSize(int totalSize) { this.totalSize = totalSize; }
	
	/**
	 * 결과 : 현재 조회된 결과 크기를 리턴한다.
	 * @return 결과 크기
	 */
	public int getResultSize() { return resultSize; }
	
	/**
	 * 결과 : 현재 조회된 결과 크기를 설정한다.
	 * @param resultSize 결과 크기
	 */
	public void setResultSize(int resultSize) { this.resultSize = resultSize; }
	
	/**
	 * 조회조건을 MAP으로 리턴한다.
	 * @return 조회조건맵
	 */
	public Map getSearchCondition()
	{
		Map condition = new HashMap();
		condition.put("pageNo", String.valueOf(this.pageNo));
		condition.put("pageSize", String.valueOf(this.pageSize));
		condition.put("sortColumn", this.sortColumn);
		condition.put("sortMethod", this.sortMethod);
		condition.put("startRow", (this.pageNo-1) * this.pageSize + 1);
		condition.put("endRow", this.pageNo * this.pageSize);
		if( this.portletCategory != null ) {
			condition.put("portletCategory", this.portletCategory);
		}
		if( this.title0Cond != null ) {
			condition.put("title0Cond", this.title0Cond);
		}
	
		return condition;
	}

	/**
	 * 내용을 문자열로 변환한다.
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("[Page] ");
		
		buffer.append(" PrincipalId=\"").append( this.principalId ).append("\"");
		buffer.append(" PageId=\"").append( this.pageId ).append("\"");

		return buffer.toString();      
	}
}
