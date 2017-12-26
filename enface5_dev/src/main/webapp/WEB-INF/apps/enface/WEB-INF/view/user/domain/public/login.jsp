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
	
		<link rel="stylesheet" href="${pageContext.request.contextPath}/face/css/styles.css" type="text/css">
		
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
				form.action += "?langKnd=" + langKnd
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
			
			function initEnview() {
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
			}
			else if (window.addEventListener )
			{
			    window.addEventListener ( "load", initEnview, false );
			}
			else
			{
			    window.onload = initEnview;
			}
			
			
		</script>
	</head>
	<body marginwidth="0" marginheight="0" class="#PageBaseCSSClass()" style="overflow: auto;">
		<div class="login-content">
			<div class="login-bg" style="background-image: url('${pageContext.request.contextPath}/face/images/user/login_bg_01.jpg'); ">
				<div class="login_form">
					<form id="LoginForm" name="LoginForm" method="POST" style="display:inline" action="${pageContext.request.contextPath}<c:out value="${loginUrl}"/>">
						<input type="hidden" name="_enpass_login_" value="submit">
						<table width="350" border="0" cellspacing="0" cellpadding="0" style="margin:0;">
							<tr>
								<td colspan="4" align="center" class="msg-alert"><B></B></td>
							</tr>
	 						<tr>
								<td width="50" height="23" align="right">&nbsp;</td> 
								<td width="80" height="23"><img src="${pageContext.request.contextPath}/face/images/user/id.gif" width="94" height="15"></td>
								<td width="90" height="23"> 
									<input type="hidden" name="username" >
									<input type="text" name="userId" size="15" class="login-input" tabindex="1">
								</td>
								<td width="120" align="center">
									<input type="checkbox" name="saveLoginID" checked value="checkbox" tabindex="3"><util:message langKnd="en" key="ev.title.user.SaveID"/>
								</td>
							</tr>
							<tr> 
								<td width="50" height="23" align="right">&nbsp;</td>
								<td width="80" height="23"><img src="${pageContext.request.contextPath}/face/images/user/password.gif" width="94" height="15"></td>
								<td width="90" height="23"> 
									<input type="password" name="password" size="15" class="login-input" tabindex="2" onkeyup="enterLogin(event)">
								</td>
								<td align="center">
									<span class="btn_pack small" style="cursor:pointer"><a href="#" onclick="javascript:enviewLogin()"><util:message langKnd="en" key="ev.title.user.Login"/></a></span>
								</td>
							</tr>
							<tr> 
								<td width="50" height="23" align="right">&nbsp;</td>
								<td width="80" height="23">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<util:message langKnd="en" key="ev.prop.lang.langKnd"/></td>
								<td width="90" height="23"> 
									<select name="langKnd" class='webdropdownlist'>
										<option value="">** Language **</option>
										<option value="ar">Arabic</option>
										<option value="de">German</option>
										<option value="en">English</option>
										<option value="es">Spanish</option>
										<option value="fa">Persian</option>
										<option value="fr">French</option>
										<option value="hu">Hungarian</option>
										<option value="it">Italian</option>
										<option value="ja">Japanese</option>
										<option value="km">Cambodian</option>
										<option value="ko">Korean</option>
										<option value="nl">Dutch</option>
										<option value="pl">Polish</option>
										<option value="pt">Portuguese</option>
										<option value="ro">Romanian</option>
										<option value="ru">Russian</option>
										<option value="sv">Swedish</option>
										<option value="vn">Vietnam</option>
										<option value="zh">Simplified Chinese</option>
										<option value="zh_TW">Traditional Chinese</option>
									</select>
								</td>
								<td align="center">
									<span class="btn_pack small" style="cursor:pointer"><a href="javascript:goJoinPage()"><util:message langKnd="en" key="ev.title.user.Join"/></a></span>
									<span class="btn_pack small" style="cursor:pointer"><a href="javascript:goHelpPage()"><util:message langKnd="en" key="ev.title.user.Help"/></a></span>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
			<div class="login-copyright" align="center">
				<img src="${pageContext.request.contextPath}/face/images/user/copyright.gif">
			</div>
			<div><!-- BEGIN : flash module for password encoding -->
				<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0" width="1" height="1" id="top_left_bg" align="middle">
					<param name="allowScriptAccess" value="always" />
					<param name="movie" value="${pageContext.request.contextPath}/face/images/user/top_left_bg.swf" />
					<embed src="${pageContext.request.contextPath}/face/images/user/top_left_bg.swf" width="1" height="1" name="top_left_bg" align="middle" allowScriptAccess="always" swLiveConnect="true"  type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
				</object>
			</div><!-- END : flash module for password encoding -->
		</div>
	</body>
</html>