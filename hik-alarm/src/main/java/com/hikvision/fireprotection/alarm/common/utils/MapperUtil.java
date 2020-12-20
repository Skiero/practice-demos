package com.hikvision.fireprotection.alarm.common.utils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 映射工具类
 *
 * @author wangjinchang5
 * @date 2020/12/18 22:00
 * @since 1.0.100
 */
public class MapperUtil {
    private MapperUtil() {
    }

    public final static MapperFactory mapperFactory;
    public final static MapperFacade mapperFacade;

    static {
        mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFacade = mapperFactory.getMapperFacade();
    }

    public static <T, S> T convertToObject(Class<T> targetClazz, S source) {
        if (source == null) {
            return null;
        }
        mapperFactory.classMap(targetClazz, source.getClass())
                .byDefault()
                .register();

        return mapperFactory.getMapperFacade().map(source, targetClazz);

    }

    public static <T, S> List<T> convertToList(Class<T> targetClazz, List<S> sourceList) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        mapperFactory.classMap(targetClazz, sourceList.get(0).getClass())
                .byDefault()
                .register();

        return mapperFactory.getMapperFacade().mapAsList(sourceList, targetClazz);
    }

    public static Map<String, String> objectToMap(Object object) {
        Map<String, String> map = new HashMap<>();
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue;
            try {
                fieldValue = field.get(object);
            } catch (IllegalAccessException e) {
                fieldValue = null;
            }
            map.put(fieldName, fieldValue == null ? null : fieldValue.toString());
        }
        return map;
    }
}
