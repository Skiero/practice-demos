package com.hikvision.fireprotection.hikalarm.module.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hikvision.fireprotection.hikalarm.common.utils.MapperUtil;
import com.hikvision.fireprotection.hikalarm.model.dto.HikAlarmDTO;
import com.hikvision.fireprotection.hikalarm.model.request.AlarmPageQuery;
import com.hikvision.fireprotection.hikalarm.model.table.AlarmDetailTable;
import com.hikvision.fireprotection.hikalarm.model.vo.AlarmDetailVO;
import com.hikvision.fireprotection.hikalarm.model.vo.PageData;
import com.hikvision.fireprotection.hikalarm.model.vo.publicsecurity.CarNumResult;
import com.hikvision.fireprotection.hikalarm.model.vo.publicsecurity.TextMsgResult;
import com.hikvision.fireprotection.hikalarm.module.mapper.AlarmDetailMapper;
import com.hikvision.fireprotection.hikalarm.module.service.AlarmService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
@Slf4j
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
            alarmDetailVOList = MapperUtil.tableConvertToVOList(records);
        }

        pageData.setTotal(selectPage.getTotal(), selectPage.getPages())
                .setList(alarmDetailVOList);

        return pageData;
    }

    @Override
    public void handleAlarmEvent(List<HikAlarmDTO> alarmDTOList) {
        if (CollectionUtils.isEmpty(alarmDTOList)) {
            log.warn("");
            return;
        }

        alarmDTOList.forEach(alarmDTO -> {
            // 根据车牌号查询联系人
            CarNumResult carNumResult = queryContactInformation(alarmDTO);
            // 给联系人下发短信
            TextMsgResult textMsgResult = sendTextMessage(alarmDTO);
            // 将结果保存到数据库
            alarmDetailMapper.insert(null);
        });
    }

    private CarNumResult queryContactInformation(HikAlarmDTO alarmDTO) {
        return new CarNumResult();
    }

    private TextMsgResult sendTextMessage(HikAlarmDTO alarmDTO) {
        return new TextMsgResult();
    }
}
