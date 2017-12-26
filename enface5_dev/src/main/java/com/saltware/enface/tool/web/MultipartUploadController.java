package com.saltware.enface.tool.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.saltware.enface.tool.service.MultipartFileVO;
import com.saltware.enface.tool.service.ToolManager;
import com.saltware.enface.util.FileUtil;
import com.saltware.enview.Enview;
import com.saltware.enview.code.EnviewCodeManager;
import com.saltware.enview.codebase.CodeBundle;
import com.saltware.enview.login.LoginConstants;
import com.saltware.enview.multiresource.EnviewMultiResourceManager;
import com.saltware.enview.multiresource.MultiResourceBundle;
import com.saltware.enview.session.SessionManager;
import com.saltware.enview.util.EnviewLocale;

public class MultipartUploadController extends MultiActionController  {

	private ToolManager toolManager;
	private SessionManager enviewSessionManager;
	private MultiResourceBundle enviewMessages; 
	private CodeBundle enviewCodeBundle;
	
	private String langKnd;
	
	public MultipartUploadController() {
		super();
		this.enviewSessionManager = (SessionManager)Enview.getComponentManager().getComponent("com.saltware.enview.session.SessionManager");
	}
	
	private void initData(HttpServletRequest request) throws Exception {
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
		
		String loginUrl = Enview.getConfiguration().getString("enface.login.page", "http://localhost:8080/enview/user/login.face");
		request.setAttribute("loginUrl", loginUrl);
		
		if(langKnd == null){
			langKnd = "ko";
		}
		request.setAttribute("langKnd", langKnd);
		logger.debug("initData: Attribute 'langKnd' is '" + langKnd + "'");
	}
	
	public ModelAndView fileUpload(HttpServletRequest request, HttpServletResponse response, MultipartFileVO multipartFile) throws Exception {
		initData(request);
		
		if(multipartFile.getMultipartFile() != null){
			logger.debug("multipartFile's OriginalFileName = " + multipartFile.getMultipartFile().getOriginalFilename());
			HttpSession session = request.getSession(true);
			FileUtil.uploadMultipartFile(Enview.getConfiguration().getString("enface.upload.tool.dir") + (String)session.getAttribute(LoginConstants.SSO_LOGIN_ID), multipartFile.getMultipartFile(), true);
			
			request.setAttribute("fileName", multipartFile.getMultipartFile().getOriginalFilename());
			request.setAttribute("fileSize", new Long(multipartFile.getMBSize()));
			request.setAttribute("fileType", multipartFile.getMultipartFile().getContentType());
		}
		return new ModelAndView("tool/multipart");
	}
	
	public ToolManager getToolManager() {
		return toolManager;
	}

	public void setToolManager(ToolManager toolManager) {
		this.toolManager = toolManager;
	}
}
