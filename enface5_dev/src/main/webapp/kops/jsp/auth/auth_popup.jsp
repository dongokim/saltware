<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>환자안전보고학습시스템</title>
<meta name="author" content="환자안전보고학습시스템"/>
<meta name="description" content="환자안전보고학습시스템"/>
<meta name="keywords" content="환자안전보고학습시스템"/>
<meta name="Resource-type" content="Document"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/kops/css/default.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/sub.js"></script>

<!-- 공인인증서 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/AnySign/anySign4PCInterface.js"></script>
	
	<style>
		.noshow { display:none; height:0px; width:0px; }
	</style>

	<script type="text/javascript">
		// 인증 여부 전역변수
		var isAuthUser = false;
		
		$(document).ready(function() {
			PrintObjectTag(); // AnySign 초기화 함수
		});

		function authUser(method) {
			if(method == null || method == "undefined") return;
			
			var frm = $("#authUserFrame");
			
			if(method == "ipin") {
				frm.attr("src", "${pageContext.request.contextPath}/kops/jsp/auth/auth_ipin.jsp");
			} else if(method == "cert") {
				Sign_with_option(0, '', function(signData){
					successCallback("cert", null, null, null, signData);
				});
			} else if(method == "sms") {
				frm.attr("src", "${pageContext.request.contextPath}/kops/jsp/auth/auth_sms.jsp");
			} else {
				alert("잘못된 인증방식입니다.");
			}
		}
		
		function successCallback(method, name, birth, gender, num) {
			var form = $("#authForm");
			form.find("#method").val(method);
			
			if(name != null) {
				form.find("#name").val(name);
			}
			
			if(birth != null) {
				form.find("#birth").val(birth);
			}
			
			if(gender != null) {
				form.find("#gender").val(gender);
			}
			
			if(num != null) {
				form.find("#num").val(num);
			}
			
			isAuthUser = true;

			if(isAuthUser){
				$.ajax({
		        	url:"${pageContext.request.contextPath}/user/userAuthCheck.face",
		        	type:'post',
		        	dataType:'json',
		        	data:$('#authForm').serialize(),
		        	success:function(json){
		        		console.log("인증성공");
		        	},
					error : function (json){
						console.log("인증실패");
					}
		    	});	
				
				alert("인증되었습니다.");
				
				setTimeout(function(){ 
					window.opener.document.body.disabled=false;
					self.close();
			    }, 3000);  
				
			}else{
				alert("잘못된 인증방식입니다. 인증서를 다시 한번 확인해주세요");
				setTimeout(function(){ 
					window.opener.document.body.disabled=false;
			    	opener.location.href ="/portal";
					self.close();
			    }, 3000);  
			}
		}
		
		
		function failCallback(rsMsg) {
			isAuthUser = false;
			alert(rsMsg);
		}

		if (window.attachEvent) {
		    /*IE and Opera*/

		    window.attachEvent("onunload", function() {
		    	window.opener.document.body.disabled=false;
		    	opener.location.href ="/portal";
				self.close();

		    });

		} else if (document.addEventListener) {

		    /*Chrome, FireFox*/

		    window.onbeforeunload = function() {
		    	window.opener.document.body.disabled=false;
		    	opener.location.href ="/portal";
				self.close();

		    };

		    /*IE 6, Mobile Safari, Chrome Mobile*/

		    window.addEventListener("unload", function() {
		    	window.opener.document.body.disabled=false;
		    	opener.location.href ="/portal";
				self.close();

		    }, false);

		} else {

		    /*etc*/

		    document.addEventListener("unload", function() {
		    	window.opener.document.body.disabled=false;
		    	opener.location.href ="/portal";
				self.close();

		    }, false);

		}

		
	</script>
		
</head>
<body>
<form id="authForm" name="authForm" method="post" action="/user/userAuthCheck.face">
	<!-- 인증받은 사용자 정보 -->
	<input type="hidden" name="method" id="method" /><!-- 인증방식 -->
	<input type="hidden" name="name" id="name" /><!-- 인증 받은 사용자 성명 -->
	<input type="hidden" name="birth" id="birth" /><!-- 인증 받은 사용자 생년월일 -->
	<input type="hidden" name="gender" id="gender" /><!-- 인증 받은 사용자 성별(여 : 0 / 남 : 1) -->
	<input type="hidden" name="num" id="num" /><!-- 인증 받은 사용자의 인증방식에 따른 고유 식별 번호 -->
</form>
<div id="popup_wrap">
	<div class="pop_title">
		<h1>본인 인증</h1>
		<a href="#n"><img src="../images/common/close_btn.gif" /></a>
	</div>
	<div class="pop_container">
		<p>본인인증이 필요한 업무입니다. 아래 <span class="c_red">인증수단중 하나를 선택</span>하여 인증해 주세요</p>
		<div class="certify_area">
			<dl class="certify1">
				<dt>공공 I-PIN</dt>
				<dd>
					<p>공공 아이핀(i-PIN)은 행정자치부에서 제공하는 주민등록번호 대체 수단으로 <br />인터넷에서 주민등록번호를 사용하지 않고도 본인임을 확인할 수 있는 서비스입니다.</p>
					<p><a href="javascript:authUser('ipin');" class="btn_wt">인증하기</a></p>
				</dd>
			</dl>
			<dl class="certify2 mgt20">
				<dt>공인인증서</dt>
				<dd>
					<p>공인인증서는 본인인증 대체 수단으로 인터넷에서 주민등록번호를 사용하지 않고도 본인임을 확인 할 수 있는 서비스입니다.</p>
					<p><a href="javascript:authUser('cert');" class="btn_wt">인증하기</a></p>
				</dd>
			</dl>
			<dl class="certify3 mgt20">
				<dt>휴대폰</dt>
				<dd>
					<p>휴대전화 본인인증은 본인인증 대체 수단으로 인터넷에서 주민등록번호를 사용하지 않고도 본인임을 확인 할 수 있는 서비스입니다.</p>
					<p><a href="javascript:authUser('sms');" class="btn_wt">인증하기</a></p>
				</dd>
			</dl>
		</div>
	</div>
</div>

<!-- 인증 호출 화면 -->
<iframe name="authUserFrame" id="authUserFrame" class="noshow" src=""></iframe>
		
</body>
</html>