package com.yupi.maker.template.model;

import lombok.Builder;
import lombok.Data;

/**
 * 文件过滤配置信息类
 */
//@Builder构造实体类方便
@Data
@Builder
public class FileFilterConfig {

    /**
     * 过滤范围
     */
    private String range;

    /**
     * 过滤规则
     */
    private String rule;

    /**
     * 过滤值
     */
    private String value;


}
