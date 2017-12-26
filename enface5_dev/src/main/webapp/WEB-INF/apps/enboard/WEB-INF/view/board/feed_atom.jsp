<%@ page contentType="text/xml;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%--ATOM 표준 버전으로 바꾸어야 함.2012.08.09.KWShin.--%>
<feed xmlns="http://www.w3.org/2005/Atom">
    <title><c:out value="${boardVO.boardTtl}"/></title>
    <link rel="alternate" type="text/html" href="/board/bltnMngr?boardId=<c:out value="${boardVO.boardId}"/>" />
    <link rel="self" type="application/atom+xml" href="/board/atom.brd?boardId=<c:out value="${boardVO.boardId}"/>" />
    <id><c:out value="${boardVO.boardId}"/></id>
    <updated></updated>
    <generator uri="http://saltware.co.kr/">Saltware RSS 1.0</generator>
	<c:forEach items="${bltnList}" var="list">         
		<entry>
			<title><![CDATA[<c:out value="${list.bltnOrgSubj}" escapeXml="false"/>]]></title>
			<link rel="alternate" type="text/html" href="/board/bltnMngr?boardId=<c:out value="${boardVO.boardId}"/>&amp;bltnNo=<c:out value="${list.bltnNo}"/>" />
			<id><c:out value="${boardVO.boardId}"/>:<c:out value="${list.boardRow}"/></id>
			<published><c:out value="${list.regDatim}"/></published>
			<updated><c:out value="${list.updDatim}"/></updated>
			<summary><![CDATA[<c:out value="${list.bltnCntt}" escapeXml="false"/>]]></summary>
			<author>
				<name><![CDATA[<c:out value="${list.userNick}" escapeXml="false"/>]]></name>
				<uri></uri>
			</author>
			<content type="html" xml:lang="en" xml:base="http://portal.saltware.co.kr:8081/">
				<![CDATA[<c:out value="${list.bltnCntt}" escapeXml="false"/>]]>
			</content>
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
		</entry>
	</c:forEach>
</feed>