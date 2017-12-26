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
		
		
		<link href="<%=request.getContextPath()%>/face/css/common_styles.css" rel="stylesheet" type="text/css">
		<link href="<%=request.getContextPath()%>/face/css/mobile_styles.css" rel="stylesheet" type="text/css">
		
		<link rel="stylesheet" href="http://code.jquery.com/mobile/1.0/jquery.mobile-1.0.min.css"/> 
		<script src="http://code.jquery.com/jquery-1.6.4.min.js"></script> 
		<script src="http://code.jquery.com/mobile/1.0/jquery.mobile-1.0.min.js"></script> 
		<script src="<%=request.getContextPath()%>/talk/util/srt-0.9.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common_new.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				$.mobile.ajaxEnabled = false;
				
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
							form["_spring_security_remember_me"].checked = true;
						}
						else {
							form["_spring_security_remember_me"].checked = false;
						}
					}
				}
				
				document.addEventListener("deviceready", onDeviceReady, false);
			
				$("#LoginForm").submit( function() {
					var form = document.getElementById("LoginForm");
					var id = form["userId"].value;
					var pass = form["password"].value;
					var isSaveLoginID = form["_spring_security_remember_me"].checked;
					
					//var langKnd = form["langKnd"].value;
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
					form.action = "<c:out value="${loginUrl}"/>";
					//alert(form.action + "," + form["destination"].value); 
					form.submit();
					return false;
				});
			});
			
			function onDeviceReady(){
				console.log("deviceready");
				
				var deviceID = srt.exec(null, null, 'MacAddress', 'getDeviceID', []);
				$("#_device_id_").val( deviceID );
				alert("deviceID=" + $("#_device_id_").val());

				/*
				//use some deviceAPIs
				if(navigator.devicestatus.isSupported('Device','imei')){
					alert("devicestatus Supported");
					navigator.devicestatus.getPropertyValue(onValueRetrievedSuccessCB ,errorCB ,{aspect:"Device", property:"imei"});
				}
				//navigator.devicestatus.getPropertyValue(onValueRetrievedSuccessCB, errorCB, {property:"Device", aspect:"imei"});
				alert("device call");
				*/
			}

			function onValueRetrievedSuccessCB(prop,value) {
			   alert("The" + prop.property + " is " + value);
			}

			function errorCB(error){
			   alert("An error occurred " + error.message);
			}
		</script>
	</head>
	<body marginwidth="0" marginheight="0" class="#PageBaseCSSClass()" style="overflow: auto;">
		<div data-role="page" > 
			<form id="LoginForm" name="LoginForm" method="POST" style="display:inline"  action="">
			<div data-role="header">
				<h1>포털 로그인</h1>
			</div> 
			<div data-role="content">
					<input type="hidden" name="_enpass_login_" value="submit">
					<input type="hidden" name="username" >
					<input type="hidden" name="destination" value="/talk/index.html">
					<input type="hidden" id="_device_id_" name="_device_id_" value="">
					<label for="userId" class="ui-hidden-accessible"></label>
					<input type="text" name="userId" id="userId" placeholder="아이디 입력:" value="">
					<label for="password" class="ui-hidden-accessible"></label>
					<input type="password" name="password" id="password" placeholder="비밀번호 입력:" value="" autocomplete="off">
	
					<fieldset data-role="controlgroup">
						<input type="checkbox" name="_spring_security_remember_me" id="remember_me" checked="true">
						<label for="remember_me">Remember Me</label>
					</fieldset>
			</div> 
			<div data-role="footer">
				<div data-role="controlgroup" data-type="horizontal" data-mini="true">
					<a href="#" data-role="button" data-icon="plus" data-theme="b">Add</a>
					<a href="#" data-role="button" data-icon="delete" data-theme="b">Delete</a>
					<a href="#" data-role="button" data-icon="grid" data-theme="b">More</a>
				</div>

				<input type="submit" value="Login" data-icon="grid" data-iconpos="right" data-mini="true" data-theme="e">
				<a href="#" data-role="button">Help</a>
			</div> 
			</form>
		</div> 
		
	</body>
</html>