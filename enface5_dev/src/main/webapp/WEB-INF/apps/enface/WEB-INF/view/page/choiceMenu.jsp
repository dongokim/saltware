<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.saltware.enview.security.EnviewMenu" %>
<%@ page import="java.util.List" %>
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
<title>Sitemap Test</title>
<body>
	<div style="overflow:auto; width:420px; height:230px;">
<%				
EnviewMenu rootMenu = (EnviewMenu)request.getAttribute("rootMenu");
//System.out.println("############################### menu=" + rootMenu.getName());
if( rootMenu != null && rootMenu.getElements() != null ) {
	Iterator it2 = rootMenu.getElements().iterator();
	for(; it2.hasNext(); tabordercnt++) {
		EnviewMenu element = (EnviewMenu)it2.next();
		String linkDescription = element.getTitle(langKnd);
		String linkTitle = element.getShortTitle(langKnd);
		String linkUrl = element.getFullUrl();
		String linkTarget = element.getTarget();
%>
	<table width="162" border="0" cellspacing="0" cellpadding="0"> 
		<tr>
			<td class="padd_left_bold"><img src="<%=themePath%>/images/left_icon.gif" /><a href="<%=linkUrl%>" target="<%=linkTarget%>" tabindex="<%=tabordercnt%>" title="<%=linkDescription%>" class="left"><%=linkTitle%></a></td>
		</tr>
		<tr>
			<td><img src="<%=themePath%>/images/leftmenu_line.gif" /></td>
		</tr>
		
<%		if( element.getElements() != null ) { %>
		<tr><td style="padding-left: 50px;">
			<table width="162" border="0" cellspacing="0" cellpadding="0">
<%			Iterator it3 = element.getElements().iterator();
			for(; it3.hasNext(); tabordercnt++) {
				EnviewMenu subelement = (EnviewMenu)it3.next();
				String subLinkDescription = subelement.getTitle(langKnd);
				String subLinkTitle = subelement.getShortTitle(langKnd);
				String subLinkUrl = subelement.getFullUrl();
				String subLinkTarget = subelement.getTarget(); %>
				<tr>
					<td class="padd_left"><img src="<%=themePath%>/images/left_icon.gif" /><a href="<%=subLinkUrl%>" target="<%=subLinkTarget%>" tabindex="<%=tabordercnt%>" title="<%=subLinkDescription%>" class="left"><%=subLinkTitle%></a></td>
				</tr>
				<tr>
					<td><img src="<%=themePath%>/images/leftmenu_line.gif" /></td>
				</tr>
<%				if( subelement.getElements() != null ) { %>
					<tr><td style="padding-left: 50px;">
						<table width="162" border="0" cellspacing="0" cellpadding="0">
<%					Iterator it4 = subelement.getElements().iterator();
						for(; it4.hasNext(); tabordercnt++) {
							EnviewMenu subelement2 = (EnviewMenu)it4.next();
							String subLinkDescription2 = subelement2.getTitle(langKnd);
							String subLinkTitle2 = subelement2.getShortTitle(langKnd);
							String subLinkUrl2 = subelement2.getFullUrl();
							String subLinkTarget2 = subelement2.getTarget(); %>
							<tr>
								<td class="padd_left"><img src="<%=themePath%>/images/left_icon.gif" /><a href="<%=subLinkUrl2%>" target="<%=subLinkTarget2%>" tabindex="<%=tabordercnt%>" title="<%=subLinkDescription2%>" class="left"><%=subLinkTitle2%></a></td>
							</tr>
							<tr>
								<td><img src="<%=themePath%>/images/leftmenu_line.gif" /></td>
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
</body>
</html>
