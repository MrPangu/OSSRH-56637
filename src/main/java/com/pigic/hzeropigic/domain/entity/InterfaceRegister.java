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

/**
 * 外部接口注册
 *
 * @author guchang.pan@hand-china.com 2019-06-29 17:02:00
 */
@ApiModel("外部接口注册")
@VersionAudit
@ModifyAudit
@Getter
@Setter
@ToString(includeFieldNames = false)
@FieldNameConstants
@Table(name = "halm_interface_register")
public class InterfaceRegister extends AuditDomain {

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
    @NotBlank
    private String interfaceName;
    @ApiModelProperty(value = "接口对应的URL")
    @NotBlank
    private String requestUrl;
   @ApiModelProperty(value = "是否保存查询参数")    
    private Integer showParam;
   @ApiModelProperty(value = "是否保存头信息")    
    private Integer showHeader;
   @ApiModelProperty(value = "是否保存请求体")    
    private Integer showBody;
   @ApiModelProperty(value = "是否保存响应体")    
    private Integer showResponse;
   @ApiModelProperty(value = "是否显示响应码")    
    private Integer showResponseCode;
   @ApiModelProperty(value = "是否启用")    
    private Integer enabledFlag;
    @ApiModelProperty(value = "租户ID")
    @NotNull
    private Long tenantId;
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

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

}
