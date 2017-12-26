/**
 * @author saltware
 */

var warnings = {
	"homeAddr1" : {
		"required": messageResource.getMessage('pt.ev.user.label.EmptyHomeAddress'),
		"err"     : 0
	}
}

function requestSearch(data){
	$.ajax({
		type: 'POST',
		url: contextPath + '/tool/ajaxSearchZip.face', 
		data:'m=search&homeAddr1=' + data,
		dataType: 'html',
		success: function(html, textStatus){
			initResults(html);
		},
		error: function(xhr, textStatus, errorThrown){
			alert('잠시 후에 다시 시도 해주십시오.');
		}
	});
}

//<div id="result"></div> 영역 변경 함수
function initResults(responseHTML){
	$('div#result').html(responseHTML);
}

function initPage(){
	$('#search').click(checkAddress);

	$('#homeAddr1').keyup(fieldIsFilled);
	$('#homeAddr1').keyup(checkAddress);
	$('#homeAddr1').blur(fieldIsFilled);
	$('#homeAddr1').focus();
}

function checkAddress(e){
	var keyCode = e.which;

	//keyup event
	if(keyCode == 13){
		homeAddr1 = $(this);
	}

	//click event
	else if(keyCode == 0){
		homeAddr1 = $('#homeAddr1');
	}
	else return false;
	
	if( homeAddr1.val() == ""){
		alert(messageResource.getMessage('pt.ev.user.label.EmptyHomeAddress'));
		homeAddr1.select();
		return false;
	}
	else {
		requestSearch(homeAddr1.val());
		homeAddr1.select();
		return false;
	}
}
