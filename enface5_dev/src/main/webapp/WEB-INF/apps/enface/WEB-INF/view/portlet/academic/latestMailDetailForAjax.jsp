
<%@ page contentType="text/xml; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<js>
    <status><c:out value="${inform.resultStatus}"/></status>
    <reason><c:out value="${inform.failureReason}"/></reason>    
    <latestMail>

		<userId><![CDATA[<c:out value="${latestMail.userId}"/>]]></userId>	
		<mailSubj><![CDATA[<c:out value="${latestMail.mailSubj}"/>]]></mailSubj>	
		<recvYmd><![CDATA[<c:out value="${latestMail.recvYmd}"/>]]></recvYmd>	
		<mailLink><![CDATA[<c:out value="${latestMail.mailLink}"/>]]></mailLink>	
	</latestMail>
</js>

