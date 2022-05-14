package com.nnte.pf_uti_client.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 数字相关辅助类
 *
 * @author GuDong
 * @version $Date: 2010-04-28 &
 */
public class NumberUtil {
    /**
     * 获得默认的Long值，默认0
     *
     * @param value
     * @return
     */
    public static Long defaultValue(Long value) {
        if (value == null)
            return 0L;
        else
            return value;
    }

    /**
     * 获得默认的Double值，默认0.0
     *
     * @param value
     * @return
     */
    public static Double defaultValue(Double value) {
        return defaultValue(value, 0.0);
    }

    public static Double defaultValue(String value) {
        if (value == null || value.equals(""))
            return 0.0;
        else
            return new Double(value);
    }

    public static Long defaultLongVal(String value) {
        if (value == null || value.equals(""))
            return 0l;
        else
            return new Long(value);
    }

    /**
     * 获得默认的Double值
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static Double defaultValue(Double value, Double defaultValue) {
        if (value == null)
            return defaultValue;
        else
            return value;
    }

    /**
     * 获得默认的Integer值，默认0
     *
     * @param value
     * @return
     */
    public static Integer defaultValue(Integer value) {
        return defaultValue(value, 0);
    }

    /**
     * 获得默认的Integer值
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static Integer defaultValue(Integer value, Integer defaultValue) {
        if (value == null)
            return defaultValue;
        else
            return value;
    }

    /**
     * 获取金钱的精度
     *
     * @param value
     * @return
     */
    public static double getScaleValue4Money(double value) {
        return getScaleValue(value, 2);
    }

    public static double getScaleValue4MoneyD(Double dval) {
        double dv = 0;
        if (dval != null)
            dv = dval;
        return getScaleValue(dv, 2);
    }

    /**
     * 获取四舍五入的精度double
     *
     * @param value
     * @param sacle
     * @return
     */
    public static double getScaleValue(double value, int sacle) {
        return getScaleValue(value, sacle, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 获取精度的double
     *
     * @param value
     * @param sacle
     * @param round
     * @return
     */
    public static double getScaleValue(double value, int sacle, int round) {
        BigDecimal db = new BigDecimal(value);
        db = db.setScale(sacle, round);
        return db.doubleValue();
    }

    /**
     * 两个数相减的金钱值
     *
     * @param value1
     * @param value2
     * @return
     */
    public static double sub4Money(Double value1, Double value2) {
        return getScaleValue4Money(defaultValue(value1).doubleValue()
                - defaultValue(value2).doubleValue());
    }

    public static String formatNumber(Double value, String format) {
        DecimalFormat myformat = new DecimalFormat(format);
        return myformat.format(value);
    }

    /**
     * 两个数相加的金钱值
     *
     * @param value1
     * @param value2
     * @return
     */
    public static double add4Money(Double value1, Double value2) {
        return getScaleValue4Money(defaultValue(value1).doubleValue()
                + defaultValue(value2).doubleValue());
    }

    /**
     * 两个数相乘的金钱值
     *
     * @param value1
     * @param value2
     * @return
     */
    public static double multi4Money(Double value1, Double value2) {
        return getScaleValue4Money(defaultValue(value1).doubleValue()
                * defaultValue(value2).doubleValue());
    }

    /**
     * @param price
     * @param num
     * @return
     */
    public static double getNumsprice(Double price, int num) {
        return (price * 100 * num) / 100;
    }

    public static Long defaultValue(Long value, Long defaultValue) {
        if (value == null)
            return defaultValue;
        else
            return value;
    }

    /**
     * 两个数相除的金钱值
     *
     * @param value1
     * @param value2
     * @return
     */
    public static double divide4Money(Double value1, Double value2) {
        return getScaleValue4Money(defaultValue(value1).doubleValue()
                / defaultValue(value2).doubleValue());
    }

    /**
     * 两数相除的整数    310/100=3   99/100=0
     *
     * @param value1
     * @param value2
     * @return
     */
    public static int intNumDiv(Double value1, Integer value2) {
        if (value1 == null || value1 == 0 || value2 == null || value2 == 0) {
            return 0;
        }
        return (int) (value1 / value2);
    }

    /**
     * 两数相除的整数    310/100=3   99/100=0
     *
     * @return int
     */
    public static int intNumDiv(Double value1, Double value2) {
        if (value1 == null || Math.abs(value1) <= 0.0000001 || value2 == null || Math.abs(value2) <= 0.0000001) {
            return 0;
        }
        return (int) (value1 / value2);
    }

    public static Boolean isNotEmpty(Long value) {
        if (value != null && value > 0) return true;
        else return false;
    }

    public static Boolean isEmpty(Long value) {
        if (value == null || value < 0l) return true;
        else return false;
    }

    public static Boolean isNotEmpty(Integer value) {
        if (value != null && value > 0) return true;
        else return false;
    }

    public static Boolean isEmpty(Integer value) {
        if (value == null || value < 0) return true;
        else return false;
    }

    public static Boolean isNotEmpty(Double value) {
        if (value != null && value > 0d) return true;
        else return false;
    }

    public static Boolean isEmpty(Double value) {
        if (value == null || value < 0d) return true;
        else return false;
    }


    /**
     * 求百分比
     *
     * @param num1 除数
     * @param num2 被除数
     * @return
     */

    public static String compute(BigDecimal num1, BigDecimal num2) {
        NumberFormat nformat = NumberFormat.getNumberInstance();
        nformat.setMaximumFractionDigits(2);

        BigDecimal result = new BigDecimal(0);
        if (num2.floatValue() > 0l) {
            result = num1.divide(num2, 2, RoundingMode.HALF_UP);
        } else { //如果被除数为0的话
            num2 = new BigDecimal(100);
            result = num1.divide(num2, 2, RoundingMode.HALF_UP);
        }
        result = result.multiply(new BigDecimal(100));
        return nformat.format(result);
    }


    public static Long getDefaultLong(Object obj) {
        Long l = 0L;
        try {
            if (obj != null) {
                String objclassname = obj.getClass().getName();
                if (objclassname.indexOf("Long")>-1)
                    l = (Long) obj;
                else if (objclassname.indexOf("String")>-1)
                    l = StringUtils.string2Long(obj.toString());
                else {
                    l = Long.valueOf(obj.toString());
                }
            }
        } catch (Exception e) {
        }
        return l;
    }

    public static Integer getDefaultInteger(Object obj) {
        Integer l = 0;
        try {
            if (obj != null) {
                String objclassname = obj.getClass().getName();
                if (objclassname.indexOf("Integer")>-1)
                    l = (Integer) obj;
                else if (objclassname.indexOf("String")>-1)
                    l = StringUtils.string2Integer(obj.toString());
                else {
                    l = Integer.valueOf(obj.toString());
                }
            }
        } catch (Exception e) {
        }
        return l;
    }

    public static Double getDefaultDouble(Object obj)	{
        Double d=0d;
        try {
            if (obj!=null){
                String objclassname=obj.getClass().getName();
                if (objclassname.indexOf("Double")>-1)
                    d=(Double)obj;
                else
                    d=StringUtils.string2Double(obj.toString());
            }
        }catch(Exception e) {}
        return d;
    }

    public static Integer Double2Integer(Double obj) {
        Integer l = 0;
        try {
            if (obj != null) {
                l = obj.intValue();
            }
        } catch (Exception e) {
        }
        return l;
    }
}
