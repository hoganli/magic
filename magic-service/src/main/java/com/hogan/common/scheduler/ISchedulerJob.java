package com.hogan.common.scheduler;

/**
 * ClassName:BaseRequestInterceptor
 * Description: 定时任务接口
 * User:dada
 * Date:2018/5/14 17:46
*/
public interface ISchedulerJob {
	/**
	 * 定时任务
	 */
	public void job();
}
