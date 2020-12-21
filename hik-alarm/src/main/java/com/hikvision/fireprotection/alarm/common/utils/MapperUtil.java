package com.hikvision.fireprotection.alarm.common.utils;

import com.hikvision.fireprotection.alarm.common.config.orika.OrikaConfiguration;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

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

    private final static ArrayList<String> EMPTY_LIST = new ArrayList<>(0);

    private final static HashMap<String, String> EMPTY_MAP = new HashMap<>(0);

    public static Map<String, String> mapperToMap(Object object) {
        if (object == null) {
            return EMPTY_MAP;
        }

        Field[] declaredFields = object.getClass().getDeclaredFields();
        Map<String, String> map = new HashMap<>(declaredFields.length);
        for (Field field : declaredFields) {
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

    public static <T, S> T mapperToObject(Class<T> targetClazz, S source) {
        return mapperToObject(targetClazz, source, EMPTY_MAP, EMPTY_LIST);
    }

    public static <T, S> T mapperToObject(Class<T> targetClazz, S source, Map<String, String> mapperFields) {
        return mapperToObject(targetClazz, source, mapperFields, EMPTY_LIST);
    }

    public static <T, S> T mapperToObject(Class<T> targetClazz, S source, List<String> excludeFields) {
        return mapperToObject(targetClazz, source, EMPTY_MAP, excludeFields);
    }

    public static <T, S> T mapperToObject(Class<T> targetClazz, S source,
                                          Map<String, String> mapperFields, List<String> excludeFields) {
        if (source == null) {
            return null;
        }

        MapperFactory mapperFactory = OrikaConfiguration.mapperFactory;
        ClassMapBuilder<?, T> classMapBuilder = mapperFactory.classMap(source.getClass(), targetClazz);

        for (Map.Entry<String, String> entry : mapperFields.entrySet()) {
            classMapBuilder.field(entry.getKey(), entry.getValue());
        }

        excludeFields.forEach(classMapBuilder::exclude);

        classMapBuilder.byDefault().register();

        return mapperFactory.getMapperFacade().map(source, targetClazz);
    }

    public static <T, S> List<T> mapperToList(Class<T> targetClazz, List<S> sourceList) {
        return mapperToList(targetClazz, sourceList, EMPTY_MAP, EMPTY_LIST);
    }

    public static <T, S> List<T> mapperToList(Class<T> targetClazz, List<S> sourceList,
                                              Map<String, String> mapperFields) {
        return mapperToList(targetClazz, sourceList, mapperFields, EMPTY_LIST);
    }

    public static <T, S> List<T> mapperToList(Class<T> targetClazz, List<S> sourceList,
                                              List<String> excludeFields) {
        return mapperToList(targetClazz, sourceList, EMPTY_MAP, excludeFields);
    }

    public static <T, S> List<T> mapperToList(Class<T> targetClazz, List<S> sourceList,
                                              Map<String, String> mapperFields, List<String> excludeFields) {
        if (sourceList == null || sourceList.isEmpty()) {
            return new ArrayList<>(0);
        }

        MapperFactory mapperFactory = OrikaConfiguration.mapperFactory;
        ClassMapBuilder<?, T> classMapBuilder = mapperFactory.classMap(sourceList.get(0).getClass(), targetClazz);

        for (Map.Entry<String, String> entry : mapperFields.entrySet()) {
            classMapBuilder.field(entry.getKey(), entry.getValue());
        }

        excludeFields.forEach(classMapBuilder::exclude);

        classMapBuilder.byDefault().register();

        return mapperFactory.getMapperFacade().mapAsList(sourceList, targetClazz);
    }
}
