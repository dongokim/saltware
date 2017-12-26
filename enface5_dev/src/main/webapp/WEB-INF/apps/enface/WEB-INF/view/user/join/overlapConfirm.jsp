<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld"%>
<html>
<head>
<title>ID중복확인</title>
<style type="text/css">
.textStyle {
	font-size: 11;
	font-family: 굴림;
	color: #334433
}

.textStyle2 {
	font-size: 13;
	font-family: 굴림;
	color: #FF0000
}

.textStyle3 {
	font-size: 13;
	font-family: 굴림;
	color: #0000FF
}
</style>
<script language="JavaScript"
	src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">
	function assign_id()
	{
		var form = opener.document.getElementById("registryForm");
		form["user_id"].value = "<c:out value="${check_id}"/>";
		form["isOverlap"].value = 0;
		form["user_id"].readOnly = true;
		self.close();
	}
	
	function checkDupID()
	{
		var form = document.getElementById("checkForm");
	    if( form["user_id"].value == "")
	    {
	        alert('<util:message key="pt.ev.user.label.EmptyUserId"/>');
	        form["user_id"].focus();
	        return false;
	    }
	    else if(6 > form["user_id"].value.length || form["user_id"].value.length > 12){
			alert('<util:message key="pt.ev.user.label.InvalidUserId"/>');
			form["user_id"].value = "";
			form["user_id"].focus();
			return false;
		}
	    return true;
	}

	function onlyEng(){
		var form = document.getElementById("checkForm");
		var firstChar = form["user_id"].value.charAt(0);
		//첫번째 문자가 영어 소문자가 아닌경우와 두번째 문자부터 영어 소문자가 아니거나 숫자가 아닌 경우
		for(var i = 0 ; i < form["user_id"].value.length ; i++){
			var charValue = form["user_id"].value.charAt(i);	
			if(i == 0){
				if( ('a' > firstChar || firstChar > 'z') && 
						!isNaN(form["user_id"].value.charCodeAt(0))){
					alert('<util:message key="pt.ev.user.required.Character"/>');
					form["user_id"].value = "";
					form["user_id"].focus();
					return false;
				}
			}
			else if( i > 0 ) {
				if( ('a' > charValue || charValue > 'z') &&
					('0' > charValue || charValue > '9') &&
					'_' != charValue	
					){
				alert('<util:message key="pt.ev.user.required.English"/>');
				form["user_id"].value = "";
				form["user_id"].focus();
				return false;
				}
			}
		}
		return true;
	}

	function initPage(){
		var errorMessage = "<c:out value="${errorMessage}"/>";
		if( errorMessage != "null" && errorMessage.length > 0 ) {
			alert( errorMessage );
		}
		
		var form = document.getElementById("checkForm");
		form["user_id"].value = "";
		form["user_id"].focus();
	}

</script>
</head>
	<body onload="initPage()">
		<c:if test="${isOverlap}" var="result" >
			<label class="textStyle2"><c:out value="${check_id}" /></label>
			<label class="textStyle">는 탈퇴한 아이디 이거나, 이미 사용중인<br>아이디 입니다.</label>
		</c:if>
		<center>
			<c:if test="${result == false}">
				<table bordercolor="gray" bgcolor="#CCCCEE" border="0" width="320">
					<tr>
						<th>
						<label class="textStyle3"><c:out value="${check_id}" /></label>
						<label class="textStyle">는 사용할  수 있는 아이디 입니다.<br></label>
						</th>
					</tr>
					<tr>
						<th><input type="button" value="사용하기" onclick="assign_id()"></th>
					</tr>
				</table> 	
			</c:if>	
			<form method="post" name="checkForm" onsubmit="return checkDupID()">
				<table bordercolor="gray" bgcolor="#CCCCEE" border="0" width="320">
					<tr align="left">
						<th class="textStyle">&nbsp;다른 아이디</th>
						<td><input name="user_id" size="17" maxlength="16" type="text" onkeyup="onlyEng()"></td>
					</tr>
				</table>
				<input type="submit" name="confirm" value="중복확인"/>
				<input type="button" name="cancel" value="취소" onclick="window.close()"/>
			</form>
		</center>
	</body>
</html>