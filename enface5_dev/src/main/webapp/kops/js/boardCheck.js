//빈칸 체크
function emptyCheck(id, label)
{
	if($("#"+id).val().length == 0)
	{
		alert(label + " 을(를) 입력하세요.");
		$("#"+id).focus();
		return false;
	}
	return true;
}

//문자열을 UTF-8로 변환했을 경우 차지하게 되는 byte 수를 리턴한다.
function fn_stringByteSize(str) 
{
    if(str == null || str.length == 0) 
    {
      return 0;
    }
    var size = 0;
    for(var i = 0; i < str.length; i++) 
    {
      size += fn_charByteSize(str.charAt(i));
    }
    return size;
}

//각 문자의 유니코드 코드를 분석하여, UTF-8로 변환시 차지하는 byte 수를 리턴한다.
function fn_charByteSize(ch) 
{
    if (ch == null || ch.length == 0) 
    {
      return 0;
    }
    var charCode = ch.charCodeAt(0);
    if(charCode <= 0x00007F) 
    {
      return 1;
    } 
    else if(charCode <= 0x0007FF) 
    {
      return 2;
    } 
    else if(charCode <= 0x00FFFF) 
    {
      return 3;
    }
    else 
    {
      return 4;
    }
}

//제목 체크
function titleCheck(id)
{
	var title = $("#"+id).val();
	
	if(emptyCheck(id, '제목'))
	{
		if(fn_stringByteSize(title) > 500)
		{
			alert("제목은 500자 이내로 입력하세요.");
			$("#"+id).focus();
			return false;
		}
		return true;
	}
	return false;
}

//답변확인용 비밀번호 체크
function passwordCheck(id, cmd)
{
	if(cmd != "REPLY")
	{
		var password = $("#"+id).val();
		
		if(emptyCheck(id, '답변확인용 비밀번호'))
		{
			if(fn_stringByteSize(password) > 100)
			{
				alert("답변확인용 비밀번호는 100bytes를 초과할 수 없습니다.");
				$("#"+id).focus();
				return false;
			}
			return true;
		}
		return false;
	}
	return true;
}