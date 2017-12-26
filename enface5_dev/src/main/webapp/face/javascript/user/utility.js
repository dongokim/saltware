/**
 * @author saltware
 */

//주민번호 앞자리
function jumin1IsValid(e) {
	var $this = $(this);
	var nonRegNumChars = /^[0-9]{6}/;
	if (!nonRegNumChars.test($this.val())) {
		warn($this, "invalid");
	} else {
		unwarn($this, "invalid");
	}
	setConfirmabled();
}

//주민번호 뒷자리
function jumin2IsValid(e) {
	var $this = $(this);
	var nonRegNumChars = /^[0-9]{7}/;
	if (!nonRegNumChars.test($this.val())) {
		warn($this, "invalid");
	} else {
		unwarn($this, "invalid");
	}
	setConfirmabled();
}

//ID validation
function idIsValid(e) {
	var $this = null;
	if( typeof(e) == "object"){
		$this = $(this);
	}
	else if(typeof(e) == "string"){
		$this = $('#'+ e);
	}
	var nonIDChars = /^[a-z0-9_]{6,16}/;
	if (!nonIDChars.test($this.val())) {
		warn($this, "invalid");
		setSubmitable();
		return false;
	} else {
		unwarn($this, "invalid");
		setSubmitable();
		return true;
	}
}

//비밀번호
function passwordIsValid(e){
	var $this = $(this);
	var nonPasswordChars = /^[a-z0-9_]{5,16}/;
	if (!nonPasswordChars.test($this.val())) {
		warn($this, "invalid");
	} else {
		unwarn($this, "invalid");
	}
	setConfirmabled();
	setSubmitable();
}

//비밀번호 재확인
function passwordIsAccord(e){
	var $this = $(this);
	var nonPasswordChars = /^[a-z0-9_]{5,16}/;
	if (!nonPasswordChars.test($this.val())) {
		warn($this, "invalid");
	} else {
		unwarn($this, "invalid");
		
		if($('#password:first').val() != $this.val()){
			warn($this, "accord");
		} 
		else{
			unwarn($this, "accord");
		}
	}
	setSubmitable();
}

//주소가 비었는지 확인
function addressIsFilled(e){
	var $this = $(this);
	if($('#homeZip1').firstElement().value == "" ||
			$('#homeZip2').firstElement().value == "" ||
			$('#homeAddr1').firstElement().value == "" ||
			$('#homeAddr2').firstElement().value == ""){
		warn($this, "required");
	} else {
		unwarn($this, "required");
	}
	setSubmitable();
}

//phone 
function phoneNumber2IsValid(e) {
	var $this = $(this);
	var nonPhoneNumericChars = /^[0-9]{3,4}/;
	if (!nonPhoneNumericChars.test($this.val())) {
		warn($this, "numbers");
	} else {
		unwarn($this, "numbers");
	}
	setSubmitable();
}

//phone 
function phoneNumber3IsValid(e) {
	var $this = $(this);
	var nonPhoneNumericChars = /^[0-9]{4}/;
	if (!nonPhoneNumericChars.test($this.val())) {
		warn($this, "numbers");
	} else {
		unwarn($this, "numbers");
	}
	setSubmitable();
}

//E-mail Domain
function emailDomainIsValid(e) {
	var $this = $(this);
	var nonEmailDomainChars = /^[\w-]+(\.\w{2,4})+$/;
	if (!nonEmailDomainChars.test($this.val())) {
		warn($this, "invalid");
	} else {
		unwarn($this, "invalid");
	}
	setSubmitable();
}




//선택된 값의 제일 처음 오브젝트 반환 플러그인 함수
$.fn.firstElement = function(){
	return this[0];
}

//오토탭
function autoTab(e){
	var keyCode = e.which;
	if(keyCode == 8 && e.type == 'keydown') { return;}
	var $this = $(this);
	
	var currentLength = $this.val().length;
	var maximumLangth = $this.attr('maxlength');

	if(currentLength == maximumLangth) { 
		$this.next().focus();
	}
	
}

//백탭 - backspace키를 누를 시 전 필드로 이동
function backTab(e){
	var keyCode = e.which;
	
	var $this = $(this);
	var currentLength = $this.val().length;

	if(keyCode == 8 && currentLength == 0) { 
		$this.prev().focus();
		$this.prev().val($this.prev().val());
	}
}

//<div id="contents"></div> 영역 변경 함수
function initContents(responseHTML){
	$('div#contents').hide();
	$('div#contents').html(responseHTML);
	$('div#contents').show();

	if ($('#user_name').length != 0) {
		$('#user_name').select();
	}
	else {
		if ($('#user_id').length != 0) {
			$('#user_id').select();
		}
		else if($('#password').length != 0) {
				$('#password').select();
		}
	}
}

//페이지 요청 함수
function requestPage(work, method){
	var data = '';
	try {
		data = initParameters();
	}catch(e){
		data = '';
	}
	
	$.ajax({
		type: 'POST',
		url: contextPath + '/user/ajax' + work + '.face', 
		data:'m=' + method + data,
		dataType: 'html',
		success: function(html, textStatus){
			initContents(html);
		},
		error: function(xhr, textStatus, errorThrown){
			alert('잠시 후에 다시 시도 해주십시오.');
		}
	});
}

//페이지 초기화 함수
function initPage(work, method){
	requestPage(work, method);
}

//페이지 이동 함수
function nextPage(e){
	var work = $('#work').val();
	var $this = $(this);
	var next = eval('methods.' + $this.attr('id') + '.next');
	initPage(work, next);
}

function prevPage(e){
	var work = $('#work').val();
	var $this = $(this);
	var prev = eval('methods.' + $this.attr('id').replace('Cancel', '') + '.prev');
	initPage(work, prev);
}

function errorPage(method){
	var prev = eval('methods.' + method + '.prev');
	initPage(prev);
}

//confirm 요청 함수
function requestConfirm(method){
	var data = initConfirmParameters();
	
	$.ajax({
		type: 'POST',
		url: contextPath + '/user/ajaxConfirm.face',
		data: 'm=' + method + data,
		dataType: 'html',
		success: function(html, textStatus){
			if(isConfirm(html)){
				$('#isConfirm').val(1);
			}
			else{
				$('#isConfirm').val(0);
			}
			setSubmitable();
		},
		error: function(xhr, textStatus, errorThrown){
			alert(textStatus+ ', 잠시 후에 다시 시도 해주십시오.');
			showConfirmForm();
		}
	});
}

