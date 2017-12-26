<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>가입 완료</title>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">
	function login(){
		window.location.href="login.face";		
	}

	function initPage(){
		
	}
</script>
</head>
<body onload="initPage()">
	<c:out value="${user.user_name}"/>님 가입을 축하합니다!<br>
	<input type="button" value="login" onclick="login()"/>
</body>
</html>