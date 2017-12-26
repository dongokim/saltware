
var aSampleManager = null;
SampleManager = function(evSecurityCode)
{
	this.m_ajax = new enview.util.Ajax();
	this.m_checkBox = new enview.util.CheckBoxUtil();
	this.m_validator = new enview.util.Validator();
	this.m_utility = new enview.util.Utility();
	this.m_contextPath = portalPage.getContextPath();
	this.m_selectRowIndex = 0;
	this.m_evSecurityCode = evSecurityCode;
	if( portalPage == null) portalPage = new enview.portal.Page();
	enviewMessageBox = new enview.portal.MessageBox(200, 100, 1000); 
}
SampleManager.prototype =
{
	m_ajax : null,
	m_checkBox : null,
	m_validator : null,
	m_utility : null,
	m_contextPath : null,
	m_selectRowIndex : 0,
	m_contextMenu : null,
	m_dataStructure : null,
	m_evSecurityCode : null,
	
	init : function() { 
		this.m_dataStructure = [
			{"fieldName":"principalId", "validation":""}
			,{"fieldName":"parentId", "validation":""}
			,{"fieldName":"shortPath", "validation":"Required,MaxLength", "maxLength":"30"}
			,{"fieldName":"principalName", "validation":"Required,MaxLength", "maxLength":"40"}
			,{"fieldName":"theme", "validation":""}
			,{"fieldName":"siteName", "validation":""}
			,{"fieldName":"defaultPage", "validation":""}
			,{"fieldName":"principalType", "validation":""}
			,{"fieldName":"principalOrder", "validation":""}
			,{"fieldName":"creationDate", "validation":""}
			,{"fieldName":"modifiedDate", "validation":""}
			,{"fieldName":"principalInfo01", "validation":"MaxLength", "maxLength":"50"}
			,{"fieldName":"principalInfo02", "validation":"MaxLength", "maxLength":"50"}
			,{"fieldName":"principalInfo03", "validation":"MaxLength", "maxLength":"50"}
			,{"fieldName":"principalDesc", "validation":"MaxLength", "maxLength":"80"}
			
		]
		
		$(function() {
			$("#SampleManager_propertyTabs").tabs();
		});
		
		$.datepicker.setDefaults({
			showMonthAfterYear: true,
			changeMonth: true,
			changeYear: true,
			showButtonPanel: true,
			showAnim : 'slideDown',
			dateFormat : 'yy-mm-dd'
		});
		
	},
	initSearch : function () {
		var formElem = document.forms[ "SampleManager_SearchForm" ];
		formElem.elements[ "pageNo" ].value = 1;
		//formElem.elements[ "pageSize" ].value = 10;
		this.m_selectRowIndex = 0;
	},
	doRetrieve : function () {
		//this.doCreate();
		
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&";
		var formElem = document.forms[ "SampleManager_SearchForm" ];
	    for (var i=0; i < formElem.elements.length; i++) {
			var tmp = formElem.elements[ i ].value;
			if( tmp.indexOf("*") != 0 ) {
				param += formElem.elements[ i ].name + "=" + encodeURIComponent( tmp ) + "&";
			}
	    }
		
		this.m_ajax.send("POST", this.m_contextPath + "/sample/listForAjax.admin", param, false, {success: function(data) { aSampleManager.doRetrieveHandler(data); }});
	},
	doRetrieveHandler : function( resultObject ) {
		this.m_utility.setListData(
			"SampleManager",
			new Array('principalId'),
			new Array('principalId', 'shortPath', 'principalName', 'modifiedDate'),
			this.m_contextPath,
			resultObject);
				
		if(resultObject.Data.length != 0) {
			//this.doDefaultSelect(); 
		}
		else {
			var param = "evSecurityCode=" + this.m_evSecurityCode + "&principalId=" + aSampleManager.m_tree.getSelectedItemId();
			this.m_ajax.send("POST", this.m_contextPath + "/sample/detailForAjax.admin", param, false, {success: function(data) { aSampleManager.doSelectHandler(data); }});
		}
	},
	doPage : function (formName, pageNo)
	{
		var formElem = document.forms[ formName ];
	    formElem.elements[ "pageNo" ].value = pageNo;
		
		this.m_selectRowIndex = 0;
		this.doRetrieve();
	},
	doSearch : function (formName)
	{
		var formElem = document.forms[ formName ];
	    formElem.elements[ "pageNo" ].value = 1;
		
		this.m_selectRowIndex = 0;
		this.doRetrieve();
	},
	doSort : function (obj, sortColumn)
	{
		var formElem = document.forms[ "SampleManager_SearchForm" ];
	    formElem.elements[ "sortColumn" ].value = sortColumn;
	    if( obj.ch % 2 == 0 ) {
			formElem.elements[ "sortMethod" ].value = "ASC";
	        obj.ch = 1;
	    }
	    else {
	        formElem.elements[ "sortMethod" ].value = "DESC";
	        obj.ch = 0;
	    }
		
		this.m_selectRowIndex = 0;
		this.doRetrieve();
	},
	doSelectTreeNode : function (id)
	{
		var param = "principalId=" + id;
		param += "&evSecurityCode=" + this.m_evSecurityCode;
		this.m_ajax.send("POST", this.m_contextPath + "/sample/detailForAjax.admin", param, false, {success: function(data) { aSampleManager.doSelectHandler(data); }});
	},
	doDefaultSelect : function ()
	{
		document.getElementById('SampleManager[' + this.m_selectRowIndex + '].checkRow').checked = true;
		
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&";
	
		param += "principalId=";
		param += document.getElementById('SampleManager[' + this.m_selectRowIndex + '].principalId').value;
		
		this.m_ajax.send("POST", this.m_contextPath + "/sample/detailForAjax.admin", param, false, {success: function(data) { aSampleManager.doSelectHandler(data); }});
	},
	doSelect : function (obj)
	{
		var rowSeq = 0;
	    if(obj.nodeName=="SPAN") {
	        rowSeq = obj.parentNode.parentNode.getAttribute("ch");
	    }
	    else if(obj.nodeName=="TD") {
	        rowSeq = obj.parentNode.getAttribute("ch");
	    }
	    else if(obj.nodeName=="TR") {
	        rowSeq = obj.getAttribute("ch");
	    }
		
		this.m_selectRowIndex = rowSeq;
		
	    this.m_checkBox.unChkAll( document.getElementById("SampleManager_ListForm") );
	    document.getElementById('SampleManager[' + rowSeq + '].checkRow').checked = true;
		
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&";
	
		param += "principalId=";
		param += document.getElementById('SampleManager[' + rowSeq + '].principalId').value;
		
		this.m_ajax.send("POST", this.m_contextPath + "/sample/detailForAjax.admin", param, false, {success: function(data) { aSampleManager.doSelectHandler(data); }});
	},
	doSelectHandler : function(resultObject)
	{
		this.m_utility.setFormDataFromXML(this.m_dataStructure, "SampleManager", resultObject);
		
		document.getElementById("SampleManager_gradeId").value = portalPage.getAjax().retrieveElementValue("gradeId", resultObject);
		
		document.getElementById("SecurityPermission_principalId").value = document.getElementById("SampleManager_principalId").value;
	
		document.getElementById("SampleManager_shortPath").readOnly = true;
		document.getElementById("SampleManager_creationDate").readOnly = true;
		document.getElementById("SampleManager_modifiedDate").readOnly = true;
		
		
		var propertyTabs = $("#SampleManager_propertyTabs").tabs();
	 
		propertyTabs.tabs('enable', 1);	 
		propertyTabs.tabs('enable', 2);	 
		propertyTabs.tabs('enable', 3);	 
		propertyTabs.tabs('enable', 4);	
		var propSelectedTabId = propertyTabs.tabs('option', 'selected');
		this.onSelectPropertyTab( propSelectedTabId );
		
		document.getElementById("SampleManager_EditFormPanel").style.display = '';
	},
	doCreate : function() 
	{
		this.m_utility.initFormData(this.m_dataStructure, "SampleManager");
		
		var propertyTabs = $("#SampleManager_propertyTabs").tabs();
		propertyTabs.tabs('select', 0);
	 
		propertyTabs.tabs('disable', 1);	 
		propertyTabs.tabs('disable', 2);	 
		propertyTabs.tabs('disable', 3);	 
		propertyTabs.tabs('disable', 4);	 
		document.getElementById("SampleManager_parentId").value = document.getElementById("SampleManager_Master_parentId").value; 
		document.getElementById("SampleManager_shortPath").readOnly = false;
		document.getElementById("SampleManager_EditFormPanel").style.display = '';
	},
	doUpdate : function (forDetail)
	{
		var elm = null;
		var val = null;
		var isCreate = document.getElementById("SampleManager_isCreate").value;
		var form = document.getElementById("SampleManager_EditForm");

		var isValid = this.m_validator.validate(this.m_dataStructure, form);
		if( isValid == false ) return;
		
		var param = this.m_utility.getFormData(this.m_dataStructure, form);
		param += "evSecurityCode=" + this.m_evSecurityCode + "&";
		param += "gradeId=" + document.getElementById("SampleManager_gradeId").value;
		
		if( isCreate == "true" ) {
			this.m_ajax.send("POST", this.m_contextPath + "/sample/addForAjax.admin", param, false, {
				success: function(data){
					aSampleManager.onRefresh( document.getElementById("SampleManager_parentId").value );
					
					aSampleManager.doRetrieve();
					
					enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.add') );
				}});
		}
		else {
			this.m_ajax.send("POST", this.m_contextPath + "/sample/updateForAjax.admin", param, false, {
				success: function(data){
					aSampleManager.onRefresh( document.getElementById("SampleManager_parentId").value );
					
					aSampleManager.doRetrieve();
					
					enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.update') );
				}});
		}
	},
	doRemove : function () {
		var rowCnts = this.m_checkBox.getCheckedElements( document.getElementById("SampleManager_ListForm") );
		if( rowCnts == "" ) return;

		var ret = confirm( portalPage.getMessageResource('pt.ev.message.remove') );
	    if( ret == true ) {
	        var formData = "";
	        var rowCntArray = rowCnts.split(",");
			
			var principalId = null;
			var param = "evSecurityCode=" + this.m_evSecurityCode;
	        for(i=0; i<rowCntArray.length; i++) {
				param += "&pks=";
				param += document.getElementById('SampleManager[' + rowCntArray[i] + '].principalId').value;
				principalId = document.getElementById('SampleManager[' + rowCntArray[i] + '].principalId').value;
	        }
			var parentId = aSampleManager.m_tree.getParentId(principalId);
			
			this.m_ajax.send("POST", this.m_contextPath + "/sample/removeForAjax.admin", param, false, {
				success: function(data){
					aSampleManager.onRefresh( parentId );
					
					aSampleManager.initSearch();
					aSampleManager.doRetrieve();
					enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.remove') );
				}});
		}
	}
	
}

