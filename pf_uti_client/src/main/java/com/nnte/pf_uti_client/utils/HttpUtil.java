package com.nnte.pf_uti_client.utils;

import com.nnte.pf_uti_client.entertity.TKeyValue;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 

public class HttpUtil {

    public static List<TKeyValue<String,String>> normalHeader = new ArrayList<>();
    public static String POST_SEND_FILE_KEY = "POST_SEND_FILE";
    public static String REV_FILE_REQUEST_KEY = "REV_FILE_REQUEST";

    static {
        normalHeader.add(new TKeyValue<>("Accept", "*/*"));
        normalHeader.add(new TKeyValue<>("Accept-Charset", "UTF-8"));
        normalHeader.add(new TKeyValue<>("Cache-Control", "no-cache"));
        normalHeader.add(new TKeyValue<>("Connection", "keep-alive"));
    }

	public static String sendPostURL(String url) throws Exception {
		return sendPostURL(url,10000);
	}
	
	public static String sendPostURL(String url,int timeout) throws Exception {
        return doPostHttps(url,normalHeader,null,timeout);
	}

    public static String sendGetURL(String url,int timeout) throws Exception {
        return doGetHttps(url,normalHeader,timeout);
    }
    public static String sendGetURL(String url) throws Exception {
        return sendGetURL(url,10000);
    }
    /**
     * 通过POST向服务器端发送一个文件
     * */
    public static String sendFileByPost(String url,String pathFileName,int timeout) throws Exception {
        Map<String,String> bodyMap = new HashMap<>();
        bodyMap.put(POST_SEND_FILE_KEY,pathFileName);
        return doPostHttps(url,normalHeader,bodyMap,timeout);
    }
    /**
     * 通过POST向服务器端发送一个文件,不设超时
     * */
    public static String sendFileByPost(String url,String pathFileName) throws Exception {
        return sendFileByPost(url,pathFileName,-1);
    }
    /**
     * 通过POST方式发送一个请求，希望服务器端返回一个文件，将文件存成指定的文件名
     * */
    public static String recvFileByPost(String url,String pathFileName,Map<String,String> bodyMap,
                                        int timeout) throws Exception {
        List<TKeyValue<String,String>> recvHeader=new ArrayList<>();
        recvHeader.addAll(normalHeader);
        recvHeader.add(new TKeyValue(REV_FILE_REQUEST_KEY,pathFileName));
        return doPostHttps(url,recvHeader,bodyMap,timeout);
    }

    public static String recvFileByPost(String url,String pathFileName,int timeout) throws Exception {
        return recvFileByPost(url,pathFileName,null,timeout);
    }

    public static String recvFileByPost(String url,String pathFileName) throws Exception {
        return recvFileByPost(url,pathFileName,null,-1);
    }
    /**
     * 通过Get方式发送一个请求，希望服务器端返回一个文件，将文件存成指定的文件名
     * */
    public static String recvFileByGet(String url,String pathFileName,Map<String,String> params,
                                        int timeout) throws Exception {
        List<TKeyValue<String,String>> recvHeader=new ArrayList<>();
        recvHeader.addAll(normalHeader);
        recvHeader.add(new TKeyValue(REV_FILE_REQUEST_KEY,pathFileName));
        return sendGetURL(url,recvHeader,params,timeout);
    }
    public static String recvFileByGet(String url,String pathFileName,int timeout) throws Exception {
        return recvFileByGet(url,pathFileName,null,timeout);
    }
    public static String recvFileByGet(String url,String pathFileName) throws Exception {
        return recvFileByGet(url,pathFileName,null,-1);
    }
    /**
     * 发送一个带参数的get请求，参数包含在路径中
     * */
    public static String sendGetURL(String url,List<TKeyValue<String,String>> headerList,
                                    Map<String,String> params,int timeout) throws Exception {
        String getPath = url.trim();
        if (params!=null && params.size()>0){
            StringBuffer sbuf=new StringBuffer();
            if (getPath.indexOf("?")>=0)
                sbuf.append("&");
            else
                sbuf.append("?");
            int count=0;
            for(String key:params.keySet()){
                if (count>0)
                    sbuf.append("&");
                sbuf.append(key).append("=").append(UrlEncodeUtil.UrlEncode(params.get(key)));
                count++;
            }
            getPath+=sbuf.toString();
        }
        return doGetHttps(getPath,headerList,timeout);
    }
    /**
     * 设置请求超时
     * */
    private static void setRequestTimeout(HttpRequestBase request, Integer timeout){
        if (timeout!=null && timeout>0) {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(timeout)
                    .setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout)
                    .build();
            request.setConfig(requestConfig);
        }
    }
    /**
     * 设置请求头
     * */
    private static String setRequestHeader(HttpRequestBase request, List<TKeyValue<String,String>> headerList){
        String fileName=null;
        if (headerList!=null && headerList.size()>0){
            for(TKeyValue<String,String> tkv:headerList){
                if (tkv.getKey().equals(REV_FILE_REQUEST_KEY)){
                    fileName = tkv.getValue();
                    request.addHeader("Content-Type","application/octet-stream");
                }else
                    request.addHeader(tkv.getKey(),tkv.getValue());
            }
        }
        return fileName;
    }
    /**
     * 设置POS body参数
     * 如果BODY参数只有一个，且KEY=POST_SEND_FILE_KEY，表示要上传文件，value必须为文件的
     * 路径文件名
     * */
    private static void setPostRequestBody(HttpPost httpPost, Map<String,String> bodyMap)
            throws UnsupportedEncodingException,Exception {
        if (bodyMap!=null && bodyMap.size()>0) {
            if (!StringUtils.isEmpty(bodyMap.get(POST_SEND_FILE_KEY))){
                try {
                    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                    String pathFileName = bodyMap.get(POST_SEND_FILE_KEY);
                    String fileName = pathFileName.substring(pathFileName.lastIndexOf("/") + 1);
                    FileInputStream is = new FileInputStream(pathFileName);
                    /*绑定文件参数，传入文件流和contenttype，此处也可以继续添加其他formdata参数*/
                    builder.addBinaryBody("file", is, ContentType.MULTIPART_FORM_DATA, fileName);
                    for(String key:bodyMap.keySet()){
                        if (!key.equals(POST_SEND_FILE_KEY)) {
                            StringBody value = new StringBody(bodyMap.get(key), ContentType.create("text/plain", Consts.UTF_8));
                            builder.addPart(key,value);
                        }
                    }
                    HttpEntity entity = builder.build();
                    httpPost.setEntity(entity);
                }catch (Exception e){
                    throw new Exception(e);
                }
            }else {
                List<NameValuePair> list = new ArrayList<>();
                for (String key : bodyMap.keySet()) {
                    list.add(new BasicNameValuePair(key, bodyMap.get(key)));
                }
                if (list.size() > 0) {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
                    httpPost.setEntity(entity);
                }
            }
        }
    }
    /**
     * 如果需要接收文件，recvFile不能为空，如果文件接收成功，返回recvFile
     * 如果不需要接收文件，返回请求返回的内容
     * */
    private static String doRequest(CloseableHttpClient httpClient, HttpRequestBase request,
                                    String recvFile) throws Exception{
        HttpResponse response = httpClient.execute(request);
        if(response != null){
            HttpEntity resEntity = response.getEntity();
            if(resEntity != null){
                if (StringUtils.isEmpty(recvFile))
                    return EntityUtils.toString(resEntity,"UTF-8");
                else{
                    //创建接收文件的流
                    File file = new File(recvFile);
                    OutputStream outputStream = new FileOutputStream(file);
                    outputStream.write(EntityUtils.toByteArray(resEntity));
                    outputStream.flush();
                    outputStream.close();
                    return recvFile;
                }
            }
        }
        return null;
    }
	/**
	 * * post方式请求服务器(https协议)
	 * 主要将数据写到请求体里面
	 */
	public static String doPostHttps(String url, List<TKeyValue<String,String>> headerList,
                                     Map<String,String> bodyMap,Integer timeout) throws Exception{
        CloseableHttpClient httpClient;
		HttpPost httpPost = null;
		String result = null;
		try{
		    if (url.indexOf("https:")>=0)
                httpClient = new SSLClient();
		    else
                httpClient = HttpClients.createDefault();
            httpPost = new HttpPost(url);
            //设置超时
            setRequestTimeout(httpPost,timeout);
			//设置Header参数
            String recvFile=setRequestHeader(httpPost,headerList);
			//设置Body参数
            setPostRequestBody(httpPost,bodyMap);
            //发送请求并处理返回
            result = doRequest(httpClient,httpPost,recvFile);
		}finally {
            httpPost.releaseConnection();
        }
		return result;
	}

    /**
     * * get方式请求服务器(https协议)
     */
    public static String doGetHttps(String url, List<TKeyValue<String,String>> headerList,
                                    Integer timeout)throws Exception{
        CloseableHttpClient httpClient;
        HttpGet httpGet = null;
        String result = null;
        try{
            if (url.substring(0,6).indexOf("https:")>=0)
                httpClient = new SSLClient();
            else
                httpClient = HttpClients.createDefault();
            httpGet = new HttpGet(url);
            //设置超时
            setRequestTimeout(httpGet,timeout);
            //设置Header参数
            String recvFile=setRequestHeader(httpGet,headerList);
            result = doRequest(httpClient,httpGet,recvFile);
        }finally {
            httpGet.releaseConnection();
        }
        return result;
    }
}
