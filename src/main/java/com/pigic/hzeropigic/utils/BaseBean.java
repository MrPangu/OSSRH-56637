package com.pigic.hzeropigic.utils;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/*
 * 生成tostring
 * @date: 2019/5/13
 * @author guchang.pan@hand-china.com
 * @version: 0.0.1
 * @copyright Copyright (c) 2019, Hand
 */
@SuppressWarnings("serial")
public abstract class BaseBean implements Serializable {
    /**
     * 重写toString
     *
     * @return String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}