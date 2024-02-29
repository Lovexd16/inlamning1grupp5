
    create table t_admin (
        adminId bigint not null,
        email varchar(255),
        password varchar(255),
        primary key (adminId),
        unique (adminId, email)
    );

    create table t_customer (
        subscribed integer,
        customerId bigint not null,
        email varchar(255),
        firstName varchar(255),
        lastName varchar(255),
        password varchar(255),
        username varchar(255),
        primary key (customerId),
        unique (customerId, username, email)
    );
INSERT INTO t_customer (customerId, firstName, lastName, email, username, password, subscribed) VALUES (123456789, 'John', 'Doe', 'john.doe@example.com', 'john_doe', 'P@ssw0rd', 0), (234567890, 'Jane', 'Smith', 'jane.smith@example.com', 'jane_smith', 'SecurePwd123', 0), (345678901, 'Alice', 'Johnson', 'alice.johnson@example.com', 'alice_j', 'StrongPassword!', 0), (456789012, 'Bob', 'Miller', 'bob.miller@example.com', 'bob_m', '1234Secret', 0), (567890123, 'Eva', 'Brown', 'eva.brown@example.com', 'eva_b', 'Pass123*', 0);
INSERT INTO t_admin (adminId, email, password) VALUES (123456789, 'admin1@example.com', 'AdminPass123'), (234567890, 'admin2@example.com', 'SecureAdminPwd');
