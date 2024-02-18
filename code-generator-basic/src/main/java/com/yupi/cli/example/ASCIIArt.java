package com.yupi.cli.example;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;



//2.打上Command注解，表示是Cli的程序，版本号和名称随便起
//mixinStandardHelpOptions = true 的作用：给命令行工具自动提供帮助手册的能力
@Command(name = "ASCIIArt", version = "ASCIIArt 1.0", mixinStandardHelpOptions = true)
//1.首先实现runnable接口，或者Callable
public class ASCIIArt implements Runnable {

    //打上Option注解的作用：声明fontSize这个变量是通过用户在命令行输入来接收值的（加-的方式输入）
    @Option(names = {"-s", "--font-size"}, description = "Font size")
    int fontSize = 19;

    //Parameters的作用：声明Parameters这个变量是通过用户在命令行输入来接收值的（不加-的方式输入）
    @Parameters(paramLabel = "<word>", defaultValue = "Hello, picocli",
            description = "Words to be translated into ASCII art.")
    //默认值
    private String[] words = {"Hello,", "picocli"};

    //用户按回车后出现的界面（用户确认命令后执行的业务逻辑）
    @Override
    public void run() {
        //自己实现业务逻辑
        System.out.println("fontSize = " + fontSize);
        System.out.println("words=" + String.join(",", words));
    }

    public static void main(String[] args) {
        //新建一个框架提供的CommandLine类的对象，传递的参数是ASCIIArt（Command类的对象和args用户的输入）和通过execute方法将用户的输入放到ASCIIArt对象中
        int exitCode = new CommandLine(new ASCIIArt()).execute(args);
        //通过exitCode码来判断程序是否正常执行
        System.exit(exitCode);
    }
}