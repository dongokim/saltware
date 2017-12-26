<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form id="userForm" method="post">
	<table id="completeTable">
		<tr>
			<th><font color="blue"><c:out value="${user.user_name}"/></font>님, 가입을 축하합니다.</th>
		</tr>
		<tr>
			<th><input type="button" id="login" class="cancel" value="로그인"/></th>
		</tr>	
	</table>
</form>
<script type="text/javascript">
	var errorMessage = "<c:out value="${errorMessage}"/>";
	alertError('complete');
	$('#login').click(goLoginPage);
</script>