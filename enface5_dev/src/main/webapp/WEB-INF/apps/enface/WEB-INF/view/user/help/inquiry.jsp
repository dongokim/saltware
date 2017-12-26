<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>아이디/비밀번호 찾기</title>
<style type="text/css">
	.commonStyle {text-align: left; font-family:굴림; color: #7744DD; font-style: bold}
</style>
<c:if test="${windowId == null}" var="result">
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/message/messageResource_<c:out value="${langKnd}"/>.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script> 
</c:if>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">
	function goLoginPage(){
		location.href="../user/login.face";
	}


	function goSearchResultPage() {
		var form = document.getElementById("inQuiryForm");
		var searchItem = form["searchItem"].value;
		var user_id = form["user_id"].value;
		var user_jumin1 = form["user_jumin1"].value;
		var user_jumin2 = form["user_jumin2"].value;
		var user_name = form["user_name"].value;

		if(searchItem == "password"){
			if(user_id == ""){
				alert('<util:message key="pt.ev.user.label.EmptyUserId"/>');
				form["user_id"].focus();
				return false;
			}
		}
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
		return true;
	}

	function autoTab(){
		var form = document.getElementById("inQuiryForm");
		var user_jumin1 = form["user_jumin1"].value;
		if(user_jumin1.length == 6){
			form["user_jumin2"].focus();
		}
	}

	function isNaNRegNo(obj){
		var form = document.getElementById("inQuiryForm");
		if(isNaN(form[obj].value)){
			alert('<util:message key="pt.ev.user.required.Number"/>');
			form[obj].value = "";
			form[obj].focus();
			return false;
		}
	}

	function getSelectedRadioButtonItem(value){
		var form = document.getElementById("inQuiryForm");
		form["searchItem"].value = value;
		if(value == "id"){
			form["user_id"].disabled = true;
			form["user_id"].style.backgroundColor = "#DDDDDD";
			form["user_name"].focus();
		}
		else if(value == "password"){
			form["user_id"].disabled = false;
			form["user_id"].style.backgroundColor = "#FFFFFF";
			form["user_id"].focus();
		}
	}

	function initPage(){
		var errorMessage = "<c:out value="${errorMessage}"/>";
		if( errorMessage != "null" && errorMessage.length > 0 ) {
			alert( errorMessage );
		}
		
		getSelectedRadioButtonItem("id");
	}

</script>
</head>
<body onload="initPage()">
	<center>
		<form id="inQuiryForm" method="post">
			<input type="hidden" name="searchItem"/>
			<table bordercolor="gray" width="300" border="0" bgcolor="#DDDDDD">
				<tr class="commonStyle" >
					<th align="center" colspan="2">
						<input type="radio" id="inquiry" name="inquiry" value="id" onclick="getSelectedRadioButtonItem(this.value)" checked="checked"><label for="inquiry">아이디 찾기</label>
						<input type="radio" id="inquiry2" name="inquiry" value="password" onclick="getSelectedRadioButtonItem(this.value)"><label for="inquiry2">비밀번호 재발급</label>
					</th>
				</tr>
				<tr>
					<th class="commonStyle">&nbsp;아이디</th>
					<td class="commonStyle">&nbsp;<input type="text" name="user_id" maxlength="16" size="22" disabled="disabled"/></td>
				</tr>
				<tr>
					<th class="commonStyle">&nbsp;이름</th>
					<td class="commonStyle">&nbsp;<input type="text" name="user_name" maxlength="16" size="22"/></td>
				</tr>
				<tr>
					<th class="commonStyle">
						&nbsp;주민등록번호
					</th>
					<td class="commonStyle">&nbsp;<input type="text" name="user_jumin1" maxlength="6" size="7" onkeyup="isNaNRegNo(this.name), autoTab()"/> - <input type="password" name="user_jumin2" maxlength="7" size="8" onkeyup="isNaNRegNo(this.name)"/></td>
				</tr>
			</table>
			<input type="submit" value="확인" onclick="return goSearchResultPage()"/>
			<input type="reset" value="다시쓰기" onclick="initPage()"/>
			<input type="button" value="로그인" onclick="return goLoginPage()"/>
		</form>
	</center>
</body>
</html>