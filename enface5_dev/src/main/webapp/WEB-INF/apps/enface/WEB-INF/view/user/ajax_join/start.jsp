<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form id="userForm" method="post">
	<table id="startTable">
		<tr>
			<td>
			<h4>
			<c:if test = "${joinLimit > 0}">
				같은 주민등록번호로 총 <font color="blue"><c:out value="${joinLimit}"/></font>개 까지 계정을 생성하실 수 있습니다.<br><br>
			</c:if>
			회원가입 버튼을 누르시고 가입 절차를 밟으세요.
			</h4>
			</td>
		</tr>
	</table>
	<p></p>
	<input type="button" id="start" class="submit" value="회원가입"/>
	<input type="button" id="login"  class="cancel" value="로그인"/>
</form>
<script type="text/javascript">
	var errorMessage = "<c:out value="${errorMessage}"/>";
	alertError('start');
	//공통으로 다음과 취소 버튼 이벤트 핸들러 등록
	var loginUrl = "<c:out value="${loginUrl}"/>";
	
	$('.submit').click(nextPage);
	$('.cancel').click(goLoginPage);
</script>