package com.hikvision.fireprotection.alarm.common.utils;

import com.hikvision.fireprotection.alarm.model.dto.AlarmEventDTO;
import com.hikvision.fireprotection.alarm.model.table.AlarmDetailTable;
import com.hikvision.fireprotection.alarm.model.vo.AlarmDetailVO;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.util.List;

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

    public static AlarmDetailTable dtoConvertToTable(AlarmEventDTO alarmEventDTO) {
        mapperFactory.classMap(AlarmDetailTable.class, AlarmEventDTO.class)
                .field("id", "alarmId")
                .byDefault()
                .register();
        AlarmDetailTable table = mapperFactory.getMapperFacade().map(alarmEventDTO, AlarmDetailTable.class);
        table.setNotifyStatus('0');
        return table;
    }

    public static List<AlarmDetailVO> tableConvertToVOList(List<AlarmDetailTable> alarmDetailTables) {
        mapperFactory.classMap(AlarmDetailVO.class, AlarmDetailTable.class)
                .byDefault()
                .register();
        List<AlarmDetailVO> alarmDetailVOList = mapperFactory.getMapperFacade()
                .mapAsList(alarmDetailTables, AlarmDetailVO.class);

        return alarmDetailVOList;
    }
}
