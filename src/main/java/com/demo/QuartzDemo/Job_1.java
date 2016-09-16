package com.demo.QuartzDemo;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Job_1 implements Job {

	private static Logger _log = LoggerFactory.getLogger(Job_1.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		_log.info("Hello World! - " + new Date());//123
	}

}
