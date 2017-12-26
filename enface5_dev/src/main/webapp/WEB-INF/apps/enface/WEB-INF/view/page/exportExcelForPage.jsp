<%@ page contentType="application/vnd.ms-excel;charset=UTF-8"%> 
<%@ page buffer = "1024kb" %>
<%@ page language="java"   isThreadSafe="false" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<html>

<head>
	<title></title>
	<meta http-equiv="Content-Disposition" content="attachment; filename=pageList.xls"> </meta>
	<meta http-equiv="Content-Description" content="JSP Generated Data"> </meta>
	<meta http-equiv="Content-Type" content="charset=UTF-8" />
</head>

<body>
		  
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<thead>
		  <tr> 
			<td colspan="7" class="webgridheaderline"></td>
		  </tr>
		  
		  <tr style="cursor: pointer;">
			<th class="webgridheader" align="center">No</th>
			<th class="webgridheader" ch="0" align="left"><util:message key="pt.ev.property.page.title"/>&nbsp;</th>
			<th class="webgridheader" ch="0" align="left"><util:message key="pt.ev.property.page.path"/>&nbsp;</th>
			<th class="webgridheader" ch="0" ><span ><util:message key="pt.ev.property.page.name"/>&nbsp;</span></th>
			<th class="webgridheader" ch="0" ><span >Menu Icon&nbsp;</span></th>
			<th class="webgridheader" ch="0" ><span >Title Icon&nbsp;</span></th>
			<th class="webgridheaderlast" ch="0" ><span >Quickmenu Icon&nbsp;</span></th>
		  </tr>
		</thead>
		<tbody id="StatisticsManager.ListBody">
<%	List resultList = (List)request.getAttribute("pageList");
	Iterator it = resultList.iterator();
	for(int i=0; it.hasNext(); i++) { %>		
			<tr>
				<td align="center"><%= i %></td>
<%		Map row = (Map)it.next();
		for(Iterator it2=row.keySet().iterator(); it2.hasNext(); ) {
			String key = (String)it2.next(); %>
				<td><%=(String)row.get(key)%></td>
<%		} %>
			</tr>
<%	} %>
		</tbody>
	</table>
</body>

</html>

