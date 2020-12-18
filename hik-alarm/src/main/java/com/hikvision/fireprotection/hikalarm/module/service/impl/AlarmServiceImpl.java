package com.hikvision.fireprotection.hikalarm.module.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.hikvision.fireprotection.hikalarm.model.request.AlarmPageQuery;
import com.hikvision.fireprotection.hikalarm.model.table.AlarmDetailTable;
import com.hikvision.fireprotection.hikalarm.model.vo.AlarmDetailVO;
import com.hikvision.fireprotection.hikalarm.model.vo.PageData;
import com.hikvision.fireprotection.hikalarm.module.mapper.AlarmDetailMapper;
import com.hikvision.fireprotection.hikalarm.module.service.AlarmService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 报警服务
 *
 * @author wangjinchang5
 * @date 2020/12/18 16:20
 * @since 1.0.100
 */
@Service
public class AlarmServiceImpl implements AlarmService {
    @Resource
    private AlarmDetailMapper alarmDetailMapper;

    @Override
    public PageData<AlarmDetailVO> queryAlarmDetailPage(AlarmPageQuery query) {
        PageData<AlarmDetailVO> pageData = new PageData<>(query.getPageNo(), query.getPageSize());

        QueryWrapper<AlarmDetailTable> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(query.getContactPhone())) {
            queryWrapper.like("contact_phone", query.getContactPhone());
        }

        Date startTime = Date.from(LocalDate.now()
                .minusDays(query.getAlarmPeriod())
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
        Date endTime = new Date();
        queryWrapper.between("alarm_time", startTime, endTime);

        Page<AlarmDetailTable> selectPage =
                alarmDetailMapper.selectPage(new Page<>(query.getPageNo(), query.getPageSize()), queryWrapper);

        pageData.setTotal(selectPage.getTotal());
        pageData.setTotalPage(selectPage.getPages());

        List<AlarmDetailVO> alarmDetailVOList;
        List<AlarmDetailTable> records = selectPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            alarmDetailVOList = Collections.emptyList();
        } else {
            alarmDetailVOList = Lists.newArrayListWithCapacity(records.size());
            records.forEach(table -> {
                AlarmDetailVO vo = new AlarmDetailVO();
                BeanUtils.copyProperties(table, vo);
                alarmDetailVOList.add(vo);
            });
        }

        pageData.setList(alarmDetailVOList);

        return pageData;
    }
}
