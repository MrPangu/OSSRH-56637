package com.pigic.hzeropigic.infra.constant;

import cn.pigicutils.core.date.DateUtil;
import org.hzero.core.base.BaseConstants;

import java.util.Date;

/**
 * @author guchang.pan@hand-china.com
 *
 */
public interface Constants extends BaseConstants {
    interface Common{
        String POINT = ".";
        String SLASH = "/";
        String BACKSLASH = "\\";
        Long TENANTID=0L;
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
    /**
     * 文件常量类
     */
    interface FileUtils{
        /**
         * 导入桶名称
         */
        String IMPORT_BUCKET="common-bucket";

        /**
         * 当前时间
         */
        String NOW=DateUtil.format(new Date(),"yyyyMMdd");
    }

    interface EmailConfig{

        /**
         * 邮箱账户编码
         */
        String SERVERCODE="EMAIL-PLUS";
        /**
         * 邮箱账户编码
         */
        String SMS_CODE="CIEC";

        /**
         * 语言
         */
        String LANG="zh_CN";
    }
}
