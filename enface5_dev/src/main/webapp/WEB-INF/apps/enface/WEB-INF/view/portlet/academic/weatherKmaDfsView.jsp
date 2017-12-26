<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>

<div class="p_10">
	<p>
		<c:forEach items="${resultList}" var="data" varStatus="status">
		기온 : ${data.temp}<br>
		최고 : ${data.tmx}<br>
		최저 : ${data.tmn}<br>
		날씨 : ${data.wfKor}/${data.wfEn}<br>
		강우확율 : ${data.pop}%<br>
		풍속 : ${data.ws}m/s<br>
		풍향 : ${data.wdKor}/${data.wdEn}<br>
		습도 : ${data.reh}%<br>
		하늘 : ${data.sky}<br>
		아이콘 : <img src="${cPath}/face/images/weather/${data.wfIcon}"/>
		</c:forEach>
	</p>
</div>

