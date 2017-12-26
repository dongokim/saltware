<%@ page contentType="text/xml; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<js>
    <status><c:out value="${inform.resultStatus}"/></status>
    <reason><c:out value="${inform.failureReason}"/></reason>    
    <sample>

		<principalId><c:out value="${sample.principalId}"/></principalId>	
		<parentId><c:out value="${sample.parentId}"/></parentId>	
		<shortPath><![CDATA[  <c:out value="${sample.shortPath}"/>  ]]></shortPath>	
		<principalName><![CDATA[  <c:out value="${sample.principalName}"/>  ]]></principalName>	
		<theme><![CDATA[  <c:out value="${sample.theme}"/>  ]]></theme>	
		<siteName><![CDATA[  <c:out value="${sample.siteName}"/>  ]]></siteName>	
		<defaultPage><![CDATA[  <c:out value="${sample.defaultPage}"/>  ]]></defaultPage>	
		<principalType><![CDATA[  <c:out value="${sample.principalType}"/>  ]]></principalType>	
		<principalOrder><c:out value="${sample.principalOrder}"/></principalOrder>	
		<creationDate><c:out value="${sample.creationDateByFormat}"/></creationDate>	
		<modifiedDate><c:out value="${sample.modifiedDateByFormat}"/></modifiedDate>	
		<principalInfo01><![CDATA[  <c:out value="${sample.principalInfo01}"/>  ]]></principalInfo01>	
		<principalInfo02><![CDATA[  <c:out value="${sample.principalInfo02}"/>  ]]></principalInfo02>	
		<principalInfo03><![CDATA[  <c:out value="${sample.principalInfo03}"/>  ]]></principalInfo03>	
		<principalDesc><![CDATA[  <c:out value="${sample.principalDesc}"/>  ]]></principalDesc>
		
		<gradeId><c:out value="${sample.gradeId}"/></gradeId>		
	</sample>
</js>