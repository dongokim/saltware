package com.saltware.enface.openapi.web;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import com.saltware.enface.openapi.service.OpenApiService;
import com.saltware.enview.Enview;
import com.saltware.enview.decoration.DecorationFactory;
import com.saltware.enview.page.PageDelegator;
import com.saltware.enview.page.PageManager;
import com.saltware.enview.session.SessionManager;

/**  
 * OpenApi Controller
 * @author psw
 * @since 2012.07.26 13:2:324
 * @version 1.0
 */
public class OpenApiController extends MultiActionController
{
	private final Log   log = LogFactory.getLog(getClass());
	private OpenApiService openApiService;
	private SessionManager enviewSessionManager;
	private PageManager pageManager;
	private DecorationFactory decorationFactory;
	private PageDelegator pageDelegator;
	
	/**
	 * OpenApiService객체를 등록한다.
	 * @param openApiService openApi서비스
	 */
	public void setOpenApiService(OpenApiService openApiService)
	{
		this.openApiService = openApiService;
	}
	
	/**
	 * OpenApiService 객체를 리턴한다.
	 * @return openApiService 
	 */
	public OpenApiService getOpenApiService()
	{
		return openApiService;
	}
	
	/**
	 * OpenApiController 생성자
	 */
	public OpenApiController()
	{
		this.enviewSessionManager = (SessionManager)Enview.getComponentManager().getComponent("com.saltware.enview.session.SessionManager");
		this.pageManager = (PageManager)Enview.getComponentManager().getComponent("com.saltware.enview.page.PageManager");
		this.decorationFactory = (DecorationFactory)Enview.getComponentManager().getComponent("DecorationFactory");
		this.pageDelegator = (PageDelegator)Enview.getComponentManager().getComponent("com.saltware.enview.admin.page.service.PageDelegator");
	}
	
	/**
	 * OPENAPI Sample화면을  보여준다. 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ModelAndView SNS선택 화면
	 * @throws Exception
	 */
	public ModelAndView openapisample(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		return new ModelAndView("openapi/index");
	}
}
