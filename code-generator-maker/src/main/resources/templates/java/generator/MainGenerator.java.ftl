package ${basePackage}.generator;

import freemarker.template.TemplateException;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

/**
 * 核心生成器
 */
public class MainGenerator {

    public static void doGenerate(Object model) throws TemplateException, IOException {

        //动态文件配置
        //绝对路径
        String inputRootPath = "${fileConfig.inputRootPath}";
        String outputRootPath = "${fileConfig.outputRootPath}";

        //相对路径
        String inputPath;
        String outputPath;

<#list fileConfig.files as fileInfo>
        inputPath = new File(inputRootPath,"${fileInfo.inputPath}").getAbsolutePath();
        outputPath = new File(outputRootPath,"${fileInfo.outputPath}").getAbsolutePath();
    <#if fileInfo.generateType == "static">
        StaticGenerator.copyFileByHutool(inputPath,outputPath);
    <#else >
        DynamicGenerator.doGenerator(inputPath,outputPath,model);
    </#if>

</#list>
    }
}
