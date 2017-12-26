package com.saltware.enface.enboard.vo;

import java.io.Serializable;
import java.util.List;

/**  
 * 개요 : 게시판 Value Object
 * @date: 2015. 9. 17.
 * @author: 나상모(솔트웨어)
 * @history : 
 *	-----------------------------------------------------------------------
 *	변경일      작성자              변경내용
 *	----------- ------------------- ---------------------------------------
 *  2015. 9. 17.  나상모(솔트웨어)    최초작성
 *	-----------------------------------------------------------------------
 */
public class BoardManageVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * ROW 번호
	 */
	int rnum = 0;
	
	/**
	 * 페이징 전체건수
	 */
	private int totCnt;
	/**
	 * 페이징 시작ROW
	 */
	private int startRow = 0;
	/**
	 * 페이징 끝 ROW
	 */
	private int endRow = 10;

	/**
	 * 검색타입
	 */
	private String searchType;
	
	/**
	 * 검색값
	 */
	private String searchValue;
	
	/*------------------------------------------*/
	
	/**
	 * 언어코드 
	 */
	private String langKnd;
	
	/**
	 * 게시판 ID
	 */
	private String boardId;
	
	/**
	 * 게시판명
	 */
	private String boardNm;
	
	/**
	 * 카테고리ID
	 */
	private String cateId;

	/**
	 * 카테고리ID 1
	 */
	private String cateId1;
	
	/**
	 * 카테고리ID 2
	 */
	private String cateId2;
	
	/**
	 * 카테고리명 1
	 */
	private String cateNm1;
	
	
	/**
	 * 카테고리명 2
	 */
	private String cateNm2;

	/**
	 * 게시물건수
	 */
	private int bltnCnt;
	
	/**
	 * 조직ID
	 */
	private int orgId;

	/**
	 * 사용자 ID
	 */
	private String userId;
	
	/**
	 * 조직명
	 */
	private String orgNm;
	
	/**
	 * 사용자 권한ID 리스트
	 */
	private List userAuthIdList;
	
	/**
	 * 한글 게시판명
	 */
	private String boardNmKo;
	/**
	 * 한글 게시판설명
	 */
	private String boardDescKo;
	
	/**
	 * 한글 게시판유의사항
	 */
	private String boardNoticeKo;
	
	/**
	 * 영문 게시판명
	 */
	private String boardNmEn;
	/**
	 * 영문 게시판설명
	 */
	private String boardDescEn;
	
	/**
	 * 영문 게시판유의사항
	 */
	private String boardNoticeEn;
	
	/**
	 * 게시판 아이콘
	 */
	private String boardIcon;
	
	/**
	 * 게시판 스킨
	 */
	private String boardSkin;

	/**
	 * 게시판 유형
	 */
	private String boardType;
	
	/**
	 * 게시판 순서
	 */
	private int boardOrder;

	/**
	 * 게시판 사용여부
	 */
	private String boardActive="N";
	
	/**
	 * 카테고리사용 사용여부
	 */
	private String cateYn="N";

	/**
	 * 카테고리 표시 여부
	 */
	private String ttlCateYn="Y";
	
	/**
	 * 댓글 사용여부
	 */
	private String memoYn="N";
	
	/**
	 * 댓댓글 사용여부
	 */
	private String memoReplyYn="N";
	
	/**
	 * 목록 댓글 표시 여부
	 */
	private String ttlMemoYn="N";
	
	/**
	 * 언어이중화 사용여부
	 */
	private String bilingualYn="N";
	
	/**
	 * 기능설정
	 */
	private String funcYns;
	
	/**
	 * 연계설정
	 */
	private String linkYns;
	
	/**
	 * 타겟윈도우
	 */
	private String targetWin;
	
	/**
	 * 타겟 URL
	 */
	private String targetUrl;
	
	
	/**
	 * 선택된아이템
	 */
	private String selectedItems;
	
	public int getRnum() {
		return rnum;
	}

	public void setRnum(int rnum) {
		this.rnum = rnum;
	}

	public int getTotCnt() {
		return totCnt;
	}

	public void setTotCnt(int totCnt) {
		this.totCnt = totCnt;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public String getLangKnd() {
		return langKnd;
	}

	public void setLangKnd(String langKnd) {
		this.langKnd = langKnd;
	}

	public String getBoardId() {
		return boardId;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public String getBoardNm() {
		return boardNm;
	}

	public void setBoardNm(String boardNm) {
		this.boardNm = boardNm;
	}

	public String getCateId1() {
		return cateId1;
	}

	public void setCateId1(String cateId1) {
		this.cateId1 = cateId1;
	}

	public String getCateId2() {
		return cateId2;
	}

	public void setCateId2(String cateId2) {
		this.cateId2 = cateId2;
	}

	public String getCateNm1() {
		return cateNm1;
	}

	public void setCateNm1(String cateNm1) {
		this.cateNm1 = cateNm1;
	}

	public String getCateNm2() {
		return cateNm2;
	}

	public void setCateNm2(String cateNm2) {
		this.cateNm2 = cateNm2;
	}

	public int getBltnCnt() {
		return bltnCnt;
	}

	public void setBltnCnt(int bltnCnt) {
		this.bltnCnt = bltnCnt;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getOrgNm() {
		return orgNm;
	}

	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List getUserAuthIdList() {
		return userAuthIdList;
	}

	public void setUserAuthIdList(List userAuthIdList) {
		this.userAuthIdList = userAuthIdList;
	}

	public String getBoardNmKo() {
		return boardNmKo;
	}

	public void setBoardNmKo(String boardNmKo) {
		this.boardNmKo = boardNmKo;
	}

	public String getBoardDescKo() {
		return boardDescKo;
	}

	public void setBoardDescKo(String boardDescKo) {
		this.boardDescKo = boardDescKo;
	}

	public String getBoardNoticeKo() {
		return boardNoticeKo;
	}

	public void setBoardNoticeKo(String boardNoticeKo) {
		this.boardNoticeKo = boardNoticeKo;
	}

	public String getBoardNmEn() {
		return boardNmEn;
	}

	public void setBoardNmEn(String boardNmEn) {
		this.boardNmEn = boardNmEn;
	}

	public String getBoardDescEn() {
		return boardDescEn;
	}

	public void setBoardDescEn(String boardDescEn) {
		this.boardDescEn = boardDescEn;
	}

	public String getBoardNoticeEn() {
		return boardNoticeEn;
	}

	public void setBoardNoticeEn(String boardNoticeEn) {
		this.boardNoticeEn = boardNoticeEn;
	}

	public String getBoardIcon() {
		return boardIcon;
	}

	public void setBoardIcon(String boardIcon) {
		this.boardIcon = boardIcon;
	}

	public String getBoardType() {
		return boardType;
	}

	public void setBoardType(String boardType) {
		this.boardType = boardType;
	}

	public String getTargetWin() {
		return targetWin;
	}

	public void setTargetWin(String targetWin) {
		this.targetWin = targetWin;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetURL) {
		this.targetUrl = targetURL;
	}

	public String getBoardSkin() {
		return boardSkin;
	}

	public void setBoardSkin(String boardSkin) {
		this.boardSkin = boardSkin;
	}

	public int getBoardOrder() {
		return boardOrder;
	}

	public void setBoardOrder(int boardOrder) {
		this.boardOrder = boardOrder;
	}

	public String getBoardActive() {
		return boardActive;
	}

	public void setBoardActive(String boardActive) {
		this.boardActive = boardActive;
	}

	public String getCateYn() {
		return cateYn;
	}

	public void setCateYn(String cateYn) {
		this.cateYn = cateYn;
	}

	public String getFuncYns() {
		return funcYns;
	}

	public void setFuncYns(String funcYns) {
		this.funcYns = funcYns;
	}

	public String getCateId() {
		return cateId;
	}

	public void setCateId(String cateId) {
		this.cateId = cateId;
	}

	public String getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(String selectedItems) {
		this.selectedItems = selectedItems;
	}

	public String getTtlCateYn() {
		return ttlCateYn;
	}

	public void setTtlCateYn(String ttlCateYn) {
		this.ttlCateYn = ttlCateYn;
	}

	public String getLinkYns() {
		return linkYns;
	}

	public void setLinkYns(String linkYns) {
		this.linkYns = linkYns;
	}

	public String getMemoYn() {
		return memoYn;
	}

	public void setMemoYn(String memoYn) {
		this.memoYn = memoYn;
	}

	public String getMemoReplyYn() {
		return memoReplyYn;
	}

	public void setMemoReplyYn(String memoReplyYn) {
		this.memoReplyYn = memoReplyYn;
	}

	public String getTtlMemoYn() {
		return ttlMemoYn;
	}

	public void setTtlMemoYn(String ttlMemoYn) {
		this.ttlMemoYn = ttlMemoYn;
	}

	public String getBilingualYn() {
		return bilingualYn;
	}

	public void setBilingualYn(String bilingualYn) {
		this.bilingualYn = bilingualYn;
	}
}
