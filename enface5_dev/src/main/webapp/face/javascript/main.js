/*************************************************************
 ************                                   ************** 
 ************  이하 enface-3.2.3 전체 공유 함수  **************
 ************                                   **************
 *************************************************************/

if ( ! window.enview )
    window.enview = new Object();
	
if ( ! window.enview.portal )
    window.enview.portal = new Object();
	
if ( ! window.enview.util )
    window.enview.util = new Object();

//메세지리소스 객체 생성
var messageResource = new enview.portal.MessageResource();


/**
 * jqeury 사용하지 않을 때  ajax 이용 함수
 **/

//request 생성 함수
function createRequest() {
	var request = null;
	try {
		request = new XMLHttpRequest();
	} catch (tryMS) {
		try {
			request = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (otherMS) {
			try {
				request = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (failed) {
				request = null;
			}
		}
	}
	return request;
}

//이벤트 받아서 해당 오브젝트 반환해주는 함수
function getActivatedObject(e) {
	var obj;
	if (!e) {
		// early version of IE
		obj = window.event.srcElement;
	} else if (e.srcElement) {
		// IE 7 or later
		obj = e.srcElement;
	} else {
		// DOM Level 2 browser
		obj = e.target;
	}
	return obj;
}

//이벤트 핸들러 등록 함수, 이 함수를 써서 여러개의 이벤트 핸들러 설정 가능
function addEventHandler(obj, eventName, handler){
	if(document.attachEvent){ //IE일 경우
		obj.attachEvent("on" + eventName, handler);
	}
	else if(document.addEventListener){//IE 제외 한 브라우저
		obj.addEventListener(eventName, handler, false);
	}
}

//로그인 페이지 이동 함수
function goLoginPage(){
	location.href = loginUrl;
}

//전화번호를 받아와서 각 필드에 값을 설정
function splitPhoneNumer(phoneNum){
	user_hp = phoneNum.split("-");
	var user_hp1 = user_hp[0];
	var user_hp2 = user_hp[1];
	var user_hp3 = user_hp[2];
	
	$('#user_hp1').val(user_hp1);
	$('#user_hp2').val(user_hp2);
	$('#user_hp3').val(user_hp3);
}

//이메일을 받아와서 각 필드에 값을 설정
function splitEmail(email){
	user_email = email.split("@");
	var user_email1 = user_email[0];
	var user_email2 = user_email[1];
	
	$('#user_email1').val(user_email1);
	$('#user_email2').val(user_email2);
}

//우편번호 검색 팝업 창
function searchZip(){
	open(contextPath + "/tool/ajaxSearchZip.face", "", "location=no, width=465, height=400, toolbar=no, menubar=no, resizable=no, status=no, scrollbars=no");
}

//사용자 검색 팝업 창
function searchUser(){
	open(contextPath + "/tool/ajaxSearchUser.face", "", "location=no, width=465, height=400, toolbar=no, menubar=no, resizable=no, status=no, scrollbars=no");
}


/*
 * 기본 validation 함수 
 */

//값이 비었는지 확인
function fieldIsFilled(e) {
	var $this = $(this);
	if ($this.val() == "") {
		warn($this, "required");
	} else {
		unwarn($this, "required");
	}
	setSubmitable();
}

//값이 숫자인지 확인
function fieldIsNumbers(e) {
	var $this = $(this);
	var nonNumericChars = /^[0-9]/;
	if (!nonNumericChars.test($this.val())) {
		warn($this, "numbers");
	} else {
		unwarn($this, "numbers");
	}
	setSubmitable();
}

/**
 * some types validation function
 * 
 **/

//Provision
function isChecked(e) {
	var $this = $(this);
	if ($this.attr('checked') != 'checked') {
		$('.submit').attr("disabled", "disabled");
	} else {
		$('.submit').removeAttr("disabled");
	}
}

/*
 * 경고 메시지 출력/제거 함수
 */
function warn(field, warningType) {
	var fieldId = field.attr('id');
	var warning = eval('warnings.' + fieldId + '.' + warningType);
	var parent = $('#' + fieldId).parent();
	var span = parent.children('span#warning');
	if (span.length == 0){
		$('<span>').attr('id', 'warning').text(warning).appendTo(parent);
	}
	else {
		span.text(warning);
	}
}

function unwarn(field, warningType){
	var fieldId = field.attr('id');
	var parent = $('#' + fieldId).parent();
	var span = parent.children('span#warning');
	var currentWarning = span.text();
	var warning = eval('warnings.' + fieldId + '.' + warningType);
	
	if (currentWarning == warning) {
		span.remove();
	}
}


//confirm 버튼의 활성화 여부 판단
function setConfirmabled(){
	var confirmDataIsFilled = true;
	$confirmData = $('.confirmData');
	for(var i =0 ; i < $confirmData.length ; i++){
		//클래스가 required 인 input 요소들 중 하나라도 빈 값이 있으면 false
		if($confirmData[i].value == ""){
			confirmDataIsFilled = false;
			break;
		}
	}
	if (confirmDataIsFilled) {
		$('.confirm').removeAttr("disabled");
	}
	else {
		$('.confirm').attr("disabled", true);
	}
}

//submit 버튼의 활성화 여부 판단
function setSubmitable(){
	var isConfirm = $('#isConfirm');
	var requiredIsFilled = true;
	var confirmValue = 1;
	
	if (isConfirm.length > 0 && isConfirm.val() != '') {
		confirmValue = isConfirm.val();
	}
	
	if (confirmValue == 1) {
		$required = $('.required').length;
		for (var i = 0; i < $required.length; i++) {
			//클래스가 required 인 input 요소들 중 하나라도 빈 값이 있으면 false
			if ($required[i].value == "") {
				requiredIsFilled = false;
				break;
			}
		}
		if (requiredIsFilled) {
			if (isSubmitOK()) {
				$('.submit').removeAttr("disabled");
				return;
			}
		}
	}
	$('.submit').attr("disabled", true);
}

function isSubmitOK(){
	if($('span#warning').length == 0){
		return true;
	}
	else return false;
	
}

//confirm 버튼 click 시 이벤트 발생
function clickConfirm(e){
	var $this = $(this);
	var method = $this.attr('id');

	showConfirmResult();
	requestConfirm(method);
}

//오류 출력 함수
function alertError(method){
	if (errorMessage != null && errorMessage.length > 0) {
		alert(errorMessage);
		errorPage(method);
		return true;
	}
}

//confirm 결과가 실패 시 다시 confirm 폼을 보여주는 함수
function showConfirmForm(){
	$('#confirmResult').hide();
	$('#confirmForm').show();
}

//confirm 결과를 보여주는 함수
function showConfirmResult(){
	$('#confirmForm').hide();
	$('#confirmResult').html("잠시만 기다리십시오...");
	$('#confirmResult').show();
}

//checkAll
function checkAllCheckboxies(){
	$checkAll = $('#checkAll');
	$checkboxs = $('input:checkbox.checkbox');
	
	if($checkAll.attr('checked') == false){
		$checkboxs.removeAttr('checked');	
	}else{
		$checkboxs.attr('checked', 'checked');
	}
}