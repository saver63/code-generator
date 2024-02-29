package com.yupi.web;

import com.yupi.web.common.ErrorCode;
import com.yupi.web.exception.BusinessException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

//public class MyTest {
//    public static void main(String[] args) {
//        //构造命令
//        File scriptDir = new File( "E:\\work\\project\\code-generator\\code-generator-web-backend\\.temp\\use\\111\\acm-template-pro-generator-dist\\generator");
//        //注意，如果是mac/linux系统,要用"./generator"
//        String[] commons1 = new String[]{".\\generator","json-generator","--file="+dataModelFilePath};
//
//        //为命令选择执行的路径，在项目所在的路径打jar包
//        //maven要按空格拆（命令本身也是按空格来区分参数）
//        ProcessBuilder processBuilder = new ProcessBuilder(commons);
//        processBuilder.directory(scriptDir);
//
//        //相当于打开终端执行命令行工具
//        Process process = processBuilder.start();
//
//        try{
//            //读取命令的输出
//            //读取输入流
//            InputStream inputStream = process.getInputStream();
//            //根据输入流去读取缓冲区读取器
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            //利用for循环读取缓冲区的每一行
//            String line;
//            while ((line = bufferedReader.readLine())!= null){
//                System.out.println(line);
//            }
//
//            //等待执行器执行完成
//            int exitCode = process.waitFor();
//            System.out.println("命令执行结束，退出码："+ exitCode);
//        } catch (Exception e){
//            e.printStackTrace();
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"执行生成器脚本错误");
//        }
//    }
//}
