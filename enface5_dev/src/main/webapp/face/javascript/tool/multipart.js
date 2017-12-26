/**
 * @author saltware
 */

var warnings = {
	"homeAddr1" : {
		"required": messageResource.getMessage('pt.ev.user.label.EmptyHomeAddress'),
		"err"     : 0
	}
}

function requestUpload(){
//	$('#uploadForm').ajaxForm({
//        beforeSubmit: function(a,f,o) {
//            o.dataType = html;
//            $('#uploadOutput').html('Submitting...');
//        },
//        success: function(data) {
//            var $out = $('#uploadOutput');
//            $out.html('Form success handler received: <strong>' + typeof data + '</strong>');
//            if (typeof data == 'object' && data.nodeType)
//                data = elementToString(data.documentElement, true);
//            else if (typeof data == 'object')
//                data = objToString(data);
//            $out.append('<div><pre>'+ data +'</pre></div>');
//        }
//    });
}

//<div id="result"></div> 영역 변경 함수
function initResults(responseHTML){
	$('div#result').html(responseHTML);
}

function initPage(){
	$('#upload').click(checkFile);
}

function checkFile(e){
	requestUpload();
}
