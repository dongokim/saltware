<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="true" %>

<html>
<head>
<title>Error report</title>
</head>

<%
	String errorMessage = "";
	Exception e = (Exception)pageContext.getAttribute("exception", PageContext.REQUEST_SCOPE);
	
	if( e != null ) {
		e.printStackTrace();
		errorMessage = e.getMessage();
	}
	else {
		errorMessage = (String)request.getAttribute("errorMessage");
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
  <tr>
    <td align="center"> 
	  <table width="200px" height="200px" border=0 cellspacing=0 cellpadding=0>
		<tr>
			<td align="center" valign="center">Application Error</td>
		</tr>
		<tr>
			<td>
				<div style="width: 200px; height:200px; border: 1px solid black; padding: 10px;">
					<%=errorMessage%>
				</div>
			</td>
		</tr>
		<tr><td align="center">
			<p><a href="javascript:history.back(-1);">Back</a></p>
		</td></tr>
	  </table>
	</td>
  </tr>
</table>

</body>
</html>