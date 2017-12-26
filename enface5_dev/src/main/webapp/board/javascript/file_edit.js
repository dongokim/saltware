<!--
if( ! window.enboard )
    window.enboard = new Object();
if( ! ebUtil )
	var ebUtil = new enboard.util();

enboard.edit = function() {
	this.boardId     = '';
	this.limitCount  = 0;
	this.badNick     = '';
	this.badCntt     = '';
	this.maxPoint    = 0;
	this.cmd         = '';
	this.totFileSize = 0;
	this.sizeSF      = '';
	this.skin        = 'default';
	this.limitSize   = 0;
	this.thumbSize   = 0;

	this.isCancel    = true;
	this.fileList    = '';
	this.totDelFileList = '';
	this.ie          = true;

	this.ebSessionKeepTimeoutID = "";
	this.ebUploadStatusTimeoutID = "";
	this.ebUploadStatusSemaphore = "";
}
enboard.edit.prototype = {

	boardId     : null,
	boardRid    : null,
	limitCount  : null,
	badNick     : null,
	badCntt     : null,
	maxPoint    : null,
	cmd         : null,
	totFileSize : null,
	sizeSF      : null,
	skin        : null,
	limitSize   : null,
	thumbSize   : null,

	isCancel    : null,
	fileList    : null,
	totDelFileList : null, // 업로드된 파일 목록에서 빼온 삭제한다고 선택된 파일 목록 
	ie          : null,
	WinPreview  : null, // 미리보기 플래그

	ebSessionKeepTimeoutID : null, // Session 유지를 위한 타임아웃ID
	
	accSetMngr  : null,
	vaultUploader : null,			// DHTMLX Vault 업로더
	extraUploader : null,			// 기타 업로더 제품용
	currentUploadFileCnt : 0,
	isNewFile : false,
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	// 1. 편집화면 window.onload시 호출된다.
	///////////////////////////////////////////////////////////////////////////////////////////////////
	edit_init : function() {
		ebEdit.boardId     = document.setTransfer.boardId.value;
		ebEdit.boardRid    = document.setTransfer.boardRid.value;
		ebEdit.limitCount  = document.setTransfer.maxFileCnt.value;
		ebEdit.badNick     = document.setTransfer.badNickDflt.value;
		ebEdit.badCntt     = document.setTransfer.badCnttDflt.value;
		ebEdit.maxPoint    = document.setTransfer.mileTot.value;
		ebEdit.cmd         = document.setTransfer.cmd.value;
		ebEdit.totFileSize = document.setTransfer.totFileSize.value;
		ebEdit.sizeSF      = document.setTransfer.sizeSF.value;
		ebEdit.skin        = document.setTransfer.boardSkinDflt.value;
		ebEdit.limitSize   = document.setTransfer.limitSize.value;
		ebEdit.thumbSize   = document.setTransfer.thumbSize.value;

		ebEdit.ebSessionKeepTimeoutID = setTimeout("ebEdit.ebSessionKeepDelay()", 200000);

		ebEdit.ie = eval('document.setTransfer.ie.value');
		enLangKnd = eval('document.setTransfer.langKnd.value');
		ebEdit.vaultUploader = document.getElementById("vault_upload");
		
		// 업로드 모듈 및 파일 리스트 초기화
		if ( ebEdit.vaultUploader && ebEdit.limitCount > 0) {
			ebEdit.vaultUploader = new dhtmlXVaultObject({
			    container:  "vault_upload",             // html container for vault
			    uploadUrl:  "/dhtmlx/vault/handler/file_handler.jsp?boardId=" + ebEdit.boardId + "&subId=sub01&dir="+document.setTransfer.mode.value,           // html4/html5 upload url
			    autoStart : false,
			    buttonUpload : false,
			    buttonClear : true
			});
			ebEdit.vaultUploader.setHeight(200);
			//ebEdit.vaultUploader.setFilesLimit(ebEdit.limitCount);
			ebEdit.vaultUploader.attachEvent("onBeforeFileAdd", function (file) {
				if (file.size > (ebEdit.vaultUploader.getMaxFileSize() / ebEdit.limitCount)) {
					alert( ebUtil.getMessage('eb.info.upload.limit.size', (ebEdit.vaultUploader.getMaxFileSize() / ebEdit.limitCount) ));
					return false; 
				}
				
				if (ebEdit.limitCount <= ebEdit.currentUploadFileCnt) {
					alert( ebUtil.getMessage('eb.info.upload.limit.cnt', ebEdit.limitCount ));
					return false; 
				}
				ebEdit.currentUploadFileCnt++;
				ebEdit.isNewFile = true;
			    return true;
			});
			
			ebEdit.vaultUploader.attachEvent("onBeforeFileRemove", function (file) {
				// 파일 삭제 호출시 여기서 false 리턴시 멈춤
				// 초기화 기능 호출시 이 이벤트가 각각 파일별로 탐.
				// 업로드 되지 않은 파일은 그냥 넘김
				
				if (file.uploaded == true) {
					if (!confirm(ebUtil.getMessage('eb.info.confirm.del'))) {
						return false;
					}
					
					var curDelFileList = file.serverName + "-" + file.size + "|";
					var unDelFileList = "";
					
					var uploadedFileList = ebEdit.vaultUploader.getData();
					
					$(uploadedFileList).each(function (i) {
						if (this.serverName != file.serverName) {
							unDelFileList += this.serverName + "-" + this.size + "|";
						}
					});
					
					ebEdit.totDelFileList += curDelFileList;
					document.setFileList.vaccum.value   = 'DIRECT';
					document.setFileList.delList.value   = ebEdit.totDelFileList;
					document.setFileList.unDelList.value = unDelFileList;				
					document.setFileList.submit();
				}
				ebEdit.currentUploadFileCnt--;
				return true;
			});
			
			ebEdit.vaultUploader.setStrings({
				done:       "업로드됨",     // text under filename in files list
			    error:      "오류발생",    // text under filename in files list
			    btnAdd:     "파일추가",   // button "add files"
			    btnUpload:  "파일업로드",   // button "upload"
			    btnCancel:  "취소",   // button "cancel uploading"
			    btnClean:   "초기화",    // button "clear all"
			 
			    dnd:        "여기에 파일을 놓으세요."   // dnd text while the user is dragging files
			});
			
			// 수정화면에서 파일 목록이 있을경우
			if ($("#vault_fileList > li").size() > 0) {
				$("#vault_fileList > li").each(function (i) {
					var fileInfo = {
						name : $(this).attr("data-name"),
						serverName : $(this).attr("data-mask"),
						size : $(this).attr("data-size")
					};
					ebEdit.vaultUploader.addFileRecord(fileInfo, "uploaded");
				});
			}
			
		}
		if( document.setFileList != null && document.setFileList.list == null) {
			$("#uploadFileList").bind("mousedown", function (e) {
			      e.metaKey = true;    
			 }).selectable();
		}		
		
		var divs = document.getElementsByTagName("div");
		for (var i=0; i<divs.length; i++) {
		}	

		if (document.setTransfer.accSetYn.value == 'Y') {
			if (document.editForm.ableListGrade) ebUtil.setSelectedValues (document.editForm.ableListGrade, document.setTransfer.ableListGrade.value);
			if (document.editForm.ableListGroup) ebUtil.setSelectedValues (document.editForm.ableListGroup, document.setTransfer.ableListGroup.value);
			if (document.editForm.ableListRole ) ebUtil.setSelectedValues (document.editForm.ableListRole,  document.setTransfer.ableListRole.value );
			if (document.editForm.ableReadGrade) ebUtil.setSelectedValues (document.editForm.ableReadGrade, document.setTransfer.ableReadGrade.value);
			if (document.editForm.ableReadGroup) ebUtil.setSelectedValues (document.editForm.ableReadGroup, document.setTransfer.ableReadGroup.value);
			if (document.editForm.ableReadRole ) ebUtil.setSelectedValues (document.editForm.ableReadRole,  document.setTransfer.ableReadRole.value );
		}
	},
	ebSessionKeepDelay : function () {
		// 5분에 한번씩 서버로 요청을 보내서, session-timeout이 짧게 잡혀있는 서버환경에서
		// 게시물을 오래 편집하여도 세션이 끊어지지 않도록 조치하여준다.
		// 2010.03.26.KWShin.
		ebUtil.loadXMLDoc("TEXT", "GET", ebUtil.getContextPath()+"/board/resource/session_keeping.jsp");
		//alert("KEEPING!!");
		clearTimeout( ebEdit.ebSessionKeepTimeoutID );
		ebEdit.ebSesssionKeepTimoutID = setTimeout("ebEdit.ebSessionKeepDelay()", 180000);
	},
	edit_destroy : function() {
		
		var unDelFileList = '';
		// 취소시
		if (ebEdit.isCancel && ebEdit.limitCount > 0) {
			if( document.setFileList.list != null) {
				// list객체가 있으면 select를 사용하는 옛날 방식으로 사용한다.
				for( var i = 1; i < document.setFileList.list.options.length; i++ ) {
					if( document.setFileList.list.options[i].value.length > 0 ) {
						if( ebEdit.cmd == 'MODIFY' ) {
							if (ebEdit.fileList.indexOf( document.setFileList.list.options[i].value) == -1)
								unDelFileList += document.setFileList.list.options[i].value + '|';
						} else
							unDelFileList += document.setFileList.list.options[i].value + '|';
					}
				}
			} else {
				// jquery-ui selectable을 사용한다.
				$("#uploadFileList .ui-selected").each( function(){
					var data =  $(this).attr('data');
					if( ebEdit.cmd == 'MODIFY' ) {
						if (ebEdit.fileList.indexOf( data) == -1)
							unDelFileList += document.setFileList.list.options[i].value + '|';
					} else
						unDelFileList += document.setFileList.list.options[i].value + '|';
				});
			}

			document.setFileList.delList.value = unDelFileList;

		//저장시
		} else if (!ebEdit.isCancel && ebEdit.limitCount > 0) {
			document.setFileList.delList.value = ebEdit.totDelFileList;
		}
		
		if (document.setFileList.delList.value.length > 0) {
			document.setFileList.vaccum.value = 'INDIRECT';
			// 삭제화면을 보여주려면 다음 두 라인을 살린다.
			//document.setFileList.target = 'popup';
			//window.open('about:blank', 'popup', 'width=200, height=50');
			document.setFileList.submit();
		}
	},
	mobile_edit_destroy : function() {},
	///////////////////////////////////////////////////////////////////////////////////////////////////
	// 업로드 제한 파일 갯수를 체크하고, Progress bar 를 보이도록 한 뒤, 
	// setUpload 폼을 서브밋한다.
	///////////////////////////////////////////////////////////////////////////////////////////////////
	uploadFile : function() {
		if( ebEdit.limitCount > 0 ) {
			var uploadCount = 0;
			
			if( document.setFileList.list != null) {
				// list객체가 있으면 select를 사용하는 옛날 방식으로 사용한다.
				for (var i = 1; i < document.setFileList.list.length; i++) {
					if (document.setFileList.list.options[i].value.length > 0) uploadCount++;
				}
			} else {
				// jquery-ui selectable 사용
				uploadCount = $("#uploadFileList li").length;
				
			}

			if( ebEdit.limitCount <= uploadCount ) {
				alert( ebUtil.getMessage('eb.info.upload.limit.cnt', ebEdit.limitCount ));
				return;
			}

			if(!ebUtil.chkValue( document.setUpload.filename, ebUtil.getMessage('eb.info.ttl.o.file'), 'true')) return;

			document.getElementById('uploading').style.display = 'inline';
			this.ebUploadStatusSemaphore = "ON";
			this.displayUploadStatus();
			document.setUpload.action = "fileMngr?cmd=upload&boardId="+document.setUpload.boardId.value;
			document.setUpload.submit();
		}
	},
	
	handleUpload : function ( text, val) {
		if( document.setFileList.list != null) {
			// list객체가 있으면 select를 사용하는 옛날 방식으로 사용한다.
			var listIndex = -1;
			for (k = 0; k < document.setFileList.list.options.length; k++) {
				if (document.setFileList.list.options[k].value == "") {
					listIndex = k;	
					break;
				}
			}
			if( listIndex != -1) {
				document.setFileList.list.options[listIndex].text = text;
				document.setFileList.list.options[listIndex].value = val;
			} else {
				document.setFileList.list.options[ document.setFileList.list.options.length] = new Option( text, val);
			}
		} else {
			// list객체가 없으면 jquery-ui의 selectable을 사용한다.
			$("#uploadFileList").append( "<li data='" + val + "'>" + text + "</li>" );
			$("#uploadFileList").bind("mousedown", function (e) {
			      e.metaKey = true;    
			 }).selectable();
		}
	},
	
	handleDelete : function ( deletedList) {
		if( document.setFileList.list != null) {
			// list객체가 있으면 select를 사용하는 옛날 방식으로 사용한다.
			for (var i = document.setFileList.list.length - 1; i >= 0; i--) {
				for (var j = 0; j < deletedList.length; j++) {
					if (document.setFileList.list.options[i].value == deletedList[j]) {
						document.setFileList.list.options[i] = null;
						break;
					}
				}
			}			
		} else {
			// list객체가 없으면 jquery-ui selectable을 사용한다.
			$("#uploadFileList li").each( function(){
				var data =  $(this).attr('data');
				for( var i=0; i < deletedList.length;i++) {
					if( data==deletedList[i]) {
						$(this).remove();
					}
				}
			});
		}
	},
	
	closeUpload : function() {
		document.getElementById('uploading').style.display = 'none';
		this.ebUploadStatusSemaphore = "OFF";
		clearTimeout (this.ebUploadStatusTimeoutID);
	},
	displayUploadStatus : function () {
		// 1초에 한번씩 서버로 요청을 보내서, FileUpload 서블릿이 세션에 매달아주는 파일의 업로드 현황을
		// 읽어와서 화면에 보여준다.
		// 2012.06.21.KWShin.
		clearTimeout (this.ebUploadStatusTimeoutID);
		var uploadStatus = ebUtil.loadXMLDoc("TEXT", "GET", ebUtil.getContextPath()+"/board/resource/getUploadStatus.jsp");
		document.getElementById("uploadStatus").innerHTML = uploadStatus;
		if (this.ebUploadStatusSemaphore == "ON") {
			this.ebUploadStatusTimeoutID = setTimeout("ebEdit.displayUploadStatus()", 500);
		}
	},
	///////////////////////////////////////////////////////////////////////////////////////////////////
	// 현재 선택된 파일을 전체 선택된 파일 목록에 추가하고, 
	// 전체 선택된 파일 목록과 선택되지 않은 파일 목록을 서브밋.
	///////////////////////////////////////////////////////////////////////////////////////////////////
	deleteFile : function() {
		var isSelected = false;
		var curDelFileList = '';
		var unDelFileList = '';
		if( document.setFileList.list != null) {
			// list객체가 있으면 select를 사용하는 옛날 방식으로 사용한다.
			for (var i = 1; i < document.setFileList.list.length; i++) {
				if (document.setFileList.list.options[i].value.length > 0 &&
					document.setFileList.list.options[i].selected) {
					if( ebEdit.fileList.indexOf( document.setFileList.list.options[i].value ) > -1 )
						// 방금 선택된 파일들을 전체 삭제 파일 리스트에 추가.
						curDelFileList += document.setFileList.list.options[i].value + '|';
					else
						unDelFileList += document.setFileList.list.options[i].value + '|';
					isSelected = true;
				}
			}
		} else {
			// list객체가 없으면 query-ui selectable 사용
			$("#uploadFileList .ui-selected").each( function(){
				var data =  $(this).attr('data');
				if( ebEdit.fileList.indexOf( data) > -1 )
					// 방금 선택된 파일들을 전체 삭제 파일 리스트에 추가.
					curDelFileList += data + '|';
				else
					unDelFileList += data + '|';
				isSelected = true;
			});
		}

		
		if (isSelected) {
			if (confirm(ebUtil.getMessage('eb.info.confirm.del'))) {
				ebEdit.totDelFileList += curDelFileList;
				document.setFileList.vaccum.value   = 'DIRECT';
				document.setFileList.delList.value   = ebEdit.totDelFileList;
				document.setFileList.unDelList.value = unDelFileList;				
				document.setFileList.submit();
			}
		} else
			//alert('삭제하고자 하는 파일을 선택하세요');
			alert(ebUtil.getMessage('eb.info.select.file.delete'));
	},
	///////////////////////////////////////////////////////////////////////////////////////////////////
	// '목록보기' 버튼을 눌렀을 때.
	///////////////////////////////////////////////////////////////////////////////////////////////////
	list : function()  {
		document.setTransfer.method = "post";
		document.setTransfer.action = "list.brd";
		document.setTransfer.submit();
	},
	///////////////////////////////////////////////////////////////////////////////////////////////////
	// '저장' 버튼을 눌렀을 때.
	///////////////////////////////////////////////////////////////////////////////////////////////////
	save : function (rtnURI) {
		if ( ebEdit.vaultUploader ) {
			ebEdit.vaultUploader.attachEvent("onUploadComplete", function (files) {
				// 파라미터로 받는 files에는 원파일명이 없다.(undefined)
				// getData로 받는 데이터에는 있음.(업로드 된 파일리스트)
				document.setTransfer.fileName.value = "";
				document.setTransfer.fileMask.value = "";
				document.setTransfer.fileSize.value = "";

				var fileList = ebEdit.vaultUploader.getData();
				if (files.length > 0) {
					for (var i = 0 ; i < fileList.length ; i++) {
						document.setTransfer.fileName.value += fileList[i].name + "|";
						document.setTransfer.fileMask.value += fileList[i].serverName + "|";
						document.setTransfer.fileSize.value += fileList[i].size + "|";
					}
				}
				ebEdit.isNewFile = false;
				ebEdit.isCancel = false;
				if (rtnURI != null) document.setTransfer.rtnURI.value = rtnURI;
				document.setTransfer.method = "post";
				document.setTransfer.action = "/user/fileSave.face";
				document.setTransfer.submit();
			});
			if (ebEdit.isNewFile) {
				ebEdit.vaultUploader.upload();
			} else {
				document.setTransfer.fileName.value = "";
				document.setTransfer.fileMask.value = "";
				document.setTransfer.fileSize.value = "";

				var fileList = ebEdit.vaultUploader.getData();
				if (fileList.length > 0) {
					for (var i = 0 ; i < fileList.length ; i++) {
						document.setTransfer.fileName.value += fileList[i].name + "|";
						document.setTransfer.fileMask.value += fileList[i].serverName + "|";
						document.setTransfer.fileSize.value += fileList[i].size + "|";
					}
				}
				ebEdit.isCancel = false;
				if (rtnURI != null) document.setTransfer.rtnURI.value = rtnURI;
				document.setTransfer.method = "post";
				document.setTransfer.action = "k";
				document.setTransfer.submit();
			}
			
		}
	},
	limitUpload : function() {
		if( ebEdit.limitCount > 0 ) {
			var uploadCount = 0;
			for(var i=1; i < document.setFileList.list.length; i++) {
				if( document.setFileList.list.options[i].value.length > 0 ) uploadCount++;
			}
			if( ebEdit.limitCount <= uploadCount ) {
				alert( ebUtil.getMessage('eb.info.upload.limit.cnt', ebEdit.limitCount));
				return false;
			}
		}
		return true;
	},
	rebuildFile : function (fileName, fileMask, fileSize) {
		if( this.vaultUploader != null ) {
			var fileInfo = {
					name : fileName,
					serverName : fileMask,
					size : fileSize
				};
				this.vaultUploader.addFileRecord(fileInfo, "uploaded");
				return;
		}
		var totalSize = eval("document.setUpload.totalsize.value");
		totalSize = eval(totalSize) + eval(fileSize);
	  
		var unit = "";
		var calsize = "";
			if ((1024 < totalSize) && (totalSize < 1024 * 1024)) {
			unit = " KB";
				calsize = (totalSize/1024)+"";
		} else if (1024 * 1024 <= totalSize) {
			unit = " MB";
			calsize = (totalSize/(1024*1024))+"";
		} else {
			unit = " Bytes";
				calsize = totalSize+"";
		}
	  
		if (calsize.indexOf(".") > -1) {
			var sosu = calsize.substring(calsize.indexOf("."), calsize.length);
	  
			if (sosu.length > 4)
				calsize = calsize.substring(0,calsize.indexOf("."))+sosu.substring(0,4);
		}
	  
		document.setUpload.totalsize.value = totalSize;
		document.setUpload.viewsize.value = "TOTAL FILE SIZE : "+calsize+unit;
		
		var fsize = eval(fileSize); var funit = "";
		if ((fsize > 1024) && (fsize < 1024 * 1024)) {
			funit = " KB";
			fsize = fsize/1024 + "";
		} else if (fsize >= 1024 * 1024) {
			funit = " MB";
			fsize = fsize/(1024*1024) + "";
		} else {
			funit = " Bytes";
			fsize = fsize + "";
		}
		if (fsize.indexOf(".") > -1) {
			var fsosu = fsize.substring(fsize.indexOf("."), fsize.length);
			if (fsosu.length > 4)
				fsize = fsize.substring(0,fsize.indexOf("."))+fsosu.substring(0,4);
		}

		// 파일명 뒤에 파일 사이즈를 같이 보여준다.
		var text = fileName + " (" + fsize + funit + ")";
		var val = fileMask+'-'+fileSize;
		this.handleUpload( text, val);
	},
}
var ebEdit = new enboard.edit();
var ebLangKnd = "ko";
//-->
