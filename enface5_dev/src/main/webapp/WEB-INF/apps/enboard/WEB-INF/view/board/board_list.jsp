<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.saltware.enboard.vo.BoardVO" %>
<%@ page import="com.saltware.enboard.vo.BoardSttVO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${boardVO.boardSys == 'CF' }">
	<script type="text/javascript" src="${cPath}/javascript/crossdomain.js"></script>
	<jsp:include page='<%="./skin/" + request.getAttribute("boardSkin") + "/board_list.jsp"%>'/>
</c:if>
<c:if test="${boardVO.boardSys != 'CF' }">
<jsp:include page="./include/board_header.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/board/javascript/enboard_list.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/board/javascript/enboard_read.js"></script>

<script type="text/javascript">
	window.onload = ebList.initList;
</script>

<%-- 게시판 본체 --%>
<jsp:include page='<%="./skin/"+request.getAttribute("boardSkin")+"/board_list.jsp"%>'/>
 
<%-- 게시판 데이타 전송 Form --%>
<form name="setForm" method=post>
<input type="hidden" name="boardId"       value="<c:out value="${boardSttVO.boardId}"/>"/>
<input type="hidden" name="cmpBrdId"      value="<c:out value="${boardSttVO.cmpBrdId}"/>"/>
<input type="hidden" name="srchKey"       value="<c:out value="${boardSttVO.srchKey}"/>"/>
<input type="hidden" name="srchType"      value="<c:out value="${boardSttVO.srchType}"/>"/>
<input type="hidden" name="srchBgnReg"    value="<c:out value="${boardSttVO.srchBgnReg}"/>"/>
<input type="hidden" name="srchEndReg"    value="<c:out value="${boardSttVO.srchEndReg}"/>"/>
<input type="hidden" name="srchReplYn"    value="<c:out value="${boardSttVO.srchReplYn}"/>"/>
<input type="hidden" name="srchMemoYn"    value="<c:out value="${boardSttVO.srchMemoYn}"/>"/>
<input type="hidden" name="page"          value="<c:out value="${boardSttVO.page}"/>"/>
<input type="hidden" name="pageSize"      value="<c:out value="${boardSttVO.pageSize}"/>"/>
<input type="hidden" name="cateId"        value="<c:out value="${boardSttVO.cateId}"/>"/>
<input type="hidden" name="bltnCateId"    value="<c:out value="${boardSttVO.bltnCateId}"/>"/>
<input type="hidden" name="listOpt"       value="<c:out value="${boardSttVO.listOpt}"/>"/>
<input type="hidden" name="onlyReplYn"    value="<c:out value="${boardSttVO.onlyReplYn}"/>"/>
<input type="hidden" name="onlyMemoYn"    value="<c:out value="${boardSttVO.onlyMemoYn}"/>"/>
<input type="hidden" name="wallUserId"    value="<c:out value="${boardSttVO.wallUserId}"/>"/>
<input type="hidden" name="systemCurrentTimeMillis" value="1<c:out value="${boardSttVO.systemCurrentTimeMillis}"/>"/>
<input type="hidden" name="boardSkinDflt" value="<c:out value="${boardVO.boardSkinDflt}"/>"/>
<input type="hidden" name="miniTrgtWin"   value="<c:out value="${boardVO.miniTrgtWin}"/>"/>
<input type="hidden" name="miniTrgtUrl"   value="<c:out value="${boardVO.miniTrgtUrl}"/>"/>
<input type="hidden" name="bltnNo" value="" />
<%--다음 rtnBltnNo 는 목록화면에서 직접 읽기화면의 기능(e.g. 'eval-up')을 호출했을 때 필요로 된다.2012.07.05.KWShin.--%>
<input type="hidden" name="rtnBltnNo" 	value=""/>
<input type="hidden" name="cmd" 	value=""/>
<input type="hidden" name="rtnURI" 	value=""/>
<input type="hidden" name="userPass" 	value=""/>
<input type="hidden" name="langKnd" value="<c:out value="${boardVO.langKnd}"/>"/>
<input type="hidden" id="viewMode"  name="viewMode" value="<c:out value="${boardSttVO.viewMode }"/>"/>
<input type="hidden" name="boardTtlYn" value="<c:out value="${param.boardTtlYn}"/>"/>
</form>
<iframe name="download" title="download" width=0 height=0 style="border: none;"></iframe>
<jsp:include page="./include/board_footer.jsp"/>
</c:if>