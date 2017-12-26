<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
	java.util.Enumeration em = request.getParameterNames();
	        for( ; em.hasMoreElements(); ) {
	            String k = (String)em.nextElement();
	            String val = request.getParameter( k );
	            System.out.println( "regNoConfirm.jsp Request : " + k + " = " + val);
	        }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>실명인증</title>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">

	/* 주민번호 유효성 여부 */
	var isValidateRegNum = false;
	
	function nextPage(){
		var user_jumin1 = document.confirmForm.user_jumin1;
		var user_jumin2 = document.confirmForm.user_jumin2;
		var user_name = document.confirmForm.user_name;
		if(user_name.value == ""){
			alert('이름을 입력하세요');
			user_name.focus();
			return false;	 
		}
		else if(isNaN(user_jumin1.value)){
			alert('주민번호는 숫자만 입력하세요.');
			user_jumin1.value = "";
			user_jumin1.focus();
			return false;
		}
		else if(isNaN(user_jumin2.value)){
			alert('주민번호는 숫자만 입력하세요.');
			user_jumin2.value = "";
			user_jumin2.focus();
			return false;
		}
		else if(user_jumin1.value.length !=6 || user_jumin2.value.length != 7){
			alert('주민번호숫자 13자리를 정확히 입력하세요.');
			return false;
		}
		else{
			return true;
		}
	}

	function back(_state){
		_state.value="back";
	}

	function initPage(){
		var user_name = document.confirmForm.user_name;
		user_name.focus();
	}
	
</script>
</head>
<body onload="initPage()">
<center>
	<form name="confirmForm" method="post">
		<table id="confirmTable" border="1" width="800" height="300">
			<tr align="center">
				<th>실명 인증</th>
			</tr>
			<tr align="center">
				<th>
				이름 <input type="text" name="user_name" maxlength="12"/>
				주민등록번호 <input type="text" name="user_jumin1" size="6" maxlength="6"/>-<input type="password" name="user_jumin2" size="7" maxlength="7"/></th>
			</tr>
			<tr align="center">
				<th colspan="2">개정 "주민등록법"에 의해 타인의 주민등록번호를 부정사용하는 자는 3년 이하의 징역 또는 1천만원. 이하의 벌금이 부과될 수 있습니다. 관련법률: 주민등록법 제37조(벌칙) 제10호(시행일 : 2009.04.01)
					만약, 타인의 주민번호를 도용하여 온라인 회원 가입을 하신 이용자분들은 지금 즉시 명의 도용을 중단하시길 바랍니다.
				</th>
			</tr>
			<tr>
				<th colspan="2">
					<input type="hidden" name="_state" value="go"/>
					<input type="submit" name="_target2" value="다음 단계로" onclick="return nextPage()"/>
					<input type="submit" name="_target0" value="약관 다시 확인" onclick="back(_state)"/>
					<input type="submit" name="_cancel" value="취소"/>
				</th>
			</tr>
		</table>
		
	</form>
</center>
</body>
</html>