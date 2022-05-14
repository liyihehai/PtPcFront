package com.nnte.pf_uti_client.utils;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {

    public static final String DF_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String DF_YMDHMS_ = "yyyy/MM/dd HH:mm:ss";
    public static final String DF_YMDHM = "yyyy-MM-dd HH:mm";
    public static final String DF_YMDHM_ = "yyyy/MM/dd HH:mm";
    public static final String DF_MDHM = "MM-dd HH:mm";
    public static final String DF_MDHM_ = "MM/dd HH:mm";
    public static final String DF_YMD = "yyyy-MM-dd";
    public static final String DF_YM = "yyyy-MM";
    public static final String DF_YMD_POINT = "yyyy.MM.dd";
    public static final String DF_HMS = "HH:mm:ss";
    public static final String DF_MD_STRING = "MM月dd号";
    public static final String DF_YMD_STRING = "yyyy年MM月dd日";
    public static final String DF_YMDHMS_STRING = "yyyyMMddHHmmss";
    public static final String DF_YMDHMSS_STRING = "yyyyMMddHHmmssSSS"; //李毅 2018/8/20
    public static final String DF_YMD_STRING_ = "yyyyMMdd";
    public static final String DF_YMDHMS_STRING_ = "yyyyMMdd HHmmss";
    public static final String DF_YMD_SLASH = "yyyy/MM/dd";
    public static final String DF_YMD_HMSSSS = "yyyy-MM-dd HH:mm:ss.SSS";//李毅 2019/9/23
    public static final String DF_YMDHMS_TZ = "yyyy-MM-dd'T'HH:mm:ss.SSS Z";//李毅 2020/12/29

    public static Map<String, String> DateFmtRegMap = new HashMap<>();

    static {
        DateFmtRegMap.put(DF_YMDHMS, "^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$");
        DateFmtRegMap.put(DF_YMDHMS_, "^[0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$");
        DateFmtRegMap.put(DF_YMDHM, "^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}$");
        DateFmtRegMap.put(DF_YMDHM_, "^[0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}:[0-9]{2}$");
        DateFmtRegMap.put(DF_MDHM, "^[0-9]{4}-[0-9]{2} [0-9]{2}:[0-9]{2}$");
        DateFmtRegMap.put(DF_MDHM_, "^[0-9]{4}/[0-9]{2} [0-9]{2}:[0-9]{2}$");
        DateFmtRegMap.put(DF_YMD, "^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
        DateFmtRegMap.put(DF_YM, "^[0-9]{4}-[0-9]{2}{2}$");
        DateFmtRegMap.put(DF_YMD_POINT, "^[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}$");
        DateFmtRegMap.put(DF_HMS, "^[0-9]{2}:[0-9]{2}:[0-9]{2}$");
        DateFmtRegMap.put(DF_MD_STRING, "^[0-9]{2}月[0-9]{2}号$");
        DateFmtRegMap.put(DF_YMD_STRING, "^[0-9]{4}年[0-9]{2}月[0-9]{2}日$");
        DateFmtRegMap.put(DF_YMDHMS_STRING, "^[0-9]{14}$");
        DateFmtRegMap.put(DF_YMDHMSS_STRING, "^[0-9]{17}$");
        DateFmtRegMap.put(DF_YMD_STRING_, "^[0-9]{8}$");
        DateFmtRegMap.put(DF_YMDHMS_STRING_, "^[0-9]{8} [0-9]{6}$");
        DateFmtRegMap.put(DF_YMD_SLASH, "^[0-9]{4}/[0-9]{2}/[0-9]{2}$");
        DateFmtRegMap.put(DF_YMD_HMSSSS, "^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{3}$");
        DateFmtRegMap.put(DF_YMDHMS_TZ, "^[0-9]{4}-[0-9]{2}-[0-9]{2}.T.[0-9]{2}:[0-9]{2}:[0-9]{2}.*Z$");
    }

    //日期格式转化为字符串格式(通用格式)
    public static String dateToString(Date date, String frormat) {
        if (date == null || frormat == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(frormat);
        return simpleDateFormat.format(date);
    }

    //日期格式转化为字符串格式(默认格式)
    public static String dateToString(Date date) {
        return dateToString(date, DF_YMD);
    }

    public static String dateToStringMD(Date date) {
        return dateToString(date, DF_MD_STRING);
    }

    //字符串格式转化为日期格式(通用格式)
    public static Date stringToDate(String string, String frormat) {
        Date dd = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(frormat);
        try {
            dd = simpleDateFormat.parse(string.trim());
        } catch (Exception e) {
            dd = null;
        }
        return dd;
    }

    //字符串格式转化为日期格式(通用格式)
    public static String stringToStringDate(String string, String frormat) {
        Date dd = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(frormat);
        try {
            dd = simpleDateFormat.parse(string.trim());
        } catch (Exception e) {
            dd = null;
        }
        return simpleDateFormat.format(dd).toString();
    }

    //字符串格式转化为日期格式(通用格式)
    public static String stringToStringDate(String string, String frormat, String switchFormat) {
        Date dd = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(frormat);
        try {
            dd = simpleDateFormat.parse(string.trim());
        } catch (Exception e) {
            dd = null;
        }
        simpleDateFormat = new SimpleDateFormat(switchFormat);
        return simpleDateFormat.format(dd).toString();
    }

    //字符串格式转化为日期格式(通用格式)
    public static Date dateToDate(Date date, String frormat) {
        Date dd = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(frormat);
        try {
            dd = simpleDateFormat.parse(simpleDateFormat.format(date));
        } catch (Exception e) {
            dd = null;
        }
        return dd;
    }

    // (计算两个日期相隔的分钟数)
    public static int nMinsBetweenTwoDate(String firstString,
                                          String secondString) {
        SimpleDateFormat df = new SimpleDateFormat(DF_YMDHMS);
        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = df.parse(firstString);
            secondDate = df.parse(secondString);
        } catch (Exception e) {
            // (日期型字符串格式错误)
        }
        int nMin = (int) ((secondDate.getTime() - firstDate.getTime()) / (60 * 1000));
        return nMin;
    }


    public static String getTime(Date trialTime) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(trialTime);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String sHour = Integer.toString(hour);
        String sMinute = Integer.toString(minute);
        if (hour < 10)
            sHour = "0" + sHour;
        if (minute < 10)
            sMinute = "0" + sMinute;

        String sTime = sHour + minute;
        return sTime;
    }


    public static String getDateHour() {
        Calendar calendar = new GregorianCalendar();
        Date trialTime = new Date();
        calendar.setTime(trialTime);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String sMonth = Integer.toString(month);
        String sDate = Integer.toString(date);
        String sHour = Integer.toString(hour);
        String sMinute = Integer.toString(minute);
        String sSecond = Integer.toString(second);
        if (month < 10)
            sMonth = "0" + sMonth;
        if (date < 10)
            sDate = "0" + sDate;
        if (hour < 10)
            sHour = "0" + sHour;
        if (minute < 10)
            sMinute = "0" + sMinute;
        if (second < 10)
            sSecond = "0" + sSecond;

        String sCurrentDateTime = calendar.get(Calendar.YEAR) + "-" + sMonth
                + "-" + sDate + " " + sHour;
        return sCurrentDateTime;
    }

    public static int getYear() {
        Calendar calendar = new GregorianCalendar();
        Date trialTime = new Date();
        calendar.setTime(trialTime);
        return calendar.get(Calendar.YEAR);
    }

    //得到日期中的年份
    public static int getYear(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    //将日期转化为long 形式的，如：20100726120530
    public static Long changeDateToLong(Date date) {
        String dateString = dateToString_full(date);
        dateString = dateString.replaceAll("[ |\\-|:]", "");
        return new Long(dateString);
    }

    //将String 形式的，如：20100726120530 转化为Date形
    public static Date changeStringToDate(String date) {
        String dateString = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8)
                + " " + date.substring(8, 10) + ":" + date.substring(10, 12) + ":" + date.substring(12, 14);
        return stringToDate(dateString, DF_YMDHMS);
    }

    //将日期格式如："8-16" 转化为2010-08-16
    public static String replenishDate(String monthDay) {
        Date date = stringToDate(getYear() + "-" + monthDay, DF_YMD);
        return dateToString(date);
    }

    public static int getMonth() {
        Calendar calendar = new GregorianCalendar();
        Date trialTime = new Date();
        calendar.setTime(trialTime);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getMonth(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getDay() {
        Calendar calendar = new GregorianCalendar();
        Date trialTime = new Date();
        calendar.setTime(trialTime);
        return calendar.get(Calendar.DATE);
    }

    public static int getDay(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    public static int getHour() {
        Calendar calendar = new GregorianCalendar();
        Date trialtime = new Date();
        calendar.setTime(trialtime);
        return calendar.get(Calendar.HOUR);
    }

    public static int getHour24() {
        Calendar calendar = new GregorianCalendar();
        Date trialtime = new Date();
        calendar.setTime(trialtime);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getHour24(Date trialtime) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(trialtime);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        Calendar calendar = new GregorianCalendar();
        Date trialtime = new Date();
        calendar.setTime(trialtime);
        return calendar.get(Calendar.MINUTE);
    }

    public static String getTime() {
        Calendar calendar = new GregorianCalendar();
        Date trialtime = new Date();
        calendar.setTime(trialtime);
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }

    public static String getCurrentDate_String() {
        Calendar calendar = new GregorianCalendar();
        Date trialTime = new Date();

        calendar.setTime(trialTime);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        String sMonth = Integer.toString(month);
        String sDate = Integer.toString(date);

        if (month < 10)
            sMonth = "0" + sMonth;
        if (date < 10)
            sDate = "0" + sDate;
        String sCurrentDateTime = calendar.get(Calendar.YEAR) + "-" + sMonth
                + "-" + sDate;
        return sCurrentDateTime;
    }

    public static String getCurrentDateTime() {
        Calendar calendar = new GregorianCalendar();

        Date trialTime = new Date();

        calendar.setTime(trialTime);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String sMonth = Integer.toString(month);
        String sDate = Integer.toString(date);
        String sHour = Integer.toString(hour);
        String sMinute = Integer.toString(minute);
        String sSecond = Integer.toString(second);

        if (month < 10)
            sMonth = "0" + sMonth;
        if (date < 10)
            sDate = "0" + sDate;
        if (hour < 10)
            sHour = "0" + sHour;
        if (minute < 10)
            sMinute = "0" + sMinute;
        if (second < 10)
            sSecond = "0" + sSecond;

        String sCurrentDateTime = calendar.get(Calendar.YEAR) + "-" + sMonth
                + "-" + sDate + " " + sHour + ":" + sMinute + ":" + sSecond;
        return sCurrentDateTime;
    }

    public static String getYMD(String pDateTime) { // throws Exception
        try {
            java.text.SimpleDateFormat format;
            if (pDateTime.length() <= 10) {
                format = new java.text.SimpleDateFormat(DF_YMD);
            } else {
                format = new java.text.SimpleDateFormat(DF_YMDHMS);
            }
            java.util.Date unFormat = format.parse(pDateTime);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(unFormat);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int date = calendar.get(Calendar.DATE);
            String sYear = null, sMonth = null, sDate = null;
            if (year < 10)
                sYear = "0" + year;
            else
                sYear = Integer.toString(year);
            if (month < 10)
                sMonth = "0" + month;
            else
                sMonth = Integer.toString(month);
            if (date < 10)
                sDate = "0" + date;
            else
                sDate = Integer.toString(date);
            return sYear + "-" + sMonth + "-" + sDate;

        } catch (Exception e) {
            // throw new Exception(e.toString());
            return "";
        }
    }

    public static String getNowYMD() {
        try {
            return getYMD(getCurrentDateTime());
        } catch (Exception e) {
        }
        return "1900-01-01";
    }

    /**
     * 通过时间判断是上午、中午、下午或者晚上
     *
     * @return
     */
    public static String checkTimeBucket(String time) {
        if (time == null || time.equals("")) {
            return "";
        }
        int date = new Integer(time.split(":")[0]);
        String result = "";
        if (date < 11)
            result = "上午";
        else if (date >= 11 && date <= 14)
            result = "中午";
        else if (date > 14 && date < 18)
            result = "下午";
        else
            result = "晚上";

        return result;
    }

    //（格式化日期字符串 如：6月25日）
    public static String formatDateyearMonth(String date) {
        Date thisDate = stringToDate(date, DF_YMD);
        Calendar c = Calendar.getInstance();
        c.setTime(thisDate);
        return (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日";
    }

    // (格式化日期为字符串 "yyyy-MM-dd hh:mm")
    public static String formatDateTime(Date basicDate, String strFormat) {
        synchronized (basicDate) {
            SimpleDateFormat df = new SimpleDateFormat(strFormat);
            return df.format(basicDate);
        }
    }

    // (检验字符串 "yyyy-MM-dd")
    public static boolean checkDateTime(String basicDate) {
        if ((basicDate == null) || (basicDate.length() != 10)) {
            return false;
        }
        SimpleDateFormat df = new SimpleDateFormat(DF_YMD);
        Date tmpDate = null;
        try {
            tmpDate = df.parse(basicDate);
        } catch (Exception e) {
            return false;
            // (日期型字符串格式错误)
        }
        return true;
    }

    // (当前日期加减n天后的日期，返回String (yyyy-mm-dd))
    public static String nDaysAftertoday(int n) {
        SimpleDateFormat df = new SimpleDateFormat(DF_YMD);
        Calendar rightNow = Calendar.getInstance();
        // rightNow.add(Calendar.DAY_OF_MONTH,-1);
        rightNow.add(Calendar.DAY_OF_MONTH, +n);
        return df.format(rightNow.getTime());
    }

    // (当前日期加减n天后的日期，可指定返回日期字符类型,，返回日期字符串)
    public static String nDaysAfterTodaytype(int n, String dateType) {
        SimpleDateFormat df = new SimpleDateFormat(dateType);
        Calendar rightNow = Calendar.getInstance();
        // rightNow.add(Calendar.DAY_OF_MONTH,-1);
        rightNow.add(Calendar.DAY_OF_MONTH, +n);
        return df.format(rightNow.getTime());
    }

    // (当前日期加减n天后的日期，返回Date)
    public static Date nDaysAfterNowDate(int n) {
        Calendar rightNow = Calendar.getInstance();
        // rightNow.add(Calendar.DAY_OF_MONTH,-1);
        rightNow.add(Calendar.DAY_OF_MONTH, +n);
        return rightNow.getTime();
    }

    public static Date nDaysAfterDate(Date basicDate, int n) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(basicDate);
        rightNow.add(Calendar.DAY_OF_MONTH, +n);
        return rightNow.getTime();
    }

    // (给定一个日期型字符串，返回加减n天后的日期型字符串)
    public static String nDaysAfterOneDateString(String basicDate, int n) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat(DF_YMD);
        Date tmpDate = null;
        tmpDate = df.parse(basicDate);
        long nDay = (tmpDate.getTime() / (24 * 60 * 60 * 1000) + 1 + n)
                * (24 * 60 * 60 * 1000);
        tmpDate.setTime(nDay);

        return df.format(tmpDate);
    }

    // (给定一个日期型字符串，返回加减n天后的日期型字符串)
    public static String nDaysAfterBeforeNow(String basicDate, int n) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat(DF_YMD);
        Date tmpDate = null;
        Calendar c = Calendar.getInstance();
        tmpDate = df.parse(basicDate);
        c.setTime(tmpDate);
        c.add(Calendar.DAY_OF_YEAR, n);
        Date nowDate = c.getTime();
        if (new Date().after(nowDate)) {
            return df.format(new Date());
        } else {
            return df.format(nowDate);
        }
    }

    // ( 给定一个日期，返回加减n天后的日期，返回Date)
    public static Date nDaysAfterOneDate(Date basicDate, int n) {
        long nDay = (basicDate.getTime() / (24 * 60 * 60 * 1000) + 1 + n)
                * (24 * 60 * 60 * 1000);
        basicDate.setTime(nDay);

        return basicDate;
    }

    // (当前日期加减n个月后的日期，返回String (yyyy-mm-dd))
    public static String nMonthsAftertoday(int n) {
        SimpleDateFormat df = new SimpleDateFormat(DF_YMD);
        Calendar rightNow = Calendar.getInstance();
        // rightNow.add(Calendar.DAY_OF_MONTH,-1);
        rightNow.add(Calendar.MONTH, +n);
        return df.format(rightNow.getTime());
    }

    public static Date nMinutesAfterNowDate(int n) {
        Calendar rightNow = Calendar.getInstance();
        // rightNow.add(Calendar.DAY_OF_MONTH,-1);
        rightNow.add(Calendar.MINUTE, +n);
        return rightNow.getTime();
    }

    public static Date nMinutesAfterDate(Date now, int n) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(now);
        // rightNow.add(Calendar.DAY_OF_MONTH,-1);
        rightNow.add(Calendar.MINUTE, +n);
        return rightNow.getTime();
    }

    // (当前日期加减n个月后的日期，返回Date)
    public static Date nMonthsAfterNowDate(int n) {
        Calendar rightNow = Calendar.getInstance();
        // rightNow.add(Calendar.DAY_OF_MONTH,-1);
        rightNow.add(Calendar.MONTH, +n);
        return rightNow.getTime();
    }

    // (当前日期加减n个月后的日期，返回Date)
    public static Date nMonthsAfterOneDate(Date basicDate, int n) {
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(basicDate);
        rightNow.add(Calendar.MONTH, +n);
        return rightNow.getTime();
    }

    // (当前日期加减n个月后的日期，返回String)
    public static String nMonthsAfterOneDateString(Date basicDate, int n) {
        SimpleDateFormat df = new SimpleDateFormat(DF_YMD);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(basicDate);
        rightNow.add(Calendar.MONTH, +n);
        return df.format(rightNow.getTime());
    }

    //当前时间加n小时后返回Date
    public static Date nHoursAfterOneDate(Date nowDate, int n) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(nowDate);
        rightNow.add(Calendar.HOUR, +n);
        return rightNow.getTime();
    }

    // (计算两个日期相隔的天数)
    public static int nDaysBetweenTwoDate(Date firstDate, Date secondDate) {
        int nDay = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
        return nDay;
    }

    // (计算两个日期相隔的天数)
    public static int nDaysBetweenTwoDate(String firstString,
                                          String secondString) {
        SimpleDateFormat df = new SimpleDateFormat(DF_YMD);
        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = df.parse(firstString);
            secondDate = df.parse(secondString);
        } catch (Exception e) {
            // (日期型字符串格式错误)
        }
        int nDay = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
        return nDay;
    }


    public static int nMinsBetweenTwoTime(String firstString,
                                          Date secondDate) {

        SimpleDateFormat df = new SimpleDateFormat(DF_YMDHM);
        Date firstDate = null;
        try {
            firstDate = df.parse(firstString);
        } catch (Exception e) {
            // (日期型字符串格式错误)
        }
        int nMin = (int) ((firstDate.getTime() - secondDate.getTime()) / (60 * 1000));
        return nMin;
    }

    public static int nMinsBetweenTwoDate(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / (60 * 1000));
    }

    public static int nSecsBetweenTwoDate(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / (1000));
    }

    /**
     * 当前时间某个范围的随机时间(分钟）
     *
     * @param startDate
     * @param mins
     * @return
     */
    public static java.util.Date nMinsBeforeDateRandam(Date startDate, long mins) {
        long n = startDate.getTime();
        long m = mins * 60 * 1000;
        m = Math.round(m * (1 - Math.random() * Math.random()) * 0.2 + m * Math.random() * 0.6 + m * Math.random() * Math.random() * 0.2);
        n = n - m;
        return new java.util.Date(n);
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static java.util.Date getCurrentDate_Date() {
        return new java.util.Date(System.currentTimeMillis());
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Time getCurrentTime() {
        return new Time(System.currentTimeMillis());
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }


    /**
     * 返回yyyy-MM-dd hh24:mm:ss格式的字符串
     *
     * @param date
     * @return
     */
    public static String dateToString_full(Date date) {
        if (date == null)
            return "";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                DF_YMDHMS);
        return simpleDateFormat.format(date);
    }


    /**
     * 返回yyyy-MM-dd hh24:mm:ss格式的字符串
     *
     * @param date
     * @return
     */
    public static String dateToString_HM(Date date) {
        if (date == null)
            return "";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(date);
    }

    /**
     * 由yyyy-MM-dd格式的字符串返回日期
     */
    public static Date stringToDate(String string) {
        Date dd = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DF_YMD);
        try {
            dd = simpleDateFormat.parse(string.trim());
        } catch (Exception e) {
            dd = null;
        }
        return dd;
    }

    public static Date stringToDate_full(String string) {
        Date dd = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DF_YMDHMS);
        try {
            if (string.trim().length() == 10) {
                string = string + " 00:00:00";
            }
            dd = simpleDateFormat.parse(string.trim());
        } catch (Exception e) {
            dd = null;
        }
        return dd;
    }

    public static java.sql.Date DateToSQLDate(Date pDate) {
        java.sql.Date dd = new java.sql.Date(pDate.getTime());
//		SimpleDateFormat   sdf   =   new   SimpleDateFormat(DF_YMDHMS);   
//		  System.out.println(sdf.format(dd));  
        return dd;
    }


    /*
     *
     *
     */
    public static Date getDateAfter2Hour(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, -2);
        return cal.getTime();
    }

    public static Date getDateAfterHour(Date date, int nHour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, nHour);
        return cal.getTime();
    }

    public static Date getDateAfterSecond(Date date, int nSecond) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, nSecond);
        return cal.getTime();
    }

    /**
     * 计算两个日期相隔的天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDaysBetween(Date startDate, Date endDate) {
        Calendar calendarStartDate = Calendar.getInstance();
        Calendar calendarEndDate = Calendar.getInstance();

        // 设日历为相应日期
        calendarStartDate.setTime(startDate);
        calendarEndDate.setTime(endDate);
        if (startDate.after(endDate)) {
            Calendar swap = calendarStartDate;
            calendarStartDate = calendarEndDate;
            calendarEndDate = swap;
        }

        int days = calendarEndDate.get(Calendar.DAY_OF_YEAR)
                - calendarStartDate.get(Calendar.DAY_OF_YEAR);
        int y2 = calendarEndDate.get(Calendar.YEAR);
        while (calendarStartDate.get(Calendar.YEAR) < y2) {
            days += calendarStartDate.getActualMaximum(Calendar.DAY_OF_YEAR);
            calendarStartDate.add(Calendar.YEAR, 1);
        }

        return days;
    }

    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days)) + 1;
    }

    /**
     * 计算两个日期相隔年数(不比较月、日)
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getYearsBetween(Date startDate, Date endDate) {
        Calendar calendarStartDate = Calendar.getInstance();
        Calendar calendarEndDate = Calendar.getInstance();

        // 设日历为相应日期
        calendarStartDate.setTime(startDate);
        calendarEndDate.setTime(endDate);
        return calendarEndDate.get(Calendar.YEAR)
                - calendarStartDate.get(Calendar.YEAR);
    }

    /**
     * 计算两个日期相隔的月数(不足整月的算一个月)
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getMonthsBetween(Date startDate, Date endDate) {
        Calendar calendarStartDate = Calendar.getInstance();
        Calendar calendarEndDate = Calendar.getInstance();

        // 设日历为相应日期
        calendarStartDate.setTime(startDate);
        calendarEndDate.setTime(endDate);
        if (startDate.after(endDate)) {
            Calendar swap = calendarStartDate;
            calendarStartDate = calendarEndDate;
            calendarEndDate = swap;
        }

        int months = calendarEndDate.get(Calendar.MONTH)
                - calendarStartDate.get(Calendar.MONTH)
                + (calendarEndDate.get(Calendar.YEAR) - calendarStartDate
                .get(Calendar.YEAR)) * 12;

        if (getEndDateByMonths(startDate, months).compareTo(endDate) < 0)
            months += 1;

        return months;
    }

    /**
     * 计算两个日期相隔的月数(不比较日)
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getActualMonths(Date startDate, Date endDate) {
        Calendar calendarStartDate = Calendar.getInstance();
        Calendar calendarEndDate = Calendar.getInstance();

        // 设日历为相应日期
        calendarStartDate.setTime(startDate);
        calendarEndDate.setTime(endDate);
        if (startDate.after(endDate)) {
            Calendar swap = calendarStartDate;
            calendarStartDate = calendarEndDate;
            calendarEndDate = swap;
        }

        int months = calendarEndDate.get(Calendar.MONTH)
                - calendarStartDate.get(Calendar.MONTH)
                + (calendarEndDate.get(Calendar.YEAR) - calendarStartDate
                .get(Calendar.YEAR)) * 12;

        return months;
    }

    /**
     * 根据起始日和相隔天数计算终止日
     *
     * @param startDate
     * @param days
     * @return
     */
    public static Date getEndDateByDays(Date startDate, int days) {
        Calendar calendarStartDate = Calendar.getInstance();
        calendarStartDate.setTime(startDate);
        calendarStartDate.add(Calendar.DAY_OF_YEAR, days);

        return calendarStartDate.getTime();
    }

    /**
     * 根据起始日和相隔月数计算终止日
     *
     * @param startDate
     * @param months
     * @return
     */
    public static Date getEndDateByMonths(Date startDate, int months) {
        Calendar calendarStartDate = Calendar.getInstance();
        calendarStartDate.setTime(startDate);
        calendarStartDate.add(Calendar.MONTH, months);

        return calendarStartDate.getTime();
    }

    /**
     * 根据起始日和期限计算终止日
     *
     * @param startDate
     * @param term      YYMMDD格式的贷款期限
     * @return
     */
    public static Date getEndDateByTerm(Date startDate, String term) {
        int years = Integer.parseInt(term.substring(0, 2));
        int months = Integer.parseInt(term.substring(2, 4));
        int days = Integer.parseInt(term.substring(4, 6));
        return getEndDateByDays(getEndDateByMonths(startDate, years * 12
                + months), days);
    }

    /**
     * 根据终止日和相隔天数计算起始日
     *
     * @param endDate
     * @param days
     * @return
     */
    public static Date getStartDateByDays(Date endDate, int days) {
        Calendar calendarEndDate = Calendar.getInstance();
        calendarEndDate.setTime(endDate);
        calendarEndDate.add(Calendar.DAY_OF_YEAR, 0 - days);

        return calendarEndDate.getTime();
    }

    /**
     * 根据终止日和相隔月数计算起始日
     *
     * @param endDate
     * @param months
     * @return
     */
    public static Date getStartDateByMonths(Date endDate, int months) {
        Calendar calendarEndDate = Calendar.getInstance();
        calendarEndDate.setTime(endDate);
        calendarEndDate.add(Calendar.MONTH, 0 - months);

        return calendarEndDate.getTime();
    }

    /**
     * 判断两个日期是否对日
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean isSameDate(Date startDate, Date endDate) {
        Calendar calendarStartDate = Calendar.getInstance();
        Calendar calendarEndDate = Calendar.getInstance();
        if (startDate == null && endDate == null) return true;
        if (startDate == null || endDate == null) return false;
        // 设日历为相应日期
        calendarStartDate.setTime(startDate);
        calendarEndDate.setTime(endDate);
        if (startDate.after(endDate)) {
            Calendar swap = calendarStartDate;
            calendarStartDate = calendarEndDate;
            calendarEndDate = swap;
        }

        if (calendarStartDate.get(Calendar.DATE) == calendarEndDate
                .get(Calendar.DATE))
            return true;

        if (calendarStartDate.get(Calendar.DATE) > calendarEndDate
                .get(Calendar.DATE)) {
            if (calendarEndDate.get(Calendar.DATE) == calendarEndDate
                    .getActualMaximum(Calendar.DATE))
                return true;
        }

        return false;
    }

    /**
     * 判断日期是否与指定的日期对日
     *
     * @param date
     * @param dd
     * @return
     */
    public static boolean isSameDate(Date date, String dd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = Integer.parseInt(dd);

        if (calendar.get(Calendar.DATE) == day)
            return true;

        if (calendar.get(Calendar.DATE) < day) {
            if (calendar.get(Calendar.DATE) == calendar
                    .getActualMaximum(Calendar.DATE))
                return true;
        }

        return false;
    }

    /**
     * 判断两个日期是否同一个月
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean isSameMonth(Date startDate, Date endDate) {
        if (startDate == null || endDate == null)
            return false;

        Calendar calendarStartDate = Calendar.getInstance();
        Calendar calendarEndDate = Calendar.getInstance();

        // 设日历为相应日期
        calendarStartDate.setTime(startDate);
        calendarEndDate.setTime(endDate);

        if (calendarStartDate.get(Calendar.YEAR) == calendarEndDate
                .get(Calendar.YEAR)
                && calendarStartDate.get(Calendar.MONTH) == calendarEndDate
                .get(Calendar.MONTH))
            return true;

        return false;
    }

    /**
     * 得到本月第一天的日期
     *
     * @param today
     * @return
     */
    public static Date getFirstDate(Date today) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, 1);

        return calendar.getTime();
    }

    /**
     * 描述:获取下一个月的第一天.
     *
     * @return
     */
    public static Date getNextDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 描述:获取上一个月的第一天.
     *
     * @return
     */
    public static Date getTopMonthDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 得到本月最后一天的日期
     *
     * @param today
     * @return
     */
    public static Date getLastDate(Date today) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

        return calendar.getTime();
    }

    public static long compareDate(Date sourceDate, Date targetDate) {
        long ret = -1;
        if (sourceDate == null && targetDate == null) {
            ret = 0;
        } else if (sourceDate == null) {
            ret = -1;
        } else if (targetDate == null) {
            ret = 1;
        } else {
            Calendar src = Calendar.getInstance();
            src.setTime(sourceDate);
            Calendar tar = Calendar.getInstance();
            tar.setTime(targetDate);
            if (src.get(Calendar.YEAR) > tar.get(Calendar.YEAR)) {
                ret = 1;
            } else if (src.get(Calendar.YEAR) < tar.get(Calendar.YEAR)) {
                ret = -1;
            } else {
                if (src.get(Calendar.MONTH) > tar.get(Calendar.MONTH)) {
                    ret = 1;
                } else if (src.get(Calendar.MONTH) < tar.get(Calendar.MONTH)) {
                    ret = -1;
                } else {
                    if (src.get(Calendar.DATE) > tar.get(Calendar.DATE)) {
                        ret = 1;
                    } else if (src.get(Calendar.DATE) < tar.get(Calendar.DATE)) {
                        ret = -1;
                    } else {
                        ret = 0;
                    }
                }
            }
        }
        return ret;
    }

    public static int getWeekNumByDate(String date) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(DF_YMD);
        Date d = format.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int num = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return num;
    }


    //是否月末
    public static boolean isEndOfMonth(Date nowday) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(nowday);
        if (ca.get(Calendar.DATE) == ca.getActualMaximum(Calendar.DAY_OF_MONTH))
            return true;
        else
            return false;
    }

    //是否月末
    public static boolean isEndOfMonth() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(getCurrentDate_Date());
        if (ca.get(Calendar.DATE) == ca.getActualMaximum(Calendar.DAY_OF_MONTH))
            return true;
        else
            return false;
    }

    //今天是否月中(两周)
    public static boolean isMiddleOfMonth() {
        int d = getDay();
        if (d == 15) {
            return true;
        } else {
            return false;
        }
    }

    //今天是否周末
    public static boolean isEndOfWeek() throws Exception {
        return getWeekNumByDate(getCurrentDate_String()) == 0;
    }

    public static int getWeekday(String date) {
        Date d = stringToDate(date);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.DATE);
    }

    //今天是否1个半月
    public static boolean isOneAndHelfMonth() {
        int m = getMonth();
        int d = getDay();
        if (m == 2 && d == 15) {
            return true;
        }
        if (m == 3 && d == 31) {
            return true;
        }
        if (m == 5 && d == 15) {
            return true;
        }
        if (m == 6 && d == 30) {
            return true;
        }
        if (m == 8 && d == 15) {
            return true;
        }
        if (m == 9 && d == 30) {
            return true;
        }
        if (m == 11 && d == 15) {
            return true;
        }
        if (m == 12 && d == 31) {
            return true;
        }
        return false;
    }

    //今天是否双月
    public static boolean isDoubleMonth() {
        int m = getMonth();
        Date nowdate = getCurrentDate_Date();
        if (m == 2 || m == 4 || m == 6 || m == 8 || m == 10 || m == 12) {
            return isEndOfMonth(nowdate);
        }
        return false;
    }

    /**
     * 获取当天是周几
     *
     * @param today
     * @return
     */
    public static int getWeek(Date today) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
//        return calendar.get(Calendar.DAY_OF_WEEK)-1;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
        }
        return 0;
    }

    /*
     * 得到出发时间为周几
     */
    public static String getWeekDay(String depDate) throws Exception {
        String today = "";
        String[] week = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        SimpleDateFormat dateFormat = new SimpleDateFormat(DF_YMD);
        Date dDate = dateFormat.parse(depDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dDate);
        int dw = cal.get(Calendar.DAY_OF_WEEK) - 1;
        today = week[dw];
        return today;
    }

    //返回指定时间的前后n小时的时间
    public static Date addNHours(Date date, int hours) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(date);
        currentDate.add(Calendar.HOUR_OF_DAY, hours);
        return currentDate.getTime();
    }

    public static Date addDay(Date date, int day) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(date);
        currentDate.add(Calendar.DATE, day);
        return currentDate.getTime();
    }

    public static Calendar turnCalendar(String datetime) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(DateUtils.stringToDate_full(datetime));
        return calendar;
    }

    //是否两小时内
    public static boolean isNearTwoHour(String time) {
        try {
            if (StringUtils.isEmpty(time)) {
                return false;
            }
            int h = DateUtils.getHour24();
            int m = DateUtils.getMinute();
            String[] times = time.split(":");
            int now_m = h * 60 + m;
            int time_m = Integer.parseInt(times[0]) * 60 + Integer.parseInt(times[1]);
            if (now_m + 120 >= time_m) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    //多长时间之后的时间串
    public static String lateTime(String dateStr, String time) {
        if (StringUtils.isEmpty(time)) {
            return "";
        }
        int d = 0;
        if (!StringUtils.isEmpty(dateStr)) {
            d = DateUtils.nDaysBetweenTwoDate(DateUtils.getNowYMD(), dateStr);
        }
        if (d > 0) {
            return d + "天后";
        }
        if (d < 0) {
            return "已过期";
        }
        int h = DateUtils.getHour24();
        int m = DateUtils.getMinute();
        String[] times = time.split(":");
        int now_m = h * 60 + m;
        int time_m = Integer.parseInt(times[0]) * 60 + Integer.parseInt(times[1]);
        if (now_m > time_m) {
            return "已过期";
        }
        int n = time_m - now_m;
        String r = "";
        int h_str = n / 60;
        int m_str = n % 60;
        if (h_str > 0) {
            r = h_str + "小时" + m_str + "分钟后";
        } else {
            r = m_str + "分钟后";
        }

        return r;
    }

    public static int[] dateSpan(Date beginDate, Date endDate) {
        Calendar b_cal = Calendar.getInstance();
        b_cal.setTime(beginDate);
        Calendar e_cal = Calendar.getInstance();
        e_cal.setTime(endDate);
        int year = e_cal.get(Calendar.YEAR) - b_cal.get(Calendar.YEAR);
        int month = e_cal.get(Calendar.MONTH) - b_cal.get(Calendar.MONTH);
        if (month < 0 && year > 0) {
            year--;
            month += 12;
        }
        int day = e_cal.get(Calendar.DATE) - b_cal.get(Calendar.DATE);
        if (day < 0 && month > 0) {
            month--;
            day += 30;
        }
        int hour = e_cal.get(Calendar.HOUR) - b_cal.get(Calendar.HOUR);
        if (hour < 0 && day > 0) {
            day--;
            hour += 24;
        }
        int minute = e_cal.get(Calendar.MINUTE) - b_cal.get(Calendar.MINUTE);
        if (minute < 0 && hour > 0) {
            hour--;
            minute += 60;
        }
        int second = e_cal.get(Calendar.SECOND) - b_cal.get(Calendar.SECOND);
        if (second < 0 && minute > 0) {
            minute--;
            second += 60;
        }
        int[] ymdhms = new int[6];
        ymdhms[0] = year;
        ymdhms[1] = month;
        ymdhms[2] = day;
        ymdhms[3] = hour;
        ymdhms[4] = minute;
        ymdhms[5] = second;
        return ymdhms;
    }

    public static int[] dateSpanEx(Date beginDate, Date endDate) {
        long i = (endDate.getTime() - beginDate.getTime()) / 1000;
        int l = (int) i;
        int second = l;
        int minute = l / 60;
        int hour = l / 60 / 60;
        int day = l / 60 / 60 / 24;
        int month = day / 30;
        int year = month / 12;
        int[] ymdhms = new int[6];
        ymdhms[0] = year;
        ymdhms[1] = month;
        ymdhms[2] = day;
        ymdhms[3] = hour;
        ymdhms[4] = minute;
        ymdhms[5] = second;
        return ymdhms;
    }

    public static String formatDateSpan(Date beginDate, Date endDate) {
        int[] ymdhms = dateSpan(beginDate, endDate);
        int y = ymdhms[0];
        if (y > 0) {
            if (y > 10) return "10年前";
            else return y + "年前";
        }
        int m = ymdhms[1];
        if (m > 0) {
            return m + "个月前";
        }
        int d = ymdhms[2];
        if (d > 0) {
            return d + "天前";
        }

        int h = ymdhms[3];
        if (h > 0) {
            return h + "小时前";
        }
        int mi = ymdhms[4];
        if (mi > 0) {
            return mi + "分钟前";
        }
        int s = ymdhms[5];
        return s + "秒前";
    }

    //得到某天的零辰时间
    public static Date todayZeroTime(Date ndate) {
        String todayStr = DateUtils.dateToString(ndate, DateUtils.DF_YMD);
        String todayStr_begin = todayStr + " 00:00:00";
        return DateUtils.stringToDate(todayStr_begin);
    }

    //得到某天的晚间零辰时间
    public static Date todayNightZeroTime(Date ndate) {
        String todayStr = DateUtils.dateToString(ndate, DateUtils.DF_YMD);
        String todayStr_begin = todayStr + " 23:59:59";
        return DateUtils.stringToDate_full(todayStr_begin);
    }

    //得到昨天晚间零辰时间
    public static Date yesterdayNightZeroTime(Date ndate) {
        Date y = nDaysAfterDate(ndate, -1);
        return todayNightZeroTime(y);
    }

    //得到昨天晚间零辰时间
    public static String yesterdayNightZeroTime(String ndateStr) {
        return DateUtils.dateToString_full(yesterdayNightZeroTime(DateUtils.stringToDate(ndateStr)));
    }

    //计算当前时间是否在有效期内
    public static boolean checkValidRebate(String rebateWeeks, String rebateTimeseg) {
        Date now = DateUtils.getCurrentDate_Date();
        int weeks = DateUtils.getWeek(now);
        String[] timesegs = StringUtils.defaultString(rebateTimeseg).split("~");
        boolean valid = StringUtils.defaultString(rebateWeeks).contains(String.valueOf(weeks));
        if (timesegs != null && timesegs.length == 2) {
            double dStart = timeToDouble(timesegs[0]);
            double dEnd = timeToDouble(timesegs[1]);
            double dNowTime = timeToDouble(DateUtils.dateToString(now, "HH:mm"));
            if (dNowTime >= dStart && dNowTime <= dEnd) {
                return valid;
            }
        }
        return false;
    }

    //计算当前时间是否在有效期内（日期类型）
    public static boolean checkValidCurrent(Date dateBegin, Date dateEnd) {
        if (null == dateBegin || null == dateEnd) {
            return false;
        }
        Long timeBegin = dateBegin.getTime();
        Long timeEnd = dateEnd.getTime();
        Long timeNow = System.currentTimeMillis();
        if (timeNow >= timeBegin && timeNow <= timeEnd) {
            return true;
        }
        return false;
    }

    private static double timeToDouble(String time) {
        String[] times = time.replace("：", ":").split(":");
        if (times.length == 2) {
            return StringUtils.string2Double(times[0])
                    + StringUtils.formatDigit(
                    StringUtils.string2Double(times[1]) / 60, 2);
        }
        return 0.0;
    }

    //计算当前时间是否在今天（日期类型）
    public static boolean isToday(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        if (fmt.format(date).toString().equals(fmt.format(new Date()).toString())) {//格式化为相同格式
            return true;
        } else {
            return false;
        }
    }

    //计算当前时间是否在今天（日期类型）
    public static boolean isToday(String dateStr) {
        Date date = stringToDate(dateStr);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        if (fmt.format(date).toString().equals(fmt.format(new Date()).toString())) {//格式化为相同格式
            return true;
        } else {
            return false;
        }
    }

    //判断是否超过24小时
    public static boolean is24hour(String date1, String date2) throws Exception {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date start = sdf.parse(date1);
        java.util.Date end = sdf.parse(date2);
        long cha = end.getTime() - start.getTime();
        double result = cha * 1.0 / (1000 * 60 * 60);
        if (result <= 24) {
            //System.out.println("可用");
            return true;
        } else {
            //System.out.println("已过期");
            return false;
        }
    }

    //判断是否超过24小时
    public static boolean is24hour(Date start, Date end) throws Exception {
        long cha = end.getTime() - start.getTime();
        double result = cha * 1.0 / (1000 * 60 * 60);
        if (result <= 24) {
            //System.out.println("可用");
            return true;
        } else {
            //System.out.println("已过期");
            return false;
        }
    }

    // 指定开始时间从" 00:00:00";
    public static String getStartTime(String startTime) {
        return startTime + " 00:00:00";
    }

    // 指定结束时间到" 23:59:59";
    public static String getEndTime(String endTime) {
        return endTime + " 23:59:59";
    }

    public static Date addDateMinute(Date now, int minute) {
        long time = minute * 60 * 1000;//分钟
        return new Date(now.getTime() + time);
    }

    public static Date addDateSecond(Date now, int second) {
        long time = second * 1000;//秒
        return new Date(now.getTime() + time);
    }

    public static Date addYear(Date date, int i) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(date);
        currentDate.add(Calendar.YEAR, i);
        return currentDate.getTime();
    }

    public static Date addMonth(Date date, int i) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(date);
        currentDate.add(Calendar.MONTH, i);
        return currentDate.getTime();
    }

    public static Date addWeek(Date date, int i) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(date);
        currentDate.add(Calendar.DATE, i * 7);
        return currentDate.getTime();
    }

    public static Date removeDay(Date date, int day) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(date);
        currentDate.add(Calendar.DATE, -day);
        return currentDate.getTime();
    }

    /**
     * 取得时间范围的字符串的起止日期(注意是日期不是时间 李毅 2018/9/28)
     * datePattern = 日期的格式 ，可以是 yyyy/MM/dd 或 yyyy-MM-dd
     */
    public static Map<String, Object> splitDateRange(String timerang, String datePattern) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("suc", 0);
        if (StringUtils.isNotEmpty(timerang)) {
            timerang = timerang.trim();
            if (timerang.length() >= 10) {
                SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
                String startdate = timerang.substring(0, 10);
                String enddate = timerang.substring(timerang.length() - 10, timerang.length());
                try {
                    map.put("startdate", sdf.parse(startdate));
                    sdf = new SimpleDateFormat(datePattern + " hh:mm:ss");
                    map.put("enddate", sdf.parse(enddate + " 23:59:59"));
                    map.put("suc", 1);
                } catch (Exception e) {
                    map.put("suc", 0);
                }
            }
        }
        return map;
    }


    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @author 李涛
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getStringDateFmt(String dateStr) {
        for (String key : DateFmtRegMap.keySet()) {
            Pattern p = Pattern.compile(DateFmtRegMap.get(key));
            Matcher m = p.matcher(StringUtils.trim(dateStr));
            if (m.matches())
                return key;
        }
        return null;
    }

    /**
     * 取得当前时间的Long值
     */
    public static Long getNowLong() {
        Date now = new Date();
        return now.getTime();
    }

    public static Date utcStringToDate(String utcString) throws Exception {
        String dateString = utcString.replace("Z", " UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        Date date = df.parse(dateString);
        return date;
    }
}
