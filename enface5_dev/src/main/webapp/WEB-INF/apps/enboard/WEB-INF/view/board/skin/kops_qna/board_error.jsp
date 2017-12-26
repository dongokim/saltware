<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String cmd = request.getParameter("cmd");
	String boardId = request.getParameter("boardId");
	String bltnNo = request.getParameter("bltnNo");
%>

<link rel="stylesheet" href="${cPath}/snu/css/reset.css"/>
<link rel="stylesheet" href="${cPath}/snu/css/common.css"/>
<link rel="stylesheet" href="${cPath}/snu/css/layout.css"/>
<link rel="stylesheet" href="${cPath}/snu/css/jquery-ui.css"/>
<script src="${cPath}/snu/js/jquery.min.js" type="text/javascript" ></script>
<script src="${cPath}/snu/js/slide.js" type="text/javascript" ></script>
<script src="${cPath}/snu/js/js.js" type="text/javascript" ></script>
<script src="${cPath}/snu/js/jquery-ui.js" type="text/javascript" ></script>

<script type="text/javascript">
	function goMain(){
		var location = document.location.protocol +"//"+ document.location.host;
		document.location.href=location;
	}
</script>

	<!-- wrap -->
	<div id="wrapper">
		<div id="container">
			<div class="errow_wrap">
				<strong>작업오류(Error)</strong>
				<c:if test="${sessionScope.langKnd=='ko'}">
					<p class="errorcode">요청하신 작업이 정상처리 되지 않았습니다.<br/>
				 	잠시 후 다시 실행하시거나 <a style="cursor:pointer; color:blue;" onclick="javascript:window.open('http://www.google.com/recaptcha/mailhide/d?k\07501ruHVbSuY-Qgte0nsyUt3ew\75\75\46c\0750Ke5YKHkXsR3rY2UcmX80ITLv_r5YoTukePuY1TwXDA\075', '', 'toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=0,width=500,height=300'); return false;">관리자에게 문의</a>하시기 바랍니다.<br/>
		                                시스템 이용에 불편을 드려 대단히 죄송합니다.<br/>
		            </p>
		            <a onclick="javascript:goMain();" class="btn_main" style="cursor:pointer">메인으로 가기</a>
				</c:if>
				<c:if test="${sessionScope.langKnd=='en'}">
					<p class="errorcode">The requested operation is not processed normally.<br/>
		            Run again later or <a style="cursor:pointer; color:blue;" onclick="javascript:window.open('http://www.google.com/recaptcha/mailhide/d?k\07501ruHVbSuY-Qgte0nsyUt3ew\75\75\46c\0750Ke5YKHkXsR3rY2UcmX80ITLv_r5YoTukePuY1TwXDA\075', '', 'toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=0,width=500,height=300'); return false;">contact your administrator.</a><br/> 
		            System use very sorry for the inconvenience.<br/>
		            </p>
		            <a onclick="javascript:goMain();" class="btn_main" style="cursor:pointer">Main</a>
				</c:if>
			</div>
		</div>
	</div>
