<%@ page language="java" contentType="text/html;charset=euc-kr" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%	
	// 2017. 10. 11 yelim �׽�Ʈ ���� üũ
	String isTest = request.getParameter("isTest");
	request.setAttribute("isTest", isTest);

	//���� �� ������� null�� ������ �κ��� ��������ڿ��� ���� �ٶ��ϴ�.
    NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

    String sEncodeData = requestReplace(request.getParameter("EncodeData"), "encodeData");

    String sSiteCode = "BC547";						// NICE�κ��� �ο����� ����Ʈ �ڵ�
    String sSitePassword = "ppf9et7kMahM";			// NICE�κ��� �ο����� ����Ʈ �н�����

    String sCipherTime = "";			// ��ȣȭ�� �ð�
    String sRequestNumber = "";			// ��û ��ȣ
    String sResponseNumber = "";		// ���� ������ȣ
    String sAuthType = "";				// ���� ����
    String sName = "";					// ����
    String sDupInfo = "";				// �ߺ����� Ȯ�ΰ� (DI_64 byte)
    String sConnInfo = "";				// �������� Ȯ�ΰ� (CI_88 byte)
    String sBirthDate = "";				// �������(YYYYMMDD)
    String sGender = "";				// ����
    String sNationalInfo = "";			// ��/�ܱ������� (���߰��̵� ����)
	String sMobileNo = "";				// �޴�����ȣ
	String sMobileCo = "";				// ��Ż�
    String sMessage = "";
    String sPlainData = "";
    
    int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);

    if( iReturn == 0 ) {
        sPlainData = niceCheck.getPlainData();
        sCipherTime = niceCheck.getCipherDateTime();
        
        // ����Ÿ�� �����մϴ�.
        java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);
        
        sRequestNumber  = (String)mapresult.get("REQ_SEQ");
        sResponseNumber = (String)mapresult.get("RES_SEQ");
        sAuthType		= (String)mapresult.get("AUTH_TYPE");
        sName			= (String)mapresult.get("NAME");
		//sName			= (String)mapresult.get("UTF8_NAME"); //charset utf8 ���� �ּ� ���� �� ���
        sBirthDate		= (String)mapresult.get("BIRTHDATE");
        sGender			= (String)mapresult.get("GENDER");
        sNationalInfo  	= (String)mapresult.get("NATIONALINFO");
        sDupInfo		= (String)mapresult.get("DI");
        sConnInfo		= (String)mapresult.get("CI");
        sMobileNo		= (String)mapresult.get("MOBILE_NO");
        sMobileCo		= (String)mapresult.get("MOBILE_CO");
        
        request.setAttribute("name", sName);
        request.setAttribute("birth", sBirthDate);
        request.setAttribute("gender", sGender);
        
        String session_sRequestNumber = (String)session.getAttribute("REQ_SEQ");
        if(!sRequestNumber.equals(session_sRequestNumber)) {
            sMessage = "���ǰ��� �ٸ��ϴ�. �ùٸ� ��η� �����Ͻñ� �ٶ��ϴ�.";
            sResponseNumber = "";
            sAuthType = "";
        }
        
    } else if( iReturn == -1) {
        sMessage = "��ȣȭ �ý��� �����Դϴ�.";
    } else if( iReturn == -4) {
        sMessage = "��ȣȭ ó�������Դϴ�.";
    } else if( iReturn == -5) {
        sMessage = "��ȣȭ �ؽ� �����Դϴ�.";
    } else if( iReturn == -6) {
        sMessage = "��ȣȭ ������ �����Դϴ�.";
    } else if( iReturn == -9) {
        sMessage = "�Է� ������ �����Դϴ�.";
    } else if( iReturn == -12) {
        sMessage = "����Ʈ �н����� �����Դϴ�.";
    } else {
        sMessage = "�˼� ���� ���� �Դϴ�. iReturn : " + iReturn;
    }
    
    request.setAttribute("smsRs", iReturn);
    request.setAttribute("smsMsg", sMessage);
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
    	<title>NICE������</title>
	</head>
	<body>
		<c:if test="${isTest ne 'y'}">
			<script>
				// �˾��� ȣ���Ų opener���� SMS callbak �Լ� ȣ��
				if(opener) {
					var smsRs = "${smsRs}";
					var smsMsg = "${smsMsg}";
					
					if(smsRs == 0) {
						var name = "${name}";
						var birth = "${birth}";
						var gender = "${gender}";
						
						opener.successCallback("sms", name, birth, gender, null);
					} else {
						opener.failCallback(smsMsg);
					}
					
					self.close();
				}
			</script>
		</c:if>
		<c:if test="${isTest eq 'y'}">
		    <center>
		    	<p><p><p><p>
		    	���������� �Ϸ� �Ǿ����ϴ�.<br>
			    <table border=1>
			        <tr>
			            <td>��ȣȭ�� �ð�</td>
			            <td><%= sCipherTime %> (YYMMDDHHMMSS)</td>
			        </tr>
			        <tr>
			            <td>��û ��ȣ</td>
			            <td><%= sRequestNumber %></td>
			        </tr>            
			        <tr>
			            <td>NICE���� ��ȣ</td>
			            <td><%= sResponseNumber %></td>
			        </tr>            
			        <tr>
			            <td>��������</td>
			            <td><%= sAuthType %></td>
			        </tr>
					<tr>
			            <td>����</td>
			            <td><%= sName %></td>
			        </tr>
					<tr>
			            <td>�ߺ����� Ȯ�ΰ�(DI)</td>
			            <td><%= sDupInfo %></td>
			        </tr>
					<tr>
			            <td>�������� Ȯ�ΰ�(CI)</td>
			            <td><%= sConnInfo %></td>
			        </tr>
					<tr>
			            <td>�������(YYYYMMDD)</td>
			            <td><%= sBirthDate %></td>
			        </tr>
					<tr>
			            <td>����</td>
			            <td><%= sGender %></td>
			        </tr>
							<tr>
			            <td>��/�ܱ�������</td>
			            <td><%= sNationalInfo %></td>
			        </tr>
			        </tr>
						<td>�޴�����ȣ</td>
			            <td><%= sMobileNo %></td>
			        </tr>
					<tr>
						<td>��Ż�</td>
						<td><%= sMobileCo %></td>
			        </tr>
					<tr>
						<td colspan="2">���� �� ������� ���� ������ ���� ���� ���Ϲ����� �� �ֽ��ϴ�. <br>
						�Ϻ� ������� null�� ���ϵǴ� ��� ��������� �Ǵ� ���μ�(02-2122-4615)�� ���ǹٶ��ϴ�.</td>
					</tr>
				</table><br><br>        
		    	<%= sMessage %><br>
		    </center>
	    </c:if>
	</body>
</html>