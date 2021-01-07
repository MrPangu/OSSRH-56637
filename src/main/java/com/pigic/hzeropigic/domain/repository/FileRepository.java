package com.pigic.hzeropigic.domain.repository;

import cn.pigicutils.core.lang.Dict;

/**
 * @author guchang.pan@hand-china.com
 *
 */
public interface FileRepository {
    /**
     * 根据文件Key获取文件信息
     * @param fileKey 文件Key
     * @return
     */
    Dict selectFileByFileKey(String fileKey);

    /**
     * 根据文件ID获取文件信息
     * @param fileId 文件ID
     * @return
     */
    Dict selectFileByFileId(String fileId);

    String getCompanyCodeById(Long companyId);

    Dict getUnitInfoByCode(String unitCode);
}
