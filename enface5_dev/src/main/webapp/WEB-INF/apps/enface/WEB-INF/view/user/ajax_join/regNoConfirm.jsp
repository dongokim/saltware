<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form id="userForm" method="post">
	<table id="regNoTable">
		<tr>
			<th>실명 인증</th>
		</tr>
		<tr>
			<th>
				<label id="confirmForm">
					이름
					<input type="text" id="user_name" class="confirmData" maxlength="12" style="ime-mode:active"/>
					&nbsp;주민등록번호 
					<input type="text" id="user_jumin1" class="confirmData" size="6" maxlength="6"/>
					-
					<input type="password"  id="user_jumin2" class="confirmData" size="7" maxlength="7"/>
					&nbsp;<input type="button" id="confirmRegNo" class="confirm" value="확인"/>
				</label>
				<label id="confirmResult" style="display: none"></label>
			</th>
		</tr>
		<tr>
			<th>
			<h5>
				개정 "주민등록법"에 의해 타인의 주민등록번호를 부정사용하는 자는 3년 이하의 징역 또는 1천만원. 이하의 벌금이 부과될 수 있습니다. 관련법률: 주민등록법 제37조(벌칙) 제10호(시행일 : 2009.04.01)
				만약, 타인의 주민번호를 도용하여 온라인 회원 가입을 하신 이용자분들은 지금 즉시 명의 도용을 중단하시길 바랍니다.
			</h5>
			</th>
		</tr>
	</table>
	<p></p>
	<input type="hidden" id="isConfirm" value="0"/>
	<input type="button" id="regNoConfirm" class="submit" value="다음"/>
	<input type="button" id="regNoConfirmCancel" class="cancel" value="취소"/>
</form>
<script type="text/javascript">
	function isConfirm(html){
		var result = html.split(":");
		
		//실명 인증 성공시(joined는 가입되어 있는 사람, not join은 아직 가입하지 않은 사람)
		if(result[0] == "joined" || result[0] == "not join"){
			$('#confirmForm').hide();
			$('#regNoConfirm').removeAttr("disabled");
			$('#confirmResult').html(result[1]);
			return true;
		}
		//그 밖에(no)는 주민번호 입력 오류, 이름 오류 등의 문제로 confirm이  실패한 경우
		else{
			$('#regNoConfirm').attr("disabled", true);
			alert(result[1]);
			showConfirmForm();
			return false;
		}
	}

	function initParameters(){
		var data = '&user_name=' + $('#user_name').val() +
			  	'&user_jumin1=' + $('#user_jumin1').val() +
			  	'&user_jumin2=' + $('#user_jumin2').val();
		return data; 
	}

	function initConfirmParameters(){
		var data = '&user_name=' + $('#user_name').val() +
			  	'&user_jumin1=' + $('#user_jumin1').val() +
			  	'&user_jumin2=' + $('#user_jumin2').val();
	  	return data;
	}
	var errorMessage = "<c:out value="${errorMessage}"/>";
	alertError('regNoConfirm');
	
	$('.submit').click(nextPage);
	$('.cancel').click(prevPage);
	
	$('#confirmRegNo').click(clickConfirm);
	
	//fieldIsFilled 이벤트 핸들러 등록
	$('#user_name').blur(fieldIsFilled);
	
	//validation 이벤트 핸들러 등록
	$('#user_jumin1').blur(jumin1IsValid);
	$('#user_jumin1').keydown(autoTab);
	$('#user_jumin1').keyup(autoTab);
	$('#user_jumin2').blur(jumin2IsValid);
	$('#user_jumin2').keydown(backTab);
	$('#user_jumin2').keyup(backTab);
</script>