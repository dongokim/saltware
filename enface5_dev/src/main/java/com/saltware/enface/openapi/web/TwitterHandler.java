package com.saltware.enface.openapi.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import com.saltware.enface.openapi.service.Oauth;
import com.saltware.enface.openapi.service.OauthConsumerVO;
import com.saltware.enface.openapi.service.OauthInfo;
import com.saltware.enface.openapi.service.OauthUrlVO;
import com.saltware.enface.openapi.service.OauthVO;
import com.saltware.enface.openapi.service.OpenApiService;
import com.saltware.enview.Enview;
import com.saltware.enview.administration.PortalConfiguration;
import com.saltware.enview.decoration.DecorationFactory;
import com.saltware.enview.page.PageDelegator;
import com.saltware.enview.page.PageManager;
import com.saltware.enview.session.SessionManager;

/**
 * 트위터 Handler
 * 
 * @author psw
 * @since 2012.07.26 13:2:324
 * @version 1.0
 */
public class TwitterHandler extends MultiActionController {
	private static TwitterHandler singleton = new TwitterHandler();
	static String inputLine = null;
	private OpenApiService openApiService;
	private SessionManager enviewSessionManager;
	private PageManager pageManager;
	private DecorationFactory decorationFactory;
	private PageDelegator pageDelegator;
	protected PortalConfiguration configuration;
	private OauthVO vo;
	private OauthUrlVO oauth_url;
	private OauthConsumerVO oauth_consumer;
	private static Twitter twitter;
	private OauthInfo oauthDB = new OauthInfo();
	private final String OauthSns = "TWITTER";

	/**
	 * TwitterHandler객체를 리턴한다.
	 * 
	 * @return singleton TwitterHandler객체
	 */
	public static TwitterHandler getInstance() {
		return singleton;
	}

	/**
	 * TwitterHandler 생성자
	 */
	public TwitterHandler() {
		this.enviewSessionManager = ((SessionManager) Enview.getComponentManager().getComponent("com.saltware.enview.session.SessionManager"));
		this.pageManager = ((PageManager) Enview.getComponentManager().getComponent("com.saltware.enview.page.PageManager"));
		this.decorationFactory = ((DecorationFactory) Enview.getComponentManager().getComponent("DecorationFactory"));
		this.pageDelegator = ((PageDelegator) Enview.getComponentManager().getComponent("com.saltware.enview.admin.page.service.PageDelegator"));
	}

	/**
	 * OpenApiService객체를 리턴한다.
	 * 
	 * @return openApiService OpenApi서비스
	 */
	public OpenApiService getOpenApiService() {
		return this.openApiService;
	}

	/**
	 * OpenApiService객체를 등록한다.
	 * 
	 * @param openApiService
	 *            OpenApi서비스
	 */
	public void setOpenApiService(OpenApiService openApiService) {
		this.openApiService = openApiService;
	}

	/**
	 * 트위터 서버를 통해서 인증을 한다 인증에 필요한 컨슈머키가 없는경우 앱에대한 정보를 찾을수 없습니다라는 메시지를 보여준다
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ModelAndView 인증에러화면으로 이동
	 * @throws Exception
	 */
	public ModelAndView twitterHandlerOauth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
//		try {
			HttpSession session = request.getSession();

			this.oauth_url = this.oauthDB.Oauth_requst_url(this.openApiService, "TWITTER");
			this.oauth_consumer = this.oauthDB.Oauth_Consumer(this.openApiService, "TWITTER");

			String enview_url = request.getRequestURL().toString().replace("/openapi/twitterHandlerOauth.face", "");

			OAuthProvider twitter_provider = new DefaultOAuthProvider(this.oauth_url.getRequestTokenUrl(), this.oauth_url.getAccessTokenUrl(), this.oauth_url.getAuthorizeUrl());
			OAuthConsumer twitter_consumer = new DefaultOAuthConsumer(this.oauth_consumer.getConsuMerKey(), this.oauth_consumer.getConsuMerSecret());

			String authUrl = twitter_provider.retrieveRequestToken(twitter_consumer, enview_url + this.oauth_url.getCallBackUrl());

			session.setAttribute("twitter_pro", twitter_provider);
			session.setAttribute("twitter_con", twitter_consumer);
			response.sendRedirect(authUrl);
			return null;
//		} catch (BaseException e) {
//			// System.out.println("#################### 개발자 키에대한 정보를 찾을수 없습니다 #####################");
//			return new ModelAndView("openapi/oautherror");
//		}
	}

	/**
	 * 트위터 서버로 부터 콜백되어 호출된다 twitterAccessToken을 DB에 등록한다
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ModelAndView 인증화면 닫기
	 * @throws Exception
	 */
	public ModelAndView twitterAccessToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String user_id = (String) session.getAttribute("user_id");

		OAuthProvider twitter_provider = (OAuthProvider) session.getAttribute("twitter_pro");
		OAuthConsumer twitter_consumer = (OAuthConsumer) session.getAttribute("twitter_con");

		String verifier = request.getParameter("oauth_verifier");

		twitter_provider.retrieveAccessToken(twitter_consumer, verifier);
		String ACCESS_TOKEN = twitter_consumer.getToken();
		String TOKEN_SECRET = twitter_consumer.getTokenSecret();

		twitter_consumer.setTokenWithSecret(ACCESS_TOKEN, TOKEN_SECRET);

		Twitter oauthIdLive = Oauth.Twitter_Oauth(getOpenApiService(), user_id);

		if (oauthIdLive == null) {
			this.oauthDB.Oauth_AccessTokenSave(user_id, ACCESS_TOKEN, TOKEN_SECRET, null, "TWITTER", this.openApiService);
		} else {
			this.oauthDB.Oauth_Re_AccessTokenSave(user_id, ACCESS_TOKEN, TOKEN_SECRET, null, "TWITTER", this.openApiService);
			// System.out.println("########### 재등록 되었습니다.");

			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true).setOAuthConsumerKey(this.oauth_consumer.getConsuMerKey()).setOAuthConsumerSecret(this.oauth_consumer.getConsuMerSecret());
			twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(this.oauth_consumer.getConsuMerKey(), this.oauth_consumer.getConsuMerSecret());

			// AccessToken accessToken = new AccessToken(ACCESS_TOKEN, TOKEN_SECRET);
			TwitterFactory tf = new TwitterFactory(cb.build());
			Twitter twitter = tf.getInstance();
			AccessToken accessToken1 = null;
			accessToken1 = new AccessToken(ACCESS_TOKEN, TOKEN_SECRET);
			twitter.setOAuthAccessToken(accessToken1);
			session.setAttribute("twitter", twitter);
		}
		return new ModelAndView("openapi/close");
	}
}