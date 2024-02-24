package com.yupi.maker.template;

import cn.hutool.core.util.StrUtil;
import com.yupi.maker.meta.Meta;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 模板制作工具类
 */
public class TemplateMakerUtils {

    /**
     * 从未分组的文件中移除组内的同名文件
     * @param fileInfoList
     * @return
     */
    public static List<Meta.FileConfig.FileInfo> removeGroupFilesFromRoot(List<Meta.FileConfig.FileInfo> fileInfoList){

        //先获取到所有分组
        //根据是否有groupKey来判断它是否是分组
        List<Meta.FileConfig.FileInfo> groupFileInfoList = fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(Collectors.toList());

        //获取所有分组内的文件列表
        //分组1：3个文件 分组2：3个文件 拼起来总共6个文件
        List<Meta.FileConfig.FileInfo> goupInnerFileInfoList = groupFileInfoList.stream()
                .flatMap(fileInfo -> fileInfo.getFiles().stream())
                .collect(Collectors.toList());


        //获取所有分组内文件的输入路径来去重
        //把得到的所有文件放入集合中，因为集合方便去重
        Set<String> fileInputPathSet = goupInnerFileInfoList.stream()
                .map(Meta.FileConfig.FileInfo::getInputPath)
                .collect(Collectors.toSet());

        //移除所有在集合内的外层文件
        //如果不在集合才保留
        // 分组是没有输入路径的，所以一定为空，那么它一定不再集合内，一定是true，也就是说如果不包含在集合中的文件那么这个文件一定在分组中
        return fileInfoList.stream()
                .filter(fileInfo -> !fileInputPathSet.contains(fileInfo.getInputPath()))
                .collect(Collectors.toList());
    }
}
