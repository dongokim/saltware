
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/face/css/styles.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/face/javascript/sampleManager.js"></script>
<script type="text/javascript">
function initSampleManager() {
	if( aSampleManager == null ) {
		aSampleManager = new SampleManager("<c:out value="${evSecurityCode}"/>");
		aSampleManager.init();
	}
}
function finishSampleManager() {
	
}
// Attach to the onload event
if (window.attachEvent)
{
    window.attachEvent ( "onload", initSampleManager );
	window.attachEvent ( "onunload", finishSampleManager );
}
else if (window.addEventListener )
{
    window.addEventListener ( "load", initSampleManager, false );
	window.addEventListener ( "unload", finishSampleManager, false );
}
else
{
    window.onload = initSampleManager;
	window.onunload = finishSampleManager;
}
</script>



<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
<tr>
	<td id="SampleManager_Left" width="280px" height="1224px" style="background:#FFFFFF;" valign="top" class="webpanel" >
		<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" >
			<tr><td height="28px" id="SampleManager_Left_Title" align="center" class="webpanel">
				 <b><util:message key='pt.ev.property.sampleTree'/></b> 
			</tr>
			<tr>
				<td valign="top" >
					<div id="SampleManager_TreeTabPage" style="overflow:auto; padding:2px;" ></div>
				</td>
			</tr>
			<tr><td> </td></tr>
		</table>
	</td>
	<td id="SampleManager_Right" valign="top">
		<div class="webpanel">
			<div id="SampleManager_PageTabPage">
				<br style='line-height:5px;'>
				<div>
					<form id="SampleManager_SearchForm" name="SampleManager_SearchForm" style="display:inline" action="javascript:aSampleManager.doSearch('SampleManager_SearchForm')" onkeydown="if(event.keyCode==13) aSampleManager.doSearch('SampleManager_SearchForm')" method="post">
					  <table width="95%">
						<input type='hidden' name='sortMethod' value='ASC'/>                    
						<input type='hidden' name='sortColumn' value=''/>  
						<input type='hidden' name='pageNo' value='1'/>
						<!--input type='hidden' name='pageSize' value='10'/-->
						<input type='hidden' name='pageFunction' value='aSampleManager.doPage'/>
						<input type='hidden' name='formName' value='SampleManager_SearchForm'/>
					 
						<input type='hidden' id='SampleManager_Master_parentId' name='parentId' value=''/>
					  <tr>
						<td align="right">
						
							<input type="text" name="shortPathCond" size="15" class="webtextfield" value="*<util:message key='pt.ev.property.shortPath'/>*" onfocus="whenSrchFocus(this,'*<util:message key='pt.ev.property.shortPath'/>*');" onblur="whenSrchBlur(this,'*<util:message key='pt.ev.property.shortPath'/>*');"/> 
										
							<input type="text" name="principalNameCond" size="20" class="webtextfield" value="*<util:message key='pt.ev.property.principalName'/>*" onfocus="whenSrchFocus(this,'*<util:message key='pt.ev.property.principalName'/>*');" onblur="whenSrchBlur(this,'*<util:message key='pt.ev.property.principalName'/>*');"/> 
										
							<select name='pageSize'>
								<option value="5">5</option>
								<option value="10" selected>10</option>
								<option value="20" >20</option>
								<option value="50">50</option>
								<option value="100">100</option>
							</select>
							<span class="btn_pack small"><a href="javascript:aSampleManager.doSearch('SampleManager_SearchForm')"><util:message key='pt.ev.button.search'/></a></span>
						</td>
					  </tr>
					  </table>    
					</form>
				</div>
				<div class="webgridpanel">
				  <form id="SampleManager_ListForm" style="display:inline" name="SampleManager_ListForm" action="" method="post">
				  <table id="grid-table" width="98%" border="0" cellspacing="0" cellpadding="0">
					<thead>
						<tr> 
							<td colspan="100" class="webgridheaderline"></td>
						</tr>
						<tr style="cursor: pointer;">
							<th class="webgridheader" align="center" width="30px">
								<input type="checkbox" id="delCheck" class="webcheckbox" onclick="aSampleManager.m_checkBox.chkAll(this)"/>
							</th>
							<th class="webgridheader" align="center" width="30px">No</th>
						
							<th class="webgridheader" ch="0" onclick="aSampleManager.doSort(this, 'PRINCIPAL_ID');" >
								<span ><util:message key='pt.ev.property.principalId'/></span>
							</th>	
							<th class="webgridheader" ch="0" onclick="aSampleManager.doSort(this, 'SHORT_PATH');" >
								<span ><util:message key='pt.ev.property.shortPath'/></span>
							</th>	
							<th class="webgridheader" ch="0" onclick="aSampleManager.doSort(this, 'PRINCIPAL_NAME');" >
								<span ><util:message key='pt.ev.property.principalName'/></span>
							</th>	
							<th class="webgridheader" ch="0" onclick="aSampleManager.doSort(this, 'MODIFIED_DATE');" >
								<span ><util:message key='pt.ev.property.modifiedDate'/></span>
							</th>			
						</tr>
					</thead>
					<tbody id="SampleManager_ListBody">

					</tbody>
				  </table>
				  </form>
				  <div id="SampleManager_ListMessage"/>
				</div>
				<div>
					<table style="width:95%;" class="webbuttonpanel">
					<tr>
					    <td align="center">
					    <div id="SampleManager_PAGE_ITERATOR" class="webnavigationpanel">
					    </div>
					    </td>    
					</tr>
					<tr>
					    <td align="right">
							<span class="btn_pack small"><a href="javascript:aSampleManager.doRemove()"><util:message key='pt.ev.button.remove'/></a></span>
					    </td>
					</tr>
					</table>
				
					<div id="SampleManager_EditFormPanel" class="webformpanel" >  
					<div id="SampleManager_propertyTabs">
						<ul>
							<li><a href="#SampleManager_DetailTabPage"><util:message key='pt.ev.property.detailTab'/></a></li>   
						</ul>
						<div id="SampleManager_DetailTabPage" style="width:100%;">
							<%@include file="sampleDetail.jsp"%>
						</div> <!--xsl:value-of select="@name"/>Manager_DetailTabPage -->
					</div>
					</div> <!-- End SampleManager_EditFormPanel -->
				</div> <!-- End webformpanel -->
			</div> <!-- End SampleManager_PageTabPage -->
		</div>
	</td>
<tr>
</table>

