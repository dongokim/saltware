<%@page import="javax.portlet.PortletRequest"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>

<script type="text/javascript">
	$(function(){
		
		$("#userGroupList").change( function() {
			var groupId =  $("#userGroupList option:selected").val();
			window.open( "/user/changeGroup.face?groupId=" + groupId, "_top");
		});	
		$("#userInfoRefresh").click( function() {
			url = $(this).attr("url");
			url = '${resourceURL}';
			alert( url)
			$.ajax({
				type: "GET",
				url: url,
				dataType : "text",
				success: function(data){
					$("#mailCount").html( data.mailCount );
					$("#noteCount").html( data.noteCount );
				}
			});
		});	
	});
</script>

<div class="p_11 personal">
	<div class="personal_info">
		<p class="identity"><span class="name"> <strong>${userInfo.userName}</strong></span><span class="major">${userInfo.orgName} (${userInfo.groupName})</span>
		</p>
	</div>
	<div class="myinfo">
		<ul>
			<li><span><util:message key='ev.prop.mail'/></span><em id="mailCount">${mailCount}</em> </li>
			<li><span class="message"><util:message key='ev.prop.message'/></span><em id="noteCount">${noteCount}</em></li>
			<li class="list"><span><util:message key='ev.prop.lastLogin'/><fmt:formatDate value="${userInfoMap.lastLogon}" pattern="yyyy-MM-dd HH:mm"/><span id="userInfoRefresh" class='ui-icon ui-icon-refresh' title="다시읽기" url="<portlet:resourceURL />"></span></span></li>
		</ul>
		<p class="adjust"><a class="left" href="#"><util:message key='ev.prop.userInfo.changeAccount'/></a><a href="#"><util:message key='ev.prop.userInfo.changePassword'/></a></p>
		<p class="account"><select id="userGroupList">
			<c:forEach items="${userGroupList}" var="userGroup" varStatus="status">
			<option value="${userGroup.groupId}" <c:if test="${userGroup.groupId == userInfoMap.groupId}">selected="selected"</c:if>>${userGroup.orgName}(${userGroup.groupName})</option>
			</c:forEach>
			</select>
		</p>
	</div>
	<div class="btnArea"><a class="logout" href="/user/logout.face"><util:message key='ev.prop.logout'/></a><a class="mypage" href="/cafe"><util:message key='ev.prop.cafe'/></a></div>
</div>