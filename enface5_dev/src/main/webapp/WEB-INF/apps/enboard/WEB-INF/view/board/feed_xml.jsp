<%@ page contentType="text/xml;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<board title="<c:out value="${boardVO.boardTtl}"/>" uri="/bltnMngr?boardId=<c:out value="${boardVO.boardId}"/>">
  <c:forEach items="${bltnList}" var="list" end="7">         
	<article id="<c:out value="${list.boardRow}"/>" uri="/bltnMngr?boardId=<c:out value="${boardVO.boardId}"/>&amp;bltnNo=<c:out value="${list.bltnNo}"/>">
		<title><![CDATA[<c:out value="${list.bltnOrgSubj}" escapeXml="false"/>]]></title>
		<author><![CDATA[<c:out value="${list.userNick}" escapeXml="false"/>]]></author>
		<date><c:out value="${list.regDatim}"/></date>
		<contents><![CDATA[<c:out value="${list.bltnCntt}" escapeXml="false"/>]]></contents>
		<c:if test="${!empty list.fileList}">
		  <c:set var="fileList" value="${list.fileList}"/>
		  <c:forEach items="${fileList}" var="file">
		    <c:if test="${boardVO.mergeType == 'A'}">
		      <attachedFile uri="/fileMngr?cmd=down&amp;boardId=<c:out value="${boardVO.boardRid}"/>&amp;bltnNo=<c:out value="${file.bltnNo}"/>&amp;fileSeq=<c:out value="${file.fileSeq}"/>&amp;subId=sub06"/>
			</c:if>
		    <c:if test="${boardVO.mergeType != 'A'}">
		      <attachedFile uri="/fileMngr?cmd=down&amp;boardId=<c:out value="${list.eachBoardVO.boardRid}"/>&amp;bltnNo=<c:out value="${file.bltnNo}"/>&amp;fileSeq=<c:out value="${file.fileSeq}"/>&amp;subId=sub06"/>
			</c:if>
		  </c:forEach>
		</c:if>
	</article>
  </c:forEach>
</board>