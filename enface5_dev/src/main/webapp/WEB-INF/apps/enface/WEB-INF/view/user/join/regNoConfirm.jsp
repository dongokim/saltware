<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>실명인증</title>
<style type="text/css">
	.a {font-family:굴림; color: #7744DD }
</style>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">

	function nextPage(){
		var form = document.getElementById("confirmForm");
		var user_name = form["user_name"].value;
		var user_jumin1 = form["user_jumin1"].value;
		var user_jumin2 = form["user_jumin2"].value;
		
		if(user_name == ""){
			alert('<util:message key="pt.ev.user.label.EmptyUserName"/>');
			form["user_name"].focus();
			return false;	 
		}
		else if(user_jumin1 == "" && user_jumin2 == ""){
			alert('<util:message key="pt.ev.user.label.EmptyUserRegNo"/>');
			form["user_jumin1"].focus();
			return false;
		}
		else if(user_jumin1.length !=6 || user_jumin2.length != 7){
			alert('<util:message key="pt.ev.user.label.InvalidRegNo"/>');
			return false;
		}
	}

	function autoTab(){
		var form = document.getElementById("confirmForm");
		var user_jumin1 = form["user_jumin1"].value;
		if(user_jumin1.length == 6){
			form["user_jumin2"].focus();
		}
	}

	function isNaNRegNo(obj){
		var form = document.getElementById("confirmForm");
		if(isNaN(form[obj].value)){
			alert('<util:message key="pt.ev.user.required.Number"/>');
			form[obj].value = "";
			form[obj].focus();
			return false;
		}
	}

	function initPage(){
		var errorMessage = "<c:out value="${errorMessage}"/>";
		if( errorMessage != "null" && errorMessage.length > 0 ) {
			alert( errorMessage );
		}
		var form = document.getElementById("confirmForm");
		form["user_name"].focus();
	}
	
</script>
</head>
<body onload="initPage()">
<center>
	<form id="confirmForm" name="confirmForm" method="post">
		<table id="confirmTable" bgcolor="#CCCCEE" border="1" width="800" height="300">
			<tr align="center">
				<th class="a">실명 인증</th>
			</tr>
			<tr align="center">
				<th class="a" >
				이름 <input type="text" name="user_name" maxlength="12" style="ime-mode:active"/>
				주민등록번호 <input type="text" name="user_jumin1" size="6" maxlength="6" onkeyup="isNaNRegNo(this.name), autoTab()"/>-<input type="password" name="user_jumin2" size="7" maxlength="7" onkeyup="isNaNRegNo(this.name)"/>
			</th>
			</tr>
			<tr align="center">
				<th class="a" colspan="2" align="left">
				<h5>
					개정 "주민등록법"에 의해 타인의 주민등록번호를 부정사용하는 자는 3년 이하의 징역 또는 1천만원. 이하의 벌금이 부과될 수 있습니다. 관련법률: 주민등록법 제37조(벌칙) 제10호(시행일 : 2009.04.01)
					만약, 타인의 주민번호를 도용하여 온라인 회원 가입을 하신 이용자분들은 지금 즉시 명의 도용을 중단하시길 바랍니다.
				</h5>
				</th>
			</tr>
			<tr>
				<th class="a" colspan="2">
					<input type="submit" name="_target3" value="다음" onclick="return nextPage()"/>
					<input type="submit" name="_cancel" value="취소"/>
				</th>
			</tr>
		</table>
		
	</form>
</center>
</body>
</html>