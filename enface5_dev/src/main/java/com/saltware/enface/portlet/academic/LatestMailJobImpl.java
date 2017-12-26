package com.saltware.enface.portlet.academic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.saltware.enview.exception.BaseException;

/**
 * @Class Name : LatestMailJobImpl.java
 * @Description : 최근메일 Job Implementation @
 * @author kevin
 * @since 2014.03.04 12:58:385
 * @version 1.0
 * @see Copyright (C) by Saltware All right reserved.
 */
public class LatestMailJobImpl implements Job {
	private final Log log = LogFactory.getLog(getClass());
	private int timeout;

	public LatestMailJobImpl() {

	}

	/**
	 * Setter called after the ExampleJob is instantiated with the value from the JobDetailBean (5)
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void execute(JobExecutionContext ctx) throws JobExecutionException {

		JobDataMap dataMap = ctx.getJobDetail().getJobDataMap();
		boolean alreadyRunningJob = dataMap.getBoolean("alreadyRunningJob");
		String batchScheduleId = dataMap.getString("batchScheduleId");
		String batchActionId = dataMap.getString("batchActionId");
		String parameter = dataMap.getString("parameter");
		log.info("*** batchScheduleId=" + batchScheduleId + ", batchActionId=" + batchActionId + ", alreadyRunningJob=" + alreadyRunningJob + ", parameter=" + parameter);
		// if( alreadyRunningJob == true ) {
		if (alreadyRunningJob) {
			return;
		}

//		try {
//
//		} catch (BaseException e) {
//			log.error(e.getMessage(), e);
//			throw new JobExecutionException(e);
//		}
	}
}
