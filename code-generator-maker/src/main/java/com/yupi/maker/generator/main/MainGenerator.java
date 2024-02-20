package com.yupi.maker.generator.main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.yupi.maker.generator.JarGenerator;
import com.yupi.maker.generator.ScriptGenerator;
import com.yupi.maker.generator.file.DynamicFileGenerator;
import com.yupi.maker.meta.Meta;
import com.yupi.maker.meta.MetaManager;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class MainGenerator {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {


        Meta meta = MetaManager.getMetaObject();
        System.out.println(meta);

        //输出的根路径
        String projectPath = System.getProperty("user.dir");
        String outputPath = projectPath + File.separator + "generated";
        if (!FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }

        //从原始模板文件路径复制到生成的代码包中
        String sourceRootPath = meta.getFileConfig().getSourceRootPath();
        String sourceCopyDestPath = outputPath + File.separator + "./source";
        FileUtil.copy(sourceRootPath,sourceCopyDestPath,false);


        //读取 resources目录
        ClassPathResource classPathResource = new ClassPathResource("");
        String inputResourcePath = classPathResource.getAbsolutePath();

        //java包的基础路径
        //com.yupi
        String outputBasePackages = meta.getBasePackage();
        //com/yupi
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(outputBasePackages, "."));
        //完整路径 generated/src/main/java/com/yupi/xxx
        String outputBaseJavaPackagePath = outputPath + File.separator + "src/main/java/" + outputBasePackagePath;

        String inputFilePath;
        String outputFilePath;

        //templates下的model.DataModel
        inputFilePath = inputResourcePath + File.separator + "templates/java/model/DataModel.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/model/DataModel.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //templates下的cli.command.GenerateCommand
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/GenerateCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/GenerateCommand.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //templates下的cli.command.ConfigCommand
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/ConfigCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/ConfigCommand.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //templates下的cli.command.ListCommand
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/ListCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/ListCommand.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //templates下的cli.CommandExecutor
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/CommandExecutor.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/CommandExecutor.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //templates下的Main
        inputFilePath = inputResourcePath + File.separator + "templates/java/Main.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/Main.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //templates下的generator.DynamicGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/DynamicGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/DynamicGenerator.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //templates下的generator.MainGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/MainGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/MainGenerator.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //templates下的generator.StaticGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/StaticGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/StaticGenerator.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //templates下的pom.xml
        inputFilePath = inputResourcePath + File.separator + "templates/pom.xml.ftl";
        outputFilePath = outputPath + File.separator + "pom.xml";
        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //构建jar包
        JarGenerator.doGenerate(outputPath);

        //封装脚本
        //脚本输出路径
        String shellOutputFilePath = outputPath + File.separator + "generator";
        //脚本名称
        String jarName = String.format("%s-%s-jar-with-dependencies.jar",meta.getName(),meta.getVersion());
        //脚本生成路径
        String jarPath = "target/" + jarName;
        ScriptGenerator.doGenerate(shellOutputFilePath,jarPath);
    }
}
