<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String cmd = request.getParameter("cmd");
	String boardId = request.getParameter("boardId");
	String bltnNo = request.getParameter("bltnNo");
%>
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
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
  <tr>
    <td align="center" valign="middle">
      <table width="344" border="0" cellspacing="0" cellpadding="0" height="147">
        <tr>
          <td background='/board/images/board/skin/<%=request.getAttribute("boardSkin")%>/board_error_bg.gif' valign="bottom" align="center">&nbsp;
            <table width="240" border="0" cellspacing="0" cellpadding="0" height="100">
              <tr height=70 valign=middle>
                <td width="80" align="center">
                  <img src='/board/images/board/skin/<%=request.getAttribute("boardSkin")%>/alert.gif' width="38" height="41">
                </td>  
                <td style="font-family:Dotum;font-size:9pt;font-color:#999999" width="160" > 
                  <c:out value="${baseForm.reason}"/>
                </td>
              </tr>
              <tr height=30 valign=top>
                <td colspan=2 align=center>
                  <a onclick='window.history.back()'>
                    <img src='/board/images/board/skin/<%=request.getAttribute("boardSkin")%>/login_alert_history_btn.gif' width="83" height="18">
                  </a>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
