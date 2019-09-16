package com.pigic.hzeropigic.api.controller.v1;

import com.github.pagehelper.PageInfo;
import com.pigic.hzeropigic.app.service.InterfaceRegisterService;
import com.pigic.hzeropigic.configration.HzeroPigicSwaggerApiConfig;
import com.pigic.hzeropigic.domain.entity.InterfaceRegister;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.CustomPageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.ToString;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 外部接口注册 管理 API
 *
 * @author guchang.pan@hand-china.com 2019-06-29 17:02:00
 */
@ConditionalOnProperty(value = "hzero.inteface.enabled", havingValue = "true")
@RestController("interfaceRegisterController.v1")
@RequestMapping("/v1/{organizationId}/interface-registers")
@Api(tags = HzeroPigicSwaggerApiConfig.APFM_INTERFACE_REGISTER)
public class InterfaceRegisterController extends BaseController {
    @Autowired
    private InterfaceRegisterService interfaceRegisterService;
    @ApiOperation(value = "外部接口注册列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    @CustomPageRequest
    public ResponseEntity<PageInfo<InterfaceRegister>> list(@PathVariable("organizationId") Long tenantId, InterfaceRegister interfaceRegister, @ApiIgnore @SortDefault(value = InterfaceRegister.FIELD_INTERFACE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        interfaceRegister.setTenantId(tenantId);
        PageInfo<InterfaceRegister> list = interfaceRegisterService.getInterfaceRegisterPageList(pageRequest, interfaceRegister);
        return Results.success(list);
    }

    @ApiOperation(value = "查询单个")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/selectOne")
    public ResponseEntity<InterfaceRegister> selectOne(@PathVariable("organizationId") Long tenantId, InterfaceRegister interfaceRegister) {
        interfaceRegister.setTenantId(tenantId);
        InterfaceRegister interfaceRegister1 = interfaceRegisterService.selectOne(interfaceRegister);
        return Results.success(interfaceRegister1);
    }

    @ApiOperation(value = "外部接口注册明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{interfaceId}")
    public ResponseEntity<InterfaceRegister> detail(@PathVariable Long interfaceId) {
        InterfaceRegister interfaceRegister = interfaceRegisterService.selectByPrimaryKey(interfaceId);
        return Results.success(interfaceRegister);
    }

    @ApiOperation(value = "创建外部接口注册")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    @CacheEvict(value = "apfm_interfaceRegister", allEntries = true)
    public ResponseEntity<InterfaceRegister> create(@PathVariable("organizationId") Long tenantId, @RequestBody InterfaceRegister interfaceRegister) {
        interfaceRegister.setTenantId(tenantId);
        validObject(interfaceRegister);
        interfaceRegisterService.insertSelective(interfaceRegister);
        return Results.success(interfaceRegister);
    }

    @ApiOperation(value = "修改外部接口注册")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    @CacheEvict(value = "apfm_interfaceRegister", allEntries = true)
    public ResponseEntity<InterfaceRegister> update(@PathVariable("organizationId") Long tenantId, @RequestBody InterfaceRegister interfaceRegister) {
        interfaceRegister.setTenantId(tenantId);
        SecurityTokenHelper.validToken(interfaceRegister);
        interfaceRegisterService.updateByPrimaryKeySelective(interfaceRegister);
        return Results.success(interfaceRegister);
    }

    @ApiOperation(value = "删除外部接口注册")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    @CacheEvict(value = "apfm_interfaceRegister", allEntries = true)
    public ResponseEntity<?> remove(@PathVariable("organizationId") Long tenantId, @RequestBody InterfaceRegister interfaceRegister) {
        interfaceRegister.setTenantId(tenantId);
        SecurityTokenHelper.validToken(interfaceRegister);
        interfaceRegisterService.deleteByPrimaryKey(interfaceRegister);
        return Results.success();
    }

}
