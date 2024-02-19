package ${basePackage}.cli.command;


import cn.hutool.core.bean.BeanUtil;
import ${basePackage}.model.DataModel;
import lombok.Data;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;


@Command(name = "generate", mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable {

<#list modelConfig.models as modelInfo>

    @Option(names = {<#if modelInfo.abbr??>"-${modelInfo.abbr}",</#if> "--${modelInfo.fieldName}"},<#if modelInfo.description??> description = "${modelInfo.description}",</#if> arity = "0..1",interactive = true,echo = true)
    private ${modelInfo.type} ${modelInfo.fieldName} <#if modelInfo.defaultValue??> = ${modelInfo.defaultValue?c}</#if> ;
</#list >

    @Override
    public Integer call() throws Exception {

        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
        return 0;
    }
}
