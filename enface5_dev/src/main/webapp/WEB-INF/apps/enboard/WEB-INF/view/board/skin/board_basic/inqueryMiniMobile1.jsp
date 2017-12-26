<%@ page import="com.saltware.enview.Enview"%>
<%@ page import="com.saltware.enview.security.EVSubject"%>
<%@ page import="com.saltware.enboard.vo.BoardVO"%>
<%@ page import="com.saltware.enboard.vo.BoardSttVO"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld"%>
<%
	String staticFileVersion = "20151216";
	String langKnd = (String) pageContext.getSession().getAttribute(
			"langKnd");
	String cPath = request.getContextPath();
	String userId = "";
	if (EVSubject.getUserInfo() != null) {
		userId = EVSubject.getUserId();
	}
	request.setAttribute("userId", userId);
	request.setAttribute("langKnd", langKnd);
	request.setAttribute("cPath", cPath);
	request.setAttribute("isUseReadYn", Enview.getConfiguration()
			.getBoolean("board.red.flag.use"));
	request.setAttribute("serverName", request.getServerName());
	request.setAttribute("localTarget",
			"http://local.abc.ac.kr:8081/demo/research/TO-BE.html");

	BoardSttVO boardSttVO = (BoardSttVO) request
			.getAttribute("boardSttVO");
	BoardVO boardVO = (BoardVO) request.getAttribute("boardVO");
%>

<link rel="stylesheet" href="<%=cPath%>/kaist_portal/css/reqt/req_mobile.css" type="text/css" media="all" />
<link rel="stylesheet" href="<%=cPath%>/javascript/jquery/jquery-ui-1.9.2.custom/css/redmond/jquery-ui-1.9.2.custom.min.css?<%=staticFileVersion%>" type="text/css" />

<script type="text/javascript" src="<%=cPath%>/javascript/jquery/jquery-1.7.2.min.js?<%=staticFileVersion%>"></script>
<script type="text/javascript" src="<%=cPath%>/javascript/jquery/jquery-ui-1.9.2.custom/js/jquery-ui-1.9.2.custom.min.js?<%=staticFileVersion%>"></script>
<script type="text/javascript" src="<%=cPath%>/kaist_portal/javascript/mobile/jquery.event.drag-1.5.min.js?<%=staticFileVersion %>"></script>
<script type="text/javascript" src="<%=cPath%>/kaist_portal/javascript/mobile/jquery.touchSlider.js?<%=staticFileVersion %>"></script>
<script type="text/javascript" src="<%=cPath%>/kaist_portal/javascript/mobile/jquery.mobileSlider.js?<%=staticFileVersion %>"></script>

<script type="text/javascript" src="<%=cPath%>/kaist_portal/javascript/mobile/fastclick.js?<%=staticFileVersion %>"></script>

<script type="text/javascript">
<!--
$(document).ready(function() {
	$("#slide_2_1").off();
	$("#slide_2_1").touchSlider({ roll : false, flexible : true, counter : function (e) {
		if (e.current == 1) {
			$("#slide_bul_2_1_" + (e.current + 1)).removeClass("on");
		} else {
			$("#slide_bul_2_1_" + (e.current - 1)).removeClass("on");
		}
		$("#slide_bul_2_1_" + e.current).addClass("on");
	}});
});
//-->
</script>
<body>
<!-- 신청내역 컨텐츠 -->
<ul class="tab_contents_list" id="slide_2_1">
	<li class="swipe_wrap">
		<div class="mover">
			<ul>
				<c:forEach items="${bltnList}" var="list" varStatus="status" end="3">
				<li>
					<c:if test="${empty boardVO.miniTrgtUrl }">
						<c:set var="url" value="/enboard/${boardVO.boardId }/${list.bltnNo}" />
						<c:set var="target" value="_top"/>
					</c:if>
					<c:if test="${!empty boardVO.miniTrgtUrl }">
						<c:set var="url" value="${boardVO.miniTrgtUrl}${fn:indexOf(boardVO.miniTrgtUrl, '?')==-1 ? '?' : '&'}boardPath=/${boardVO.boardId}/${list.bltnNo}" />
						<c:set var="target" value="_top"/>
					</c:if>
					<a href="<c:out value='${url }'/>" target="${target }" title="<c:out value="${list.bltnOrgSubj}"/>">
						<c:out value="${list.bltnOrgSubj}"/>
					</a>
				</li>
				</c:forEach>
			</ul>
		</div>
		<c:if test="${fn:length(bltnList) gt 4 }">
		<div class="mover">
			<ul>
				<c:forEach items="${bltnList}" var="list" varStatus="status" begin="4" end="7">
				<li>
					<c:if test="${empty boardVO.miniTrgtUrl }">
						<c:set var="url" value="/enboard/${boardVO.boardId }/${list.bltnNo}" />
						<c:set var="target" value="_top"/>
					</c:if>
					<c:if test="${!empty boardVO.miniTrgtUrl }">
						<c:set var="url" value="${boardVO.miniTrgtUrl}${fn:indexOf(boardVO.miniTrgtUrl, '?')==-1 ? '?' : '&'}boardPath=/${boardVO.boardId}/${list.bltnNo}" />
						<c:set var="target" value="_top"/>
					</c:if>
					<a href="<c:out value='${url }'/>" target="${target }" title="<c:out value="${list.bltnOrgSubj}"/>">
						<c:out value="${list.bltnOrgSubj}"/>
					</a>
				</li>
				</c:forEach>
			</ul>
		</div>
		</c:if>
	</li>
</ul>
<!-- //신청내역 컨텐츠 -->
<!-- 페이징 -->
<div class="contents_paging" style="height: 19px;">
	<ol>
		<c:set var="end" value="1"/>
       	<c:if test="${fn:length(bltnList) gt 4 }">
       		<c:set var="end" value="2"/>
       	</c:if>
       	<c:forEach begin="1" end="${end }" step="1" varStatus="stat">
       		<li id="slide_bul_2_1_${stat.count }">${stat.count }</li>
       	</c:forEach>
	</ol>
</div>
<!-- //페이징 -->
</body>