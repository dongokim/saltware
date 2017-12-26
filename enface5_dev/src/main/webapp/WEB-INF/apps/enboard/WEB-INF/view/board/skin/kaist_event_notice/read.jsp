<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.saltware.enview.Enview"%>
<%@ page import="com.saltware.enboard.vo.BoardVO"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<input type="hidden" id="clipTemp"/>
<c:set var="loginInfo" value="${secPmsnVO.loginInfo}" />
<c:forEach items="${bltnVOs}" var="list">
	<div class="req_h3_box"><h3><c:out value="${boardNm}"/></h3> <span class="req_location"><a onclick="ebUtil.goPage('/portal');" target="_self"><util:message key='eb.title.navi.home'/></a><a onclick="ebUtil.goPage('/portal/default/notice');" target="_self"><util:message key='eb.title.navi.notice'/></a><span><c:out value="${boardNm}"/></span></span></div>
	<table summary="테이블설명" class="req_tbl_02">
		<caption>테이블명</caption>
		<colgroup>
			<col width="135px" />
			<col />
			<col width="100px" />
			<col />
		</colgroup>
		<tbody>
			<tr>
				<th scope="row" class="req_first"><util:message key='eb.info.ttl.bltnSubj'/></th>
				<td colspan="3" class="req_first"><c:out value="${list.bltnOrgSubj}"/></td>
			</tr>
			<tr>
				<th scope="row"><util:message key='eb.info.ttl.schedule'/></th>
				<td>
					<c:if test="${list.isAllday == 'Y' }">
						<c:out value="${list.bltnBgnYmdF}" escapeXml="false"/> ~ <c:out value="${list.bltnEndYmdF}" escapeXml="false"/>
					</c:if>
					<c:if test="${list.isAllday != 'Y' }">
						<c:out value="${list.bltnBgnYmdDatimF}" escapeXml="false"/> ~ <c:out value="${list.bltnEndYmdDatimF}" escapeXml="false"/>
					</c:if>
				</td>
				<th scope="row"><util:message key='eb.info.ttl.place'/></th>
				<td><c:out value="${list.bltnPlace}" escapeXml="false"/></td>
			</tr>
			<tr>
				<th scope="row"><util:message key='eb.info.ttl.author'/>(<util:message key='eb.info.ttl.organ'/>)</th>
				<td>
					<label style="float: left;"><c:out value="${list.userNick}" /><c:if test="${ list.userOrgNm != null && fn:length(list.userOrgNm) > 0 }">(<c:out value="${list.userOrgNm}" />)</c:if></label>
				</td>
				<th scope="row"><util:message key='eb.info.ttl.date'/>(<util:message key='kaist.mobile.board.views' langKnd="${langKnd}"/>)</th>
				<td><c:out value="${list.regDatimF}" />(<c:out value="${list.bltnReadCnt}"/>)</td>
			<tr>
				<td colspan="4">
					<c:out value="${list.bltnOrgCntt}" escapeXml="false" />
				</td>
			</tr>
			<c:set var="rsfile" value="${list.fileList}" />
			<tr>
				<th scope="row" class="req_last"><util:message key='kaist.mobile.board.attachment'/></th>
				<td colspan="3" class="req_last">
					<c:forEach items="${rsfile}" var="fList" varStatus="status">
						<c:if test="${fList.atchType == 'A' }">
							<a href="<c:out value="${fList.downloadUrl}"/>" target="download" class="req_file ellipsis" title="<c:out value="${fList.fileName}"/>"><c:out value="${fList.fileName}"/><c:if test="${fList.fileSize>0}">&nbsp;(<c:out value="${fList.sizeSF}"/>)</c:if></a>&nbsp;
						</c:if>
					</c:forEach>
				</td>
			</tr>
		</tbody>
	</table>
	<div class="pr" style="float: left; margin-top:9px;">
		<span class="req_btn_pack basic">
			<a title="<c:out value="${list.bltnOrgSubj}"/>" href="<%=request.getContextPath()%>/ennotice/<c:out value="${list.boardId}"/>" target="_top">
				<util:message key='kaist.mobile.board.list' langKnd="${langKnd}"/>
			</a>
		</span>
	</div>
	<div style="float: right; margin-top:9px;">
		<c:if test="${boardVO.anonYn == 'N'}">
			<%--'익명/실명인증 게시판'이 아니면--%>
			<c:if test="${list.editable == 'true'}">
			<%--수정권한이 있는 경우--%>
				<span class="req_btn_pack dark"><a type="button" onclick="ebRead.actionSecurity('modify',<c:out value="${list.bltnNo}"/>,true)"><util:message key='kaist.mobile.board.modification'/></a></span>
           	</c:if>
			<c:if test="${list.editable == 'false'}">
				<c:if test="${list.editableUserId == '_is_admin_'}">
					<%--관리자인 경우--%>
					<span class="req_btn_pack dark"><a type="button" onclick="ebRead.actionSecurity('modify',<c:out value="${list.bltnNo}"/>,true)"><util:message key='kaist.mobile.board.modification'/></a></span>
				</c:if>
			</c:if>
		</c:if>
		<c:if test="${list.deletable == 'true'}">
			<%--삭제권한이 있는 경우--%>
			<span class="req_btn_pack dark"><a type="button" onclick="ebRead.actionSecurity('delete',<c:out value="${list.bltnNo}"/>,true)"><util:message key='kaist.mobile.board.delete'/></a></span>
		</c:if>
		<c:if test="${list.deletable == 'false'}">
			<%--삭제권한이 없는 경우--%>
			<c:if test="${list.deletableUserId == '_is_admin_'}">
				<%--관리자인 경우--%>
				<span class="req_btn_pack dark"><a type="button" onclick="ebRead.actionSecurity('delete',<c:out value="${list.bltnNo}"/>,true)"><util:message key='kaist.mobile.board.delete'/></a></span>
			</c:if>
		</c:if>
	</div>
	<div class="req_btn_wrap" style="background:#f7f7f7; height:45px; border-bottom:1px solid #e8e8e8;">
		<div class="pl" style="margin:12px 0 0 15px;">
			<span ><util:message key='eb.info.ttl.article.no'/></span>: <label for="Num"></label><input type="text" id="unique_num" value="#N<c:out value="${list.bltnNo}"/>"/>
		</div>
	</div>
	<script>
		$.ajax({
			url: '<%=request.getContextPath()%>/kapi/info.face?bn=<c:out value="${list.bltnNo}" />',
			success : function(data){
				var idx = document.getElementById('clipTemp');
				idx.value = data;
			}
		});
	</script>
</c:forEach>