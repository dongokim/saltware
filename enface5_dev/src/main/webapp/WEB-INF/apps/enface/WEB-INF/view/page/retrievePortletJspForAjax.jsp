<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

	<div style="overflow:auto; width:420px; height:230px; border:1px solid lightgray">
	<table class="webgridpanel" style="width:100%;" cellpadding="0" cellspacing="0" id="grid-table">
		<thead>
			<tr> <td colspan='6' class='webgridheaderline'></td></tr>
			<tr style='cursor: pointer;'>
				<th class="webgridheader" align="center" width="30px">&nbsp;</th>
				<th ch='0' class='webgridheader' width='30px'>&nbsp;</th>
				<th ch='0' class='webgridheaderlast' width='150px' onclick="portalPortletSelector.doPortletSort(this, 'DISPLAY_NAME');" >
					<span><util:message key='ev.title.portlet.PortletName'/></span>
					</span><img src="<%=request.getContextPath()%>/decorations/layout/images/v-direction.png" align="absmiddle"></span>
				</th>
			</tr>
		</thead>
		
		<tbody id="PortletSelectorListBody">
		<c:forEach items="${results}" var="portlet" varStatus="status">
			<tr id="PortletSelector:Portlet[<c:out value="${status.index}"/>]" ch="<c:out value="${status.index}"/>" class="even">
				<td align="center" class="webgridbody">
					<input type="checkbox" id="PortletSelector[<c:out value="${status.index}"/>].checkRow" ch="0" <c:if test="${portlet.USED == 'T'}">checked="true"</c:if> class="webcheckbox">
					<input type="hidden" id="PortletSelector[<c:out value="${status.index}"/>].portletId" value="<c:out value="${portlet.ID}"/>">
					<input type="hidden" id="PortletSelector[<c:out value="${status.index}"/>].portletAppName" value="<c:out value="${portlet.APP_NAME}"/>">
					<input type="hidden" id="PortletSelector[<c:out value="${status.index}"/>].portletName" value="<c:out value="${portlet.NAME}"/>">
					<input type="hidden" id="PortletSelector[<c:out value="${status.index}"/>].portletTitle" value="<c:out value="${portlet.TITLE}"/>">
					<input type="hidden" id="PortletSelector[<c:out value="${status.index}"/>].title" value="<c:out value="${portlet.TITLE}"/>">
					<input type="hidden" id="PortletSelector[<c:out value="${status.index}"/>].uniqueId" value="<c:out value="${portlet.UNIQUE_NAME}"/>">
				</td>
				<td class="webgridbody" align="center" onclick="portalPortletSelector.doPortletSelect(this)">
					<img src="<%=request.getContextPath()%>/images/portlets/<c:out value="${portlet.ICON}"/>">
				</td>
				<td class="webgridbodylast" align="left" style="cursor: pointer;" onclick="portalPortletSelector.doPortletSelect(this)">
					<span style="width:100%;" class="webgridrowlabel" id="PortletSelector[<c:out value="${status.index}"/>].name.label" 
							dragcopy="true" portletid="<c:out value="${portlet.ID}"/>" portletappname="<c:out value="${portlet.APP_NAME}"/>" portletname="<c:out value="${portlet.NAME}"/>" 
							portlettitle="<c:out value="${portlet.title}"/>" uniqueid="<c:out value="${portlet.UNIQUE_NAME}"/>" title="<c:out value="${portlet.DESCRIPTION}"/>">
							<c:out value="${portlet.DISPLAY_NAME}"/>
					</span>
				</td>
			</tr>
		</c:forEach>
		
		</tbody>
	</table>
	</div>
	<br>
	<div class="webgridpanel">
		<c:if test="${empty results}">
			<util:message key='ev.info.notFoundData'/><br>
		</c:if>
		<c:if test="${!empty results}">
			<util:message key='ev.info.resultSize' param='${resultSize}'/><br>
		</c:if>
	</div>
	<br>
	<table style="width:100%;" class="webbuttonpanel">
		<tbody>
		<tr>
			<td align="center">
				<div id="PORTLETSELECTOR_PAGE_ITERATOR" class="webnavigationpanel"><c:out escapeXml='false' value='${pageIterator}'/></div>
			</td>
		</tr>
		</tbody>
	</table>
