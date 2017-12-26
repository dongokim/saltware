package com.saltware.enface.user.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.saltware.enface.common.BaseVo;
import com.saltware.enview.util.EnviewMap;


public class UserVO extends BaseVo{

	/**
	 * for password in SECURITY_CREDENTIAL
	 */
	private int	 credentialId;
	/**
	 * 자격ID를 리턴한다
	 * @return 자격ID
	 */
	public 	int	 getCredentialId(){return credentialId;}
	/**
	 * 자격ID를 설정한다.
	 * @param credentialId
	 */
	public	void setCredentialId(int credentialId){this.credentialId = credentialId;}
	
    private String columnValue;
    /**
     * 컬럼값를 리턴한다.
     * @return 컬럼값
     */
	public  String getColumnValue() {return columnValue;}
	
	/**
	 * 컬럼값을 설정하낟.
	 * @param columnValue 컬럼값
	 */
	public  void   setColumnValue(String columnValue) {this.columnValue = columnValue;}
	
	private String password;
	/**
	 * 비밀번호를 리턴한다.
	 * @return 비밀번호
	 */
	public  String getPassword() {return password;}
	/**
	 * 비밀번호를 설정한다.
	 * @param password 비밀번호
	 */
	public  void   setPassword(String password) {this.password = password;}
	
	private String passwordNew;
	/**
	 * 신규비밀번호를 리턴한다.
	 * @return 신규비밀번호
	 */
	public  String getPasswordNew() {return passwordNew;}
	/**
	 * 신규비밀번호를 설정한다.
	 * @param passwordNew 신규비밀번호
	 */
	public  void   setPasswordNew(String passwordNew) {this.passwordNew = passwordNew;}
	
	private String passwordConfirm;
	/**
	 * 확인비밀번호를 리턴한다.
	 * @return 확인비밀번호
	 */
	public  String getPasswordConfirm() {return passwordConfirm;}
	/**
	 * 확인비밀번호를 설정한다.
	 * @param passwordConfirm 확인비밀번호
	 */
	public  void   setPasswordConfirm(String passwordConfirm) {this.passwordConfirm = passwordConfirm;}
	/**
	 * for getting the value of record sequence
	 */
	private String rnum;
	
	/**
	 * 레코드번호를 리턴한다
	 * @return 레코드번호
	 */
	public  String getRnum () {return this.rnum;}
	/**
	 * 레코드번호를 설정한다
	 * @param rnum 레코드번호
	 */
	public  void   setRnum (String rnum) {this.rnum = rnum;}
	/**
	 * for getting the value of principal_id from the table 'SecurityPrincipal'
	 */
	private int	 principalId;					
	
	/**
	 * 주체ID를 리턴한다
	 * @return 주체ID
	 */
	public  int  getPrincipalId () {return this.principalId;}
	/**
	 * 주체ID를 설정한다
	 * @param principalId 주체ID
	 */
	public  void setPrincipalId(int principalId) {this.principalId = principalId;}
	
	
	private String destination;
	/**
	 * 작업완료 후 이동할 목적지를 리턴한다.
	 * @return 목적지
	 */
	public  String getDestination () {return this.destination;}
	/**
	 * 작업완료 후 이동할 목적지를 설정한다.
	 * @param destination 목적지
	 */
	public  void   setDestination (String destination) {this.destination = destination;}
	
	private String current;		
	
	/**
	 * 현재 위치를 리턴한다.
	 * @return 현재위치
	 */
	public  String getCurrent () {return this.current;}
	/**
	 * 현재위치를 설정한다.
	 * @param current 현재위치
	 */
	public  void   setCurrent (String current) {this.current = current;}
	
	private String langKnd;		
	/**
	 * 언어종류를 리턴한다
	 * @return 언어종류
	 */
	public  String getLangKnd () {return this.langKnd;}
	/**
	 * 언어종류를 설정한다
	 * @param langKnd 언어종류
	 */
	public  void   setLangKnd (String langKnd) {this.langKnd = langKnd;}
	
	/**
	 * 언어목록을 리턴한다
	 */
	private List langKndList;					
	/**
	 * 언어목록을 리턴한다
	 * @return 언어목록
	 */
	public  List getLangKndList () {return this.langKndList;}
	/**
	 * 언어목록을 설정한다.
	 * @param langKndList
	 */
	public  void   setLangKndList (List langKndList) {this.langKndList = langKndList;}
	
	private String groupId;
	/**
	 * 그룹ID를 리턴한다.
	 * @return 그룹ID
	 */
	public String getGroupId() { return groupId; }
	/**
	 * 그룹ID를 설정한다.
	 * @param groupId 그룹ID
	 */
	public void setGroupId(String groupId) { this.groupId = groupId; }

	private String roleId;
	/**
	 * 역할ID를 리턴한다
	 * @return 역할ID
	 */
	public String getRoleId() { return roleId; }
	/**
	 * 역할ID를 설정한다
	 * @param roleId 역할ID
	 */
	public void setRoleId(String roleId) { this.roleId = roleId; }
	
	private int enabled;
	/**
	 * 사용여부를 리턴한다.
	 * @return 사용여부
	 */
	public int getEnabled() { return enabled; }
	/**
	 * 사용여부를 설정한다.
	 * @param enabled
	 */
	public void setEnabled(int enabled) { this.enabled = enabled; }

	private int domainId;
	/**
	 * 도메인ID를 리턴한다. 
	 * @return 도메인ID
	 */
	public int getDomainId() { return domainId; }
	/**
	 * 도메인ID를 설정한다. 
	 * @param domainId 도메인ID
	 */
	public void setDomainId(int domainId) { this.domainId = domainId; }
	
	private String domainNm;
	/**
	 * 도메인명을 리턴한다. 
	 * @return 도메인명
	 */
	public String getDomainNm() { return domainNm; }
	/**
	 * 도메인명을 설정한다. 
	 * @param domainNm 도메인명
	 */
	public void setDomainNm(String domainNm) { this.domainNm = domainNm; }
	
	
	private String	userId;					
	private String	regNo;					
	private String	comNo;					
	private String	orgCd;		
	private String	gradeCd;	
	private String	empNo;					
	private String	nmKor;					
	private String	stateFlag;				
	private String	orgFlag;				
	private String	levelCd;				
	private String	kindCd;					
	private String	typeCd;					
	private int	  	themeId;			
	private String	mailFlag;				
	private String	blogFlag;				
	private String	memoFlag;				
	private String	diskFlag;				
	private String	rcmdUserId;			
	private long	mileTot;
	private String	nmChr;					
	private String	nmEng;					
	private String	nmNic;					
	private String	sexFlag;				
	private String	homeTel;				
	private String	offcTel;				
	private String	mobileTel;				
	private String	faxNo;					
	private Timestamp birthYmd;				
	private String	luorsunFlag;			
	private Timestamp marryYmd;				
	private String	emailAddr;				
	private String	openFlag;			
	private String	homeZip;				
	private String	homeAddr1;				
	private String	homeAddr2;				
	private String	offcZip;				
	private String	offcAddr1;				
	private String	offcAddr2;				
	private String	userIcon;				
	private String	userInfo01;				
	private String	userInfo02;				
	private String	userInfo03;				
	private String	userInfo04;				
	private String	userInfo05;				
	private String	userInfo06;				
	private String	userInfo07;				
	private String	userInfo08;				
	private String	userInfo09;				
	private String	userInfo10;				
	private Timestamp lastLogon;				
	private String	lastIp;					
	private Timestamp regDatim;				
	private String	updUserId;				
	private Timestamp updDatim;				
	private String	page1stFlag;			
	private String	page1stUrl;				
	private String	picPath;				
	private String	intro;
	private int sortOrder = 0;
	private String principalInfo01;
	private String principalInfo02;
	private String principalInfo03;
	
	
	/**
	 * 작성일: 2010년 12월 6일
	 * 작성자: 김성욱 
	 * 추가변수: user_email1, user_email2, user_hp
	 * 추가함수: 위 변수들에 대한 getter, setter 함수
	 */
	private boolean isAgree;
	/**
	 * 사용자ID를 리턴한다.
	 * @return 사용자ID
	 */
	public String	getUserId     () { return userId;		}
	/**
	 * 주민번호를 리턴한다
	 * @return 주민번호
	 */
	public String	getRegNo      () { return regNo;		}
	/**
	 * 주민번호 앞6자리를 리턴한다
	 * @return 주민번호 앞6자리
	 */
	public String	getRegNo1     () { return StringUtils.substring(regNo,0,6); }
	/**
	 * 주민번호 뒤7자리를 리턴한다.
	 * @return 주민번호 뒤7자리
	 */
	public String	getRegNo2     () { return StringUtils.substring(regNo,6); }
	
	/**
	 * 사업자등록번호를 리턴한다
	 * @return 사업자등록번호
	 */
	public String	getComNo      () { return comNo;		}
	/**
	 * 조직코드를 리턴한다.
	 * @return 조직코드
	 */
	public String	getOrgCd      () { return orgCd;		}
	
	/**
	 * 등급코드를 리턴한다.
	 * @return 등급코드
	 */
	public String 	getGradeCd() 	 { return gradeCd; 		}
	/**
	 * 사번을 리턴한다
	 * @return 사번
	 */
	public String	getEmpNo      () { return empNo;		}
	/**
	 * 한글이름을 리턴한다
	 * @return 한글이름
	 */
	public String	getNmKor      () { return nmKor;		}
	/**
	 * 상태플래그를 리턴한다.
	 * @return 상태플래그
	 */
	public String	getStateFlag  () { return stateFlag;	}
	/**
	 * 조직플래그를 리턴한다 
	 * @return 조직플래그
	 */
	public String	getOrgFlag    () { return orgFlag;		}
	/**
	 * 레벨코드를 리턴한다
	 * @return 레벨코드
	 */
	public String	getLevelCd    () { return levelCd;		}
	/**
	 * 사용자타입코드
	 * @return 사용자타입
	 */
	public String	getKindCd     () { return kindCd;		}
	/**
	 * 사용자종류코드를 리턴한다.
	 * @return 사용자종류코드
	 */
	public String	getTypeCd     () { return typeCd;		}
	/**
	 * 사용자테마ID를 리턴한다.
	 * @return 사용자테마ID
	 */
	public int	  	getThemeId    () { return themeId;		}
	/**
	 * 메일사용여부를 리턴한다 
	 * @return 메일사용여부
	 */
	public String	getMailFlag   () { return mailFlag;		}
	/**
	 * 블로그사용여부를 리턴한다
	 * @return 블로그사용여부
	 */
	public String	getBlogFlag   () { return blogFlag;		}
	/**
	 * 메모사용여부를 리턴한다
	 * @return 메모사용여부
	 */
	public String	getMemoFlag   () { return memoFlag;		}
	/**
	 * 웹디스크사용여부를 리턴한다
	 * @return 웹디스크사용여부
	 */
	public String	getDiskFlag   () { return diskFlag;		}
	/**
	 * 추천인ID를 리턴한다
	 * @return 추천인ID 
	 */
	public String	getRcmdUserId () { return rcmdUserId;	}
	/**
	 * 전체마일리지를 리턴한다.
	 * @return 전체마일리지
	 */
	public long		getMileTot    () { return mileTot;		}
	/**
	 * 중문이름을 리턴한다.
	 * @return 중문이름
	 */
	public String	getNmChr      () { return nmChr;		}
	/**
	 * 영문이름을 리턴한다.
	 * @return 영문이름
	 */
	public String	getNmEng      () { return nmEng;		}
	/**
	 * 별명을 리턴한다
	 * @return 별명
	 */
	public String	getNmNic      () { return nmNic;		}
	/**
	 * 성별플래그를 리턴한다
	 * @return 성별플래그
	 */
	public String	getSexFlag    () { return sexFlag;		}
	/**
	 * 집전화를 리턴한다
	 * @return 집전화
	 */
	public String	getHomeTel    () { return homeTel;		}
	/**
	 * 집전화 지역번호를 리턴한다 
	 * @return 집전화 지역번호
	 */
	public String	getHomeTel1   () { return getToken(homeTel, "-", 1); }
	/**
	 * 집전화의 국번을 리턴한다.
	 * @return 집전화 국번
	 */
	public String	getHomeTel2   () { return getToken(homeTel, "-", 2); }
	/**
	 * 집전화의 개별번호를 리턴한다.
	 * @return 집전화 개별번호
	 */
	public String	getHomeTel3   () { return getToken(homeTel, "-", 3); }
	/**
	 * 사무실전화번호를 리턴한다.
	 * @return 사무실전화
	 */
	public String	getOffcTel    () { return offcTel;		}
	/**
	 * 사무실 지역번호를 리턴한다
	 * @return 사무실 지역번호
	 */
	public String	getOffcTel1   () { return getToken(offcTel, "-", 1); }
	/**
	 * 사무실 국번을 리턴한다
	 * @return 사무실 국번
	 */
	public String	getOffcTel2   () { return getToken(offcTel, "-", 2); }
	/**
	 * 사무실 개별번호를 리턴한다.
	 * @return 사무실개별번호
	 */
	public String	getOffcTel3   () { return getToken(offcTel, "-", 3); }
	/**
	 * 휴대전화번호를 리턴한다  
	 * @return 휴대전화번호
	 */
	public String	getMobileTel  () { return mobileTel;	}
	/**
	 * 휴대전화 사업자식별번호를 리턴한다
	 * @return 휴대전화 사업자 식별번호
	 */
	public String	getMobileTel1 () { return getToken(mobileTel, "-", 1); }
	/**
	 * 휴대전화 국번을 리턴한다
	 * @return 휴대전화 국번
	 */
	public String	getMobileTel2 () { return getToken(mobileTel, "-", 2); }
	/**
	 * 휴대전화 개별전호를 리턴한다
	 * @return 휴대전화 개별번호
	 */
	public String	getMobileTel3 () { return getToken(mobileTel, "-", 3); }
	/**
	 * 팩스번호를 리턴한다
	 * @return 팩스번호
	 */
	public String	getFaxNo      () { return faxNo;		}
	/**
	 * 팩스 지역번호를 리턴한다
	 * @return 팩스 지역번호
	 */
	public String	getFaxNo1     () { return getToken(faxNo, "-", 1); }
	/**
	 * 팩스 국번를 리턴한다
	 * @return 팩스 국번
	 */
	public String	getFaxNo2     () { return getToken(faxNo, "-", 2); }
	/**
	 * 팩스 개별번호를 리턴한다
	 * @return 팩스 개별번호
	 */
	public String	getFaxNo3     () { return getToken(faxNo, "-", 3); }
	/**
	 * 생년월일을 리턴한다
	 * @return 생년월일
	 */
	public Timestamp getBirthYmd  () { return birthYmd;		}
	/**
	 * 생년을 리턴한다
	 * @return 생년
	 */
	public String	getBirthYear  () { return getDateF(getBirthYmd(), "yyyy"); }
	/**
	 * 생월을 리턴한다
	 * @return 생월
	 */
	public String	getBirthMonth () { return getDateF(getBirthYmd(), "MM");   }
	/**
	 * 생일을 리턴한다
	 * @return 생일
	 */
	public String	getBirthDay   () { return getDateF(getBirthYmd(), "dd");   }
	/**
	 * 생년월일을 포맷하여 리턴한다
	 * @return 생년월일
	 */
	public String	getBirthYmdF  () { return getDateF(birthYmd, "yyyy.MM.dd"); }
	/**
	 * 음력양력구분을 리턴한다.
	 * @return 음력양력구분
	 */
	public String	getLuorsunFlag() { return luorsunFlag;	}
	/**
	 * 결혼일자을 리턴한다
	 * @return 결혼일자
	 */
	public Timestamp getMarryYmd  () { return marryYmd;		}
	/**
	 * 결혼년도를 리턴한다.
	 * @return 결혼년도
	 */
	public String	getMarryYear  () { return getDateF(getMarryYmd(), "yyyy"); }
	/**
	 * 결혼월을 리턴한다
	 * @return 결혼월
	 */
	public String	getMarryMonth () { return getDateF(getMarryYmd(), "MM");   }
	/**
	 * 결혼일을 리턴
	 * @return 결혼일
	 */
	public String	getMarryDay   () { return getDateF(getMarryYmd(), "dd");   }
	/**
	 * 포맷된결혼일자를 리턴한다.
	 * @return 결혼일자
	 */
	public String	getMarryYmdF  () { return getDateF(marryYmd, "yyyy.MM.dd"); }
	/**
	 * 이메일주소를 리턴한다
	 * @return 이메일 주소
	 */
	public String	getEmailAddr  () { return emailAddr;	}
	/**
	 * 이메일주소의 사용자ID를 리턴한다.
	 * @return 이메일주소의 사용자ID
	 */
	public String	getEmailAddr1 () { return getToken(emailAddr, "@", 1); }
	/**
	 * 이메일주소의 호스트명을 리턴한다.
	 * @return 이메일주소의 호스트명
	 */
	public String	getEmailAddr2 () { return getToken(emailAddr, "@", 2); }
	/**
	 * 공개여부를 리턴한다
	 * @return 공개여부
	 */
	public String	getOpenFlag   () { return openFlag;		}
	/**
	 * 집 우편번호를 리턴한다.
	 * @return 집우편번호
	 */
	public String	getHomeZip    () { return homeZip;		}
	/**
	 * 집 우편번호 앞 3자리를 리턴한다
	 * @return 집 우편번호 앞 3자리
	 */
	public String	getHomeZip1   () { return StringUtils.substring(homeZip,0,3); }
	/**
	 * 집 우편번호 뒤 3자리를 리턴한다
	 * @return 집 우편번호 뒤 3자리
	 */
	public String	getHomeZip2   () { return StringUtils.substring(homeZip,3);  }
	/**
	 * 집주소1을 리턴한다
	 * @return 집주소1
	 */
	public String	getHomeAddr1  () { return homeAddr1;	}
	/**
	 * 집주소2를 리턴한다
	 * @return 집주소2
	 */
	public String	getHomeAddr2  () { return homeAddr2;	}

	/**
	 * 사무실우편번호를 리턴한다.
	 * @return 사무실 우편번호
	 */
	public String	getOffcZip    () { return offcZip;		}
	/**
	 * 사무실우편번호 앞 3자리를 리턴한다.
	 * @return 사무실 우편번호 앞 3자리
	 */
	public String	getOffcZip1   () { return StringUtils.substring(offcZip,0,3); }
	/**
	 * 사무실우편번호 뒤 3자리를 리턴한다.
	 * @return 사무실 우편번호 뒤 3자리
	 */
	public String	getOffcZip2   () { return StringUtils.substring(offcZip,3);	  }
	/**
	 * 사무실 주소1을 리턴한다
	 * @return 사무실 주소1
	 */
	public String	getOffcAddr1  () { return offcAddr1;	}
	/**
	 * 사무실 주소2를 리턴한다
	 * @return 사무실 주소2
	 */
	public String	getOffcAddr2  () { return offcAddr2;	}
	/**
	 * 사용자 아이콘을 리턴한다
	 * @return 사용자 아이콘
	 */
	public String	getUserIcon   () { return userIcon;		}
	/**
	 * 사용자정보1을 리턴한다 
	 * @return 사용자정보1
	 */
	public String		getUserInfo01 () { return userInfo01;	}
	/**
	 * 사용자정보2를 리턴한다 
	 * @return 사용자정보2
	 */
	public String		getUserInfo02 () { return userInfo02;	}
	/**
	 * 사용자정보3을 리턴한다 
	 * @return 사용자정보3
	 */
	public String		getUserInfo03 () { return userInfo03;	}
	/**
	 * 사용자정보4를 리턴한다 
	 * @return 사용자정보4
	 */
	public String	getUserInfo04 () { return userInfo04;	}
	/**
	 * 사용자정보5를 리턴한다 
	 * @return 사용자정보5
	 */
	public String	getUserInfo05 () { return userInfo05;	}
	/**
	 * 사용자정보6을 리턴한다 
	 * @return 사용자정보6
	 */
	public String	getUserInfo06 () { return userInfo06;	}
	/**
	 * 사용자정보7을 리턴한다 
	 * @return 사용자정보7
	 */
	public String	getUserInfo07 () { return userInfo07;	}
	/**
	 * 사용자정보8을 리턴한다 
	 * @return 사용자정보8
	 */
	public String	getUserInfo08 () { return userInfo08;	}
	/**
	 * 사용자정보9를 리턴한다 
	 * @return 사용자정보9
	 */
	public String	getUserInfo09 () { return userInfo09;	}
	/**
	 * 사용자정보10을 리턴한다 
	 * @return 사용자정보9
	 */
	public String	getUserInfo10 () { return userInfo10;	}
	
	/**
	 * 마지막로그인시간을 리턴한다
	 * @return 마지막로그인시간
	 */
	public Timestamp getLastLogon () { return lastLogon;	}
	/**
	 * 마지막로그인IP를 리턴한다.
	 * @return 마지막로그인IP
	 */
	public String	getLastIp     () { return lastIp;		}
	/**
	 * 등록일시를 리턴한다
	 * @return 등록일시
	 */
	public Timestamp getRegDatim  () { return regDatim;		}
	/**
	 * 포맷된 등록일시를 리턴한다
	 * @return 포맷된등록일시
	 */
	public String	getRegDatimF  () { return getDateF(regDatim, "yyyy.MM.dd"); }
	/**
	 * 수정자ID를 리턴한다.
	 * @return 수정자ID
	 */
	public String	getUpdUserId  () { return updUserId;	}
	/**
	 * 수정일시를 리턴한다
	 * @return 수정일시
	 */
	public Timestamp getUpdDatim  () { return updDatim;		}
	/**
	 * 포맷된수정일시를 리턴한다.
	 * @return 포맷된수정일시
	 */
	public String	getUpdDatimF  () { return getDateF(updDatim, "yyyy.MM.dd"); }
	
	/**
	 * Page1stFlag를 리턴한다.
	 * @return Page1stFlag
	 */
	public String	getPage1stFlag() { return page1stFlag;	}
	/**
	 * Page1stUrl를 리턴한다
	 * @return Page1stUrl
	 */
	public String	getPage1stUrl () { return page1stUrl;	}
	/**
	 * 사진경로를 리턴한다
	 * @return 사진경로
	 */
	public String	getPicPath    () { return picPath;		}
	/**
	 * 소개를 리턴한다
	 * @return 소개
	 */
	public String	getIntro      () { return intro;		}

	/**
	 * 사용자ID를 설정한다.
	 * @param userId 사용자ID
	 */
	public void setUserId     ( String userId	  ) { this.userId	   = userId;	  }
	/**
	 * 주민번호를 설정한다
	 * @param regNo 주민번호
	 */
	public void setRegNo      ( String regNo	  ) { this.regNo	   = regNo;		  }
	/**
	 * ComNo를 설정한다.
	 * @param comNo ComNo
	 */
	public void setComNo      ( String comNo	  ) { this.comNo	   = comNo;		  }
	/**
	 * 조직코드를 설정한다.
	 * @param orgCd
	 */
	public void setOrgCd      ( String orgCd	  ) { this.orgCd	   = orgCd;		  }
	/**
	 * 등급코드를 설정한다.
	 * @param gradeCd 등급코드
	 */
	public void setGradeCd	  (String gradeCd	  ) { this.gradeCd 	   = gradeCd; 	  }
	/**
	 *  사번을 설정한다
	 * @param empNo 사번
	 */
	public void setEmpNo      ( String empNo	  ) { this.empNo	   = empNo;		  }
	/**
	 *  한글이름을 설정한다
	 * @param nmKor 한글이름
	 */
	public void setNmKor      ( String nmKor	  ) { this.nmKor	   = nmKor;		  }
	/**
	 *  상태플래그를 설정한다.
	 * @param stateFlag 상태플래그
	 */
	public void setStateFlag  ( String stateFlag  ) { this.stateFlag   = stateFlag;	  }
	/**
	 *  조직플래그를 설정한다 
	 * @param orgFlag 조직플래그
	 */
	public void setOrgFlag    ( String orgFlag	  ) { this.orgFlag	   = orgFlag;	  }
	/**
	 *  레벨코드를 설정한다
	 * @param levelCd 레벨코드
	 */
	public void setLevelCd    ( String levelCd	  ) { this.levelCd	   = levelCd;	  }
	/**
	 *  사용자종류코드를 설정한다.
	 * @param kindCd
	 */
	public void setKindCd     ( String kindCd	  ) { this.kindCd	   = kindCd;	  }
	/**
	 *  사용자종류코드를 설정한다
	 * @param typeCd 사용자종류코드
	 */
	public void setTypeCd     ( String typeCd	  ) { this.typeCd	   = typeCd;	  }
	/**
	 *  사용자테마ID를 설정한다.
	 * @param themeId 테마ID
	 */
	public void setThemeId    ( int	   themeId	  ) { this.themeId	   = themeId;	  }
	/**
	 *  메일사용여부를 설정한다 
	 * @param mailFlag 메일사용여부
	 */
	public void setMailFlag   ( String mailFlag	  ) { this.mailFlag	   = mailFlag;	  }
	/**
	 *  블로그사용여부를 설정한다
	 * @param blogFlag 블로그사용여부
	 */
	public void setBlogFlag   ( String blogFlag	  ) { this.blogFlag	   = blogFlag;	  }
	/**
	 *  메모사용여부를 설정한다
	 * @param memoFlag 메모사용여부
	 */
	public void setMemoFlag   ( String memoFlag	  ) { this.memoFlag	   = memoFlag;	  }
	/**
	 *  웹디스크사용여부를 설정한다
	 * @param diskFlag 웹디스크사용여부
	 */
	public void setDiskFlag   ( String diskFlag	  ) { this.diskFlag	   = diskFlag;	  }
	/**
	 *  추천인ID를 설정한다
	 * @param rcmdUserId 추천인ID
	 */
	public void setRcmdUserId ( String rcmdUserId ) { this.rcmdUserId  = rcmdUserId;  }
	/**
	 *  전체마일리지를 설정한다.
	 * @param mileTot 전체마일리지
	 */
	public void setMileTot    ( long   mileTot	  ) { this.mileTot	   = mileTot;	  }
	/**
	 *  중문이름을 설정한다.
	 * @param nmChr 중문이름
	 */
	public void setNmChr      ( String nmChr	  ) { this.nmChr	   = nmChr;		  }
	/**
	 *  영문이름을 설정한다.
	 * @param nmEng 영문이름
	 */
	public void setNmEng      ( String nmEng	  ) { this.nmEng	   = nmEng;		  }
	/**
	 *  별명을 설정한다
	 * @param nmNic 별
	 */
	public void setNmNic      ( String nmNic	  ) { this.nmNic	   = nmNic;		  }
	/**
	 *  성별플래그를 설정한다
	 * @param sexFlag 성별플래그
	 */
	public void setSexFlag    ( String sexFlag	  ) { this.sexFlag	   = sexFlag;	  }
	/**
	 *  집전화번호를 설정한다
	 * @param homeTel 집전화번호
	 */
	public void setHomeTel    ( String homeTel	  ) { this.homeTel	   = homeTel;	  }
	/**
	 *  사무실전화번호를 설정한다.
	 * @param offcTel 사무실전화번호
	 */
	public void setOffcTel    ( String offcTel	  ) { this.offcTel	   = offcTel;	  }
	/**
	 *  휴대전화번호를 설정한다  
	 * @param mobileTel 휴대전화번호
	 */
	public void setMobileTel  ( String mobileTel  ) { this.mobileTel   = mobileTel;	  }
	/**
	 *  팩스번호를 설정한다
	 * @param faxNo 팩스번호
	 */
	public void setFaxNo      ( String faxNo	  ) { this.faxNo	   = faxNo;		  }
	/**
	 *  생년월일을 설정한다
	 * @param birthYmd 생년월일
	 */
	public void setBirthYmd   ( Timestamp birthYmd) { this.birthYmd	   = birthYmd;	  }
	/**
	 *  음력양력구분을 설정한다.
	 * @param luorsunFlag 음력양력구분
	 */
	public void setLuorsunFlag( String luorsunFlag) { this.luorsunFlag = luorsunFlag; }
	/**
	 *  결혼일자를 설정한다
	 * @param marryYmd 결혼일자
	 */
	public void setMarryYmd   ( Timestamp marryYmd) { this.marryYmd	   = marryYmd;	  }
	/**
	 *  이메일주소를 설정한다
	 * @param emailAddr 이메일주소
	 */
	public void setEmailAddr  ( String emailAddr  ) { this.emailAddr   = emailAddr;	  }
	/**
	 *  공개여부를 설정한다
	 * @param openFlag 공개여부
	 */
	public void setOpenFlag   ( String openFlag	  ) { this.openFlag	   = openFlag;	  }
	/** 집 우편번호를 설정한다.
	 * 
	 * @param homeZip 집 우편번호
	 */
	public void setHomeZip    ( String homeZip	  ) { this.homeZip	   = homeZip;	  }
	/**
	 *  집주소1을 설정한다
	 * @param homeAddr1 집주소1
	 */
	public void setHomeAddr1  ( String homeAddr1  ) { this.homeAddr1   = homeAddr1;	  }
	/**
	 *  집주소2를 설정한다
	 * @param homeAddr2 집주소2
	 */
	public void setHomeAddr2  ( String homeAddr2  ) { this.homeAddr2   = homeAddr2;	  }
	/**
	 *  사무실우편번호를 설정한다.
	 * @param offcZip 사무실우편번호
	 */
	public void setOffcZip    ( String offcZip	  ) { this.offcZip	   = offcZip;	  }
	/**
	 *  사무실 주소1을 설정한다
	 * @param offcAddr1 사무실 주소1
	 */
	public void setOffcAddr1  ( String offcAddr1  ) { this.offcAddr1   = offcAddr1;	  }
	/**
	 *  사무실 주소2을 설정한다
	 * @param offcAddr2 사무실 주소2
	 */
	public void setOffcAddr2  ( String offcAddr2  ) { this.offcAddr2   = offcAddr2;	  }
	/**
	 *  사용자 아이콘을 설정한다
	 * @param userIcon 사용자 아이콘
	 */
	public void setUserIcon   ( String userIcon	  ) { this.userIcon	   = userIcon;	  }
	/**
	 * 사용자정보1을 설정한다.
	 * @param userInfo01 사용자정보1
	 */
	public void setUserInfo01 ( String   userInfo01 ) { this.userInfo01  = userInfo01;  }
	/**
	 * 사용자정보2을 설정한다.
	 * @param userInfo02 사용자정보2
	 */
	public void setUserInfo02 ( String   userInfo02 ) { this.userInfo02  = userInfo02;  }
	
	/**
	 * 사용자정보3을 설정한다.
	 * @param userInfo03 사용자정보3
	 */
	public void setUserInfo03 ( String   userInfo03 ) { this.userInfo03  = userInfo03;  }
	/**
	 * 사용자정보4를 설정한다.
	 * @param userInfo04 사용자정보4
	 */
	public void setUserInfo04 ( String userInfo04 ) { this.userInfo04  = userInfo04;  }
	/**
	 * 사용자정보5를 설정한다.
	 * @param userInfo05 사용자정보5
	 */
	public void setUserInfo05 ( String userInfo05 ) { this.userInfo05  = userInfo05;  }
	/**
	 * 사용자정보6을 설정한다.
	 * @param userInfo06 사용자정보6
	 */
	public void setUserInfo06 ( String userInfo06 ) { this.userInfo06  = userInfo06;  }
	/**
	 * 사용자정보7을 설정한다.
	 * @param userInfo07 사용자정보7
	 */
	public void setUserInfo07 ( String userInfo07 ) { this.userInfo07  = userInfo07;  }
	/**
	 * 사용자정보8을 설정한다.
	 * @param userInfo08 사용자정보8
	 */
	public void setUserInfo08 ( String userInfo08 ) { this.userInfo08  = userInfo08;  }
	/**
	 * 사용자정보9를 설정한다.
	 * @param userInfo09 사용자정보9
	 */
	public void setUserInfo09 ( String userInfo09 ) { this.userInfo09  = userInfo09;  }
	/**
	 * 사용자정보10을 설정한다.
	 * @param userInfo10 사용자정보10
	 */
	public void setUserInfo10 ( String userInfo10 ) { this.userInfo10  = userInfo10;  }
	/**
	 *  마지막로그인시간을 설정한다
	 * @param lastLogon 마지막로그인시간
	 */
	public void setLastLogon  ( Timestamp lastLogon){ this.lastLogon   = lastLogon;	  }
	/**
	 *  마지막로그인IP를 설정한다.
	 * @param lastIp 마지막로그인IP
	 */
	public void setLastIp     ( String lastIp	  ) { this.lastIp	   = lastIp;	  }
	/**
	 *  등록일시를 설정한다
	 * @param regDatim 등록일시
	 */
	public void setRegDatim   ( Timestamp regDatim) { this.regDatim	   = regDatim;	  }
	/**
	 *  수정자ID를 설정한다.
	 * @param updUserId 수정자ID
	 */
	public void setUpdUserId  ( String updUserId  ) { this.updUserId   = updUserId;	  }
	/**
	 *  수정일시를 설정한다
	 * @param updDatim 수정일시
	 */
	public void setUpdDatim   ( Timestamp updDatim) { this.updDatim	   = updDatim;	  }
	/**
	 * Page1stFlag를 설정한다.
	 * @param page1stFlag Page1stFlag
	 */
	public void setPage1stFlag( String page1stFlag) { this.page1stFlag = page1stFlag; }
	/**
	 *  Page1stUrl를 설정한다
	 * @param page1stUrl Page1stUrl
	 */
	public void setPage1stUrl ( String page1stUrl ) { this.page1stUrl  = page1stUrl;  }
	/**
	 *  사진경로를 설정한다
	 * @param picPath 사진경로
	 */
	public void setPicPath    ( String picPath	  ) { this.picPath	   = picPath;	  }
	/**
	 * 소개를 설정한다
	 * @param intro 소개
	 */
	public void setIntro      ( String intro	  ) { this.intro	   = intro;		  }
	
	
	/**
	 * 동의여부를 리턴한다
	 * @return 동의여부
	 */
	public boolean getIsAgree() {
		return isAgree;
	}
	/**
	 * 동의여부를 설정한다
	 * @param isAgree 동의여부
	 */
	public void setIsAgree(boolean isAgree) {
		this.isAgree = isAgree;
	}
	public int authFailures;
	
	/**
	 * 인증오류횟수를 설정한다
	 * @param authFailures 인증오류횟수
	 */
	public void setAuthFailures(int authFailures){
		this.authFailures = authFailures;
	}
	
	/**
	 * 인증오류횟수를 리턴한다
	 * @return 인증오류횟수
	 */
	public int getAuthFailures(){
		return this.authFailures;
	}
	
	public int updateRequired;
	/**
	 * 갱신필요여부를 설정한다
	 * @param updateRequired 갱신필요여부
	 */
	public void setUpdateRequired(int updateRequired){
		this.updateRequired = updateRequired;
	}
	
	/**
	 * 갱신필요여부를 리턴한다
	 * @return 갱신필요여부
	 */
	public int getUpdateRequired(){
		return updateRequired;
	}
	
	public int isEnabled;
	
	/**
	 * 사용여부를 설정한다
	 * @param isEnabled 사용여부
	 */
	public void setIsEnabled(int isEnabled){
		this.isEnabled = isEnabled;
	}
	
	/**
	 * 사용여부를 리턴한다
	 * @return 사용여부
	 */
	public int getIsEnabled(){
		return this.isEnabled;
	}
	
	public String modifyMenu;
	
	/**
	 * 메뉴수정여부를 설정한다
	 * @param modifyMenu 메뉴수정여부
	 */
	public void setModifyMenu(String modifyMenu){
		this.modifyMenu = modifyMenu;
	}
	
	/**
	 * 메뉴수정여부
	 * @return 메뉴수정여부
	 */
	public String getModifyMenu(){
		return this.modifyMenu;
	}
	public int authMethod;
	/**
	 * 인증방법을 설정한다
	 * @param authMethod 인증방법
	 */
	public void setAuthMethod(int authMethod){
		this.authMethod = authMethod;
	}
	
	/**
	 * 인증방법을 리턴한다
	 * @return 인증방법
	 */
	public int getAuthMethod(){
		return this.authMethod;
	}
	public String defaultPage;
	/**
	 * 기본페이지를 설정한다
	 * @param defaultPage 기본페이지
	 */
	public void setDefaultPage(String defaultPage){
		this.defaultPage = defaultPage;
	}
	/**
	 * 기본페이지를 설정한다
	 * @return 기본페이지
	 */
	public String getDefaultPage(){
		return defaultPage;
	}
	
	public String subPage;
	/**
	 * 서브페이지를 설정한다
	 * @param subPage 서브페이지
	 */
	public void setSubPage(String subPage){
		this.subPage = subPage;
	}
	/**
	 * 서브페이지를 설정한다
	 * @return 서브페이지
	 */
	public String getSubPage(){
		return subPage;
	}
	
	public String theme;
	/**
	 * 테마를 설정한다
	 * @param theme 테마
	 */
	public void setTheme(String theme){
		this.theme = theme;
	}
	
	/**
	 * 테마를 리턴한다.
	 * @return 테마
	 */
	public String getTheme(){
		return this.theme;
	}
	
	public String principalName;
	/**
	 * 주체명을 설정한다
	 * @param principalName 주체명
	 */
	public void setPrincipalName(String principalName){
		this.principalName = principalName;
	}
	
	/**
	 * 주체명을 리턴한다
	 * @return 주체명
	 */
	public String getPrincipalName(){
		return this.principalName;
	}
	
	@SuppressWarnings("unchecked")
	/** 맵으로 변환한다
	 * @return 맵
	 */
	public Map toMap(){
		Map model = new EnviewMap();
		
//		System.out.println("PUT TO MAP [domain_id]= " + this.getDomainId());
		model.put("domain_id", new BigDecimal(this.getDomainId()));
		model.put("domainId", Integer.toString(this.getDomainId()));
		model.put("domainNm", this.getDomainNm());
		//findUser
//		System.out.println("PUT TO MAP [principal_id]= " + this.getPrincipalId());
//		System.out.println("PUT TO MAP [column_value]= " + this.getColumnValue());
//		System.out.println("PUT TO MAP [update_required]= " + this.getUpdateRequired());
//		System.out.println("PUT TO MAP [auth_failures]= " + this.getAuthFailures());
//		System.out.println("PUT TO MAP [is_enabled]= " + this.getIsEnabled());
		
		model.put("principal_id", new BigDecimal(this.getPrincipalId()));
		model.put("column_value", this.getColumnValue());
		model.put("update_required", new BigDecimal(this.getUpdateRequired()));
		model.put("auth_failures", new BigDecimal(this.getAuthFailures()));
		model.put("is_enabled", new BigDecimal(this.getIsEnabled()));
		
		//detail
//		System.out.println("PUT TO MAP [user_id]= " + this.getUserId());
//		System.out.println("PUT TO MAP [reg_no]= " + this.getRegNo());
//		System.out.println("PUT TO MAP [org_cd]= " + this.getOrgCd());
//		System.out.println("PUT TO MAP [emp_no]= " + this.getEmpNo());
//		System.out.println("PUT TO MAP [kind_cd]= " + this.getKindCd());
//		System.out.println("PUT TO MAP [type_cd]= " + this.getTypeCd());
//		System.out.println("PUT TO MAP [nm_kor]= " + this.getNmKor());
//		System.out.println("PUT TO MAP [nm_eng]= " + this.getNmEng());
//		System.out.println("PUT TO MAP [mobile_tel]= " + this.getMobileTel());
//		System.out.println("PUT TO MAP [email_addr]= " + this.getEmailAddr());
//		System.out.println("PUT TO MAP [user_info04]= " + this.getUserInfo04());
//		System.out.println("PUT TO MAP [lang_knd]= " + this.getLangKnd());
//		System.out.println("PUT TO MAP [reg_datim]= " + this.getRegDatim());
//		System.out.println("PUT TO MAP [home_zip]= " + this.getHomeZip());
//		System.out.println("PUT TO MAP [home_addr1]= " + this.getHomeAddr1());
//		System.out.println("PUT TO MAP [home_addr2]= " + this.getHomeAddr2());
//		System.out.println("PUT TO MAP [last_logon]= " + this.getLastLogon());
//		System.out.println("PUT TO MAP [modify_menu]= " + this.getModifyMenu());
//		System.out.println("PUT TO MAP [principal_name]= " + this.getPrincipalName());
//		System.out.println("PUT TO MAP [auth_method]= " + this.getAuthMethod());
//		System.out.println("PUT TO MAP [default_page]= " + this.getDefaultPage());
//		System.out.println("PUT TO MAP [theme]= " + this.getTheme());
		
		
		
		model.put("user_id", this.getUserId());
		model.put("reg_no", this.getRegNo());
		model.put("org_cd", this.getOrgCd());
		model.put("emp_no", this.getEmpNo());
		model.put("kind_cd", this.getKindCd());
		model.put("type_cd", this.getTypeCd());
		model.put("nm_kor", this.getNmKor());
		model.put("nm_eng", this.getNmEng());
		model.put("nm_nic", this.getNmNic());
		
		model.put("mobile_tel", this.getMobileTel());
		model.put("email_addr", this.getEmailAddr());
		
		model.put("lang_knd", this.getLangKnd());
		model.put("reg_datim", this.getRegDatim());
		model.put("home_zip", this.getHomeZip());
		model.put("home_addr1", this.getHomeAddr1());
		model.put("home_addr2", this.getHomeAddr2());
		model.put("last_logon", this.getLastLogon());
		model.put("last_ip", this.getLastIp());
		model.put("modify_menu", this.getModifyMenu());
		model.put("principal_name", this.getPrincipalName());
		model.put("auth_method", this.getAuthMethod() + "");
		model.put("default_page", this.getDefaultPage());
		model.put("sub_page", this.getSubPage());
		model.put("theme", this.getTheme());
		
		model.put("userInfo01",this.getUserInfo01());
		model.put("userInfo02",this.getUserInfo02());
		model.put("userInfo03",this.getUserInfo03());
		model.put("userInfo04",this.getUserInfo04());
		model.put("userInfo05",this.getUserInfo05());
		model.put("userInfo06",this.getUserInfo06());
		model.put("userInfo07",this.getUserInfo07());
		model.put("userInfo08",this.getUserInfo08());
		model.put("userInfo09",this.getUserInfo09());
		model.put("userInfo10",this.getUserInfo10());

		model.put("principalInfo01",this.getPrincipalInfo01());
		model.put("principalInfo02",this.getPrincipalInfo02());
		model.put("principalInfo03",this.getPrincipalInfo03());
		
		model.put("blockAbord", this.getPrincipalInfo01());
		
		model.put("userName", this.getNmKor());
		model.put("nickName", this.getNmNic());
		model.put("orgName", this.getUserInfo04());
		model.put("posiName", this.getUserInfo10());
		
		return model;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getPrincipalInfo01() {
		return principalInfo01;
	}
	public void setPrincipalInfo01(String principalInfo01) {
		this.principalInfo01 = principalInfo01;
	}
	public String getPrincipalInfo02() {
		return principalInfo02;
	}
	public void setPrincipalInfo02(String principalInfo02) {
		this.principalInfo02 = principalInfo02;
	}
	public String getPrincipalInfo03() {
		return principalInfo03;
	}
	public void setPrincipalInfo03(String principalInfo03) {
		this.principalInfo03 = principalInfo03;
	}
	
	public String getBlockAbroad() {
		return getPrincipalInfo01();
	}
}
