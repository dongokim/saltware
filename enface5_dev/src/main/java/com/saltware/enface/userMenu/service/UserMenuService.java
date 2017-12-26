
package com.saltware.enface.userMenu.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;

import com.saltware.enview.exception.BaseException;
import com.saltware.enview.security.CommonUserMenu;
import com.saltware.enview.security.EnviewMenu;


/**  
 * @Class Name : UserMenuService.java
 * @Description : 사용자메뉴관리 Service Interface
 * @
 * @author kevin
 * @since 2011.07.26 13:2:324
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by Saltware All right reserved.
 */
public interface UserMenuService extends CommonUserMenu
{
	
	/**
	 * 페이지ID를 부모로 하는 하위 페이지들을 조회한다.
	 * 주로 트리메뉴 구성 시 '+'버튼을 클릭 했을 때 사용된다. 
	 * 
	 * @param request - HttpServletRequest
	 * @param pageId - Page ID
	 * @return Data Map Collection
	 * @exception BaseException
	 */
	public Collection findById(HttpServletRequest request, String pageId) throws BaseException;
	
	/**
	 * 사용자 메뉴의 존재여부를 확인한다.
	 * 
	 * @param VO - modify data UserMenuVO
	 * @return boolean
	 * @exception DataAccessBaseException
	 */
	public boolean exist(EnviewMenu userMenu) throws BaseException;
	
	/**
	 * 해당하는 메뉴 타입에 주어진 페이지 갯수 만큼 추가한다.
	 * 
	 * @param request - HttpServletRequest
	 * @param principalType - U/G/R
	 * @param menuType - Menu Type (mainMenu : 0, myMenu : 1, myQuickMenu : 2)
	 * @param pageIds - pageId string with comma
	 * @return void
	 * @exception DataAccessException
	 */
	public void insert(HttpServletRequest request, String principalType, int menuType, String pageIds) throws BaseException;
	
	/**
	 * 해당하는 메뉴 타입에 주어진 페이지 갯수 만큼 추가한다.
	 * 
	 * @param request - HttpServletRequest
	 * @param principalType - U/G/R
	 * @param menuType - Menu Type (mainMenu : 0, myMenu : 1, myQuickMenu : 2)
	 * @param pageIds - pageId string with comma
	 * @return void
	 * @exception DataAccessException
	 */
	public void setMyMenu(HttpServletRequest request, String principalType, int menuType, String pageIds) throws BaseException;
	
	/**
	 * 해당하는 메뉴를 주어진 위치로 이동 시키고 전체를 다시 재 정렬 한다.
	 * 
	 * @param request - HttpServletRequest
	 * @param principalType - U/G/R
	 * @param menuType - Menu Type (mainMenu : 0, myMenu : 1, myQuickMenu : 2)
	 * @param pageId - pageId string
	 * @param toDown - Sorting direction
	 * @return void
	 * @exception DataAccessBaseException
	 */
	public void changeOrder(HttpServletRequest request, String principalType, int menuType, String pageId, boolean toDown) throws BaseException;
	
	/**
	 * 마이페이지 목록 중 주어진 위치로 이동 시키고 전체를 다시 재 정렬 한다.
	 * 
	 * @param request - HttpServletRequest
	 * @param pageId - pageId string
	 * @param toDown - Sorting direction
	 * @return void
	 * @exception DataAccessBaseException
	 */
	public void changeMyPageOrder(HttpServletRequest request, String pageId, boolean toDown) throws BaseException;

	/**
	 * 해당하는 메뉴 타입에 주어진 페이지 갯수 만큼 삭제한다.
	 * 
	 * @param principalType - U/G/R
	 * @param request - HttpServletRequest
	 * @param menuType - Menu Type (mainMenu : 0, myMenu : 1, myQuickMenu : 2)
	 * @param pageIds - pageId string with comma
	 * @return void
	 * @exception BaseException
	 */
	public void delete(HttpServletRequest request, String principalType, int menuType, String pageId, String domainId) throws BaseException;
	
	
}
