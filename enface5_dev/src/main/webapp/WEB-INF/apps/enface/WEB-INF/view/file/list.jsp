<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.saltware.enview.Enview"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

		<link rel="stylesheet" type="text/css" href="${cPath}/decorations/layout/css/jquery/base/jquery.ui.all.css" />
		<link rel="stylesheet" type="text/css" href="${cPath}/dhtmlx/dhtmlx.css" />

		<script type="text/javascript" src="${cPath}/javascript/jquery/jquery-1.10.2.min.js"></script>
		<script type="text/javascript" src="${cPath}/javascript/jquery/jquery-ui-1.9.2.custom.min.js"></script>
		<script type="text/javascript" src="${cPath}/javascript/jquery/ui/i18n/jquery.ui.datepicker-${langKnd}.js"></script>
		
		<script type="text/javascript" src="${cPath}/javascript/message/mrbun_${langKnd}_mm.js"></script>
		<script type="text/javascript" src="${cPath}/javascript/message/mrbun_${langKnd}_ev.js"></script>
		<script type="text/javascript" src="${cPath}/javascript/message/mrbun_${langKnd}_eb.js"></script>
		
		<script type="text/javascript" src="${cPath}/javascript/portal_new.js"></script>
		<script type="text/javascript" src="${cPath}/javascript/common_new.js"></script>
		<script type="text/javascript" src="${cPath}/javascript/utility.js"></script>
		<script type="text/javascript" src="${cPath}/javascript/validator.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/dhtmlx/dhtmlx.js"></script>
		

		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/dhtmlx/vault/dhtmlxvault.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/dhtmlx/vault/dhtmlxvault.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/admin/javascript/fileManager2.js"></script>


<script type="text/javascript">
	function initFileManager(){
		if( aFileManager == null ) {
			aFileManager = new FileManager("<c:out value="${evSecurityCode}"/>");
			aFileManager.init();
			aFileManager.doDefaultSelect();
		}
	}
	
	function finishFileManager(){
		
	}
	
	// Attach to the onload event
	if (window.attachEvent){
	    window.attachEvent ( "onload", initFileManager );
		window.attachEvent ( "onunload", finishFileManager );
	}
	else if (window.addEventListener ){
	    window.addEventListener ( "load", initFileManager, false );
		window.addEventListener ( "unload", finishFileManager, false );
	}
	else{
	    window.onload = initFileManager;
		window.onunload = finishFileManager;
	}
</script>

<!-- sub_contents -->
<div class="sub_contents">
	
	<!-- detail -->
	<div class="detail">
		<!-- board -->
		<div class="board first">
			
			<!-- btnArea-->
			<div class="btnArea">
				<div class="rightArea">
			    	<a href="javascript:aFileManager.getFileUploader().doShow();" class="btn_W"><span><util:message key='ev.title.upload'/></span></a>
			    	<a href="javascript:aFileManager.doRemove();" class="btn_W"><span><util:message key='ev.title.remove'/></span></a>
				</div>
			</div>
			<!-- btnArea//-->												
		</div>
		<!-- board first// -->
			
	</div>
	<!-- detail// -->
</div>
<!-- sub_contents// -->

<input type="hidden" id="uploadDir" value="/"/>
<div id="FileManager_FileUploader" title="File Uploader" ></div>
<div id="dhtmlx_context_data">
	<div id="onRefresh" text="<util:message key="ev.info.menu.refresh" />" img="${cPath }/admin/images/Service.gif"></div>
	<div id="onCreate" text="<util:message key="ev.info.menu.addPage" />" img="${cPath }/admin/images/ic_make_page.gif"></div>
	<div id="onMoveUp" text="<util:message key="ev.info.menu.moveUp" />" img="${cPath }/admin/images/ic_up.gif"></div>
	<div id="onMoveDown" text="<util:message key="ev.info.menu.moveDown" />" img="${cPath }/admin/images/ic_down.gif"></div>
	<div id="onUpload" text="<util:message key="ev.info.menu.upload" />" img="${cPath }/admin/images/createsmall.gif"></div>
</div>