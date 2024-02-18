package com.yupi.cli.command;


import cn.hutool.core.bean.BeanUtil;
import com.yupi.generator.MainGenerator;
import com.yupi.model.MainTemplateConfig;
import lombok.Data;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

/**
 * 接收用户的输入的信息，发给上期写的静动结合的模板生成代码
 */
@Command(name = "generate", mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable {

    /**
     * 作者（填充值）
     */
    @Option(names = {"-a", "--author"}, description = "作者名称", arity = "0..1",interactive = true,echo = true)
    private String author ;

    /**
     * 输出信息
     */
    @Option(names = {"-o", "--outputText"}, description = "输出文本", arity = "0..1",interactive = true,echo = true)
    private String outputText;

    /**
     * 是否循环（开关）
     */
    @Option(names = {"-l", "--loop"}, description = "是否循环", arity = "0..1",interactive = true,echo = true)
    private boolean loop;

    @Override
    public Integer call() throws Exception {
        //把传递的参数给动态模板配置
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        //把对象的名称属性都复制到动态模板，这个copyProperties适用于传递过来的参数和要发送的对象都一模一样时使用,从this传过来，传递到mainTemplateConfig对象
        BeanUtil.copyProperties(this, mainTemplateConfig);
        MainGenerator.doGenerate(mainTemplateConfig);
        return 0;
    }
}
