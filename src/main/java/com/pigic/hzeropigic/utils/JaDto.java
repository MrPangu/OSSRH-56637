package com.pigic.hzeropigic.utils;

import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**
 * @author guchang.pan@hand-china.com
 * @date 2019/12/16 15:10
 */

@Getter
@Setter
@NoArgsConstructor
@FieldNameConstants(prefix = "FIELD_")
public class JaDto extends AuditDomain implements Serializable {
    /**
     * 租户ID
     */
    @ApiModelProperty(value = "租户ID",hidden = true)
    private Long tenantId;

    /**
     * 是否启用。1启用，0未启用
     */
    @ApiModelProperty(value = "是否启用。1启用，0未启用")
    private Long enabledFlag;

    /**
     * 弹性域类别
     */
    @ApiModelProperty(value = "")
    private String attributeCategory;

    /**
     * 弹性域1
     */
    @ApiModelProperty(value = "")
    private String attribute1;

    /**
     * 弹性域2
     */
    @ApiModelProperty(value = "")
    private String attribute2;

    /**
     * 弹性域3
     */
    @ApiModelProperty(value = "")
    private String attribute3;

    /**
     * 弹性域4
     */
    @ApiModelProperty(value = "")
    private String attribute4;

    /**
     * 弹性域5
     */
    @ApiModelProperty(value = "")
    private String attribute5;

    /**
     * 弹性域6
     */
    @ApiModelProperty(value = "")
    private String attribute6;

    /**
     * 弹性域7
     */
    @ApiModelProperty(value = "")
    private String attribute7;

    /**
     * 弹性域8
     */
    @ApiModelProperty(value = "")
    private String attribute8;

    /**
     * 弹性域9
     */
    @ApiModelProperty(value = "")
    private String attribute9;

    /**
     * 弹性域10
     */
    @ApiModelProperty(value = "")
    private String attribute10;

    /**
     * 弹性域11
     */
    @ApiModelProperty(value = "")
    private String attribute11;

    /**
     * 弹性域12
     */
    @ApiModelProperty(value = "")
    private String attribute12;

    /**
     * 弹性域13
     */
    @ApiModelProperty(value = "")
    private String attribute13;

    /**
     * 弹性域14
     */
    @ApiModelProperty(value = "")
    private String attribute14;

    /**
     * 弹性域15
     */
    @ApiModelProperty(value = "")
    private String attribute15;


}
