package com.saltware.enface.user.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.saltware.enface.user.service.UserVO;
import com.saltware.enview.Enview;

/**
 * 사용자관리 Data Access Object
 * 
 * @author kevin
 * @since 2011.07.26 13:2:324
 * @version 1.0
 */
public class SiteUserManagerDAO extends SqlMapClientDaoSupport {
	private static final Log log = LogFactory.getLog(SiteUserManagerDAO.class);

	/**
	 * 사용자를 찾는다.
	 * 
	 * @param paramMap
	 *            조회조건(userId,domainId)
	 */
	public UserVO findUser(Map paramMap) throws DataAccessException {
		return (UserVO) getSqlMapClientTemplate().queryForObject("user.findUser", paramMap);
	}

	/**
	 * 인증오류건수를 증가시킨다.
	 * 
	 * @param paramMap
	 *            조회조건(userId)
	 */
	public void updateAuthFailure(Map paramMap) throws DataAccessException {
		getSqlMapClientTemplate().update("user.updateAuthFailure", paramMap);
	}

	/**
	 * 인증오류건수를 초기화한다
	 * 
	 * @param paramMap
	 *            매개변수맵(userId)
	 */
	public void initAuthFailure(Map paramMap) throws DataAccessException {
		getSqlMapClientTemplate().update("user.initAuthFailure", paramMap);
	}

	/**
	 * 사용자의 상세정보를 조회한다.
	 * 
	 * @param paramMap
	 *            조회조건(userId, domainId)
	 * @return 사용자 상세정보
	 */
	public UserVO getUserDetail(Map paramMap) throws DataAccessException {
		return (UserVO) getSqlMapClientTemplate().queryForObject("user.detail", paramMap);
	}

	/**
	 * 사용자의 그룹과 역할, 그룹역할목록을 조회한다.
	 * 
	 * @param userInfoMap
	 *            조회조건 (principal_id(user), domainId)
	 * @return 그룹,역할목록
	 * @throws DataAccessException
	 */
	public List findGroupRole(Map userInfoMap) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("user.findGroupRole", userInfoMap);
	}

	/**
	 * 사용자의 그룹과 역할, 그룹역할목록을 조회한다.
	 * 
	 * @param userInfoMap
	 *            조회조건 (principal_id(user), domainId)
	 * @return 그룹,역할목록
	 * @throws DataAccessException
	 */
	public Map findGroup(Map userInfoMap) throws DataAccessException {
		return (Map) getSqlMapClientTemplate().queryForObject("user.findGroup", userInfoMap);
	}

	/**
	 * 사용자의 그룹 리스트를 조회한다.
	 * 
	 * @throws DataAccessException
	 */
	public List findGroupList(Map userInfoMap) throws DataAccessException {
		return (List) getSqlMapClientTemplate().queryForList("user.findGroupList", userInfoMap);
	}

	/**
	 * 사용자의 그룹과 역할, 그룹역할목록을 조회한다.
	 * 
	 * @param userInfoMap
	 *            조회조건 (principal_id(user), domainId)
	 * @return 그룹,역할목록
	 * @throws DataAccessException
	 */
	public List findRole(Map userInfoMap) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("user.findRole", userInfoMap);
	}

	/**
	 * 사용자의 그룹과 역할, 그룹역할목록을 조회한다.
	 * 
	 * @param userInfoMap
	 *            조회조건 (principal_id(user), domainId)
	 * @return 그룹,역할목록
	 * @throws DataAccessException
	 */
	public List findRolesForGroup(Map userInfoMap) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("user.findRolesForGroup", userInfoMap);
	}

	/**
	 * 사용자의 그룹과 역할, 그룹역할목록을 조회한다.
	 * 
	 * @param parentId
	 *            조회조건 (principal_id(user), domainId)
	 * @return 그룹,역할목록
	 * @throws DataAccessException
	 */
	public Map findParentRole(String parentId) throws DataAccessException {
		return (Map) getSqlMapClientTemplate().queryForObject("user.findParentRole", parentId);
	}

	/***
	 * 상위그룹의 그룹, 그룹역할목록을 조회한다
	 * 
	 * @param parentId
	 *            상위그룹ID
	 * @return 그룹,역할목록
	 * @throws DataAccessException
	 */
	public List findParentGroupRole(String parentId) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("user.findParentGroupRole", parentId);
	}

	/**
	 * 사용자권한을 조회한다.
	 * 
	 * @param userInfoMap
	 *            조회조건(principal_id(user))
	 * @return 사용자권한목록
	 * @throws DataAccessException
	 */
	public List getPermission(Map userInfoMap) throws DataAccessException {
		log.debug( "userInfoMap=" + userInfoMap);
		return getSqlMapClientTemplate().queryForList("user.getPermission", userInfoMap);
	}

	/**
	 * 사용자권한을 조회한다.
	 * 
	 * @param userInfoMap
	 *            조회조건(principal_id(user))
	 * @return 사용자권한목록
	 * @throws DataAccessException
	 */
	public List getPermissionForRole(Map userInfoMap) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("user.getPermissionForRole", userInfoMap);
	}

	/**
	 * 사용자권한을 조회한다.
	 * 
	 * @param userInfoMap
	 *            조회조건(principal_id(user))
	 * @return 사용자권한목록
	 * @throws DataAccessException
	 */
	public List getPermission2(String domain, String firstGroupRoleId) throws DataAccessException {
		Map paramMap = new HashMap();
		paramMap.put("domainId", domain);
		paramMap.put("firstGroupRoleId", firstGroupRoleId);
		return getSqlMapClientTemplate().queryForList("user.getPermission2", paramMap);
	}

	/**
	 * 사용자권한을 조회한다.
	 * 
	 * @param userInfoMap
	 *            조회조건(principal_id(user))
	 * @return 사용자권한목록
	 * @throws DataAccessException
	 */
	public List getPermission3(Map userInfoMap) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("user.getPermission3", userInfoMap);
	}

	/**
	 * @param userInfoMap
	 *            조회조건(principal_id(user))
	 * @return 사용자 첫번째 그룹의 롤
	 * @throws DataAccessException
	 */
	public String findGroupRoleId(Map userInfoMap) throws DataAccessException {
		return (String) getSqlMapClientTemplate().queryForObject("user.findGroupRoleId", userInfoMap);
	}

	/**
	 * @param userInfoMap
	 *            조회조건(principal_id(user))
	 * @return 사용자 첫번째 그룹의 롤
	 * @throws DataAccessException
	 */
	public List findGroupRoleIds(Map userInfoMap) throws DataAccessException {
		return (List) getSqlMapClientTemplate().queryForList("user.findGroupRoleIds", userInfoMap);
	}

	/**
	 * 비밀번호를 변경한다.
	 * 
	 * @param paramMap
	 *            매개변수맵(userId, password)
	 * @throws DataAccessException
	 */
	public void changePassword(Map paramMap) throws DataAccessException {
		getSqlMapClientTemplate().update("user.changePassword", paramMap);
	}

	/**
	 * 사용자로그인내역(USER_STATISTICS)에 로그를 추가한다.
	 * 
	 * @param paramMap
	 *            매개변수맵
	 * @throws DataAccessException
	 */
	public void writeLog(Map paramMap) throws DataAccessException {
		List userInfoList = getSqlMapClientTemplate().queryForList("user.detailExtraInfo", paramMap);
		if (userInfoList != null && userInfoList.size() > 0) {
			// userInfoList가 2개 이상 되는 경우는 security_user_group에 sort_order가 '0'인게 2개 이상 있는 경우이다.
			// 이 경우에는 group중 하나의 sort_order를 바꿔준다.
			Map userInfoMap = (Map) userInfoList.get(0);
			paramMap.put("orgCd", userInfoMap.get("groupId"));
			paramMap.put("orgName", userInfoMap.get("groupName"));
			paramMap.put("userName", userInfoMap.get("userName"));
			paramMap.put("principal_id", userInfoMap.get("principal_id"));
			paramMap.put("domainId", Enview.getUserDomain().getDomainId());
			getSqlMapClientTemplate().update("user.writeLog", paramMap);
		}
	}

	/**
	 * 사용자ID 중복체크를 한다.
	 * 
	 * @param userId
	 *            사용자 ID
	 * @return 중복여부
	 * @throws DataAccessException
	 */

	public boolean isOverlap(String userId) throws DataAccessException {
		Integer cnt = (Integer) getSqlMapClientTemplate().queryForObject("user.exist", userId);
		return (cnt != 0) ? true : false;
	}

	/**
	 * 사용자정보를 추가한다.
	 * 
	 * @param userVO
	 *            사용자정보
	 * @throws DataAccessException
	 * @throws SQLException
	 */
	public void regist(UserVO userVO, String[] groups, String[] roles) throws DataAccessException, SQLException {
		getSqlMapClientTemplate().getSqlMapClient().startBatch();

		getSqlMapClientTemplate().insert("user.insertSecurityPrincipal", userVO);
		getSqlMapClientTemplate().insert("user.insertSecurityCredential", userVO);
		getSqlMapClientTemplate().insert("user.insertDomainPrincipal", userVO);
		getSqlMapClientTemplate().insert("user.insert", userVO);
		if (groups == null) {
			if (userVO.getGroupId() != null) {
				getSqlMapClientTemplate().insert("user.insertUserGroup", userVO);
			}
		} else {
			for (int i = 0; i < groups.length; i++) {
				userVO.setGroupId(groups[i]);
				getSqlMapClientTemplate().insert("user.insertUserGroup", userVO);
			}
		}

		if (roles == null) {
			if (userVO.getRoleId() != null) {
				getSqlMapClientTemplate().insert("user.insertUserRole", userVO);
			}
		} else {
			for (int i = 0; i < roles.length; i++) {
				userVO.setRoleId(roles[i]);
				getSqlMapClientTemplate().insert("user.insertUserRole", userVO);
			}
		}
		// getSqlMapClientTemplate().insert("user.insertPushConfig", userVO);
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}

	/**
	 * 사용자정보를 수정한다.
	 * 
	 * @param userVO
	 * @throws DataAccessException
	 * @throws SQLException
	 */
	public void update(UserVO userVO, String[] groups, String[] roles) throws DataAccessException, SQLException {
		getSqlMapClientTemplate().getSqlMapClient().startBatch();

		getSqlMapClientTemplate().insert("user.updateSecurityPrincipal", userVO);
		getSqlMapClientTemplate().insert("user.update", userVO);

		if (groups != null) {
			getSqlMapClientTemplate().insert("user.deleteUserGroup", userVO);
			for (int i = 0; i < groups.length; i++) {
				userVO.setGroupId(groups[i]);
				getSqlMapClientTemplate().insert("user.insertUserGroup", userVO);
			}
		}

		if (roles != null) {
			getSqlMapClientTemplate().insert("user.deleteUserRole", userVO);
			for (int i = 0; i < roles.length; i++) {
				userVO.setRoleId(roles[i]);
				getSqlMapClientTemplate().insert("user.insertUserRole", userVO);
			}
		}

		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}

	/**
	 * 사용자의 메뉴변경플래그를 업데이트한다.
	 * 
	 * @param paramMap
	 *            매개변수맵( principal_id, domainId, modifyMenu)
	 * @throws DataAccessException
	 */
	public void updateModifyMenu(Map paramMap) throws DataAccessException {
		getSqlMapClientTemplate().update("user.updateModifyMenu", paramMap);
	}

	/**
	 * 여러 사용자의 메뉴변경플래그를 업데이트한다.
	 * 
	 * @param updateList
	 *            수정 목록
	 * @throws DataAccessException
	 * @throws SQLException
	 */
	public void updateModifyMenu(List updateList) throws DataAccessException, SQLException {
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		for (Iterator it = updateList.iterator(); it.hasNext();) {
			Map paramMap = (Map) it.next();
			getSqlMapClientTemplate().update("user.updateModifyMenu", paramMap);
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}

	/**
	 * 지정된 역할을 가진 모든 사용자의 메뉴변경플래그를 변경한다.<br>
	 * 역할이 변경된 경우 해당 역할을 가진 모든 사용자의 메뉴를 재구성해야 하므로 메뉴변경플래그를 설정한다.
	 * 
	 * @param paramMap
	 *            매개변수맵( roleId, domainId, modifyMenu)
	 * @throws DataAccessException
	 */
	public void updateModifyMenuByRole(Map paramMap) throws DataAccessException {
		getSqlMapClientTemplate().update("user.updateModifyMenuByRole", paramMap);
	}

	/**
	 * 여러 역할사용자의 메뉴변경플래그를 변경한다.<br>
	 * 역할이 변경된 경우 해당 역할을 가진 모든 사용자의 메뉴를 재구성해야 하므로 메뉴변경플래그를 설정한다.
	 * 
	 * @param updateList
	 *            수정 목록
	 * @throws DataAccessException
	 * @throws SQLException
	 */
	public void updateModifyMenuByRole(List updateList) throws DataAccessException, SQLException {
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		for (Iterator it = updateList.iterator(); it.hasNext();) {
			Map paramMap = (Map) it.next();
			getSqlMapClientTemplate().update("user.updateModifyMenuByRole", paramMap);
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}

	/**
	 * 지정된 그룹을 가진 모든 사용자의 메뉴변경플래그를 변경한다. 그웁역할이 변경된 경우 해당 그룹을 가진 모든 사용자의 메뉴를 재구성해야 하므로 메뉴변경플래그를 설정한다.
	 * 
	 * @param paramMap
	 *            매개변수맵( roleId, domainId, modifyMenu)
	 * @throws DataAccessException
	 */
	public void updateModifyMenuByGroup(Map paramMap) throws DataAccessException {
		getSqlMapClientTemplate().update("user.updateModifyMenuByGroup", paramMap);
	}

	/**
	 * 여러 그룹사용자의 메뉴변경플래그를 변경한다. 그웁역할이 변경된 경우 해당 그룹을 가진 모든 사용자의 메뉴를 재구성해야 하므로 메뉴변경플래그를 설정한다.
	 * 
	 * @param updateList
	 *            수정 목록
	 * @throws DataAccessException
	 */
	public void updateModifyMenuByGroup(List updateList) throws DataAccessException, SQLException {
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		for (Iterator it = updateList.iterator(); it.hasNext();) {
			Map paramMap = (Map) it.next();
			getSqlMapClientTemplate().update("user.updateModifyMenuByGroup", paramMap);
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}

	/**
	 * 그룹역할이 포함된 그룹의 사용자의 메뉴변경플래그를 변경한다. 그웁역할이 변경된 경우 해당 그룹을 가진 모든 사용자의 메뉴를 재구성해야 하므로 메뉴변경플래그를 설정한다.
	 * 
	 * @param paramMap
	 *            매개변수맵
	 * @throws DataAccessException
	 */
	public void updateModifyMenuByGroupRole(Map paramMap) throws DataAccessException {
		getSqlMapClientTemplate().update("user.updateModifyMenuByGroupRole", paramMap);
	}

	/**
	 * 여러 그룹역할이 포함된 그룹의 사용자의 메뉴변경플래그를 변경한다. 그웁역할이 변경된 경우 해당 그룹을 가진 모든 사용자의 메뉴를 재구성해야 하므로 메뉴변경플래그를 설정한다.
	 * 
	 * @param updateList
	 *            수정 목록
	 * @throws DataAccessException
	 */
	public void updateModifyMenuByGroupRole(List updateList) throws DataAccessException, SQLException {
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		for (Iterator it = updateList.iterator(); it.hasNext();) {
			Map paramMap = (Map) it.next();
			getSqlMapClientTemplate().update("user.updateModifyMenuByGroupRole", paramMap);
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}

	/**
	 * 사용자의 도메인ID목록을 조회한다.
	 * 
	 * @param userId
	 *            사용자ID
	 * @return 사용자 도메인ID 목록
	 */
	public List getDomainIdsByUserId(String userId) {
		return (List) getSqlMapClientTemplate().queryForList("user.domainIds", userId);
	}

	public String getNow() {
		return (String) getSqlMapClientTemplate().queryForObject("user.now");
	}

	/**
	 * 사용자정보에 세션ID를 설정한다.
	 * 
	 * @param paramMap
	 */
	public void updateSessionId(Map paramMap) {
		getSqlMapClientTemplate().update("user.updateSessionId", paramMap);
	}
	
	/**
	 * 사용자정보에 세션ID를 설정한다.
	 * 
	 * @param paramMap
	 */
	public void logoutSessionId(Map paramMap) {
		getSqlMapClientTemplate().update("user.logoutSessionId", paramMap);
	}

	/**
	 * 사용자정보에 마지막 로그인 IP와 일자를 업데이트한다
	 * 
	 * @param paramMap
	 */
	public void updateLastLogin(Map paramMap) {
		getSqlMapClientTemplate().update("user.updateLastLogin", paramMap);
	}

	/**
	 * 사용자정보에서 세션ID를 읽어온다.
	 * 
	 * @return
	 */
	public Map getSessionId(String userId) {
		return (Map) getSqlMapClientTemplate().queryForObject("user.getSessionId", userId);
	}
	
	/**
	 * 사용자의 로그인IP목록을 조회한다
	 * 
	 * @param userId
	 *            사용자ID
	 * @return 사용자 로그인IP 목록 
	 */
	public List getLoginIps(String userId) {
		return (List) getSqlMapClientTemplate().queryForList("user.loginIps", userId);
	}
	
	/**
	 * 해외로그인차단 설정을 저장한다 
	 * @param paramMap
	 * @return
	 * @throws DataAccessException
	 */
	public int updateBlockAbroad(Map paramMap) throws DataAccessException {
		return getSqlMapClientTemplate().update("user.updateBlockAbroad", paramMap);
	}
	
	public int updateCurrentSession( Map paramMap ) throws DataAccessException {
		return getSqlMapClientTemplate().update("user.updateCurrentSession", paramMap);
	}



}
