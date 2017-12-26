<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>가입 완료</title>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">
	function goLoginPage(){
		window.location.href="<c:out value='${loginUrl}'/>";		
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
<center>
	<table>
		<tr>
			<th><font color="blue"><c:out value="${user.user_name}"/></font>님, 가입을 축하합니다.</th>
		</tr>
		<tr>
			<th><input type="button" value="로그인" onclick="goLoginPage()"/></th>
		</tr>	
	</table>
</center>
</body>
</html>