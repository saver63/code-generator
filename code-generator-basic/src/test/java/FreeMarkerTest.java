import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class FreeMarkerTest {

    @Test
    public void test() throws IOException, TemplateException {
        //new出Configeration对象，参数为Freemarker版本号
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

        //指定模板文件所在的路径
        configuration.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));

        //设置模板文件的字符集
        configuration.setDefaultEncoding("UTF-8");

        //解决本地化敏感 例如2,023->2023
        configuration.setNumberFormat("0.######");

        //创建模板对象，加载指定模板
        Template template = configuration.getTemplate("myweb.html.ftl");

        //数据模型
        Map<String, Object>dataModel = new HashMap<>();
        dataModel.put("currentYear", 2023);
        List<Map<String, Object>> menuItems = new ArrayList<>();
        Map<String, Object> menuItem1 = new HashMap<>();
        menuItem1.put("url","https://codefather.cn");
        menuItem1.put("label","编程导航");
        Map<String, Object> menuItem2 = new HashMap<>();
        menuItem2.put("url","https://laoyujianli.com");
        menuItem2.put("label","老鱼简历");
        menuItems.add(menuItem1);
        menuItems.add(menuItem2);
        dataModel.put("menuItems",menuItems);

        //指定生成文件
        Writer out = new FileWriter("myweb.html");

        //调用模板对象的生成文件
        template.process(dataModel,out);
        //生成后关闭
        out.close();
     }
}
