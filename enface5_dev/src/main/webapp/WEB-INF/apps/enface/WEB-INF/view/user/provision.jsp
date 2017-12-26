<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<%
	java.util.Enumeration em = request.getParameterNames();
	        for( ; em.hasMoreElements(); ) {
	            String k = (String)em.nextElement();
	            String val = request.getParameter( k );
	            System.out.println( "provision.jsp Request : " + k + " = " + val);
	        }
%>

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
	function nextPage(){
		if(!document.joinForm.useProCheck.checked){
			alert('이용약관에 동의해주세요');
			return false;
		}
		else{
			document.joinForm.isAgree.value="agree";
			return true;
		}
	}

	function initPage(){
		
	}
	
</script>
</head>
<body onload="initPage()">
	<center>
		<br><br>
		<form name="joinForm" method="post">
			<textarea rows="7" cols="80" readonly="readonly"><c:out value="${useProvision}"/></textarea>
			<div><input type="hidden" name="isAgree" value="disAgree"></div>
			<input type="checkbox" id="useProCheck" name="useProCheck"/><label for="useProCheck">이용약관에 동의 합니다.</label>
			<div>
				<input type="submit" name="_target1" value="동의" onclick="return nextPage()"/>
				<input type="submit" name="_cancel" value="동의하지 않습니다."/>
			</div>
		</form>
	</center>
</body>
</html>