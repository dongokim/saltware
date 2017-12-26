<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<%
	request.setAttribute("cPath", request.getContextPath());
%>   
<html lang="ko-KR">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>서비스연계-미니보드세션종료안내</title>
	<link rel="stylesheet" href="${cPath}/kaist/common/css/common.css" />
    <link rel="stylesheet" href="${cPath}/kaist/skin/kaist_basic/css/style.css" />
	<script type="text/javascript" src="${cPath}/javascript/jquery/jquery-1.10.2.min.js"></script>
    <script type="text/javascript">
	</script>
</head>
<body>
	<div class="bd_minierror">
		<div class="minierr_img_wrap">
			<img src="${cPath}/kaist/skin/kaist_basic/images/icon_minierrer_bd.png" alt="Session Time out" class="" />
		</div>
    	<div>
    		<div class="minierr_text_wrap">
    			<p class="orange bold ">Portal SSO 세션이 종료되었습니다 <br/>
				Portal 로그인 후 재접속 하시길 바랍니다.</p>
    		</div>
    		<div class="minierr_text_wrap">
    			<p class="orange bold ">Portal SSO Session Expired <br/>
				Please login to Portal and retry.</p>
    		</div>
    		
    	</div>
        
    </div>	
</body>
</html>