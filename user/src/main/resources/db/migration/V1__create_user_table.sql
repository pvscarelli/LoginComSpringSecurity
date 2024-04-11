create table users_entry (
    id UUID PRIMARY KEY,
    name VARCHAR(100),
    mail VARCHAR(100) UNIQUE,
    login VARCHAR(50) UNIQUE,
    password VARCHAR(100),
    role VARCHAR(20)
);