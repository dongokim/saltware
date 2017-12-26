/**
 * @author saltware
 */

var receivers = '';
var pureOptions;
var groupOptions;

function isAdd(id){
	var $receiver = $('option', '#receiverListBox');
	var isAdd = false;
	$receiver.each(function(){
		var $this = $(this);
		var rid = $this.attr('id');
		if(id == rid){
			isAdd = true;
		}
	});
	return isAdd;
}
		
function addReceiver(){
	var $selectedUser = $('option:selected','#userListBox');
	var $receiver = $('#receiverListBox');
	$selectedUser.each(function(){
		var $this = $(this);
		var id = $this.attr('id');
		if(isAdd(id)){
			alert($this.text() + '님은 이미 추가되어있습니다.');
		}else{
			var optionId = $this.attr('id');
			var optionValue = $this.val();
			var optionText = optionValue + '(' + optionId + ')';
			$('<option/>').attr('id',optionId).text(optionText).attr('value', optionValue).appendTo($receiver);
			$this.remove();
		}
	});
}

function removeReceiver(){
	$selected = $('option:selected','#receiverListBox');
	$selected.appendTo('#userListBox');
}

function initPage(){
	$('#searchBtn').click(userSearch);
	window.resizeTo(606,450);
}

/* ajax request */
function requestReceiverList(){
	$('option','#userListBox').remove();
	$.ajax({
		type: 'POST',
	url: contextPath + '/tool/searchUser.face', 
	data:'m=search&searchName=' + $('#searchName').val() + '&searchType=' + $('#searchType').val(),
	dataType: 'html',
	success: function(html, textStatus){
		receivers = html.split(",");
		var new_option;
		for(var i = 0; i < receivers.length-1; i++){
			var info = receivers[i].split(":");
			var receiver_id = info[0];
			var receiver_name = info[1];
			var group_id = info[2];
			
			var optionId = receiver_id;
			var optionValue = receiver_name;
			var optionText = optionValue + '(' + optionId + ')';
			$('<option/>').attr('id',optionId).text(optionText).attr('value', receiver_name).appendTo('#userListBox');
		}
		pureOptions = $('option', '#userListBox');
		groupOptions = pureOptions;
	},
	error: function(xhr, textStatus, errorThrown){
		alert('잠시 후에 다시 시도 해주십시오.');
		}
	});
}

/* search input keyup callback */
/* search button clicked callback */
function userSearch(){
	if(event.keyCode != 13 && event.keyCode != 0) { //enter key
		return false;
	}else{
		var searchName = $('#searchName').val();
		requestReceiverList(searchName);
	}
	
}

/* 결과 내 검색 */
function resultSearch(){
	var $userList = $('#userListBox');
	var $options = $('option', $userList);
	var searchWord = $('#userIdorName').val();
	var $serachOptions = groupOptions.filter(':contains("' + searchWord + '")');
	$options.remove();
	$serachOptions.each(function(){
		var $this = $(this);
		$this.appendTo($userList);
	});
}


/* ok button clicked callback function */
function selectUser(){
	$receiverList = $('option','#receiverListBox');
	var id='', name='';
	var i = 1;
	$receiverList.each(function(){
		var $this = $(this);
		
		if(i == $receiverList.length){
			id += $this.attr('id');
			name += $this.val();
		}
		else{
			id += $this.attr('id') + ",";
			name += $this.val() + ";";
		}
		i++;
	});
	$('#receiverIds', opener.document).val(id);
	$('#receiverList', opener.document).val(name);
	self.close();
}