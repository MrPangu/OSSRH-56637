package com.pigic.hzeropigic.infra.mapper;

import cn.pigicutils.core.lang.Dict;
import org.apache.ibatis.annotations.Param;

/**
 * @author guchang.pan@hand-china.com
 * @date: 2019/12/17 15:22
 */
public interface FileMapper {
    Dict selectFileByFileKey(
            @Param("fileKey") String fileKey
    );

    Dict selectFileByFileId(
            @Param("fileId") String fileId
    );

    String getCompanyCodeById(@Param("companyId") Long companyId);

    Dict getUnitInfoByCode(@Param("unitCode") String unitCode);
}
