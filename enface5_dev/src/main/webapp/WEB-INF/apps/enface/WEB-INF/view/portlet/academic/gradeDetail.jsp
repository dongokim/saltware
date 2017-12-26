
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/face/css/academic/styles.css">

<script type="text/javascript" src="<%=request.getContextPath()%>/face/javascript/academic/gradeManager.js"></script>

<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
<tr>
	<td width="100%" valign="top">
		<div style=" " class="webpanel">
			<div id="GradeManager_GradeTabPage">
				<br style='line-height:5px;'>

					<div id="GradeManager_EditFormPanel" class="webformpanel" >  
					<div id="propertyTabs">
						<div id="GradeManager_DetailTabPage" style="width:100%;">
							<div class="webformpanel" style="width:100%;">
								<form id="GradeManager_EditForm" style="display:inline" action="" method="post">
									<table cellpadding=0 cellspacing=0 border=0 width='100%'>
									<input type="hidden" id="GradeManager_isCreate" value="<c:out value="${isCreate}"/>">
									
									<input type="hidden" id="GradeManager_stuNo" name="stuNo"> 
									<input type="hidden" id="GradeManager_stuYear" name="stuYear"> 
									<input type="hidden" id="GradeManager_stuSeme" name="stuSeme"> 
									<input type="hidden" id="GradeManager_subjName" name="subjName"> 
									<input type="hidden" id="GradeManager_gainGrade" name="gainGrade"> 
									<input type="hidden" id="GradeManager_score" name="score"> 
									<tr> 
										<td colspan="2" width="100%" class="webformheaderline"></td>    
									</tr>
								
								</table>
								<table style="width:100%;" class="webbuttonpanel">
								<tr>
									<td align="right">  
										<img src="<%=request.getContextPath()%>/face/images/academic/button/btn_list.gif" hspace="2" style="cursor:hand" onclick="javascript:aGradeManager.doRetrieve()">
										<img src="<%=request.getContextPath()%>/face/images/academic/button/btn_save.gif" hspace="2" style="cursor:hand" onclick="javascript:aGradeManager.doUpdate()">
										<img src="<%=request.getContextPath()%>/face/images/academic/button/btn_delete.gif" hspace="2" style="cursor:hand" onclick="javascript:aGradeManager.doRemove()">
									</td>
								</tr>
								</table>
								</form>
							</div>
						</div> <!--xsl:value-of select="@name"/>Manager_DetailTabPage -->
					</div>
					</div> <!-- End GradeManager_EditFormPanel -->
				</div> <!-- End webformpanel -->
			</div> <!-- End GradeManager_GradeTabPage -->
		</div>
	</td>
<tr>
</table>

<div id="GradeManager_GradeChooser"></div>

