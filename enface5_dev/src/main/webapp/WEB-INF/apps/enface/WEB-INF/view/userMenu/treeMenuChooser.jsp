<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.saltware.enview.security.EnviewMenu" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Iterator" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<html>
<head>
	<title>Sitemap Test</title>
	<!--link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/admin/css/styles.css"-->
	<script type="text/javascript" src="<%=request.getContextPath()%>/face/javascript/menu.js"></script>
</head>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
	<tr>
		<td valign='top'>
			<table width="100%" border="0" cellspacing="1" cellpadding="0"> 
			<tr>
				<td width="100px" style="width:26%;background:#FFFFFF;" valign="top" class="webpanel">
					<div >
						<div id="TreeMenuChooser_TreeTabPage" style="width:100%; height:200px; overflow:auto;"></div>
					</div>
				</td>
			</tr>
			</table>
		</td>
	</tr>
</table>
</body>
</html>
