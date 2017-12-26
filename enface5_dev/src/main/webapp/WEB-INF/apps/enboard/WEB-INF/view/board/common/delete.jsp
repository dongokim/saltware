<%@ page contentType="text/html;charset=UTF-8" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<%
	String   subId       = (String)request.getAttribute  ("subId");
	String   vaccum      = (String)request.getAttribute  ("vaccum");
	String[] deletedList = (String[])request.getAttribute("deletedList");
	String   delBoardId  = (String)request.getAttribute  ("delBoardId");
	String   seq         = (String)request.getAttribute  ("seq");
%>

<% 
if ("sub11".equals(subId)) { // 설문 이미지를 삭제하는 경우.090603.KWShin.
%>

<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/crossdomain.js">
<script language="javascript">
<!--
	parent.document.forms['setPoll<%=seq%>'].fileMask.value = '';
	parent.document.getElementById('pollImg<%=seq%>').innerHTML = '';
	
	parent.document.setPollDel.seq.value = '';
	parent.document.setPollDel.fileMask.value = '';

	location.href = 'blank.brd';
-->
</script>

<%
} else {
	if ("DIRECT".equals(vaccum) && deletedList != null) { 
%>

<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/crossdomain.js">
<script language="javascript">
<!--
	var deletedList = new Array();
<% 
		for (int i = 0; i < deletedList.length; i++) 
			out.println("deletedList["+i+"] = '"+deletedList[i]+"';");	
%>
	parent.ebEdit.handleDelete( deletedList);
	/*
	for (var i = 0; i < parent.document.setFileList.list.length; i++) {
		for (var j = 0; j < <%=deletedList.length%>; j++) {
			if (parent.document.setFileList.list.options[i].value == deletedList[j]) {
				parent.document.setFileList.list.options[i].value = '';
				parent.document.setFileList.list.options[i].text = '';
			}
		}
	}

	var tmp1 = new Array();
	var tmp2 = new Array();
	var cnt = 0;
	for (var i = 0; i < parent.document.setFileList.list.length; i++) {
		if (parent.document.setFileList.list.options[i].value != '') {
			tmp1[cnt] = parent.document.setFileList.list.options[i].value;
			tmp2[cnt] = parent.document.setFileList.list.options[i].text;
			cnt++;
		}
	}

	for (var i = 0; i < parent.document.setFileList.list.length; i++) {
		parent.document.setFileList.list.options[i].value = '';
		parent.document.setFileList.list.options[i].text = '';
	}

	for (var i = 0; i < cnt; i++) {
		parent.document.setFileList.list.options[i].value = tmp1[i];
		parent.document.setFileList.list.options[i].text = tmp2[i];
	}
	*/

	var ie = eval(parent.document.setTransfer.ie.value);
	var contents = '';

	var totalsize = eval("parent.document.setUpload.totalsize.value");
	for (var i=0; i<deletedList.length; i++) {
		var fileSize = deletedList[i].split("-");
		try {
			contents = parent.FCKeditorAPI.GetInstance('editorCntt').GetData();
			var tmpidx = contents.indexOf('/upload/editor/<%=delBoardId%>/'+fileSize[0]);
			if (tmpidx > -1) {
				var tmp1 = contents.substring(0, tmpidx);
				var tmp2 = contents.substring(tmpidx);
				var startidx = tmp1.lastIndexOf('<img');
				var endidx = tmp2.indexOf('>')+1;
				var tmp3 = contents.substring(0, startidx);
				var tmp4 = tmp2.substring(endidx);
				contents = tmp3+tmp4;
			}
			parent.FCKeditorAPI.GetInstance('editorCntt').SetData(contents);
		} catch (e) {
			// FCKeditor 를 사용하지 않을 때는 에러가 난다.
		}
		totalsize -= eval(fileSize[1]);
	}
	totalsize = (totalsize < 0) ? 0 : totalsize;

	var unit = "";
	var calsize = "";
	if ((1024 < totalsize) && (totalsize < 1024 * 1024)) {
		unit = " KB";
		calsize = (totalsize/1024)+"";
	} else if (1024 * 1024 <= totalsize) {
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
	parent.document.setUpload.viewsize.value = "TOTAL FILE SIZE : "+calsize+unit;

	location.href = 'blank.brd';
//-->
</script>

<%
	} else if ("INDIRECT".equals(vaccum)) { 
%>
<%-- 파일 삭제하는 것을 보여주려면 아래 코멘트를 푼다.090524.KWShin. --%>
<%--title>Delete File...</title>
<body bgcolor=#D6D3CE>
  <img src=./images/bbs/site/alert.gif align=absmiddle />&nbsp;&nbsp;<font style=font-size:9pt>파일 삭제중..</font><br/>
  <center><input type=button name=bt style='width:80;font-size:9pt' value='확 인' onclick='terminate()'></center>
</body>
<script>
<!--
	function window.onload() {
		setTimeout('terminate()', 3000);
	}
	function terminate() {
		window.close();
	}
//-->
</script--%>
<%
	} // if "INDIRECT
} // if subId
%>





















