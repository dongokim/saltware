<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>환자안전보고학습시스템</title>
<meta name="author" content="환자안전보고학습시스템"/>
<meta name="description" content="환자안전보고학습시스템"/>
<meta name="keywords" content="환자안전보고학습시스템"/>
<meta name="Resource-type" content="Document"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/kops/css/common.css">
<script>
	function fn_close()
	{
		window.close();
	}
</script>
</head>

<style>
	.popup_wrap {position:relative;height:600px;padding-top:50px;border-top:2px solid #d66c92;text-align:center;background:url('${pageContext.request.contextPath}/kops/images/popup_bg.png') no-repeat center bottom;}
	.popup_wrap .txt1 {font-family:'NanumSquare';font-size:25px;color:#666;font-weight:bold;}
	.popup_wrap .txt2 {font-family:'NanumSquare';font-size:30px;color:#252525;font-weight:bold;padding:15px 0;}
	.popup_wrap .txt2 span {display:inline-block;color:#af3f69;border-bottom:1px solid #af3f69;line-height: 34px;}
	.popup_wrap a.close {position:absolute;top:10px;right:10px;cursor:pointer;}
</style>

<body>
	<div id="main_pop" class="popup_wrap">
		<p class="txt1">시스템 점검 및 업데이트를 위하여</p>
		<p class="txt2"><span>2017년 12월 00일 00시~00일 05시까지</span><br />서비스가 중단됩니다.</p>
		<p class="txt1">서비스이용에 참고바랍니다.</p>
		<a class="close" href="javascript:void(0);" onclick="fn_close()"><img src="${pageContext.request.contextPath}/kops/images/common/close_btn.gif" alt="닫기" /></a>
	</div>
</body>
</html>
