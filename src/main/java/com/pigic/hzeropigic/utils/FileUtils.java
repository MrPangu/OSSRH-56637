package com.pigic.hzeropigic.utils;

import cn.pigicutils.core.convert.Convert;
import cn.pigicutils.core.io.IoUtil;
import cn.pigicutils.core.lang.Assert;
import cn.pigicutils.core.lang.Dict;
import cn.pigicutils.core.util.StrUtil;
import cn.pigicutils.core.util.URLUtil;
import com.google.common.io.ByteStreams;
import com.pigic.hzeropigic.domain.dto.FileInfos;
import com.pigic.hzeropigic.domain.repository.FileRepository;
import com.pigic.hzeropigic.infra.constant.Constants;
import org.hzero.boot.file.FileClient;
import org.hzero.core.base.BaseConstants;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author guchang.pan@hand-china.com
 *
 */
public class FileUtils {

    /**
     * 删除文件
     * @param organizationId 组织ID
     * @param fileKey 文件Key
     */
    public static void deleteFile (Long organizationId, String fileKey){
        // 获取HZERO文件客户端
        FileClient fileClient = SpringUtils.getBean(FileClient.class);
        fileClient.deleteFileByKey(organizationId, fileKey);
    }

    /**
     * 附件上传返回附件信息
     * @param organizationId 租户ID
     * @param multipartFile 文件
     * @return 文件详情
     */
    public static FileInfos uploadFile(Long organizationId, MultipartFile multipartFile){
        // 获取HZERO文件客户端
        FileClient fileClient = SpringUtils.getBean(FileClient.class);

        // 获取原文件名称
        String originalFilename = multipartFile.getOriginalFilename();

        // 获取原文件类型
        String contentType = multipartFile.getContentType();

        // 构建UUID
        String uuid = fileClient.getAttachmentUUID(organizationId);

        // 文件桶名称
        String bucket = Constants.FileUtils.IMPORT_BUCKET;

        // 构建新文件名称
        Assert.notNull(originalFilename, "空文件名");
        String fileName = uuid + originalFilename.substring(originalFilename.lastIndexOf(Constants.Symbol.POINT));

        String directory = Constants.FileUtils.NOW;
        // 上传文件
        String fileUrl = fileClient.uploadFile(
                organizationId,
                bucket,
                directory,
                fileName,
                multipartFile
        );

        // 构建fileKey
        String suffix = fileUrl.substring(fileUrl.lastIndexOf(BaseConstants.Symbol.SLASH));
        String fileKey= StrUtil.concat(
                true,
                directory,
                "/",
                Convert.convert(String.class, organizationId),
                suffix
        );
        // 获取文件明细
        FileRepository  fileRepository= SpringUtils.getBean(FileRepository.class);
        Dict dict = fileRepository.selectFileByFileKey(fileKey);
        String createdName = dict.getStr("created_name");
        Long createdBy = dict.getLong("created_by");
        Long fileId = dict.getLong("file_id");
        String fileSize = dict.getStr("file_size");
        String creationDate = dict.getStr("creation_date");

        // 上传成功构建者模式构建文件信息返回
        return FileInfos.builder()
                .fileName(originalFilename)
                .fileUrl(fileUrl)
                .fileKey(fileKey)
                .contentType(contentType)
                .bucket(bucket)
                .directory(directory)
                .createName(createdName)
                .createBy(createdBy)
                .createTime(creationDate)
                .fileSize(fileSize)
                .fileId(fileId)
                .build();
    }

    /**
     * 附件上传返回附件信息
     * @param organizationId 租户ID
     * @param fileUrl 文件
     * @return 文件详情
     */
    public static FileInfos uploadFile(Long organizationId, String originalFilename, String contentType,  String fileUrl){
        InputStream stream = URLUtil.getStream(URLUtil.toUrlForHttp(fileUrl));
        byte[] bytes = IoUtil.readBytes(stream);

        // 获取HZERO文件客户端
        FileClient fileClient = SpringUtils.getBean(FileClient.class);

        // 构建UUID
        String uuid = fileClient.getAttachmentUUID(organizationId);

        // 文件桶名称
        String bucket = Constants.FileUtils.IMPORT_BUCKET;

        // 构建新文件名称
        Assert.notNull(originalFilename, "空文件名");
        String fileName = uuid + originalFilename.substring(originalFilename.lastIndexOf(Constants.Symbol.POINT));

        String directory = Constants.FileUtils.NOW;
        // 上传文件
        String fileUrlNew = fileClient.uploadFile(
                organizationId,
                bucket,
                directory,
                fileName,
                bytes
        );

        // 构建fileKey
        String suffix = fileUrlNew.substring(fileUrlNew.lastIndexOf(BaseConstants.Symbol.SLASH));
        String fileKey= StrUtil.concat(
                true,
                directory,
                "/",
                Convert.convert(String.class, organizationId),
                suffix
        );
        // 获取文件明细
        FileRepository  fileRepository= SpringUtils.getBean(FileRepository.class);
        Dict dict = fileRepository.selectFileByFileKey(fileKey);
        String createdName = dict.getStr("created_name");
        Long createdBy = dict.getLong("created_by");
        Long fileId = dict.getLong("file_id");
        String fileSize = dict.getStr("file_size");
        String creationDate = dict.getStr("creation_date");

        // 上传成功构建者模式构建文件信息返回
        return FileInfos.builder()
                .fileName(originalFilename)
                .fileUrl(fileUrlNew)
                .fileKey(fileKey)
                .contentType(contentType)
                .bucket(bucket)
                .directory(directory)
                .createName(createdName)
                .createBy(createdBy)
                .createTime(creationDate)
                .fileSize(fileSize)
                .fileId(fileId)
                .build();
    }

    /**
     * 附件上传返回附件信息
     * @param organizationId
     * @param originalFilename
     * @param contentType
     * @param fileBytes
     * @return
     */
    public static FileInfos uploadFile(Long organizationId, String originalFilename, String contentType, byte[] fileBytes){
        // 获取HZERO文件客户端
        FileClient fileClient = SpringUtils.getBean(FileClient.class);

        // 构建UUID
        String uuid = fileClient.getAttachmentUUID(organizationId);

        // 文件桶名称
        String bucket = Constants.FileUtils.IMPORT_BUCKET;

        // 构建新文件名称
        Assert.notNull(originalFilename, "空文件名");
        String fileName = uuid + originalFilename.substring(originalFilename.lastIndexOf(Constants.Symbol.POINT));

        String directory = Constants.FileUtils.NOW;
        // 上传文件
        String fileUrl = fileClient.uploadFile(
                organizationId,
                bucket,
                directory,
                fileName,
                contentType,
                fileBytes
        );

        // 构建fileKey
        String suffix = fileUrl.substring(fileUrl.lastIndexOf(BaseConstants.Symbol.SLASH));
        String fileKey= StrUtil.concat(
                true,
                directory,
                "/",
                Convert.convert(String.class, organizationId),
                suffix
        );
        // 获取文件明细
        FileRepository  fileRepository= SpringUtils.getBean(FileRepository.class);
        Dict dict = fileRepository.selectFileByFileKey(fileKey);
        String createdName = dict.getStr("created_name");
        Long createdBy = dict.getLong("created_by");
        Long fileId = dict.getLong("file_id");
        String fileSize = dict.getStr("file_size");
        String creationDate = dict.getStr("creation_date");

        // 上传成功构建者模式构建文件信息返回
        return FileInfos.builder()
                .fileName(originalFilename)
                .fileUrl(fileUrl)
                .fileKey(fileKey)
                .contentType(contentType)
                .bucket(bucket)
                .directory(directory)
                .createName(createdName)
                .createBy(createdBy)
                .createTime(creationDate)
                .fileSize(fileSize)
                .fileId(fileId)
                .build();
    }
}
