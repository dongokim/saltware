<%@ page language="java" contentType="text/html; charset=UTF-8"pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>OPENAPI_SAMPLE</title>
<style type="text/css">
Center a{padding: 20px;} 
</style>
</head>
<body>
	<Center>
		<%String themePath = request.getContextPath();%>
		<a href="<%=themePath%>/openapi/facebookUser.face" target="_blank"><img src= "<%=themePath%>/admin/images/openapi/facebook.png" width="50" height="50"/></a>
		<a href="<%=themePath%>/openapi/twitterUser.face" target="_blank" ><img src= "<%=themePath%>/admin/images/openapi/twitter.jpg" width="50" height="50"/></a>
		<a href="<%=themePath%>/openapi/me2dayUser.face" target="_blank" ><img src= "<%=themePath%>/admin/images/openapi/me2day.png" width="50" height="50"/></a>
		<a href="<%=themePath%>/openapi/yozmUser.face" target="_blank" ><img src= "<%=themePath%>/admin/images/openapi/yozm.png" width="50" height="50"/></a>
		<a href="<%=themePath%>/openapi/googleplusUser.face" target="_blank" ><img src= "<%=themePath%>/admin/images/openapi/google.png" width="50" height="50"/></a>
		<a href="<%=themePath%>/openapi/googleCalendar.face?bun=0" target="_blank" ><img src= "<%=themePath%>/admin/images/openapi/calendar.png" width="50" height="50"/></a>
	</Center>
</body>
</html>