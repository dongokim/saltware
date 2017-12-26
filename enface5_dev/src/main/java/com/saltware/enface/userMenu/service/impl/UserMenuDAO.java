
package com.saltware.enface.userMenu.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.saltware.enview.security.EnviewMenu;



/**  
 * @Class Name : UserMenuDAO.java
 * @Description : 사용자메뉴관리 Data Access Object
 * @
 * @author kevin
 * @since 2011.07.26 13:2:324
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by Saltware All right reserved.
 */
public class UserMenuDAO extends SqlMapClientDaoSupport
{
	/**
	 * Retrieve all user Menu list for Guest
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception DataAccessException
	 */
	public Collection findAll4Guest(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userMenu.findAll4Guest", paramMap);
	}
	
	/**
	 * Retrieve all user Menu list for Admin
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception DataAccessException
	 */
	public Collection findAll4Admin(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userMenu.findAll4Admin", paramMap);
	}
	
	/**
	 * Retrieve all user Menu list
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception DataAccessException
	 */
	public Collection findAll(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userMenu.findAll", paramMap);
	}
	
	/**
	 * 카이스트 메뉴 캐시 - gjrjf
	 * Retrieve all user Menu list
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception DataAccessException
	 */
	public Collection findAll2(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userMenu.findAll2", paramMap);
	}
	
	/**
	 * Retrieve all quick Menu list for Guest
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception DataAccessException
	 */
	public Collection findQuick4Guest(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userMenu.findQuick4Guest", paramMap);
	}
	
	/**
	 * Retrieve all quick Menu list for Admin
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception DataAccessException
	 */
	public Collection findQuick4Admin(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userMenu.findQuick4Admin", paramMap);
	}
	
	/**
	 * Retrieve all quick Menu list
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception DataAccessException
	 */
	public Collection findQuick(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userMenu.findQuick", paramMap);
	}
	
	/**
	 * Retrieve child user Menu list by id for Guest
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception DataAccessException
	 */
	public Collection findById4Guest(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userMenu.findById4Guest", paramMap);
	}
	
	/**
	 * Retrieve child user Menu list by id for Admin
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception DataAccessException
	 */
	public Collection findById4Admin(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userMenu.findById4Admin", paramMap);
	}
	
	/**
	 * Retrieve child user Menu list by id
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception DataAccessException
	 */
	public Collection findById(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userMenu.findById", paramMap);
	}
	
	/**
	 * Retrieve child user Menu list by path for Guest
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception DataAccessException
	 */
	public Collection findByPath4Guest(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userMenu.findByPath4Guest", paramMap);
	}
	
	/**
	 * Retrieve child user Menu list by path for Admin
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception DataAccessException
	 */
	public Collection findByPath4Admin(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userMenu.findByPath4Admin", paramMap);
	}
	
	/**
	 * Retrieve child user Menu list by path
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception DataAccessException
	 */
	public Collection findByPath(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userMenu.findByPath", paramMap);
	}
	
	/**
	 * Retrieve My Menu list by principal_id
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception DataAccessException
	 */
	public Collection findByUser(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userMenu.findByUser", paramMap);
	}
	
	/**
	 * Retrieve Group Menu list by principal_id
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception DataAccessException
	 */
	public Collection findByGroup(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userMenu.findByGroup", paramMap);
	}
	
	/**
	 * Get userMenu for Guest
	 * @param enviewMenu - EnviewMenu
	 * @return EnviewMenu
	 * @exception DataAccessException
	 */
	public EnviewMenu detail4Guest(Map paramMap) throws DataAccessException 
	{
		return (EnviewMenu)getSqlMapClientTemplate().queryForObject("userMenu.detail4Guest", paramMap);
	}
	
	/**
	 * Get userMenu for Admin
	 * @param enviewMenu - EnviewMenu
	 * @return EnviewMenu
	 * @exception DataAccessException
	 */
	public EnviewMenu detail4Admin(Map paramMap) throws DataAccessException 
	{
		return (EnviewMenu)getSqlMapClientTemplate().queryForObject("userMenu.detail4Admin", paramMap);
	}

	/**
	 * Get userMenu
	 * @param enviewMenu - EnviewMenu
	 * @return EnviewMenu
	 * @exception DataAccessException
	 */
	public EnviewMenu detail(Map paramMap) throws DataAccessException 
	{
		return (EnviewMenu)getSqlMapClientTemplate().queryForObject("userMenu.detail", paramMap);
	}
	
	/**
	 * Get userMenu for Guest
	 * @param enviewMenu - EnviewMenu
	 * @return EnviewMenu
	 * @exception DataAccessException
	 */
	public EnviewMenu detailByPath4Guest(Map paramMap) throws DataAccessException 
	{
		return (EnviewMenu)getSqlMapClientTemplate().queryForObject("userMenu.detailByPath4Guest", paramMap);
	}
	
	/**
	 * Get userMenu for Admin
	 * @param enviewMenu - EnviewMenu
	 * @return EnviewMenu
	 * @exception DataAccessException
	 */
	public EnviewMenu detailByPath4Admin(Map paramMap) throws DataAccessException 
	{
		return (EnviewMenu)getSqlMapClientTemplate().queryForObject("userMenu.detailByPath4Admin", paramMap);
	}
	
	
	/**
	 * Get userMenu
	 * @param enviewMenu - EnviewMenu
	 * @return EnviewMenu
	 * @exception DataAccessException
	 */
	public EnviewMenu detailByPath(Map paramMap) throws DataAccessException 
	{
		return (EnviewMenu)getSqlMapClientTemplate().queryForObject("userMenu.detailByPath", paramMap);
	}
	
	/**
	 * Add new user Menu list information
	 * @param paramMap - add userMenus
	 * @return int
	 * @exception DataAccessException
	 */
	public int insertList(List newList) throws DataAccessException, SQLException
	{
		int retCnt = 0;

		if( newList != null && newList.size() > 0 ) {

			List insertList = new ArrayList();
			List updateList = new ArrayList();
			List deleteList = new ArrayList();
		
			getSqlMapClientTemplate().getSqlMapClient().startBatch();

			//새로 추가될 메뉴 셋 설정
			Set newMenuSet = new HashSet();
			Iterator it = newList.iterator();
			for(int i=0; it.hasNext(); i++) {
				//new Menu List 객체들을 가져와서 해당 메뉴의 domainId를 찾아서 셋팅 후 메뉴들을 테이블에 새로 삽입될 메뉴 셋에 추가
				EnviewMenu enviewMenu = (EnviewMenu)it.next();
				int domainId = (Integer)getSqlMapClientTemplate().queryForObject("userMenu.domainIdByPageId", Integer.valueOf(enviewMenu.getPageId()));
				enviewMenu.setDomainId(Integer.toString(domainId));
				newMenuSet.add( enviewMenu.getPageId() );
			}
			for(int i=0; it.hasNext(); i++) {
				//new Menu List 객체들을 가져와서 해당 메뉴의 domainId를 찾아서 셋팅 후 메뉴들을 테이블에 새로 삽입될 메뉴 셋에 추가
				EnviewMenu enviewMenu = (EnviewMenu)it.next();
				//System.out.println("*************** enviewMenu.getDomainId()=["+enviewMenu.getDomainId()+"]");
			}
			//System.out.println("### newMenuSet=" + newMenuSet);
			
			Map paramMap = new HashMap();
			paramMap.put("principalId", ((EnviewMenu)newList.get(0)).getPrincipalId());
			paramMap.put("langKnd", ((EnviewMenu)newList.get(0)).getLangKnd());
			paramMap.put("menuType", ((EnviewMenu)newList.get(0)).getMenuType());

			List tmpList = getSqlMapClientTemplate().queryForList("userMenu.findAll", paramMap);
			//System.out.println("######## tmpList=" + tmpList + ", paramMap=" + paramMap);
			it = tmpList.iterator();
			for(int i=0; it.hasNext(); i++) {
				EnviewMenu enviewMenu = (EnviewMenu)it.next();
				//System.out.println("######## all enviewMenu=" + enviewMenu);
				// 기존 목록 중 새로운 목록에 존재하면 업데이트 목록으로
				// 기존 목록 중 새로운 목록에 존재하지 않으면 삭제목록으로
				//if( newMenuSet.contains(enviewMenu.getPageId()) == true ) {
				if( newMenuSet.contains(enviewMenu.getPageId()) ) {
					//System.out.println("######## add updateList");
					updateList.add( enviewMenu );
				}
				else {
					enviewMenu.setPrincipalId( (String)paramMap.get("principalId") );
					enviewMenu.setLangKnd( (String)paramMap.get("langKnd") );
					enviewMenu.setMenuType( (String)paramMap.get("menuType") );
					//System.out.println("######## add deleteList");
					deleteList.add( enviewMenu );
				}
			}
			//System.out.println("### updateList=" + updateList);
			//System.out.println("### deleteList=" + deleteList);
			
			Set prevMenuSet = new HashSet();
			it = tmpList.iterator();
			for(int i=0; it.hasNext(); i++) {
				EnviewMenu enviewMenu = (EnviewMenu)it.next();
				prevMenuSet.add( enviewMenu.getPageId() );
			}
			//System.out.println("### prevMenuSet=" + prevMenuSet);
			
			it = newList.iterator();
			for(int i=0; it.hasNext(); i++) {
				EnviewMenu enviewMenu = (EnviewMenu)it.next();
				// 새로운 목록 중 기존목록에 존재하지 않으면 신규목록으로
				//if( prevMenuSet.contains(enviewMenu.getPageId()) == false ) {
				if( !prevMenuSet.contains(enviewMenu.getPageId()) ) {
					insertList.add( enviewMenu );
				}
			}
			//System.out.println("### insertList=" + insertList);
			
			List sortList = new ArrayList();
			sortList.addAll( updateList );
			sortList.addAll( insertList );
			
			it = sortList.iterator();
			for(int i=0; it.hasNext(); i++) {
				EnviewMenu enviewMenu = (EnviewMenu)it.next();
				enviewMenu.setPrincipalId( (String)paramMap.get("principalId") );
				enviewMenu.setLangKnd( (String)paramMap.get("langKnd") );
				enviewMenu.setMenuType( (String)paramMap.get("menuType") );
				enviewMenu.setMenuOrder(i);
			}
			
			it = insertList.iterator();
			for(int i=0; it.hasNext(); i++) {
				EnviewMenu enviewMenu = (EnviewMenu)it.next();
				//System.out.println("######## insert enviewMenu=" + enviewMenu);
				getSqlMapClientTemplate().insert("userMenu.insert", enviewMenu);
			}
			
			it = updateList.iterator();
			for(int i=0; it.hasNext(); i++) {
				EnviewMenu enviewMenu = (EnviewMenu)it.next();
				//System.out.println("######## update enviewMenu=" + enviewMenu);
				getSqlMapClientTemplate().update("userMenu.update", enviewMenu);
			}
			
			it = deleteList.iterator();
			for(int i=0; it.hasNext(); i++) {
				EnviewMenu enviewMenu = (EnviewMenu)it.next();
				//System.out.println("######## delete enviewMenu=" + enviewMenu);
				getSqlMapClientTemplate().delete("userMenu.delete", enviewMenu);
			}
			
			retCnt = getSqlMapClientTemplate().getSqlMapClient().executeBatch();
		}

		return retCnt;
	}
	
	/**
	 * Add new user Menu list information
	 * @param paramMap - paramMap(principal_id, domainId)
	 * @param pageList - add userMenus
	 * @return int
	 * @exception DataAccessException
	 */
	public int insertList(Map paramMap, List pageList) throws DataAccessException, SQLException
	{
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		
		paramMap.put("menuType", "" + MenuSessionContext.MENUTYPE_MAIN_MENU);
		//getSqlMapClientTemplate().delete("userMenu.deleteByGroup", paramMap);
		getSqlMapClientTemplate().delete("userMenu.deleteAll", paramMap);
		
		for(Iterator it=pageList.iterator(); it.hasNext(); ) {
			paramMap.put("pagePath", (String)it.next());
//			System.out.println( "pagePath=" + paramMap.get("pagePath"));
			getSqlMapClientTemplate().insert("userMenu.insertByPath", paramMap);
		}
		
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
		
		return 0;
	}
	
	
	/**
	 * Update user Menu list order information
	 * @param updateList - new data EnviewMenu
	 * @return int
	 * @exception DataAccessException
	 */
	public int updateList(List updateList) throws DataAccessException, SQLException
	{
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		Iterator it = updateList.iterator();
		for(int i=0; it.hasNext(); i++) {
			EnviewMenu enviewMenu = (EnviewMenu)it.next();
			enviewMenu.setMenuOrder(i);
			getSqlMapClientTemplate().update("userMenu.update", enviewMenu);
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
		
		return 0;
	}
	
	/**
	 * Update user my page list order information
	 * @param updateList - new data EnviewMenu
	 * @return int
	 * @exception DataAccessException
	 */
	public int updateMyPageOrderList(List updateList) throws DataAccessException, SQLException
	{
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		Iterator it = updateList.iterator();
		for(int i=0; it.hasNext(); i++) {
			EnviewMenu enviewMenu = (EnviewMenu)it.next();
			enviewMenu.setMenuOrder(i);
			getSqlMapClientTemplate().update("userMenu.updateMyPageOrder", enviewMenu);
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
		
		return 0;
	}

	/**
	 * Add new user Menu list information
	 * @param paramMap - add userMenus
	 * @return int
	 * @exception DataAccessException
	 */
	public int deleteList(List deleteList) throws DataAccessException, SQLException
	{
		int retCnt = 0;
		if( deleteList != null && deleteList.size() > 0 ) {
		
			getSqlMapClientTemplate().getSqlMapClient().startBatch();
			
			Iterator it = deleteList.iterator();
			for(int i=0; it.hasNext(); i++) {
				EnviewMenu enviewMenu = (EnviewMenu)it.next();
				getSqlMapClientTemplate().delete("userMenu.delete", enviewMenu);
			}
			
			retCnt = getSqlMapClientTemplate().getSqlMapClient().executeBatch();
		}

		return retCnt;
	}
	
	/**
	 * Check exist userMenu by name
	 * @param VO - modify data EnviewMenu
	 * @return boolean
	 * @exception DataAccessException
	 */
	public boolean exist(EnviewMenu enviewMenu) throws DataAccessException
	{
		Integer cnt = (Integer)getSqlMapClientTemplate().queryForObject("userMenu.exist", enviewMenu);
		return (cnt != 0) ? true : false;
	}
	
	/**
	 * Delete All My menu(menuType=1)
	 * @param paramMap.principalId String
	 * @param paramMap.menuType int
	 * @exception DataAccessException
	 */
	public void deleteMyMenuAll(Map paramMap) throws DataAccessException
	{
		getSqlMapClientTemplate().delete("userMenu.deleteMyMenuAll", paramMap);
	}
}
