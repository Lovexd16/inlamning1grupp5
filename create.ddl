
    create table t_customer (
        customerId bigint not null,
        email varchar(255),
        firstName varchar(255),
        lastName varchar(255),
        password varchar(255),
        username varchar(255),
        primary key (customerId),
        unique (customerId, username, email)
    );
