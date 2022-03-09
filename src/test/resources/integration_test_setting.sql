--- 회원 테이블 DDL ---
create table users
(
    id           bigint auto_increment comment '아이디' primary key,
    nickname     varchar(30)  default '' not null comment '유저아이디',
    email        varchar(30)             not null comment '이메일',
    password     varchar(128) default '' not null comment '비밀번호',
    name         varchar(30)             null comment '이름',
    phone_number varchar(15)             not null comment '휴대폰번호',
    birth        varchar(10)             null comment '생년월일',
    auth_status  int                     not null comment '가입상태',
    address      text                    not null comment '주소',
    created_at   timestamp               not null comment '생성일',
    updated_at   timestamp               not null comment '변경일'
);

alter table users comment '회원';

create index users_nickname
    on users (nickname);

--- 채팅방 테이블 DDL ---
create table chat_rooms
(
    id           bigint auto_increment comment '아이디'
        primary key,
    product_code varchar(30) not null comment '경매물품코드',
    product_name varchar(20) null comment '경매물품이름',
    description  text        null comment '경매물품설명'
);

alter table users comment '채팅방';

--- 채팅 메시지 테이블 DDL ---
create table chat_messages
(
    id         bigint auto_increment comment '아이디'
        primary key,
    room_id    bigint                 not null comment '채팅방아이디',
    user_id    bigint                 not null comment '회원아이디',
    message    text                   null comment '채팅메시지',
    created_at timestamp              null comment '생성일',
    updated_at timestamp              null comment '변경일',
    nickname   varchar(30) default '' not null comment '회원아이디',
    type       varchar(10)            null,
    constraint chat_messages_fk1
        foreign key (room_id) references chat_rooms (id),

    constraint chat_messages_fk2
        foreign key (user_id) references users (id)
);

alter table users comment '채팅 메시지';

--- Sago Data Insert ---
insert into users (id, nickname, email, password, name, phone_number, birth, auth_status, address, created_at, updated_at)
values  (NULL, 'sagotest18', 'teamsagocorp@gmail.com', '$2a$16$eA.xg3o4PKK8bCc4DESXp.M/yo9XBcz4Mkf60WEoaeK3pXTQrpP5a', 'hello', '010-6666-0000', '2021.12.10', 1, 'Seoul', '2021-12-18 04:47:48', '2021-12-18 04:47:48');