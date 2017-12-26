package com.saltware.enface.openapi.service.impl;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;
import com.saltware.enface.openapi.service.OauthConsumerVO;
import com.saltware.enface.openapi.service.OauthUrlVO;
import com.saltware.enface.openapi.service.OauthVO;
import com.saltware.enface.openapi.service.OpenApiService;
import com.saltware.enview.Enview;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.idgenerator.IdGenerator;

/**  
 * OpenApiServiceImpl
 * @author psw
 * @since 2012.07.26 13:2:324
 * @version 1.0
 */
public class OpenApiServiceImpl implements OpenApiService
{
	private final Log log = LogFactory.getLog(getClass());
	private IdGenerator idGenerator = null;
	private OpenApiDAO openApiDAO;

	/**
	 * OpenApiServiceImpl 생성자 
	 */
	public OpenApiServiceImpl()
	{
		this.idGenerator = (IdGenerator)Enview.getComponentManager().getComponent("IdGenerator");
	}
	
	/**
	 * openApiDAO를 리턴한다
	 * @return openApiDAO openApiDAO객체
	 */
	public OpenApiDAO getOpenApiDAO() 
	{
		return openApiDAO;
	}

	/**
	 * openApiDAO를 설정한다
	 * @param OpenApiDAO openApiDAO객체
	 */
	public void setOpenApiDAO(OpenApiDAO OpenApiDAO) 
	{
		openApiDAO = OpenApiDAO;
	}
	
    /**
     * 등록된 SNS가 있는 여부를 확인 사용자 Oauth정보를  리턴한다
     * @return oauthVo2 인증정보
     */
	public OauthVO Oauth(OauthVO oauthVo)
	{		
		//System.out.println("Oauth Impl");
		OauthVO oauthVo2 = null;
		
//	   try {	
		oauthVo2 = openApiDAO.Oauth(oauthVo);
//		}catch (BaseException e) {
//			log.error( e, e);
//		}
	   return oauthVo2;
	}

	/**
	 *  SNS의 Oauth_URL정보를  리턴한다
	 *  @return oauthUrlVo2 인증주소
	 */
	public OauthUrlVO Oauth_Url(OauthUrlVO oauthUrlVo) throws BaseException 
	{
		//System.out.println("Oauth_Url Impl");		
		OauthUrlVO oauthUrlVo2 = null;
//		try {
			oauthUrlVo2 = openApiDAO.Oauth_Url(oauthUrlVo);
//		}catch (BaseException e) {
//			log.error( e, e);
//		}
		return oauthUrlVo2;
	}
	
	/**
	 * SNS의 Consumer(개발자)정보를 리턴한다
	 * @return oauthConsumerVo2 개발자 컨슈머키 정보
	 */
	public OauthConsumerVO Oauth_Consumer(OauthConsumerVO oauthConsumerVo) throws BaseException
	{
		//System.out.println("Oauth_Consumer Impl");

		OauthConsumerVO oauthConsumerVo2 = null;
//		try{
		 oauthConsumerVo2 = openApiDAO.Oauth_Consumer(oauthConsumerVo);
//		}catch (BaseException e) {
//			log.error( e, e);
//		}
		return oauthConsumerVo2;
	}

	/**
	 * SNS의 인증후 AccessToken을 등록한다
	 */
	public void Oauth_AccessTokenInsert(OauthVO oauthVo) throws BaseException 
	{
		//System.out.println("AccessTokenInsert Impl");
//		try{
		 openApiDAO.Oauth_AccessTokenInser(oauthVo);
//		}catch (BaseException e) {
//			log.error( e, e);
//		}
	}
	
	/**
	 * SNS인증이 만료된것일 경우 AccessToken을 업데이트 한다
	 */
	public void Oauth_Re_AccessTokenUpdate(OauthVO oauthVo) throws BaseException 
	{
		//System.out.println("AccessTokenUpdate Impl");
//		try{
         openApiDAO.Oauth_Re_AccessTokenUpdate(oauthVo);
//		}catch (BaseException e) {
//			log.error( e, e);
//		}
	}	
}
