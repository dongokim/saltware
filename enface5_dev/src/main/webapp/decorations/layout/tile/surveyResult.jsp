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
		<table cellpadding="0" cellspacing="0" summary="설문조사참여">
		<caption>설문조사참여</caption>
			<colgroup>
				<col width="15%" />
				<col width="35%" />
				<col width="12%" />
				<col width="38%" />
			</colgroup>
			<tr>
				<th scope="row" class="L first">제목</th>
				<td colspan="3">포털시스템 만족도 조사</td>
			</tr>
			<tr>
				<th scope="row" class="L first">구분</th>
				<td>진행중</td>
				<th scope="row" class="L">작성자</th>
				<td>홍길동</td>
			</tr>
			<tr>
				<th scope="row" class="L first">설문기간</th>
				<td>2013. 12. 10 ~ 2014. 02. 12</td>
				<th scope="row" class="L">조회</th>
				<td>1111</td>
			</tr>
			<tr>
				<th scope="row" class="L first">내용</th>
				<td colspan="3">
					<ul>
						<li class="first">
							<p class="question">Q. 응답자 유형은 어떻게 되나요? (총 참여인원 100명)</p>
							<ul>
								<li><span class="type">1. 직원</span> <span class="graph"></span> <span class="percent">50%(50명)</span> </li>
								<li><span class="type">2. 임원</span> <span class="graph"></span> <span class="percent">30%(30명)</span> </li>
								<li><span class="type">3. 관리자</span> <span class="graph"></span> <span class="percent">20%(20명)</span> </li>
							</ul>
						</li>
						<li>
							<p class="question">Q. 포털시스템에 추가 되어야할 기능이 있다면 무엇인가요? (총 참여인원 100명)</p>
							<ul>
								<li><span class="type">1. 응답율</span> <span class="graph"></span> <span class="percent">50%(50명)</span> </li>
							</ul>
						</li>
						<li>
							<p class="question">Q. 포털시스템 디자인 중 선호하는 유형을 선택하세요. (총 참여인원 100명)</p>
							<ul>
								<li><span class="type">1. 이미지보기</span> <span class="graph"></span> <span class="percent">50%(50명)</span> </li>
								<li><span class="type">2. 이미지보기</span> <span class="graph"></span> <span class="percent">50%(50명)</span> </li>
								<li><span class="type">3. 이미지보기</span> <span class="graph"></span> <span class="percent">50%(50명)</span> </li>
							</ul>
						</li>
						<li>
							<p class="question">Q. 신용보증재단 홍보 동영상 중 선호하는 유형을 선택하세요. (총 참여인원 100명)</p>
							<ul>
								<li><span class="type">1. 동영상보기</span> <span class="graph"></span> <span class="percent">50%(50명)</span> </li>
								<li><span class="type">2. 동영상보기</span> <span class="graph"></span> <span class="percent">50%(50명)</span> </li>
								<li><span class="type">3. 동영상보기</span> <span class="graph"></span> <span class="percent">50%(50명)</span> </li>
							</ul>
						</li>
					</ul>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
