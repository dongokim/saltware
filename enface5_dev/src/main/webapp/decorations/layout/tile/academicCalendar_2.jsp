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
	<!-- 일정등록-->
	<!-- searchArea-->
	<div class="tsearchArea">
		<p><strong>일정등록</strong></p>
	</div>
	<!-- searchArea//-->
	<!-- table-->
	<table cellpadding="0" cellspacing="0" summary="게시판리스트"  style="margin-top:0;">  
	<caption>게시판리스트</caption>
		<colgroup>
			<col width="15%" />
			<col width="85%" />
		</colgroup>
		<tr>
			<th class="first L">제목</th>
			<td><label for="subject" style="display:none;"></label><input type="text" id="subject" class="subject" /></td>
		</tr>
		<tr>
			<th class="first L">유형</th>
			<td>
				<div class="sel_150">                            	
					<select class="txt_150">
						<option>2004-06-18(수)</option>
					</select>
				</div>
				<span style="margin:3px 0 0 5px"><img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/icon_calender.gif" alt="달력" /></span>
			</td>
		</tr>
		<tr>
			<th class="first L" rowspan="2">시간</th>
			<td class="selzone">
				<span class="start">
					<div class="sel_120">                            	
						<select class="txt_120">
							<option>2004-06-18(수)</option>
						</select>
					</div>                            
					<div class="sel_100">                            	
						<select class="txt_100">
							<option>오전 12시</option>
						</select>
					</div>
					<div class="sel_60">                            	
						<select class="txt_60">
							<option>00분</option>
						</select>
					</div>
				</span><span>~</span>
				<span class="end">
					<div class="sel_120">                            	
						<select class="txt_120">
							<option>2004-06-18(수)</option>
						</select>
					</div>                            
					<div class="sel_100">                            	
						<select class="txt_100">
							<option>오전 12시</option>
						</select>
					</div>
					<div class="sel_60">                            	
						<select class="txt_60">
							<option>00분</option>
						</select>
					</div>
				</span>
			</td>
	   </tr>
	   <tr>
			<td style="border-top:0 none;">
				<input name="" type="checkbox" id="chek" value="종일" /> <label for="chek">종일</label>
			</td>
		</tr>                   
		<tr>
			<th class="first L" rowspan="2">반복</th>
			<td>
				<form id="sched" name="sched" method="post" action="">
					<label for="sched1_0"><input type="checkbox" name="sched" value="매일" id="sched1_0" /> 매일</label>
					<label for="sched1_1"><input type="checkbox" name="sched" value="월" id="sched1_1" /> 월</label>
					<label for="sched1_2"><input type="checkbox" name="sched" value="화" id="sched1_2" /> 화</label>
					<label for="sched1_3"><input type="checkbox" name="sched" value="수" id="sched1_3" /> 수</label>
					<label for="sched1_4"><input type="checkbox" name="sched" value="목" id="sched1_4" /> 목</label>
					<label for="sched1_5"><input type="checkbox" name="sched" value="금" id="sched1_5" /> 금</label>
					<label for="sched1_6"><input type="checkbox" name="sched" value="토" id="sched1_6" /> 토</label>
					<label for="sched1_7"><input type="checkbox" name="sched" value="일" id="sched1_7" /> 일</label>
				</form>
			</td>
		</tr>
		<tr>
			<td style="border-top:0 none;">
				<label for="start">시작일</label> <input type="text" id="start" class="txt_150" value="2014.01.13"/> <label for="end">종료일</label> <input type="text" id="end" class="txt_150" value="2014.01.15"/>
			</td>
		</tr>                         
		<tr>
			<th class="first L">공유</th>
			<td>
				<fieldset>
					<form id="share" name="share" method="post" action="">
						<label for="share_0"><input type="radio" name="share" value="share" id="share_0" /> 전사</label>
						<label for="share_1"><input type="radio" name="share" value="share" id="share_1" /> 부서</label>
						<label for="share_2"><input type="radio" name="share" value="share" id="share_2" /> 개인</label>
					</form>
				</fieldset>
			</td>
		</tr>                    
		<tr>
			<th class="first L">알림</th>
			<td>
				<form id="notice" name="notice" method="post" action="">
					<label for="notice 1_0"><input type="checkbox" name="notice" value="10분 전" id="sched1_0" /> 10분 전</label>
					<label for="notice 1_1"><input type="checkbox" name="notice" value="20분 전" id="sched1_1" /> 20분 전</label>
					<label for="notice 1_2"><input type="checkbox" name="notice" value="1시간 전" id="sched1_2" /> 1시간 전</label>
					<label for="notice 1_3"><input type="checkbox" name="notice" value="12시간 전" id="sched1_3" /> 12시간 전</label>
					<label for="notice 1_4"><input type="checkbox" name="notice" value="하루 전" id="sched1_4" /> 하루 전</label>
				</form> 
			</td>
		</tr>                   
		<tr>
			<th class="first L">내용</th>
			<td><textarea name="conttxt" cols="" rows=""></textarea></td>
		</tr>
	</table>
	<!-- table//-->
	<!-- btnArea-->
	<div class="btnArea">
		<div class="leftArea"><a href="#" class="btn_gray"><span>삭 제</span></a></div>
		<div class="rightArea"><a href="#" class="btn_gray"><span>취 소</span></a> <a href="#" class="btn_gray"><span>등 록</span></a></div>
	</div>
	<!-- btnArea//-->                
	<!-- 일정등록//-->
	</div>
</body>
</html>
