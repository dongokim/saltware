<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>가입 여부 확인</title>
<style>
	.a {font-family:굴림; color: #7744DD; font-size: 13}
</style>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">
	function initPage(){
	}

</script>
</head>
<body onload="initPage()">
<center>
	<form id="confirmForm" name="confirmForm" method="post"">
		<table id="confirmTable"  bgcolor="#CCCCEE" border="0" width="450">
			<tr class="a" align="center">
				<th colspan="4" height="45">가입 여부 및 가입된 아이디 확인</th>
			</tr>
			<c:if test="${empty userList}" var="isEmptyUserList"/>
			<c:if test="${isEmptyUserList == true}">
			<tr class="a" align="left">
				<th>
					<h5>
						가입하신 계정이 없습니다. 다음을 눌러 신규가입을 해주세요.
					</h5>
				</th>
			</tr>
			</c:if>
			<c:if test="${isEmptyUserList == false}">
			<c:forEach items="${userList}" var="user">
			<tr  class="a" align="left">
				<th>아이디</th><td><c:out value="${user.user_id}"/></td>
				<th>가입일자</th><td><c:out value="${user.regDatimF}"/></td>
			</tr>
			</c:forEach>
			</c:if>
			<tr class="a" align="center">
				<th colspan="4">
					<c:if test="${userListSize >= joinLimit and 0 < joinLimit }" var="isOverJoinLimit">
						<h5>같은 주민등록번호로 <font color="red"><%=request.getAttribute("joinLimit")%></font>개 이상 가입 하실 수 없습니다.</h5>
					</c:if>
					<c:if test="${isOverJoinLimit == false}">
						<c:if test="${isEmptyUserList == true}">
						<input type="submit" name="_target5" value="다음"/>
						</c:if>
						<c:if test="${isEmptyUserList == false}">
						<input type="submit" name="_target4" value="다음"/>
						</c:if>
					</c:if>
					<input type="submit" name="_cancel" value="취소"/>
				</th>
			</tr>
		</table>
	</form>
</center>
</body>
</html>