
    alter table if exists t_user_purchase_history 
       drop constraint if exists FKmg4ry9ws7yjpiswc5h6905pu8;

    drop table if exists t_admin cascade;

    drop table if exists t_user cascade;

    drop table if exists t_user_purchase_history cascade;
