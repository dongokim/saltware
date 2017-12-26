<%@ page contentType="text/xml; charset=UTF-8"%>
<%@ page import="com.saltware.enview.security.EnviewMenu" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Iterator" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<%				
StringBuffer buff = new StringBuffer();
String langKnd = (String)request.getAttribute("langKnd");
EnviewMenu rootMenu = (EnviewMenu)request.getAttribute("rootMenu");
if( rootMenu != null && rootMenu.getElements() != null ) {
	Iterator it = rootMenu.getElements().iterator();
	for(int i=0; it.hasNext(); i++) {
		EnviewMenu element = (EnviewMenu)it.next();
		String linkDescription = element.getTitle(langKnd);
		String linkTitle = element.getShortTitle(langKnd);
		String linkUrl = element.getFullUrl();
		String linkTarget = element.getTarget();

		buff.append("<data seq='").append( i ).append("'>");
		buff.append("<manu_name>").append( linkTitle ).append("</manu_name>");
		buff.append("<menu_addr>").append( linkUrl ).append("</menu_addr>");
		buff.append("</data>");
	}
} 
%>
<%= buff.toString() %>

