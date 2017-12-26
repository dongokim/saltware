<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form id="userForm" method="post">
	<table id="idListTable">
		<tr>
			<th colspan="4">가입 여부 및 가입된 아이디 확인</th>
		</tr>
		<c:if test="${empty userList}" var="isEmptyUserList"/>
		<c:if test="${isEmptyUserList == true}">
		<tr>
			<th colspan="4">
				<h5>
					가입하신 계정이 없습니다. 다음을 눌러 신규가입을 해주세요.
				</h5>
			</th>
		</tr>
		</c:if>
		<c:if test="${isEmptyUserList == false}">
		<c:forEach items="${userList}" var="user">
		<tr>
			<th>아이디</th><td align="center"><c:out value="${user.user_id}"/></td>
			<th>가입일자</th><td align="center"><c:out value="${user.regDatimF}"/></td>
		</tr>
		</c:forEach>
		</c:if>
		<tr>
			<th colspan="4">
				<c:if test="${userListSize >= joinLimit and 0 < joinLimit }" var="isOverJoinLimit">
					<h5>같은 주민등록번호로 <font color="red"><%=request.getAttribute("joinLimit")%></font>개 이상 가입 하실 수 없습니다.</h5>
				</c:if>
				<c:if test="${isOverJoinLimit == false}">
					<c:if test="${isEmptyUserList == true}">
					<input type="button" id="passwordConfirm" class="submit" value="다음"/>
					</c:if>
					<c:if test="${isEmptyUserList == false}">
					<input type="button" id="idList" class="submit" value="다음"/>
					</c:if>
				</c:if>
				<input type="button" id="idListCancel" class="cancel" value="취소"/>
			</th>
		</tr>
	</table>
</form>
<script type="text/javascript">
	var errorMessage = "<c:out value="${errorMessage}"/>";
	alertError('idList');
	
	$('.submit').click(nextPage);
	$('.cancel').click(prevPage);
	
	function initParameters(){
		var data = '&user_name=' + $('#user_name').val() +
		  	'&user_jumin1=' + $('#user_jumin1').val() +
		  	'&user_jumin2=' + $('#user_jumin2').val();
		return data; 
	}
</script>