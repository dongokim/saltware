<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page language="java" import="Kisinfo.Check.IPINClient" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	//2017.10.10 yelim 추가
	String reqUrl = request.getRequestURL().toString().replaceAll(request.getRequestURI(), "");			
	String reqPath = request.getContextPath();
	
	String sSiteCode				= "BX67";				// IPIN 서비스 사이트 코드		(NICE평가정보에서 발급한 사이트코드)
	String sSitePw					= "koihaipin1!@";		// IPIN 서비스 사이트 패스워드	(NICE평가정보에서 발급한 사이트패스워드)
	
	String sReturnURL				= reqUrl + reqPath + "/ipin/ipin_process.jsp";
	
	String sCPRequest				= "";
	
	// 객체 생성
	IPINClient pClient = new IPINClient();
	
	// 앞서 설명드린 바와같이, CP 요청번호는 배포된 모듈을 통해 아래와 같이 생성할 수 있습니다.
	sCPRequest = pClient.getRequestNO(sSiteCode);
	
	// CP 요청번호를 세션에 저장합니다.
	// 현재 예제로 저장한 세션은 ipin_result.jsp 페이지에서 데이타 위변조 방지를 위해 확인하기 위함입니다.
	// 필수사항은 아니며, 보안을 위한 권고사항입니다.
	session.setAttribute("CPREQUEST" , sCPRequest);
	
	// Method 결과값(iRtn)에 따라, 프로세스 진행여부를 파악합니다.
	int iRtn = pClient.fnRequest(sSiteCode, sSitePw, sCPRequest, sReturnURL);
	
	String sRtnMsg					= "";			// 처리결과 메세지
	String sEncData					= "";			// 암호화 된 데이타
	
	// Method 결과값에 따른 처리사항
	if (iRtn == 0) {
		// fnRequest 함수 처리시 업체정보를 암호화한 데이터를 추출합니다.
		// 추출된 암호화된 데이타는 당사 팝업 요청시, 함께 보내주셔야 합니다.
		sEncData = pClient.getCipherData();		//암호화 된 데이타
		sRtnMsg = "정상 처리되었습니다.";
	} else if (iRtn == -1 || iRtn == -2) {
		sRtnMsg =	"배포해 드린 서비스 모듈 중, 귀사 서버환경에 맞는 모듈을 이용해 주시기 바랍니다.<BR>" +
					"귀사 서버환경에 맞는 모듈이 없다면 ..<BR><B>iRtn 값, 서버 환경정보를 정확히 확인하여 메일로 요청해 주시기 바랍니다.</B>";
	} else if (iRtn == -9) {
		sRtnMsg = "입력값 오류 : fnRequest 함수 처리시, 필요한 4개의 파라미터값의 정보를 정확하게 입력해 주시기 바랍니다.";
	} else {
		sRtnMsg = "iRtn 값 확인 후, NICE평가정보 개발 담당자에게 문의해 주세요.";
	}
	
	request.setAttribute("ipinRs", iRtn);
    request.setAttribute("ipinMsg", sRtnMsg);
%>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>회원가입 - IPIN 인증 요청</title>
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery/jquery-1.10.2.min.js"></script>
		
		<style>
			.noshow { display:none; }
		</style>
		
		<script>
			$(document).ready(function() {
				authIpin();
			});
			
			function authIpin() {
				var ipinRs = "${ipinRs}";
				var ipinMsg = "${ipinMsg}";
				
				if(ipinRs != 0) {
					alert(ipinMsg);
					return;
				}
				
				window.name = "authUserFrame";
				window.open('', 'popupIPIN2', 'width=450, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
				document.authIpinForm.target = "popupIPIN2";
				document.authIpinForm.action = "https://cert.vno.co.kr/ipin.cb";
				document.authIpinForm.submit();
			}
			
			function successCallback(method, name, birth, gender, num) {
				if(parent) {
					parent.successCallback(method, name, birth, gender, num);
				}	
			}
			
			function failCallback(rsMsg) {
				if(parent) {
					parent.failCallback(rsMsg);
				}
			}
		</script>
	</head>
	<body>
		<!-- 가상주민번호 서비스 팝업을 호출하기 위해서는 다음과 같은 form이 필요합니다. -->
		<form name="authIpinForm" method="post">
			<input type="hidden" name="m" value="pubmain" /><!-- 필수 데이타로, 누락하시면 안됩니다. -->
		    <input type="hidden" name="enc_data" value="<%= sEncData %>" /><!-- 위에서 업체정보를 암호화 한 데이타입니다. -->
		    
		    <!-- 업체에서 응답받기 원하는 데이타를 설정하기 위해 사용할 수 있으며, 인증결과 응답시 해당 값을 그대로 송신합니다.
		    	 해당 파라미터는 추가하실 수 없습니다. -->
		    <input type="hidden" name="param_r1" value="" />
		    <input type="hidden" name="param_r2" value="" />
		    <input type="hidden" name="param_r3" value="" />
		</form>
	</body>
</html>