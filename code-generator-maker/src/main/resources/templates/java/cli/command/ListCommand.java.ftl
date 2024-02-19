package ${basePackage}.cli.command;



import cn.hutool.core.io.FileUtil;
import picocli.CommandLine.Command;

import java.io.File;
import java.util.List;

@Command(name = "list", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable{



    @Override
    public  void run() {

        //输入路径
        String inputPath = "${fileConfig.inputRootPath}";
        //遍历获取所有的文件
        List<File> files = FileUtil.loopFiles(inputPath);

        for (File file: files){
            System.out.println(file);
        }
    }


}
