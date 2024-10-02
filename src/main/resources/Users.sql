create table if not exists Users (id serial primary key, name varchar(255) not null);
truncate table Users;
insert into Users (name) values ('John'), ('Jane'), ('Bob'), ('Alice'), ('Charlie');