package com.hikvision.fireprotection.alarm.common.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * MybatisPlusConfiguration
 *
 * @author wangjinchang5
 * @date 2020/12/19 11:00
 * @since 1.0.100
 */
@Configuration
public class MybatisPlusConfiguration {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        paginationInterceptor.setLimit(1000L);
        paginationInterceptor.setDbType(DbType.MYSQL);
        return paginationInterceptor;
    }

    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                Object createTime = getFieldValByName("createTime", metaObject);
                if (createTime == null) {
                    setFieldValByName("createTime", new Date(), metaObject);
                }
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                Object updateTime = getFieldValByName("updateTime", metaObject);
                if (updateTime == null) {
                    setFieldValByName("updateTime", new Date(), metaObject);
                }
            }
        };
    }
}
