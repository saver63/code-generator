package com.yupi.maker.model;

import lombok.Data;

/**
 * 静态模板配置
 */
@Data
public class DataModel {


    /**
     * 作者（填充值）
     */
    private String author ;

    /**
     * 输出信息
     */
    private String outputText ;

    /**
     * 是否循环（开关）
     */
    private boolean loop;
}
