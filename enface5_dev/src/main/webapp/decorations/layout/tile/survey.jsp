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
							<p class="question">Q. 응답자 유형은 어떻게 되나요?</p>
							<p>
								<fieldset>
									<form id="answerer" name="answerer" method="post" action="" style="margin:0 0 0 20px; ">
										<input type="radio" name="answerer" value="직원" id="answerer_0" />
										<label for="answerer_0">직원</label>
										<input type="radio" name="answerer" value="임원" id="answerer_1" />
										<label for="answerer_1">임원</label>
										<input type="radio" name="answerer" value="관리자" id="answerer_2" />
										<label for="answerer_2">관리자</label>
									</form>
								</fieldset>
							</p>
						</li>
						<li>
							<p class="question">Q. 포털시스템에 추가 되어야할 기능이 있다면 무엇인가요?</p>
							<p><textarea name="conttxt" cols="" rows="" style="height:24px; "></textarea></p>
						</li>
						<li>
							<p class="question">Q. 포털시스템 디자인 중 선호하는 유형을 선택하세요.</p>
							<p>
								<fieldset>
									<form id="dtype" name="dtype" method="post" action="">
										<span class="image first">
											<input type="radio" name="dtype" value="1type" id="dtype_0" />
											<label for="dtype_0"><img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/img_default.gif" /></label>
										</span>
										<span class="image">
										<input type="radio" name="dtype" value="2type" id="dtype_1" />
										<label for="dtype_1"><img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/img_default.gif" /></label>
										</span>
										<span class="image">
										<input type="radio" name="dtype" value="3type" id="dtype_2" />
										<label for="dtype_2"><img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/img_default.gif" /></label>
										</span>
									</form>
								</fieldset>
							</p>
						</li>
						<li>
							<p class="question">Q. 신용보증재단 홍보 동영상 중 선호하는 유형을 선택하세요.</p>
							<p>
								<fieldset>
									<form id="vtype" name="vtype" method="post" action="">
										<span class="image first">
											<input type="radio" name="vtype" value="1type" id="vtype_0" />
											<label for="vtype_0"><img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/img_default.gif" /></label>
										</span>
										<span class="image">
										<input type="radio" name="vtype" value="2type" id="vtype_1" />
										<label for="vtype_1"><img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/img_default.gif" /></label>
										</span>
										<span class="image">
										<input type="radio" name="vtype" value="3type" id="vtype_2" />
										<label for="vtype_2"><img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/img_default.gif" /></label>
										</span>
									</form>
								</fieldset>
							</p>
						</li>
						<li class="C">                                
							<a href="#" class="btn_black"><span>참여하기</span></a> <a href="#" class="btn_black"><span>결과보기</span></a> 
						</li>
					</ul>
				</td>
			</tr>
		</table>
		<!-- table//--> 
		<!-- btnArea-->
		<div class="btnArea">
			<div class="rightArea"><a href="#" class="btn_gray"><span>목 록</span></a></div>
		</div>
	</div>
</body>
</html>
