<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원 정보</title>
<style type="text/css">
	.a {font-family:굴림; color: #7744DD }
</style>
<c:if test="${windowId == null}" var="result">
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/message/messageResource_<c:out value="${langKnd}"/>.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script> 
</c:if>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">
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
		<br><br>
		<form id="modifyForm" name="modifyForm" method="post">
			<input type="hidden" name="isOverlap" value="1"/>
			<table width="740" height="320" bordercolor="gray" border="1" bgcolor="#DDDDDD">
				<tr height="40" class="a" height="30">
					<th colspan="2" align="center" style="font-style: bold">가입 정보 확인</th>
				</tr>
				<tr height="40" class="a" align="left">
					<th style="font-style: bold">&nbsp;사용자 이름</th>
					<td width="580">&nbsp;<c:out value="${user.user_name }"/></td>
				</tr>
				<tr height="40" class="a" align="left">
					<th style="font-style: bold">&nbsp;아이디</th>
					<td>&nbsp;<c:out value="${user.user_id }"/></td>
				</tr>
				<tr height="70" class="a" align="left">
					<th style="font-style: bold">&nbsp;주         소 </th>
					<td>
						&nbsp;우편번호&nbsp;<c:out value="${user.homeZip1}"/>-<c:out value="${user.homeZip2}"/>
						<br><br>
						&nbsp;<c:out value="${user.homeAddr1}"/><br><br>
						&nbsp;<c:out value="${user.homeAddr2}"/>
					</td>
				</tr>
				<tr height="40" class="a" align="left">
					<th style="font-style: bold">&nbsp;휴대폰 번호</th>
					<td>&nbsp;<c:out value="${user.user_hp }"/></td>
				</tr>
				<tr height="40" class="a" align="left">
					<th style="font-style: bold">&nbsp;이메일</th>
					<td>&nbsp;<c:out value="${user.user_email }"/></td>
				</tr>
			</table>
			<p></p>
			<input type="submit" name="_finish" value="확인"/>
			<input type="submit" name="_target2" value="정보 변경"/>
		</form>
	</center>
</body>
</html>