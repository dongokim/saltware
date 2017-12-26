<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.saltware.enboard.vo.BoardVO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${boardVO.boardSys == 'CF' }">
	<jsp:include page='<%="./skin/" + request.getAttribute("boardSkin") + "/board_edit.jsp"%>'/>
</c:if>
<c:if test="${boardVO.boardSys != 'CF' }">
<jsp:include page="./include/board_header.jsp"/>
<%---------------------------------------------------------------------------------------------------------------------%>  
<title><c:out value="${boardVO.boardTtl}"/></title>

<c:if test="${clientInfo.name != 'Phone' && clientInfo.name != 'Tablet'}">
  <c:if test="${boardVO.accSetYn == 'Y'}">
  <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery/jquery-1.10.2.min.js"></script>
  <%-- <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/tree/dhtmlxcommon.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/tree/dhtmlXTree.js"></script> --%>
  <script type="text/javascript" src="${pageContext.request.contextPath}/dhtmlx/dhtmlx.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/dhtmlx/thirdparty/excanvas/excanvas.js"></script>
  <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/dhtmlx/dhtmlx.css" />
  </c:if>
  <c:if test="${boardVO.boardSkin != 'jwskin'}">
  <script type="text/javascript" src="${pageContext.request.contextPath}/board/fckeditor/fckeditor.js"></script>
  </c:if>
  <script type="text/javascript" src="${pageContext.request.contextPath}/board/javascript/enboard_edit.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery/jquery-1.10.2.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery/jquery-ui-1.9.2.custom.min.js"></script>
  
  <!--smarteditor js-->
  <script type="text/javascript" src="${pageContext.request.contextPath}/board/smarteditor/js/HuskyEZCreator.js"></script>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/dhtmlx/vault/dhtmlxvault.css" />
  <script type="text/javascript" src="${pageContext.request.contextPath}/dhtmlx/vault/dhtmlxvault.js"></script>
  <script type="text/javascript">
	<!--
	<c:if test="${!empty bltnVO.fileList}">
	  <c:forEach items="${bltnVO.fileList}" var="fList">
		ebEdit.fileList += "<c:out value="${fList.fileMask}"/>-<c:out value="${fList.fileSize}"/>;";
	  </c:forEach>
	</c:if>
	window.onload   = ebEdit.edit_init;
	window.onunload = ebEdit.edit_destroy;
	//-->
  </script>
</c:if>
<c:if test="${clientInfo.name == 'Phone' || clientInfo.name == 'Tablet'}">
  <script type="text/javascript" src="${pageContext.request.contextPath}/board/javascript/enboard_edit.js"></script>
  <script type="text/javascript">
	<!--
	window.onload   = ebEdit.mobile_edit_init;
	window.onunload = ebEdit.mobile_edit_destroy;
	//-->
  </script>
</c:if>
      <%-- 게시판 본체 --%>
      <jsp:include page='<%="./skin/"+request.getAttribute("boardSkin")+"/board_edit.jsp"%>' />
<form name=setTransfer method=post action=list.brd>   
	<input type=hidden name=boardId       value=<c:out value="${boardSttVO.boardId}"/>>
	<input type=hidden name=cmpBrdId      value=<c:out value="${boardSttVO.cmpBrdId}"/>>
	<input type=hidden name=srchKey       value=<c:out value="${boardSttVO.srchKey}"/>>
	<input type=hidden name=srchType      value=<c:out value="${boardSttVO.srchType}"/>>
	<input type=hidden name=srchBgnReg    value=<c:out value="${boardSttVO.srchBgnReg}"/>>
	<input type=hidden name=srchEndReg    value=<c:out value="${boardSttVO.srchEndReg}"/>>
	<input type=hidden name=srchReplYn    value=<c:out value="${boardSttVO.srchReplYn}"/>>
	<input type=hidden name=srchMemoYn    value=<c:out value="${boardSttVO.srchMemoYn}"/>>
	<input type=hidden name=page          value=<c:out value="${boardSttVO.page}"/>>
	<input type=hidden name=pageSize      value=<c:out value="${boardSttVO.pageSize}"/>>
	<input type=hidden name=cateId        value=<c:out value="${boardSttVO.cateId}"/>>
	<input type=hidden name=bltnCateId    value=<c:out value="${boardSttVO.bltnCateId}"/>>
	<input type=hidden name=bltnNo        value=<c:out value="${boardSttVO.bltnNo}"/>>
	<input type=hidden name=cmd       	  value=<c:out value="${boardSttVO.cmd}"/>>
	<input type=hidden name=ie            value=<c:out value="${boardSttVO.ie}"/>>
	<input type=hidden name=onlyReplYn    value=<c:out value="${boardSttVO.onlyReplYn}"/>>
	<input type=hidden name=onlyMemoYn    value=<c:out value="${boardSttVO.onlyMemoYn}"/>>
	<input type=hidden name=wallUserId    value=<c:out value="${boardSttVO.wallUserId}"/>>
	<input type=hidden name=boardRid      value=<c:out value="${boardVO.boardRid}"/>>
	<input type=hidden name=accSetYn      value=<c:out value="${boardVO.accSetYn}"/>>
	<input type=hidden name=boardSkinDflt value=<c:out value="${boardVO.boardSkinDflt}"/>>
	<input type=hidden name=skin value=<c:out value="${boardSkin}"/>>
	<input type=hidden name=boardWidth    value=<c:out value="${boardVO.boardWidth}"/>>
	<input type=hidden name=maxFileCnt    value=<c:out value="${boardVO.maxFileCnt}"/>>
	<input type=hidden name=thumbSize     value=<c:out value="${boardVO.thumbSize}"/>>
	<input type=hidden name=badNickDflt   value=<c:out value="${boardVO.badNickDflt}"/>>
	<input type=hidden name=badCnttDflt   value=<c:out value="${boardVO.badCnttDflt}"/>>
	<input type=hidden name=limitSize     value=<c:out value="${boardVO.limitSize}"/>>
	<%--BEGIN::Keep this snippet for compatiablility with old version--%>
	<input type=hidden name=ableListGrade value=<c:out value="${bltnVO.ableListGrade}"/>>
	<input type=hidden name=ableListGroup value=<c:out value="${bltnVO.ableListGroup}"/>>
	<input type=hidden name=ableListRole  value=<c:out value="${bltnVO.ableListRole}"/>>
	<input type=hidden name=ableReadGrade value=<c:out value="${bltnVO.ableReadGrade}"/>>
	<input type=hidden name=ableReadGroup value=<c:out value="${bltnVO.ableReadGroup}"/>>
	<input type=hidden name=ableReadRole  value=<c:out value="${bltnVO.ableReadRole}"/>>
	<%--END::Keep this snippet for compatiablility with old version--%>
	<input type=hidden name=accSetInfo>
	<input type=hidden name=totFileSize   value=<c:out value="${bltnVO.totFileSize}"/>>
	<input type=hidden name=sizeSF        value=<c:out value="${bltnVO.sizeSF}"/>>
	<c:set var="loginInfo" value="${secPmsnVO.loginInfo}"/>
	<input type=hidden name=mileTot       value=<c:out value="${loginInfo.mile_tot}"/>>
	<input type=hidden name=bltnSubj>
	<input type=hidden name=bltnCntt>
	<input type=hidden name=ext_str0>
	<input type=hidden name=ext_str1>
	<input type=hidden name=ext_str2>
	<input type=hidden name=ext_str3>
	<input type=hidden name=ext_str4>
	<input type=hidden name=ext_str5>
	<input type=hidden name=ext_str6>
	<input type=hidden name=ext_str7>
	<input type=hidden name=ext_str8>
	<input type=hidden name=ext_str9>
	<input type=hidden name=bltnSecretYn>
	<input type=hidden name=userNick>
	<input type=hidden name=userPass>
	<input type=hidden name=anonFlag>
	<input type=hidden name=fileName>
	<input type=hidden name=fileMask>
	<input type=hidden name=fileSize>
	<input type=hidden name=pollCntt>
	<input type=hidden name=pollMask>
	<input type=hidden name=betPnt>
	<input type=hidden name=bltnTopTag>
	<input type=hidden name=bltnBgnYmd>
	<input type=hidden name=bltnEndYmd>
	<input type=hidden name=rtnURI>
	<input type=hidden name=langKnd value=<c:out value="${secPmsnVO.locale}"/>>
</form>
<iframe name='invisible' frameborder=0 width=0 height=0></iframe>
<iframe name='download' frameborder=0 width=0 height=0></iframe>
</c:if>