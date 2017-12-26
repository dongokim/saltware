package com.saltware.enface.user.service.impl;

import java.math.BigDecimal;
import java.security.AccessControlException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enboard.dao.MileageDAO;
import com.saltware.enboard.form.AdminMileForm;
import com.saltware.enface.security.UserInfomationHandler;
import com.saltware.enface.tool.service.impl.TempPasswordMailSender;
import com.saltware.enface.user.service.SiteUserManager;
import com.saltware.enface.user.service.UserVO;
import com.saltware.enface.userMenu.service.impl.UserMenuDAO;
import com.saltware.enface.util.IpUtil;
import com.saltware.enface.util.StringUtil;
import com.saltware.enview.Enview;
import com.saltware.enview.components.dao.ConnectionContext;
import com.saltware.enview.components.dao.ConnectionContextForRdbms;
import com.saltware.enview.components.dao.DAOFactory;
import com.saltware.enview.domain.DomainInfo;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.exception.EnfaceException;
import com.saltware.enview.idgenerator.IdGenerator;
import com.saltware.enview.login.LoginConstants;
import com.saltware.enview.multiresource.EnviewMultiResourceManager;
import com.saltware.enview.multiresource.MultiResourceBundle;
import com.saltware.enview.om.role.Role;
import com.saltware.enview.role.RoleManager;
import com.saltware.enview.security.PasswordEncoder;
import com.saltware.enview.security.SecurityPolicy;
import com.saltware.enview.security.SecurityPolicyManager;
import com.saltware.enview.security.SecurityPolicyServiceDAC;
import com.saltware.enview.security.UserInfo;
import com.saltware.enview.security.impl.UserInfoImpl;
import com.saltware.enview.sso.EnviewSSOManager;
import com.saltware.enview.statistics.PortalStatistics;
import com.saltware.enview.util.EnviewMap;
import com.saltware.enview.util.HttpUtil;
import com.saltware.enview.util.TreeObject;

/**
 * 사용자관리자 구현
 * 
 */
public class SiteUserManagerImpl implements SiteUserManager {
	protected Log log = LogFactory.getLog(getClass());

	protected SiteUserManagerDAO siteUserManagerDAO = null;
	protected UserMenuDAO userMenuDAO = null;
	protected PasswordEncoder passwordEncoder;
	protected PasswordEncoder oldPasswordEncoder;
	protected TempPasswordMailSender tempPasswordMailSender;
	protected IdGenerator idGenerator = null;

	protected UserInfomationHandler userInfomationHandler;
	protected final static int HOST_DOMAIN = 0; // 호스트명에 따라 도메인 결정
	protected final static int USER_DOMAIN = 1; // 사용자 정보에 따라 도메인 결정
	protected int domainRule = HOST_DOMAIN;
	protected RoleManager roleManager;
	final static String CAPTCHA_ANSWER = "captchaAnswer";
	
	/**
	 * 생성자
	 */
	public SiteUserManagerImpl() {
		this.idGenerator = (IdGenerator) Enview.getComponentManager().getComponent("IdGenerator");
		this.roleManager = (RoleManager) Enview.getComponentManager().getComponent("com.saltware.enview.role.RoleManager");
	}

	/**
	 * 비밀번호암호화기를 리턴한다
	 * 
	 * @return 비밀번호암호화
	 */
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	/**
	 * 비밀번호암호화기를 설정한다.
	 * 
	 * @param passwordEncoder
	 *            비밀번호암호화
	 */
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * 사용자정보핸들러를 리턴한다.
	 * 
	 * @return
	 */
	public UserInfomationHandler getUserInfomationHandler() {
		if (userInfomationHandler == null) {
			this.userInfomationHandler = (UserInfomationHandler) Enview.getComponentManager().getComponent("com.saltware.enface.security.UserInfomationHandler");
		}
		return userInfomationHandler;
	}

	/**
	 * 임시비밀번호메일발송기를 설정한다.
	 * 
	 * @param tempPasswordMailSender
	 */
	public void setTempPasswordMailSender(TempPasswordMailSender tempPasswordMailSender) {
		this.tempPasswordMailSender = tempPasswordMailSender;
	}

	/**
	 * 사용자관리자DAO를 설정한다
	 * 
	 * @param siteUserManagerDAO
	 */
	public void setSiteUserManagerDAO(SiteUserManagerDAO siteUserManagerDAO) {
		this.siteUserManagerDAO = siteUserManagerDAO;
	}

	/**
	 * 사용자메뉴DAO를 설정한다.
	 * 
	 * @param userMenuDAO
	 */
	public void setUserMenuDAO(UserMenuDAO userMenuDAO) {
		this.userMenuDAO = userMenuDAO;
	}

	/**
	 * ID와 비밀번호를 사용자를 인증한다
	 * 
	 * @param userId 사용자ID
	 * @param password 비밀번호
	 */
	public String authenticate(String userId, String password) throws UserException {
		return authenticate(userId, password, null);
	}

	/**
	 * ID와 비밀번호를 사용자를 인증한다
	 * 
	 * @param userId 사용자ID
	 * @param password 비밀번호
	 */
	public String authenticate(String userId, String password, String clientIp) throws UserException {
		try {
			Map paramMap = new HashMap();
			UserVO user = null;

			paramMap.put("userId", userId);
			if (domainRule == HOST_DOMAIN) {
				// 호스트 도메인이 먼저이면 현재 도메인에서 사용자를 찾는다.
				DomainInfo domain = Enview.getUserDomain();
				paramMap.put("domainId", domain.getDomainId());
			}
			user = siteUserManagerDAO.findUser(paramMap);

			if (user == null)
				throw new UserException("ev.error.user.ErrorCode.1"); // 사용자 없음
			Map userInfoMap = user.toMap();
			String encPasswd = passwordEncoder.encode(userId, password);
			log.info("encPasswd=" + encPasswd);
			if (userInfoMap != null) {
				int principal_id = ((BigDecimal) userInfoMap.get("principal_id")).intValue();
				String dbPassword = (String) userInfoMap.get("column_value");
				int updateRequired = ((BigDecimal) userInfoMap.get("update_required")).intValue();
				int authFailures = ((BigDecimal) userInfoMap.get("auth_failures")).intValue();
				int isEnabled = ((BigDecimal) userInfoMap.get("is_enabled")).intValue();
				
				boolean blockAbroad = "1".equals(user.getPrincipalInfo01());
				if( clientIp!=null && blockAbroad) {
					if( ! IpUtil.isKoreanIp( clientIp)) {
						throw new UserException("ev.error.user.ErrorCode.15"); // 해외IP차단
					}
				}

				if (isEnabled == 0) {
					throw new UserException("ev.error.user.ErrorCode.3"); // 비활성화 계정
				}

				int portalLoginFailLimit = Enview.getConfiguration().getInt("portal.login.failLimit", 3);
				if (authFailures >= portalLoginFailLimit) {
					throw new UserException("ev.error.user.ErrorCode.8"); // 비밀번호 오류횟수 초과
				}

				// if( encPasswd.equals(prevPassword) == false ) {
				log.info("password=" + password + ", encPassword=" + encPasswd + ",dbPassword=" + dbPassword);
				if (!encPasswd.equals(dbPassword)) {
					if (oldPasswordEncoder != null) {
						// 비밀번호 알고리즘 마이그래 이션
						String oldPassword = oldPasswordEncoder.encode(userId, password);
						log.info("password=" + password + ", oldcPassword=" + oldPassword + ",dbPassword=" + dbPassword);
						if (oldPassword.equals(dbPassword)) {
							// 옛날 방식이면 새로운 암호로 업데이트
							DomainInfo domain = Enview.getUserDomain();
							paramMap.put("userId", userId);
							paramMap.put("domainId", domain.getDomainId());
							paramMap.put("password", encPasswd);
							siteUserManagerDAO.changePassword(paramMap);
						} else {
							throw new UserException("ev.error.user.ErrorCode.2"); // 비밀번호 오류
						}
					} else {
						throw new UserException("ev.error.user.ErrorCode.2"); // 비밀번호 오류
					}
				}

				if (authFailures > 0)
					siteUserManagerDAO.initAuthFailure(paramMap); // 오류횟수 초기화
			} else {
				throw new UserException("ev.error.user.ErrorCode.1"); // 사용자 없음
			}
			return encPasswd;
		} catch (UserException ue) {
			throw ue;
		} catch (BaseException e) {
			log.error(e.getMessage(), e);
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * ID로 사용자를 인증한다
	 * 
	 * @param userId
	 *            사용자ID
	 */
	public void authenticate(String userId) throws UserException {
		try {
			Map paramMap = new HashMap();
			paramMap.put("userId", userId);
			if (domainRule == HOST_DOMAIN) {
				// 호스트 도메인이 먼저이면 현재 도메인에서 사용자를 찾는다.
				DomainInfo domain = Enview.getUserDomain();
				paramMap.put("domainId", domain.getDomainId());
			}
			UserVO user = siteUserManagerDAO.findUser(paramMap);
			if (user == null)
				throw new UserException("ev.error.user.ErrorCode.1"); // 사용자 없음
			Map userInfoMap = user.toMap();
			if (userInfoMap != null) {
				int principal_id = ((BigDecimal) userInfoMap.get("principal_id")).intValue();
				String prevPassword = (String) userInfoMap.get("column_value");
				int updateRequired = ((BigDecimal) userInfoMap.get("update_required")).intValue();
				int authFailures = ((BigDecimal) userInfoMap.get("auth_failures")).intValue();
				int isEnabled = ((BigDecimal) userInfoMap.get("is_enabled")).intValue();
				if (isEnabled == 0) {
					throw new UserException("ev.error.user.ErrorCode.3"); // 비활성화 계정
				}

				int portalLoginFailLimit = Enview.getConfiguration().getInt("portal.login.failLimit", 3);
				if (authFailures >= portalLoginFailLimit) {
					throw new UserException("ev.error.user.ErrorCode.8"); // 비밀번호 오류횟수 초과
				}
				// 로그인 마지막으로 이동. 2013.11.21/smna
				// siteUserManagerDAO.initAuthFailure(paramMap); // 오류횟수 초기화
			} else {
				throw new UserException("ev.error.user.ErrorCode.1"); // 사용자 없음
			}
		} catch (BaseException e) {
			log.error(e.getMessage(), e);
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * 인증오류건수를 증가시킨다.
	 * 
	 * @param paramMap
	 *            조회조건(userId)
	 */
	public void updateAuthFailure(Map paramMap) throws UserException {
		siteUserManagerDAO.updateAuthFailure(paramMap);
	}

	/**
	 * 인증오류건수를 초기화한다
	 * 
	 * @param paramMap
	 *            매개변수맵(userId)
	 */
	public void initAuthFailure(Map paramMap) throws UserException {
		siteUserManagerDAO.initAuthFailure(paramMap);
	}

	/**
	 * 사용자의 상세정보를 조회한다.<br>
	 * 도메인은 현재로그인 사용자의 도메인으로 자동 설정된다.
	 * 
	 * @param userId
	 *            사용자ID
	 * @return 사용자 상세정보
	 */
	public UserInfo getUserInfo(String userId, String groupId, String langKnd) throws BaseException {
		try {
			UserInfo userInfo = null;

			Map paramMap = new HashMap();
			paramMap.put("userId", userId);
			if (domainRule == HOST_DOMAIN) {
				// 호스트 도메인이 먼저이면 현재 도메인에서 사용자를 찾는다.
				DomainInfo domain = Enview.getUserDomain();
				paramMap.put("domainId", domain.getDomainId());
			}

			// 그룹 체인지 일때
			if (groupId != null) {
				try {
					Long.parseLong(groupId);
					// 숫자이면 group princial id
					paramMap.put("group_principal_id", groupId);
				} catch (NumberFormatException nfe) {
					// 숫자가 아니면 groupId
					paramMap.put("groupId", groupId);
				}
			}
			if (langKnd != null)
				paramMap.put("lang_knd", langKnd);
			return getUserInfo(paramMap);
		} catch (BaseException e) {
			throw e;
		}
	}

	/**
	 * 사용자의 상세정보를 조회한다.<br>
	 * 
	 * @param userId
	 *            사용자ID
	 * @param domainId
	 *            도메인ID
	 * @return 사용자 상세정보
	 */
	public UserInfo getUserInfo(String userId, String domainId, String langKnd, boolean isCheckPermission) throws BaseException {
		UserInfo userInfo = null;

		Map paramMap = new HashMap();
		paramMap.put("userId", userId);
		paramMap.put("domainId", domainId);
		paramMap.put("lang_knd", langKnd);
		return getUserInfo(paramMap, isCheckPermission);
	}

	/**
	 * 사용자의 상세정보를 조회한다.<br>
	 * 
	 * @param paramMap
	 *            매개변수맵(userId, domainId)
	 * @return 사용자 상세정보
	 */
	private UserInfo getUserInfo(Map paramMap) throws BaseException {
		try {
			return this.getUserInfo(paramMap, false);
		} catch (BaseException e) {
			throw e;
		}
	}

	/**
	 * 사용자의 상세정보를 조회한다.<br>
	 * 
	 * @param paramMap
	 *            매개변수맵(userId, domainId)
	 * @param isCheckPermission
	 *            권한 체크 시 가져오는 정보(true)인지 인지 로그인 시 가져오는 정보인지 지정(false)
	 * @return 사용자 상세정보
	 */
	private UserInfo getUserInfo(Map paramMap, boolean isCheckPermission) throws BaseException {
		UserInfo userInfo = new UserInfoImpl();

		try {
			log.info("*** paramMap=" + paramMap);
			UserVO userVO = siteUserManagerDAO.getUserDetail(paramMap);
			if (userVO == null)
				return userInfo;

			// 사용자 정보 추출
			Map userInfoMap = userVO.toMap();
			String userPrincipalId = ((BigDecimal) userInfoMap.get("principal_id")).toString();
			String userDefaultPage = (String) userInfoMap.get("default_page");
			if (userDefaultPage == null || userDefaultPage.length() == 0) {
				userInfoMap.put("hasUserPage", "true");
			}

			userInfoMap.put("principal_id", userPrincipalId);

			if (userInfoMap.get("domainId") == null) {
				userInfoMap.put("domainId", Enview.getUserDomain().getDomainId());
				userInfoMap.put("domainNm", Enview.getUserDomain().getDomainNm());
			}
			log.info("*** USER INFORMATION(userInfoMap)=[" + userInfoMap + "]");

			// 그룹 principalid가 지정된 경우
			String group_principal_id = (String) paramMap.get("group_principal_id");
			if (group_principal_id != null)
				userInfoMap.put("group_principal_id", group_principal_id);

			// 그룹 id가 지정된 경우
			String groupId = (String) paramMap.get("groupId");
			if (groupId != null)
				userInfoMap.put("groupId", groupId);

			// 그룹 정보 추출하고 성공여부를 반환한다.
			boolean hasGroup = this.getGroupInfo(userInfoMap);
			log.info("*** GROUP_ID=[" + userInfoMap.get("groupId") + "], GROUP_NAME=[" + userInfoMap.get("groupName") + "], SITE_NAME=[" + userInfoMap.get("site_name") + "]");

			// 역할을 트리구조화 시키고 성공여부를 반환한다.
			boolean hasRole = this.getRoleInfo(userInfoMap);
			log.info("*** ROLE_IDS=[" + userInfoMap.get("roleIdList") + "] ROLE_NAMES=[" + userInfoMap.get("roles") + "]");

			// 그룹과 역할이 둘 중 하나라도 없으면 에러를 출력한다.
			// if( !(hasGroup && hasRole) ) {
			// //if(!isCheckPermission) throw new BaseException("You have no role or group belong to");
			// if(!isCheckPermission)
			// throw new UserException("mm.error.login.nogrouprole");
			// }

			// 메뉴 생성 로직
			String modify_menu = (String) userInfoMap.get("modify_menu");
			userInfoMap.put("principalId", userPrincipalId);
			log.debug("*** CHECK USER MENU MODIFIED..");
			group_principal_id = (String) userInfoMap.get("group_principal_id");
			if (modify_menu != null && modify_menu.equals("1")) {
				log.info("*** USER MENU MODIFIED. CREATE NEW USER MENU.");
				userMenuDAO.insertList(userInfoMap, getAccessableMenu(userInfoMap));
				paramMap.put("principal_id", userPrincipalId);
				paramMap.put("modifyMenu", "0");
				siteUserManagerDAO.updateModifyMenu(paramMap);
				log.debug("*** NEW USER MENU CREATION COMPLETED SUCCESSFULLY.");
			}
			userInfo.setUserInfoMap(userInfoMap);
			log.debug("*** TOTAL USER INFORMATION(userInfoMap, group, role)=[" + userInfoMap + "]");
		} catch (SQLException e) {
			throw new BaseException(e);
		}
		return userInfo;
	}

	private boolean getGroupInfo(Map userInfoMap) {
		String userTheme = (String) userInfoMap.get("theme");
		String userDefaultPage = (String) userInfoMap.get("default_page");
		String userSubPage = (String) userInfoMap.get("sub_page");

		// 그룹의 정보를 추출하여 userInfoMap에 할당한다.
		Map groupMap = siteUserManagerDAO.findGroup(userInfoMap);
		if (groupMap == null)
			return false;
		String group_principal_id = ((BigDecimal) groupMap.get("principal_id")).toString();
		String groupName = (String) groupMap.get("principal_name");
		String groupSubPage = (String) groupMap.get("sub_page");
		String groupShortPath = (String) groupMap.get("short_path");

		String groupTheme = (String) groupMap.get("theme");
		String groupDefaultPage = (String) groupMap.get("default_page");
		String groupSiteName = (String) groupMap.get("site_name");

		// userInfoMap에 groupPrincipalId와 groupName, groupId(shortPath)를 할당한다.
		userInfoMap.put("groupPrincipalId", "" + group_principal_id);
		userInfoMap.put("group_principal_id", "" + group_principal_id);
		userInfoMap.put("groupName", groupName);
		userInfoMap.put("groupId", groupShortPath);
		if (groupDefaultPage != null) {
			userInfoMap.put("groupDefaultPage", groupDefaultPage);
		} else {
			userInfoMap.put("groupDefaultPage", "/public/default-page");
		}

		// 사용자의 사번/학번, 조직(학과/부서)코드를 읽어 추가 한다.
		userInfoMap.put("emp_no", groupMap.get("emp_no"));
		userInfoMap.put("org_cd", groupMap.get("org_cd"));

		// 언어별 사용자명, 그룹명, 조직명을 추가한다.
		Iterator it = groupMap.keySet().iterator();
		String key;
		while (it.hasNext()) {
			key = (String) it.next();
			if (key.startsWith("user_name") || key.startsWith("group_name") || key.startsWith("org_name")) {
				userInfoMap.put(EnviewMap.convertKey(key), groupMap.get(key));
			}
		}

		// 3.2.4이하 하위버전과의 호환성을 위해서 groups 키값을 설정
		List groups = new ArrayList();
		List groupIdList = new ArrayList();
		userInfoMap.put("groupIdList", groupIdList);
		userInfoMap.put("groups", groups);
		groups.add(groupShortPath);
		groupIdList.add(group_principal_id);

		// 사용자의 테마가 지정 안되어 있는 경우 그룹의 테마를 쓰도록 한다.
		// 최종적으로 없는 경우에는 "enview"테마가 지정된다.
		if (userTheme == null || userTheme.length() == 0) {
			if (groupTheme != null) {
				userInfoMap.put("theme", groupTheme);
			} else {
				userInfoMap.put("theme", "enview");
			}
		}

		// 최종적으로 없는 경우에는 "public"사이트로 지정된다.
		if (groupSiteName != null && groupSiteName.length() > 0) {
			userInfoMap.put("site_name", groupSiteName);
		} else {
			userInfoMap.put("site_name", "/public");
		}

		// 사용자의 초기 페이지가 지정되지 않았으면, 그룹의 초기 페이지를 쓰도록 한다.
		// 최종적으로 없는 경우에는 "/public/default-page.page"가 지정된다.
		if (userDefaultPage == null || userDefaultPage.length() == 0) {
			if (groupDefaultPage != null) {
				userInfoMap.put("default_page", groupDefaultPage);
			} else {
				userInfoMap.put("default_page", "/public/default-page");
			}
		}

		// 사용자의 초기 서브(모바일) 페이지가 지정되지 않았으면, 그룹의 초기서브(모바일) 페이지를 쓰도록 한다.
		// 최종적으로 없는 경우에는 "/public/sub-page.page"가 지정된다.
		if (userSubPage == null || userSubPage.length() == 0) {
			if (groupSubPage != null) {
				userInfoMap.put("sub_page", groupSubPage);
			} else {
				userInfoMap.put("sub_page", "/default/sub-page");
			}
		}

		return true;
	}

	/**
	 * Principal 트리(Group/Role)를 순회하여 PrincipalList와 ShortPathList를 만든다.
	 * 
	 * @param parent
	 *            Role트리
	 * @param roleList
	 *            roleList
	 * @param roleIdList
	 *            roleIdList
	 * @param index
	 */
	private void traverseTreeForRole(TreeObject parent, List roleList, List roleIdList, int index) {
		for (Iterator it = parent.getChildren().iterator(); it.hasNext();) {
			TreeObject child = (TreeObject) it.next();
			Map childInfoMap = (Map) child.getAttribute();
			Map childTmpMap = new HashMap();
			childTmpMap.put("id", ((BigDecimal) childInfoMap.get("principal_id")).toString());
			childTmpMap.put("index", "" + index--);

			roleList.add(childTmpMap);
			roleIdList.add((String) childInfoMap.get("short_path"));

			traverseTreeForRole(child, roleList, roleIdList, index);
		}
	}

	/**
	 * 상위주체ID로 상위주체에 포함된 주체들을 조회하여 주체맵에 추가한다.<br>
	 * 사용자의 기본 주체(Group/Role)를 조회 후 각 추제의 상위 주체를 조회하는데 사용된다.
	 * 
	 * @param userInfoMap
	 *            사용자정보맵
	 * @param roleListMap
	 *            주체맵
	 * @param parentId
	 *            상위주체ID
	 */
	private void findParentRole(Map roleListMap, BigDecimal parentId) {
		log.debug("Finding parent role " + parentId);

		// 부모가 이미 존재하면 리턴
		if (roleListMap.containsKey(parentId)) {
			log.debug("==> Already exists");
			return;
		}

		try {
			Role role = roleManager.getRole(parentId.intValue());
			if (role != null) {
				Map roleMap = new HashMap();
				BigDecimal principalId = new BigDecimal(role.getPrincipalId());
				roleMap.put("principal_id", principalId);
				roleMap.put("parent_id", role.getParentId() == null ? null : new BigDecimal(role.getParentId()));
				roleMap.put("short_path", role.getShortPath());
				log.debug("==> adding role " + roleMap);
				TreeObject treeObject = new TreeObject();
				treeObject.setAttribute(roleMap);
				roleListMap.put(principalId, treeObject);

				if (role.getParentId() != null) {
					log.debug("==> parent exists " + role.getParentId());
					findParentRole(roleListMap, new BigDecimal(role.getParentId()));
				}
			} else {
				log.debug("==> role not found");
			}
		} catch (BaseException e) {
			log.error(e, e);
		}

		/*
		 * Map parentRoleMap = siteUserManagerDAO.findParentRole(parentId.toString()); // 상위 UserInfoMap에 설정된 값 parentId = (BigDecimal)parentRoleMap.get("parent_id"); BigDecimal principalId = (BigDecimal)parentRoleMap.get("principal_id");
		 * 
		 * if( roleListMap.containsKey(principalId) == true ) return; TreeObject treeObject = new TreeObject(); treeObject.setAttribute(parentRoleMap); roleListMap.put(principalId, treeObject);
		 * 
		 * // 상위 값이 존재하고 DB에 접근한 적이 없는 경우 재귀적 호출을 한다. if( parentId != null && roleListMap.containsKey(parentId) == false ) { findParentRole(roleListMap, parentId); }
		 */

	}

	private boolean getRoleInfo(Map userInfoMap) {
		List roleIdList = new ArrayList();
		List roles = new ArrayList();
		userInfoMap.put("roleIdList", roleIdList);
		userInfoMap.put("roles", roles);

		TreeObject rootRoleTreeObject = null;
		Map roleListMap = new HashMap();

		// 사용자의 역할(principalId와 parentId 조합)들을 Map에 담는다. - 사용자 첫번째 그룹의 롤, 개인 롤 전부
		// List roleMapList = siteUserManagerDAO.findRolesForGroup(userInfoMap);
		List roleMapList = siteUserManagerDAO.findRole(userInfoMap);
		if (roleMapList == null || roleMapList.size() == 0)
			return false;
		for (Iterator it = roleMapList.iterator(); it.hasNext();) {
			Map roleMap = (Map) it.next();
			BigDecimal rolePrincipalId = (BigDecimal) roleMap.get("principal_id");
			BigDecimal roleParentId = (BigDecimal) roleMap.get("parent_id");
			String roleId = (String) roleMap.get("short_path").toString();
			// if( roleListMap.containsKey(rolePrincipalId) == true ) continue;
			if (roleListMap.containsKey(rolePrincipalId))
				continue;
			TreeObject treeObject = new TreeObject();
			treeObject.setAttribute(roleMap);
			roleListMap.put(rolePrincipalId, treeObject);

			// 상위 값이 존재하고 DB에 접근한 적이 없는 경우 재귀적 호출을 한다.
			// if( roleParentId != null && roleListMap.containsKey(roleParentId) == false ) {
			if (roleParentId != null && !roleListMap.containsKey(roleParentId)) {
				findParentRole(roleListMap, roleParentId);
			}
		}

		// 사용자의 역할 Map을 분석하여 트리구조로 변환한다.
		for (Iterator it = roleListMap.keySet().iterator(); it.hasNext();) {
			BigDecimal id = (BigDecimal) it.next();
			TreeObject treeObject = (TreeObject) roleListMap.get(id);
			Map roleMap = (Map) treeObject.getAttribute();
			BigDecimal parentId = (BigDecimal) roleMap.get("parent_id");
			if (parentId != null) {
				TreeObject parentTreeObject = (TreeObject) roleListMap.get(parentId);
				parentTreeObject.addChild(treeObject);
				treeObject.setParent(parentTreeObject);
			} else {
				rootRoleTreeObject = treeObject;
			}
		}

		// 변환된 역할의 트리구조가 널이 아닐 때,
		if (rootRoleTreeObject != null) {
			int index = 9999;
			Map roleMap = (Map) rootRoleTreeObject.getAttribute();
			Map tmpMap = new HashMap();
			tmpMap.put("id", ((BigDecimal) roleMap.get("principal_id")).toString());
			tmpMap.put("index", "" + index--);
			roleIdList.add(tmpMap);
			roles.add((String) roleMap.get("short_path"));
			traverseTreeForRole(rootRoleTreeObject, roleIdList, roles, index);
		}
		return true;
	}

	/**
	 * 사용자가 사용가능한 메뉴를 조회한다.
	 */
	public List getAccessableMenu(Map userInfoMap) throws BaseException {
		List resultList = new ArrayList();
		if( userInfoMap==null) {
			return resultList;
		}
		/*
		 * // ROLE ONLY List roldIdList = (List) userInfoMap.get("roleIdList"); if (roldIdList != null && roldIdList.size() > 0) { results = siteUserManagerDAO.getPermissionForRole(userInfoMap); }
		 */
		// ROLE + USER
		List results = null;
		results = siteUserManagerDAO.getPermission(userInfoMap);

		Set allowMenus = new HashSet();
		Set denyMenus = new HashSet();
		int pos = 0;

		allowMenus.add("/");
		// 사용자의 역할에 허용된 페이지의 모든 path를 저장한다.
		// 반드시 "/xxx"이 있기 때문에 pathToken의 2개 이상이 나오고 pathToken[0]="" 이다.
		if (results != null) {
			for (Iterator it = results.iterator(); it.hasNext();) {
				Map resultMap = (Map) it.next();

				String resUrl = (String) resultMap.get("res_url");
				int resType = ((BigDecimal) resultMap.get("res_type")).intValue();
				int authCode = ((BigDecimal) resultMap.get("action_mask")).intValue();
				int allow = ((BigDecimal) resultMap.get("is_allow")).intValue();
				boolean isAllow = (allow == 1) ? true : false;

				if (resType == 0) {
					// if( isAllow == true ) {
					if (isAllow) {
						// 허용이면 허용목록에 추가(부모경로 포함)
						String[] pathToken = resUrl.split("/");
						String token = "";
						for (int i = 1; i < pathToken.length; i++) {
							token = token + "/" + pathToken[i];
							// if( allowMenus.contains(token) == false ) {
							if (!allowMenus.contains(token)) {
								allowMenus.add(token);
							}
						}
					} else {
						// 거부이면 거부목록에 추가
						denyMenus.add(resUrl);
					}
				}
			}
		}

		ArrayList denyList = new ArrayList();
		for (Iterator it = denyMenus.iterator(); it.hasNext();) {
			denyList.add((String) it.next());
		}

		// log.info("***** allowMenus=" + allowMenus);
		String path;
		boolean denied;
		for (Iterator it = allowMenus.iterator(); it.hasNext();) {
			path = (String) it.next();
			denied = false;
			// 주어진 패스가 거부 패스 하위패스이면 건너뛴다.
			for (int i = 0; i < denyList.size(); i++) {
				String denyPath = (String) denyList.get(i);
				if (path.startsWith(denyPath)) {
					denied = true;
					break;
				}
			}
			if (!denied) {
				resultList.add(path);
			}
		}

		return resultList;
	}

	/**
	 * 사용자 첫번째 그룹의 롤에 대한 허용권한 카이스트 메뉴 캐싱 추가 메소드 MenuSessionContext 에서 사용
	 */
	public List getAccessableMenuForFirstGroup(String domain, String firstGroupRoleId) throws BaseException {
		List resultList = new ArrayList();
		List results = siteUserManagerDAO.getPermission2(domain, firstGroupRoleId);

		Set allowMenus = new HashSet();
		int pos = 0;

		allowMenus.add("/");
		// 사용자의 역할에 허용된 페이지의 모든 path를 저장한다.
		// 반드시 "/xxx"이 있기 때문에 pathToken의 2개 이상이 나오고 pathToken[0]="" 이다.
		if (results != null) {
			for (Iterator it = results.iterator(); it.hasNext();) {
				Map resultMap = (Map) it.next();

				String resUrl = (String) resultMap.get("res_url");
				int resType = ((BigDecimal) resultMap.get("res_type")).intValue();
				int authCode = ((BigDecimal) resultMap.get("action_mask")).intValue();
				int allow = ((BigDecimal) resultMap.get("is_allow")).intValue();
				boolean isAllow = (allow == 1) ? true : false;

				if (resType == 0) {
					// if( isAllow == true ) {
					if (isAllow) {
						String[] pathToken = resUrl.split("/");
						String token = "";
						for (int i = 1; i < pathToken.length; i++) {
							token = token + "/" + pathToken[i];
							// if( allowMenus.contains(token) == false ) {
							if (!allowMenus.contains(token)) {
								allowMenus.add(token);
							}
						}
					}
				}
			}
		}

		// log.info("***** allowMenus=" + allowMenus);

		for (Iterator it = allowMenus.iterator(); it.hasNext();) {
			resultList.add((String) it.next());
		}

		return resultList;
	}

	/**
	 * 사용자에게 맵핑된 롤(그룹의 롤, 개인 롤 등등) 중에서 사용자 첫번째 그룹의 롤은 제외한 나머지 롤들의 허용권한 페이지를 가져옴 사용자 첫번째 그룹의 롤은 추후 캐싱 하여 합침 카이스트 메뉴 캐싱 추가 메소드 허걸
	 */
	public List getAccessableMenuWithOutFirstGroup(Map userInfoMap) throws BaseException {
		List resultList = new ArrayList();

		List results = null;

		List<HashMap> roleIdList = (List) userInfoMap.get("roleIdList");
		String firstGroupRoleId = (String) userInfoMap.get("firstGroupRoleId");

		userInfoMap.put("roleIdList", roleIdList);

		Set allowMenus = new HashSet();
		int pos = 0;

		allowMenus.add("/");

		if (results != null) {
			for (Iterator it = results.iterator(); it.hasNext();) {
				Map resultMap = (Map) it.next();

				String resUrl = (String) resultMap.get("res_url");
				int resType = ((BigDecimal) resultMap.get("res_type")).intValue();
				int authCode = ((BigDecimal) resultMap.get("action_mask")).intValue();
				int allow = ((BigDecimal) resultMap.get("is_allow")).intValue();
				boolean isAllow = allow == 1;

				if ((resType != 0) || (!isAllow))
					continue;
				String[] pathToken = resUrl.split("/");
				String token = "";
				for (int i = 1; i < pathToken.length; i++) {
					token = token + "/" + pathToken[i];
					if (!allowMenus.contains(token)) {
						allowMenus.add(token);
					}
				}
			}
		}

		for (Iterator it = allowMenus.iterator(); it.hasNext();) {
			resultList.add((String) it.next());
		}
		return resultList;
	}

	/**
	 * 사용자로그를 남긴다.
	 * 
	 * @param paramMap
	 *            사용자정보맵
	 */
	public void log(Map paramMap) throws BaseException {
		// try {
		siteUserManagerDAO.writeLog(paramMap);
		// } catch (BaseException e) {
		// log.error(e.getMessage(), e);
		// throw e;
		// }
	}

	/**
	 * 사용자로그를 남긴다.
	 * 
	 * @param paramMap
	 *            사용자정보맵
	 * @param status
	 *            상태
	 */
	public void log(Map paramMap, int status) throws BaseException {
		// try {
		paramMap.put("status", status);

		siteUserManagerDAO.writeLog(paramMap);
		// } catch (BaseException e) {
		// log.error(e.getMessage(), e);
		// throw e;
		// }
	}

	/**
	 * 비밀번호를 변경한다.
	 * 
	 * @param userId
	 *            사용자ID
	 * @param password
	 *            이전비밀번호
	 * @param passwordNew
	 *            신규비밀번호
	 */

	public void changePassword(String userId, String password, String passwordNew) throws UserException, BaseException {
		DomainInfo domain = Enview.getUserDomain();
		Map paramMap = new HashMap();
		paramMap.put("userId", userId);
		paramMap.put("domainId", domain.getDomainId());
		Map userInfoMap = siteUserManagerDAO.findUser(paramMap).toMap();
		String prevPassword = (String) userInfoMap.get("column_value");
		String encPasswd = passwordEncoder.encode(userId, password);

		// if( prevPassword.equals(encPasswd) == false ) {
		if (!prevPassword.equals(encPasswd)) {
			throw new UserException("ev.error.user.ErrorCode.2"); // 비밀번호 오류
		}

		paramMap.put("password", passwordEncoder.encode(userId, passwordNew));
		siteUserManagerDAO.changePassword(paramMap);
	}

	/**
	 * 사용자ID 중복체크를 한다.
	 * 
	 * @param userId
	 *            사용자 ID
	 * @return 중복여부
	 * @throws BaseException
	 */
	public boolean isOverlap(String userId) throws BaseException {
		return siteUserManagerDAO.isOverlap(userId);
	}

	/**
	 * 사용자정보를 추가한다.
	 * 
	 * @param userVO
	 *            사용자정보
	 * @throws UserException
	 */
	public void regist(UserVO userVO) throws UserException, BaseException {
		try {
			userVO.setPrincipalId(this.idGenerator.getNextIntegerId("SECURITY_PRINCIPAL"));
			userVO.setCredentialId(this.idGenerator.getNextIntegerId("SECURITY_CREDENTIAL"));
			userVO.setColumnValue(passwordEncoder.encode(userVO.getUserId(), userVO.getPassword()));
			userVO.setGroupId(Enview.getConfiguration().getString("default.user.group", "GuestGroup"));
			userVO.setRoleId(Enview.getConfiguration().getString("default.user.role", "user"));

			siteUserManagerDAO.regist(userVO, null, null);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			throw new UserException(e);
		} catch (BaseException e) {
			log.error(e.getMessage(), e);
			throw new UserException(e);
		}
	}

	/**
	 * 사용자정보를 수정한다.
	 * 
	 * @param userVO
	 *            사용자정보
	 * @throws UserException
	 */
	public void update(UserVO userVO) throws UserException, BaseException {
		try {
			siteUserManagerDAO.update(userVO, null, null);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			throw new UserException(e);
		}
	}

	/**
	 * 사용자의 도메인ID목록을 조회한다.
	 * 
	 * @param userId
	 *            사용자ID
	 * @return 도메인ID목록
	 */
	public List getDomainIds(String userId) throws EnfaceException {
		List domainIdList = new ArrayList();
		// try {
		domainIdList = siteUserManagerDAO.getDomainIdsByUserId(userId);
		// } catch (BaseException e) {
		// log.error(e.getMessage(), e);
		// throw new UserException(e);
		// }
		return domainIdList;
	}

	/**
	 * DB의 시간을 가져온다.
	 * 
	 * @throws BaseException
	 */
	public String getNowFromDB() throws BaseException {
		String now = null;
		// try {
		now = siteUserManagerDAO.getNow();
		// } catch (BaseException e) {
		// log.error(e.getMessage(), e);
		// }
		return now;
	}

	public String getLangKnd(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		UserInfo userInfo = null;
		String langKnd = request.getParameter("langKnd");
		if (langKnd == null) {
			langKnd = (String) session.getAttribute("langKnd");
		}
		if (langKnd == null) {
			langKnd = request.getLocale().getLanguage();
		}
		return langKnd;
	}

	public void checkAdminAccess2(HttpServletRequest request) throws UserException, BaseException {
		String ipAddress = HttpUtil.getClientIp(request);
		
		// localhost check;
		if("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
			return;
		}
	
		// userid check;
		String userId = request.getParameter("userId");
		
		if(! "admin".equals(userId)) {
			return;
		}
		
		SecurityPolicyServiceDAC securityPolicyServiceDAC = null;
		securityPolicyServiceDAC = (SecurityPolicyServiceDAC) Enview.getComponentManager().getComponent("com.saltware.enview.admin.security.service.SecurityPolicyService");
		List securityPolicyList = securityPolicyServiceDAC.findAll();
		if (securityPolicyList == null || securityPolicyList.size() == 0) {
			return;
		}
		
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		for (Iterator it = securityPolicyList.iterator(); it.hasNext();) {
			SecurityPolicy securityPolicy = (SecurityPolicy) it.next();
			String resUrl = securityPolicy.getResUrl();
			String pattern = securityPolicy.getIpAddress();
	
			if (securityPolicy.isPattern() == true) {
			} else {
			}
	
			if (pattern.indexOf("*") > -1) {
				pattern = pattern.replaceAll("[*]", "([0-9]|[0-9][0-9]|[0-9][0-9][0-9])");
			}
	
			if (ipAddress.matches(pattern)) {
				long current = System.currentTimeMillis();
				if (securityPolicy.getStartDate() <= current && current <= securityPolicy.getEndDate()) {
					if (securityPolicy.isAllow() == true) {
						return;
					}
				}
			}
		}
	
		throw new UserException("ev.error.user.ErrorCode.3001");
	}
	
	public void checkAdminAccess(HttpServletRequest request) throws UserException, BaseException {
		String ipAddress = HttpUtil.getClientIp(request);
		
		// localhost check;
		if("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
			return;
		}
		
		// userid check;
		String userId = request.getParameter("userId");
		
		if(! "admin".equals(userId)) {
			return;
		}
		

		try {
			SecurityPolicyManager sp = (SecurityPolicyManager) Enview.getComponentManager().getComponent("com.saltware.enview.security.impl.SecurityPolicyManager");
			sp.checkAccess(ipAddress, request.getRequestURI(), "1");
		} catch (AccessControlException e) {
			throw new UserException("ev.error.user.ErrorCode.3001");
		}

	}

	/**
	 * 사용자를 인증하고 로그인 프로세스를 진행한다.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param formData
	 *            UserForm
	 * @return ModelAndView
	 * @throws BaseException
	 */
	public boolean processLogin(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(true);

		UserInfo userInfo = null;
		String langKnd = getLangKnd(request);
		session.setAttribute("langKnd", langKnd);

		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);

		String userId = null;

		try {
			userInfo = EnviewSSOManager.getUserInfo(request);
			if (userInfo != null && userInfo.getUserId() != null) {
				return true;
			}

			boolean adminPolicyCheck = Enview.getConfiguration().getBoolean("security.admin.policy.check", false);
			if (adminPolicyCheck) {
				checkAdminAccess(request);
			}
			
			String clientIp = HttpUtil.getClientIp(request);
			String clientAgent = HttpUtil.getUserAgent(request);

			String encodedPwd = null;
			// 자동로그인ID가 있는지 확인한다.
			String autoLoginId = StringUtil.isNullTrim((String) session.getAttribute(LoginConstants.AUTO_LOGIN_ID));
			if (autoLoginId.length() > 0) {
				// 자동로그인 인증
				// 사용자ID로 사용자 존재여부, 사용가능여부, 비밀번호오류를 체크한다.
				userId = autoLoginId.trim();
				authenticate(userId);
			} else {
				// ID/PW 인증
				String encorderPassword = null;
				String password = null;
				userId = request.getParameter("userId");
				encorderPassword = request.getParameter("password");

				// 아이디 체크
				if (userId == null || userId.equals("")) {
					throw new UserException("ev.error.user.ErrorCode.EmptyUserid");
				}

				// 비밀번호 체크
				if (encorderPassword == null || encorderPassword.equals("")) {
					throw new UserException("ev.error.user.ErrorCode.EmptyPassword");
				}
				
				// 보안문자를 사용하는 경우
				// 값이 있으면 보안문자를 화면에 표시했음 -> 보안문자 체크  
				if( "true".equals(Enview.getConfiguration().getString("sso.login.captcha", "false")))  {
					String correctAnswer = (String)session.getAttribute(CAPTCHA_ANSWER);
					String answer = request.getParameter("answer");
					if (answer == null || answer.equals("")) {
						throw new UserException("ev.error.user.ErrorCode.EmptyAnswer");
					}
					if( ! answer.equals(correctAnswer )) {
						log.error( "answer=" + answer + ",correct answer=" + correctAnswer);
						throw new UserException("ev.error.user.ErrorCode.WrongAnswer");
					}
				}

				if (encorderPassword != null) {
					boolean enableInterEncryption = Enview.getConfiguration().getBoolean("sso.interEncryption", true);
					if (enableInterEncryption) {
						byte[] value = encorderPassword.getBytes();
						for (int i = 0; i < value.length; i++) {
							value[i] = (byte) (value[i] + ((i + 2) % 7));
						}
						password = new String(value);
					} else {
						password = encorderPassword;
					}
				}

				encodedPwd = authenticate(userId, password, clientIp);
			}
			
			
			// 세션고정공격 방어를 위해 세션을 다시 만든다. 2017.9.22  
			session = regenerateSession(request);

			session.setAttribute(LoginConstants.SSO_LOGIN_ID, userId);
			
			userInfo = getUserInfomationHandler().createUserInfomation(request, response, userId, null);
			

			userInfo.setString("sid", session.getId());

			Map paramMap = new HashMap();
			paramMap.put("userId", userId);
			paramMap.put("status", "" + PortalStatistics.STATUS_LOGGED_IN);
			paramMap.put("remoteAddress", HttpUtil.getClientIp(request));
			paramMap.put("userAgent", HttpUtil.getUserAgent(request));
			log(paramMap);

			// 마지막 로그인 시간을 업데이트
			updateLastLogin(paramMap);

			session.setAttribute(LoginConstants.USERNAME, userInfo.getUserName());
			session.setAttribute("langKnd", langKnd);
			
			// IP보안 2단계 처리를 위해 최근에 로그인한 IP목록을 가져온다.
			session.setAttribute("loginIps", siteUserManagerDAO.getLoginIps(userId));
						
			// 로그인 마일리지 추가
			ConnectionContext connCtxt = null;
			try {
				connCtxt = new ConnectionContextForRdbms();
				MileageDAO mileDAO = (MileageDAO) DAOFactory.getInst().getDAO(MileageDAO.DAO_NAME_PREFIX);
				AdminMileForm amForm = new AdminMileForm();
				amForm = new AdminMileForm();
				amForm.setMileCd("EB_LOGIN");
				amForm.setLoginInfo(userInfo.getUserInfoMap());
				mileDAO.saveMileage(amForm, connCtxt);
				connCtxt.commit();
			} catch (SQLException e) {
				throw new BaseException(e);
			} catch (com.saltware.enboard.exception.BaseException e) {
				throw new BaseException(e);
			} finally {
				if (connCtxt != null) {
					connCtxt.release();
				}
			}

		} catch (UserException se) {
			String msgKey = se.getMessageKey();
			if (msgKey != null) {
				String errorMessage = enviewMessages.getString(msgKey);
				request.setAttribute("errorMessage", errorMessage);
				log.debug("*** errorMessage=" + errorMessage);
				// if ("ev.error.user.ErrorCode.1".equals(msgKey)) {
				// 사용자 없음
				// } else if ("ev.error.user.ErrorCode.2".equals(msgKey)) {
				if ("ev.error.user.ErrorCode.2".equals(msgKey) ) {
					// 비밀번호 오류
					Map paramMap = new HashMap();
					paramMap.put("userId", userId);
					paramMap.put("status", "" + PortalStatistics.STATUS_ERROR_PASSWORD);
					paramMap.put("remoteAddress", HttpUtil.getClientIp(request));
					paramMap.put("userAgent", HttpUtil.getUserAgent(request));

					try {
						updateAuthFailure(paramMap);
						log(paramMap);
					} catch (BaseException e) {
						log.error(e, e);
					}
				} else if ("ev.error.user.ErrorCode.15".equals(msgKey) ) {
						// 해외IP차단
						Map paramMap = new HashMap();
						paramMap.put("userId", userId);
						paramMap.put("status", "" + PortalStatistics.STATUS_ERROR_OVERSEAS_IP_BLOCKED);
						paramMap.put("remoteAddress", HttpUtil.getClientIp(request));
						paramMap.put("userAgent", HttpUtil.getUserAgent(request));
						try {
							log(paramMap);
						} catch (BaseException e) {
							log.error(e, e);
						}
				} // else if ("ev.error.user.ErrorCode.3".equals(msgKey)) {
					// 비활성 ID
				// } else if ("ev.error.user.ErrorCode.8".equals(msgKey)) {
				// 비밀번호 오류횟수 초과
				// }
			}
			return false;
		} catch (BaseException e) {
			log.error(e.getMessage(), e);
			return false;
		} finally {
			// 보안문자를 화면에 보일때 세션에 보관한 보안문자를  제거한다
			session.removeAttribute(CAPTCHA_ANSWER);
		}

		return true;
	}

	/**
	 * RememberMe와 관련하여 자동 로그인 처리를 해준다.
	 * 
	 * @param paramMap
	 */
	public void autoLogin(String userId, HttpServletRequest request, HttpServletResponse response) throws BaseException {
		UserInfo userInfo = null;
		String langKnd = request.getParameter("langKnd") == null ? request.getLocale().getLanguage() : request.getParameter("langKnd");
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);

		HttpSession session = request.getSession(true);

		try {
			session.setAttribute(LoginConstants.SSO_LOGIN_ID, userId);

			userInfo = getUserInfomationHandler().createUserInfomation(request, response, userId, null);
			userInfo.setString("sid", session.getId());

			Map paramMap = new HashMap();
			paramMap.put("userId", userId);
			paramMap.put("status", "" + PortalStatistics.STATUS_LOGGED_IN);
			paramMap.put("remoteAddress", HttpUtil.getClientIp(request));
			paramMap.put("userAgent", HttpUtil.getUserAgent(request));
			paramMap.put("orgCd", userInfo.getGroupId());
			paramMap.put("orgNm", userInfo.getGroupId());
			paramMap.put("userNm", userInfo.getUserName());
			paramMap.put("principal_id", userInfo.getUserInfoMap().get("principal_id"));
			paramMap.put("sessionId", session.getId());

			// 로그인 로그 추가
			log(paramMap);

			session.setAttribute(LoginConstants.USERNAME, userInfo.getUserName());
			session.setAttribute("langKnd", langKnd);

		} catch (BaseException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 사용자정보에 세션ID를 설정한다.
	 * 
	 * @param paramMap
	 */
	public void updateSessionId(Map paramMap) {
		siteUserManagerDAO.updateSessionId(paramMap);
	}
	
	/**
	 * 사용자정보에 세션ID를 설정한다.
	 * 
	 * @param paramMap
	 */
	public void logoutSessionId(Map paramMap) {
		siteUserManagerDAO.logoutSessionId(paramMap);
	}

	/**
	 * 사용자정보에서 세션ID를 읽어온다.
	 * 
	 * @return
	 */
	public Map getSessionId(String userId) {
		return (Map)siteUserManagerDAO.getSessionId(userId);

	}
	
	public int updateCurrentSession( Map paramMap) throws BaseException{
		return siteUserManagerDAO.updateCurrentSession(paramMap);
	}

	/**
	 * 사용자정보에 마지막 로그인 IP와 일자를 업데이트한다
	 * 
	 * @param paramMap
	 */
	public void updateLastLogin(Map paramMap) {
		// System.out.println( paramMap);
		siteUserManagerDAO.updateLastLogin(paramMap);
	}

	/**
	 * 사용자를 동기화한다.
	 */
	public void syncUsers(Map paramMap) throws BaseException {
		//
	}

	public PasswordEncoder getOldPasswordEncoder() {
		return oldPasswordEncoder;
	}

	public void setOldPasswordEncoder(PasswordEncoder oldPasswordEncoder) {
		this.oldPasswordEncoder = oldPasswordEncoder;
	}
	
	/**
	 * 세션을 재생성한다. 세션고정공격을 방어하기위해 로그인시 세션을 새로 생성한다.
	 * @param request
	 * @return
	 */
	public HttpSession regenerateSession( HttpServletRequest request) {
		// 세션을 복제할때 예외처리 
		// com.saltware.enview.container.session.PortalSessionMonitor : 포탈에서 세션을 모니터링할때 쓴다. 자체에 현재 세션정보를 가지고 있으므로 복제 대상에서 뺀다.
		String[] filterList = {"com.saltware.enview.container.session.PortalSessionMonitor"};
		
		HttpSession oldSession = request.getSession( false);
		if( oldSession==null) {
			// 세션이 없으면 새로 만드어 리턴한다.
			return request.getSession();
		}

		// 세션이 이미 존재하면 예전 세션의 값을 보관한 뒤 새로 세션을 만들고 보관한 값을 추가한다. 이 과정에서 세션아이디가 변경된다.  
		Map attrs = new HashMap();
		String key;
		Enumeration en = oldSession.getAttributeNames();
		while( en.hasMoreElements()) {
			key = (String)en.nextElement();
			if( !ArrayUtils.contains( filterList, key)) {
				attrs.put( key, oldSession.getAttribute(key)  );
			}
		}
		oldSession.invalidate();
		HttpSession newSession = request.getSession(true);
		Iterator it = attrs.keySet().iterator();
		while( it.hasNext()) {
			key = (String)it.next();
			newSession.setAttribute( key, attrs.get(key));
		}
		return newSession;
	}
	
	public UserVO findUser( String userId) {
			Map paramMap = new HashMap();
			paramMap.put("userId", userId);

			if (domainRule == HOST_DOMAIN) {
				// 호스트 도메인이 먼저이면 현재 도메인에서 사용자를 찾는다.
				DomainInfo domain = Enview.getUserDomain();
				paramMap.put("domainId", domain.getDomainId());
			}
			return siteUserManagerDAO.findUser(paramMap);
	}
	
	public int updateBlockAbroad( String userId, String blockAbroad) {
		Map paramMap = new HashMap();
		paramMap.put("userId", userId);
		paramMap.put("blockAbroad", blockAbroad);
		return siteUserManagerDAO.updateBlockAbroad(paramMap);		
	}
	
}
