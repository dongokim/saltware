package com.saltware.enface.user.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saltware.enface.user.service.impl.UserException;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.security.CommonUserManager;
import com.saltware.enview.security.UserInfo;

public interface SiteUserManager extends CommonUserManager {

	/**
	 * id와비밀번호, IP주소로 사용자를 인증한다.
	 * @param userId 사용자ID
	 * @param passwd 비밀번호
	 */
	public String authenticate(String userId, String passwd, String clientIp) throws UserException, BaseException;

	/**
	 * id와비밀번호로 사용자를 인증한다.
	 * @param userId 사용자ID
	 * @param passwd 비밀번호
	 */
	public String authenticate(String userId, String passwd) throws UserException, BaseException;
	
	/**
	 * id로 사용자를 인증한다.
	 * @param userId 사용자ID
	 */
	public void authenticate(String userId) throws UserException, BaseException;

	/**
	 * 인증실패건수를 증가시킨다.
	 * @param paramMap
	 * @throws UserException
	 */
	public void updateAuthFailure(Map paramMap) throws UserException;
	
	/**
	 * 인증실패건수를 초기화한다.
	 * @param paramMap
	 * @throws UserException
	 */
	public void initAuthFailure(Map paramMap) throws UserException;
	
	/**
	 * 사용자정보를 읽는다.
	 */
	public UserInfo getUserInfo(String userId, String groupId, String langKnd) throws UserException, BaseException;
	
	
	public UserInfo getUserInfo(String userId, String domainId,  String langKnd, boolean isCheckPermission) throws BaseException;
	
	/**
	 * 사용자정보맵을 로그에 남긴다
	 * @param paramMap 맵정보
	 * @throws UserException
	 * @throws BaseException
	 */
	public void log(Map paramMap) throws UserException, BaseException;
	
	/**
	 * 비밀번호를 변경한다.
	 * @param userId 사용자ID
	 * @param password 비밀번호
	 * @param passwordNew
	 * @throws UserException
	 * @throws BaseException
	 */
	public void changePassword(String userId, String password, String passwordNew) throws UserException, BaseException;
	
	/**
	 * 이미 존재하는 ID인지 검증한다.
	 * @param userId 사용자ID
	 * @return 존재여부
	 * @throws BaseException
	 */
	public boolean isOverlap(String userId) throws BaseException;

	/**
	 * 사용자를 등록한다.
	 * @param userVO 사용자정보
	 * @throws UserException
	 * @throws BaseException
	 */
	public void regist(UserVO userVO) throws UserException, BaseException;
	
	
	/**
	 * 사용자를 수정한다
	 * @param userVO 사용자정보
	 * @throws UserException
	 * @throws BaseException
	 */
	public void update(UserVO userVO) throws UserException, BaseException;

	/**
	 * DB의 시간을 가져온다.
	 * @throws BaseException
	 */
	public String getNowFromDB() throws BaseException;

	public List getAccessableMenuForFirstGroup(String domainId, String firstGroupRoleId) throws BaseException;
	

	/**
	 * 사용자를 인증하고 로그인 프로세스를 진행한다.
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData UserForm
	 * @return ModelAndView
	 * @throws BaseException
	 */
	public boolean processLogin(HttpServletRequest request, HttpServletResponse response) throws BaseException;
	
	/**
	 * 사용자정보에 세션ID를 설정한다.
	 * @param paramMap
	 */
	public void updateSessionId( Map paramMap);
	
	/**
	 * 사용자정보에 세션ID를 설정한다.
	 * @param paramMap
	 */
	public void logoutSessionId( Map paramMap);

	/**
	 * 사용자정보에서 세션ID를 읽어온다.
	 * @return
	 */
	public Map getSessionId( String userId);
	
	/**
	 * 사용자정보에 마지막 로그인 IP와 일자를 업데이트한다
	 * @param paramMap
	 */
	public void updateLastLogin( Map paramMap);
	
	
	public void syncUsers( Map paramMap) throws BaseException;
	
	public UserVO findUser( String userId)  throws BaseException;
	
	public int updateBlockAbroad( String userId, String blockAbroad) throws BaseException;
	
	public int updateCurrentSession( Map paramMap )  throws BaseException;
	

	
}
