package com.pigic.hzeropigic.feign;

import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author guchang.pan@hand-china.com
 * @Date: 2019/7/2 18:49
 */
@FeignClient(name = "halm-mdm")
public interface HalmPigicAmdmFeginClient {

    /**
     *
     * 接口注册
     */
    @GetMapping("/v1/{organizationId}/interface-registers")
    public PageInfo<Map> interfaceRegisterslist(@PathVariable("organizationId") Long tenantId, @RequestParam("interfaceRegister") Map interfaceRegister);

    @GetMapping("/v1/{organizationId}/interface-registers/{interfaceId}")
    public Map interfaceRegistersdetail(@PathVariable("organizationId") Long tenantId, @PathVariable("interfaceId") Long interfaceId);

    @PostMapping("/v1/{organizationId}/interface-registers")
    public Map interfaceRegisterscreate(@PathVariable("organizationId") Long tenantId, @RequestBody Map interfaceRegister);

    @PutMapping("/v1/{organizationId}/interface-registers")
    public Map interfaceRegistersupdate(@PathVariable("organizationId") Long tenantId, @RequestBody Map interfaceRegister);

    @DeleteMapping("/v1/{organizationId}/interface-registers")
    public Map interfaceRegistersremove(@PathVariable("organizationId") Long tenantId, @RequestBody Map interfaceRegister);

    @GetMapping("/v1/{organizationId}/interface-registers/selectOne")
    public Map interfaceRegistersselectOne(@PathVariable("organizationId") Long tenantId, @RequestParam("interfaceRegister") Map interfaceRegister);

    /**
     *
     * 接口监控
     */
    @GetMapping("/v1/{organizationId}/interface-monitors")
    public PageInfo<Map> interfaceMonitorslist(@PathVariable("organizationId") Long tenantId, @RequestParam("interfaceMonitor") Map interfaceMonitor);

    @GetMapping("/v1/{organizationId}/interface-monitors/{interfaceId}")
    public Map interfaceMonitorsdetail(@PathVariable("organizationId") Long tenantId, @PathVariable("interfaceId") Long interfaceId);

    @PostMapping("/v1/{organizationId}/interface-monitors")
    public Map interfaceMonitorscreate(@PathVariable("organizationId") Long tenantId, @RequestBody Map interfaceMonitor);

}
