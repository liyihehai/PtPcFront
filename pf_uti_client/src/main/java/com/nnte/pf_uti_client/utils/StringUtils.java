package com.nnte.pf_uti_client.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.lang.Character.UnicodeBlock;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtils {

    public static String INT_F_D = "%d";      //直接打印整数
    public static String INT_F_TSD = "%,d";     //按千分位打印整数
    public static String FLOAT_F_1F = "%.1f";    //打印1位小数浮点数
    public static String FLOAT_F_2F = "%.2f";    //打印2位小数浮点数
    public static String FLOAT_F_3F = "%.3f";    //打印3位小数浮点数
    public static String FLOAT_F_TS2F = "%,.2f";   //按千分位打印2位小数浮点数

    /**
     * trim
     *
     * @param string
     * @return
     */
    public static String trim(String string) {
        if (string == null)
            return "";
        else
            return string.trim();
    }

    /**
     * isEmpty
     *
     * @param string
     * @return boolean
     */
    public static boolean isEmpty(Object string) {
        if (string == null)
            return true;
        String str = string.toString();
        if ("".equals(str.trim()))
            return true;
        else
            return false;
    }

    /**
     * isNotEmpty
     *
     * @param string
     * @return boolean
     */
    public static boolean isNotEmpty(Object string) {
        return !isEmpty(string);
    }

    /**
     * string2Integer
     *
     * @param string
     * @return Integer
     */
    public static Integer string2Integer(String string) {
        String str = trim(string);
        if ("".equals(str))
            str = "0";
        if (str == null)
            str = "0";
        return new Integer(str);
    }

    /**
     * Upper substring assigned length.
     *
     * @param s
     * @param start
     * @param end
     * @return
     */
    public static String toUpperCase(String s, int start, int end) {
        int length = s.length();
        if (length >= end) {
            s = s.substring(0, start) + s.substring(start, end).toUpperCase() + s.substring(end, length);
        }
        return s;
    }

    /**
     * Lower substring assigned length.
     *
     * @param s
     * @param start
     * @param end
     * @return
     */
    public static String toLowerCase(String s, int start, int end) {
        int length = s.length();
        if (length >= end) {
            s = s.substring(0, start) + s.substring(start, end).toLowerCase() + s.substring(end, length);
        }
        return s;
    }

    /**
     * 首字符小写.
     */
    public static String toLowerCaseFirst(String s) {
        return toLowerCase(StringUtils.defaultString(s), 0, 1);
    }

    //首字符大写，其余的小写
    public static String changetoUpper(String name) {
        String needTransferChar = name.substring(0, 1).toUpperCase();
        return needTransferChar + name.substring(1).toLowerCase();
    }

    /**
     * 首字符大写
     */
    public static String toUpperCaseFirst(String s) {
        return toUpperCase(StringUtils.defaultString(s), 0, 1);
    }

    /**
     * integer2String
     */
    public static String integer2String(Integer value) {
        String result = "0";
        if (value != null)
            result = String.valueOf(value.intValue());
        return result;
    }

    /**
     * string2Int
     *
     * @param string
     * @return int
     */
    public static int string2Int(String string) {
        String str = trim(string);
        if ("".equals(str))
            str = "0";
        return Integer.parseInt(str);
    }

    public static long string2Long(String string) {
        String str = trim(string);
        if ("".equals(str))
            str = "0";
        return Long.parseLong(str);
    }

    /**
     * string2Double
     *
     * @param string
     * @return double
     */
    public static double string2Double(String string) {
        String str = trim(string);
        if ("".equals(str))
            str = "0.0";
        return Double.parseDouble(str);
    }

    /**
     * double2String
     *
     * @param value
     * @return
     */
    public static String double2String(Double value) {
        String result = "0.0";
        if (value != null)
            result = String.valueOf(value.doubleValue());
        return result;
    }

    public static String URLEncodeString(String str) throws UnsupportedEncodingException {
        if (isEmpty(str))
            return "";
        return URLEncoder.encode(str, "UTF-8");
    }

    public static String URLDecodeString(String str) throws UnsupportedEncodingException {
        if (isEmpty(str))
            return "";
        return URLDecoder.decode(str, "UTF-8");
    }

    public static String formatTrim(String str) {
        String re = "";
        StringBuffer sb = new StringBuffer("");
        if (str == null)
            return "";
        try {
            String[] s = str.split(";");
            for (int i = 0; i < s.length; i++) {
                if (!"".equals(s[i])) {
                    sb.append(s[i]);
                    sb.append(";");
                }
            }
            re = sb.toString();
            if (re.endsWith(";"))
                re = re.substring(0, re.length() - 1);
            return re;
        } catch (Exception io) {
            return "";
        }
    }

    /**
     * 设置默认值（为NULL时用空值代替）
     *
     * @param value
     * @return
     */
    public static String defaultString(String value) {
        return value == null || value.trim().equals("null") ? "" : value;
    }

    /*
     * Object对象提取String 李毅 2018/12/21
     * */
    public static String defaultString(Object value) {
        String s = "";
        try {
            s = (value == null || value.toString().trim().equals("null")) ? "" : value.toString();
        } catch (Exception e) {
        }
        return s;
    }

    /**
     * 删除字符串中的子字符串
     *
     * @param str
     * @param substr
     * @return
     */
    public static String delete(String str, String substr) {
        return str.replaceAll(substr, "");
    }

    /**
     * 删除字符串中的子字符串，并且原来的分隔符不变
     *
     * @param str
     * @param substr
     * @param separator
     * @return
     */
    public static String delete(String str, String substr, String separator) {
        String[] sList = str.trim().split(separator);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < sList.length; i++) {
            if (sList[i] != null && sList[i].length() > 0 && !substr.equals(sList[i]))
                sb.append(sList[i].trim()).append(separator);
        }
        if (sb.length() > 0)
            sb.delete(sb.length() - separator.length(), sb.length());
        return sb.toString();
    }

    /**
     * 转化为2位小数点的金额格式
     *
     * @param price
     * @return
     */
    public static String turnPriceFormat(String price) {
        if (price == null || "".equals(price)) {
            return "0.00";
        } else {
            int k = price.indexOf(".");
            if (k >= 0) {
                String s = price + "00";
                return s.substring(0, k + 3);
            } else {
                return price + ".00";
            }
        }
    }

    /**
     * 实现小数的精度变换
     *
     * @param value
     * @param scale
     * @return
     */
    public static double formatDigit(double value, int scale) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }

    /**
     * 小数转换成指定精度的小数（即保留几位小数）
     *
     * @param value
     * @param scale
     * @return
     */
    public static String digitToString(double value, int scale) {
        return String.valueOf(formatDigit(value, scale));
    }

    public static String delZeroString(String s) {
        if (s == null || "".equals(s))
            return s;
        if (s.lastIndexOf(".00") > 0 && s.lastIndexOf(".00") == (s.length() - 3)) {
            s = s.substring(0, s.length() - 3);
        }
        if (s.lastIndexOf(".0") > 0 && s.lastIndexOf(".0") == (s.length() - 2)) {
            s = s.substring(0, s.length() - 2);
        }
        return s;
    }

    /**
     * 格式化数字，在数字之前加指定数量的0值
     *
     * @param value
     * @param len
     * @return
     */
    public static String formatPreDigit(int value, int len) {
        if (value < 0)
            value = 0;
        String res = "";
        for (int i = 0; i < len - String.valueOf(value).length(); i++) {
            res += "0";
        }
        return res + value;
    }

    /**
     * 去掉传入值小数点之后无用的0
     *
     * @param comRate
     * @return
     */
    public static String formatDot(double comRate) {
        int iCom = (int) (comRate * 100);
        if (iCom % 100 > 0) {
            return comRate + "";
        } else {
            return (int) comRate + "";
        }
    }

    public static String formatDot(String sRate) {
        double comRate = string2Double(sRate);
        return formatDot(comRate);
    }

    /*
     * 从jsp传过来的中文参数需要在servlet中调用此方法将其转换为中文。
     */
    public static String isoToGB2312(String param) throws Exception {
        String result = "";
        if (param != null && !param.equals(""))
            result = new String(param.getBytes("ISO-8859-1"), "gb2312");
        return result;
    }

    public static String gb2312ToUtf8(String param) throws Exception {
        String result = "";
        if (param != null && !param.equals(""))
            result = new String(param.getBytes("gb2312"), "utf-8");
        return result;
    }

    public static String Utf8ToGb2312(String param) throws Exception {
        String result = "";
        if (param != null && !param.equals(""))
            result = new String(param.getBytes("utf-8"), "gb2312");
        return result;
    }

    /**
     * 取得字符串靠左的部分
     *
     * @param str
     * @param size
     * @return
     */
    public static String left(String str, int size) {
        if (str == null)
            return null;
        if (str.length() < size)
            size = str.length();
        return str.substring(0, size);
    }

    //返回指定字符左边的字符串
    public static String leftByChar(String str, String leftChar) {
        if (str == null)
            return null;
        int lefindex = str.indexOf(leftChar);
        if (lefindex >= 0)
            return left(str, lefindex);
        else
            return str;
    }

    /**
     * 取得字符串靠右的部分
     *
     * @param str
     * @param size
     * @return
     */
    public static String right(String str, int size) {
        if (str == null)
            return null;
        if (str.length() < size)
            size = str.length();
        return str.substring(str.length() - size, str.length());
    }

    // 中文符转unicode
    public static String chineseToUnicode(String str) {
        char[] myBuffer = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
            if (ub == UnicodeBlock.BASIC_LATIN) {
                sb.append(myBuffer[i]);
            } else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                int j = (int) myBuffer[i] - 65248;
                sb.append((char) j);
            } else {
                short s = (short) myBuffer[i];
                String hexS = Integer.toHexString(s);
                String unicode = "\\u" + hexS;
                sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * unicode转中文
     */
    public static String UnicodeTochinese(String dataStr) {
        int index = 0;
        StringBuffer buffer = new StringBuffer();

        while (index < dataStr.length()) {
            if (!"\\u".equals(dataStr.substring(index, index + 2))) {
                buffer.append(dataStr.charAt(index));
                index++;
                continue;
            }
            String charStr = "";
            charStr = dataStr.substring(index + 2, index + 6);
            char letter = (char) Integer.parseInt(charStr, 16);
            buffer.append(letter);
            index += 6;
        }
        return buffer.toString();
    }

    // 将 s 的{}处理成<>
    public static String getReplaceCookie(String s) {
        if (s == null)
            return null;
        return s.replaceAll("\\{", "_A_").replaceAll("\\}", "_B_").replaceAll("\\[", "_C_").replaceAll("\\]", "_D_")
                .replaceAll(",", "_E_");
    }

    public static String getUnReplaceCookie(String s) {
        if (s == null)
            return "";
        return s.replaceAll("_A_", "{").replaceAll("_B_", "}").replaceAll("_C_", "[").replaceAll("_D_", "]")
                .replaceAll("_E_", ",");
    }

    /**
     * 把string[] 转为int[]
     *
     * @param ss
     * @return
     */
    public static Integer[] turnArrayStringToInt(String[] ss) throws Exception {
        Integer[] result = new Integer[ss.length];
        for (int i = 0; i < ss.length; i++) {
            result[i] = Integer.parseInt(ss[i]);
        }
        return result;
    }

    /**
     * a是否在s中，s是逗号分隔的数字
     *
     * @param s
     * @param a
     * @return
     */
    public static boolean isInArrayString(String s, Long a) {
        if (s == null)
            return false;
        if ("".equals(s))
            return false;
        if (a == null)
            return false;
        if (a.longValue() == 0)
            return false;
        String[] ss = s.split(",");
        for (int i = 0; i < ss.length; i++) {
            String si = ss[i];
            if (Long.parseLong(si) == a.longValue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 产生一个随机的字符串
     */
    public static String RandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(62);
            buf.append(str.charAt(num));
        }
        return buf.toString();
    }

    /**
     * 产生一个随机的字符串
     */
    public static String RandomNumString(int length) {
        String str = "123456789";
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(9);
            buf.append(str.charAt(num));
        }
        return buf.toString();
    }

    /**
     * 生成一串Token
     *
     * @return
     */
    public static String GenerateToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }

    public static boolean isMobile(String mobile) {
        if (mobile == null || "".equals(mobile)) {
            return false;
        }
        Pattern p = Pattern.compile("^([1])\\d{10}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    public static String filterTxt(String s) {
        if (s == null)
            return "";
        return s.replaceAll("<", "").replaceAll(">", "").replaceAll("'", "");
    }

    public static String turnNullTxt(String s) {
        if (s == null)
            return "";
        return s;
    }

    public static String trimLength(String s, int n) {
        if (StringUtils.isEmpty(s)) {
            return s;
        }
        if (s.length() <= n) {
            return s;
        }
        return (s.substring(0, n) + "……");
    }

    public static String formatAppUrl(String appUrl, String picUrl) {
        if (isNotEmpty(picUrl)) {
            if (picUrl.toLowerCase().startsWith("http")) {
                return picUrl;
            } else {
                return appUrl + (picUrl.startsWith("/") ? picUrl.substring(1) : picUrl);
            }
        }
        return "";
    }

    public static String pathAppend(String path, String dirOrFileName) {
        if (isEmpty(dirOrFileName))
            return path;
        String dfn = dirOrFileName.startsWith("/") ? dirOrFileName.substring(1) : dirOrFileName;
        if (isNotEmpty(path)) {
            if (path.endsWith("/")) {
                return path + dfn;
            } else {
                return path + "/" + dfn;
            }
        }
        return "/" + dfn;
    }

    public static String formatImageUrl(String imageUrl, int w, int h) {
        if (isNotEmpty(imageUrl)) {
            int pos = imageUrl.lastIndexOf(".");
            String ext = imageUrl.substring(pos);
            String prefix = imageUrl.substring(0, pos);
            return prefix + "_" + w + "x" + h + ext;
        }
        return imageUrl;
    }

    public static boolean isFindStr(String s, String cs) {
        if (isEmpty(s) || isEmpty(cs))
            return false;
        String[] ss = s.split(",");
        for (int i = 0; i < ss.length; i++) {
            if (cs.equals(ss[i])) {
                return true;
            }
        }
        return false;
    }

    // 把排序好的数字拼成字符串返回
    public static String sortNubmers(Integer[] ns) {
        String result = "";
        if (ns == null)
            return result;
        for (int i = 0; i < ns.length; i++) {
            if (i == 0) {
                result = String.valueOf(ns[i]);
            } else {
                result += "," + String.valueOf(ns[i]);
            }
        }
        return result;
    }

    public static boolean isZeroCoordinates(String c) {
        if (c == null || "".equals(c))
            return true;
        if (!c.contains(",")) {
            return true;
        }
        String[] cc = c.split(",");
        try {
            if (Double.parseDouble(cc[0]) == 0) {
                if (Double.parseDouble(cc[1]) == 0) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static String fromDigit(Double value) {
        DecimalFormat df = new DecimalFormat("0.#");
        String xs = df.format(value == null ? 0.0 : value);
        return xs;
    }

    public static String dealHtml(String html) {
        if (html == null) {
            return "";
        }
        String s = html;
        s = s.replaceAll("&", "&amp;");
        s = s.replaceAll("<", "&lt;");
        s = s.replaceAll(">", "&gt;");
        s = s.replaceAll("\"", "&quot;");
        s = s.replaceAll("\r\n", "&lt;br&gt;");
        s = s.replaceAll("http://", "&-http-;");
        return s;
    }

    public static String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {
        byte[] bytes = text.getBytes("UTF-8");
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        int i = 0;
        while (i < bytes.length) {
            short b = bytes[i];
            if (b > 0) {
                buffer.put(bytes[i++]);
                continue;
            }
            b += 256;
            if ((b ^ 0xC0) >> 4 == 0) {
                buffer.put(bytes, i, 2);
                i += 2;
            } else if ((b ^ 0xE0) >> 4 == 0) {
                buffer.put(bytes, i, 3);
                i += 3;
            } else if ((b ^ 0xF0) >> 4 == 0) {
                i += 4;
            }
        }
        buffer.flip();
        return new String(buffer.array(), "utf-8");
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数 此方法中前三位格式有： 13+任意数 15+除4的任意数 18+除1和4的任意数
     * 17+除9的任意数 147
     */
    public static boolean isChinaPhone(String str) throws PatternSyntaxException {
        String regExp = "^(((13[0-9]{1})|(15[0-9]{1})|(16[0-9]{1})|(18[0-9]{1})|(17[0-9]{1})|(19[0-9]{1}))+\\d{8})$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 将字符串集合转换成字符串 以什么符号进行分割
     *
     * @param list
     * @param str
     * @return
     */
    public static String join(List<String> list, String str) {
        String returnStr = "";
        for (String string : list) {
            returnStr += string + ",";
        }
        return returnStr.substring(0, returnStr.length() - 1);
    }

    /**
     * @param cityDomainIds 1,2,3,4
     * @param cityDomainId  1
     * @return true | false
     */
    public static boolean getCityDomainId(String cityDomainIds, String cityDomainId) {
        if (isEmpty(cityDomainIds)) {
            return false;
        }
        String cityDomainIdArray[] = cityDomainIds.split(",");
        for (String id : cityDomainIdArray) {
            if (cityDomainId.equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将map转成url拼接参数字符串
     * demo3=撒手&demo=12&demo2=qqw
     *
     * @return demo3=撒手&demo=12&demo2=qqw
     */
    public static String mapToStringUrlParam(Map map) {
        StringBuffer sbf = new StringBuffer();

        Set keySet = map.keySet();
        for (Object key : keySet) {
            sbf.append(key + "=" + map.get(key) + "&");
        }
        return StringUtils.isNotEmpty(sbf.toString()) ? sbf.substring(0, sbf.toString().length() - 1) : null;
    }

    public static String mapToStringUrlParamValNotEmpty(Map<String, Object> map) {
        StringBuffer sbf = new StringBuffer();

        Set<String> keySet = map.keySet();
        for (Object key : keySet) {
            if (map.get(key) != null) {
                Object obj = map.get(key);
                if (obj.getClass().getName().equals("java.lang.String")) {
                    String val = (String) obj;
                    if (StringUtils.isNotEmpty(val))
                        sbf.append(key + "=" + val + "&");
                } else if (obj.getClass().getName().equals("java.lang.Integer") || obj.getClass().getName().equals("java.lang.Long")
                        || obj.getClass().getName().equals("java.lang.Double") || obj.getClass().getName().equals("java.lang.Float")) {
                    //需要再处理
                    sbf.append(key + "=" + map.get(key) + "&");
                } else if (obj.getClass().getName().equals("java.lang.Boolean")) {
                    //需要再处理
                    sbf.append(key + "=" + map.get(key) + "&");
                }
            }
        }
        return StringUtils.isNotEmpty(sbf.toString()) ? sbf.substring(0, sbf.toString().length() - 1) : null;
    }

    /**
     * 将url拼接参数字符串转成map
     *
     * @param params demo3=撒手&demo=12&demo2=qqw
     * @return
     * @author litao
     */
    public static Map<String, Object> urlParamToMap(String params) {

        String[] paramsArray = params.split("&");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String key = "";
        String value = "";
        for (int i = 0; i < paramsArray.length; i++) {
            try {
                key = paramsArray[i].split("=")[0];
                value = paramsArray[i].split("=")[1];
            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                //logger.error("Stringutils数组越界="+e.getMessage());
            }
            paramMap.put(key, value + "");
        }
        return paramMap;
    }

    /**
     * base64字符串转化成图片
     *
     * @param imgStr
     * @return
     * @throws IOException
     */
    public static String GenerateImage(String imgStr, String imgFilePath) throws Exception {
        Base64 base64 = new Base64();
//	     byte[] k = base64.decode(imgStr);  
        //新年祝福！！！处理
        imgStr = imgStr.substring("data:image/jpeg;base64,".length());
        byte[] k = base64.decode(imgStr);
        InputStream inputStream = new ByteArrayInputStream(k);
        byte[] data = new byte[1024];
        int len = 0;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(imgFilePath);
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
        return imgFilePath;
    }


    /**
     * 时间戳 + 6位数随机数
     *
     * @return
     */
    public static String getRanDomNum() {
        return System.currentTimeMillis() + (int) ((Math.random() * 9 + 1) * 100000) + "";
    }

    /**
     * 随机最大最少数量
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandomNum(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }


    /**
     * 获取指定字符串出现的次数
     *
     * @param srcText  源字符串
     * @param findText 要查找的字符串
     * @return
     */
    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        Pattern p = Pattern.compile(findText);
        Matcher m = p.matcher(srcText);
        while (m.find()) {
            count++;
        }
        return count;
    }

    /**
     * 手机号打马赛克
     *
     * @param mobile
     * @return
     */
    public static String hiddenMobile(String mobile) {
        return mobile.substring(0, 3) + "****" + mobile.substring(mobile.length() - 4);
    }

    /**
     * 检测车牌号
     *
     * @param content
     * @return
     */
    public static boolean checkPlateNumberFormat(String content) {
        String pattern = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";
        return Pattern.matches(pattern, content);
    }

    /**
     * 截取中英文混合字符串
     *
     * @param str 原字符串 ,len 长度
     * @return
     */
    public static String subTextString(String str, int len) {
        if (str.length() < len / 2) return str;
        int count = 0;
        StringBuffer sb = new StringBuffer();
        String[] ss = str.split("");
        for (int i = 1; i < ss.length; i++) {
            count += ss[i].getBytes().length > 1 ? 2 : 1;
            sb.append(ss[i]);
            if (count >= len) break;
        }
        //不需要显示...的可以直接return sb.toString();
        return (sb.toString().length() < str.length()) ? sb.append("...").toString() : str;
    }


    /**
     * StringUtils工具类方法
     * 获取一定长度的随机字符串，范围0-9，a-z
     *
     * @param length：指定字符串长度
     * @return 一定长度的随机字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    //字符串补足长度,flag=1前补0,否则后部0
    public static String addZeroForNum(String str, int strLength, int flag) {
        int strLen = str.length();
        StringBuffer sb = null;
        while (strLen < strLength) {
            sb = new StringBuffer();
            if (flag == 1)
                sb.append("0").append(str);// 左补0
            else
                sb.append(str).append("0");//右补0
            str = sb.toString();
            strLen = str.length();
        }
        return str;
    }

    /**
     * 返回程序到的当前代码文件名及行号
     * offLine 默认为1
     */
    public static String getLineInfo(String methodName, int offLine) {
        StackTraceElement[] stes = new Throwable().getStackTrace();
        for (int i = 0; i < stes.length; i++) {
            StackTraceElement ste = stes[i];
            if (ste.getMethodName().equalsIgnoreCase(methodName)) {
                StackTraceElement g_ste = stes[i];
                int gindex = i + offLine;
                if (gindex < stes.length) {
                    g_ste = stes[gindex];
                }
                return (g_ste.getFileName() + ":" + g_ste.getLineNumber());
            }
        }
        return "";
    }

    //字符串val与字符串列表vals中有一个大小写不敏感相等就返回true,都不相等返回false
    public static boolean equalsIgnoreCaseOneof(String val, String[] vals) {
        if (isEmpty(val) || vals == null || vals.length <= 0)
            return false;
        for (String v : vals) {
            if (val.equalsIgnoreCase(v))
                return true;
        }
        return false;
    }

    /**
     * 字符串+1方法，该方法将其结尾的整数+1,适用于任何以整数结尾的字符串,不限格式，不限分隔符。
     *
     * @param testStr 要+1的字符串
     * @return + number 后的字符串
     * @throws NumberFormatException
     * @author zxcvbnmzb
     */
    public static String stringAddNumber(String testStr, int number) {
        String[] strs = testStr.split("[^0-9]");//根据不是数字的字符拆分字符串
        String numStr = strs[strs.length - 1];//取出最后一组数字
        if (numStr != null && numStr.length() > 0) {//如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常
            int n = numStr.length();//取出字符串的长度
            int num = Integer.parseInt(numStr) + number;//将该数字加 number
            String added = String.valueOf(num);
            n = Math.min(n, added.length());
            //拼接字符串
            return testStr.subSequence(0, testStr.length() - n) + added;
        } else {
            throw new NumberFormatException();
        }
    }

    public static String replaceSubRangeString(String src, String left,
                                               String right, String replacement) {
        if (isNotEmpty(src) &&
                isNotEmpty(left) &&
                isNotEmpty(right) &&
                isNotEmpty(replacement)) {
            String regex = left + ".*" + right;
            return src.replaceAll(regex, replacement);
        }
        return src;
    }

    /**
     * 通过正则表达式取得前后标志范围内的文本
     */
    public static String getSubRangeString(String src, String left,
                                           String right) {
        if (StringUtils.isNotEmpty(src) &&
                StringUtils.isNotEmpty(left) &&
                StringUtils.isNotEmpty(right)) {
            String regex = left + "(.*)" + right;
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(src);
            if (matcher.find() && matcher.groupCount() > 0) {
                String results = matcher.group(0);
                results = results.replaceAll(left, "");
                results = results.replaceAll(right, "");
                return results;
            }
        }
        return null;
    }

    /**
     * 判断文本是否满足要求，不满足要求时抛出错误
     *
     * @Illegal = 非法字符 样例："[~#@*+%{}<>\\[\\]|\"\\_^]"
     */
    public static final String Default_Illegal = "[~#@*+%{}<>\\[\\]|\"\\_^]";

    public static void checkStringValid(String name, String src, boolean canNull, int min, int max, String Illegal) throws Exception {
        if (src == null) {
            if (!canNull)
                throw new Exception(name + "不能为空");
        } else {
            if (src.length() < min)
                throw new Exception(name + "的长度不能小于" + Integer.valueOf(min));
            if (src.length() > max)
                throw new Exception(name + "的长度不能大于" + Integer.valueOf(max));
            if (Illegal != null && Illegal.length() > 0) {
                Pattern pattern = Pattern.compile(Illegal);
                Matcher matcher = pattern.matcher(src);
                if (matcher.find())
                    throw new Exception(name + "中存在非法字符");
            }
        }
    }

    /**
     * srcStr 是分割符“ , ”分割的字符串，给每个分割的部分增加“ ' ”用于
     * SQL语句中的in（）函数操作
     */
    public static String stringsToSqlIn(String srcStr) throws Exception {
        if (srcStr == null || srcStr.length() <= 0)
            throw new Exception("字符串不能为空");
        String[] items = srcStr.split(",");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            item = trim(item);
            if (sb.length() > 0)
                sb.append(",");
            sb.append("'");
            sb.append(item);
            sb.append("'");
        }
        return sb.toString();
    }

    /**
     * 增强版的String Join操作
     */
    public static String joinEx(String delimiter, String[] srcStr, String wordLeft, String wordRight) {
        StringBuilder sb = new StringBuilder();
        boolean isLR = false;
        if (wordLeft != null && wordRight != null)
            isLR = true;
        for (String s : srcStr) {
            if (isNotEmpty(s)) {
                if (sb.length() > 0)
                    sb.append(delimiter);
                if (isLR)
                    sb.append(wordLeft).append(s).append(wordRight);
                else
                    sb.append(s);
            }
        }
        return sb.toString();
    }

    public static String joinEx(String delimiter, String[] srcStr) {
        return joinEx(delimiter, srcStr, null, null);
    }

    public static String joinEx(String[] srcStr) {
        return joinEx(",", srcStr, null, null);
    }
}
