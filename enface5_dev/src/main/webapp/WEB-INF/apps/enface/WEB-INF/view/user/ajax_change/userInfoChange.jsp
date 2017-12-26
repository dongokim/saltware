<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form id="changeForm" method="post">
	<input type="hidden" name="isOverlap" value="1"/>
	<table class="userInfoTable">
		<tr>
			<th colspan="2" align="center">변경 정보 입력</th>
		</tr>
		<tr align="left">
			<th>&nbsp;사용자 이름</th>
			<td>&nbsp;<c:out value="${user.user_name}"/></td>
	</tr>
	<tr align="left">
		<th>&nbsp;아이디</th>
		<td>
			&nbsp;<c:out value="${user.user_id}"/>
		</td>
	</tr>
	<tr class="rowspan2" align="left">
		<th>&nbsp;주         소 *</th>
		<td>
			&nbsp;<input type="text" id="homeZip1" style="text-align: center;" value="<c:out value="${user.homeZip1 }"/>" readonly="readonly" maxlength="3" size="3"/>
			&nbsp;-
			&nbsp;<input type="text" id="homeZip2" style="text-align: center;" value="<c:out value="${user.homeZip2 }"/>" readonly="readonly" maxlength="3" size="3"/> 
			&nbsp;<input type="button" id="searchZipButton" value="우편번호 찾기 "/>
			&nbsp;<input type="text" size=40 id="homeAddr1" value="<c:out value="${user.homeAddr1 }"/>" readonly="readonly"/>
			<br></br>
			&nbsp;<input type="text" size=40 class="required" id="homeAddr2" value="<c:out value="${user.homeAddr2 }"/>" style="ime-mode:active"/>&nbsp;
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
			&nbsp;<input type="text" id="user_hp2" value="<c:out value="${user.user_hp2 }"/>" class="required" maxlength="4" size="6"/>
			&nbsp;-
			&nbsp;<input type="text" id="user_hp3" value="<c:out value="${user.user_hp3 }"/>" class="required" maxlength="4" size="6"/>
		</td>
	</tr>
	<tr align="left">
		<th>&nbsp;이메일*</th>
		<td>
			&nbsp;<input type="text" id="user_email1" value="<c:out value="${user.user_email1 }"/>" class="required" maxlength="16" size="17" style="ime-mode:inactive"/>
			&nbsp;@
			&nbsp;<input type="text" id="user_email2" value="<c:out value="${user.user_email2 }"/>" class="required" maxlength="16" size="17" style="ime-mode:inactive"/>
			&nbsp;
		</td>
	</tr>
</table>
<p></p>

<input type="button" class="submit" id="userInfoChange" value="다음"/>
<input type="button" class="cancel" id="userInfoChangeCancel" value="취소"/>
</form>
<script type="text/javascript">
	var errorMessage = "<c:out value="${errorMessage}"/>";
	alertError('userInfoChange');

	$('.submit').click(nextPage);
	$('.cancel').click(prevPage);
	
	//fieldIsFilled
	$('#homeAddr2').blur(addressIsFilled);
	
	//validation
	$('#user_hp2').blur(phoneNumber2IsValid);
	$('#user_hp2').keydown(autoTab);
	$('#user_hp2').keyup(autoTab);
	$('#user_hp3').blur(phoneNumber3IsValid);
	$('#user_hp3').keydown(backTab);
	$('#user_hp3').keyup(backTab);
	$('#user_email1').blur(idIsValid);
	//$('#user_email2').blur(emailDomainIsValid);
	
	$('#searchZipButton').click(searchZip);
	
	//전화번호를 받아와서 각 필드에 값을 설정
	user_hp = "<c:out value='${user.user_hp}'/>".split("-");
	var user_hp1 = user_hp[0];
	var user_hp2 = user_hp[1];
	var user_hp3 = user_hp[2];
	
	$('#user_hp1').val(user_hp1);
	$('#user_hp2').val(user_hp2);
	$('#user_hp3').val(user_hp3);

	//이메일을 받아와서 각 필드에 값을 설정
	user_email = "<c:out value='${user.user_email}'/>".split("@");
	var user_email1 = user_email[0];
	var user_email2 = user_email[1];
	
	$('#user_email1').val(user_email1);
	$('#user_email2').val(user_email2);

	function initParameters(){
		var data = '&homeZip1=' +
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
</script>