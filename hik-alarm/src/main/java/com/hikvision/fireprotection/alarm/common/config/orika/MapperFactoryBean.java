package com.hikvision.fireprotection.alarm.common.config.orika;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * MapperFactoryBean
 *
 * @author wangjinchang5
 * @date 2020/12/21 10:00
 * @since 1.0.100
 */
public class MapperFactoryBean implements FactoryBean<MapperFactory>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public MapperFactory getObject() throws Exception {
        DefaultMapperFactory build = new DefaultMapperFactory.Builder()
                .classMapBuilderFactory(new HikClassMapBuilder.Factory())
                .build();

        for (CustomConverter<?, ?> converter : applicationContext.getBeansOfType(CustomConverter.class).values()) {
            build.getConverterFactory().registerConverter(converter);
        }

        for (Mapper<?, ?> mapper : applicationContext.getBeansOfType(Mapper.class).values()) {
            build.registerMapper(mapper);
        }

        for (ClassMapBuilder<?, ?> mapper : applicationContext.getBeansOfType(ClassMapBuilder.class).values()) {
            build.registerClassMap(mapper);
        }

        return build;
    }

    @Override
    public Class<?> getObjectType() {
        return MapperFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
