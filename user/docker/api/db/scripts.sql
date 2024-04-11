CREATE DATABASE IF NOT EXISTS
    auth;
    use auth;

    CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    mail VARCHAR(255),
    login VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(255)
);

INSERT INTO users VALUE("c0841fd2-d2fb-432b-9434-7b0cf3cfca55","Pedro", "pedro@teste.com", "login", "$2a$10$1exZzwZy8lR0qmh4TlAOJe3z4kxroaTkeDunT2b.9mrWACY2VywEq", "ROLE_ADMIN" )

INSERT INTO users VALUE("08b5c787-6229-46dc-9f8f-1a8f8d27d679","Luiza", "email@teste.com", "loginteste", "$2a$10$j7yvl06nZ5/WNdlqxsKa.O6pAyhcSbqZQQq1.dNwY4xvPEg1mTmQ2", "ROLE_ADMIN" )
