<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>환영합니다!</title>
<style type="text/css">
	.a {font-family:굴림; color: #7744DD }
</style>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">
	function initPage(){

	}
	
	function goPasswordChange() {
		location.href = "<util:url view='/user/changePassword.face'/>";
	}
	
</script>
</head>
<body onload="initPage()">
	<center>
		<br><br>
		<form name="defaultForm" method="post">
			<table bordercolor="gray" border="0" bgcolor="#DDDDDD">
				<tr class="a" height="30">
					<th colspan="2" align="center" style="font-style: bold"><c:out value="${user.user_id}"/>님 환영합니다!</th>
				</tr>
				<tr class="a" align="left">
					<th style="font-style: bold">회원정보를 변경할 수 있습니다.</th>
				</tr>
			</table>
			<p></p>
			<input type="submit" name="_target1" value="회원정보변경""/>
			<input type="button" value="비밀번호변경" onclick="goPasswordChange()"/>
		</form>
	</center>
</body>
</html>