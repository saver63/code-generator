package com.yupi.cli.example;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import java.util.concurrent.Callable;

/**
 * 引导用户交互式输入Demo
 */
//subcommands = {ASCIIArt.class}:把其它开发好的命令直接作为子命令
@CommandLine.Command(subcommands = {ASCIIArt.class})
public class Login implements Callable<Integer> {
    @Option(names = {"-u", "--user"}, description = "User name")
    String user;

    //interactive = true:给这个选项添加用户交互能力
    @Option(names = {"-p", "--password"}, description = "Passphrase", arity = "0..1",interactive = true,echo = true,prompt = "请输入密码: ")
    String password;

    //interactive = true:给这个选项添加用户交互能力
    @Option(names = {"-cp", "--checkPassword"}, description = "Check Password",arity = "0..1", interactive = true,echo = true,prompt = "请再次输入密码: ")
    String checkPassword;


    //所有参数输入完后才会调用call方法
    public Integer call() throws Exception{
        System.out.println("passwoprd = "+password);
        System.out.println("passwoprd = "+checkPassword);
        return 0;
    }

    public static void main(String[] args) {
        args = new String[]{"-u" , "user123","-p"};
        new CommandLine(new Login()).execute(args);
    }


}