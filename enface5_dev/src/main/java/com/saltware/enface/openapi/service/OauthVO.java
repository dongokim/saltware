package com.saltware.enface.openapi.service;


/**  
 * OPEN_API 테이블 객체(인증정보 )
 * @author psw
 * @since 2012.07.26 13:2:324
 * @version 1.0
 */

public class OauthVO{
	
	/**
	 * 사용자에 대한 SNS의 종류의 정보를 입력하거나 가져온다
	 * 2011.07.26 Sangwoo. Saltware.
	 */
	// fields
	private String userId = null;
	private String accessToken = null;
	private String accessSecret = null;
	private String m2UserId = null;
	private String oauthSns = null;

	/**
	 * OauthVO 생성자 
	 */
	public OauthVO() {
	}

	/**
	 *  사용자 ID를 리턴한다
	 *  @return userId 사용자id
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * 사용자 ID설정을 한다
	 * @param userId 사용자id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * 엑서스토큰을 리턴한다
	 * @return accessToken 엑서스토큰 값
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * 엑서스토큰을 설정한다
	 * @param accessToken 엑서스토큰값
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * 엑서스시크릿 리턴한다
	 * @return accessSecret 엑서스시크릿 값
	 */
	public String getAccessSecret() {
		return accessSecret;
	}

	/**
	 * 엑서스시크릿  설정한다
	 * @param accessSecret 엑서스시크릿 값
	 */
	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}

	/**
	 * 미투데이 사용자id 리턴한다
	 * @return m2UserId 미투데이터 사용자 id
	 */
	public String getM2UserId() {
		return m2UserId;
	}

	/**
	 * 미투데이 사용자id 설정한다
	 * @param m2UserId 미투데이터 사용자 id
	 */
	public void setM2UserId(String m2UserId) {
		this.m2UserId = m2UserId;
	}

	/**
	 * SNS 타입을  리턴한다
	 * @return oauthSns SNS타입
	 */
	public String getOauthSns() {
		return oauthSns;
	}

	/**
	 * SNS 타입을 설정한다
	 * @param oauthSns SNS타입
	 */
	public void setOauthSns(String oauthSns) {
		this.oauthSns = oauthSns;
	}	
}
