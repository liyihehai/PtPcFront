package com.nnte.pf_basic.component;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.annotation.WatchAttr;
import com.nnte.basebusi.annotation.WatchInterface;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.fdfs_client_mgr.FdfsClientMgrComponent;
import com.nnte.framework.utils.FileUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.config.AppBasicConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Iterator;

@Component
@WatchAttr(value = 102)
@BusiLogAttr(AppBasicConfig.JarLoggerName)
public class PFBasicComponent extends BaseComponent implements WatchInterface {
    @Autowired
    private FdfsClientMgrComponent fdfsClientMgrComponent;

    /**
     * 通过FastDFS返回的groupName（分组名:一般为group1,group2...）
     * 和storePath(挂载点名称：一般为M00,M01,...)确定静态文件访问
     * 时应设置的前缀,在nginx中会设置静态文件访问的路径，如：
     * location / 或  location /api/等，系统中保存文件路径应包含
     * 前缀部分，如/api/00/00/...，不同的程序应更改本函数的返回
     * <p>
     * storePath在FastDFS的配置文件storage.conf中定义，store_path_count = 1
     * 表示1个挂载点，名称为M00,路径由store_path0定义，如果有两个挂载点，应
     * 设置store_path_count = 2，同时设置store_path1，此时名称为M01
     */
    public String getPathPri(String groupName, String storePath) {
        return groupName + "/" + storePath; //返回 /group1/M00
    }

    /**
     * 上传并保存图片文件,为防止垃圾文件需删除原图片文件
     */
    public String uploadImageFile(MultipartHttpServletRequest multipartRequest, String groupName) throws BusiException {
        Iterator<String> Is = multipartRequest.getFileNames();
        if (!Is.hasNext())
            throw new BusiException(1003, "没有取得上传的文件内容");
        String uploadFileName = Is.next();
        MultipartFile templateFile = multipartRequest.getFile(uploadFileName);
        if (templateFile == null) {
            throw new BusiException(1003, "不能取得上传的文件内容");
        }
        String srcFile=multipartRequest.getParameter("srcFile");
        String filename = templateFile.getOriginalFilename();
        byte[] bytes;
        try {
            bytes = templateFile.getBytes();
            if (bytes == null || bytes.length <= 0) {
                throw new BusiException(1003, "不能取得上传的文件内容");
            }
        } catch (IOException e) {
            throw new BusiException(1003, "不能取得上传的文件内容");
        }
        //String srcFile = null;
        return uploadImageFile(groupName, filename, srcFile, bytes);
    }

    public String uploadImageFile(String imageGroup, String fileName,
                                  String srcFile, byte[] content) throws BusiException {
        //替换原有将模板文件保存在应用服务器,模板文件改为保存在文件服务器中
        String submitName = fdfsClientMgrComponent.uploadFile(imageGroup, content, FileUtil.getExtention(fileName));
        if (StringUtils.isNotEmpty(submitName)) {
            String[] names = submitName.split(":");
            String groupName = names[0];
            String subString = names[1];
            String subPath = StringUtils.right(subString, subString.length() - subString.indexOf('/'));
            String fdfs_store_path = StringUtils.leftByChar(subString, "/");
            if (StringUtils.isNotEmpty(srcFile)) {
                //如果存在被替换的模板文件名，需要在文件服务器端删除原始的模板文件
                String srcGroup=StringUtils.left(srcFile,srcFile.indexOf('/'));
                String noGroupSrcPath = StringUtils.right(srcFile, srcFile.length() - srcFile.indexOf('/')-1);
                fdfsClientMgrComponent.deleteFile(srcGroup, noGroupSrcPath);
            }
            return getPathPri(groupName, fdfs_store_path) + subPath;
        } else {
            throw new BusiException(1003, "保存图片文件失败");
        }
    }

    @Override
    public void runWatch() {
        Boolean result = fdfsClientMgrComponent.activeTest();
        outLogDebug("fdfsClientMgrComponent.activeTest......" + result.toString());
    }
}
