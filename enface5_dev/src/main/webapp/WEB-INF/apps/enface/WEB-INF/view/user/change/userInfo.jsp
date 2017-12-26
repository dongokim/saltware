<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원 정보 변경</title>
<style type="text/css">
	.a {font-family:굴림; color: #7744DD }
</style>
<c:if test="${windowId == null}" var="result">
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/message/messageResource_<c:out value="${langKnd}"/>.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script> 
</c:if>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">

	/* submit이 될 때 데이터 확인 */
	function nextPage(){
		var form = document.getElementById("modifyForm");
		var user_hp1 = form["user_hp1"].value;
		var user_hp2 = form["user_hp2"].value;
		var user_hp3 = form["user_hp3"].value;
		
		var homeZip1 = form["homeZip1"].value;
		var homeZip2 = form["homeZip2"].value;
		var homeAddr1 = form["homeAddr1"].value;
		var homeAddr2 = form["homeAddr2"].value;
		
		var user_email1 = form["user_email1"].value;
		var user_email2 = form["user_email2"].value;
		
		/* 주소 누락 확인 */
		if(homeZip1 =="" || homeZip2==""){
			alert('<util:message key="pt.ev.user.label.EmptyHomeZipCode"/>');
			if(homeZip1 == ""){
				form["searchZipButton"].focus();
			}
			else if(homeZip2 ==""){
				form["searchZipButton"].focus();
			}
			return false;
		}
		else if(isNaN(homeZip1) || isNaN(homeZip2)){
			alert('<util:message key="pt.ev.user.required.Number"/>');
			if(isNaN(homeZip1)){
				form["homeZip1"].value = "";
				form["homeZip1"].focus();
			}
			else if(isNaN(homeZip2)){
				form["homeZip2"].value = "";
				form["homeZip2"].focus();
			}
			return false;
		}
		
		else if(homeAddr1 == ""){
			alert('<util:message key="pt.ev.user.label.EmptyHomeAddress"/>');
			form["homeAddr1"].focus();
			return false;	 
		}
		
		else if(homeAddr2 == ""){
			alert('<util:message key="pt.ev.user.label.EmptyHomeAddress2"/>');
			form["homeAddr2"].focus();
			return false;	 
		}
		
		/* 휴대폰 번호 누락 확인*/
		if(user_hp1 == "" || user_hp2 == "" || user_hp3 == ""){
			alert('<util:message key="pt.ev.user.label.EmptyUserHp"/>');
			if(user_hp1 == ""){
				form["user_hp1"].focus();
			}
			else if(user_hp2 == ""){
				form["user_hp2"].focus();
			}
			else if(user_hp3 == ""){
				form["user_hp3"].focus();
			}
			return false;
		}
		
		/* 휴대폰 번호숫자여부 확인*/
		else if(isNaN(user_hp2) || isNaN(user_hp3)){
			alert('<util:message key="pt.ev.user.required.Number"/>');
			if(isNaN(user_hp2.value)){
				form["user_hp2"].value = "";
				form["user_hp2"].focus();
			}
			else if(isNaN(user_hp3.value)){
				form["user_hp3"].value = "";
				form["user_hp3"].focus();
			}
			return false;
		}
		
		/* 이메일 주소 입력 확인*/
		else if(user_email1=="" || user_email2==""){
			if(user_email1 ==""){
				alert('<util:message key="pt.ev.user.label.EmptyEmailId"/>');
				form["user_email1"].focus();
			}
			else if(user_email2 ==""){
				alert('<util:message key="pt.ev.user.label.EmptyEmailDomain"/>');
				form["user_email2"].focus();
			}
			return false;
		}
		else {
			form["homeZip"].value = form["homeZip1"].value + form["homeZip2"].value;
			return true;
		}
	}

	function onlyEng(fieldName){
		var form = document.getElementById("modifyForm");
		var firstChar = form[fieldName].value.charAt(0);
		//첫번째 문자가 영어 소문자가 아닌경우와 두번째 문자부터 영어 소문자가 아니거나 숫자가 아닌 경우
		for(var i = 0 ; i < form[fieldName].value.length ; i++){
			var charValue = form[fieldName].value.charAt(i);	
			if(i == 0){
				if( ('a' > firstChar || firstChar > 'z') && 
						!isNaN(form[fieldName].value.charCodeAt(0))){
					alert('<util:message key="pt.ev.user.required.Character"/>');
					form[fieldName].value = "";
					form[fieldName].focus();
					return false;
				}
			}
			else if( i > 0 ) {
				if( ('a' > charValue || charValue > 'z') &&
					('0' > charValue || charValue > '9') &&
					'_' != charValue	
					){
				alert('<util:message key="pt.ev.user.required.English"/>');
				form[fieldName].value = "";
				form[fieldName].focus();
				return false;
				}
			}
		}
		return true;
	}


	function onlyEngWithDot(fieldName){
		var form = document.getElementById("modifyForm");
		var firstChar = form[fieldName].value.charAt(0);
		//첫번째 문자가 영어 소문자가 아닌경우와 두번째 문자부터 영어 소문자가 아니거나 숫자가 아닌 경우
		for(var i = 0 ; i < form[fieldName].value.length ; i++){
			var charValue = form[fieldName].value.charAt(i);	
			if(i == 0){
				if( ('a' > firstChar || firstChar > 'z') && 
						!isNaN(form[fieldName].value.charCodeAt(0))){
					alert('<util:message key="pt.ev.user.required.Character"/>');
					form[fieldName].value = "";
					form[fieldName].focus();
					return false;
				}
			}
			else if( i > 0 ) {
				if( ('a' > charValue || charValue > 'z') &&
					('0' > charValue || charValue > '9') &&
					'_' != charValue &&
					'.' != charValue	
					){
				alert('<util:message key="pt.ev.user.required.English"/>');
				form[fieldName].value = "";
				form[fieldName].focus();
				return false;
				}
			}
		}
		return true;
	}

	function isNaNValue(fieldName){
		var form = document.getElementById("modifyForm");
		if(isNaN(form[fieldName].value)){
			alert('<util:message key="pt.ev.user.required.Number"/>');
			form[fieldName].value = "";
			form[fieldName].focus();
			return false;
		}
	}

	function autoTab(){
		var form = document.getElementById("modifyForm");
		var user_hp2 = form["user_hp2"].value;
		if(user_hp2.length == 4){
			form["user_hp3"].focus();
		}
	}
	
	function searchZip(){
		open("<%=request.getContextPath()%>/tool/searchZip.face?formName=modifyForm", "", "location=no, width=465, height=400, toolbar=no, menubar=no, resizable=no, status=no, scrollbars=no");
	}
	
	/* 초기화 함수: 사용자 정보를 받아서 폼에 뿌려줌. */
	function initPage(){
		var errorMessage = "<c:out value="${errorMessage}"/>";
		if( errorMessage != "null" && errorMessage.length > 0 ) {
			alert( errorMessage );
		}
		
		var form = document.getElementById("modifyForm");
		var user_hp = "<c:out value="${user.user_hp}"/>".split("-");
		var user_email = "<c:out value="${user.user_email}"/>".split("@");

		form["user_hp1"].value = user_hp[0];
		form["user_hp2"].value = user_hp[1];
		form["user_hp3"].value = user_hp[2];
		form["user_email1"].value = user_email[0];
		form["user_email2"].value = user_email[1];
		form["homeZip1"].value = "<c:out value="${user.homeZip1}"/>";
		form["homeZip2"].value = "<c:out value="${user.homeZip2}"/>";
		form["homeAddr1"].value = "<c:out value="${user.homeAddr1}"/>";
		form["homeAddr2"].value = "<c:out value="${user.homeAddr2}"/>";
	}
	</script>
</head>
<body onload="initPage()">
	<center>
		<br><br>
		<form id="modifyForm" name="modifyForm" method="post">
			<table width="740" bordercolor="gray" border="1" bgcolor="#DDDDDD">
				<tr height="40" class="a" height="30">
					<th colspan="2" align="center" style="font-style: bold">회원 정보 입력</th>
				</tr>
				<tr height="40" class="a" align="left">
					<th style="font-style: bold">&nbsp;사용자 이름</th>
					<td width="580">&nbsp;<c:out value="${user.user_name }"/></td>
				</tr>
				<tr height="40" class="a" align="left">
					<th style="font-style: bold">&nbsp;아이디</th>
					<td>&nbsp;<c:out value="${user.user_id}"/></td>
				</tr>
				<tr height="80"  class="a" align="left">
					<th style="font-style: bold">&nbsp;주         소 *</th>
					<td>
						<input type="hidden" name="homeZip"/>
						&nbsp;<input type="text" name="homeZip1" readonly="readonly" maxlength="3" size="3"/>
						-
						<input type="text" name="homeZip2" readonly="readonly" maxlength="3" size="3"/> 
						<input type="button" name="searchZipButton" value="우편번호 찾기 >" onclick="searchZip()">
						<br><br>
						&nbsp;<input type="text" size=40 name="homeAddr1" readonly="readonly"/>
						&nbsp;<input type="text" size=40 name="homeAddr2"/>
					</td>
				</tr>
				<tr height="40" class="a" align="left">
					<th style="font-style: bold">&nbsp;휴대폰 번호*</th>
					<td>&nbsp;
						<select name="user_hp1">
							<c:forEach items="${hpList}" var="user_hp1">
								<option value="<c:out value="${user_hp1.codeName}"/>"><c:out value="${user_hp1.codeName}"/></option>
							</c:forEach>
						</select> - <input type="text" name="user_hp2" maxlength="4" size="6" onkeyup="return isNaNValue(this.name), autoTab()"/> - <input type="text" name="user_hp3" maxlength="4" size="6" onkeyup="return isNaNValue(this.name)"/>
					</td>
				</tr>
				<tr height="40" class="a" align="left">
					<th style="font-style: bold">&nbsp;이메일*</th>
					<td>&nbsp;<input type="text" name="user_email1" maxlength="16" size="17" style="ime-mode:inactive" onkeyup="onlyEng(this.name)"/> @ <input type="text" name="user_email2" maxlength="16" size="17" style="ime-mode:inactive" onkeyup="onlyEngWithDot(this.name)"/></td>
				</tr>
			</table>
			<p></p>
			<input type="submit" name="_target3" value="변경" onclick="return nextPage()"/>
			<input type="reset" name="reset" value="다시입력"/>
			<input type="submit" name="_cancel" value="취소"/>
		</form>
	</center>
</body>
</html>