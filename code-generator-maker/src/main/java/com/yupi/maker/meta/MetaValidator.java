package com.yupi.maker.meta;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.yupi.maker.meta.Meta.FileConfig;
import com.yupi.maker.meta.Meta.ModelConfig;
import com.yupi.maker.meta.enums.FileGenerateTypeEnum;
import com.yupi.maker.meta.enums.FileTypeEnum;
import com.yupi.maker.meta.enums.ModelTypeEnum;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 校验元信息
 */
public class MetaValidator {
    /**
     * 校验和默认值填充
     *
     * @param meta 元信息文件
     */
    public static void doValidAndFill(Meta meta) {

        validAndFillMetaRoot(meta);

        validAndFillFileConfig(meta);

        validAndFillModelConfig(meta);


    }

    private static void validAndFillModelConfig(Meta meta) {
        //modelConfig校验和默认值
        ModelConfig modelConfig = meta.getModelConfig();
        if (modelConfig == null) {
            return;
        }
        List<ModelConfig.ModelInfo> modelInfoList = modelConfig.getModels();
        if (CollUtil.isEmpty(modelInfoList)) {
            return;
        }
        for (ModelConfig.ModelInfo modelInfo : modelInfoList) {
            //为group,不校验
            String groupKey = modelInfo.getGroupKey();
            if (StrUtil.isNotBlank(groupKey)){
                //生成中间参数，目标生成"--author","-outputText"
                List<ModelConfig.ModelInfo> subModelInfoList = modelInfo.getModels();
                String allArgsStr = subModelInfoList.stream()
                        .map(subModelInfo -> String.format("\"--%s\"", subModelInfo.getFieldName())).
                        collect(Collectors.joining(", "));
                modelInfo.setAllArgsStr(allArgsStr);
                continue;
            }
            //输出路径默认值
            String fieldName = modelInfo.getFieldName();
            if (StrUtil.isBlank(fieldName)) {
                throw new MetaException("未填写fileName");
            }

            String modelInfoType = modelInfo.getType();
            if (StrUtil.isEmpty(modelInfoType)) {
                modelInfo.setType(ModelTypeEnum.STRING.getValue());
            }
        }
    }

    private static void validAndFillFileConfig(Meta meta) {
        //fileConfig校验和默认值
        FileConfig fileConfig = meta.getFileConfig();
        if (fileConfig == null) {
            return;
        }
        //sourceRootPath必填
        String sourceRootPath = fileConfig.getSourceRootPath();
        if (StrUtil.isBlank(sourceRootPath)) {
            throw new MetaException("未填写 sourceRootPath");
        }

        //inputRootPath: .source + sourceRootPath 的最后一个层级路径
        String inputRootPath = fileConfig.getInputRootPath();
        //todo 根据不同系统取不同的路径符号
        String defaultInputRootPath = "source/"+
               FileUtil.getLastPathEle(Paths.get(sourceRootPath)).getFileName().toString();
        //如果输入路径为空则给他自动天机最后一层路径
        if (StrUtil.isEmpty(inputRootPath)) {
            fileConfig.setInputRootPath(defaultInputRootPath);
        }

        //outputRootPath默认为当前路径下的generated路径
        String outputRootPath = fileConfig.getOutputRootPath();
        String defaultOutputRootPath = "generated";
        if (StrUtil.isEmpty(outputRootPath)) {
            fileConfig.setOutputRootPath(defaultOutputRootPath);
        }

        String fileConfigType = fileConfig.getType();
        String defaultType = FileTypeEnum.DIR.getValue();
        if (StrUtil.isEmpty(fileConfigType)) {
            fileConfig.setType(defaultType);
        }
        List<FileConfig.FileInfo> fileInfoList = fileConfig.getFiles();
        if (!CollUtil.isEmpty(fileInfoList)) {
            return;
        }
        for (FileConfig.FileInfo fileInfo : fileInfoList) {

            //如果是组类别就不需要填写inputPath
            String type = fileInfo.getType();
            if (FileTypeEnum.Group.getValue().equals(type)){
                continue;
            }
            //inputPath: 必填
            String inputPath = fileInfo.getInputPath();
            if (StrUtil.isBlank(inputPath)) {
                throw new MetaException("未填写 inputPath");
            }
            // outputPath:默认等于inputPath
            String outputPath = fileInfo.getOutputPath();
            if (StrUtil.isEmpty(outputPath)) {
                fileInfo.setOutputPath(inputPath);
            }
            //type: 默认inputPath 有文件后缀(比如.java)默认为file,否则就dir

            if (StrUtil.isBlank(type)) {
                //无文件后缀
                if (StrUtil.isBlank(FileUtil.getSuffix(inputPath))) {
                    fileInfo.setType(FileTypeEnum.DIR.getValue());
                } else {
                    fileInfo.setType(FileTypeEnum.FILE.getValue());
                }
            }

            // generateType 文件结尾不为ftl,generateType 为static,否则为dynamic
            String generateType = fileInfo.getGenerateType();
            if (StrUtil.isBlank(generateType)) {
                if (inputPath.endsWith(".ftl")) {
                    fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
                } else {
                    fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
                }
            }
        }
    }

    private static void validAndFillMetaRoot(Meta meta) {
        //基础信息校验和默认值
        String name = StrUtil.blankToDefault(meta.getName(),"my-generator");
        meta.setName(name);

        String description = StrUtil.emptyToDefault(meta.getDescription(),"我的模板代码生成器");
        meta.setDescription(description);

        String basePackage = StrUtil.blankToDefault(meta.getBasePackage(), "com.yupi");
        meta.setBasePackage(basePackage);

        String version = StrUtil.emptyToDefault(meta.getVersion(), "1.0");
        meta.setVersion(version);

        String author = StrUtil.emptyToDefault(meta.getAuthor(), "zlz");
        meta.setAuthor(author);

        String createTime = StrUtil.emptyToDefault(meta.getCreateTime(), DateUtil.now());
        meta.setCreateTime(createTime);

    }
}
