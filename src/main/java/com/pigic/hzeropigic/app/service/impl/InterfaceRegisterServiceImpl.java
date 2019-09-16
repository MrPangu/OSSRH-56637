package com.pigic.hzeropigic.app.service.impl;

import com.github.pagehelper.PageInfo;
import com.pigic.hzeropigic.app.service.InterfaceRegisterService;
import com.pigic.hzeropigic.domain.entity.InterfaceRegister;
import com.pigic.hzeropigic.domain.repository.InterfaceRegisterRepository;
import com.pigic.hzeropigic.utils.PageHelperUtil;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 外部接口注册应用服务默认实现
 *
 * @author guchang.pan@hand-china.com 2019-06-29 17:02:00
 */
@Service
public class InterfaceRegisterServiceImpl extends BaseServiceImpl<InterfaceRegister> implements InterfaceRegisterService {
    @Autowired
    private InterfaceRegisterRepository interfaceRegisterRepository;
    @Cacheable(value = "apfm_interfaceRegister", keyGenerator = "customKeyGenerator")
    @Override
    public PageInfo<InterfaceRegister> getInterfaceRegisterPageList(PageRequest pageRequest, InterfaceRegister interfaceRegister) {
        io.choerodon.core.domain.PageInfo pageInfo1 = PageHelper.startPageAndSort(pageRequest);
        List<InterfaceRegister> interfaceRegisterList = interfaceRegisterRepository.select(interfaceRegister);
        PageInfo<InterfaceRegister> pageInfo = PageHelperUtil.MiddlewarePageInfo(pageInfo1, interfaceRegisterList);
        return pageInfo;
    }
}
