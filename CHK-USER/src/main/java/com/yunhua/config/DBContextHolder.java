package com.yunhua.config;

import lombok.extern.slf4j.Slf4j;

//线程级别的数据源设置
@Slf4j
public class DBContextHolder {

    private static final ThreadLocal<DBTypeEnum> contextHolder = new ThreadLocal<>();

    public static void set(DBTypeEnum dbType){
        contextHolder.set(dbType);
    }

    public static DBTypeEnum get(){
        return contextHolder.get();
    }

    public static void master(){
        set(DBTypeEnum.MASTER);
        log.info("=============================切换到master===========================");
    }

    public static void slave(){
        set(DBTypeEnum.SLAVE);
        log.info("=============================切换到slave============================");
    }
}
