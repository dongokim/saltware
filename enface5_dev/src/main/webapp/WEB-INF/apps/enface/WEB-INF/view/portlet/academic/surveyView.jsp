<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<div class="p_11 survey"> 
	<h2><util:message key='ev.prop.poll'/></h2>
	<p class="question"><strong>질문) </strong>신용보증재단 포털시스템을 사용해보신 소감을 선택해주세요.</p>
	<form id="form1" name="form1" method="post" action="">
		<label for="survey_0"></label><input type="radio" name="survey" value="매우만족" id="survey_0" /> 매우만족한다<br />
		<label for="survey_1"></label><input type="radio" name="survey" value="만족한다" id="survey_1" /> 만족한다<br />
		<label for="survey_2"></label><input type="radio" name="survey" value="불만족한다." id="survey_2" /> 불만족한다<br />
		<label for="survey_3"></label><input type="radio" name="survey" value="매우 불만족한다." id="survey_3" /> 매우 불만족한다
	</form>
	<!-- 버튼영역 -->
	<div class="btnArea"><a href="#">참여하기</a><span><a href="#">결과보기</a></span></div>
	<!-- 버튼영역 //-->
</div>