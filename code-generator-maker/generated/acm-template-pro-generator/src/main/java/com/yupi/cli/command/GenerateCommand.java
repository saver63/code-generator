package com.yupi.cli.command;


import cn.hutool.core.bean.BeanUtil;
import com.yupi.generator.MainGenerator;
import com.yupi.model.DataModel;
import lombok.Data;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;


@Command(name = "generate", mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable {


    @Option(names = {"-l", "--loop"}, description = "是否生成循环", arity = "0..1",interactive = true,echo = true)
    private boolean loop  = false ;

    @Option(names = {"-a", "--author"}, description = "作者注释", arity = "0..1",interactive = true,echo = true)
    private String author  = "yupi" ;

    @Option(names = {"-o", "--outputText"}, description = "输出信息", arity = "0..1",interactive = true,echo = true)
    private String outputText  = "sum = " ;

    @Override
    public Integer call() throws Exception {

        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
        MainGenerator.doGenerate(dataModel);
        return 0;
    }
}
