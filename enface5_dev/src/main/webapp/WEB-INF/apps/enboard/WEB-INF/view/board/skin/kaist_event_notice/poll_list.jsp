<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link href="<%=request.getContextPath()%>/board/css/board/skin/enboard/bbs.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
  body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>

<div class="board">
  <%-- contents start --%>
  <table width="800" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="top" >
      <%--리스트시작 --%>
	  <form name="pollForm"><%--폼이름은 반드시 'pollForm'이어야 한다(javascript에서 참조됨)--%>
	  <table width=100% border="0" cellspacing="0" cellpadding="0">
	    <tr>
	      <td height="2" colspan="3" bgcolor="blue"></td>
	    </tr>
	    <tr>
          <td width="40" align="center" background="<%=request.getContextPath()%>/board/images/board/skin/enboard/bg_t_title.gif" class="table_title"><b>번호</b></td>
	      <td width="3" background="<%=request.getContextPath()%>/board/images/board/skin/enboard/bg_t_title.gif"><img src="<%=request.getContextPath()%>/board/images/board/skin/enboard/img_t_bar.gif" width="3" height="30"></td>
	      <td align="center" background="<%=request.getContextPath()%>/board/images/board/skin/enboard/bg_t_title.gif" class="table_title"><b>문항</b></td>
	    </tr>

        <%-- Bulletins are not exist.. --%>
        <c:if test="${empty bltnList}">
        <tr><td height="1" colspan="3" bgcolor="#dbdee7" ></td></tr>
        <tr height=22>
          <td align="center" colspan=3>등록된 설문문항이 존재하지 않습니다.<input type=hidden name=chk></td>
        </tr>
        <tr><td height="2" colspan="3" bgcolor="#dbdee7" ></td></tr>
        </c:if>

        <%-- List of Bulletin --%>
        <c:forEach items="${bltnList}" var="list">         
        <tr><td height="1" colspan="3" bgcolor="#dbdee7" ></td></tr>
        <tr onMouseOver=this.style.backgroundColor='#E8ECF9' onMouseOut=this.style.backgroundColor=''>
          <td valign="top" class="table_list_c">
            <c:out value="${list.bltnGq}"/>
          </td>
          <td width="3" nowrap></td>
          <td class="table_list_l">
            <c:out value="${list.bltnOrgCntt}" escapeXml="false"/><br>
            <c:if test="${!empty list.pollList}">
              <c:set var="pollAs" value="${list.pollList}"/>
			  <table>
		        <colgroup width='40'/>
		        <colgroup width='40'/>
		        <colgroup width='680'/>
                <c:forEach items="${pollAs}" var="pList">           
			    <tr>
				  <td valign="top" class="table_list_c">(<c:out value="${pList.pollSeq}"/>).</td>
			      <c:if test="${pList.pollKind == 'A'}">
				    <td class="table_list_c">
				      <input type="radio" name='poll<c:out value="${list.bltnGq}"/>'
              	             id='poll<c:out value="${list.bltnGq}"/>'
						     value='<c:out value="${pList.pollSeq}"/>'
							 <c:if test="${list.polledSeq == pList.pollSeq}">checked</c:if>
					  /> 
				    </td>
				    <td class="table_list_l"><c:out value="${pList.pollCntt}" escapeXml="false"/></td>
				  </c:if>
			      <c:if test="${pList.pollKind == 'B'}">
				    <td colspan="2" class="table_list_l">
					  <c:if test="${!empty pList.pollCntt}">
					    <c:out value="${pList.pollCntt}" escapeXml="false"/><br>
					  </c:if>
                      <%--textarea는 태그 사이에 있는 문자를 모두 value로 인식하므로 꽉 붙여야 한다.2010.05.05.KWShin.--%>
					  <textarea name='pollReply<c:out value="${list.bltnGq}"/>'
					            id='pollReply<c:out value="${list.bltnGq}"/>' 
								pollSeq='<c:out value="${pList.pollSeq}"/>' cols="80" rows="2"
								onfocus="ebList.actionPoll('selTA','','',<c:out value="${list.bltnGq}"/>)"
					  ><c:out value="${list.polledReply}" escapeXml="false"/></textarea>
				      <input type="radio" name='poll<c:out value="${list.bltnGq}"/>'
              	             id='poll<c:out value="${list.bltnGq}"/>'
						     value='<c:out value="${pList.pollSeq}"/>'
							 <c:if test="${list.polledSeq == pList.pollSeq}">checked</c:if>
							 style='display:none'
					  /> 
					</td>
				  </c:if>
			    </tr>
				</c:forEach>
			  </table>
			</c:if>
		  </td>
        </tr>
        </c:forEach>
        <%-- End of List of Bulletin --%>
        <tr><td height="2" colspan="3" bgcolor="#dbdee7" ></td></tr>
      </table>
	  <input type="hidden" name="boardId"    value="<c:out value="${boardSttVO.boardId}"/>">
	  <input type="hidden" name="cmpBrdId"   value="<c:out value="${boardSttVO.boardId}"/>"><%--목록화면이지만 설문참여처리는 읽기화면에서 이루어지는 모양새이므로 반드시 필요--%>
	  <input type="hidden" name="cmd"        value="<c:out value="${boardSttVO.cmd}"/>">
	  <input type="hidden" name="totalBltns" value="<c:out value="${boardSttVO.totalBltns}"/>">
      </form>
      <%--리스트끝 --%>
    </td>
  </tr>
  <tr><td height="11" align="right" background="<%=request.getContextPath()%>/board/images/board/skin/enboard/bg_dot.gif"></td></tr>
  <tr>
    <td align="right">
      <table width=100% border="0" cellspacing="0" cellpadding="0">
        <tr>
          <c:if test="${boardVO.isPolled == 'true'}">
		    이미 참여하셨습니다.
		  </c:if>
          <c:if test="${boardVO.isPolled == 'false'}">
		  <c:if test="${boardVO.writableYn == 'Y'}">
            <td width=80 align="right">
			  <c:if test="${!empty bltnList}">
			    <a onClick="ebList.actionPoll('poll-multi')" style=cursor:pointer><c:out value="${boardVO.imgSmallPoll}" escapeXml="false"/></a>
              </c:if>				
			</td>
          </c:if>
		  </c:if>
        </tr>
      </table>
    </td>
  </tr>
  <tr><td height="11" align="right" background="<%=request.getContextPath()%>/board/images/board/skin/enboard/bg_dot.gif"></td></tr>
  </table>
</div>
