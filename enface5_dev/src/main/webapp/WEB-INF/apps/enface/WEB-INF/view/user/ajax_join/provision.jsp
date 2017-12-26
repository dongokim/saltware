<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form id="userForm" method="post">
	<table id="provisionTable">
		<tr>
			<td><textarea id="provisionText" rows="7" cols="90" readonly="readonly"></textarea></td>
		</tr>
		<tr align="left">
			<td><input type="checkbox" id="isAgree" class="checkbox" name="isAgree"/><label for="isAgree">이용약관에 동의 합니다.</label></td>
		</tr>
	</table>
	<p></p>
	<input type="button" id="provision" class="submit" value="다음" disabled="disabled"/>
	<input type="button" id="provisionCancel" class="cancel" value="취소"/>
</form>
<script type="text/javascript">
	var errorMessage = "<c:out value="${errorMessage}"/>";
	alertError('provision');
	
	//공통으로 다음과 취소 버튼 이벤트 핸들러 등록
	$('.submit').click(nextPage);
	$('.cancel').click(prevPage);
	
	$('#isAgree').click(isChecked);

	//기존 properties에서 가져오던 약관을 jsp파일로 가져와서 textarea에 innerHTML로 삽입함
	$.ajax({
		type: 'POST',
		url: contextPath + '/user/ajaxJoin.face', 
		data:'m=provisionText',
		dataType: 'html',
		success: function(html, textStatus){
			$('#provisionText').html(html);
		},
		error: function(xhr, textStatus, errorThrown){
			alert('약관불러오기에 문제가 생겼습니다.');
		}
	});
</script>