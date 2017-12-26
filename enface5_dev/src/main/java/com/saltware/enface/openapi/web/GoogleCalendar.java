package com.saltware.enface.openapi.web;
 
import java.io.IOException; 
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gdata.client.Query;
import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.calendar.ColorProperty;
import com.google.gdata.data.calendar.HiddenProperty;
import com.google.gdata.data.calendar.SelectedProperty;
import com.google.gdata.data.extensions.EventEntry;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.ServiceException;
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
 * 구글 캘린더
 * @author psw
 * @since 2012.07.26 13:2:324
 * @version 1.0
 */
public class GoogleCalendar extends MultiActionController
{
	private final Log log = LogFactory.getLog(getClass());
	private OpenApiService openApiService;
	private SessionManager enviewSessionManager;
	private PageManager pageManager;
	private DecorationFactory decorationFactory;
	private PageDelegator pageDelegator;
	protected PortalConfiguration configuration;
	private String googleplus;
	private OauthVO vo;
	private OauthUrlVO oauth_url;
	private OauthConsumerVO oauth_consumer;
	public static CalendarService myService = new CalendarService("exampleCo-exampleApp-1");
	public static CalendarEventEntry insertedEntry;
	public static EventEntry updatedEntry;
	
	/**
	 * OpenApiService객체를 등록한다.
	 * @param openApiService OpenApi서비스
	 */
	public void setOpenApiService(OpenApiService openApiService)
	{
		this.openApiService = openApiService;
	}
	
	/**
	 * OpenApiService객체를 리턴한다.
	 * @return openApiService OpenApi서비스
	 */
	public OpenApiService getOpenApiService()
	{
		return openApiService;
	}
	
	/**
	 * GoogleCalendar 생성자
	 */
	public GoogleCalendar()
	{
		this.enviewSessionManager = (SessionManager)Enview.getComponentManager().getComponent("com.saltware.enview.session.SessionManager");
		this.pageManager = (PageManager)Enview.getComponentManager().getComponent("com.saltware.enview.page.PageManager");
		this.decorationFactory = (DecorationFactory)Enview.getComponentManager().getComponent("DecorationFactory");
		this.pageDelegator = (PageDelegator)Enview.getComponentManager().getComponent("com.saltware.enview.admin.page.service.PageDelegator");
	}

  /**
   * 사용자의 캘린더 정보를 가져오기 위해서 인증을 받는다.
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @return googleplus 구글플러스 객체
   * @throws Exception
   */
   public String getGoogleCalendar(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
	   String enview_url =  request.getRequestURL().toString();
	   //System.out.println("URL" + enview_url);
	   
	   HttpSession session = request.getSession();
	   this.configuration = Enview.getConfiguration();
	   String user_id = (String)session.getAttribute(configuration.getString("sso.login.id.key"));
	   session.setAttribute("user_id",user_id);
	   session.setAttribute("enview_url",enview_url);
	   googleplus = Oauth.GooglePlus_Oauth(getOpenApiService(), user_id); // 호출
	   
	    if(googleplus == null){
		      response.sendRedirect(request.getContextPath() + "/openapi/googleplusHandlerOauth.face?");		   
		}else{
			session.setAttribute("googleplus", googleplus);
			HttpUtil.sendRedirect( response, enview_url);
			return googleplus;
		}
	   return null;
	}
  /**
   * 캘린더에 대한 정보를 조회를 한다.
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @return ModelAndView 인증 페이지 닫기
   * @throws Exception
   */
   public ModelAndView googleCalendar(HttpServletRequest request, HttpServletResponse response) throws Exception
	{	
	    HttpSession session = request.getSession();
		googleplus = (String) session.getAttribute("googleplus");

		int bun = Integer.parseInt(request.getParameter("bun"));
		String  title = request.getParameter("title");
		String  summary = request.getParameter("summary");
		String  content = request.getParameter("content");
		String  name =  request.getParameter("name");
		String  stime = request.getParameter("stime");
		String  etime = request.getParameter("etime");
		String  color = request.getParameter("color");
		
				if (googleplus == null){
					googleplus = getGoogleCalendar(request, response);
				}else 
				{	
					switch(bun)
					{
				        case 0:
				        	//System.out.println("캘린더를 호출합니다");
						    calendar(googleplus);
						break;
						
						case 1:
							calendarAdd(googleplus, title, summary, color);
							break;
							
						case 2:
							calendarUpdate(googleplus, title, summary, color);
							break;
							
						case 3:
						    calendarDel(googleplus, title);
						    break;
						
						case 4:    
						    event(googleplus, stime, etime);
						    break;
						 
						case 5:
						    eventSelect(googleplus, title);
						    break;
						    
						case 6:    
							eventAdd(googleplus, title, content, stime, etime);
							break;
							
						case 7: 
						    eventUpdate(googleplus, name, title, content);
						    break;
						    
						case 8:
						    eventDel(googleplus, title, stime, etime);
						    break;
						    
						default : 
							break;
					}
					  return new ModelAndView("openapi/close");
		} 
		return new ModelAndView("openapi/close");
	}
  //캘린더 관련------------------------------------------------------------------------------------------------------------------------------------------	
  /**
   * 캘린더 전체검색한다.
   * @param googleplus 구글플러스 객체
   * @throws IOException 
   * @throws ServiceException
   */
  public static void calendar(String googleplus) throws IOException, ServiceException              
	{		
		myService.setAuthSubToken(googleplus);
		URL feedUrl = new URL("https://www.google.com/calendar/feeds/default/owncalendars/full");
		//URL feedUrl = new URL("https://www.google.com/calendar/feeds/default/allcalendars/full");
		//https://www.google.com/calendar/feeds/default/owncalendars/full
		CalendarFeed resultFeed = myService.getFeed(feedUrl, CalendarFeed.class);
		//System.out.println("Your calendars:");

		for (int i = 0; i<resultFeed.getEntries().size(); i++) 
		{
			  CalendarEntry entry = resultFeed.getEntries().get(i);
			  //System.out.println("---->" + entry.getTitle().getPlainText());
		}
	}
  
	/**
	 * 캘린더 (이름,설명,색상)정보를 등록한다.
	 * @param googleplus 구글플러스 객체
	 * @param title  제목
	 * @param summary 내용
	 * @param color 색상
	 * @throws IOException
	 * @throws ServiceException
	 */
	public static void calendarAdd(String googleplus, String title, String summary, String color) throws IOException, ServiceException
	{		
		myService.setAuthSubToken(googleplus);
		CalendarEntry calendar = new CalendarEntry();
		calendar.setTitle(new PlainTextConstruct(title));
		calendar.setSummary(new PlainTextConstruct(summary));
		calendar.setColor(new ColorProperty(color));
		calendar.setHidden(HiddenProperty.FALSE);

		//calendar.setTimeZone(new TimeZoneProperty("America/Los_Angeles"));
		//calendar.addLocation(new Where("","","Korea"));
		// Insert the calendar
		URL postUrl = new URL("https://www.google.com/calendar/feeds/default/owncalendars/full");
		CalendarEntry returnedCalendar = myService.insert(postUrl, calendar);
	}
	
	/**
	 * 캘린더 업데이트(수정)를 한다.
	 * @param googleplus 구글플러스 객체
	 * @param title 제목
	 * @param summary 내용
	 * @param color 색상
	 * @throws IOException
	 * @throws ServiceException
	 */
    public static void calendarUpdate(String googleplus, String title, String summary, String color) throws IOException, ServiceException
    {
    	myService.setAuthSubToken(googleplus);
    	URL feedUrl1 = new URL("https://www.google.com/calendar/feeds/default/owncalendars/full");
		CalendarFeed resultFeed1 = myService.getFeed(feedUrl1, CalendarFeed.class);
		CalendarEntry calendar = resultFeed1.getEntries().get(0);
		calendar.setTitle(new PlainTextConstruct(title));
		calendar.setSummary(new PlainTextConstruct(summary));
		calendar.setColor(new ColorProperty(color));
		calendar.setSelected(SelectedProperty.TRUE);
		CalendarEntry returnedCalendar = calendar.update();
    }
    
    /**
     * 캘린더를 삭제한다
     * @param googleplus 구글 플러스 객체
     * @param title 제목
     * @throws IOException
     * @throws ServiceException
     */
    public static void calendarDel(String googleplus, String title) throws IOException, ServiceException
    {
    	myService.setAuthSubToken(googleplus);
    	URL feedUrl1 = new URL("https://www.google.com/calendar/feeds/default/owncalendars/full");
		CalendarFeed resultFeed1 = myService.getFeed(feedUrl1, CalendarFeed.class);
		for (int i = 0; i < resultFeed1.getEntries().size(); i++) 
		{
			CalendarEntry entry = resultFeed1.getEntries().get(i);

			if (entry.getTitle().getPlainText().equals(title)) 
			{
				//System.out.println("삭제 캘린더 : " + entry.getTitle().getPlainText());
				entry.delete();
				break;
			} 
			//else{}
		}
    }
    //일정 관련 --------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * 일정 전체를 조회한다
     * @param googleplus 구글플러스 객체
     * @param stime 시작시간
     * @param etime 종료시간
     * @throws IOException
     * @throws ServiceException
     */
	public static void event(String googleplus, String stime, String etime) throws IOException, ServiceException
	{
		myService.setAuthSubToken(googleplus);
		URL postUrl = new URL("http://www.google.com/calendar/feeds/default/private/full");
		CalendarQuery myQuery = new CalendarQuery(postUrl);
	    myQuery.setMinimumStartTime(DateTime.parseDateTime(stime));
        myQuery.setMaximumStartTime(DateTime.parseDateTime(etime));
 
        CalendarEventFeed resultFeed1 = myService.query(myQuery, CalendarEventFeed.class);
        
        for (int i = 0; i < resultFeed1.getEntries().size(); i++)
        {
                CalendarEventEntry firstMatchEntry = resultFeed1.getEntries().get(i);
         
                /*System.out.println("제   목:" + firstMatchEntry.getTitle().getPlainText()); 
                System.out.println("설   명:" + firstMatchEntry.getPlainTextContent());
                System.out.println("장   소:" + firstMatchEntry.getLocations().get(0).getValueString());
                System.out.println("시작시간 = "+firstMatchEntry.getTimes().get(0).getStartTime());
                System.out.println("종료시간 = "+firstMatchEntry.getTimes().get(0).getEndTime());
                System.out.println("\n");*/
                
              // firstMatchEntry.delete();
              // System.out.println("이메일:"+firstMatchEntry.getAuthors().get(0).getEmail());
              // System.out.println("html :"+firstMatchEntry.getHtmlLink().getHref());  // 수정
        }
	}

	/**
	 * 일정의 일부만 조회한다
	 * @param googleplus 구글 플러스 객체
	 * @param title 제목
	 * @throws IOException
	 * @throws ServiceException
	 */
    public static void eventSelect(String googleplus, String title) throws IOException, ServiceException
			{
    	        myService.setAuthSubToken(googleplus);
				URL postUrl1 = new URL("http://www.google.com/calendar/feeds/default/private/full");
		        Query myQuery1 = new Query(postUrl1);
		        myQuery1.setFullTextQuery(title);
		        
		        Feed myResultsFeed = myService.query(myQuery1, Feed.class);
		        if (myResultsFeed.getEntries().size() > 0)
		        {
			        Entry firstMatchEntry = myResultsFeed.getEntries().get(0); 
			        
			        /*System.out.println("제   목:" + firstMatchEntry.getTitle().getPlainText()); 
		            System.out.println("설   명:" + firstMatchEntry.getPlainTextContent());
		            System.out.println("시간:" + firstMatchEntry.getPublished());
		            System.out.println("\n");*/
		            //firstMatchEntry.delete();
		        }
			}
	/**
	 * 일정을 추가한다.
	 * @param googleplus 구글플러스 객체
	 * @param title 제목
	 * @param content 설명
	 * @param stime 시작시간
	 * @param etime 종료시간
	 * @throws IOException
	 * @throws ServiceException
	 */
	public static void eventAdd(String googleplus, String title, String content, String stime, String etime) throws IOException, ServiceException
	{   
		    myService.setAuthSubToken(googleplus);
		    String googleUserId = "sangwou@gmail.com"; // 구글 계정Id
	        URL postUrl1 = new URL("https://www.google.com/calendar/feeds/" + googleUserId + "/private/full");
			CalendarEventEntry myEntry = new CalendarEventEntry();

			myEntry.setTitle(new PlainTextConstruct(title));
			myEntry.setContent(new PlainTextConstruct(content));
			
			DateTime startTime = DateTime.parseDateTime(stime );
		  	DateTime endTime = DateTime.parseDateTime(etime);
		  	
		  	
			When eventTimes = new When();
			eventTimes.setStartTime(startTime);
			eventTimes.setEndTime(endTime);
			myEntry.addTime(eventTimes);
			insertedEntry = myService.insert(postUrl1, myEntry);
	}
	
	/**
	 * 일정을 업데이트한다.
	 * @param googleplus 구글플러스 객체
	 * @param name 이름
	 * @param title 제목
	 * @param content 설명
	 * @throws IOException
	 * @throws ServiceException
	 */
	public static void eventUpdate(String googleplus, String name, String title, String content) throws IOException, ServiceException
	{
		    myService.setAuthSubToken(googleplus);
			URL postUrl1 = new URL("http://www.google.com/calendar/feeds/default/private/full");
	        Query myQuery1 = new Query(postUrl1);
	        myQuery1.setFullTextQuery(name);
	        
	        Feed myResultsFeed = myService.query(myQuery1, Feed.class);
	        if (myResultsFeed.getEntries().size() > 0)
	        {
		        Entry firstMatchEntry = myResultsFeed.getEntries().get(0); 
	            firstMatchEntry.setTitle(new PlainTextConstruct(title));
	            firstMatchEntry.setContent(new PlainTextConstruct(content));
	            firstMatchEntry.update();
	            
		        /*System.out.println("제   목:" + firstMatchEntry.getTitle().getPlainText()); 
	            System.out.println("설   명:" + firstMatchEntry.getPlainTextContent());
	            System.out.println("\n");*/
	            //firstMatchEntry.delete();
	        }
		
	}
	
	/**
	 * 일정을 삭제한다.
	 * @param googleplus 구글플러스
	 * @param title 제목
	 * @param stime 시작시간
	 * @param etime 종료시간
	 * @throws IOException
	 * @throws ServiceException
	 */
	public static void eventDel(String googleplus, String title, String stime, String etime) throws IOException, ServiceException 
	  {
		String name = title;
		String st = stime;
		String en = etime; 
		    myService.setAuthSubToken(googleplus);
			URL postUrl = new URL("http://www.google.com/calendar/feeds/default/private/full");
			CalendarQuery myQuery = new CalendarQuery(postUrl);
			myQuery.setMinimumStartTime(DateTime.parseDateTime(st+".000+09:00")); // 넘어온 시간
			myQuery.setMaximumStartTime(DateTime.parseDateTime(en+".000+09:00")); // 넘어온 시간
			
			CalendarEventFeed resultFeed1 = myService.query(myQuery,CalendarEventFeed.class);
			for (int i = 0; i < resultFeed1.getEntries().size(); i++) 
			{
				CalendarEventEntry firstMatchEntry = resultFeed1.getEntries().get(i);
				/*System.out.println("제   목:" + firstMatchEntry.getTitle().getPlainText());
				System.out.println("시   간:" + firstMatchEntry.getPublished());*/
				
				if (firstMatchEntry.getTitle().getPlainText().equals(name)) 
				{
						 firstMatchEntry.delete();
						 break;
				}
				//else {}
			}
		}
}
