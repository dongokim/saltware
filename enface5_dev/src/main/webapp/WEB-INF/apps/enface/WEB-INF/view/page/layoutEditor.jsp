<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<%
	System.out.println("######### layoutList=" + request.getAttribute("layoutList"));
%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/admin/css/styles.css">

<form id='LayoutPortletEditorDialog.PortalFragmentEditForm' name='PortalLayoutFragmentEditForm' action='' method='post'>
	<input type='hidden' id='LayoutPortletEditorDialog.Fragment' name='fragment' value=''>
	<input type='hidden' name='remove' value=''>
	<input type='hidden' id='LayoutPortletEditorDialog.LayoutSize' name='layoutsize' value=''>
	<input type='hidden' name='actiontype' value=''>
	<table border='0' cellpadding='1' cellspacing='0'  class='webformpanel'>
		<tr><td colspan='5' class='webformheaderline'></td></tr>
		<tr>
			<td class='webformlabel'><util:message key='ev.title.page.pageEditingName'/></td>
			<td class='webformfield' id='LayoutPortletEditorDialog.Name'></td>
		</tr>
		<tr>
			<td class='webformlabel'><util:message key='ev.title.page.pageEditingLayout'/></td>
			<td class='webformfield'>
				<select id='LayoutPortletEditor.SELECT_LAYOUT' name='layout' onchange='javascript:portalLayoutPortletEditor.selectLayoutChanage();'>
					<c:forEach items="${layoutList}" var="layout">
					<option value="<c:out value="${layout.name}"/>"><c:out value="${layout.displayName}"/></option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td class='webformlabel'><util:message key='ev.title.page.pageEditingColumnSize'/> (px)</td>
			<td class='webformfield'>
				<span id="LayoutPortletEditorDialog.ColumnSize"></span>
				<select id='LayoutPortletEditor.ColumnSizeUnit' name='unit'>
					<option value='px'>px
					<option value='%'>%
				</select>
			<td>
		</tr>
		<!--tr>
			<td class='webformlabel'><util:message key='ev.title.page.pageEditingAlign'/></td>
			<td class='webformfield'>
				<select id='LayoutPortletEditor.SELECT_ALIGN' name='align' style='display:none'>
					<option value='default'>Default
					<option value='tab'>Tab
				</select>
			</td>
		</tr-->
		<tr>
			<td class='webformlabel'>컨텐츠 타입</td>
			<td class='webformfield'>
				<select id='LayoutPortletEditor.SELECT_CONTENTTYPE' name='contentType'>
					<option value="">전체</option>
					<c:forEach items="${contentTypeList}" var="contentType">
					<option value="<c:out value="${contentType.code}"/>"><c:out value="${contentType.codeName}"/></option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr id='LayoutPortletEditor.ACTIONMASK_PANE'>
			<td width='20%' class='webformlabel'><util:message key='ev.title.page.pageEditingActionMask'/></td>
			<td colspan='3' class='webformfield'>
				<input type='checkbox' id='LayoutPortletEditor.ADD_ENABLE' name='add_enable' value=''><util:message key='ev.title.page.pageEditingAddEnable'/>&nbsp;&nbsp;
				<input type='checkbox' id='LayoutPortletEditor.EDIT_ENABLE' name='edit_enable' value=''><util:message key='ev.title.page.pageEditingEditEnable'/>&nbsp;&nbsp;
				<input type='checkbox' id='LayoutPortletEditor.MOVE_ENABLE' name='move_enable' value=''><util:message key='ev.title.page.pageEditingMoveEnable'/>&nbsp;&nbsp;
				<input type='checkbox' id='LayoutPortletEditor.DELETE_ENABLE' name='delete_enable' value=''><util:message key='ev.title.page.pageEditingDeleteEneble'/>&nbsp;&nbsp;
			</td>
		</tr>
	</table>
</form>
		

		
			

