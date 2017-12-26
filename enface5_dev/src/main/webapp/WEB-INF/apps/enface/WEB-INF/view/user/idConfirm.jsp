<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>기존 아이디 인증</title>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">
	function initPage(){
		
	}

	function nextPage(){
		var password = document.confirmForm.password;
		if(password.value == ""){
			alert('비밀번호를 입력하세요');
			password.focus();
			return false;	 
		}
		else{
			return true;
		}
	}

	/* 전 단계로 가기 위한 변수 설정(provision, finish 페이지를 제외한 모든 페이지 공통 */
	function back(_state){
		_state.value="back";
	}
</script>
</head>
<body onload="initPage()">
<center>
	<form name="confirmForm" method="post">
		<table id="confirmTable" border="1" width="400" height="50">
			<tr align="center">
				<th>기존 아이디 인증</th>
			</tr>
			<tr align="center">
				<th>아이디&nbsp;&nbsp;<c:out value="${user.user_id}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					비밀번호&nbsp;<input type="password" name="password"/>
				</th>
			</tr>
			<tr>
				<th>
					<input type="hidden" name="_state" value="go"/>
					<input type="submit" name="_target4" value="확인" onclick="return nextPage()"/>
					<input type="submit" name="_target0" value="약관 다시 확인" onclick="back(_state)"/>
					<input type="submit" name="_cancel" value="취소"/>
				</th>
			</tr>
		</table>
		<input type="text" style= "visibility:hidden;">
	</form>
</center>
</body>
</html>