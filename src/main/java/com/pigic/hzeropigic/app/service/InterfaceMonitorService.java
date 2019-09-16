package com.pigic.hzeropigic.app.service;

import com.github.pagehelper.PageInfo;
import com.pigic.hzeropigic.domain.entity.InterfaceMonitor;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseService;

import java.util.List;

/**
 * 外部接口监控应用服务
 *
 * @author guchang.pan@hand-china.com 2019-06-29 11:23:53
 */
public interface InterfaceMonitorService extends BaseService<InterfaceMonitor> {

    PageInfo<InterfaceMonitor> getInterfaceMonitorPageList(PageRequest pageRequest, InterfaceMonitor interfaceMonitor);
}
