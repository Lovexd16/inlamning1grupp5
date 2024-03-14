
    create table t_admin (
        adminId bigint not null,
        email varchar(255),
        password varchar(255),
        primary key (adminId),
        unique (adminId, email)
    );

    create table t_user (
        userId uuid not null,
        email varchar(255) unique,
        firstName varchar(255),
        lastName varchar(255),
        password varchar(255),
        subscribed varchar(255),
        username varchar(255) unique,
        primary key (userId),
        unique (userId, username, email)
    );

    create table t_user_purchase_history (
        usedId uuid not null,
        userPurchaseHistory varchar(255)
    );

    alter table if exists t_user_purchase_history 
       add constraint FKmg4ry9ws7yjpiswc5h6905pu8 
       foreign key (usedId) 
       references t_user;
INSERT INTO t_admin (adminId, email, password) VALUES (123456789, 'admin1@example.com', '0f7d9d9a44a85c8f3d2bc47c865b06baad091a5c8b1bc578e346df2f23c0327c'), (234567890, 'admin2@example.com', 'c1652a68f3de88e1e31b81eac1ec8a4e200a0f9f1145a1f1c5f4aae769a53f2b');
