package com.hikvision.fireprotection.alarm.common.config.orika;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OrikaConfiguration
 *
 * @author wangjinchang5
 * @date 2020/12/21 10:00
 * @since 1.0.100
 */
@Configuration
public class OrikaConfiguration {
    public static MapperFactory mapperFactory;

    @Autowired
    public void setMapperFactory(MapperFactory mapperFactory) {
        OrikaConfiguration.mapperFactory = mapperFactory;
    }

    @Bean
    public MapperFactoryBean mapperFactory() {
        return new MapperFactoryBean();
    }

    @Bean
    public MapperFacade mapperFacade(MapperFactory mapperFactory) {
        return mapperFactory.getMapperFacade();
    }
}
