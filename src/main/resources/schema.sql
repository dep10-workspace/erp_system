CREATE TABLE IF NOT EXISTS User(
    full_name VARCHAR(250) NOT NULL ,
    id VARCHAR(100) PRIMARY KEY ,
    role ENUM('ADMIN','PM','SM','STM','DC')NOT NULL,
    password VARCHAR(300) NOT NULL
);

CREATE TABLE IF NOT EXISTS Supplier(
    id VARCHAR(100) PRIMARY KEY,
    name VARCHAR(250) NOT NULL ,
    address VARCHAR(350) NOT NULL ,
    bank VARCHAR(150) NOT NULL ,
    account_number VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS contact(
    contact VARCHAR(15),
    supplier_id VARCHAR(100) NOT NULL,
    CONSTRAINT fk_supplier_id Foreign Key (supplier_id) REFERENCES Supplier(id),
    PRIMARY KEY (supplier_id,contact)
);

CREATE TABLE IF NOT EXISTS Quotation(
    quotation_number INT PRIMARY KEY AUTO_INCREMENT,
    supplier_id VARCHAR(100) NOT NULL ,
    CONSTRAINT fk_supplier_id FOREIGN KEY (supplier_id) REFERENCES Supplier(id)
);

CREATE TABLE IF NOT EXISTS Item_Type(
    name VARCHAR(200) PRIMARY KEY,
    unit VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS Item(
    quotation_number INT NOT NULL ,
    item_type VARCHAR(100),
    id VARCHAR(200) PRIMARY KEY ,
    unit VARCHAR(20),
    price DECIMAL(10,2),
    approval ENUM('APPROVE','PENDING','NOT APPROVED'),
    CONSTRAINT fk_quotation_number FOREIGN KEY (quotation_number) REFERENCES Quotation(quotation_number)
);

CREATE TABLE IF NOT EXISTS Site_Order(
    number INT PRIMARY KEY AUTO_INCREMENT,
    item_id VARCHAR(200),
    site VARCHAR(50) Not Null ,
    user_id VARCHAR(100) Not Null ,
    quantity DECIMAL(10,2) NOT NULL ,
    status ENUM('OPEN','CANCEL','READY','ISSUED'),
    CONSTRAINT fk_item_id FOREIGN KEY (item_id) REFERENCES Item(id)
);

CREATE TABLE IF NOT EXISTS ISSUE(
    issue_number INT PRIMARY KEY AUTO_INCREMENT ,
    order_number INT NOT NULL ,
    issue_name VARCHAR(50) NOT NULL ,
    CONSTRAINT fk_order_number FOREIGN KEY (order_number) REFERENCES Site_Order(number)
);

CREATE TABLE IF NOT EXISTS Stock(
    item_id VARCHAR(200) PRIMARY KEY ,
    balanced_quantity DECIMAL(10,2) NOT NULL ,
    CONSTRAINT fk_item_id FOREIGN KEY (item_id) REFERENCES Item(id)
);
