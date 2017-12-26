
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/face/css/styles.css">

<script type="text/javascript">
function initCompleteCreditManager() {
	
}
function finishCompleteCreditManager() {
	
}
// Attach to the onload event
if (window.attachEvent)
{
    window.attachEvent ( "onload", initCompleteCreditManager );
	window.attachEvent ( "onunload", finishCompleteCreditManager );
}
else if (window.addEventListener )
{
    window.addEventListener ( "load", initCompleteCreditManager, false );
	window.addEventListener ( "unload", finishCompleteCreditManager, false );
}
else
{
    window.onload = initCompleteCreditManager;
	window.onunload = finishCompleteCreditManager;
}
</script>


<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
<tr>
	<td width="100%" valign="top">
		<div style=" " class="webpanel">
			<div id="CompleteCreditManager_CompleteCreditTabPage">
				<br style='line-height:5px;'>
				<div>
					<form id="CompleteCreditManager_SearchForm" name="CompleteCreditManager_SearchForm" style="display:inline" action="javascript:aCompleteCreditManager.doSearch('CompleteCreditManager_SearchForm')" onkeydown="if(event.keyCode==13) aCompleteCreditManager.doSearch('CompleteCreditManager_SearchForm')" method="post">
					  <table width="100%">
						<input type='hidden' name='sortMethod' value='<c:out value="${searchCondition.sortMethod}"/>'/>                    
						<input type='hidden' name='sortColumn' value='<c:out value="${searchCondition.sortColumn}"/>'/>  
						<input type='hidden' name='pageNo' value='<c:out value="${searchCondition.pageNo}"/>'/>
						<!--input type='hidden' name='pageSize' value='<c:out value="${searchCondition.pageSize}"/>'/-->
						<input type='hidden' name='pageFunction' value='aCompleteCreditManager.doPage'/>
						<input type='hidden' name='formName' value='CompleteCreditManager_SearchForm'/>
					
					  <tr>
						<td align="right">
						
							<select name='pageSize'>
								<option value="5">5</option>
								<option value="10" selected>10</option>
								<option value="20" >20</option>
								<option value="50">50</option>
								<option value="100">100</option>
							</select>
							<span class="btn_pack small"><a href="javascript:aCompleteCreditManager.doSearch('CompleteCreditManager_SearchForm')"><util:message key='ev.title.search'/></a></span>
						</td>
					  </tr>
					  </table>    
					</form>
				</div>
				<div class="webgridpanel">
				  <form id="CompleteCreditManager_ListForm" style="display:inline" name="CompleteCreditManager_ListForm" action="" method="post">
				  <table id="grid-table" width="100%" border="0" cellspacing="0" cellpadding="0">
					<thead>
						<tr> 
							<td colspan="100" class="webgridheaderline"></td>
						</tr>
						<tr style="cursor: pointer;">
							<th class="webgridheader" align="center" width="30px">
								<input type="checkbox" id="delCheck" class="webcheckbox" onclick="aCompleteCreditManager.m_checkBox.chkAll(this)"/>
							</th>
							<th class="webgridheader" align="center" width="30px">No</th>
							
						</tr>
					</thead>
					<tbody id="CompleteCreditManager_ListBody">
			
					
					<c:forEach items="${results}" var="completecredit" varStatus="status">
						<tr ch="<c:out value="${status.index}"/>" style="cursor:pointer;" class="<c:choose><c:when test="{$status.index%2 == 0}">even</c:when><c:otherwise>odd</c:otherwise></c:choose>" onmouseover="whenListMouseOver(this)" onmouseout="whenListMouseOut(this)">
							<td align="center" class="webgridbody">
								<input type="checkbox" id="CompleteCreditManager[<c:out value="${status.index}"/>].checkRow" class="webcheckbox"/>
							
								<input type="hidden" id="CompleteCreditManager[<c:out value="${status.index}"/>].stuNo" value="<c:out value='${completecredit.stuNo}'/>"/>
								<input type="hidden" id="CompleteCreditManager[<c:out value="${status.index}"/>].stuYear" value="<c:out value='${completecredit.stuYear}'/>"/>
								<input type="hidden" id="CompleteCreditManager[<c:out value="${status.index}"/>].subjType" value="<c:out value='${completecredit.subjType}'/>"/>
							</td>
							<td class="webgridbody">
								<span><c:out value="${status.index}"/></span>
							</td>
							
						</tr>
					</c:forEach>
				
					</tbody>
				  </table>
				  </form>
				  <div id="CompleteCreditManager_ListMessage">
					<c:out value='${resultMessage}'/>
				  </div>
				</div>
				<div>
					<table style="width:100%;" class="webbuttonpanel">
					<tr>
					    <td align="center">
					    <div id="CompleteCreditManager_PAGE_ITERATOR" class="webnavigationpanel">
							<c:out escapeXml='false' value='${pageIterator}'/>
					    </div>
					    </td>    
					</tr>
					<tr>
					    <td align="right">
							<span class="btn_pack small"><a href="javascript:aCompleteCreditManager.doCreate()"><util:message key='ev.title.new'/></a></span>
							<span class="btn_pack small"><a href="javascript:aCompleteCreditManager.doRemoves()"><util:message key='ev.title.remove'/></a></span>
					    </td>
					</tr>
					</table>
				</div> <!-- End webformpanel -->
			</div> 
		</div>
	</td>
<tr>
</table>


