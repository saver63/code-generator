package com.yupi.maker.template.model;

import com.yupi.maker.meta.Meta;
import lombok.Data;

/**
 * 模板制作配置
 */
@Data
public class TemplateMakerConfig {

    private Long id;

    private Meta meta = new Meta();

    private String originProjectPath;

    //可能为空，所以需要给默认值
    private TemplateMakerModelConfig modelConfig = new TemplateMakerModelConfig() ;

    //可能为空，所以需要给默认值
    private TemplateMakerFileConfig fileConfig = new TemplateMakerFileConfig();

    //默认值为true
    private TemplateMakerOutputConfig outputConfig = new TemplateMakerOutputConfig();
}
