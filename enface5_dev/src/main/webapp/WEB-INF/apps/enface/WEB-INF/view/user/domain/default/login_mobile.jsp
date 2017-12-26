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
		<meta name="viewport" content="user-scalable=no, width=device-width" />
	
		<link rel="stylesheet" href="${pageContext.request.contextPath}/decorations/layout/tile/css/default.css" type="text/css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/decorations/layout/tile/css/login.css" type="text/css">
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
				
				if( isSaveLoginID == true ) {
					var today = new Date();
					var expires = new Date();
					expires.setTime(today.getTime() + 1000*60*60*24*365);
					SetCookie('EnviewLoginID', id+";1", expires, '/');
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
			
			function initEnview() {
				initPosition();
				
				var errorMessage = "<c:out value="${errorMessage}"/>";
				if( errorMessage != "null" && errorMessage.length > 0 ) {
					alert( errorMessage );
				}
			
				var form = document.getElementById("LoginForm");
				if( form ) {
					var userinfo = GetCookie('EnviewLoginID');
					if( userinfo ) {
						var userinfoArray = userinfo.split(";");
						//var id = GetCookie('EnviewLoginID');
						//if( id ) {
						if( userinfoArray[0] ) {
							form["userId"].value = userinfoArray[0];
							//form["userId"].value = id;
							form["password"].focus();
						}
						else {
							form["userId"].focus();
						}
						
						if( userinfoArray[1] == "1" ) {
							form["saveLoginID"].checked = true;
						}
						else {
							form["saveLoginID"].checked = false;
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
		<div id="wrap" class="mobile">
			<div id="loginArea" class="mobile">
				<h1><img src="${pageContext.request.contextPath}/decorations/layout/tile/images/login/logo.png" alt="enview Appliance 로고" /></h1>
				<fieldset>
					<legend>로그인영역</legend>
					<form id="LoginForm" name="LoginForm" method="POST" style="display:inline" action="${pageContext.request.contextPath}<c:out value="${loginUrl}"/>">
						<input type="hidden" name="username" >
						<p class="uid"><label for="userid"><span class="user"></span><input name="userId" type="text" id="userid" class="userid" value="" placeholder="<util:meaage key='mm.title.id'/>"/></label></p>
						<p class="upw"><label for="userpw"><span class="password"></span><input name="password" type="password" id="userpw" class="userpw" value="" placeholder="<util:meaage key='ev.title.user.Password'/>" /></label> </p>
						<input type="checkbox" class="saveLoginID" name="saveLoginID" checked value="checkbox" tabindex="3"><util:message langKnd="en" key="ev.title.user.SaveID"/>
						<p class="ubtn"><input type="image" name="btn_login" id="btn_login" onclick="javascript:enviewLogin()" src="${pageContext.request.contextPath}/decorations/layout/tile/images/login/login_btn.gif" alt="로그인" /></p>
						<select name="langKnd" class='webdropdownlist' style="display: none;">
							<option value="">** Language **</option>
							<option value="en">English</option>
							<option value="ko">Korean</option>
						</select>
					</form>
				</fieldset>
			</div>
		</div>
	</body>
</html>