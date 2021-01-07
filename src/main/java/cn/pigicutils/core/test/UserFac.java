package cn.pigicutils.core.test;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author guchang.pan@hand-china.com
 *
 */
@Getter
@Setter
@ToString
public class UserFac {
    private Integer userId;
    private String userName;
    private String userPhone;
}
