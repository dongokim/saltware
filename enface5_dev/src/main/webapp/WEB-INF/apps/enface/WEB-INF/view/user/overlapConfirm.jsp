<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>ID중복확인</title>
<script language="JavaScript" src="<%=request.getContextPath()%>/face/javascript/main.js"></script>
<script language="JavaScript">
function assign_id()
{
	opener.document.registryForm.user_id.value = document.checkForm.chkId.value;
	opener.document.registryForm.isOverlap.value = 0;
	opener.document.registryForm.user_id.readOnly = true;
	self.close();
}

function checkDupID()
{
    if( document.checkForm.user_id2.value == "")
    {
        alert("아이디를 입력하세요.");
        document.checkForm.user_id2.focus();
        return false;
    }
    return true;
}

</script>
</head>
<body>
<center>
	<form method="post" name="checkForm" onsubmit="return checkDupID()">
		<input type="hidden" name="chkId" value="<c:out value="${user_id}"/>"/>
		<table border="1">
			<tr>
				<c:if test="${isOverlap}" var="result">
				<th colspan="2"><strong><c:out value="${user_id}"/></strong>는 이미 사용 중이거나<br>
							탈퇴한 아이디 입니다.</th>
				</c:if>
				<c:if test="${result == false}">
				<th colspan="2"><strong><c:out value="${user_id}"/></strong>는 사용할 수 있는 아이디 입니다.<br>
				<input type="button" value="사용하기" onclick="assign_id()"></th>
				</c:if>
			</tr>
			<tr>
				<th colspan="2">다른 아이디를 사용하려면!</th>
			</tr>
			<tr>
				<th colspan="2">아래 다른 아이디를 입력하고 중복확인을 클릭하세요.</th>
			</tr>
			<tr>
				<th>다른 아이디 입력</th>
				<td><input name="user_id2" maxlength="12" type="text"></td>
			</tr>
			<tr>
				<th colspan="2"><input type="submit" name="confirm" value="중복확인" onclick="return checkDupID()" /></th>		
			</tr>
		</table>
	</form>
</center>
</body>
</html>