//빈칸 체크
function emptyCheck(id, label)
{
	if($("#"+id).val().length == 0)
	{
		alert(label + " 을(를) 입력하세요.");
		$("#"+id).focus();
		return false;
	}
	return true;
}

// 아이디 체크(숫자, 영문자 5-12)
function idCheck(id){
	var reg = /^[A-Za-z0-9]{5,12}$/;
	var reg2 = /[A-Za-z]/g;
	var reg3 = /[0-9]/g;
	
	var value = $('#'+id).val();
	
	if(emptyCheck(id, '아이디'))
	{
		if(!reg.test(value) || !reg2.test(value) || !reg3.test(value))
		{
			alert("아이디는 숫자, 영문자로만 5~12자 이내로 입력하세요.");
			$("#user_id").focus();
			return false;
		}
		return true;
	}
	return false;
}

// 아이디 중복체크
function fn_idOverlap(id)
{
	$.ajax({
		url:'/user/userIdOverlapCheck.face',
		type:'post',
		dataType:'json',
		data:{"userId":id},
		success:function(json){
			if(json.result =="0"){
				alert("사용가능한 아이디입니다.");
				$('#isOverLapChk').val("Y");
			}else{
				alert("사용불가능한 아이디입니다.");
				$('#isOverLapChk').val("N");
				}
			}
		});			
}

// 비밀번호 체크
function passwordCheck(pwd, repwd) {
	  var reg = /^.*(?=^.{8,}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^*&+=]).*$/;  // 영문자+숫자+특수문자 8자리 이상
	  
	  if(emptyCheck(pwd, '비밀번호') && emptyCheck(repwd, '비밀번호 확인'))
	  {
		  if(!reg.test($("#"+pwd).val()))
		  {
			  alert("비밀번호는 숫자, 영문자, 특수문자로 8자 이상 입력하세요.");
			  $("#user_password").focus();
			  return false;
		  }
		  if($("#"+pwd).val() != $("#"+repwd).val())
		  {
			  alert("비밀번호가 일치하지 않습니다.");
			  $("#confirm_password").focus();
			  return false;
		  }
		  return true;
	  }
	  return false;
}

function nameCheck(id, name)
{
	var reg = /^[가-힣a-zA-Z]+$/;
	var value = $("#"+id).val();
	
	if(emptyCheck(id, name))
	{
		if(!reg.test(value))
		{
			alert("이름은 한글 또는 영문으로 입력하세요.");
			$("#"+id).focus();
			return false;
		}
		return true;
	}
	return false;
}

//전화번호 체크
function telCheck(returnId, name)
{
	var reg = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})([0-9]{3,4})([0-9]{4})$/;
	
	var tel = $("#"+returnId).val();
	
	if(emptyCheck(returnId, name))
	{
		if(!reg.test(tel))
		{
			alert(name + " 형식이 일치하지 않습니다.");
			$("#"+returnId).focus();
			return false;
		}
		$("#"+returnId).val($("#"+returnId).val());
		return true;
	}
	return false;
}

//전화번호 체크(필수x)
function homeTelCheck(returnId)
{
	var reg = /^(0(2|3[1-3]|4[1-4]|5[1-5]|6[1-4]))(\d{3,4})(\d{4})$/;
	
	var tel = $("#"+returnId).val();
	
	if(tel != "")
	{
		if(!reg.test(tel))
		{
			alert("유선번호 형식이 일치하지 않습니다.");
			$("#"+returnId).focus();
			return false;
		}
	}
	return true;
}

//팩스 체크(필수x)
function faxCheck(returnId)
{
	var reg = /^(0(2|3[1-3]|4[1-4]|5[1-5]|6[1-4]))(\d{3,4})(\d{4})$/;
	
	var fax = $("#"+returnId).val();
	
	if(fax != "")
	{
		if(!reg.test(fax))
		{
			alert("팩스 형식이 일치하지 않습니다.");
			$("#"+returnId).focus();
			return false;
		}
	}
	return true;
}

//전화번호 체크
function mobileTelCheck(returnId, name)
{
	var reg = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})([0-9]{3,4})([0-9]{4})$/;
	
	var tel = $("#"+returnId).val();
	
	if(emptyCheck(returnId, name))
	{
		if(!reg.test(tel))
		{
			alert(name + " 형식이 일치하지 않습니다.");
			$("#"+returnId).focus();
			return false;
		}
		return true;
	}
	return false;
}

//전화번호 체크
function applyMobileTelCheck(returnId)
{
	var reg = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})([0-9]{3,4})([0-9]{4})$/;
	
	var tel = $("#"+returnId).val();
	
	if(emptyCheck(returnId, '신청 담당자 전화번호'))
	{
		if(!reg.test(tel))
		{
			alert("신청 담당자 전화번호 형식이 일치하지 않습니다.");
			$("#"+returnId).focus();
			return false;
		}
		return true;
	}
	return false;
}

//전화번호 체크
function telCheck2(returnId, ceoCheck)
{
	var reg = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})([0-9]{3,4})([0-9]{4})$/;
	
	var id = "";
	if(ceoCheck){
		id = "ceo_phone_1";
	} else {
		id = "user_phone_1";
	}

	var tel = $("#"+id).val();
	
	if(emptyCheck(id, '전화번호'))
	{
		if(!reg.test(tel))
		{
			alert("전화번호 형식이 일치하지 않습니다.");
			$("#"+id).focus();
			return false;
		}
		$("#"+returnId).val($("#"+id).val());
		return true;
	}
	return false;
}

// 사무실 전화번호 체크
function offcTelCheck(id,returnId){
	var reg = /^(0(2|3[1-3]|4[1-4]|5[1-5]|6[1-4]))-(\d{3,4})-(\d{4})$/;
	
	var tel = $("#"+id).val();
	
	if(emptyCheck(id, '전화번호'))
	{
		if(!reg.test(tel))
		{
			alert("전화번호 형식이 일치하지 않습니다.");
			$("#"+id).focus();
			return false;
		}
		$("#"+returnId).val($("#"+id).val());
		return true;
	}
	return false;
}

//이메일 체크
function emailCheck(returnId,ceoCheck){
	var reg = /^[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[@]{1}[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[.]{1}[A-Za-z]{2,5}$/;

	var email1 = null;
	var email2 = null;
	
	if(ceoCheck){
		 email1 = $("#ceo_email1").val();
		 email2 = $("#ceo_email2").val();
	}else{
		 email1 = $("#email_1").val();
		 email2 = $("#email_2").val();
	}
	if(email1 != "" && email2 != ""){
		$("#"+returnId).val(email1 + "@" + email2);
	}
	 
	if(email1 == ""){
		alert("이메일을 입력하세요.");
 		$("#email_1").focus();
    	return false;
	}
	if (!reg.test($("#"+returnId).val()))
	{
		alert("이메일 형식이 일치하지 않습니다.");
		$("#email_1").focus();
		return false;
	}
	return true;
}


//이메일 체크
function emailCheck2(returnId){
	var reg = /^[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[@]{1}[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[.]{1}[A-Za-z]{2,5}$/;

	var email1 = null;
	var email2 = null;
	

		 email1 = $("#email1").val();
		 email2 = $("#email2").val();

	if(email1 != "" && email2 != ""){
		$("#"+returnId).val(email1 + "@" + email2);
	}
	 
	if(email1 == ""){
		alert("이메일을 입력하세요.");
 		$("#email1").focus();
    	return false;
	}
	if (!reg.test($("#"+returnId).val()))
	{
		alert("이메일 형식이 일치하지 않습니다.");
		$("#email1").focus();
		return false;
	}
	return true;
}

//생년월일 체크
function birthCheck(id, name)
{
	var reg = /^(19|20)\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$/;
	
	var birth = $("#"+id).val();
	
	if(birth != "")
	{
		if(!reg.test(birth))
		{
			alert(name + "은 숫자로만 입력하세요.");
			$("#" + id).focus();
			return false;
		}
	}
	return true;
}

function birthCheck2(birthObj)
{
	var reg = /^(19|20)\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$/;
	
	var birth = birthObj.val();
	
	if(birth != "")
	{
		if(!reg.test(birth))
		{
			alert("생년월일은 8자리의 숫자로만 입력하세요.");
			birthObj.focus();
			return false;
		}
	}
	return true;
}

// 이메일 select box 변경
function emailChange(obj, emailValue){
	if(emailValue ==""){
		$("input[name=email_2]").attr("readonly",false);
		$("#email_2").val("");
		$("#email_2").focus();
	}else{
		$("input[name=email_2]").attr("readonly",true);
		$("#email_2").val(emailValue);
	}
}

function sexFlagCheck(){
	
	$("#sexFlag").val($("input[name='sex']:checked").val());
	
	if($("#sexFlag").val() ==""){
		alert("성별을 선택하세요");
		$(':radio[name="sex"]').focus();
		return false;
	}
	return true;
}

//사업자 등록증 체크
function bsFileCheck(returnId)
{
	var file = $("#"+returnId).val();
	
	if(emptyCheck(returnId, '사업자 등록증'))
	{
		return true;
	}
	return false;
}

function checkUsePrePwd (id, pass) { 
	var result = false;
	
	$.ajax({
		type : 'POST',
		url : '/user/checkPrePwd.face',
		async : false,
		dataType : 'json',
		data : {
			userId : id,
			pass : pass
		},
		success : function(json, textStatus){
			if(json.result == 'success') { // 사용할 수 있는 비밀번호
				result = true;
			} else if(json.result == 'fail') { // 이전에 사용한 비밀번호
				result = false;
			}				
		},
		error : function(x, e, textStatus, errorThrown, XMLHttpRequest){
		
		}
	});
	
	return result;
}


function pwModify(){
	var form = $("#pwChangeForm");
	var prepw = form.find("#prepw");
	var pw = form.find("#pw");
	var repw = form.find("#repw");
	var userId = "${user.userId}";
	
	
	
	var pMap = {
		userId : userId,
		userNewPw : pw.val(),
		userPrePw : prepw.val()
   };

   $.ajax({
       type : "POST",
       url : "/user/changePw.face",
       dataType : "json",
       data : pMap,
       error:function(json) {
           alert(json.msg);
       },
       success:function(json) {
           if(json.status == "success") {
               alert(json.msg);
               window.location.href = "/portal";
           } else if(json.status == "fail") {
               alert(json.msg);
           }
       }
   });
}

function userCheckPw(){
	$("#userId").val($("#user_id").val());
	$("#password").val($("#user_password").val());
				
	if($("#realpw").val() != null && $("#realpw").val() == '') {
		alert("이전 비밀번호를 입력해주세요.");
		$("#realpw").focus();
		return false;
	}

	// 이전 비밀번호가 맞는지 확인
	if(checkUsePrePwd($("#userId").val(), $("#realpw").val()) == true) {
		alert("비밀번호가 일치하지 않습니다.");
		$("#realpw").focus();
		return false;
	}
					
	if(!($("#user_password").val() == null || $("#user_password").val() == '')) {
					
		if($("#confirm_password").val() == null && $("#confirm_password").val() == '') {
			alert("새로운 비밀번호 확인을 입력해주세요.");
			$("#confirm_password").focus();
			return false;
		}
						
		if(checkUsePrePwd($("#userId").val(), $("#user_password").val()) == false) {
			alert("이전 비밀번호는 사용하실 수 없습니다.");
			return false;
		}

		if(!passwordCheck('user_password', 'confirm_password')) {
			return false;
		}

		if($("#user_password").val() != $("#confirm_password").val()) {
			alert("비밀번호 확인 값이 비밀번호와 다릅니다.");
			$("#confirm_password").focus();
			return false;
		}
	} 
			
	return true;
}
			