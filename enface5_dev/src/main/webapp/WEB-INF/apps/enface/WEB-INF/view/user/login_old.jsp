<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<!--%@ taglib prefix="util" uri="/tld/utility.tld" %-->


<html>
  <head>
    <title></title>
    <meta http-equiv="Content-type" content="text/html" />  
    <meta http-equiv="Content-style-type" content="text/css" />
    <meta name="version" content="3.2.5" />
    <meta name="keywords" content="" />
    <meta name="description" content="" />
	
	<link rel="stylesheet" href="<%=request.getContextPath()%>/face/css/styles.css" type="text/css">
	
	<script language="JavaScript" src="<%=request.getContextPath()%>/javascript/common.js"></script>
	<script language="JavaScript">

		function goJoinPage(){
			location.href="../user/ajaxJoin.face";
		}

		function goHelpPage(){
			location.href="../user/help.face";
		}
	
		function getMovieName(movieName) {
	      if (navigator.appName.indexOf("Microsoft") != -1) {
	        return window[movieName];
	      }
	      else {
	        return document[movieName];
	      }
	    }

		function enviewLogin() {
			var form = document.getElementById("LoginForm");
			var id = form["userId"].value;
			var pass = form["password"].value;
			var isSaveLoginID = form["saveLoginID"].checked;
			
			if( id==null || id.length==0 ) {
				alert( "사용자 ID를 입력하시기 바랍니다." );
				form["userId"].focus();
				return false;
			}
			else if( pass==null || pass.length==0 ) {
				alert( "비밀번호를 입력하시기 바랍니다." );
				form["password"].focus();
				return false;
			}
			//alert("form.action=" + form.action + ", id=" + id + ", pass=" + pass + ", isSaveLoginID=" + isSaveLoginID);
			form["password"].value = pass; //getMovieName("top_left_bg").epPassEncode(pass); 
			
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

  <body marginwidth="0" marginheight="0" class="#PageBaseCSSClass()">
	
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
  <tr>
    <td align="center"> 
	  <table width="700" border="0" cellspacing="0" cellpadding="0">
		<tr>
          <td width="8"></td>
          <td >
          <!-- BEGIN : flash module for password encoding -->
	          <div>
	          <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0" width="1" height="1" id="top_left_bg" align="middle">
              <param name="allowScriptAccess" value="always" />
              <param name="movie" value="<%=request.getContextPath()%>/face/images/user/top_left_bg.swf" />
              <embed src="<%=request.getContextPath()%>/face/images/user/top_left_bg.swf" width="1" height="1" name="top_left_bg" align="middle" allowScriptAccess="always" swLiveConnect="true"  type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
            </object>
            </div>	
	      <!-- END : flash module for password encoding --></td>
          <td width="8"></td>
        </tr>
	  </table>
      <table width="572" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td><img src="<%=request.getContextPath()%>/face/images/user/form_01.gif" width="571" height="18"></td>
        </tr>
        <tr> 
          <td> 
            <table width="571" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td width="19"><img src="<%=request.getContextPath()%>/face/images/user/form_02.gif" width="19" height="266"></td> 
                <td width="533" valign="bottom" background="<%=request.getContextPath()%>/face/images/user/theme_bg_01.gif"> 
				  <form id="LoginForm" name="LoginForm" method="POST" style="display:inline" action="<c:out value="${loginUrl}"/>" onsubmit="javascript:return enviewLogin();">
					<input type="hidden" name="_enpass_login_" value="submit">
                    <table width="350" border="0" cellspacing="0" cellpadding="0" style="margin:0;">
					  <tr>
						<td colspan="4" align="center" class="msg-alert"><B>

						</B></td>
					  </tr>
                      <tr> 
                        <td width="50" height="23" align="right">&nbsp;</td> 
                        <td width="80" height="23"><img src="<%=request.getContextPath()%>/face/images/user/id.gif" width="94" height="15"></td>
                        <td width="90" height="23"> 
                          <input type="text" name="userId" size="15" class="login-input" tabindex="1">
                        </td>
						<td width="120" align="center"><input type="checkbox" name="saveLoginID" checked value="checkbox" tabindex="3"><util:message key="ev.title.user.SaveID"/></td>
                        <!--td rowspan="2" height="60"><img src="<%=request.getContextPath()%>/face/images/user/bu_login_blue.gif" width="73" height="56"></td-->
                      </tr>
                      <tr> 
                        <td width="50" height="23" align="right">&nbsp;</td>
                        <td width="80" height="23"><img src="<%=request.getContextPath()%>/face/images/user/password.gif" width="94" height="15"></td>
                        <td width="90" height="23"> 
                          <input type="password" name="password" size="15" class="login-input" tabindex="2">
                        </td>
						<td align="center"><input type="image" src="<%=request.getContextPath()%>/face/images/user/btn_login.gif" style="cursor:hand" tabindex="4"/></td>
                      </tr>
					  <tr> 
                        <td width="50" height="23" align="right">&nbsp;</td>
                        <td width="80" height="23">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<util:message key="ev.property.langKnd"/></td>
                        <td width="90" height="23"> 
							<select name="langKnd" class='webdropdownlist'>
								<c:forEach items="${langKndList}" var="codebase">
								<option value="<c:out value="${codebase.code}"/>"><c:out value="${codebase.codeName}"/></option>
								</c:forEach>
							</select>
                        </td>
						<td align="center">
							<input type="button" value="Join" style="cursor:hand" tabindex="4" onclick="goJoinPage()"/><input type="button" value="Help" style="cursor:hand" tabindex="4" onclick="goHelpPage()"/>
						</td>
						
                      </tr>
					  <tr>
						<td colspan="4">&nbsp;</td>
					  </tr>
                    </table>
                  </form>
                </td>
                <td width="19"><img src="<%=request.getContextPath()%>/face/images/user/form_03.gif" width="19" height="266"></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr> 
          <td><img src="<%=request.getContextPath()%>/face/images/user/form_04.gif" width="571" height="19"></td>
        </tr>
        <tr>
          <td align="center" height="50"><img src="<%=request.getContextPath()%>/face/images/user/copyright.gif" width="402" height="38"></td>
        </tr>
      </table>
      <p class="b2c_price">&nbsp;</p>
    </td>
  </tr>
</table>

</body>
</html>