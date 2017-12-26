<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, target-densitydpi=medium-dpi" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/kaist/css/mobile/style.css" type="text/css" media="all">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/kaist/css/fullcalendar.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/board/css/board/skin/<c:out value="${boardVO.boardSkin }"/>/calendar.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery/jquery-1.10.2.min.js"></script>
<script type='text/javascript' src="<%=request.getContextPath()%>/javascript/jquery/jquery-ui-1.9.2.custom.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/board/javascript/fullcalendar.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/board/javascript/kaist_event_notice_cal.js"></script>
<style>
	#calendar { 
		background-color : rgb(246, 246, 246); 
		margin-top: 10px; 
		padding: 10px 0; 
		width: 98%;		 
		font-family : '돋음', 'Arial'; 
		font-size: 13px;
		margin: 0 auto; 
		text-shadow:0 0 0;
	}
</style>
<div class="h2_box">
	<h2 class="hidden"><c:out value="${boardNm}"/></h2>
	<ul class="location">
		<li class="first"><a href="/portal/default/mobile/notice" target="_top"><util:message key="kaist.mobile.fullmenu.notice"/></a></li>
		<li><a><c:out value="${boardNm}"/></a></li>
	</ul>
	<a href="/portal/default/mobile/notice" target="_top" class="btn_a"><util:message key="kaist.mobile.board.otherboardview"/></a>
</div>
<section role="main" id="container">
	<div id="calendar" ></div>
	<article class="content_gray" id="content_gray">
		<div style="height: 315px; overflow-y: auto;" class="list_wrap_w" id="onedayList"></div>
	</article>
	
</section>
