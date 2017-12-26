<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-type" content="text/html" />  
		<meta http-equiv="Content-style-type" content="text/css" />
		<meta name="version" content="3.2.5" />
		<meta name="keywords" content="" />
		<meta name="description" content="" />
	
		<link rel="stylesheet" href="${pageContext.request.contextPath}/decorations/layout/tile/css/default.css" type="text/css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/decorations/layout/tile/css/login.css" type="text/css">
		<style type="text/css">
		#ipProtectHelp {width:600px;display: none;font-size: 12px; border: 5px solid white;}
		#ipProtectHelp table {width:100%;background-color: white; border: 2px ;}
		#ipProtectHelp table th {background-color: aqua;padding: 5px; border:5px solid white; font-size: 12px;font-weight: bold;text-align: center;}
		#ipProtectHelp table td {background-color: lime; padding: 5px; border :5px solid white;font-size: 12px;}
		#ipProtectHelp table td.C {text-align: cenetr}
		</style>
		
		<link type="text/css" href="${pageContext.request.contextPath}/decorations/layout/css/jquery/base/jquery.ui.all.css" rel="stylesheet" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery/jquery-1.10.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery/jquery-ui-1.9.2.custom.min.js"></script>
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/common_new.js"></script>
		<script type="text/javascript">
			<!-- iframe 포틀릿의 세션이 끊겼을 때 메인 화면이 로그인 화면으로 팅기게 해주는 로직 :: BEGIN  -->
			var iframes = window.parent.document.getElementsByTagName('iframe');
			if(iframes.length > 0) window.parent.location.href = "${pageContext.request.contextPath}/user/login.face";
			<!-- iframe 포틀릿의 세션이 끊겼을 때 메인 화면이 로그인 화면으로 팅기게 해주는 로직 :: END  -->
			<!-- 팝업창의 세션일 끊겼을 때 메인 화면이 로그인 화면으로 팅기게 해주는 로직 :: BEGIN  -->
			if(window.opener){
				window.opener.location.href = "${pageContext.request.contextPath}/user/login.face";
				self.close();
			}
			<!-- 팝업창의 세션일 끊겼을 때 메인 화면이 로그인 화면으로 팅기게 해주는 로직 :: END  -->
			
			function goJoinPage(){
				location.href="../user/ajaxJoin.face";
			}
		
			function goHelpPage(){
				location.href="../user/help.face";
			}
		
			function getMovieName(movieName) {
				if (navigator.appName.indexOf("Microsoft") != -1) {
					return document[movieName];
					//return window[movieName];
				}
				else {
					return document[movieName];
				}
			}
		
			function enterLogin(event){
				if(event.keyCode == 13){
					enviewLogin();
				}
			}
			
			function enviewLogin() {
				var form = document.getElementById("LoginForm");
				var id = form["userId"].value;
				var pass = form["password"].value;
				var isSaveLoginID = form["saveLoginID"].checked;
				
				var langKnd = form["langKnd"].value;
				if( id == null || id.length == 0 ) {
					alert( "사용자 ID를 입력하시기 바랍니다." );
					form["userId"].focus();
					return false;
				}
				else if( pass == null || pass.length == 0 ) {
					alert( "비밀번호를 입력하시기 바랍니다." );
					form["password"].focus();
					return false;
				}
				//alert("form.action=" + form.action + ", id=" + id + ", pass=" + pass + ", isSaveLoginID=" + isSaveLoginID);
				//form["password"].value = pass;
				try {
					var decodedPass = document["top_left_bg"].epPassEncode(pass);
					//alert("babo=["+babo+"]");
				} catch (e) {
					//alert("error=["+e+"]");
				}
				var ipProtect = $("#ipProtect").val(); 
				
				if( isSaveLoginID == true ) {
					var today = new Date();
					var expires = new Date();
					expires.setTime(today.getTime() + 1000*60*60*24*365);
					SetCookie('EnviewLoginID', id+";1" + ";" + ipProtect, expires, '/');
				}
				else {
					//DeleteCookie('EnviewLoginID', '/');
					var today = new Date();
					var expires = new Date();
					expires.setTime(today.getTime() + 1000*60*60*24*365);
					SetCookie('EnviewLoginID', ";0", expires, '/');
				}
				form["username"].value = id;
				form.submit();
				return false;
			}
		
			function invokeNextProcess() {
		
			}
		
			function iframe_autoresize(arg) {
				try {
					var ht = arg.contentWindow.document.documentElement.scrollHeight;
					arg.height = 0;
					arg.height = ht;
				} catch(e) {
					
				}
			}
			
			function initPosition() {
				var mt = window.innerHeight /2 - document.getElementById("wrap").scrollHeight/2;
				document.getElementById("wrap").style.marginTop=mt-5;
			}
			
			function refreshCaptcha(){
			    $("#captchaImage").attr("src", "/user/captcha.face?id=" + Math.random());
			}			
			
			function initEnview() {
				initPosition();
				
				var errorMessage = "<c:out value="${errorMessage}"/>";
				if( errorMessage != "null" && errorMessage.length > 0 ) {
					alert( errorMessage );
				}
			
				var form = document.getElementById("LoginForm");
				if( form ) {
					var saveId = GetCookie('EnviewLoginID');
					if( saveId ) {
						var saveIdArray = saveId.split(";");
						//var id = GetCookie('EnviewLoginID');
						//if( id ) {
						if( saveIdArray[0] ) {
							form["userId"].value = saveIdArray[0];
							//form["userId"].value = id;
							form["password"].focus();
						}
						else {
							form["userId"].focus();
						}
						
						if( saveIdArray[1] == "1" ) {
							form["saveLoginID"].checked = true;
						}
						else {
							form["saveLoginID"].checked = false;
						}
						if( saveIdArray.length>2) {
							$("#ipProtect").val( saveIdArray[2]);
						}
						
						form["langKnd"].value = "<c:out value="${langKnd}"/>";
					}
				}
			}
			
			// Attach to the onload event
			if (window.attachEvent)
			{
			    window.attachEvent ( "onload", initEnview );
			    window.attachEvent ( "onresize", initPosition );
			}
			else if (window.addEventListener )
			{
			    window.addEventListener ( "load", initEnview, false );
			    window.addEventListener ( "resize", initPosition, false );
			}
			else
			{
			    window.onload = initEnview;
			    window.onresize = initPoistion;
			}
			
		</script>
	</head>
	<body>
	<form id="LoginForm" name="LoginForm" method="POST" style="display:inline" action="${pageContext.request.contextPath}<c:out value="${loginUrl}"/>" onsubmit="return false">
		<div id="wrap">
			<div id="loginArea" style="background-color: white;height: auto;border-radius:5px">
				<h1><img src="${pageContext.request.contextPath}/decorations/layout/tile/images/login/logo.png" alt="enview Appliance 로고" /></h1>
				<fieldset>
					<legend>로그인영역</legend>
					<input type="hidden" name="username" >
					<p class="uid"><label for="userid"><span class="user"></span><input name="userId" type="text" id="userid" class="userid" value="" /></label></p>
					<p class="uid"><label for="userpw"><span class="password"></span><input name="password" type="password" id="userpw" class="userpw" value="" /></label> </p>
					<%--
					<select name="langKnd">
						<option value="">** Language **</option>
						<option value="en">English</option>
						<option value="ko">Korean</option>
					</select>
					 --%>
					<p style="height:40px;"></p>
					<c:if test="${useCaptcha=='true'}">
					<!--  보안문자 -->
					<p style="margin-bottom: 10px"><img id="captchaImage" src="/user/captcha.face" height="45"> <a onclick="refreshCaptcha()" id="refreshBtn">새로고침</a></p>
					<p class="uid"><label for="capcha"><span class="password"></span><input name="answer" type="text" id="userpw" class="userpw" value="" placeholder="보안문자"/></label> </p>
					<!-- //보안문자 -->
					</c:if>		
					<p style="margin-top:5px;height: 20px;vertical-align: middle">
					<input type="hidden" name="langKnd" value="ko"/>
					<input type="checkbox" class="" name="saveLoginID" checked value="checkbox" tabindex="3"><util:message langKnd="en" key="ev.title.user.SaveID"/>
					<span style="float:right;">
					<a href="#" onclick="$('#ipProtectHelp').dialog({width:600});"> IP보안 </a> :
					<select name="ipProtect" id="ipProtect">
					<option value="0">없음</option> 
					<option value="1" selected="selected">1단계</option> 
					<option value="2">2단계</option> 
					<option value="3">3단계</option> 
					</select>
					</span>
					<%--
					<input type="radio" name="ipProtect" value="0">없음
					<input type="radio" name="ipProtect" value="1" checked="checked">1단계
					<input type="radio" name="ipProtect" value="2">2단계
					<input type="radio" name="ipProtect" value="3">3단계
					 --%>
					</p>
					<p class="ubtn"><input type="image" name="btn_login" id="btn_login" onclick="javascript:enviewLogin()" src="${pageContext.request.contextPath}/decorations/layout/tile/images/login/login_btn.gif" alt="로그인" /></p>
				</fieldset>
			</div>
			<!-- loginArea //-->
		</div>
	</form>
	<div id="ipProtectHelp" title="IP보안">
		<div>
			<b>IP보안이란?</b>
		</div>
		<div>IP주소정보의 사용범위를 사용자의 인터넷 접속환경에 맞게 3단계로 설정할 수 있도록 하여 타인이 로그인
			권한을 가로채어 부정하게 사용하는 것을 방지하는, 사용성 및 보안성이 강화된 포탈 로그인상태 관리서비스입니다.</div>
		<table>
		<tr>
		<th width="80px">단계</th>
		<th>사용단계</th>
		<th>보안효과</th>
		</tr>
		<tr>
		<th>1단계</th>
		<td>유동 IP를 사용하는 일반 인터넷 사용자 (권장수준)</td>
		<td>로그인 후 IP주소가 최근 로그인 시 IP주소들과 일정범위(C클래스)까지 동일한 경우만 허용.<br>
			멀리 떨어진 장소에서 로그인 권한을 부정사용 하는 것을 차단</td>
		</tr>		
		<tr>
		<th>2단계</th>
		<td>유동 IP의 일반 인터넷에서 높은 수준의 IP보안을 원하는 사용자</td>
		<td> 로그인 후 IP주소가 최근 로그인 시 IP주소들과 동일한 경우만 허용<br>
		 인터넷 접속중 IP주소 변경 시 사용자 재확인을 최소화하고, 3단계와 동일한 보안수준을 제공</td>
		</tr>		
		<tr>
		<th>3단계</th>
		<td>인터넷 접속 중 IP주소가 변경되지 않는 사용자</td>
		<td>로그인 후 IP주소가 변경되지 않는 경우만 허용.<br>
		IP주소정보를 사용한 최고의 보안수준을 제공</td>
		</tr>		
		<tr>
		<th>사용안함</th>
		<td>로그인 상태에서 IP주소가 자주 변경되는 사용자의 경우 선택.</td>
		<td>없음. 보안에 취약.</td>
		</tr>
		</table>		
	</div>
	<!-- wrap //-->
</body>
</html>