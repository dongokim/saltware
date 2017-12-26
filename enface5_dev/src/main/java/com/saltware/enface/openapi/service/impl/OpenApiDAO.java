package com.saltware.enface.openapi.service.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.saltware.enface.openapi.service.OauthConsumerVO;
import com.saltware.enface.openapi.service.OauthUrlVO;
import com.saltware.enface.openapi.service.OauthVO;

/**  
 * OpenApiDAO
 * @author psw
 * @since 2012.07.26 13:2:324
 */
public class OpenApiDAO extends SqlMapClientDaoSupport
{
	/**
	 * 등록된 SNS가 있는 여부를 확인 사용자 Oauth정보를  리턴한다
	 * @param openApiVO 사용자에 대한 openapi정보
	 * @return OauthVO 인증정보
	 * @throws DataAccessException
	 */
	public OauthVO Oauth(OauthVO openApiVO) throws DataAccessException
	{
		//System.out.println("Oauth");
		return (OauthVO)getSqlMapClientTemplate().queryForObject("oauth.serch", openApiVO);
	}
	
	/**
	 * SNS의 Oauth_URL정보를  리턴한다
	 * @param openApiOauthUrlVo SNS타입을 가지고 있는 객체
	 * @return OauthUrlVO 인증주소
	 * @throws DataAccessException
	 */
	public OauthUrlVO Oauth_Url(OauthUrlVO openApiOauthUrlVo) throws DataAccessException
	{
		//System.out.println("Oauth_Url");
		return (OauthUrlVO)getSqlMapClientTemplate().queryForObject("oauth.url", openApiOauthUrlVo);
	}
	
	/**
	 * SNS의 Consumer정보를 가져온다 (개발자)
	 * @param OpenApiOauthConsumerVo SNS타입을 가지고 있는 객체
	 * @return OauthConsumerVO  개발자 컨슈머 정보
	 * @throws DataAccessException
	 */
	public OauthConsumerVO Oauth_Consumer(OauthConsumerVO OpenApiOauthConsumerVo) throws DataAccessException
	{
		//System.out.println("Oauth_Consumer");
		return (OauthConsumerVO)getSqlMapClientTemplate().queryForObject("oauth.consumer", OpenApiOauthConsumerVo);
	}

	/**
	 * SNS로 부터 인증된 토큰을 추가한다
	 * @param openApiVO 인증정보
	 * @throws DataAccessException
	 */
	public void Oauth_AccessTokenInser(OauthVO openApiVO) throws DataAccessException
	{
		//System.out.println("Oauth_AccessTokenInser");
		getSqlMapClientTemplate().insert("accesstoken.insert", openApiVO);
	}
	
	/**
	 * SNS로 부터 인증이 완료된 토큰 재 인증하여 업데이트 한다
	 * @param openApiVO 인증정보
	 * @throws DataAccessException
	 */
	public void Oauth_Re_AccessTokenUpdate(OauthVO openApiVO) throws DataAccessException
	{
		//System.out.println("Oauth_AccessTokenUpdate");
		getSqlMapClientTemplate().update("accesstoken.update", openApiVO);
	}
}