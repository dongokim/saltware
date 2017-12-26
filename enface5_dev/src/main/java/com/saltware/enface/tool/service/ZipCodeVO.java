package com.saltware.enface.tool.service;

import java.io.Serializable;

/**
 * http://www.postman.pe.kr/zipcode/ 에서 제공하는 
 * 우정사업본부 제공 우편번호부 원본 Type4 파일을 기준으로 정의하였음.
 * link: http://www.postman.pe.kr/zipcode/zipcode_20101119(4).zip
 * @author secrain
 *
 */

public class ZipCodeVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//primary key;
	private String zipCode;	//우편번호, 필드길이 7
	private String seq;		//데이터 순서	5
	private String langKnd;
	
	//value 총 7개, 
	private String sido;	//특별시, 광역시, 도	6, 서울,경기,전북,전남,충북,충남,경북,경남,제주 등으로 표시
	private String gugun;	//시,군,구	17
	private String dong;	//읍,면,동	26
	private String ri;		//리명	18
	private String bldg;	//건물명 40
	private String stBunji;//시작번지	9
	private String edBunji;//끝번지	9
	
	private String address; //번지 포함
	
	private String simpleAddress; // 번지 제외
	/**
	 * 총 우편번호 데이터 수 51,360개
	 */
	
	public ZipCodeVO() {
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getLangKnd() {
		return langKnd;
	}

	public void setLangKnd(String langKnd) {
		this.langKnd = langKnd;
	}

	public String getSido() {
		return sido;
	}

	public void setSido(String sido) {
		this.sido = sido;
	}

	public String getGugun() {
		return gugun;
	}

	public void setGugun(String gugun) {
		this.gugun = gugun;
	}

	public String getDong() {
		return dong;
	}

	public void setDong(String dong) {
		this.dong = dong;
	}

	public String getRi() {
		return ri;
	}

	public void setRi(String ri) {
		this.ri = ri;
	}

	public String getBldg() {
		return bldg;
	}

	public void setBldg(String bldg) {
		this.bldg = bldg;
	}

	public String getStBunji() {
		return stBunji;
	}

	public void setStBunji(String stBunji) {
		this.stBunji = stBunji;
	}

	public String getEdBunji() {
		return edBunji;
	}

	public void setEdBunji(String edBunji) {
		this.edBunji = edBunji;
	}

	//번지 포함 주소 리턴
	public String getAddress() {
		if(ri == null){
			if(bldg == null){
				if(stBunji == null){
					address = sido + " " + gugun + " " + dong;
				}
				else if(stBunji != null){
					if(edBunji == null){
						address = sido + " " + gugun + " " + dong + " " + stBunji;
					}
					else if(edBunji != null){
						address = sido + " " + gugun + " " + dong + " " + stBunji + " ~ " + edBunji;
					}
				}
			}
			else if(bldg != null){
				if(stBunji == null){
					address = sido + " " + gugun + " " + dong + " " + bldg;
				}
				else if(stBunji != null){
					if(edBunji == null){
						address = sido + " " + gugun + " " + dong + " " + bldg + " " + stBunji;
					}
					else if(edBunji != null){
						address = sido + " " + gugun + " " + dong + " " + bldg + " " + stBunji + " ~ " + edBunji;
					}
				}
			}
		}
		else if(ri != null){
			if(bldg == null){
				if(stBunji == null){
					address = sido + " " + gugun + " " + dong + " " + ri;
				}
				else if(stBunji != null){
					if(edBunji == null){
						address = sido + " " + gugun + " " + dong + " " + ri + " " + stBunji;
					}
					else if(edBunji != null){
						address = sido + " " + gugun + " " + dong + " " + ri + " " + stBunji + " ~ " + edBunji;
					}
				}
			}
			else if(bldg != null){
				if(stBunji == null){
					address = sido + " " + gugun + " " + dong + " " + ri + " " + bldg;
				}
				else if(stBunji != null){
					if(edBunji == null){
						address = sido + " " + gugun + " " + dong + " " + ri + " " + bldg + " " + stBunji;
					}
					else if(edBunji != null){
						address = sido + " " + gugun + " " + dong + " " + ri + " " + bldg + " " + stBunji + " ~ " + edBunji;
					}
				}
			}
		}
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getSimpleAddress() {
		if(ri == null){
			if(bldg == null){
				simpleAddress = sido + " " + gugun + " " + dong;
			}
			else if(bldg != null){
				simpleAddress = sido + " " + gugun + " " + dong + " " + bldg;
			}
		}
		else if(ri != null){
			if(bldg == null){
				simpleAddress = sido + " " + gugun + " " + dong + " " + ri;
			}
			else if(bldg != null){
				simpleAddress = sido + " " + gugun + " " + dong + " " + ri + " " + bldg;
			}
		}
		return simpleAddress;
	}

	public void setSimpleAddress(String simpleAddress) {
		this.simpleAddress = simpleAddress;
	}

	public String toString(){
		return getAddress();
	}
}
