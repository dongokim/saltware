
<%@ page contentType="text/xml; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<js>
    <status><c:out value="${inform.resultStatus}"/></status>
    <reason><c:out value="${inform.failureReason}"/></reason>    
    <latestApproval>

		<userId><![CDATA[<c:out value="${latestApproval.userId}"/>]]></userId>	
		<gwSubj><![CDATA[<c:out value="${latestApproval.gwSubj}"/>]]></gwSubj>	
		<recvYmd><![CDATA[<c:out value="${latestApproval.recvYmd}"/>]]></recvYmd>	
		<gwLink><![CDATA[<c:out value="${latestApproval.gwLink}"/>]]></gwLink>	
	</latestApproval>
</js>

