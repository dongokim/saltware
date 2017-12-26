<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/admin/css/styles.css">

<form id='MyPageEditorDialog.EditForm' name='MyPageEditorDialogEditForm' method='post' >
	<input type='hidden' id='MyPageEditorDialog.isCreate'>
	<input type='hidden' id='MyPageEditorDialog.theme'>
	<input type='hidden' id='MyPageEditorDialog.path' name='path' value='" + portalPage.getPath() + "'>
	<table width="100%" border='0' cellpadding='1' cellspacing='0' class='webformpanel'>
		<tr><td colspan="4" class='webformheaderline'></td></tr>
		<tr>
			<td width='20%' class='webformlabel'><util:message key='ev.title.portlet.title'/></td>
			<td colspan="3" class='webformfield'>
				<input type='text' id='MyPageEditorDialog.title' name='title' value='' size='12' maxLength='6' class='full_webtextfield'>
			</td>
		</tr>
		<tr >
			<td colspan="4" class='webformfield'>
				<div id="MyPageEditorDialog_Template_select" style="overflow:auto; padding:2px; height:230px;" class="webgridpanel"> 
				  
				</div>
			</td>
		</tr>
	</table>
</form>
		

		
			

