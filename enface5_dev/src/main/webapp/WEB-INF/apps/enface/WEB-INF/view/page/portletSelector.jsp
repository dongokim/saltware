<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<!--div id="PortletSelectorDialog" title="<util:message key='ev.title.property.portletCategoryTree'/>" style="display:none;"-->
<table width="280" height="240" border="0" cellspacing="0" cellpadding="0" >
	<tr>
		<td valign='top'>
			<table width="100%" border="0" cellspacing="1" cellpadding="0"> 
			<tr>
				<td valign="top">
					<form id='PortletSelectorSearchForm' name='PortletSelectorSearchForm' style='display:inline' action="javascript:portalPortletSelector.doPortletSearch('PortletSelectorSearchForm')" onkeydown="if(event.keyCode==13) portalPortletSelector.doPortletSearch('PortletSelectorSearchForm')" method='post'>
						<input type='hidden' name='sortMethod' value='ASC'/>
						<input type='hidden' name='sortColumn' value=''/>
						<input type='hidden' name='pageNo' value='1'/>
						<input type='hidden' name='pageSize' value='5'/> 
						<input type='hidden' name='pageFunction' value='portalPortletSelector.doPortletPage'/>
						<input type='hidden' name='formName' value='PortletSelectorSearchForm'/>
						<input type='hidden' id='PortletSelector_categoryId' name='categoryId' >
						<input type='hidden' id='PortletSelector_categoryType' name='categoryType'  value='portlet'>
						<table width='100%' border='0' cellspacing='0' cellpadding='0'>
						<tr>
							<td align='right'>
								<input type='text' name='title0Cond' size='20' class='portlet-form-field-label' value="*<util:message key='ev.title.portletSelectorPortletName'/>*" onfocus="portalPage.getUtility().setFieldFocus(this,'*<util:message key='ev.title.portletSelectorPortletName'/>*');" onblur="portalPage.getUtility().setFieldBlur(this,'*<util:message key='ev.title.portletSelectorPortletName'/>*');">
								<span class="btn_pack small"><a href="javascript:portalPortletSelector.doPortletSearch('PortletSelectorSearchForm')"><util:message key='ev.title.button.search'/></a></span>
							</td>
						</tr>
						</table>
					</form>
					<form id='PortletSelectorListForm' style='display:inline' name='PortletSelectorListForm' action='' method='post'>
						<table class='webgridpanel' style='width:100%;' cellpadding='0' cellspacing='0' id='grid-table'>
							<thead>
								<tr> <td colspan='3' class='webgridheaderline'></td></tr>
								<tr style='cursor: pointer;'>
									<th class="webgridheader" align="center" width="30px"></th>
									<th ch='0' class='webgridheader' width='30px'>&nbsp;</th>
									<th ch='0' class='webgridheaderlast' width='250px' onclick="portalPortletSelector.doPortletSort(this, 'DISPLAY_NAME');" >
										<span><util:message key='ev.title.portlet.PortletName'/></span>
										</span><img src="<%=request.getContextPath()%>/decorations/layout/images/v-direction.png" align="absmiddle"></span>
									</th>
								</tr>
							</thead>
							<tbody id='PortletSelectorListBody'></tbody>
							<table style='width:100%;' class='webbuttonpanel'>
								<br>
								<tr>
									<td align='center'>
										<div id='PORTLETSELECTOR_PAGE_ITERATOR' class="webnavigationpanel"></div>
									</td>
								</tr>
							</table>
						</table>
					</form>
				</td>
			</tr>
			</table>
		</td>
	</tr>
</table>
<!--/div-->
