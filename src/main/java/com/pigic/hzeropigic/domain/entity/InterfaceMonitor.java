package com.pigic.hzeropigic.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.NotBlank;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 外部接口监控
 *
 * @author guchang.pan@hand-china.com
 */
@ApiModel("外部接口监控")
@VersionAudit
@ModifyAudit
@Table(name = "halm_interface_monitor")
@Getter
@Setter
@ToString(includeFieldNames = false)
@FieldNameConstants
public class InterfaceMonitor extends AuditDomain {

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long interfaceId;
    @ApiModelProperty(value = "接口名")
    private String interfaceName;
    @ApiModelProperty(value = "请求的url")
    @NotBlank
    private String requestUrl;
    @ApiModelProperty(value = "请求头")
    private String requestHeader;
    @ApiModelProperty(value = "请求参数")
    private String requestParam;
    @ApiModelProperty(value = "请求体")
    private String requestBody;
    @ApiModelProperty(value = "响应状态，1：成功,0: 失败")
    @NotNull
    private Integer responseStatus;
    @ApiModelProperty(value = "响应码，200,404，500")
    private Long responseCode;
    @ApiModelProperty(value = "响应体，如果成功为响应消息，如果失败为失败信息。")
    private String responseBody;
    @ApiModelProperty(value = "注册的接口ID")
    private Long registerId;
    @ApiModelProperty(value = "租户ID")
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "请求方法")
    private String requestMethod;
    @ApiModelProperty(value = "弹性域1")
    private String attribute1;
    @ApiModelProperty(value = "弹性域2")
    private String attribute2;
    @ApiModelProperty(value = "弹性域3")
    private String attribute3;
    @ApiModelProperty(value = "弹性域4")
    private String attribute4;
    @ApiModelProperty(value = "弹性域5")
    private String attribute5;
    @ApiModelProperty(value = "弹性域6")
    private String attribute6;

    private List<InterfaceRegister> interfaceRegisterList;
	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

}
