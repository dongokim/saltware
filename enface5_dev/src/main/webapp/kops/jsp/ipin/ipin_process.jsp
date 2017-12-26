<%@ page contentType="text/html;charset=euc-kr" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
	/*********************************************************************************************
		NICE평가정보 Copyright(c) KOREA INFOMATION SERVICE INC. ALL RIGHTS RESERVED
		
		서비스명 : 가상주민번호서비스 (IPIN) 서비스
		페이지명 : 가상주민번호서비스 (IPIN) 사용자 인증 정보 처리 페이지
		
				   수신받은 데이터(인증결과)를 메인화면으로 되돌려주고, close를 하는 역활을 합니다.
	**********************************************************************************************/
	
	// 2017. 10. 11 yelim 테스트 여부 체크
	String isTest = request.getParameter("isTest");
	request.setAttribute("isTest", isTest);
	
	//2017.10.10 yelim 추가
	String reqUrl = request.getRequestURL().toString().replaceAll(request.getRequestURI(), "");			
	String reqPath = request.getContextPath();
	
	request.setAttribute("reqUrl", reqUrl);
	request.setAttribute("reqPath", reqPath);
	
	// 사용자 정보 및 CP 요청번호를 암호화한 데이타입니다. (ipin_main.jsp 페이지에서 암호화된 데이타와는 다릅니다.)
	String sResponseData = requestReplace(request.getParameter("enc_data"), "encodeData");
	
	// ipin_main.jsp 페이지에서 설정한 데이타가 있다면, 아래와 같이 확인가능합니다.
	String sReservedParam1  = requestReplace(request.getParameter("param_r1"), "");
	String sReservedParam2  = requestReplace(request.getParameter("param_r2"), "");
	String sReservedParam3  = requestReplace(request.getParameter("param_r3"), "");
    
    
    // 암호화된 사용자 정보가 존재하는 경우
    if (!sResponseData.equals("") && sResponseData != null) {

%>
		<html>
			<head>
				<title>NICE평가정보 가상주민번호 서비스</title>
				<script language='javascript'>
					function fnLoad() {
						// 인증 완료시에 인증결과를 수신하게 되는 귀사 클라이언트 결과 페이지 URL
						var rsUrl = "${reqUrl}${reqPath}/ipin/ipin_result.jsp";
						var isTest = "${isTest}";
						
						if(isTest == "y") rsUrl += "?isTest=y";
						document.vnoform.action = rsUrl;
						document.vnoform.submit();
					}
				</script>
			</head>
			<body onLoad="fnLoad()">
				<!-- 가상주민번호 서비스 팝업 페이지에서 사용자가 인증을 받으면 암호화된 사용자 정보는 해당 팝업창으로 받게됩니다.
	 		 		따라서 부모 페이지로 이동하기 위해서는 다음과 같은 form이 필요합니다. -->
				<form name="vnoform" method="post">
					<input type="hidden" name="enc_data" value="<%= sResponseData %>" /><!-- 인증받은 사용자 정보 암호화 데이타입니다. -->								
					
					<!-- 업체에서 응답받기 원하는 데이타를 설정하기 위해 사용할 수 있으며, 인증결과 응답시 해당 값을 그대로 송신합니다.
				    	 해당 파라미터는 추가하실 수 없습니다. -->
				    <input type="hidden" name="param_r1" value="<%= sReservedParam1 %>">
				    <input type="hidden" name="param_r2" value="<%= sReservedParam2 %>">
				    <input type="hidden" name="param_r3" value="<%= sReservedParam3 %>">
				</form>
<%
	} else {
%>
		<html>
			<head>
				<title>NICE평가정보 가상주민번호 서비스</title>
			</head>
			<body>
				<script>
					var isTest = "${isTest}";
					if(isTest != "y") {
						opener.failCallback("사용자 정보가 없습니다.");
					} 
					
					self.close();
				</script>
<%
	}
%>
<%!
	public String requestReplace (String paramValue, String gubun) {
        String result = "";
        
        if (paramValue != null) {
        	
        	paramValue = paramValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

        	paramValue = paramValue.replaceAll("\\*", "");
        	paramValue = paramValue.replaceAll("\\?", "");
        	paramValue = paramValue.replaceAll("\\[", "");
        	paramValue = paramValue.replaceAll("\\{", "");
        	paramValue = paramValue.replaceAll("\\(", "");
        	paramValue = paramValue.replaceAll("\\)", "");
        	paramValue = paramValue.replaceAll("\\^", "");
        	paramValue = paramValue.replaceAll("\\$", "");
        	paramValue = paramValue.replaceAll("'", "");
        	paramValue = paramValue.replaceAll("@", "");
        	paramValue = paramValue.replaceAll("%", "");
        	paramValue = paramValue.replaceAll(";", "");
        	paramValue = paramValue.replaceAll(":", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll("#", "");
        	paramValue = paramValue.replaceAll("--", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll(",", "");
        	
        	if(gubun != "encodeData"){
        		paramValue = paramValue.replaceAll("\\+", "");
        		paramValue = paramValue.replaceAll("/", "");
            paramValue = paramValue.replaceAll("=", "");
        	}
        	
        	result = paramValue;
            
        }
        return result;
  }
%>
	</body>
</html>