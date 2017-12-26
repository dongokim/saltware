<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	request.setAttribute("cPath", request.getContextPath());
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<script src="${cPath }/javascript/crossdomain.js" type="text/javascript" ></script>
<!--[if !IE 9]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<title>서울대학교 포털</title>
<link rel="stylesheet" href="${cPath }/snu/css/reset.css">
<link rel="stylesheet" href="${cPath }/snu/css/common.css">
<link rel="stylesheet" href="${cPath }/snu/css/layout.css">
<link rel="stylesheet" href="${cPath }/snu/css/jquery-ui.css">
<script src="${cPath }/snu/js/jquery.min.js" type="text/javascript" ></script>
<script src="${cPath }/snu/js/jquery-ui.js" type="text/javascript" ></script>
</head>
<body>
	<div class="content_center_top">
		<div class="content_center_top_lan">
			<div id="google_translate_element">
				<!-- <div class="skiptranslate goog-te-gadget" dir="ltr">
					<div id=":0.targetLanguage" class="goog-te-gadget-simple"
						style="white-space: nowrap;"></div>
				</div> -->
			</div>
			<script type="text/javascript">
				function googleTranslateElementInit() {
					new google.translate.TranslateElement(
							{
								pageLanguage : 'en',
								layout : google.translate.TranslateElement.InlineLayout.SIMPLE,
								multilanguagePage: true
							}, 'google_translate_element'
					);
					
					$("#contents").html(opener.$(".view").html());
				}
			</script>
			<script type="text/javascript"
				src="//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
		</div>
	</div>
	<div class="board_wrap">
		<table id="contents" class="table_type01 view" summary="게시판">
		</table>
	</div>
</body>
</html>
