package com.yupi.maker.generator.main;


import com.yupi.maker.model.DataModel;
import freemarker.template.TemplateException;

import java.io.IOException;

public class MainGenerator extends GenerateTemplate {

    @Override
    protected void buildDist(String outputPath, String sourceCopyDestPath, String jarPath, String shellOutputFilePath) {
        System.out.println("不输出dist了");
    }


}
