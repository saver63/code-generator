package com.yupi.maker.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.maker.meta.Meta;
import com.yupi.maker.meta.enums.FileGenerateTypeEnum;
import com.yupi.maker.meta.enums.FileTypeEnum;
import com.yupi.maker.template.model.TemplateMakerFileConfig;
import com.yupi.maker.template.model.TemplateMakerModelConfig;
import com.yupi.maker.template.model.TemplateMakerModelConfig.ModelGroupConfig;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 模板制作工具
 */
public class TemplateMaker {

    /**
     * 制作模板
     *
     * @param newMeta
     * @param originProjectPath
     * @param templateMakerFileConfig
     * @param templateMakerModelConfig
     * @param id
     * @return
     */
    public static long makeTemplate(Meta newMeta, String originProjectPath , TemplateMakerFileConfig templateMakerFileConfig, TemplateMakerModelConfig templateMakerModelConfig, Long id){
        //没有id则生成
        if (id == null){
            id = IdUtil.getSnowflakeNextId();
        }

        //业务逻辑...

        //复制目录
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = projectPath + File.separator + ".temp";
        String templatePath = tempDirPath + File.separator + id;
        if (!FileUtil.exist(templatePath)){
            FileUtil.mkdir(templatePath);
            FileUtil.copy(originProjectPath, templatePath,true);
        }


        //一.输入信息

        //处理模型信息
        List<TemplateMakerModelConfig.ModelInfoConfig> models = templateMakerModelConfig.getModels();
        //转化为配置文件接收的modelInfo对象
        List<Meta.ModelConfig.ModelInfo> inputModelInfoList = models.stream()
                .map(modelInfoConfig -> {
                    Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
                    BeanUtil.copyProperties(modelInfoConfig, modelInfo);
                    return modelInfo;
                }).collect(Collectors.toList());

        //本次新增的模型列表
        List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>();

        //如果是模型组
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        if (modelGroupConfig != null){
            //加外层的分组
            String condition = modelGroupConfig.getCondition();
            String groupKey = modelGroupConfig.getGroupKey();
            String groupName = modelGroupConfig.getGroupName();

            Meta.ModelConfig.ModelInfo goupModelInfo = new Meta.ModelConfig.ModelInfo();
            goupModelInfo.setCondition(condition);
            goupModelInfo.setGroupKey(groupKey);
            goupModelInfo.setGroupName(groupName);
            //把外层模型全放到一个分组内，相当于把多层文件放在了一个包下
            goupModelInfo.setModels(inputModelInfoList);
            newModelInfoList = new ArrayList<>();
            newModelInfoList.add(goupModelInfo);

        }else {
            //不分组，添加所有的模型信息到列表
            newModelInfoList.addAll(inputModelInfoList);
        }
        
        
        
        
        
        
        

        //2.输入文件信息
        //要挖坑的项目根目录
        File tempFile = new File(templatePath);
        templatePath = tempFile.getAbsolutePath();
        String sourceRootPath = templatePath + File.separator + FileUtil.getLastPathEle(Paths.get(originProjectPath)).toString();
        //注意win系统需要对路径进行转义
        sourceRootPath = sourceRootPath.replaceAll("\\\\","/");

        //获取文件配置列表
        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = templateMakerFileConfig.getFiles();

        //支持遍历输入文件
        List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>();
        //遍历文件配置,每个文件都要路径和文件过滤列表
        for (TemplateMakerFileConfig.FileInfoConfig fileInfoConfig : fileInfoConfigList) {
            String inputFilePath = fileInfoConfig.getPath();
            //将外层输入的相对路径修改为绝对路径
            if (!inputFilePath.startsWith(sourceRootPath)){
                inputFilePath = sourceRootPath + File.separator + inputFilePath;
            }


            //传入绝对路径,进行过滤
            //得到过滤后的文件列表
            List<File> fileList = FileFilter.doFilter(inputFilePath, fileInfoConfig.getFileFilterConfigList());
            //不处理已经生成的ftl模板文件
            fileList = fileList.stream()
                    .filter(file -> !file.getAbsolutePath().endsWith(".ftl"))
                    .collect(Collectors.toList());

            //直接遍历文件去制作模板
            for (File file : fileList) {
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(templateMakerModelConfig, sourceRootPath, file);
                newFileInfoList.add(fileInfo);
            }
        }

        //分组配置
        //如果是文件组
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = templateMakerFileConfig.getFileGroupConfig();
        if (fileGroupConfig != null){
            String condition = fileGroupConfig.getCondition();
            String groupKey = fileGroupConfig.getGroupKey();
            String groupName = fileGroupConfig.getGroupName();

            Meta.FileConfig.FileInfo goupFileInfo = new Meta.FileConfig.FileInfo();
            goupFileInfo.setCondition(condition);
            goupFileInfo.setGroupKey(groupKey);
            goupFileInfo.setGroupName(groupName);
            //把外层文件全放到一个分组内，相当于把多层文件放在了一个包下
            goupFileInfo.setFiles(newFileInfoList);
            newFileInfoList = new ArrayList<>();
            newFileInfoList.add(goupFileInfo);

        }


        
        



        //三.生成配置文件(在.temp工作空间的根目录生成)
        String metaOutputPath = templatePath + File.separator + "meta.json";

        //已有meta文件不是第一次制作,则在原有的meta基础上进行修改
        if (FileUtil.exist(metaOutputPath)){

            //创建meta对象,复用newMeta
            newMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath),Meta.class);
            //1.追加配置参数
            List<Meta.FileConfig.FileInfo> fileInfoList = newMeta.getFileConfig().getFiles();
            fileInfoList.addAll(newFileInfoList);

            List<Meta.ModelConfig.ModelInfo> modelsInfoList = newMeta.getModelConfig().getModels();
            modelsInfoList.addAll(newModelInfoList);

            //配置去重
            newMeta.getFileConfig().setFiles(distinctFiles(fileInfoList));
            newMeta.getModelConfig().setModels(distinctModels(modelsInfoList));


        }else {
            //利用对象构建文件配置
            Meta.FileConfig fileConfig = new Meta.FileConfig();
            newMeta.setFileConfig(fileConfig);
            fileConfig.setSourceRootPath(sourceRootPath);
            List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
            fileConfig.setFiles(fileInfoList);

            fileInfoList.addAll(newFileInfoList);

            //利用对象构建模型配置
            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            newMeta.setModelConfig(modelConfig);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
            modelConfig.setModels(modelInfoList);
            modelInfoList.addAll(newModelInfoList);
        }
        //2.生成元信息文件
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), metaOutputPath);

        return id;
    }

    /**
     * 制作模板文件
     *
     * @param templateMakerModelConfig 模型配置对象
     * @param sourceRootPath 源文件路径
     * @param inputFile 输入的文件
     * @return 文件配置信息
     */
    private static Meta.FileConfig.FileInfo makeFileTemplate(TemplateMakerModelConfig templateMakerModelConfig, String sourceRootPath,File inputFile) {



        String fileInputAbsolutePath = inputFile.getAbsolutePath();

        //注意win系统需要对路径进行转义
        fileInputAbsolutePath = fileInputAbsolutePath.replaceAll("\\\\","/");

        //指定具体挖坑的文件（meta.json中的files）
        //去掉绝对路径当中相对路径前面的路径（注意一定要是相对路径）
        String fileInputPath = fileInputAbsolutePath.replace(sourceRootPath+"/","");
        String fileOutputPath = fileInputPath + ".ftl";

        //二.使用字符串替换，并得到文件信息
        String fileOutPutAbsolutePath = fileInputAbsolutePath+".ftl";





        String fileContent ;
        //如果已有模板文件，表示不是第一次制作，则在原有的模板基础上再挖坑
        boolean hasTemplateFile = FileUtil.exist(fileOutPutAbsolutePath);
        if (hasTemplateFile){
            //如果不是第一次挖：fileOutPutAbsolutePath表示上次的输出路径
            fileContent = FileUtil.readUtf8String(fileOutPutAbsolutePath);
        }else {
            //如果是第一次挖则用的是输入文件
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }

        //支持多个模型：对于同一个文件内容，遍历模型进行多轮替换
        ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();

        //newFileContent记录最新替换的内容
        String newFileContent = fileContent;
        String replacement;
        for (TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig : templateMakerModelConfig.getModels()) {
            String fieldName = modelInfoConfig.getFieldName();
            //模型配置
            if (modelGroupConfig == null){
                //不是分组,跟原来的替换规则一样 直接取filedName
                replacement= String.format("${%s}", fieldName);
            }else {
                //如果是分组才能获取到GroupKey
                String groupKey = modelGroupConfig.getGroupKey();
                //如果是分组，则需要先加groupKey.filedName
                replacement = String.format("${%s.%s}", groupKey,fieldName);

            }
            //第二次替换，把上一次的替换作为输入，括号中的newFileContent：为上一次结果（递归）
            newFileContent = StrUtil.replace(newFileContent,modelInfoConfig.getReplaceText(), replacement);
        }
        //  Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        //        fileInfo.setInputPath(fileInputPath);
        //        fileInfo.setOutputPath(fileOutputPath);
        //        fileInfo.setType(FileTypeEnum.FILE.getValue());
        //
        //        // 和原文件一致，没有挖坑，则为静态生成
        //        if (newFileContent.equals(fileContent)) {
        //            // 输出路径 = 输入路径
        //            fileInfo.setOutputPath(fileInputPath);
        //            fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
        //        } else {
        //            // 生成模板文件
        //            fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
        //            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
        //        }
        //        return fileInfo;


        //文件配置信息
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileOutputPath);
        fileInfo.setOutputPath(fileInputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        //文件默认是动态
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());

        //是否更改了文件内容
        boolean contentEquals = newFileContent.equals(fileContent);
        //和原文件内容一致，没有挖坑，静态生成
        if (!hasTemplateFile){
            if (contentEquals){
                //之前不存在文件，并且这次与之前一致，才是静态生成
                //如果是静态生成的话，输入路径 = 输出路径
                fileInfo.setInputPath(fileInputPath);
                fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
            }else {
                //之前不存在文件，并且这次与之前不一致，动态生成
                //输出模板文件
                FileUtil.writeUtf8String(newFileContent, fileOutPutAbsolutePath);
            }
        }else if (!contentEquals){
            //如果之前有模板文件，并且增加了新坑，则生成/更新模板文件
            FileUtil.writeUtf8String(newFileContent, fileOutPutAbsolutePath);
        }


        return fileInfo;
    }


    /**
     * 文件去重
     * @param fileInfoList
     * @return
     */
    private static List<Meta.FileConfig.FileInfo> distinctFiles (List<Meta.FileConfig.FileInfo> fileInfoList){

        //用了分治思想（一刀切）
        //1.将所有的文件配置（fileInfo）分为有分组和无分组
        //先处理有分组的文件 filter返回值为true 就保留这个文件
        //{"groupKey": "a",files:[1,2]},{"groupKey": "a",files:[2,3]},{"groupKey": "b",files:[4,5]}
        //{"groupKey": "a",files:[[1,2][2,3]]},{"groupKey": "b",files:[[4,5]]}
        //以组为单位划分
        Map<String, List<Meta.FileConfig.FileInfo>> groupKeyFileInfoListMap = fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(
                        Collectors.groupingBy(Meta.FileConfig.FileInfo::getGroupKey)
                );


        //新建合并后的对象Map
        Map<String, Meta.FileConfig.FileInfo> groupKeyMergedFileInfo = new HashMap<>();



        //2.对于有分组的文件配置，如果有相同的文件分组，同分组内的文件进行合并（merge），不同分组可同时保留
        //同组内配置合并
        //{"groupKey": "a",files:[[1,2][2,3]]} -> {"groupKey": "a",files:[[1,2,3]]}
        for (Map.Entry<String, List<Meta.FileConfig.FileInfo>> entry : groupKeyFileInfoListMap.entrySet()) {
            //{"groupKey": "a",files:[[1,2][2,3]]}
            List<Meta.FileConfig.FileInfo> tempFileInfoList = entry.getValue();
            //{"groupKey": "a",files:[[1,2,2,3]]} 将对象打平 flatMap：允许你将一个对象转化成多个对象（这里相当于把一个数组变成多个文件）  Map：是一个对象变成多个对象
            List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(tempFileInfoList.stream()
                    .flatMap(fileInfo -> fileInfo.getFiles().stream())
                    //[1,2,3]
                    .collect(
                            Collectors.toMap(Meta.FileConfig.FileInfo::getOutputPath, o -> o, (e, r) -> r)
                    ).values());


            //使用新的group配置去覆盖旧的配置，根据我们之前的规则：先添加老的，再添加新的，所以新的文件一定在最后
            Meta.FileConfig.FileInfo newFileInfo = CollUtil.getLast(tempFileInfoList);
            //把刚合并后新的文件放到一个文件对象中（组）
            newFileInfo.setFiles(newFileInfoList);
            //获取这个组对象的groupKey
            String groupKey = entry.getKey();
            //放入合并后的对象Map
            groupKeyMergedFileInfo.put(groupKey,newFileInfo);
        }

        //3.创建新的文件配置列表（结果列表），先将合并后的分组添加到结果列表
        ArrayList<Meta.FileConfig.FileInfo> resultList = new ArrayList<>(groupKeyMergedFileInfo.values());

        //4.再将无分组的文件配置列表添加到结果列表(GroupKey为空)
        //Meta.FileConfig.FileInfo::getInputPath ：键值  o -> o： FileInfo的每条信息直接返回，不需要对对象进行任何处理，直接作为value
        //(e, r) -> r 表示新值与旧值冲突会选用已经存在的新值
        resultList.addAll( new ArrayList<>(fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isBlank(fileInfo.getGroupKey()))
                .collect(
                        Collectors.toMap(Meta.FileConfig.FileInfo::getOutputPath, o -> o, (e, r) -> r)
                ).values()));
        return resultList;
    }

    /**
     * 模型去重
     * @param modelsInfoList
     * @return
     */
    private static List<Meta.ModelConfig.ModelInfo> distinctModels (List<Meta.ModelConfig.ModelInfo> modelsInfoList){
        //Meta.ModelConfig.ModelInfo::getInputPath ：键值  o -> o： ModelInfo的每条信息直接返回，不需要对对象进行任何处理，直接作为value
        //(e, r) -> r 表示新值与旧值冲突会选用已经存在的新值
//        List<Meta.ModelConfig.ModelInfo> newModelsInfoList = new ArrayList<>(modelsInfoList.stream()
//                .collect(
//                        Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)
//                ).values());
//        return newModelsInfoList;


        //用了分治思想（一刀切）
        //1.将所有的模型配置（modelInfo）分为有分组和无分组
        //先处理有分组的模型 filter返回值为true 就保留这个模型
        //{"groupKey": "a",models:[1,2]},{"groupKey": "a",models:[2,3]},{"groupKey": "b",models:[4,5]}
        //{"groupKey": "a",models:[[1,2][2,3]]},{"groupKey": "b",models:[[4,5]]}
        //以组为单位划分
        Map<String, List<Meta.ModelConfig.ModelInfo>> groupKeyModelInfoListMap = modelsInfoList.stream()
                .filter(modelInfo -> StrUtil.isNotBlank(modelInfo.getGroupKey()))
                .collect(
                        Collectors.groupingBy(Meta.ModelConfig.ModelInfo::getGroupKey)
                );


        //新建合并后的对象Map
        Map<String, Meta.ModelConfig.ModelInfo> groupKeyMergedModelInfo = new HashMap<>();



        //2.对于有分组的模型配置，如果有相同的模型分组，同分组内的模型进行合并（merge），不同分组可同时保留
        //同组内配置合并
        //{"groupKey": "a",models:[[1,2][2,3]]} -> {"groupKey": "a",models:[[1,2,3]]}
        for (Map.Entry<String, List<Meta.ModelConfig.ModelInfo>> entry : groupKeyModelInfoListMap.entrySet()) {
            //{"groupKey": "a",models:[[1,2][2,3]]}
            List<Meta.ModelConfig.ModelInfo> tempModelInfoList = entry.getValue();
            //{"groupKey": "a",models:[[1,2,2,3]]} 将对象打平 flatMap：允许你将一个对象转化成多个对象（这里相当于把一个数组变成多个模型）  Map：是一个对象变成多个对象
            List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>(tempModelInfoList.stream()
                    .flatMap(modelInfo -> modelInfo.getModels().stream())
                    //[1,2,3]
                    .collect(
                            Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)
                    ).values());


            //使用新的group配置去覆盖旧的配置，根据我们之前的规则：先添加老的，再添加新的，所以新的模型一定在最后
            Meta.ModelConfig.ModelInfo newModelInfo = CollUtil.getLast(tempModelInfoList);
            //把刚合并后新的模型放到一个模型对象中（组）
            newModelInfo.setModels(newModelInfoList);
            //获取这个组对象的groupKey
            String groupKey = entry.getKey();
            //放入合并后的对象Map
            groupKeyMergedModelInfo.put(groupKey,newModelInfo);
        }

        //3.创建新的模型配置列表（结果列表），先将合并后的分组添加到结果列表
        ArrayList<Meta.ModelConfig.ModelInfo> resultList = new ArrayList<>(groupKeyMergedModelInfo.values());

        //4.再将无分组的模型配置列表添加到结果列表(GroupKey为空)
        //Meta.ModelConfig.ModelInfo::getInputPath ：键值  o -> o： ModelInfo的每条信息直接返回，不需要对对象进行任何处理，直接作为value
        //(e, r) -> r 表示新值与旧值冲突会选用已经存在的新值
        resultList.addAll( new ArrayList<>(modelsInfoList.stream()
                .filter(modelInfo -> StrUtil.isBlank(modelInfo.getGroupKey()))
                .collect(
                        Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)
                ).values()));
        return resultList;
    }

}
