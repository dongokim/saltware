<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원 정보 변경</title>
<style type="text/css">
	.a {font-family:굴림; color: #7744DD }
</style>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">

	/* submit이 될 때 데이터 확인 */
	function nextPage(){
		var form = document.getElementById("modifyForm");
		var user_hp1 = form["user_hp1"].value;
		var user_hp2 = form["user_hp2"].value;
		var user_hp3 = form["user_hp3"].value;
		
		var user_email1 = form["user_email1"].value;
		var user_email2 = form["user_email2"].value;
		
		/* 휴대폰 번호 누락 확인*/
		if(user_hp1 == "" || user_hp2 == "" || user_hp3 == ""){
			alert('휴대폰 번호를 입력하세요.');
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
			alert('휴대폰 번호에 숫자를 입력하세요.');
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
				alert('Email 아이디를 입력하세요.');
				form["user_email1"].focus();
			}
			else if(user_email2 ==""){
				alert('Email 도메인을 입력하세요.');
				form["user_email2"].focus();
			}
			return false;
		}
		
		else {
			return true;
		}
	}
	
	/* 초기화 함수: 사용자 정보를 받아서 폼에 뿌려줌. */
	function initPage(){
		var form = document.getElementById("modifyForm");
		var user_hp = "<c:out value="${user.user_hp}"/>".split("-");
		var user_email = "<c:out value="${user.user_email}"/>".split("@");

		form["user_hp1"].value = user_hp[0];
		form["user_hp2"].value = user_hp[1];
		form["user_hp3"].value = user_hp[2];
		form["user_email1"].value = user_email[0];
		form["user_email2"].value = user_email[1];
	}
	</script>
</head>
<body onload="initPage()">
	<center>
		<br><br>
		<form name="modifyForm" method="post">
			<table width="480" height="320" bordercolor="gray" border="1" bgcolor="#DDDDDD">
				<tr class="a" height="30">
					<th colspan="2" align="center" style="font-style: bold">회원 정보 입력</th>
				</tr>
				<tr class="a" align="left">
					<th style="font-style: bold">사용자 이름</th>
					<td>&nbsp;<c:out value="${user.user_name }"/></td>
				</tr>
				<tr class="a" align="left">
					<th style="font-style: bold">아이디</th>
					<td>&nbsp;<c:out value="${user.user_id}"/></td>
				</tr>
				<tr class="a" align="left">
					<th style="font-style: bold">휴대폰 번호*</th>
					<td>&nbsp;
						<select name="user_hp1">
							<c:forEach items="${portableNumList}" var="hp1">
								<option value="<c:out value="${hp1}"/>"><c:out value="${hp1}"/></option>
							</c:forEach>
						</select> - <input type="text" name="user_hp2" maxlength="4" size="6"/> - <input type="text" name="user_hp3" maxlength="4" size="6"/>
					</td>
				</tr>
				<tr class="a" align="left">
					<th style="font-style: bold">이메일*</th>
					<td>&nbsp;<input type="text" name="user_email1" maxlength="16" size="17"/> @ <input type="text" name="user_email2" maxlength="16" size="17"/></td>
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