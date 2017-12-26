<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/admin/css/styles.css">

<form id='GroupPageEditorDialog.EditForm' name='GroupPageEditorDialogEditForm' method='post'>
	<input type='hidden' id='GroupPageEditorDialog.isCreate'>
	<input type='hidden' id='GroupPageEditorDialog.theme'>
	<input type='hidden' id='GroupPageEditorDialog.path' name='path' value='" + portalPage.getPath() + "'>
	<table width="100%" border='0' cellpadding='1' cellspacing='0' class='webformpanel'>
		<tr><td colspan="2" class='webformheaderline'></td></tr>
		<tr>
			<td width='20%' class='webformlabel'><util:message key='ev.title.page.pageEditingName'/></td>
			<td colspan="3" class='webformfield'>
				<input type='text' id='GroupPageEditorDialog.title' name='title' value='' size='12' maxLength='6' class='webtextfield'>
				<select id='GroupPageEditorDialog_changeMakeType' name='changeMakeType' onchange="portalGroupPageEditor.doChangeMakeType(this)">
					<option value="1" selected="selected">Select Layout</option>
					<option value="2" >Select Template</option>
				</select>
			</td>
		</tr>
		<tr id="GroupPageEditorDialog_Layout_select">
			<td width='20%' class='webformlabel'><util:message key='ev.title.property.layout'/></td>
			<td class='webformfield'>
				<input type="radio" name="layoutCheck" value="enview-layouts::VelocityOneColumn" class="webcheckbox"/><img title="" align="absmiddle" src="<%=request.getContextPath()%>/images/portlets/1line.png">
				<input type="radio" name="layoutCheck" value="enview-layouts::VelocityTwoColumnsSmallLeft" class="webcheckbox"/><img title="" align="absmiddle" src="<%=request.getContextPath()%>/images/portlets/2line_15.png">
				<input type="radio" name="layoutCheck" value="enview-layouts::VelocityTwoColumns2575" class="webcheckbox"/><img title="" align="absmiddle" src="<%=request.getContextPath()%>/images/portlets/2line_25.png">
				<input type="radio" name="layoutCheck" value="enview-layouts::VelocityTwoColumns" class="webcheckbox"/><img title="" align="absmiddle" src="<%=request.getContextPath()%>/images/portlets/2line_50.png">
				<input type="radio" name="layoutCheck" value="enview-layouts::VelocityThreeColumns" class="webcheckbox"/><img title="" align="absmiddle" src="<%=request.getContextPath()%>/images/portlets/3line.png">
				<input type="radio" name="layoutCheck" value="enview-layouts::VelocityFourColumns" class="webcheckbox"/><img title="" align="absmiddle" src="<%=request.getContextPath()%>/images/portlets/4line.png">
			</td>
		</tr>
		<tr id="GroupPageEditorDialog_Template_select" style="display:none;">
			<td class='webformlabel'><util:message key='ev.title.property.template'/></td>
			<td colspan="3" class='webformfield'>
				<div style="overflow:auto; padding:2px;" class="webgridpanel"> 
				  <table id="grid-table" width="100%" border="0" cellspacing="0" cellpadding="0">
					<thead>
						
						<tr style="cursor: pointer;">
							<th></th>
							<th></th>
							<th></th>
						</tr>
					</thead>
					<tbody id="GroupPageEditorDialog_ListBody">

					</tbody>
				   </table>
				</div>
			</td>
		</tr>
	</table>
</form>
		

		
			

