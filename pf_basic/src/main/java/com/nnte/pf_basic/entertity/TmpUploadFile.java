package com.nnte.pf_basic.entertity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TmpUploadFile {
    private String filePath;    //路径文件名
    private Date expireTime;    //过期时间
}
