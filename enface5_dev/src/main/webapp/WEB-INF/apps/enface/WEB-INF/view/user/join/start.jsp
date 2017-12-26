<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원 가입</title>
<style type="text/css">
	.a {font-family:굴림; color: #7744DD }
</style>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">
	function goLoginPage(){
		location.href="../user/login.face";
	}

	function initPage(){

	}
	
</script>
</head>
<body onload="initPage()">
	<center>
		<br><br>
		<form name="defaultForm" method="post">
			<table bordercolor="gray" border="0" bgcolor="#DDDDDD">
				<tr class="a">
					<th style="font-style: bold" align="left">
					<h5>
					<c:if test = "${joinLimit > 0}" var = "isOverZero">
						같은 주민등록번호로 총 <c:out value="${joinLimit }"/>개 까지 계정을 생성하실 수 있습니다.<br><br>
					</c:if>
					회원가입 버튼을 누르시고 가입 절차를 밟으세요.
					</h5>
					</th>
				</tr>
			</table>
			<p></p>
			<input type="submit" name="_target1" value="회원가입"/>
			<input type="button" name="login" value="로그인" onclick="goLoginPage()"/>
		</form>
	</center>
</body>
</html>