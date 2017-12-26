<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<div class="p_11 Portlet_tab">
	<div id="Portlet_tab_mini" class="Portlet_tab_mini m1">
		<ul>
			<li class="m1"><a href="#" class="first"><span><util:message key='ev.prop.attendedLecture.myCourse'/></span></a>
				<ul class="overflow">
					<c:forEach items="${atlcList}" var="atlc" varStatus="status">
					<li <c:if test="${status.index==0}">class="first"</c:if>><span class="title"><a href="#" title="${atlc.lectRoom}"> ${atlc.strtTime} ~ ${atlc.endTime} [ ${atlc.subjName} ] </a></span>
					</li>
					</c:forEach>
				</ul>				
			 </li>
			 <li class="m2"><a href="#"><span><util:message key='ev.prop.attendedLecture.lectureTimeTable'/></span></a>
				<ul class="overflow">
					<table cellpadding="0" cellspacing="0" summary="나의강의시간표" class="lecture_table">
						<caption>나의 강의시간표 테이블</caption>
						<colgroup>
							<col width="40px" />
							<col width="40px" />
							<col width="40px" />
							<col width="40px" />
							<col width="40px" />
							<col width="40px" />
							<col width="40px" />
							<col width="40px" />
						</colgroup>
							<thead>
								<tr>
									<th class="first"><util:message key="ev.prop.week"/></th>
									<th><util:message key="ev.prop.week.mon"/></th>
									<th><util:message key="ev.prop.week.tue"/></th>
									<th><util:message key="ev.prop.week.wed"/></th>
									<th><util:message key="ev.prop.week.thu"/></th>
									<th><util:message key="ev.prop.week.fri"/></th>
									<th><util:message key="ev.prop.week.sat"/></th>
									<th><util:message key="ev.prop.week.sun"/></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${ttData}" var="hrData" varStatus="hr">
								<tr>
									<td class="first">${hr.count}</td>
									<c:forEach items="${hrData}" var="lecture" varStatus="dow">
									<c:if test="${empty lecture}"><td>&nbsp;</td></c:if>
									<c:if test="${not empty lecture}"><td title="${lecture.subjName}:${lecture.lectRoom}">●</td></c:if>
									</c:forEach>
								</tr>
								</c:forEach>
							</tbody>
					</table>
				</ul>				
			</li>
		</ul>
	</div>
</div>						