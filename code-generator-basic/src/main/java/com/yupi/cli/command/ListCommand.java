package com.yupi.cli.command;



import cn.hutool.core.io.FileUtil;
import picocli.CommandLine.Command;

import java.io.File;
import java.util.List;

@Command(name = "list", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable{



    @Override
    public  void run() {
        //获取basic路径和整个项目的根路径
        String parentFile = System.getProperty("user.dir");
        String projectPath = parentFile+ File.separator+"code-generator-basic";
        System.out.println(projectPath);
        //输入路径
        String inputPath = new File(parentFile,"code-generator-demo-projects/acm-template").getAbsolutePath();
        //遍历获取所有的文件
        List<File> files = FileUtil.loopFiles(inputPath);

        for (File file: files){
            System.out.println(file);
        }
    }


}
