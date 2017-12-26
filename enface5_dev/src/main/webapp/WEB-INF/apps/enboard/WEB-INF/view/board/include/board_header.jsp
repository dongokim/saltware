<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%
// 서블릿 URI를 알아낸다
String requestUri = (String)request.getAttribute("javax.servlet.forward.request_uri");
// 모바일 URI인지 체크한다. 
if( requestUri.indexOf("/mobile/")!=-1)		 {
	request.setAttribute("isMobilePath", true);
}

if( requestUri.indexOf("/enboard/")!=-1) {
	request.setAttribute("boardSkin", "board_basic");
}

if( request.getAttribute("boardPath")==null) {
	request.setAttribute("boardPath", "apiboard");
	request.setAttribute("boardTarget", "_self");
}

if (requestUri.indexOf("/enboard/") > -1 || requestUri.indexOf("/category/") > -1) {
	request.setAttribute("isInternal", true);
} else {
	request.setAttribute("isInternal", false);
}

request.setAttribute("isApiboard", requestUri.contains("/apiboard/"));

// 문구 관리자
//request.setAttribute("commentManager", CommentManager.getInstance());

// 에디터 및 업로드의 경우 boardId를 파라미터로 알 수 없다.
session.setAttribute("currentBoardId", request.getParameter("boardId"));

String protocol = request.getProtocol();
String server = request.getServerName();
int port = request.getServerPort();

String hostUrl = (request.isSecure()?"https://":"http://") + server +( port==80 || port==443 ? "" :  ":" + port); 

request.setAttribute("fullUrl", hostUrl + requestUri.replaceAll("(/category/|/enboard/)", "/apiboard/"));
%>
<%
	if( request.getAttribute("windownId") == null && !((Boolean) request.getAttribute("isInternal")) ) {
%>
<html lang="ko">
	<head>
		<title>SNU Board</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="X-UA-Compatible" content="IE=EDGE" />
		<c:if test="${isMobile}">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" />
		</c:if>
		<meta name="robots" content="noindex,nofollow" />		
<%
	}
%>
		<script type="text/javascript" src="${cPath}/javascript/crossdomain.js"></script>
 		<link type="text/css" rel="stylesheet" href="${cPath}/snu/css/jquery-ui.custom.css" />
<%
	if( request.getAttribute("windownId") == null && !((Boolean) request.getAttribute("isInternal")) ) {
%>

  <%--포틀릿으로서 동작하고 있지 않을 때에는 enView의 Javascript를 포함시켜준다--%>
  		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/javascript/jquery/css/redmond/jquery-ui-1.9.2.custom.min.css" />
  		
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/common_new.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/portal_new.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/utility.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery/jquery-1.10.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery/jquery-ui-1.9.2.custom.min.js"></script>
<%
	}
%>
		<c:if test="${empty secPmsnVO}">
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/message/mrbun_<%=request.getLocale().getLanguage()%>_mm.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/message/mrbun_<%=request.getLocale().getLanguage()%>_eb.js"></script>
		</c:if>
		<c:if test="${!empty secPmsnVO}">
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/message/mrbun_<c:out value="${secPmsnVO.locale}"/>_mm.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/message/mrbun_<c:out value="${secPmsnVO.locale}"/>_eb.js"></script>
		</c:if>
		<script type="text/javascript" src="${pageContext.request.contextPath}/board/javascript/enboard_util.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/board/calendar/calendar.js"></script>
		
		<%-- <script type="text/javascript" src="${cPath}/kaist/main/js/browser_chk.js"></script> --%>
		<script type="text/javascript">
			
			$(document).ready(function() {
				<c:if test="${isMobile}">
				// iOS ellipsis 버그관련
				try {
					var topWidth = $(top.window).width();
					var thisWidth = $(window).width(); 
					 
					if (thisWidth > topWidth) {
						thisWidth = topWidth;
					}
					
					$("body").width(thisWidth);
				} catch(e) {
				}
				</c:if>
			});
			
			function adminEventFree (win) { }
		</script>
	</head>
	
	<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
