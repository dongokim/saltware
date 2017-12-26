<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>가입 완료</title>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">
	function initPage(){
		
	}

	function defaultPage(){
		window.location.href="info.face";
	}
</script>
</head>
<body onload="initPage()">
	회원정보 변경이 완료되었습니다.<br>
	<input type="submit" value="메인으로" onclick="defaultPage()"/>
</body>
</html>