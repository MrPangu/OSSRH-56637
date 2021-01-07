package com.pigic.hzeropigic.feign;

import cn.pigicutils.core.lang.Dict;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HzeroPlatFormFeignClient
 * @author guchang.pan@hand-china.com
 **/
@FeignClient(name = "hzero-platform")
public interface PigicPlatFormFeignClient {
    /**
     * 查询配置维护信息
     * @param organizationId
     * @param profileName
     * @param userId
     * @param roleId
     * @return
     */
    @GetMapping("/v1/{organizationId}/profile-value")
    String selectProfile(@PathVariable("organizationId")
                         Long organizationId,
                         @RequestParam("profileName")
                         String profileName,
                         @RequestParam("userId")
                         Long userId,
                         @RequestParam("roleId")
                         Long roleId
    );

    /**
     * 获取值集编码
     * @param lovCode
     * @param tenantId
     * @return
     */
    @GetMapping("/v1/{organizationId}/lovs/value")
    List<Dict> queryLovValue(@ApiParam(value = "值集代码", required = true) @RequestParam("lovCode") String lovCode, @ApiParam("租户ID") @PathVariable("organizationId") Long tenantId);


    /**
     * 员工岗位分配
     * @param organizationId
     * @param employeeId
     * @return
     */
    @GetMapping("/v1/{organizationId}/employee-assigns")
    List<Hashtable> queryEmployeeAssigns(@PathVariable("organizationId") Long organizationId, @RequestParam("employeeId") Long employeeId);
}
