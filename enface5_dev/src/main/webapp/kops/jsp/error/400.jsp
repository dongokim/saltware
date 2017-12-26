<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.saltware.enview.Enview"%>
<%@ page import="com.saltware.enview.security.EVSubject"%>
<%
	String langKnd = (String) pageContext.getSession().getAttribute("langKnd");
	String cPath = request.getContextPath();
	String userId = "";
	String groupId = "";
	if (EVSubject.getUserInfo() != null) {
		userId = EVSubject.getUserId();
		groupId = EVSubject.getUserInfo().getGroupId();
	}
	request.setAttribute("userId", userId);
	request.setAttribute("groupId", groupId);
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>환자안전보고학습시스템</title>
		<meta name="author" content="환자안전보고학습시스템" />
		<meta name="keywords"  content="환자안전보고학습시스템,환자안전보고학습시스템" />
		<meta name="Resource-type" content="Document" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<meta http-equiv="Content-style-type" content="text/css"/>   
		<meta name="version" content="3.2.5"/>
		<meta name="description" content="${PageTitle}"/>
		
		<link rel="stylesheet" type="text/css" media="screen, projection" href="${cPath}/decorations/layout/css/dhtmlxtree.css"  />
		<link type="text/css" href="${cPath}/decorations/layout/css/jquery/base/jquery.ui.all.css" rel="stylesheet" />
		<link rel="stylesheet" type="text/css" media="screen, projection" href="${cPath}/face/css/utility.css" />
		
		<!-- 환자안전포탈  -->
		<link rel="stylesheet" type="text/css" href="${cPath}/kops/css/default.css"/>
</head>

<body>

<!-- Contents -->
<div id="error">
	<h1><a href="#"><img src="/kops/images/contents/logo_error.png" alt="환자안전보고학습시스템 로고" /></a></h1>
	<div class="number">
		<p>400</p>
		<span>Bad Request</span>
	</div>
	<p class="b_txt">요청에 실패했습니다.</p>
	<p>The server failed the request.<br /> Authorization will not help and the request SHOULD NOT be repeated.<br /> If the request method was not HEAD and the server wishes to make public why the request has not been fulfilled,<br /> it SHOULD describe the reason for the refusal in the entity. <br />If the server does not wish to make this information available to the client, the status code 400 (Not Found) can be used instead.</p>
</div>
<!-- Contents -->

</body>
</html>