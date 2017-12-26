package com.saltware.enface.openapi.service;

import com.saltware.enview.exception.BaseException;

/**  
 * OPEN_API 테이블 객체(인증정보 )
 * @author psw
 * @since 2012.07.26 13:2:324
 * @version 1.0
 */
public interface OpenApiService
{
	/**
	 * 등록된 SNS가 있는 여부를 확인 사용자 Oauth정보를  리턴한다
	 * @param oauthvo 인증정보
	 * @return oauthVo2 인증정보
	 * @throws BaseException
	 */
	public OauthVO Oauth(OauthVO oauthvo) throws BaseException;
	
	
	/**
	 *  SNS의 Oauth_URL정보를  리턴한다
	 * @param oauthurlvO  인증주소
	 * @return oauthUrlVo2 인증주소
	 * @throws BaseException
	 */
	public OauthUrlVO Oauth_Url(OauthUrlVO oauthurlvO) throws BaseException;
	
	/**
	 * SNS의 Consumer(개발자)정보를 리턴한다
	 * @param oauthconsumervo 개발자 컨슈머정보
	 * @return oauthConsumerVo2 개발자 컨슈머 정보
	 * @throws BaseException
	 */
	public OauthConsumerVO Oauth_Consumer(OauthConsumerVO oauthconsumervo) throws BaseException;
	
	/**
	 * SNS의 인증후 AccessToken을 등록한다
	 * @param oauthvo 
	 * @throws BaseException
	 */
	public void Oauth_AccessTokenInsert(OauthVO oauthvo) throws BaseException;
	
	/**
	 * SNS인증이 만료된것일 경우 AccessToken을 업데이트 한다
	 * @param oauthvo 인증정보
	 * @throws BaseException
	 */
	public void Oauth_Re_AccessTokenUpdate(OauthVO oauthvo) throws BaseException;
}
