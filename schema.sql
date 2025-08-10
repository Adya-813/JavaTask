CREATE DATABASE reservation_db;
USE reservation_db;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE reservations (
    pnr BIGINT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    train_number VARCHAR(10) NOT NULL,
    train_name VARCHAR(50) NOT NULL,
    class_type VARCHAR(20) NOT NULL,
    date_of_journey DATE NOT NULL,
    from_place VARCHAR(50) NOT NULL,
    to_place VARCHAR(50) NOT NULL
);
