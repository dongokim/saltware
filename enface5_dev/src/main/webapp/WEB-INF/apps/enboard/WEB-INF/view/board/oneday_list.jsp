<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty bltnList}">
	<tr><td class="title" colspan="4">등록된 게시물이 존재하지 않습니다.<input type="hidden" name="chk"></td></tr>
</c:if>
<c:forEach items="${bltnList}" var="list">
	<tr>
		<td class="title"><a onclick="ebList.readBulletin('<c:out value="${boardVO.boardId}"/>','<c:out value="${list.bltnNo}"/>')"><c:out value="${list.bltnOrgSubj}"/></a></td>
		<td class="place center"><c:out value="${list.bltnPlace}"/></td>
		<td class="dateRange center">
			<label 
				<c:if test="${list.isAllday == 'Y' }">title="<c:out value="${list.bltnBgnYmdF}"/> ~ <c:out value="${list.bltnEndYmdF}"/>"</c:if>
				<c:if test="${list.isAllday != 'Y' }">title="<c:out value="${list.bltnBgnYmdDatimF}"/> ~ <c:out value="${list.bltnEndYmdDatimF}"/>"</c:if>
			>
			<c:if test="${list.bltnBgnYmdF == list.bltnEndYmdF}">
				<c:out value="${list.bltnBgnYmdF}"/>
			</c:if>
			<c:if test="${list.bltnBgnYmdF != list.bltnEndYmdF}">
				<c:out value="${list.bltnBgnYmdF}"/> ~ <c:out value="${list.bltnEndYmdF}"/>
			</c:if>
			</label>
		</td>
		<td class="views center"><fmt:formatNumber value="${list.bltnReadCnt}" pattern="#,##0" /></td>
	</tr>
</c:forEach>