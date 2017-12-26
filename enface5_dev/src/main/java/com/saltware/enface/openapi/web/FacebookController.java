
package com.saltware.enface.openapi.web;
 
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.restfb.DefaultFacebookClient;
import com.restfb.types.User;
import com.saltware.enface.openapi.service.Oauth;
import com.saltware.enface.openapi.service.OauthConsumerVO;
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
 * 페이스북 Controller
 * @author psw
 * @since 2012.07.26 13:2:324
 * @version 1.0
 */
public class FacebookController extends MultiActionController
{
	private final Log log = LogFactory.getLog(getClass());
	private OpenApiService openApiService;
	private SessionManager enviewSessionManager;
	private PageManager pageManager;
	private DecorationFactory decorationFactory;
	private PageDelegator pageDelegator;
	private FacebookHandler facebookHandler;
	private DefaultFacebookClient facebook;
	protected PortalConfiguration configuration;
	private OauthVO vo;
	private OauthUrlVO oauth_url;
	private OauthConsumerVO oauth_consumer;
	
	/**
	 * OpenApiService객체 등록
	 * @param openApiService openApi서비스
	 */
	public void setOpenApiService(OpenApiService openApiService)
	{
		this.openApiService = openApiService;
	}
	
	/**
	 * OpenApiService객체 리턴
	 * @return openApiService openApi서비스
	 */
	public OpenApiService getOpenApiService()
	{
		return openApiService;
	}
	
	/**
	 *   FacebookController 생성자
	 */
	public FacebookController()
	{
		this.enviewSessionManager = (SessionManager)Enview.getComponentManager().getComponent("com.saltware.enview.session.SessionManager");
		this.pageManager = (PageManager)Enview.getComponentManager().getComponent("com.saltware.enview.page.PageManager");
		this.decorationFactory = (DecorationFactory)Enview.getComponentManager().getComponent("DecorationFactory");
		this.pageDelegator = (PageDelegator)Enview.getComponentManager().getComponent("com.saltware.enview.admin.page.service.PageDelegator");
	}
	
	/**
	 * 페이스북 DB인증키를 확인한다.
	 * 인증키가 없을경우 페이스북 인증화면으로 넘어간다.
	 * @param request 
	 * @param response
	 * @return facebook 페이스북 객체
	 * @throws Exception
	 */
	private DefaultFacebookClient getFacebook(HttpServletRequest request, HttpServletResponse response) throws Exception
	{

	   String enview_url =  request.getRequestURL().toString();
	   //System.out.println("URL" + enview_url);
	   
	   HttpSession session = request.getSession();
	   this.configuration = Enview.getConfiguration();
	   String user_id = (String)session.getAttribute(configuration.getString("sso.login.id.key"));
	   session.setAttribute("user_id",user_id);
	   session.setAttribute("enview_url",enview_url);
	   facebook = Oauth.Facebook_Oauth(getOpenApiService(), user_id);
	   
	   if(facebook == null){
		    response.sendRedirect(request.getContextPath() + "/openapi/facebookHandlerOauth.face?");
		}else
		{
			session.setAttribute("facebook", facebook);
			HttpUtil.sendRedirect( response, enview_url);
			return facebook;
		}
	   return null;
	}

	/**
	 * 인증이 되어있으면 그 인증된 객체를 가지고 사용자 이름을 가져온다
	 * @param request
	 * @param response
	 * @return ModelAndView 인증 페이지 닫기
	 * @throws Exception
	 */
	public ModelAndView facebookUser(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
	   HttpSession session = request.getSession();
	   facebook = (DefaultFacebookClient)session.getAttribute("facebook");
	  
//	   try {
			if (facebook == null) {
				facebook = getFacebook(request, response);
			}

			else {
				User user = facebook.fetchObject("me", User.class);
				//System.out.println("사용자 이름 : " + user.getName());
				return new ModelAndView("openapi/close");
			}
//		} 
//		catch (BaseException e) 
//		{
//			//System.out.println("인증 만료되었거나 문제가 있습니다");
//		    response.sendRedirect(request.getContextPath() + "/openapi/facebookHandlerOauth.face?");
//		}
	   
	   return null;
	}

	/**
	 * 인증이 되어있으면 그 인증된 객체를 가지고 친구목록을 가져온다
	 * @param request
	 * @param response
	 * @return ModelAndView 인증 페이지 닫기
	 * @throws Exception
	 */
	public ModelAndView facebookFriend(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		 HttpSession session = request.getSession();
		 facebook = (DefaultFacebookClient)session.getAttribute("facebook");
//		try {

			if (facebook == null) {
				facebook = getFacebook(request, response);
			} else {
				List<User> friends = null;
				friends = facebook.fetchConnection("me/friends", User.class)
						.getData();
				//System.out.println("친구수  : " + friends.size());
				return new ModelAndView("openapi/close");
			}
//		} catch (BaseException e) {
//			//System.out.println("인증 만료되었거나 문제가 있습니다");
//			response.sendRedirect(request.getContextPath() + "/openapi/facebookHandlerOauth.face?");
//		}
		 return null;
	}
}
