package com.pigic.hzeropigic.api.controller.v1;

import com.github.pagehelper.PageInfo;
import com.pigic.hzeropigic.app.service.InterfaceMonitorService;
import com.pigic.hzeropigic.configration.HzeroPigicSwaggerApiConfig;
import io.choerodon.swagger.annotation.CustomPageRequest;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.pigic.hzeropigic.domain.entity.InterfaceMonitor;
import com.pigic.hzeropigic.domain.repository.InterfaceMonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 外部接口监控 管理 API
 *
 * @author guchang.pan@hand-china.com 2019-06-29 11:23:53
 */
@ConditionalOnProperty(value = "hzero.inteface.enabled", havingValue = "true")
@RestController("interfaceMonitorController.v1")
@RequestMapping("/v1/{organizationId}/interface-monitors")
@Api(tags = HzeroPigicSwaggerApiConfig.APFM_INTERFACE_MONITOR)
public class InterfaceMonitorController extends BaseController {

    @Autowired
    private InterfaceMonitorService interfaceMonitorService;
    @ApiOperation(value = "外部接口监控列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    @CustomPageRequest
    public ResponseEntity<PageInfo<InterfaceMonitor>> list(@PathVariable("organizationId") Long tenantId, InterfaceMonitor interfaceMonitor, @ApiIgnore @SortDefault(value = InterfaceMonitor.FIELD_INTERFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        interfaceMonitor.setTenantId(tenantId);
        pageRequest.setSort(new Sort(new Sort.Order(Sort.Direction.DESC,"creationDate")));
        PageInfo<InterfaceMonitor> interfaceMonitorPageList = interfaceMonitorService.getInterfaceMonitorPageList(pageRequest, interfaceMonitor);
        return Results.success(interfaceMonitorPageList);
    }

    @ApiOperation(value = "外部接口监控明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{interfaceId}")
    public ResponseEntity<InterfaceMonitor> detail(@PathVariable Long interfaceId) {
        InterfaceMonitor interfaceMonitor = interfaceMonitorService.selectByPrimaryKey(interfaceId);
        return Results.success(interfaceMonitor);
    }

    @ApiOperation(value = "创建外部接口监控")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<InterfaceMonitor> create(@PathVariable("organizationId") Long tenantId, @RequestBody InterfaceMonitor interfaceMonitor) {
        interfaceMonitor.setTenantId(tenantId);
        validObject(interfaceMonitor);
        interfaceMonitorService.insertSelective(interfaceMonitor);
        return Results.success(interfaceMonitor);
    }

    @ApiOperation(value = "修改外部接口监控")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<InterfaceMonitor> update(@PathVariable("organizationId") Long tenantId, @RequestBody InterfaceMonitor interfaceMonitor) {
        interfaceMonitor.setTenantId(tenantId);
        SecurityTokenHelper.validToken(interfaceMonitor);
        interfaceMonitorService.updateByPrimaryKeySelective(interfaceMonitor);
        return Results.success(interfaceMonitor);
    }

    @ApiOperation(value = "删除外部接口监控")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@PathVariable("organizationId") Long tenantId, @RequestBody InterfaceMonitor interfaceMonitor) {
        interfaceMonitor.setTenantId(tenantId);
        SecurityTokenHelper.validToken(interfaceMonitor);
        interfaceMonitorService.deleteByPrimaryKey(interfaceMonitor);
        return Results.success();
    }

}
