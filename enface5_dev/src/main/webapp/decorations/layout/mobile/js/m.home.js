$(document).ready(function() {
	$("#knd_home").val($("#knd", parent.document).val());
	$("#groups_home").val($("#groups", parent.document).val());
	$("#name_ko_home").val($("#name_ko", parent.document).val());
	$("#name_en_home").val($("#name_en", parent.document).val());
		
	touch('today_notice');
	touch('notice');
	touch('student_notice');
		
	userinfo();

	$("#height_home").val($("#height", parent.document).val());
	$("#width_home").val($("#width", parent.document).val());
	heightResize($("#height_home").val(),$("#width_home").val());
		
	menu();
		
});

function tabMenuSel(val){
	$(".tab_wrap > ul > li").removeClass();
	$("#tab_M"+val+"").addClass("active");
	if (val==1) {
		$("#touchSlider1").show();
		$("#touchSlider2 , #touchSlider3").hide();
	}else if(val==2){
		$("#touchSlider2").show();
		$("#touchSlider1 , #touchSlider3").hide();
	}else if(val==3){
		$("#touchSlider3").show();
		$("#touchSlider1 , #touchSlider2").hide();
	}
}
	
function sliderchk(slid,page){
	if (page==1) {
		$("#slider"+slid+"_bul"+(page+1)+"").removeClass();
		$("#slider"+slid+"_bul"+page+"").addClass("active");
		
		$("#slider"+slid+"_bul"+(page+1)+"_"+(page+1)+"").removeClass();
		$("#slider"+slid+"_bul"+page+"_"+page+"").addClass("active");
	} else {
		$("#slider"+slid+"_bul"+(page-1)+"").removeClass();
		$("#slider"+slid+"_bul"+page+"").addClass("active");
		
		$("#slider"+slid+"_bul"+(page-1)+"_"+(page-1)+"").removeClass();
		$("#slider"+slid+"_bul"+page+"_"+page+"").addClass("active");
	}
}

function imageError(element) {
    element.onerror='';
    element.src='/kaist/images/portal/noImg/noImgM.png';
}

function menu(){
	var now = new Date();  
	var pr = now.getSeconds();
	var linkiconPath = "/images/menu/";
	var url = "/link/mobile/mobileList.face?dummy=" + pr;
	var linkId, linkNmKo, linkNmEn, mhdnFlg, mobileUrlKo, mobileUrlEn, icon;
	
	$(".quick_wrap *").remove();
	
	$.ajax({	
		type: "GET",
		url: url,
		success : function(data){
			for(var i = 0 ; i < data.length ; i++){
				var menu = data[i];
				if ( menu.mhdnFlg == 'Y' ) {
					var table = '<li id="' + menu.linkId + '">';
					table += '<a href="' + menu.mobileUrl +'" target="' + menu.target + '">';
					table += '<img src="' + linkiconPath + menu.icon + '" alt="' + menu.linkNm + '" onerror="imageError(this);" /><p>' + menu.linkNm + '</p>';
					table += '</a>';
					table += '</li>';
					$(".quick_wrap").append(table);
				}
			}
		}
	});
}

function userinfo(){
	touch('touchSlider4');
}

function touch(boardId){
	if(boardId=="today_notice"){
		if($("#notice_tab1_chk").val()=="Y"){
			$("#today_notice_tab2").remove();
			$("#slider1_bul2_2").attr("style","visibility: hidden;");
			$("#slider1_bul2").attr("style","visibility: hidden;");
		}
		$("#touchSlider1").off();
		$("#touchSlider1").touchSlider({ roll : false, flexible : true, counter : function (e) { 
			sliderchk(1,e.current);
			if(e.current=="0"){
				touch("today_notice");
			}
		}});
	} else if(boardId=="notice"){
		$("#touchSlider2").off();
		$("#touchSlider2").touchSlider({ roll : false, flexible : true, counter : function (e) { 
			sliderchk(2,e.current);
			if(e.current=="0"){
				touch("notice");
			}
		}});
	} else if(boardId=="student_notice"){
		$("#touchSlider3").off();
		$("#touchSlider3").touchSlider({ roll : false, flexible : true, counter : function (e) { 
			sliderchk(3,e.current);
			if(e.current=="0"){
				touch("student_notice");
			}
		}});
	} else if(boardId=="touchSlider4"){
		$("#touchSlider4").off();
		$("#touchSlider4").touchSlider({	roll : false, flexible : true, counter : function (e) { 
			sliderchk(4,e.current);
			if(e.current=="0"){
					
			}
		}});
	}
}

function setname(){
	if($("#knd_home").val()=="ko"){
		if($("#name_ko_home").val()==""){
			$(".name").text($("#name_en_home").val());
		}else{
			$(".name").text($("#name_ko_home").val());
		}
	}else{
		if($("#name_en_home").val()==""){
			$(".name").text($("#name_ko_home").val());
		}else{
			$(".name").text($("#name_en_home").val());
		}
	}
}
	
function refresh(){
	$("#refresh").show();
}

function refreshAjaxCall(){
	if(!$('#email_Url').hasClass('noPermission')) emailCountUpdate();
	if(!$('#wf_Url').hasClass('noPermission')) wfCountUpdate();
	if(!$('#erp_Url').hasClass('noPermission')) erpCountUpdate();
	if(!$('#lib_Url').hasClass('noPermission')) libCountUpdate();
	reqCountUpdate();
}

//이메일
function emailCountUpdate() {
	$.ajax({
		url : "/personArea/countUpdate.face",
		type : "POST",
		timeout : 4000,
		data : "reqType=EMA", // REQ_TYPE
		dataType : "json",
		success : function(data) {
			$("#email_span").text(data.result);
			$(".emailUrl").attr("href", data.webUrl);
		}, error: function(data) {
			$("#email_span").text(data.result);
		}
	});
}

//전자문서
function wfCountUpdate() {
	$.ajax({
		url : "/personArea/countUpdate.face",
		type : "POST",
		timeout : 4000,
		data : "reqType=WF", // REQ_TYPE
		dataType : "json",
		success : function(json) {
			$("#wf_span").text(json.result);
			$(".workFlowUrl").attr("href", json.webUrl);
		}, error: function(json) {
			$("#wf_span").text(json.result);
		}
	});
}

// ERP
function erpCountUpdate() {
	$.ajax({
		url: "/personArea/countUpdate.face",
		type:"POST",
		timeout:4000,
		data : "reqType=ERP", // REQ_TYPE
		dataType:"json",
		success: function(data) {
			$("#erp_span").text(data.result);
			$(".erpUrl").attr("href",data.webUrl);
		}, error: function(data) {
			$("#erp_span").text(data.result);
		}
	});
}

// 도서관
function libCountUpdate() {
	$.ajax({
		url: "/personArea/countUpdate.face",
		type:"POST",
		timeout:4000,
		data : "reqType=LIB", // REQ_TYPE
		dataType:"json",
		success: function(data) {
			$("#lib_span").text(data.result);
			$(".libUrl").attr("href",data.webUrl);
		}, error: function(data) {
			$("#lib_span").text(data.result);
		}
	});
}

//문의/신청
function reqCountUpdate() {
	$.ajax({
		url : "/personArea/countUpdate.face",
		type : "POST",
		timeout : 4000,
		data : "reqType=REQ",
		dataType : "json",
		success : function(data) {
			
		}, error: function(data) {
			
		}
	});
}


function heightResize(height, width){
	if(width<717){
		$(".quick_wrap").attr("style","height:165px;");
	}else{
		$(".quick_wrap").attr("style","height:75px;");
	}
	
	if(height > 850){
		$("#tab_wrap").hide();
		$("#tab_M2 , #tab_M3").hide();
		
		$("#tab_wrap1, #tab_wrap2 , #tab_wrap3" ).show();
		$("#touchSlider3 , #touchSlider2").show();

		if(width < 700){
			parent.iframeController("1053");	
		}else{
			parent.iframeController("978");
		}
	} else {
		$(".tab_wrap > ul li").removeClass();
		$("#tab_M1").addClass("active");
		$("#touchSlider1").show();
		
		//3단 탭을 숨김
		$("#tab_wrap").show();
		$("#tab_M2 , #tab_M3").show();
		// 1,2,3번 탭을 보여줌
		$("#tab_wrap1, #tab_wrap2 , #tab_wrap3" ).hide();
		// 2,3번 슬라이드를 보여줌
		$("#touchSlider3 , #touchSlider2").hide();
		if(width < 700){
			parent.iframeController("608");
		}else{
			parent.iframeController("543");
		}
	}
};

// null 체크
function emptySet(value,id){
	var temp = (4-value);
	for(var i=1 ; i <= temp ; i++){
		var rowString = '';
		if(i == 1) rowString = portalPage.getMessage('eb.info.desc.not.exist.bltn');
		$("#"+id+"").append('<li class="first">'+rowString+'</li>');
	}
	
	if(temp <= 4){
		for(var i=1 ; i <= 4 ; i++){
			$("#today_notice_List2").append('<li class="first">&nbsp;</li>');
		}	
	}else{
		var temp1 = (4-temp);
		for(var i=1 ; i <= temp1 ; i++){
			$("#today_notice_List2").append('<li class="first">&nbsp;</li>');
		}
	}
}

function date(time){
	var date = time.indexOf(" ");
	date = time.substring(0,date);
	date = date.replace(/-/g,".");
	return date;
}

function bltnNo(article){
	
	var bltnNo = article.indexOf("bltnNo=");
	bltnNo = article.substring((bltnNo+7));
	return bltnNo;
}