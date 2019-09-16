package cn.pigicutils.core.test;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @Author: 潘顾昌
 * @Date: 2019/9/8 16:47
 */
@Getter
@Setter
@ToString
public class UserFac {
    private Integer userId;
    private String userName;
    private String userPhone;
}
