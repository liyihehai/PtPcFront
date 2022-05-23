package com.nnte.pf_uti.utils;

import com.nnte.framework.utils.NumberUtil;
import com.nnte.pf_uti.entertity.UtiTokenDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class UtiJwtUtils {
    private static SignatureAlgorithm signatureAlgorithm=SignatureAlgorithm.HS512;
    private static Key secretKey = deserializeKey("L7A/6zARSkK1j7Vd5SDD9pSSqZlqF7mAhiOgRbgv9Smce6tf4cJnvKOjtKPxNNnWQj+2lQEScm3XIUjhW+YVZg==");
    private static long EXPIRED_TIME = 60*60*2*1000;//到期时间为2小时

    public static SignatureAlgorithm getSignatureAlgorithm() {
        return signatureAlgorithm;
    }
    private static Key getSecretKey() {
        return secretKey;
    }
    private static Key deserializeKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        Key key = new SecretKeySpec(decodedKey, getSignatureAlgorithm().getJcaName());
        return key;
    }
    /**
     * encodedKey = "L7A/6zARSkK1j7Vd5SDD9pSSqZlqF7mAhiOgRbgv9Smce6tf4cJnvKOjtKPxNNnWQj+2lQEScm3XIUjhW+YVZg==";
     * sa=SignatureAlgorithm.HS512
     * EXPIRED_TIME = 60*60*24*1000 (一天)
     * */
    public static void initUtiJwtParams(SignatureAlgorithm sa,String encodedKey,String expiredTime) {
        signatureAlgorithm = sa;
        secretKey = deserializeKey(encodedKey);
        EXPIRED_TIME = NumberUtil.getDefaultLong(expiredTime);
    }
    /**
     * 根据客户信息创建并返回一个Token
     * */
    public static String createUtiToken(UtiTokenDTO utiTokenDTO) {
        Date now = new Date();
        utiTokenDTO.setCreateTime(now);
        utiTokenDTO.setExpirationDate(new Date(now.getTime()+EXPIRED_TIME));
        String token = Jwts.builder().setSubject(utiTokenDTO.getUtiAccount())
                .claim("merchantCode", utiTokenDTO.getMerchantCode())
                .claim("terminal",utiTokenDTO.getTerminal())
                .claim("loginIp", utiTokenDTO.getLoginIp())
                .claim("timeStamp",utiTokenDTO.getTimeStamp())
                .setExpiration(utiTokenDTO.getExpirationDate())
                .signWith(getSignatureAlgorithm(), getSecretKey()).compact();
        return token;
    }

    public static UtiTokenDTO parseAndValidate(String token) throws Exception {
        UtiTokenDTO utiTokenDTO = null;
        try {
            Claims claims = Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody();
            String utiAccount = claims.getSubject();
            String merchantCode = (String) claims.get("merchantCode");
            String terminal = (String) claims.get("terminal");
            String loginIp = (String) claims.get("loginIp");
            Long timeStamp = (Long)claims.get("timeStamp");
            Date expirationDate = claims.getExpiration();
            utiTokenDTO = new UtiTokenDTO();
            utiTokenDTO.setUtiAccount(utiAccount);
            utiTokenDTO.setTerminal(terminal);
            utiTokenDTO.setMerchantCode(merchantCode);
            utiTokenDTO.setLoginIp(loginIp);
            utiTokenDTO.setTimeStamp(timeStamp);
            utiTokenDTO.setExpirationDate(expirationDate);
        } catch (JwtException ex) {
            throw new Exception(ex);
        }
        return utiTokenDTO;
    }

    private static String serializeKey(Key key) {
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        return encodedKey;
    }
}
