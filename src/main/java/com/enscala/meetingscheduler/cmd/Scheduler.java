/**
 * 
 */
package com.enscala.meetingscheduler.cmd;

import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.enscala.meetingscheduler.service.ScheduleService;


/**
 * This class handles necessary work to call the Schedule Service
 * @author Melik
 *
 */
public class Scheduler {
	private transient String scheduleIngestFolder;
	private transient ScheduleService scheduleService;
	
	
	@Resource
	public void setScheduleService(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

	/**
	 * @param scheduleIngestFolder the scheduleIngestFolder to set
	 */
	public void setScheduleIngestFolder(String scheduleIngestFolder) {
		this.scheduleIngestFolder = scheduleIngestFolder;
	}
	
	
	
	public void ingestSchedule(Logger logger, String file) {
		String path = scheduleIngestFolder+file;
		String result = null;
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(path);
		if (in == null) {
			logger.info("Cannot find file");
		}
		
		else {
			result = scheduleService.processSchedule(in);
		}
		
		logger.info(result);
	}
}
