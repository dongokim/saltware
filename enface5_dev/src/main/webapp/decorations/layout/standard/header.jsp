<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="com.saltware.enface.portlet.academic.WeatherPortlet"%>
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
	request.setAttribute("topMenuList", site.getTopMenu( request, rootSite ));
	request.setAttribute("leftMenuList", site.getSubMenu( request, rootSite , 2));
	//request.setAttribute("quickMenuList", site.getQuickMenuList(request));
	//request.setAttribute("myQuickMenuList", site.getMyQuickMenuList(request));

	request.setAttribute("topLeftMenuList", site.getTopMenu( request, "/default/link/top_left" ));
	request.setAttribute("topRightMenuList",  site.getTopMenu( request, "/default/link/top_right" ));
	request.setAttribute("moreSystemMenuList", site.getTopMenu( request, "/default/link/more_system" ));
	
	request.setAttribute("isMyPage", currentPage.getPath().contains("/user/"));
	request.setAttribute("isEditPage", "/contentonly".equals( request.getServletPath()));

	// 날씨정보. 지역코드는 기상청 사이트(kma.or.kr)에서 확인
	request.setAttribute("weatherInfo", WeatherPortlet.getWeather("4573025000", langKnd));
	
	// 현재 언어와 동일한 언어로 변경하는 메뉴 숨김 	
	request.setAttribute( "menu2hide1", "lang_" + EnviewSSOManager.getLangKnd(request));
	// 현재 모바일 여부와 동일하게변경하는 메뉴 숨김
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
	
	//------------------------------------------------------	
	// 게시판 상세페이지인 경우 부모페이지인 목록페이지인것처럼 속인다.
	//------------------------------------------------------	
	String currentPath = currentPage.getPath();
	if( currentPath.endsWith("/detail")) {
		currentPath = currentPath.substring( 0, currentPath.length() - "/detail".length());
	}
	request.setAttribute("currentPath", currentPath);
	//------------------------------------------------------	
	try{
%>
<html>
	<head>
		<title>${PageTitle}</title>	
		<meta http-equiv="Content-type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" />
		<meta http-equiv="Content-style-type" content="text/css"/>   
		<meta name="version" content="3.2.5"/>
		<meta name="keywords" content="" />
		<meta name="description" content="${PageTitle}"/>

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
		
		<!-- 
		<script type="text/javascript" src="${cPath}/javascript/tree/dhtmlxcommon.js"></script>
		<script type="text/javascript" src="${cPath}/javascript/tree/dhtmlXTree.js"></script>
		 -->
		 
		<link rel="stylesheet" type="text/css" media="screen, projection" href="${cPath}/face/css/utility.css" />
		<link rel="stylesheet" type="text/css" media="screen, projection" href="${themePath}/css/style.css" />
		<script type="text/javascript" src="${themePath}/js/main.js"></script>
		
		<script type="text/javascript">
		
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
<!-- header -->
<div id="header">
	<!-- header_wrap -->
	<div class="header_wrap">
		<!-- navigation -->
		<div class="nav">
			<!-- 포틀릿 바로가기 네비게이션 영역 -->
			<ul >
			<c:forEach items="${topLeftMenuList}" var="menu" varStatus="status">
				<c:if test="${!menu.hidden && menu.name != menu2hide1 && menu.name != menu2hide2 && menu.deviceType!='M'}">
				<li <c:if test="${status.index==0}">class='first'</c:if>><a href="<c:out value="${menu.menuUrl}"/>" target='<c:out value="${menu.target}"/>'><c:out value="${menu.shortTitle}"/></a></li>
				</c:if>
			</c:forEach>
			</ul>
			<!-- 포틀릿 바로가기 네비게이션 영역 //-->
			<!-- 서브메뉴 바로가기 네비게이션 영역 -->
			<ul class="nav_login">
			<c:forEach items="${topRightMenuList}" var="menu" varStatus="status">
				<c:if test="${!menu.hidden && menu.name != menu2hide1 && menu.name != menu2hide2 && menu.deviceType!='M'}">
				<li <c:if test="${status.index==0}">class='first'</c:if>><a href="<c:out value="${menu.menuUrl}"/>" target='<c:out value="${menu.target}"/>'><c:out value="${menu.shortTitle}"/></a></li>
				</c:if>
			</c:forEach>
			</ul>
			<!-- 서브메뉴 바로가기 네비게이션 영역 //-->
		</div>
		<!-- navigation //-->
		<h1><img src="${themePath}/images/main/logo.png" alt="솔트웨어 로고" /></h1>
        <!-- searchArea -->
        <div class="searchArea">
            <form name="searchfrm">
                <fieldset>
                    <legend>검색</legend>
					<div class="searchbar">
						<label for="search"></label><input type="text" id="search" class="search" />
					</div>
					<a href="#"><util:message key="ev.prop.search"/></a>
                </fieldset>
            </form>
        </div>
        <!-- searchArea //-->
        <!-- weather -->		
        <a href="#" class="weather">
        	<p><img src="${cPath}/face/images/weather/${weatherInfo.wfIcon}" alt="${weatherInfo.wfIcon}" /><span class="templete">${weatherInfo.tmp}</span></p>
        	<span><strong>${weatherInfo.wf}</strong><strong></strong>${weatherInfo.hour}:00</span>
        </a>
        <!-- weather //-->
		<!-- GNB 영역 -->
		<div class="gnb">
			<ul>
			<c:forEach items="${topMenuList}" var="menu" varStatus="status">
				<c:if test="${!menu.hidden && menu.name != menu2hide1 && menu.name != menu2hide2}">
				<li class="<c:if test="${status.index==0}">first </c:if>${menu.name}"><a href="<c:out value="${menu.fullUrl}"/>" target='<c:out value="${menu.target}"/>'><c:out value="${menu.shortTitle}"/></a></li>
				</c:if>
			</c:forEach>
			</ul>
		</div>
		<!-- GNB 영역 //-->
		<!-- 시스템 더보기 영역 -->
		<div id="menu">
			<!-- allmArea//-->
			<div class="allmArea">
				<p class="btn_off"><a href="#"><span> <util:message key="ev.prop.moreSystems"/></span></a></p>
				<p class="btn_on"><a href="#"><span> <util:message key="ev.prop.moreSystems"/></span></a></p>
				<!-- 시스템 더보기 영역 리스트 리스트-->
				<div id="allmenu">
					<ul>
					<c:forEach items="${moreSystemMenuList}" var="menu" varStatus="status">
						<c:if test="${!menu.hidden && menu.name != menu2hide1 && menu.name != menu2hide2 && menu.deviceType!='M'}">
						<li class="${menu.name}"><a href="<c:out value="${menu.fullUrl}"/>" target='<c:out value="${menu.target}"/>'><c:out value="${menu.shortTitle}"/></a></li>
						</c:if>
					</c:forEach>
					</ul>
				</div>
				<!-- 시스템 더보기 영역 리스트 //-->
			</div>
			<!-- allmArea//-->
		</div>
		<!-- 시스템 더보기 영역 // -->
	</div>
	<!-- header_wrap -->
</div>
<!-- header //-->
<!-- body -->
	<c:if test="${requestScope['enview.portal.requestPage'].pageType!='sub'}">
		<div id="body">
			<!-- body_wrap -->
			<div class="body_wrap">
				<div id="contentsArea">
		            <div id="EnviewContentArea" >
	</c:if>	
	<c:if test="${requestScope['enview.portal.requestPage'].pageType=='sub'}">
		<div id="subbody">
			<!-- body_wrap -->
			<div class="body_wrap">
				<div id="contentsArea">
					<!-- lnbArea -->
					<div id="lnbArea">
						<h2 class="menutitle"><%=subtitle%></h2>
						<ul>
						<c:forEach items="${leftMenuList}" var="menu" varStatus="status"><c:if test="${menu.hidden=='false'}">
							<c:set var="menuClass" value=""/>
							<c:if test="${fn:contains( menu.fullUrl, currentPath)}">
								<c:set var="menuClass" value="class='on'"/>
								<c:if test="${not empty menu.elements}">
								<c:set var="menuClass" value="class='liston'"/>
								</c:if>
							</c:if>
							<c:if test="${not fn:contains( menu.fullUrl, currentPath)}">
								<c:if test="${not empty menu.elements}">
								<c:set var="menuClass" value="class='listoff'"/>
								</c:if>
							</c:if>
							<li ${menuClass}><a href="<c:out value="${menu.fullUrl}"/>" target="${menu.target}"> <c:out value="${menu.shortTitle}"/></a>
							<c:if test="${not empty menu.elements}">
								<ul class="menu_02">
								<c:forEach items="${menu.elements}" var="subMenu" varStatus="status"><c:if test="${subMenu.hidden=='false'}">
									<li class="off"><a href="<c:out value="${subMenu.fullUrl}"/>"  target="${subMenu.target}">&nbsp;<c:out value="${subMenu.shortTitle}"/></a></li>
								</c:if></c:forEach>
								</ul>
							</c:if>
							</li>
						</c:if></c:forEach>
						</ul>
					</div>
		            <!-- container-->
		            <div id="EnviewContentArea">
				</c:if>	
<% } catch(Exception e) {
	Log log = LogFactory.getLog(getClass());
	log.error( e, e);
} %>
