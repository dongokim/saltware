<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>약관동의</title>
<style type="text/css">
	.a {font-family:굴림; color: #FF77AA }
	.b {font-family:굴림; color: #FF00AA; font-size:18px }
	.c {font-family:굴림; color: blue; font-size:16px }
</style>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">
	function goRegNoConfirmPage(){
		var form = document.getElementById("provisionForm");
		if(form["isAgree"].checked == true){
			return true;
		}
		else{
			alert('<util:message key="pt.ev.user.required.DoNotAgree"/>');
			return false;
		}
	}

	function initPage(){
		
	}
	
</script>
</head>
<body onload="initPage()">
	<center>
		<br><br>
		<form id="provisionForm" name="provisionForm" method="post">
		<table>
		<tr align="center">
			<td><textarea rows="7" cols="80" readonly="readonly"><c:out value="${useProvision}"/></textarea></td>
		</tr>
		<tr align="left">
			<td><input type="checkbox" id="isAgree" name="isAgree"/><label for="isAgree">이용약관에 동의 합니다.</label></td>
		</tr>
		</table>
			<div>
				<input type="submit" name="_target2" value="다음" onclick="return goRegNoConfirmPage()"/>
				<input type="submit" name="_cancel" value="취소"/>
			</div>
		
		</form>
	</center>
</body>
</html>