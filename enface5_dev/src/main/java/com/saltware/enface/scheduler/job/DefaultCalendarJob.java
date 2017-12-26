package com.saltware.enface.scheduler.job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.saltware.enhancer.calendar.service.CalendarService;
import com.saltware.enview.Enview;
import com.saltware.enview.components.dao.ConnectionContext;
import com.saltware.enview.components.dao.ConnectionContextForRdbms;
import com.saltware.enview.exception.BaseException;

public class DefaultCalendarJob implements Job {
	
	private static final Log logger = LogFactory.getLog(DefaultCalendarJob.class);
	
	protected int timeout;
	
	protected CalendarService calendarService;
	
	public DefaultCalendarJob()	{
		calendarService = (CalendarService)Enview.getComponentManager().getComponent("com.saltware.enhancer.calendar.service.CalendarService");
	}
	  
	/**
	  * Setter called after the ExampleJob is instantiated
	  * with the value from the JobDetailBean (5)
	 */ 
	protected void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	protected void init(){ 
		//
	}
	
	public final void execute(JobExecutionContext ctx) throws JobExecutionException {
		JobDataMap dataMap = ctx.getJobDetail().getJobDataMap();
		boolean alreadyRunningJob = dataMap.getBoolean("alreadyRunningJob");
		String batchScheduleId = dataMap.getString("batchScheduleId");
		String batchActionId = dataMap.getString("batchActionId");
		String parameter = dataMap.getString("parameter");
		
		logger.info("start batch schedule id=["+batchActionId+"] for batch action id=["+batchActionId+"]");
    	try {
    		if(!alreadyRunningJob) {
	    		this.init();
	    		this.batchProcess(alreadyRunningJob, batchScheduleId, batchActionId, parameter);
    		}
    		else {
    			logger.warn("batch already running! schedule id=["+batchActionId+"] for batch action id=["+batchActionId+"]");
    		}
        } catch (BaseException e) {
            throw new JobExecutionException(e);
        }
    	logger.info("complete batch schedule id=["+batchActionId+"] for batch action id=["+batchActionId+"]");
	}
	
	protected void batchProcess(boolean alreadyRunningJob, String batchScheduleId, String batchActionId, String parameter) throws BaseException{
		if (logger.isInfoEnabled()) logger.info("BEGIN::DefaultDefaultCalendarJob.batchProcess()");
		
		ConnectionContext connCtxt = null;
		Connection        conn     = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		try {
			connCtxt = new ConnectionContextForRdbms(true);
			conn = connCtxt.getConnection();
			// 디폴트 칼렌더가 없는 사용자를 조회한다.
			sb.append(" SELECT a.SHORT_PATH AS USER_ID, b.DOMAIN_ID, (SELECT COUNT(*) FROM CALENDAR c WHERE c.OWNER_ID = a.SHORT_PATH AND c.DOMAIN_ID = b.DOMAIN_ID AND IS_DEFAULT = 'Y') AS DEFAULT_COUNT ");
			sb.append(" FROM SECURITY_PRINCIPAL a  ");
			sb.append(" JOIN DOMAIN_PRINCIPAL b ON b.PRINCIPAL_ID = a.PRINCIPAL_ID ");
			sb.append( "WHERE  b.DOMAIN_ID || ':' || a.SHORT_PATH  NOT IN (    SELECT  DOMAIN_ID || ':' || SHORT_PATH  FROM CALENDAR WHERE IS_DEFAULT='Y' ) ");
			
			ps = conn.prepareStatement(sb.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				if( rs.getInt("DEFAULT_COUNT") ==0) {
					calendarService.createDefaultCalendar(rs.getString("USER_ID"), rs.getLong("DOMAIN_ID"));
				}
			}
			connCtxt.commit();
		} catch(SQLException e) {
			rollback (connCtxt);
			if (logger.isErrorEnabled()) logger.error("DefaultCalendarJob.batchProcess()",e);
			throw new BaseException(e);
		} finally {
			close (rs);
			close (ps);
			release (connCtxt);
		}
		if (logger.isInfoEnabled()) logger.info("END::DefaultCalendarJob.batchProcess()");
	}
	
	protected void close(PreparedStatement ps) {
		if( ps != null) {
			try {
				ps.close();
			} catch(SQLException e) {
				logger.error( e.getMessage(), e);
			}
		}
    }

	public void close(Statement stmt){
		if (stmt != null) {
			try {
				stmt.close();
			} catch(SQLException e) {
				logger.error( e.getMessage(), e);
			}
		}
	}
	protected void close(ResultSet result) {
		if( result != null) {
			try {
				result.close();
			} catch(SQLException e) {
				logger.error( e.getMessage(), e);
			}
		}
    }
	
	protected void rollback( ConnectionContext connCtxt ) {
		if( connCtxt != null ) connCtxt.rollback();
	}
	protected void release( ConnectionContext connCtxt ) {
		if( connCtxt != null ) connCtxt.release();
	}

	public void afterPropertiesSet() throws Exception {
		// nothing to do...
    }
	
}
