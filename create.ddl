
    create table t_admin (
        adminId bigint not null,
        email varchar(255),
        password varchar(255),
        primary key (adminId),
        unique (adminId, email)
    );

    create table t_user (
        subscribed integer,
        userId bigint not null,
        email varchar(255) unique,
        firstName varchar(255),
        lastName varchar(255),
        password varchar(255),
        username varchar(255) unique,
        primary key (userId),
        unique (userId, username, email)
    );

    create table t_user_purchase_history (
        usedId bigint not null,
        userPurchaseHistory varchar(255)
    );

    alter table if exists t_user_purchase_history 
       add constraint FKmg4ry9ws7yjpiswc5h6905pu8 
       foreign key (usedId) 
       references t_user;
INSERT INTO t_user (userId, firstName, lastName, email, username, password, subscribed) VALUES (123456789, 'John', 'Doe', 'john.doe@example.com', 'john_doe', '$2a$12$hFuUFRFD6UNL8MBADtN0eucfv6DjTcyhERrsIguzPEeKs0C15XkVu', 1), (234567890, 'Jane', 'Smith', 'jane.smith@example.com', 'jane_smith', '$2a$12$L/eTW9lweH7v1Dfz9kSC2eV/NKUpuz3.Qvvl5w16wdh3AzZ8F4NwC', 0), (345678901, 'Alice', 'Johnson', 'alice.johnson@example.com', 'alice_j', '$2a$12$aW2Brgw.Uk1TOwvQgQNe1.GVV1e1tqj1GvP/vVt7lOeIYV/ePcJoa', 1), (456789012, 'Bob', 'Miller', 'bob.miller@example.com', 'bob_m', '$2a$12$ZYFpz87Un0ZxKTlOumLQO.XopZCIe1GhpxXoQ6zg3HGTZQ6CJYqau', 0), (567890123, 'Eva', 'Brown', 'eva.brown@example.com', 'eva_b', '$2a$12$zZZ0X7C3MCek32dZWdncg.Ey4CBVeKQHHWbb/QvOmy5nCkEAlbY5y', 1);
INSERT INTO t_admin (adminId, email, password) VALUES (123456789, 'admin1@example.com', '0f7d9d9a44a85c8f3d2bc47c865b06baad091a5c8b1bc578e346df2f23c0327c'), (234567890, 'admin2@example.com', 'c1652a68f3de88e1e31b81eac1ec8a4e200a0f9f1145a1f1c5f4aae769a53f2b');
