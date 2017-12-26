<%@page import="com.saltware.enface.enboard.service.BoardMenuManager"%>
<%@page import="com.saltware.enboard.dao.BoardDAO"%>
<%@page import="com.saltware.enview.components.dao.ConnectionContextForRdbms"%>
<%@page import="com.saltware.enview.components.dao.ConnectionContext"%>
<%@page import="com.saltware.enboard.util.Constants"%>
<%@page import="com.saltware.enboard.util.ValidateUtil"%>
<%@	page import="com.saltware.enview.security.EVSubject"%>
<%@	page import="com.saltware.enview.security.UserInfo"%>
<%@ page import="java.util.List,java.util.ArrayList" %>
<%@ page import="com.saltware.enview.Enview"%>
<%@ page import="com.saltware.enboard.vo.BoardVO,com.saltware.enboard.vo.BulletinVO,com.saltware.enboard.vo.BltnPollVO" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%
	String langKnd = (String) pageContext.getSession().getAttribute("langKnd");
	String cPath = request.getContextPath();
	String nmKor = "";
	String nmEng = "";
	String tel = "";
	String orgNm = "";
	
	String userId = "";
	if (EVSubject.getUserInfo() != null) {
		userId = EVSubject.getUserId();
		nmKor = (String) EVSubject.getUserInfo().getUserInfoMap().get("nm_kor");
		nmEng = (String) EVSubject.getUserInfo().getUserInfoMap().get("nm_eng");
		tel = (String) EVSubject.getUserInfo().getUserInfoMap().get("offc_tel");
		orgNm = (String) EVSubject.getUserInfo().getUserInfoMap().get("orgNm");
	}
	
	request.setAttribute("langKnd", langKnd);
	request.setAttribute("cPath", cPath);
	request.setAttribute("nmKor", nmKor);
	request.setAttribute("nmEng", nmEng);
	request.setAttribute("userId", userId);
	request.setAttribute("tel", tel);
	request.setAttribute("orgNm", orgNm);
	
//	BoardMenuManager boardMenuManager = (BoardMenuManager)Enview.getComponentManager().getComponent("com.saltware.enface.enboard.service.BoardMenuManager");
//	List boardMenuList = boardMenuManager.getBoardMenuList( Constants.PMSN_WRITE);
//	pageContext.setAttribute("copyToList", boardMenuList);
%>

<c:if test="${isInternal ne true }">
<%-- 스킨별 css 설정 --%>
<%-- 예시 : <link type="text/css" href="${cPath}/kaist/skin/${boardSkin }/css/style.css" rel="stylesheet" />  --%>
<%-- 20161122 프로토 타입용 스킨작업 --%>
<link type="text/css" rel="stylesheet" href="${cPath}/snu/css/reset.css">
<link type="text/css" rel="stylesheet" href="${cPath}/snu/css/common.css">
<link type="text/css" rel="stylesheet" href="${cPath}/snu/css/layout.css">
<link type="text/css" rel="stylesheet" href="${cPath}/snu/css/jquery-ui.css" />
<script type="text/javascript" src="${cPath}/snu/js/jquery.min.js" ></script>
<script type="text/javascript" src="${cPath}/snu/js/jquery-ui.js" ></script>
<script type="text/javascript" src="${cPath}/snu/js/js.js" ></script>
<script type="text/javascript" src="${cPath}/snu/js/jquery.PrintArea.js" ></script>
<script type="text/javascript" src="${cPath}/snu/js/common.js"></script>
<%-- //20161122 프로토 타입용 스킨작업 --%>
</c:if>
<script type="text/javascript">
function fn_save( tempSave) { 
	if( tempSave=='TEMP') {
		document.setTransfer.tempSave.value = "Y";
	} else {
		document.setTransfer.tempSave.value = "N";
	}
	// validationForm 이후 upload 및 save 호출
	if (ebEdit.validationForm()) {
		if ( ebEdit.vaultUploader ) {
			document.setTransfer.fileName.value = "";
			document.setTransfer.fileMask.value = "";
			document.setTransfer.fileSize.value = "";
			
			ebEdit.vaultUploader.attachEvent("onUploadComplete", function (files) {
				// 파라미터로 받는 files에는 원파일명이 없다.(undefined)
				// getData로 받는 데이터에는 있음.(업로드 된 파일리스트)
				var fileList = ebEdit.vaultUploader.getData();
				if (files.length > 0) {
					for (var i = 0 ; i < fileList.length ; i++) {
						document.setTransfer.fileName.value += fileList[i].name + "|";
						document.setTransfer.fileMask.value += fileList[i].serverName + "|";
						document.setTransfer.fileSize.value += fileList[i].size + "|";
					}
				}
				ebEdit.isNewFile = false;
				ebEdit.isCancel = false;
				
				ebEdit.save();
			});
			
			if (ebEdit.isNewFile) {
				ebEdit.vaultUploader.upload();
			} else {
				var fileList = ebEdit.vaultUploader.getData();
				if (fileList.length > 0) {
					for (var i = 0 ; i < fileList.length ; i++) {
						document.setTransfer.fileName.value += fileList[i].name + "|";
						document.setTransfer.fileMask.value += fileList[i].serverName + "|";
						document.setTransfer.fileSize.value += fileList[i].size + "|";
					}
				}
				ebEdit.isCancel = false;
				ebEdit.save();
			}
		} else {
			ebEdit.save();	
		}
	}
}

<%--
// 에디터 로딩 완료시
function dext_editor_loaded_event(DEXT5Editor) {
	if (document.setTransfer.cmd.value === "MODIFY") {
		DEXT5.setBodyValue($("#bltnOrgCntt").val(), DEXT5Editor.editor.ID);
	}
}
--%>
$(document).ready(function () {
	var ctrlDown = false;
	/* $("input.subj").keydown(function (e) {
		if (e.keyCode == 17 || e.keyCode == 91) ctrlDown = true;
		
		if ( !((e.which >= 37 && e.which <= 40) || e.which == 8 || e.which == 46 || (ctrlDown && (e.keyCode == 67 || e.keyCode == 86))) ) {
			var newStr = ebUtil.limitByteLength(this, $(this).attr("maxlength"));
			$(this).val(newStr);
		}
	}).keyup(function (e) {
		if (e.keyCode == 17 || e.keyCode == 91) ctrlDown = false;
	}); */
	$("input.subj").each(function (e) {
		<c:if test="${langKnd eq 'en'}">
		$(this).attr("maxlength", "200");
		</c:if>
		<c:if test="${langKnd ne 'en'}">
		$(this).attr("maxlength", "200");
		</c:if>
	});
});
</script>

<c:set var="loginInfo" value="${secPmsnVO.loginInfo}"/>
<%--
<c:if test="${boardVO.extUseYn eq 'Y'}">
  <c:set var="extnMapper" value="${boardVO.bltnExtnMapper}"/>
  <c:if test="${!empty bltnVO.bltnExtnVO}">
	<c:set var="extnVO" value="${bltnVO.bltnExtnVO}"/>
  </c:if>
</c:if>
 --%>
<input type="hidden" id="isLogin" name="isLogin" value="${secPmsnVO.isLogin}" />
<c:if test="${(empty param.boardTtlYn || param.boardTtlYn eq 'Y') && isApiboard}">
<div class="content_center_top">
	<h3><c:out value="${boardVO.boardNm }" /></h3>
</div>
</c:if>
<div class="content_center_top" style="padding-top: 0px;margin-top: 0px;height: 26px;">
	<div class="title_text tit_width" style="margin-top: 0px; height: 26px;">
		<%--
		<c:out value="${(!empty boardVO.boardNotice) ? boardVO.boardNotice : commentManager.BOARD_DEFAULT_NOTICE.text}" escapeXml="false" />
		 --%>
	</div>
</div>
<div class="board_wrap">
	<form name="editForm" action="" >
	<c:if test="${bltnVO.delFlag=='B'}">
	<font color='red'><util:message key="eb.title.badBltn.bad"/></font>
	</c:if>
	<c:if test="${bltnVO.delFlag=='T'}">
	<font color='red'><util:message key="eb.title.tempSave.stat"/></font>
	</c:if>		
	<table class="table_type01 write" summary="<util:message key='eb.title.navi.board'/>">
		<caption><util:message key='eb.title.navi.board'/></caption>
		<colgroup>
			<col width="110px;">
			<col width="auto;">
			<col width="110px;">
			<col width="auto;">
		</colgroup>			
		<tbody>
   			<tr>
				<th><util:message key='eb.info.ttl.author'/></th>
				<td colspan="3">
					<%--익명게시판이 아닌 경우--%>
					<c:if test="${boardVO.anonYn eq 'N'}">
						<c:if test="${boardSttVO.cmd ne 'MODIFY'}"><c:out value="${secPmsnVO.userNm}"/></c:if>
						<c:if test="${boardSttVO.cmd eq 'MODIFY'}"><c:out value="${bltnVO.userNick}"/></c:if>
						<input type="hidden" name="userNick"
							<c:if test="${boardSttVO.cmd ne 'MODIFY'}">value="<c:out value="${secPmsnVO.userNm}"/>"</c:if>
							<c:if test="${boardSttVO.cmd eq 'MODIFY'}">value="<c:out value="${bltnVO.userNick}"/>"</c:if>
						/>
					</c:if>
					
					<%--익명게시판인 경우--%>
	    			<c:if test="${boardVO.anonYn eq 'Y'}">
	    				<input type="text" name="userNick" value="<c:if test="${boardSttVO.cmd eq 'MODIFY'}"><c:out value="${bltnVO.userNick}"/></c:if>" class="bd_basicwrite_text1" maxlength="72"/>
	    			</c:if>
				</td>
			</tr>
   		<c:if test="${boardVO.termYn == 'Y'}">
	  		<tr>
				<th><util:message key='eb.info.ttl.term'/></th>
				<td colspan="3">
					<input name="bltnBgnYmdHm" id="bltnBgnYmdHm" type="hidden" value="<c:out value="${bltnVO.bltnBgnYmdDatimF}"/>">
	    			<input name="bltnEndYmdHm" id="bltnEndYmdHm" type="hidden" value="<c:out value="${bltnVO.bltnEndYmdDatimF}"/>">
					<label for="bltnBgnYmd">
						<input type="text" readonly id="bltnBgnYmd" name="bltnBgnYmd" <c:if test="${boardSttVO.cmd == 'MODIFY'}">value="<c:out value="${bltnVO.bltnBgnYmdF}"/>"</c:if> class="w100 datepicker"/>
						<img src="${cPath }/snu/images/calendar.png" alt="datepicker" />
					</label>
					<select name="bltnBgnHm" id="bltnBgnHm"></select>
					~					
					<label for="bltnEndYmd">
						<input type="text" readonly id="bltnEndYmd" name="bltnEndYmd" <c:if test="${boardSttVO.cmd == 'MODIFY'}">value="<c:out value="${bltnVO.bltnEndYmdF}"/>"</c:if> class="w100 datepicker"/>
						<img src="${cPath }/snu/images/calendar.png" alt="datepicker" />
					</label>
	
					<select name="bltnEndHm" id="bltnEndHm" ></select>
					<input type="checkbox" type="checkbox" id="isAllday"  name="isAllday" <c:if test="${bltnVO.isAllday == 'Y'}">checked="checked"</c:if> /> <label for="check_2" class=""><util:message key='eb.info.ttl.allday'/></label>
				</td>
			</tr>
   		</c:if>
		<c:if test="${boardVO.noticeYn eq 'Y' && secPmsnVO.ableNotice eq 'true' && boardSttVO.cmd ne 'REPLY' && bltnVO.bltnLev < 2}">
			<tr>
				<th><util:message key='eb.info.ttl.noticeYn'/></th>
				<td colspan="3">
					<input class="rm5 <c:if test="${isAbleNotice eq false && boardSttVO.cmd eq 'WRITE'}">none</c:if>" type="checkbox" id="bltnYTag" name="bltnTopTag" <c:if test="${bltnVO.bltnTopTag == 'Y'}">checked</c:if> value="Y" onclick="ebUtil.setCheckedValue(document.editForm.bltnTopTag, 'Y');$('#notiTerm').show();">
					<label for="bltnYTag" class="<c:if test="${isAbleNotice eq false}">none</c:if>"><util:message key='eb.info.ttl.notice'/></label>
					<input class="rm5" type="checkbox" id="bltnNTag" name="bltnTopTag" <c:if test="${bltnVO.bltnTopTag == 'N'}">checked</c:if> value="N" onclick="ebUtil.setCheckedValue(document.editForm.bltnTopTag, 'N');$('#notiTerm').hide();">
					<label for="bltnNTag"><util:message key='eb.info.ttl.notice.not'/></label>
				</td>
			</tr>
		</c:if>
		<c:if test="${boardVO.secretYn == 'Y' && secPmsnVO.isLogin == 'true'}">
			<tr>
				<th><util:message key='eb.info.ttl.secret'/></th>
				<td colspan="3">
					<input class="rm5" type="radio" id="bltnSecretY" name="bltnSecret" value="Y" <c:if test="${bltnVO.bltnSecretYn == 'Y'}">checked</c:if>/>
					<label for="bltnSecretY">Y</label>
					<input class="rm5" type="radio" id="bltnSecretN" name="bltnSecret" value="N" <c:if test="${bltnVO.bltnSecretYn == 'N'}">checked</c:if> />	
					<label for="bltnSecretN">N</label>
				</td>
			</tr>
		</c:if>
		<c:if test="${boardVO.cateYn eq 'Y'}">
			<tr>
				<th><util:message key='ev.prop.configuration.configType'/></th>
				<td colspan="3">
					<c:if test="${!empty bltnCateList1 }">
					<select name="cateList">
						<option value=-1><util:message key='ev.title.all'/></option>
	                	<c:forEach items="${bltnCateList1}" var="cList">
	                		<c:if test="${!empty cList.bltnCateNm}">
	                	<option value="<c:out value="${cList.bltnCateId}"/>"<c:if test="${bltnVO.cateId eq cList.bltnCateId}">selected</c:if>><c:out value="${cList.bltnCateNm}"/></option>
	                		</c:if>
	                	</c:forEach>
					</select>
					</c:if>
				</td>
			</tr>
		</c:if>
    		<tr>
				<th><util:message key='eb.info.ttl.bltnSubj'/></th>
				<td colspan="3">
					<input class="w_p100 subj" type="text" id="id_title" name="bltnSubj" value="<c:out value="${bltnVO.bltnSubj}"/>" maxlength="120"/>
					<label for="id_title" class="none w_p100"></label>
				</td>
			</tr>
			<tr>
				<td colspan="4" id="smarteditor">
					<textarea id=editorCntt style="width:100%;height:200px"><c:out value="${bltnVO.bltnCntt}"/></textarea>
   					<input type=hidden name=bltnOrgCntt>
				</td>
			</tr>
		</tbody>
	</table>
	</form>
	<table class="table_type01" summary="<util:message key='eb.title.navi.board'/>">
		<caption>게시판</caption>
		<colgroup>
			<col width="auto;">
		</colgroup>			
		<tbody>
			<c:if test="${boardVO.maxFileCnt > '0'}">
	    	<tr >
	    		<td colspan="4" style="padding-left: 10px;padding-right: 10px">
	    			<input type="hidden" id="badFileList" name="badFileList" value="${boardVO.badExtMask}">
	    			<input type="hidden" id="sendFlg" name="sendFlg" value="false">
	    			<div id="vault_upload">
	    				
	    			</div>
	    			<c:if test="${boardSttVO.cmd eq 'MODIFY'}">
		          	<c:set var="fileList" value="${bltnVO.fileList}"/>
		          	<ul id="vault_fileList" style="display: none;">
		          		<c:forEach items="${fileList}" var="file">
		          		<li data-name="<c:out value='${file.fileName}'/>" data-mask="<c:out value='${file.fileMask}'/>" data-size="<c:out value='${file.fileSize}'/>"><c:out value="${file.fileName}"/> (<c:out value="${file.sizeSF}"/>)</li>
		          		</c:forEach>	
		          	</ul>
		          	</c:if>
	    			<form name=setUpload method=post enctype="multipart/form-data" target=invisible action="${cPath }/board/fileMngr?cmd=upload">
	    				<input type="hidden" id="totalsize" name="totalsize" value="0" />
	    				<input type="hidden" name="viewsize" readonly="readonly" style=background-color:#eeeeee;width:100%;text-align:center;border:none value='<util:message key='eb.title.total.file.size'/>: 0KB'>
						<input type="hidden" name="boardId" value="<c:out value="${boardVO.boardRid}"/>">
						<input type="hidden" name="totalsize" value=0>
						<input type="hidden" name="subId" value=sub01>
						<input type="hidden" name="mode" value=attach>
	    			</form>
	    			<form name="setFileList" method="post" target="invisible" action="${cPath }/board/fileMngr?cmd=delete">
	    				<input type=hidden name=semaphore value="<c:out value="${boardSttVO.semaphore}"/>">
						<input type=hidden name=vaccum>
						<%-- 게시판별 별도 디렉터리에 첨부파일 관리하는 것 때문에 추가 2009.02.25.KWShin. --%>
						<input type=hidden name=delBoardId value="<c:out value="${boardVO.boardRid}"/>">
						<input type=hidden name=unDelList>
						<input type=hidden name=delList>
						<input type=hidden name=subId value=sub01>
	    			</form>
	    		</td>
	    	</tr>
	    	</c:if>
		</tbody>
	</table>
	
	<%-- 파일 업로드가 불가능 하더라도 에디터는 가능하니까 존재하고 있어야 한다. --%>
	<ul id="uploadFileList" style="display: none">
		<c:if test="${boardSttVO.cmd == 'MODIFY'}">
			<c:set var="file" value="${bltnVO.fileList}"/>
			<c:forEach items="${file}" var="fList">
			<li data="<c:out value="${fList.fileMask}"/>|<c:out value="${fList.fileSize}"/>"><c:out value="${fList.fileName}"/>&nbsp;(<c:out value="${fList.sizeSF}"/>)</li>
			</c:forEach>
		</c:if>
	</ul>
	
	<div class="board_btn_wrap">						
		<div class="board_btn_wrap_right">
			<a href="javascript:void(0);" class="btn black" target="_self" onclick="ebEdit.save()"><util:message key="ev.title.save" /></a>
			<a href="javascript:void(0);"class="btn white" target="_self" onclick="ebEdit.list();"><util:message key='ev.hnevent.label.list' langKnd="${langKnd}"/></a>
		</div>
	</div>
	
</div><!-- board_wrap end -->

<div id="loadBltnDialog">

</div>