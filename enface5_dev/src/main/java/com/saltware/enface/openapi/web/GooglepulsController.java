
package com.saltware.enface.openapi.web;
 
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarFeed;
import com.saltware.enface.openapi.service.Oauth;
import com.saltware.enface.openapi.service.OpenApiService;
import com.saltware.enface.util.HttpUtil;
import com.saltware.enview.Enview;
import com.saltware.enview.administration.PortalConfiguration;
import com.saltware.enview.decoration.DecorationFactory;
import com.saltware.enview.page.PageDelegator;
import com.saltware.enview.page.PageManager;
import com.saltware.enview.session.SessionManager;

/**  
 * 구글 플러스 Controller
 * @author psw
 * @since 2012.07.26 13:2:324
 * @version 1.0
 */
public class GooglepulsController extends MultiActionController
{
	private final Log   log = LogFactory.getLog(getClass());
	private SessionManager enviewSessionManager;
	private PageManager pageManager;
	private DecorationFactory decorationFactory;
	private PageDelegator pageDelegator;
	protected PortalConfiguration configuration;
    private String googleplus;
	private OpenApiService openApiService;
	public static CalendarService myService = new CalendarService("exampleCo-exampleApp-1");
	
	/**
	 * OpenApiService객체를 리턴한다.
	 * @return openApiService  OpenApi서비스
	 */
	public OpenApiService getOpenApiService() 
	{
		return openApiService;
	}
	
    /**
     * OpenApiService객체를 등록한다.
     * @param openApiService   OpenApi서비스
     */
	public void setOpenApiService(OpenApiService openApiService) 
	{
		this.openApiService = openApiService;
	}
	
    /**
     * GooglepulsController 생성자
     */
	public GooglepulsController()
	{
		this.enviewSessionManager = (SessionManager)Enview.getComponentManager().getComponent("com.saltware.enview.session.SessionManager");
		this.pageManager = (PageManager)Enview.getComponentManager().getComponent("com.saltware.enview.page.PageManager");
		this.decorationFactory = (DecorationFactory)Enview.getComponentManager().getComponent("DecorationFactory");
		this.pageDelegator = (PageDelegator)Enview.getComponentManager().getComponent("com.saltware.enview.admin.page.service.PageDelegator");
	}

	/**
	 * 구글+ DB인증키를 확인한다.
	 * 인증키가 없을경우 구글+ 인증화면으로 넘어간다.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return null null
	 * @throws Exception
	 */
	public String getGoogleplus(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
	   String enview_url =  request.getRequestURL().toString();
	   //System.out.println("URL" + enview_url);
	   
	   HttpSession session = request.getSession();
	  
	   this.configuration = Enview.getConfiguration();
	   String user_id = (String)session.getAttribute(configuration.getString("sso.login.id.key"));
	   session.setAttribute("user_id",user_id);
	   session.setAttribute("enview_url",enview_url);
	   googleplus = Oauth.GooglePlus_Oauth(getOpenApiService(), user_id); // 호출
	   if(googleplus == null)
		{
		      response.sendRedirect(request.getContextPath() + "/openapi/googleplusHandlerOauth.face?");		   
		}else
		{
			session.setAttribute("googleplus", googleplus);
			HttpUtil.sendRedirect( response, enview_url);
			return null;
		}
	   return null;
	}

	/**
	 * 인증이 되어있으면 그 인증된 객체를 가지고 사용자 이름을 가져온다
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ModelAndView 인증페이지 닫기
	 * @throws Exception
	 */
	public ModelAndView googleplusUser(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HttpSession session = request.getSession();
		googleplus = (String) session.getAttribute("googleplus");
//		try{	
		if(googleplus==null) {
			 googleplus = getGoogleplus(request, response);	
		  } else
			  {
				//System.out.println("#### Token : " + googleplus.toString());
				session.setAttribute("googleplus", null);
				myService.setAuthSubToken(googleplus); 
				// 현재 캘린더를 사용하여 토큰판독 테스트를 하고있습니다 추후 사용자정보로 수정하겠습니다
				
				// 캘린더 
				URL feedUrl = new URL("https://www.google.com/calendar/feeds/default/owncalendars/full");
				//URL feedUrl = new URL("https://www.google.com/calendar/feeds/default/allcalendars/full");
				//https://www.google.com/calendar/feeds/default/owncalendars/full
				CalendarFeed resultFeed = myService.getFeed(feedUrl, CalendarFeed.class);
				//System.out.println("Your calendars:");
	
				for (int i = 0; i<resultFeed.getEntries().size(); i++) 
				{
					  CalendarEntry entry = resultFeed.getEntries().get(i);
					  //System.out.println("##################" + entry.getTitle().getPlainText());
				}
				
				/*GoogleCalendar.calendar();*/
			  }
		
//		}    //googleplus
//			 //https://www.googleapis.com/plus/v1/people/{userId} 
//		catch (BaseException e) 
//		{
//			//System.out.println("인증 만료되었거나 문제가 있습니다");
//			response.sendRedirect(request.getContextPath() + "/openapi/googleplusHandlerOauth.face?");	
//		}
		   return new ModelAndView("openapi/close");	
	}
}
