<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<script type='text/javascript' src="<%=request.getContextPath()%>/face/javascript/portlet/calendar/minifullcalendar.js"></script>
<script type='text/javascript' src="<%=request.getContextPath()%>/face/javascript/portlet/calendar/myMinicalendar.js"></script>
<script type='text/javascript'>
	$(document).ready(function(){
		enMyMiniCal.m_contextPath = '<%=request.getContextPath()%>';
		enMyMiniCal.m_themePath = '${themePath}';
		enMyMiniCal.m_emptyMessage = '<util:message key="hn.info.schedule.empty"/>';
		var themePathConverted = $("[converURL*='themePath']");
		themePathConverted.each(function(){
			var $this = $(this);
			var convertPattern = $this.attr('converURL').split('_')[0];
			var convertAttr = $this.attr('converURL').split('_')[1];
			$this.attr(convertAttr, $this.attr(convertAttr).replace('@' + convertPattern + '@', '${themePath}'));
		});
		
		var themePathConverted = $("[converURL*='contextPath']");
		themePathConverted.each(function(){
			var $this = $(this);
			var convertPattern = $this.attr('converURL').split('_')[0];
			var convertAttr = $this.attr('converURL').split('_')[1];
			$this.attr(convertAttr, $this.attr(convertAttr).replace('@' + convertPattern + '@', '<%=request.getContextPath()%>'));
		});
		
		var moreButtonText = "<util:message key='ev.prop.list'/>";
		$('#myMoreButton').text(moreButtonText);
	});
</script>
<input id="myMonthURL" type="hidden" value="<portlet:resourceURL id="scheduleCount"/>"/>
<input id="myDateURL" type="hidden" value="<portlet:resourceURL id="scheduleDate"/>"/>

<div class ="p_13 checkschedule">
	<h2 id="calendarName">미니캘린더</h2>
                <!-- data-->
                <div id="myCalendar" class="data">
                    <ul id="myScheduleListUl">
				        <c:forEach items="${scheduleList}" var="schedule" varStatus="status" end="4">
							<li><span class="title"><a href="${pageContext.request.contextPath}/portal/default/link/top_left/schedule.page">${schedule.name}</a></span></li>
						</c:forEach>
                    </ul>
                </div>
				<span class="more"><a href="/portal/default/link/top_left/schedule.page"><util:message key='ev.prop.more'/></a></span>
</div>