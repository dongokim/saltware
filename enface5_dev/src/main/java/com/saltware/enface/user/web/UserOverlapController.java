package com.saltware.enface.user.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.saltware.enface.user.service.SiteUserManager;
import com.saltware.enface.user.service.UserVO;

public class UserOverlapController extends SimpleFormController {
	
	private SiteUserManager siteUserManager;
	
	public UserOverlapController() {
		setCommandName("user");
        setCommandClass(UserVO.class);
        
	}
	
	public SiteUserManager getUserManager() {
		return siteUserManager;
	}

	public void setUserManager(SiteUserManager siteUserManager) {
		this.siteUserManager = siteUserManager;
	}
	
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {
		String user_id = (String)request.getParameter("check_id");
		if(user_id != null && !user_id.equals("")){
			request.setAttribute("isOverlap", new Boolean(siteUserManager.isOverlap(user_id)));
			request.setAttribute("check_id", user_id);
		}
		return new ModelAndView(getFormView());
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String user_id = request.getParameter("user_id");
		if(user_id != null && ! user_id.equals("")){
			request.setAttribute("isOverlap", new Boolean(siteUserManager.isOverlap(user_id)));
			request.setAttribute("check_id", user_id);
		}
		return new ModelAndView(getSuccessView());
	}

}
