package com.hikvision.fireprotection.alarm.module.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hikvision.fireprotection.alarm.model.request.publicsecurity.param.PersonalInfo;
import com.hikvision.fireprotection.alarm.model.request.publicsecurity.param.ValidateInfo;
import com.hikvision.fireprotection.alarm.common.constant.CommonConstant;
import com.hikvision.fireprotection.alarm.common.constant.UrlConstant;
import com.hikvision.fireprotection.alarm.common.enums.BusinessExceptionEnum;
import com.hikvision.fireprotection.alarm.common.enums.NotifyStatus;
import com.hikvision.fireprotection.alarm.common.utils.HttpUtil;
import com.hikvision.fireprotection.alarm.common.utils.MapperUtil;
import com.hikvision.fireprotection.alarm.model.dto.AlarmEventDTO;
import com.hikvision.fireprotection.alarm.model.request.AlarmPageQuery;
import com.hikvision.fireprotection.alarm.model.request.publicsecurity.CarNumParam;
import com.hikvision.fireprotection.alarm.model.table.AlarmDetailTable;
import com.hikvision.fireprotection.alarm.model.vo.AlarmDetailVO;
import com.hikvision.fireprotection.alarm.model.vo.PageData;
import com.hikvision.fireprotection.alarm.model.vo.publicsecurity.CarNumResult;
import com.hikvision.fireprotection.alarm.model.request.publicsecurity.TextMsgParam;
import com.hikvision.fireprotection.alarm.model.vo.publicsecurity.TextMsgResult;
import com.hikvision.fireprotection.alarm.module.dao.AlarmDetailMapper;
import com.hikvision.fireprotection.alarm.module.service.AlarmService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 报警服务
 *
 * @author wangjinchang5
 * @date 2020/12/18 16:20
 * @since 1.0.100
 */
@Service
@EnableConfigurationProperties({PersonalInfo.class, ValidateInfo.class})
@Slf4j
public class AlarmServiceImpl implements AlarmService {
    private final PersonalInfo personalInfo;
    private final ValidateInfo validateInfo;

    @Autowired
    public AlarmServiceImpl(PersonalInfo personalInfo, ValidateInfo validateInfo) {
        this.personalInfo = personalInfo;
        this.validateInfo = validateInfo;
    }

    @Resource
    private AlarmDetailMapper alarmDetailMapper;

    @Override
    public PageData<AlarmDetailVO> queryAlarmDetailPage(AlarmPageQuery query) {
        PageData<AlarmDetailVO> pageData = new PageData<>(query.getPageNo(), query.getPageSize());

        QueryWrapper<AlarmDetailTable> queryWrapper = new QueryWrapper<>();
        // 车牌号模糊查询
        if (StringUtils.isNotEmpty(query.getCarNum())) {
            queryWrapper.like("car_num", query.getCarNum());
        }
        // 联系人模糊查询
        if (StringUtils.isNotEmpty(query.getContactName())) {
            queryWrapper.like("contact_name", query.getContactName());
        }
        // 手机号码模糊查询
        if (StringUtils.isNotEmpty(query.getContactPhone())) {
            queryWrapper.like("contact_phone", query.getContactPhone());
        }
        // 通知状态查询
        if (null != query.getNotifyStatus()) {
            queryWrapper.eq("notify_status", query.getNotifyStatus());
        }

        Date endTime = query.getEndTime() == null
                ? new Date()
                : query.getEndTime();

        Date startTime = query.getStartTime() == null || query.getStartTime().after(endTime)
                ? Date.from(
                endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        .minusMonths(1)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant())
                : query.getStartTime();

        queryWrapper.between("alarm_time", startTime, endTime);

        Page<AlarmDetailTable> selectPage =
                alarmDetailMapper.selectPage(new Page<>(query.getPageNo(), query.getPageSize()), queryWrapper);

        List<AlarmDetailVO> alarmDetailVOList;
        List<AlarmDetailTable> records = selectPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            alarmDetailVOList = Collections.emptyList();
        } else {
            alarmDetailVOList = MapperUtil.mapperToList(AlarmDetailVO.class, records);
            alarmDetailVOList.forEach(vo -> {
                String contactPhone = vo.getContactPhone();
                if (contactPhone != null && contactPhone.matches(CommonConstant.MOBILE_REGEX)) {
                    contactPhone = contactPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                }
                vo.setContactPhone(contactPhone);
            });
        }

        pageData.setTotal(selectPage.getTotal(), selectPage.getPages())
                .setList(alarmDetailVOList);

        return pageData;
    }

    @Override
    public Set<String> processAlarmEvent(List<AlarmEventDTO> alarmEventDTOList) {
        if (CollectionUtils.isEmpty(alarmEventDTOList)) {
            return Collections.emptySet();
        }

        // 1 事件入库
        List<AlarmDetailTable> alarmDetailTables = MapperUtil.mapperToList(AlarmDetailTable.class, alarmEventDTOList);

        alarmDetailTables.forEach(table -> {
            table.setNotifyStatus(NotifyStatus.NOT_NOTIFIED.code);
            alarmDetailMapper.insert(table);
        });

        // 2 异步处理
        CompletableFuture.runAsync(() -> this.asyncProcessingAlarmEvent(alarmDetailTables));

        return alarmEventDTOList.stream().map(AlarmEventDTO::getFilePath).collect(Collectors.toSet());
    }

    private void asyncProcessingAlarmEvent(List<AlarmDetailTable> alarmDetailTables) {
        log.debug("开始处理  -->");
        // 3 处理事件
        alarmDetailTables.forEach(table -> {
            // 3.1 查询联系人

//            CarNumResult carNumResult = queryContactInformation(table.getCarNum());

            String s = HttpUtil.executeGet("https://www.baidu.com/", null);
            log.debug("请求成功  -- > {}", s.length());

            table.setContactName("hik-" + RandomUtils.nextInt(1000, 9999));
            table.setContactPhone(RandomUtils.nextLong(15000000000L, 18999999999L) + "");

            // 3.2 发送短信
            TextMsgResult textMsgResult = sendTextMessage(table.getContactPhone(), "测试短信");
            log.info("发送短信  -->");

            if (CommonConstant.SUCCESS_CODE.equals(textMsgResult.getResponseCode())) {
                // 发送成功
                table.setNotifyStatus(NotifyStatus.ALREADY_NOTIFIED.code);
            } else {
                // 发送失败
                table.setNotifyStatus(NotifyStatus.FAILED_NOTIFIED.code);
            }
            table.setNotifyTime(new Date());
            table.setRemark(textMsgResult.getComment());

            log.warn("发送完成 --> {}", textMsgResult.getResponseCode());
            table.setNotifyStatus(RandomUtils.nextInt(1, 4));
            table.setNotifyTime(new Date());

            // 3.3 更新短信通知状态
            alarmDetailMapper.updateById(table);

            log.error("更新完成 --> ");
        });
    }

    private CarNumResult queryContactInformation(String carNum) {
        CarNumParam.RealInfo realInfo = CarNumParam.RealInfo.build(personalInfo);
        CarNumParam.Validate validate = CarNumParam.Validate.build(validateInfo);
        CarNumParam carNumParam = new CarNumParam(realInfo, null, validate);
        String response = HttpUtil.executePost(UrlConstant.VEHICLE_INFORMATION,
                JSONObject.toJSONString(carNumParam), null);

        return JSONObject.parseObject(response, CarNumResult.class);
    }

    private TextMsgResult sendTextMessage(String contactPhone, String textContent) {
        TextMsgParam textMsgParam = new TextMsgParam();
        textMsgParam.setBizType("01");
        textMsgParam.setDestMobile(contactPhone);
        textMsgParam.setLoginName("test");
        textMsgParam.setPassword("test");
        textMsgParam.setSMSContent(textContent);

        Map<String, String> parameter = MapperUtil.mapperToMap(textMsgParam);
        String response = HttpUtil.executeGet(UrlConstant.SEND_TEXT_MSG, null, parameter);

        TextMsgResult textMsgResult = new TextMsgResult();
        textMsgResult.setBizType(textMsgParam.getBizType());
        if (StringUtils.isEmpty(response)) {
            // 发送失败
            textMsgResult.setResponseCode(BusinessExceptionEnum.SYSTEM_ERROR.getCode());
            textMsgResult.setComment(BusinessExceptionEnum.SYSTEM_ERROR.getMsg());
        } else {
            textMsgResult.setResponseCode("0");
            textMsgResult.setComment("success");
        }
        return textMsgResult;
    }
}
