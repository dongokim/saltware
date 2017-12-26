<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<c:if test="${isMobile != true }">
	<c:if test="${empty bltnList}">
		<tr><td class="br_bottom_1sd2 text_center" colspan="4"><util:message key='eb.info.desc.not.exist.bltn'/><input type="hidden" name="chk"></td></tr>
	</c:if>
	<c:forEach items="${bltnList}" var="list" varStatus="status">
		<tr class="br_bottom_1sd2">
			<td class="pdd_left10">
				<a title="<c:out value="${list.bltnOrgSubj}"/>" class="text_cut bd_callist_a2" href="javascript:ebList.readBulletin('<c:out value="${list.boardId}"/>','<c:out value="${list.bltnNo}"/>');" target="_self">
					<c:out value="${list.bltnOrgSubj}"/>
				</a>
				<span class="bd_callist_span3">
				<c:if test="${boardVO.ttlMemoYn == 'Y'}">
					<c:if test="${list.bltnMemoCnt != '0'}">
						<span class="orange">[<c:out value="${list.bltnMemoCnt}"/>]</span>
					</c:if>
				</c:if>
				<c:if test="${!empty list.fileList}">
					<img src="<%=request.getContextPath()%>/kaist/skin/basic/images/icon_file_list.png" alt="File"/>
				</c:if>
				</span>
			</td>
			<td class="text_center"><c:out value="${list.bltnPlace}"/></td>
			<td class="text_center">
				<c:if test="${list.bltnBgnYmdF == list.bltnEndYmdF}">
					<c:out value="${list.bltnBgnYmdF}"/>
				</c:if>
				<c:if test="${list.bltnBgnYmdF != list.bltnEndYmdF}">
					<c:out value="${list.bltnBgnYmdF}"/> ~ <c:out value="${list.bltnEndYmdF}"/>
				</c:if>
			</td>
			<td class="text_center"><fmt:formatNumber value="${list.bltnReadCnt}" pattern="#,##0" /></td>
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
				<a href="javascript:ebList.readBulletin('<c:out value="${list.boardId}"/>','<c:out value="${list.bltnNo}"/>');" target="_self">
					<p class="title ellipsis" title="<c:out value="${list.bltnOrgSubj}" escapeXml="false"/>"><c:out value="${list.bltnOrgSubj}" escapeXml="false"/></p>
					<c:if test="${list.bltnBgnYmdF == list.bltnEndYmdF}">
						<span class="date"><c:out value="${list.bltnBgnYmdF}"/></span>
					</c:if>
					<c:if test="${list.bltnBgnYmdF != list.bltnEndYmdF}">
						<span class="date"><util:message key="ev.hnevent.label.date"/> : <c:out value="${list.bltnBgnYmdF}"/> ~ <c:out value="${list.bltnEndYmdF}"/></span>
					</c:if>
						<span class="num_ck"><util:message key="eb.info.ttl.place"/> : <c:out value="${list.bltnPlace}"/></span>
						<span class="num_ck"><util:message key="eb.text.views"/> : <fmt:formatNumber value="${list.bltnReadCnt}" pattern="#,##0" /></span>
				</a>
			</li>
		</c:forEach>
	</ul>
</c:if>