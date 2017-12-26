<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form id="userForm" method="post">
	<input type="hidden" name="isOverlap" value="1"/>
	<table id="userInfoTable">
		<tr>
			<th colspan="2" align="center">회원 정보 입력</th>
		</tr>
		<tr align="left">
			<th>&nbsp;사용자 이름 *</th>
			<td>&nbsp;<c:out value="${user.user_name}"/></td>
	</tr>
	<tr align="left">
		<th>&nbsp;희망 아이디 *</th>
		<td>
			&nbsp;<input type="text" id="user_id" class="confirmData" alt="confirmOverlap" style="ime-mode:inactive"/>
			<label id="confirmForm"></label>
			<label id="confirmResult" style="display: none"></label>
		</td>
	</tr>
	<tr align="left">
		<th >&nbsp;비밀번호 *</th>
		<td>
			&nbsp;<input type="password" class="required" id="password"/>
			&nbsp;
		</td>
	</tr>
	<tr align="left">
		<th>&nbsp;비밀번호 확인 *</th>
		<td>
			&nbsp;<input type="password" class="required" id="passwordConfirm"/>
			&nbsp;
		</td>
	</tr>
	<tr id="two" align="left">
		<th>&nbsp;주         소 *</th>
		<td>
			&nbsp;<input type="text" id="homeZip1" style="text-align: center;" readonly="readonly" maxlength="3" size="3"/>
			&nbsp;-
			&nbsp;<input type="text" id="homeZip2" style="text-align: center;" readonly="readonly" maxlength="3" size="3"/> 
			&nbsp;<input type="button" id="searchZipButton" value="우편번호 찾기 "/>
			&nbsp;<input type="text" size=40 id="homeAddr1" readonly="readonly"/>
			<br></br>
			&nbsp;<input type="text" size=40 id="homeAddr2" class="required" style="ime-mode:active"/>&nbsp;
		</td>
	</tr>
	<tr align="left">
		<th>&nbsp;휴대폰 번호*</th>
		<td>&nbsp;
			<select id="user_hp1">
				<c:forEach items="${hpList}" var="user_hp1">
					<option value="<c:out value="${user_hp1.codeName}"/>"><c:out value="${user_hp1.codeName}"/></option>
				</c:forEach>
			</select>
			&nbsp;-
			&nbsp;<input type="text" id="user_hp2" class="required" maxlength="4" size="6"/>
			&nbsp;-
			&nbsp;<input type="text" id="user_hp3" class="required" maxlength="4" size="6"/>
		</td>
	</tr>
	<tr align="left">
		<th>&nbsp;이메일*</th>
		<td>
			&nbsp;<input type="text" id="user_email1" class="required" maxlength="16" size="17" style="ime-mode:inactive"/>
			&nbsp;@
			&nbsp;<input type="text" id="user_email2" class="required" maxlength="16" size="17" style="ime-mode:inactive"/>
			&nbsp;
		</td>
	</tr>
</table>
<p></p>
<input type="hidden" id="isConfirm" value="0"/>
<input type="button" class="submit" id="userInfo" value="다음" disabled="disabled"/>
<input type="button" class="cancel" id="userInfoCancel" value="취소"/>
</form>
<script type="text/javascript">
	var errorMessage = "<c:out value="${errorMessage}"/>";
	alertError('userInfo');
	
	$('.submit').click(nextPage);
	$('.cancel').click(prevPage);
	
	//confirm
	$('#user_id').blur(blurConfirm);
	
	//fieldIsFilled
	$('#homeAddr2').blur(addressIsFilled);
	
	//validation
	$('#password').blur(passwordIsValid);
	$('#passwordConfirm').blur(passwordIsAccord);
	$('#user_hp2').blur(phoneNumber2IsValid);
	$('#user_hp2').keydown(autoTab);
	$('#user_hp2').keyup(autoTab);
	$('#user_hp3').blur(phoneNumber3IsValid);
	$('#user_hp3').keydown(backTab);
	$('#user_hp3').keyup(backTab);
	$('#user_email1').blur(idIsValid);
	$('#user_email2').blur(emailDomainIsValid);
	
	
	$('#searchZipButton').click(searchZip);
	
	$('input#user_id').select();

	function isConfirm(html){
		var result = html.split(":");
		
		$('#confirmResult').html(result[1]);
		if(result[0] == "yes"){
			$('#userInfo').removeAttr("disabled");
			return true;
		}else if(result[0] == "no"){
			$('#userInfo').attr("disabled", true);
			return false;
		}
	}

	function initParameters(){
		var data = '&user_id=' + $('#user_id').val() +
				'&password=' +
				$('#password').val() +
				'&homeZip1=' +
				$('#homeZip1').val() +
				'&homeZip2=' +
				$('#homeZip2').val() +
				'&homeAddr1=' +
				$('#homeAddr1').val() +
				'&homeAddr2=' +
				$('#homeAddr2').val() +
				'&user_hp1=' +
				$('#user_hp1').val() +
				'&user_hp2=' +
				$('#user_hp2').val() +
				'&user_hp3=' +
				$('#user_hp3').val() +
				'&user_email1=' +
				$('#user_email1').val() +
				'&user_email2=' +
				$('#user_email2').val();
		return data;
	}
	
	function initConfirmParameters(){
		var data = '&user_id=' + $('#user_id').val();
		return data; 
	}
	
</script>