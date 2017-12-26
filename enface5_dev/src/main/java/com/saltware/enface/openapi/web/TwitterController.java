package com.saltware.enface.openapi.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import twitter4j.Twitter;
import twitter4j.TwitterException;

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
 * 트위터 Controller
 * 
 * @author psw
 * @since 2012.07.26 13:2:324
 * @version 1.0
 */
public class TwitterController extends MultiActionController {
	private final Log log = LogFactory.getLog(getClass());
	private SessionManager enviewSessionManager;
	private PageManager pageManager;
	private DecorationFactory decorationFactory;
	private PageDelegator pageDelegator;
	private TwitterHandler twitterHandler;
	private Twitter twitter;
	private OpenApiService openApiService;
	protected PortalConfiguration configuration;

	/**
	 * OpenApiService객체를 리턴한다.
	 * 
	 * @return openApiService OpenApi서비스
	 */
	public OpenApiService getOpenApiService() {
		return openApiService;
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
	 * TwitterController 생성자
	 * 
	 * @throws IllegalStateException
	 * @throws TwitterException
	 */
	public TwitterController() throws IllegalStateException, TwitterException {
		this.enviewSessionManager = (SessionManager) Enview.getComponentManager().getComponent("com.saltware.enview.session.SessionManager");
		this.pageManager = (PageManager) Enview.getComponentManager().getComponent("com.saltware.enview.page.PageManager");
		this.decorationFactory = (DecorationFactory) Enview.getComponentManager().getComponent("DecorationFactory");
		this.pageDelegator = (PageDelegator) Enview.getComponentManager().getComponent("com.saltware.enview.admin.page.service.PageDelegator");
	}

	/**
	 * 트위터 DB인증키를 확인한다. 인증키가 없을경우 트위터 인증화면으로 넘어간다.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return twitter 트위터 객체
	 * @throws Exception
	 */
	private Twitter getTwitter(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String enview_url = request.getRequestURL().toString();
		// System.out.println("URL" + enview_url);
		HttpSession session = request.getSession();

		this.configuration = Enview.getConfiguration();
		String user_id = (String) session.getAttribute(configuration.getString("sso.login.id.key"));
		session.setAttribute("user_id", user_id);
		session.setAttribute("enview_url", enview_url);
		twitter = Oauth.Twitter_Oauth(getOpenApiService(), user_id);

		if (twitter == null) {
			response.sendRedirect(request.getContextPath() + "/openapi/twitterHandlerOauth.face");
		} else {
			session.setAttribute("twitter", twitter);
			HttpUtil.sendRedirect(response, enview_url);
			return twitter;
		}
		return null;
	}

	/**
	 * 인증이 되어있으면 그 인증된 객체를 가지고 사용자 이름을 가져온다
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ModelAndView 인증화면 닫기
	 * @throws Exception
	 */
	public ModelAndView twitterUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		twitter = (Twitter) session.getAttribute("twitter");
//		try {
			if (twitter == null) {
				twitter = getTwitter(request, response);
			} else {
				// System.out.println("사용자 아이디 : " + twitter.getScreenName());
				return new ModelAndView("openapi/close");
			}
//		} catch (BaseException e) {
//			// System.out.println("인증 만료되었거나 문제가 있습니다");
//			response.sendRedirect(request.getContextPath() + "/openapi/twitterHandlerOauth.face");
//		}
		return null;

	}

	/**
	 * 인증이 되어있으면 그 인증된 객체를 가지고 사용자 친구목록을 가져온다
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ModelAndView 인증 페이지 닫기
	 * @throws Exception
	 */
	public ModelAndView twitterFriend(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		this.configuration = Enview.getConfiguration();
		String user_id = (String) session.getAttribute(configuration.getString("sso.login.id.key"));
		session.setAttribute("user_id", user_id);
//		try {
			twitter = Oauth.Twitter_Oauth(getOpenApiService(), user_id);

			List status2 = twitter.getHomeTimeline();
			for (int i = 0; i < status2.size(); i++) {
				// System.out.print(status2.get(i) + "</br>");
			}
//		} catch (BaseException e) {
//			// System.out.println("인증 만료되었거나 문제가 있습니다");
//			response.sendRedirect(request.getContextPath() + "/openapi/twitterHandlerOauth.face");
//		}
		return new ModelAndView("openapi/close");
	}

}
