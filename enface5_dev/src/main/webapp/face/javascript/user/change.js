/*
*	JSON Object
*/
var warnings = {
	"user_name" : {
		"required": messageResource.getMessage('pt.ev.user.label.EmptyUserName'),
		"err"     : 0
	},
	"user_jumin1" : {
		"required": messageResource.getMessage('pt.ev.user.label.EmptyUserRegNo'),
		"invalid" : messageResource.getMessage('pt.ev.user.label.InvalidRegNo'),
		"err"     : 0
	},
	"user_jumin2" : {
		"required": messageResource.getMessage('pt.ev.user.label.EmptyUserRegNo'),
		"invalid" : messageResource.getMessage('pt.ev.user.label.InvalidRegNo'),
		"err"     : 0
	},
	"password" : {
		"required": messageResource.getMessage('pt.ev.user.label.EmptyPassword'),
		"invalid" : messageResource.getMessage('pt.ev.user.label.InvalidPassword'),
		"confirm" : messageResource.getMessage('pt.ev.user.label.InvalidConfirmPassword'),
		"accord"  : "비밀번호가 일치하지 않습니다."
	},
	 "passwordConfirm" : {
		"required": messageResource.getMessage('pt.ev.user.label.EmptyConfirmPassword'),
		"invalid" : messageResource.getMessage('pt.ev.user.label.InvalidPassword'),
		"confirm" : messageResource.getMessage('pt.ev.user.label.InvalidConfirmPassword'),
		"accord"  : "비밀번호가 일치하지 않습니다."
	},
	"homeAddr2" : {
		"required": messageResource.getMessage('pt.ev.user.label.EmptyHomeAddress2'),
		"err"     : 0
	},
	"user_hp1" : {
		"required": messageResource.getMessage('pt.ev.user.label.EmptyUserHp'),
		"numbers" : messageResource.getMessage('pt.ev.user.required.Number'),
		"err"     : 0
	},
	"user_hp2" : {
		"required": messageResource.getMessage('pt.ev.user.label.EmptyUserHp'),
		"numbers" : messageResource.getMessage('pt.ev.user.required.Number'),
		"err"     : 0
	},
	"user_hp3" : {
		"required": messageResource.getMessage('pt.ev.user.label.EmptyUserHp'),
		"numbers" : messageResource.getMessage('pt.ev.user.required.Number'),
		"err"     : 0
	},
	"user_email1" : {
		"required": messageResource.getMessage('pt.ev.user.label.EmptyEmailId'),
		"invalid" : messageResource.getMessage('pt.ev.user.required.English'),
		"err"     : 0
	},
	"user_email2" : {
		"required": messageResource.getMessage('pt.ev.user.label.EmptyEmailDomain'),
		"invalid" : messageResource.getMessage('pt.ev.user.label.InvalidUserEmail'),
		"err"     : 0
	}
}


var methods = {
	"start" : {
		"prev" : "change",
		"next" : "passwordConfirm"
	},
	"passwordConfirm" : {
		"prev" : "start",
		"next" : "userInfo"
	},
	"userInfo" : {
		"prev" : "passwordConfirm",
		"next" : "userInfoConfirm"
	},
	"userInfoChange" : {
		"prev" : "passwordConfirm",
		"next" : "userInfoConfirm"
	},
	"userInfoConfirm" : {
		"prev" : "userInfoChange",
		"next" : "complete"
	}
}

//회원정보 변경 메인 이동
function goChangeInfoPage(e){
	initPage('ChangeInfo', 'start');
}