package com.saltware.enface.openapi.service;

import java.io.Serializable;

/**  
 * OPEN_API_ADDRESS 테이블 객체
 * @author psw
 * @since 2012.07.26 13:2:324
 * @version 1.0
 */
public class OauthUrlVO implements Serializable
{
	// fields
	private String oauthSns = null;
	private String apiUrl=null;
	private String requestTokenUrl=null;
	private String authorizeUrl=null;
	private String accessTokenUrl=null;
	private String callBackUrl=null;
	
	/**
	 * SNS 타입을 리턴한다
	 * @return oauthSns SNS 타입
	 */
	public String getOauthSns() {
		return oauthSns;
	}
	/**
	 * SNS 타입울 설정한다
	 * @param oauthSns SNS 타입
	 */
	public void setOauthSns(String oauthSns) {
		this.oauthSns = oauthSns;
	}
	
	/**
	 * SNS의 API_URL을 리턴한다
	 * @return apiUrl SNS의 API_URL
	 */
	public String getApiUrl() {
		return apiUrl;
	}
	
	/**
	 * SNS의 API_URL을 설정한다
	 * @param apiUrl SNS의 API_URL
	 */
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	/**
	 * SNS의 RequestTokenUrl을 설정한다
	 * @return requestTokenUrl SNS의 RequestTokenUrl
	 */
	public String getRequestTokenUrl() {
		return requestTokenUrl;
	}
	
	/**
	 * SNS의 RequestTokenUrl을 설정한다
	 * @param requestTokenUrl SNS의 RequestTokenUrl
	 */
	public void setRequestTokenUrl(String requestTokenUrl) {
		this.requestTokenUrl = requestTokenUrl;
	}
	
	/**
	 * SNS 요청  AuthorizeUrl을  리턴한다
	 * @return authorizeUrl SNS의 AuthorizeUrl
	 */
	public String getAuthorizeUrl() {
		return authorizeUrl;
	}
	
	/**
	 * SNS 요청 AuthorizeUrl을  설정한다
	 * @param authorizeUrl SNS의 AuthorizeUrl
	 */
	public void setAuthorizeUrl(String authorizeUrl) {
		this.authorizeUrl = authorizeUrl;
	}
	
	/**
	 * SNS의  AccessTokenUrl을  리턴한다
	 * @return accessTokenUrl SNS의  AccessTokenUrl
	 */
	public String getAccessTokenUrl() {
		return accessTokenUrl;
	}
	
	/**
	 * SNS의  AccessTokenUrl을  설정한다
	 * @param accessTokenUrl SNS의   AccessTokenUrl
	 */
	public void setAccessTokenUrl(String accessTokenUrl) {
		this.accessTokenUrl = accessTokenUrl;
	}
	
	/**
	 * SNS의  CallBackUrl을  리턴한다
	 * @return callBackUrl SNS의  CallBackUrl
	 */
	public String getCallBackUrl() {
		return callBackUrl;
	}
	
	/**
	 * SNS의  CallBackUrl을  설정한다
	 * @param callBackUrl SNS의 callBackUrl
	 */
	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}
	
	
	
}
