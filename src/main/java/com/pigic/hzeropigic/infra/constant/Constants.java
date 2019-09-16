package com.pigic.hzeropigic.infra.constant;

/**
 * @Author: 潘顾昌
 * @Date: 2019/6/25 13:35
 */
public interface Constants {
    interface Common{
        String POINT = ".";
        String SLASH = "/";
        String BACKSLASH = "\\";
    }
    interface File{
        /**
         * 文件桶名
         */
        String BUCKET_NAME = "pigic";
    }
    interface ContentType{
        /**
         * 文件类型
         */
        String MULTIPART_FORM_DATA="multipart/form-data";
    }
}
