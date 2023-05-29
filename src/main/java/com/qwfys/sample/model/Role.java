package com.qwfys.sample.model;

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
public class Role {

    private Long id;

    private String code;

    private String name;
}
