-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

INSERT INTO t_customer (customerId, firstName, lastName, email, username, password, subscribed) VALUES
(123456789, 'John', 'Doe', 'john.doe@example.com', 'john_doe', '$2a$12$hFuUFRFD6UNL8MBADtN0eucfv6DjTcyhERrsIguzPEeKs0C15XkVu', 1),
(234567890, 'Jane', 'Smith', 'jane.smith@example.com', 'jane_smith', '$2a$12$L/eTW9lweH7v1Dfz9kSC2eV/NKUpuz3.Qvvl5w16wdh3AzZ8F4NwC', 0),
(345678901, 'Alice', 'Johnson', 'alice.johnson@example.com', 'alice_j', '$2a$12$aW2Brgw.Uk1TOwvQgQNe1.GVV1e1tqj1GvP/vVt7lOeIYV/ePcJoa', 1),
(456789012, 'Bob', 'Miller', 'bob.miller@example.com', 'bob_m', '$2a$12$ZYFpz87Un0ZxKTlOumLQO.XopZCIe1GhpxXoQ6zg3HGTZQ6CJYqau', 0),
(567890123, 'Eva', 'Brown', 'eva.brown@example.com', 'eva_b', '$2a$12$zZZ0X7C3MCek32dZWdncg.Ey4CBVeKQHHWbb/QvOmy5nCkEAlbY5y', 1);

INSERT INTO t_admin (adminId, email, password) VALUES
(123456789, 'admin1@example.com', 'AdminPass123'),
(234567890, 'admin2@example.com', 'SecureAdminPwd');