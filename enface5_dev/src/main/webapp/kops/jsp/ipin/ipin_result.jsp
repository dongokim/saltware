<%@ page contentType="text/html;charset=euc-kr" %>
<%@ page language="java" import="Kisinfo.Check.IPINClient" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
	/********************************************************************************************************************************************
		NICE������ Copyright(c) KOREA INFOMATION SERVICE INC. ALL RIGHTS RESERVED
		
		���񽺸� : �����ֹι�ȣ���� (IPIN) ����
		�������� : �����ֹι�ȣ���� (IPIN) ��� ������
	*********************************************************************************************************************************************/
	
	// 2017. 10. 11 yelim �׽�Ʈ ���� üũ
	String isTest = request.getParameter("isTest");
	request.setAttribute("isTest", isTest);
		
	//2017.10.10 yelim �߰�
	String reqUrl = request.getRequestURL().toString().replaceAll(request.getRequestURI(), "");			
	String reqPath = request.getContextPath();
		
	request.setAttribute("reqUrl", reqUrl);
	request.setAttribute("reqPath", reqPath);
		
	String sSiteCode				= "BX67";				// IPIN ���� ����Ʈ �ڵ�		(NICE���������� �߱��� ����Ʈ�ڵ�)
	String sSitePw					= "koihaipin1!@";		// IPIN ���� ����Ʈ �н�����	(NICE���������� �߱��� ����Ʈ�н�����)
		
	// ����� ���� �� CP ��û��ȣ�� ��ȣȭ�� ����Ÿ�Դϴ�.
    String sResponseData = requestReplace(request.getParameter("enc_data"), "encodeData");
    
    // CP ��û��ȣ : ipin_main.jsp ���� ���� ó���� ����Ÿ
    String sCPRequest = (String)session.getAttribute("CPREQUEST");
    
    // ��ü ����
	IPINClient pClient = new IPINClient();
	
	int iRtn = pClient.fnResponse(sSiteCode, sSitePw, sResponseData);
	//int iRtn = pClient.fnResponse(sSiteCode, sSitePw, sResponseData, sCPRequest);
	
	String sRtnMsg				= "";							// ó����� �޼���
	String sVNumber				= pClient.getVNumber();			// �����ֹι�ȣ (13�ڸ��̸�, ���� �Ǵ� ���� ����)
	String sName				= pClient.getName();			// �̸�
	String sDupInfo				= pClient.getDupInfo();			// �ߺ����� Ȯ�ΰ� (DI - 64 byte ������)
	String sAgeCode				= pClient.getAgeCode();			// ���ɴ� �ڵ� (���� ���̵� ����)
	String sGenderCode			= pClient.getGenderCode();		// ���� �ڵ� (���� ���̵� ����)
	String sBirthDate			= pClient.getBirthDate();		// ������� (YYYYMMDD)
	String sNationalInfo		= pClient.getNationalInfo();	// ��/�ܱ��� ���� (���� ���̵� ����)
	String sCPRequestNum		= pClient.getCPRequestNO();		// CP ��û��ȣ
			
	// Method ������� ���� ó������
	if (iRtn == 1) {
		/*
			������ ���� ����� ������ ������ �� �ֽ��ϴ�.
			����ڿ��� �����ִ� ������, '�̸�' ����Ÿ�� ���� �����մϴ�.
		
			����� ������ �ٸ� ���������� �̿��Ͻ� ��쿡��
			������ ���Ͽ� ��ȣȭ ����Ÿ(sResponseData)�� ����Ͽ� ��ȣȭ �� �̿��Ͻǰ��� �����մϴ�. (���� �������� ���� ó�����)
			
			����, ��ȣȭ�� ������ ����ؾ� �ϴ� ��쿣 ����Ÿ�� ������� �ʵ��� ������ �ּ���. (����ó�� ����)
			form �±��� hidden ó���� ����Ÿ ���� ������ �����Ƿ� �������� �ʽ��ϴ�.
		*/
		
		request.setAttribute("vnum", sVNumber);
		request.setAttribute("name", sName);
		request.setAttribute("gender", sGenderCode);
		request.setAttribute("birth", sBirthDate);
		
	} else if (iRtn == -1 || iRtn == -4) {
		sRtnMsg =	"iRtn ��, ���� ȯ�������� ��Ȯ�� Ȯ���Ͽ� �ֽñ� �ٶ��ϴ�.";
	} else if (iRtn == -6) {
		sRtnMsg =	"���� �ѱ� charset ������ euc-kr �� ó���ϰ� ������, euc-kr �� ���ؼ� ����� �ֽñ� �ٶ��ϴ�.<BR>" +
					"�ѱ� charset ������ ��Ȯ�ϴٸ� ..<BR><B>iRtn ��, ���� ȯ�������� ��Ȯ�� Ȯ���Ͽ� ���Ϸ� ��û�� �ֽñ� �ٶ��ϴ�.</B>";
	} else if (iRtn == -9) {
		sRtnMsg = "�Է°� ���� : fnResponse �Լ� ó����, �ʿ��� �Ķ���Ͱ��� ������ ��Ȯ�ϰ� �Է��� �ֽñ� �ٶ��ϴ�.";
	} else if (iRtn == -12) {
		sRtnMsg = "CP ��й�ȣ ����ġ : IPIN ���� ����Ʈ �н����带 Ȯ���� �ֽñ� �ٶ��ϴ�.";
	} else if (iRtn == -13) {
		sRtnMsg = "CP ��û��ȣ ����ġ : ���ǿ� ���� sCPRequest ����Ÿ�� Ȯ���� �ֽñ� �ٶ��ϴ�.";
	} else {
		sRtnMsg = "iRtn �� Ȯ�� ��, NICE������ ���� ����ڿ��� ������ �ּ���.";
	}
	
	request.setAttribute("ipinRs", iRtn);
    request.setAttribute("ipinMsg", sRtnMsg);
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

<html>
	<head>
		<title>NICE������ �����ֹι�ȣ ����</title>
	</head>
	<body>
		<c:if test="${isTest ne 'y'}">
			<script>
				// �˾��� ȣ���Ų opener���� SMS callbak �Լ� ȣ��
				if(opener) {
					var ipinRs = "${ipinRs}";
					var ipinMsg = "${ipinMsg}";
					
					if(ipinRs == 1) {
						var name = "${name}";
						var birth = "${birth}";
						var gender = "${gender}";
						var vnum = "${vnum}";
						opener.successCallback("ipin", name, birth, gender, vnum);
					} else {
						opener.failCallback(ipinMsg);
					}
					self.close();
				}
			</script>
		</c:if>
		<c:if test="${isTest eq 'y'}">
			iRtn : <%= iRtn %> - <%= sRtnMsg %><br><br>
		
			<!-- ����� ������ '�̸�' �ܿ��� ȭ�鿡 �����Ͻø� �ȵ˴ϴ�.
				 ����� ������ ����ؾ� �ϴ� ��쿣, �Ʒ��� ���� ��ȣȭ ������ ��� �� ��ȣȭ�Ͽ� �̿��Ͻñ� �ٶ��ϴ�.
				 ����, ��ȣȭ �� ����Ÿ�� ����ؾ� �ϴ� ��쿡�� ���������� ���Ͽ� ������ �ֽñ� �ٶ��ϴ�. -->
				 
			<table border="0">
				<tr>
					<td>�̸� : <%= sName %></td>
				</tr>
				<form name="user" method="post">
					<input type="hidden" name="enc_data" value="<%= sResponseData %>"><br>
				</form>
			</table>
		</c:if>
	</body>
</html>