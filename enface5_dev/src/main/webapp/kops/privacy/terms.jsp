<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>환자안전보고학습시스템</title>
<meta name="author" content="환자안전보고학습시스템"/>
<meta name="description" content="환자안전보고학습시스템"/>
<meta name="keywords" content="환자안전보고학습시스템"/>
<meta name="Resource-type" content="Document"/>
<!-- 환자안전보고학습시스템  -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/kops/css/default.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/kops/js/sub.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			var termsArea = $("#termsArea");
			
			$.ajax({
				type : 'POST',
				url : '/user/getTerms.face',
				dataType : 'json',
				success : function(json){
					var jsonText = json.terms;
					termsArea.html(jsonText);
				},error : function(json){
				}
			});
		});

		
	</script>
</head>

<body>
	<!-- iframe -->
	<div class="contents">
		<!-- contents -->
		<h2 class="sub_title">이용약관</h2>
		
		<div class="agree_wrap">
				<pre id="termsArea"><c:out value="${term.bltnCntt}" escapeXml="false" /></pre>
		</div>
		<!-- //contents -->
	</div>
	<!-- //iframe -->
</body>
</html>