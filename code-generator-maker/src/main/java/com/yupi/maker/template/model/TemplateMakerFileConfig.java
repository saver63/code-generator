package com.yupi.maker.template.model;

import com.yupi.maker.meta.Meta;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文件过滤配置JSON实体类:控制多个模板文件的配置
 */
@Data
public class TemplateMakerFileConfig {

    private List<FileInfoConfig> files;

    private FileGroupConfig fileGroupConfig;

    @NoArgsConstructor
    @Data
    public static class FileInfoConfig{
        private String path;

        private String condition;

        private List<FileFilterConfig> fileFilterConfigList;
    }

    @Data
    public static class FileGroupConfig{

        private String condition;
        private String groupKey;
        private String groupName;
    }
}
