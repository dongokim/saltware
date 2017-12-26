<%@ page contentType="text/html;charset=euc-kr" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
	/*********************************************************************************************
		NICE������ Copyright(c) KOREA INFOMATION SERVICE INC. ALL RIGHTS RESERVED
		
		���񽺸� : �����ֹι�ȣ���� (IPIN) ����
		�������� : �����ֹι�ȣ���� (IPIN) ����� ���� ���� ó�� ������
		
				   ���Ź��� ������(�������)�� ����ȭ������ �ǵ����ְ�, close�� �ϴ� ��Ȱ�� �մϴ�.
	**********************************************************************************************/
	
	// 2017. 10. 11 yelim �׽�Ʈ ���� üũ
	String isTest = request.getParameter("isTest");
	request.setAttribute("isTest", isTest);
	
	//2017.10.10 yelim �߰�
	String reqUrl = request.getRequestURL().toString().replaceAll(request.getRequestURI(), "");			
	String reqPath = request.getContextPath();
	
	request.setAttribute("reqUrl", reqUrl);
	request.setAttribute("reqPath", reqPath);
	
	// ����� ���� �� CP ��û��ȣ�� ��ȣȭ�� ����Ÿ�Դϴ�. (ipin_main.jsp ���������� ��ȣȭ�� ����Ÿ�ʹ� �ٸ��ϴ�.)
	String sResponseData = requestReplace(request.getParameter("enc_data"), "encodeData");
	
	// ipin_main.jsp ���������� ������ ����Ÿ�� �ִٸ�, �Ʒ��� ���� Ȯ�ΰ����մϴ�.
	String sReservedParam1  = requestReplace(request.getParameter("param_r1"), "");
	String sReservedParam2  = requestReplace(request.getParameter("param_r2"), "");
	String sReservedParam3  = requestReplace(request.getParameter("param_r3"), "");
    
    
    // ��ȣȭ�� ����� ������ �����ϴ� ���
    if (!sResponseData.equals("") && sResponseData != null) {

%>
		<html>
			<head>
				<title>NICE������ �����ֹι�ȣ ����</title>
				<script language='javascript'>
					function fnLoad() {
						// ���� �Ϸ�ÿ� ��������� �����ϰ� �Ǵ� �ͻ� Ŭ���̾�Ʈ ��� ������ URL
						var rsUrl = "${reqUrl}${reqPath}/ipin/ipin_result.jsp";
						var isTest = "${isTest}";
						
						if(isTest == "y") rsUrl += "?isTest=y";
						document.vnoform.action = rsUrl;
						document.vnoform.submit();
					}
				</script>
			</head>
			<body onLoad="fnLoad()">
				<!-- �����ֹι�ȣ ���� �˾� ���������� ����ڰ� ������ ������ ��ȣȭ�� ����� ������ �ش� �˾�â���� �ްԵ˴ϴ�.
	 		 		���� �θ� �������� �̵��ϱ� ���ؼ��� ������ ���� form�� �ʿ��մϴ�. -->
				<form name="vnoform" method="post">
					<input type="hidden" name="enc_data" value="<%= sResponseData %>" /><!-- �������� ����� ���� ��ȣȭ ����Ÿ�Դϴ�. -->								
					
					<!-- ��ü���� ����ޱ� ���ϴ� ����Ÿ�� �����ϱ� ���� ����� �� ������, ������� ����� �ش� ���� �״�� �۽��մϴ�.
				    	 �ش� �Ķ���ʹ� �߰��Ͻ� �� �����ϴ�. -->
				    <input type="hidden" name="param_r1" value="<%= sReservedParam1 %>">
				    <input type="hidden" name="param_r2" value="<%= sReservedParam2 %>">
				    <input type="hidden" name="param_r3" value="<%= sReservedParam3 %>">
				</form>
<%
	} else {
%>
		<html>
			<head>
				<title>NICE������ �����ֹι�ȣ ����</title>
			</head>
			<body>
				<script>
					var isTest = "${isTest}";
					if(isTest != "y") {
						opener.failCallback("����� ������ �����ϴ�.");
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