<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.saltware.enview.security.EnviewMenu" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Iterator" %>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
java.util.Set myMenuSet = (java.util.Set)request.getAttribute("myMenuSet");	
List menuList = (List)request.getAttribute("results");
if( menuList != null ) {
	Iterator it2 = menuList.iterator();
	for(; it2.hasNext(); tabordercnt++) {
		EnviewMenu element = (EnviewMenu)it2.next();
		String linkDescription = element.getTitle();
		String linkTitle = element.getShortTitle();
		String linkUrl = element.getFullUrl();
		String linkTarget = element.getTarget();
		String isMyMenu = ((myMenuSet.contains(element.getPageId()) == true) ? "checked=\"true\"" : "");
		//System.out.println("############################################ isMyMenu=" + isMyMenu);
%>
	<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
		<tr>
			<td class="padd_left_bold"><input type="checkBox" id="<%=element.getPageId()%>" <%=isMyMenu%>>&nbsp;<%=linkTitle%></td>
		</tr>
<%		if( element.getElements() != null ) { %>
		<tr><td style="padding-left: 50px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%			Iterator it3 = element.getElements().iterator();
			for(; it3.hasNext(); tabordercnt++) {
				EnviewMenu subelement = (EnviewMenu)it3.next();
				String subLinkDescription = subelement.getTitle();
				String subLinkTitle = subelement.getShortTitle();
				String subLinkUrl = request.getContextPath() + subelement.getFullUrl();
				String subLinkTarget = subelement.getTarget(); 
				String isSubMyMenu = ((myMenuSet.contains(subelement.getPageId()) == true) ? "checked=\"true\"" : ""); %>
				<tr>
					<td class="padd_left"><input type="checkBox" id="<%=subelement.getPageId()%>" <%=isSubMyMenu%>>&nbsp;<%=subLinkTitle%></td>
				</tr>
<%				if( subelement.getElements() != null ) { %>
					<tr><td style="padding-left: 50px;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%					Iterator it4 = subelement.getElements().iterator();
						for(; it4.hasNext(); tabordercnt++) {
							EnviewMenu subelement2 = (EnviewMenu)it4.next();
							String subLinkDescription2 = subelement2.getTitle();
							String subLinkTitle2 = subelement2.getShortTitle();
							String subLinkUrl2 = request.getContextPath() + subelement2.getFullUrl();
							String subLinkTarget2 = subelement2.getTarget(); 
							String isSubMyMenu2 = ((myMenuSet.contains(subelement2.getPageId()) == true) ? "checked=\"true\"" : ""); %>
							<tr>
								<td class="padd_left"><input type="checkBox" id="<%=subelement2.getPageId()%>" <%=isSubMyMenu2%>>&nbsp;<%=subLinkTitle2%></td>
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
