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
	request.setAttribute("cmd", request.getParameter("cmd"));
	
//	BoardMenuManager boardMenuManager = (BoardMenuManager)Enview.getComponentManager().getComponent("com.saltware.enface.enboard.service.BoardMenuManager");
//	List boardMenuList = boardMenuManager.getBoardMenuList( Constants.PMSN_WRITE);
//	pageContext.setAttribute("copyToList", boardMenuList);
%>

<c:if test="${isInternal ne true }">
<meta name="author" content="환자안전포탈"/>
<meta name="description" content="환자안전포탈"/>
<meta name="keywords" content="환자안전포탈"/>
<meta name="Resource-type" content="Document"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/kops/css/default.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/sub.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/boardCheck.js"></script>	<!-- 2017.10.16 게시판 유효성 체크 -->
<%-- //20161122 프로토 타입용 스킨작업 --%>
</c:if>
<script type="text/javascript">
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
		
		//2017.07.27 sskim : iframe resize 추가 
		try
		{
			parent.fn_childIframeAutoresize(parent.document.getElementById(window.name),parseInt($("#sub_container").height()+100));
			parent.fn_scrollTop();	
		}
		catch(e)
		{console.log(e);}
	});
	function fn_checkValidation()
	{
		if(titleCheck('qu_title') && passwordCheck('password', '${cmd}'))
		{
			ebEdit.save();
		}
	}
</script>
<c:set var="loginInfo" value="${secPmsnVO.loginInfo}"/>
<c:set var="extnVO" value="${bltnVO.bltnExtnVO}"/>
<input type="hidden" id="isLogin" name="isLogin" value="${secPmsnVO.isLogin}" />

<!-- Contents -->
<div id="container" style="margin-top:0px">
	<div class="contents"> 
	
		<!-- contents -->
		<h2 class="sub_title"><c:out value="${boardVO.boardNm}"/></h2>
		
		<div class="board_box">
			<div class="board_list mgt10">
				<form action="" method="post" name="editForm" id="editForm">
					<table summary="질문제목, 이름, 답변 확인용 비밀번호, 질문 내용 정보입니다" class="table_t1">
						<caption>문의 및 상담 입력</caption>
						<colgroup>
							<col width="20%">
							<col width="80%">
						</colgroup>
						<tbody>
							<c:if test="${cmd eq 'REPLY'}">
								<tr>
									<th scope="col">
										<label for="subject">원글</label>
									</th>
									<td>
										<c:out value="${rplbltnVO.bltnOrgCntt}" escapeXml="false" />
									</td>
								</tr>								
							</c:if>
							<tr>
								<th scope="col"><label for="subject">제목</label>
								<td><input type="text" class="text" name="bltnSubj"	id="qu_title" style="width: 700px"  value="<c:out value="${bltnVO.bltnSubj}"/>"/></td>
							</tr>
							<tr>
								<th scope="col"><label for="name">이름</label></th>
								<td>
									<c:if test="${boardSttVO.cmd ne 'MODIFY'}"><c:out value="${nmKor}"/></c:if>
									<c:if test="${boardSttVO.cmd eq 'MODIFY'}"><c:out value="${bltnVO.userNick}"/></c:if>
									<input type="hidden" name="userNick"
										<c:if test="${boardSttVO.cmd ne 'MODIFY'}">value="<c:out value="${nmKor}"/>"</c:if>
										<c:if test="${boardSttVO.cmd eq 'MODIFY'}">value="<c:out value="${bltnVO.userNick}"/>"</c:if>
									/>
								</td>
							</tr>
							<c:if test="${cmd ne 'REPLY' && bltnVO.bltnLev < 2}">
								<tr>
									<th scope="col"><label for="password">답변 확인용 비밀번호</label></th>
									<td><input type="password" class="text" name="ext_str1"	id="password" value="<c:out value="${extnVO.ext_str1}"/>"/></td>
								</tr>								
							</c:if>
							<tr>
								<th scope="col"><label for="cont">내용</label></th>
								<td id="smarteditor">
									<textarea class="text" id="editorCntt" style="width: 100%; height: 500px;" cols="" rows="30" class="w_100"><c:out value="${bltnVO.bltnCntt}"/></textarea>
									<input type="hidden" name="bltnOrgCntt" id="bltnOrgCntt"/>
								</td>
							</tr>
						</tbody>
					</table>
				</form>
				<table class="table_t1" summary="<util:message key='eb.title.navi.board'/>">
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
				    			<div id="vault_upload" style="width=100%">
				    				
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
			</div>
		</div>
		<div class="btn_box mgt40">
			<a href="javascript:void(0);" class="btn_pre" onclick="ebEdit.list();">취 소</a>
			<a href="javascript:fn_checkValidation();" class="btn_next mgl20">등 록</a>
		</div>
	</div>
</div>
