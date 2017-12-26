<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form id="changeForm" method="post">
	<table id="satartTable">
		<tr>
			<th><c:out value="${user_id}"/>님 환영합니다!</th>
		</tr>
		<tr>
			<th>회원정보를 변경할 수 있습니다.</th>
		</tr>
	</table>
	<p></p>
	<input type="button" id="start" class="submit" value="회원정보변경"/>
</form>
<script type="text/javascript">
	var errorMessage = "<c:out value="${errorMessage}"/>";
	alertError('start');
	//공통으로 다음과 취소 버튼 이벤트 핸들러 등록
	$('.submit').click(nextPage);
</script>