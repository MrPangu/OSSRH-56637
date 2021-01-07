package cn.pigicutils.core.bean;

import lombok.*;
import lombok.experimental.FieldNameConstants;

/**
 * @author pgc
 * @date 2020/5/7 15:05
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants(prefix = "FIELD_")
public class Token {
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private Long expiresIn;
    private String scope;
}
