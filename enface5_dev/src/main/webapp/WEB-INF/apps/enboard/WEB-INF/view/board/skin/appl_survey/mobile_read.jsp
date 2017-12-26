<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="board" style="padding:0px 5px 0px 5px">
  <c:forEach items="${bltnVOs}" var="list">
  <table width=100% border=0 cellspacing=0 cellpadding=0>
    <tr>
	  <td align=left>
		<font style="color:blue;font-weight:bold;font-size:18px"><c:out value="${boardVO.boardNm}"/></font>
	  </td>
	  <td align=right>
		  <c:if test="${list.bltnRcmded != 'Y'}">
		    <a style=cursor:pointer onclick="ebRead.actionBulletin('eval-up','<c:out value="${list.bltnNo}"/>')">좋아요</a>
		  </c:if>
		  <c:if test="${list.bltnRcmded == 'Y'}">
		    좋아했음
		  </c:if>
	  </td>
	</tr>
  </table>
  <table width=100% border=0 cellspacing=0 cellpadding=0>
	<tr>
	  <td width=38 align=left valign=top>
		  <img src="/board/images/board/skin/<c:out value="${boardSkin}"/>/no_user_pic.png"/>
	  </td>
	  <td align=left style="padding-left:5px">
	    <table width=100% border=0 cellspacing=0 cellpadding=0>
		  <tr>
		    <td align=left>
			  <b><c:out value="${list.userNick}"/></b>
			</td>
			<td align=right>
			  <c:out value="${list.smartRegDatimSF}"/>
			</td>
		  </tr>
		  <tr>
			<td colspan=2 align=left style="word-wrap:break-word;word-break:break-all">
			  <c:out value="${list.bltnOrgCntt}" escapeXml="false"/>
			</td>
		  </tr>
		</table>
	  </td>
	</tr>
  </table>
  <table width=100% border=0 cellspacing=0 cellpadding=0>
    <c:if test="${list.bltnRcmdUpCnt > 0}">
	<tr><td colspan=2><font color=blue><b>♥<c:out value="${list.bltnRcmdUpCntCF}"/>&nbsp;명이 좋아합니다.</b></font></td></tr>
	</c:if>
	<c:forEach items="${list.memoList}" var="mList">
	<tr>
	  <td width=38 align=left valign=top>
		  <img src="/board/images/board/skin/<c:out value="${boardSkin}"/>/no_user_pic.png"/>
	  </td>
	  <td align=left style="padding-left:5px">
	    <table width=100% border=0 cellspacing=0 cellpadding=0>
		  <tr>
		    <td align=left>
			  <b><c:out value="${mList.userNick}"/></b>
			</td>
			<td align=right>
			  <c:out value="${mList.smartRegDatimSF}"/>
			</td>
		  </tr>
		  <tr>
			<td colspan=2 align=left style="word-wrap:break-word;word-break:break-all">
			  <c:out value="${mList.memoCntt}" escapeXml="false"/>
			</td>
		  </tr>
		</table>
	  </td>
	</tr>
	</c:forEach>
  </table>
  <form name=memoForm_<c:out value="${list.bltnNo}"/>>
  <table width=100% border=0 cellspacing=0 cellpadding=0>
	<tr>
      <td align=left>
        <textarea name=memoCntt style="overflow:visible;height:40;width:100%"></textarea>
	  </td>
      <td width=50 align=right>
        <a name=memoBttn 
			<c:if test="${boardVO.memoWritableYn == 'Y'}">
			  onclick="ebRead.actionBulletin('write-memo',<c:out value="${list.bltnNo}"/>);return false;"
			</c:if>
		/>
          등록
        </a>
      </td>
	</tr>
  </table>
  <input type=hidden name=userNick
		<c:if test="${boardVO.anonYn == 'N'}"><%--익명게시판이 아닌 경우--%>
		  value="<c:out value="${secPmsnVO.userNick}"/>"
		</c:if>
		<c:if test="${boardVO.anonYn == 'Y'}"><%--익명게시판인 경우--%>
		  value=anonymous
		</c:if>
  />
  </form>
  </c:forEach>
</div>
