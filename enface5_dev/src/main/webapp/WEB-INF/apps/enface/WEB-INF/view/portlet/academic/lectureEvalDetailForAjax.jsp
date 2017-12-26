
<%@ page contentType="text/xml; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<js>
    <status><c:out value="${inform.resultStatus}"/></status>
    <reason><c:out value="${inform.failureReason}"/></reason>    
    <lectureEval>

		<lectYear><![CDATA[<c:out value="${lectureEval.lectYear}"/>]]></lectYear>	
		<lectSeme><![CDATA[<c:out value="${lectureEval.lectSeme}"/>]]></lectSeme>	
		<subjName><![CDATA[<c:out value="${lectureEval.subjName}"/>]]></subjName>	
		<evalCount><c:out value="${lectureEval.evalCount}"/></evalCount>	
		<regCount><c:out value="${lectureEval.regCount}"/></regCount>	
		<evalScore><c:out value="${lectureEval.evalScore}"/></evalScore>	
	</lectureEval>
</js>

