package com.saltware.enface.enboard.integrate;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.saltware.enboard.dao.BaseDAO;
import com.saltware.enboard.dao.BoardDAO;
import com.saltware.enboard.dao.SecurityDAO;
import com.saltware.enboard.exception.BaseException;
import com.saltware.enboard.form.AdminPollForm;
import com.saltware.enboard.integrate.BltnGidMapper;
import com.saltware.enboard.util.Constants;
import com.saltware.enview.components.dao.ConnectionContext;
import com.saltware.enview.components.dao.DAOFactory;

/**
 * 설문을 커스터마이즈하기 위한 클래스 
 * - enboard.properties에 등록한다 
 * - board.bltnGidMapper.class=com.saltware.enface.enboard.integrate.DefaultBltnGidMapper
 * @author smna
 *
 */
public final class DefaultBltnGidMapper extends BaseDAO implements BltnGidMapper {
	
    public String getBltnGid (HttpServletRequest request) {
        return Constants.DEFAULT_BLTN_GID;
    }    

    /********************************************************************************************************
     * 목록화면에서 게시물목록을 구하는데 이용된다.
     * BoardDAO.getBltnList() 와 getBoardStatus() 에서 사용되므로, 해당 메쏘드 내의 SQL 구문에 적절한 형태로 생성되어야 한다.
     * 2011.06.03.KWShin.
     ********************************************************************************************************/
    public String getBltnGidSql (HttpServletRequest request) {
        return "t1.bltn_gid='"+Constants.DEFAULT_BLTN_GID+"'";
    }
	/********************************************************************************************************/

    /********************************************************************************************************
     *  다문항 설문 관련하여 사용된다. 사이트마다 조건이 다를 수 있어서 이쪽으로 뺐다.
     *  2010.12.30.KWShin.
     ********************************************************************************************************/
	public int getPollTrgtCnt (String boardId, ConnectionContext connCtxt) throws BaseException {
		BoardDAO boardDAO = (BoardDAO)DAOFactory.getInst().getDAO (BoardDAO.DAO_NAME_PREFIX);
		return boardDAO.getPollTrgtCnt (boardId, connCtxt);							
	}
	public List getUserAdminList (AdminPollForm apForm, ConnectionContext connCtxt) throws BaseException {
		SecurityDAO secDAO = (SecurityDAO)DAOFactory.getInst().getDAO(SecurityDAO.DAO_NAME_PREFIX);
		return secDAO.getUserAdminList (apForm, connCtxt);
	}
	public String[] getPollExelColTitles() {
		return new String[] {"영문명","닉네임", "핸드폰"};
	}
	public String[] getPollExelColValues(AdminPollForm pollForm, ConnectionContext connCtxt) throws BaseException {
		String[] data = new String[3];
		try {
			List list = queryForList(connCtxt, "SELECT nm_eng, nm_nic, mobile_tel FROM userpass WHERE user_id=?", new String[] { pollForm.getUserId()});
			if( list!=null && list.size() > 0) {
				Map map = (Map)list.get(0);
				data[0] = (String)map.get("nmEng");
				data[1] = (String)map.get("nmNic");
				data[2] = (String)map.get("mobileTel");
			}
		} catch (SQLException e) {
			log.error( e, e);
		}
		return data;
	}
	/********************************************************************************************************/
}