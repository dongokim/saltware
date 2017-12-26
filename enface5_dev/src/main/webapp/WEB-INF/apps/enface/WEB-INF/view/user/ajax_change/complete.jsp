<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form id="changeForm" method="post">
	<table class="completeTable">
		<tr>
			<th><font color="blue">회원정보 변경이 완료되었습니다.</font></th>
		</tr>
		<tr>
			<th><input type="button" id="change" class="cancel" value="메인으로"/></th>
		</tr>	
	</table>
</form>
<script>
	var errorMessage = "<c:out value="${errorMessage}"/>";
	alertError('complete');
	
	$('#change').click(goChangeInfoPage);
</script>