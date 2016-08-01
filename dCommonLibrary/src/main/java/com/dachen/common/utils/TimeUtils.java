package com.dachen.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.dachen.common.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class TimeUtils {

	// ///s 代表Simple日期格式：yyyy-MM-dd
	// ///f 代表Full日期格式：yyyy-MM-dd hh:mm:ss

	public static final SimpleDateFormat ss_format = new SimpleDateFormat("MM-dd", Locale.CHINA);
	public static final SimpleDateFormat s_format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	public static final SimpleDateFormat f_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	public static final SimpleDateFormat a_format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
	public static final SimpleDateFormat chat_time_format = new SimpleDateFormat("HH:mm", Locale.CHINA);
	public static final SimpleDateFormat u_format = new SimpleDateFormat("yy/MM/dd", Locale.CHINA);

	public static String getHourMinuteSecondStr(long milliseconds) {
		final int S = 1000;
		final int M = S * 60;
		final int H = M * 60;

		long hour = milliseconds / H;
		long minute = (milliseconds - hour * H) / M;
		long second = (milliseconds - hour * H - minute * M) / S;

		if (hour > 0) {
			return String.format("%02d:%02d:%02d", hour, minute, second);
		} else {
			return String.format("%02d:%02d", minute, second);
		}
	}

	public static String u_format(long timestamp) {
		return u_format.format(new Date(timestamp));
	}

	public static long s_str_2_long(String dateString) {
		try {
			Date d = s_format.parse(dateString);
			return d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long f_str_2_long(String dateString) {
		try {
			Date d = f_format.parse(dateString);
			return d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long f_str_3_long(String dateString) {
		try {
			Date d = a_format.parse(dateString);
			return d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static long chat_str_2_long(String dateString) {
		try {
			Date d = chat_time_format.parse(dateString);
			return d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String ss_long_2_str(long timestamp) {
		return ss_format.format(new Date(timestamp));
	}

	public static String s_long_2_str(long timestamp) {
		return s_format.format(new Date(timestamp));
	}

	public static String f_long_2_str(long timestamp) {
		return f_format.format(new Date(timestamp));
	}

	public static String f_long_3_str(long timestamp) {
		return a_format.format(new Date(timestamp));
	}

	public static String a_chat_time_format(long timestamp) {
		return chat_time_format.format(new Date(timestamp));
	}
	public static String a_long_3_str(long timestamp) {
		return a_format.format(new Date(timestamp));
	}

	public static String chat_long_2_str(long timestamp) {
		return chat_time_format.format(new Date(timestamp));
	}

	/**
	 * 获取字符串时间的年份
	 * 
	 * @param dateString
	 *            格式为yyyy-MM-ss，或者yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static int getYear(String dateString) {
		try {
			Date d = s_format.parse(dateString);
			return d.getYear() + 1900;// 年份是基于格林威治时间，所以加上1900
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取字符串时间的月份
	 * 
	 * @param dateString
	 *            格式为yyyy-MM-ss，或者yyyy-MM-dd hh:mm:ss
	 * @return
	 */
	public static int getMonth(String dateString) {
		try {
			Date d = s_format.parse(dateString);
			return d.getMonth();// 月份从0-11
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean isleapyear(int year) {
		boolean isLeap = false;
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			isLeap = true;
		}
		return isLeap;
	}
	
	/**
	 * 获取字符串时间的天
	 * 
	 * @param dateString
	 *            格式为yyyy-MM-ss，或者yyyy-MM-dd hh:mm:ss
	 * @return
	 */
	public static int getDayOfMonth(String dateString) {
		try {
			Date d = s_format.parse(dateString);
			return d.getDate();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 根据年 月 获取对应的月份 天数
	 */
	public static int getDaysByYearMonth(int year, int month) {

		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	public static int getHours(String timeString) {
		SimpleDateFormat formart = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date = formart.parse(timeString);
			return date.getHours();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getMinutes(String timeString) {
		SimpleDateFormat formart = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date = formart.parse(timeString);
			return date.getMinutes();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getSeconds(String timeString) {
		SimpleDateFormat formart = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date = formart.parse(timeString);
			return date.getSeconds();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getCurrentTime() {
		return f_format.format(new Date(System.currentTimeMillis()));
	}

	public static String getCurrentTimeSimple() {
		return s_format.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * 在当前时间上加上多少毫秒，返回这个时间
	 * 
	 * @param mask
	 * @return
	 */
	public static String getCurrentTimeMask(long mask) {
		return f_format.format(new Date(System.currentTimeMillis() + mask));
	}
	
	/**
	 * 在指定时间上加上多少毫秒，返回这个时间
	 * 
	 * @param mask
	 * @return
	 */
	public static String getDesignTimeMask(long time, long mask) {
		return chat_time_format.format(new Date(time + mask));
	}

	/**
	 * 在指定时间上加上多少毫秒，返回这个时间,把00:00变成24:00
	 * 
	 * @param mask
	 * @return
	 */
	public static String getDesignTimeMask2(long time, long mask) {
		String end = chat_time_format.format(new Date(time + mask));
		if(end.equals("00:00"))
			end = "24:00";
		return end;
	}

	// /////////////////////以上是通用的，下面为特殊需求的////////////////////////
	// /**
	// * 时间戳转换日期格式
	// *
	// * @param timestamp
	// * 单位秒
	// * @return
	// */
	// public static String getCurrentTime(long timestamp) {
	// SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// return f.format(new Date(timestamp * 1000));
	// }

	/**
	 * 获取精简的日期
	 * 
	 * @param time
	 * @return
	 */
	public static String getSimpleDate(String time) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = f_format.parse(time);
			return formater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 获取精简的日期
	 * 
	 * @param time
	 * @return
	 */
	public static String getFormaterSimpleDate(String time) {
		SimpleDateFormat formater = new SimpleDateFormat("MM-dd HH:mm");
		Date date = null;
		try {
			date = f_format.parse(time);
			return formater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public static String getSimpleDateTime(String time) {
		SimpleDateFormat formater = new SimpleDateFormat("yy-MM-dd HH:mm");
		Date date = null;
		try {
			date = f_format.parse(time);
			return formater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取yyyy-MM-dd HH:mm格式的日期时间
	 * 
	 * @param time
	 * @return
	 */
	public static String getDateTime(String time) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = null;
		try {
			date = f_format.parse(time);
			return formater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取yyyy-MM-dd HH:mm格式的日期时间
	 * 
	 * @param milliseconds
	 * @return
	 */
	public static String getDateTime(long milliseconds) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE);
		Date date = new Date(milliseconds);
		return formater.format(date);
	}

	/**
	 * 获取yyyy-MM-dd HH:mm格式的日期时间
	 * 
	 * @param milliseconds
	 * @return
	 */
	public static String getDateTime1(long milliseconds) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
		Date date = new Date(milliseconds);
		return formater.format(date);
	}
	
	public static String getNowDateTime(long milliseconds) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
		Date date = new Date(milliseconds);
		return formater.format(date);
	}
	
	/**
	 * 获取yyyy-MM-dd格式的日期
	 * 
	 * @param milliseconds
	 * @return
	 */

	public static String getSimpleDate(long milliseconds) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
		Date date = new Date(milliseconds);
		return formater.format(date);
	}

	public static String getSimpleTime(String time) {
		SimpleDateFormat formater = new SimpleDateFormat("HH:mm", Locale.CHINESE);
		Date date = null;
		try {
			date = f_format.parse(time);
			return formater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getSimpleTime(long milliseconds) {
		SimpleDateFormat formater = new SimpleDateFormat("HH:mm", Locale.CHINESE);
		Date date = new Date(milliseconds);
		return formater.format(date);
	}

	public static long getSecondsFromDateString(String dateString) {
		Date date = null;
		try {
			date = f_format.parse(dateString);
			return date.getTime() / 1000L;
		} catch (ParseException e) {
			e.printStackTrace();
			return -1L;
		}
	}

	public static String getChatSimpleDate(String time) {
		SimpleDateFormat formater = new SimpleDateFormat("yy-MM-dd");
		Date date = null;
		try {
			date = f_format.parse(time);
			return formater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getTimeHM(String time) {
		SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
		Date date = null;
		try {
			date = f_format.parse(time);
			return formater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static SimpleDateFormat friendly_format1 = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat friendly_format2 = new SimpleDateFormat("MM-dd HH:mm");

	/**
	 * 获取友好的时间显示
	 * 
	 * @param time
	 *            秒级别的时间戳
	 * @return
	 */
	public static String getFriendlyTimeDesc(Context context, long time) {
		String desc = "";
		Date timeDate = new Date(time * 1000L);
		Date nowDate = new Date();
		long delaySeconds = nowDate.getTime() / 1000 - time;// 相差的秒数

		if (delaySeconds < 10) {// 小于10秒，显示刚刚
			desc = context.getString(R.string.friendly_time_just_now);// 显示刚刚
		} else if (delaySeconds <= 60) {// 小于1分钟，显示如“25秒前”
			desc = delaySeconds + context.getString(R.string.friendly_time_before_seconds);
		} else if (delaySeconds < 60 * 30) {// 小于30分钟，显示如“25分钟前”
			desc = (delaySeconds / 60) + context.getString(R.string.friendly_time_before_minute);
		} else if (delaySeconds < 60 * 60 * 24) {// 小于1天之内
			if (nowDate.getDay() - timeDate.getDay() == 0) {// 同一天
				desc = friendly_format1.format(timeDate);
			} else {// 前一天
				desc = context.getString(R.string.friendly_time_yesterday) + " " + friendly_format1.format(timeDate);
			}
		} else if (delaySeconds < 60 * 60 * 24 * 2) {// 小于2天之内
			if (nowDate.getDay() - timeDate.getDay() == 1 || nowDate.getDay() - timeDate.getDay() == -6) {// 昨天
				desc = context.getString(R.string.friendly_time_yesterday) + " " + friendly_format1.format(timeDate);
			} else {// 前天
				desc = context.getString(R.string.friendly_time_before_yesterday) + " "
						+ friendly_format1.format(timeDate);
			}
		} else if (delaySeconds < 60 * 60 * 24 * 3) {// 小于三天
			if (nowDate.getDay() - timeDate.getDay() == 2 || nowDate.getDay() - timeDate.getDay() == -5) {// 前天
				desc = context.getString(R.string.friendly_time_before_yesterday) + " "
						+ friendly_format1.format(timeDate);
			}
			// else 超过前天
		}

		if (TextUtils.isEmpty(desc)) {
			desc = friendly_format2.format(timeDate);
		}
		return desc;
	}

	public static String sk_time_friendly_format2(long time) {
		return friendly_format2.format(new Date(time * 1000));
	}

	public static String sk_time_s_long_2_str(long time) {
		return s_long_2_str(time * 1000);
	}

	public static String sk_time_ss_long_2_str(long time) {
		return ss_long_2_str(time * 1000);
	}

	public static long sk_time_s_str_2_long(String dateString) {
		return s_str_2_long(dateString) / 1000;
	}

	public static int sk_time_current_time() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	private static SimpleDateFormat hm_formater = new SimpleDateFormat("HH:mm");

	public static String sk_time_long_to_hm_str(long time) {
		try {
			return hm_formater.format(new Date(time * 1000));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	
	/**
	 * 根据 timestamp 生成各类时间状态串
	 * @return 时间状态串(如：刚刚5分钟前)
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTimeState(long time){
		
		if(time <= 0){
			return "";
		}
		
		time = time/1000;
		String timestamp = String.valueOf(time);
		if (TextUtils.isEmpty(timestamp)){
			return "";
		}

		try{
			timestamp = formatTimestamp(timestamp);
			long _timestamp = Long.parseLong(timestamp);
			
			Calendar now = Calendar.getInstance();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(_timestamp);
			
			if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) 
					&& c.get(Calendar.MONTH) == now.get(Calendar.MONTH) 
					&& c.get(Calendar.DATE) == now.get(Calendar.DATE)){
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				return sdf.format(c.getTime());
			}
			
			if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
					&& c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
					&& c.get(Calendar.DATE) == now.get(Calendar.DATE) - 1){
				SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
				return "昨天";
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				return sdf.format(c.getTime());
			}
			
			/*if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
					&& c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
					&& c.get(Calendar.DATE) == now.get(Calendar.DATE) - 2){
				SimpleDateFormat sdf = new SimpleDateFormat("前天 HH:mm");
				return sdf.format(c.getTime());
			} 
			
			else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)){
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
				return sdf.format(c.getTime());
			} 
			
			else{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				return sdf.format(c.getTime());
			}
			*/
		} catch (Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 根据 timestamp 生成各类时间状态串
	 * 
	 * @return 时间状态串(如：刚刚5分钟前)
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTimeStateForCircle(long time){
		
		if(time <= 0){
			return "";
		}
		
		time = time/1000;
		String timestamp = String.valueOf(time);
		if (TextUtils.isEmpty(timestamp)){
			return "";
		}

		try{
			timestamp = formatTimestamp(timestamp);
			long _timestamp = Long.parseLong(timestamp);
			
			Calendar now = Calendar.getInstance();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(_timestamp);
			
			if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) 
					&& c.get(Calendar.MONTH) == now.get(Calendar.MONTH) 
					&& c.get(Calendar.DATE) == now.get(Calendar.DATE)){
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				return sdf.format(c.getTime());
			}
			
			if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
					&& c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
					&& c.get(Calendar.DATE) == now.get(Calendar.DATE) - 1){
				SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
				return "昨天";
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				return sdf.format(c.getTime()).substring(2,sdf.format(c.getTime()).length());
			}
			
			/*if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
					&& c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
					&& c.get(Calendar.DATE) == now.get(Calendar.DATE) - 2){
				SimpleDateFormat sdf = new SimpleDateFormat("前天 HH:mm");
				return sdf.format(c.getTime());
			} 
			
			else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)){
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
				return sdf.format(c.getTime());
			} 
			
			else{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				return sdf.format(c.getTime());
			}
			*/
		} catch (Exception e){
			e.printStackTrace();
			return "";
		}
	}

	public static String sk_time_long_to_chat_time_str(long time) {
		String date1 = sk_time_s_long_2_str(time);
		String date2 = sk_time_s_long_2_str(System.currentTimeMillis() / 1000);
		if (date1.compareToIgnoreCase(date2) == 0) {// 是同一天
			return sk_time_long_to_hm_str(time);
		} else {
			return long_to_yMdHm_str(time * 1000);
		}
	}

	public static String sk_time_long_to_true_time_str(long time) {
		String date1 = sk_time_s_long_2_str(time);
		String[] times = date1.split("-");
		if (times.length == 3) {
			String trueTime = times[1] + "月" + times[2] + "日";
			return trueTime;
		}
		String date2 = sk_time_s_long_2_str(System.currentTimeMillis() / 1000);
		if (date1.compareToIgnoreCase(date2) == 0) {// 是同一天
			return sk_time_long_to_hm_str(time);
		} else {
			return long_to_yMdHm_str(time * 1000);
		}
	}

	public static String time_long_to_true_time_str(long time) {
		String date1 = sk_time_s_long_2_str(time);
		String[] times = date1.split("-");
		if(times.length==3)
		{
			String trueTime = times[0] + "年" + times[1] + "月" + times[2] + "日";
			return trueTime;
		}
		String date2 = sk_time_s_long_2_str(System.currentTimeMillis() / 1000);
		if (date1.compareToIgnoreCase(date2) == 0) {// 是同一天
			return sk_time_long_to_hm_str(time);
		} else {
			return long_to_yMdHm_str(time * 1000);
		}
	}
	
	public static final SimpleDateFormat sk_format_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	// 日期加小时的字符串
	public static String long_to_yMdHm_str(long time) {
		return sk_format_1.format(new Date(time));
	}

	public static long sk_time_yMdHm_str_to_long(String time) {
		try {
			return sk_format_1.parse(time).getTime() / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int yMdHm_getYear(String dateString) {
		try {
			Date d = sk_format_1.parse(dateString);
			return d.getYear() + 1900;// 年份是基于格林威治时间，所以加上1900
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int yMdHm_getMonth(String dateString) {
		try {
			Date d = sk_format_1.parse(dateString);
			return d.getMonth();// 月份从0-11
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int yMdHm_getDayOfMonth(String dateString) {
		try {
			Date d = sk_format_1.parse(dateString);
			return d.getDate();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int yMdHm_getHours(String timeString) {
		try {
			Date date = sk_format_1.parse(timeString);
			return date.getHours();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int yMdHm_getMinutes(String timeString) {
		try {
			Date date = sk_format_1.parse(timeString);
			return date.getMinutes();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * @param textView
	 * @param time
	 *            时间戳/1000
	 * @return
	 */
	public static long getSpecialBeginTime(TextView textView, long time) {
		long currentTime = System.currentTimeMillis() / 1000;
		if (time > currentTime) {
			time = currentTime;
		}
		textView.setText(sk_time_s_long_2_str(time));
		return time;
	}

	/**
	 * 
	 * @param textView
	 * @param time
	 *            时间戳/1000
	 * @return
	 */
	public static long getSpecialEndTime(TextView textView, long time) {
		long currentTime = System.currentTimeMillis() / 1000;
		if (time == 0 || time > currentTime - 24 * 60 * 60) {
			textView.setText(R.string.to_this_day);
			return 0;
		}
		textView.setText(sk_time_s_long_2_str(time));
		return time;
	}

	public static int sk_time_age(long birthday) {
		int age = (new Date().getYear()) - (new Date(birthday * 1000).getYear());
		if (age < 0 || age > 100) {
			return 25;
		}
		return age;
	}

	/**
	 * 对时间戳格式进行格式化，保证时间戳长度为13位
	 *
	 * @param timestamp
	 *            时间戳
	 * @return 返回为13位的时间戳
	 */
	public static String formatTimestamp(String timestamp) {
		if (timestamp == null || "".equals(timestamp)) {
			return "";
		}

		String tempTimeStamp = timestamp + "00000000000000";
		StringBuffer stringBuffer = new StringBuffer(tempTimeStamp);
		return tempTimeStamp = stringBuffer.substring(0, 13);
	}

	/**
	 * 获得下一次铃响时间
	 *
	 * @param hour
	 * @param minute
	 * @param repeatPeriod
	 * @return
	 */
	public static long getNextAlarmTimeInMillis(int hour, int minute, int repeatPeriod) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// 如果闹钟的时间今天已过，则加上离它下一次铃响的天数
		if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
			calendar.add(Calendar.DAY_OF_YEAR, repeatPeriod);
		}

		return calendar.getTimeInMillis();
	}

	public static long getAlarmTimeInMillis(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTimeInMillis();
	}
	public static String getTimeStampNoClock(long time){
		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(new Date(time));
	}
	
	public static String m_2_h(long minute) {
		String time = "";
		if (minute / 60 > 0) {
			if (minute % 60 == 0) {
				time = (minute / 60) + "小时";
			} else {
				time = (minute / 60) + "小时" + minute % 60 + "分钟";
			}
			
		} else {
			time = minute +  "分钟";
		}
		return time;
	}
	public static int getCurrentDayOfWeek(String curMonthStr,int i) {
		String month = curMonthStr.replace("日", "");
		month = month.replace("月", "-");
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		if( month.equals("01-01") && i>0){
			year = c.get(Calendar.YEAR)+1;
		}
		String dateString = year + "-" + month;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(dateString);
			c.setTime(date);
			int weekday = c.get(Calendar.DAY_OF_WEEK);
			return weekday;
		} catch (Exception e) {
		}
		return 0;
	}


	
	public static String[] addWeekStr(String[] str){
		for(int i=0;i<str.length;i++){
			int index = getCurrentDayOfWeek(str[i],i)-1;
			switch (index) {
			case 0:
				str[i] = str[i]+" 星期日";
				break;
			case 1:
				str[i] = str[i]+" 星期一";
				break;
			case 2:
				str[i] = str[i]+" 星期二";
				break;
			case 3:
				str[i] = str[i]+" 星期三";
				break;
			case 4:
				str[i] = str[i]+" 星期四";
				break;
			case 5:
				str[i] = str[i]+" 星期五";
				break;
			case 6:
				str[i] = str[i]+" 星期六";
				break;
			default:
				break;
			}
		}
		
		return str;
	}
	
	public static int getCurrentDayOfWeek(String curMonthStr) {
		String month = curMonthStr.replace("日", "");
		month = month.replace("月", "-");
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		String dateString = year + "-" + month;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(dateString);
			c.setTime(date);
			int weekday = c.get(Calendar.DAY_OF_WEEK);
			return weekday;
		} catch (Exception e) {
		}
		return 0;
	}
	
	public static int getDayInYearCount() {

		int sum = 0;
		int leap = 0;
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int months = c.get(Calendar.MONTH) + 1;
		switch (months) /* 先计算某月以前月份的总天数 */
		{
		case 1:
			sum = 0;
			break;
		case 2:
			sum = 31;
			break;
		case 3:
			sum = 59;
			break;
		case 4:
			sum = 90;
			break;
		case 5:
			sum = 120;
			break;
		case 6:
			sum = 151;
			break;
		case 7:
			sum = 181;
			break;
		case 8:
			sum = 212;
			break;
		case 9:
			sum = 243;
			break;
		case 10:
			sum = 273;
			break;
		case 11:
			sum = 304;
			break;
		case 12:
			sum = 334;
			break;
		default:
			break;
		}
		sum = sum + day; /* 再加上某天的天数 */
		if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0))/* 判断是不是闰年 */
			leap = 1;
		else
			leap = 0;
		if (leap == 1 && months > 2)/* 如果是闰年且月份大于2,总天数应该加一天 */
			sum++;
		System.out.println("It is the the day:" + sum);

		return sum;
	}
	
	public static int timeSetForDay() {
		int defaultTime = 0;
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		if(minute<=30){
			defaultTime = (hour+1)*2;
		}else{
			defaultTime = (hour+1)*2+1;
		}
		return defaultTime;
	}

	public static int dayForWeek() {
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		int weekday = c.get(Calendar.DAY_OF_WEEK);
		return weekday;
	}


	public static long getTimeStamp(String timeStr){
		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm");
	     Date date;
	     long timeStemp =0;
		try {
			date = simpleDateFormat .parse(timeStr);
			timeStemp = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timeStemp;
	}

	/** 
     * 将时间改成半小时的时间段
     * 00:00 改成  00:00 - 00:00
     */ 
    public static String changeTimeToPeriod(String timeText) {
		// 设置时间
		if(!TextUtils.isEmpty(timeText))
		{
			String left = timeText.split(":")[0];
			String right = timeText.split(":")[1];
			int leftInt = Integer.valueOf(left);
			int rightInt = Integer.valueOf(right);
			rightInt = rightInt + 30;
			if(rightInt==60){
				left = String.valueOf(leftInt+1);
				right = "0";
				if(left.length()<2){
					left = "0" + left;
				}
				if(right.length()<2){
					right = "0" + right;
				}
				timeText = timeText + "-" + left + ":" + right; 			
			}
			else if(rightInt<60){
				left = String.valueOf(leftInt);
				right = String.valueOf(rightInt);
				if(left.length()<2){
					left = "0" + left;
				}
				if(right.length()<2){
					right = "0" + right;
				}
				timeText = timeText + "-" + left + ":" + right; 
			}
			else if(rightInt>60){
				left = String.valueOf(leftInt+1);
				right = String.valueOf(rightInt-60);
				if(left.length()<2){
					left = "0" + left;
				}
				if(right.length()<2){
					right = "0" + right;
				}
				timeText = timeText + "-" + left + ":" + right; 
			}
			return  timeText;
		}
		return "";
	}
    
    public static String getTimeStr(String timeStr){
		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    long timeStemp =Long.parseLong(timeStr);
	    String date ="";
		date = simpleDateFormat.format(new Date(timeStemp));
		return date;
	}

	/**
	 * 比较时间
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDate(String date1, String date2){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d1 = df.parse(date1);
			Date d2 = df.parse(date2);
			if (d1.getTime() > d2.getTime()){
				return 1;
			}else if (d1.getTime() < d2.getTime()){
				return -1;
			}else{
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 得到今天的时间
	 * @return
	 */
	public static String getToday(){
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 0);

		date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);

		return dateString;
	}

	public static String getMsgTimeStr(long timeStamp){
        Calendar now=Calendar.getInstance();
        Calendar target=Calendar.getInstance();
        target.setTimeInMillis(timeStamp);
        if(target.get(Calendar.YEAR)!=now.get(Calendar.YEAR)){
            return new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.US).format(target.getTime());
        }else if(now.get(Calendar.DAY_OF_YEAR)-target.get(Calendar.DAY_OF_YEAR)==0){
            return new SimpleDateFormat("HH:mm",Locale.US).format(target.getTime());
        }else if(now.get(Calendar.DAY_OF_YEAR)-target.get(Calendar.DAY_OF_YEAR)==1){
            return new SimpleDateFormat("昨天 HH:mm",Locale.US).format(target.getTime());
        }else{
            return new SimpleDateFormat("MM-dd HH:mm",Locale.US).format(target.getTime());
        }
        //            int dayInWeek=target.get(Calendar.DAY_OF_WEEK);
//            return "周"+getWeekStr(dayInWeek)+new SimpleDateFormat(" HH:mm",Locale.US).format(target.getTime());
	}

    public static String getWeekStr(int dayInWeek){
        switch (dayInWeek) {
            case Calendar.SUNDAY:
                return "日";
            case Calendar.MONDAY:
                return "一";
            case Calendar.TUESDAY:
                return "二";
            case Calendar.WEDNESDAY:
                return "三";
            case Calendar.THURSDAY:
                return "四";
            case Calendar.FRIDAY:
                return "五";
            case Calendar.SATURDAY:
                return "六";
        }
        return "";
    }

	public static String getChatGroupTimeStr(long timeStamp){
		Calendar now=Calendar.getInstance();
		Calendar target=Calendar.getInstance();
		target.setTimeInMillis(timeStamp);
        if(target.get(Calendar.YEAR)!=now.get(Calendar.YEAR)){
            return new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.US).format(target.getTime());
        }else if(now.get(Calendar.DAY_OF_YEAR)-target.get(Calendar.DAY_OF_YEAR)==0){
            return new SimpleDateFormat("HH:mm",Locale.US).format(target.getTime());
        }else if(now.get(Calendar.DAY_OF_YEAR)-target.get(Calendar.DAY_OF_YEAR)==1){
            return new SimpleDateFormat("昨天",Locale.US).format(target.getTime());
        }else{
            return new SimpleDateFormat("MM-dd",Locale.US).format(target.getTime());
        }
	}
}
