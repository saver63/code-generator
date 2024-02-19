package com.yupi.maker.generator.file;

import com.yupi.maker.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class FileGenerator {

    public static void doGenerate(Object model) throws TemplateException, IOException {
        //1.静态文件生成
        //获取相对路径
        String projectPath = System.getProperty("user.dir");
        //输入路径
        String inputPath = projectPath+ File.separator+"code-generator-demo-projects"+ File.separator +"acm-template";
        //输出路径
        String outputPath = projectPath;
        //复制
        StaticFileGenerator.copyFileByHutool(inputPath,outputPath);

        //2.动态文件生成
        String dynamicInpuPath = projectPath +File.separator+ "code-generator-maker" + File.separator+"src/main/resources/templates/MainTemplate.java.ftl";
        String dynamicOutputPath = projectPath + File.separator+"acm-template/src/com/yupi/acm/MainTemplate.java";
        DynamicFileGenerator.doGenerator(dynamicInpuPath,dynamicOutputPath,model);

    }

    public static void main(String[] args) throws TemplateException, IOException {

        //数据模型
        DataModel dataModel = new DataModel();
        dataModel.setAuthor("zlz1");
        dataModel.setOutputText("输出的结果：");
        dataModel.setLoop(true);

        doGenerate(dataModel);
    }
}
