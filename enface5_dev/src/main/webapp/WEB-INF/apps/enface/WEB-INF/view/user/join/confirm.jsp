<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>기존 아이디 인증</title>
<style type="text/css">
	.a {font-family:굴림; color: #7744DD }
</style>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">

	function nextPage(){
		var form = document.getElementById("confirmForm");
		var password = form["password"].value;
		if(password == ""){
			alert('<util:message key="pt.ev.user.label.EmptyPassword"/>');
			form["password"].focus();
			return false;	 
		}
	}

	function initPage(){
		var errorMessage = "<c:out value="${errorMessage}"/>";
		if( errorMessage != "null" && errorMessage.length > 0 ) {
			alert( errorMessage );
		}
		
		var form = document.getElementById("confirmForm");
		form["password"].focus();
	}
</script>
</head>
<body onload="initPage()">
<center>
	<form id="confirmForm" name="confirmForm" method="post">
		<table id="confirmTable"  bgcolor="#CCCCEE" border="1">
			<tr align="center">
				<th class="a" colspan="2">가입 인증</th>
			</tr>
			<tr align="left">
				<th class="a" align="left">&nbsp;아이디</th>
				<td class="a" align="left">&nbsp;<c:out value="${firstJoinUser}"/></td>
			</tr>
			<tr align="left">
				<th class="a" align="left">&nbsp;비밀번호</th>
				<td class="a" align="left">&nbsp;<input type="password" name="password" maxlength="16" size="17"/></td>
			</tr>
			<tr>
				<th class="a" colspan="2">
					<input type="submit" name="_target5" value="확인" onclick="return nextPage()"/>
					<input type="submit" name="_cancel" value="취소"/>
				</th>
			</tr>
		</table>
		<input type="text" style= "visibility:hidden;">
	</form>
</center>
</body>
</html>