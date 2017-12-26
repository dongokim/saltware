<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

        <c:forEach items="${bltnList}" var="list" varStatus="status">         
        <hr/>
        <li align=left>
		  <div><font style="color:blue;font-weight:bold;font-size:18px"><c:out value="${list.boardNm}"/></font></div>
		  <div style=float:right><c:out value="${list.smartRegDatimSF}"/></div>
		</li>
		<li align=left
			<c:if test="${list.readable == 'true'}">
			  onclick="ebList.readBulletin('<c:out value="${list.boardId}"/>','<c:out value="${list.bltnNo}"/>')"
			</c:if>
			<c:if test="${list.readable == 'false'}">
			  onclick="alert('작성자가 글읽기를 허용하지 않았습니다');"
			</c:if>
		  >
		    <font style="font-weight:bold;font-size:16px">
			  <c:out value="${list.bltnOrgSubj}" escapeXml="false"/>
			</font>
		</li>
		<li align=left style="padding-left:5px;word-wrap:break-word;word-break:break-all"
		    <c:if test="${list.readable == 'true'}">
			  onclick="ebList.readBulletin('<c:out value="${list.boardId}"/>','<c:out value="${list.bltnNo}"/>')"
			</c:if>
			<c:if test="${list.readable == 'false'}">
			  onclick="alert('작성자가 글읽기를 허용하지 않았습니다');"
			</c:if>
		  >
		    <c:out value="${list.bltnOrgCntt}" escapeXml="false"/>
		</li>
		<li align=center>
		  <span id="likeSpan<c:out value="${boardSttVO.page}"/><c:out value="${status.index}"/>" style="margin-right:10px">
		    <c:if test="${list.bltnRcmded != 'Y'}">
		    <a style=cursor:pointer onclick="ebList.like('<c:out value="${list.bltnNo}"/>',document.getElementById('likeSpan<c:out value="${boardSttVO.page}"/><c:out value="${status.index}"/>'),document.getElementById('likeCntSpan<c:out value="${boardSttVO.page}"/><c:out value="${status.index}"/>'))">좋아요</a>
			</c:if>
		    <c:if test="${list.bltnRcmded == 'Y'}">
		    좋아했음
			</c:if>
		  </span>
		  <span>
		    <a style=cursor:pointer onclick="ebList.readBulletin('<c:out value="${list.boardId}"/>','<c:out value="${list.bltnNo}"/>')">댓글달기</a>
		  </span>
		  <span style="margin-left:30px">&nbsp;</span>
		  <span style="margin-right:10px">
			좋아요(<span id="likeCntSpan<c:out value="${boardSttVO.page}"/><c:out value="${status.index}"/>" name="likeCntSpan"><c:out value="${list.bltnRcmdUpCntCF}"/></span>)
		  </span>
		  <span>
			댓글(<c:out value="${list.bltnMemoCnt}"/>)
		  </span>
		</li>
		<li><span height=10>&nbsp;</span></li>
        </c:forEach>
