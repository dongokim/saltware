
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/face/css/styles.css">

<script type="text/javascript">
function initLectureManager() {
	
}
function finishLectureManager() {
	
}
// Attach to the onload event
if (window.attachEvent)
{
    window.attachEvent ( "onload", initLectureManager );
	window.attachEvent ( "onunload", finishLectureManager );
}
else if (window.addEventListener )
{
    window.addEventListener ( "load", initLectureManager, false );
	window.addEventListener ( "unload", finishLectureManager, false );
}
else
{
    window.onload = initLectureManager;
	window.onunload = finishLectureManager;
}
</script>


<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
<tr>
	<td width="100%" valign="top">
		<div style=" " class="webpanel">
			<div id="LectureManager_LectureTabPage">
				<br style='line-height:5px;'>
				<div>
					<form id="LectureManager_SearchForm" name="LectureManager_SearchForm" style="display:inline" action="javascript:aLectureManager.doSearch('LectureManager_SearchForm')" onkeydown="if(event.keyCode==13) aLectureManager.doSearch('LectureManager_SearchForm')" method="post">
					  <table width="100%">
						<input type='hidden' name='sortMethod' value='<c:out value="${searchCondition.sortMethod}"/>'/>                    
						<input type='hidden' name='sortColumn' value='<c:out value="${searchCondition.sortColumn}"/>'/>  
						<input type='hidden' name='pageNo' value='<c:out value="${searchCondition.pageNo}"/>'/>
						<!--input type='hidden' name='pageSize' value='<c:out value="${searchCondition.pageSize}"/>'/-->
						<input type='hidden' name='pageFunction' value='aLectureManager.doPage'/>
						<input type='hidden' name='formName' value='LectureManager_SearchForm'/>
					
					  <tr>
						<td align="right">
						
							<select name='pageSize'>
								<option value="5">5</option>
								<option value="10" selected>10</option>
								<option value="20" >20</option>
								<option value="50">50</option>
								<option value="100">100</option>
							</select>
							<span class="btn_pack small"><a href="javascript:aLectureManager.doSearch('LectureManager_SearchForm')"><util:message key='ev.title.search'/></a></span>
						</td>
					  </tr>
					  </table>    
					</form>
				</div>
				<div class="webgridpanel">
				  <form id="LectureManager_ListForm" style="display:inline" name="LectureManager_ListForm" action="" method="post">
				  <table id="grid-table" width="100%" border="0" cellspacing="0" cellpadding="0">
					<thead>
						<tr> 
							<td colspan="100" class="webgridheaderline"></td>
						</tr>
						<tr style="cursor: pointer;">
							<th class="webgridheader" align="center" width="30px">
								<input type="checkbox" id="delCheck" class="webcheckbox" onclick="aLectureManager.m_checkBox.chkAll(this)"/>
							</th>
							<th class="webgridheader" align="center" width="30px">No</th>
							
						</tr>
					</thead>
					<tbody id="LectureManager_ListBody">
			
					
					<c:forEach items="${results}" var="lecture" varStatus="status">
						<tr ch="<c:out value="${status.index}"/>" style="cursor:pointer;" class="<c:choose><c:when test="{$status.index%2 == 0}">even</c:when><c:otherwise>odd</c:otherwise></c:choose>" onmouseover="whenListMouseOver(this)" onmouseout="whenListMouseOut(this)">
							<td align="center" class="webgridbody">
								<input type="checkbox" id="LectureManager[<c:out value="${status.index}"/>].checkRow" class="webcheckbox"/>
							
								<input type="hidden" id="LectureManager[<c:out value="${status.index}"/>].userId" value="<c:out value='${lecture.userId}'/>"/>
								<input type="hidden" id="LectureManager[<c:out value="${status.index}"/>].lectYear" value="<c:out value='${lecture.lectYear}'/>"/>
								<input type="hidden" id="LectureManager[<c:out value="${status.index}"/>].lectSeme" value="<c:out value='${lecture.lectSeme}'/>"/>
								<input type="hidden" id="LectureManager[<c:out value="${status.index}"/>].subjName" value="<c:out value='${lecture.subjName}'/>"/>
							</td>
							<td class="webgridbody">
								<span><c:out value="${status.index}"/></span>
							</td>
							
						</tr>
					</c:forEach>
				
					</tbody>
				  </table>
				  </form>
				  <div id="LectureManager_ListMessage">
					<c:out value='${resultMessage}'/>
				  </div>
				</div>
				<div>
					<table style="width:100%;" class="webbuttonpanel">
					<tr>
					    <td align="center">
					    <div id="LectureManager_PAGE_ITERATOR" class="webnavigationpanel">
							<c:out escapeXml='false' value='${pageIterator}'/>
					    </div>
					    </td>    
					</tr>
					<tr>
					    <td align="right">
							<span class="btn_pack small"><a href="javascript:aLectureManager.doCreate()"><util:message key='ev.title.new'/></a></span>
							<span class="btn_pack small"><a href="javascript:aLectureManager.doRemoves()"><util:message key='ev.title.remove'/></a></span>
					    </td>
					</tr>
					</table>
				</div> <!-- End webformpanel -->
			</div> 
		</div>
	</td>
<tr>
</table>


