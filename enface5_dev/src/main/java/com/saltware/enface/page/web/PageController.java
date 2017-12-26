package com.saltware.enface.page.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.saltware.enface.user.service.SiteUserManager;
import com.saltware.enview.Enview;
import com.saltware.enview.code.EnviewCodeManager;
import com.saltware.enview.codebase.CodeBundle;
import com.saltware.enview.components.portletregistry.PortletRegistry;
import com.saltware.enview.decoration.DecorationFactory;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.login.LoginConstants;
import com.saltware.enview.multiresource.EnviewMultiResourceManager;
import com.saltware.enview.multiresource.MultiResourceBundle;
import com.saltware.enview.om.page.Page;
import com.saltware.enview.om.portlet.InitParam;
import com.saltware.enview.om.portlet.PortletDefinition;
import com.saltware.enview.page.PageDelegator;
import com.saltware.enview.page.PageManager;
import com.saltware.enview.security.UserInfo;
import com.saltware.enview.session.SessionManager;
import com.saltware.enview.sso.EnviewSSOManager;
import com.saltware.enview.userpreference.UserPreferenceManager;
import com.saltware.enview.util.EnviewLocale;
import com.saltware.enview.util.PageNavigationUtil;
import com.saltware.enview.util.ResultSetList;
import com.saltware.enview.util.json.JSONArray;
import com.saltware.enview.util.json.JSONException;
import com.saltware.enview.util.json.JSONObject;

/**
 * 페이지 Controller
 * 
 * @author kevin
 * @since 2011.07.26 13:2:324
 * @version 1.0
 */
public class PageController extends MultiActionController {
	private final Log log = LogFactory.getLog(getClass());

	private SessionManager enviewSessionManager;
	private PageManager pageManager;
	private DecorationFactory decorationFactory;
	private PageDelegator pageDelegator;
	private PortletRegistry registry;
	private UserPreferenceManager userPreferenceManager;
	private SiteUserManager siteUserManager;

	/**
	 * 페이지 Controller 생성자 
	 */
	public PageController() {
		this.enviewSessionManager = (SessionManager) Enview.getComponentManager().getComponent("com.saltware.enview.session.SessionManager");
		this.pageManager = (PageManager) Enview.getComponentManager().getComponent("com.saltware.enview.page.PageManager");
		this.decorationFactory = (DecorationFactory) Enview.getComponentManager().getComponent("DecorationFactory");
		this.pageDelegator = (PageDelegator) Enview.getComponentManager().getComponent("com.saltware.enview.admin.page.service.PageDelegator");
		this.registry = (PortletRegistry)Enview.getComponentManager().getComponent("com.saltware.enview.components.portletregistry.PortletRegistry");
		this.userPreferenceManager = (UserPreferenceManager) Enview.getComponentManager().getComponent("com.saltware.enview.userpreference.UserPreferenceManager");
		this.siteUserManager = (SiteUserManager) Enview.getComponentManager().getComponent("com.saltware.enface.user.service.UserManager");
		
	}

	/**
	 * 포틀릿 선택 리스트화면을 출력한다.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView listForPortletSelector(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {

		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		CodeBundle enviewCodeBundle = EnviewCodeManager.getInstance().getBundle(langKnd);

//		try {
			request.setAttribute("themeList", decorationFactory.getPageDecorations(null));
			request.setAttribute("portletCategory", registry.getPortletCategorys(langKnd));
			request.setAttribute("systemCodeList", (List) enviewCodeBundle.getCodes("PT", "001", 1, true));
			request.setAttribute("langKnd", langKnd);
			request.setAttribute("userId", request.getAttribute(LoginConstants.SSO_LOGIN_ID));
			request.setAttribute("messages", enviewMessages);
//		} catch (BaseException be) {
//			log.error( be.getMessage(), be);
//		}
		return new ModelAndView("page/portletSelectorAll");
	}

	/**
	 * 포틀릿 편집 리스트화면을 출력한다.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView listForPortletEditor(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {

		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		CodeBundle enviewCodeBundle = EnviewCodeManager.getInstance().getBundle(langKnd);

		String type = request.getParameter("type");
		String page = "page/" + type + "Editor";

		request.setAttribute("myPageThemeList", decorationFactory.getMyPageDecorations(null));
		request.setAttribute("groupPageThemeList", decorationFactory.getGroupPageDecorations(null));
		request.setAttribute("layoutList", decorationFactory.getLayouts(new Locale(langKnd)));
		request.setAttribute("systemCodeList", (List) enviewCodeBundle.getCodes("PT", "001", 1, true));
		request.setAttribute("contentTypeList", (List) enviewCodeBundle.getCodes("PT", "121", 1, false));
		request.setAttribute("langKnd", langKnd);
		request.setAttribute("userId", request.getAttribute(LoginConstants.SSO_LOGIN_ID));
		request.setAttribute("messages", enviewMessages);

		return new ModelAndView(page);
	}

	/**
	 * 포틀릿 목록을 조회한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView listPortletForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {

		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		CodeBundle enviewCodeBundle = EnviewCodeManager.getInstance().getBundle(langKnd);
		StringBuffer buffer = new StringBuffer();
		String returnType = "json"; 

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new Exception("You have to login !!!");
			}
			
			Map paramMap = formData.getSearchCondition();
			paramMap.put("langKnd", langKnd);
			String pagePath = request.getParameter("pagePath");
			if (pagePath != null && pagePath.length() > 0) {
				paramMap.put("pagePath", pagePath);
			}
			paramMap.put("allowDuplicate", request.getParameter("allowDuplicate"));

			List results = pageDelegator.getPortletList(userInfoMap, paramMap);
			
			int totalCount = ((ResultSetList)results).getTotalCount();
			String pageIteratorHtmlString = PageNavigationUtil.getInstance().getPageIteratorHtmlString(results.size(), formData.getPageNo(), formData.getPageSize(), totalCount, "PortletSelectorSearchForm", "portalPortletSelector.doPortletPage");

			returnType = request.getParameter("returnType");
			if( returnType != null && returnType.equals("jsp") ) {
				returnType = "jsp"; 
			}
			else returnType = "json"; 
			
			formData.setResultStatus("success");
    		formData.setFailureReason("");
    		formData.setTotalSize( totalCount );
    		formData.setResultSize( results.size() );
    		
    		request.setAttribute("inform", formData);
			request.setAttribute("pageIterator", pageIteratorHtmlString);
			request.setAttribute("results", results);
			request.setAttribute("resultSize", totalCount);
			//System.out.println("################# resultSize=" + results.size() + ", pageIteratorHtmlString=" + pageIteratorHtmlString);
		} catch (BaseException be) {
			log.error( be.getMessage(), be);

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(be.getMessage()).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}
		if( "json".equals( returnType) ) {
			return new ModelAndView("page/retrievePortletForAjax");
		}
		else {
			return new ModelAndView("page/retrievePortletJspForAjax");
		}
	}

	/**
	 * 페이지의 포틀릿들을 조회한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView getPagePortletsForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {

		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		CodeBundle enviewCodeBundle = EnviewCodeManager.getInstance().getBundle(langKnd);
		StringBuffer buffer = new StringBuffer();

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new BaseException("You have to login !!!");
			}

			Map paramMap = new HashMap();
			String path = request.getParameter("path");
			paramMap.put("path", path);

			String result = pageDelegator.getPagePortlets(userInfoMap, paramMap);

			log.info("*** result=" + result);

			if (result.length() > 0) {
				response.setContentType("text/json;charset=UTF-8");
				response.getWriter().print(result);
			} else {
				log.error("Page [" + path + "] was not found !!!");
			}
		} catch (BaseException be) {
			log.error( be.getMessage(), be);

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(be.getMessage()).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}
		return null;
	}

	/**
	 * 포틀릿 정보를 조회한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView getPortletInfoForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {

		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		CodeBundle enviewCodeBundle = EnviewCodeManager.getInstance().getBundle(langKnd);
		StringBuffer buffer = new StringBuffer();

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new Exception("You have to login !!!");
			}

			Map paramMap = new HashMap();
			String path = request.getParameter("path");
			paramMap.put("path", path);

			String result = pageDelegator.getPortletInfo(userInfoMap, paramMap);

			// log.info("*** result=" + result);

			if (result.length() > 0) {
				response.setContentType("text/json;charset=UTF-8");
				response.getWriter().print(result);
			} else {
				log.error("Page [" + path + "] was not found !!!");
			}
			log.info("success getPortletInfoForAjax");
		} catch (BaseException be) {
			log.error( be.getMessage(), be);

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(be.getMessage()).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}
		return null;
	}

	/**
	 * 레이아웃 포틀릿정보를 로 조회한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView getLayoutPortletInfoForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {

		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		CodeBundle enviewCodeBundle = EnviewCodeManager.getInstance().getBundle(langKnd);
		StringBuffer buffer = new StringBuffer();

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new Exception("You have to login !!!");
			}

			Map paramMap = new HashMap();
			paramMap.put("langKnd", langKnd);

			String result = pageDelegator.getLayoutPortletInfo(userInfoMap, paramMap);

			log.debug("*** result=" + result);

			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(result);
		} catch (Exception be) {
			log.error( be.getMessage(), be);

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(be.getMessage()).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}
		return null;
	}

	private void showMap( Map map, int maxCol, int maxRow) {
		for ( int r = 0; r <= maxRow; r++) {
			for (int c = 0; c < maxCol; c++) {
				System.out.print( map.get( c+ ":" + r) == null ? "_" : "*");
			}
			System.out.println();
		}
	}

	
	private int getParamInt( PortletDefinition pd, String key, int defaultValue) {
		try {
			InitParam param = pd.getInitParam(key);
			if( param==null) {
				return defaultValue;
			}
			return Integer.parseInt( param.getParamValue());
		} catch (NullPointerException e) {
			return defaultValue;
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	
	/**
	 * 사용자 페이지에 포틀릿을 추가한다.
	 * @param fragmentLayout
	 * @param portletName
	 * @param maxCol
	 * @throws JSONException
	 */
	private void insertPortlet( JSONArray fragmentLayout, String portletName, int maxCol) throws JSONException{
		// 추가 할려는 포틀릿이 존재하는지 
		PortletDefinition pd = registry.getPortletDefinitionByUniqueName( portletName);
		if( pd==null) {
			return ;
		}
		
		// 사용자 페이지에 새로 추가 하는 경우 아이디가 이 번호 이후로 추가한다. 
		long USER_ID_BASE = 100000000;
		int maxRow =-1;
		long  maxId = 0;
		long id = 0;
		int col=0, row=0, dataSizex, dataSizey;
		Map map = new HashMap();
		// 사용하고있는 영역에 대한 포틀릿 맵을 작성 
		for (int i = 0; i < fragmentLayout.length(); i++) {
			JSONObject f = fragmentLayout.getJSONObject(i);
			id = f.getInt("id");
			row = f.getInt("row");
			col = f.getInt("col");
			
			// 포틀릿 정보를 읽어 가로 세로 크기를 알아낸다. 
			String name = f.getString("name");
			pd = registry.getPortletDefinitionByUniqueName( name);
			dataSizex = getParamInt(pd, "SIZEX", 1);
			dataSizey = getParamInt(pd, "SIZEY", 2);

			// 포틀릿이 차지하는 영역만큼 *표를 찍는다.
			for ( int r = row; r < row + dataSizey; r++) {
				for (int c = col; c < col + dataSizex; c++) {
					map.put( c + ":" + r, "*");
					maxRow = Math.max( maxRow, r);
				}
			}
			maxId = Math.max( maxId, id);
		}
		if( log.isDebugEnabled()) {
			showMap( map, maxCol, maxRow);
		}

		// 아이디를 결정한다.
		id = maxId < USER_ID_BASE ? USER_ID_BASE : maxId + 1;

		// 포틀릿 정보를 읽어 가로 세로 크기를 알아낸다. 
		pd = registry.getPortletDefinitionByUniqueName( portletName);
		dataSizex = getParamInt(pd, "SIZEX", 1);
		dataSizey = getParamInt(pd, "SIZEY", 2);
		
		// 포틀릿 맵에서 새로운 포틀릿이 들어갈만한 공간을 찾는다.
		boolean match = false;
		for (int r = 0; r <= maxRow+1; r++) {
			for (int c = 0; c < maxCol; c++) {
				match = true;
				// 포틀릿이 차지하는 영역만큼 빈공간이 있는지 체크한다.
				for (int r1 = r; r1 <= r + dataSizey -1; r1++) {
					for (int c1 = c; c1 <= c + dataSizex -1; c1++) {
						// 사용중이거나 가로 크기를 초과하면 면 match = false;
						if( map.get( c1 + ":" + r1 ) != null || c1 >= maxCol) {
							match = false;
						}
					}
				}
				if( match) {
					col = c;
					row = r;
					break;
				}
			}
			if( match) break;
		}
		
		JSONObject f = new JSONObject();
		f.put("id", id);
		f.put("col", col);
		f.put("row", row);
		f.put("name", portletName);
		fragmentLayout.put( fragmentLayout.length(), f);
	}
	
	/**
	 * 선택된 포틀릿을 페이지에 추가한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView saveSelectPortletForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {
		StringBuffer buffer = new StringBuffer();
		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new Exception("You have to login !!!");
			}

			String path = request.getParameter("path");
			
			String isEditMode = request.getParameter("isEditMode");
			Map paramMap = new HashMap();
			paramMap.put("path", path);
			paramMap.put("layoutId", request.getParameter("layoutId"));
			paramMap.put("selectPortletIds", request.getParameter("selectPortletIds"));
			paramMap.put("layoutColumn", request.getParameter("layoutColumn"));
			paramMap.put("layoutRow", request.getParameter("layoutRow"));
			
			String layoutName = request.getParameter("layoutName");
			if( layoutName==null) layoutName = "";
			

			if( "true".equals(isEditMode) || layoutName.indexOf("Gridster") == -1) {
				pageDelegator.insertPortletToPage(userInfoMap, paramMap);
				Map syncParamMap = new HashMap();
				syncParamMap.put("method", "remove");
				syncParamMap.put("path", path);
				pageManager.syncCache(syncParamMap);
			} else {
				int maxColumn = 4;
				try {
					maxColumn = Integer.parseInt( request.getParameter("maxColumn"));
				} catch( NumberFormatException e) {
					maxColumn = 4;
				}
				
				String pageData = userPreferenceManager.getPreference(request, "MY",  path);
				JSONObject pageInfo = new JSONObject( pageData);
				JSONArray fragmentLayout = pageInfo.getJSONArray("fragmentLayout");
				String[] portletNames = request.getParameter("selectPortletIds").split(",");
				for (int i = 0; i < portletNames.length; i++) {
					insertPortlet(fragmentLayout, portletNames[i], maxColumn);
				}
				String newPageData = pageInfo.toString();
				userPreferenceManager.setPreference( request, "MY", path, newPageData);
				//System.out.println("newPageData=" + newPageData);
			}


			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");
			log.info("*** buff=" + buffer.toString());
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException be) {
			log.error( be.getMessage(), be);

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(be.getMessage()).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}
		return null;
	}
	
	/**
	 * Fragment 정보를 저장한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView saveFragmentInfoForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {
		StringBuffer buffer = new StringBuffer();
		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new Exception("You have to login !!!");
			}

			Map paramMap = new HashMap();
			paramMap.put("fragments", request.getParameter("fragments"));

			String path = pageDelegator.updatePageFragments(userInfoMap, paramMap);

			Map syncParamMap = new HashMap();
			syncParamMap.put("method", "remove");
			syncParamMap.put("path", path);
			pageManager.syncCache(syncParamMap);

			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");
			log.info("*** buff=" + buffer.toString());
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException e) {
			log.error( e.getMessage(), e);

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(e.getMessage()).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}
		return null;
	}
	

	/**
	 * 마이페이지 템플릿 목록을 조회한다(ajax)
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView getMyPageTemplateListForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {

		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		CodeBundle enviewCodeBundle = EnviewCodeManager.getInstance().getBundle(langKnd);

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);

			String templateDir = Enview.getConfiguration().getString("mypage.template.dir", "/public/template/mypage");
			List templatePages = (List) pageManager.getPages(templateDir);

			request.setAttribute("results", templatePages);

			/*
			 * StringBuffer buffer = new StringBuffer(); buffer.append("{");
			 * buffer.append("\"Reason\": \"success\",");
			 * buffer.append("\"TotalSize\": \"").append( templatePages.size()
			 * ).append("\","); buffer.append("\"ResultSize\": \"").append(
			 * templatePages.size() ).append("\","); buffer.append("\"Data\":");
			 * buffer.append("["); int idx = 0; for(Iterator
			 * it=templatePages.iterator(); it.hasNext(); idx++) { Page
			 * templatePage = (Page)it.next(); if( idx > 0 ) {
			 * buffer.append(","); } buffer.append(" {");
			 * 
			 * buffer.append("\"id\": \""); buffer.append( templatePage.getId()
			 * ); buffer.append("\" ");buffer.append(", ");
			 * buffer.append("\"path\": \""); buffer.append(
			 * templatePage.getPath() );
			 * buffer.append("\" ");buffer.append(", ");
			 * buffer.append("\"name\": \""); buffer.append(
			 * templatePage.getName() );
			 * buffer.append("\" ");buffer.append(", ");
			 * buffer.append("\"title\": \""); buffer.append(
			 * templatePage.getShortTitle(locale) );
			 * buffer.append("\" ");buffer.append(", ");
			 * buffer.append("\"desc\": \""); buffer.append(
			 * templatePage.getTitle(locale) ); buffer.append("\" ");
			 * buffer.append("}"); } buffer.append("]}");
			 * 
			 * log.debug("*** result=" + buffer.toString());
			 * 
			 * response.setContentType("text/json;charset=UTF-8");
			 * response.getWriter().print(buffer.toString());
			 */
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
		}
		return new ModelAndView("page/myPageTemplateList");
	}

	/**
	 * 마이페이지를 추가한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView addMyPageForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		StringBuffer buffer = new StringBuffer();

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new BaseException("You have to login !!!");
			}

			Map paramMap = new HashMap();
			paramMap.put("langKnd", langKnd);
			paramMap.put("title", request.getParameter("title"));
			paramMap.put("theme", request.getParameter("theme"));
			paramMap.put("templatePath", request.getParameter("templatePath"));
			paramMap.put("isMyPage", true);

			String path = pageDelegator.addMyPage(userInfoMap, paramMap);

			Map syncParamMap = new HashMap();
			syncParamMap.put("path", path);
			syncParamMap.put("method", "insert");
			pageManager.syncCache(syncParamMap);

			buffer.append("{");
			buffer.append("\"Status\": \"success\",");
			buffer.append("\"Path\": \"" + path + "\"");
			buffer.append("}");
			log.info("*** buff=" + buffer.toString());
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				errorMessage = enviewMessages.getString(msgKey);
			}

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(errorMessage).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}

		return null;
	}

	/**
	 * 마이페이지 정보를 저장한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView updateMyPageForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		StringBuffer buffer = new StringBuffer();

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new BaseException("You have to login !!!");
			}

			Map paramMap = new HashMap();
			paramMap.put("langKnd", langKnd);
			paramMap.put("path", request.getParameter("path"));
			paramMap.put("pageOrder", request.getParameter("pageOrder"));
			paramMap.put("title", request.getParameter("title"));
			paramMap.put("theme", request.getParameter("theme"));
			paramMap.put("templatePath", request.getParameter("templatePath"));

			pageDelegator.updateMyPage(userInfoMap, paramMap);

			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
				errorMessage = enviewMessages.getString(msgKey);
			}

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(errorMessage).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}

		return new ModelAndView("userMenu/detailForAjax");
	}

	/**
	 * 마이페이지 정보를 삭제한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView removeMyPageForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {

		StringBuffer buffer = new StringBuffer();

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new BaseException("You have to login !!!");
			}

			Map paramMap = new HashMap();
			paramMap.put("path", request.getParameter("path"));

			pageDelegator.removeMyPage(userInfoMap, paramMap);

			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");

			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
				MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
				errorMessage = enviewMessages.getString(msgKey);
			}

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(errorMessage).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}

		return null; // new ModelAndView();
	}
	
	
	/**
	 * 마이페이지 정보를 삭제한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView removeAllMyPageForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {

		StringBuffer buffer = new StringBuffer();

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new BaseException("You have to login !!!");
			}
			String groupDefaultPage = (String)userInfoMap.get("group_default_page");
			
			Map paramMap = new HashMap();
			String path="/user/" + userInfoMap.get("user_id");
			logger.info("path=" + path);
			paramMap.put("path", path);

			pageDelegator.removeAllMyPage(userInfoMap, paramMap);
			
			paramMap.clear();
			paramMap.put("principalId", (String) userInfoMap.get("principal_id"));
			paramMap.put("pagePath", null);
			paramMap.put("domainId", Enview.getUserDomain().getDomainId());
			pageDelegator.setHomepage(userInfoMap, paramMap);

			UserInfo userInfo = enviewSessionManager.getUserData(request);
			
			userInfo.getUserInfoMap().put("default_page", groupDefaultPage);
			enviewSessionManager.setUserData(request, userInfo);
			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");

			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
				MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
				errorMessage = enviewMessages.getString(msgKey);
			}

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(errorMessage).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}

		return null; // new ModelAndView();
	}
	
	/**
	 * 마이페이지를 생성한다.(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView createMyPageForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {

		StringBuffer buffer = new StringBuffer();

		try {
			UserInfo userInfo = EnviewSSOManager.getUserInfo(request);
			
			String userId = request.getParameter("userId");
			String templetePath = request.getParameter("templetePath");
			Map userInfoMap = null;
			if( userId == null || userId.length()==0) {
				userInfoMap = userInfo.getUserInfoMap();
				userId = userInfo.getUserId();
			} else {
				// 다른 사람의 마이페이지 설정. admin이나 도메인 매니저만 가능
				if( !userInfo.getHasAdminRole() && userInfo.getHasDomainManagerRole()) {
					throw new BaseException("you have not admin/doimain manager role !!!");
				}
				userInfoMap = siteUserManager.getUserInfo(userId, null, EnviewSSOManager.getLangKnd(request)).getUserInfoMap();
			}

			// remove existing my page
			Map paramMap = new HashMap();
			String path="/user/" + getMyPageName(userInfoMap);
			logger.info("path=" + path);
			paramMap.put("path", path);
			pageDelegator.removeAllMyPage(userInfoMap, paramMap);
			
			// create my page of other user
			pageDelegator.createMyPage( userInfoMap,  templetePath);

			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");

			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
				MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
				errorMessage = enviewMessages.getString(msgKey);
			}

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(errorMessage).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}

		return null; // new ModelAndView();
	}

	
	/**
	 * 마이페이지를 홈페이지로 설정한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView setMyPageToHomeForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		StringBuffer buffer = new StringBuffer();

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new BaseException("You have to login !!!");
			}
			String myPage = request.getParameter("path");
			Map paramMap = new HashMap();
			paramMap.put("principalId", (String) userInfoMap.get("principal_id"));
			paramMap.put("pagePath", myPage);
			paramMap.put("domainId", Enview.getUserDomain().getDomainId());
			pageDelegator.setHomepage(userInfoMap, paramMap);

			UserInfo userInfo = enviewSessionManager.getUserData(request);
			userInfo.getUserInfoMap().put("default_page", myPage);
			enviewSessionManager.setUserData(request, userInfo);
			
			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");

			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
				errorMessage = enviewMessages.getString(msgKey);
			}

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(errorMessage).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}

		return null;
	}

	/**
	 * 기본페이지를 홈페이지로 설정한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView setDefaultPageToHomeForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		StringBuffer buffer = new StringBuffer();

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new BaseException("You have to login !!!");
			}
			String groupDefaultPage = (String)userInfoMap.get("group_default_page");
			
			Map paramMap = new HashMap();
			paramMap.put("principalId", (String) userInfoMap.get("principal_id"));
			paramMap.put("pagePath", null);
			paramMap.put("domainId", Enview.getUserDomain().getDomainId());
			pageDelegator.setHomepage(userInfoMap, paramMap);

			UserInfo userInfo = enviewSessionManager.getUserData(request);
			userInfo.getUserInfoMap().put("default_page", groupDefaultPage);
			enviewSessionManager.setUserData(request, userInfo);
			
			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");

			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
				errorMessage = enviewMessages.getString(msgKey);
			}

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(errorMessage).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}

		return null;
	}
	
	/**
	 * 마이페이지안의 포틀릿을 재배치 한다.(ajax).
	 * 현재 레이아웃안의 컬럼안에 들어 있는 포틀릿들에 대해서 순서를 재 배치한다. 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView reorderMyPagePortletForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {

		StringBuffer buffer = new StringBuffer();

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new BaseException("You have to login !!!");
			}

			Map paramMap = new HashMap();
			paramMap.put("pagePath", request.getParameter("page_path"));
			paramMap.put("fragmentIds", request.getParameter("fragment_ids"));
			paramMap.put("parentId", request.getParameter("parent_id"));
			paramMap.put("layoutColumn", request.getParameter("layout_column"));
			paramMap.put("fragments", request.getParameter("fragments"));

			pageDelegator.reorderMyPagePortlet(userInfoMap, paramMap);

			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");

			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException be) {
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			
			if( errorMessage.indexOf("NullPointerException") != -1) {
				msgKey = "ev.error.not.exist.page";
			}
			
			if (msgKey != null) {
				String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
				MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
				errorMessage = enviewMessages.getString(msgKey);
			}

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(errorMessage).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}

		return null; // new ModelAndView();
	}
	
	/**
	 * 마이페이지안의 포틀릿을 삭제한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView removeMyPagePortletForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {

		StringBuffer buffer = new StringBuffer();

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new BaseException("You have to login !!!");
			}

			Map paramMap = new HashMap();
			paramMap.put("pagePath", request.getParameter("page_path"));
			paramMap.put("fragmentId", request.getParameter("fragment_id"));

			pageDelegator.removeMyPagePortlet(paramMap);

			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");

			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
				MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
				errorMessage = enviewMessages.getString(msgKey);
			}

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(errorMessage).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}

		return null; // new ModelAndView();
	}

	/**
	 * 그룹페이지 템플릿 목록을 조회한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView getGroupPageTemplateListForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {

		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		Locale locale = new Locale(langKnd);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		CodeBundle enviewCodeBundle = EnviewCodeManager.getInstance().getBundle(langKnd);

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);

			String templateDir = Enview.getConfiguration().getString("grouppage.template.dir", "/public/template/grouppage");
			List templatePages = (List) pageManager.getPages(templateDir);

			StringBuffer buffer = new StringBuffer();
			buffer.append("{");
			buffer.append("\"Reason\": \"success\",");
			buffer.append("\"TotalSize\": \"").append(templatePages.size()).append("\",");
			buffer.append("\"ResultSize\": \"").append(templatePages.size()).append("\",");
			buffer.append("\"Data\":");
			buffer.append("[");
			int idx = 0;
			for (Iterator it = templatePages.iterator(); it.hasNext(); idx++) {
				Page templatePage = (Page) it.next();
				if (idx > 0) {
					buffer.append(",");
				}
				buffer.append(" {");

				buffer.append("\"id\": \"");
				buffer.append(templatePage.getId());
				buffer.append("\" ");
				buffer.append(", ");
				buffer.append("\"path\": \"");
				buffer.append(templatePage.getPath());
				buffer.append("\" ");
				buffer.append(", ");
				buffer.append("\"name\": \"");
				buffer.append(templatePage.getName());
				buffer.append("\" ");
				buffer.append(", ");
				buffer.append("\"title\": \"");
				buffer.append(templatePage.getShortTitle(locale));
				buffer.append("\" ");
				buffer.append(", ");
				buffer.append("\"desc\": \"");
				buffer.append(templatePage.getTitle(locale));
				buffer.append("\" ");
				buffer.append("}");
			}
			buffer.append("]}");

			log.debug("*** result=" + buffer.toString());

			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
		}
		return null;
	}

	/**
	 * 그룹페이지를 추가한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView addGroupPageForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
		StringBuffer buffer = new StringBuffer();

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new BaseException("You have to login !!!");
			}

			Map paramMap = new HashMap();
			paramMap.put("langKnd", langKnd);
			paramMap.put("title", request.getParameter("title"));
			paramMap.put("theme", request.getParameter("theme"));
			paramMap.put("pageLayout", request.getParameter("pageLayout"));
			paramMap.put("templatePath", request.getParameter("templatePath"));

			String path = pageDelegator.addGroupPage(userInfoMap, paramMap);

			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("\"Path\": \"" + path + "\"");
			buffer.append("}");
			log.info("*** buff=" + buffer.toString());
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				errorMessage = enviewMessages.getString(msgKey);
			}

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(errorMessage).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}

		return null;
	}

	/**
	 * 그룹페이지를 수정한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView updateGroupPageForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		StringBuffer buffer = new StringBuffer();

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new BaseException("You have to login !!!");
			}

			Map paramMap = new HashMap();
			paramMap.put("langKnd", langKnd);
			paramMap.put("path", request.getParameter("path"));
			paramMap.put("pageOrder", request.getParameter("pageOrder"));
			paramMap.put("title", request.getParameter("title"));
			paramMap.put("theme", request.getParameter("theme"));
			paramMap.put("templatePath", request.getParameter("templatePath"));

			pageDelegator.updateGroupPage(userInfoMap, paramMap);

			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
				errorMessage = enviewMessages.getString(msgKey);
			}

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(errorMessage).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}

		return null;
	}

	/**
	 * 그룹페이지를 삭제한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView removeGroupPageForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {

		StringBuffer buffer = new StringBuffer();

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new BaseException("You have to login !!!");
			}

			Map paramMap = new HashMap();
			paramMap.put("path", request.getParameter("path"));

			pageDelegator.removeGroupPage(userInfoMap, paramMap);

			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");

			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
				MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
				errorMessage = enviewMessages.getString(msgKey);
			}

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(errorMessage).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}

		return null; // new ModelAndView();
	}

	/**
	 * 그룹페이지를 홈페이지로 지정한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView setGroupPageToHomeForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		StringBuffer buffer = new StringBuffer();

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new BaseException("You have to login !!!");
			}

			Map paramMap = new HashMap();
			String groupPrincipalId = (String) userInfoMap.get("groupPagePrincipalId");
			log.info("*** groupPrincipalId=" + groupPrincipalId);
			paramMap.put("principalId", groupPrincipalId);
			paramMap.put("pagePath", formData.getPath());

			pageDelegator.setHomepage(userInfoMap, paramMap);

			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");

			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
				errorMessage = enviewMessages.getString(msgKey);
			}

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(errorMessage).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}

		return null;
	}

	/**
	 * 그룹의 홈페이지를기본페이지로 설정한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView setGroupDefaultPageToHomeForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {
		String langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		StringBuffer buffer = new StringBuffer();

		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new BaseException("You have to login !!!");
			}

			Map paramMap = new HashMap();
			String groupPrincipalId = (String) userInfoMap.get("groupPagePrincipalId");
			log.info("*** groupPrincipalId=" + groupPrincipalId);
			paramMap.put("principalId", groupPrincipalId);

			pageDelegator.setHomepage(userInfoMap, paramMap);

			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");

			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException be) {
			log.error( be.getMessage(), be);
			String errorMessage = be.getMessage();
			String msgKey = be.getMessageKey();
			if (msgKey != null) {
				MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle(langKnd);
				errorMessage = enviewMessages.getString(msgKey);
			}

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(errorMessage).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}

		return null;
	}
	
	/**
	 * 테스트(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView testForAjax(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		StringBuffer buffer = new StringBuffer();

		try {
			List results = new ArrayList();
			
			Thread.sleep(1500);

			request.setAttribute("results", results);
		} catch (InterruptedException be) {
			log.error( be.getMessage(), be);

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(be.getMessage()).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}
		
		return new ModelAndView("page/testForAjax");
	}
	
	/**
	 * 사용자 페이지 정보를 저장한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView saveUserPageForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {
		StringBuffer buffer = new StringBuffer();
		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new Exception("You have to login !!!");
			}
			String userId = (String)userInfoMap.get("userId");
			long domainId = Long.parseLong((String)userInfoMap.get("domainId"));

			// 사용자 설정에 저장한다.
			userPreferenceManager.setPreference( request, "MY", request.getParameter("page_path"), request.getParameter("fragments"));
			
			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");
			log.info("*** buff=" + buffer.toString());
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException e) {
			log.error( e.getMessage(), e);

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(e.getMessage()).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}
		return null;
	}
	
	/**
	 * 사용자 페이지 정보를 삭제한다(ajax).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formData 페이지 폼 정보  
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView removeUserPageForAjax(HttpServletRequest request, HttpServletResponse response, PageForm formData) throws Exception {
		StringBuffer buffer = new StringBuffer();
		try {
			Map userInfoMap = EnviewSSOManager.getUserInfoMap(request);
			if (userInfoMap == null) {
				log.debug("*** You have to login !!!");
				throw new Exception("You have to login !!!");
			}
			String userId = (String)userInfoMap.get("userId");
			long domainId = Long.parseLong((String)userInfoMap.get("domainId"));

			// 사용자 설정에 저장한다.
			userPreferenceManager.removePreference( request, "MY", request.getParameter("page_path"));
			
			buffer.append("{");
			buffer.append("\"Status\": \"success\"");
			buffer.append("}");
			log.info("*** buff=" + buffer.toString());
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		} catch (BaseException e) {
			log.error( e.getMessage(), e);

			buffer.append("{");
			buffer.append("\"Status\": \"fail\",");
			buffer.append("\"Reason\": \"").append(e.getMessage()).append("\"");
			buffer.append("}");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(buffer.toString());
		}
		return null;
	}
	
	public String getMyPageName(Map userInfoMap) {
		String myPageNameKey =  Enview.getConfiguration().getString("mypage.pageName.key", "userId");
		return  (String)userInfoMap.get( myPageNameKey);
	}
	
}
