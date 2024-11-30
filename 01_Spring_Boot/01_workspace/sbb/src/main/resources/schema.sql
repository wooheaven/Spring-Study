drop table if exists dho_user cascade ;
create table dho_user (
    user_id varchar(3) not null,
    user_nm varchar(3),
    create_date timestamp(6),
    game_id varchar(12),
    primary key (user_id)
) ;
drop table if exists to_do cascade ;
create table to_do (
    todo_id integer generated by default as identity,
    user_user_id varchar(3),
    content TEXT,
    create_date timestamp(6),
    primary key (todo_id)
) ;
drop table if exists answer ;
create table answer (
    a_id integer generated by default as identity, 
    question_q_id integer, 
    create_date timestamp(6), 
    content TEXT, 
    primary key (a_id)
) ;
drop table if exists question ;
create table question (
    q_id integer generated by default as identity, 
    create_date timestamp(6), 
    subject varchar(200), 
    content TEXT, 
    primary key (q_id)
) ;