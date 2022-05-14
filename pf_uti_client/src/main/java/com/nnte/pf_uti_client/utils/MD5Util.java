package com.nnte.pf_uti_client.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MD5Util {

    private static Log logger = LogFactory.getLog(MD5Util.class);

    private MD5Util() {
    }

    /**
     * Returns a MessageDigest for the given <code>algorithm</code>.
     * <p>
     * The MessageDigest algorithm name.
     *
     * @return An MD5 digest instance.
     * @throws RuntimeException when a {@link java.security.NoSuchAlgorithmException} is
     *                          caught
     */

    static MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calculates the MD5 digest and returns the value as a 16 element
     * <code>byte[]</code>.
     *
     * @param data Data to digest
     * @return MD5 digest
     */
    public static byte[] md5(byte[] data) {
        return getDigest().digest(data);
    }

    /**
     * Calculates the MD5 digest and returns the value as a 16 element
     * <code>byte[]</code>.
     *
     * @param data Data to digest
     * @return MD5 digest
     */
    public static byte[] md5(String data) {
        return md5(data.getBytes());
    }

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex
     * string.
     *
     * @param data Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(byte[] data) {
        return HexUtil.toHexString(md5(data));
    }

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex
     * string.
     *
     * @param data Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(String data) {
        if (StringUtils.isEmpty(data))
            return "";
        return HexUtil.toHexString(md5(data));
    }


    /**
     * md5加密 32位小写
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String md5For32LowerCase(String encryptStr, String coding) throws UnsupportedEncodingException {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = StringUtils.isNotEmpty(coding) ? md5.digest(encryptStr.getBytes(coding)) : md5.digest(encryptStr.getBytes());
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            encryptStr = hexValue.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return encryptStr;
    }

    /**
     * md5加密 32位大写
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String md5For32UpperCase(String encryptStr) throws UnsupportedEncodingException {
        return md5For32LowerCase(encryptStr, null).toUpperCase();
    }


    /**
     * 32位小写加密  自带编码转码
     *
     * @param encryptStr 加密内容
     * @param coding     UTF-8
     * @return
     */
    public static String md5ToDigest(String encryptStr, String coding) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(encryptStr.getBytes(coding));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    /**
     * 32位小写加密  自带编码转码
     *
     * @param encryptStr 加密内容
     * @param coding     UTF-8
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String md5ToDigestUpperCase(String encryptStr, String coding) throws UnsupportedEncodingException {
        return md5ToDigest(encryptStr, coding).toUpperCase();
    }

    public static String toMD5(String signature) throws Exception {
        MessageDigest mDigest = null;
        mDigest = MessageDigest.getInstance("MD5");
        mDigest.update(signature.getBytes());
        return encodeHex(mDigest.digest());
    }

    private static String encodeHex(byte[] bytes) {
        StringBuffer buffer = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {

            if (((int) bytes[i] & 0xff) < 0x10)
                buffer.append("0");
            buffer.append(Long.toString((int) bytes[i] & 0xff, 16));

        }
        return buffer.toString();
    }

    public static String signValue(String param, String key) throws Exception {
        param = param + key;
        String out = "";
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(param.getBytes());
        byte[] md5Bytes = md.digest();
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        out = hexValue.toString();
        return out;
    }

    /**
     * 验签
     *
     * @param sign 验签字符串
     * @return
     */
    public static Boolean signTheCheck(String sign, Map<String, String> treeMap, String secretKey) {
        try {
            String param = "";
            for (Map.Entry<String, String> entry : treeMap.entrySet()) {
                param += entry.getKey() + entry.getValue();
            }

            param = param + secretKey;
            logger.info("sign=" + sign + " param=" + param + " secretKey=" + secretKey);

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(param.getBytes());
            byte[] md5Bytes = md.digest();
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            String out = hexValue.toString();
            logger.info("out=" + out);
            if (!sign.equals(out)) {
                return false;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return true;
    }

}
