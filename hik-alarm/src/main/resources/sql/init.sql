begin;

create table if not exists tb_alarm_detail
(
    id             varchar(64) not null, -- id
    alarm_name     varchar(255) null,    -- 名称
    alarm_position varchar(255) null,    -- 位置
    alarm_time     timestamp null,       -- 发生时间
    car_num        varchar(64) null,     -- 车牌号
    contact_name   varchar(128) null,    -- 联系人
    contact_phone  varchar(64) null,     -- 手机号码
    create_time    timestamp null,       -- 创建时间
    notify_status  bpchar(1) null,       -- 通知状态：0=未通知；1=已通知；2=通知失败
    notify_time    timestamp null,       -- 通知时间
    remark         varchar(255) null,    -- 备注
    update_time    timestamp null,       -- 更新时间
    constraint tb_alarm_detail_pkey primary key (id)
);

comment on column tb_alarm_detail.id is 'id';
comment on column tb_alarm_detail.alarm_name is '名称';
comment on column tb_alarm_detail.alarm_position is '位置';
comment on column tb_alarm_detail.alarm_time is '发生时间';
comment on column tb_alarm_detail.car_num is '车牌号';
comment on column tb_alarm_detail.contact_name is '联系人';
comment on column tb_alarm_detail.contact_phone is '手机号码';
comment on column tb_alarm_detail.create_time is '创建时间';
comment on column tb_alarm_detail.notify_status is '通知状态：0=未通知；1=已通知；2=通知失败';
comment on column tb_alarm_detail.notify_time is '通知时间';
comment on column tb_alarm_detail.remark is '备注';
comment on column tb_alarm_detail.update_time is '更新时间';

commit;