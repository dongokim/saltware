<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원 정보 확인</title>
<style type="text/css">
	.a {font-family:굴림; color: #7744DD }
</style>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">
	function initPage(){

	}

	/* 전 단계로 가기 위한 변수 설정(provision, finish 페이지를 제외한 모든 페이지 공통 */
	function back(_state){
		_state.value="back";
	}

</script>
</head>
<body onload="initPage()">
	<center>
		<br><br>
		<form name="registryForm" method="post">
			<input type="hidden" name="isOverlap" value="1"/>
			<table width="480" height="320" bordercolor="gray" border="1" bgcolor="#DDDDDD">
				<tr class="a" height="30">
					<th colspan="2" align="center" style="font-style: bold">가입 정보 확인</th>
				</tr>
				<tr class="a" align="left">
					<th style="font-style: bold">사용자 이름 *</th>
					<td>&nbsp;<c:out value="${user.user_name }"/></td>
				</tr>
				<tr class="a" align="left">
					<th style="font-style: bold">희망 아이디 *</th>
					<td>&nbsp;<c:out value="${user.user_id }"/></td>
				</tr>
				<tr class="a" align="left">
					<th style="font-style: bold">휴대폰 번호*</th>
					<td>&nbsp;<c:out value="${user.user_hp }"/></td>
				</tr>
				<tr class="a" align="left">
					<th style="font-style: bold">이메일*</th>
					<td>&nbsp;<c:out value="${user.user_email }"/></td>
				</tr>
			</table>
			<p></p>
			<input type="hidden" name="_state" value="go"/>
			<input type="submit" name="_finish" value="완료"/>
			<input type="submit" name="_target4" value="전단계로" onclick="back(_state)"/>
			<input type="submit" name="_cancel" value="취소"/>
		</form>
	</center>
</body>
</html>