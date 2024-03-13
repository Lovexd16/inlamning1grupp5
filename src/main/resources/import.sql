-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

INSERT INTO t_admin (adminId, email, password) VALUES
(123456789, 'admin1@example.com', '0f7d9d9a44a85c8f3d2bc47c865b06baad091a5c8b1bc578e346df2f23c0327c'),
(234567890, 'admin2@example.com', 'c1652a68f3de88e1e31b81eac1ec8a4e200a0f9f1145a1f1c5f4aae769a53f2b');