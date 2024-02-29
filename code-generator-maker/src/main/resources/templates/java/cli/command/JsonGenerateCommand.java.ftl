package ${basePackage}.cli.command;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import ${basePackage}.generator.MainGenerator;
import ${basePackage}.model.DataModel;
import lombok.Data;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;


/**
 * 读取JSON文件生成代码
 */
@Command(name = "json-generate", description = "读取JSON文件生成代码",mixinStandardHelpOptions = true)
@Data
public class JsonGenerateCommand implements Callable {

    @Option(names = {"-f", "--file"}, description = "Json文件路径", arity = "0..1",interactive = true,echo = true)
    private String filePath  ;

    @Override
    public Integer call() throws Exception {

        //读取json文件转换为数据模型
        String jsonStr = FileUtil.readUtf8String(filePath);
        DataModel dataModel = JSONUtil.toBean(jsonStr,DataModel.class);
        MainGenerator.doGenerate(dataModel);
        return 0;
    }
}
