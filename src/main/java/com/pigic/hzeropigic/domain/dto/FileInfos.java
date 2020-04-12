package com.pigic.hzeropigic.domain.dto;

import lombok.*;
import lombok.experimental.FieldNameConstants;

/**
 * @author guchang.pan@hand-china.com
 * @date 2019/12/17 13:27
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants(prefix = "FIELD_")
public class FileInfos {
    private String fileName;
    private String fileUrl;
    private String fileKey;
    private Long fileId;
    private String bucket;
    private String directory;
    private String contentType;
    private String fileSize;
    private Long createBy;
    private String createName;
    private String createTime;
}
