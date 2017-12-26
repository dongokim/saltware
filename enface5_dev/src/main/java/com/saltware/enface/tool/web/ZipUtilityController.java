package com.saltware.enface.tool.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.saltware.enface.tool.service.ToolManager;
import com.saltware.enview.Enview;
import com.saltware.enview.code.EnviewCodeManager;
import com.saltware.enview.codebase.CodeBundle;
import com.saltware.enview.multiresource.EnviewMultiResourceManager;
import com.saltware.enview.multiresource.MultiResourceBundle;
import com.saltware.enview.session.SessionManager;
import com.saltware.enview.util.EnviewLocale;

public class ZipUtilityController extends MultiActionController {
	
	private ToolManager toolManager;
	private SessionManager enviewSessionManager;
	private MultiResourceBundle enviewMessages; 
	private CodeBundle enviewCodeBundle;
	
	private String langKnd;
	private String formName;
	
	public ZipUtilityController() {
		super();
        this.enviewSessionManager = (SessionManager)Enview.getComponentManager().getComponent("com.saltware.enview.session.SessionManager");
	}
	
	public ToolManager getToolManager() {
		return toolManager;
	}

	public void setToolManager(ToolManager toolManager) {
		this.toolManager = toolManager;
	}
	
	private String initData(HttpServletRequest request) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s initData' method...");
		}
		if( this.langKnd == null ) {
			this.langKnd = EnviewLocale.getLocale(request, enviewSessionManager);
		}
		
		if( this.enviewMessages == null ) {
			this.enviewMessages = EnviewMultiResourceManager.getInstance().getBundle( langKnd );
		}
		
		if( this.enviewCodeBundle == null ) {
			this.enviewCodeBundle = EnviewCodeManager.getInstance().getBundle( langKnd );
		}
		
		formName = (String)request.getParameter("formName");
		
		String loginUrl = Enview.getConfiguration().getString("enface.login.page", "http://localhost:8080/enview/user/login.face");
		request.setAttribute("loginUrl", loginUrl);
		
		if(langKnd == null){
			langKnd = "ko";
		}
		
		request.setAttribute("langKnd", langKnd);
		request.setAttribute("formName", formName);
		return request.getParameter("m");
		
	}
	
	public ModelAndView searchZip(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s ajaxSearchZip' method... parameter method is '" + request.getParameter("m") + "'");
		}
		String method = initData(request);
		if(method != null && !method.equals("")){
			if(method.equals("search")){
				return responseSearch(request);
			}
			else{
				return responseForm(request);
			}
		}
		else{
			return new ModelAndView("tool/ajaxSearchZip");
		}
		
	}
	
	protected ModelAndView responseForm(HttpServletRequest request)	throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseForm' method...");
		}
		return new ModelAndView("tool/ajaxSearchZip");
	}
	
	protected ModelAndView responseSearch(HttpServletRequest request) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseSearch' method...");
		}
		String dong = request.getParameter("homeAddr1");
		List zipCodes = new ArrayList();
		zipCodes.addAll(toolManager.getZipCodes(dong, langKnd));
		if(zipCodes.isEmpty()){
			request.setAttribute("dong", dong);
		}
		request.setAttribute("zipCodes", zipCodes);
		return new ModelAndView("tool/ajaxZipResultSet");
	}

}
