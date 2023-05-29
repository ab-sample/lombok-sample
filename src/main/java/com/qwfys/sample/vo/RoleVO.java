package com.qwfys.sample.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liuwenke
 * @since 0.0.1
 */
@Data
@Builder
@Accessors(chain = true)
public class RoleVO {

    private Long id;

    private String code;

    private String name;
}
