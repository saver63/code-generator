package com.yupi.maker.generator.main;




public class MainGenerator extends GenerateTemplate {

    @Override
    protected String buildDist(String outputPath, String sourceCopyDestPath, String jarPath, String shellOutputFilePath) {
        System.out.println("不输出dist了");
        return "";
    }


}
