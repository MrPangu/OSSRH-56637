package com.pigic.hzeropigic.infra.repository.impl;

import cn.pigicutils.core.lang.Dict;
import com.pigic.hzeropigic.domain.repository.FileRepository;
import com.pigic.hzeropigic.infra.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author guchang.pan@hand-china.com
 *
 */
@Component
public class FileRepositoryImpl implements FileRepository {
    @Autowired
    private FileMapper fileMapper;
    /**
     * 根据文件Key获取文件信息
     *
     * @param fileKey 文件Key
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    @Override
    public Dict selectFileByFileKey(String fileKey) {
        Dict dict = fileMapper.selectFileByFileKey(fileKey);
        return dict;
    }

    /**
     * 根据文件ID获取文件信息
     *
     * @param fileId 文件ID
     * @return
     */
    @Override
    public Dict selectFileByFileId(String fileId) {
        return fileMapper.selectFileByFileId(fileId);
    }

    @Override
    public String getCompanyCodeById(Long companyId) {
        return fileMapper.getCompanyCodeById(companyId);
    }

    @Override
    public Dict getUnitInfoByCode(String unitCode) {
        return fileMapper.getUnitInfoByCode(unitCode);
    }
}
