package com.pigic.hzeropigic.app.service.impl;

import com.github.pagehelper.PageInfo;
import com.pigic.hzeropigic.app.service.InterfaceMonitorService;
import com.pigic.hzeropigic.domain.entity.InterfaceMonitor;
import com.pigic.hzeropigic.domain.repository.InterfaceMonitorRepository;
import com.pigic.hzeropigic.utils.PageHelperUtil;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.mybatis.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 外部接口监控应用服务默认实现
 *
 * @author guchang.pan@hand-china.com 2019-06-29 11:23:53
 */
@Service
public class InterfaceMonitorServiceImpl extends BaseServiceImpl<InterfaceMonitor> implements InterfaceMonitorService {
    @Autowired
    private InterfaceMonitorRepository interfaceMonitorRepository;
    @Override
    public PageInfo<InterfaceMonitor> getInterfaceMonitorPageList(PageRequest pageRequest, InterfaceMonitor interfaceMonitor) {
        io.choerodon.core.domain.PageInfo pageAndSort = PageHelper.startPageAndSort(pageRequest);
        List<InterfaceMonitor> interfaceMonitorList = interfaceMonitorRepository.select(interfaceMonitor);
        PageInfo<InterfaceMonitor> pageInfo = PageHelperUtil.MiddlewarePageInfo(pageAndSort, interfaceMonitorList);
        return pageInfo;
    }
}
