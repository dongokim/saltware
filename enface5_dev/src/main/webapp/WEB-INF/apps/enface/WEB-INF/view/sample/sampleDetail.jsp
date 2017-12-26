
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/face/css/styles.css">

<script type="text/javascript" src="<%=request.getContextPath()%>/face/javascript/sampleManager.js"></script>


<div class="webformpanel" style="width:100%;">
	<form id="SampleManager_EditForm" style="display:inline" action="" method="post">
		<table cellpadding=0 cellspacing=0 border=0 width='100%'>
		<input type="hidden" id="SampleManager_isCreate">
		
		<input type="hidden" id="SampleManager_parentId" name="parentId">	
		<input type="hidden" id="SampleManager_principalType" name="principalType">	
		<input type="hidden" id="SampleManager_principalOrder" name="principalOrder">	
		<tr> 
			<td colspan="2" width="100%" class="webformheaderline"></td>    
		</tr>
	
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/face/images/tb_icon.gif" width="5" height="5" align="absmiddle"> <util:message key='pt.ev.property.principalId'/></td>
		
			<td class="webformfield">
				
				<input type="text" id="SampleManager_principalId" name="principalId" value="<c:out value="${aSampleVO.principalId}"/>" maxLength="" class="full_webtextfield" />
					
			</td>
			
		</tr>
		
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/face/images/tb_icon.gif" width="5" height="5" align="absmiddle"> <util:message key='pt.ev.property.shortPath'/> *</td>
		
			<td class="webformfield">
				
				<input type="text" id="SampleManager_shortPath" name="shortPath" value="<c:out value="${aSampleVO.shortPath}"/>" maxLength="30" class="full_webtextfield" />
					
			</td>
			
		</tr>
		
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/face/images/tb_icon.gif" width="5" height="5" align="absmiddle"> <util:message key='pt.ev.property.principalName'/> *</td>
		
			<td class="webformfield">
				
				<input type="text" id="SampleManager_principalName" name="principalName" value="<c:out value="${aSampleVO.principalName}"/>" maxLength="40" class="full_webtextfield" />
					
			</td>
			
		</tr>
		
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/face/images/tb_icon.gif" width="5" height="5" align="absmiddle"> <util:message key='pt.ev.property.theme'/></td>
		
			<td class="webformfield">
				
				<input type="text" id="SampleManager_theme" name="theme" value="<c:out value="${aSampleVO.theme}"/>" maxLength="" class="full_webtextfield" />
					
			</td>
			
		</tr>
		
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/face/images/tb_icon.gif" width="5" height="5" align="absmiddle"> <util:message key='pt.ev.property.defaultPage'/></td>
		
			<td class="webformfield">
				
				<input type="text" id="SampleManager_defaultPage" name="defaultPage" value="<c:out value="${aSampleVO.defaultPage}"/>" maxLength="" class="full_webtextfield" />
					
			</td>
			
		</tr>
		
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/face/images/tb_icon.gif" width="5" height="5" align="absmiddle"> <util:message key='pt.ev.property.creationDate'/></td>
		
			<td class="webformfield"><input type="text" id="SampleManager_creationDate" name="creationDate" value="<c:out value="${aSampleVO.creationDate}"/>" class="full_webtextfield" /></td>
		</tr>
		
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/face/images/tb_icon.gif" width="5" height="5" align="absmiddle"> <util:message key='pt.ev.property.modifiedDate'/></td>
		
			<td class="webformfield"><input type="text" id="SampleManager_modifiedDate" name="modifiedDate" value="<c:out value="${aSampleVO.modifiedDate}"/>" class="full_webtextfield" /></td>
		</tr>
		
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/face/images/tb_icon.gif" width="5" height="5" align="absmiddle"> <util:message key='pt.ev.property.principalInfo01'/></td>
		
			<td class="webformfield">
				
				<input type="text" id="SampleManager_principalInfo01" name="principalInfo01" value="<c:out value="${aSampleVO.principalInfo01}"/>" maxLength="50" class="full_webtextfield" />
					
			</td>
			
		</tr>
		
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/face/images/tb_icon.gif" width="5" height="5" align="absmiddle"> <util:message key='pt.ev.property.principalInfo02'/></td>
		
			<td class="webformfield">
				
				<input type="text" id="SampleManager_principalInfo02" name="principalInfo02" value="<c:out value="${aSampleVO.principalInfo02}"/>" maxLength="50" class="full_webtextfield" />
					
			</td>
			
		</tr>
		
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/face/images/tb_icon.gif" width="5" height="5" align="absmiddle"> <util:message key='pt.ev.property.principalInfo03'/></td>
		
			<td class="webformfield">
				
				<input type="text" id="SampleManager_principalInfo03" name="principalInfo03" value="<c:out value="${aSampleVO.principalInfo03}"/>" maxLength="50" class="full_webtextfield" />
					
			</td>
			
		</tr>
		
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/face/images/tb_icon.gif" width="5" height="5" align="absmiddle"> <util:message key='pt.ev.property.principalDesc'/></td>
		
			<td class="webformfield">
				
				<input type="text" id="SampleManager_principalDesc" name="principalDesc" value="<c:out value="${aSampleVO.principalDesc}"/>" maxLength="80" class="full_webtextfield" />
					
			</td>
			
		</tr>
		
	</table>
	<table style="width:100%;" class="webbuttonpanel">
	<tr>
		<td align="right">  
			<img src="<%=request.getContextPath()%>/face/images/button/btn_create.gif" hspace="2" style="cursor:hand" onclick="javascript:aSampleManager.doCreate()">
			<img src="<%=request.getContextPath()%>/face/images/button/btn_save.gif" hspace="2" style="cursor:hand" onclick="javascript:aSampleManager.doUpdate(true)">
		</td>
	</tr>
	</table>
	</form>
</div>


