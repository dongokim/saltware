/**
 * Copyright (c) 2010 Saltware, Inc.
 * 
 * http://www.saltware.co.kr
 * 
 * Kolon Science Valley Bldg 2th. 901, Guro-dong 811, Guro-gu,
 * Seoul, 152-878, South Korea.
 * All Rights Reserved.
 * 
 * This software is the Java based Enterprise Portal of Saltware, Inc.
 * Making any change or distributing this without permission from us is out of law.
 */
package com.saltware.enface.common;

/**
 * enview 코드 테이블 객체.<br/>
 * 테이블명 : CODEBASE
 * 
 * @version 3.2.2
 */
public class CodeVo extends BaseVo {

	private String systemCode;
    private String codeId;
    private String code;
    private String codeTag1;
    private String codeTag2;
    private String codeName;
    private String codeName2;
    private String remark;

    /**
     * 시스템 코드를  리턴한다.
     * @return 시스템 코드
     */
    public String getSystemCode() { return systemCode; }

    /**
     * 코드ID를 리턴한다.
     * @return 코드ID
     */
    public String getCodeId    () { return codeId;     }
    
    /**
     * 코드를 리턴한다.
     * @return 코드
     */
    public String getCode      () { return code;       }
    
    /**
     * 코드태그1을 리턴한다
     * @return 코드태그1
     */
    public String getCodeTag1  () { return codeTag1;   }
    
    /**
     * 코드태그2를 리턴한다
     * @return 코드태그2
     */
    public String getCodeTag2  () { return codeTag2;   }
    
    /**
     * 코드명을 리턴한다.
     * @return - 코드명
     */
    public String getCodeName  () { return codeName;   }
    
    /**
     * 코드명2를 리턴한다.
     * @return - 코드명2
     */
    public String getCodeName2 () { return codeName2;  }
    
    /**
     * 비고를 리턴한다.
     * @return 비고
     */
    public String getRemark    () { return remark;     }

    /**
     * 시스템코드를 설정한다.
     * @param systemCode 시스템코드
     */
    public void setSystemCode(String systemCode) { this.systemCode = systemCode; }
    
    /**
     * 코드ID를 설정하낟.
     * @param codeId 코드ID
     */
    public void setCodeId    (String codeId) { this.codeId     = codeId; }
    
    /**
     * 코드를 설정한다.
     * @param code 코드
     */
    public void setCode      (String code) { this.code       = code; }
    
    /**
     * 코드태그1을 설정한다.
     * @param codeTag1 코드태그1
     */
    public void setCodeTag1  (String codeTag1) { this.codeTag1   = codeTag1; }
    
    /**
     * 코드태그2를 설정한다.
     * @param codeTag2
     */
    public void setCodeTag2  (String codeTag2) { this.codeTag2   = codeTag2; }
    
    /**
     * 코드명을 설정한다.
     * @param codeName - 코드명
     */
    public void setCodeName  (String codeName) { this.codeName   = codeName; }
    
    /**
     * 코드명2를 설정한다.
     * @param codeName2
     */
    public void setCodeName2 (String codeName2) { this.codeName2  = codeName2; }
    
    /**
     * 비고를 설정한다.
     * @param remark
     */
    public void setRemark    (String remark) { this.remark     = remark; }
}
