<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="./include/board_header.jsp"/>
<table border="0" width="100%" cellspacing="0" cellpadding="0" height="100%">
<tr>
  <td width=0><jsp:include page="./include/board_left.jsp"/></td>
  <td valign=top>
<%---------------------------------------------------------------------------------------------------------------------%>  
<title><c:out value="${boardVO.boardTtl}"/></title>
 
<script type="text/javascript" src="./javascript/enboard_list.js"></script>
<script type="text/javascript">
	<!--
	window.onload = ebList.initList;
	//-->
</script>

<table border=0 cellspacing=0 cellpadding=0 align=center width=100%>
  <tr>
    <td align=center> 
      <jsp:include page='<%="./skin/"+request.getAttribute("boardSkin")+"/poll_list.jsp"%>'/>
    </td>
  </tr>
</table>  
 
<!-- 게시판 데이타 전송 Form -->
<form name="setForm" method="post">
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
  <input type="hidden" name="systemCurrentTimeMillis" value="1<c:out value="${boardSttVO.systemCurrentTimeMillis}"/>"/>
  <input type="hidden" name="boardSkinDflt" value="<c:out value="${boardVO.boardSkinDflt}"/>"/>
  <input type="hidden" name="miniTrgtWin"   value="<c:out value="${boardVO.miniTrgtWin}"/>"/>
  <input type="hidden" name="miniTrgtUrl"   value="<c:out value="${boardVO.miniTrgtUrl}"/>"/>
  <input type="hidden" name="bltnNo">
  <input type="hidden" name="command">
  <input type="hidden" name="rtnURI">
</form>
<iframe name="download" src="blank.brd" width="0" height="0"></iframe>
<%---------------------------------------------------------------------------------------------------------------------%>  
  </td>
  <td width=0><jsp:include page="./include/board_right.jsp"/></td>
</tr>
</table>
<jsp:include page="./include/board_footer.jsp"/>
