package com.pigic.hzeropigic.utils;

import cn.pigicutils.core.convert.Convert;
import cn.pigicutils.core.io.IoUtil;
import cn.pigicutils.core.lang.Dict;
import cn.pigicutils.core.util.URLUtil;
import com.google.common.collect.Lists;
import com.pigic.hzeropigic.infra.constant.Constants;
import org.hzero.boot.message.MessageClient;
import org.hzero.boot.message.entity.Attachment;
import org.hzero.boot.message.entity.Message;
import org.hzero.boot.message.entity.Receiver;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 邮件发送工具类
 * @author guchang.pan@hand-china.com
 * @date: 2019/12/19 22:55
 */
public class MessageUtils {

    /**
     * 发送邮件携带附件
     * @param organizationId 租户id
     * @param args 模板参数 {xxx: "aaa", yyy: "bbb"}
     * @param files 附件链接列表 {fileName: 'a.png', fileUrl: 'http://www.baidu.tutu.png'}
     * @param emails 邮件
     * @return
     */
    @SuppressWarnings("Duplicates")
    public static Message sendEmail(Long organizationId, Dict args, List<Dict> files, String templateCode, String... emails){
        List<Receiver> receiverList = new ArrayList<>();
        for (int i = 0; i < emails.length; i++) {
            Receiver receiver = new Receiver();
            receiver.setUserId(Convert.convert(Long.class, i+1));
            receiver.setEmail(emails[i]);
            receiver.setTargetUserTenantId(organizationId);
            receiverList.add(receiver);
        }

        MessageClient messageClient = SpringUtils.getBean(MessageClient.class);

        // 构建附件
        List<Attachment> attachmentList = Lists.newArrayList();
        files.forEach(file->{
            Boolean isUrl = file.getBool("isUrl");
            byte[] fileBytes = file.getBytes("fileBytes");
            String fileName = file.getStr("fileName");
            String fileUrl = file.getStr("fileUrl");
            Attachment attachment = new Attachment();
            attachment.setFileName(fileName);
            if (isUrl){
                URL url = URLUtil.toUrlForHttp(fileUrl);
                InputStream inputStream = URLUtil.getStream(url);
                attachment.setFile(IoUtil.readBytes(inputStream));
            }else{
                attachment.setFile(fileBytes);
            }
            attachmentList.add(attachment);
        });

        // 发送邮件
        return messageClient.async().sendEmail(
                // 租户ID
                organizationId,
                Constants.EmailConfig.SERVERCODE,
                templateCode,
                Constants.EmailConfig.LANG,
                receiverList,
                args.toMapStr(),
                attachmentList.toArray(new Attachment[]{})
        );
    }

    public static Message sendWebMessage(String templateCode, Dict param, Long... userIds) {
        List<Receiver> receiverList = new ArrayList<>();
        for (int i = 0; i < userIds.length; i++) {
            Receiver receiver = new Receiver();
            receiver.setUserId(userIds[i]);
            receiver.setTargetUserTenantId(Constants.DEFAULT_TENANT_ID);
            receiverList.add(receiver);
        }
        MessageClient messageClient = SpringUtils.getBean(MessageClient.class);
        return messageClient.sendWebMessage(
                Constants.DEFAULT_TENANT_ID,
                templateCode,
                Constants.EmailConfig.LANG,
                receiverList,
                param.toMapStr()
        );
    }
}
