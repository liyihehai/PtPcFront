package com.nnte.pf_uti_client.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlEncodeUtil {

	public static String UrlEncode(String content) {
		String encode_content="";
		try {
			String decStr=UrlDecode(content);
			if (!decStr.equals(content))
				encode_content=content;
			else
				encode_content=URLEncoder.encode(content,"UTF-8");
		} catch (Exception e) {}
		return encode_content;
	}
	
	public static String UrlDecode(String content) {
		String decode_content="";
		try {
			if (content.indexOf('%')>-1)
				decode_content=URLDecoder.decode(content,"UTF-8");
			else
				decode_content=content;
		} catch (Exception e) {}
		return decode_content;
	}
}
