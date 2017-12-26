
var aUserMenuManager = null;
UserMenuManager = function()
{
	this.m_ajax = new enview.util.Ajax();
	this.m_checkBox = new enview.util.CheckBoxUtil();
	this.m_validator = new enview.util.Validator();
	this.m_utility = new enview.util.Utility();
	this.m_contextPath = this.m_utility.getContextPath();
	this.m_selectRowIndex = 0;
	if( portalPage == null) portalPage = new enview.portal.Page();
	enviewMessageBox = new enview.portal.MessageBox(200, 100, 1000); 
}
UserMenuManager.prototype =
{
	m_ajax : null,
	m_checkBox : null,
	m_validator : null,
	m_utility : null,
	m_contextPath : null,
	m_selectRowIndex : 0,
	m_dataStructure : null,

	
	init : function() { 
		
		this.m_dataStructure = [
			{"fieldName":"principalId", "validation":""}
			,{"fieldName":"pageId", "validation":""}
			,{"fieldName":"parentId", "validation":""}
			,{"fieldName":"menuType", "validation":""}
			,{"fieldName":"menuOrder", "validation":""}
			
		]
		
		$(function() {
			$("#UserMenuManager_propertyTabs").tabs();
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
	/*
	getUserMenuChooser : function () {
		if( aUserMenuChooser == null ) {
			this.m_ajax.send("POST", this.m_contextPath + "/userMenu/listForChooser.admin", "", false, {
				success: function(data){
					//alert(data);
					document.getElementById("UserMenuManager_UserMenuChooser").innerHTML = data;
					aUserMenuChooser = new UserMenuChooser( aUserMenuManager.setUserMenuChooserCallback );
				}});
		}
		return aUserMenuChooser;
	},
	setUserMenuChooserCallback : function (rowArray) {
		var chooseVal = "";
		for(var i=0; i<rowArray.length; i++) {
		
			chooseVal += rowArray[i].get("principalId") + ":";
		
			chooseVal += rowArray[i].get("pageId") + ":";
		
		}
		alert("chooseValue=" + chooseVal);
	},
	*/
	onSelectPropertyTab : function (id) {
		var param = "";	
		switch(id) {
			case 0 : 
				break;
		
		}
		
		return true; 
	},
	initSearch : function () {
		var formElem = document.forms[ "UserMenuManager_SearchForm" ];
		formElem.elements[ "pageNo" ].value = 1;
		//formElem.elements[ "pageSize" ].value = 10;
	},
	doRetrieve : function () {
		this.doCreate();
		
		var param = "";
		var formElem = document.forms[ "UserMenuManager_SearchForm" ];
	    for (var i=0; i < formElem.elements.length; i++) {
			var tmp = formElem.elements[ i ].value;
			if( tmp.indexOf("*") != 0 ) {
				param += formElem.elements[ i ].name + "=" + encodeURIComponent( tmp ) + "&";
			}
	    }
		
		this.m_ajax.send("POST", this.m_contextPath + "/userMenu/listForAjax.admin", param, false, {success: function(data) { aUserMenuManager.doRetrieveHandler(data); }});
	},
	doRetrieveHandler : function( resultObject ) {
		
		this.m_utility.setListData(
			"UserMenuManager",
			new Array('principalId', 'pageId'),
			new Array(),
			this.m_contextPath,
			resultObject);
				
		if(resultObject.Data.length != 0) {
			this.doDefaultSelect(); 
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
		var formElem = document.forms[ "UserMenuManager_SearchForm" ];
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
	doDefaultSelect : function ()
	{
		document.getElementById('UserMenuManager[' + this.m_selectRowIndex + '].checkRow').checked = true;
		
		var param = "";
	
		param += "principalId=";
		param += document.getElementById('UserMenuManager[' + this.m_selectRowIndex + '].principalId').value;
		param += "&pageId=";
		param += document.getElementById('UserMenuManager[' + this.m_selectRowIndex + '].pageId').value;
		
		this.m_ajax.send("POST", this.m_contextPath + "/userMenu/detailForAjax.admin", param, false, {success: function(data) { aUserMenuManager.doSelectHandler(data); }});
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
		
	    this.m_checkBox.unChkAll( document.getElementById("UserMenuManager_ListForm") );
	    document.getElementById('UserMenuManager[' + rowSeq + '].checkRow').checked = true;
		
		var param = "";
	
		param += "principalId=";
		param += document.getElementById('UserMenuManager[' + rowSeq + '].principalId').value;
		param += "&pageId=";
		param += document.getElementById('UserMenuManager[' + rowSeq + '].pageId').value;
			
		this.m_ajax.send("POST", this.m_contextPath + "/userMenu/detailForAjax.admin", param, false, {success: function(data) { aUserMenuManager.doSelectHandler(data); }});
	},
	doSelectHandler : function(resultObject)
	{
		this.m_utility.setFormData(this.m_dataStructure, "UserMenuManager", resultObject);
		
	
		
		
		var propertyTabs = $("#UserMenuManager_propertyTabs").tabs();
	
		var propSelectedTabId = propertyTabs.tabs('option', 'selected');
		this.onSelectPropertyTab( propSelectedTabId );
		
		document.getElementById("UserMenuManager_EditFormPanel").style.display = '';
	},
	doCreate : function() 
	{
		this.m_utility.initFormData(this.m_dataStructure, "UserMenuManager");
		
		var propertyTabs = $("#UserMenuManager_propertyTabs").tabs();
		propertyTabs.tabs('select', 0);
	
		document.getElementById("UserMenuManager_principalId").readOnly = false;
		document.getElementById("UserMenuManager_pageId").readOnly = false;
		document.getElementById("UserMenuManager_EditFormPanel").style.display = '';
	},
	doUpdate : function (forDetail)
	{
		var elm = null;
		var val = null;
		var param = "";
		var isCreate = document.getElementById("UserMenuManager_isCreate").value;
		var form = document.getElementById("UserMenuManager_EditForm");

		var isValid = this.m_validator.validate(this.m_dataStructure, form);
		if( isValid == false ) return;
		
		var param = this.m_utility.getFormData(this.m_dataStructure, form);
	
		if( isCreate == "true" ) {
			this.m_ajax.send("POST", this.m_contextPath + "/userMenu/addForAjax.admin", param, false, {
				success: function(data){
					if( forDetail == null || forDetail == false ) {
						aUserMenuManager.doRetrieve();
					}
					enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.add') );
				}});
		}
		else {
			this.m_ajax.send("POST", this.m_contextPath + "/userMenu/updateForAjax.admin", param, false, {
				success: function(data){
					if( forDetail == null || forDetail == false ) {
						aUserMenuManager.doRetrieve();
					}
					enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.update') );
				}});
		}
	},
	doRemove : function () {
		var rowCnts = this.m_checkBox.getCheckedElements( document.getElementById("UserMenuManager_ListForm") );
		if( rowCnts == "" ) return;

		var ret = confirm( portalPage.getMessageResource('pt.ev.message.remove') );
	    if( ret == true ) {
	        var formData = "";
	        var rowCntArray = rowCnts.split(",");
			
			var param = "";
	        for(i=0; i<rowCntArray.length; i++) {
				param += "&pks=";
				param += 
					document.getElementById('UserMenuManager[' + rowCntArray[i] + '].principalId').value + ":" +
					document.getElementById('UserMenuManager[' + rowCntArray[i] + '].pageId').value;
	        }
			
			this.m_ajax.send("POST", this.m_contextPath + "/userMenu/removeForAjax.admin", param, false, {
				success: function(data){
					aUserMenuManager.m_selectRowIndex = 0;
					aUserMenuManager.doRetrieve();
					enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.remove') );
				}});
			
		}
	}
	
}

var aUserMenuChooser = null;
UserMenuChooser = function(callback, parent)
{
	this.m_ajax = new enview.util.Ajax();
	this.m_checkBox = new enview.util.CheckBoxUtil();
	if( portalPage == null) portalPage = new enview.portal.Page();
	this.m_selectRowIndex = 0;
	this.m_contextPath = this.m_utility.getContextPath();
	this.m_domElement = document.getElementById("UserMenuManager_UserMenuChooser");
	this.m_callback = callback;
	this.m_parent = parent;
	enviewMessageBox = new enview.portal.MessageBox(200, 100, 1000); 
	
	$("#UserMenuManager_UserMenuChooser").dialog({
		autoOpen: false,
		resizable: false,
		width:540, 
		height:400,
		modal: true,
		buttons: {
			"Apply": function() {
				aUserMenuChooser.doApply();
			},
			Cancel: function() {
				$(this).dialog('close');
			}
		}
	});
	
	this.init();
}

UserMenuChooser.prototype =
{
	m_domElement : null,
	m_checkBox : null,
	m_sourceElement : null,
	m_callback : null,
	m_parent : null,
	
	init : function() {
		
	},
	whenSrchFocus : function ( obj, lvS ) {
		if( obj.value == lvS ) obj.value = "";
	},
	whenSrchBlur : function ( obj, lvS ) {
		if( obj.value == "" ) obj.value = lvS;
	},
	doShow : function (source)
	{
		if( source ) {
			this.m_sourceElement = source;
		}
		
		this.doRetrieve();
		$('#UserMenuManager_UserMenuChooser').dialog('open');
	},
	doRetrieve : function () {
		var param = "";
		var formElem = document.forms[ "UserMenuChooser_SearchForm" ];
	    for (var i=0; i < formElem.elements.length; i++) {
			var tmp = formElem.elements[ i ].value;
			if( tmp.indexOf("*") != 0 ) {
				param += formElem.elements[ i ].name + "=" + encodeURIComponent( tmp ) + "&";
			}
	    }
		
		this.m_ajax.send("POST", this.m_contextPath + "/userMenu/listForAjax.admin", param, false, {success: function(data) { aUserMenuChooser.doRetrieveHandler(data); }});
	},
	doRetrieveHandler : function( resultObject ) {
		this.m_utility.setListData(
			"UserMenuChooser",
			new Array('principalId', 'pageId'),
			new Array(),
			this.m_contextPath,
			resultObject);
	},
	doPage : function (formName, pageNo)
	{
		var formElem = document.forms[ formName ];
	    formElem.elements[ "pageNo" ].value = pageNo;
		
		this.doRetrieve();
	},
	doSearch : function (formName)
	{
		var formElem = document.forms[ formName ];
	    formElem.elements[ "pageNo" ].value = 1;
		
		this.doRetrieve();
	},
	doSort : function (obj, sortColumn)
	{
		var formElem = document.forms[ "UserMenuChooser_SearchForm" ];
	    formElem.elements[ "sortColumn" ].value = sortColumn;
	    if( obj.ch % 2 == 0 ) {
			formElem.elements[ "sortMethod" ].value = "ASC";
	        obj.ch = 1;
	    }
	    else {
	        formElem.elements[ "sortMethod" ].value = "DESC";
	        obj.ch = 0;
	    }
		
		this.doRetrieve();
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
		
		if( this.m_parent != null ) {
			this.m_checkBox.unChkAll( document.getElementById("UserMenuManager_ListForm") );
			document.getElementById('UserMenuChooser[' + rowSeq + '].checkRow').checked = true;
			this.m_parent.doApply();
			return;
		}
		
		$('#UserMenuManager_UserMenuChooser').dialog('close');
		
		if( this.m_sourceElement != null ) {
			
			this.m_sourceElement.value = document.getElementById('UserMenuChooser[' + rowSeq + '].principalId').value;	
			this.m_sourceElement.value = document.getElementById('UserMenuChooser[' + rowSeq + '].pageId').value;	
		}
		else {
			var result = new Array(1);
			var rowMap = new Map();
			
			rowMap.put("principalId", document.getElementById('UserMenuChooser[' + rowSeq + '].principalId').value);	
			rowMap.put("pageId", document.getElementById('UserMenuChooser[' + rowSeq + '].pageId').value);	
			result[0] = rowMap;
			
			this.m_callback(result);
		}
	},
	getSelectedItems : function ()
	{
		var rowCnts = this.m_checkBox.getCheckedElements( document.getElementById("UserMenuChooser_ListForm") );
		if( rowCnts == "" ) return;
		
		var rowCntArray = rowCnts.split(",");
		if( rowCntArray.length > 0 ) {
			if( rowCntArray.length > 1 ) {
				alert( portalPage.getMessageResource('pt.ev.message.select.onlyone') );
				return;
			}
			
			var result = new Array(rowCntArray.length);
			for(var i=0; i<rowCntArray.length; i++) {
				var rowMap = new Map();
			
				rowMap.put("id", document.getElementById('UserMenuChooser[' + rowCntArray[i] + '].id').value);	
				result[i] = rowMap;
			}
			
			return result;
		}
		
		return null;
	},
	doApply : function ()
	{
		var rowCnts = this.m_checkBox.getCheckedElements( document.getElementById("UserMenuChooser_ListForm") );
		if( rowCnts == "" ) return;
		
		var rowCntArray = rowCnts.split(",");
		if( rowCntArray.length > 0 ) {
			if( rowCntArray.length > 1 ) {
				alert( portalPage.getMessageResource('pt.ev.message.select.onlyone') );
				return;
			}
			
			$('#UserMenuManager_UserMenuChooser').dialog('close');
			
			if( this.m_sourceElement != null ) {
				
				this.m_sourceElement.value = document.getElementById('UserMenuChooser[' + rowCntArray[0] + '].principalId').value;	
				this.m_sourceElement.value = document.getElementById('UserMenuChooser[' + rowCntArray[0] + '].pageId').value;	
			}
			else {
				var result = new Array(rowCntArray.length);
				for(var i=0; i<rowCntArray.length; i++) {
					var rowMap = new Map();
				
					rowMap.put("principalId", document.getElementById('UserMenuChooser[' + rowCntArray[i] + '].principalId').value);	
					rowMap.put("pageId", document.getElementById('UserMenuChooser[' + rowCntArray[i] + '].pageId').value);	
					result[i] = rowMap;
				}
				
				this.m_callback(result);
			}
		}
	}
}

