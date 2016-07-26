package com.cms.mylive.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static String longToDate(Long time, String patterm) {
		if (patterm == null)
			throw new RuntimeException("模式不能为空");

		SimpleDateFormat format = new SimpleDateFormat(patterm);
		Date date = new Date(time);
		String dateStr = format.format(date);
		return dateStr;
	}
}
