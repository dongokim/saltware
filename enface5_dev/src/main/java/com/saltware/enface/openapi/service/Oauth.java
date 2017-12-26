package com.saltware.enface.openapi.service; 

import javax.xml.parsers.ParserConfigurationException;

import net.daum.dna.api.YozmAPI;
import net.daum.dna.api.basic.DefaultYozmAPI;
import net.daum.dna.oauth.DaumOAuth;
import net.daum.dna.oauth.basic.DefaultDaumOAuth;
import net.daum.dna.oauth.vo.DaumConsumer;
import net.daum.dna.oauth.vo.DaumToken;
import net.me2day.Me2API;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import com.restfb.DefaultFacebookClient;
import com.saltware.enview.exception.BaseException;

/**  
 * 인증 유무 검사
 * @author psw
 * @since 2012.07.26 13:2:324
 * @version 1.0
 */

public class Oauth 
{
	private static Me2API me2api;
	private static Twitter twitter;
	private static OauthVO vo;
	private static OauthUrlVO oauth_url;
	private static OauthConsumerVO oauth_consumer;
	private static OauthInfo oauthDB = new OauthInfo();
	private static Me2API me2day; 
	
	/**
	 * (페이스북) 기존에 등록되어있다면 인증과정을 생략한다.
	 * 등록이 안되있을시 인증화면으로 넘어간다.
	 * @param service OpenApi서비스 객체
	 * @param userid 사용자id
	 * @return fbClient 페이스북 객체
	 * @throws BaseException
	 */
	public static DefaultFacebookClient Facebook_Oauth(OpenApiService service,String userid) throws BaseException
	{
		//System.out.println("Facebook_Oauth AccessToken검사");
		OauthVO oauthVo = new OauthVO();
		oauthVo.setUserId(userid);
		oauthVo.setOauthSns("FACEBOOK");
		oauthVo = service.Oauth(oauthVo);
		
		//System.out.println(userid);

		if (oauthVo == null) {
			//System.out.println("해당 아이디 없음");
			return null;
		 } else {
			//System.out.println("해당 아이디 있음");
			DefaultFacebookClient fbClient = new DefaultFacebookClient(oauthVo.getAccessToken());
			return fbClient;
		 }
	}
	
	/**
	 * (트위터) 기존에 등록되어있다면 인증과정을 생략한다.
	 * 등록이 안되있을시 인증화면으로 넘어간다.
	 * @param openApiService OpenApi서비스 객체
	 * @param userid 사용자id
	 * @return twitter 트위터 객체
	 * @throws BaseException
	 */
	public static Twitter Twitter_Oauth(OpenApiService openApiService,String userid) throws BaseException
	{
		//System.out.println("Twitter_Oauth AccessToken검사");
		OauthVO oauthVo = new OauthVO();
		oauthVo.setUserId(userid);
		oauthVo.setOauthSns("TWITTER");
		oauthVo = openApiService.Oauth(oauthVo);

		if (oauthVo == null) {
			//System.out.println("해당 아이디 없음");
			return null;
		 } else {
			oauth_url = oauthDB.Oauth_requst_url(openApiService, "TWITTER");
			oauth_consumer = oauthDB.Oauth_Consumer(openApiService, "TWITTER");

			//System.out.println("해당 아이디 있음");
			ConfigurationBuilder cb = new ConfigurationBuilder();
	   	    cb.setDebugEnabled(true)
						.setOAuthConsumerKey(oauth_consumer.getConsuMerKey())
						.setOAuthConsumerSecret(
						oauth_consumer.getConsuMerSecret());
			twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(oauth_consumer.getConsuMerKey(),oauth_consumer.getConsuMerSecret());
			
//			try {
//				AccessToken accessToken = new AccessToken(oauthVo.getAccessToken(), oauthVo.getAccessSecret());
//			 }catch(BaseException e) {
//				TwitterFactory tf = new TwitterFactory(cb.build());
//				Twitter twitter2 = tf.getInstance();
//				return twitter2;
//			 }
			TwitterFactory tf = new TwitterFactory(cb.build());
			Twitter twitter = tf.getInstance();
			AccessToken accessToken1 = null;

			accessToken1 = new AccessToken(oauthVo.getAccessToken(),oauthVo.getAccessSecret());
			twitter.setOAuthAccessToken(accessToken1);
			return twitter;
		 }
	}
	
	/**
	 * (미투데이) 기존에 등록되어있다면 인증과정을 생략한다. 
	 * 등록이 안되있을시 인증화면으로 넘어간다.
	 * @param service OpenApi서비스 객체
	 * @param userid 사용자id
	 * @return me2api 미투데이 객체
	 * @throws BaseException
	 * @throws ParserConfigurationException
	 */
	public static Me2API Me2day_Oauth(OpenApiService service, String userid)throws BaseException, ParserConfigurationException
	{
		//System.out.println("Me2day_Oauth AccessToken검사");
		OauthVO oauthVo = new OauthVO();
		oauthVo.setUserId(userid);
		oauthVo.setOauthSns("ME2DAY");
		oauthVo = service.Oauth(oauthVo);

		if (oauthVo == null) {
			//System.out.println("해당 아이디 없음");
			return null;
		 } else {
			me2api = new Me2API();
			me2api.setApplicationKey(oauthVo.getAccessToken());
			me2api.setUserKey(oauthVo.getAccessSecret());
			me2api.setUsername(oauthVo.getM2UserId());
			return me2api;
		 }
	}
	
	/**
	 * (요즘) 기존에 등록되어있다면 인증과정을 생략한다.
	 * 등록이 안되있을시 인증화면으로 넘어간다.
	 * @param openApiService OpenApi서비스 객체
	 * @param userid 사용자id
	 * @return yozm 요즘객체
	 * @throws Exception
	 */
	public static YozmAPI Yozm_Oauth(OpenApiService openApiService,String userid) throws Exception
	{
		//System.out.println("Yozm_Oauth AccessToken검사");
		oauth_url = oauthDB.Oauth_requst_url(openApiService, "YOZM");         
		oauth_consumer = oauthDB.Oauth_Consumer(openApiService, "YOZM");      
		
		OauthVO oauthVo = new OauthVO();
		oauthVo.setUserId(userid);
		oauthVo.setOauthSns("YOZM");
        oauthVo = openApiService.Oauth(oauthVo);
		
		if (oauthVo == null) {
			//System.out.println("해당 아이디 없음");
			return null;
		 }  else {
		    //System.out.println("해당 아이디 있음");
		    DaumConsumer consumer = new DaumConsumer(oauth_consumer.getConsuMerKey() , oauth_consumer.getConsuMerSecret() , oauth_url.getCallBackUrl());	
			DaumToken token = new DaumToken(oauthVo.getAccessToken(), oauthVo.getAccessSecret());
			DaumOAuth oauth = new DefaultDaumOAuth(consumer,token);
			YozmAPI yozm = new DefaultYozmAPI(oauth);
			return yozm;
		 }
	}
	
	/**
	 * (구글+) 기존에 등록되어있다면 인증과정을 생략한다.
	 *  등록이 안되있을시 인증화면으로 넘어간다.
	 * @param openApiService OpenApi서비스 객체
	 * @param userid 사용자id
	 * @return AccessToken 구글+ 객체
	 * @throws Exception
	 */
	public static String GooglePlus_Oauth(OpenApiService openApiService,String userid) throws Exception
	{
		//System.out.println("GooglePlus_Oauth AccessToken검사");
		oauth_url = oauthDB.Oauth_requst_url(openApiService, "GOOGLEPLUS");         
		oauth_consumer = oauthDB.Oauth_Consumer(openApiService, "GOOGLEPLUS");      
		
		OauthVO oauthVo = new OauthVO();
		oauthVo.setUserId(userid);
		oauthVo.setOauthSns("GOOGLEPLUS");
        oauthVo = openApiService.Oauth(oauthVo);
		
		if (oauthVo == null) {
			//System.out.println("해당 아이디 없음");
			return null;
		 } else {
		 	//System.out.println("해당 아이디 있음");
		    String  access= oauthVo.getAccessToken();
		    String reaccess = oauthVo.getAccessSecret();
		    String client_id = oauth_consumer.getConsuMerKey();
		    String client_secret = oauth_consumer.getConsuMerSecret();
		    String AccessToken = access;
			return AccessToken;
		}
	}
}