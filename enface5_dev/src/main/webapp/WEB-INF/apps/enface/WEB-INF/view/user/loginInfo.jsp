<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-type" content="text/html" />  
		<meta http-equiv="Content-style-type" content="text/css" />
		<meta name="version" content="3.2.5" />
		<meta name="keywords" content="" />
		<meta name="description" content="" />
	

		<style type="text/css">
		#ipProtect {width:600px;display: none;font-size: 12px; border: 5px solid white;}
		#ipProtect table {width:100%;background-color: white; border: 2px ;}
		#ipProtect table th {background-color: aqua;padding: 5px; border:5px solid white; font-size: 12px;font-weight: bold;text-align: center;}
		#ipProtect table td {background-color: lime; padding: 5px; border :5px solid white;font-size: 12px;}
		#ipProtect table td.C {text-align: cenetr}
		</style>
		
		<link href="${pageContext.request.contextPath}/board/css/style.css"/ type="text/css" rel="stylesheet" >
		<link href="${pageContext.request.contextPath}/board/calendar/css/calendar.css" type="text/css" rel="stylesheet" >
		<link href="${pageContext.request.contextPath}/decorations/layout/tile/css/style.css" rel="stylesheet" type="text/css" />
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery/jquery-1.10.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery/jquery-ui-1.9.2.custom.min.js"></script>
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/common_new.js"></script>


<style type="text/css">
  <!--
  body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
  }
  -->
</style>

<c:set var="loginInfo" value="${secPmsnVO.loginInfo}"/>
<c:if test="${boardVO.extUseYn == 'Y'}">
  <c:set var="rsExtnMapper" value="${boardVO.bltnExtnMapper}"/>
</c:if>

<body>
	   	 	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="float: left;">
	   	 	<tr>
	   	 		<td>
					<div id="EnviewContentsArea">
					<table cellpadding="0" cellspacing="0" summary="게시판읽기">
					<caption>게시판</caption>
					<colgroup>
						<col width="140px" />
						<col width="*" />
						<col width="140px" />
						<col width="*" />
					</colgroup>
					<tr>
						<th class="L"><util:message key='ev.prop.userpass.userId'/> </th>
						<td class="L" colspan="3">
							<c:out value="${userInfo.userId}"/>
						</td>
					</tr>
					<tr>
						<th class="L"><util:message key='ev.prop.userpass.nmKor'/> </th>
						<td class="L" colspan="3">
							<c:out value="${userInfo.userName}"/>
						</td>
					</tr>
					<tr>
						<th class="L"><util:message key='ev.prop.userpass.emailAddr'/> </th>
						<td class="L" colspan="3">
							<c:out value="${userInfo.emailAddr}"/>
						</td>
					</tr>
					<tr>
						<th class="L"><util:message key='ev.prop.userpass.mobileTel'/> </th>
						<td class="L" colspan="3">
							<c:out value="${userInfo.mobileTel}"/>
						</td>
					</tr>
						</table>
						<!-- table//--> 
					</div> <!-- EnviewContentsArea -->
					</td>
				 </tr>
			 </div>
			 </table>
</body>
