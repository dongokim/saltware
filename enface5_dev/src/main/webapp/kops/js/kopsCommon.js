/*파일 ID가져오기*/
function cfnMultiFileUploadFormAtchFileId(divId){
	var atchFileIdArea = $("#"+divId).find("[id='atchFileId']");
	var atchFileId ="";
	
	$.each(atchFileIdArea,function(key,html){
		if(atchFileId!=""){
			return;
		}		
		atchFileId = $(html).val();
	});
	
	return atchFileId;
}
/**멀티 파일 상세 가져오기
 * div영역안에 file id영역을 찾아 뿌려준다.
 * id = fileDetail_1 : fileDetail(필수)_(순번)
 * ex)<div id="fileArea"><p id="fileDetail_1"></p></div>
 * cfnMultiFileDetailForm("파일ID","영역")
 * */

function cfnMultiFileDetailForm(atchFileId, divId){
	var url = '/cmn/selectFileList.do?atchFileId='+atchFileId;
	if(atchFileId != undefined || atchFileId != ""){
		$.ajax({
	        url : url,
	        type : 'post',
	        async : false,
	        data : '',
	        success: function(fileData, textStatus, xhr)
			{
	        	var fileList = JSON.parse(fileData)["fileList"];
	        	
				$.each(fileList ,function(key,val){
					var fileSn = val.fileSn;
					var orignlFileNm = val.orignlFileNm;
					var $fileSpanHtml = $('<span></span>');
						$fileSpanHtml.append(orignlFileNm+"&nbsp;&nbsp;");
					
					var $fileHtml = $('<button type="button" class="btn btn-default btn-xs"></button>');
					var $fileSpan = $('<span class="glyphicon glyphicon-save"></span>');
					
					var onclickAt = "cfnFileDownload('"+atchFileId+"','"+fileSn+"')";
					$fileHtml.attr("onclick",onclickAt);
					$fileHtml.after("<span>"+orignlFileNm+"</span>");
					$fileSpan.html("파일다운");
					$fileHtml.append($fileSpan);
					
					$fileSpanHtml.append($fileHtml);
					$("#"+divId).find("[id=fileDetail_"+fileSn+"]").children().remove();
					$("#"+divId).find("[id=fileDetail_"+fileSn+"]").append($fileSpanHtml);
				});	        	
	        },
	        error: function(xhr, textStatus, errorThrown)
	        {
	        	//alert(JSON.parse(xhr.responseText)['error']);
	        	console.log(JSON.parse(xhr.responseText)['error']);
	        }
	    });		
	}
}

//파일 업로드 폼2
function cfnMultiFileUploadForm(atchFileId, fileInfo, targetUrl, divId)
{
	var table = $("<table border='1'></table>");//.addClass('foo');
	$('#' + divId).append(table);
	var fileSns = fileInfo.keys();
	$(fileSns).each(function(idx, fileSn)
	{
		var row = $("<tr></tr>");//.addClass('bar').text('result ' + i);
		table.append(row);
		var col1 = $("<td>" + fileInfo.get(fileSn) + "</td>");
	    row.append(col1);
	    var col2 = $("<td id='file_" + fileSn + "'></td>");
	    row.append(col2);

	    cfnFileUploadForm(atchFileId, targetUrl, fileSn, fileSn);
	});
}

// 파일 업로드
function cfnFileUpload(formData, targetUrl, divId, fileSn)
{
	$.ajax({
        url: targetUrl,
        type: 'POST',
        data: formData,
        cache: false,
        processData: false, // Don't process the files
        contentType: false, // Set content type to false as jQuery will tell the server its a query string request
        success : function(atchFileId)
		{
        	if(fileSn != undefined) cfnFileUploadForm(atchFileId, targetUrl, fileSn, fileSn);
        	else cfnFileUploadForm(atchFileId, targetUrl, divId);
        },
        error: function(xhr, textStatus, errorThrown)
        {
        	//alert(JSON.parse(xhr.responseText)['error']);
        	console.log(JSON.parse(xhr.responseText)['error']);
        }
    });
}

// 파일다운로드
function cfnFileDownload(atchFileId, fileSn , divId)
{
	location.href = '/cmn/fileDownload.do?atchFileId=' + atchFileId + '&fileSn=' + fileSn;
}

// 파일삭제
function cfnFileDelete(atchFileId, fileSn, targetUrl, divId)
{
	$.ajax({
        url : '/cmn/deleteFile.do?atchFileId=' + atchFileId + '&fileSn=' + fileSn,
        type : 'post',
        async : false,
        data : '',
        success: function(data, textStatus, xhr)
		{
        	cfnFileUploadForm(atchFileId, targetUrl, fileSn, divId);
        },
        error: function(xhr, textStatus, errorThrown)
        {
        	//alert(JSON.parse(xhr.responseText)['error']);
        	console.log(JSON.parse(xhr.responseText)['error']);
        }
    });
}

/*
 * 길이만큼 0으로 채워주는 함수
 */
function cfnPrependZero(num, len) {
    while(num.toString().length < len) {
        num = "0" + num;
    }
    return num;
}
