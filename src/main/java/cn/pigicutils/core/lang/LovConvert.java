package cn.pigicutils.core.lang;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * @author guchang.pan@hand-china.com
 * @Date: 2019/10/11 15:47
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
@RequiredArgsConstructor(staticName = "getInstance")
@FieldNameConstants(prefix = "FILED_")
public class LovConvert {
    private String lovCode;
    private String valueFiled;
    private String meaningFiled;
}
