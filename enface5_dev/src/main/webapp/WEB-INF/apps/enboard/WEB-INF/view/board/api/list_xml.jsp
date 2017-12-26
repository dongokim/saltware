<?xml version="1.0" encoding="UTF-8"?>
<%@ page contentType="text/xml;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<response>
<c:forEach items="${bltnList}" var="bltn">	<resource>
		<id>${bltn.bltnNo}</id>
		<slug>${bltn.boardId}</slug>
		<title><![CDATA[${bltn.bltnOrgSubj}]]></title>
		<content><![CDATA[${bltn.bltnOrgCntt}]]></content>
		<%-- <title_ko><![CDATA[${bltn.bltnOrgSubj}]]></title_ko>
		<content_ko><![CDATA[${bltn.bltnOrgCntt}]]></content_ko> --%>
		<read_count>${bltn.bltnReadCnt}</read_count>
		<created_at>${bltn.regDatimF}</created_at>
		<user>
			<first_name>${bltn.userNick}</first_name>
			<last_name></last_name>
		</user>
		<attachments><c:if test="${!empty bltn.fileList}"> <c:set var="fileList" value="${bltn.fileList}"/><c:forEach items="${fileList}" var="file">
			<resource>
				<id>${file.fileSeq}</id>
				<filename>${file.fileName}</filename>
				<c:if test="${boardVO.mergeType == 'A'}"><download_path>/fileMngr?cmd=down&amp;boardId=<c:out value="${boardVO.boardRid}"/>&amp;bltnNo=<c:out value="${file.bltnNo}"/>&amp;fileSeq=<c:out value="${file.fileSeq}"/>&amp;subId=sub06</download_path></c:if><c:if test="${boardVO.mergeType != 'A'}"><download_path>/fileMngr?cmd=down&amp;boardId=<c:out value="${list.eachBoardVO.boardRid}"/>&amp;bltnNo=<c:out value="${file.bltnNo}"/>&amp;fileSeq=<c:out value="${file.fileSeq}"/>&amp;subId=sub06</download_path></c:if>
			</resource></c:forEach></c:if>
		</attachments>
	</resource>
 </c:forEach></response>