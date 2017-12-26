package com.saltware.enface.openapi.service;

import com.saltware.enview.exception.BaseException;

/**  
 * OPEN_API 테이블 객체(인증정보 )
 * @author psw
 * @since 2012.07.26 13:2:324
 * @version 1.0
 */

public class OauthInfo
{
	/***
	 * OpenApi 주소를 리턴한다.
	 * @param openApiService openApi서비스
	 * @param oauthSns SNS타입
	 * @return oauth_url 인증주소
	 * @throws BaseException
	 */ 
	public OauthUrlVO Oauth_requst_url(OpenApiService openApiService, String oauthSns) throws BaseException 
	{
		OauthUrlVO oauth_url = new OauthUrlVO();
		oauth_url.setOauthSns(oauthSns);
		oauth_url = openApiService.Oauth_Url(oauth_url);
		return oauth_url;
	}
	
	/**
	 * 개발자 키 DB를 리턴한다.
	 * @param openApiService openApi서비스
	 * @param oauthSns SNS타입
	 * @return oauth_consumer 개발자 컨슈머 정보
	 * @throws BaseException
	 */
	public OauthConsumerVO Oauth_Consumer(OpenApiService openApiService, String oauthSns) throws BaseException 
	{
		OauthConsumerVO oauth_consumer = new OauthConsumerVO();
		oauth_consumer.setOauthSns(oauthSns);
		oauth_consumer = openApiService.Oauth_Consumer(oauth_consumer);
		return oauth_consumer;
	}
	
	/**
	 * 발급받은 AcessToken 등록한다.
	 * @param userId 사용자id
	 * @param accessToken 엑서스 토큰
	 * @param accessSecret 엑서스 시크릿
	 * @param m2_userId 미투데이 id
	 * @param oauthSns SNS타입
	 * @param openApiService openApi서비스
	 * @throws BaseException
	 */
	public void Oauth_AccessTokenSave(String userId, String accessToken, String accessSecret, String m2_userId, String oauthSns, OpenApiService openApiService) throws BaseException 
	{
	     OauthVO oauthVo = new OauthVO();
	     oauthVo.setUserId(userId);
	     oauthVo.setAccessToken(accessToken);
	     oauthVo.setAccessSecret(accessSecret);
	     oauthVo.setM2UserId(m2_userId);
	     oauthVo.setOauthSns(oauthSns);
	     openApiService.Oauth_AccessTokenInsert(oauthVo);
	}
	
	/**
	 * 인증키가 만료된경우 인증키를 새로 발급을 받는다.
	 * @param userId 사용자id
	 * @param accessToken 엑서스 토큰
	 * @param accessSecret 엑서스 시크릿 
	 * @param m2_userId 미투데이 id
	 * @param oauthSns SNS타입
	 * @param openApiService openApi서비스
	 * @throws BaseException
	 */
	public void Oauth_Re_AccessTokenSave(String userId, String accessToken, String accessSecret, String m2_userId, String oauthSns, OpenApiService openApiService) throws BaseException 
	{
	     OauthVO oauthVo = new OauthVO();
	     oauthVo.setUserId(userId);
	     oauthVo.setAccessToken(accessToken);
	     oauthVo.setAccessSecret(accessSecret);
	     oauthVo.setM2UserId(m2_userId);
	     oauthVo.setOauthSns(oauthSns);
	     openApiService.Oauth_Re_AccessTokenUpdate(oauthVo);
	}
}
