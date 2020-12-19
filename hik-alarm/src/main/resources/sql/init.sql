begin;
create table web_demo.tb_alarm_detail
(
    id             mediumint auto_increment not null COMMENT '主键id',
    alarm_id       varchar(100) not null COMMENT '原始报警id',
    alarm_name     varchar(100) null COMMENT '报警名称',
    alarm_position varchar(100) null COMMENT '报警位置',
    alarm_time     TIMESTAMP null COMMENT '报警时间',
    alarm_type     varchar(100) null COMMENT '报警类型',
    car_num        varchar(100) not null COMMENT '车牌号',
    contact_name   varchar(100) null COMMENT '联系人',
    contact_phone  varchar(100) null COMMENT '手机号码',
    notify_status  CHAR         not null COMMENT '通知状态：0=未通知；1=已通知；2=通知失败',
    notify_time    TIMESTAMP null COMMENT '通知时间',
    remark         varchar(100) null COMMENT '备注',
    sensor_name    varchar(100) null COMMENT '通道名称',
    create_time    TIMESTAMP null COMMENT '创建时间',
    update_time    varchar(100) null COMMENT '更新时间',
    constraint tb_alarm_detail_pk primary key (id)
) ENGINE = InnoDB default CHARSET = utf8mb4 collate = utf8mb4_0900_ai_ci;

commit;