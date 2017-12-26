package com.saltware.enface.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SampleJob implements Job {
	private static final Log logger = LogFactory.getLog(SampleJob.class);

	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		JobDataMap dataMap = ctx.getJobDetail().getJobDataMap();
		boolean alreadyRunningJob = dataMap.getBoolean("alreadyRunningJob");
		String batchScheduleId = dataMap.getString("batchScheduleId");
		String batchActionId = dataMap.getString("batchActionId");
		
		logger.info("start batch schedule id=["+batchScheduleId+"] for batch action id=["+batchActionId+"]");
		
//		try {
			if(!alreadyRunningJob) {
				this.batchProcess(); 
			}
    		else {
    			logger.warn("batch already running! schedule id=["+batchScheduleId+"] for batch action id=["+batchActionId+"]");
    		}
//		} catch (BaseException e) { 
//			throw new JobExecutionException(e);
//		} 
		logger.info("complete batch schedule id=["+batchScheduleId+"] for batch action id=["+batchActionId+"]");		
	}
	
	
	public void batchProcess() {
		
		System.out.println("############ 1111 ######");
		
	}

}
