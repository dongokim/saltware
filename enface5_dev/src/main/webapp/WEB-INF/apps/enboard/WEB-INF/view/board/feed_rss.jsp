<%@page import="java.util.List"%>
<%@page import="com.saltware.enboard.vo.BulletinVO"%>
<%@page import="com.saltware.enface.util.StringUtil"%>
<%@page import="com.saltware.enview.sso.EnviewSSOManager"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@ page contentType="text/xml;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String host = (request.isSecure() ? "https://" : "http://") + request.getServerName() + ( request.getServerPort()!=80 && request.getServerPort()!=443 ? ":" + request.getServerPort() : "" );
	//String langKnd = EnviewSSOManager.getUserInfo(request).getLocale();
	String langKnd = StringUtil.isNull2(request.getParameter("langKnd"), "ko");
	if (!("en".equals(langKnd) || "ko".equals(langKnd))) {
		langKnd = (String) session.getAttribute("langKnd");
	}
	String rssPubDate = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss Z", Locale.US).format(new Date());
	pageContext.setAttribute("host", host);
	pageContext.setAttribute("rssPubDate", rssPubDate);
%>
<rss version="2.0">
	<channel>
		<title><c:out value="${boardVO.boardTtl}"/></title>
		<link><c:out value="${host }" />/enboard/<c:out value="${boardVO.boardId}"/></link>
		<description></description>
		<language><c:out value="${langKnd }" /></language>
		<pubDate><c:out value="${rssPubDate }"/></pubDate>
		<generator></generator>
		<managingEditor>snu@snu.ac.kr</managingEditor>
		<%-- <c:forEach items="${bltnList}" var="list"> --%>
		<%
		// 20170106 RSS <pubDate> Format 변경
		List list = (List) request.getAttribute("bltnList");
		for (int i = 0; i < list.size() ; i++) {
			BulletinVO vo = (BulletinVO) list.get(i);
			request.setAttribute("list", vo);
		%>
			<item>
				<title><![CDATA[<c:out value="${list.bltnOrgSubj}" escapeXml="false"/>]]></title>
				<link><c:out value="${host }" />/apiboard/<c:out value="${boardVO.boardId}"/>/<c:out value="${list.bltnNo }" /></link>
				<description><![CDATA[<c:out value="${list.bltnCntt}" escapeXml="false"/>]]></description>
				<category></category>
				<author><![CDATA[<c:out value="${list.userNick}" escapeXml="false"/>]]></author>
				<guid><c:out value="${host }" />/apiboard/<c:out value="${boardVO.boardId}"/>/<c:out value="${list.bltnNo }" /></guid>
				<comments><c:out value="${host }" />/apiboard/<c:out value="${boardVO.boardId}"/>/<c:out value="${list.bltnNo }" /></comments>
				<pubDate><%=new SimpleDateFormat("E, d MMM yyyy HH:mm:ss Z", Locale.US).format(vo.getRegDatim())%></pubDate>
				<c:if test="${!empty list.fileList}">
					<c:set var="fileList" value="${list.fileList}"/>
					<c:forEach items="${fileList}" var="file">
						<c:if test="${boardVO.mergeType == 'A'}">
							<attachedFile uri="<c:out value="${host }" />/board/fileMngr?cmd=down&amp;boardId=<c:out value="${boardVO.boardRid}"/>&amp;bltnNo=<c:out value="${file.bltnNo}"/>&amp;fileSeq=<c:out value="${file.fileSeq}"/>&amp;subId=sub06"/>
						</c:if>
						<c:if test="${boardVO.mergeType != 'A'}">
							<attachedFile uri="<c:out value="${host }" />/board/fileMngr?cmd=down&amp;boardId=<c:out value="${list.eachBoardVO.boardRid}"/>&amp;bltnNo=<c:out value="${file.bltnNo}"/>&amp;fileSeq=<c:out value="${file.fileSeq}"/>&amp;subId=sub06"/>
						</c:if>
					</c:forEach>
				</c:if>
			</item>
		<%-- </c:forEach> --%>
		<%
		}
		%>
	</channel>
</rss>