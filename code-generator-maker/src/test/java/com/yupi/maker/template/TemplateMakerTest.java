package com.yupi.maker.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.maker.meta.Meta;
import com.yupi.maker.template.model.TemplateMakerConfig;
import com.yupi.maker.template.model.TemplateMakerFileConfig;
import com.yupi.maker.template.model.TemplateMakerModelConfig;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class TemplateMakerTest  {


    @Test
    public void testMakeTemplateBug1() {


        //1.项目的基本信息
        Meta meta = new Meta();
        meta.setName("acm-template-generator");
        meta.setDescription("ACM 示例模板生成器");

        //指定原始项目路径
        String projectPath = System.getProperty("user.dir");
        //要挖坑的项目根目录
        String originProjectPath = FileUtil.getAbsolutePath(new File(projectPath).getParentFile())
                + File.separator + "code-generator-demo-projects/springboot-init";
        String fileInputPath1 = "src/main/java/com/yupi/springbootinit/common";


        // 数据库-模型参数配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("url");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3306/my_db");
        modelInfoConfig1.setReplaceText("jdbc:mysql://localhost:3306/my_db");

        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1);
        templateMakerModelConfig.setModels(modelInfoConfigList);

        //文件过滤配置
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        //传入文件1的路径
        fileInfoConfig1.setPath(fileInputPath1);
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig1));


        long id = TemplateMaker.makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, null,1L);
        System.out.println(id);
    }


    @Test
    public void testMakeTemplateBug2() {


        //1.项目的基本信息
        Meta meta = new Meta();
        meta.setName("acm-template-generator");
        meta.setDescription("ACM 示例模板生成器");

        //指定原始项目路径
        String projectPath = System.getProperty("user.dir");
        //要挖坑的项目根目录
        String originProjectPath = FileUtil.getAbsolutePath(new File(projectPath).getParentFile())
                + File.separator + "code-generator-demo-projects/springboot-init";
        String fileInputPath1 = "./";


        // 数据库-模型参数配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("className");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setReplaceText("BaseResponse");

        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1);
        templateMakerModelConfig.setModels(modelInfoConfigList);

        //文件过滤配置
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        //传入文件1的路径
        fileInfoConfig1.setPath(fileInputPath1);
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig1));


        long id = TemplateMaker.makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, null,1L);
        System.out.println(id);
    }


    @Test
    public void testMakerTemplateWithJSON(){
        String congfigStr = ResourceUtil.readUtf8Str("templateMaker.json");
        TemplateMakerConfig templateMakerConfig = JSONUtil.toBean(congfigStr, TemplateMakerConfig.class);
        long id = TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);
    }


    /**
     * 制作spirng Boot 模板
     */
    @Test
    public void makeSpringBootTemplate(){
        String rootPath = "examples/springboot-init/";
        String congfigStr = ResourceUtil.readUtf8Str(rootPath+"templateMaker.json");
        TemplateMakerConfig templateMakerConfig = JSONUtil.toBean(congfigStr, TemplateMakerConfig.class);
        long id = TemplateMaker.makeTemplate(templateMakerConfig);

        congfigStr = ResourceUtil.readUtf8Str(rootPath+"templateMaker1.json");
        templateMakerConfig = JSONUtil.toBean(congfigStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        congfigStr = ResourceUtil.readUtf8Str(rootPath+"templateMaker2.json");
        templateMakerConfig = JSONUtil.toBean(congfigStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        congfigStr = ResourceUtil.readUtf8Str(rootPath+"templateMaker3.json");
        templateMakerConfig = JSONUtil.toBean(congfigStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        congfigStr = ResourceUtil.readUtf8Str(rootPath+"templateMaker4.json");
        templateMakerConfig = JSONUtil.toBean(congfigStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        congfigStr = ResourceUtil.readUtf8Str(rootPath+"templateMaker5.json");
        templateMakerConfig = JSONUtil.toBean(congfigStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        congfigStr = ResourceUtil.readUtf8Str(rootPath+"templateMaker6.json");
        templateMakerConfig = JSONUtil.toBean(congfigStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        congfigStr = ResourceUtil.readUtf8Str(rootPath+"templateMaker7.json");
        templateMakerConfig = JSONUtil.toBean(congfigStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        congfigStr = ResourceUtil.readUtf8Str(rootPath+"templateMaker8.json");
        templateMakerConfig = JSONUtil.toBean(congfigStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        System.out.println(id);
    }



}