begin;
create table if not exists tb_alarm_detail
(
    id             mediumint auto_increment not null comment '主键id',
    alarm_id       varchar(64)  not null comment '原始报警id',
    alarm_name     varchar(128) null comment '报警名称',
    alarm_position varchar(256) null comment '报警位置',
    alarm_time     timestamp    null comment '报警时间',
    alarm_type     varchar(128) null comment '报警类型',
    car_num        varchar(64)  not null comment '车牌号',
    contact_name   varchar(128) null comment '联系人',
    contact_phone  varchar(64)  null comment '手机号码',
    notify_status  tinyint      not null comment '通知状态：0=未通知；1=已通知；2=通知失败',
    notify_time    timestamp    null comment '通知时间',
    remark         varchar(128) null comment '备注',
    sensor_name    varchar(128) null comment '通道名称',
    create_time    timestamp    null comment '创建时间',
    update_time    timestamp    null comment '更新时间',
    constraint tb_alarm_detail_pk primary key (id)
) engine = InnoDB default charset = utf8mb4 collate = utf8mb4_0900_ai_ci;

commit;