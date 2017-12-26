<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="com.saltware.enview.om.page.Page"%>
<%@page import="com.saltware.enview.sso.EnviewSSOManager"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.saltware.enview.security.spi.impl.IsMemberOfPrincipalAssociationHandler"%>
<%@page import="com.saltware.enview.security.EnviewMenu"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="../initLayoutDecorators.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<%
	List topMenuList = site.getTopMenu( request, rootSite );
	List leftMenuList = site.getSubMenu( request, rootSite , 2);
	List quickMenuList = site.getQuickMenuList(request);
	List myQuickMenuList = site.getMyQuickMenuList(request);
	
	request.setAttribute("topMenuList", topMenuList);
	request.setAttribute("leftMenuList", leftMenuList);
	request.setAttribute("quickMenuList", quickMenuList);
	request.setAttribute("myQuickMenuList", myQuickMenuList);

	request.setAttribute("topLeftMenuList", site.getTopMenu( request, "/default/link/top_left" ));
	request.setAttribute("topRightMenuList",  site.getTopMenu( request, "/default/link/top_right" ));
	request.setAttribute("moreSystemMenuList", site.getTopMenu( request, "/default/link/more_system" ));
	
	request.setAttribute("isMyPage", currentPage.getPath().contains("/user/"));
	request.setAttribute("isEditPage", "/contentonly".equals( request.getServletPath()));
	
	// 현재 언어와 동일한 언어로 변경하는 메뉴 숨김 	
	request.setAttribute( "menu2hide1", "lang_" + EnviewSSOManager.getLangKnd(request));
	// 여재 모바일 여부와 동일하게변경하는 메뉴 숨김
	request.setAttribute( "menu2hide2", "mobile_" + ( EnviewSSOManager.isMobile(request) ? "t" : "f"));
	
	String theme = request.getParameter("theme");
	if( theme == null || theme.length() == 0 ) theme = "";
	
	String pageIds = site.getCurrentPageIds(request, 2);
	String breadcrumbs = includeLinksNavigation( site.getNavigation(request, "/admin"), langKnd );
	String subtitle = "";

	if( currentPage.getParent() != null ) 
	{
		subtitle = currentPage.getParent().getShortTitle(locale);
	}
	else 
	{
		subtitle = currentPage.getShortTitle(locale);
	}
	int tabordercnt = 0;   
	
	//System.out.println("#### editing=" + editing + ", noticeList=" + noticeList + ", currentPath=" + currentPath + ", groups=" + groups + ", titleicon=" + titleicon);
	try{
%>
<html>
	<head>
		<title>${PageTitle} [${isMobile}]</title>	
		<meta http-equiv="Content-type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" />
		<meta http-equiv="Content-style-type" content="text/css"/>   
		<meta name="version" content="3.2.5"/>
		<meta name="keywords" content="" />
		<meta name="description" content="${PageTitle}"/>
		<meta name="viewport" content="user-scalable=no, width=device-width" />

		<link rel="stylesheet" type="text/css" media="screen, projection" href="${cPath}/decorations/layout/css/dhtmlxtree.css"  />
		<!-- 
		<link rel="stylesheet" type="text/css" media="screen, projection" href="${cPath}/decorations/layout/css/styles.css"  />
 		-->
		
		<link type="text/css" href="${cPath}/decorations/layout/css/jquery/base/jquery.ui.all.css" rel="stylesheet" />
		<script type="text/javascript" src="${cPath}/javascript/jquery/jquery-1.10.2.min.js"></script>
		<script type="text/javascript" src="${cPath}/javascript/jquery/jquery-ui-1.9.2.custom.min.js"></script>
		<script type="text/javascript" src="${cPath}/javascript/jquery/ui/i18n/jquery.ui.datepicker-${langKnd}.js"></script>
		
		<script type="text/javascript" src="${cPath}/javascript/message/mrbun_${langKnd}_mm.js"></script>
		<script type="text/javascript" src="${cPath}/javascript/message/mrbun_${langKnd}_ev.js"></script>
		<script type="text/javascript" src="${cPath}/javascript/message/mrbun_${langKnd}_eb.js"></script>
		<script type="text/javascript" src="${cPath}/javascript/common_new.js"></script>
		<script type="text/javascript" src="${cPath}/javascript/portal_new.js"></script>
		<script type="text/javascript" src="${cPath}/javascript/utility.js"></script>
		<script type="text/javascript" src="${cPath}/javascript/validator.js"></script>
		
		<script type="text/javascript" src="${cPath}/admin/javascript/portlet.js"></script>
		
		<!-- 
		<script type="text/javascript" src="${cPath}/javascript/tree/dhtmlxcommon.js"></script>
		<script type="text/javascript" src="${cPath}/javascript/tree/dhtmlXTree.js"></script>
		 -->
		 
		<link rel="stylesheet" href="${cPath}/decorations/layout/mobile/css/style.css" type="text/css" media="all"/>
		<link rel="stylesheet" href="${cPath}/decorations/layout/mobile/css/contents.css" type="text/css" media="all"/>
		<link rel="stylesheet" href="${cPath}/decorations/layout/mobile/css/mypage.css" type="text/css" media="all"/>
		<!--[if lt IE 9]>
			<script src="${cPath}/decorations/layout/mobile/js/html5.js"></script>
		<![endif]-->
		<link rel=stylesheet href="${cPath}/decorations/layout/mobile/css/touchSlider.css"/>
		<link rel=stylesheet href="${cPath}/decorations/layout/mobile/css/webkit.css"/>
		<script type=text/javascript src="${cPath}/decorations/layout/mobile/js/jquery.event.drag-1.5.min.js"></script>
		<script type=text/javascript src="${cPath}/decorations/layout/mobile/js/jquery.touchSlider.js"></script>
		<script type=text/javascript src="${cPath}/decorations/layout/mobile/js/portal_new.js"></script>
		<script type=text/javascript src="${cPath}/decorations/layout/mobile/js/fastclick.js"></script>
		<script type=text/javascript src="${cPath}/decorations/layout/mobile/js/m.fullmenu.js"></script>
		<script type=text/javascript src="${cPath}/decorations/layout/mobile/js/commons.js"></script>
		
		<script type="text/javascript">
			$(document).ready(function() {
				$("#wrap").height($("html").height());
				$(".full_menu_wrap").height($("html").height());
	
				$("#wrap").attr("style", "overflow: auto; ");
			});
	
		
			function iframe_autoresize(arg) {
				try {
					var ht = arg.contentWindow.document.documentElement.scrollHeight;
					arg.height = 0;
					arg.height = ht;
				} catch(e) {
					
				}
			}
			
			function getTickerMessage() {
				return tickerMessage;
			}
			
			function changeUser(obj) {
				var userId = obj.options[obj.selectedIndex].value;
				location.href = "${cPath}/login/changeuser?username=" + userId;
			}
			
			function initEnview() {
				<%=initEnviewScript%>
//				initNavigation("<%=userId%>", "<%=pageIds%>");
				//quickMenuInitialize("Enview.Portal.Quickmenu", 1200,300);
			}	
			
			function getAction()
			{
				return 
			}
			
			function resize()
			{
				var menu = document.getElementById("Enview.LeftMenu");
				menu.style.display = "none";
			}
			
			function print(arg) {
				eval(arg.name).focus();
				eval(arg.name).print();
			}
			
			function checkModified() {
				portalPage.checkModified();
			}
			
			// Attach to the onload event
			if (window.attachEvent)
			{
				//window.attachEvent ( "onload", initEnview );
				window.attachEvent ( "onunload", checkModified );
			}
			else if (window.addEventListener )
			{
				//window.addEventListener ( "load", initEnview, false );
				window.addEventListener ( "unload", checkModified, false );
			}
			else
			{
				//window.onload = initEnview;
				window.onunload = checkModified;
			}
		</script>
	</head>
<body>
	<div id="wrap">
		<header role="banner">
			<div class="logo_wrap">
				<h1>
					<a href=""><img src="${cPath}/decorations/layout/mobile/images/logo.png" alt="enview appliance"></a>
				</h1>
				<A id="fullMenu" class=btn_menu style="top:10px" href="javascript:fullmenu('true');"><IMG alt="메뉴" src="${cPath}/decorations/layout/mobile/images/btn_menu.png"></A>
				<A id="fullMenu_dis" class=btn_menu style="top:10px;DISPLAY: none" href="javascript:fullmenu(false);"><IMG alt="메뉴" src="${cPath}/decorations/layout/mobile/images/btn_menu.png"></A>
			</div>
		</header>
		<!-- end header -->

		<section role="main" id="container">
			<article class="content_gray">
				<div id="EnviewContentArea" >
						
<% } catch(Exception e) {
	Log log = LogFactory.getLog(getClass());
	log.error( e, e);
} %>
