<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<!-- ToDoList -->
<div class="p_21">
	<!-- Portlet_tab_normal -->
	<div id="Portlet_tab_normal" class="Portlet_tab_normal m1">
		<!-- 탭영역 -->
		<ul>
			<!-- To-Do List -->
			<li class="m1"><a href="#"><span><util:message key='ev.prop.todoList'/></span></a>
				<p class="month">
				<ul>
					<li><span class="month"><strong>2014</strong> <img src="${themePath}/images/main/month_pre.gif" alt="이전날"> <em>06월 24일 화요일</em> <img src="${themePath}/images/main/month_next.gif" alt="다음날"> <img class="toDoCalendar" src="${themePath}/images/main/calender.gif" alt="달력 아이콘"></span></li>
					<li><span class="title"><a href="#">13:00  구비문학개론 강의</a></span></li>
					<li><span class="title"><a href="#">14:30  세미나 참석 일정 확인 및 담당자에게 전화 통화 약속</a></span></li>
					<li><span class="title"><a href="#">15:00  국제 어학회 기조연설 및 학회 참석</a></span></li>
					<li><span class="title"><a href="#">16:00  유재석, 하동훈씨 저녁 약속</a></span></li>
					<li><span class="title"><a href="#">17:00  상암동 세미나 상담 및 인사관리 </a></span></li>
				</ul>
				<span class="more"><a href="#"><util:message key='ev.prop.more'/></a></span>                            
			 </li>
			 <!-- To-Do List //-->
			 <!-- 주요통계 -->
			 <li class="m2"><a href="#"><span><util:message key='ev.prop.statistics'/></span></a>
				<ul>
					<li><span class="month"><strong>2014</strong><a href="#"><img src="${themePath}/images/main/month_pre.gif" alt="이전날"></a><em>06월 24일 화요일</em><a href="#"><img src="${themePath}/images/main/month_next.gif" alt="다음날"></a><img class="toDoCalendar" src="${themePath}/images/main/calender.gif" alt="달력 아이콘"></span></li>
					<li><span class="title"><a href="#">13:00  구비문학개론 강의</a></span></li>
					<li><span class="title"><a href="#">14:30  세미나 참석 일정 확인 및 담당자에게 전화 통화 약속</a></span></li>
					<li><span class="title"><a href="#">15:00  국제 어학회 기조연설 및 학회 참석</a></span></li>
					<li><span class="title"><a href="#">16:00  유재석, 하동훈씨 저녁 약속</a></span></li>
					<li><span class="title"><a href="#">17:00  상암동 세미나 상담 및 인사관리 </a></span></li>
				</ul>
				<span class="more"><a href="#"><util:message key='ev.prop.more'/></a></span>                            
			</li>
			<!-- 주요통계 //-->
		</ul>
		<!-- 탭영역//-->
	</div>
	<!-- Portlet_tab_normal //-->
</div>