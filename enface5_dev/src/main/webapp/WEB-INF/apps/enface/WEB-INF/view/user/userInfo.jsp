<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원 정보 입력</title>
<style type="text/css">
	.a {font-family:굴림; color: #7744DD }
</style>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">

	/* submit이 될 때 데이터 확인 */
	function nextPage(){
		
		/* 데이터 누락 여부 확인을 위한 변수 선언*/
		var user_id = document.registryForm.user_id;
		var isOverlap = document.registryForm.isOverlap;

		var password = document.registryForm.password;
		var passwordConfirm = document.registryForm.passwordConfirm;
		
		var user_hp1 = document.registryForm.user_hp1;
		var user_hp2 = document.registryForm.user_hp2;
		var user_hp3 = document.registryForm.user_hp3;
		
		var user_email1 = document.registryForm.user_email1;
		var user_email2 = document.registryForm.user_email2;
		
		/* 아이디 확인 */
		if(user_id.value==""){
			alert('아이디를 입력해주세요');
			user_id.focus();
			return false;
		}
		else if(isOverlap.value == 1){
			alert('아이디 중복체크를 해주세요.');
			return false;
		}
		/* 비밀번호 확인 */
		else if(password.value == ""){
			alert('비밀번호를 입력해주세요.');
			password.focus();
			return false;
		}
		else if(passwordConfirm.value ==""){
			alert('비밀번호를 확인해주세요.');
			passwordConfirm.focus();
			return false;
		}
		else if(password.value != passwordConfirm.value){
			alert('비밀번호가 일치하지 않습니다.\n다시  확인하세요.');
			password.value="";
			passwordConfirm.value="";
			password.focus();
			return false;			
		}
		
		/* 휴대폰 번호 누락 확인*/
		else if(user_hp1.value =="" || user_hp2.value =="" || user_hp3.value==""){
			alert('휴대폰 번호를 입력하세요.');
			if(user_hp1.value ==""){
				user_hp1.focus();
			}
			else if(user_hp2.value ==""){
				user_hp2.focus();
			}
			else if(user_hp3.value ==""){
				user_hp3.focus();
			}
			return false;
		}
		
		/* 휴대폰 번호숫자여부 확인*/
		else if(isNaN(user_hp2.value) || isNaN(user_hp3.value)){
			alert('휴대폰 번호에 숫자를 입력하세요.');
			if(isNaN(user_hp2.value)){
				user_hp2.value = "";
				user_hp2.focus();
			}
			else if(isNaN(user_hp3.value)){
				user_hp3.value = "";
				user_hp3.focus();
			}
			return false;
		}
		
		/* 이메일 주소 입력 확인*/
		else if(user_email1.value=="" || user_email2.value==""){
			if(user_email1.value ==""){
				alert('Email 아이디를 입력하세요.');
				user_email1.focus();
			}
			else if(user_email2.value ==""){
				alert('Email 도메인을 입력하세요.');
				user_email2.focus();
			}
			return false;
		}
		
		else {
			return true;
		}
	}
	
	/* 아이디 중복체크 */
	function overlapCheck(){
		if(document.registryForm.user_id.value==""){
			alert('아이디를 입력해주세요');
			document.registryForm.user_id.focus();
			return false;
		}
		else{
			document.registryForm.isOverlap.value = 1;
			open("overlap.face?user_id=" + document.registryForm.user_id.value, "", "width=600, height=200, toolbar=no, menubar=no, resizable=no, status=no, scrollbars=no");
		}
	}
	
	/* 전 단계로 가기 위한 변수 설정(provision, finish 페이지를 제외한 모든 페이지 공통 */
	function back(_state){
		_state.value="back";
	}
	
	function initPage(){
			
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
					<th colspan="2" align="center" style="font-style: bold">회원 정보 입력</th>
				</tr>
				<tr class="a" align="left">
					<th style="font-style: bold">사용자 이름 *</th>
					<td>&nbsp;<c:out value="${user.user_name }"/></td>
				</tr>
				<tr class="a" align="left">
					<th style="font-style: bold">희망 아이디 *</th>
					<td>&nbsp;<input type="text" name="user_id"/>&nbsp;<input type="button" value="중복확인*" onclick="return overlapCheck()"/></td>
				</tr>
				<tr class="a" align="left">
					<th style="font-style: bold">비밀번호 *</th>
					<td>&nbsp;<input type="password" name="password"/></td>
				</tr>
				<tr class="a" align="left">
					<th style="font-style: bold">비밀번호 확인 *</th>
					<td>&nbsp;<input type="password" name="passwordConfirm"/></td>
				</tr>
				<tr class="a" align="left">
					<th style="font-style: bold">휴대폰 번호*</th>
					<td>&nbsp;
						<select name="user_hp1">
							<c:forEach items="${portableNumList}" var="user_hp1">
								<option value="<c:out value="${user_hp1}"/>"><c:out value="${user_hp1}"/></option>
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
			<input type="hidden" name="_state" value="go"/>
			<input type="submit" name="_target5" value="다음 단계로" onclick="return nextPage()"/>
			<input type="submit" name="_target0" value="약관 다시 확인" onclick="back(_state)"/>
			<input type="submit" name="_cancel" value="취소"/>
		</form>
	</center>
</body>
</html>