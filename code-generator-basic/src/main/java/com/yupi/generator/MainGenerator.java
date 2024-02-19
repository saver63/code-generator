package com.yupi.generator;

import com.yupi.model.MainTemplateConfig;
import freemarker.template.TemplateException;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

public class MainGenerator {

    public static void doGenerate(Object model) throws TemplateException, IOException {

        //动态文件配置
        //绝对路径
        String inputRootPath = "E:\\work\\project\\code-generator\\code-generator-demo-projects\\acm-template-pro";
        String outputRootPath = "E:\\work\\project\\code-generator";

        //相对路径
        String inputPath;
        String outputPath;

        inputPath = new File(inputRootPath,"src/com/yupi/acm/MainTemplate.java.ftl").getAbsolutePath();
        outputPath = new File(outputRootPath,"src/com/yupi/acm/MainTemplate.java").getAbsolutePath();
        DynamicGenerator.doGenerator(inputPath,outputPath,model);



        //动态文件生成
        inputPath = new File(inputRootPath,".gitignore").getAbsolutePath();
        outputPath = new File(outputRootPath,".gitignore").getAbsolutePath();
        StaticGenerator.copyFileByHutool(inputPath,outputPath);

        //静态文件生成
        inputPath = new File(inputRootPath, "README.md").getAbsolutePath();
        outputPath = new File(outputRootPath,"README.md").getAbsolutePath();
        StaticGenerator.copyFileByHutool(inputPath,outputPath);


    }

    public static void main(String[] args) throws TemplateException, IOException {

        //数据模型
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setAuthor("zlz1");
        mainTemplateConfig.setOutputText("输出的结果：");
        mainTemplateConfig.setLoop(true);

        doGenerate(mainTemplateConfig);
    }
}
