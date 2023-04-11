CREATE TABLE IF NOT EXISTS User(
                                   full_name VARCHAR(250) NOT NULL ,
                                   id VARCHAR(100) PRIMARY KEY ,
                                   role ENUM('ADMIN','PM','SM','STM','DC')NOT NULL,
                                   password VARCHAR(300) NOT NULL

)