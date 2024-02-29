package com.yupi.maker.generator;

import java.io.*;

public class JarGenerator {

    public static void doGenerate(String projectDir) throws IOException, InterruptedException {
        //调用 Process 类执行Maven 打包命令
        //先执行clean清除缓存，再打包
        // -DiskTests=true:跳过所有的测试用例，打包更快
        //不同操作系统的命令是不一样的

        String winMavenCommand = "mvn.cmd clean package -DiskTests=true";
        String otherMavenCommand = "mvn clean package -DiskTests=true";
        String mavenCommand = winMavenCommand;

        //为命令选择执行的路径，在项目所在的路径打jar包
        //maven要按空格拆（命令本身也是按空格来区分参数）
        ProcessBuilder processBuilder = new ProcessBuilder(mavenCommand.split(" "));
        processBuilder.directory(new File(projectDir));



        //相当于打开终端执行命令行工具
        Process process = processBuilder.start();

        //读取命令的输出
        //读取输入流
        InputStream inputStream = process.getInputStream();
        //根据输入流去读取缓冲区读取器
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        //利用for循环读取缓冲区的每一行
        String line;
        while ((line = bufferedReader.readLine())!= null){
            System.out.println(line);
        }

        //等待执行器执行完成
        int exitCode = process.waitFor();
        System.out.println("命令执行结束，退出码："+ exitCode);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        doGenerate("E:\\work\\project\\code-generator\\code-generator-basic");
    }
}
