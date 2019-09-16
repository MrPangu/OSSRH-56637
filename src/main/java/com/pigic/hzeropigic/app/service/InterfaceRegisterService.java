package com.pigic.hzeropigic.app.service;

import com.github.pagehelper.PageInfo;
import com.pigic.hzeropigic.domain.entity.InterfaceRegister;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseService;

/**
 * 外部接口注册应用服务
 *
 * @author guchang.pan@hand-china.com 2019-06-29 17:02:00
 */
public interface InterfaceRegisterService extends BaseService<InterfaceRegister> {

    PageInfo<InterfaceRegister> getInterfaceRegisterPageList(PageRequest pageRequest, InterfaceRegister interfaceRegister);
}
