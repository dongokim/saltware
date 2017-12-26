<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.saltware.enview.security.EnviewMenu" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Iterator" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>


<%
	String langKnd = "ko";
	String themePath = request.getContextPath() + "/decorations/layout/enview";
	int tabordercnt = 0;
%>

<html>
<head>
<title>Sitemap Test</title>
</head>
<body>
	<form id='MenuSelectListForm' style='display:inline' name='MenuSelectListForm' action='' method='post'>
	<div style="overflow:auto; width:420px; height:230px; border: 1px solid lightgray;">
<%			
Map myMenuMap = (Map)request.getAttribute("myMenuMap");	
EnviewMenu rootMenu = (EnviewMenu)request.getAttribute("rootMenu");
if( rootMenu != null && rootMenu.getElements() != null ) {
	Iterator it2 = rootMenu.getElements().iterator();
	for(; it2.hasNext(); tabordercnt++) {
		EnviewMenu element = (EnviewMenu)it2.next();
		String linkDescription = element.getTitle(langKnd);
		String linkTitle = element.getShortTitle(langKnd);
		String linkUrl = element.getFullUrl();
		String linkTarget = element.getTarget();
		String isMyMenu = ((myMenuMap.containsKey(element.getId()) == true) ? "checked=\"true\"" : "");
		//System.out.println("############################################ isMyMenu=" + isMyMenu);
%>
	<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
		<tr>
			<td class="padd_left_bold"><input type="checkBox" id="<%=element.getId()%>" <%=isMyMenu%>>&nbsp;<%=linkTitle%></td>
		</tr>
		
<%		if( element.getElements() != null ) { %>
		<tr><td style="padding-left: 50px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%			Iterator it3 = element.getElements().iterator();
			for(; it3.hasNext(); tabordercnt++) {
				EnviewMenu subelement = (EnviewMenu)it3.next();
				String subLinkDescription = subelement.getTitle(langKnd);
				String subLinkTitle = subelement.getShortTitle(langKnd);
				String subLinkUrl = subelement.getFullUrl();
				String subLinkTarget = subelement.getTarget(); 
				String isSubMyMenu = ((myMenuMap.containsKey(subelement.getId()) == true) ? "checked=\"true\"" : ""); %>
				<tr>
					<td class="padd_left"><input type="checkBox" id="<%=subelement.getId()%>" <%=isSubMyMenu%>>&nbsp;<%=subLinkTitle%></td>
				</tr>
<%				if( subelement.getElements() != null ) { %>
					<tr><td style="padding-left: 50px;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%					Iterator it4 = subelement.getElements().iterator();
						for(; it4.hasNext(); tabordercnt++) {
							EnviewMenu subelement2 = (EnviewMenu)it4.next();
							String subLinkDescription2 = subelement2.getTitle(langKnd);
							String subLinkTitle2 = subelement2.getShortTitle(langKnd);
							String subLinkUrl2 = subelement2.getFullUrl();
							String subLinkTarget2 = subelement2.getTarget(); 
							String isSubMyMenu2 = ((myMenuMap.containsKey(subelement2.getId()) == true) ? "checked=\"true\"" : ""); %>
							<tr>
								<td class="padd_left"><input type="checkBox" id="<%=subelement2.getId()%>" <%=isSubMyMenu2%>>&nbsp;<%=subLinkTitle2%></td>
							</tr>
<%						} %>
						</table>
					</td></tr>
<%					} %>
<%			} %>
			</table>
		</td></tr>
<%		} %>
	</table>
<%	}
} %>
	</div>
	</form>
</body>
</html>
