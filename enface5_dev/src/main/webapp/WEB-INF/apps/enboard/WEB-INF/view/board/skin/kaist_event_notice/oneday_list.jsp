<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<c:if test="${isMobile != true }">
	<c:if test="${empty bltnList}">
		<tr><td class="req_tl req_bn" colspan="5"><util:message key='eb.info.desc.not.exist.bltn'/><input type="hidden" name="chk"></td></tr>
	</c:if>
	<c:forEach items="${bltnList}" var="list" varStatus="status">
		<tr>
			<td class="req_tl req_bn<c:if test="${status.last }"> req_last</c:if>">
				<a title="<c:out value="${list.bltnOrgSubj}"/>" href="<%=request.getContextPath()%>/ennotice/<c:out value="${list.boardId}"/>/<c:out value="${list.bltnNo}"/>" target="_top">
					<c:out value="${list.bltnOrgSubj}"/>
				</a><c:if test="${boardVO.ttlMemoYn == 'Y'}"><c:if test="${list.bltnMemoCnt != '0'}"><div class="memoCnt">[<c:out value="${list.bltnMemoCnt}"/>]</div></c:if></c:if>
				<c:if test="${!empty list.fileList}"><img src="<%=request.getContextPath()%>/board/images/board/skin/default/icon_file.png"/></c:if></td>
			<td width="170px" <c:if test="${status.last }">class="req_last"</c:if>><label class="ellipsis" title="<c:out value="${list.bltnPlace}"/>"><c:out value="${list.bltnPlace}"/></label></td>
			<td<c:if test="${status.last }"> class="req_last"</c:if>>
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
			<td<c:if test="${status.last }"> class="req_last"</c:if>><c:out value="${list.bltnReadCnt}"/></td>
		</tr>
	</c:forEach>
</c:if>
<c:if test="${isMobile == true }">
	<ul class="ul_list">
		<c:if test="${empty bltnList}">
			<li class="" style="text-align: center; padding-top: 30px; padding-bottom: 30px;"  ><util:message key='eb.info.desc.not.exist.bltn'/><input type="hidden" name="chk"></li>
		</c:if>
		<c:forEach items="${bltnList}" var="list" varStatus="status">
			<li <c:if test="${status.count == 1 }">class="first"</c:if>>
				<a href="<%=request.getContextPath()%>/ennotice/<c:out value="${list.boardId}"/>/<c:out value="${list.bltnNo}"/>" target="_top">
					<p class="title ellipsis" title="<c:out value="${list.bltnOrgSubj}" escapeXml="false"/>"><c:out value="${list.bltnOrgSubj}" escapeXml="false"/></p>
					<c:if test="${list.bltnBgnYmdF == list.bltnEndYmdF}">
						<span class="date"><c:out value="${list.bltnBgnYmdF}"/></span>
					</c:if>
					<c:if test="${list.bltnBgnYmdF != list.bltnEndYmdF}">
						<span class="date"><util:message key="kaist.mobile.calendar.date"/> : <c:out value="${list.bltnBgnYmdF}"/> ~ <c:out value="${list.bltnEndYmdF}"/></span>
					</c:if>
						<span class="num_ck"><util:message key="kaist.mobile.calendar.place"/> : <c:out value="${list.bltnPlace}"/></span>
						<span class="num_ck"><util:message key="kaist.mobile.board.views"/> : <c:out value="${list.bltnReadCnt}"/></span>
				</a>
			</li>
		</c:forEach>
	</ul>
</c:if>