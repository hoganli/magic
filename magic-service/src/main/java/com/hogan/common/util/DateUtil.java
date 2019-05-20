package com.hogan.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil
{
	public static final String PARTERN_YMD = "yyyyMMdd";
	public static final String PARTERN_Y_M_D = "yyyy-MM-dd";
	public static final String PARTERN_YMDHIS = "yyyyMMddHHmmss";
	public static final String PARTERN_YMDHISS = "yyyyMMddHHmmssSSS";
	public static final String PARTERN_Y_M_D_H_I_S = "yyyy-MM-dd HH:mm:ss";

	public static long getCurrentTime()
	{
		return new Date().getTime();
	}

	public static Timestamp getCurrentTimeStamp()
	{
		return new Timestamp(new Date().getTime());
	}

	public static String formatDate(Date dDate, String dateFormat)
	{
		if (dateFormat == null)
			dateFormat = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(dDate);
	}

	public static String formatDateHMS(Date dDate)
	{
		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(dDate);
	}

	public static Date parseDate(String sDate, String dateFormat)
	{
		if (dateFormat == null)
			dateFormat = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try
		{
			return sdf.parse(sDate);
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static String getLastDay(Date date, String dateFormat)
	{
		if (dateFormat == null)
			dateFormat = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(date);
		// 当前月加1，变为下个月
		gcLast.add(Calendar.MONTH, 1);
		// 当前月设置为第一天 01
		gcLast.set(Calendar.DATE, 1);
		// 再减一天即为上个月最后一天
		gcLast.add(Calendar.DATE, -1);
		return sdf.format(gcLast.getTime());
	}

	/**
	 * 获取今天是星期几
	 * @return
	 */
	public static String getWeekDayToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		Integer weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if(weekDay == 0) {
			weekDay = 7;
		}
		return String.valueOf(weekDay);
	}

	/**
	 * 根据日期和格式返回字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getDateByPattern(Date date, String pattern) {
		SimpleDateFormat simpleDateFormat =  new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}

	/**
	 * 判断两个时间间隔的天数
	 * @param startDate
	 * @param endDate
	 * @throws ParseException
	 */
	public static Long getDayByTimeLag(String startDate, String endDate) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date sDate = sdf.parse(startDate);
		Date eDate = sdf.parse(endDate);

		long sd = sDate.getTime();
		long ed = eDate.getTime();

		return (ed - sd) / (24L * 60L * 60L * 1000L);
	}

	public static void main(String argsp[]) {
		System.out.print(getWeekDayToday());
	}
}
