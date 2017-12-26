package com.saltware.enface.openapi.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.restfb.DefaultFacebookClient;
import com.saltware.enface.openapi.service.Oauth;
import com.saltware.enface.openapi.service.OauthConsumerVO;
import com.saltware.enface.openapi.service.OauthInfo;
import com.saltware.enface.openapi.service.OauthUrlVO;
import com.saltware.enface.openapi.service.OauthVO;
import com.saltware.enface.openapi.service.OpenApiService;
import com.saltware.enface.util.HttpUtil;
import com.saltware.enview.Enview;
import com.saltware.enview.administration.PortalConfiguration;
import com.saltware.enview.decoration.DecorationFactory;
import com.saltware.enview.page.PageDelegator;
import com.saltware.enview.page.PageManager;
import com.saltware.enview.session.SessionManager;



/**  
 * 페이스북 FacebookHandler
 * @author psw
 * @since 2012.07.26 13:2:324
 * @version 1.0
 */
public class FacebookHandler extends MultiActionController
{
  private static FacebookHandler singleton = new FacebookHandler();
  private String inputLine;
  static String facebook_request_url;
  private final Log log = LogFactory.getLog(getClass());
  private OpenApiService openApiService;
  private SessionManager enviewSessionManager;
  private PageManager pageManager;
  private DecorationFactory decorationFactory;
  private PageDelegator pageDelegator;
  private DefaultFacebookClient facebook;
  protected PortalConfiguration configuration;
  private OauthVO vo;
  private OauthUrlVO oauth_url;
  private OauthConsumerVO oauth_consumer;
  private OauthInfo oauthDB = new OauthInfo();
  private final String OauthSns = "FACEBOOK";
  private int SpecialChar = 0;
			   
 /**
  * FacebookHandler객체 리턴한다.
  * @return singleton 페이스북 객체
  */
  public static FacebookHandler getInstance()
  {
    return singleton;
  }

  /**
   * OpenApiService객체를 리턴한다.
   * @return openApiService openApi서비스
   */
  public OpenApiService getOpenApiService()
  {
    return this.openApiService;
  }
  
 /**
  * OpenApiService객체를 등록한다
  * @param openApiService openApi서비스
  */
  public void setOpenApiService(OpenApiService openApiService)
  {
    this.openApiService = openApiService;
  }
  
 /**
  * FacebookHandler 생성자
  */
  public FacebookHandler()
  {
    this.enviewSessionManager = ((SessionManager)Enview.getComponentManager().getComponent("com.saltware.enview.session.SessionManager"));
    this.pageManager = ((PageManager)Enview.getComponentManager().getComponent("com.saltware.enview.page.PageManager"));
    this.decorationFactory = ((DecorationFactory)Enview.getComponentManager().getComponent("DecorationFactory"));
    this.pageDelegator = ((PageDelegator)Enview.getComponentManager().getComponent("com.saltware.enview.admin.page.service.PageDelegator"));
  }

  /**
   * 페이스북서버를 통해서 인증을 한다
   * 인증에 필요한 컨슈머키가 없는경우 앱에대한 정보를 찾을수 없습니다라는 메시지를 보여준다
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @return ModelAndView 에러페이지 이동
   * @throws Exception
   */
  public ModelAndView facebookHandlerOauth(HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    response.setContentType("text/html;charset=utf-8");
//    try
//    {
      HttpSession session = request.getSession();
      this.oauth_url = this.oauthDB.Oauth_requst_url(this.openApiService, "FACEBOOK");
      this.oauth_consumer = this.oauthDB.Oauth_Consumer(this.openApiService, "FACEBOOK");

      String enview_url = request.getRequestURL().toString().replace("/openapi/facebookHandlerOauth.face", "");

      session.setAttribute("facebook_oauth_url", this.oauth_url);
      session.setAttribute("facebook_oauth_consumer", this.oauth_consumer);

      String RequestTokenUrl = this.oauth_url.getRequestTokenUrl();
      String ConsuMerKeys = this.oauth_consumer.getConsuMerKey();
      String FaceBook_CallBackUrl = enview_url + this.oauth_url.getCallBackUrl();
      String url = RequestTokenUrl + ConsuMerKeys + "&redirect_uri=" + FaceBook_CallBackUrl + "&scope=publish_stream,offline_access";

		HttpUtil.sendRedirect( response, url);
      return null;
//    }
//    catch (BaseException e)
//    {
//      //System.out.println("#################### 개발자 키에대한 정보를 찾을수 없습니다 #####################");
//      return new ModelAndView("openapi/oautherror");
//    }
  }
  
  /**
   * 페이스북 서버로 부터 콜백되어 호출된다
   * facebookAccessToken을 DB에 등록한다
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @throws Exception
   * @return ModelAndView 인증 페이지 닫기
   */
  public ModelAndView facebookAccessToken(HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    HttpSession session = request.getSession();
    String facebook_code = request.getParameter("code");
    String user_id = (String)session.getAttribute("user_id");
    OauthUrlVO oauth_url = (OauthUrlVO)session.getAttribute("facebook_oauth_url");
    OauthConsumerVO oauth_consumer = (OauthConsumerVO)session.getAttribute("facebook_oauth_consumer");
    String enview_url = request.getRequestURL().toString().replace("/openapi/facebookAccessToken.face", "");

    String facebook_access_token_url = oauth_url.getAccessTokenUrl() + "client_id=" + oauth_consumer.getConsuMerKey() + "&redirect_uri=" + 
      enview_url + oauth_url.getCallBackUrl() + "&client_secret=" + oauth_consumer.getConsuMerSecret() + 
      "&code=" + facebook_code;

    URL url = new URL(facebook_access_token_url);
    URLConnection yc = url.openConnection();
    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
    String ACCESS_TOKEN = null;

    while ((this.inputLine = in.readLine()) != null)
    {
      ACCESS_TOKEN = this.inputLine.substring(13, this.inputLine.length());
    }
    in.close();    
    
    SpecialChar = ACCESS_TOKEN.indexOf("&");
    ACCESS_TOKEN = ACCESS_TOKEN.substring(0, SpecialChar);
    DefaultFacebookClient oauthIdLive = Oauth.Facebook_Oauth(getOpenApiService(), user_id);

    if (oauthIdLive == null)
    {
      this.oauthDB.Oauth_AccessTokenSave(user_id, ACCESS_TOKEN, null, null, "FACEBOOK", this.openApiService);
    }
    else
    {
      this.oauthDB.Oauth_Re_AccessTokenSave(user_id, ACCESS_TOKEN, null, null, "FACEBOOK", this.openApiService);
      DefaultFacebookClient fbClient = new DefaultFacebookClient(ACCESS_TOKEN);
      session.setAttribute("facebook", fbClient);
    }
    return new ModelAndView("openapi/close");
  }
}