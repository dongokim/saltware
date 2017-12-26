<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/admin/css/styles.css">

<form id='PortletEditorDialog.PortalFragmentEditForm' name='PortalFragmentEditForm' action='' method='post'>
	<input type='hidden' id='PortletEditorDialog.Fragment' name='fragment' value=''>
	<input type='hidden' name='remove' value=''>
	<table border='0' cellpading='1' cellspacing='0'  class='webformpanel'>
		<tr><td colspan='4' class='webformheaderline'></td></tr>
		<tr>
			<td class='webformlabel'><util:message key='ev.title.page.pageEditingName'/></td>
			<td colspan='3' class='webformfield' id='PortletEditorDialog.Name'></td>
		</tr>
		<!--tr id='PortletEditor.SELECT_CONTENT_TYPE_PANE' >
			<td width='20%' class='webformlabel'><util:message key='ev.title.page.pageEditingPortletContentType'/></td>
			<td class='webformfield'>
				<select id='PortletEditor.SELECT_CONTENT_TYPE' name='content_type' disabled='true'>
					<option value='normal'>JSR168 Content
					<option value='static'>Static Content
					<option value='iframe'>IFrame Content
				</select>
			</td>
			<td width='20%' class='webformlabelmiddle'><util:message key='ev.title.page.pageEditingPortletSystemCode'/></td>
			<td class='webformfield'>
				<select id='PortletEditor.SELECT_SYSTEM_CODE' name='system_code' >
					<c:forEach items="${systemCodeList}" var="systemCode">
					<option value="<c:out value="${systemCode.code}"/>"><c:out value="${systemCode.codeName}"/></option>
					</c:forEach>
				</select>
			</td>
		</tr-->
		<tr id='PortletEditor.OPTION_PANE'>
			<td width='20%' class='webformlabel'><util:message key='ev.title.page.pageEditingOption'/></td>
			<td colspan='3' class='webformfield'>
				<input type='checkbox' id='PortletEditor.AUTO_RESIZE' name='auto_resize' value=''><util:message key='ev.title.page.pageEditingAutoResize'/>&nbsp;&nbsp;
				<input type='checkbox' id='PortletEditor.SCROLLING' name='scrolling' value=''><util:message key='ev.title.page.pageEditingScrolling'/>&nbsp;&nbsp;
				<input type='checkbox' id='PortletEditor.TITLE_SHOW' name='title_hidden' value=''><util:message key='ev.title.page.pageEditingTitleShow'/>&nbsp;&nbsp;
			</td>
		</tr>
		<!--tr id='PortletEditor.ACTIONMASK_PANE'>
			<td width='20%' class='webformlabel'><util:message key='ev.title.page.pageEditingActionMask'/></td>
			<td colspan='3' class='webformfield'>
				<input type='checkbox' id='PortletEditor.ADD_ENABLE' name='add_enable' value=''><util:message key='ev.title.page.pageEditingAddEnable'/>&nbsp;&nbsp;
				<input type='checkbox' id='PortletEditor.EDIT_ENABLE' name='edit_enable' value=''><util:message key='ev.title.page.pageEditingEditEnable'/>&nbsp;&nbsp;
				<input type='checkbox' id='PortletEditor.MOVE_ENABLE' name='move_enable' value=''><util:message key='ev.title.page.pageEditingMoveEnable'/>&nbsp;&nbsp;
				<input type='checkbox' id='PortletEditor.DELETE_ENABLE' name='delete_enable' value=''><util:message key='ev.title.page.pageEditingDeleteEneble'/>&nbsp;&nbsp;
			</td>
		</tr-->
		<tr id='PortletEditor.CONTENT_TITLE_PANE'>
			<td width='20%' class='webformlabel'><util:message key='ev.title.page.pageEditingContentTitle'/></td>
			<td colspan='3' class='webformfield'>
				<input type='text' id='PortletEditor.CONTENT_TITLE' style='width:95%;' name='content_title' value=''>
			</td>
		</tr>
		<tr>
			<td width='20%' class='webformlabel'><util:message key='ev.title.page.pageEditingContentStyle'/></td>
			<td colspan='3'  class='webformfield'>
				<input type='text' id='PortletEditor.CONTENT_STYLE' style='width:95%;' name='content_style' value=''>
			</td>
		</tr>
		<tr id='PortletEditor.CONTENT_STYLE_PANE'>
			<td width='20%' class='webformlabel'><util:message key='ev.title.page.pageEditingContentClass'/></td>
			<td class='webformfield'>
				<input type='text' id='PortletEditor.CONTENT_CLASS' style='width:90%;' name='content_class' value=''>
			</td>
			<td width='20%' class='webformlabelmiddle'>&nbsp;</td>
			<td class='webformfield'>&nbsp;</td>
		</tr>
		<tr id='PortletEditor.SIZE_PANE'> 
			<td width='20%' class='webformlabel'><util:message key='ev.title.pageEditing.Width'/></td>
			<td width='30%' class='webformfield'>
				<input type='text' id='PortletEditor.WIDTH' style='width:90%;' name='width' value=''>
			</td>
			<td width='20%' class='webformlabelmiddle'><util:message key='ev.title.pageEditing.Height'/></td>
			<td class='webformfield'>
				<input type='text' id='PortletEditor.HEIGHT' style='width:90%;' name='height' value=''>
			</td>
		</tr>
		<!--
		<tr id='PortletEditor.CONTENT_EXTRA_PANE'>
			<td width='20%' class='webformlabel'><util:message key='ev.title.page.pageEditingMoreTarget'/></td>
			<td class='webformfield'>
				<input type='text' id='PortletEditor.MORE_SRC_TARGET' style='width:90%;' name='more_target' value=''>
			</td>
			<td width='20%' class='webformlabelmiddle'><util:message key='ev.title.page.pageEditingMaxHeight'/></td>
			<td class='webformfield'>
				<input type='text' id='PortletEditor.MAX_HEIGHT' style='width:90%;' name='max_height' value=''>
			</td>
		</tr>
		<tr id='PortletEditor.MORE_SOURCE_PANE'>
			<td width='20%' class='webformlabel'><util:message key='ev.title.page.pageEditingMoreSource'/></td>
			<td colspan='3' class='webformfield'>
				<input type='text' id='PortletEditor.MORE_SRC' style='width:95%;' name='more_source' value=''>
			</td>
		</tr>
		<tr id='PortletEditor.CONTENT_SOURCE_PANE'>
			<td width='20%' class='webformlabel'><util:message key='ev.title.page.pageEditingContentSource'/></td>
			<td colspan='3' class='webformfield'>
				<input type='text' id='PortletEditor.CONTENT_SOURCE' style='width:95%;' name='content_source' value=''>
			</td>
		</tr>
		-->
	</table>
</form>