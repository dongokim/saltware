<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form id="userForm"method="post">
	<table id="passwordConfirmTable">
		<tr>
			<th>가입 인증</th>
		</tr>
		<tr>
			<th>
				<div id="confirmForm">
				&nbsp;아이디:&nbsp;<c:out value="${firstJoinUser}"/>
				&nbsp;&nbsp;&nbsp;비밀번호&nbsp;<input type="password" id="password" class="confirmData" maxlength="16" size="17"/>
				&nbsp;<input type="button" id="confirmPassword" class="confirm" value="확인" disabled="disabled"/>
				</div>
				<div id="confirmResult"></div>
			</th>
		</tr>
		<tr>
			<th>
				<input type="button" id="passwordConfirm" class="submit" value="다음" disabled="disabled"/>
				<input type="button" id="passwordConfirmCancel" class="cancel" value="취소"/>
			</th>
		</tr>
	</table>
	<input type="hidden" id="isConfirm" value="0"/>
	<input type="hidden" id="firstJoinUser" value="<c:out value="${firstJoinUser}"/>"/>
	<input type="text" style="visibility: hidden;"/>
</form>
<script type="text/javascript">
	var errorMessage = "<c:out value="${errorMessage}"/>";
	alertError('passwordConfirm');
	
	$('.submit').click(nextPage);
	$('.cancel').click(prevPage);
	$('#confirmPassword').click(clickConfirm);
	
	$('#password').blur(passwordIsValid);
	$('#password').select();

	function isConfirm(html){
		var result = html.split(":");
		
		if(result[0] == "yes"){
			$('#confirmForm').hide();
			$('#passwordConfirm').removeAttr("disabled");
			$('#confirmResult').html(result[1])
			return true;
		}else if(result[0] == "no"){
			$('#passwordConfirm').attr("disabled", true);
			alert(result[1]);
			showConfirmForm();
			return false;
		}
	}

	function initConfirmParameters(){
		var data = '&firstJoinUser=' + $('#firstJoinUser').val() +  
				'&password=' + $('#password').val();
		return data; 
	}
	
</script>