package com.yupi.cli.example;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "main", mixinStandardHelpOptions = true)
public class SubCommandExample implements Runnable{



    @Override
    public void run() {
        System.out.println("执行主命令");
    }

    @Command(name = "add", description = "增加",mixinStandardHelpOptions =true)
    static class AddCommand implements Runnable{

        @Override
        public void run() {
            System.out.println("执行增加命令");
        }

    }
    @Command(name = "delete", description = "删除",mixinStandardHelpOptions =true)
    static class DeleteCommand implements Runnable{

        @Override
        public void run() {
            System.out.println("执行删除命令");
        }

    }
    @Command(name = "query", description = "查询",mixinStandardHelpOptions =true)
    static class QueryCommand implements Runnable{

        @Override
        public void run() {
            System.out.println("执行查询命令");
        }

    }

    public static void main(String[] args) {
        //执行主命令
//        String[] myArgs = new String[]{};
        //查看主命令的帮助手册
//        String[] myArgs = new String[]{"--help"};
        //执行增加命令
//        String[] myArgs = new String[]{"add"};
        //查看增加命令的帮助手册
        String[] myArgs = new String[]{"add","--help"};

        //执行删除命令
//        String[] myArgs = new String[]{"delete"};
        //查看增加命令的帮助手册
//        String[] myArgs = new String[]{"delete","--help"};
        //执行查询命令
//        String[] myArgs = new String[]{"query"};
        //查看增加命令的帮助手册
//        String[] myArgs = new String[]{"query","--help"};
        //执行不存在的命令会报错,而且如果命令没输全，它会报错并且提示补充完整的命令


        //先创建副命令，再绑定子命令
        int exitCode = new CommandLine(new SubCommandExample())
                .addSubcommand(new AddCommand())
                .addSubcommand(new DeleteCommand())
                .addSubcommand(new QueryCommand())
                .execute(myArgs);
        System.exit(exitCode);
    }
}
