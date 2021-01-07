package cn.pigicutils.core.lang;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * @author guchang.pan@hand-china.com
 *
 */
@ToString
@Getter
@Setter
@Accessors(chain = true)
@FieldNameConstants
public class Lov {
    private String value;
    private String meaning;
    private String description;
    private String tag;
    private String parentValue;
    private Integer orderSeq;
}
