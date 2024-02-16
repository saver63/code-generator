package com.yupi.generator;

import cn.hutool.core.io.FileUtil;

import java.io.File;

/**
 * 静态文件生成器
 */
public class StaticGenerator {

    public static void main(String[] args) {
        //获取相对路径
        String projectPath = System.getProperty("user.dir");
        //输入路径
        String inputPath = projectPath+File.separator+"code-generator-demo-projects"+ File.separator +"acm-template";
        //输出路径
        String outputPath = projectPath;
        copyFileByHutool(inputPath,outputPath);
    }

    /**
     * 拷贝文件
     * @param inputPath 输入路径
     * @param outputPath 输出路径
     */
    public static void copyFileByHutool(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, true);
    }
}
