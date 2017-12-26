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
		<script type="text/javascript">
		function setBlockAbroad( blockAbroad) {
			if( blockAbroad==1) {
				if(!confirm('해외에서의 로그인을 차단하시겟습니까? ')) {
					return;
				}
			} if( blockAbroad==0) {
				if(!confirm('해외 로그인 차단을 해지 하시겟습니까? ')) {
					return;
				}
			} else {
				 return;
			}
			$("#blockAbroad").val(blockAbroad);
			$("#setBlockAbroad").submit();
		}
		
		function changePassword() {
			var pageURL = "/user/changePassword.face";
			var w = 650;
			var h = 350;
			var left = (screen.width/2)-(w/2);
			var top = (screen.height/2)-(h/2);
			var title='changePassword';
			var targetWin = window.open (pageURL, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
			if( targetWin!=null) {
				targetWin.focus();
			} 			
		}
		</script>
		
		


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
			<form id="setBlockAbroad" method="post">
			<input type="hidden" name="blockAbroad" id="blockAbroad" >
	   	 	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="float: left;">
	   	 	<tr>
	   	 		<td>
					<div id="EnviewContentsArea">
					<table cellpadding="0" cellspacing="0" summary="게시판읽기">
					<caption>게시판</caption>
					<colgroup>
						<col width="200px" />
						<col width="*" />
						<col width="200px" />
						<col width="*" />
					</colgroup>
					<tr>
						<th class="L">로그인 시간</th>
						<td class="L" colspan="3">
							<fmt:formatDate value="${userInfo.loginDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
					</tr>
					<tr>
						<th class="L">로그인 IP</th>
						<td class="L" colspan="3">
							<c:out value="${userInfo.loginIp}"/>
						</td>
					</tr>
					<tr>
						<th class="L">최근 로그인IP </th>
						<td class="L" colspan="3">
						${loginIps}
						</td>
					</tr>
					<tr>
						<th class="L">현재 IP</th>
						<td class="L" colspan="3">
							<c:out value="${clientIp}"/>
						</td>
					</tr>
					<tr>
						<th class="L">IP보안 </th>
						<td class="L" colspan="3">
							<c:choose>
							<c:when test="${userInfo.ipProtect==1}">1단계</c:when>
							<c:when test="${userInfo.ipProtect==2}">2단계</c:when>
							<c:when test="${userInfo.ipProtect==3}">3단계</c:when>
							<c:otherwise>
							사용안함
							</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="L" rowspan="2">해외 로그인 차단</th>
						<td class="L" colspan="3">
							<c:choose>
							<c:when test="${securityInfo.blockAbroad==0}">해제 <input type="button" value=" 차단 " onclick="setBlockAbroad(1)"/> </c:when>
							<c:otherwise>차단 <input type="button" value=" 해제 " onclick="setBlockAbroad(0)"/></c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td class="L" colspan="3">
							* 해외에서 포탈을 사용하지 않으신다면 해외 로그인을 차단하시기를 권장합니다.	해외여행등으로 해외에서 포탈을 사용하시려면 미리 해외로그인 차단을 해지하시기 바랍니다. 
						</td>
					</tr>
					<tr>
						<th class="L">비밀번호 </th>
						<td class="L" colspan="3">
							<input type="button" value=" 변경 " onclick="changePassword()"/>
						</td>
					</tr>
						</table>
						<!-- table//--> 
					</div> <!-- EnviewContentsArea -->
					</td>
				 </tr>
			 </table>
			 </div>
			</form>
</body>
