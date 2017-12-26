<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>가입 완료</title>
<c:if test="${windowId == null}" var="result">
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/message/messageResource_<c:out value="${langKnd}"/>.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script> 
</c:if>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">
	function goChangeInformationPage(){
		window.location.href="<util:url view='/user/changeInformation.face'/>";
	}

	function initPage(){
		var errorMessage = "<c:out value="${errorMessage}"/>";
		if( errorMessage != "null" && errorMessage.length > 0 ) {
			alert( errorMessage );
		}
	}
</script>
</head>
<body onload="initPage()">
	회원정보 변경이 완료되었습니다.<br>
	<input type="button" value="메인으로" onclick="goChangeInformationPage()"/>
</body>
</html>