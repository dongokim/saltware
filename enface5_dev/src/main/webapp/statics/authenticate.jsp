<%@page import="com.saltware.enview.util.json.JSONObject"%>
<%@page import="java.util.List"%>
<%@page import="com.saltware.enview.exception.BaseException"%>
<%@page import="com.saltware.enface.user.service.SiteUserManager"%>
<%@page import="com.saltware.enview.security.UserManager"%>
<%@page import="java.util.Map"%>
<%@page import="com.saltware.enview.multiresource.EnviewMultiResourceManager"%>
<%@page import="com.saltware.enview.multiresource.MultiResourceBundle"%>
<%@page import="com.saltware.enview.sso.EnviewSSOManager"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%@ page import="com.saltware.enview.Enview"%>
<%
	String langKnd = request.getParameter("langKnd");
	if( langKnd==null) {
		langKnd = EnviewSSOManager.getLangKnd(request);
	}
	MultiResourceBundle enviewMessages = EnviewMultiResourceManager.getInstance().getBundle( langKnd);
	SiteUserManager siteUserManager = (SiteUserManager)Enview.getComponentManager().getComponent("com.saltware.enface.user.service.UserManager");
	
	String status = "Success";
	String reason = "";
	
	Enview.setUserDomain( EnviewSSOManager.getDomainInfo(request));
	
	String userId = request.getParameter("userId");
	String password = request.getParameter("password");
	Map userInfoMap = null;
	try {
		siteUserManager.authenticate(userId, password);
		userInfoMap = siteUserManager.getUserInfo(userId, null, langKnd).getUserInfoMap();
		
	} catch( BaseException be){
		status = "Fail";
		String msgKey = be.getMessageKey();
		if (msgKey != null) {
			reason = enviewMessages.getString(msgKey);
		} else {
			reason = be.getMessage();
		}
	} catch( Exception e){
		status = "Fail";
		reason = e.getMessage();
	}
	
	StringBuilder sb = new StringBuilder();
	if( "Success".equals(status)) {
		StringBuilder groups = new StringBuilder();
		List groupList = (List)userInfoMap.get("groups");
		for (int i = 0; i < groupList.size(); i++) {
			if( groups.length() > 0) {
				groups.append(",");
			}
			groups.append( groupList.get(i));
		}
		sb.append( "{")
		.append("Status : ").append( JSONObject.quote(status))
		.append( ",").append( "Reason : ").append( JSONObject.quote(reason))
		.append( ",").append( "UserInfo : ")
			.append("{")
			.append("userId : ").append( JSONObject.quote((String)userInfoMap.get("userId")))
			.append( ",").append("nmKor : ").append( JSONObject.quote((String)userInfoMap.get("userNameKo")))
			.append( ",").append("orgName : ").append( JSONObject.quote((String)userInfoMap.get("orgNameKo")))
			.append( ",").append("emailAddr : ").append( JSONObject.quote((String)userInfoMap.get("emailAddr")))
			.append( ",").append("mobileTel : ").append( JSONObject.quote((String)userInfoMap.get("mobileTel")))
			.append( ",").append("groups : ").append( JSONObject.quote( groups.toString()))
			.append( "}")
		.append( "}");
		
	} else {
		sb.append( "{")
		.append("Status : ").append( JSONObject.quote(status))
		.append( ",").append( "Reason : ").append( JSONObject.quote(reason))
		.append( "}");
	}
	out.print(sb.toString());
	out.flush();
%>
