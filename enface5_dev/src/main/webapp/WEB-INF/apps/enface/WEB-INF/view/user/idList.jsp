<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>가입 여부 확인</title>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">
	function initPage(){
		
	}

	/* 전 단계로 가기 위한 변수 설정(provision, finish 페이지를 제외한 모든 페이지 공통 */
	function back(_state){
		_state.value="back";
	}
</script>
</head>
<body onload="initPage()">
<center>
	<form name="confirmForm" method="post"">
		<table id="confirmTable" border="0" width="400">
			<tr align="center">
				<th colspan="4" height="45">기존 아이디 확인</th>
			</tr>
			<c:if test="${empty userList}" var="result"/>
			<c:if test="${result == true}">
			<tr align="center">
				<th>
					가입하신 계정이 없습니다!<br>
					다음을 눌러 신규가입을 해주세요^.^
				</th>
			</tr>
			</c:if>
			<c:if test="${result == false}">
				<c:forEach items="${userList}" var="user">
					<tr align="left">
						<th>아이디</th><td><c:out value="${user.user_id}"/></td>
						<th>가입일자</th><td><c:out value="${user.regDatim}"/></td>
					</tr>
				</c:forEach>
				<input type="hidden" name="user_id" value="${user.user_id}"/>
			</c:if>
			<tr>
				<th colspan="4">
					<input type="hidden" name="_state" value="go"/>
					<c:if test="${result == true}">
					<input type="submit" name="_target4" value="확인"/>
					</c:if>
					<c:if test="${result == false}">
					<input type="submit" name="_target3" value="확인"/>
					</c:if>
					<input type="submit" name="_target0" value="약관 다시 확인" onclick="back(_state)"/>
					<input type="submit" name="_cancel" value="취소"/>
				</th>
			</tr>
		</table>
	</form>
</center>
</body>
</html>