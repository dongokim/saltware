<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld"%>
<html>
	<head>
		<title>파일 업로드 폼</title>
		<meta http-equiv="content-type" content="text/html; charset=utf=8">
		<%-- <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/admin/css/styles.css"> --%>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/admin/css/contents.css" />
		<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery/jquery-1.10.2.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/admin/javascript/fileManager.js"></script>
	</head>
	<body>
		<form name="formName" action="<%=request.getContextPath()%>/file/uploadProgress.admin" method="post">
			<table cellpadding="0" cellspacing="0" summary="게시판" class="table_board">
		 		<caption>게시판</caption>
				<colgroup>
					<col width="20%" />
					<col width="*" />
				</colgroup>				
				<tr>
					<th class="L">
						<util:message key='ev.title.upload.path' />
					</th>
					<td class="L" colspan="3">
						<input type="text" id="dirPath" name="dirPaths" value="<c:out value="${uploadDir}"/>" style="width: 300px; border: 1px solid #999999;">
					</td>
				</tr>
				<tr>
					<th class="L">
						<util:message key='ev.title.upload.duplicate' />
					</th>
					<td class="L" colspan="3">
						<input type="radio" id="pass" name="uploadOption" value="pass" checked="checked"><label for="pass" style="cursor:pointer;"><util:message key='ev.title.upload.pass' /></label>&nbsp;<input type="radio" id="overwrite" name="uploadOption" value="overwrite" class="webradiogroup"><label for="overwrite" style="cursor:pointer;"><util:message key='ev.title.upload.overwrite' /></label>&nbsp;<input type="radio" id="changeName" name="uploadOption" value="changeName"><label for="changeName" style="cursor:pointer;"><util:message key='ev.title.upload.change' /></label>
					</td>
				</tr>
				<tr>
					<th class="L">
						<util:message key='ev.title.upload.uploadlist' />
					</td>
					<td id="uploadFlash" colspan="3" class="L"></td>
				</tr>
			</table>
			<input type="hidden" id="seesionId" value="<%=session.getId() %>" />
			<input type="hidden" id="swfUrl" value="<%=request.getContextPath()%>/admin/upload" />
		</form>
		<!-- btnArea-->
		<div class="btnArea">
			<div class="rightArea">
				<a href="javascript:aFileUploader.doApply();" class="btn_W"><span><util:message key='ev.title.upload.send' /></span></a>
				<a href="javascript:aFlashController.callUploadclose()" class="btn_W"><span><util:message key='ev.title.close'/></span></a>
			</div>
		</div>
		<!-- btnArea//-->		
	</body>
</html>