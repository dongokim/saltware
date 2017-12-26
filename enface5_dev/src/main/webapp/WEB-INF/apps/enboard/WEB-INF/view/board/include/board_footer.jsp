<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
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

	if( request.getAttribute("windownId") == null && !((Boolean) request.getAttribute("isInternal")) ) {
%>
	</body>
</html>
<%
	}
%> 