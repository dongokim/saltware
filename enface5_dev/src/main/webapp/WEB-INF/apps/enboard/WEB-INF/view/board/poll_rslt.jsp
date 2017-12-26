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
      <jsp:include page='<%="./skin/"+request.getAttribute("boardSkin")+"/poll_rslt.jsp"%>'/>
    </td>
  </tr>
</table>  
 
<!-- 게시판 데이타 전송 Form -->
<form name=setForm method=post>
  <input type=hidden name=boardSkinDflt value="<c:out value="${boardVO.boardSkinDflt}"/>"/>
  <input type=hidden name=miniTrgtWin   value="<c:out value="${boardVO.miniTrgtWin}"/>"/>
  <input type=hidden name=miniTrgtUrl   value="<c:out value="${boardVO.miniTrgtUrl}"/>"/>
  <input type=hidden name=bltnNo>
  <input type=hidden name=command>
  <input type=hidden name=rtnURI>
</form>
<%---------------------------------------------------------------------------------------------------------------------%>  
  </td>
  <td width=0><jsp:include page="./include/board_right.jsp"/></td>
</tr>
</table>
<jsp:include page="./include/board_footer.jsp"/>

