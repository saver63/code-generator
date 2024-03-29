package com.yupi.generator;

import com.yupi.model.MainTemplateConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态文件生成器
 */
public class DynamicGenerator {
    public static void main(String[] args) throws IOException, TemplateException {

        String projectPath = System.getProperty("user.dir")+ File.separator+"code-generator-basic";
        String inpuPath = projectPath + File.separator+"src/main/resources/templates/MainTemplate.java.ftl";
        String outputPath = projectPath + File.separator+"MainTemplate.java.ftl";

        //数据模型
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setAuthor("zlz1");
        mainTemplateConfig.setOutputText("输出的结果：");
        mainTemplateConfig.setLoop(true);
        doGenerator(inpuPath,outputPath,mainTemplateConfig);
    }

    /**
     * 生成文件
     *
     * @param inputPath 模板文件输入路径
     * @param outputPath 输出路径
     * @param model 数据模型
     * @throws IOException
     * @throws TemplateException
     */
    public static void doGenerator(String inputPath, String outputPath, Object model)throws IOException, TemplateException{
        //new出Configeration对象，参数为Freemarker版本号
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

        //获取实际模板所在文件的父目录（这样就不用写死了）
        File templateDir = new File(inputPath).getParentFile();
        configuration.setDirectoryForTemplateLoading(templateDir);


        //设置模板文件的字符集
        configuration.setDefaultEncoding("UTF-8");

        //解决本地化敏感 例如2,023->2023
        configuration.setNumberFormat("0.######");

        //获取模板名称，创建模板对象，加载指定模板
        String templateName = new File(inputPath).getName();
        Template template = configuration.getTemplate(templateName);





        //指定生成文件
        Writer out = new FileWriter(outputPath);

        //调用模板对象的生成文件
        template.process(model,out);
        //生成后关闭
        out.close();
    }
}
