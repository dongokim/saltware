var aFileManager = null;
var oEditors = [];

FileManager = function(evSecurityCode) {
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

FileManager.prototype = { 
	m_ajax : null,
	m_checkBox : null,
	m_validator : null,
	m_utility : null,
	m_contextPath : null,
	m_selectRowIndex : 0,
	m_contextMenu : null,
	m_dataStructure : null,
	m_evSecurityCode : null,
	
	m_uploadDir : '/',
	m_selectedFilePath : null,
	
	oFCKeditor : null, 
	smarteditor : null,
	
	m_dhtmlxContextMenu : null,
	
	init : function() { 
		this.m_dataStructure = [
			{"fieldName":"filePath", "validation":""}
			,{"fieldName":"fileName", "validation":""}
			,{"fieldName":"fileSize", "validation":""}
			,{"fieldName":"modifiedDate", "validation":""}
		];
		this.m_contextMenu = new FileMenu();
		
		this.m_dhtmlxContextMenu = new dhtmlXMenuObject();
		this.m_dhtmlxContextMenu.renderAsContextMenu();
		this.m_dhtmlxContextMenu.attachEvent("onClick",this.onDhtmlxContextClick);
		this.m_dhtmlxContextMenu.loadFromHTML("dhtmlx_context_data", true);
		
		$(function() {
			$("#FileManager_propertyTabs").tabs();
		});
		
		this.m_tree = new dhtmlXTreeObject(document.getElementById('FileManager_TreeTabPage'),"100%","100%",0);
		this.m_tree.setImagePath(this.m_contextPath + "/dhtmlx/imgs/dhxtree_skyblue/");
		this.m_tree.setOnClickHandler( this.onNodeSelect );
		this.m_tree.enableDragAndDrop(true);
		this.m_tree.setDragHandler( this.onDragAndDrop );
		//this.m_tree.setOnRightClickHandler( this.onContextMenuHandler );
		this.m_tree.enableContextMenu(this.m_dhtmlxContextMenu);
		this.m_tree.attachEvent("onBeforeContextMenu", function (id) {
			aFileManager.onNodeSelect( id );
			aFileManager.m_contextMenu.setItemId( id );
			return true;
		});
		this.m_tree.enableAutoTooltips(true);
		//this.m_tree.setChildCalcMode(true);
		//this.m_tree.enableItemEditor(true);
		//this.m_tree.enableKeyboardNavigation(true);
		this.m_tree.setXMLAutoLoading(this.m_contextPath + "/file/retrieveTreeForAjax.admin?evSecurityCode=" + this.m_evSecurityCode);
		this.m_tree.load(this.m_contextPath + "/file/retrieveTreeForAjax.admin?id=/&evSecurityCode=" + this.m_evSecurityCode);

		$.datepicker.setDefaults({
			showMonthAfterYear: true,
			changeMonth: true,
			changeYear: true,
			showButtonPanel: true,
			showAnim : 'slideDown',
			dateFormat : 'yy-mm-dd'
		});
			
		document.onkeyup = aFileManager.onCheckKey;
	},
	getFileUploader : function () {
		aFileManager.getContextMenu().hide();
		if( aFileUploader == null ) {
			aFileUploader = new FileUploader( aFileManager.m_evSecurityCode);
		}
		aFileUploader.setUploadDir( this.m_uploadDir );
		return aFileUploader;
	},
	setFileUploaderCallback : function (rowArray) {
		var chooseVal = "";
		for(var i=0; i<rowArray.length; i++) {
			chooseVal += rowArray[i].get("filePath") + ":";
		}
	},
	getContextMenu : function() {
		return this.m_contextMenu;
	},
	onContextMenuHandler : function (treeitemId, item) {
		aFileManager.doShowContextMenu( treeitemId, item );
	},
	onDhtmlxContextClick : function (menuitemId, type) {
		if (menuitemId == "onRefresh") {
			aFileManager.onRefresh();
		} else if (menuitemId == "onCreate") {
			aFileManager.onCreate();
		} else if (menuitemId == "onMoveUp") {
			aFileManager.onMoveUp();
		} else if (menuitemId == "onMoveDown") {
			aFileManager.onMoveDown();
		} else if (menuitemId == "onUpload") {
			aFileManager.getFileUploader().doShow();
		}
	},
	onSelect : function (treeitemId) {
		aFileManager.doSelect( treeitemId );
	},
	onRefresh : function () {
		var treeitemId = aFileManager.getContextMenu().getItemId();
		aFileManager.getContextMenu().hide();
		if( treeitemId == 1 ) aFileManager.m_tree.deleteChildItems( 0 );
		aFileManager.m_tree.refreshItem( treeitemId );
	},
	onCheckKey : function (a_event) {
		if( document.all ) {
			if( event.keyCode ) {
				if( event.ctrlKey == true && event.keyCode == 90 ) {
					aFileManager.toggleMaximize();
				}
				else if( event.ctrlKey == true && event.keyCode == 38 ) {
					aFileManager.doMoveUpNodeTree( aFileManager.m_tree.getSelectedItemId() );
				}
				else if( event.ctrlKey == true && event.keyCode == 40 ) {
					aFileManager.doMoveDownNodeTree( aFileManager.m_tree.getSelectedItemId() );
				}
			}
		}
		else {
			if( a_event.ctrlKey == true && a_event.keyCode == 90 ) {
				aFileManager.toggleMaximize();
			}
			else if( a_event.ctrlKey == true && a_event.keyCode == 38 ) {
				aFileManager.doMoveUpNodeTree( aFileManager.m_tree.getSelectedItemId() );
			}
			else if( a_event.ctrlKey == true && a_event.keyCode == 40 ) {
				aFileManager.doMoveDownNodeTree( aFileManager.m_tree.getSelectedItemId() );
			}
		}
	},
	onCreate : function () {
		var treeitemId = aFileManager.getContextMenu().getItemId();
		aFileManager.getContextMenu().hide();
		aFileManager.doCreate( treeitemId );
	},
	onDelete : function () {
		var treeitemId = aFileManager.getContextMenu().getItemId();
		aFileManager.getContextMenu().hide();
		return aFileManager.doDeleteTree(treeitemId);
	},
	onDragAndDrop : function (subKey, item_id, parent_id, before, item, parent) {
		if( parent_id.lastIndexOf(".") > -1 ) {
			alert( portalPage.getMessageResource('ev.error.tree.moveDenied') );
			return false;
		}
		
		if( subKey == 0 ) {	// move
			var msg = portalPage.getMessageResourceByParam('ev.info.tree.node.move', aFileManager.m_tree.getItemText(item_id), aFileManager.m_tree.getItemText(parent_id));
			var ret = confirm( msg );
			if( ret == true ) {
				aFileManager.doMoveParentTree(item_id, parent_id);
				return false;
			}
			else return false;
		}
		else if( subKey == 1 ) { // shiftKey (move order)
			return false;
		}
		else if( subKey == 2 ) { // ctrlKey (copy)
			return false;
		}
		else if( subKey == 3 ) { // altKey
			return false;
		}
		else {
			return true;
		}
	},
	onMoveUp : function () {
		var treeitemId = aFileManager.getContextMenu().getItemId();
		aFileManager.getContextMenu().hide();
		aFileManager.doMoveUpNodeTree( treeitemId );
	},
	onMoveDown : function () {
		var treeitemId = aFileManager.getContextMenu().getItemId();
		aFileManager.getContextMenu().hide();
		aFileManager.doMoveDownNodeTree( treeitemId );
	},
	onNodeSelect : function (id) {
		aFileManager.m_contextMenu.hide();
		aFileManager.doSelectTree(id);
	},
	toggleMaximize : function () {
		var left = document.getElementById("FileManager_Left");
		var title = document.getElementById("FileManager_Left_Title");
		var right = document.getElementById("FileManager_Right");
		if( this.m_maximize == false ) {
			aFileManager.m_oldTabWidth = right.clientWidth;
			aFileManager.m_oldTabHeight = right.clientHeight;
			left.style.width = "0%";
			left.style.display = "none";
			right.style.width = "100%";
			title.style.display = "none";
			this.m_maximize = true;
		}
		else {
			left.style.width = "26%";
			left.style.display = "";
			right.style.width = "74%";
			title.style.display = "";
			this.m_maximize = false;
		}
	},
	doShowContextMenu : function(id, item) {
		var pos = (new enview.util.Utility()).getAbsolutePosition( item );
		this.onNodeSelect( id );
		
		this.m_contextMenu.setItemId( id );
		this.m_contextMenu.show( pos.getX()+45, pos.getY()+20 );
	},
	onSelectPropertyTab : function (tabId) {
		if(tabId == 1) {
			smarteditor = document.getElementById("smarteditor");
			
		 	if( smarteditor) {
		 		if (oEditors.length == 0) {
					nhn.husky.EZCreator.createInIFrame({
						oAppRef: oEditors,
						elPlaceHolder: "FileManagerEditor_content",
						sSkinURI: portalPage.getContextPath() + "/smarteditor/SmartEditor2Skin.html",	
						htParams : {
							bUseToolbar : true,				// 툴바 사용 여부 (true:사용/ false:사용하지 않음)
							bUseVerticalResizer : false,		// 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
							bUseModeChanger : true,			// 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
							//aAdditionalFontList : aAdditionalFontSet,		// 추가 글꼴 목록
							fOnBeforeUnload : function(){
								//alert("완료!");
							}
						}, //boolean
						fOnAppLoad : function(){
							//예제 코드
							//oEditors.getById["ir1"].exec("PASTE_HTML", ["로딩이 완료된 후에 본문에 삽입되는 text입니다."]);
						},
						fCreator: "createSEditor2"
					});
		 		}
		 	} else {
		 		if (this.oFCKeditor == null || typeof(FCKeditorAPI) == 'undefined') {
					oFCKeditor = new FCKeditor( 'FileManagerEditor_content' ) ;
					oFCKeditor.BasePath= portalPage.getContextPath() +  "/fckeditor/";
//					oFCKeditor.BasePath = portalPage.getContextPath() + "/board/fckeditor/"
			        oFCKeditor.ToolbarSet = "Default";
//			        oFCKeditor.EditMode = "FCK_EDITMODE_SOURCE";
			        oFCKeditor.Height = 250;
			        oFCKeditor.ReplaceTextarea();
		 		}
		 	}
		 	setTimeout("aFileManager.openFile();", 200);
		}
		if(tabId == 2) $("#filePreviewIFrame").attr('src', this.m_contextPath + this.m_selectedFilePath + "?dummy=" + Math.random());
	},
	doSelectTree : function (id) {
		var formElem = document.forms[ "FileManager_SearchForm" ];
		formElem.elements[ "pageNo" ].value = "1";
		formElem.elements[ "id" ].value = id;
		this.m_selectRowIndex = 0;
		this.m_uploadDir = id;
		$('#targetImg').attr('src', this.m_contextPath + '/admin/images/blank.gif');
		this.doRetrieve();
		
	},
	doMoveParentTree : function ( item_id, parent_id ) {
		aFileManager.m_contextMenu.hide();
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&id=" + item_id + "&=" + parent_id;
		
		this.m_ajax.send("POST", this.m_contextPath + "/file/changeParentForAjax.admin", param, false, {
			success: function(data){
				if( aFileManager.m_tree.getOpenState(parent_id)==1 || aFileManager.m_tree.hasChildren(parent_id)==0) {
					aFileManager.m_tree.moveItem(item_id, "item_child", parent_id);
					aFileManager.m_tree.openItem( item_id );
				}
				else {
					aFileManager.m_tree.deleteItem(item_id, aFileManager.m_tree.getParentId(item_id));
					aFileManager.m_tree.refreshItem( parent_id );
					aFileManager.onNodeSelect( item_id );
				}
			}
		});
	},
	doMoveUpNodeTree : function ( item_id ) {
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&id=" + item_id + "&toDown=false";
		this.m_ajax.send("POST", this.m_contextPath + "/file/changeOrderForAjax.admin", param, false, { success: function(data){ aFileManager.m_tree.moveItem(item_id, "up_strict"); }});
	},
	doMoveDownNodeTree : function ( item_id ) {
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&id=" + item_id + "&toDown=true";
		this.m_ajax.send("POST", this.m_contextPath + "/file/changeOrderForAjax.admin", param, false, { success: function(data){ aFileManager.m_tree.moveItem(item_id, "down_strict"); }});
	},
	doDeleteTree : function () {
		var item_id = this.m_tree.getSelectedItemId();
		if( item_id == 0 ) {
			alert( portalPage.getMessageResourceByParam('ev.error.tree.node.system.delete') );
			return;
		}
		//alert("id=" + id);
		var msg = portalPage.getMessageResourceByParam('ev.info.tree.node.remove', aFileManager.m_tree.getItemText(item_id));
		var ret = confirm( msg );
		if( ret == false ) {
			return false;
		}
		
		item_id = $("#FileManager_filePath").val();
				
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&pks=" + item_id + "&";
		
		this.m_ajax.send("POST", this.m_contextPath + "/file/removeForAjax.admin", param, false, {
			success: function(data){
				aFileManager.m_contextMenu.hide();
				aFileManager.onRefresh( aFileManager.m_tree.getParentId(item_id) );
				enviewMessageBox.doShow( portalPage.getMessageResource('ev.info.success.remove') );
			}
		});
	},
	initSearch : function () {
		var formElem = document.forms[ "FileManager_SearchForm" ];
		formElem.elements[ "pageNo" ].value = 1;
		//formElem.elements[ "pageSize" ].value = 10;
		this.m_selectRowIndex = 0;
	},
	doRetrieve : function () {
		//this.doCreate();
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&";
		var formElem = document.forms[ "FileManager_SearchForm" ];
	    for (var i=0; i < formElem.elements.length; i++) {
			var tmp = formElem.elements[ i ].value;
			if( tmp.indexOf("*") != 0 ) {
				param += formElem.elements[ i ].name + "=" + encodeURIComponent( tmp ) + "&";
			}
	    }
		this.m_ajax.send("POST", this.m_contextPath + "/file/listForAjax.admin", param, false, {success: function(data) { aFileManager.doRetrieveHandler(data); }});
	},
	doRetrieveHandler : function( resultObject ) {
		this.m_utility.setListData(
			"FileManager",
			new Array('filePath', 'fileName'),
			new Array('fileName', 'fileSize', 'modifiedDate'),
			this.m_contextPath,
			resultObject);
		if(resultObject.Data.length > 0) {
			this.doDefaultSelect(); 
		}
		else {
			this.setTabEnabled(true, false, false);
		}
		/*alert(FileManager_ListMessage);*/
//		var messageElem = document.getElementById("FileManager_ListMessage");
//		var filemessageElem = document.getElementById("FileManager_FilesListMessage");
//		var msg1 = "" +portalPage.getMessageResourceByParam('ev.info.fileresultSize', resultObject.TotalSize) + "";
//		var msg2 = resultObject.FilesSizeMessage;
//		enviewMessageBox.doShow( msg2);
	},
	doRetrieveOpen : function () {
		//this.doCreate();
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&";
		var formElem = document.forms[ "FileManager_SearchForm" ];
	    for (var i=0; i < formElem.elements.length; i++) {
			var tmp = formElem.elements[ i ].value;
			if( tmp.indexOf("*") != 0 ) {
				param += formElem.elements[ i ].name + "=" + encodeURIComponent( tmp ) + "&";
			}
	    }
		this.m_ajax.send("POST", this.m_contextPath + "/file/listForAjax.admin", param, false, {success: function(data) { aFileManager.doRetrieveOpenHandler(data);}});
	},
	
	doPage : function (formName, pageNo) {
		var formElem = document.forms[ formName ];
	    formElem.elements[ "pageNo" ].value = pageNo;
		
		this.m_selectRowIndex = 0;
		this.doRetrieve();
	},
	doSearch : function (formName) {
		var formElem = document.forms[ formName ];
	    formElem.elements[ "pageNo" ].value = 1;
		
		this.m_selectRowIndex = 0;
		this.doRetrieve();
	},
	doSort : function (obj, sortColumn) {
		var formElem = document.forms[ "FileManager_SearchForm" ];
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
	doDefaultSelect : function () {	
		if(!document.getElementById('FileManager[' + this.m_selectRowIndex + '].checkRow')) return;
		document.getElementById('FileManager[' + this.m_selectRowIndex + '].checkRow').checked = true;
		
		var param = "evSecurityCode=" + this.m_evSecurityCode;
		param += "&filePath=" + document.getElementById('FileManager[' + this.m_selectRowIndex + '].filePath').value;
		param += "&fileName=" + document.getElementById('FileManager[' + this.m_selectRowIndex + '].fileName').value;
		this.m_ajax.send("POST", this.m_contextPath + "/file/detailForAjax.admin", param, false, {success: function(data) { aFileManager.doSelectHandler(data); }});
	},
    doSelect : function (obj) {    
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
	    this.m_checkBox.unChkAll( document.getElementById("FileManager_ListForm") );
	    document.getElementById('FileManager[' + rowSeq + '].checkRow').checked = true;
		var param = "evSecurityCode=" + this.m_evSecurityCode;
		param += "&filePath=" + document.getElementById('FileManager[' + rowSeq + '].filePath').value;
		param += "&fileName=" + document.getElementById('FileManager[' + rowSeq + '].fileName').value;
		this.m_ajax.send("POST", this.m_contextPath + "/file/detailForAjax.admin", param, false, {success: function(data) { aFileManager.doSelectHandler(data); }});
	},
	
	setTabEnabled : function(isFolder, isEditable, isPreviewable) {
		var propertyTabs = $("#FileManager_propertyTabs").tabs();
		propertyTabs.tabs('option', 'active', 0);
		if(isFolder){
			//폴더이면 편집/미리보기 탭 비활성화
			propertyTabs.tabs('disable', 1);
			propertyTabs.tabs('disable', 2);
		}
		else {
			//편집 가능/뷸가능 하면 편집탭 활성화/비활성화
			propertyTabs.tabs(isEditable, 1);
			//미리보기 가능/뷸가능 하면 미리보기 탭 활성화/비활성화
			propertyTabs.tabs(isPreviewable, 2);
			
			//if(isEditable == 'enable') aFileManager.openFile();
			if(isPreviewable == 'enable') aFileManager.previewFile();
			
		}
	},
	openFile : function() {
		var param = "fname=" + this.m_selectedFilePath;
		
		if( smarteditor) {
			this.m_ajax.send("POST",this.m_contextPath + "/file/fileOpen.admin", param, true, {success: function(data) {
				oEditors.getById["FileManagerEditor_content"].exec("CHANGE_EDITING_MODE", ['HTMLSrc']);
				oEditors.getById["FileManagerEditor_content"].exec("SET_IR", [data]);
			}});
		} else {
			var FCK_edit = FCKeditorAPI.GetInstance('FileManagerEditor_content');
			FCK_edit.EditMode = 1;
			this.m_ajax.send("POST",this.m_contextPath + "/file/fileOpen.admin", param, true, {success: function(data) {
				FCK_edit.SetHTML(data);
			}});
		}
	},
	
	previewFile : function() {
		$("#filePreviewIFrame").attr('src', this.m_contextPath + this.m_selectedFilePath);
	},
	
	changeContents : function() {
		var edit_cntt = "";
		if( smarteditor) {
			edit_cntt = oEditors.getById["FileManagerEditor_content"].getIR();
		} else {
			var FCK_edit = FCKeditorAPI.GetInstance('FileManagerEditor_content');
			edit_cntt = FCK_edit.GetXHTML( true );
		}
		
		if(this.m_selectedFilePath != null) {
			  var param = "filePath=" + this.m_selectedFilePath +"&contents=" + encodeURIComponent(edit_cntt);
			  this.m_ajax.send("POST",this.m_contextPath + "/file/fileSave.admin",param,true,{success: function(data) {aFileManager.changeContentsHandler(data);}});
		} else {
			alert(portalPage.getMessageResource('ev.error.file.savefail'));
		}
	},
	
	changeContentsHandler : function(resultObject){
		enviewMessageBox.doShow( portalPage.getMessageResource('ev.info.file.save') );
	},
	
	doSelectHandler : function(resultObject) {
		this.m_utility.setFormDataFromXML(this.m_dataStructure, "FileManager", resultObject);
		var isEnabled = portalPage.getAjax().retrieveElementValue("editable", resultObject);
		var isFolder = portalPage.getAjax().retrieveElementValue("isFolder", resultObject);
		var isPreviewable = portalPage.getAjax().retrieveElementValue("isPreviewable", resultObject);
		var fileName = portalPage.getAjax().retrieveElementValue("fileName", resultObject);
		var filePath = portalPage.getAjax().retrieveElementValue("filePath", resultObject);
		this.m_selectedFilePath = filePath;
		document.getElementById("FileManager_modifiedDate").readOnly = true;
		document.getElementById("FileManager_EditFormPanel").style.display = '';
		if(isFolder != 'true' && (isEnabled == 'disable' && isPreviewable == 'disable')){
			$('#targetImg').attr('src', this.m_contextPath + this.m_selectedFilePath);
		}else{
			$('#targetImg').attr('src', this.m_contextPath + '/admin/images/blank.gif');
		}
		this.setTabEnabled(isFolder, isEnabled, isPreviewable);
	},
	
	doCreate : function() 
	{
		this.m_utility.initFormData(this.m_dataStructure, "FileManager");
		
		var propertyTabs = $("#FileManager_propertyTabs").tabs();
		propertyTabs.tabs('option', 'active', 0);
	
		document.getElementById("FileManager_filePath").readOnly = false;
		document.getElementById("FileManager_EditFormPanel").style.display = '';
	},
	doUpdate : function (forDetail)
	{
		var elm = null;
		var val = null;
		var param = "";
		var isCreate = document.getElementById("FileManager_isCreate").value;
		var form = document.getElementById("FileManager_EditForm");

		var isValid = this.m_validator.validate(this.m_dataStructure, form);
		if( isValid == false ) return;
		
		//var param = this.m_utility.getFormData(this.m_dataStructure, form);
		param = "evSecurityCode=" + this.m_evSecurityCode + "&";
		param += "filePath=" + $("#FileManager_filePath").val() + "&" + "fileName=" + $("#FileManager_fileName").val(); 
		
		this.m_ajax.send("POST", this.m_contextPath + "/file/updateForAjax.admin", param, false, {
			success: function(data) {
				//aFileManager.onRefresh( document.getElementById("FileManager_parentId").value );
				aFileManager.doRetrieve();
				enviewMessageBox.doShow( portalPage.getMessageResource('ev.info.success.update') );
			}
		});
		
/*		if( isCreate == "true" ) {
			this.m_ajax.send("POST", this.m_contextPath + "/file/addForAjax.admin", param, false, {
				success: function(data){
					aFileManager.onRefresh( document.getElementById("FileManager_parentId").value );
					
					aFileManager.doRetrieve();
					
					enviewMessageBox.doShow( portalPage.getMessageResource('ev.info.success.add') );
				}});
		}
		else {
			this.m_ajax.send("POST", this.m_contextPath + "/file/updateForAjax.admin", param, false, {
				success: function(data){
					aFileManager.onRefresh( document.getElementById("FileManager_parentId").value );
					
					aFileManager.doRetrieve();
					
					enviewMessageBox.doShow( portalPage.getMessageResource('ev.info.success.update') );
				}});
		}*/
	},
	doRemove : function () {
		var rowCnts = this.m_checkBox.getCheckedElements( document.getElementById("FileManager_ListForm") );
		if( rowCnts == "" ) return;

		var ret = confirm( portalPage.getMessageResource('ev.info.remove') );
	    if( ret == true ) {
	    	var formData = "";
	    	var rowCntArray = rowCnts.split(",");
			
	    	var param = "evSecurityCode=" + this.m_evSecurityCode;
	    	for(i=0; i<rowCntArray.length; i++) {
	    		var filePath = document.getElementById('FileManager[' + rowCntArray[i] + '].filePath').value;
	    		if(filePath == "/") filePath = "";
	    		var fileName = document.getElementById('FileManager[' + rowCntArray[i] + '].fileName').value;
				param += "&pks=" + filePath + "/" + fileName;
	        }
		    
			this.m_ajax.send("POST", this.m_contextPath + "/file/removeForAjax.admin", param, false, {
				success: function(data){
					//aFileManager.onRefresh( parentId );
					
					aFileManager.m_selectRowIndex = 0;
					aFileManager.doRetrieve();
					enviewMessageBox.doShow( portalPage.getMessageResource('ev.info.success.remove') );
				}
			});
		}
	}
}

FileMenu = function()
{
	this.m_domElement = document.createElement('div');
	this.m_domElement.id = "File_Menu";
	this.m_domElement.title = "Category Menu";
	var htmlStr = "";
	htmlStr += "<a onclick='aFileManager.onRefresh()' style='border:none; cursor:hand;'><img src='" + portalPage.getContextPath() + "/admin/images/Service.gif'>" + portalPage.getMessageResource('ev.info.menu.refresh') + "</a>";
	htmlStr += "<hr>";
	htmlStr += "<a onclick='aFileManager.onCreate()' style='border:none; cursor:hand;'><img src='" + portalPage.getContextPath() + "/admin/images/ic_make_page.gif'>" + portalPage.getMessageResource('ev.info.menu.addPage') + "</a>";
	htmlStr += "<hr>";
	htmlStr += "<a onclick='aFileManager.onMoveUp()' style='border:none; cursor:hand;'><img src='" + portalPage.getContextPath() + "/admin/images/ic_up.gif'>" + portalPage.getMessageResource('ev.info.menu.moveUp') + "</a><br>";
	htmlStr += "<a onclick='aFileManager.onMoveDown()' style='border:none; cursor:hand;'><img src='" + portalPage.getContextPath() + "/admin/images/ic_down.gif'>" + portalPage.getMessageResource('ev.info.menu.moveDown') + "</a>";
	htmlStr += "<hr>";
	/*htmlStr += "<a onclick='aFileManager.onDelete()' style='border:none; cursor:hand;'><img src='" + portalPage.getContextPath() + "/admin/images/ic_del_b.gif'>" + portalPage.getMessageResource('ev.info.menu.delete') + "</a>";*/
	htmlStr += "<a onclick='aFileManager.getFileUploader().doShow()' style='border:none; cursor:hand;'><img src='" + portalPage.getContextPath() + "/admin/images/createsmall.gif'>" + portalPage.getMessageResource('ev.info.menu.upload') + "</a>";
	this.m_domElement.innerHTML = htmlStr;

	//document.body.appendChild( this.m_domElement );
		
	$("#File_Menu").dialog({
		autoOpen: false,
		width: "140px",
		resizable: false,
		modal: true,
		open: function(event, ui) { 
			$(".ui-dialog-titlebar-close").hide(); 
		}
	});
	
	this.init();
}

FileMenu.prototype =
{
	m_domElement : null,
	m_id : null,
	
	init : function() {
	
	},
	getItemId : function ()
	{
		return this.m_id;
	},
	setItemId : function (id)
	{
		this.m_id = id;
	},
	show : function (left, top)
	{
		$('#File_Menu').dialog( "option", "position", [left,top] );
		$('#File_Menu').dialog('open');
	},
	hide : function()
	{
		$('#File_Menu').dialog('close');
	}
}

var aFileUploader = null;
FileUploader = function(evSecurityCode, selectedFilePath)
{
	this.m_ajax = new enview.util.Ajax();
	this.m_checkBox = new enview.util.CheckBoxUtil();
	if( portalPage == null) portalPage = new enview.portal.Page();
	this.m_selectRowIndex = 0;
	this.m_evSecurityCode = evSecurityCode;
	this.m_utility = new enview.util.Utility();
	this.m_contextPath = portalPage.getContextPath();
	this.m_domElement = document.getElementById("FileManager_FileUploader");
	this.m_uploadDir = selectedFilePath;
	
	enviewMessageBox = new enview.portal.MessageBox(200, 100, 1000); 
	
	$("#FileManager_FileUploader").dialog({
		autoOpen: false,
		resizable: true,
		width:600, 
		height:'auto',
		modal: false,
		buttons: {
/*			Cancel: function() {
				$(this).dialog('close');
			},
			Apply: function() {
				var test = document.getElementById('IframeFileUpload').contentWindow.document.getElementById("send")
				    alert(test.value);
			}*/
		},
		beforeClose: function( event, ui ) {
			if (aFileUploader.m_vault != null) {
				aFileUploader.m_vault.unload();
				aFileUploader.m_vault = null;
			}
		}
	});
	
	this.init();
}

FileUploader.prototype =
{
    m_ajax : null,
	m_domElement : null,
	m_tree : null,
	m_checkBox : null,
	m_sourceElement : null,
	m_callback : null,
	m_evSecurityCode : null,
	m_uploadDir : '/',
	m_vault : null,
	
	init : function() {
		//this.getFlashController();
		//this.initMultiFileDhtmlx();
		
	},
	
	setUploadDir : function(uploadDir){
		this.m_uploadDir = uploadDir;
		this.getFlashController().setUploadDir(uploadDir);
	},
	
	getUploadDir : function(){
		/*if($('.selectedTreeRow').first().text() != "/") this.m_uploadDir = "/" + $('.selectedTreeRow').first().text() + "/";
		else this.m_uploadDir = "/";*/
		return this.m_uploadDir;
	},
	
	initMultiFileSWF : function(sessionId, swfUrl){
		aFlashController.makeSwfMultiUpload(
				movie_id='smu03', //파일폼 고유ID
				flash_width='300', //파일폼 너비 (기본값 400, 권장최소 300)
				list_rows='3', // 파일목록 행 (기본값:3)
				limit_size='30', // 업로드 제한용량 (기본값 10)
				file_type_name='모든파일', // 파일선택창 파일형식명 (예: 그림파일, 엑셀파일, 모든파일 등)
				allow_filetype='*.*', // 파일선택창 파일형식 (예: *.jpg *.jpeg *.gif *.png)
				deny_filetype='*.cgi *.pl', // 업로드 불가형식 
				upload_exe='/file/uploadProgress.admin', // 업로드 담당프로그램
				browser_id=sessionId,
				url=swfUrl
			);
	},
	
	initMultiFileDhtmlx : function () {
		if (this.m_vault == null) {
			this.m_vault = new dhtmlXVaultObject({
			    container:  "uploadFlash",             // html container for vault
			    uploadUrl:  "/file/uploadProgress.admin",           // html4/html5 upload url
			    autoStart : false,
			    buttonUpload : false,
			    buttonClear : true,
			    paramName : "Filedata"
			});
			
			this.m_vault.attachEvent("onBeforeFileAdd", function (file) {
				console.log("before add");
				return true;
			});
			
			this.m_vault.attachEvent("onFileAdd", function (file) {
				console.log("add");
			});
			
			this.m_vault.attachEvent("onBeforeFileRemove", function (file) {
				return true;
			});
			
			this.m_vault.attachEvent("onUploadComplete", function (files) {
				var uploadOption = $(':radio[name="uploadOption"]:checked').val();
				var param = "dirPath=" +  aFileUploader.m_uploadDir;
				param+= "&uploadOption=" + uploadOption;
				
				aFileUploader.m_ajax.send("POST", aFlashController.m_contextPath + "/file/uploadLastSave.admin", param, false, {
					success: function(data) {
						$("#FileManager_FileUploader").dialog("close");
						enviewMessageBox.doShow( portalPage.getMessageResource('ev.info.file.successupload') );
						aFileManager.doRetrieve();
					}
				});
			});
			
			this.m_vault.setStrings({
				done:       "업로드됨",     // text under filename in files list
			    error:      "오류발생",    // text under filename in files list
			    btnAdd:     "파일추가",   // button "add files"
			    btnUpload:  "파일업로드",   // button "upload"
			    btnCancel:  "취소",   // button "cancel uploading"
			    btnClean:   "초기화",    // button "clear all"
			 
			    dnd:        "여기에 파일을 놓으세요."   // dnd text while the user is dragging files
			});
			
			this.m_vault.setHeight(200);
		}
	},
	
	doShow : function (callback) {
		this.callback = callback;
		var param = 'uploadDir=' + this.m_uploadDir;
		param += '&dummy=' + Math.random();
		this.m_ajax.send("POST", this.m_contextPath + "/file/uploadListForAjax.admin", param, false, {
			success: function(data){
				document.getElementById("FileManager_FileUploader").innerHTML = data;
				//aFileUploader.initMultiFileSWF($('#seesionId').val(), $('#swfUrl').val());
				$('#FileManager_FileUploader').dialog('open');
				aFileUploader.initMultiFileDhtmlx();
			}});
		
	},
	doApply : function(id) {
		//$('#FileManager_FileUploader').dialog('close');
     /* var uploader = document.getElementById("IframeFileUpload");
		uploader.checkForm();*/ 
		this.m_vault.upload();
	},
	
	getFlashController : function () {
		if( aFlashController == null ) {
			aFlashController = new FlashController();
		}
		return aFlashController;
	}
}

var aFlashController = null;
FlashController = function()
{
	this.m_ajax = new enview.util.Ajax();	
	this.m_contextPath = portalPage.getContextPath();
}

FlashController.prototype = {
	
	m_uploadDir : '/',
	
	setUploadDir : function(uploadDir){
		this.m_uploadDir = uploadDir;
	},
		 
	getCookie : function(name){ 
		alert("getCookie");
		
		var Found = false;
		var start, end;
		var i = 0;

		while(i <= document.cookie.length) { 
			start = i;
			end = start + name.length;

			if(document.cookie.substring(start, end) == name) { 
				Found = true;
				break;
			} 
			i++;
		} 
		 
		if(Found == true) { 
			start = end + 1;
			end = document.cookie.indexOf(";", start); 
			if(end < start) end = document.cookie.length;
			return document.cookie.substring(start, end);
		} 
		return "";
	},

	makeSwfMultiUpload : function(movie_id,flash_width,list_rows,limit_size,file_type_name,allow_filetype,deny_filetype,upload_exe,browser_id,url){
		var flashvars = "flash_width="+flash_width+"&amp;";
		flashvars += "list_rows="+list_rows+"&amp;";
		flashvars += "limit_size="+limit_size+"&amp;";
		flashvars += "file_type_name="+file_type_name+"&amp;";
		flashvars += "allow_filetype="+allow_filetype+"&amp;";
		flashvars += "deny_filetype="+deny_filetype+"&amp;";
		flashvars += "upload_exe="+upload_exe+"&amp;";
		flashvars += "upload_id="+movie_id+"&amp;";
		flashvars += "browser_id="+browser_id+"&amp;"; 
		flashvars += "flashController=aFlashController";
		
		var flashStr = "<object classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000'";
		flashStr += "codebase='http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0'";
		flashStr += "width='"+flash_width+"' height='"+parseInt(list_rows*20+25,10)+"' align='middle' id='"+movie_id+"' method='multi_upload'>";
		flashStr += "<param name='allowScriptAccess' value='sameDomain' />";
		flashStr += "<param name='movie' value='" + url + "/multi_upload.swf' />";
		flashStr += "<param name='quality' value='high' />";
		flashStr += "<param name='bgcolor' value='#ffffff' />";
		flashStr += "<param name='flashvars' value='"+flashvars+"' />";
		flashStr += "<param name='upJSP' value='" + aFileManager.m_contextPath + "/file/uploadProgress.admin' />";
		flashStr += "<embed src='" + url + "/multi_upload.swf' width='"+flash_width+"' height='"+parseInt(list_rows*20+25,10)+"' quality='high'";
		flashStr += "bgcolor='#ffffff' name='"+movie_id+"' align='middle' allowScriptAccess='sameDomain' type='application/x-shockwave-flash'";
		flashStr += "pluginspage='http://www.macromedia.com/go/getflashplayer' flashvars='"+flashvars+"' />";
		flashStr += "</object>";
		
//		document.write(flashStr);
		$('#uploadFlash').html(flashStr);
	},

	callSwfUpload : function(){
		arrMovie = new Array();
		var arr_num = 0;
		var objectTags = document.getElementsByTagName('object');
		var movie;
		for (i = 0; i < objectTags.length; i++ ){		
			if(objectTags[i].getAttribute("method")=="single_upload" || objectTags[i].getAttribute("method")=="multi_upload"){
				if(document.getElementsByName(objectTags[i].getAttribute("id"))[0]) {
					movie = document.getElementsByName(objectTags[i].getAttribute("id"))[0];
				
				}else{
					movie = document.getElementById(objectTags[i].getAttribute("id"));
				}			
				if(movie.GetVariable("totalSize")>0){				
					arrMovie[arr_num] = movie;
					arr_num++;
				}
			}		
		}

		if(arrMovie.length>0){
			/*alert("폴더명 : "  + filename);*/
			if(arrMovie[0].getAttribute("method")=="single_upload" || arrMovie[0].parentNode.getAttribute("method")=="single_upload") arrMovie[0].height = 70;
			if(arrMovie[0].getAttribute("method")=="multi_upload" || arrMovie[0].parentNode.getAttribute("method")=="multi_upload") arrMovie[0].height = parseInt(20*arrMovie[0].GetVariable("listRows")+25+45,10);
			arrMovie[0].SetVariable( "startUpload", "" );
			arr_mov = 0;
		} else {
			//document.forms['formName'].submit();
		}
	},
	
	callUploadclose : function()
	{
		$("#FileManager_FileUploader").dialog("close");
	},
	
	swfUploadComplete : function()
	{
		//document.forms['formName'].submit();
		var uploadOption = $(':radio[name="uploadOption"]:checked').val();
		var param = "dirPath=" +  this.m_uploadDir;
		param+= "&uploadOption=" + uploadOption;
		
	    this.m_ajax.send("POST", aFlashController.m_contextPath + "/file/uploadLastSave.admin", param, false, {
		success: function(data){
			 $("#FileManager_FileUploader").dialog("close");
        	 enviewMessageBox.doShow( portalPage.getMessageResource('ev.info.file.successupload') );
        	 aFileManager.doRetrieve();
		}});
		
		arr_mov++;
		if(arrMovie.length>arr_mov){
			if(arrMovie[arr_mov].getAttribute("method")=="single_upload" || arrMovie[arr_mov].parentNode.getAttribute("method")=="single_upload") arrMovie[arr_mov].height = 70;
			if(arrMovie[arr_mov].getAttribute("method")=="multi_upload" || arrMovie[arr_mov].parentNode.getAttribute("method")=="multi_upload") arrMovie[arr_mov].height = parseInt(20*arrMovie[arr_mov].GetVariable("listRows")+25+45,10);
			arrMovie[arr_mov].SetVariable( "startUpload", "" );

		}else{

		}	
	},

	fileTypeError : function( notAllowFileType ){ //허용하지 않은 형식의 파일일 경우 에러 메시지 출력
		alert("확장자가 " + notAllowFileType + "인 파일들은 업로드 할 수 없습니다.");
	},

	overSize : function( limitSize ){ //허용하지 않은 형식의 파일일 경우 에러 메시지 출력
		alert("선택한 파일의 크기가 " + limitSize + "를 초과했습니다.");
	},

	versionError : function(){ //플래쉬 버전이 8 미만일 경우 에러 메시지 출력
		alert("플래쉬 버전이 8.0 이상인지 확인하세요.\n이미 설치하신 경우는 브라우저를 전부 닫고 다시 시작하세요.");
	},

	httpError : function(){ //http 에러 발생시 출력 메시지
		alert("네트워크 에러가 발생하였습니다. 관리자에게 문의하세요.");
	},

	ioError : function(){ //파일 입출력 에러 발생시 출력 메시지
		alert("입출력 에러가 발생하였습니다.\n 다른 프로그램에서 이 파일을 사용중인지 확인하세요.");
	},

	onSecurityError : function(){ //파일 입출력 에러 발생시 출력 메시지
		alert("보안 에러가 발생하였습니다. 관리자에게 문의하세요.");
	}
}
