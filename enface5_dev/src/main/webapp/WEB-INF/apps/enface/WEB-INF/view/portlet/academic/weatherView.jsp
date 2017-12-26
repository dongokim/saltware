<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>

<div class="p_10">
	<p>
	<ul>	
		<c:forEach items="${resultList}" var="data" varStatus="status">
		<li>D+${data.day}
		<ul>
			<li><util:message key="ev.prop.weather.temp"/> : ${data.temp}</li>
			<li><util:message key="ev.prop.weather.tmx"/> : ${data.tmx}</li>
			<li><util:message key="ev.prop.weather.tmn"/> : ${data.tmn}</li>
			<li><util:message key="ev.prop.weather.wf"/> : ${data.wf} <img src="${cPath}/face/images/weather/${data.wfIcon}"></li>
			<li><util:message key="ev.prop.weather.pop"/> : ${data.pop}%</li>
			<li><util:message key="ev.prop.weather.ws"/> : ${data.ws}m/s</li>
			<li><util:message key="ev.prop.weather.wd"/> : ${data.wd}</li>
			<li><util:message key="ev.prop.weather.reh"/> : ${data.reh}%</li>
		</ul>
		</li>
		</c:forEach>
	</ul>		
	</p>
</div>

