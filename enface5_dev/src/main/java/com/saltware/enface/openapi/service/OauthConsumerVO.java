package com.saltware.enface.openapi.service;

import java.io.Serializable;

/**  
 * OPEN_API_APP 테이블 객체
 * @author psw
 * @since 2012.07.26 13:2:324
 * @version 1.0
 */
public class OauthConsumerVO implements Serializable
{
	// fieldsSr
	private String oauthSns = null;
	private String consuMerKey = null;
	private String consuMerSecret = null;
	
	/**
	 * SNS의 타입을 리턴한다
	 * @return oauthSns SNS타입
	 */
	public String getOauthSns() {
		return oauthSns;
	}
	
	/**
	 * SNS의 타입을 설정한다
	 * @param oauthSns SNS타입
	 */
	public void setOauthSns(String oauthSns) {
		this.oauthSns = oauthSns;
	}
	
	/**
	 * 개발자 컨슈머키를 리턴한다.
	 * @return consuMerKey 컨슈머키
	 */
	public String getConsuMerKey() {
		return consuMerKey;
	}
	
	/**
	 * 개발자 컨슈머키를 입력한다
	 * @param consuMerKey 컨슈머키
	 */
	public void setConsuMerKey(String consuMerKey) {
		this.consuMerKey = consuMerKey;
	}
	
	/**
	 * 개발자 컨슈머 시크릿키를 리턴한다
	 * @return consuMerSecret 컨슈머 시크릿키
	 */
	public String getConsuMerSecret() {
		return consuMerSecret;
	}
	
	/**
	 * 개발자 컨슈머 시크릿키를 입력한다 
	 * @param consuMerSecret 컨슈머 시크릿키
	 */
	public void setConsuMerSecret(String consuMerSecret) {
		this.consuMerSecret = consuMerSecret;
	}
}
