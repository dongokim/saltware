var Map = function (obj)
{
	var mapData = (obj != null) ? cloneObject(obj) : new Object();
	this.put = function(key, value) {
		mapData[key] = value;
	};
	this.get = function(key) {
		return mapData[key];
	};
	this.remove = function(key) {
		for (var tKey in mapData) {
			if (tKey == key) {
				delete mapData[tKey];
				break;
			}
		}
	};
	this.keys = function() {
		var keys = [];
		for (var key in mapData) {
			keys.push(key);
		}
		return keys;
	};
	this.values = function() {
		var values = [];
		for (var key in mapData) {
			values.push(mapData[key]);
		}
		return values;
	};
	this.containsKey = function(key) {
		for (var tKey in mapData) {
			if (tKey == key) {
				return true;
			}
		}
		return false;
	};
	this.isEmpty = function() {
		return (this.size() == 0);
	};
	this.clear = function() {
		for (var key in mapData) {
			delete mapData[key];
		}
	};
	this.size = function() {
		var size = 0;
		for (var key in mapData) {
			size++;
		}
		return size;
	};
	this.getObject = function() {
		return cloneObject(mapData);
	};
	var cloneObject = function(obj) {
		var cloneObj = {};
		for (var attrName in obj) {
			cloneObj[attrName] = obj[attrName];
		}
		return cloneObj;
	};
};

//$.jMaskGlobals.watchDataMask = true;

// 날짜선택 설정
var datepickerConfig = {
	locale: 'ko',
	format: 'YYYY-MM-DD',
//	showTodayButton: true,
//	showClear: true,
//	showClose: true
	ignoreReadonly: true,
	useCurrent: false,
	allowInputToggle: true
};

// 시간선택 설정
var timepickerConfig = {
	locale: 'ko',
	format: 'HH:mm',
	ignoreReadonly: true,
	useCurrent: false,
	allowInputToggle: true
};

/*
 * 그리드 데이타 Validation 체크 함수
 */
function fnValidCheck(gridName, columns)
{
	var rst = true;
	//var ids = $('#' + gridName).getDataIDs();
	var columnNames = $('#' + gridName).getGridParam('colNames');
	$('#' + gridName + ' tr').each(function(idx, obj)
	{
		if(idx > 0)
		{
			if($(obj).find('td')[0].innerText == '입력' || $(obj).find('td')[0].innerText == '수정')
			{
				$($(obj).find('td')).each(function(idx1, obj1)
				{
					for(var i = 0; i < columns.length; i++)
					{
						if(columnNames[idx1] == columns[i])
						{
							//console.log(ids[j] + ', ' + $(obj1).text());
							if(($(obj1).text() + '') == ' ')
							{
								alert(columns[i] + '를 입력하여 주세요');
								$('#' + gridName + ' tr').eq(idx).trigger('click');
								//$('#grid1').editRow('16081001', true);
								rst = false;
								return false;
							}
						}
					};
				});
			}
		}
	});

	return rst;
}

/*
 * 그리드 데이타 Validation 체크 함수 - 지원 : 전담인력 신청, 신고 전용
 */
function fnValidCheck2(gridName, columns){
	var rst = true;
	var columnNames = $('#' + gridName).getGridParam('colNames');
	
	$('#' + gridName + ' tr').each(function(idx, obj){
		if(idx > 0){
			if($(obj).find('td')[0].innerText != '삭제'){
				$($(obj).find('td')).each(function(idx1, obj1){
					for(var i = 0; i < columns.length; i++){
						if(columnNames[idx1] == columns[i]){
							
							//재직인 경우, 종료일은 필수입력 사항이 아니다.
							//반대로 재직이 아니면 종료일은 핑수입력 사항이다.
							if(columnNames[idx1] == '종료일'){	
								$($(obj).find('input')).each(function(idx2, obj2){
									if($(this).is(":checked")){
										if(($(obj1).text() + '') != ' '){
											alert('재직일 경우 종료일은 없습니다.');
											$('#' + gridName + ' tr').eq(idx).trigger('click');
											rst = false;
											return false;
										}
									}else{
										if(($(obj1).text() + '') == ' '){
											alert(columns[i] + '를 입력하여 주세요.');
											$('#' + gridName + ' tr').eq(idx).trigger('click');
											rst = false;
											return false;
										}
									}
								});
								
							}else{
								if(($(obj1).text() + '') == ' '){
									alert(columns[i] + '를 입력하여 주세요.');
									$('#' + gridName + ' tr').eq(idx).trigger('click');
									rst = false;
									return false;
								}
							}
							
						}
					};
				});
			}
		}
	});
	return rst;
}

/*
 * 그리드 데이타 변경 감시
 */
function checkOnChange(gridName)
{
	if($('#' + gridName).getCell($('#' + gridName).getGridParam('selrow'), 'rowStatus') != 'I' && $('#' + gridName).getCell($('#' + gridName).getGridParam('selrow'), 'rowStatus') != 'D')
	$('#' + gridName).setCell($('#' + gridName).getGridParam('selrow'), 'rowStatus', 'U');
}

/*
 * 동적 콤보박스 생성
 */
function cfnComboBox(codeId, name, div, option)
{
	$.ajax({
        url: '/ccd/selectCmmnCodeDList.do?codeId=' + codeId,
        type : 'post',
        async : false,
        data : '',
        success: function(data)
		{
		    var combo = $("<select class='form-control input-sm'></select>").attr("id", name).attr("name", name);
		    if(option != undefined)
		    {
		    	combo.append("<option value=''>" + option + "</option>");
		    }
		    $.each((JSON.parse(data))['cmmnCodeDList'], function (i, row)
		    {
		        combo.append("<option value='" + row['code'] + "'>" + row['codeNm'] + "</option>");
		    });

		    $('#' + div).append(combo);
        },
        error: function(xhr, textStatus, errorThrown)
        {
        	alert(xhr.responseText);
        }
    });
}


/*
 * 동적 콤보박스 생성(분류체계코드)
 */
function cfnClSystemCodeComboBox(codeId, name, div, prpos, sysNm, option)
{
	var url = '';
	if (sysNm == 'rlm') {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=rlm&codeId=' + codeId+'&prpos='+prpos;
	} else {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=sps&codeId=' + codeId+'&prpos='+prpos;
	}
	$.ajax({
		url: url,
		type : 'post',
		async : false,
		data : '',
		success: function(data)
		{
			var combo = $("<select></select>").attr("id", name).attr("name", name);
			if(option != undefined)
			{
				combo.append("<option value=''>" + option + "</option>");
			}
			$.each((JSON.parse(data))['clSystemCodeDList'], function (i, row)
			{
				combo.append("<option value='" + row['code'] + "'>" + row['codeNm'] + "</option>");
			});

			$('#' + div).append(combo);
//			var inputTy = row['inputTy'];
//			if (inputTy == 'N') {
//				$("label[for="+row['code']+"]").after('<input name=\"'+row['code']+'\" type="text" value="">');
//			}
		},
		error: function(xhr, textStatus, errorThrown)
		{
			alert(xhr.responseText);
		}
	});
}

/*
 * 동적 라디오박스 생성
 */
function cfnRadioBox(codeId, name, div, option)
{
	$.ajax({
        url: '/ccd/selectCmmnCodeDList.do?codeId=' + codeId,
        type : 'post',
        async : false,
        data : '',
        success: function(data)
		{
		    if(option != undefined)
		    {
		    	var combo = '<label class="radio-inline"><input type="radio" name="' + name + '" value=""> ' + option + '</label> ';
		    	//var combo = $("<input type='radio'/>").attr("id", name).attr("name", name).attr('value', '');
		    	$('#' + div).append(combo);
		    	//$('#' + div).append(' ' + option);
		    }

		    $.each((JSON.parse(data))['cmmnCodeDList'], function (i, row)
		    {
		    	var combo = '<label class="radio-inline"><input type="radio" name="' + name + '" value="' + row['code'] + '"> ' + row['codeNm'] + '</label> ';
		    	//var combo = $("<input type='radio'/>").attr("id", name).attr("name", name).attr('value', row['code']);
		    	$('#' + div).append(combo);
		    	//$('#' + div).append(' ' + row['codeNm']);
		    });
        },
        error: function(xhr, textStatus, errorThrown)
        {
        	alert(xhr.responseText);
        }
    });
}

/*
 * 동적 라디오박스 생성(분류체계코드)
 */
function cfnClSystemCodeRadioBox(codeId, div, prpos, sysNm)
{
	var url = '';
	if (sysNm == 'rlm') {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=rlm&codeId=' + codeId+'&prpos='+prpos;
	} else {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=sps&codeId=' + codeId+'&prpos='+prpos;
	}
	$.ajax({
		url: url,
		type : 'post',
		async : false,
		data : '',
		success: function(data)
		{
//			if(option != undefined)
//			{
//				var combo = $("<input type='radio'/>").attr("id", name).attr("name", name).attr('value', '');
//				$('#' + div).append(combo);
//				$('#' + div).append(' ' + option);
//			}

			var inputTyDiv = "Y";
			var textName = "";
			$.each((JSON.parse(data))['clSystemCodeDList'], function (i, row)
					{
				var name = row['code'];
				var combo = $("<input type='radio'/>").attr("id", row['code']).attr("name", row['codeId']).attr('value', row['code']).attr("title", row['upperClNm']);
				$('#' + div).append(combo);
				$('#' + div).append('<label for=\"'+row['code']+'\">'+row['codeNm']+"</label>&nbsp;");
				var inputTy = row['inputTy'];
				if (inputTy == 'N') {
					inputTyDiv = 'N';
					textName = row['code'];
					$("label[for="+row['code']+"]").after('<input name=\"theOthers'+row['code']+'\" type="text" value="" disabled="disabled" maxlength="100">');

				}
					});
			if (inputTyDiv == 'N') {
				$("input[name="+$("#"+textName).attr("name")+"]").change(
						function (){
							if ($("#"+textName).is(':checked')) {
								$("input[name=theOthers"+textName+"]").removeAttr("disabled");
							}else{
								$("input[name=theOthers"+textName+"]").val("");
								$("input[name=theOthers"+textName+"]").attr("disabled", "disabled");
							}
						}
				);
			}
		},
//		sb.append("<label for=\""+name+"_"+index+"\">"+cd_nm+"</label>&nbsp;");
		error: function(xhr, textStatus, errorThrown)
		{
			alert(xhr.responseText);
		}
	});
}

/*
 * 동적 라디오박스 생성(분류체계코드)
 */
function cfnClSystemCodeRadioBox22(codeId, div, prpos, sysNm)
{
	var url = '';
	if (sysNm == 'rlm') {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=rlm&codeId=' + codeId+'&prpos='+prpos;
	} else {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=sps&codeId=' + codeId+'&prpos='+prpos;
	}
	$.ajax({
		url: url,
		type : 'post',
		async : false,
		data : '',
		success: function(data)
		{
//			if(option != undefined)
//			{
//				var combo = $("<input type='radio'/>").attr("id", name).attr("name", name).attr('value', '');
//				$('#' + div).append(combo);
//				$('#' + div).append(' ' + option);
//			}

//			<li><label for="where_1"><input type="radio" title="" name="where_1" id="where_1"/><span>서울</span></label></li>
//			<input type="text" readonly="readonly" class='text readonly_input ad_class' name="" id=""  style="width:650px" />
			var inputTyDiv = "Y";
			var textName = "";
			$.each((JSON.parse(data))['clSystemCodeDList'], function (i, row)
					{
				var name = row['code'];
				var inputTy = row['inputTy'];
				if (inputTy == 'N') {

					$('#' + div).append('<li class="w_100"><label for="'+row['code']+'" class="read_only"><input type="radio" title="'+row['upperClNm']+'" name="'+row['codeId']+'" id="'+row['code']+'" value="'+row['code']+'"/><span>'+row['codeNm']+'</span><input type="text" class="text readonly_input ad_class" name="theOthers'+row['code']+'" disabled="disabled" style="width:650px" maxlength="100"/></label></li>');
					inputTyDiv = 'N';
					textName = row['code'];

//					$("label[for="+row['code']+"]").after('<input type="text" class="text readonly_input ad_class" name="theOthers'+row['code']+'" disabled="disabled" style="width:650px" />');

				}else{
					$('#' + div).append('<li><label for="'+row['code']+'"><input type="radio" title="'+row['upperClNm']+'" name="'+row['codeId']+'" id="'+row['code']+'" value="'+row['code']+'"/><span>'+row['codeNm']+'</span></label></li>');
				}
					});
			if (inputTyDiv == 'N') {
				$("input[name="+$("#"+textName).attr("name")+"]").change(
						function (){

							if ($("#"+textName).is(':checked')) {
								$("input[name=theOthers"+textName+"]").removeAttr("disabled");

							}else{
								$("input[name=theOthers"+textName+"]").val("");
								$("input[name=theOthers"+textName+"]").attr("disabled", "disabled");

							}
						}
				);
			}
		},
//		sb.append("<label for=\""+name+"_"+index+"\">"+cd_nm+"</label>&nbsp;");
		error: function(xhr, textStatus, errorThrown)
		{
			alert(xhr.responseText);
		}
	});
}

/*
 * 동적 라디오박스 생성(분류체계코드) 내부 관리자
 */
function cfnClSystemCodeRadioBoxManager(codeId, div, prpos, sysNm)
{
	var url = '';
	if (sysNm == 'rlm') {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=rlm&codeId=' + codeId+'&prpos='+prpos;
	} else {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=sps&codeId=' + codeId+'&prpos='+prpos;
	}
	$.ajax({
		url: url,
		type : 'post',
		async : false,
		data : '',
		success: function(data)
		{
//			if(option != undefined)
//			{
//				var combo = $("<input type='radio'/>").attr("id", name).attr("name", name).attr('value', '');
//				$('#' + div).append(combo);
//				$('#' + div).append(' ' + option);
//			}

//			<li><label for="where_1"><input type="radio" title="" name="where_1" id="where_1"/><span>서울</span></label></li>
//			<input type="text" readonly="readonly" class='text readonly_input ad_class' name="" id=""  style="width:650px" />
			var inputTyDiv = "Y";
			var textName = "";
			$.each((JSON.parse(data))['clSystemCodeDList'], function (i, row)
					{
				var name = row['code'];
				var inputTy = row['inputTy'];
				if (inputTy == 'N') {
//					$('#' + div).append('<li class="w_100"><label for="'+row['code']+'" class="read_only"><input type="radio" title="'+row['upperClNm']+'" name="'+row['codeId']+'" id="'+row['code']+'" value="'+row['code']+'"/><span>'+row['codeNm']+'</span><input type="text" class="text readonly_input ad_class" name="theOthers'+row['code']+'" disabled="disabled" style="width:650px" maxlength="300"/></label></li>');
					$('#' + div).append('<li><input type="radio" name="'+row['codeId']+'" id="'+row['code']+'" title="'+row['upperClNm']+'" value="'+row['code']+'"><label for="'+row['code']+'">'+row['codeNm']+'</label><input type="text" name="theOthers'+row['code']+'" class="form-control input-sm" title="Text Input" disabled="disabled" maxlength="100"></li>');
					inputTyDiv = 'N';
					textName = row['code'];


				}else{
					$('#' + div).append('<li><input type="radio" name="'+row['codeId']+'" id="'+row['code']+'" title="'+row['upperClNm']+'" value="'+row['code']+'"><label for="'+row['code']+'">'+row['codeNm']+'</label></li>');
				}
					});
			if (inputTyDiv == 'N') {
				$("input[name="+$("#"+textName).attr("name")+"]").change(
						function (){

							if ($("#"+textName).is(':checked')) {
								$("input[name=theOthers"+textName+"]").removeAttr("disabled");

							}else{
								$("input[name=theOthers"+textName+"]").val("");
								$("input[name=theOthers"+textName+"]").attr("disabled", "disabled");

							}
						}
				);
			}
		},
//		sb.append("<label for=\""+name+"_"+index+"\">"+cd_nm+"</label>&nbsp;");
		error: function(xhr, textStatus, errorThrown)
		{
			alert(xhr.responseText);
		}
	});
}

/*
 * 동적 체크박스 생성
 */
function cfnCheckBox(codeId, name, div, option)
{
	$.ajax({
        url: '/ccd/selectCmmnCodeDList.do?codeId=' + codeId,
        type : 'post',
        async : false,
        data : '',
        success: function(data)
		{
		    if(option != undefined)
		    {
		    	var combo = '<label class="checkbox-inline"><input type="checkbox" name="' + name + '" value=""> ' + option + '</label> ';
		    	//var combo = $("<input type='checkbox'/>").attr("id", name).attr("name", name).attr('value', '');
		    	$('#' + div).append(combo);
		    	//$('#' + div).append(' ' + option);
		    }

		    $.each((JSON.parse(data))['cmmnCodeDList'], function (i, row)
		    {
		    	var combo = '<label class="checkbox-inline"><input type="checkbox" name="' + name + '" value="' + row['code'] + '"> ' + row['codeNm'] + '</label> ';
		    	//var combo = $("<input type='checkbox'/>").attr("name", name).attr('value', row['code']);
		    	$('#' + div).append(combo);
		    	//$('#' + div).append(' ' + row['codeNm']);
		    });
        },
        error: function(xhr, textStatus, errorThrown)
        {
        	alert(xhr.responseText);
        }
    });
}

/*
 * 동적 체크박스 생성(분류체계코드)
 */
function cfnClSystemCodeCheckBox(codeId, div, prpos, sysNm)
{
	var url = '';
	if (sysNm == 'rlm') {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=rlm&codeId=' + codeId + '&prpos='+prpos;
	} else {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=sps&codeId=' + codeId + '&prpos='+prpos;
	}
	$.ajax({
		url: url,
		type : 'post',
		async : false,
		data : '',
		success: function(data)
		{
//			if(option != undefined)
//			{
//				var combo = $("<input type='checkbox'/>").attr("name", name).attr('value', '');
//				$('#' + div).append(combo);
//				$('#' + div).append(' ' + option);
//			}

			$.each((JSON.parse(data))['clSystemCodeDList'], function (i, row)
					{
				var name = row['code'];
				var combo = $("<input type='checkbox'/>").attr("id", row['code']).attr("name", row['codeId']).attr('value', row['code']).attr("title", row['upperClNm']);
				$('#' + div).append(combo);
				$('#' + div).append('<label for=\"'+row['code']+'\">'+row['codeNm']+"</label>&nbsp;");
//				$('#' + div).append(' ' + row['codeNm']);
				var inputTy = row['inputTy'];
				if (inputTy == 'N') {
					$("label[for="+row['code']+"]").after('<input name=\"theOthers'+row['code']+'\" type="text" value="" disabled="disabled" maxlength="100">');
					$("input[name="+$("#"+name).attr("name")+"]").change(
							function (){
								if ($("#"+name).is(':checked')) {
									$("input[name=theOthers"+name+"]").removeAttr("disabled");
								}else{
									$("input[name=theOthers"+name+"]").val("");
									$("input[name=theOthers"+name+"]").attr("disabled", "disabled");
								}
							}
					);
				}

			});
		},
		error: function(xhr, textStatus, errorThrown)
		{
			alert(xhr.responseText);
		}
	});
}

/*
 * 동적 체크박스 생성(분류체계코드)
 */
function cfnClSystemCodeCheckBox22(codeId, div, prpos, sysNm)
{
	var url = '';
	if (sysNm == 'rlm') {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=rlm&codeId=' + codeId+'&prpos='+prpos;
	} else {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=sps&codeId=' + codeId+'&prpos='+prpos;
	}
	$.ajax({
		url: url,
		type : 'post',
		async : false,
		data : '',
		success: function(data)
		{
//			if(option != undefined)
//			{
//				var combo = $("<input type='radio'/>").attr("id", name).attr("name", name).attr('value', '');
//				$('#' + div).append(combo);
//				$('#' + div).append(' ' + option);
//			}

//			<li class="w_100"><label for="employe_28" class="read_only"><input type="checkbox" title="" name="employe_28" id="employe_28"/><span>기타 </span><input type="text" readonly="readonly" class='text readonly_input ad_class margin_l_10' name="" id=""  style="width:650px" /></label></li>


			var inputTyDiv = "Y";
			var textName = "";
			$.each((JSON.parse(data))['clSystemCodeDList'], function (i, row)
					{
				var name = row['code'];
				var inputTy = row['inputTy'];
				if (inputTy == 'N') {

					$('#' + div).append('<li class="w_100"><label for="'+row['code']+'" class="read_only"><input type="checkbox" title="'+row['upperClNm']+'" name="'+row['codeId']+'" id="'+row['code']+'" value="'+row['code']+'"/><span>'+row['codeNm']+'</span><input type="text" disabled="disabled" class="text ad_class margin_l_10" name="theOthers'+row['code']+'" style="width:650px" maxlength="100"/></label></li>');
					inputTyDiv = 'N';
					textName = row['code'];

//					$("label[for="+row['code']+"]").after('<input type="text" readonly="readonly" class="text readonly_input ad_class" name="theOthers'+row['code']+'" disabled="disabled" />');

				}else{
					$('#' + div).append('<li><label for="'+row['code']+'"><input type="checkbox" title="'+row['upperClNm']+'" name="'+row['codeId']+'" id="'+row['code']+'" value="'+row['code']+'"/><span>'+row['codeNm']+'</span></label></li>');
				}
					});

			//조치시간은 따로
			$("#0000011135").parent().parent().remove();

			if (inputTyDiv == 'N') {
				$("input[name="+$("#"+textName).attr("name")+"]").change(
						function (){

							if ($("#"+textName).is(':checked')) {
								$("input[name=theOthers"+textName+"]").removeAttr("disabled");

							}else{
								$("input[name=theOthers"+textName+"]").val("");
								$("input[name=theOthers"+textName+"]").attr("disabled", "disabled");

							}
						}
				);
			}
		},
//		sb.append("<label for=\""+name+"_"+index+"\">"+cd_nm+"</label>&nbsp;");
		error: function(xhr, textStatus, errorThrown)
		{
			alert(xhr.responseText);
		}
	});
}
/*
 * 동적 체크박스 생성(분류체계코드) 관리자
 */
function cfnClSystemCodeCheckBoxManager(codeId, div, prpos, sysNm)
{
	var url = '';
	if (sysNm == 'rlm') {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=rlm&codeId=' + codeId+'&prpos='+prpos;
	} else {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=sps&codeId=' + codeId+'&prpos='+prpos;
	}
	$.ajax({
		url: url,
		type : 'post',
		async : false,
		data : '',
		success: function(data)
		{
//			if(option != undefined)
//			{
//				var combo = $("<input type='radio'/>").attr("id", name).attr("name", name).attr('value', '');
//				$('#' + div).append(combo);
//				$('#' + div).append(' ' + option);
//			}

//			<li class="w_100"><label for="employe_28" class="read_only"><input type="checkbox" title="" name="employe_28" id="employe_28"/><span>기타 </span><input type="text" readonly="readonly" class='text readonly_input ad_class margin_l_10' name="" id=""  style="width:650px" /></label></li>


			var inputTyDiv = "Y";
			var textName = "";
			$.each((JSON.parse(data))['clSystemCodeDList'], function (i, row)
					{
				var name = row['code'];
				var inputTy = row['inputTy'];
				if (inputTy == 'N') {

					$('#' + div).append('<li><input type="checkbox" title="'+row['upperClNm']+'" name="'+row['codeId']+'" id="'+row['code']+'" value="'+row['code']+'"><label for="'+row['code']+'" class="checkbox-inline">'+row['codeNm']+'</label><input type="text" name="theOthers'+row['code']+'" class="form-control input-sm" title="Text Input" disabled="disabled" maxlength="100"></li>');
					inputTyDiv = 'N';
					textName = row['code'];
//					$("label[for="+row['code']+"]").after('<input type="text" readonly="readonly" class="text readonly_input ad_class" name="theOthers'+row['code']+'" disabled="disabled" />');
				}else{
					$('#' + div).append('<li><input type="checkbox" title="'+row['upperClNm']+'" name="'+row['codeId']+'" id="'+row['code']+'" value="'+row['code']+'"><label for="'+row['code']+'" class="checkbox-inline">'+row['codeNm']+'</label></li>');
				}
					});

			//조치시간은 따로
			$("#0000011135").parent().remove();

			if (inputTyDiv == 'N') {
				$("input[name="+$("#"+textName).attr("name")+"]").change(
						function (){

							if ($("#"+textName).is(':checked')) {
								$("input[name=theOthers"+textName+"]").removeAttr("disabled");

							}else{
								$("input[name=theOthers"+textName+"]").val("");
								$("input[name=theOthers"+textName+"]").attr("disabled", "disabled");

							}
						}
				);
			}
		},
//		sb.append("<label for=\""+name+"_"+index+"\">"+cd_nm+"</label>&nbsp;");
		error: function(xhr, textStatus, errorThrown)
		{
			alert(xhr.responseText);
		}
	});
}

// 파일 업로드 폼1
function cfnFileUploadForm(atchFileId, targetUrl, divId, fileSn)
{	
	$.support.cors = true;
	$.ajax({					
		type : 'POST',
		url  : 'http://${pageContext.request.serverName}:90/cmn/fileUploadForm.doatchFileId=' + atchFileId + '&targetUrl=' + targetUrl + '&divId=' + divId,
		data : '',
		contentType : 'text/plain; charset=utf-8',
		dataType : 'jsonp',
		withCredentials: true,
		jsonp : "callback",
		success: function(data, textStatus, xhr)
		{
        	if(fileSn != undefined) $('#file_' + fileSn).html(data);
        	else $('#' + divId).html(data);
        },
        error: function(xhr, textStatus, errorThrown)
        {
        	alert(xhr);
        	alert(textStatus);
        	alert(errorThrown);
        }
	});

	
}
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
	var url = 'http://dev.kops.or.kr:90/cmn/selectFileList.do?atchFileId='+atchFileId;
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
	location.href = 'http://dev.kops.or.kr:90/cmn/fileDownload.do?atchFileId=' + atchFileId + '&fileSn=' + fileSn;
}

// 파일삭제
function cfnFileDelete(atchFileId, fileSn, targetUrl, divId)
{
	$.ajax({
        url : 'http://dev.kops.or.kr:90/cmn/deleteFile.do?atchFileId=' + atchFileId + '&fileSn=' + fileSn,
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

/*
 * cfnDoughnutCrt
 * dutNm : 데이터 이름 , dutSu : 데이터 값
 */
function cfnDoughnutCrt(id, data, colval, crtnm) {

	var arrLabel = new Array();
	var arrValue = new Array();
	var arrColor = new Array();

	$(data).each(function(idx, obj) {
		arrLabel.push(obj['dutLbl'] + '');
		arrValue.push(obj['dutVal'] + '');
	});

	$(colval).each(function(idx, obj) {
		arrColor.push(obj);
	});

	var config = {
		type : 'doughnut',
		data : {
			datasets : [ {
				data : arrValue,
				backgroundColor : arrColor,
				label : 'Dataset 1'
			} ],
			labels : arrLabel

		},
		options : {
			responsive : true,
			legend : {
				position : 'top',
			},
			title : {
				display : true,
				text : crtnm
			},
			animation : {
				animateScale : true,
				animateRotate : true
			}
		}
	};

	var ctx = document.getElementById(id).getContext("2d");
	window.myDoughnut = new Chart(ctx, config);

}

/*
 * BubbleChart : (id:위치할 자리, data:x,y좌표와 r반지름, color)
 * x좌표 : bubX, y좌표 : bubY, 반지름 : bubR, label이름 : bubLbl
 */
function cfnBubbleCrt(id, data, colval, crtnm) {

	var arrdata = new Array();
	var arrlabel = new Array();
	var arrxyr = [];
	var arrdatasets = [];

	$(data).each(function(idx, obj) {

		var color = Chart.helpers.color;

		arrxyr = [];
		arrlabel = [];

		$(data[idx]).each(function(idx, obj) {
			arrxyr.push({
				x : obj['bubX'],
				y : obj['bubY'],
				r : obj['bubR']
			});
			arrlabel.push(obj['bubLbl']);
		});

		var a = color(colval[idx]).alpha(0.5).rgbString();

		arrdata.push(arrxyr);

		arrdatasets.push({
			label : arrlabel[idx],
			backgroundColor : a,
			borderWidth : 1,
			data : arrxyr
		});
	});

	var bubbleChartData = {
		animation : {
			duration : 10000
		},
		datasets : arrdatasets

	};

	var ctx = document.getElementById(id).getContext("2d");
	window.myChart = new Chart(ctx, {
		type : 'bubble',
		data : bubbleChartData,
		options : {
			responsive : true,
			title : {
				display : true,
				text : crtnm
			},
			tooltips : {
				mode : 'point'
			}
		}
	});

}

/*
 * cfnPieCrt
 * pieVal : 데이터 값, pieLbl: 데이터 이름
 */
function cfnPieCrt(id, data, colval, crtnm) {

	var arrval = new Array();
	var arrlbl = new Array();
	var arrcol = new Array();

	$(data).each(function(idx,obj) {
		arrval.push(obj['pieVal'] + '');
		arrlbl.push(obj['pieLbl'] + '');
	});

	$(colval).each(function(idx, obj) {
		arrcol.push(obj);
	});

    var config = {
        type: 'pie',
        data: {
            datasets: [{
                data: arrval,
                backgroundColor: arrcol ,
                label: 'Dataset 1'
            }],
            labels: arrlbl
        },
        options: {
            responsive: true,
            title : {
				display : true,
				text : crtnm
			},
        }
    };

        var ctx = document.getElementById(id).getContext("2d");
        window.myPie = new Chart(ctx, config);


}

/*
 * cfnRadarCrt
 * radLbls : , radLbl: 데이터이름, radVal : 데이터값
 */
function cfnRadarCrt(id, data, colval, crtnm) {

	var arrlabel = new Array();
	var arrlabels = new Array();
	var arrdatasets = [];
	var color = Chart.helpers.color;

	$(data).each(function(idx,obj){
		arrval = [];
		arrlabels = [];
		arrlabel = [];

		$(data[idx]).each(function(idx,obj) {
			arrlabels.push(obj['radLbls']);
			arrval.push(obj['radVal']);
			arrlabel.push(obj['radLbl']);
		});

		console.log(arrlabel);

		var colorrgb = color(colval[idx]).alpha(0.5).rgbString();

		arrdatasets.push({
			 label: arrlabel[idx],	//
             backgroundColor: colorrgb,
            // borderColor: window.chartColors.colorrgb,
             borderColor: colorrgb,
             //pointBackgroundColor: window.chartColors.colorrgb,
             pointBackgroundColor : colorrgb,
             data: arrval,
		});


	});

    var config = {
        type: 'radar',
        data: {
            labels: arrlabels,
            datasets: arrdatasets,
        },
        options: {
            legend: {
                position: 'top',
            },
            title: {
                display: true,
                text: crtnm
            },
            scale: {
              ticks: {
                beginAtZero: true
              }
            }
        }
    };

    window.myRadar = new Chart(document.getElementById(id), config);

}

/*
 * cfnRadarpointCrt
 * radpLbls : , radpLbl : 데이터이름, radpVal : 데이터값
 */
function cfnRadarpointCrt(id, data, colval, crtnm) {

	var arrval = new Array();
	var arrlbl = new Array();
	var arrlbls = new Array();
	var arrdatasets = [];
	var color = Chart.helpers.color;

	$(data).each(function(idx,obj){

		arrlbl = [];
		arrlbls =[];
		arrval = [];

		$(data[idx]).each(function(idx,obj){
			arrlbl.push(obj['radpLbl']);
			arrlbls.push(obj['radpLbls']);
			arrval.push(obj['radpVal']);
		});

		var colorrgb = color(colval[idx]).alpha(0.5).rgbString();

		arrdatasets.push({
			 label: arrlbl[idx],
             borderColor: colorrgb,
             backgroundColor: colorrgb,
             pointBackgroundColor:colorrgb,
             data: arrval,
		});

	});

    var config = {
        type: 'radar',
        data: {
            labels: arrlbls,
            datasets: arrdatasets
        },
        options: {
            title:{
                display: true,
                text: crtnm
            },
            elements: {
                line: {
                    tension: 0.0,
                }
            },
            scale: {
                beginAtZero: true,
            }
        }
    };

    window.myRadar = new Chart(document.getElementById(id), config);
}

/*
 * cfnPolarAreaCrt
 * polLbl : 데이터 이름 ,polVal : 데이터 값
 */
function cfnPolarCrt(id, data, colval, crtnm) {
	var arrval = new Array();
	var arrlbl = new Array();
	var arrcol = new Array();

	$(data).each(function(idx,obj) {
		arrval.push(obj['polVal'] + '');
		arrlbl.push(obj['polLbl'] + '');
	});

	var color = Chart.helpers.color;

	$(colval).each(function(idx,obj) {
		arrcol.push(color(obj).alpha(0.5).rgbString());
	});

	var config = {
		data : {
			datasets : [ {
				data : arrval,
				backgroundColor : arrcol,
				label : "My dataset" // for legend
			} ],
			labels : arrlbl
		},
		options : {
			responsive : true,
			legend : {
				position : 'right',
			},
			title : {
				display : true,
				text : crtnm
			},
			scale : {
				ticks : {
					beginAtZero : true
				},
				reverse : false
			},
			animation : {
				animateRotate : false,
				animateScale : true
			}
		}
	};

		var ctx = document.getElementById(id);
		window.myPolarArea = Chart.PolarArea(ctx, config);

}

/*
 * cfnComboCrt
 * comLbl : 데이터 이름 , comVal : 데이터 값  , comLbls :
 */
function cfnComboCrt(id, data, crttype, colval, crtnm) {
	var arrval = new Array();
	var arrlbl = new Array();
	var arrcol = new Array();
	var arrlbls = new Array();
	var arrdata = new Array();
	var arrtype = new Array();

	$(data).each(function(idx, obj) {
		arrval = [];
		arrlbl = [];
		arrlbls = [];
		arrcol = [];

		$(data[idx]).each(function(idx, obj) {
			arrval.push(obj['comVal'] + '');
			arrlbl.push(obj['comLbl'] + '');
			arrlbls.push(obj['comLbls'] + '');

		});

		$(colval).each(function(idx, obj) {
			arrcol.push(obj);
		});

		$(crttype).each(function(idx, obj) {
			arrtype.push(obj);
		});


		var color = Chart.helpers.color;
		var colorrgb = color(colval[idx]).alpha(0.5).rgbString();

		arrdata.push({
			type : arrtype[idx],
			label : arrlbl[idx],
			backgroundColor : colorrgb,
			borderColor : colorrgb,
			borderWidth : 2,
			fill : false,
			data : arrval,
		});

	});


	var chartData = {
		labels : arrlbls,
		datasets : arrdata

	};
	var ctx = document.getElementById(id).getContext("2d");
	window.myMixedChart = new Chart(ctx, {
		type : 'bar',
		data : chartData,
		options : {
			responsive : true,
			title : {
				display : true,
				text : crtnm
			},
			tooltips : {
				mode : 'index',
				intersect : true
			}
		}
	});
}

function createCookie(name, value)
{
	var expires = "";
	document.cookie = name + "=" + value + expires + "; path=/";
}

function eraseCookie(name)
{
    createCookie(name, "", -1);
}

//replaceAll
String.prototype.replaceAll = function(org,dest) {
	 return this.split(org).join(dest);
};
//trim
String.prototype.trim = function() {
    return this.replace(/(^\s*)|(\s*$)/g, "");
};

/*
 * ajax post json
 */
function ajaxPost(url, data, callback) {
	$.post(url, data)
	.done(function(data) {
		if (callback != undefined) {
			callback(data);
		}
	})
	.fail(function(data) {
		alert(JSON.parse(data.responseText).error);
	}, 'json');
}

/*
 * ajax get json
 */
function getJson(url, callback) {
	$.getJSON(encodeURI(url))
	.done(function(data) {
		if (callback != undefined) {
			callback(data);
		}
	})
	.fail(function(data) {
		alert(JSON.parse(data.responseText).error);
	});
}

/*
 * 팝오버 생성
 */
function openPopOver(obj, title, content, placement) {
	$(obj).popover('destroy');
	$(obj).popover({
		title: title,
		trigger: 'focus',
		placement: placement != undefined ? placement : 'auto',
		html: true,
		content: content
	}).popover('show');
}

/*
 * 모달창 생성
 */
function openModal(url) {
	$.get(encodeURI(url), function(data) {
		$('<div class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">' + data + '</div>').modal({
			backdrop: 'static',
			keyboard: false
		}).on('hidden.bs.modal', function() {
			$(this).remove();
		});
	})
	.fail(function(data) {
		alert(JSON.parse(data.responseText).error);
	});
}

/*
 * 모달창 생성(POST)
 */
function openModalPost(url, param) {
	$.post(encodeURI(url), param, function(data) {
		$('<div class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">' + data + '</div>').modal({
			backdrop: 'static',
			keyboard: false
		}).on('hidden.bs.modal', function() {
			$(this).remove();
		});
	})
	.fail(function(data) {
		alert(JSON.parse(data.responseText).error);
	});
}

/*
 * 모달리스창 생성
 */
function openModeless(url) {
	$.get(encodeURI(url), function(data) {
		$('<div class="modal modeless fade" tabindex="-1" role="dialog" aria-hidden="true">' + data + '</div>').on('shown.bs.modal', function() {
			$(this).draggable({
				cursor : 'move',
				handle : '.modal-header'
			});
		}).modal({
			backdrop: false,
			keyboard: false
		}).on('hidden.bs.modal', function() {
			$(this).remove();
		});
	})
	.fail(function(data) {
		alert(JSON.parse(data.responseText).error);
	});
}

/*
 * 모달창 닫기
 */
function closeModal(obj) {
	$(obj).parents('.modal').modal('hide');
}

/*
 * 주소 검색
 */
function openSearchAddr(callback) {
	openModal('/zip/zipListPop.do');
	searchAddrCallback = callback;
}

/*
 * 사용자 검색
 */
function openSearchUser(callback) {
	openModal('/rba/userPopup.do');
	searchUserCallback = callback;
}

/*
 * 스케줄러 검색
 */
function openSearchJob(callback) {
	openModal('/scd/jobBeanPopup.do');
	searchJobCallback = callback;
}

/*
 * 사용자역할 팝업
 */		 
function openSearchUserAndRole(callback) {
	openModal('/rba/userAndRoleConfirm.do');
	searchUserAndRoleCallback = callback;
}


//숫자만 입력
function cfnOnlyNumberCheck(sip){
	if (event.keyCode >= 48 && event.keyCode <= 57) { //숫자키만 입력
		return true;
	}else{
		if(event.preventDefault){	//IE11 브라우져
			event.preventDefault();
	    }else{
	    	event.returnValue = false;  //그외 브라우져
	    }
	}
	//인풋박스에 반드시 포함 style="ime-mode:disabled;"
}

/*
 * Rlm 동적 콤보박스 생성
 */
function cfnComboBoxRlm(codeId, name, div, option)
{
	$.ajax({
        url: '/ccd/selectCmmnCodeDList.do?codeId=' + codeId,
        type : 'post',
        async : false,
        data : '',
        success: function(data)
		{
		    var combo = $("<select class='form-control input-sm input-sm'></select>").attr("id", name).attr("name", name);
		    if(option != undefined)
		    {
		    	combo.append("<option value=''>" + option + "</option>");
		    }
		    $.each((JSON.parse(data))['cmmnCodeDList'], function (i, row)
		    {
		        combo.append("<option value='" + row['code'] + "'>" + row['codeNm'] + "</option>");
		    });

		    $('#' + div).append(combo);
        },
        error: function(xhr, textStatus, errorThrown)
        {
        	alert(xhr.responseText);
        }
    });
}

/*
 * Rlm 동적 콤보박스 생성 div after에 생성
 */
function cfnComboBoxRlmAfter(codeId, name, div, option)
{
	$.ajax({
		url: '/ccd/selectCmmnCodeDList.do?codeId=' + codeId,
		type : 'post',
		async : false,
		data : '',
		success: function(data)
		{
			var combo = $("<select class='form-control input-sm input-sm'></select>").attr("id", name).attr("name", name);
			if(option != undefined)
			{
				combo.append("<option value=''>" + option + "</option>");
			}
			$.each((JSON.parse(data))['cmmnCodeDList'], function (i, row)
					{
				combo.append("<option value='" + row['code'] + "'>" + row['codeNm'] + "</option>");
					});

			$('#' + div).after(combo);
		},
		error: function(xhr, textStatus, errorThrown)
		{
			alert(xhr.responseText);
		}
	});
}

/*
 * Rlm 동적 콤보박스 생성(분류체계코드)
 */
function cfnClSystemCodeComboBoxRlm(codeId, name, div, prpos, sysNm, option)
{
	var url = '';
	if (sysNm == 'rlm') {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=rlm&codeId=' + codeId+'&prpos='+prpos;
	} else {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=sps&codeId=' + codeId+'&prpos='+prpos;
	}
	$.ajax({
		url: url,
		type : 'post',
		async : false,
		data : '',
		success: function(data)
		{
			var combo = $("<select  class='form-control input-sm input-sm' ></select>").attr("id", name).attr("name", name);
			if(option != undefined)
			{
				combo.append("<option value=''>" + option + "</option>");
			}
			$.each((JSON.parse(data))['clSystemCodeDList'], function (i, row)
					{
				combo.append("<option value='" + row['code'] + "'>" + row['codeNm'] + "</option>");
					});

			$('#' + div).append(combo);
//			var inputTy = row['inputTy'];
//			if (inputTy == 'N') {
//				$("label[for="+row['code']+"]").after('<input name=\"'+row['code']+'\" type="text" value="">');
//			}
		},
		error: function(xhr, textStatus, errorThrown)
		{
			alert(xhr.responseText);
		}
	});
}

/*
 * Rlm 동적 콤보박스 생성(분류체계코드)
 */
function cfnClSystemCodeComboBoxAfterRlm(codeId, name, div, prpos, sysNm, option)
{
	var url = '';
	if (sysNm == 'rlm') {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=rlm&codeId=' + codeId+'&prpos='+prpos;
	} else {
		url = '/ccd/selectClSystemCodeDList.do?sysNm=sps&codeId=' + codeId+'&prpos='+prpos;
	}
	$.ajax({
		url: url,
		type : 'post',
		async : false,
		data : '',
		success: function(data)
		{
			var combo = $("<select  class='form-control input-sm input-sm' ></select>").attr("id", name).attr("name", name);
			if(option != undefined)
			{
				combo.append("<option value='' seleted='seleted'>" + option + "</option>");
			}
			$.each((JSON.parse(data))['clSystemCodeDList'], function (i, row)
					{
				combo.append("<option value='" + row['code'] + "'>" + row['codeNm'] + "</option>");
					});

			$('#' + div).after(combo);
//			var inputTy = row['inputTy'];
//			if (inputTy == 'N') {
//				$("label[for="+row['code']+"]").after('<input name=\"'+row['code']+'\" type="text" value="">');
//			}
		},
		error: function(xhr, textStatus, errorThrown)
		{
			alert(xhr.responseText);
		}
	});
}

/*
 * 그리드 date formatter(yyyy-MM-dd)
 */
function strDateFmatter(cellvalue) {
	return cellvalue.substring(0, 4) + '-' + cellvalue.substring(4, 6) + '-' + cellvalue.substring(6, 8);
}

function cfnOzPrint(param)
{
	console.log('xxxxxxxxxx : ' + param + ', ' + param.size());
	/*var pop_target = "Print_pop";
	window.open("", pop_target);

	var viewFrm = document.slfctrlReportSttemntViewFrm;
	viewFrm.target = pop_target;
	viewFrm.action = '/rlm/slr/slfctrlReportSttemntManagePrint.do';
	viewFrm.submit();*/


	$.getScript("http://192.168.0.200:8088/oz70/ozfviewer/AC_OETags.js", function()
	{
		$.getScript("http://192.168.0.200:8088/oz70/ozfviewer/ozutil.js", function()
		{
			$.getScript("http://192.168.0.200:8088/oz70/ozfviewer/ozjscript.js", function()
			{
				//Globals
				//Major version of Flash required
				var requiredMajorVersion = 10;
				//Minor version of Flash required
				var requiredMinorVersion = 0;
				//Minor version of Flash required
				var requiredRevision = 0;

				var hasProductInstall = DetectFlashVer(6, 0, 65);

				//Version check based upon the values defined in globals
				var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);

				if(hasProductInstall && !hasRequestedVersion) {
				  //DO NOT MODIFY THE FOLLOWING FOUR LINES
				  //Location visited after installation is complete if installation is required
				  var MMPlayerType = (isIE == true) ? "ActiveX" : "PlugIn";
				  var MMredirectURL = window.location;
				  document.title = document.title.slice(0, 47) + " - Flash Player Installation";
				  var MMdoctitle = document.title;
				  AC_FL_RunContent(
				    "src", "http://192.168.0.200:8088/oz70/ozfviewer/playerProductInstall",
				    "FlashVars", "MMredirectURL=" + MMredirectURL + '&MMplayerType=' + MMPlayerType + '&MMdoctitle=' + MMdoctitle+"",
				    "width", "100%",
				    "height", "100%",
				    "align", "middle",
				    "id", "playerProductInstall",
				    "quality", "high",
				    "bgcolor", "#ffffff",
				    "name", "playerProductInstall",
				    "allowScriptAccess", "always",
				    "type", "application/x-shockwave-flash",
				    "pluginspage", "http://www.adobe.com/go/getflashplayer"
				  );
				} else if(hasRequestedVersion) {
				  //if we've detected an acceptable version
				  //embed the Flash Content SWF when all tests are passed
				  function SetOZParamters_OZViewer() {
				    var oz;
				    var file = '${OPEN_ORZ}';
				    var map_len = '${fn:length(Param)}';
				    if(navigator.appName.indexOf("Microsoft") != -1) {
				      oz = window["OZViewer"];
				    } else {
				      oz = document["OZViewer"];
				    }
				    oz.sendToActionScript("connection.servlet", "http://192.168.0.200:8088/oz70/server");
				    oz.sendToActionScript("connection.reportname", file);
				    oz.sendToActionScript("connection.pcount", map_len);
				    oz.sendToActionScript("connection.args1", "Param1=${Param.Param1}");
				    oz.sendToActionScript("connection.args2", "Param2=${Param.Param2}");
				    oz.sendToActionScript("connection.args3", "Param3=${Param.Param3}");
				    oz.sendToActionScript("connection.args4", "Param4=${Param.Param4}");
				    oz.sendToActionScript("connection.args5", "Param5=${Param.Param5}");
				    oz.sendToActionScript("connection.args6", "Param6=${Param.Param6}");
				    oz.sendToActionScript("connection.args7", "Param7=${Param.Param7}");
				    oz.sendToActionScript("connection.args8", "Param8=${Param.Param8}");
				    oz.sendToActionScript("connection.args9", "Param9=${Param.Param9}");
				    oz.sendToActionScript("connection.args10", "Param10=${Param.Param10}");
				    oz.sendToActionScript("connection.args11", "Param11=${Param.Param11}");
				    oz.sendToActionScript("connection.args12", "Param12=${Param.Param12}");
				    oz.sendToActionScript("connection.args13", "Param13=${Param.Param13}");
				    oz.sendToActionScript("connection.args14", "Param14=${Param.Param14}");
				    oz.sendToActionScript("connection.args15", "Param15=${Param.Param15}");
				    oz.sendToActionScript("connection.args16", "Param16=${Param.Param16}");
				    oz.sendToActionScript("connection.args17", "Param17=${Param.Param17}");
				    oz.sendToActionScript("connection.args18", "Param18=${Param.Param18}");
				    oz.sendToActionScript("connection.args19", "Param19=${Param.Param19}");
				    oz.sendToActionScript("connection.args20", "Param20=${Param.Param20}");
				    oz.sendToActionScript("connection.args21", "Param21=${Param.Param21}");
				    oz.sendToActionScript("connection.args22", "Param22=${Param.Param22}");
				    oz.sendToActionScript("connection.args23", "Param23=${Param.Param23}");
				    oz.sendToActionScript("connection.args24", "Param24=${Param.Param24}");
				    oz.sendToActionScript("connection.args25", "Param25=${Param.Param25}");
				    oz.sendToActionScript("connection.args26", "Param26=${Param.Param26}");
				    oz.sendToActionScript("connection.args27", "Param27=${Param.Param27}");
				    oz.sendToActionScript("connection.args28", "Param28=${Param.Param28}");
				    oz.sendToActionScript("connection.args29", "Param29=${Param.Param29}");
				    oz.sendToActionScript("connection.args30", "Param30=${Param.Param30}");
				    oz.sendToActionScript("connection.args31", "Param31=${Param.Param31}");
				    oz.sendToActionScript("connection.args32", "Param32=${Param.Param32}");
				    oz.sendToActionScript("connection.args33", "Param33=${Param.Param33}");
				    oz.sendToActionScript("connection.args34", "Param34=${Param.Param34}");
				    oz.sendToActionScript("connection.args35", "Param35=${Param.Param35}");

				    return true;
				  }
				  AC_FL_RunContent(
				    "src", "http://192.168.0.200:8088/oz70/ozfviewer/OZViewer10",
				    "width", "100%",
				    "height", "100%",
				    "align", "middle",
				    "id", "OZViewer",
				    "quality", "high",
				    "bgcolor", "#ffffff",
				    "name", "OZViewer",
				    "allowScriptAccess", "always",
				    "type", "application/x-shockwave-flash",
				    "pluginspage", "http://www.adobe.com/go/getflashplayer",
				    "flashVars", "flash.objectid=OZViewer"
				  );
				} else { //flash is too old or we can't detect the plugin
				  var alternateContent = 'Alternate HTML content should be placed here. '
				                         + 'This content requires the Adobe Flash Player. '
				                         + '<a href=http://www.adobe.com/go/getflash/>Get Flash</a>';
				  document.write(alternateContent); //insert non-flash content
				}
			});
		});
	});
}

//전체 문자열 치환
function replaceAll(str, searchStr, replaceStr) {
    return str.split(searchStr).join(replaceStr);
}
// <> 치환
var XSSfilter = function (content) {
    return content.replace(/</g, "&lt;").replace(/>/g, "&gt;");
};

/*
 * OZ Viewer 호출
 */
function cfnOpenOzViewer(param) {
	var formData = '<form name="form" method="post" action="http://dev.kops.or.kr:90/cmn/ozviewer.do">';
	$.each(param.keys(), function(idx, key) {
		if (param.get(key) instanceof Array) {
			$.each(param.get(key), function(idx, value) {
				formData += '<input type="hidden" name="' + key + '" value="' + value + '"/>';
			});
		} else {
			formData += '<input type="hidden" name="' + key + '" value="' + param.get(key) + '"/>';
		}
	});
	formData += '</form>';

	var ozviewer = window.open('', 'ozviewer', 'left=0, top=0, width=1024, height=768, resizable=yes');
	ozviewer.document.write(formData);
	ozviewer.document.form.submit();
}

/*
 * form data -> stringify
 */
$.fn.serializeObject = function() {
	var result = {};
	var extend = function(i, element) {
		var node = result[element.name];
		if ('undefined' !== typeof node && node !== null) {
			if ($.isArray(node)) {
				node.push(element.value);
			} else {
				result[element.name] = [ node, element.value ];
			}
		} else {
			result[element.name] = element.value;
		}
	};

	$.each(this.serializeArray(), extend);
	return result;
};

var confirmYN = '';
window.confirm = function(text, callback, width) {
	if (width == undefined) {
		width = 500;
	}
	var yes = '예';
	var no = '아니오';
	var textArr = text.split('||');
	if (textArr.length == 3) {
		yes = textArr[0];
		no = textArr[1];
		text = textArr[2];
	}
	var confirmModal = '<div class="modal fade alert-modal" tabindex="-1" role="dialog" aria-hidden="true">'
		+ '<div class="modal-dialog" style="width:'+width+'px;max-width:'+width+'px;">'
		+ '  <div class="modal-content">'
		+ '    <div class="modal-body text-center">' + text.split('\n').join('<br/>') + '</div>'
		+ '    <div class="modal-footer">'
		+ '      <button type="button" class="btn btn-default btn-success" onclick="closeConfirmModal(this, \'Y\')">' + yes + '</button>'
		+ '      <button type="button" class="btn btn-default" onclick="closeConfirmModal(this, \'N\')">' + no + '</button>'
		+ '    </div>'
		+ '  </div>'
		+ '</div>';

	$(confirmModal).modal({
		backdrop: 'static',
		keyboard: false,
		show: true
	}).on('hidden.bs.modal', function () {
		callback((confirmYN == 'Y'));
		$(this).remove();
	});
};
function closeConfirmModal(obj, yn) {
	confirmYN = yn;
	$(obj).parents('.modal').modal('hide');
}

/*
 * TODO 표시
 */
function TODO(text) {
	cfnNotifyWarning('TODO', text, 1000);
}

/*
 * 알림(에러)
 */
function cfnNotifyError(title, text, delay) {
	cfnOpenNotify('error', title, text, (delay == undefined) ? 5000 : delay);
}

/*
 * 알림(정보)
 */
function cfnNotifyInfo(title, text, delay) {
	cfnOpenNotify('info', title, text, (delay == undefined) ? 8000 : delay);
}

/*
 * 알림(성공)
 */
function cfnNotifySuccess(title, text, delay) {
	cfnOpenNotify('success', title, text, (delay == undefined) ? 2000 : delay);
}

/*
 * 알림(경고)
 */
function cfnNotifyWarning(title, text, delay) {
	cfnOpenNotify('warning', title, text, (delay == undefined) ? 4000 : delay);
}

/*
 * 알림 표시
 */
function cfnOpenNotify(type, title, text, delay) {
	PNotify.prototype.options.styling = 'bootstrap3';
	new PNotify({
		type: type,
		title: title,
		text: text,
		delay: delay
	});
}