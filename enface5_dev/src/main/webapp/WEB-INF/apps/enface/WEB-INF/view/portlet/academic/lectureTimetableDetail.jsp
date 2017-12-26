
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/face/css/academic/styles.css">

<script type="text/javascript" src="<%=request.getContextPath()%>/face/javascript/academic/lectureTimetableManager.js"></script>

<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
<tr>
	<td width="100%" valign="top">
		<div style=" " class="webpanel">
			<div id="LectureTimetableManager_LectureTimetableTabPage">
				<br style='line-height:5px;'>

					<div id="LectureTimetableManager_EditFormPanel" class="webformpanel" >  
					<div id="propertyTabs">
						<div id="LectureTimetableManager_DetailTabPage" style="width:100%;">
							<div class="webformpanel" style="width:100%;">
								<form id="LectureTimetableManager_EditForm" style="display:inline" action="" method="post">
									<table cellpadding=0 cellspacing=0 border=0 width='100%'>
									<input type="hidden" id="LectureTimetableManager_isCreate" value="<c:out value="${isCreate}"/>">
									
									<input type="hidden" id="LectureTimetableManager_stuNo" name="stuNo"> 
									<input type="hidden" id="LectureTimetableManager_lectYear" name="lectYear"> 
									<input type="hidden" id="LectureTimetableManager_lectSeme" name="lectSeme"> 
									<input type="hidden" id="LectureTimetableManager_lectDow" name="lectDow"> 
									<input type="hidden" id="LectureTimetableManager_strtHr" name="strtHr"> 
									<input type="hidden" id="LectureTimetableManager_endHr" name="endHr"> 
									<input type="hidden" id="LectureTimetableManager_subjName" name="subjName"> 
									<tr> 
										<td colspan="2" width="100%" class="webformheaderline"></td>    
									</tr>
								
								</table>
								<table style="width:100%;" class="webbuttonpanel">
								<tr>
									<td align="right">  
										<img src="<%=request.getContextPath()%>/face/images/academic/button/btn_list.gif" hspace="2" style="cursor:hand" onclick="javascript:aLectureTimetableManager.doRetrieve()">
										<img src="<%=request.getContextPath()%>/face/images/academic/button/btn_save.gif" hspace="2" style="cursor:hand" onclick="javascript:aLectureTimetableManager.doUpdate()">
										<img src="<%=request.getContextPath()%>/face/images/academic/button/btn_delete.gif" hspace="2" style="cursor:hand" onclick="javascript:aLectureTimetableManager.doRemove()">
									</td>
								</tr>
								</table>
								</form>
							</div>
						</div> <!--xsl:value-of select="@name"/>Manager_DetailTabPage -->
					</div>
					</div> <!-- End LectureTimetableManager_EditFormPanel -->
				</div> <!-- End webformpanel -->
			</div> <!-- End LectureTimetableManager_LectureTimetableTabPage -->
		</div>
	</td>
<tr>
</table>

<div id="LectureTimetableManager_LectureTimetableChooser"></div>

