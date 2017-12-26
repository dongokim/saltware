package com.saltware.enface.openapi.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessTokenRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.saltware.enface.openapi.service.Oauth;
import com.saltware.enface.openapi.service.OauthConsumerVO;
import com.saltware.enface.openapi.service.OauthInfo;
import com.saltware.enface.openapi.service.OauthUrlVO;
import com.saltware.enface.openapi.service.OauthVO;
import com.saltware.enface.openapi.service.OpenApiService;
import com.saltware.enface.util.HttpUtil;
import com.saltware.enview.administration.PortalConfiguration;
import com.saltware.enview.decoration.DecorationFactory;
import com.saltware.enview.page.PageDelegator;
import com.saltware.enview.page.PageManager;
import com.saltware.enview.session.SessionManager;


/**  
 * 구글 플러스 Handler
 * @author psw
 * @since 2012.07.26 13:2:324
 */
public class GooglepulsHandler extends MultiActionController
{
  private final Log log = LogFactory.getLog(getClass());
  private OpenApiService openApiService;
  private SessionManager enviewSessionManager;
  private PageManager pageManager;
  private DecorationFactory decorationFactory;
  private PageDelegator pageDelegator;
  private GooglepulsHandler googleplusHandler;
  protected PortalConfiguration configuration;
  private OauthVO vo;
  private OauthUrlVO oauth_url;
  private OauthConsumerVO oauth_consumer;
  private OauthInfo oauthDB = new OauthInfo();
  private final String OauthSns = "GOOGLEPLUS";
  private static final String ACCESS_TYPE = "offline";
  private static final HttpTransport TRANSPORT = new NetHttpTransport();
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();

  /**
   * OpenApiService객체를 리턴한다.
   * @return openApiService OpenApi서비스
   */
  public OpenApiService getOpenApiService()
  {
    return this.openApiService;
  }
  
  /**
   * OpenApiService객체를 등록한다.
   * @param openApiService OpenApi서비스
   */
  public void setOpenApiService(OpenApiService openApiService)
  {
    this.openApiService = openApiService;
  }
  
  /**
   * 구글+ 서버를 통해서 인증을 한다
   * 인증에 필요한 컨슈머키가 없는경우 앱에대한 정보를 찾을수 없습니다라는 메시지를 보여준다
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @return ModelAndView 인증에러페이지 이동
   * @throws Exception
   */
  public ModelAndView googleplusHandlerOauth(HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    response.setContentType("text/html;charset=euc-kr");
//    try
//    {
      HttpSession session = request.getSession();

      this.oauth_url = this.oauthDB.Oauth_requst_url(this.openApiService, "GOOGLEPLUS");
      this.oauth_consumer = this.oauthDB.Oauth_Consumer(this.openApiService, "GOOGLEPLUS");

      String enview_url = request.getRequestURL().toString().replace("/openapi/googleplusHandlerOauth.face", "");

      session.setAttribute("googleplus_oauth_url", this.oauth_url);
      session.setAttribute("googleplus_oauth_consumer", this.oauth_consumer);

      String SCOPE = this.oauth_url.getAccessTokenUrl();
      String CLIENT_ID = this.oauth_consumer.getConsuMerKey();
      String GooglePlus_CallBackUrl = enview_url + this.oauth_url.getCallBackUrl();

      String authUrl = "https://accounts.google.com/o/oauth2/auth?client_id=" + CLIENT_ID + "&redirect_uri=" + GooglePlus_CallBackUrl + "&scope=" + SCOPE + 
        "&response_type=code" + "&access_type=" + "offline";
		HttpUtil.sendRedirect( response, authUrl);
      return null;
//    }
//    catch (BaseException e)
//    {
//        //System.out.println("#################### 개발자 키에대한 정보를 찾을수 없습니다 #####################");
//        return new ModelAndView("openapi/oautherror");
//    }
  }

  /**
   * 구글+ 서버로 부터 콜백되어 호출된다
   * googleplusAccessToken을 DB에 등록한다
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @return ModelAndView 인증페이지 닫기
   * @throws Exception
   */
  public ModelAndView googleplusAccessToken(HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    HttpSession session = request.getSession();
    String googleplus_code = request.getParameter("code");
    String user_id = (String)session.getAttribute("user_id");
    OauthUrlVO oauth_url = (OauthUrlVO)session.getAttribute("googleplus_oauth_url");
    OauthConsumerVO oauth_consumer = (OauthConsumerVO)session.getAttribute("googleplus_oauth_consumer");
    String CLIENT_ID = oauth_consumer.getConsuMerKey();
    String CLIENT_SECRET = oauth_consumer.getConsuMerSecret();

    String enview_url = request.getRequestURL().toString().replace("/openapi/googleplusAccessToken.face", "");
    String CALLBACK_URL = enview_url + oauth_url.getCallBackUrl();

    GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant authRequest = new GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant(TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, googleplus_code, CALLBACK_URL);
    authRequest.useBasicAuthorization = false;
    AccessTokenResponse authResponse = authRequest.execute();
    String accessToken = authResponse.accessToken;

    String oauthIdLive = Oauth.GooglePlus_Oauth(getOpenApiService(), user_id);
    if (oauthIdLive == null)
    {
      this.oauthDB.Oauth_AccessTokenSave(user_id, authResponse.accessToken, authResponse.refreshToken, null, "GOOGLEPLUS", this.openApiService);
    }
    else
    {
      String AccessToken = googleplusAccessToken2(authResponse.accessToken, authResponse.refreshToken, oauth_consumer.getConsuMerKey(), oauth_consumer.getConsuMerSecret());
      session.setAttribute("googleplus", AccessToken);
    }

    return new ModelAndView("openapi/close");
  }

  public static String googleplusAccessToken2(String token, String retoken, String client_id, String client_secret) throws Exception
  {
    GoogleAccessProtectedResource access = new GoogleAccessProtectedResource(token, TRANSPORT, JSON_FACTORY, client_id, client_secret, retoken);
    HttpRequestFactory rf = TRANSPORT.createRequestFactory(access);
    access.refreshToken();

    return access.getAccessToken();
  }
}