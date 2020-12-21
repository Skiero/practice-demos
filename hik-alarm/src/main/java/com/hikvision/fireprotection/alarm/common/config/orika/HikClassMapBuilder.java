package com.hikvision.fireprotection.alarm.common.config.orika;

import ma.glasnost.orika.DefaultFieldMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.ClassMapBuilderFactory;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.property.PropertyResolverStrategy;

/**
 * HikClassMapBuilder
 *
 * @author wangjinchang5
 * @date 2020/12/21 10:00
 * @since 1.0.100
 */
public class HikClassMapBuilder<A, B> extends ClassMapBuilder<A, B> {

    public static class Factory extends ClassMapBuilderFactory {
        @Override
        protected <A, B> ClassMapBuilder<A, B> newClassMapBuilder(Type<A> aType, Type<B> bType,
                                                                  MapperFactory mapperFactory,
                                                                  PropertyResolverStrategy propertyResolver,
                                                                  DefaultFieldMapper[] defaults) {

            return new HikClassMapBuilder<>(aType, bType, mapperFactory, propertyResolver, defaults);
        }
    }

    private final MapperFactory innerMapperFactory;

    private HikClassMapBuilder(Type<A> aType, Type<B> bType, MapperFactory mapperFactory,
                               PropertyResolverStrategy propertyResolver, DefaultFieldMapper[] defaults) {
        super(aType, bType, mapperFactory, propertyResolver, defaults);
        innerMapperFactory = mapperFactory;
    }

    @Override
    public void register() {
        if (this.innerMapperFactory == null) {
            throw new IllegalStateException("MapperFactory is null.");
        }
        if (!innerMapperFactory.existsRegisteredMapper(this.getAType(), this.getBType(), false)) {
            super.register();
        }
    }
}
