//탭메뉴 부분
$(function(){
	loadTopMenuCnt();
})

function loadTopMenuCnt() {
	var frlmCnt = $("#frlmCnt");
	var frlsCnt = $("#frlsCnt");
	
	if(frlmCnt == null || frlsCnt == null ) return;
	
	$.ajax({
		type : 'POST',
		url : '/user/getTopMenuCnt.face',
		dataType : 'json',
		async : false,
		success : function(json){
			frlmCnt.html(json.frlmCnt+"건");
			frlsCnt.html(json.frlsCnt+"건");
		}
	});
}
//Datepicker default
function comDatepicker(id,obj){
	//default
	if(obj == undefined){
		var obj = {
	    	dateFormat: 'yy-mm-dd',
	    	showButtonPanel: true, 
	        currentText: '오늘 날짜', 
	        closeText: '닫기', 
	        changeMonth: true, 
	        changeYear: true,
	        nextText: '다음 달',
	        prevText: '이전 달',
	        dayNames: ['월요일', '화요일', '수요일', '목요일', '금요일', '토요일', '일요일'],
	        dayNamesMin: ['월', '화', '수', '목', '금', '토', '일'], 
	        monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
	        monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
	        onClose: function( selectedDate ) {
	        	  $("#startDatepicker").datepicker( "option", "maxDate", selectedDate );
	        	 } 
	    }
	}
	$(id).datepicker(obj);
}


function Request(valuename){
	var rtnval = "";
	var nowAddress = unescape(location.href);
	var parameters = (nowAddress.slice(nowAddress.indexOf("?")+1,nowAddress.length)).split("&");
   
	for(var i = 0 ; i < parameters.length ; i++){
		var varName = parameters[i].split("=")[0];
		if(varName.toUpperCase() == valuename.toUpperCase())
		{
			rtnval = parameters[i].split("=")[1];
			break;
		}
	}
	return rtnval;
}


 
function funcBisDec(bizrno){
		var val = null;
		var returnbizrno = null;
		val = bizrno.replace(/^([0-9]{3})-?([0-9]{2})-?([0-9]{5})$/, "$1-$2-$3").split("-");
		if(val.length == 3){
			returnbizrno =val[0]+"-"+val[1]+"-"+val[2];
		}
		return returnbizrno;
}
	

function passwordCheck(str) { 
	  var chk=0;
	  var reg1 = /^.*(?=^.{9,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^*&+=]).*$/;  // 영문자+숫자+특수문자 9자리 이상 20자리 이내
	  var specialChar = /^.*(?=.*[!@#$%^*&+=]).*$/;  // 영문자+숫자+특수문자 9자리 이상 20자리 이내
	  if(!reg1.test(str))
	  {
		  return false;
	  } else{
		  if(str.length <9 || str.length >15){
			  return false;
		  }else return true;
	  }
} 	

function fileUpload_get(kindCd, returnId){
	var strObj  = "?atchmnflGroupNo="+$("#atchmnflGroupNo").val();
		strObj += "&kindCd="+ kindCd;
		strObj += "&returnId="+ returnId;
		window.open("/com/cmm/dext5Up.do" + strObj,"fileUp", "width=800,height=200, toolbar=no, menubar=no, scrollbars=no, resizable=yes");
}


function fileUpload(kindCd, returnId){
var strObj  = "?atchmnflGroupNo="+$("#atchmnflGroupNo").val();
		strObj += "&kindCd="+ kindCd;
		strObj += "&returnId="+ returnId;
		
		$("#kindCd").val(kindCd);
		$("#returnId").val(returnId);
		window.open('about:blank','popwin',"width=800,height=200, toolbar=no, menubar=no, scrollbars=no, resizable=yes");
		var theForm = document.cmpnyForm;
		theForm.method = "post"; 
		theForm.target = "popwin";
		theForm.action = "/com/cmm/dext5Up.do";
		theForm.submit();
}

function funcTelDec2(id, returnId1, returnId2, returnId3){
	var val = $(id).val();
	val = val.replace(/^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?([0-9]{3,4})-?([0-9]{4})$/, "$1-$2-$3").split("-");
	if(val.length == 3){
		$(returnId1).val(val[0]);
		$(returnId1).val(val[0]).attr("selected", "selected");
		$(returnId2).val(val[1]);
		$(returnId3).val(val[2]);
	}
}
function funcTelDec3(id, textId){
	var val = $(id).val();
	val = val.replace(/^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?([0-9]{3,4})-?([0-9]{4})$/, "$1-$2-$3");
	$(textId).html(val);
}

function funcTelDec4(val, textId){
	val = val.replace(/^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?([0-9]{3,4})-?([0-9]{4})$/, "$1-$2-$3");
	$(textId).text(val);
}

function funcTelDec5(val, returnId1, returnId2, returnId3){
	if(val != null){
		val = val.replace(/^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?([0-9]{3,4})-?([0-9]{4})$/, "$1-$2-$3").split("-");
		if(val.length == 3){
			$(returnId1).val(val[0]);
			$(returnId1).val(val[0]).attr("selected", "selected");
			$(returnId2).val(val[1]);
			$(returnId3).val(val[2]);
		} 
	}else {
		$(returnId1).val("");
		$(returnId1).val("").attr("selected", "selected");
		$(returnId2).val("");
		$(returnId3).val("");
	}
}


function emailCheckUser(email1,email2,email3,returnId, label,condObj){
	 var trueYn = true;
	 if($("#"+email1).val() != "" && ($("#"+email2).val() != "" || $("#"+email3).val() != "")){
		 $("#"+returnId).val($("#"+email1).val() + "@" + $("#"+email2).val());
	 }
	 
	if(condObj.required && $("#"+email1).val() == ""){
		alert(label + " 는 반드시 입력해야 합니다.");
 		$("#"+email1).focus();
    	return trueYn;
	}
	if($("#"+email1).val() != ""){
		if (!fncRegReturn("email","#"+returnId)){
	 		alert(label + " 형식이 아닙니다.");
	 		$("#"+email1).focus();
	    	return trueYn;
		}
	}
	return !trueYn;
}
 
function ckBisNo(bisNo)
{
	 // bisNo는 숫자만 10자리로 해서 문자열로 넘긴다. 
    var checkID = new Array(1, 3, 7, 1, 3, 7, 1, 3, 5, 1); 
    var tmpBizID, i, chkSum=0, c2, remander; 
     bisNo = bisNo.replace(/-/gi,''); 

     for (i=0; i<=7; i++) chkSum += checkID[i] * bisNo.charAt(i); 
     c2 = "0" + (checkID[8] * bisNo.charAt(8)); 
     c2 = c2.substring(c2.length - 2, c2.length); 
     chkSum += Math.floor(c2.charAt(0)) + Math.floor(c2.charAt(1)); 
     remander = (10 - (chkSum % 10)) % 10 ; 

    if (Math.floor(bisNo.charAt(9)) == remander) return true ; // OK! 
      return false; 
}



function bizrnoCheck(param1,param2,param3,param,label,condObj){
	var trueYn = true;  
 	$("#"+param).val($("#"+param1).val() + "" + $("#"+param2).val() + "" + $("#"+param3).val());
 	if(condObj.required && $("#"+param1).val() == ""){
 		alert(label + " 는 반드시 입력해야 합니다.");
  		$("#"+param1).focus();
     	return trueYn;
 	}
 	if($("#"+param1).val() != ""){
 		if (!fncRegReturn("bizrno","#"+param)){
 	 		alert(label+" 형식이 아닙니다.");
 	 		$("#"+param1).focus();
 	    	return trueYn;
 		}
 	}
 	
 	if($("#"+param1).val() != ""){
		if(!ckBisNo($("#"+param).val())){
			alert(label+"가 맞지 않습니다.");
			$("#"+param1).focus();
			return trueYn;
		}
	}
 	
 	return !trueYn;
 }

 

function jurinoCheck(jurino1,jurino2,returnId, label,condObj){
	 var trueYn = true;
	 if($("#"+jurino1).val() != "" && ($("#"+jurino2).val() != "")){
		 $("#"+returnId).val($("#"+jurino1).val() + $("#"+jurino2).val());
	 }
	 
	if(condObj.required && $("#"+jurino1).val() == ""){
		alert(label + " 는 반드시 입력해야 합니다.");
		$("#"+jurino1).focus();
   	return trueYn;
	}
	if(condObj.required && $("#"+jurino2).val() == ""){
		alert(label + " 는 반드시 입력해야 합니다.");
		$("#"+jurino2).focus();
   	return trueYn;
	}	
	
	if($("#"+jurino1).val() != ""){
		if (!fncRegReturn("jurino","#"+returnId)){
	 		alert(label + " 형식이 아닙니다.");
	 		$("#"+jurino2).focus();
	    	return trueYn;
		}
	}
	if($("#"+jurino2).val() != ""){
		if (!fncRegReturn("jurino","#"+returnId)){
	 		alert(label + " 형식이 아닙니다.");
	 		$("#"+jurino2).focus();
	    	return trueYn;
		}
	}
	return !trueYn;
}

function funJuriNoDesc(id, returnId1, returnId2){
	var val = $("#"+id).val();
	val = val.replace(/^([0-9]{6})-?([0-9]{7})$/, "$1-$2").split("-");
	if(val.length == 2){ 
		$("#"+returnId1).val(val[0]);
		$("#"+returnId2).val(val[1]);
	}
}
function fncRegReturn(type,id){
	 var retVal = null;
	 if(type == "email"){
		 retVal = /^[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[@]{1}[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[.]{1}[A-Za-z]{2,5}$/;
	 }else if(type == "phone"){
		 retVal = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})?[0-9]{3,4}?[0-9]{4}$/;
	 }else if(type == "phone2"){
		 retVal = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/;
	 }else if(type == "kor"){
		 retVal = /([^가-힣\x20])/i;
	 }else if(type == "number"){
		 retVal = /^[12][0-9]{3}[01][0-9][0-3][0-9]/;
	 }else if(type == "korEn"){
		 retVal = /^[가-힣a-zA-Z]+$/;
	 }else if(type == "bizrno"){
		 retVal = /^[0-9]{3}?[0-9]{2}?[0-9]{5}$/;
	 }else if(type == "jurino"){
		 retVal = /^[0-9]{6}?[0-9]{7}$/;
	 }else if(type == "phoneSpace"){
		 retVal = /[01](0|1|6|7|8|9)[-](\d{4}|\d{3})[-]\d{4}$/g;
	 }
	 return retVal.test($(id).val());
} 

function textCheck(id,label,condObj){
	var trueYn = true;
 	var id = "#"+id;
 	if(condObj.required && $(id + "[type='text']," + id + "[type='num']").val() == ""){
 		alert(label + "은(는) 반드시 입력해야 합니다.");
 		$(id).focus();
 		return trueYn;
 	}
 	
 	if(condObj.min != undefined && getTextLength($(id + "[type='text']," + id + "[type='nummber']").val()) < condObj.min){
 		alert(label + "은(는) " + condObj.min + "자 이상이여야 합니다.");
 		$(id).focus();
 		return trueYn;
 	}
 	
 	if(condObj.max != undefined && getTextLength($(id + "[type='text']," + id + "[type='nummber']").val()) > condObj.max){
 		alert(label + "은(는)" + condObj.max + "자 이하이여야 합니다.");
 		$(id).focus();
 		return trueYn;
 	}
 	return !trueYn;
 }

/*
 * 콤마형식으로 데이터 변환
 */

function setComma(inNum){
	 inNum = String(inNum).replace(/,/gi, '');
	 var rgx2 = /(\d+)(\d{3})/;
    while (rgx2.test(inNum)) {
   	 inNum = inNum.replace(rgx2, '$1' + ',' + '$2');
     }
    return inNum;
}

function date_mask(str) {
	str = str.replace(/[^0-9]/g, '');
	var tmp = '';
	if( str.length < 5){
		return str;
	}else if(str.length < 7){
		tmp += str.substr(0, 4);
		tmp += '-';
		tmp += str.substr(4);
		return tmp;
	}else if(str.length < 9){
		tmp += str.substr(0, 4);
		tmp += '-';
		tmp += str.substr(4, 2);
		tmp += '-';
		tmp += str.substr(6,2);
		return tmp;
	}else if(str.length > 9){
		tmp += str.substr(0, 4);
		tmp += '-';
		tmp += str.substr(4, 2);
		tmp += '-';
		tmp += str.substr(6,2);
		return tmp;		
	}else{
		tmp += str.substr(0, 4);
		tmp += '-';
		tmp += str.substr(4, 2);
		tmp += '-';
		tmp += str.substr(6,2);
		return tmp;
	}
	return str; 
}


/*
* 숫자키만 가능하게 변경
*/ 
function showKeyCode(event) {
	event = event || window.event;
	var keyValue = (event.which) ? event.which : event.keyCode;
	if( ((keyValue >= 48) && (keyValue <= 57)) || keyValue==8 ||keyValue==9 || ((keyValue >= 96) && (keyValue <= 105)) ){
		return true;
	}else{ 
		return false;
	}
}
/**
 * 한글포함 문자열 길이를 구한다(인터넷)
 */
function getTextLength(str) {
	var str = str+"";
    var len = 0;
    for (var i = 0; i < str.length; i++) {
        if (escape(str.charAt(i)).length == 6) {
            len++;
        }
        len++;
    }
    return len;
}

/*
 * 용도 : radio 유효성 체크 화면(name기준)
 */
function radioCheck(name,label,condObj){
	 var trueYn = true;
	 if(condObj == undefined){
		 condObj = {required:true};
	 }
	if(condObj.required && $("input[name='entrprsSeCode_pc'][type='radio']:checked").val() == undefined){
		alert(label + "를(을) 선택해주세요.");
		$("input[name='" + name + "']").focus();
		return trueYn;
	}else{
		return !trueYn;
	}
}

function selectCheck(id,label){
	 var trueYn = true;  
	 if($("#"+id).val() == "-1"){
		alert(label+"을 선택해주세요");
		$("#"+id).focus();
		return trueYn;
	}
	return !trueYn;
}




/*
 * 용도 : 전화번호 유효성 체크 화면(id기준)
 */
function telCheck(telId1,telId2,telId3,returnId,label,condObj){
	var trueYn = true;
	$("#"+returnId).val($("#"+telId1).val() + "-" + $("#"+telId2).val() + "-" + $("#"+telId3).val());
	if(condObj.required && ($("#"+telId1).val() == "" || $("#"+telId2).val() == "" || $("#"+telId3).val() == "")){
		alert(label+" 는 반드시 입력해야 합니다.");
 		$("#"+telId2).focus();
    	return trueYn;
	}
	if($("#"+telId2).val() != ""){
		if (!fncRegReturn("phone2","#"+returnId)){
	 		alert(label+" 형식이 아닙니다.");
	 		$("#"+telId2).focus();
	    	return trueYn;
		}
	}
	return !trueYn;
}
/*
 * 용도 : 전화번호 유효성 체크 화면(id기준)
 */
function telCheckSpace(telId1,telId2,telId3,returnId,label,condObj){
	var trueYn = true;
	$("#"+returnId).val($("#"+telId1).val() + "-" + $("#"+telId2).val() + "-" + $("#"+telId3).val());
	if(condObj.required && ($("#"+telId1).val() == "" || $("#"+telId2).val() == "" || $("#"+telId3).val() == "")){
		alert(label+" 는 반드시 입력해야 합니다.");
 		$("#"+telId2).focus();
    	return trueYn;
	}
	if($("#"+telId2).val() != ""){
		if (!fncRegReturn("phoneSpace","#"+returnId)){
	 		alert(label+" 형식이 아닙니다.");
	 		$("#"+telId2).focus();
	    	return trueYn;
		}
	}
	return !trueYn;
}

function funcEmailDec(id, returnId1, returnId2, returnId3){
	var val = $("#"+id).val();
	val = val.split("@");
	if(val.length == 2){
		$("#"+returnId1).val(val[0]);
		var indexVal = $("#"+returnId3).text().indexOf(val[1]);
		if(indexVal > 0){
			$("#"+returnId3).val(val[1]);
			$("#"+returnId2).val(val[1]);
			$("#"+returnId2).attr("disabled",true);
		}else{
			$("#"+returnId2).val(val[1]);
			
		}
	}
}

function funcEmailDec2(val, returnId1, returnId2, returnId3){
	if(val != null){
		val = val.split("@");
		if(val.length == 2){
			$("#"+returnId1).val(val[0]);
			var indexVal = $("#"+returnId3).text().indexOf(val[1]);
			if(indexVal > 0){
				$("#"+returnId3).val(val[1]);
				$("#"+returnId2).val(val[1]);
			}else{
				$("#"+returnId2).val(val[1]);
				
			}
		}
	}else {
		$("#"+returnId1).val("");
		$("#"+returnId2).val("");
		$("#"+returnId3).val("").attr("selected", "selected");
	}
}

 function sample4_UserexecDaumPostcode(zipCd,addr,buldManageNo) {
     new daum.Postcode({
         oncomplete: function(data) {
             // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

             // 도로명 주소의 노출 규칙에 따라 주소를 조합한다.
             // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
             var fullRoadAddr = data.roadAddress; // 도로명 주소 변수
             var extraRoadAddr = ''; // 도로명 조합형 주소 변수

             // 법정동명이 있을 경우 추가한다. (법정리는 제외)
             // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
             if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                 extraRoadAddr += data.bname;
             }
             // 건물명이 있고, 공동주택일 경우 추가한다.
             if(data.buildingName !== '' && data.apartment === 'Y'){
                extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
             }
             
             
             // 도로명, 지번 조합형 주소가 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
             if(extraRoadAddr !== ''){
                 extraRoadAddr = ' (' + extraRoadAddr + ')';
             }
             // 도로명, 지번 주소의 유무에 따라 해당 조합형 주소를 추가한다.
             if(fullRoadAddr !== ''){
                 fullRoadAddr += extraRoadAddr;
             }

             if(document.getElementById(zipCd+"_pc") != null){
            	 document.getElementById(zipCd+"_pc").value = data.zonecode; 			//5자리 새우편번호 사용
             }		             
             if(document.getElementById(addr+"_pc") != null ){
            	 document.getElementById(addr+"_pc").value = fullRoadAddr;			//도로명
             }
             
             if(document.getElementById(buldManageNo+"_pc") != null ){
            	 document.getElementById(buldManageNo+"_pc").value = data.buildingCode; 		//건물관리 번호
             }

         }
     }).open();
 }
	
	//AnySign에서 사용하는 Util성격의 함수
	function Escape(url) {
		var i;
		var ch;
		var out = '';
		var url_string = '';

		url_string = String(url);

		for (i = 0; i < url_string.length; i++) {
			ch = url_string.charAt(i);
			if (ch == ' ')
				out += '%20';
			else if (ch == '%')
				out += '%25';
			else if (ch == '&')
				out += '%26';
			else if (ch == '+')
				out += '%2B';
			else if (ch == '=')
				out += '%3D';
			else if (ch == '?')
				out += '%3F';
			else
				out += ch;
		}
		return out;
	}
	
//구간암호화

function Ajax_Enc_Submit(param, paramUrl, returnPage, parentCheck,rbaServer,rbaPort) {
	var target_url = paramUrl; //전송시킬 페이지(Response) URL

	var xecure_url; //최종 전송될 URL을 저장할 변수
	var xecure_q;  

	var final_plan = param;

	BlockEncEx("", "GET", "UTF-8", Ajax_GET_CallBack);

	function Ajax_GET_CallBack(aResult) {
		xecure_q = "q="+ Escape(aResult)+ "&charset=UTF-8";
		xecure_url = target_url;
		BlockEncEx(final_plan, "POST", "UTF-8",	Ajax_Call);
	}

	function Ajax_Call(enc_data) {
		$.ajax({
			type : "POST",
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			url : target_url, //SID(q값)를 포함한 URL
			data : xecure_q+"&p=" + Escape(enc_data), //암호화된 String 값
			// false:동기, true:비동기
			async : false,
			xhrFields: { withCredentials: true },
			crossDomain : true,
			dataType : "text",
			success : function(response) {
				try {
					ReturnData_callback(response);
				} catch (e) {
					alert("시스템 처리 중 에러가 발생하였습니다. 관리자에게 문의하여 주세요.\n ");
					if(target_url.indexOf("/rba/syncSessionInfo")  > 0){
						enviewLogout();
					}
				}
			},error : function(err) {
				var args;
				try {
					var ex = $.parseJSON(err.responseText);
					args = ex.Message;
				} catch (e) {
					args = "네트워크 연결이 원활하지 않습니다. 다시 해보시기 바랍니다.";
				}
				alert("시스템 처리 중 에러가 발생하였습니다. 관리자에게 문의하여 주세요. \n");
				if(target_url.indexOf("/rba/syncSessionInfo")  > 0){
					enviewLogout();
				}
			}
		});

		//복호화 결과값 받는 함수
		function ReturnData_callback(response) {
			//로그인 시
			if(target_url.indexOf("/security/login") > -1){
				if(response.indexOf("JSESSIONID_rlx") > -1){
					//리로그인 체크
					if(returnPage != "relogin"){
						if(document.getElementById("LoginForm")){
							var form = document.getElementById("LoginForm");
							AnySign.XecureSubmit(form, ''); 
						}else{
							alert("로그인 처리 중 에러가 발생하였습니다. 관리자에게 문의하여 주세요.");
							enviewLogout();	
						}
					}else{
						$.ajax({
							url : "/user/chageReloginSession.face",
								async : false,
							success : function(json) { 
								if(json.result == 'error') {	
										alert("로그인 처리 중 에러가 발생하였습니다. 관리자에게 문의하여 주세요.");
										enviewLogout();	
									}else if(json.result == 'success'){
										if($('#sessionForm').serialize()){
											var param = $('#sessionForm').serialize();
											Ajax_Enc_Submit(param,'http://'+rbaServer+':'+rbaPort+'/rba/syncSessionInfo.do','relogin');
										}else{
											alert("로그인 처리 중 에러가 발생하였습니다. 관리자에게 문의하여 주세요.");
											enviewLogout();	
										}
									}
							}, 
							error : function(e) {
								alert("로그인 처리 중 에러가 발생하였습니다. 관리자에게 문의하여 주세요.");
								enviewLogout();	
							}
						});
					}
				}else if(response == "1"){
					alert("해당하는 사용자가 없습니다.");
					parent.location.href = "/portal/kops/login";
				}else if(response == "2"){
					alert("비밀번호가 일치하지 않습니다.");
					parent.location.href = "/portal/kops/login";
				}else if(response == "3"){
					alert("세션시간이 만료되었습니다.");
					parent.location.href = "/portal/kops/login";
				}else if(response == "4"){
					alert("등록된 인증서가 없습니다.");
					parent.location.href = "/portal/kops/login";
				}else if(response == "5"){
					alert("비밀번호 실패횟수 5회 제한입니다. 관리자에게 문의하여 주세요.");
					parent.location.href = "/portal/kops/login";
				}else{
					alert("로그인 처리 중 에러가 발생하였습니다. 관리자에게 문의하여 주세요.");
					parent.location.href = "/portal/kops/login";
				}
			//비밀번호 초기화 시 
			}else if(target_url.indexOf("/psh/sendEncEmail")  > -1 && returnPage != "regUser" ){
				if(response.indexOf("오류") > -1){
					alert("이메일 발송 중 에러가 발생하였습니다. 관리자에게 문의하여 주세요.");
				}else {
					alert("성공적으로 이메일이 발송되었습니다.");
				}
			}else if(target_url.indexOf("/psh/sendEncSms")  > -1 && returnPage != "regUser" ){
				if(response.indexOf("오류") > -1){
					alert("SMS 발송 중 에러가 발생하였습니다. 관리자에게 문의하여 주세요.");
				}else {
					alert("성공적으로 SMS 발송되었습니다.");
				}
			}else if(target_url.indexOf("regDedicatedUserAction") > -1){
				var res = JSON.parse(response);
				if(res.result == "-1"){
					alert(res.msg);
				}else{
					parent.location.href = '/user/reRegDedicatedUserResult.face';
				}
			}else if(target_url.indexOf("deleteUserAction") > -1){
				var res = JSON.parse(response);
				if(res.result == "-1"){
					alert(res.msg);
				}else{
					alert("탈퇴되었습니다.");
					enviewLogout();
				}
			}else if(target_url.indexOf("regHealthCareUserAction") > -1){
				var res = JSON.parse(response);
				if(res.result == "-1"){
					alert(res.msg);
					parent.location.href = "/portal/kops/join";
				}else{
					alert(res.msg);
					parent.location.href = '/user/regUserResultPage.face?userId='+res.userId;
				}
			}else if(target_url.indexOf("regUserAction") > -1){
				var res = JSON.parse(response);
				if(res.result == "-1"){
					alert(res.msg);
					parent.location.href = "/portal/kops/join";
				}else{
					alert(res.msg);
					parent.location.href = '/user/regUserResultPage.face?userId='+res.userId;
				}
			}else if(target_url.indexOf("updateUserAction") > -1){
				var res = JSON.parse(response);
				if(res.result == "-1"){
					alert(res.msg);
					window.location.href = "/user/updateUserView.face";
				}else{
					alert(res.msg);
					window.location.href = "/user/updateUserView.face";
				}
			}else if(target_url.indexOf("/rba/syncSessionInfo.do") > -1){
				if(response.indexOf("success") > -1){
					if(returnPage == "relogin"){
						var reloginUrl = window.location.href;
						if(reloginUrl.indexOf("/portal/kops/declare/personnel.page") > -1){
							alert('전담인력 배치를 위해 전담인력 신고 페이지로 이동합니다.');
							parent.location.reload(true);
						}else if(window.location.href.indexOf("/portal/kops/myKops/myDeclare.page") > -1){
							alert('전담인력 배치가 된 사용자로 나의 신고내역 페이지로 이동합니다.');
							parent.location.reload(true);
						}
					}
				}
			}
		}
	}
}