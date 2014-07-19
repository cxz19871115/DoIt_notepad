package com.cxz.studio.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	public static String parseLong2DateTime(long time, String format) {
		DateFormat df=new SimpleDateFormat(format);
        Date date=new Date(time);
		return df.format(date);
	}
}
