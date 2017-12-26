<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String cmd = request.getParameter("cmd");
	String boardId = request.getParameter("boardId");
	String bltnNo = request.getParameter("bltnNo");
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/kaist/board/css/default.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/kaist/board/css/popup_error.css"/>


<script>
  <!--
  if ("<c:out value="${baseForm.reasonCd}"/>" == "eb.error.must.auth.realMan") { // �Ǹ������� �޾ƾ� �ϴ� ��Ȳ�̸�
	if ("READ" == "<%=cmd%>") {
		location.href = "realManSet.jsp?rtnUrl=read.brd?boardId=<%=boardId%>&bltnNo=<%=bltnNo%>&cmd=<%=cmd%>";
	} else if ("WRITE" == "<%=cmd%>" || "REPLY" == "<%=cmd%>" || "MODIFY" == "<%=cmd%>") {
		location.href = "realManSet.jsp?rtnUrl=edit.brd?boardId=<%=boardId%>&bltnNo=<%=bltnNo%>&cmd=<%=cmd%>";
	} else {
		location.href = "realManSet.jsp?rtnUrl=list.brd?boardId=<%=boardId%>";
	}
  }
  //-->
</script>

<!-- wrap -->
	<div id="wrap" > 
		<h1><img src="<%=request.getContextPath()%>/kaist/images/portal/error/logo_01.gif" /></h1>
		<!-- text_box -->
		<div class="text_box">
			<ul>
				<li class="text"><em><img src="<%=request.getContextPath()%>/kaist/images/portal/error/error_img_01.gif" /> <c:out value="${baseForm.reason}"/> </em></li>
			</ul>
		</div>
		<!-- text_box //-->
		<!-- back_btn -->
		<ul class="back_btn">
			<li>
				<a href="javascript:window.history.back();">
					<c:if test="${langKnd == 'ko'}">이전 페이지</c:if>
					<c:if test="${langKnd == 'en'}">Previous Page</c:if> <br/>
				</a>
			</li>
		</ul>
		<!-- back_btn //-->
	</div>