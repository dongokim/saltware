<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>인증에러</title>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<style type="text/css">
	Center{ padding: 270px;}
	p{color: blue;font-style: inherit;}
	img {border: none;} 
	Center a{padding: 20px;} 
</style>
</head>
<body>
	<Center>
		<p> 개발자 키에 대한 정보를 찾을수 없습니다. <br><br> 해당하는 SNS에 개발자 등록후 발급된 키를 등록하시기 바랍니다 </p>
		<%String themePath = request.getContextPath();%>
		<a href="http://developers.facebook.com/"><img src= "<%=themePath%>/admin/images/openapi/facebook.png" width="50" height="50"/></a>
		<a href="https://dev.twitter.com/"><img src= "<%=themePath%>/admin/images/openapi/twitter.jpg" width="50" height="50"/></a>
		<a href="http://dev.naver.com/openapi/"><img src= "<%=themePath%>/admin/images/openapi/me2day.png" width="50" height="50"/></a>
		<a href="http://dna.daum.net/apis/yozm"><img src= "<%=themePath%>/admin/images/openapi/yozm.png" width="50" height="50"/></a>
		<a href="https://accounts.google.com"><img src= "<%=themePath%>/admin/images/openapi/google.png" width="50" height="50"/></a>
		<a href="https://accounts.google.com"><img src= "<%=themePath%>/admin/images/openapi/calendar.png" width="50" height="50"/></a>
	</Center>
</body>
</html>