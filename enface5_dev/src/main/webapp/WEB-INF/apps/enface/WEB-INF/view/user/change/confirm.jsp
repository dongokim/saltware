<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원정보 변경</title>
<c:if test="${windowId == null}" var="result">
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/message/messageResource_<c:out value="${langKnd}"/>.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script> 
</c:if>
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
		else{
			return true;
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
				<th colspan="2">가입 인증</th>
			</tr>
			<tr align="left">
				<th>&nbsp;아이디</th>
				<td>&nbsp;<c:out value="${user.user_id}"/></td>
			</tr>
			<tr align="left">
				<th>&nbsp;비밀번호</th>
				<td>&nbsp;<input type="password" name="password" maxlength="16" size="17"/></td>
			</tr>
			<tr>
				<th colspan="2">
					<input type="submit" name="_target2" value="확인" onclick="return nextPage()"/>
					<input type="submit" name="_cancel" value="취소"/>
				</th>
			</tr>
		</table>
		<input type="text" style= "visibility:hidden;">
	</form>
</center>
</body>
</html>