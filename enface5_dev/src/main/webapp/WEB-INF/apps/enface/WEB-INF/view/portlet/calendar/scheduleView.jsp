<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<script type="text/javascript">
	function prevMonth() {
		var year = parseInt($('#scheduleYear').val());
		var month = parseInt($('#scheduleMonth').val())-1;
		
		if(month == 0) {
			year--;
			month = 12;
		} else if(month == 12) {
			year++;
			month = 1;
		}
		getScheduleData(year, month);
	}
	
	function nextMonth() {
		var year = parseInt($('#scheduleYear').val());
		var month = parseInt($('#scheduleMonth').val())+1;
		
		if(month == 0) {
			year--;
			month = 12;
		} else if(month == 12) {
			year++;
			month = 1;
		}
		getScheduleData(year, month);
	}
	
	function getScheduleData(year, month){
		$.ajax({
			type: "GET",
			url: "<portlet:resourceURL />",
			data: "year=" + year + "&month=" + month,
			dataType : "json",
			success: function(json){
				$("#scheduleYear").val( json.year );
				$("#scheduleMonth").val( json.month );
				$("#yearView").text(json.year);
				$("#monthView").text(json.month);
				
				var scheduleHTML = '';
				for(var i = 0; i < json.scheduleList.length; i++){
					var schedule = json.scheduleList[i];
					scheduleHTML += '<li><span class="title" title="' + schedule.startDatim + ' ~ ' + schedule.endDatim + '">';
					scheduleHTML += '<a href="${pageContext.request.contextPath}/portal/default/link/top_left/schedule.page">' + schedule .name + '</a></span></li>';
				}
				if(json.scheduleList.length == 0) {
					scheduleHTML += '<li><span class="title" title="<util:message key="hn.info.schedule.empty"/>">';
					scheduleHTML += '<a href="${pageContext.request.contextPath}/portal/default/link/top_left/schedule.page"><util:message key="hn.info.schedule.empty"/></a></span></li>';
				}
				$('#scListUl').html(scheduleHTML);
			}
		});
	}
</script>
<input id="scheduleYear" type="hidden" value="${year }"/>
<input id="scheduleMonth" type="hidden" value="${month}"/>
<div class ="p_11 schedule">
	<h2><util:message key='ev.prop.academicCalendar'/></h2>
	<p class="schedule_date"><a href="javascript:prevMonth();"><img src="${themePath}/images/main/schedule_pre.gif" alt="이전달" /></a> <span class="month"><label id="yearView" style="display:inline-block; text-indent:0px;">${year}</label><strong id="monthView">${month}</strong><em><util:message key='ev.prop.month'/></em></span> <a href="javascript:nextMonth();"><img src="${themePath}/images/main/schedule_next.gif" alt="다음달" /></a></p>
	<ul class="list" id="scListUl">
		<c:forEach items="${scheduleList}" var="schedule" varStatus="status" end="4">
			<li><span class="title" title="${schedule.startDatim} ~ ${schedule.endDatim}"><a href="${pageContext.request.contextPath}/portal/default/link/top_left/schedule.page">${schedule.name}</a></span></li>
		</c:forEach>
	</ul>
	<span class="more"><a href="${pageContext.request.contextPath}/portal/default/link/top_left/schedule.page"><util:message key='ev.prop.more'/></a></span>
</div>