<?xml version="1.0" encoding="UTF-8"?>
<%@ page contentType="text/xml;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<response><c:forEach items="${bltnVOs}" var="bltn"><c:forEach items="${bltn.memoList}" var="memo">
	<resource>
	   	<id>${memo.memoSeq}</id>
	   	<content><![CDATA[${memo.memoCntt}]]></content>
	   	<created_at>${memo.regDatimF}</created_at>
	   	<user>
	   		<first_name>${memo.userNick}</first_name>
	   		<last_name></last_name>
	   	</user>
		<attachments>
	   	</attachments>
	</resource>   	
</c:forEach></c:forEach></response>