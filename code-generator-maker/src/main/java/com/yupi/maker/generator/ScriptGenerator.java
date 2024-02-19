package com.yupi.maker.generator;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public class ScriptGenerator {

    /**
     *
     * @param outputPath 脚本文件的输出路径（调用方指定，根据配置文件输出）
     * @param jarPath jar包路径，没办法写死，根据外层配置文件读取
     */
    public static void doGenerate(String outputPath ,String jarPath ){
        //Linux脚本
        StringBuilder sb =new StringBuilder();
        //#!/bin/bash
        //java -jar target/code-generator-basic-1.0-SNAPSHOT-jar-with-dependencies.jar "$@"
        //拼接字符串
        sb.append("#!/bin/bash").append("\n");
        sb.append(String.format("java -jar %s \"$@\"", jarPath)).append("\n");
        //写入到输出路径的文件
        FileUtil.writeBytes(sb.toString().getBytes(StandardCharsets.UTF_8),outputPath);
        //linux默认是没有可执行权限，所有需要添加可执行权限
        //用try-cache而不直接抛出去是为了防止windows执行不报错
        try {
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rmxrmxrmx");
            Files.setPosixFilePermissions(Paths.get(outputPath),permissions);
        } catch (Exception e) {

        }


        //windows脚本
        sb = new StringBuilder();
        // @echo off
        //java -jar target/code-generator-basic-1.0-SNAPSHOT-jar-with-dependencies.jar %*
        sb.append("@echo off").append("\n");
        //%表示转义
        sb.append(String.format("java -jar %s %%*", jarPath)).append("\n");
        FileUtil.writeBytes(sb.toString().getBytes(StandardCharsets.UTF_8),outputPath  +".bat");
    }
}
