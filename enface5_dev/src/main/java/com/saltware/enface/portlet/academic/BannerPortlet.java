package com.saltware.enface.portlet.academic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enface.util.StringUtil;
import com.saltware.enhancer.banner.service.BannerUserVO;
import com.saltware.enhancer.banner.service.impl.BannerUserDAO;
import com.saltware.enview.Enview;
import com.saltware.enview.PortalReservedParameters;
import com.saltware.enview.domain.DomainInfo;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.request.RequestContext;
import com.saltware.enview.sso.EnviewSSOManager;

/**  
 * @Class Name : BannerPortlet.java
 * @Description : 배너 Portlet
 * @
 * @author kevin
 * @since 2014.03.04 12:58:385
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by Saltware All right reserved.
 */

public class BannerPortlet extends AcademicPortlet {
	private final Log log = LogFactory.getLog(getClass());
	private BannerUserDAO bannerUserDAO;
	
    /**
	 * lecturePortlet을 초기화한다.
	 */
	public void init(PortletConfig config) throws PortletException{
		this.bannerUserDAO = (BannerUserDAO)Enview.getComponentManager().getComponent("bannerUserDAO");
        super.init(config);
	}
	
	/**
	 * lecturePortlet의 화면을 출력한다.
	 *
	 * @see javax.portlet.lecturePortlet#doView
	 */
	public void doView(RenderRequest request, RenderResponse response)	throws PortletException, IOException 
	{
		String viewName="";
		String categorySeq=null;
		DomainInfo domain = Enview.getUserDomain();
		String domainId = domain.getDomainId();
		
		try {
			RequestContext requestContext = (RequestContext) request.getAttribute(PortalReservedParameters.REQUEST_CONTEXT_ATTRIBUTE);
			HttpServletRequest req = requestContext.getRequest();
			HttpServletResponse res = requestContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			
			Map userInfo = EnviewSSOManager.getUserInfo(req).getUserInfoMap();
 			if( userInfo == null ) {
            	throw new BaseException("You have to login !!!");
 			}

 			List<BannerUserVO> bannerList = new ArrayList<BannerUserVO>();
			String userId = StringUtil.nvl( (String) userInfo.get("user_id"));   //로그인아이디
			List groups  = (List)userInfo.get("groups");
			String flag_group = request.getParameter("flag_group"); // 관리자계정 접속시 각 그룹별 포틀릿확인.
			String langKnd = "ko";
			String site_name ="";
			
 			if(userInfo != null ) {
 					langKnd = (String)userInfo.get("lang_knd");
 					
 					if(langKnd == null || "".equals(langKnd)){
	 					langKnd = "ko";
	 					userInfo.put("lang_knd", langKnd);
 					}else if(!"ko".equals(langKnd) && !"en".equals(langKnd)){
 						langKnd = "ko";
 						userInfo.put("lang_knd", langKnd);
 					}
 					site_name = (String)userInfo.get("site_name");
 				}
 			
 			categorySeq =  request.getPreferences().getValue("CATEGORY_SEQ","0");
 					 
 			Map param = new HashMap();
 			param.put("DOMAIN_ID", domainId);
 			param.put("LANG_KND", langKnd);
 			param.put("CATEGORY_SEQ", categorySeq);

 			bannerList = bannerUserDAO.getBannerList(param);
			req.setAttribute("bannerList", bannerList);
			req.setAttribute("site_name", site_name);
			req.setAttribute("langKnd", langKnd);
			super.doView(request, response);
		} catch (SQLException e) {
            log.error( e.getMessage(), e);
			throw new PortletException(e);
		} catch (BaseException e) {
            log.error( e.getMessage(), e);
			throw new PortletException(e);
		}
	}
}