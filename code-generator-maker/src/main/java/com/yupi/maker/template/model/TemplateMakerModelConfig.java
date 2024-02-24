package com.yupi.maker.template.model;


import com.yupi.maker.meta.Meta;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 模型过滤配置JSON实体类:控制多个模板文件的配置
 */
@Data
public class TemplateMakerModelConfig {

    private List<ModelInfoConfig> models;

    private ModelGroupConfig modelGroupConfig;

    @NoArgsConstructor
    @Data
    public static class ModelInfoConfig{
        private String fieldName;
        private String type;
        private String description;
        private Object defaultValue;
        private String abbr;
        private List<Meta.ModelConfig.ModelInfo> models;

        //用于替换哪些文本
        private String replaceText;
    }

    @Data
    public static class ModelGroupConfig{

        private String condition;
        private String groupKey;
        private String groupName;
        private String type;
        private String description;
    }
}
