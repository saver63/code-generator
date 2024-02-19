package com.yupi.maker.meta;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

import java.util.Stack;

/**
 * 业务层:读取meta文件
 */
public class MetaManager {

    //单例模式

    //volatile关键字是为了确保多线程情况下的内存可见性（多个线程设置一个值的时候，不是共享内存，而是有每个线程各自先更新本地内存，经过一段时间后才会去合并共享）
    //如果没有这个关键字，还是有可能会出现重复初始化
    private static volatile Meta meta;

    public static Meta getMetaObject(){
        //双检索单例模式
        //不加锁直接这样写，当有多个线程同时进来时，此时的meta都为null，就有问题
        if (meta == null){
            //静态对象：基于整个类加锁
            synchronized (MetaManager.class){
                if (meta == null){
                    meta = initMeta();
                }
            }
        }
        return meta;
    }

    private static Meta initMeta(){
        String metaJson = ResourceUtil.readUtf8Str("meta.json");
        Meta newMeta = JSONUtil.toBean(metaJson, Meta.class);
        //todo 校验配置文件的合法性，处理默认值
        return newMeta;
    }

}
