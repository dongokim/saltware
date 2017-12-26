package com.saltware.enboard.dao;

import com.saltware.enboard.cache.CacheMngr;
import com.saltware.enboard.exception.BaseException;
import com.saltware.enboard.form.AdminAuxilForm;
import com.saltware.enboard.form.AdminBoardForm;
import com.saltware.enboard.form.AdminCateForm;
import com.saltware.enboard.form.AdminPollForm;
import com.saltware.enboard.integrate.BltnExtnMapper;
import com.saltware.enboard.security.SecurityMngr;
import com.saltware.enboard.util.FormatUtil;
import com.saltware.enboard.util.ValidateUtil;
import com.saltware.enboard.vo.BltnPollEvalVO;
import com.saltware.enboard.vo.BltnPollVO;
import com.saltware.enboard.vo.BoardBaseVO;
import com.saltware.enboard.vo.BoardLangVO;
import com.saltware.enboard.vo.BoardSnntVO;
import com.saltware.enboard.vo.BoardTblVO;
import com.saltware.enboard.vo.BoardVO;
import com.saltware.enboard.vo.BulletinVO;
import com.saltware.enboard.vo.CateLangVO;
import com.saltware.enboard.vo.CatebaseVO;
import com.saltware.enboard.vo.CodebaseVO;
import com.saltware.enboard.vo.ParamMap;
import com.saltware.enboard.vo.PollPropVO;
import com.saltware.enview.Enview;
import com.saltware.enview.administration.PortalConfiguration;
import com.saltware.enview.components.dao.ConnectionContext;
import com.saltware.enview.components.dao.DAOFactory;
import com.saltware.enview.util.StringUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractAdminDAO extends BaseDAO
  implements AdminDAO
{
  private Log logger = LogFactory.getLog(getClass());

  public CatebaseVO getCatebase(String cateId, String langKnd, ConnectionContext connCtxt) 
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDao.getCatebase()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;
    StringBuffer sb = new StringBuffer();

    CatebaseVO cbVO = null;
    try {
      conn = connCtxt.getConnection();

      sb.append("SELECT cate_id, domain_id, cate_level, parent_cate_id, cate_order, cate_kind, upd_user_id, upd_datim");
      sb.append(" FROM catebase WHERE cate_id=?");
      pstmt = conn.prepareStatement(sb.toString());
      pstmt.setLong(1, Long.parseLong(cateId));
      result = pstmt.executeQuery();

      int idx = 0;
      if (result.next()) {
        cbVO = new CatebaseVO();
        idx = 0;
        idx++; cbVO.setCateId(result.getString(idx));
        idx++; cbVO.setDomainId(result.getLong(idx));
        idx++; cbVO.setCateLevel(result.getInt(idx));
        idx++; cbVO.setParentCateId(result.getString(idx));
        idx++; cbVO.setCateOrder(result.getInt(idx));
        idx++; cbVO.setCateKind(result.getString(idx));
        idx++; cbVO.setUpdUserId(result.getString(idx));
        idx++; cbVO.setUpdDatim(result.getTimestamp(idx));
      }
      result.close();
      pstmt.close();

      List clList = new ArrayList();
      CateLangVO clVO = null;
      sb.setLength(0);
      sb.append("SELECT cate_id, lang_knd, cate_nm");
      sb.append(" FROM catebase_lang WHERE cate_id=?");
      pstmt = conn.prepareStatement(sb.toString());
      pstmt.setLong(1, Long.parseLong(cateId));
      result = pstmt.executeQuery();

      while (result.next()) {
        clVO = new CateLangVO();
        idx = 0;
        idx++; clVO.setCateId(result.getString(idx));
        idx++; clVO.setLangKnd(result.getString(idx));
        idx++; clVO.setCateNm(result.getString(idx));
        clList.add(clVO);
      }
      result.close();
      pstmt.close();

      cbVO.setLangKnd(langKnd);
      cbVO.setCateLangList(clList);

      sb.setLength(0);
      sb.append("SELECT MAX(cate_order) FROM catebase WHERE parent_cate_id=?");
      pstmt = conn.prepareStatement(sb.toString());

      pstmt.setLong(1, Long.parseLong(cbVO.getParentCateId()));
      result = pstmt.executeQuery();
      if (result.next()) {
        cbVO.setNextOrder(result.getInt(1) + 1);
      }
      result.close();
      pstmt.close();

      sb.setLength(0);
      sb.append("SELECT MAX(cate_order) FROM catebase WHERE parent_cate_id=?");
      pstmt = conn.prepareStatement(sb.toString());
      pstmt.setLong(1, Long.parseLong(cbVO.getCateId()));
      result = pstmt.executeQuery();
      if (result.next())
        cbVO.setNextChildOrder(result.getInt(1) + 1);
    }
    catch (Exception e) {
      this.logger.error("AdminDAO.getCatebase()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.getCatebase()");
    return cbVO;
  }

  public List getChildCatebase(String parentCateId, boolean onlyId, String langKnd, ConnectionContext connCtxt)
    throws BaseException
  {
    return getChildCatebase(parentCateId, onlyId, langKnd, 0L, connCtxt);
  }

  public List getChildCatebase(String parentCateId, boolean onlyId, String langKnd, long domainId, ConnectionContext connCtxt)
    throws BaseException
  {
    Connection conn = null;
    PreparedStatement pstmt = null; PreparedStatement langPstmt = null;
    ResultSet rslt = null; ResultSet langRslt = null;
    ArrayList cateList = new ArrayList();
    StringBuffer sb = new StringBuffer();

    if (onlyId) {
      sb.append("SELECT cate_id FROM catebase WHERE parent_cate_id=?");
      if (domainId != 0L)
        sb.append(" AND domain_id IN (0,?)");
      sb.append(" ORDER BY cate_order");
    } else {
      sb.append("SELECT a.cate_id, a.domain_id, a.cate_level, a.parent_cate_id, a.cate_order, a.cate_kind");
      sb.append(", (SELECT COUNT(c.cate_id) FROM catebase c WHERE c.parent_cate_id=a.cate_id) child_cnt");
      sb.append(" FROM catebase a");
      sb.append(" WHERE a.parent_cate_id=?");
      if (domainId != 0L)
        sb.append(" AND domain_id IN (0,?)");
      sb.append(" ORDER BY a.cate_order");
    }
    try
    {
      conn = connCtxt.getConnection();

      langPstmt = conn.prepareStatement("SELECT cate_id, lang_knd, cate_nm FROM catebase_lang WHERE cate_id=?");

      pstmt = conn.prepareStatement(sb.toString());
      pstmt.setLong(1, Long.parseLong(parentCateId));
      if (domainId != 0L)
        pstmt.setLong(2, domainId);
      rslt = pstmt.executeQuery();

      int idx = 0;
      CatebaseVO brdCateVO = null;
      while (rslt.next()) {
        if (onlyId) {
          idx++; cateList.add(rslt.getString(idx));
        } else {
          brdCateVO = new CatebaseVO();
          idx++; brdCateVO.setCateId(rslt.getString(idx));
          idx++; brdCateVO.setDomainId(rslt.getLong(idx));
          idx++; brdCateVO.setCateLevel(rslt.getInt(idx));
          idx++; brdCateVO.setParentCateId(rslt.getString(idx));
          idx++; brdCateVO.setCateOrder(rslt.getInt(idx));
          idx++; brdCateVO.setCateKind(rslt.getString(idx));
          idx++; brdCateVO.setChildCnt(rslt.getInt(idx));

          langPstmt.setLong(1, Long.parseLong(brdCateVO.getCateId()));
          langRslt = langPstmt.executeQuery();
          List clList = new ArrayList();
          while (langRslt.next()) {
            CateLangVO clVO = new CateLangVO();
            clVO.setCateId(langRslt.getString("cate_id"));
            clVO.setLangKnd(langRslt.getString("lang_knd"));
            clVO.setCateNm(langRslt.getString("cate_nm"));
            clList.add(clVO);
          }
          langRslt.close();

          brdCateVO.setLangKnd(langKnd);
          brdCateVO.setCateLangList(clList);

          cateList.add(brdCateVO);
        }
        idx = 0;
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.getChildCatebase()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(langRslt);
      close(langPstmt);
      close(rslt);
      close(pstmt);
    }

    return cateList;
  }

  public String getCateOfBoard(String boardId, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.getgetCateOfBoard()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;

    String cateId = null;
    try {
      conn = connCtxt.getConnection();

      pstmt = conn.prepareStatement("SELECT cate_id FROM cate_board WHERE board_id=?");
      pstmt.setString(1, boardId);
      result = pstmt.executeQuery();
      if (result.next()) {
        cateId = result.getString(1);
      }
      close(result);
      close(pstmt);
    } catch (Exception e) {
      this.logger.error("AdminDAO.getgetCateOfBoard()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.getgetCateOfBoard()");
    return cateId;
  }

  public List getCateBoard(AdminAuxilForm aaForm, String sysopBoardInStr, boolean onlyId, ConnectionContext connCtxt)
    throws BaseException
  {
    List boardList = null;
    AdminCateForm acForm = new AdminCateForm();
    acForm.setPageNo(aaForm.getPageNo());
    acForm.setPageSize(aaForm.getPageSize());
    acForm.setCateId(aaForm.getCateId());
    acForm.setLangKnd(aaForm.getLangKnd());

    boardList = getCateBoard(acForm, sysopBoardInStr, onlyId, connCtxt);

    aaForm.setPageNo(acForm.getPageNo());
    aaForm.setPageSize(acForm.getPageSize());
    aaForm.setTotalSize(acForm.getTotalSize());
    aaForm.setResultSize(acForm.getResultSize());

    return boardList;
  }

  public List getCateBoard(AdminCateForm acForm, String sysopBoardInStr, boolean onlyId, ConnectionContext connCtxt) throws BaseException {
    this.logger.info("BEGIN::AdminDAO.getCateBoard()");

    Connection conn = null;
    PreparedStatement pstmt = null; PreparedStatement langPstmt = null;
    ResultSet result = null; ResultSet langResult = null;
    ArrayList boardList = new ArrayList();
    StringBuffer sb = new StringBuffer();

    int srnum = 0; int ernum = 0;
    if (acForm.getPageNo() == 0)
      acForm.setPageNo(1);
    if (acForm.getPageSize() == 0)
      acForm.setPageSize(10);
    srnum = acForm.getPageNo() == 1 ? 1 : acForm.getPageNo() * acForm.getPageSize() - acForm.getPageSize() + 1;
    ernum = srnum + acForm.getPageSize() - 1;
    try
    {
      conn = connCtxt.getConnection();

      sb.append("SELECT COUNT(board_id) FROM board");
      sb.append(" WHERE board_id IN ( SELECT board_id FROM cate_board WHERE cate_id=? )");
      if (sysopBoardInStr != null) {
        sb.append(" AND board_id IN ( " + sysopBoardInStr + " )");
      }
      pstmt = conn.prepareStatement(sb.toString());
      pstmt.setLong(1, Long.parseLong(acForm.getCateId()));
      result = pstmt.executeQuery();
      if (result.next()) {
        acForm.setTotalSize(result.getInt(1));
      }
      result.close();
      pstmt.close();

      sb.setLength(0);
      if (onlyId) {
        sb.append("SELECT board_id");
        sb.append(" FROM board");
        sb.append(" WHERE board_id IN ( SELECT board_id FROM cate_board WHERE cate_id=? )");
        if (sysopBoardInStr != null) {
          sb.append(" AND a.board_id IN ( " + sysopBoardInStr + " )");
        }
        sb.append(" ORDER BY board_id");

        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setLong(1, Long.parseLong(acForm.getCateId()));
      }
      else {
        if (StringUtil.isEmpty(acForm.getSortColumn())) {
          acForm.setSortColumn("a.board_id");
        }
        if (StringUtil.isEmpty(acForm.getSortMethod())) {
          acForm.setSortMethod("ASC");
        }

        sb.append("SELECT r1.* FROM (");
        sb.append(" SELECT ROW_NUMBER() OVER ( ORDER BY ").append(acForm.getSortColumn()).append(" ").append(acForm.getSortMethod()).append(") RNUM,");
        sb.append(" a.board_id, board_rid, board_active, board_sys, owntbl_yn, owntbl_fix, board_type, merge_type, board_skin, upd_datim");
        sb.append(" FROM board a");
        sb.append(" WHERE a.board_id IN ( SELECT board_id FROM cate_board WHERE cate_id=? )");
        if (sysopBoardInStr != null) {
          sb.append(" AND a.board_id IN ( " + sysopBoardInStr + " )");
        }

        sb.append(" ) r1 WHERE r1.rnum BETWEEN " + srnum + " AND " + ernum);
        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setLong(1, Long.parseLong(acForm.getCateId()));

        sb.setLength(0);
        sb.append("SELECT lang_knd, board_nm, board_ttl FROM board_lang WHERE board_id=?");
        langPstmt = conn.prepareStatement(sb.toString());
      }

      result = pstmt.executeQuery();

      int resultSize = 0;
      BoardBaseVO boardBaseVO = null;
      while (result.next()) {
        resultSize++;
        if (onlyId) {
          boardList.add(result.getString("board_id"));
        } else {
          boardBaseVO = new BoardBaseVO();

          boardBaseVO.setLangKnd(acForm.getLangKnd());

          boardBaseVO.setBoardId(result.getString("board_id"));
          boardBaseVO.setBoardRid(result.getString("board_rid"));
          boardBaseVO.setBoardActive(result.getString("board_active"));
          boardBaseVO.setBoardSys(result.getString("board_sys"));
          boardBaseVO.setOwntblYn(result.getString("owntbl_yn"));
          boardBaseVO.setOwntblFix(result.getString("owntbl_fix"));
          boardBaseVO.setBoardType(result.getString("board_type"));
          boardBaseVO.setMergeType(result.getString("merge_type"));
          boardBaseVO.setBoardSkin(result.getString("board_skin"));
          boardBaseVO.setUpdDatim(result.getTimestamp("upd_datim"));

          langPstmt.setString(1, boardBaseVO.getBoardId());
          langResult = langPstmt.executeQuery();

          List boardLangList = new ArrayList();
          while (langResult.next())
          {
            BoardLangVO boardLangVO = new BoardLangVO();

            boardLangVO.setBoardId(result.getString("board_id"));
            boardLangVO.setLangKnd(langResult.getString("lang_knd"));
            boardLangVO.setBoardNm(langResult.getString("board_nm"));
            boardLangVO.setBoardTtl(langResult.getString("board_ttl"));

            boardLangList.add(boardLangVO);
          }
          boardBaseVO.setBoardLangList(boardLangList);
          boardList.add(boardBaseVO);
        }
      }
      acForm.setResultSize(resultSize);
    } catch (Exception e) {
      this.logger.error("AdminDAO.getCateBoard()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(langResult);
      close(langPstmt);
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.getCateBoard()");
    return boardList;
  }

  public void setCatebase(ParamMap paramMap, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setCatebase()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;
    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();

      CatebaseVO curCate = getCatebase(paramMap.getString("cateId"), paramMap.getString("langKnd"), connCtxt);

      paramMap.put("parentCateId", curCate.getParentCateId());

      int idx = 0;

      if ("ins".equals(paramMap.getString("act")))
      {
        long nextKey = getNextKey("CATEBASE");
        paramMap.put("newCateId", String.valueOf(nextKey));

        sb.setLength(0);
        sb.append("INSERT INTO catebase (cate_id, domain_id, cate_level, parent_cate_id, cate_order, cate_kind, upd_user_id, upd_datim)");
        sb.append(" VALUES (?,?,?,?,?,?,?,?)");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setLong(idx, nextKey);

        idx++; pstmt.setLong(idx, paramMap.getLong("domainId"));
        idx++; pstmt.setInt(idx, curCate.getCateLevel() + 1);
        idx++; pstmt.setLong(idx, Long.parseLong(curCate.getCateId()));
        idx++; pstmt.setInt(idx, curCate.getNextChildOrder());
        idx++; pstmt.setString(idx, curCate.getCateKind());
        idx++; pstmt.setString(idx, paramMap.getString("updUserId"));
        idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
        pstmt.executeUpdate();
        pstmt.close();

        sb.setLength(0);
        sb.append("INSERT INTO catebase_lang (cate_id, lang_knd, cate_nm) VALUES (?,?,?)");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setLong(idx, nextKey);
        idx++; pstmt.setString(idx, paramMap.getString("langKnd"));
        idx++; pstmt.setString(idx, paramMap.getString("cateNm"));
        pstmt.executeUpdate();
      }
      else if ("upd".equals(paramMap.getString("act")))
      {
        sb.setLength(0);
        sb.append("UPDATE catebase SET domain_id=? WHERE cate_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;

        idx++; pstmt.setLong(idx, paramMap.getLong("domainId"));
        idx++; pstmt.setLong(idx, Long.parseLong(curCate.getCateId()));
        pstmt.executeUpdate();

        sb.setLength(0);
        sb.append("UPDATE catebase_lang SET cate_nm=? WHERE cate_id=? AND lang_knd=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, paramMap.getString("cateNm"));
        idx++; pstmt.setLong(idx, Long.parseLong(curCate.getCateId()));
        idx++; pstmt.setString(idx, paramMap.getString("langKnd"));
        pstmt.executeUpdate();
      }
      else if ("del".equals(paramMap.getString("act")))
      {
        if (curCate.getCateLevel() == 0)
        {
          throw new BaseException("eb.error.cant.delCate.sysCate");
        }

        sb.setLength(0);
        sb.append("SELECT COUNT(board_id) FROM cate_board WHERE cate_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setLong(1, Long.parseLong(curCate.getCateId()));
        result = pstmt.executeQuery();
        if ((result.next()) && 
          (result.getInt(1) > 0)) {
          throw new BaseException("eb.error.cant.delCate.contain");
        }
        result.close();
        pstmt.close();

        sb.setLength(0);
        sb.append("SELECT COUNT(cate_id) FROM catebase WHERE parent_cate_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setLong(1, Long.parseLong(curCate.getCateId()));
        result = pstmt.executeQuery();
        if ((result.next()) && 
          (result.getInt(1) > 0)) {
          throw new BaseException("eb.error.cant.delCate.subCate");
        }
        result.close();
        pstmt.close();

        sb.setLength(0);
        sb.append("UPDATE catebase SET cate_order=cate_order-1 WHERE parent_cate_id=? AND cate_order > ?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setLong(idx, Long.parseLong(curCate.getParentCateId()));
        idx++; pstmt.setInt(idx, curCate.getCateOrder());
        pstmt.executeUpdate();
        pstmt.close();

        sb.setLength(0);
        sb.append("DELETE FROM catebase WHERE cate_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setLong(1, Long.parseLong(curCate.getCateId()));
        pstmt.executeUpdate();
        pstmt.close();

        sb.setLength(0);
        sb.append("DELETE FROM catebase_lang WHERE cate_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setLong(1, Long.parseLong(curCate.getCateId()));
        pstmt.executeUpdate();
        pstmt.close();
      }
    } catch (BaseException be) {
      this.logger.error("AdminDAO.setCatebase()", be);
      throw be;
    } catch (Exception e) {
      this.logger.error("AdminDAO.setCatebase()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.setCatebase()");
  }

  public void setCatebaseMove(AdminCateForm acForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setCatebaseMove()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;
    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();

      CatebaseVO dragCate = null; CatebaseVO dropCate = null;
      dragCate = getCatebase(acForm.getDragId(), acForm.getLangKnd(), connCtxt);
      if ((!"U".equals(acForm.getDropId())) && (!"D".equals(acForm.getDropId())))
        dropCate = getCatebase(acForm.getDropId(), acForm.getLangKnd(), connCtxt);
      else {
        acForm.setParentCateId(dragCate.getParentCateId());
      }

      int idx = 0;

      if ("U".equals(acForm.getDropId())) {
        if (dragCate.getCateOrder() == 1) {
          throw new BaseException("eb.error.cant.ordUp");
        }
        sb.setLength(0);
        sb.append("UPDATE catebase SET cate_order=cate_order+1 WHERE parent_cate_id=? AND cate_order=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setLong(idx, Long.parseLong(dragCate.getParentCateId()));
        idx++; pstmt.setInt(idx, dragCate.getCateOrder() - 1);
        pstmt.executeUpdate();
        pstmt.close();

        sb.setLength(0);
        sb.append("UPDATE catebase SET cate_order=cate_order-1 WHERE cate_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setLong(idx, Long.parseLong(dragCate.getCateId()));
        pstmt.executeUpdate();
        pstmt.close();
      }
      else if ("D".equals(acForm.getDropId())) {
        if (dragCate.getCateOrder() + 1 == dragCate.getNextOrder()) {
          throw new BaseException("eb.error.cant.ordDown");
        }
        sb.setLength(0);
        sb.append("UPDATE catebase SET cate_order=cate_order-1 WHERE parent_cate_id=? AND cate_order=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setLong(idx, Long.parseLong(dragCate.getParentCateId()));
        idx++; pstmt.setInt(idx, dragCate.getCateOrder() + 1);
        pstmt.executeUpdate();
        pstmt.close();

        sb.setLength(0);
        sb.append("UPDATE catebase SET cate_order=cate_order + 1 WHERE cate_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setLong(idx, Long.parseLong(dragCate.getCateId()));
        pstmt.executeUpdate();
        pstmt.close();
      }
      else if (dragCate.getParentCateId().equals(dropCate.getCateId()))
      {
        sb.setLength(0);
        sb.append("UPDATE catebase SET cate_order=cate_order-1 WHERE parent_cate_id=? AND cate_order > ?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setLong(idx, Long.parseLong(dragCate.getParentCateId()));
        idx++; pstmt.setInt(idx, dragCate.getCateOrder());
        pstmt.executeUpdate();
        pstmt.close();

        sb.setLength(0);
        sb.append("UPDATE catebase SET cate_order=? WHERE cate_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setInt(idx, dragCate.getNextOrder() - 1);
        idx++; pstmt.setLong(idx, Long.parseLong(dragCate.getCateId()));
        pstmt.executeUpdate();
      }
      else
      {
        sb.setLength(0);
        sb.append("UPDATE catebase SET cate_order=cate_order-1 WHERE parent_cate_id=? AND cate_order > ?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setLong(idx, Long.parseLong(dragCate.getParentCateId()));
        idx++; pstmt.setInt(idx, dragCate.getCateOrder());
        pstmt.executeUpdate();
        pstmt.close();

        sb.setLength(0);
        sb.append("UPDATE catebase SET cate_level=?, parent_cate_id=?, cate_order=? WHERE cate_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setInt(idx, dropCate.getCateLevel() + 1);
        idx++; pstmt.setLong(idx, Long.parseLong(dropCate.getCateId()));
        idx++; pstmt.setInt(idx, dropCate.getNextChildOrder());
        idx++; pstmt.setLong(idx, Long.parseLong(dragCate.getCateId()));
        pstmt.executeUpdate();

        dragCate.setCateLevel(dropCate.getCateLevel() + 1);
        dragCate.setParentCateId(dropCate.getCateId());
        dragCate.setCateOrder(dropCate.getNextChildOrder());

        setCatebaseMoveRecursive(dragCate, acForm, connCtxt);
      }
    } catch (BaseException be) {
      this.logger.error("AdminDAO.setCatebaseMove()", be);
      throw be;
    } catch (Exception e) {
      this.logger.error("AdminDAO.setCatebaseMove()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.setCatebaseMove()");
  }

  public List getCatebaseLangList(AdminCateForm acForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.getCatebaseLangList()");
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;

    StringBuffer sb = new StringBuffer();
    List cateLangList = new ArrayList();
    try {
      conn = connCtxt.getConnection();
      sb.append("SELECT cate_id, lang_knd, cate_nm FROM catebase_lang WHERE cate_id=?");
      pstmt = conn.prepareStatement(sb.toString());
      pstmt.setLong(1, Long.parseLong(acForm.getCateId()));
      result = pstmt.executeQuery();
      while (result.next()) {
        CateLangVO cateLangVO = new CateLangVO();
        cateLangVO.setCateId(result.getString("cate_id"));
        cateLangVO.setLangKnd(result.getString("lang_knd"));
        cateLangVO.setCateNm(result.getString("cate_nm"));
        cateLangList.add(cateLangVO);
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.getCatebaseLangList()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.getCatebaseLangList()");
    return cateLangList;
  }

  public void setCatebaseLang(AdminCateForm acForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setCatebaseLang()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;
    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();

      int idx = 0;

      if ("ins".equals(acForm.getAct()))
      {
        sb.setLength(0);
        sb.append("SELECT COUNT(lang_knd) FROM catebase_lang WHERE cate_id=? AND lang_knd=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setLong(idx, Long.parseLong(acForm.getCateId()));
        idx++; pstmt.setString(idx, acForm.getLangKnd());
        result = pstmt.executeQuery();
        if ((result.next()) && 
          (result.getInt(1) > 0)) {
          throw new BaseException("eb.error.already.lang");
        }
        result.close();
        pstmt.close();

        sb.setLength(0);
        sb.append("INSERT INTO catebase_lang (cate_id, lang_knd, cate_nm) VALUES (?,?,?)");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setLong(idx, Long.parseLong(acForm.getCateId()));
        idx++; pstmt.setString(idx, acForm.getLangKnd());
        idx++; pstmt.setString(idx, acForm.getCateNm());
        pstmt.executeUpdate();
      }
      else if ("upd".equals(acForm.getAct()))
      {
        sb.setLength(0);
        sb.append("UPDATE catebase_lang set cate_nm=? WHERE cate_id=? AND lang_knd=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, acForm.getCateNm());
        idx++; pstmt.setLong(idx, Long.parseLong(acForm.getCateId()));
        idx++; pstmt.setString(idx, acForm.getLangKnd());
        pstmt.executeUpdate();
      }
      else if ("del".equals(acForm.getAct()))
      {
        sb.setLength(0);
        sb.append("DELETE FROM catebase_lang WHERE cate_id=? AND lang_knd=?");
        pstmt = conn.prepareStatement(sb.toString());

        StringTokenizer stnz = new StringTokenizer(acForm.getLangKnd(), ",");
        while (stnz.hasMoreTokens()) {
          String langKnd = stnz.nextToken();
          if (Enview.getConfiguration().getString("portal.default.locale").equals(langKnd)) {
            continue;
          }
          idx = 0;
          idx++; pstmt.setLong(idx, Long.parseLong(acForm.getCateId()));
          idx++; pstmt.setString(idx, langKnd);
          pstmt.executeUpdate();
        }
      }
    }
    catch (BaseException be) {
      this.logger.error("AdminDAO.setCatebase()", be);
      throw be;
    } catch (Exception e) {
      this.logger.error("AdminDAO.setCatebaseLang()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.setCatebaseLang()");
  }

  private void setCatebaseMoveRecursive(CatebaseVO brdCateVO, AdminCateForm acForm, ConnectionContext connCtxt)
    throws Exception
  {
    ArrayList childs = null;

    Connection conn = null;
    PreparedStatement pstmt = null;
    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();

      sb.append("UPDATE catebase SET cate_level=? WHERE parent_cate_id=?");
      pstmt = conn.prepareStatement(sb.toString());
      int idx = 0;
      idx++; pstmt.setInt(idx, brdCateVO.getCateLevel() + 1);
      idx++; pstmt.setLong(idx, Long.parseLong(brdCateVO.getCateId()));
      pstmt.executeUpdate();

      childs = (ArrayList)getChildCatebase(brdCateVO.getCateId(), false, acForm.getLangKnd(), connCtxt);
      for (int i = 0; i < childs.size(); i++)
        setCatebaseMoveRecursive((CatebaseVO)childs.get(i), acForm, connCtxt);
    }
    catch (Exception e) {
      this.logger.error("AdminDAO.setCatebaseMoveRecursive()", e);
      throw e;
    } finally {
      close(pstmt);
    }
  }

  private void makeAtchDir(File dir) throws BaseException {
    if (dir.exists()) {
      this.logger.info("Directory already exists! [" + dir.getAbsolutePath() + "]");
    }
    else if (!dir.mkdir()) {
      this.logger.error("Make directory failed! " + dir.getAbsolutePath() + "]");
      throw new BaseException("eb.error.make.atch.dir");
    }
  }

  public void boardCreate(ParamMap paramMap, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.boardCreate()");

    Connection conn = null;
    PreparedStatement pstmt = null; PreparedStatement insPstmt = null;
    ResultSet result = null;
    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();
      int idx = 0;

      CatebaseVO boardCateVO = getCatebase(paramMap.getString("cateId"), paramMap.getString("langKnd"), connCtxt);
      if (boardCateVO.getCateLevel() == 0)
      {
        throw new BaseException("eb.error.cant.creBoard.superCate");
      }

      pstmt = conn.prepareStatement("SELECT COUNT(board_id) FROM board WHERE board_id=?");
      idx++; pstmt.setString(idx, paramMap.getString("boardId"));
      result = pstmt.executeQuery();
      if ((result.next()) && 
        (result.getInt(1) > 0))
        throw new BaseException("mm.error.sql.badIntegrity");
      result.close();
      pstmt.close();

      if (ValidateUtil.isEmpty(ValidateUtil.validate(paramMap.getString("boardRid"))))
      {
        sb.setLength(0);
        sb.append("INSERT INTO board");
        sb.append(" SELECT ?, ?, ?, board_active, board_sys, owntbl_yn, owntbl_fix, board_type, merge_type, func_yns, buga_yns,");
        sb.append(" list_yns, read_yns, srch_yns, ttl_yns, ttl_lens, term_flag, board_skin, raise_color, raise_cnt, new_term, max_file_cnt, max_file_size,");
        sb.append(" max_file_down, list_set_cnt, bad_std_cnt, mini_trgt_win, mini_trgt_url, extn_class_nm, board_width, top_html, bottom_html, board_bg_pic,");
        sb.append(" board_bg_color, ?, ?, ?, ?");
        sb.append(" FROM board WHERE board_id='" + paramMap.getString("refBoardId") + "'");

        pstmt = conn.prepareStatement(sb.toString());

        idx = 0;
        idx++; pstmt.setString(idx, paramMap.getString("boardId"));
        idx++; pstmt.setString(idx, paramMap.getString("boardId"));
        idx++; pstmt.setLong(idx, boardCateVO.getDomainId());
        idx++; pstmt.setString(idx, paramMap.getString("updUserId"));
        idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
        idx++; pstmt.setString(idx, paramMap.getString("updUserId"));
        idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));

        pstmt.executeUpdate();
      }
      else {
        sb.setLength(0);
        sb.append("INSERT INTO board");
        sb.append(" SELECT ?, board_id, domain_id, board_active, board_sys, owntbl_yn, owntbl_fix, board_type, merge_type, func_yns, buga_yns,");
        sb.append(" list_yns, read_yns, srch_yns, ttl_yns, ttl_lens, term_flag, board_skin, raise_color, raise_cnt, new_term, max_file_cnt, max_file_size,");
        sb.append(" max_file_down, list_set_cnt, bad_std_cnt, mini_trgt_win, mini_trgt_url, extn_class_nm, board_width, top_html, bottom_html, board_bg_pic,");
        sb.append(" board_bg_color,?, ?, ?, ?");
        sb.append(" FROM board WHERE board_id=?");
        pstmt = conn.prepareStatement(sb.toString());

        idx = 0;
        idx++; pstmt.setString(idx, paramMap.getString("boardId"));
        idx++; pstmt.setString(idx, paramMap.getString("updUserId"));
        idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
        idx++; pstmt.setString(idx, paramMap.getString("updUserId"));
        idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
        idx++; pstmt.setString(idx, paramMap.getString("boardRid"));

        pstmt.executeUpdate();
      }
      pstmt.close();

      sb.setLength(0);
      sb.append("INSERT INTO board_lang (board_id, lang_knd, board_nm, board_ttl) VALUES (?,?,?,?)");
      pstmt = conn.prepareStatement(sb.toString());
      idx = 0;
      idx++; pstmt.setString(idx, paramMap.getString("boardId"));
      idx++; pstmt.setString(idx, paramMap.getString("langKnd"));
      idx++; pstmt.setString(idx, paramMap.getString("boardNm"));
      idx++; pstmt.setString(idx, paramMap.getString("boardTtl"));
      pstmt.executeUpdate();
      pstmt.close();

      SecurityDAO securityDAO = (SecurityDAO)DAOFactory.getInst().getDAO("com.saltware.enboard.dao.spi.Security");
      List gradeList = securityDAO.getGradeList(paramMap.getString("gradeFix"), connCtxt);

      sb.setLength(0);
      sb.append("INSERT INTO security_permission");
      sb.append(" SELECT ?, ?, ?, ?, ?, 5, ?, 1, SYSDATE, SYSDATE FROM DUAL");
      pstmt = conn.prepareStatement(sb.toString());

      Iterator it = gradeList.iterator();
      while (it.hasNext()) {
        CodebaseVO cdVO = (CodebaseVO)it.next();

        idx = 0;
        idx++; pstmt.setLong(idx, getNextKey("SECURITY_PERMISSION"));
        idx++; pstmt.setLong(idx, Long.parseLong(cdVO.getCodeId()));

        idx++; pstmt.setLong(idx, boardCateVO.getDomainId());
        idx++; pstmt.setString(idx, paramMap.getString("boardNm"));
        idx++; pstmt.setString(idx, paramMap.getString("boardId"));
        idx++; pstmt.setInt(idx, Integer.parseInt(cdVO.getCodeTag2()));

        pstmt.executeUpdate();
      }
      result.close();
      pstmt.close();

      sb.setLength(0);
      sb.append("INSERT INTO mileage_cd");
      sb.append(" SELECT ?, ?, mile_active, mile_io, mile_sys, mile_pnt, mile_sttg, tlimit_cnt,");
      sb.append(" dlimit_cnt, wlimit_cnt, mlimit_cnt, ylimit_cnt, mile_nm, mile_rem, upd_user_id, ?");
      sb.append(" FROM mileage_cd WHERE mile_cd=?");
      insPstmt = conn.prepareStatement(sb.toString());
      idx = 0;
      if ("PT".equals(paramMap.getString("boardSys")))
      {
        pstmt = conn.prepareStatement("SELECT SUBSTR(mile_cd,14) base_nm, mile_cd FROM mileage_cd WHERE mile_cd LIKE ?");
        idx++; pstmt.setString(idx, "ENBOARD.BASE%");
      } else if ("CF".equals(paramMap.getString("boardSys")))
      {
        pstmt = conn.prepareStatement("SELECT SUBSTR(mile_cd,11) base_nm, mile_cd FROM mileage_cd WHERE mile_cd LIKE ?");
        idx++; pstmt.setString(idx, "CAFE.BASE%");
      }
      result = pstmt.executeQuery();
      while (result.next()) {
        idx = 0;
        idx++; insPstmt.setString(idx, "EB" + paramMap.getString("boardId") + result.getString("base_nm"));
        idx++; insPstmt.setLong(idx, boardCateVO.getDomainId());
        idx++; insPstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
        idx++; insPstmt.setString(idx, result.getString("mile_cd"));

        insPstmt.executeUpdate();
      }
      insPstmt.close();
      pstmt.close();

      sb.setLength(0);
      sb.append("INSERT INTO cate_board (cate_id, board_id) VALUES (?,?)");
      pstmt = conn.prepareStatement(sb.toString());

      idx = 0;
      idx++; pstmt.setLong(idx, Long.parseLong(paramMap.getString("cateId")));
      idx++; pstmt.setString(idx, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      if (!ValidateUtil.isEmpty(ValidateUtil.validate(paramMap.getString("boardRid")))) {
        sb.setLength(0);
        sb.append("INSERT INTO bltn_cate");
        sb.append(" SELECT ?, cate_id, cate_order, upd_user_id, upd_datim");
        sb.append(" FROM bltn_cate WHERE board_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, paramMap.getString("boardId"));
        idx++; pstmt.setString(idx, paramMap.getString("boardRid"));
        pstmt.executeUpdate();
        pstmt.close();

        sb.setLength(0);
        sb.append("INSERT INTO bltn_cate_lang");
        sb.append(" SELECT ?, cate_id, lang_knd, cate_nm");
        sb.append(" FROM bltn_cate_lang WHERE board_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, paramMap.getString("boardId"));
        idx++; pstmt.setString(idx, paramMap.getString("boardRid"));
        pstmt.executeUpdate();
        pstmt.close();
      }

      if (ValidateUtil.isEmpty(ValidateUtil.validate(paramMap.getString("boardRid"))))
      {
        String upload = paramMap.getString("uploadPath");
        String sep = System.getProperty("file.separator");
        String attachPath = upload + sep + "attach" + sep;
        String editorPath = upload + sep + "editor" + sep;
        String thumbPath = upload + sep + "thumb" + sep;
        String pollPath = upload + sep + "poll" + sep;
        this.logger.info("attachPath=[" + attachPath + "]");

        File attachDir = new File(attachPath + paramMap.getString("boardId"));
        File editorDir = new File(editorPath + paramMap.getString("boardId"));
        File thumbDir = new File(thumbPath + paramMap.getString("boardId"));
        File pollDir = new File(pollPath + paramMap.getString("boardId"));

        makeAtchDir(attachDir);
        makeAtchDir(editorDir);
        makeAtchDir(thumbDir);
        makeAtchDir(pollDir);
      }
    }
    catch (BaseException be) {
      this.logger.error("AdminDAO.boardCreate()", be);
      throw be;
    } catch (Exception e) {
      this.logger.error("AdminDAO.boardCreate()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(result);
      close(pstmt);
      close(insPstmt);
    }
    this.logger.info("END::AdminDAO.boardCreate()");
  }

  public void setBoardLang(AdminBoardForm abForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setBoardLang()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;
    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();

      int idx = 0;

      if ("ins".equals(abForm.getAct()))
      {
        sb.setLength(0);
        sb.append("SELECT COUNT(lang_knd) FROM board_lang WHERE board_id=? AND lang_knd=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, abForm.getBoardId());
        idx++; pstmt.setString(idx, abForm.getLangKnd());
        result = pstmt.executeQuery();
        if ((result.next()) && 
          (result.getInt(1) > 0)) {
          throw new BaseException("eb.error.already.lang");
        }
        result.close();
        pstmt.close();

        sb.setLength(0);
        sb.append("INSERT INTO board_lang (board_id, lang_knd, board_nm, board_ttl) VALUES (?,?,?,?)");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, abForm.getBoardId());
        idx++; pstmt.setString(idx, abForm.getLangKnd());
        idx++; pstmt.setString(idx, abForm.getBoardNm());
        idx++; pstmt.setString(idx, abForm.getBoardTtl());
        pstmt.executeUpdate();
      }
      else if ("upd".equals(abForm.getAct()))
      {
        sb.setLength(0);
        sb.append("UPDATE board_lang set board_nm=?, board_ttl=? WHERE board_id=? AND lang_knd=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, abForm.getBoardNm());
        idx++; pstmt.setString(idx, abForm.getBoardTtl());
        idx++; pstmt.setString(idx, abForm.getBoardId());
        idx++; pstmt.setString(idx, abForm.getLangKnd());
        pstmt.executeUpdate();
      }
      else if ("del".equals(abForm.getAct()))
      {
        sb.setLength(0);
        sb.append("DELETE FROM board_lang WHERE board_id=? AND lang_knd=?");
        pstmt = conn.prepareStatement(sb.toString());

        StringTokenizer stnz = new StringTokenizer(abForm.getLangKnd(), ",");
        while (stnz.hasMoreTokens()) {
          String langKnd = stnz.nextToken();
          if (Enview.getConfiguration().getString("portal.default.locale").equals(langKnd))
          {
            continue;
          }
          idx = 0;
          idx++; pstmt.setString(idx, abForm.getBoardId());
          idx++; pstmt.setString(idx, langKnd);
          pstmt.executeUpdate();
        }
      }
    }
    catch (BaseException be) {
      this.logger.error("AdminDAO.setBoardLang()", be);
      throw be;
    } catch (Exception e) {
      this.logger.error("AdminDAO.setBoardLang()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.setBoardLang()");
  }

  public void setBoardBase(AdminBoardForm abForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setBoardBase()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;
    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();

      int idx = 0;

      if ("active".equals(abForm.getAct()))
      {
        sb.setLength(0);
        sb.append("UPDATE board SET board_active='Y' WHERE board_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, abForm.getBoardId());
        pstmt.executeUpdate();
      }
      else if ("inactive".equals(abForm.getAct()))
      {
        sb.setLength(0);
        sb.append("UPDATE board SET board_active='N' WHERE board_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, abForm.getBoardId());
        pstmt.executeUpdate();
      }

    }
    catch (SQLException e)
    {
      this.logger.error("AdminDAO.setBoardBase()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.setBoardBase()");
  }

  public void setCateBoardMove(AdminBoardForm abForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setCateBoardMove()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;

    String srcCateId = abForm.getCateId();
    String dstCateId = abForm.getTrgtCateId();
    String boardId = abForm.getBoardId();

    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();

      if ("MOVE".equals(abForm.getAct()))
      {
        sb.append("DELETE FROM cate_board WHERE cate_id IN (?,?) AND board_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setLong(1, Long.parseLong(srcCateId));
        pstmt.setLong(2, Long.parseLong(dstCateId));
        pstmt.setString(3, boardId);
        pstmt.executeUpdate();
        pstmt.close();

        sb.setLength(0);
        sb.append("INSERT INTO cate_board VALUES (?,?)");
        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setLong(1, Long.parseLong(dstCateId));
        pstmt.setString(2, boardId);
        pstmt.executeUpdate();
        pstmt.close();
      }
      else if ("COPY".equals(abForm.getAct()))
      {
        sb.append("DELETE FROM cate_board WHERE cate_id=? AND board_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setLong(1, Long.parseLong(dstCateId));
        pstmt.setString(2, boardId);
        pstmt.executeUpdate();
        pstmt.close();

        sb.setLength(0);
        sb.append("INSERT INTO cate_board VALUES (?,?)");
        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setLong(1, Long.parseLong(dstCateId));
        pstmt.setString(2, boardId);
        pstmt.executeUpdate();
        pstmt.close();
      }
      else if ("DELETE".equals(abForm.getAct()))
      {
        sb.append("SELECT COUNT(cate_id) FROM cate_board WHERE board_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setString(1, boardId);
        result = pstmt.executeQuery();

        if ((result.next()) && 
          (result.getInt(1) == 1)) {
          throw new BaseException("eb.error.last.boardCateBoard");
        }
        pstmt.close();

        sb.setLength(0);
        sb.append("DELETE FROM cate_board WHERE cate_id=? AND board_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setLong(1, Long.parseLong(srcCateId));
        pstmt.setString(2, boardId);
        pstmt.executeUpdate();
        pstmt.close();
      }
    } catch (BaseException be) {
      this.logger.error("AdminDAO.setCateBoardMove()", be);
      throw be;
    } catch (Exception e) {
      this.logger.error("AdminDAO.setCateBoardMove()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.setCateBoardMove()");
  }

  public CodebaseVO getPrin(String prinId, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDao.getPrin()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;

    CodebaseVO cdVO = null;
    try {
      conn = connCtxt.getConnection();

      pstmt = conn.prepareStatement("SELECT principal_id, short_path FROM security_principal WHERE principal_id=?");
      pstmt.setLong(1, Long.parseLong(prinId));
      result = pstmt.executeQuery();

      int idx = 0;
      if (result.next()) {
        cdVO = new CodebaseVO();
        idx = 0;
        idx++; cdVO.setCodeId(result.getString(idx));
        idx++; cdVO.setCodeName(result.getString(idx));
      }
      result.close();
      pstmt.close();
    }
    catch (Exception e) {
      this.logger.error("AdminDao.getPrin()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDao.getPrin()");
    return cdVO;
  }

  public List getChildPrin(String parentPrinId, boolean onlyId, ConnectionContext connCtxt)
    throws BaseException
  {
    return getChildPrin(parentPrinId, onlyId, -1L, connCtxt);
  }

  public List getChildPrin(String parentPrinId, boolean onlyId, long domainId, ConnectionContext connCtxt)
    throws BaseException
  {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rslt = null;
    ArrayList prinList = new ArrayList();
    StringBuffer sb = new StringBuffer();

    if (onlyId) {
      sb.append("SELECT principal_id FROM security_principal WHERE parent_id=?");
      if (domainId != -1L)
        sb.append(" AND domain_id=?");
      sb.append(" ORDER BY principal_order");
    } else {
      sb.append("SELECT a.principal_id, a.domain_id, a.short_path");
      sb.append(", (SELECT COUNT(b.principal_id) FROM security_principal b WHERE b.parent_id=a.principal_id) child_cnt");
      sb.append(" FROM security_principal a");
      sb.append(" WHERE a.parent_id=?");
      if (domainId != -1L)
        sb.append(" AND domain_id=?");
      sb.append(" ORDER BY a.principal_order");
    }
    try
    {
      conn = connCtxt.getConnection();

      pstmt = conn.prepareStatement(sb.toString());
      pstmt.setLong(1, Long.parseLong(parentPrinId));
      if (domainId != -1L)
        pstmt.setLong(2, domainId);
      rslt = pstmt.executeQuery();

      int idx = 0;
      CodebaseVO cdVO = null;
      while (rslt.next()) {
        if (onlyId) {
          idx++; prinList.add(rslt.getString(idx));
        } else {
          cdVO = new CodebaseVO();
          idx++; cdVO.setCodeId(rslt.getString(idx));
          idx++; cdVO.setDomainId(rslt.getLong(idx));
          idx++; cdVO.setCodeName(rslt.getString(idx));
          idx++; cdVO.setCodeTag1(rslt.getString(idx));

          prinList.add(cdVO);
        }
        idx = 0;
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.getChildPrin()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(rslt);
      close(pstmt);
    }

    return prinList;
  }

  public CodebaseVO getPrin(String prinId, String boardId, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDao.getPrin(boardId)");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;

    StringBuffer sb = new StringBuffer();
    CodebaseVO cdVO = null;
    try {
      conn = connCtxt.getConnection();

      sb.append("SELECT a.principal_id, a.short_path, a.principal_name");
      sb.append(", (SELECT COUNT(s.permission_id) FROM security_permission s WHERE s.res_type=5 AND s.principal_id=a.principal_id AND s.res_url=?) is_exist");
      sb.append(" FROM security_principal a");
      sb.append(" WHERE a.principal_id=?");
      pstmt = conn.prepareStatement(sb.toString());
      pstmt.setString(1, boardId);
      pstmt.setLong(2, Long.parseLong(prinId));
      result = pstmt.executeQuery();

      int idx = 0;
      if (result.next()) {
        cdVO = new CodebaseVO();
        idx = 0;
        idx++; cdVO.setCodeId(result.getString(idx));
        idx++; cdVO.setCode(result.getString(idx));
        idx++; cdVO.setCodeName(result.getString(idx));
        idx++; cdVO.setCodeTag2(result.getString(idx));
      }
      result.close();
      pstmt.close();
    }
    catch (Exception e) {
      this.logger.error("AdminDao.getPrin(boardId)", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDao.getPrin(boardId)");
    return cdVO;
  }

  public List getChildPrin(String parentPrinId, boolean onlyId, String boardId, ConnectionContext connCtxt)
    throws BaseException
  {
    return getChildPrin(parentPrinId, onlyId, boardId, -1L, connCtxt);
  }

  public List getChildPrin(String parentPrinId, boolean onlyId, String boardId, long domainId, ConnectionContext connCtxt)
    throws BaseException
  {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rslt = null;
    ArrayList prinList = new ArrayList();
    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();

      if (onlyId) {
        sb.append("SELECT principal_id FROM security_principal WHERE parent_id=?");
        if (domainId != -1L)
          sb.append(" AND domain_id=?");
        sb.append(" ORDER BY principal_order");

        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setLong(1, Long.parseLong(parentPrinId));
        if (domainId != -1L)
          pstmt.setLong(2, domainId);
      } else {
        sb.append("SELECT a.principal_id, a.domain_id, a.short_path, a.principal_name");
        sb.append(", (SELECT COUNT(b.principal_id) FROM security_principal b WHERE b.parent_id=a.principal_id) child_cnt");
        sb.append(", (SELECT COUNT(s.permission_id) FROM security_permission s WHERE s.res_type=5 AND s.principal_id=a.principal_id AND s.res_url=?) is_exist");
        sb.append(" FROM security_principal a");
        sb.append(" WHERE a.parent_id=?");
        if (domainId != -1L)
          sb.append(" AND domain_id=?");
        sb.append(" ORDER BY a.principal_order");

        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setString(1, boardId);
        pstmt.setLong(2, Long.parseLong(parentPrinId));
        if (domainId != -1L) {
          pstmt.setLong(3, domainId);
        }
      }
      rslt = pstmt.executeQuery();

      int idx = 0;
      CodebaseVO cdVO = null;
      while (rslt.next()) {
        if (onlyId) {
          idx++; prinList.add(rslt.getString(idx));
        } else {
          cdVO = new CodebaseVO();
          idx++; cdVO.setCodeId(rslt.getString(idx));
          idx++; cdVO.setDomainId(rslt.getLong(idx));
          idx++; cdVO.setCode(rslt.getString(idx));
          idx++; cdVO.setCodeName(rslt.getString(idx));
          idx++; cdVO.setCodeTag1(rslt.getString(idx));
          idx++; cdVO.setCodeTag2(rslt.getString(idx));

          prinList.add(cdVO);
        }
        idx = 0;
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.getChildPrin(boardId)", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(rslt);
      close(pstmt);
    }

    return prinList;
  }

  public CodebaseVO getPrin(String prinId, String boardId, String bltnNo, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDao.getPrin(boardId/bltnNo)");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;

    StringBuffer sb = new StringBuffer();
    CodebaseVO cdVO = null;
    try {
      conn = connCtxt.getConnection();

      int idx = 0;

      sb.append("SELECT a.principal_id, a.short_path, a.principal_name");
      if ((!ValidateUtil.isEmpty(boardId)) && (!ValidateUtil.isEmpty(bltnNo)))
        sb.append(", (SELECT COUNT(s.board_id) FROM bltn_auth s WHERE s.board_id=? AND s.bltn_no=? AND s.prin_id=a.principal_id) is_exist");
      else {
        sb.append(", 0 is_exist");
      }
      sb.append(" FROM security_principal a");
      sb.append(" WHERE a.principal_id=?");
      pstmt = conn.prepareStatement(sb.toString());

      if ((!ValidateUtil.isEmpty(boardId)) && (!ValidateUtil.isEmpty(bltnNo))) {
        idx++; pstmt.setString(idx, boardId);
        idx++; pstmt.setString(idx, bltnNo);
      }
      idx++; pstmt.setLong(idx, Long.parseLong(prinId));
      result = pstmt.executeQuery();

      if (result.next()) {
        cdVO = new CodebaseVO();
        idx = 0;
        idx++; cdVO.setCodeId(result.getString(idx));
        idx++; cdVO.setCode(result.getString(idx));
        idx++; cdVO.setCodeName(result.getString(idx));
        idx++; cdVO.setCodeTag2(result.getString(idx));
      }
      result.close();
      pstmt.close();
    }
    catch (Exception e) {
      this.logger.error("AdminDao.getPrin(boardId/bltnNo)", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDao.getPrin(boardId/bltnNo)");
    return cdVO;
  }

  public List getChildPrin(String parentPrinId, boolean onlyId, String boardId, String bltnNo, ConnectionContext connCtxt)
    throws BaseException
  {
    return getChildPrin(parentPrinId, onlyId, boardId, bltnNo, -1L, connCtxt);
  }

  public List getChildPrin(String parentPrinId, boolean onlyId, String boardId, String bltnNo, long domainId, ConnectionContext connCtxt)
    throws BaseException
  {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rslt = null;
    ArrayList prinList = new ArrayList();
    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();

      int idx = 0;

      if (onlyId) {
        sb.append("SELECT principal_id FROM security_principal WHERE parent_id=?");
        if (domainId != -1L)
          sb.append(" AND domain_id=?");
        sb.append(" ORDER BY principal_order");

        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setLong(1, Long.parseLong(parentPrinId));
        if (domainId != -1L)
          pstmt.setLong(2, domainId);
      } else {
        sb.append("SELECT a.principal_id, a.domain_id, a.short_path, a.principal_name");
        sb.append(", (SELECT COUNT(b.principal_id) FROM security_principal b WHERE b.parent_id=a.principal_id) child_cnt");
        if ((!ValidateUtil.isEmpty(boardId)) && (!ValidateUtil.isEmpty(bltnNo)))
          sb.append(", (SELECT COUNT(s.board_id) FROM bltn_auth s WHERE s.board_id=? AND s.bltn_no=? AND s.prin_id=a.principal_id) is_exist");
        else {
          sb.append(", 0 is_exist");
        }
        sb.append(" FROM security_principal a");
        sb.append(" WHERE a.parent_id=?");
        if (domainId != -1L)
          sb.append(" AND domain_id=?");
        sb.append(" ORDER BY a.principal_order");

        pstmt = conn.prepareStatement(sb.toString());
        if ((!ValidateUtil.isEmpty(boardId)) && (!ValidateUtil.isEmpty(bltnNo))) {
          idx++; pstmt.setString(idx, boardId);
          idx++; pstmt.setString(idx, bltnNo);
        }
        idx++; pstmt.setLong(idx, Long.parseLong(parentPrinId));
        if (domainId != -1L) {
          idx++; pstmt.setLong(idx, domainId);
        }
      }
      rslt = pstmt.executeQuery();

      CodebaseVO cdVO = null;
      while (rslt.next()) {
        idx = 0;
        if (onlyId) {
          idx++; prinList.add(rslt.getString(idx));
        } else {
          cdVO = new CodebaseVO();
          idx++; cdVO.setCodeId(rslt.getString(idx));
          idx++; cdVO.setDomainId(rslt.getLong(idx));
          idx++; cdVO.setCode(rslt.getString(idx));
          idx++; cdVO.setCodeName(rslt.getString(idx));
          idx++; cdVO.setCodeTag1(rslt.getString(idx));
          idx++; cdVO.setCodeTag2(rslt.getString(idx));

          prinList.add(cdVO);
        }
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.getChildPrin(boardId/bltnNo)", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(rslt);
      close(pstmt);
    }

    return prinList;
  }

  public void boardDelete(ParamMap paramMap, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.boardDelete()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;
    try
    {
      if ("PT".equals(paramMap.getString("boardSys")))
      {
        if ((Enview.getConfiguration().getBoolean("board.delete.pwdchk", true)) && 
          (!SecurityMngr.getInst().matchPass(paramMap.getString("updUserId"), paramMap.getString("columnValue")))) {
          throw new BaseException("eb.error.invalid.pass");
        }
        if (!SecurityMngr.getInst().isAdmin(paramMap.getString("updUserId")))
          throw new BaseException("eb.error.invalid.right");
      }
      conn = connCtxt.getConnection();

      pstmt = conn.prepareStatement("SELECT board_id FROM board WHERE board_id!=? AND board_rid=?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.setString(2, paramMap.getString("boardId"));
      result = pstmt.executeQuery();
      if (result.next()) {
        throw new BaseException("eb.error.exist.virtual.board", new Object[] { result.getString("board_id") });
      }

      result.close();
      pstmt.close();

      CacheMngr cacheMngr = (CacheMngr)getCacheMngr("EB");

      BoardVO boardVO = cacheMngr.getBoard(paramMap.getString("boardId"), Enview.getConfiguration().getString("portal.default.locale"), connCtxt);

      pstmt = conn.prepareStatement("DELETE FROM " + boardVO.getBltnTbl() + " WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM " + boardVO.getCnttTbl() + " WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      if ("Y".equals(boardVO.getExtUseYn())) {
        pstmt = conn.prepareStatement("DELETE FROM " + boardVO.getBltnExtnMapper().getBltnExtnTblNm() + " WHERE board_id = ?");
        pstmt.setString(1, paramMap.getString("boardId"));
        pstmt.executeUpdate();
        pstmt.close();
      }

      pstmt = conn.prepareStatement("DELETE FROM " + boardVO.getFileTbl() + " WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM " + boardVO.getEvalTbl() + " WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM " + boardVO.getMemoTbl() + " WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM bltn_memo_bad WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM bltn_poll WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM bltn_poll_rslt WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM board_prop WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM board_snnt WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM bltn_cate WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM bltn_cate_lang WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM cate_board WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM mileage_cd WHERE mile_cd like ?");
      pstmt.setString(1, "EB" + paramMap.getString("boardId") + "%");
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM mileage_sttg WHERE mile_cd like ?");
      pstmt.setString(1, "EB" + paramMap.getString("boardId") + "%");
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM security_permission WHERE res_type = 5 AND res_url = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM security_principal WHERE principal_type = 'g' AND short_path LIKE ?");
      pstmt.setString(1, "/PT" + paramMap.getString("boardId") + "/%");

      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM bltn_prin WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM bltn_extn_prop WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM bltn_extn_prop_lang WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM board_lang WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("DELETE FROM board WHERE board_id = ?");
      pstmt.setString(1, paramMap.getString("boardId"));
      pstmt.executeUpdate();
      pstmt.close();

      String sep = System.getProperty("file.separator");
      String attachPath = paramMap.getString("uploadPath") + sep + "attach" + sep + paramMap.getString("boardId");
      String editorPath = paramMap.getString("uploadPath") + sep + "editor" + sep + paramMap.getString("boardId");
      String thumbPath = paramMap.getString("uploadPath") + sep + "thumb" + sep + paramMap.getString("boardId");
      String pollPath = paramMap.getString("uploadPath") + sep + "poll" + sep + paramMap.getString("boardId");

      File attachDir = new File(attachPath);
      File editorDir = new File(editorPath);
      File thumbDir = new File(thumbPath);
      File pollDir = new File(pollPath);

      String[] attachList = attachDir.list();
      String[] editorList = editorDir.list();
      String[] thumbList = thumbDir.list();
      String[] pollList = pollDir.list();

      boolean deletable = false;
      if (attachDir.exists()) {
        deletable = true;
        for (int i = 0; i < attachList.length; i++)
          if (deleteFile(attachPath + sep + attachList[i]) == 0) {
            deletable = false;
            break;
          }
      }
      if (deletable) {
        attachDir.delete();
      }
      deletable = false;
      if (editorDir.exists()) {
        deletable = true;
        for (int i = 0; i < editorList.length; i++)
          if (deleteFile(editorPath + sep + editorList[i]) == 0) {
            deletable = false;
            break;
          }
      }
      if (deletable) {
        editorDir.delete();
      }
      deletable = false;
      if (thumbDir.exists()) {
        deletable = true;
        for (int i = 0; i < thumbList.length; i++)
          if (deleteFile(thumbPath + sep + thumbList[i]) == 0) {
            deletable = false;
            break;
          }
      }
      if (deletable) {
        thumbDir.delete();
      }
      deletable = false;
      if (pollDir.exists()) {
        deletable = true;
        for (int i = 0; i < pollList.length; i++)
          if (deleteFile(pollPath + sep + pollList[i]) == 0) {
            deletable = false;
            break;
          }
      }
      if (deletable)
        pollDir.delete();
    }
    catch (BaseException be) {
      this.logger.error("AdminDAO.boardDelete()", be);
      throw be;
    } catch (Exception e) {
      this.logger.error("AdminDAO.boardDelete()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.boardDelete()");
  }

  public int deleteFile(String filePath) {
    int del = 0;
    File file = new File(filePath);

    if ((file.exists()) && (file.isFile())) {
      del = file.delete() ? 1 : 0;
    }
    if (del == 1)
      this.logger.info("deleteFile::OK! " + filePath);
    else {
      this.logger.info("delteFile::Fail " + filePath);
    }
    return del;
  }

  public void setBltnCate(AdminBoardForm abForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setBltnCate()");

    Connection conn = null;
    PreparedStatement pstmt = null; PreparedStatement langPstmt = null;
    ResultSet result = null;
    StringBuffer sb = new StringBuffer();

    int idx = 0;
    try {
      conn = connCtxt.getConnection();

      if ("ins".equals(abForm.getAct()))
      {
        sb.setLength(0);
        sb.append("SELECT count(lang_knd) FROM bltn_cate_lang WHERE board_id=? AND cate_id=? AND lang_knd=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, abForm.getBoardId());
        idx++; pstmt.setString(idx, abForm.getBltnCateId());
        idx++; pstmt.setString(idx, abForm.getLangKnd());
        result = pstmt.executeQuery();
        if ((result.next()) && 
          (result.getInt(1) > 0)) {
          throw new BaseException("eb.error.duplicated");
        }
        result.close();
        pstmt.close();

        sb.setLength(0);
        sb.append("INSERT INTO bltn_cate");
        sb.append(" (board_id, cate_id, cate_order, upd_user_id, upd_datim)");
        sb.append(" VALUES (?,?,?,?,?)");
        pstmt = conn.prepareStatement(sb.toString());

        idx = 0;
        idx++; pstmt.setString(idx, abForm.getBoardId());
        idx++; pstmt.setString(idx, abForm.getBltnCateId());
        idx++; pstmt.setInt(idx, Integer.parseInt(abForm.getBltnCateOrder()));
        idx++; pstmt.setString(idx, abForm.getUpdUserId());
        idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
        pstmt.executeUpdate();

        sb.setLength(0);
        sb.append("INSERT INTO bltn_cate_lang");
        sb.append(" (board_id, cate_id, lang_knd, cate_nm)");
        sb.append(" VALUES (?,?,?,?)");
        langPstmt = conn.prepareStatement(sb.toString());

        idx = 0;
        idx++; langPstmt.setString(idx, abForm.getBoardId());
        idx++; langPstmt.setString(idx, abForm.getBltnCateId());
        idx++; langPstmt.setString(idx, abForm.getLangKnd());
        idx++; langPstmt.setString(idx, abForm.getBltnCateNm());
        langPstmt.executeUpdate();
      }
      else if ("upd".equals(abForm.getAct()))
      {
        sb.setLength(0);
        sb.append("UPDATE bltn_cate SET cate_order=?, upd_user_id=?, upd_datim=?");
        sb.append(" WHERE board_id=? AND cate_id=?");
        pstmt = conn.prepareStatement(sb.toString());

        sb.setLength(0);
        sb.append("UPDATE bltn_cate_lang SET cate_nm=? WHERE board_id=? AND cate_id=? AND lang_knd=?");
        langPstmt = conn.prepareStatement(sb.toString());

        StringTokenizer stringtokenizer = new StringTokenizer(abForm.getBltnCateId(), ",");
        while (stringtokenizer.hasMoreTokens()) {
          String token = stringtokenizer.nextToken();
          idx = 0;
          idx++; pstmt.setInt(idx, Integer.parseInt(abForm.getBltnCateOrder()));
          idx++; pstmt.setString(idx, abForm.getUpdUserId());
          idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
          idx++; pstmt.setString(idx, abForm.getBoardId());
          idx++; pstmt.setString(idx, token);
          pstmt.executeUpdate();

          idx = 0;
          idx++; langPstmt.setString(idx, abForm.getBltnCateNm());
          idx++; langPstmt.setString(idx, abForm.getBoardId());
          idx++; langPstmt.setString(idx, token);
          idx++; langPstmt.setString(idx, abForm.getLangKnd());
          langPstmt.executeUpdate();
        }
      } else if ("del".equals(abForm.getAct()))
      {
        pstmt = conn.prepareStatement("DELETE FROM bltn_cate WHERE board_id=? AND cate_id=?");
        langPstmt = conn.prepareStatement("DELETE FROM bltn_cate_lang WHERE board_id=? AND cate_id=? AND lang_knd=?");

        StringTokenizer stringtokenizer = new StringTokenizer(abForm.getBltnCateId(), ",");
        while (stringtokenizer.hasMoreTokens()) {
          String token = stringtokenizer.nextToken();
          idx = 0;
          idx++; pstmt.setString(idx, abForm.getBoardId());
          idx++; pstmt.setString(idx, token);
          pstmt.executeUpdate();

          idx = 0;
          idx++; langPstmt.setString(idx, abForm.getBoardId());
          idx++; langPstmt.setString(idx, token);
          idx++; langPstmt.setString(idx, abForm.getLangKnd());
          langPstmt.executeUpdate();
        }
      }
    } catch (BaseException e) {
      throw e;
    } catch (Exception e) {
      this.logger.error("AdminDAO.setBltnCate()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(result);
      close(pstmt);
      close(langPstmt);
    }
    this.logger.info("END::AdminDAO.setBltnCate()");
  }

  public void setBltnCateLang(AdminBoardForm abForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setBltnCateLang()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;
    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();

      int idx = 0;

      if ("ins".equals(abForm.getAct()))
      {
        sb.setLength(0);
        sb.append("SELECT count(lang_knd) FROM bltn_cate_lang WHERE board_id=? AND cate_id=? AND lang_knd=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, abForm.getBoardId());
        idx++; pstmt.setString(idx, abForm.getBltnCateId());
        idx++; pstmt.setString(idx, abForm.getLangKnd());
        result = pstmt.executeQuery();
        if ((result.next()) && 
          (result.getInt(1) > 0)) {
          throw new BaseException("eb.error.already.lang");
        }
        result.close();
        pstmt.close();

        sb.setLength(0);
        sb.append("INSERT INTO bltn_cate_lang (board_id, cate_id, lang_knd, cate_nm) VALUES (?,?,?,?)");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, abForm.getBoardId());
        idx++; pstmt.setString(idx, abForm.getBltnCateId());
        idx++; pstmt.setString(idx, abForm.getLangKnd());
        idx++; pstmt.setString(idx, abForm.getBltnCateNm());
        pstmt.executeUpdate();
      }
      else if ("upd".equals(abForm.getAct()))
      {
        sb.setLength(0);
        sb.append("UPDATE bltn_cate_lang set cate_nm=? WHERE board_id=? AND cate_id=? AND lang_knd=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, abForm.getBltnCateNm());
        idx++; pstmt.setString(idx, abForm.getBoardId());
        idx++; pstmt.setString(idx, abForm.getBltnCateId());
        idx++; pstmt.setString(idx, abForm.getLangKnd());
        pstmt.executeUpdate();
      }
      else if ("del".equals(abForm.getAct()))
      {
        sb.setLength(0);
        sb.append("DELETE FROM bltn_cate_lang WHERE board_id=? AND cate_id=? AND lang_knd=?");
        pstmt = conn.prepareStatement(sb.toString());

        StringTokenizer stnz = new StringTokenizer(abForm.getLangKnd(), ",");
        while (stnz.hasMoreTokens()) {
          String langKnd = stnz.nextToken();
          if (Enview.getConfiguration().getString("portal.default.locale").equals(langKnd))
          {
            continue;
          }
          idx = 0;
          idx++; pstmt.setString(idx, abForm.getBoardId());
          idx++; pstmt.setString(idx, abForm.getBltnCateId());
          idx++; pstmt.setString(idx, langKnd);
          pstmt.executeUpdate();
        }
      }
    }
    catch (BaseException be) {
      this.logger.error("AdminDAO.setBltnCateLang()", be);
      throw be;
    } catch (Exception e) {
      this.logger.error("AdminDAO.setBltnCateLang()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.setBltnCateLang()");
  }

  public List getBoardTblList(AdminAuxilForm aaForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.getBoardTblList()");
    PreparedStatement pstmt = null; PreparedStatement cntPstmt = null;
    ResultSet rslt = null; ResultSet cntRslt = null;
    List tblList = new ArrayList();
    BoardTblVO tableVO = null;

    StringBuffer sb = new StringBuffer();
    try {
      if ("simpleList".equals(aaForm.getCmd())) {
        sb.append("SELECT tbl_id, tbl_fix, tbl_desc FROM board_tbl");
        sb.append(" ORDER BY tbl_fix");

        pstmt = connCtxt.getConnection().prepareStatement(sb.toString());
        rslt = pstmt.executeQuery();

        while (rslt.next()) {
          tableVO = new BoardTblVO();
          tableVO.setTblId(rslt.getString("tbl_id"));
          tableVO.setTblFix(rslt.getString("tbl_fix"));
          tableVO.setTblDesc(rslt.getString("tbl_fix") + "(" + rslt.getString("tbl_desc") + ")");
          tblList.add(tableVO);
        }
      }
      else if ("list".equals(aaForm.getCmd()))
      {
        sb.append("SELECT COUNT(tbl_id) FROM board_tbl");
        sb.append(" WHERE tbl_id > 0");
        if (aaForm.getSrchTableNm() != null)
          sb.append(" AND tbl_fix like '%" + aaForm.getSrchTableNm() + "%'");
        else
          sb.append(" AND tbl_fix like '%'");
        pstmt = connCtxt.getConnection().prepareStatement(sb.toString());
        rslt = pstmt.executeQuery();
        if (rslt.next())
          aaForm.setTotalSize(rslt.getInt(1));
        rslt.close();
        pstmt.close();

        int srnum = 0; int ernum = 0;
        if (aaForm.getPageNo() == 0)
          aaForm.setPageNo(1);
        if (aaForm.getPageSize() == 0)
          aaForm.setPageSize(10);
        srnum = aaForm.getPageNo() == 1 ? 1 : aaForm.getPageNo() * aaForm.getPageSize() - aaForm.getPageSize() + 1;
        ernum = srnum + aaForm.getPageSize() - 1;

        String indexStr = "";
        if ("FIX".equals(aaForm.getSrchOrder()))
          indexStr = "/*+ INDEX(board_tbl board_tbli1)*/";
        else if ("DATIM".equals(aaForm.getSrchOrder())) {
          indexStr = "/*+ INDEX_DESC(board_tbl board_tblp1)*/";
        }

        sb.setLength(0);
        sb.append("SELECT r1.* FROM ");
        sb.append(" ( SELECT " + indexStr);
        sb.append(" rownum rnum, tbl_id, tbl_fix, tbl_desc, upd_datim");
        sb.append("     FROM board_tbl");
        sb.append("    WHERE rownum <= ? AND tbl_id > 0");
        if (aaForm.getSrchTableNm() != null)
          sb.append(" AND tbl_fix like '%" + aaForm.getSrchTableNm() + "%'");
        else
          sb.append(" AND tbl_fix like '%'");
        sb.append(" ) r1");
        sb.append(" WHERE r1.rnum BETWEEN ? AND ?");

        pstmt = connCtxt.getConnection().prepareStatement(sb.toString());
        int idx = 0;
        idx++; pstmt.setInt(idx, ernum);
        idx++; pstmt.setInt(idx, srnum);
        idx++; pstmt.setInt(idx, ernum);
        rslt = pstmt.executeQuery();

        while (rslt.next()) {
          tableVO = new BoardTblVO();
          tableVO.setRnum(rslt.getString("rnum"));
          tableVO.setTblId(rslt.getString("tbl_id"));
          tableVO.setTblFix(rslt.getString("tbl_fix"));
          tableVO.setTblDesc(rslt.getString("tbl_desc"));
          tableVO.setUpdDatim(rslt.getDate("upd_datim"));

          cntPstmt = connCtxt.getConnection().prepareStatement("SELECT COUNT(bltn_no) FROM bltn_" + tableVO.getTblFix());
          cntRslt = cntPstmt.executeQuery();
          if (cntRslt.next())
            tableVO.setRowCnt(cntRslt.getString(1));
          cntRslt.close();
          cntPstmt.close();

          tblList.add(tableVO);
        }
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.getBoardTblList()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(rslt);
      close(pstmt);
      close(cntRslt);
      close(cntPstmt);
    }
    this.logger.info("END::AdminDAO.getBoardTblList()");
    return tblList;
  }

  public BoardTblVO getBoardTbl(String tblId, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminORA.getBoardTbl()");

    PreparedStatement pstmt = null;
    ResultSet rslt = null;
    BoardTblVO tableVO = new BoardTblVO();

    StringBuffer sb = new StringBuffer();
    sb.append("SELECT tbl_id, tbl_fix, tbl_desc, upd_user_id, upd_datim");
    sb.append(" FROM board_tbl");
    sb.append(" WHERE tbl_id = ?");
    try
    {
      pstmt = connCtxt.getConnection().prepareStatement(sb.toString());
      pstmt.setLong(1, Long.parseLong(tblId));
      rslt = pstmt.executeQuery();

      if (rslt.next()) {
        int idx = 0;
        idx++; tableVO.setTblId(rslt.getString(idx));
        idx++; tableVO.setTblFix(rslt.getString(idx));
        idx++; tableVO.setTblDesc(rslt.getString(idx));
        idx++; tableVO.setUpdUserId(rslt.getString(idx));
        idx++; tableVO.setUpdDatim(rslt.getDate(idx));

        pstmt.close();
        rslt.close();

        pstmt = connCtxt.getConnection().prepareStatement("SELECT count(bltn_no) FROM bltn_" + tableVO.getTblFix());
        rslt = pstmt.executeQuery();
        if (rslt.next())
          tableVO.setRnum(rslt.getString(1));
      }
    }
    catch (Exception e) {
      this.logger.error("AdminORA.getBoardTbl()()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(rslt);
      close(pstmt);
    }

    this.logger.info("END::AdminORA.getBoardTbl()");
    return tableVO;
  }

  public void setBoardTbl(AdminAuxilForm aaForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminORA.setBoardTbl()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    Statement stmt = null;
    ResultSet rslt = null;

    int idx = 0;
    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();

      if ("ins".equals(aaForm.getAct()))
      {
        String dbType = Enview.getConfiguration().getString("enview.db.type");
        String fileNm = "make_bulletin_set_" + dbType + ".sql";

        String sep = System.getProperty("file.separator");

        String sqlPath = aaForm.getAppPath() + "/board/resource/" + fileNm;
        File file = new File(sqlPath);
        if ((!file.exists()) || (!file.canRead())) {
          this.logger.info("File is not exist or can't be red::[" + sqlPath + "]");
          throw new Exception();
        }

        BufferedReader br = new BufferedReader(new FileReader(file));
        stmt = conn.createStatement();
        String line = null;
        StringBuffer sbTbl = new StringBuffer();
        String tblFix = null;
        Map tblFixMap = new HashMap();
        tblFixMap.put("BULLETIN", "BLTN_" + aaForm.getTblFix());
        tblFixMap.put("BLTN_CNTT", "BLTN_" + aaForm.getTblFix() + "_CNTT");
        tblFixMap.put("BLTN_MEMO", "BLTN_" + aaForm.getTblFix() + "_MEMO");
        tblFixMap.put("BLTN_FILE", "BLTN_" + aaForm.getTblFix() + "_FILE");
        tblFixMap.put("BLTN_EVAL", "BLTN_" + aaForm.getTblFix() + "_RCMD");
        while ((line = br.readLine()) != null) {
          if ((!"".equals(line)) && (!line.startsWith("REM")) && (!line.startsWith("--"))) {
            if (line.startsWith("CREATE TABLE")) {
              tblFix = line.substring(13, line.indexOf('(')).trim();
              sbTbl.append(line + "\n");
            } else if (line.indexOf(';') > -1) {
              this.logger.info(line);
              line = line.replace(';', ' ');
              sbTbl.append(line + "\n");
              this.logger.info(line);
              this.logger.info(sbTbl);
              this.logger.info(FormatUtil.replace(sbTbl.toString(), tblFix, (String)tblFixMap.get(tblFix), -1));
              stmt.executeUpdate(FormatUtil.replace(sbTbl.toString(), tblFix, (String)tblFixMap.get(tblFix), -1));
              sbTbl.setLength(0);
            } else {
              sbTbl.append(line + "\n");
            }
          }
        }
        br.close();
        sb.setLength(0);
        sb.append("INSERT INTO board_tbl");
        sb.append(" (tbl_id, tbl_fix, tbl_desc, upd_user_id, upd_datim)");
        sb.append(" VALUES (?,?,?,?,?)");
        pstmt = conn.prepareStatement(sb.toString());

        long nextKey = getNextKey("BOARD_TBL");
        idx++; pstmt.setLong(idx, nextKey);
        idx++; pstmt.setString(idx, aaForm.getTblFix());
        idx++; pstmt.setString(idx, aaForm.getTblDesc());
        idx++; pstmt.setString(idx, aaForm.getUpdUserId());
        idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));

        pstmt.executeUpdate();
        pstmt.close();
      }
      else if ("upd".equals(aaForm.getAct()))
      {
        sb.append("UPDATE board_tbl SET tbl_fix=?, tbl_desc=?, upd_user_id=?, upd_datim=?");
        sb.append(" WHERE tbl_id=?");
        pstmt = conn.prepareStatement(sb.toString());

        idx++; pstmt.setString(idx, aaForm.getTblFix());
        idx++; pstmt.setString(idx, aaForm.getTblDesc());
        idx++; pstmt.setString(idx, aaForm.getUpdUserId());
        idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
        idx++; pstmt.setLong(idx, Long.parseLong(aaForm.getTblId()));

        pstmt.executeUpdate();
        pstmt.close();
      }
      else if ("del".equals(aaForm.getAct()))
      {
        sb.append("DELETE FROM board_tbl WHERE tbl_id=?");
        pstmt = conn.prepareStatement(sb.toString());

        idx++; pstmt.setLong(idx, Long.parseLong(aaForm.getTblId()));

        pstmt.executeUpdate();
        pstmt.close();
      }
    }
    catch (Exception e) {
      this.logger.error(e.getMessage(), e);

      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(pstmt);
      close(rslt);
      close(stmt);
    }

    this.logger.info("END::AdminORA.setBoardTbl()");
  }

  public void setBoardFunc(AdminBoardForm abForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setBoardFunc()");

    PreparedStatement pstmt = null; PreparedStatement delPstmt = null; PreparedStatement insPstmt = null; PreparedStatement insPstmt2 = null;
    PreparedStatement userPstmt = null; PreparedStatement groupPstmt = null; PreparedStatement rolePstmt = null;
    ResultSet result = null;
    StringBuffer sb = new StringBuffer();
    int idx = 0;
    try
    {
      Connection conn = connCtxt.getConnection();

      if (("type".equals(abForm.getAct())) || ("tmpl".equals(abForm.getAct())))
      {
        sb.setLength(0);
        sb.append("SELECT owntbl_yn, owntbl_fix FROM board WHERE board_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setString(1, abForm.getBoardId());
        result = pstmt.executeQuery();
        if (result.next()) {
          abForm.setOwntblYn(result.getString(1));

          abForm.setOwntblFix(result.getString(2));
        }

        close(result);
        close(pstmt);
      }
      if ("owntbl".equals(abForm.getAct()))
      {
        sb.setLength(0);
        sb.append("SELECT merge_type FROM board WHERE board_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setString(1, abForm.getBoardId());
        result = pstmt.executeQuery();
        if (result.next()) {
          abForm.setMergeType(result.getString(1));
        }

        close(result);
        close(pstmt);
      }

      List childList = null;
      String boardIdInStr = "";
      if (("tmpl".equals(abForm.getAct())) || ("type".equals(abForm.getAct())) || ("owntbl".equals(abForm.getAct()))) {
        sb.setLength(0);
        sb.append("SELECT cate_id FROM cate_board WHERE board_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setString(1, abForm.getBoardId());
        result = pstmt.executeQuery();
        while (result.next())
        {
          childList = getCateBoardList(result.getString("cate_id"), connCtxt);
          for (int i = 0; i < childList.size(); i++) {
            boardIdInStr = boardIdInStr + ",'" + (String)childList.get(i) + "'";
          }
        }
        if (!"".equals(boardIdInStr)) {
          boardIdInStr = boardIdInStr.substring(1);
        }
        pstmt.close();
      }

      if ((!"".equals(boardIdInStr)) && (!"A".equals(abForm.getMergeType()))) {
        if ("Y".equals(abForm.getOwntblYn()))
        {
          sb.setLength(0);
          sb.append("SELECT COUNT(board_id) FROM board");
          sb.append(" WHERE board_id IN (" + boardIdInStr + ")");
          sb.append(" AND board_rid != ?");

          sb.append(" AND (owntbl_yn = 'N' OR owntbl_fix != ?)");
          pstmt = conn.prepareStatement(sb.toString());
          pstmt.setString(1, abForm.getBoardId());
          pstmt.setString(2, abForm.getOwntblFix());
        }
        else {
          sb.setLength(0);
          sb.append("SELECT COUNT(board_id) FROM board");
          sb.append(" WHERE board_id IN (" + boardIdInStr + ")");
          sb.append(" AND board_rid != ?");

          sb.append(" AND owntbl_yn = 'Y'");
          pstmt = conn.prepareStatement(sb.toString());
          pstmt.setString(1, abForm.getBoardId());
        }
        result = pstmt.executeQuery();
        if ((result.next()) && 
          (result.getInt(1) > 0))
        {
          throw new BaseException("eb.error.owntbl.conflict");
        }

        pstmt.close();
      }

      if (("tmpl".equals(abForm.getAct())) || ("type".equals(abForm.getAct()))) {
        idx = 0;
        sb.setLength(0);
        sb.append("UPDATE board");
        sb.append(" SET board_type=?, merge_type=?, upd_user_id=?, upd_datim=?");
        sb.append(" WHERE board_id=?");

        sb.append(" OR ( board_rid=? AND board_id != board_rid )");
        pstmt = conn.prepareStatement(sb.toString());
        idx++; pstmt.setString(idx, abForm.getBoardType());
        idx++; pstmt.setString(idx, abForm.getMergeType());
      } else if ("func".equals(abForm.getAct())) {
        idx = 0;
        sb.setLength(0);
        sb.append("UPDATE board");
        sb.append(" SET func_yns=?, max_file_cnt=?, max_file_size=?, max_file_down=?, bad_std_cnt=?,");
        if ("Y".equals(abForm.getTermYn())) {
          sb.append(" term_flag=?,");
        }
        if ("Y".equals(abForm.getExtUseYn()))
        {
          sb.append(" extn_class_nm=?,");
        }
        sb.append(" upd_user_id=?, upd_datim=?");
        sb.append(" WHERE board_id=?");
        sb.append(" OR ( board_rid=? AND board_id != board_rid )");
        pstmt = conn.prepareStatement(sb.toString());
        idx++; pstmt.setString(idx, abForm.getFuncYns());
        idx++; pstmt.setInt(idx, abForm.getMaxFileCnt());
        idx++; pstmt.setLong(idx, abForm.getMaxFileSize());
        idx++; pstmt.setInt(idx, abForm.getMaxFileDown());
        idx++; pstmt.setInt(idx, abForm.getBadStdCnt());
        if ("Y".equals(abForm.getTermYn())) {
          idx++; pstmt.setString(idx, abForm.getTermFlag());
        }
        if ("Y".equals(abForm.getExtUseYn()))
        {
          idx++; pstmt.setString(idx, abForm.getExtnClassNm());
        }
      }
      else if ("buga".equals(abForm.getAct()))
      {
        SecurityDAO securityDAO = (SecurityDAO)DAOFactory.getInst().getDAO("com.saltware.enboard.dao.spi.Security");

        String boardSys = "PT";
        String ownGrdYn = "N";
        String orgDelYn = "N";

        sb.setLength(0);
        if ("mssql".equals(Enview.getConfiguration().getString("enview.db.type")))
          sb.append("SELECT board_sys, SUBSTRING(buga_yns,10,1), SUBSTRING(buga_yns,13,1) FROM board WHERE board_id=?");
        else {
          sb.append("SELECT board_sys, SUBSTR(buga_yns,10,1), SUBSTR(buga_yns,13,1) FROM board WHERE board_id=?");
        }
        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setString(1, abForm.getBoardId());
        result = pstmt.executeQuery();
        if (result.next()) {
          boardSys = result.getString(1);
          ownGrdYn = result.getString(2);
          orgDelYn = result.getString(3);
        }
        result.close();
        pstmt.close();

        if (("PT".equals(boardSys)) && (!ownGrdYn.equals(abForm.getOwnGrdYn())))
        {
          if ("Y".equals(abForm.getOwnGrdYn()))
          {
            securityDAO.makeEachGrade("PT" + abForm.getBoardId(), abForm.getDomainId(), abForm.getLangKnd(), connCtxt);

            securityDAO.makeDefaultPermission("PT" + abForm.getBoardId(), abForm.getDomainId(), abForm.getBoardId(), abForm.getLangKnd(), connCtxt);

            securityDAO.removePermission("PT", abForm.getBoardId(), connCtxt);
          }
          else if ("N".equals(abForm.getOwnGrdYn()))
          {
            securityDAO.removePermission("PT" + abForm.getBoardId(), abForm.getBoardId(), connCtxt);

            securityDAO.removeEachGrade("PT" + abForm.getBoardId(), connCtxt);

            securityDAO.makeDefaultPermission("PT", abForm.getDomainId(), abForm.getBoardId(), abForm.getLangKnd(), connCtxt);
          }

        }

        if (("M".equals(orgDelYn)) && (!orgDelYn.equals(abForm.getOrgDelYn())))
        {
          CacheMngr cacheMngr = (CacheMngr)getCacheMngr("EB");
          BoardVO brdVO = cacheMngr.getBoard(abForm.getBoardId(), abForm.getLangKnd());

          sb.setLength(0);
          if ("Y".equals(abForm.getDelFlagYn()))
          {
            sb.append("UPDATE " + brdVO.getBltnTbl() + " SET del_flag='Y' WHERE board_id=? AND del_flag='y'");
          }
          else sb.append("DELETE FROM " + brdVO.getBltnTbl() + " WHERE board_id=? AND del_flag='y'");

          pstmt = conn.prepareStatement(sb.toString());
          pstmt.setString(1, abForm.getBoardId());
          pstmt.executeUpdate();
          pstmt.close();
        }

        idx = 0;
        sb.setLength(0);
        sb.append("UPDATE board");
        sb.append(" SET buga_yns=?, upd_user_id=?, upd_datim=?");
        sb.append(" WHERE board_id=?");
        sb.append(" OR ( board_rid=? AND board_id != board_rid )");
        pstmt = conn.prepareStatement(sb.toString());
        idx++; pstmt.setString(idx, abForm.getBugaYns());
      } else if ("srch".equals(abForm.getAct())) {
        idx = 0;
        sb.setLength(0);
        sb.append("UPDATE board");
        sb.append(" SET srch_yns=?, upd_user_id=?, upd_datim=?");
        sb.append(" WHERE board_id=?");
        sb.append(" OR ( board_rid=? AND board_id != board_rid )");
        pstmt = conn.prepareStatement(sb.toString());
        idx++; pstmt.setString(idx, abForm.getSrchYns());
      } else if ("owntbl".equals(abForm.getAct())) {
        sb.setLength(0);
        sb.append("UPDATE board");
        sb.append(" SET owntbl_yn=?, owntbl_fix=?, upd_user_id=?, upd_datim=?");
        sb.append(" WHERE board_id=?");
        sb.append(" OR ( board_rid=? AND board_id != board_rid )");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, abForm.getOwntblYn());
        idx++; pstmt.setString(idx, abForm.getOwntblFix());
      }
      idx++; pstmt.setString(idx, abForm.getUpdUserId());
      idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
      idx++; pstmt.setString(idx, abForm.getBoardId());
      idx++; pstmt.setString(idx, abForm.getBoardId());
      pstmt.executeUpdate();

      if ("tmpl".equals(abForm.getAct()))
        applyTemplate(connCtxt, abForm.getBoardId(), abForm.getBoardType());
    }
    catch (BaseException be)
    {
      this.logger.error("AdminDAO.setBoardFunc()", be);
      throw be;
    } catch (Exception e) {
      this.logger.error("AdminDAO.setBoardFunc()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(pstmt);
      close(delPstmt);
      close(insPstmt);
      close(insPstmt2);
      close(userPstmt);
      close(groupPstmt);
      close(rolePstmt);
    }
    this.logger.info("END::AdminDAO.setBoardFunc()");
  }

  public List getCateBoardList(String parentCateId, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setCateBoardList()");
    ArrayList cateList = new ArrayList();
    ArrayList boardList = new ArrayList();

    cateList.add(parentCateId);

    cateList.addAll(getChildCatebase(parentCateId, true, null, connCtxt));
    if (cateList.size() > 1)
    {
      for (int i = 1; i < cateList.size(); i++)
      {
        String newPCId = (String)cateList.get(i);
        ArrayList childCateList = (ArrayList)getChildCatebase(newPCId, true, null, connCtxt);
        if (childCateList.size() <= 0)
          continue;
        cateList.addAll(cateList.size(), childCateList);
      }

    }

    if (cateList.size() > 0) {
      boardList = new ArrayList();
      AdminCateForm acForm = new AdminCateForm();
      for (int i = 0; i < cateList.size(); i++) {
        acForm.setCateId((String)cateList.get(i));
        boardList.addAll(getCateBoard(acForm, null, true, connCtxt));
      }
    }

    this.logger.info("END::AdminDAO.getCateBoardList()");
    return boardList;
  }

  public int getBltnCnt(BoardVO boardVO, ConnectionContext connCtxt)
    throws BaseException
  {
    if (this.logger.isDebugEnabled())
      this.logger.debug("BEGIN::adminDAO.getBltnCnt()");
    PreparedStatement pstmt = null;
    ResultSet result = null;

    int bltnCnt = 0;
    try {
      pstmt = connCtxt.getConnection().prepareStatement("SELECT COUNT(board_id) FROM " + boardVO.getBltnTbl() + " WHERE board_id=?");
      if (boardVO.getBoardId().equals(boardVO.getBoardRid()))
        pstmt.setString(1, boardVO.getBoardId());
      else {
        pstmt.setString(1, boardVO.getBoardRid());
      }
      result = pstmt.executeQuery();
      if (result.next())
        bltnCnt = result.getInt(1);
    }
    catch (Exception e) {
      if (this.logger.isErrorEnabled())
        this.logger.error("adminDAO.getBltnCnt()", e);
      throw new BaseException("mm.error.sql.problem", e);
    } finally {
      close(result);
      close(pstmt);
    }
    if (this.logger.isDebugEnabled())
      this.logger.debug("END::adminDAO.getBltnCnt()");
    return bltnCnt;
  }

  public void setBoardBojo(AdminBoardForm abForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setBoardBojo()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;
    StringBuffer sb = new StringBuffer();

    label1746: 
    try { if ("prop".equals(abForm.getAct()))
      {
        conn = connCtxt.getConnection();

        if ("RESTORE".equals(abForm.getCmd())) {
          sb.setLength(0);
          sb.append("SELECT input_term, eval_level, best_limit, ext_mask, bad_ext_mask, accept_ip, accept_id, bad_ip, bad_id, bad_nick, bad_cntt, bad_nick_yn, bad_cntt_yn");
          sb.append("  FROM board_prop");
          sb.append(" WHERE board_id = 'ENBOARD.BASE'");

          pstmt = conn.prepareStatement(sb.toString());
          result = pstmt.executeQuery();

          if (result.next()) {
            int idx = 0;
            idx++; abForm.setInputTerm(result.getInt(idx));
            idx++; abForm.setEvalLevel(result.getString(idx));
            idx++; abForm.setBestLimit(result.getString(idx));
            idx++; abForm.setExtMask(result.getString(idx));
            idx++; abForm.setBadExtMask(result.getString(idx));
            idx++; abForm.setAcceptIp(result.getString(idx));
            idx++; abForm.setAcceptId(result.getString(idx));
            idx++; abForm.setBadIp(result.getString(idx));
            idx++; abForm.setBadId(result.getString(idx));
            idx++; abForm.setBadNick(result.getString(idx));
            idx++; abForm.setBadCntt(result.getString(idx));
            idx++; abForm.setBadNickYn(result.getString(idx));
            idx++; abForm.setBadCnttYn(result.getString(idx));
          }
          pstmt.close();
        }
        sb.setLength(0);
        sb.append("SELECT count(board_id)");
        sb.append(" FROM board_prop");
        sb.append(" WHERE board_id = ?");

        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setString(1, abForm.getBoardId());
        result = pstmt.executeQuery();
        boolean exist = false;
        if (result.next()) {
          int c = result.getInt(1);
          if (c > 0)
            exist = true;
        }
        pstmt.close();

        abForm.setEvalLevel(StringUtils.deleteWhitespace(abForm.getEvalLevel()));
        abForm.setEvalLevel(StringUtils.replace(abForm.getEvalLevel(), "\r\n", ""));
        abForm.setBestLimit(StringUtils.deleteWhitespace(abForm.getBestLimit()));
        abForm.setBestLimit(StringUtils.replace(abForm.getBestLimit(), "\r\n", ""));
        abForm.setExtMask(StringUtils.deleteWhitespace(abForm.getExtMask()));
        abForm.setExtMask(StringUtils.replace(abForm.getExtMask(), "\r\n", ""));
        abForm.setBadExtMask(StringUtils.deleteWhitespace(abForm.getBadExtMask()));
        abForm.setBadExtMask(StringUtils.replace(abForm.getBadExtMask(), "\r\n", ""));
        abForm.setAcceptIp(StringUtils.deleteWhitespace(abForm.getAcceptIp()));
        abForm.setAcceptIp(StringUtils.replace(abForm.getAcceptIp(), "\r\n", ""));
        abForm.setAcceptId(StringUtils.deleteWhitespace(abForm.getAcceptId()));
        abForm.setAcceptId(StringUtils.replace(abForm.getAcceptId(), "\r\n", ""));
        abForm.setBadIp(StringUtils.deleteWhitespace(abForm.getBadIp()));
        abForm.setBadIp(StringUtils.replace(abForm.getBadIp(), "\r\n", ""));
        abForm.setBadId(StringUtils.deleteWhitespace(abForm.getBadId()));
        abForm.setBadId(StringUtils.replace(abForm.getBadId(), "\r\n", ""));
        abForm.setBadNick(StringUtils.deleteWhitespace(abForm.getBadNick()));
        abForm.setBadNick(StringUtils.replace(abForm.getBadNick(), "\r\n", ""));
        abForm.setBadCntt(StringUtils.deleteWhitespace(abForm.getBadCntt()));
        abForm.setBadCntt(StringUtils.replace(abForm.getBadCntt(), "\r\n", ""));

        sb.setLength(0);
        if (exist) {
          sb.append("UPDATE board_prop");
          sb.append(" SET input_term=?, eval_level=?, best_limit=?, ext_mask=?, bad_ext_mask=?, accept_ip=?, accept_id=?, bad_ip=?, bad_id=?, bad_nick=?, bad_cntt=?, bad_nick_yn=?, bad_cntt_yn=?");
          sb.append(" WHERE board_id = ?");

          pstmt = conn.prepareStatement(sb.toString());

          int idx = 0;
          idx++; pstmt.setInt(idx, abForm.getInputTerm());
          idx++; pstmt.setString(idx, abForm.getEvalLevel());
          idx++; pstmt.setString(idx, abForm.getBestLimit());
          idx++; pstmt.setString(idx, abForm.getExtMask());
          idx++; pstmt.setString(idx, abForm.getBadExtMask());
          idx++; pstmt.setString(idx, abForm.getAcceptIp());
          idx++; pstmt.setString(idx, abForm.getAcceptId());
          idx++; pstmt.setString(idx, abForm.getBadIp());
          idx++; pstmt.setString(idx, abForm.getBadId());
          idx++; pstmt.setString(idx, abForm.getBadNick());
          idx++; pstmt.setString(idx, abForm.getBadCntt());
          idx++; pstmt.setString(idx, abForm.getBadNickYn());
          idx++; pstmt.setString(idx, abForm.getBadCnttYn());
          idx++; pstmt.setString(idx, abForm.getBoardId());

          pstmt.executeUpdate();

          break label1746;
        }
        if (!"RESTORE".equals(abForm.getAct()))
        {
          sb.append("INSERT INTO board_prop");
          sb.append(" SELECT ?, ?, ?, ?, ?, ?,");
          sb.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ");
          sb.append(" FROM board_prop WHERE board_id=? ");

          pstmt = conn.prepareStatement(sb.toString());

          int idx = 0;
          idx++; pstmt.setString(idx, abForm.getBoardId());
          idx++; pstmt.setInt(idx, abForm.getInputTerm());
          idx++; pstmt.setString(idx, abForm.getEvalLevel());
          idx++; pstmt.setString(idx, abForm.getBestLimit());
          idx++; pstmt.setString(idx, abForm.getExtMask());
          idx++; pstmt.setString(idx, abForm.getBadExtMask());
          idx++; pstmt.setString(idx, abForm.getAcceptIp());
          idx++; pstmt.setString(idx, abForm.getAcceptId());
          idx++; pstmt.setString(idx, abForm.getBadIp());
          idx++; pstmt.setString(idx, abForm.getBadId());
          idx++; pstmt.setString(idx, abForm.getBadNick());
          idx++; pstmt.setString(idx, abForm.getBadCntt());
          idx++; pstmt.setString(idx, abForm.getBadNickYn());
          idx++; pstmt.setString(idx, abForm.getBadCnttYn());
          idx++; pstmt.setString(idx, abForm.getUpdUserId());
          idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
          idx++; pstmt.setString(idx, "ENBOARD.BASE");

          pstmt.executeUpdate();

          break label1746;
        } } else if ("cntt".equals(abForm.getAct()))
      {
        String sep = System.getProperty("file.separator");
        String filePath = Enview.getConfiguration().getString("board.resource.path");
        String fileNm = "bad_cntt.txt";

        if (filePath == null)
          fileNm = abForm.getRealPath() + sep + "board" + sep + "resource" + sep + fileNm;
        else {
          fileNm = filePath + sep + fileNm;
        }

        File badCnttFile = new File(fileNm);
        if (badCnttFile.exists()) {
          if (!badCnttFile.canWrite()) {
            this.logger.info("File cant't be written::[" + badCnttFile.getPath() + "]");
            throw new BaseException("eb.error.cant.write.file");
          }
        } else {
          this.logger.info("File is not exist::[" + badCnttFile.getPath() + "]");
          throw new BaseException("eb.error.not.exist.file");
        }
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(badCnttFile), "UTF-8");
        abForm.setBadCnttCommon(StringUtils.deleteWhitespace(abForm.getBadCnttCommon()));
        abForm.setBadCnttCommon(StringUtils.replace(abForm.getBadCnttCommon(), "\r\n", ""));
        osw.write(abForm.getBadCnttCommon());
        osw.close();
      }
    } catch (BaseException be) {
      this.logger.error("AdminDAO.setBoardBojo()", be);
      throw be;
    } catch (Exception e) {
      this.logger.error("AdminDAO.setBoardBojo()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.setBoardBojo()");
  }

  public void setBoardScrn(AdminBoardForm abForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setBoardScrn()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();
      int idx = 0;

      if ("base".equals(abForm.getAct())) {
        sb.append("UPDATE board SET board_skin=?, raise_color=?, raise_cnt=?, new_term=?, list_set_cnt=?, mini_trgt_win=?, mini_trgt_url=?, board_width=?, top_html=?, bottom_html=?, board_bg_pic=?, board_bg_color=?,");
        sb.append(" upd_user_id=?, upd_datim=?");
        sb.append(" WHERE board_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx++; pstmt.setString(idx, abForm.getBoardSkin());
        idx++; pstmt.setString(idx, abForm.getRaiseColor());
        idx++; pstmt.setInt(idx, abForm.getRaiseCnt());
        idx++; pstmt.setInt(idx, abForm.getNewTerm());
        idx++; pstmt.setInt(idx, abForm.getListSetCnt());
        idx++; pstmt.setString(idx, abForm.getMiniTrgtWin());
        idx++; pstmt.setString(idx, abForm.getMiniTrgtUrl());
        idx++; pstmt.setInt(idx, abForm.getBoardWidth());
        idx++; pstmt.setString(idx, abForm.getTopHtml());
        idx++; pstmt.setString(idx, abForm.getBottomHtml());
        idx++; pstmt.setString(idx, abForm.getBoardBgPic());
        idx++; pstmt.setString(idx, abForm.getBoardBgColor());
      }
      else if ("ttl".equals(abForm.getAct())) {
        sb.append("UPDATE board SET ttl_yns=?, upd_user_id=?, upd_datim=? WHERE board_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx++; pstmt.setString(idx, abForm.getTtlYns());
      }
      else if ("size".equals(abForm.getAct())) {
        sb.append("UPDATE board SET ttl_lens=?, upd_user_id=?, upd_datim=? WHERE board_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx++; pstmt.setString(idx, abForm.getTtlLens());
      }
      else if ("listread".equals(abForm.getAct())) {
        sb.append("UPDATE board SET list_yns=?, read_yns=?, upd_user_id=?, upd_datim=? WHERE board_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx++; pstmt.setString(idx, abForm.getListYns());
        idx++; pstmt.setString(idx, abForm.getReadYns());
      }
      idx++; pstmt.setString(idx, abForm.getUpdUserId());
      idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
      idx++; pstmt.setString(idx, abForm.getBoardId());
      pstmt.executeUpdate();
    } catch (Exception e) {
      this.logger.error("AdminDAO.setBoardScrn()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.setBoardScrn()");
  }

  public void setBltnExtnProp(AdminBoardForm abForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setBltnExtnProp()");

    Connection conn = null;
    PreparedStatement pstmt = null; PreparedStatement langPstmt = null;
    StringBuffer sb = new StringBuffer();

    int idx = 0;
    try {
      conn = connCtxt.getConnection();

      if ("ins".equals(abForm.getAct())) {
        sb.setLength(0);
        sb.append("INSERT INTO bltn_extn_prop");
        sb.append(" (board_id, fld_nm, use_yn, ttl_yn, srch_yn, data_type, util_class_nm, upd_user_id, upd_datim)");
        sb.append(" VALUES (?,?,?,?,?,?,?,?,?)");
        pstmt = conn.prepareStatement(sb.toString());

        idx = 0;
        idx++; pstmt.setString(idx, abForm.getBoardId());
        idx++; pstmt.setString(idx, abForm.getFldNm());
        idx++; pstmt.setString(idx, abForm.getUseYn());
        idx++; pstmt.setString(idx, abForm.getTtlYn());
        idx++; pstmt.setString(idx, abForm.getSrchYn());
        idx++; pstmt.setString(idx, abForm.getDataType());
        idx++; pstmt.setString(idx, abForm.getUtilClassNm());
        idx++; pstmt.setString(idx, abForm.getUpdUserId());
        idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
        pstmt.executeUpdate();

        sb.setLength(0);
        sb.append("INSERT INTO bltn_extn_prop_lang");
        sb.append(" (board_id, fld_nm, lang_knd, title)");
        sb.append(" VALUES (?,?,?,?)");
        langPstmt = conn.prepareStatement(sb.toString());

        idx = 0;
        idx++; langPstmt.setString(idx, abForm.getBoardId());
        idx++; langPstmt.setString(idx, abForm.getFldNm());
        idx++; langPstmt.setString(idx, abForm.getLangKnd());
        idx++; langPstmt.setString(idx, abForm.getTitle());
        langPstmt.executeUpdate();
      }
      else if ("upd".equals(abForm.getAct()))
      {
        sb.setLength(0);
        sb.append("UPDATE bltn_extn_prop SET use_yn=?, ttl_yn=?, srch_yn=?, data_type=?, util_class_nm=?, upd_user_id=?, upd_datim=?");
        sb.append(" WHERE board_id=? AND fld_nm=?");
        pstmt = conn.prepareStatement(sb.toString());

        sb.setLength(0);
        sb.append("UPDATE bltn_extn_prop_lang SET title=? WHERE board_id=? AND fld_nm=? AND lang_knd=?");
        langPstmt = conn.prepareStatement(sb.toString());

        idx = 0;
        idx++; pstmt.setString(idx, abForm.getUseYn());
        idx++; pstmt.setString(idx, abForm.getTtlYn());
        idx++; pstmt.setString(idx, abForm.getSrchYn());
        idx++; pstmt.setString(idx, abForm.getDataType());
        idx++; pstmt.setString(idx, abForm.getUtilClassNm());
        idx++; pstmt.setString(idx, abForm.getUpdUserId());
        idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
        idx++; pstmt.setString(idx, abForm.getBoardId());
        idx++; pstmt.setString(idx, abForm.getFldNm());
        pstmt.executeUpdate();

        idx = 0;
        idx++; langPstmt.setString(idx, abForm.getTitle());
        idx++; langPstmt.setString(idx, abForm.getBoardId());
        idx++; langPstmt.setString(idx, abForm.getFldNm());
        idx++; langPstmt.setString(idx, abForm.getLangKnd());
        langPstmt.executeUpdate();
      }
      else if ("del".equals(abForm.getAct()))
      {
        pstmt = conn.prepareStatement("DELETE FROM bltn_extn_prop WHERE board_id=? AND fld_nm=?");
        langPstmt = conn.prepareStatement("DELETE FROM bltn_extn_prop_lang WHERE board_id=? AND fld_nm=? AND lang_knd=?");

        StringTokenizer stringtokenizer = new StringTokenizer(abForm.getFldNm(), ",");
        while (stringtokenizer.hasMoreTokens()) {
          String token = stringtokenizer.nextToken();
          idx = 0;
          pstmt.clearParameters();
          idx++; pstmt.setString(idx, abForm.getBoardId());
          idx++; pstmt.setString(idx, token);
          pstmt.executeUpdate();

          idx = 0;
          langPstmt.clearParameters();
          idx++; langPstmt.setString(idx, abForm.getBoardId());
          idx++; langPstmt.setString(idx, token);
          idx++; langPstmt.setString(idx, abForm.getLangKnd());
          langPstmt.executeUpdate();
        }
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.setBltnExtnProp()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(pstmt);
      close(langPstmt);
    }
    this.logger.info("END::AdminDAO.setBltnExtnProp()");
  }

  public void setBltnExtnPropLang(AdminBoardForm abForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setBltnExtnPropLang()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;
    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();

      int idx = 0;

      if ("ins".equals(abForm.getAct()))
      {
        sb.setLength(0);
        sb.append("SELECT count(lang_knd) FROM bltn_extn_prop_lang WHERE board_id=? AND fld_nm=? AND lang_knd=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, abForm.getBoardId());
        idx++; pstmt.setString(idx, abForm.getFldNm());
        idx++; pstmt.setString(idx, abForm.getLangKnd());
        result = pstmt.executeQuery();
        if ((result.next()) && 
          (result.getInt(1) > 0)) {
          throw new BaseException("eb.error.already.lang");
        }
        result.close();
        pstmt.close();

        sb.setLength(0);
        sb.append("INSERT INTO bltn_extn_prop_lang (board_id, fld_nm, lang_knd, title) VALUES (?,?,?,?)");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, abForm.getBoardId());
        idx++; pstmt.setString(idx, abForm.getFldNm());
        idx++; pstmt.setString(idx, abForm.getLangKnd());
        idx++; pstmt.setString(idx, abForm.getTitle());
        pstmt.executeUpdate();
        pstmt.close();
      }
      else if ("upd".equals(abForm.getAct()))
      {
        sb.setLength(0);
        sb.append("UPDATE bltn_extn_prop_lang set title=? WHERE board_id=? AND fld_nm=? AND lang_knd=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, abForm.getTitle());
        idx++; pstmt.setString(idx, abForm.getBoardId());
        idx++; pstmt.setString(idx, abForm.getFldNm());
        idx++; pstmt.setString(idx, abForm.getLangKnd());
        pstmt.executeUpdate();
        pstmt.close();
      } else if ("del".equals(abForm.getAct()))
      {
        sb.setLength(0);
        sb.append("DELETE FROM bltn_extn_prop_lang WHERE board_id=? AND fld_nm=? AND lang_knd=?");
        pstmt = conn.prepareStatement(sb.toString());

        StringTokenizer stnz = new StringTokenizer(abForm.getLangKnd(), ",");
        while (stnz.hasMoreTokens()) {
          String langKnd = stnz.nextToken();
          if (Enview.getConfiguration().getString("portal.default.locale").equals(langKnd))
          {
            continue;
          }
          idx = 0;
          pstmt.clearParameters();
          idx++; pstmt.setString(idx, abForm.getBoardId());
          idx++; pstmt.setString(idx, abForm.getFldNm());
          idx++; pstmt.setString(idx, langKnd);
          pstmt.executeUpdate();
        }

        pstmt.close();
      }
    } catch (BaseException be) {
      this.logger.error("AdminDAO.setBltnExtnPropLang()", be);
      throw be;
    } catch (Exception e) {
      this.logger.error("AdminDAO.setBltnExtnPropLang()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.setBltnExtnPropLang()");
  }

  public void setBoardAuth(AdminAuxilForm aaForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setBoardAuth()");

    Connection conn = null;
    PreparedStatement pstmt = null; PreparedStatement pstmt2 = null; PreparedStatement updPstmt = null; PreparedStatement insPstmt = null; PreparedStatement delPstmt = null;
    ResultSet result = null; ResultSet result2 = null;
    StringBuffer sb = new StringBuffer();
    List gradeList = null;

    int idx = 0;
    try {
      conn = connCtxt.getConnection();

      if ("gradeAuth".equals(aaForm.getAct()))
      {
        sb.append("SELECT permission_id FROM security_permission");
        sb.append(" WHERE res_type=5 AND principal_id = ( SELECT principal_id FROM security_principal");
        sb.append(" WHERE principal_type='g' AND short_path=? )");
        sb.append(" AND res_url=?");
        pstmt = conn.prepareStatement(sb.toString());

        sb.setLength(0);
        sb.append("UPDATE security_permission SET action_mask=?");
        sb.append(" WHERE permission_id=?");
        updPstmt = conn.prepareStatement(sb.toString());

        sb.setLength(0);
        sb.append("DELETE FROM security_permission ");
        sb.append(" WHERE permission_id=?");
        delPstmt = conn.prepareStatement(sb.toString());

        sb.setLength(0);
        sb.append("SELECT principal_id FROM security_principal WHERE principal_type='g' AND short_path=?");
        pstmt2 = conn.prepareStatement(sb.toString());

        sb.setLength(0);
        sb.append("INSERT INTO security_permission");
        sb.append(" SELECT ?, ?, ?, board_nm, ?, 5, ?, 1, ?, ? FROM board_lang");
        sb.append(" WHERE board_id=? AND lang_knd=?");
        insPstmt = conn.prepareStatement(sb.toString());

        SecurityDAO securityDAO = (SecurityDAO)DAOFactory.getInst().getDAO("com.saltware.enboard.dao.spi.Security");

        gradeList = securityDAO.getGradeList(aaForm.getGradePrefix(), connCtxt);

        Class[] clazz = new Class[0];
        Iterator it = gradeList.iterator();
        while (it.hasNext()) {
          CodebaseVO cdVO = (CodebaseVO)it.next();

          String actionMasks = (String)aaForm.getClass().getDeclaredMethod("getAuthGrcd" + cdVO.getCode(), clazz).invoke(aaForm, clazz);
          StringTokenizer stnz = new StringTokenizer(actionMasks, ",");

          int actionMask = 0;
          while (stnz.hasMoreTokens()) {
            actionMask += Integer.parseInt(stnz.nextToken());
          }

          idx = 0;
          String shortPath = null;

          shortPath = "/" + aaForm.getGradePrefix() + "/" + cdVO.getCode();

          idx++; pstmt.setString(idx, shortPath);
          idx++; pstmt.setString(idx, aaForm.getBoardId());
          result = pstmt.executeQuery();

          if (result.next()) {
            if (actionMask > 0) {
              idx = 0;
              idx++; updPstmt.setInt(idx, actionMask);
              idx++; updPstmt.setLong(idx, result.getLong("permission_id"));
              updPstmt.executeUpdate();
            }
            else {
              idx = 0;
              idx++; delPstmt.setLong(idx, result.getLong("permission_id"));
              delPstmt.executeUpdate();
            }
          } else {
            idx = 0;
            idx++; pstmt2.setString(idx, shortPath);
            result2 = pstmt2.executeQuery();

            if (result2.next()) {
              idx = 0;
              idx++; insPstmt.setLong(idx, getNextKey("SECURITY_PERMISSION"));
              idx++; insPstmt.setLong(idx, result2.getLong("principal_id"));
              idx++; insPstmt.setLong(idx, aaForm.getDomainId());
              idx++; insPstmt.setString(idx, aaForm.getBoardId());
              idx++; insPstmt.setInt(idx, actionMask);
              idx++; insPstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
              idx++; insPstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
              idx++; insPstmt.setString(idx, aaForm.getBoardId());
              idx++; insPstmt.setString(idx, aaForm.getLangKnd());
              insPstmt.executeUpdate();
            } else {
              throw new BaseException("eb.error.not.exist", new Object[] { "SECURITY_PRINCIPAL(PRINCIPAL_TYPE='g',SHORT_PATH='" + shortPath + "')" });
            }
          }
        }
      } else {
        pstmt = conn.prepareStatement("SELECT permission_id FROM security_permission WHERE res_type=? AND res_url=? AND principal_id=?");
        updPstmt = conn.prepareStatement("UPDATE security_permission SET action_mask=? WHERE permission_id=?");
        sb.append("INSERT INTO security_permission");
        sb.append(" SELECT ?, ?, ?, board_nm, ?, ?, ?, 1, ?, ? FROM board_lang");
        sb.append(" WHERE board_id=? AND lang_knd=?");
        insPstmt = conn.prepareStatement(sb.toString());

        sb.setLength(0);
        sb.append("DELETE FROM security_permission ");
        sb.append(" WHERE permission_id=?");
        delPstmt = conn.prepareStatement(sb.toString());

        String actionMasks = "";
        if ("everyAuth".equals(aaForm.getAct()))
          actionMasks = aaForm.getAuthGrcdE();
        else if ("roleAuth".equals(aaForm.getAct()))
          actionMasks = aaForm.getAuthGrcdR();
        else if ("groupAuth".equals(aaForm.getAct()))
          actionMasks = aaForm.getAuthGrcdG();
        else if ("userAuth".equals(aaForm.getAct())) {
          actionMasks = aaForm.getAuthGrcdU();
        }
        actionMasks = actionMasks != null ? actionMasks : "";

        StringTokenizer stnz = new StringTokenizer(actionMasks, ",");
        int actionMask = 0;
        while (stnz.hasMoreTokens()) {
          actionMask += Integer.parseInt(stnz.nextToken());
        }

        if (ValidateUtil.isEmpty(aaForm.getRguIds()))
          aaForm.setRguIds("0");
        stnz = new StringTokenizer(aaForm.getRguIds(), ",");
        String rguId = "";
        long pmsnId = 0L;
        while (stnz.hasMoreTokens())
        {
          rguId = stnz.nextToken();

          idx = 0;
          if ("everyBefore".equals(rguId)) {
            idx++; pstmt.setInt(idx, 50);
            idx++; pstmt.setString(idx, aaForm.getBoardId());
            idx++; pstmt.setLong(idx, 0L);
          } else if ("everyAfter".equals(rguId)) {
            idx++; pstmt.setInt(idx, 51);
            idx++; pstmt.setString(idx, aaForm.getBoardId());
            idx++; pstmt.setLong(idx, 0L);
          } else {
            idx++; pstmt.setInt(idx, 5);
            idx++; pstmt.setString(idx, aaForm.getBoardId());
            idx++; pstmt.setLong(idx, Long.parseLong(rguId));
          }
          result = pstmt.executeQuery();
          if (result.next())
            pmsnId = result.getLong(1);
          else {
            pmsnId = 0L;
          }
          result.close();

          idx = 0;
          if (pmsnId == 0L) {
            idx++; insPstmt.setLong(idx, getNextKey("SECURITY_PERMISSION"));
            if ("everyBefore".equals(rguId)) {
              idx++; insPstmt.setLong(idx, 0L);
              idx++; insPstmt.setLong(idx, aaForm.getDomainId());
              idx++; insPstmt.setString(idx, aaForm.getBoardId());
              idx++; insPstmt.setInt(idx, 50);
            } else if ("everyAfter".equals(rguId)) {
              idx++; insPstmt.setLong(idx, 0L);
              idx++; insPstmt.setLong(idx, aaForm.getDomainId());
              idx++; insPstmt.setString(idx, aaForm.getBoardId());
              idx++; insPstmt.setInt(idx, 51);
            } else {
              idx++; insPstmt.setLong(idx, Long.parseLong(rguId));
              idx++; insPstmt.setLong(idx, aaForm.getDomainId());
              idx++; insPstmt.setString(idx, aaForm.getBoardId());
              idx++; insPstmt.setInt(idx, 5);
            }
            idx++; insPstmt.setInt(idx, actionMask);
            idx++; insPstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
            idx++; insPstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
            idx++; insPstmt.setString(idx, aaForm.getBoardId());
            idx++; insPstmt.setString(idx, aaForm.getLangKnd());
            insPstmt.executeUpdate();
          }
          else if (actionMask > 0) {
            idx++; updPstmt.setInt(idx, actionMask);
            idx++; updPstmt.setLong(idx, pmsnId);
            updPstmt.executeUpdate();
          } else {
            idx++; delPstmt.setLong(idx, pmsnId);
            delPstmt.executeUpdate();
          }

        }

      }

    }
    catch (Exception e)
    {
      this.logger.error("AdminDAO.setBoardAuth()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(result);
      close(result2);
      close(pstmt);
      close(pstmt2);
      close(updPstmt);
      close(insPstmt);
    }
    this.logger.info("END::AdminDAO.setBoardAuth()");
  }

  public BoardSnntVO getBoardSnnt(char fetchOption, String boardId, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.getBoardSnnt()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;
    BoardSnntVO snntVO = new BoardSnntVO();

    StringBuffer sb = new StringBuffer();

    if (ValidateUtil.isEmpty(boardId)) {
      return snntVO;
    }
    sb.append("SELECT snnt_kind, sms_datasource, sms_jdbc_driver, sms_jdbc_url, sms_jdbc_user,");
    sb.append(" sms_jdbc_pass, sms_cntt_sql, sms_ins_sql, sms_cnt_sql, email_svr_addr,");
    sb.append(" email_user, email_pass, email_sender_id, email_subj, email_cntt, upd_user_id, upd_datim");
    sb.append(" FROM board_snnt");
    sb.append(" WHERE board_id = ?");
    try
    {
      conn = connCtxt.getConnection();

      pstmt = conn.prepareStatement(sb.toString());
      pstmt.setString(1, boardId);
      result = pstmt.executeQuery();

      snntVO.setBoardId(boardId);

      if (result.next()) {
        snntVO.setSnntKind(result.getString("snnt_kind"));
        snntVO.setSmsDatasource(result.getString("sms_datasource"));
        snntVO.setSmsJdbcDriver(result.getString("sms_jdbc_driver"));
        snntVO.setSmsJdbcUrl(result.getString("sms_jdbc_url"));
        snntVO.setSmsJdbcUser(result.getString("sms_jdbc_user"));
        snntVO.setSmsJdbcPass(result.getString("sms_jdbc_pass"));
        snntVO.setSmsCnttSql(result.getString("sms_cntt_sql"));
        snntVO.setSmsInsSql(result.getString("sms_ins_sql"));
        snntVO.setSmsCntSql(result.getString("sms_cnt_sql"));
        snntVO.setEmailSvrAddr(result.getString("email_svr_addr"));
        snntVO.setEmailUser(result.getString("email_user"));
        snntVO.setEmailPass(result.getString("email_pass"));
        snntVO.setEmailSenderId(result.getString("email_sender_id"));
        snntVO.setEmailSubj(result.getString("email_subj"));
        snntVO.setEmailCntt(result.getString("email_cntt"));
        snntVO.setUpdUserId(result.getString("upd_user_id"));
        snntVO.setUpdDatim(result.getTimestamp("upd_datim"));
      }

      if ('U' == fetchOption) if (!"0".equals(boardId))
        {
          result.close();
          pstmt.setString(1, "0");
          result = pstmt.executeQuery();

          if (result.next()) {
            if ((snntVO.getSnntKind() == null) || (snntVO.getSnntKind().length() == 0))
              snntVO.setSnntKind(result.getString("snnt_kind"));
            if ((snntVO.getSmsDatasource() == null) || (snntVO.getSmsDatasource().length() == 0))
              snntVO.setSmsDatasource(result.getString("sms_datasource"));
            if ((snntVO.getSmsJdbcDriver() == null) || (snntVO.getSmsJdbcDriver().length() == 0))
              snntVO.setSmsJdbcDriver(result.getString("sms_jdbc_driver"));
            if ((snntVO.getSmsJdbcUrl() == null) || (snntVO.getSmsJdbcUrl().length() == 0))
              snntVO.setSmsJdbcUrl(result.getString("sms_jdbc_url"));
            if ((snntVO.getSmsJdbcUser() == null) || (snntVO.getSmsJdbcUser().length() == 0))
              snntVO.setSmsJdbcUser(result.getString("sms_jdbc_user"));
            if ((snntVO.getSmsJdbcPass() == null) || (snntVO.getSmsJdbcPass().length() == 0))
              snntVO.setSmsJdbcPass(result.getString("sms_jdbc_pass"));
            if ((snntVO.getSmsCnttSql() == null) || (snntVO.getSmsCnttSql().length() == 0))
              snntVO.setSmsCnttSql(result.getString("sms_cntt_sql"));
            if ((snntVO.getSmsInsSql() == null) || (snntVO.getSmsInsSql().length() == 0))
              snntVO.setSmsInsSql(result.getString("sms_ins_sql"));
            if ((snntVO.getSmsCntSql() == null) || (snntVO.getSmsCntSql().length() == 0))
              snntVO.setSmsCntSql(result.getString("sms_cnt_sql"));
            if ((snntVO.getEmailSvrAddr() == null) || (snntVO.getEmailSvrAddr().length() == 0))
              snntVO.setEmailSvrAddr(result.getString("email_svr_addr"));
            if ((snntVO.getEmailUser() == null) || (snntVO.getEmailUser().length() == 0))
              snntVO.setEmailUser(result.getString("email_user"));
            if ((snntVO.getEmailPass() == null) || (snntVO.getEmailPass().length() == 0))
              snntVO.setEmailPass(result.getString("email_pass"));
            if ((snntVO.getEmailSenderId() == null) || (snntVO.getEmailSenderId().length() == 0))
              snntVO.setEmailSenderId(result.getString("email_sender_id"));
            if ((snntVO.getEmailSubj() == null) || (snntVO.getEmailSubj().length() == 0))
              snntVO.setEmailSubj(result.getString("email_subj"));
            if ((snntVO.getEmailCntt() == null) || (snntVO.getEmailCntt().length() == 0))
              snntVO.setEmailCntt(result.getString("email_cntt"));
          }
        } 
    }
    catch (Exception e) {
      this.logger.error("AdminDAO.getBoardSnnt()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(result);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.getBoardSnnt()");
    return snntVO;
  }

  public void setBoardTell(AdminBoardForm abForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setBoardTell()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;
    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();

      sb.append("SELECT count(board_id)");
      sb.append(" FROM board_tell");
      sb.append(" WHERE board_id = ?");

      pstmt = conn.prepareStatement(sb.toString());
      pstmt.setString(1, abForm.getBoardId());
      result = pstmt.executeQuery();
      boolean exist = false;
      if (result.next()) {
        int c = result.getInt(1);
        if (c > 0)
          exist = true;
      }
      pstmt.close();

      sb.setLength(0);
      if (exist) {
        sb.append("UPDATE board_tell");
        sb.append(" SET tell_kind=?, sms_datasource=?, sms_jdbc_driver=?, sms_jdbc_url=?, sms_jdbc_user=?,");
        sb.append(" sms_jdbc_pass=?, sms_cntt_sql=?, sms_ins_sql=?, sms_cnt_sql=?, email_svr_addr=?,");
        sb.append(" email_user=?, email_pass=?, email_sender_id=?, email_subj=?, email_cntt=?, upd_user_id=?, upd_datim=?");
        sb.append(" WHERE board_id = ?");

        pstmt = conn.prepareStatement(sb.toString());

        int idx = 0;
        idx++; pstmt.setString(idx, abForm.getSnntKind());
        idx++; pstmt.setString(idx, abForm.getSmsDatasource());
        idx++; pstmt.setString(idx, abForm.getSmsJdbcDriver());
        idx++; pstmt.setString(idx, abForm.getSmsJdbcUrl());
        idx++; pstmt.setString(idx, abForm.getSmsJdbcUser());
        idx++; pstmt.setString(idx, abForm.getSmsJdbcPass());
        idx++; pstmt.setString(idx, abForm.getSmsCnttSql());
        idx++; pstmt.setString(idx, abForm.getSmsInsSql());
        idx++; pstmt.setString(idx, abForm.getSmsCntSql());
        idx++; pstmt.setString(idx, abForm.getEmailSvrAddr());
        idx++; pstmt.setString(idx, abForm.getEmailUser());
        idx++; pstmt.setString(idx, abForm.getEmailPass());
        idx++; pstmt.setString(idx, abForm.getEmailSenderId());
        idx++; pstmt.setString(idx, abForm.getEmailSubj());
        idx++; pstmt.setString(idx, abForm.getEmailCntt());
        idx++; pstmt.setString(idx, abForm.getUpdUserId());
        idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
        idx++; pstmt.setString(idx, abForm.getBoardId());

        pstmt.executeUpdate();
      }
      else {
        sb.append("INSERT INTO board_tell");
        sb.append(" ( board_id, snnt_kind, sms_datasource, sms_jdbc_driver, sms_jdbc_url, sms_jdbc_user,");
        sb.append(" sms_jdbc_pass, sms_cntt_sql, sms_ins_sql, sms_cnt_sql, email_svr_addr,");
        sb.append(" email_user, email_pass, email_sender_id, email_subj, email_cntt, upd_user_id, upd_datim)");
        sb.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        pstmt = conn.prepareStatement(sb.toString());

        int idx = 0;
        idx++; pstmt.setString(idx, abForm.getBoardId());
        idx++; pstmt.setString(idx, abForm.getSnntKind());
        idx++; pstmt.setString(idx, abForm.getSmsDatasource());
        idx++; pstmt.setString(idx, abForm.getSmsJdbcDriver());
        idx++; pstmt.setString(idx, abForm.getSmsJdbcUrl());
        idx++; pstmt.setString(idx, abForm.getSmsJdbcUser());
        idx++; pstmt.setString(idx, abForm.getSmsJdbcPass());
        idx++; pstmt.setString(idx, abForm.getSmsCnttSql());
        idx++; pstmt.setString(idx, abForm.getSmsInsSql());
        idx++; pstmt.setString(idx, abForm.getSmsCntSql());
        idx++; pstmt.setString(idx, abForm.getEmailSvrAddr());
        idx++; pstmt.setString(idx, abForm.getEmailUser());
        idx++; pstmt.setString(idx, abForm.getEmailPass());
        idx++; pstmt.setString(idx, abForm.getEmailSenderId());
        idx++; pstmt.setString(idx, abForm.getEmailSubj());
        idx++; pstmt.setString(idx, abForm.getEmailCntt());
        idx++; pstmt.setString(idx, abForm.getUpdUserId());
        idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));

        pstmt.executeUpdate();
      }
      pstmt.close();
    }
    catch (Exception e) {
      this.logger.error("AdminDAO.setBoardTell()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(result);
      close(pstmt);
    }

    this.logger.info("END::AdminDAO.setBoardTell()");
  }

  public long getCatebaseId(String boardId, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.getCatebaseId()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rslt = null;

    long cateId = 0L;
    try {
      conn = connCtxt.getConnection();
      pstmt = conn.prepareStatement("SELECT cate_id FROM cate_board WHERE board_id=?");
      pstmt.setString(1, boardId);
      rslt = pstmt.executeQuery();
      if (rslt.next())
        cateId = rslt.getLong(1);
    } catch (Exception e) {
      this.logger.error("AdminDAO.getCatebaseId()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(rslt);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.getCatebaseId()");
    return cateId;
  }

  protected String getCols(String table, ConnectionContext connCtxt) throws SQLException
  {
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    StringBuffer sb = new StringBuffer();
    this.log.debug("=====================================");
    this.log.debug("get columns for " + table);
    try {
      conn = connCtxt.getConnection();
      st = conn.createStatement();
      rs = st.executeQuery("SELECT * FROM " + table);
      ResultSetMetaData rsmd = rs.getMetaData();
      for (int i = 0; i < rsmd.getColumnCount(); i++) {
        String col = rsmd.getColumnName(i + 1);
        this.log.debug("column " + (i + 1) + "=" + col);
        if ("board_id".compareToIgnoreCase(col) == 0)
          sb.append("?");
        else if ("bltn_no".compareToIgnoreCase(col) == 0)
          sb.append(",?");
        else if ("bltn_gn".compareToIgnoreCase(col) == 0)
          sb.append(",?");
        else {
          sb.append("," + col);
        }
      }
      this.log.debug("=====================================");
    } finally {
      close(rs);
      close(st);
    }
    return sb.toString();
  }

  public void moveBulletin(AdminBoardForm abForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.moveBulletin()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    PreparedStatement bltnNoPstmt = null; PreparedStatement fileMaskPstmt = null; PreparedStatement fileMaskSelPstmt = null;

    PreparedStatement bltnPstmt = null; PreparedStatement cnttPstmt = null; PreparedStatement filePstmt = null; PreparedStatement memoPstmt = null; PreparedStatement evalPstmt = null; PreparedStatement extnPstmt = null;
    PreparedStatement srcCnttPstmt = null; PreparedStatement dstCnttUpdPstmt = null; PreparedStatement dstCnttSelPstmt = null;
    ResultSet rslt = null; ResultSet rslt1 = null; ResultSet srcCnttRslt = null; ResultSet fileMaskRslt = null; ResultSet dstCnttSelRslt = null;
    StringBuffer sb = new StringBuffer();

    String sep = System.getProperty("file.separator");
    String srcBoardId = abForm.getBoardId();
    String dstBoardId = abForm.getDstBoardId();
    String srcBltnTbl = "bulletin"; String srcCnttTbl = "bltn_cntt"; String srcFileTbl = "bltn_file"; String srcMemoTbl = "bltn_memo"; String srcEvalTbl = "bltn_eval";
    String dstBltnTbl = "bulletin"; String dstCnttTbl = "bltn_cntt"; String dstFileTbl = "bltn_file"; String dstMemoTbl = "bltn_memo"; String dstEvalTbl = "bltn_eval";
    boolean isSrcExtnUse = false; boolean isDstExtnUse = false;
    String srcExtnClassNm = null; String dstExtnClassNm = null;
    try
    {
      conn = connCtxt.getConnection();

      pstmt = conn.prepareStatement("SELECT owntbl_yn, owntbl_fix, SUBSTR(func_yns,9,1), extn_class_nm FROM board WHERE board_id=?");

      pstmt.setString(1, srcBoardId);
      rslt = pstmt.executeQuery();
      if (rslt.next()) {
        if ("Y".equals(rslt.getString(1)))
        {
          srcBltnTbl = "bltn_" + rslt.getString(2);
          srcCnttTbl = "bltn_" + rslt.getString(2) + "_cntt";
          srcFileTbl = "bltn_" + rslt.getString(2) + "_file";
          srcMemoTbl = "bltn_" + rslt.getString(2) + "_memo";
          srcEvalTbl = "bltn_" + rslt.getString(2) + "_eval";
        }
        if ("Y".equals(rslt.getString(3))) {
          isSrcExtnUse = true;
          srcExtnClassNm = rslt.getString(4);
        }
      }
      rslt.close();

      if (!"DELETE".equals(abForm.getCmd())) {
        pstmt.setString(1, dstBoardId);
        rslt = pstmt.executeQuery();
        if (rslt.next()) {
          if ("Y".equals(rslt.getString(1)))
          {
            dstBltnTbl = "bltn_" + rslt.getString(2);
            dstCnttTbl = "bltn_" + rslt.getString(2) + "_cntt";
            dstFileTbl = "bltn_" + rslt.getString(2) + "_file";
            dstMemoTbl = "bltn_" + rslt.getString(2) + "_memo";
            dstEvalTbl = "bltn_" + rslt.getString(2) + "_eval";
          }
          if ("Y".equals(rslt.getString(3))) {
            isDstExtnUse = true;
            dstExtnClassNm = rslt.getString(4);
          }
        }
        rslt.close();
      }
      pstmt.close();

      String extnTblNm = null;
      if (isSrcExtnUse) {
        if (("MOVE".equals(abForm.getCmd())) || ("COPY".equals(abForm.getCmd())))
        {
          if ((!isDstExtnUse) || (!srcExtnClassNm.equals(dstExtnClassNm))) {
            throw new BaseException("eb.error.moveBltn.extn.not.match");
          }

        }

        CacheMngr cacheMngr = (CacheMngr)getCacheMngr("EB");
        BoardVO boardVO = cacheMngr.getBoard(srcBoardId, Enview.getConfiguration().getString("portal.default.locale"));

        extnTblNm = boardVO.getBltnExtnMapper().getBltnExtnTblNm();
      }

      String[] selectedItems = abForm.getSelectedItems().split(";");

      if (("MOVE".equals(abForm.getCmd())) || ("COPY".equals(abForm.getCmd())))
      {
        bltnNoPstmt = conn.prepareStatement("SELECT bltn_no, bltn_gn FROM " + srcBltnTbl + " WHERE board_id=? AND bltn_no=?");

        String col = null;

        col = getCols("BULLETIN", connCtxt);

        bltnPstmt = conn.prepareStatement("INSERT INTO " + dstBltnTbl + " SELECT " + col + " FROM " + srcBltnTbl + " WHERE board_id=? AND bltn_no=?");

        col = getCols("BLTN_CNTT", connCtxt);

        cnttPstmt = conn.prepareStatement("INSERT INTO " + dstCnttTbl + " SELECT " + col + " FROM " + srcCnttTbl + " WHERE board_id=? AND bltn_no=?");

        if (isSrcExtnUse)
        {
          col = getCols(extnTblNm.toUpperCase(), connCtxt);

          extnPstmt = conn.prepareStatement("INSERT INTO " + extnTblNm + " SELECT " + col + " FROM " + extnTblNm + " WHERE board_id=? AND bltn_no=?");
        }

        col = getCols("BLTN_FILE", connCtxt);

        filePstmt = conn.prepareStatement("INSERT INTO " + dstFileTbl + " SELECT " + col + " FROM " + srcFileTbl + " WHERE board_id=? AND bltn_no=?");

        srcCnttPstmt = conn.prepareStatement("SELECT bltn_cntt FROM " + srcCnttTbl + " WHERE board_id=? AND bltn_no=?");

        fileMaskPstmt = conn.prepareStatement("SELECT file_mask FROM " + srcFileTbl + " WHERE board_id=? AND bltn_no=?");

        if ("true".equals(Enview.getConfiguration().getString("board.ora.clob.mode"))) {
          dstCnttUpdPstmt = conn.prepareStatement("UPDATE " + dstCnttTbl + " SET bltn_cntt=empty_clob() WHERE board_id=? AND bltn_no=?");
          dstCnttSelPstmt = conn.prepareStatement("SELECT bltn_cntt FROM " + dstCnttTbl + " WHERE board_id=? AND bltn_no=? FOR UPDATE");
        } else {
          dstCnttUpdPstmt = conn.prepareStatement("UPDATE " + dstCnttTbl + " SET bltn_cntt=? WHERE board_id=? AND bltn_no=?");
        }

        col = getCols("BLTN_MEMO", connCtxt);

        memoPstmt = conn.prepareStatement("INSERT INTO " + dstMemoTbl + " SELECT " + col + " FROM " + srcMemoTbl + " WHERE board_id=? AND bltn_no=?");

        col = getCols("BLTN_EVAL", connCtxt);

        evalPstmt = conn.prepareStatement("INSERT INTO " + dstEvalTbl + " SELECT " + col + " FROM " + srcEvalTbl + " WHERE board_id=? AND bltn_no=?");

        int idx = 0;
        Hashtable hashtable = new Hashtable();

        for (int i = 0; i < selectedItems.length; i++)
        {
          idx = 0;
          idx++; bltnNoPstmt.setString(idx, srcBoardId);
          idx++; bltnNoPstmt.setString(idx, selectedItems[i]);
          rslt = bltnNoPstmt.executeQuery();

          if (rslt.next())
          {
            String dstBltnNo = "1" + String.valueOf(System.currentTimeMillis() + i);
            String dstBltnGn = "0";
            String srcBltnGn = rslt.getString(2);

            if (hashtable.containsKey(srcBltnGn))
            {
              dstBltnGn = (String)hashtable.get(srcBltnGn);
            }
            else {
              dstBltnGn = getMaxGn(dstBltnTbl) +"";
              if (Long.parseLong(srcBltnGn) > 9900000000L)
                dstBltnGn = Long.toString(Long.parseLong(dstBltnGn) + 9900000000L);
              else if (Long.parseLong(srcBltnGn) > 9000000000L) {
                dstBltnGn = Long.toString(Long.parseLong(dstBltnGn) + 9000000000L);
              }
            }

            idx = 0;
            idx++; bltnPstmt.setString(idx, dstBoardId);
            idx++; bltnPstmt.setString(idx, dstBltnNo);
            idx++; bltnPstmt.setLong(idx, Long.parseLong(dstBltnGn));

            idx++; bltnPstmt.setString(idx, srcBoardId);
            idx++; bltnPstmt.setString(idx, selectedItems[i]);

            idx = 0;
            idx++; cnttPstmt.setString(idx, dstBoardId);
            idx++; cnttPstmt.setString(idx, dstBltnNo);
            idx++; cnttPstmt.setString(idx, srcBoardId);
            idx++; cnttPstmt.setString(idx, selectedItems[i]);

            if (isSrcExtnUse) {
              idx = 0;
              idx++; extnPstmt.setString(idx, dstBoardId);
              idx++; extnPstmt.setString(idx, dstBltnNo);
              idx++; extnPstmt.setString(idx, srcBoardId);
              idx++; extnPstmt.setString(idx, selectedItems[i]);
            }
            idx = 0;
            idx++; filePstmt.setString(idx, dstBoardId);
            idx++; filePstmt.setString(idx, dstBltnNo);
            idx++; filePstmt.setString(idx, srcBoardId);
            idx++; filePstmt.setString(idx, selectedItems[i]);

            idx = 0;
            idx++; srcCnttPstmt.setString(idx, srcBoardId);
            idx++; srcCnttPstmt.setString(idx, selectedItems[i]);

            idx = 0;
            idx++; fileMaskPstmt.setString(idx, srcBoardId);
            idx++; fileMaskPstmt.setString(idx, selectedItems[i]);

            if ("true".equals(Enview.getConfiguration().getString("board.ora.clob.mode"))) {
              idx = 0;
              idx++; dstCnttUpdPstmt.setString(idx, dstBoardId);
              idx++; dstCnttUpdPstmt.setString(idx, dstBltnNo);
              idx = 0;
              idx++; dstCnttSelPstmt.setString(idx, dstBoardId);
              idx++; dstCnttSelPstmt.setString(idx, dstBltnNo);
            }

            idx = 0;
            idx++; memoPstmt.setString(idx, dstBoardId);
            idx++; memoPstmt.setString(idx, dstBltnNo);
            idx++; memoPstmt.setString(idx, srcBoardId);
            idx++; memoPstmt.setString(idx, selectedItems[i]);

            idx = 0;
            idx++; evalPstmt.setString(idx, dstBoardId);
            idx++; evalPstmt.setString(idx, dstBltnNo);
            idx++; evalPstmt.setString(idx, srcBoardId);
            idx++; evalPstmt.setString(idx, selectedItems[i]);

            bltnPstmt.executeUpdate();
            cnttPstmt.executeUpdate();
            if (isSrcExtnUse)
              extnPstmt.executeUpdate();
            int fileCnt = filePstmt.executeUpdate();

            String fileMask = null;

            if (fileCnt > 0) {
              fileMaskRslt = fileMaskPstmt.executeQuery();

              while (fileMaskRslt.next()) {
                fileMask = fileMaskRslt.getString("file_mask");
                if ((fileMask.startsWith("T")) || (fileMask.startsWith("X"))) {
                  continue;
                }
                File srcFile = new File(abForm.getUploadPath() + sep + "attach" + sep + srcBoardId + sep + fileMask);
                File dstFile = new File(abForm.getUploadPath() + sep + "attach" + sep + dstBoardId + sep + fileMask);
                copyFile(srcFile, dstFile);
              }

            }

            String bltnCntt = null; String tempCntt = null;
            StringBuffer bltnCnttSB = new StringBuffer();

            srcCnttRslt = srcCnttPstmt.executeQuery();

            if (srcCnttRslt.next()) {
              Reader reader = null;
              char[] buf = new char[1024];
              int red = 0;
              if ("9i".equals(Enview.getConfiguration().getString("enview.db.version"))) {
                reader = srcCnttRslt.getCharacterStream("bltn_cntt");
                while ((red = reader.read(buf, 0, 1024)) != -1)
                  bltnCnttSB.append(buf, 0, red);
              } else {
                bltnCnttSB.append(srcCnttRslt.getString("bltn_cntt"));
              }
            }
            tempCntt = bltnCnttSB.toString();
            while (true)
            {
              int pos = tempCntt.indexOf("/upload/editor/" + srcBoardId + "/");
              if (pos == -1) {
                break;
              }
              tempCntt = tempCntt.substring(pos + ("/upload/editor/" + srcBoardId + "/").length());
              fileMask = tempCntt.substring(0, tempCntt.indexOf("\""));

              int posSpace = 0;
              if ((posSpace = fileMask.indexOf(" ")) > -1) {
                fileMask = fileMask.substring(0, posSpace);
              }

              tempCntt = tempCntt.substring(tempCntt.indexOf("\"") + 1);

              File srcFile = new File(abForm.getUploadPath() + sep + "editor" + sep + srcBoardId + sep + fileMask);
              File dstFile = new File(abForm.getUploadPath() + sep + "editor" + sep + dstBoardId + sep + fileMask);
              copyFile(srcFile, dstFile);

              if (fileMask.startsWith("T"))
              {
                srcFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + fileMask);
                if ((srcFile.exists()) && (srcFile.isFile())) {
                  dstFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + dstBoardId + sep + fileMask);
                  copyFile(srcFile, dstFile);
                }
                srcFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T050" + fileMask.substring(1));
                if ((srcFile.exists()) && (srcFile.isFile())) {
                  dstFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + dstBoardId + sep + "T050" + fileMask.substring(1));
                  copyFile(srcFile, dstFile);
                }
                srcFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T100" + fileMask.substring(1));
                if ((srcFile.exists()) && (srcFile.isFile())) {
                  dstFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + dstBoardId + sep + "T100" + fileMask.substring(1));
                  copyFile(srcFile, dstFile);
                }
                srcFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T150" + fileMask.substring(1));
                if ((srcFile.exists()) && (srcFile.isFile())) {
                  dstFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + dstBoardId + sep + "T150" + fileMask.substring(1));
                  copyFile(srcFile, dstFile);
                }
              }
              bltnCntt = FormatUtil.replace(bltnCnttSB.toString(), "editor/" + srcBoardId + "/" + fileMask, "editor/" + dstBoardId + "/" + fileMask);
              bltnCnttSB.setLength(0);
              bltnCnttSB.append(bltnCntt);
            }
            if ("true".equals(Enview.getConfiguration().getString("board.ora.clob.mode"))) {
              dstCnttUpdPstmt.executeUpdate();
              dstCnttSelRslt = dstCnttSelPstmt.executeQuery();

              if (dstCnttSelRslt.next())
                writeClob(dstCnttSelRslt.getClob(1), bltnCnttSB.toString());
            } else {
              idx = 0;
              idx++; dstCnttUpdPstmt.setString(idx, bltnCnttSB.toString());
              idx++; dstCnttUpdPstmt.setString(idx, dstBoardId);
              idx++; dstCnttUpdPstmt.setString(idx, dstBltnNo);
              dstCnttUpdPstmt.executeUpdate();
            }
            memoPstmt.executeUpdate();
            evalPstmt.executeUpdate();

            hashtable.put(srcBltnGn, dstBltnGn);
          }
          close(rslt);
          close(rslt1);
          close(srcCnttRslt);
          close(fileMaskRslt);
          close(dstCnttSelRslt);
        }
        close(bltnNoPstmt);

        close(bltnPstmt);
        close(cnttPstmt);
        close(extnPstmt);
        close(filePstmt);
        close(srcCnttPstmt);
        close(fileMaskPstmt);
        close(dstCnttUpdPstmt);
        close(dstCnttSelPstmt);
        close(memoPstmt);
        close(evalPstmt);
      }

      if (("MOVE".equals(abForm.getCmd())) || ("DELETE".equals(abForm.getCmd())))
      {
        bltnPstmt = conn.prepareStatement("DELETE FROM " + srcBltnTbl + " WHERE board_id = ? AND bltn_no = ?");
        cnttPstmt = conn.prepareStatement("DELETE FROM " + srcCnttTbl + " WHERE board_id = ? AND bltn_no = ?");
        if (isSrcExtnUse) {
          extnPstmt = conn.prepareStatement("DELETE FROM " + extnTblNm + " WHERE board_id = ? AND bltn_no = ?");
        }
        fileMaskSelPstmt = conn.prepareStatement("SELECT file_mask FROM " + srcFileTbl + " WHERE board_id = ? AND bltn_no = ?");
        filePstmt = conn.prepareStatement("DELETE FROM " + srcFileTbl + " WHERE board_id = ? AND bltn_no = ?");
        memoPstmt = conn.prepareStatement("DELETE FROM " + srcMemoTbl + " WHERE board_id = ? AND bltn_no = ?");
        evalPstmt = conn.prepareStatement("DELETE FROM " + srcEvalTbl + " WHERE board_id = ? AND bltn_no = ?");
        srcCnttPstmt = conn.prepareStatement("SELECT bltn_cntt FROM " + srcCnttTbl + " WHERE board_id=? AND bltn_no=?");

        StringBuffer bltnCnttSB = new StringBuffer();

        for (int i = 0; i < selectedItems.length; i++)
        {
          bltnPstmt.setString(1, srcBoardId);
          bltnPstmt.setString(2, selectedItems[i]);
          bltnPstmt.executeUpdate();

          if (isSrcExtnUse) {
            extnPstmt.setString(1, srcBoardId);
            extnPstmt.setString(2, selectedItems[i]);
            extnPstmt.executeUpdate();
          }

          fileMaskSelPstmt.setString(1, srcBoardId);
          fileMaskSelPstmt.setString(2, selectedItems[i]);
          rslt = fileMaskSelPstmt.executeQuery();
          while (rslt.next()) {
            String fileMask = rslt.getString("file_mask");
            if ((!fileMask.startsWith("T")) && (!fileMask.startsWith("X")))
              deleteFile(abForm.getUploadPath() + sep + "editor" + sep + srcBoardId + sep + fileMask);
            else {
              deleteFile(abForm.getUploadPath() + sep + "attach" + sep + srcBoardId + sep + fileMask);
            }

          }

          bltnCnttSB.setLength(0);
          srcCnttPstmt.setString(1, srcBoardId);
          srcCnttPstmt.setString(2, selectedItems[i]);
          srcCnttRslt = srcCnttPstmt.executeQuery();
          if (srcCnttRslt.next()) {
            Reader reader = null;
            char[] buf = new char[1024];
            int red = 0;
            if ("9i".equals(Enview.getConfiguration().getString("enview.db.version"))) {
              reader = srcCnttRslt.getCharacterStream("bltn_cntt");
              while ((red = reader.read(buf, 0, 1024)) != -1)
                bltnCnttSB.append(buf, 0, red);
            } else {
              bltnCnttSB.append(srcCnttRslt.getString("bltn_cntt"));
            }
          }
          String tempCntt = bltnCnttSB.toString();
          while (true)
          {
            int pos = tempCntt.indexOf("/upload/editor/" + srcBoardId + "/");
            if (pos == -1) {
              break;
            }
            tempCntt = tempCntt.substring(pos + ("/upload/editor/" + srcBoardId + "/").length());
            String fileMask = tempCntt.substring(0, tempCntt.indexOf("\""));

            tempCntt = tempCntt.substring(tempCntt.indexOf("\"") + 1);

            deleteFile(abForm.getUploadPath() + sep + "editor" + sep + srcBoardId + sep + fileMask);

            if (!fileMask.startsWith("T")) {
              continue;
            }
            deleteFile(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + fileMask);
            deleteFile(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T050" + fileMask.substring(1));
            deleteFile(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T100" + fileMask.substring(1));
            deleteFile(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T150" + fileMask.substring(1));
          }

          close(srcCnttRslt);

          cnttPstmt.setString(1, srcBoardId);
          cnttPstmt.setString(2, selectedItems[i]);
          cnttPstmt.executeUpdate();

          filePstmt.setString(1, srcBoardId);
          filePstmt.setString(2, selectedItems[i]);
          filePstmt.executeUpdate();

          memoPstmt.setString(1, srcBoardId);
          memoPstmt.setString(2, selectedItems[i]);
          memoPstmt.executeUpdate();

          evalPstmt.setString(1, srcBoardId);
          evalPstmt.setString(2, selectedItems[i]);
          evalPstmt.executeUpdate();
        }
        close(bltnPstmt);
        close(cnttPstmt);
        close(extnPstmt);
        close(fileMaskSelPstmt);
        close(filePstmt);
        close(memoPstmt);
        close(evalPstmt);
        close(srcCnttPstmt);
      }
    }
    catch (BaseException be) {
      this.logger.error("AdminDAO.moveBulletin()", be);
      throw be;
    } catch (Exception e) {
      this.logger.error("AdminDAO.moveBulletin()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(rslt);
      close(rslt1);
      close(srcCnttRslt);
      close(fileMaskRslt);
      close(dstCnttSelRslt);
      close(pstmt);
      close(bltnNoPstmt);

      close(fileMaskPstmt);
      close(fileMaskSelPstmt);
      close(bltnPstmt);
      close(cnttPstmt);
      close(filePstmt);
      close(memoPstmt);
      close(evalPstmt);
      close(extnPstmt);
      close(srcCnttPstmt);
      close(dstCnttUpdPstmt);
      close(dstCnttSelPstmt);
    }

    this.logger.info("END::AdminDAO.moveBulletin()");
  }

  public void moveBulletinBAK(AdminBoardForm abForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.moveBulletin()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    PreparedStatement bltnNoPstmt = null; PreparedStatement bltnGnPstmt = null; PreparedStatement fileMaskPstmt = null; PreparedStatement fileMaskSelPstmt = null;
    PreparedStatement bltnPstmt = null; PreparedStatement cnttPstmt = null; PreparedStatement filePstmt = null; PreparedStatement memoPstmt = null; PreparedStatement evalPstmt = null; PreparedStatement extnPstmt = null;
    PreparedStatement srcCnttPstmt = null; PreparedStatement dstCnttUpdPstmt = null; PreparedStatement dstCnttSelPstmt = null;
    ResultSet rslt = null; ResultSet rslt1 = null; ResultSet srcCnttRslt = null; ResultSet fileMaskRslt = null; ResultSet dstCnttSelRslt = null;
    StringBuffer sb = new StringBuffer();

    String sep = System.getProperty("file.separator");
    String srcBoardId = abForm.getBoardId();
    String dstBoardId = abForm.getDstBoardId();
    String srcBltnTbl = "bulletin"; String srcCnttTbl = "bltn_cntt"; String srcFileTbl = "bltn_file"; String srcMemoTbl = "bltn_memo"; String srcEvalTbl = "bltn_eval";
    String dstBltnTbl = "bulletin"; String dstCnttTbl = "bltn_cntt"; String dstFileTbl = "bltn_file"; String dstMemoTbl = "bltn_memo"; String dstEvalTbl = "bltn_eval";
    boolean isSrcExtnUse = false; boolean isDstExtnUse = false;
    String srcExtnClassNm = null; String dstExtnClassNm = null;
    try
    {
      conn = connCtxt.getConnection();

      pstmt = conn.prepareStatement("SELECT owntbl_yn, owntbl_fix, SUBSTR(func_yns,9,1), extn_class_nm FROM board WHERE board_id=?");

      pstmt.setString(1, srcBoardId);
      rslt = pstmt.executeQuery();
      if (rslt.next()) {
        if ("Y".equals(rslt.getString(1)))
        {
          srcBltnTbl = "bltn_" + rslt.getString(2);
          srcCnttTbl = "bltn_" + rslt.getString(2) + "_cntt";
          srcFileTbl = "bltn_" + rslt.getString(2) + "_file";
          srcMemoTbl = "bltn_" + rslt.getString(2) + "_memo";
          srcEvalTbl = "bltn_" + rslt.getString(2) + "_eval";
        }
        if ("Y".equals(rslt.getString(3))) {
          isSrcExtnUse = true;
          srcExtnClassNm = rslt.getString(4);
        }
      }
      rslt.close();

      if (!"DELETE".equals(abForm.getCmd())) {
        pstmt.setString(1, dstBoardId);
        rslt = pstmt.executeQuery();
        if (rslt.next()) {
          if ("Y".equals(rslt.getString(1)))
          {
            dstBltnTbl = "bltn_" + rslt.getString(2);
            dstCnttTbl = "bltn_" + rslt.getString(2) + "_cntt";
            dstFileTbl = "bltn_" + rslt.getString(2) + "_file";
            dstMemoTbl = "bltn_" + rslt.getString(2) + "_memo";
            dstEvalTbl = "bltn_" + rslt.getString(2) + "_eval";
          }
          if ("Y".equals(rslt.getString(3))) {
            isDstExtnUse = true;
            dstExtnClassNm = rslt.getString(4);
          }
        }
        rslt.close();
      }
      pstmt.close();

      String extnTblNm = null;
      if (isSrcExtnUse) {
        if (("MOVE".equals(abForm.getCmd())) || ("COPY".equals(abForm.getCmd())))
        {
          if ((!isDstExtnUse) || (!srcExtnClassNm.equals(dstExtnClassNm))) {
            throw new BaseException("eb.error.moveBltn.extn.not.match");
          }

        }

        CacheMngr cacheMngr = (CacheMngr)getCacheMngr("EB");
        BoardVO boardVO = cacheMngr.getBoard(srcBoardId, Enview.getConfiguration().getString("portal.default.locale"));

        extnTblNm = boardVO.getBltnExtnMapper().getBltnExtnTblNm();
      }

      String[] selectedItems = abForm.getSelectedItems().split(";");

      if (("MOVE".equals(abForm.getCmd())) || ("COPY".equals(abForm.getCmd())))
      {
        bltnNoPstmt = conn.prepareStatement("SELECT bltn_no, bltn_gn FROM " + srcBltnTbl + " WHERE board_id=? AND bltn_no=?");

        sb.setLength(0);
        sb.append("SELECT MAX(gn) FROM (");
        sb.append(" SELECT MAX(bltn_gn) gn FROM " + dstBltnTbl + " WHERE bltn_gn<9000000000");
        sb.append(" UNION");
        sb.append(" SELECT MAX(bltn_gn-9000000000) gn FROM " + dstBltnTbl + " WHERE bltn_gn>=9000000000 AND bltn_gn<9900000000");
        sb.append(" UNION");
        sb.append(" SELECT MAX(bltn_gn-9900000000) gn FROM " + dstBltnTbl + " WHERE bltn_gn>=9900000000");
        sb.append(")");
        bltnGnPstmt = conn.prepareStatement(sb.toString());

        String col = null;
        pstmt = conn.prepareStatement("SELECT column_name FROM cols WHERE table_name='BULLETIN' ORDER BY column_id");
        rslt = pstmt.executeQuery();
        sb.setLength(0);
        while (rslt.next()) {
          col = rslt.getString(1);
          if ("board_id".compareToIgnoreCase(col) == 0)
            sb.append("?");
          else if ("bltn_no".compareToIgnoreCase(col) == 0)
            sb.append(",?");
          else if ("bltn_gn".compareToIgnoreCase(col) == 0)
            sb.append(",?");
          else
            sb.append("," + col);
        }
        rslt.close();
        pstmt.close();
        col = sb.toString();

        bltnPstmt = conn.prepareStatement("INSERT INTO " + dstBltnTbl + " SELECT " + col + " FROM " + srcBltnTbl + " WHERE board_id=? AND bltn_no=?");

        pstmt = conn.prepareStatement("SELECT column_name FROM cols WHERE table_name='BLTN_CNTT' ORDER BY column_id");
        rslt = pstmt.executeQuery();
        sb.setLength(0);
        while (rslt.next()) {
          col = rslt.getString(1);
          if ("board_id".compareToIgnoreCase(col) == 0)
            sb.append("?");
          else if ("bltn_no".compareToIgnoreCase(col) == 0)
            sb.append(",?");
          else
            sb.append("," + col);
        }
        rslt.close();
        pstmt.close();
        col = sb.toString();

        cnttPstmt = conn.prepareStatement("INSERT INTO " + dstCnttTbl + " SELECT " + col + " FROM " + srcCnttTbl + " WHERE board_id=? AND bltn_no=?");

        if (isSrcExtnUse)
        {
          pstmt = conn.prepareStatement("SELECT column_name FROM cols WHERE table_name='" + extnTblNm.toUpperCase() + "' ORDER BY column_id");
          rslt = pstmt.executeQuery();
          sb.setLength(0);
          while (rslt.next()) {
            col = rslt.getString(1);
            if ("board_id".compareToIgnoreCase(col) == 0)
              sb.append("?");
            else if ("bltn_no".compareToIgnoreCase(col) == 0)
              sb.append(",?");
            else
              sb.append("," + col);
          }
          rslt.close();
          pstmt.close();
          col = sb.toString();

          extnPstmt = conn.prepareStatement("INSERT INTO " + extnTblNm + " SELECT " + col + " FROM " + extnTblNm + " WHERE board_id=? AND bltn_no=?");
        }

        pstmt = conn.prepareStatement("SELECT column_name FROM cols WHERE table_name='BLTN_FILE' ORDER BY column_id");
        rslt = pstmt.executeQuery();
        sb.setLength(0);
        while (rslt.next()) {
          col = rslt.getString(1);
          if ("board_id".compareToIgnoreCase(col) == 0)
            sb.append("?");
          else if ("bltn_no".compareToIgnoreCase(col) == 0)
            sb.append(",?");
          else
            sb.append("," + col);
        }
        rslt.close();
        pstmt.close();
        col = sb.toString();

        filePstmt = conn.prepareStatement("INSERT INTO " + dstFileTbl + " SELECT " + col + " FROM " + srcFileTbl + " WHERE board_id=? AND bltn_no=?");

        srcCnttPstmt = conn.prepareStatement("SELECT bltn_cntt FROM " + srcCnttTbl + " WHERE board_id=? AND bltn_no=?");

        fileMaskPstmt = conn.prepareStatement("SELECT file_mask FROM " + srcFileTbl + " WHERE board_id=? AND bltn_no=?");

        if ("true".equals(Enview.getConfiguration().getString("board.ora.clob.mode"))) {
          dstCnttUpdPstmt = conn.prepareStatement("UPDATE " + dstCnttTbl + " SET bltn_cntt=empty_clob() WHERE board_id=? AND bltn_no=?");
          dstCnttSelPstmt = conn.prepareStatement("SELECT bltn_cntt FROM " + dstCnttTbl + " WHERE board_id=? AND bltn_no=? FOR UPDATE");
        } else {
          dstCnttUpdPstmt = conn.prepareStatement("UPDATE " + dstCnttTbl + " SET bltn_cntt=? WHERE board_id=? AND bltn_no=?");
        }

        pstmt = conn.prepareStatement("SELECT column_name FROM cols WHERE table_name='BLTN_MEMO' ORDER BY column_id");
        rslt = pstmt.executeQuery();
        sb.setLength(0);
        while (rslt.next()) {
          col = rslt.getString(1);
          if ("board_id".compareToIgnoreCase(col) == 0)
            sb.append("?");
          else if ("bltn_no".compareToIgnoreCase(col) == 0)
            sb.append(",?");
          else
            sb.append("," + col);
        }
        rslt.close();
        pstmt.close();
        col = sb.toString();

        memoPstmt = conn.prepareStatement("INSERT INTO " + dstMemoTbl + " SELECT " + col + " FROM " + srcMemoTbl + " WHERE board_id=? AND bltn_no=?");

        pstmt = conn.prepareStatement("SELECT column_name FROM cols WHERE table_name='BLTN_EVAL' ORDER BY column_id");
        rslt = pstmt.executeQuery();
        sb.setLength(0);
        while (rslt.next()) {
          col = rslt.getString(1);
          if ("board_id".compareToIgnoreCase(col) == 0)
            sb.append("?");
          else if ("bltn_no".compareToIgnoreCase(col) == 0)
            sb.append(",?");
          else
            sb.append("," + col);
        }
        rslt.close();
        pstmt.close();
        col = sb.toString();

        evalPstmt = conn.prepareStatement("INSERT INTO " + dstEvalTbl + " SELECT " + col + " FROM " + srcEvalTbl + " WHERE board_id=? AND bltn_no=?");

        int idx = 0;
        Hashtable hashtable = new Hashtable();

        for (int i = 0; i < selectedItems.length; i++)
        {
          idx = 0;
          idx++; bltnNoPstmt.setString(idx, srcBoardId);
          idx++; bltnNoPstmt.setString(idx, selectedItems[i]);
          rslt = bltnNoPstmt.executeQuery();

          if (rslt.next())
          {
            String dstBltnNo = "1" + String.valueOf(System.currentTimeMillis() + i);
            String dstBltnGn = "0";
            String srcBltnGn = rslt.getString(2);

            dstBltnGn = getMaxGn(dstBltnTbl) +"";

            if (hashtable.containsKey(srcBltnGn)) {
              dstBltnGn = (String)hashtable.get(srcBltnGn);
            }
            idx = 0;
            idx++; bltnPstmt.setString(idx, dstBoardId);
            idx++; bltnPstmt.setString(idx, dstBltnNo);
            idx++; bltnPstmt.setLong(idx, Long.parseLong(dstBltnGn));

            idx++; bltnPstmt.setString(idx, srcBoardId);
            idx++; bltnPstmt.setString(idx, selectedItems[i]);

            idx = 0;
            idx++; cnttPstmt.setString(idx, dstBoardId);
            idx++; cnttPstmt.setString(idx, dstBltnNo);
            idx++; cnttPstmt.setString(idx, srcBoardId);
            idx++; cnttPstmt.setString(idx, selectedItems[i]);

            if (isSrcExtnUse) {
              idx = 0;
              idx++; extnPstmt.setString(idx, dstBoardId);
              idx++; extnPstmt.setString(idx, dstBltnNo);
              idx++; extnPstmt.setString(idx, srcBoardId);
              idx++; extnPstmt.setString(idx, selectedItems[i]);
            }
            idx = 0;
            idx++; filePstmt.setString(idx, dstBoardId);
            idx++; filePstmt.setString(idx, dstBltnNo);
            idx++; filePstmt.setString(idx, srcBoardId);
            idx++; filePstmt.setString(idx, selectedItems[i]);

            idx = 0;
            idx++; srcCnttPstmt.setString(idx, srcBoardId);
            idx++; srcCnttPstmt.setString(idx, selectedItems[i]);

            idx = 0;
            idx++; fileMaskPstmt.setString(idx, srcBoardId);
            idx++; fileMaskPstmt.setString(idx, selectedItems[i]);

            if ("true".equals(Enview.getConfiguration().getString("board.ora.clob.mode"))) {
              idx = 0;
              idx++; dstCnttUpdPstmt.setString(idx, dstBoardId);
              idx++; dstCnttUpdPstmt.setString(idx, dstBltnNo);
              idx = 0;
              idx++; dstCnttSelPstmt.setString(idx, dstBoardId);
              idx++; dstCnttSelPstmt.setString(idx, dstBltnNo);
            }

            idx = 0;
            idx++; memoPstmt.setString(idx, dstBoardId);
            idx++; memoPstmt.setString(idx, dstBltnNo);
            idx++; memoPstmt.setString(idx, srcBoardId);
            idx++; memoPstmt.setString(idx, selectedItems[i]);

            idx = 0;
            idx++; evalPstmt.setString(idx, dstBoardId);
            idx++; evalPstmt.setString(idx, dstBltnNo);
            idx++; evalPstmt.setString(idx, srcBoardId);
            idx++; evalPstmt.setString(idx, selectedItems[i]);

            bltnPstmt.executeUpdate();
            cnttPstmt.executeUpdate();
            if (isSrcExtnUse)
              extnPstmt.executeUpdate();
            int fileCnt = filePstmt.executeUpdate();

            String fileMask = null;
            String bltnCntt = null; String tempCntt = null;
            String fileMaskList = "|";
            StringBuffer bltnCnttSB = new StringBuffer();

            if (fileCnt > 0)
            {
              srcCnttRslt = srcCnttPstmt.executeQuery();

              if (srcCnttRslt.next()) {
                Reader reader = null;
                char[] buf = new char[1024];
                int red = 0;
                if ("9i".equals(Enview.getConfiguration().getString("enview.db.version"))) {
                  reader = srcCnttRslt.getCharacterStream("bltn_cntt");
                  while ((red = reader.read(buf, 0, 1024)) != -1)
                    bltnCnttSB.append(buf, 0, red);
                } else {
                  bltnCnttSB.append(srcCnttRslt.getString("bltn_cntt"));
                }

              }

              fileMaskRslt = fileMaskPstmt.executeQuery();

              while (fileMaskRslt.next()) {
                fileMask = fileMaskRslt.getString("file_mask");
                if (fileMask.startsWith("T"))
                {
                  fileMaskList = fileMaskList + fileMask + "|";

                  File srcFile = new File(abForm.getUploadPath() + sep + "editor" + sep + srcBoardId + sep + fileMask);
                  File dstFile = new File(abForm.getUploadPath() + sep + "editor" + sep + dstBoardId + sep + fileMask);
                  copyFile(srcFile, dstFile);

                  srcFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + fileMask);
                  if ((srcFile.exists()) && (srcFile.isFile())) {
                    dstFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + dstBoardId + sep + fileMask);
                    copyFile(srcFile, dstFile);
                  }
                  srcFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T050" + fileMask.substring(1));
                  if ((srcFile.exists()) && (srcFile.isFile())) {
                    dstFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + dstBoardId + sep + "T050" + fileMask.substring(1));
                    copyFile(srcFile, dstFile);
                  }
                  srcFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T100" + fileMask.substring(1));
                  if ((srcFile.exists()) && (srcFile.isFile())) {
                    dstFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + dstBoardId + sep + "T100" + fileMask.substring(1));
                    copyFile(srcFile, dstFile);
                  }
                  srcFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T150" + fileMask.substring(1));
                  if ((srcFile.exists()) && (srcFile.isFile())) {
                    dstFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + dstBoardId + sep + "T150" + fileMask.substring(1));
                    copyFile(srcFile, dstFile);
                  }

                  bltnCntt = FormatUtil.replace(bltnCnttSB.toString(), "editor/" + srcBoardId + "/" + fileMask, "editor/" + dstBoardId + "/" + fileMask);
                  bltnCnttSB.setLength(0);
                  bltnCnttSB.append(bltnCntt);
                } else if (fileMask.startsWith("X"))
                {
                  fileMaskList = fileMaskList + fileMask + "|";

                  File srcFile = new File(abForm.getUploadPath() + sep + "editor" + sep + srcBoardId + sep + fileMask);
                  File dstFile = new File(abForm.getUploadPath() + sep + "editor" + sep + dstBoardId + sep + fileMask);
                  copyFile(srcFile, dstFile);
                  bltnCntt = FormatUtil.replace(bltnCnttSB.toString(), "editor/" + srcBoardId + "/" + fileMask, "editor/" + dstBoardId + "/" + fileMask);
                  bltnCnttSB.setLength(0);
                  bltnCnttSB.append(bltnCntt);
                } else {
                  File srcFile = new File(abForm.getUploadPath() + sep + "attach" + sep + srcBoardId + sep + fileMask);
                  File dstFile = new File(abForm.getUploadPath() + sep + "attach" + sep + dstBoardId + sep + fileMask);
                  copyFile(srcFile, dstFile);
                }
              }

              if ("true".equals(Enview.getConfiguration().getString("board.ora.clob.mode"))) {
                dstCnttUpdPstmt.executeUpdate();

                dstCnttSelRslt = dstCnttSelPstmt.executeQuery();

                if (dstCnttSelRslt.next())
                  writeClob(dstCnttSelRslt.getClob(1), bltnCnttSB.toString());
              } else {
                idx = 0;
                idx++; dstCnttUpdPstmt.setString(idx, bltnCnttSB.toString());
                idx++; dstCnttUpdPstmt.setString(idx, dstBoardId);
                idx++; dstCnttUpdPstmt.setString(idx, dstBltnNo);
                dstCnttUpdPstmt.executeUpdate();
              }

            }

            if ("true".equals(Enview.getConfiguration().getString("board.bltnCntt.scan.mode")))
            {
              bltnCnttSB.setLength(0);

              srcCnttRslt = srcCnttPstmt.executeQuery();

              if (srcCnttRslt.next()) {
                Reader reader = null;
                char[] buf = new char[1024];
                int red = 0;
                if ("9i".equals(Enview.getConfiguration().getString("enview.db.version"))) {
                  reader = srcCnttRslt.getCharacterStream("bltn_cntt");
                  while ((red = reader.read(buf, 0, 1024)) != -1)
                    bltnCnttSB.append(buf, 0, red);
                } else {
                  bltnCnttSB.append(srcCnttRslt.getString("bltn_cntt"));
                }
              }
              tempCntt = bltnCnttSB.toString();
              while (true)
              {
                int posIMG = tempCntt.indexOf("<IMG");
                int posimg = tempCntt.indexOf("<img");

                if (posIMG < posimg)
                  tempCntt = tempCntt.substring(posIMG + 4);
                else if (posimg < posIMG)
                  tempCntt = tempCntt.substring(posimg + 4);
                else {
                  if ((posIMG == -1) && (posimg == -1)) {
                    break;
                  }
                }
                tempCntt = tempCntt.substring(tempCntt.indexOf("/upload/editor/" + srcBoardId + "/") + ("/upload/editor/" + srcBoardId + "/").length());
                fileMask = tempCntt.substring(0, tempCntt.indexOf("\""));

                tempCntt = tempCntt.substring(tempCntt.indexOf("\"") + 1);
                this.logger.info("fileMask=[" + fileMask + "]");

                if (fileMaskList.indexOf(fileMask) != -1)
                {
                  continue;
                }

                File srcFile = new File(abForm.getUploadPath() + sep + "editor" + sep + srcBoardId + sep + fileMask);
                File dstFile = new File(abForm.getUploadPath() + sep + "editor" + sep + dstBoardId + sep + fileMask);
                copyFile(srcFile, dstFile);

                if (fileMask.startsWith("T"))
                {
                  srcFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + fileMask);
                  if ((srcFile.exists()) && (srcFile.isFile())) {
                    dstFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + dstBoardId + sep + fileMask);
                    copyFile(srcFile, dstFile);
                  }
                  srcFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T050" + fileMask.substring(1));
                  if ((srcFile.exists()) && (srcFile.isFile())) {
                    dstFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + dstBoardId + sep + "T050" + fileMask.substring(1));
                    copyFile(srcFile, dstFile);
                  }
                  srcFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T100" + fileMask.substring(1));
                  if ((srcFile.exists()) && (srcFile.isFile())) {
                    dstFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + dstBoardId + sep + "T100" + fileMask.substring(1));
                    copyFile(srcFile, dstFile);
                  }
                  srcFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T150" + fileMask.substring(1));
                  if ((srcFile.exists()) && (srcFile.isFile())) {
                    dstFile = new File(abForm.getUploadPath() + sep + "thumb" + sep + dstBoardId + sep + "T150" + fileMask.substring(1));
                    copyFile(srcFile, dstFile);
                  }
                }
                bltnCntt = FormatUtil.replace(bltnCnttSB.toString(), "editor/" + srcBoardId + "/" + fileMask, "editor/" + dstBoardId + "/" + fileMask);
                bltnCnttSB.setLength(0);
                bltnCnttSB.append(bltnCntt);
              }

              if ("true".equals(Enview.getConfiguration().getString("board.ora.clob.mode"))) {
                dstCnttUpdPstmt.executeUpdate();

                dstCnttSelRslt = dstCnttSelPstmt.executeQuery();

                if (dstCnttSelRslt.next())
                  writeClob(dstCnttSelRslt.getClob(1), bltnCnttSB.toString());
              } else {
                idx = 0;
                idx++; dstCnttUpdPstmt.setString(idx, bltnCnttSB.toString());
                idx++; dstCnttUpdPstmt.setString(idx, dstBoardId);
                idx++; dstCnttUpdPstmt.setString(idx, dstBltnNo);
                dstCnttUpdPstmt.executeUpdate();
              }

            }

            memoPstmt.executeUpdate();
            evalPstmt.executeUpdate();

            hashtable.put(srcBltnGn, dstBltnGn);
          }
          close(rslt);
          close(rslt1);
          close(srcCnttRslt);
          close(fileMaskRslt);
          close(dstCnttSelRslt);
        }
        close(bltnNoPstmt);
        close(bltnGnPstmt);
        close(bltnPstmt);
        close(cnttPstmt);
        close(extnPstmt);
        close(filePstmt);
        close(srcCnttPstmt);
        close(fileMaskPstmt);
        close(dstCnttUpdPstmt);
        close(dstCnttSelPstmt);
        close(memoPstmt);
        close(evalPstmt);
      }

      if (("MOVE".equals(abForm.getCmd())) || ("DELETE".equals(abForm.getCmd())))
      {
        bltnPstmt = conn.prepareStatement("DELETE FROM " + srcBltnTbl + " WHERE board_id = ? AND bltn_no = ?");
        cnttPstmt = conn.prepareStatement("DELETE FROM " + srcCnttTbl + " WHERE board_id = ? AND bltn_no = ?");
        extnPstmt = conn.prepareStatement("DELETE FROM " + extnTblNm + " WHERE board_id = ? AND bltn_no = ?");
        fileMaskSelPstmt = conn.prepareStatement("SELECT file_mask FROM " + srcFileTbl + " WHERE board_id = ? AND bltn_no = ?");
        filePstmt = conn.prepareStatement("DELETE FROM " + srcFileTbl + " WHERE board_id = ? AND bltn_no = ?");
        memoPstmt = conn.prepareStatement("DELETE FROM " + srcMemoTbl + " WHERE board_id = ? AND bltn_no = ?");
        evalPstmt = conn.prepareStatement("DELETE FROM " + srcEvalTbl + " WHERE board_id = ? AND bltn_no = ?");

        for (int i = 0; i < selectedItems.length; i++)
        {
          bltnPstmt.setString(1, srcBoardId);
          bltnPstmt.setString(2, selectedItems[i]);
          bltnPstmt.executeUpdate();

          if (isSrcExtnUse) {
            extnPstmt.setString(1, srcBoardId);
            extnPstmt.setString(2, selectedItems[i]);
            extnPstmt.executeUpdate();
          }

          String fileMaskList = "|";

          fileMaskSelPstmt.setString(1, srcBoardId);
          fileMaskSelPstmt.setString(2, selectedItems[i]);
          rslt = fileMaskSelPstmt.executeQuery();
          while (rslt.next())
          {
            String fileMask = rslt.getString("file_mask");
            fileMaskList = fileMaskList + fileMask + "|";

            if (fileMask.startsWith("T")) {
              deleteFile(abForm.getUploadPath() + sep + "editor" + sep + srcBoardId + sep + fileMask);

              deleteFile(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + fileMask);
              deleteFile(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T050" + fileMask.substring(1));
              deleteFile(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T100" + fileMask.substring(1));
              deleteFile(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T150" + fileMask.substring(1));
            }
            else if (fileMask.startsWith("X")) {
              deleteFile(abForm.getUploadPath() + sep + "editor" + sep + srcBoardId + sep + fileMask);
            } else {
              deleteFile(abForm.getUploadPath() + sep + "attach" + sep + srcBoardId + sep + fileMask);
            }

          }

          if ("true".equals(Enview.getConfiguration().getString("board.bltnCntt.scan.mode")))
          {
            StringBuffer bltnCnttSB = new StringBuffer();

            srcCnttPstmt = conn.prepareStatement("SELECT bltn_cntt FROM " + srcCnttTbl + " WHERE board_id=? AND bltn_no=?");
            srcCnttPstmt.setString(1, srcBoardId);
            srcCnttPstmt.setString(2, selectedItems[i]);
            srcCnttRslt = srcCnttPstmt.executeQuery();

            if (srcCnttRslt.next()) {
              Reader reader = null;
              char[] buf = new char[1024];
              int red = 0;
              if ("9i".equals(Enview.getConfiguration().getString("enview.db.version"))) {
                reader = srcCnttRslt.getCharacterStream("bltn_cntt");
                while ((red = reader.read(buf, 0, 1024)) != -1)
                  bltnCnttSB.append(buf, 0, red);
              } else {
                bltnCnttSB.append(srcCnttRslt.getString("bltn_cntt"));
              }
            }
            String tempCntt = bltnCnttSB.toString();
            while (true)
            {
              int posIMG = tempCntt.indexOf("<IMG");
              int posimg = tempCntt.indexOf("<img");

              if (posIMG < posimg)
                tempCntt = tempCntt.substring(posIMG + 4);
              else if (posimg < posIMG)
                tempCntt = tempCntt.substring(posimg + 4);
              else {
                if ((posIMG == -1) && (posimg == -1)) {
                  break;
                }
              }
              tempCntt = tempCntt.substring(tempCntt.indexOf("/upload/editor/" + srcBoardId + "/") + ("/upload/editor/" + srcBoardId + "/").length());
              String fileMask = tempCntt.substring(0, tempCntt.indexOf("\""));

              tempCntt = tempCntt.substring(tempCntt.indexOf("\"") + 1);
              this.logger.info("fileMask to del=[" + fileMask + "]");

              if (fileMaskList.indexOf(fileMask) != -1)
              {
                continue;
              }

              deleteFile(abForm.getUploadPath() + sep + "editor" + sep + srcBoardId + sep + fileMask);

              if (!fileMask.startsWith("T"))
              {
                continue;
              }
              deleteFile(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + fileMask);
              deleteFile(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T050" + fileMask.substring(1));
              deleteFile(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T100" + fileMask.substring(1));
              deleteFile(abForm.getUploadPath() + sep + "thumb" + sep + srcBoardId + sep + "T150" + fileMask.substring(1));
            }

            close(srcCnttRslt);
          }

          cnttPstmt.setString(1, srcBoardId);
          cnttPstmt.setString(2, selectedItems[i]);
          cnttPstmt.executeUpdate();

          filePstmt.setString(1, srcBoardId);
          filePstmt.setString(2, selectedItems[i]);
          filePstmt.executeUpdate();

          memoPstmt.setString(1, srcBoardId);
          memoPstmt.setString(2, selectedItems[i]);
          memoPstmt.executeUpdate();

          evalPstmt.setString(1, srcBoardId);
          evalPstmt.setString(2, selectedItems[i]);
          evalPstmt.executeUpdate();
        }
        close(bltnPstmt);
        close(cnttPstmt);
        close(extnPstmt);
        close(fileMaskSelPstmt);
        close(filePstmt);
        close(memoPstmt);
        close(evalPstmt);
      }
    }
    catch (BaseException be) {
      this.logger.error("AdminDAO.moveBulletin()", be);
      throw be;
    } catch (Exception e) {
      this.logger.error("AdminDAO.moveBulletin()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(rslt);
      close(rslt1);
      close(srcCnttRslt);
      close(fileMaskRslt);
      close(dstCnttSelRslt);
      close(pstmt);
      close(bltnNoPstmt);
      close(bltnGnPstmt);
      close(fileMaskPstmt);
      close(fileMaskSelPstmt);
      close(bltnPstmt);
      close(cnttPstmt);
      close(filePstmt);
      close(memoPstmt);
      close(evalPstmt);
      close(extnPstmt);
      close(srcCnttPstmt);
      close(dstCnttUpdPstmt);
      close(dstCnttSelPstmt);
    }

    this.logger.info("END::AdminDAO.moveBulletin()");
  }

  public void copyFile(File src, File dst) {
    try {
      this.logger.debug("copy " + src.getAbsolutePath() + " to " + dst.getAbsolutePath());
      FileInputStream fis = new FileInputStream(src);
      FileOutputStream fos = new FileOutputStream(dst);
      byte[] buf = new byte[1024];
      int red = 0;
      while ((red = fis.read(buf, 0, 1024)) != -1)
        fos.write(buf, 0, red);
      fis.close();
      fos.close();
    } catch (Exception e) {
      this.logger.info("AdminDAO.copyFile()", e);
    }
  }

  public PollPropVO getPollProp(String boardId, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.getPollProp()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rslt = null;

    PollPropVO pollPropVO = new PollPropVO();

    StringBuffer sb = new StringBuffer();

    if (ValidateUtil.isEmpty(boardId)) {
      return pollPropVO;
    }
    sb.append("SELECT board_id, poll_bgn_ymd, poll_end_ymd, func_yns, upd_user_id, upd_datim");
    sb.append(" FROM poll_prop WHERE board_id=?");
    try
    {
      conn = connCtxt.getConnection();

      pstmt = conn.prepareStatement(sb.toString());
      pstmt.setString(1, boardId);
      rslt = pstmt.executeQuery();

      int idx = 0;
      if (rslt.next()) {
        idx++; pollPropVO.setBoardId(rslt.getString(idx));
        idx++; pollPropVO.setPollBgnYmd(rslt.getDate(idx));
        idx++; pollPropVO.setPollEndYmd(rslt.getDate(idx));

        idx++; String funcYns = rslt.getString(idx);
        pollPropVO.setIpdupYn(funcYns.substring(0, 1));

        idx++; pollPropVO.setUpdUserId(rslt.getString(idx));
        idx++; pollPropVO.setUpdDatim(rslt.getTimestamp(idx));
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.getPollProp()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(rslt);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.getPollProp()");
    return pollPropVO;
  }

  public void setPollBasic(AdminPollForm apForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setPollBasic()");

    Connection conn = null;
    PreparedStatement pstmt = null; PreparedStatement insPstmt = null; PreparedStatement bltnSel = null; PreparedStatement pollSel = null; PreparedStatement bltnIns = null; PreparedStatement pollIns = null;
    ResultSet rslt = null; ResultSet rslt2 = null;
    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();
      int idx = 0;

      if ("NEW".equals(apForm.getCmd())) {
        pstmt = conn.prepareStatement("SELECT COUNT(board_id) FROM board WHERE board_id=?");
        idx++; pstmt.setString(idx, apForm.getBoardId());
        rslt = pstmt.executeQuery();
        if ((rslt.next()) && 
          (rslt.getInt(1) > 0))
          throw new BaseException("mm.error.sql.badIntegrity");
        rslt.close();
        pstmt.close();

        CatebaseVO pollCateVO = getCatebase(apForm.getCateId(), apForm.getLangKnd(), connCtxt);

        sb.setLength(0);
        sb.append("INSERT INTO board");
        sb.append(" SELECT ?, ?, ?, ?, board_sys, owntbl_yn, owntbl_fix, board_type, merge_type, func_yns, ?,");
        sb.append(" list_yns, read_yns, srch_yns, ttl_yns, ttl_lens, term_flag, ?, raise_color, raise_cnt, new_term, max_file_cnt, max_file_size,");
        sb.append(" max_file_down, list_set_cnt, bad_std_cnt, mini_trgt_win, mini_trgt_url, extn_class_nm, board_width, top_html, bottom_html, board_bg_pic,");
        sb.append(" board_bg_color, ?, ?");
        sb.append(" FROM board WHERE board_id='ENBOARD.BASE.PT.POLL'");
        pstmt = conn.prepareStatement(sb.toString());

        idx = 0;
        idx++; pstmt.setString(idx, apForm.getBoardId());
        idx++; pstmt.setString(idx, apForm.getBoardId());

        idx++; pstmt.setLong(idx, pollCateVO.getDomainId());
        idx++; pstmt.setString(idx, apForm.getBoardActive());
        idx++; pstmt.setString(idx, apForm.getBugaYns());
        idx++; pstmt.setString(idx, apForm.getBoardSkin());
        idx++; pstmt.setString(idx, apForm.getUpdUserId());

        idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));

        pstmt.executeUpdate();

        sb.setLength(0);
        sb.append("INSERT INTO board_lang (board_id, lang_knd, board_nm, board_ttl) VALUES (?,?,?,?)");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setString(idx, apForm.getBoardId());
        idx++; pstmt.setString(idx, apForm.getLangKnd());
        idx++; pstmt.setString(idx, apForm.getBoardNm());
        idx++; pstmt.setString(idx, apForm.getBoardTtl());
        pstmt.executeUpdate();
        pstmt.close();

        sb.setLength(0);
        sb.append("INSERT INTO poll_prop");
        sb.append(" (board_id, poll_bgn_ymd, poll_end_ymd, func_yns, upd_user_id, upd_datim)");
        sb.append(" VALUES (?,?,?,?,?,?)");
        pstmt = conn.prepareStatement(sb.toString());

        idx = 0;
        idx++; pstmt.setString(idx, apForm.getBoardId());
        idx++; pstmt.setTimestamp(idx, new Timestamp(FormatUtil.string2Date(FormatUtil.discardNonNum(apForm.getPollBgnYmd())).getTime()));
        idx++; pstmt.setTimestamp(idx, new Timestamp(FormatUtil.string2Date(FormatUtil.discardNonNum(apForm.getPollEndYmd())).getTime()));

        String funcYns = null;
        funcYns = (apForm.getIpdupYn() == null) || (apForm.getIpdupYn().length() == 0) ? "Y" : apForm.getIpdupYn();
        idx++; pstmt.setString(idx, funcYns);

        idx++; pstmt.setString(idx, apForm.getUpdUserId());

        idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
        pstmt.executeUpdate();
        pstmt.close();

        sb.setLength(0);
        sb.append("SELECT code, code_tag2, code_name FROM codebase");
        sb.append(" WHERE system_code='PT' AND code_id='963'");
        sb.append(" AND code_tag1='Y' AND code!='0000000000'");
        sb.append(" ORDER BY code");
        pstmt = conn.prepareStatement(sb.toString());
        rslt = pstmt.executeQuery();
        List gcdList = new ArrayList();
        while (rslt.next()) {
          CodebaseVO cdVO = new CodebaseVO();
          cdVO.setCode(rslt.getString(1));
          cdVO.setCodeTag2(rslt.getString(2));
          cdVO.setCodeName(rslt.getString(3));
          gcdList.add(cdVO);
        }
        rslt.close();
        pstmt.close();

        sb.setLength(0);
        sb.append("INSERT INTO security_principal ( principal_id, domain_id, parent_id, principal_type, classname, is_mapping_only, is_enabled, principal_order, short_path, full_path, principal_name, theme, site_name, default_page, auth_method, principal_desc, principal_info01, principal_info02, principal_info03, creation_date, modified_date)");
        sb.append(" SELECT ?, ?, ?, 'g', 'com.saltware.enview.security.internalGradePrincipalImpl', 0, 1, 0,");
        sb.append(" ?, ?, ?, '', '', '', 0, ?, ?, '', '', SYSDATE, SYSDATE");
        sb.append(" FROM DUAL");
        pstmt = conn.prepareStatement(sb.toString());

        SecurityDAO secDAO = (SecurityDAO)DAOFactory.getInst().getDAO("com.saltware.enboard.dao.spi.Security");
        long topGradeId = secDAO.getTopGradeId("PT", connCtxt);

        Iterator it = gcdList.iterator();
        while (it.hasNext()) {
          CodebaseVO cdVO = (CodebaseVO)it.next();
          idx = 0;
          idx++; pstmt.setLong(idx, getNextKey("SECURITY_PRINCIPAL"));
          idx++; pstmt.setLong(idx, pollCateVO.getDomainId());
          idx++; pstmt.setLong(idx, topGradeId);
          idx++; pstmt.setString(idx, "/PT" + apForm.getBoardId() + "/" + cdVO.getCode());
          idx++; pstmt.setString(idx, "/PT" + apForm.getBoardId() + "/" + cdVO.getCode());
          idx++; pstmt.setString(idx, cdVO.getCodeName());
          idx++; pstmt.setString(idx, "GRADE OF '/PT" + apForm.getBoardId() + "'");
          idx++; pstmt.setString(idx, cdVO.getCodeTag2());

          pstmt.executeUpdate();
        }
        pstmt.close();

        List gradeList = secDAO.getGradeList("PT" + apForm.getBoardId(), connCtxt);

        sb.setLength(0);
        sb.append("INSERT INTO security_permission");
        sb.append(" SELECT ?, ?, ?, ?, ?, 5, ?, 1, SYSDATE, SYSDATE FROM DUAL");
        pstmt = conn.prepareStatement(sb.toString());

        it = gradeList.iterator();
        while (it.hasNext()) {
          CodebaseVO cdVO = (CodebaseVO)it.next();
          idx = 0;
          idx++; pstmt.setLong(idx, getNextKey("SECURITY_PERMISSION"));
          idx++; pstmt.setLong(idx, Long.parseLong(cdVO.getCodeId()));

          idx++; pstmt.setLong(idx, apForm.getDomainId());
          idx++; pstmt.setString(idx, apForm.getBoardNm());
          idx++; pstmt.setString(idx, apForm.getBoardId());
          idx++; pstmt.setInt(idx, Integer.parseInt(cdVO.getCodeTag2()));

          pstmt.executeUpdate();
        }
        pstmt.close();

        sb.setLength(0);
        sb.append("INSERT INTO mileage_cd");
        sb.append(" SELECT ?, ?, mile_active, mile_io, mile_sys, mile_pnt, mile_sttg, tlimit_cnt,");
        sb.append(" dlimit_cnt, wlimit_cnt, mlimit_cnt, ylimit_cnt, mile_nm, mile_rem, upd_user_id, ?");
        sb.append(" FROM mileage_cd WHERE mile_cd=?");
        insPstmt = conn.prepareStatement(sb.toString());

        pstmt = conn.prepareStatement("SELECT SUBSTR(mile_cd,14) base_nm, mile_cd FROM mileage_cd WHERE mile_cd=?");
        pstmt.setString(1, "ENBOARD.BASE.MPL");
        rslt = pstmt.executeQuery();
        while (rslt.next()) {
          idx = 0;
          idx++; insPstmt.setString(idx, "EB" + apForm.getBoardId() + rslt.getString("base_nm"));
          idx++; insPstmt.setLong(idx, pollCateVO.getDomainId());
          idx++; insPstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
          idx++; insPstmt.setString(idx, rslt.getString("mile_cd"));

          insPstmt.executeUpdate();
        }
        insPstmt.close();
        pstmt.close();

        sb.setLength(0);
        sb.append("INSERT INTO cate_board (cate_id, board_id) VALUES (?,?)");
        pstmt = conn.prepareStatement(sb.toString());

        idx = 0;
        idx++; pstmt.setLong(idx, Long.parseLong(apForm.getCateId()));
        idx++; pstmt.setString(idx, apForm.getBoardId());
        pstmt.executeUpdate();
        pstmt.close();
      }
      else if ("UPD".equals(apForm.getCmd()))
      {
        pstmt = conn.prepareStatement("SELECT COUNT(board_id) FROM board WHERE board_id=?");
        idx++; pstmt.setString(idx, apForm.getBoardId());
        rslt = pstmt.executeQuery();
        if ((rslt.next()) && 
          (rslt.getInt(1) <= 0))
          throw new BaseException("eb.error.pollId.already.deleted");
        rslt.close();
        pstmt.close();

        pstmt = conn.prepareStatement("UPDATE board SET board_active=?, buga_yns = ?, board_skin=?, upd_user_id=?, upd_datim=? WHERE board_id=?");
        idx = 0;
        idx++; pstmt.setString(idx, apForm.getBoardActive());
        idx++; pstmt.setString(idx, apForm.getBugaYns());
        idx++; pstmt.setString(idx, apForm.getBoardSkin());
        idx++; pstmt.setString(idx, apForm.getUpdUserId());

        idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
        idx++; pstmt.setString(idx, apForm.getBoardId());
        pstmt.executeUpdate();
        pstmt.close();

        pstmt = conn.prepareStatement("UPDATE board_lang SET board_nm=?, board_ttl=? WHERE board_id=? AND lang_knd=?");
        idx = 0;
        idx++; pstmt.setString(idx, apForm.getBoardNm());
        idx++; pstmt.setString(idx, apForm.getBoardTtl());
        idx++; pstmt.setString(idx, apForm.getBoardId());
        idx++; pstmt.setString(idx, apForm.getLangKnd());
        pstmt.executeUpdate();
        pstmt.close();

        sb.setLength(0);
        sb.append("UPDATE poll_prop");
        sb.append(" SET poll_bgn_ymd=?, poll_end_ymd=?, func_yns=?, upd_user_id=?, upd_datim=?");
        sb.append(" WHERE board_id=?");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setTimestamp(idx, new Timestamp(FormatUtil.string2Date(FormatUtil.discardNonNum(apForm.getPollBgnYmd())).getTime()));
        idx++; pstmt.setTimestamp(idx, new Timestamp(FormatUtil.string2Date(FormatUtil.discardNonNum(apForm.getPollEndYmd())).getTime()));

        String funcYns = null;
        funcYns = (apForm.getIpdupYn() == null) || (apForm.getIpdupYn().length() == 0) ? "Y" : apForm.getIpdupYn();
        idx++; pstmt.setString(idx, funcYns);

        idx++; pstmt.setString(idx, apForm.getUpdUserId());

        idx++; pstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
        idx++; pstmt.setString(idx, apForm.getBoardId());
        pstmt.executeUpdate();
        pstmt.close();
      }
      else if ("DEL".equals(apForm.getCmd()))
      {
        pstmt = conn.prepareStatement("SELECT count(board_id) FROM board WHERE board_id=?");
        idx++; pstmt.setString(idx, apForm.getBoardId());
        rslt = pstmt.executeQuery();
        if ((rslt.next()) && 
          (rslt.getInt(1) <= 0))
          throw new BaseException("eb.error.pollId.already.deleted");
        rslt.close();
        pstmt.close();

        pstmt = conn.prepareStatement("DELETE FROM board WHERE board_id=?");
        pstmt.setString(1, apForm.getBoardId());
        pstmt.executeUpdate();
        pstmt.close();

        pstmt = conn.prepareStatement("DELETE FROM board_lang WHERE board_id=? AND lang_knd=?");
        pstmt.setString(1, apForm.getBoardId());
        pstmt.setString(2, apForm.getLangKnd());
        pstmt.executeUpdate();
        pstmt.close();

        pstmt = conn.prepareStatement("DELETE FROM poll_prop WHERE board_id=?");
        pstmt.setString(1, apForm.getBoardId());
        pstmt.executeUpdate();
        pstmt.close();

        pstmt = conn.prepareStatement("DELETE FROM BLTN_POLL_EVAL WHERE board_id=?");
        pstmt.setString(1, apForm.getBoardId());
        pstmt.executeUpdate();
        pstmt.close();

        if (Enview.getConfiguration().getBoolean("board.poll.detail.delete"))
        {
          pstmt = conn.prepareStatement("DELETE FROM bulletin WHERE board_id=?");
          pstmt.setString(1, apForm.getBoardId());
          pstmt.executeUpdate();
          pstmt.close();

          pstmt = conn.prepareStatement("DELETE FROM bltn_cntt WHERE board_id=?");
          pstmt.setString(1, apForm.getBoardId());
          pstmt.executeUpdate();
          pstmt.close();

          pstmt = conn.prepareStatement("DELETE FROM bltn_poll WHERE board_id=?");
          pstmt.setString(1, apForm.getBoardId());
          pstmt.executeUpdate();
          pstmt.close();

          pstmt = conn.prepareStatement("DELETE FROM bltn_poll_rslt WHERE board_id=?");
          pstmt.setString(1, apForm.getBoardId());
          pstmt.executeUpdate();
          pstmt.close();
        }

        pstmt = conn.prepareStatement("DELETE FROM security_permission WHERE res_type=5 AND principal_id IN (SELECT principal_id FROM security_principal WHERE principal_type='g' AND short_path LIKE ?)");
        pstmt.setString(1, "/PT" + apForm.getBoardId() + "%");
        pstmt.executeUpdate();
        pstmt.close();

        pstmt = conn.prepareStatement("DELETE FROM security_grade_user WHERE grade_id IN (SELECT principal_id FROM security_principal WHERE principal_type='g' AND short_path LIKE ?)");
        pstmt.setString(1, "/PT" + apForm.getBoardId() + "%");
        pstmt.executeUpdate();
        pstmt.close();
        pstmt = conn.prepareStatement("DELETE FROM security_grade_group WHERE grade_id IN (SELECT principal_id FROM security_principal WHERE principal_type='g' AND short_path LIKE ?)");
        pstmt.setString(1, "/PT" + apForm.getBoardId() + "%");
        pstmt.executeUpdate();
        pstmt.close();
        pstmt = conn.prepareStatement("DELETE FROM security_grade_role WHERE grade_id IN (SELECT principal_id FROM security_principal WHERE principal_type='g' AND short_path LIKE ?)");
        pstmt.setString(1, "/PT" + apForm.getBoardId() + "%");
        pstmt.executeUpdate();
        pstmt.close();

        pstmt = conn.prepareStatement("DELETE FROM security_principal WHERE principal_type='g' and short_path LIKE ?");
        pstmt.setString(1, "/PT" + apForm.getBoardId() + "%");
        pstmt.executeUpdate();
        pstmt.close();

        pstmt = conn.prepareStatement("DELETE FROM mileage_cd WHERE mile_cd=?");
        pstmt.setString(1, "EB" + apForm.getBoardId() + "MPL");
        pstmt.executeUpdate();
        pstmt.close();

        pstmt = conn.prepareStatement("DELETE FROM cate_board WHERE board_id=?");
        pstmt.setString(1, apForm.getBoardId());
        pstmt.executeUpdate();
        pstmt.close();
      }
      else if ("DUP".equals(apForm.getCmd()))
      {
        pstmt = conn.prepareStatement("SELECT count(board_id) FROM board WHERE board_id=?");
        idx++; pstmt.setString(idx, apForm.getDstBoardId());
        rslt = pstmt.executeQuery();
        if ((rslt.next()) && 
          (rslt.getInt(1) > 0))
          throw new BaseException("mm.error.sql.badIntegrity");
        rslt.close();
        pstmt.close();

        CacheMngr cacheMngr = (CacheMngr)getCacheMngr("EB");
        BoardVO boardVO = cacheMngr.getBoard(apForm.getBoardId(), apForm.getLangKnd());

        List cols = getColumns(connCtxt, "BOARD");

        sb.setLength(0);
        sb.append("'" + apForm.getDstBoardId() + "', '" + apForm.getDstBoardId() + "'");
        for (int i = 2; i < cols.size(); i++) {
          sb.append("," + (String)cols.get(i));
        }
        pstmt = conn.prepareStatement("INSERT INTO board SELECT " + sb.toString() + " FROM board WHERE board_id='" + apForm.getBoardId() + "'");
        pstmt.executeUpdate();
        pstmt.close();

        cols = getColumns(connCtxt, "BOARD_LANG");

        sb.setLength(0);
        sb.append("'" + apForm.getDstBoardId() + "'");
        for (int i = 1; i < cols.size(); i++) {
          sb.append("," + (String)cols.get(i));
        }
        pstmt = conn.prepareStatement("INSERT INTO board_lang SELECT " + sb.toString() + " FROM board_lang WHERE board_id='" + apForm.getBoardId() + "'");
        pstmt.executeUpdate();
        pstmt.close();

        cols = getColumns(connCtxt, "POLL_PROP");

        sb.setLength(0);
        sb.append("'" + apForm.getDstBoardId() + "'");
        for (int i = 1; i < cols.size(); i++) {
          sb.append("," + (String)cols.get(i));
        }
        pstmt = conn.prepareStatement("INSERT INTO poll_prop SELECT " + sb.toString() + " FROM poll_prop WHERE board_id='" + apForm.getBoardId() + "'");
        pstmt.executeUpdate();
        pstmt.close();

        cols = getColumns(connCtxt, "BLTN_POLL_EVAL");
        sb.setLength(0);
        sb.append("'" + apForm.getDstBoardId() + "'");
        for (int i = 1; i < cols.size(); i++) {
          sb.append("," + (String)cols.get(i));
        }
        pstmt = conn.prepareStatement("INSERT INTO BLTN_POLL_EVAL SELECT " + sb.toString() + " FROM BLTN_POLL_EVAL WHERE board_id='" + apForm.getBoardId() + "'");
        pstmt.executeUpdate();
        pstmt.close();

        cols = getColumns(connCtxt, "BLTN_POLL");

        sb.setLength(0);
        sb.append("'" + apForm.getDstBoardId() + "'");
        for (int i = 1; i < cols.size(); i++) {
          sb.append("," + (String)cols.get(i));
        }
        pollIns = conn.prepareStatement("INSERT INTO bltn_poll SELECT " + sb.toString() + " FROM bltn_poll" + " WHERE board_id='" + apForm.getBoardId() + "' AND bltn_no=? AND poll_seq=?");

        cols = getColumns(connCtxt, "BULLETIN");

        sb.setLength(0);
        sb.append("'" + apForm.getDstBoardId() + "'");
        for (int i = 1; i < cols.size(); i++) {
          sb.append("," + (String)cols.get(i));
        }
        bltnIns = conn.prepareStatement("INSERT INTO bulletin SELECT " + sb.toString() + " FROM bulletin" + " WHERE board_id='" + apForm.getBoardId() + "' AND bltn_no=?");

        insPstmt = conn.prepareStatement("INSERT INTO bltn_cntt (board_id, bltn_no, bltn_cntt) SELECT ?,?,bltn_cntt FROM bltn_cntt WHERE board_id=? AND bltn_no=?");

        pollSel = conn.prepareStatement("SELECT poll_seq FROM bltn_poll WHERE board_id='" + apForm.getBoardId() + "' AND bltn_no=?");
        bltnSel = conn.prepareStatement("SELECT bltn_no FROM bulletin WHERE board_id='" + apForm.getBoardId() + "'");
        rslt = bltnSel.executeQuery();
        while (rslt.next()) {
          bltnIns.setString(1, rslt.getString("bltn_no"));
          bltnIns.executeUpdate();

          insPstmt.setString(1, apForm.getDstBoardId());
          insPstmt.setString(2, rslt.getString("bltn_no"));
          insPstmt.setString(3, apForm.getBoardId());
          insPstmt.setString(4, rslt.getString("bltn_no"));
          insPstmt.executeUpdate();

          pollSel.setString(1, rslt.getString("bltn_no"));
          rslt2 = pollSel.executeQuery();
          while (rslt2.next()) {
            pollIns.setString(1, rslt.getString("bltn_no"));
            pollIns.setInt(2, rslt2.getInt("poll_seq"));
            pollIns.executeUpdate();
          }
        }
        close(rslt2);
        close(rslt);
        pollSel.close();
        bltnSel.close();
        insPstmt.close();
        pollIns.close();
        bltnIns.close();

        sb.setLength(0);
        sb.append("SELECT code, code_tag2, code_name FROM codebase");
        sb.append(" WHERE system_code='PT' AND code_id='963'");
        sb.append(" AND code_tag1='Y' AND code!='0000000000'");
        sb.append(" ORDER BY code");
        pstmt = conn.prepareStatement(sb.toString());
        rslt = pstmt.executeQuery();
        List gcdList = new ArrayList();
        while (rslt.next()) {
          CodebaseVO cdVO = new CodebaseVO();
          cdVO.setCode(rslt.getString(1));
          cdVO.setCodeTag2(rslt.getString(2));
          cdVO.setCodeName(rslt.getString(3));
          gcdList.add(cdVO);
        }
        rslt.close();
        pstmt.close();

        sb.setLength(0);
        sb.append("INSERT INTO security_principal( principal_id, domain_id, parent_id, principal_type, classname, is_mapping_only, is_enabled, principal_order, short_path, full_path, principal_name, theme, site_name, default_page, auth_method, principal_desc, principal_info01, principal_info02, principal_info03, creation_date, modified_date)");
        sb.append(" SELECT ?, ?, ?, 'g', 'com.saltware.enview.security.internalGradePrincipalImpl', 0, 1, 0,");
        sb.append(" ?, ?, ?, '', '', '', 0, ?, ?, '', '', SYSDATE, SYSDATE");
        sb.append(" FROM DUAL");
        pstmt = conn.prepareStatement(sb.toString());

        SecurityDAO secDAO = (SecurityDAO)DAOFactory.getInst().getDAO("com.saltware.enboard.dao.spi.Security");
        long topGradeId = secDAO.getTopGradeId("PT", connCtxt);

        Iterator it = gcdList.iterator();
        while (it.hasNext()) {
          CodebaseVO cdVO = (CodebaseVO)it.next();
          idx = 0;
          idx++; pstmt.setLong(idx, getNextKey("SECURITY_PRINCIPAL"));
          idx++; pstmt.setLong(idx, boardVO.getDomainId());
          idx++; pstmt.setLong(idx, topGradeId);
          idx++; pstmt.setString(idx, "/PT" + apForm.getDstBoardId() + "/" + cdVO.getCode());
          idx++; pstmt.setString(idx, "/PT" + apForm.getDstBoardId() + "/" + cdVO.getCode());
          idx++; pstmt.setString(idx, cdVO.getCodeName());
          idx++; pstmt.setString(idx, "GRADE OF '/PT" + apForm.getDstBoardId() + "'");
          idx++; pstmt.setString(idx, cdVO.getCodeTag2());

          pstmt.executeUpdate();
        }
        pstmt.close();

        List gradeList = secDAO.getGradeList("PT" + apForm.getDstBoardId(), connCtxt);

        sb.setLength(0);
        sb.append("INSERT INTO security_permission");
        sb.append(" SELECT ?, ?, ?, board_nm, ?, 5, ?, 1, SYSDATE, SYSDATE FROM board_lang WHERE board_id=? AND lang_knd=?");
        pstmt = conn.prepareStatement(sb.toString());

        it = gradeList.iterator();
        while (it.hasNext()) {
          CodebaseVO cdVO = (CodebaseVO)it.next();
          idx = 0;
          idx++; pstmt.setLong(idx, getNextKey("SECURITY_PERMISSION"));
          idx++; pstmt.setLong(idx, Long.parseLong(cdVO.getCodeId()));

          idx++; pstmt.setLong(idx, boardVO.getDomainId());
          idx++; pstmt.setString(idx, apForm.getDstBoardId());
          idx++; pstmt.setInt(idx, Integer.parseInt(cdVO.getCodeTag2()));

          idx++; pstmt.setString(idx, apForm.getBoardId());
          idx++; pstmt.setString(idx, apForm.getLangKnd());
          pstmt.executeUpdate();
        }
        pstmt.close();

        sb.setLength(0);
        sb.append("INSERT INTO mileage_cd");
        sb.append(" SELECT ?, ?, mile_active, mile_io, mile_sys, mile_pnt, mile_sttg, tlimit_cnt,");
        sb.append(" dlimit_cnt, wlimit_cnt, mlimit_cnt, ylimit_cnt, mile_nm, mile_rem, upd_user_id, ?");
        sb.append(" FROM mileage_cd WHERE mile_cd=?");
        insPstmt = conn.prepareStatement(sb.toString());

        pstmt = conn.prepareStatement("SELECT SUBSTR(mile_cd,14) base_nm, mile_cd FROM mileage_cd WHERE mile_cd=?");
        pstmt.setString(1, "ENBOARD.BASE.MPL");
        rslt = pstmt.executeQuery();
        while (rslt.next()) {
          idx = 0;
          idx++; insPstmt.setString(idx, "EB" + apForm.getDstBoardId() + rslt.getString("base_nm"));
          idx++; insPstmt.setLong(idx, boardVO.getDomainId());
          idx++; insPstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
          idx++; insPstmt.setString(idx, rslt.getString("mile_cd"));

          insPstmt.executeUpdate();
        }
        insPstmt.close();
        pstmt.close();

        sb.setLength(0);
        sb.append("INSERT INTO cate_board (cate_id, board_id) VALUES (?,?)");
        pstmt = conn.prepareStatement(sb.toString());
        idx = 0;
        idx++; pstmt.setLong(idx, Long.parseLong(apForm.getCateId()));
        idx++; pstmt.setString(idx, apForm.getDstBoardId());
        pstmt.executeUpdate();
        pstmt.close();

        copyPollBoardImage(apForm);
      }
    } catch (BaseException be) {
      this.logger.error("AdminDAO.setPollBasic()", be);
      throw be;
    } catch (Exception e) {
      this.logger.error("AdminDAO.setPollBasic()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(rslt);
      close(rslt2);
      close(pstmt);
      close(insPstmt);
      close(bltnSel);
      close(pollSel);
      close(bltnIns);
      close(pollIns);
    }
    this.logger.info("END::AdminDAO.setPollBasic()");
  }

  public List getAllPollQs(String boardId, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.getAllPollQs()");

    Connection conn = null;
    PreparedStatement bltnPstmt = null; PreparedStatement cnttPstmt = null;
    ResultSet rslt = null; ResultSet rslt2 = null;

    List pollQList = new ArrayList();
    try
    {
      conn = connCtxt.getConnection();
      bltnPstmt = conn.prepareStatement("SELECT board_id, bltn_no, bltn_gq, bltn_subj, user_id, upd_datim FROM bulletin WHERE board_id=? ORDER BY bltn_gq DESC");
      cnttPstmt = conn.prepareStatement("SELECT bltn_cntt FROM bltn_cntt WHERE board_id=? AND bltn_no=?");

      bltnPstmt.setString(1, boardId);
      rslt = bltnPstmt.executeQuery();
      int boardRow = 0;
      while (rslt.next())
      {
        int idx = 0;
        BulletinVO bltnVO = new BulletinVO();

        boardRow++; bltnVO.setBoardRow(boardRow);
        idx++; bltnVO.setBoardId(rslt.getString(idx));
        idx++; bltnVO.setBltnNo(rslt.getString(idx));
        idx++; bltnVO.setBltnGq(rslt.getInt(idx) * -1);

        idx++; bltnVO.setBltnSubj(rslt.getString(idx));
        idx++; bltnVO.setUserId(rslt.getString(idx));
        idx++; bltnVO.setUpdDatim(rslt.getTimestamp(idx));

        cnttPstmt.setString(1, bltnVO.getBoardId());
        cnttPstmt.setString(2, bltnVO.getBltnNo());
        rslt2 = cnttPstmt.executeQuery();
        if (rslt2.next()) {
          if ("9i".equals(Enview.getConfiguration().getString("enview.db.version"))) {
            StringBuffer stringbuffer = new StringBuffer();
            char[] charbuffer = new char[1024];
            int read = 0;
            Reader reader = null;
            try {
              reader = rslt2.getCharacterStream(1);
              while ((read = reader.read(charbuffer, 0, 1024)) != -1)
                stringbuffer.append(charbuffer, 0, read);
              bltnVO.setBltnCntt(stringbuffer.toString());
            } catch (Exception exception) {
              throw exception;
            } finally {
              if (reader != null)
                reader.close();
            }
          } else {
            bltnVO.setBltnCntt(rslt2.getString(1));
          }
        }

        pollQList.add(bltnVO);
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.getAllPollQs()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(rslt);
      close(rslt2);
      close(bltnPstmt);
      close(cnttPstmt);
    }
    this.logger.info("END::AdminDAO.getAllPollQs(),pollQList=[" + pollQList + "]");
    return pollQList;
  }

  public List getPollEvalList(String boardId, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.getPollEvalList()");

    Connection conn = null;
    PreparedStatement evalPstmt = null;
    ResultSet rslt = null;

    List pollEvalList = new ArrayList();
    try
    {
      conn = connCtxt.getConnection();
      evalPstmt = conn.prepareStatement("SELECT BOARD_ID, EVAL_SEQ, EVAL_FROM, EVAL_TO, EVAL_REMARK FROM BLTN_POLL_EVAL WHERE BOARD_ID=? ORDER BY EVAL_FROM ASC");

      evalPstmt.setString(1, boardId);
      rslt = evalPstmt.executeQuery();
      int boardRow = 0;
      while (rslt.next()) {
        BltnPollEvalVO bltnPollEvalVO = new BltnPollEvalVO();

        boardRow++; bltnPollEvalVO.setBoardRow(boardRow);
        bltnPollEvalVO.setBoardId(rslt.getString("BOARD_ID"));
        bltnPollEvalVO.setEvalSeq(rslt.getInt("EVAL_SEQ"));
        bltnPollEvalVO.setEvalFrom(rslt.getInt("EVAL_FROM"));
        bltnPollEvalVO.setEvalTo(rslt.getInt("EVAL_TO"));
        bltnPollEvalVO.setEvalRemark(rslt.getString("EVAL_REMARK"));

        pollEvalList.add(bltnPollEvalVO);
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.getPollEvalList()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(rslt);
      close(evalPstmt);
    }
    this.logger.info("END::AdminDAO.getPollEvalList(),pollEvalList=[" + pollEvalList + "]");
    return pollEvalList;
  }

  public BltnPollEvalVO getPollEvalProp(String boardId, String evalSeq, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.getPollEvalProp()");

    Connection conn = null;
    PreparedStatement evalPstmt = null;
    ResultSet rslt = null;

    BltnPollEvalVO bltnPollEvalVO = null;
    try
    {
      conn = connCtxt.getConnection();

      evalPstmt = conn.prepareStatement("SELECT BOARD_ID, EVAL_SEQ, EVAL_FROM, EVAL_TO, EVAL_REMARK FROM BLTN_POLL_EVAL WHERE BOARD_ID=? AND EVAL_SEQ = CAST(? AS INTEGER)");

      evalPstmt.setString(1, boardId);
      evalPstmt.setString(2, evalSeq);

      rslt = evalPstmt.executeQuery();

      if (rslt.next())
      {
        bltnPollEvalVO = new BltnPollEvalVO();

        bltnPollEvalVO.setBoardId(rslt.getString("BOARD_ID"));
        bltnPollEvalVO.setEvalSeq(rslt.getInt("EVAL_SEQ"));
        bltnPollEvalVO.setEvalFrom(rslt.getInt("EVAL_FROM"));
        bltnPollEvalVO.setEvalTo(rslt.getInt("EVAL_TO"));
        bltnPollEvalVO.setEvalRemark(rslt.getString("EVAL_REMARK"));
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.getPollEvalProp()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(rslt);
      close(evalPstmt);
    }
    this.logger.info("END::AdminDAO.getPollEvalProp()");
    return bltnPollEvalVO;
  }

  public void setPollEvalProp(AdminPollForm apForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setPollEvalProp()");

    Connection conn = null;
    PreparedStatement pstmt = null; PreparedStatement evalPstmt = null;
    ResultSet rslt = null;

    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();

      if ("ins".equals(apForm.getAct()))
      {
        int evalSeq = 0;

        sb.setLength(0);
        sb.append("SELECT MAX(EVAL_SEQ) FROM BLTN_POLL_EVAL WHERE BOARD_ID=?");
        pstmt = conn.prepareStatement(sb.toString());

        pstmt.setString(1, apForm.getBoardId());

        rslt = pstmt.executeQuery();
        if (rslt.next()) {
          evalSeq = rslt.getInt(1) + 1;
        }

        rslt.close();
        pstmt.close();

        evalPstmt = conn.prepareStatement("INSERT INTO bltn_poll_eval (board_id, eval_seq, eval_from, eval_to, eval_remark, upd_user_id, upd_datim) VALUES (?,?,?,?,?,?,?)");
        int idx = 0;
        idx++; evalPstmt.setString(idx, apForm.getBoardId());
        idx++; evalPstmt.setInt(idx, evalSeq);
        idx++; evalPstmt.setInt(idx, apForm.getEvalFrom());
        idx++; evalPstmt.setInt(idx, apForm.getEvalTo());
        idx++; evalPstmt.setString(idx, apForm.getEvalRemark());
        idx++; evalPstmt.setString(idx, apForm.getUpdUserId());
        idx++; evalPstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));

        evalPstmt.executeUpdate();
        evalPstmt.close();
      }
      else if ("upd".equals(apForm.getAct()))
      {
        evalPstmt = conn.prepareStatement("UPDATE bltn_poll_eval SET eval_from=?, eval_to=?, eval_remark=?, upd_user_id=?, upd_datim=? WHERE board_id=? AND eval_seq=?");
        int idx = 0;
        idx++; evalPstmt.setInt(idx, apForm.getEvalFrom());
        idx++; evalPstmt.setInt(idx, apForm.getEvalTo());
        idx++; evalPstmt.setString(idx, apForm.getEvalRemark());
        idx++; evalPstmt.setString(idx, apForm.getUpdUserId());
        idx++; evalPstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
        idx++; evalPstmt.setString(idx, apForm.getBoardId());
        idx++; evalPstmt.setString(idx, apForm.getEvalSeq());

        evalPstmt.executeUpdate();
        evalPstmt.close();
      }
      else if ("del".equals(apForm.getAct()))
      {
        evalPstmt = conn.prepareStatement("DELETE FROM bltn_poll_eval WHERE board_id=? AND eval_seq=?");
        evalPstmt.setString(1, apForm.getBoardId());
        evalPstmt.setString(2, apForm.getEvalSeq());

        evalPstmt.executeUpdate();
        evalPstmt.close();
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.setPollEvalProp()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(rslt);
      close(pstmt);
      close(evalPstmt);
    }
    this.logger.info("END::AdminDAO.setPollEvalProp()");
  }

  public BulletinVO getPollQuestion(String boardId, String bltnNo, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.getPollQuestion()");

    Connection conn = null;
    PreparedStatement bltnPstmt = null; PreparedStatement cnttPstmt = null;
    ResultSet rslt = null; ResultSet rslt2 = null;

    BulletinVO bltnVO = null;

    label474: 
    try { conn = connCtxt.getConnection();

      bltnPstmt = conn.prepareStatement("SELECT board_id, bltn_no, bltn_gq, bltn_subj, bet_pnt, user_id, upd_datim FROM bulletin WHERE board_id=? AND bltn_no=?");
      cnttPstmt = conn.prepareStatement("SELECT bltn_cntt FROM bltn_cntt WHERE board_id=? AND bltn_no=?");

      bltnPstmt.setString(1, boardId);
      bltnPstmt.setString(2, bltnNo);
      rslt = bltnPstmt.executeQuery();
      if (rslt.next())
      {
        int idx = 0;
        bltnVO = new BulletinVO();

        idx++; bltnVO.setBoardId(rslt.getString(idx));
        idx++; bltnVO.setBltnNo(rslt.getString(idx));
        idx++; bltnVO.setBltnGq(rslt.getInt(idx) * -1);

        idx++; bltnVO.setFileMask(rslt.getString(idx));
        idx++; bltnVO.setBetPnt(rslt.getInt(idx));
        idx++; bltnVO.setUserId(rslt.getString(idx));
        idx++; bltnVO.setUpdDatim(rslt.getTimestamp(idx));

        cnttPstmt.setString(1, boardId);
        cnttPstmt.setString(2, bltnNo);
        rslt2 = cnttPstmt.executeQuery();

        if (rslt2.next()) {
          if ("9i".equals(Enview.getConfiguration().getString("enview.db.version"))) {
            StringBuffer stringbuffer = new StringBuffer();
            char[] charbuffer = new char[1024];
            int read = 0;
            Reader reader = null;
            try {
              reader = rslt2.getCharacterStream(1);
              while ((read = reader.read(charbuffer, 0, 1024)) != -1)
                stringbuffer.append(charbuffer, 0, read);
              bltnVO.setBltnCntt(stringbuffer.toString());
            } catch (Exception exception) {
              throw exception;
            } finally {
              if (reader != null)
                reader.close();
            }
            break label474;
          }bltnVO.setBltnCntt(rslt2.getString(1));
        }
      }
    } catch (Exception e)
    {
      this.logger.error("AdminDAO.getPollQuestion()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(rslt);
      close(rslt2);
      close(bltnPstmt);
      close(cnttPstmt);
    }
    this.logger.info("END::AdminDAO.getPollQuestion()");
    return bltnVO;
  }

  public void setPollQuestion(AdminPollForm apForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setPollQuestion()");

    Connection conn = null;
    PreparedStatement pstmt = null; PreparedStatement bltnPstmt = null; PreparedStatement cnttPstmt = null;
    ResultSet rslt = null;

    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();

      if ("ins".equals(apForm.getAct()))
      {
        String bltnNo = String.valueOf(System.currentTimeMillis());

        bltnPstmt = conn.prepareStatement("INSERT INTO bulletin (board_id, bltn_no, bltn_gid, bltn_gn, bltn_gq, bltn_subj, user_id, upd_datim) VALUES (?,?,?,?,?,?,?,?)");
        int idx = 0;
        idx++; bltnPstmt.setString(idx, apForm.getBoardId());
        idx++; bltnPstmt.setString(idx, bltnNo);
        idx++; bltnPstmt.setString(idx, "EB");
        idx++; bltnPstmt.setInt(idx, 1);
        idx++; bltnPstmt.setInt(idx, apForm.getBltnGq() * -1);

        idx++; bltnPstmt.setString(idx, apForm.getFileMask());
        idx++; bltnPstmt.setString(idx, apForm.getUpdUserId());
        idx++; bltnPstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
        bltnPstmt.executeUpdate();
        bltnPstmt.close();

        cnttPstmt = conn.prepareStatement("INSERT INTO bltn_cntt (board_id, bltn_no, bltn_cntt) VALUES (?,?,?)");
        idx = 0;
        idx++; cnttPstmt.setString(idx, apForm.getBoardId());
        idx++; cnttPstmt.setString(idx, bltnNo);
        idx++; cnttPstmt.setString(idx, apForm.getBltnCntt());
        cnttPstmt.executeUpdate();
        cnttPstmt.close();
      }
      else if ("upd".equals(apForm.getAct()))
      {
        bltnPstmt = conn.prepareStatement("UPDATE bulletin SET bltn_gq=?, user_id=?, upd_datim=? WHERE board_id=? AND bltn_no=?");
        int idx = 0;
        idx++; bltnPstmt.setInt(idx, apForm.getBltnGq() * -1);

        idx++; bltnPstmt.setString(idx, apForm.getUpdUserId());
        idx++; bltnPstmt.setTimestamp(idx, new Timestamp(System.currentTimeMillis()));
        idx++; bltnPstmt.setString(idx, apForm.getBoardId());
        idx++; bltnPstmt.setString(idx, apForm.getBltnNo());
        bltnPstmt.executeUpdate();
        bltnPstmt.close();

        cnttPstmt = conn.prepareStatement("UPDATE bltn_cntt SET bltn_cntt=? WHERE board_id=? AND bltn_no=?");
        idx = 0;
        idx++; cnttPstmt.setString(idx, apForm.getBltnCntt());
        idx++; cnttPstmt.setString(idx, apForm.getBoardId());
        idx++; cnttPstmt.setString(idx, apForm.getBltnNo());
        cnttPstmt.executeUpdate();
        cnttPstmt.close();
      }
      else if ("del".equals(apForm.getAct()))
      {
        pstmt = conn.prepareStatement("DELETE FROM bltn_poll WHERE board_id=? AND bltn_no=?");
        pstmt.setString(1, apForm.getBoardId());
        pstmt.setString(2, apForm.getBltnNo());
        pstmt.executeUpdate();
        pstmt.close();

        pstmt = conn.prepareStatement("DELETE FROM bltn_cntt WHERE board_id=? AND bltn_no=?");
        pstmt.setString(1, apForm.getBoardId());
        pstmt.setString(2, apForm.getBltnNo());
        pstmt.executeUpdate();
        pstmt.close();

        pstmt = conn.prepareStatement("DELETE FROM bulletin WHERE board_id=? AND bltn_no=?");
        pstmt.setString(1, apForm.getBoardId());
        pstmt.setString(2, apForm.getBltnNo());
        pstmt.executeUpdate();
      }
      else if ("dup".equals(apForm.getAct()))
      {
        List cols = new ArrayList();

        pstmt = conn.prepareStatement("SELECT column_name FROM cols WHERE table_name='BULLETIN' ORDER BY column_id");
        rslt = pstmt.executeQuery();
        cols.clear();
        while (rslt.next()) {
          cols.add(rslt.getString(1));
        }
        rslt.close();
        pstmt.close();

        sb.setLength(0);
        sb.append("'" + apForm.getDstBoardId() + "'");
        for (int i = 1; i < cols.size(); i++) {
          sb.append("," + (String)cols.get(i));
        }
        pstmt = conn.prepareStatement("INSERT INTO bulletin SELECT " + sb.toString() + " FROM bulletin WHERE board_id=? AND bltn_no=?");

        pstmt.setString(1, apForm.getBoardId());
        pstmt.setString(2, apForm.getBltnNo());
        pstmt.executeUpdate();
        pstmt.close();

        pstmt = conn.prepareStatement("INSERT INTO bltn_cntt SELECT ?, bltn_no, bltn_cntt FROM bltn_cntt WHERE board_id=? AND bltn_no=?");
        pstmt.setString(1, apForm.getDstBoardId());
        pstmt.setString(2, apForm.getBoardId());
        pstmt.setString(3, apForm.getBltnNo());
        pstmt.executeUpdate();
        pstmt.close();

        pstmt = conn.prepareStatement("SELECT column_name FROM cols WHERE table_name='BLTN_POLL' ORDER BY column_id");
        rslt = pstmt.executeQuery();
        cols.clear();
        while (rslt.next()) {
          cols.add(rslt.getString(1));
        }
        rslt.close();
        pstmt.close();

        sb.setLength(0);
        sb.append("'" + apForm.getDstBoardId() + "'");
        for (int i = 1; i < cols.size(); i++) {
          sb.append("," + (String)cols.get(i));
        }
        pstmt = conn.prepareStatement("INSERT INTO bltn_poll SELECT " + sb.toString() + " FROM bltn_poll WHERE board_id=? AND bltn_no=?");
        pstmt.setString(1, apForm.getBoardId());
        pstmt.setString(2, apForm.getBltnNo());
        pstmt.executeUpdate();
        pstmt.close();

        copyPollBltnImage(apForm, connCtxt);
      }
    }
    catch (Exception e) {
      this.logger.error("AdminDAO.setPollQuestion()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(rslt);
      close(pstmt);
      close(bltnPstmt);
      close(cnttPstmt);
    }
    this.logger.info("END::AdminDAO.setPollQuestion()");
  }

  protected void copyPollBoardImage(AdminPollForm apForm)
  {
    String srcDirName = apForm.getUploadPath() + File.separator + apForm.getBoardId();
    String dstDirName = apForm.getUploadPath() + File.separator + apForm.getDstBoardId();
    this.logger.debug("Copy " + srcDirName + " to " + dstDirName);
    File srcDir = new File(srcDirName);
    if (srcDir.isDirectory()) {
      File[] srcFiles = srcDir.listFiles();
      File dstDir = new File(dstDirName);
      dstDir.mkdirs();

      for (int i = 0; i < srcFiles.length; i++) {
        this.logger.debug("\tCopying " + srcFiles[i]);
        File dstFile = new File(dstDirName + File.separator + srcFiles[i].getName());
        copyFile(srcFiles[i], dstFile);
      }
    }
  }

  protected void copyPollBltnImage(AdminPollForm apForm, ConnectionContext ctx)
  {
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "SELECT BLTN_SUBJ FROM BULLETIN WHERE BLTN_NO=? AND BLTN_SUBJ IS NOT NULL AND BLTN_SUBJ <> ' ' UNION SELECT FILE_MASK FROM BLTN_POLL WHERE BLTN_NO=? AND FILE_MASK IS NOT NULL AND FILE_MASK <> ' '";

    String srcDirName = apForm.getUploadPath() + File.separator + apForm.getBoardId();
    String dstDirName = apForm.getUploadPath() + File.separator + apForm.getDstBoardId();

    File dstDir = new File(dstDirName);
    dstDir.mkdirs();
    try
    {
      ps = ctx.getConnection().prepareStatement(sql);
      ps.setString(1, apForm.getBltnNo());
      ps.setString(2, apForm.getBltnNo());
      rs = ps.executeQuery();
      while (rs.next()) {
        String fileMask = rs.getString(1);
        File srcFile = new File(srcDirName + File.separator + fileMask);
        if (srcFile.exists()) {
          File dstFile = new File(dstDirName + File.separator + fileMask);
          copyFile(srcFile, dstFile);
        } else {
          this.log.debug("src file " + srcFile.getAbsoluteFile() + " not found");
        }
      }
    }
    catch (Exception e) {
      this.log.error(e, e);
    } finally {
      close(rs);
      close(ps);
    }
  }

  public List getAllPollAs(String boardId, String bltnNo, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.getAllPollAs()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rslt = null;

    List pollAList = new ArrayList();
    StringBuffer sb = new StringBuffer();

    sb.append("SELECT rownum, board_id, bltn_no, poll_seq, poll_kind, t2.code_name, poll_pnt, file_mask, poll_cntt");
    sb.append(" FROM bltn_poll t1 LEFT OUTER JOIN codebase t2 ON t2.system_code='PT' AND t2.code_id='962' AND t2.code=t1.poll_kind");
    sb.append(" WHERE t1.board_id=? AND t1.bltn_no=?");
    sb.append(" ORDER BY poll_seq");
    try
    {
      conn = connCtxt.getConnection();

      pstmt = conn.prepareStatement(sb.toString());
      pstmt.setString(1, boardId);
      pstmt.setString(2, bltnNo);

      rslt = pstmt.executeQuery();
      while (rslt.next())
      {
        int idx = 0;
        BltnPollVO bltnPollVO = new BltnPollVO();

        idx++; bltnPollVO.setRnum(rslt.getInt(idx));
        idx++; bltnPollVO.setBoardId(rslt.getString(idx));
        idx++; bltnPollVO.setBltnNo(rslt.getString(idx));
        idx++; bltnPollVO.setPollSeq(rslt.getInt(idx));
        idx++; bltnPollVO.setPollKind(rslt.getString(idx));
        idx++; bltnPollVO.setPollKindNm(rslt.getString(idx));
        idx++; bltnPollVO.setPollPnt(rslt.getInt(idx));
        idx++; bltnPollVO.setFileMask(rslt.getString(idx));
        idx++; bltnPollVO.setPollCntt(rslt.getString(idx));

        pollAList.add(bltnPollVO);
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.getAllPollAs()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(rslt);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.getAllPollAs()");
    return pollAList;
  }

  public BltnPollVO getPollAnswer(String boardId, String bltnNo, int pollSeq, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.getPollAnswer()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rslt = null;

    BltnPollVO bltnPollVO = null;
    StringBuffer sb = new StringBuffer();

    sb.append("SELECT board_id, bltn_no, poll_seq, poll_kind, t2.code_name, poll_pnt, file_mask, poll_cntt");
    sb.append(" FROM bltn_poll t1 LEFT OUTER JOIN codebase t2 ON t2.system_code='PT' AND t2.code_id='962' AND t2.code=t1.poll_kind");
    sb.append(" WHERE t1.board_id=? AND t1.bltn_no=? AND poll_seq=?");
    try
    {
      conn = connCtxt.getConnection();

      pstmt = conn.prepareStatement(sb.toString());
      pstmt.setString(1, boardId);
      pstmt.setString(2, bltnNo);
      pstmt.setInt(3, pollSeq);

      rslt = pstmt.executeQuery();
      while (rslt.next())
      {
        int idx = 0;
        bltnPollVO = new BltnPollVO();

        idx++; bltnPollVO.setBoardId(rslt.getString(idx));
        idx++; bltnPollVO.setBltnNo(rslt.getString(idx));
        idx++; bltnPollVO.setPollSeq(rslt.getInt(idx));
        idx++; bltnPollVO.setPollKind(rslt.getString(idx));
        idx++; bltnPollVO.setPollKindNm(rslt.getString(idx));
        idx++; bltnPollVO.setPollPnt(rslt.getInt(idx));
        idx++; bltnPollVO.setFileMask(rslt.getString(idx));
        idx++; bltnPollVO.setPollCntt(rslt.getString(idx));
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.getPollAnswer()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(rslt);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.getPollAnswer()");
    return bltnPollVO;
  }

  public void setPollAnswer(AdminPollForm apForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setPollAnswer()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rslt = null;

    StringBuffer sb = new StringBuffer();
    try
    {
      conn = connCtxt.getConnection();

      if ("ins".equals(apForm.getAct()))
      {
        sb.append("INSERT INTO bltn_poll");
        sb.append(" ( board_id, bltn_no, poll_seq, poll_kind, poll_pnt, file_mask, poll_cntt )");
        sb.append(" VALUES (?,?,?,?,?,?,?)");

        pstmt = conn.prepareStatement(sb.toString());

        int idx = 0;
        idx++; pstmt.setString(idx, apForm.getBoardId());
        idx++; pstmt.setString(idx, apForm.getBltnNo());
        idx++; pstmt.setInt(idx, apForm.getPollSeq());
        idx++; pstmt.setString(idx, apForm.getPollKind());
        idx++; pstmt.setInt(idx, apForm.getPollPnt());
        idx++; pstmt.setString(idx, apForm.getFileMask());
        idx++; pstmt.setString(idx, apForm.getPollCntt());

        pstmt.executeUpdate();
      }
      else if ("upd".equals(apForm.getAct()))
      {
        sb.append("UPDATE bltn_poll");
        sb.append(" SET poll_kind=?, poll_pnt=?, file_mask=?, poll_cntt=?");
        sb.append(" WHERE board_id=? AND bltn_no=? AND poll_seq=?");

        pstmt = conn.prepareStatement(sb.toString());

        int idx = 0;
        idx++; pstmt.setString(idx, apForm.getPollKind());
        idx++; pstmt.setInt(idx, apForm.getPollPnt());
        idx++; pstmt.setString(idx, apForm.getFileMask());
        idx++; pstmt.setString(idx, apForm.getPollCntt());
        idx++; pstmt.setString(idx, apForm.getBoardId());
        idx++; pstmt.setString(idx, apForm.getBltnNo());
        idx++; pstmt.setInt(idx, apForm.getPollSeq());

        pstmt.executeUpdate();
      }
      else if ("del".equals(apForm.getAct()))
      {
        sb.append("DELETE FROM bltn_poll WHERE board_id=? AND bltn_no=? AND poll_seq=?");

        int idx = 0;
        pstmt = conn.prepareStatement(sb.toString());
        idx++; pstmt.setString(idx, apForm.getBoardId());
        idx++; pstmt.setString(idx, apForm.getBltnNo());
        idx++; pstmt.setInt(idx, apForm.getPollSeq());

        pstmt.executeUpdate();
      }
    }
    catch (Exception e) {
      this.logger.error("AdminDAO.setPollAnswer()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(rslt);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.setPollAnswer()");
  }

  public List getPollUserResult(BoardVO brdVO, AdminPollForm apForm, ConnectionContext connCtxt)
    throws BaseException
  {
    Connection conn = null;
    PreparedStatement pstmt = null; PreparedStatement cnttPstmt = null; PreparedStatement pollPstmt = null; PreparedStatement pollRsltPstmt = null;
    ResultSet rslt = null; ResultSet cnttRslt = null; ResultSet pollRslt = null; ResultSet pollRsltRslt = null;
    ArrayList pollQList = new ArrayList();
    try
    {
      conn = connCtxt.getConnection();

      StringBuffer sb = new StringBuffer();

      if ("rsltX".equals(apForm.getView())) {
        sb.append("SELECT bltn_gq, poll_seq, poll_reply");
        sb.append(" FROM bulletin a JOIN bltn_poll_rslt b ON b.board_id=a.board_id AND b.bltn_no=a.bltn_no");
        sb.append(" WHERE a.board_id=? AND b.user_id=?");
        sb.append(" ORDER BY a.bltn_gq DESC");

        pstmt = conn.prepareStatement(sb.toString());
        pstmt.setString(1, brdVO.getBoardRid());
        pstmt.setString(2, apForm.getUserId());
        rslt = pstmt.executeQuery();

        while (rslt.next()) {
          BulletinVO bltnVO = new BulletinVO();
          brdVO.setIsPolled(true);
          bltnVO.setPolledSeq(rslt.getInt(2));
          bltnVO.setPolledReply(rslt.getString(3));
          pollQList.add(bltnVO);
        }
      } else {
        sb.setLength(0);
        sb.append("SELECT poll_seq, poll_kind, poll_pnt, file_mask, poll_cntt");
        sb.append(" FROM bltn_poll");
        sb.append(" WHERE board_id=? AND bltn_no=?");
        sb.append(" ORDER BY poll_seq");
        pollPstmt = conn.prepareStatement(sb.toString());

        sb.setLength(0);
        sb.append("SELECT a.poll_seq, a.poll_reply, b.poll_pnt");
        sb.append(" FROM bltn_poll_rslt a ");
        sb.append(" JOIN bltn_poll b ON a.board_id = b.board_id and a.bltn_no = b.bltn_no and a.poll_seq = b.poll_seq ");
        sb.append(" WHERE a.board_id=? AND a.bltn_no=?");
        if (!ValidateUtil.isEmpty(apForm.getUserId()))
          sb.append(" AND user_id='" + apForm.getUserId() + "'");
        else if (!ValidateUtil.isEmpty(apForm.getUserIp())) {
          sb.append(" AND user_ip='" + apForm.getUserIp() + "'");
        }
        pollRsltPstmt = conn.prepareStatement(sb.toString());

        cnttPstmt = conn.prepareStatement("SELECT bltn_cntt FROM bltn_cntt WHERE board_id=? AND bltn_no=?");

        pstmt = conn.prepareStatement("SELECT board_id, bltn_no, bltn_gq FROM bulletin WHERE board_id=? ORDER BY bltn_gq DESC");
        pstmt.setString(1, brdVO.getBoardRid());
        rslt = pstmt.executeQuery();

        ArrayList pollAList = null;
        while (rslt.next()) {
          BulletinVO bltnVO = new BulletinVO();
          bltnVO.setBoardId(rslt.getString("board_id"));
          bltnVO.setBltnNo(rslt.getString("bltn_no"));
          bltnVO.setBltnGq(rslt.getInt("bltn_gq") * -1);

          cnttPstmt.setString(1, bltnVO.getBoardId());
          cnttPstmt.setString(2, bltnVO.getBltnNo());
          cnttRslt = cnttPstmt.executeQuery();

          if (cnttRslt.next()) {
            bltnVO.setBltnCntt(cnttRslt.getString("bltn_cntt"));
          }

          pollPstmt.setString(1, bltnVO.getBoardId());
          pollPstmt.setString(2, bltnVO.getBltnNo());
          pollRslt = pollPstmt.executeQuery();
          pollAList = new ArrayList();
          int rnum = 0;
          while (pollRslt.next())
          {
            int idx = 0;
            BltnPollVO bltnPollVO = new BltnPollVO();

            rnum++; bltnPollVO.setRnum(rnum);
            idx++; bltnPollVO.setPollSeq(pollRslt.getInt(idx));
            idx++; bltnPollVO.setPollKind(pollRslt.getString(idx));
            idx++; bltnPollVO.setPollPnt(pollRslt.getInt(idx));
            idx++; bltnPollVO.setFileMask(pollRslt.getString(idx));
            idx++; bltnPollVO.setPollCntt(pollRslt.getString(idx));

            pollAList.add(bltnPollVO);
          }
          pollRslt.close();

          bltnVO.setPollList(pollAList);

          pollRsltPstmt.setString(1, bltnVO.getBoardId());
          pollRsltPstmt.setString(2, bltnVO.getBltnNo());
          pollRsltRslt = pollRsltPstmt.executeQuery();
          if (pollRsltRslt.next())
          {
            brdVO.setIsPolled(true);

            bltnVO.setPolledSeq(pollRsltRslt.getInt("poll_seq"));
            bltnVO.setPolledReply(pollRsltRslt.getString("poll_reply"));
            bltnVO.setPolledPnt(pollRsltRslt.getInt("poll_pnt"));
          }
          pollRsltRslt.close();

          pollQList.add(bltnVO);
        }
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.getPollUserResult()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(rslt);
      close(cnttRslt);
      close(pollRslt);
      close(pollRsltRslt);
      close(pstmt);
      close(cnttPstmt);
      close(pollPstmt);
      close(pollRsltPstmt);
    }
    return pollQList;
  }

  public int getPollUserPntSum(String boardId, String userId, ConnectionContext connCtxt)
    throws BaseException
  {
    try
    {
      String user = null;
      StringBuffer sb = new StringBuffer();
      sb.setLength(0);
      sb.append("SELECT sum( b.poll_pnt)");
      sb.append(" FROM bltn_poll_rslt a ");
      sb.append(" JOIN bltn_poll b ON a.board_id = b.board_id and a.bltn_no = b.bltn_no and a.poll_seq = b.poll_seq ");
      sb.append(" WHERE a.board_id=? ");
      sb.append(" AND ( user_id=? OR user_ip = ?)");

      return queryForInt(connCtxt, sb.toString(), new Object[] { boardId, userId, userId });
    }
    catch (Exception e) {
      this.logger.error(e, e);
    }throw new BaseException("mm.error.sql.problem");
  }

  public String getPollEvalRemark(String boardId, int pnt, ConnectionContext connCtxt)
    throws BaseException
  {
    try
    {
      return queryForString(connCtxt, "SELECT eval_remark FROM bltn_poll_eval WHERE board_id = ? AND ? BETWEEN EVAL_FROM AND EVAL_TO ", new Object[] { boardId, Integer.valueOf(pnt) });
    }
    catch (Exception e) {
      this.logger.error(e, e);
    }throw new BaseException("mm.error.sql.problem");
  }

  public List getPollEvalCntList(BoardVO brdVO, ConnectionContext connCtxt)
    throws BaseException
  {
    try
    {
      return queryForList(connCtxt, " SELECT eval_remark  , ( SELECT count(*)      FROM (        SELECT a.user_id, sum( poll_pnt) sum_pnt        FROM bltn_poll_rslt  a        JOIN bltn_poll b ON a.board_id = b.board_id AND a.bltn_no = b.bltn_no AND a.poll_seq = b.poll_seq        GROUP BY a.user_id      ) t WHERE sum_pnt BETWEEN a.eval_from AND a.eval_to  ) eval_cnt  FROM bltn_poll_eval a  WHERE board_id=?  ORDER BY eval_seq  ", 
        new Object[] { brdVO.getBoardRid() });
    } catch (Exception e) {
      this.logger.error(e, e);
    }throw new BaseException("mm.error.sql.problem");
  }

  public void setPollPartType(AdminAuxilForm aaForm, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.setPollPartType()");

    Connection conn = null;
    PreparedStatement pstmt = null; PreparedStatement selPstmt = null;
    ResultSet rslt = null;
    try
    {
      conn = connCtxt.getConnection();
      pstmt = conn.prepareStatement("UPDATE security_permission SET action_mask=0 WHERE res_type=5 AND res_url=?");
      pstmt.setString(1, aaForm.getBoardId());
      pstmt.executeUpdate();
      pstmt.close();

      pstmt = conn.prepareStatement("UPDATE security_permission SET action_mask=63 WHERE res_type=5 AND res_url=? AND principal_id=?");
      pstmt.setString(1, aaForm.getBoardId());
      pstmt.setLong(2, Long.parseLong(aaForm.getSelGradeId()));
      pstmt.executeUpdate();
      pstmt.close();

      selPstmt = conn.prepareStatement("SELECT SUBSTR(short_path, LENGTH(short_path)) FROM security_principal WHERE principal_id=?");
      selPstmt.setLong(1, Long.parseLong(aaForm.getSelGradeId()));
      rslt = selPstmt.executeQuery();
      if ((rslt.next()) && 
        (!"D".equals(rslt.getString(1))))
      {
        pstmt = conn.prepareStatement("DELETE FROM security_grade_user WHERE grade_id IN (SELECT principal_id FROM security_principal WHERE principal_type='g' AND short_path LIKE ?)");
        pstmt.setString(1, "/PT" + aaForm.getBoardId() + "%");
        pstmt.executeUpdate();
        pstmt.close();
        pstmt = conn.prepareStatement("DELETE FROM security_grade_group WHERE grade_id IN (SELECT principal_id FROM security_principal WHERE principal_type='g' AND short_path LIKE ?)");
        pstmt.setString(1, "/PT" + aaForm.getBoardId() + "%");
        pstmt.executeUpdate();
        pstmt.close();
        pstmt = conn.prepareStatement("DELETE FROM security_grade_role WHERE grade_id IN (SELECT principal_id FROM security_principal WHERE principal_type='g' AND short_path LIKE ?)");
        pstmt.setString(1, "/PT" + aaForm.getBoardId() + "%");
        pstmt.executeUpdate();
        pstmt.close();
      }
    }
    catch (Exception e)
    {
      this.logger.error("AdminDAO.setPollPartType()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(rslt);
      close(pstmt);
      close(selPstmt);
    }
    this.logger.info("END::AdminDAO.setPollPartType()");
  }

  public void queueingMail(ParamMap paramMap, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.queueBatchMail()");

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rslt = null;

    StringBuffer sb = new StringBuffer();
    String curSeq = null;
    try
    {
      conn = connCtxt.getConnection();

      sb.append("INSERT INTO queue_mail");
      sb.append(" ( queue_seq, email_svr_addr, email_svr_user, email_svr_pass, fr_addr, to_addr, email_subj, email_cntt )");
      sb.append(" VALUES (?,?,?,?,?,?,?,?)");
      pstmt = conn.prepareStatement(sb.toString());
      int idx = 0;
      curSeq = FormatUtil.getDateF(new Date(System.currentTimeMillis()), "yyyyMMddHHmmssSSS");
      idx++; pstmt.setString(idx, curSeq);
      idx++; pstmt.setString(idx, paramMap.getString("mailSmtpHost"));
      idx++; pstmt.setString(idx, paramMap.getString("mailHostUser"));
      idx++; pstmt.setString(idx, paramMap.getString("mailHostPass"));
      idx++; pstmt.setString(idx, paramMap.getString("frAddr"));
      idx++; pstmt.setString(idx, paramMap.getString("toAddr"));
      idx++; pstmt.setString(idx, paramMap.getString("mailSubj"));
      idx++; pstmt.setString(idx, paramMap.getString("mailCntt"));

      pstmt.executeUpdate();
      pstmt.close();
    }
    catch (Exception e) {
      this.logger.error("MailSender.queueing()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(rslt);
      close(pstmt);
    }
    this.logger.info("END::AdminDAO.queueBatchMail()");
  }

  public List getQueueMailList(ConnectionContext connCtxt)
    throws BaseException
  {
    if (this.logger.isInfoEnabled()) {
      this.logger.info("BEGIN::AdminDAO.getQueueMailList()");
    }
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rslt = null;

    List mailList = new ArrayList();
    try
    {
      conn = connCtxt.getConnection();
      pstmt = conn.prepareStatement("SELECT queue_seq, email_svr_addr, email_svr_user, email_svr_pass, fr_addr, to_addr, email_subj, email_cntt FROM queue_mail");
      rslt = pstmt.executeQuery();

      while (rslt.next())
      {
        ParamMap paramMap = new ParamMap();
        paramMap.putString("queueSeq", rslt.getString("queue_seq"));
        paramMap.putString("mailSmtpHost", rslt.getString("email_svr_addr"));
        paramMap.putString("mailHostUser", rslt.getString("email_svr_user"));
        paramMap.putString("mailHostPass", rslt.getString("email_svr_pass"));
        paramMap.putString("frAddr", rslt.getString("fr_addr"));

        paramMap.putString("toAddr", rslt.getString("to_addr"));

        paramMap.putString("mailSubj", rslt.getString("email_subj"));
        paramMap.putString("mailCntt", rslt.getString("email_cntt"));

        mailList.add(paramMap);
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.getQueueMailList()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(rslt);
      close(pstmt);
    }
    if (this.logger.isInfoEnabled())
      this.logger.info("END::AdminDAO.getQueueMailList()");
    return mailList;
  }

  public void setQueueMail(ParamMap paramMap, ConnectionContext connCtxt)
    throws BaseException
  {
    if (this.logger.isInfoEnabled()) {
      this.logger.info("BEGIN::AdminDAO.setQueueMail()");
    }
    Connection conn = null;
    PreparedStatement pstmt = null;
    try
    {
      conn = connCtxt.getConnection();

      if ("del".equals(paramMap.getString("cmd")))
      {
        pstmt = conn.prepareStatement("DELETE FROM queue_mail WHERE queue_seq=?");
        pstmt.setString(1, paramMap.getString("queueSeq"));
        pstmt.executeUpdate();
      }
      else if ("upd".equals(paramMap.getString("cmd")))
      {
        pstmt = conn.prepareStatement("UPDATE queue_mail SET trial_cnt=NVL(trial_cnt,0)+1, last_trial=?, last_fail_reason=? WHERE queue_seq=?");
        pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
        pstmt.setString(2, paramMap.getString("failReason"));
        pstmt.setString(3, paramMap.getString("queueSeq"));

        pstmt.executeUpdate();
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.setQueueMail()", e);
      throw new BaseException("mm.error.sql.problem");
    } finally {
      close(pstmt);
    }
    if (this.logger.isInfoEnabled())
      this.logger.info("END::AdminDAO.setQueueMail()");
  }

  public int fixAttachedFile(String root, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.fixAttachedFile()");

    Connection conn = null;
    Statement stmt = null;
    ResultSet result = null;
    int delCnt = 0;
    try
    {
      String sep = System.getProperty("file.separator");

      String attachPath = root + sep + "attach";
      String editorPath = root + sep + "editor";
      String thumbPath = root + sep + "thumb";
      String pollPath = root + sep + "poll";

      File attachDir = new File(attachPath);
      File editorDir = new File(editorPath);
      File thumbDir = new File(thumbPath);
      File pollDir = new File(pollPath);

      conn = connCtxt.getConnection();
      stmt = conn.createStatement();

      String[] attachDirList = attachDir.list();
      String[] editorDirList = editorDir.list();
      String[] thumbDirList = thumbDir.list();
      String[] pollDirList = pollDir.list();

      String qry1 = "SELECT board_id, owntbl_yn, owntbl_fix FROM board WHERE board_id = board_rid ORDER BY board_id";
      String qry2 = null;
      String qry3 = null;

      Map brdMap = new HashMap();
      result = stmt.executeQuery(qry1);
      while (result.next()) {
        if ("Y".equals(result.getString(2)))
          brdMap.put(result.getString(1), result.getString(3).toLowerCase());
        else {
          brdMap.put(result.getString(1), "#N#");
        }
      }
      if (attachDirList != null)
      {
        for (int di = 0; di < attachDirList.length; di++)
        {
          File subDir = new File(attachPath + sep + attachDirList[di]);
          if (!subDir.isDirectory()) {
            continue;
          }
          String[] attachList = subDir.list();
          if (attachList == null) {
            continue;
          }
          if (brdMap.get(attachDirList[di]) == null)
          {
            for (int i = 0; i < attachList.length; i++)
              delCnt += deleteFile(attachPath + sep + attachDirList[di] + sep + attachList[i]);
            subDir.delete();
          } else {
            String fileTbl = "bltn_file";

            if (!"#N#".equals(brdMap.get(attachDirList[di]))) {
              fileTbl = "bltn_" + brdMap.get(attachDirList[di]) + "_file";
            }

            for (int i = 0; i < attachList.length; i++) {
              qry2 = "SELECT COUNT(board_id) FROM " + fileTbl + " WHERE board_id='" + attachDirList[di] + "' AND file_mask='" + attachList[i] + "'";
              result = stmt.executeQuery(qry2);
              if ((!result.next()) || 
                (result.getInt(1) >= 1))
                continue;
              delCnt += deleteFile(attachPath + sep + attachDirList[di] + sep + attachList[i]);
            }
          }
        }
      }

      if (thumbDirList != null) {
        for (int di = 0; di < thumbDirList.length; di++)
        {
          File subDir = new File(thumbPath + sep + thumbDirList[di]);
          if (!subDir.isDirectory()) {
            continue;
          }
          String[] thumbList = subDir.list();
          if (thumbList == null) {
            continue;
          }
          if (brdMap.get(thumbDirList[di]) == null)
          {
            for (int i = 0; i < thumbList.length; i++)
              delCnt += deleteFile(thumbPath + sep + thumbDirList[di] + sep + thumbList[i]);
            subDir.delete();
          } else {
            String cnttTbl = "bltn_cntt";
            if (!"#N#".equals(brdMap.get(thumbDirList[di]))) {
              cnttTbl = "bltn_" + brdMap.get(thumbDirList[di]) + "_cntt";
            }
            for (int i = 0; i < thumbList.length; i++) {
              String thumbFileNm = thumbList[i];
              if ((thumbList[i].startsWith("T050")) || (thumbList[i].startsWith("T100")) || (thumbList[i].startsWith("T150"))) {
                thumbFileNm = "T" + thumbList[i].substring(4);
              }
              qry2 = "SELECT COUNT(board_id) FROM " + cnttTbl + " WHERE board_id='" + thumbDirList[di] + "' AND bltn_cntt LIKE '%" + thumbFileNm + "%'";
              result = stmt.executeQuery(qry2);
              if ((!result.next()) || 
                (result.getInt(1) >= 1)) continue;
              delCnt += deleteFile(thumbPath + sep + thumbDirList[di] + sep + thumbList[i]);
            }
          }
        }
      }

      if (editorDirList != null) {
        for (int di = 0; di < editorDirList.length; di++)
        {
          File subDir = new File(editorPath + sep + editorDirList[di]);
          if (!subDir.isDirectory()) {
            continue;
          }
          String[] editorList = subDir.list();
          if (editorList == null) {
            continue;
          }
          if (brdMap.get(editorDirList[di]) == null) {
            for (int i = 0; i < editorList.length; i++)
              delCnt += deleteFile(editorPath + sep + editorDirList[di] + sep + editorList[i]);
            subDir.delete();
          } else {
            String cnttTbl = "bltn_cntt";
            if (!"#N#".equals(brdMap.get(editorDirList[di]))) {
              cnttTbl = "bltn_" + brdMap.get(editorDirList[di]) + "_cntt";
            }
            for (int i = 0; i < editorList.length; i++) {
              qry3 = "SELECT COUNT(board_id) FROM " + cnttTbl + " WHERE board_id='" + editorDirList[di] + "' AND bltn_cntt LIKE '%" + editorList[i] + "%'";
              result = stmt.executeQuery(qry3);
              if ((!result.next()) || 
                (result.getInt(1) >= 1)) continue;
              delCnt += deleteFile(editorPath + sep + editorDirList[di] + sep + editorList[i]);
            }
          }
        }
      }

      if (pollDirList != null)
      {
        for (int di = 0; di < pollDirList.length; di++)
        {
          File subDir = new File(pollPath + sep + pollDirList[di]);
          if (!subDir.isDirectory()) {
            continue;
          }
          String[] pollList = subDir.list();
          if (pollList == null) {
            continue;
          }
          if (brdMap.get(pollDirList[di]) == null)
          {
            for (int i = 0; i < pollList.length; i++)
              delCnt += deleteFile(pollPath + sep + pollDirList[di] + sep + pollList[i]);
            subDir.delete();
          } else {
            String pollTbl = "bltn_poll";

            for (int i = 0; i < pollList.length; i++) {
              qry2 = "SELECT COUNT(board_id) FROM " + pollTbl + " WHERE board_id='" + pollDirList[di] + "' AND file_mask='" + pollList[i] + "'";
              result = stmt.executeQuery(qry2);
              if ((!result.next()) || 
                (result.getInt(1) >= 1))
                continue;
              delCnt += deleteFile(pollPath + sep + pollDirList[di] + sep + pollList[i]);
            }
          }
        }
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.fixAttachedFile()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(result);
      close(stmt);
    }

    this.logger.info("END::AdminDAO.fixAttachedFile()");

    return delCnt;
  }

  public int fixPollImage(String root, ConnectionContext connCtxt) throws BaseException {
    this.logger.debug(root);
    int count = 0;
    String sql = "SELECT COUNT(*) FROM ( SELECT FILE_MASK FROM BLTN_POLL WHERE BOARD_ID=? AND FILE_MASK=? UNION SELECT BLTN_SUBJ FROM BULLETIN WHERE BOARD_ID=? AND BLTN_SUBJ=? ) A";
    PreparedStatement ps = null;
    ResultSet rs = null;
    String boardId = null; String fileMask = null;
    boolean exist = false;
    try {
      ps = connCtxt.getConnection().prepareStatement(sql);
      File rootDir = new File(root);

      File[] files = rootDir.listFiles();
      this.logger.debug("file count=" + files.length);
      for (int i = 0; i < files.length; i++) {
        this.log.debug("checking " + files[i].getName());
        if (!files[i].isDirectory()) {
          continue;
        }
        boardId = files[i].getName();
        File[] images = files[i].listFiles();

        if (images.length == 0) {
          files[i].delete();
        }

        for (int j = 0; j < images.length; j++) {
          this.log.debug("checking " + boardId + "." + images[j].getName());
          if (images[j].isDirectory()) {
            continue;
          }
          fileMask = images[j].getName();
          ps.clearParameters();
          ps.setString(1, boardId);
          ps.setString(2, fileMask);
          ps.setString(3, boardId);
          ps.setString(4, fileMask);
          rs = ps.executeQuery();
          exist = false;
          if (rs.next()) {
            exist = rs.getInt(1) > 0;
          }
          close(rs);
          if (!exist) {
            count++;
            images[j].delete();
            this.log.debug("** delete " + boardId + "." + fileMask);
          }
        }
      }
    } catch (Exception e) {
      this.log.error(e, e);
    } finally {
      close(rs);
      close(ps);
    }
    return count;
  }

  public int fixPollImage(File dir, PreparedStatement ps) throws SQLException {
    int count = 0;
    String boardId = dir.getName();
    File[] files = dir.listFiles();
    this.logger.debug("file count=" + files.length);
    this.logger.debug("boardId=" + boardId + ", file count=" + files.length);
    return count;
  }

  public int fixAttachedFileBAK(String root, ConnectionContext connCtxt)
    throws BaseException
  {
    this.logger.info("BEGIN::AdminDAO.fixAttachedFile()");

    Connection conn = null;
    Statement stmt = null;
    ResultSet result = null;
    int delCnt = 0;
    try
    {
      String sep = System.getProperty("file.separator");

      String attachPath = root + sep + "attach";
      String editorPath = root + sep + "editor";
      String thumbPath = root + sep + "thumb";
      String pollPath = root + sep + "poll";

      File attachDir = new File(attachPath);
      File editorDir = new File(editorPath);
      File thumbDir = new File(thumbPath);
      File pollDir = new File(pollPath);

      conn = connCtxt.getConnection();
      stmt = conn.createStatement();

      String[] attachDirList = attachDir.list();
      String[] editorDirList = editorDir.list();
      String[] thumbDirList = thumbDir.list();
      String[] pollDirList = pollDir.list();

      String qry1 = "SELECT board_id, owntbl_yn, owntbl_fix FROM board WHERE board_id = board_rid ORDER BY board_id";
      String qry2 = null;
      String qry3 = null;

      Map brdMap = new HashMap();
      result = stmt.executeQuery(qry1);
      while (result.next()) {
        if ("Y".equals(result.getString(2)))
          brdMap.put(result.getString(1), result.getString(3).toLowerCase());
        else {
          brdMap.put(result.getString(1), "#N#");
        }
      }
      if (attachDirList != null)
      {
        for (int di = 0; di < attachDirList.length; di++)
        {
          File subDir = new File(attachPath + sep + attachDirList[di]);
          if (!subDir.isDirectory()) {
            continue;
          }
          String[] attachList = subDir.list();
          if (attachList == null) {
            continue;
          }
          if (brdMap.get(attachDirList[di]) == null)
          {
            for (int i = 0; i < attachList.length; i++)
              delCnt += deleteFile(attachPath + sep + attachDirList[di] + sep + attachList[i]);
            subDir.delete();
          } else {
            String fileTbl = "bltn_file";

            if (!"#N#".equals(brdMap.get(attachDirList[di]))) {
              fileTbl = "bltn_" + brdMap.get(attachDirList[di]) + "_file";
            }

            for (int i = 0; i < attachList.length; i++) {
              qry2 = "SELECT COUNT(board_id) FROM " + fileTbl + " WHERE board_id='" + attachDirList[di] + "' AND file_mask='" + attachList[i] + "'";
              result = stmt.executeQuery(qry2);
              if ((!result.next()) || 
                (result.getInt(1) >= 1))
                continue;
              delCnt += deleteFile(attachPath + sep + attachDirList[di] + sep + attachList[i]);
            }
          }
        }
      }

      if (thumbDirList != null) {
        for (int di = 0; di < thumbDirList.length; di++)
        {
          File subDir = new File(thumbPath + sep + thumbDirList[di]);
          if (!subDir.isDirectory()) {
            continue;
          }
          String[] thumbList = subDir.list();
          if (thumbList == null) {
            continue;
          }
          if (brdMap.get(thumbDirList[di]) == null)
          {
            for (int i = 0; i < thumbList.length; i++)
              delCnt += deleteFile(thumbPath + sep + thumbDirList[di] + sep + thumbList[i]);
            subDir.delete();
          } else {
            String fileTbl = "bltn_file";
            if (!"#N#".equals(brdMap.get(thumbDirList[di]))) {
              fileTbl = "bltn_" + brdMap.get(thumbDirList[di]) + "_file";
            }
            for (int i = 0; i < thumbList.length; i++) {
              String thumbFileNm = thumbList[i];
              if ((thumbList[i].startsWith("T050")) || (thumbList[i].startsWith("T100")) || (thumbList[i].startsWith("T150"))) {
                thumbFileNm = "T" + thumbList[i].substring(4);
              }
              qry2 = "SELECT COUNT(board_id) FROM " + fileTbl + " WHERE board_id='" + thumbDirList[di] + "' AND file_mask='" + thumbFileNm + "'";
              result = stmt.executeQuery(qry2);
              if ((!result.next()) || 
                (result.getInt(1) >= 1)) continue;
              delCnt += deleteFile(thumbPath + sep + thumbDirList[di] + sep + thumbList[i]);
            }
          }
        }
      }

      if (editorDirList != null) {
        for (int di = 0; di < editorDirList.length; di++)
        {
          File subDir = new File(editorPath + sep + editorDirList[di]);
          if (!subDir.isDirectory()) {
            continue;
          }
          String[] editorList = subDir.list();
          if (editorList == null) {
            continue;
          }
          if (brdMap.get(editorDirList[di]) == null) {
            for (int i = 0; i < editorList.length; i++)
              delCnt += deleteFile(editorPath + sep + editorDirList[di] + sep + editorList[i]);
            subDir.delete();
          } else {
            String cnttTbl = "bltn_cntt";
            if (!"#N#".equals(brdMap.get(editorDirList[di]))) {
              cnttTbl = "bltn_" + brdMap.get(editorDirList[di]) + "_cntt";
            }
            for (int i = 0; i < editorList.length; i++) {
              qry3 = "SELECT COUNT(board_id) FROM " + cnttTbl + " WHERE board_id='" + editorDirList[di] + "' AND bltn_cntt LIKE '%" + editorList[i] + "%'";
              result = stmt.executeQuery(qry3);
              if ((!result.next()) || 
                (result.getInt(1) >= 1)) continue;
              delCnt += deleteFile(editorPath + sep + editorDirList[di] + sep + editorList[i]);
            }
          }
        }
      }

      if (pollDirList != null)
      {
        for (int di = 0; di < pollDirList.length; di++)
        {
          File subDir = new File(pollPath + sep + pollDirList[di]);
          if (!subDir.isDirectory()) {
            continue;
          }
          String[] pollList = subDir.list();
          if (pollList == null) {
            continue;
          }
          if (brdMap.get(pollDirList[di]) == null)
          {
            for (int i = 0; i < pollList.length; i++)
              delCnt += deleteFile(pollPath + sep + pollDirList[di] + sep + pollList[i]);
            subDir.delete();
          } else {
            String pollTbl = "bltn_poll";

            if (!"#N#".equals(brdMap.get(pollDirList[di]))) {
              pollTbl = "bltn_" + brdMap.get(pollDirList[di]) + "_poll";
            }

            for (int i = 0; i < pollList.length; i++) {
              qry2 = "SELECT COUNT(board_id) FROM " + pollTbl + " WHERE board_id='" + pollDirList[di] + "' AND file_mask='" + pollList[i] + "'";
              result = stmt.executeQuery(qry2);
              if ((!result.next()) || 
                (result.getInt(1) >= 1))
                continue;
              delCnt += deleteFile(pollPath + sep + pollDirList[di] + sep + pollList[i]);
            }
          }
        }
      }
    } catch (Exception e) {
      this.logger.error("AdminDAO.fixAttachedFile()", e);
      BaseException be = new BaseException("mm.error.sql.problem");
      throw be;
    } finally {
      close(result);
      close(stmt);
    }

    this.logger.info("END::AdminDAO.fixAttachedFile()");

    return delCnt;
  }

  public abstract List getColumns(ConnectionContext paramConnectionContext, String paramString)
    throws Exception;

  private void applyTemplate(ConnectionContext connCtxt, String boardId, String boardType)
    throws Exception
  {
    String sql = "SELECT CODE_NAME2 FROM CODEBASE WHERE CODE_ID='951' AND CODE=? AND LANG_KND='ko'";

    String templateId = queryForString(connCtxt, sql, new String[] { boardType });
    if (templateId == null) {
      templateId = "ENBOARD.BASE.PT";
    }
    sql = " UPDATE BOARD SET ( \t\t\t    MERGE_TYPE,\t\t\t    FUNC_YNS,\t\t\t    BUGA_YNS,\t\t\t    LIST_YNS,\t\t\t    READ_YNS,  SRCH_YNS,\t\t\t    TTL_YNS,\t\t\t    TTL_LENS,\t\t\t    TERM_FLAG,\t\t\t    RAISE_COLOR,  RAISE_CNT,\t\t\t    NEW_TERM,\t\t\t    MAX_FILE_CNT,\t\t\t    MAX_FILE_SIZE,\t\t\t    MAX_FILE_DOWN,  LIST_SET_CNT,\t\t\t    BAD_STD_CNT,\t\t\t    EXTN_CLASS_NM,\t\t\t    BOARD_WIDTH,\t\t\t    TOP_HTML,  BOTTOM_HTML )  = ( SELECT   MERGE_TYPE,\t\t\t    FUNC_YNS,\t\t\t    BUGA_YNS,\t\t\t    LIST_YNS,\t\t\t    READ_YNS,  SRCH_YNS,\t\t\t    TTL_YNS,\t\t\t    TTL_LENS,\t\t\t    TERM_FLAG,\t\t\t    RAISE_COLOR,  RAISE_CNT,\t\t\t    NEW_TERM,\t\t\t    MAX_FILE_CNT,\t\t\t    MAX_FILE_SIZE,\t\t\t    MAX_FILE_DOWN,  LIST_SET_CNT,\t\t\t    BAD_STD_CNT,\t\t\t    EXTN_CLASS_NM,\t\t\t    BOARD_WIDTH,\t\t\t    TOP_HTML,  BOTTOM_HTML  FROM BOARD   WHERE board_id=?  )  WHERE (board_id=?  OR ( board_rid=? AND board_id != board_rid ))";

    update(connCtxt, sql, new String[] { templateId, boardId, boardId });
  }
}