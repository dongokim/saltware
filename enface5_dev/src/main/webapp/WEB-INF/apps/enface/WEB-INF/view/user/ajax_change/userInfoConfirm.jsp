<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form id="changeForm" method="post">
	<table class="userInfoConfirmTable">
		<tr>
			<th colspan="2" align="center">변경 정보 확인</th>
		</tr>
		<tr align="left">
			<th>&nbsp;사용자 이름</th>
			<td>&nbsp;<c:out value="${user.user_name }"/></td>
		</tr>
		<tr align="left">
			<th>&nbsp;희망 아이디</th>
			<td>&nbsp;<c:out value="${user.user_id }"/></td>
		</tr>
		<tr align="left">
			<th>&nbsp;주         소</th>
			<td>
				&nbsp;우편번호&nbsp;<c:out value="${user.homeZip1}"/>-<c:out value="${user.homeZip2}"/>
				<br>
				&nbsp;<c:out value="${user.homeAddr1}"/>&nbsp;<c:out value="${user.homeAddr2}"/>
			</td>
		</tr>
		<tr align="left">
			<th>&nbsp;휴대폰 번호</th>
			<td>&nbsp;<c:out value="${user.user_hp }"/></td>
		</tr>
		<tr align="left">
			<th>&nbsp;이메일</th>
			<td>&nbsp;<c:out value="${user.user_email }"/></td>
		</tr>
	</table>
	<p></p>
	<input type="button" id="userInfoConfirm" class="submit" value="다음"/>
	<input type="button" id="userInfoConfirmCancel" class="cancel" value="정보 변경"/>
</form>
<script type="text/javascript">
	var errorMessage = "<c:out value="${errorMessage}"/>";
	alertError('userInfoConfirm');
	
	$('.submit').click(nextPage);
	$('.cancel').click(prevPage);
</script>