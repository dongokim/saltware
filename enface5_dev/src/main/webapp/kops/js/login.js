var loginUt = null;

var certi_register_option = {
	url : "/kops/jsp/login/register_certi.jsp",
	w : 600,
	h : 700
};
LoginUtill = function() {
	this.init();
}
LoginUtill.prototype = {		
		init : function() {
			
		},
		showPop : function(popId) {
			var popWin = null;
			var LeftPosition = 0;
	        var TopPosition = 0;
	        var option = "";
			
			if(popId == 'certi') {
				LeftPosition = ((screen.width-certi_register_option.w)/2);
				TopPosition = ((screen.height-certi_register_option.h)/2);
				option = "width="+certi_register_option.w+", height="+certi_register_option.h+",top="+TopPosition+",left="+LeftPosition+",scrollbars=yes";
				popWin = window.open(certi_register_option.url, popId, option);
			} else if(popId == 'certi_login') {
				LeftPosition = ((screen.width-certi_login_option.w)/2);
				TopPosition = ((screen.height-certi_login_option.h)/2);
				option = "width="+certi_login_option.w+", height="+certi_login_option.h+",top="+TopPosition+",left="+LeftPosition+",scrollbars=yes";
				popWin = window.open(certi_login_option.url, popId, option);
			} 
		},
		checkAccessIp : function() {
			var result = false;
			
			$.ajax({
				type : 'POST',
				url : '/user/checkAccessIp.face',
				dataType : 'json',
				async : false,
				success : function(json, textStatus){
					if(json.result == "success") {
						result = true;
					} else {
						result = false;
					}
				},
				error : function(x, e, textStatus, errorThrown, XMLHttpRequest){
					result = false;
				}
			});
			
			return result;
		}
}