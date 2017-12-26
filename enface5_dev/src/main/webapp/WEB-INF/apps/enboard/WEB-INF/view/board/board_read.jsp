<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.saltware.enboard.vo.BoardVO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${boardVO.boardSys == 'CF' }">
	<script type="text/javascript" src="${cPath}/javascript/crossdomain.js"></script>
	<jsp:include page='<%="./skin/" + request.getAttribute("boardSkin") + "/board_read.jsp"%>'/>
</c:if>
<c:if test="${boardVO.boardSys != 'CF' }">
<jsp:include page="./include/board_header.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/board/javascript/enboard_list.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/board/javascript/enboard_read.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/dext5editor/js/dext5editor.js"></script>
<script type="text/javascript">
	if (window.attachEvent) {
		window.attachEvent ("onload", ebRead.initRead);
	} else if (window.addEventListener ) {
		window.addEventListener ("load", ebRead.initRead, false);
	} else {
		window.onload = ebRead.initRead;
	}
</script>
<%-- 게시판 본체 --%>

<jsp:include page='<%="./skin/"+request.getAttribute("boardSkin")+"/board_read.jsp"%>'/>

<form name="setForm" method="post" action="">
  <input type="hidden" name="boardId"        value="<c:out value="${boardSttVO.boardId}"/>"/>
  <input type="hidden" name="cmpBrdId"       value="<c:out value="${boardSttVO.cmpBrdId}"/>"/>
  <input type="hidden" name="srchKey"        value="<c:out value="${boardSttVO.srchKey}"/>"/>
  <input type="hidden" name="srchType"       value="<c:out value="${boardSttVO.srchType}"/>"/>
  <input type="hidden" name="srchBgnReg"     value="<c:out value="${boardSttVO.srchBgnReg}"/>"/>
  <input type="hidden" name="srchEndReg"     value="<c:out value="${boardSttVO.srchEndReg}"/>"/>
  <input type="hidden" name="srchReplYn"     value="<c:out value="${boardSttVO.srchReplYn}"/>"/>
  <input type="hidden" name="srchMemoYn"     value="<c:out value="${boardSttVO.srchMemoYn}"/>"/>
  <input type="hidden" name="page"           value="<c:out value="${boardSttVO.page}"/>"/>
  <input type="hidden" name="pageSize"       value="<c:out value="${boardSttVO.pageSize}"/>"/>
  <input type="hidden" name="cateId"         value="<c:out value="${boardSttVO.cateId}"/>"/>
  <input type="hidden" name="bltnCateId"     value="<c:out value="${boardSttVO.bltnCateId}"/>"/>
  <input type="hidden" name="rtnBltnNo"      value="<c:out value="${boardSttVO.rtnBltnNo}"/>"/>
  <input type="hidden" name="bltnNo"         value="<c:out value="${boardSttVO.bltnNo}"/>"/>
  <input type="hidden" name="cmd"            value="<c:out value="${boardSttVO.cmd}"/>"/>
  <input type="hidden" name="onlyReplYn"     value="<c:out value="${boardSttVO.onlyReplYn}"/>"/>
  <input type="hidden" name="onlyMemoYn"     value="<c:out value="${boardSttVO.onlyMemoYn}"/>"/>
  <input type="hidden" name="wallUserId"     value="<c:out value="${boardSttVO.wallUserId}"/>"/>
  <input type="hidden" name="badNickDflt"    value="<c:out value="${boardVO.badNickDflt}"/>"/>
  <input type="hidden" name="badCnttDflt"    value="<c:out value="${boardVO.badCnttDflt}"/>"/>
  <input type="hidden" name="boardSkinDflt"  value="<c:out value="${boardVO.boardSkinDflt}"/>"/> 
  <input type="hidden" name="boardWidth"     value="<c:out value="${boardVO.boardWidth}"/>"/>
  <input type="hidden" name="writableYn"     value="<c:out value="${boardVO.writableYn}"/>"/>
  <input type="hidden" name="replyableYn"    value="<c:out value="${boardVO.replyableYn}"/>"/>
  <input type="hidden" name="memoWritableYn" value="<c:out value="${boardVO.memoWritableYn}"/>"/>
 
  <input type="hidden" name="userPass" />
  <input type="hidden" name="userNick" />
  <input type="hidden" name="memoCntt" />
  <input type="hidden" name="memoSeq" value="0" />
  <input type="hidden" name="memoGn" value="0" />
  <input type="hidden" name="memoGq" value="0" />
  <input type="hidden" name="memoLev" value="0" />
  <input type="hidden" name="pollSeq" />
  <input type="hidden" name="evalPnt" />
  <input type="hidden" name="bltnBestLevel" />
  <input type="hidden" name="rtnURI" />
  <input type="hidden" name="befCmd" />
  <input type="hidden" name="langKnd" value="<c:out value="${boardVO.langKnd}"/>"/>
  <input type="hidden" name="viewMode" value="<c:out value="${boardSttVO.viewMode }"/>"/>
  <input type="hidden" name="boardTtlYn" value="<c:out value="${param.boardTtlYn}"/>"/>
</form>

<script>ebRead.loadPoll();</script> 
<iframe name="download" title="download" width=0 height=0 style="border:none;"></iframe>
<%---------------------------------------------------------------------------------------------------------------------%>  
<jsp:include page="./include/board_footer.jsp"/>
</c:if>