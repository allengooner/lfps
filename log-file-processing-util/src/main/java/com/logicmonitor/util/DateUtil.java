package com.logicmonitor.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by allen.gl on 2015/5/13.
 */
public abstract class DateUtil {
    private final static String SHORT_FORMAT = "yyyy-MM-dd";

    private final static String FULL_FORMAT  = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 把Date对象按yyyyMMdd格式化
     *
     * @param date
     * @return yyyyMMdd格式的日期
     */
    public static String getShortDateString(Date date) {
        DateFormat df = new SimpleDateFormat(SHORT_FORMAT); // not thread-safe
        df.setLenient(false);
        return df.format(date);
    }

    /**
     * 把Date对象按"yyyy-MM-dd hh:mm:ss"格式化
     *
     * @param date
     * @return
     */
    public static String getFullDateString(Date date) {
        if (null == date) {
            return null;
        }

        DateFormat df = new SimpleDateFormat(FULL_FORMAT); // not thread-safe
        df.setLenient(false);
        return df.format(date);
    }

}
