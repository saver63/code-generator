package ${basePackage}.cli.command;

import cn.hutool.core.util.ReflectUtil;
import ${basePackage}.model.DataModel;
import picocli.CommandLine.Command;

import java.lang.reflect.Field;

@Command(name = "config", mixinStandardHelpOptions = true)
public class ConfigCommand implements Runnable{


    @Override
    public void run() {
        Field[] files = ReflectUtil.getFields(DataModel.class);
        for (Field filed:files){
            System.out.println("字段类型"+filed.getType());
            System.out.println("字段名称"+filed.getName());
        }
    }
}
