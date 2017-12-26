<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.saltware.enview.Enview" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<%
	String boardId  = (String)request.getAttribute("boardId");
	String fileName = (String)request.getAttribute("fileName");
	String fileMask = (String)request.getAttribute("fileMask");
	String fileSize = (String)request.getAttribute("fileSize");
	String mode     = (String)request.getAttribute("mode");
	String jalign   = (String)request.getAttribute("jalign");
	String jvalign  = (String)request.getAttribute("jvalign");
	String fwidth   = (String)request.getAttribute("fwidth");
	String fheight  = (String)request.getAttribute("fheight");
	String seq      = (String)request.getAttribute("seq");
	String message  = (String)request.getAttribute("message");
	
	String cafe     = request.getParameter("cafe");
%>

<%
if (message != null) { // UploadMngr 에서 Exception 을 catch 한 경우.
%>
<script language="javascript">
<!--
	alert('<%=message%>');
<%	if ("editor".equals(mode)) { %>
	window.close();
<%	} else { %>
	parent.ebEdit.closeUpload(); // Progress bar(id='uploading') 의 style 을 'none' 으로 바꾼다.
<%	} %>
//-->
</script>
<%
} else { // UploadMngr 이 Exception 없이 잘 종료되었다.
	if ("icon".equals(mode)) {
%>

<script language="javascript">
<!--
	if ("<%=fileMask%>" == "" || "<%=fileMask%>" == "null")
		alert('<eb:message bundle="lcBun" key="attach.upload.fail"/>');

	window.opener.pasteImage("<%=fileMask%>");
	window.close();
//-->
</script>

<%
	} else if ("photo".equals(mode)) {
%>

<script language="javascript">
<!--
	if ("<%=fileMask%>" == "" || "<%=fileMask%>" == "null")
		alert('<eb:message bundle="lcBun" key="attach.upload.fail"/>');

	window.opener.pastePhoto("<%=fileMask%>");
	window.close();
//-->
</script>


<%
	} else if ("attach".equals(mode) || "editor".equals(mode) || "flash".equals(mode)) {
		if ("1".equals( Enview.getConfiguration().getString("board.upload.encode")))
			fileName = new String(fileName.getBytes("EUC-KR"),"8859_1");
		else if ("2".equals( Enview.getConfiguration().getString("board.upload.encode")))
			fileName = new String(fileName.getBytes("8859_1"),"KSC5601");
		else if ("3".equals( Enview.getConfiguration().getString("board.upload.encode")))
			fileName = new String(fileName.getBytes("KSC5601"),"8859_1");
	
		if ("editor".equals(mode) || "flash".equals(mode)) {
%>
<script language="javascript">
<!--
	if ("<%=fileMask%>" == "" || "<%=fileMask%>" == "null")
		alert('<util:message key="eb.error.upload.atch.file"/>');

	var mode = '<%=mode%>';
	var cafe = '<%=cafe%>';
	if (mode == 'editor') {
		if (cafe == 'gate') {
			parent.uploadCafeGateImage("<%=fileName%>", "<%=fileMask%>", <%=fileSize%>, "<%=jalign%>","<%=jvalign%>");
		} else {
			parent.uploadImage("<%=fileName%>", "<%=fileMask%>", <%=fileSize%>, "<%=jalign%>","<%=jvalign%>");
		}
	} else if (mode == 'flash') {
		if (cafe == 'gate') {
			parent.uploadCafeGateFlash("<%=fileName%>", "<%=fileMask%>", <%=fileSize%>, "<%=fwidth%>","<%=fheight%>");
		} else {
			parent.uploadFlash("<%=fileName%>", "<%=fileMask%>", <%=fileSize%>, "<%=fwidth%>","<%=fheight%>");
		}
	}
//-->
</script>
<%		
		} else if ("attach".equals(mode)) { // 첨부파일을 업로드 하는 경우.
%>
<script language="javascript">
<!--
	parent.ebEdit.closeUpload();

	if ("<%=fileMask%>" == "" || "<%=fileMask%>" == "null")
		alert('<util:message key="eb.error.upload.atch.file"/>');
	else {
		<%--
		  1. 전체 파일 크기에 방금 업로드된 파일의 크기를 더해주고,
		  2. 전체 파일 크기의 단위(B,KB,MB)를 정하고,
		  3. 파일 목록을 보여주는 'select' boxt에 방금 업로드된 파일을 추가하고,
		  4. iframe 의 location.href 를 blank.brd 로 바꾸어버린다.
		--%>
		var filesize = <%=fileSize%>;
		var totalsize = eval("parent.document.setUpload.totalsize.value");
		totalsize = eval(totalsize) + eval(filesize);

		var unit = "";
		var calsize = "";
    	if ((totalsize > 1024) && (totalsize < 1024 * 1024)) {
			unit = " KB";
    		calsize = (totalsize/1024)+"";
		} else if (totalsize >= 1024 * 1024) {
			unit = " MB";
			calsize = (totalsize/(1024*1024))+"";
		} else {
			unit = " Bytes";
    		calsize = totalsize+"";
		}

		if (calsize.indexOf(".") > -1) {
			var sosu = calsize.substring(calsize.indexOf("."), calsize.length);

			if (sosu.length > 4)
				calsize = calsize.substring(0,calsize.indexOf("."))+sosu.substring(0,4);
		}

		parent.document.setUpload.totalsize.value = totalsize;
		parent.document.setUpload.viewsize.value = '<util:message key="eb.title.total.file.size"/>: '+calsize+unit;

		var val = "<%=fileMask%>-<%=fileSize%>";
		var fsize = eval(filesize); var funit = "";
		if ((fsize > 1024) && (fsize < 1024 * 1024)) {
			funit = " KB";
			fsize = fsize/1024 + "";
		} else if (fsize >= 1024 * 1024) {
			funit = " MB";
			fsize = fsize/(1024*1024) + "";
		} else {
			funit = " Bytes";
			fsize = fsize + "";
		}
		if (fsize.indexOf(".") > -1) {
			var fsosu = fsize.substring(fsize.indexOf("."), fsize.length);
			if (fsosu.length > 4)
				fsize = fsize.substring(0,fsize.indexOf("."))+fsosu.substring(0,4);
		}
		<%-- 파일명 뒤에 파일 사이즈를 같이 보여준 --%>
		var text = "<%=fileName%> (" + fsize + funit + ")";
		/*
	
		var listIndex = 1;
		for (k = 0; k < parent.document.setFileList.list.options.length; k++) {
			if (parent.document.setFileList.list.options[k].value == "") {
				listIndex = k;	
				break;
			}
		}
		parent.document.setFileList.list.options[listIndex].text = text;
		parent.document.setFileList.list.options[listIndex].value = val;
		parent.document.setFileList.list.options[listIndex].value = val;
		*/
		parent.ebEdit.handleUpload( text, val);
		location.href = 'blank.brd';	
	}
//-->
</script>

<%		
		}
	} else if ("poll".equals(mode)) {
%>
<script language="javascript">
<!--
	parent.closeUpload();

	if ("<%=fileMask%>" == "" || "<%=fileMask%>" == "null")
		alert('<util:message key="eb.error.upload.atch.file"/>');
	else {
		parent.document.forms["setPoll"+<%=seq%>].elements["fileMask"].value = '<%=fileMask%>';
		
		var innrHtml = "";
		innrHtml += "<img";
		innrHtml += " src='./upload/poll/<%=boardId%>/<%=fileMask%>'";
		innrHtml += " style='boarder:1px #dddddd solid'";
		innrHtml += " onerror=this.src='./upload/poll/no.gif'";
		innrHtml += " >";
		
		parent.document.getElementById("pollImg<%=seq%>").innerHTML = innrHtml;

		location.href = 'blank.brd';	
	}
//-->
</script>
<%		
	} else if ("test".equals(mode)) {
		String no = fileName;
		String en = new String(fileName.getBytes("8859_1"),"KSC5601");	
		String ko = new String(fileName.getBytes("KSC5601"),"8859_1");
%>
<script>
<!--
	alert('CharacterSet Test\n\nNO : <%=no%>\nEN : <%=en%>\nKO : <%=ko%>');
//-->
</script>
<%		
	} else if ("thumbnailtest".equals(mode)) {
%>
<script>
<!--
		parent.document.getElementById('thumbnail-preview').innerHTML = '<img src="./upload/thumb/<%=boardId%>/<%=fileMask%>" />';
//-->
</script>
<%		
	}	
}
%>