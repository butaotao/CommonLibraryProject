package com.cms.mylive.chat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import android.annotation.SuppressLint;

import com.gensee.utils.StringUtil;

@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
public class DateUtil {

	public static Date getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		String dateString = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(8);
		Date currentTime_2 = formatter.parse(dateString, pos);
		return currentTime_2;
	}

	public static Date getNowDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
				Locale.ENGLISH);
		String dateString = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(8);
		Date currentTime_2 = formatter.parse(dateString, pos);
		return currentTime_2;
	}

	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getStringDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getStringDateShort(long questTime) {
		Date date = new Date(questTime * 1000);
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String getTimeShort() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss",
				Locale.ENGLISH);
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getTimeShort(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm",
				Locale.ENGLISH);
		Date date = new Date(time * 1000);
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String getTimeHHMMSS(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss",
				Locale.CHINESE);
		formatter.setTimeZone(TimeZone.getDefault());
		Date date = new Date(time);
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String getTimeShort(String time) {
		return StringUtil.isEmpty(time) ? getTimeShort() : getTimeShort(Long
				.valueOf(time));
	}

	public static String getTimeMMDDHHMM() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm",
				Locale.ENGLISH);
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getTimeMMDDHHMM(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm",
				Locale.ENGLISH);
		Date date = new Date(time * 1000);
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String getTimeMMDDHHMM(String time) {
		return StringUtil.isEmpty(time) ? getTimeMMDDHHMM()
				: getTimeMMDDHHMM(Long.valueOf(time));
	}

	public static Date strToDateLong(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	public static String dateToStrLong(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	public static String dateToStr(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(dateDate);
		return dateString;
	}
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	public static Date getNow() {
		Date currentTime = new Date();
		return currentTime;
	}

	public static Date getLastDate(long day) {
		Date date = new Date();
		long date_3_hm = date.getTime() - 3600000 * 34 * day;
		Date date_3_hm_date = new Date(date_3_hm);
		return date_3_hm_date;
	}

	public static String getStringToday() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getHour() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		String hour;
		hour = dateString.substring(11, 13);
		return hour;
	}

	public static String getTime() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		String min;
		min = dateString.substring(14, 16);
		return min;
	}

	public static String getUserDate(String sformat) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(sformat);
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	public static String getTwoHour(String st1, String st2) {
		String[] kk = null;
		String[] jj = null;
		kk = st1.split(":");
		jj = st2.split(":");
		if (Integer.parseInt(kk[0]) < Integer.parseInt(jj[0]))
			return "0";
		else {
			double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1])
					/ 60;
			double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1])
					/ 60;
			if ((y - u) > 0)
				return y - u + "";
			else
				return "0";
		}
	}

	public static String getTwoDay(String sj1, String sj2) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		long day = 0;
		try {
			java.util.Date date = myFormatter.parse(sj1);
			java.util.Date mydate = myFormatter.parse(sj2);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return "";
		}
		return day + "";
	}

	public static String getPreTime(String sj1, String jj) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String mydate1 = "";
		try {
			Date date1 = format.parse(sj1);
			long Time = (date1.getTime() / 1000) + Integer.parseInt(jj) * 60;
			date1.setTime(Time * 1000);
			mydate1 = format.format(date1);
		} catch (Exception e) {
		}
		return mydate1;
	}

	public static String getNextDay(String nowdate, String delay) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String mdate = "";
			Date d = strToDate(nowdate);
			long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24
					* 60 * 60;
			d.setTime(myTime * 1000);
			mdate = format.format(d);
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}

	public static boolean isLeapYear(String ddate) {

		Date d = strToDate(ddate);
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(d);
		int year = gc.get(Calendar.YEAR);
		if ((year % 400) == 0)
			return true;
		else if ((year % 4) == 0) {
			if ((year % 100) == 0)
				return false;
			else
				return true;
		} else
			return false;
	}


	public static String getNowMonth(String sdate) {
		sdate = sdate.substring(0, 8) + "01";

		Date date = strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int u = c.get(Calendar.DAY_OF_WEEK);
		String newday = getNextDay(sdate, (1 - u) + "");
		return newday;
	}


	public static String getNo(int k) {

		return getUserDate("yyyyMMddhhmmss") + getRandom(k);
	}

	public static String getRandom(int i) {
		Random jjj = new Random();
		// int suiJiShu = jjj.nextInt(9);
		if (i == 0)
			return "";
		String jj = "";
		for (int k = 0; k < i; k++) {
			jj = jj + jjj.nextInt(9);
		}
		return jj;
	}

	/**
	 * 
	 * @param args
	 */
	public static boolean RightDate(String date) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		;
		if (date == null)
			return false;
		if (date.length() > 10) {
			sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		} else {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
		try {
			sdf.parse(date);
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	public static String getTimeStr(SimpleDateFormat formatter, long timeLong) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeLong);
		String timeStr = formatter.format(calendar.getTime());
		return timeStr;
	}
	
}
