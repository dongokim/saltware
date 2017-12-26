<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>아이디 찾기</title>
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
	
	function goLoginPage(){
		location.href="../user/login.face";	
	}
	
	function goJoinPage(){
		location.href="../user/join.face";
	}
	
	function goHelpPage(){
		location.href="../user/help.face";
	}
</script>
</head>
<body onload="initPage()">
	<center>
		<br><br>
		<form name="defaultForm" method="post">
			<table bordercolor="gray" border="0" bgcolor="#DDDDDD">
				<c:if test="${isIdSearch == true}">
					<c:if test="${empty userList}" var="isEmpty">
					<tr class="a" align="left">
						<th style="font-style: bold">입력하신 정보와 일치하는 ID가 없습니다.</th>
					</tr>
					<tr class="a" align="left"></tr>
					<tr class="a" align="left"></tr>
					<tr class="a" align="left"></tr>
					<tr class="a" align="left"></tr>
					<tr class="a" align="left">
						<th style="font-style: bold"><h5>아직 enface 회원이 아니십니까?<br>회원으로 가입하세요!</h5></th>
					</tr>
					</c:if>
					<c:if test="${isEmpty == false}">
					<c:forEach items="${userList}" var="user">
					<tr class="a" align="left">
						<th style="font-style: bold">
							<c:out value="${user.user_id}"/>&nbsp;(<c:out value="${user.regDatimF}"/> 가입)
						</th>
					</tr>
					</c:forEach>
					</c:if>
				</c:if>
				<c:if test="${isIdSearch == false}">
					<tr class="a" height="30">
						<th colspan="2" align="left" style="font-style: bold">
							임시 비밀번호가 <c:out value="${user_email}"/>으로<br>
							발송 되었습니다.
						</th>
					</tr>
					<tr class="a" align="left">
						<th>
							
						</th>					
					</tr>
				</c:if>
			</table>
			<br>
			<table>
				<tr align="left">
					<th>
						<h5>가입하신 ID 혹은 임시 비밀번호가 발송된 E-mail 주소의 ID는<br>
						          정보보호를 위해 8자리로 통일되며 뒤의 4자리는 *표로 표시됩니다.<br>
						</h5>
					</th>
					
				</tr>
			</table>
			<p></p>
			<input type="button" value="로그인" onclick="goLoginPage()"/>
			<input type="button" value="회원가입" onclick="goJoinPage()"/>
			<input type="button" value="다시찾기" onclick="goHelpPage()"/>
		</form>
	</center>
</body>
</html>