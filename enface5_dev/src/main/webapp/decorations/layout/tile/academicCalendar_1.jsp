<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.saltware.enview.om.page.Page"%>
<%@page import="com.saltware.enview.sso.EnviewSSOManager"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.saltware.enview.security.spi.impl.IsMemberOfPrincipalAssociationHandler"%>
<%@page import="com.saltware.enview.security.EnviewMenu"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="../initLayoutDecorators.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" />
		<meta http-equiv="Content-style-type" content="text/css"/>   
		<link rel="stylesheet" type="text/css" href="css/style.css" />
		<script type="text/javascript" src="js/jquery-1.7.min.js"></script>
		<script type="text/javascript" src="js/main.js"></script>
	</head>
<body>
	<div id="EnviewContentsArea">
		<div class="cover">
			<div id="Schedetail" class="Schedetail">
				<p class="titleAera">
					<span class="title">일정</span>
					<span class="layerClose"><a href="javascript:closeSched();"><img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/layer_close.gif" alt="닫기" /></a></span>
				</p>
				<ul>
					<li class="first"><span class="title">시간</span> <span>2014-01-13(월) 오후 12시 00분~ 2014년 01월 13일(월) 오후 1시 50분</span></li>
					<li><span class="title">제목</span> <span>고객과의 점심약속</span></a></li>
					<li><span class="title">내용</span> <span>정기적인 고객과의 약속</span></li>
				</ul>
				<ul style="background:#fff; padding:3px 0 0 0;">
					<li class="leftArea" style="background:none; padding:0; line-height:28px"><a href="#" class="btn_gray"><span>삭 제</span></a></li>
					<li class="rightArea" style="background:none; padding:0; line-height:28px"><a href="#" class="btn_gray"><span>수 정</span></a></li>
				</ul>
			</div>
			<!-- Schedetail //-->
			<!-- monthpage -->
			<div class="btnArea" style="margin:10px 0 0 0;">
				<div class="leftArea"><a href="#" class="btn_white"><span>일(Day)</span></a> <a href="#" class="btn_darkgra"><span>주(Week)</span></a> <a href="#" class="btn_white"><span>월(Month)</span></a> <a href="#"><img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/arr_perv_day.gif" alt="이전달" /></a> <span class="tomonth">2014년 1월 13일(월)</span> <a href="#"><img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/arr_next_day.gif" alt="다음달" /></a></div>
				<div class="rightArea">
					<div class="sel_150">                            	
						<select class="txt_150">
							<option>전체</option>
						</select>
					</div>                    	
				</div>
			</div>
			<!-- monthpage//-->
			<!-- table-->
			<table cellpadding="0" cellspacing="0" summary="일정리스트" style="*margin:15px 0 0 0;border-left:1px solid #e1e1e1; border-right:1px solid #e1e1e1;">  
			<caption>일정리스트</caption>
				<colgroup>
				<col width="15%" />
				<col width="85%" />
				</colgroup>
				<tr>
					<th class="first"  rowspan="2"></th>
					<th>종일일정 1</th>
				</tr>
				<tr>
					<th>종일일정 2</th>
				</tr>
				<tr>
					<th class="first"  scope="row">오전 6시</th>
					<td><a href="javascript:openSched();">일정이 들어감</a></td>
				</tr>
				<tr>
					<th class="first"  scope="row">오전 7시</th>
					<td></td>
				</tr>
				<tr>
					<th class="first"  scope="row">오전 8시</th>
					<td></td>
				</tr>
				<tr>
					<th class="first"  scope="row">오전 9시</th>
					<td></td>
				</tr>
				<tr>
					<th class="first"  scope="row">오전 10시</th>
					<td></td>
				</tr>
				<tr>
					<th class="first"  scope="row">오전 11시</th>
					<td></td>
				</tr>
				<tr>
					<th class="first"  scope="row">오후 12시</th>
					<td></td>
				</tr>
				<tr>
					<th class="first"  scope="row">오후 1시</th>
					<td></td>
				</tr>
				<tr>
					<th class="first"  scope="row">오후 2시</th>
					<td></td>
				</tr>
				<tr>
					<th class="first"  scope="row">오후 3시</th>
					<td></td>
				</tr>
				<tr>
					<th class="first"  scope="row">오후 4시</th>
					<td></td>
				</tr>
				<tr>
					<th class="first"  scope="row">오후 5시</th>
					<td></td>
				</tr>
				<tr>
					<th class="first"  scope="row">오후 6시</th>
					<td></td>
				</tr>
				<tr>
					<th class="first"  scope="row">오후 7시</th>
					<td></td>
				</tr>
				<tr>
					<th class="first"  scope="row">오후 8시</th>
					<td></td>
				</tr>
				<tr>
					<th class="first" scope="row">오후 9시</th>
					<td></td>
				</tr>
				<tr>
					<th class="first"  scope="row">오후 10시</th>
					<td></td>
				</tr>
				<tr>
					<th class="first"  scope="row">오후 11시</th>
					<td></td>
				</tr>
				<tr>
					<th class="first"  scope="row">오후 12시</th>
					<td></td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>
