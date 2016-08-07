CREATE TABLE CUSTOMER (
	CUSTOMER_KEY BIGINT AUTO_INCREMENT NOT NULL,
	CUSTOMER_NAME VARCHAR(63),
	FIRST_NAME VARCHAR(63),
	LAST_NAME VARCHAR(63),
	BIRTHDATE DATE,
	EMAIL VARCHAR(255),
    ENABLED BIT,
	PASSWORD VARCHAR(80),
	PRIMARY KEY (CUSTOMER_KEY)
) ENGINE=INNODB;

CREATE TABLE ROLE (
	ROLE_KEY BIGINT AUTO_INCREMENT NOT NULL,
	ROLE_NAME VARCHAR(64),
	PRIMARY KEY (ROLE_KEY)
) ENGINE=INNODB;

CREATE TABLE CUSTOMER_ROLE (
	CUSTOMER_KEY BIGINT NOT NULL,
	ROLE_KEY BIGINT NOT NULL,
	CONSTRAINT FK_CUSTOMER_ROLE FOREIGN KEY (CUSTOMER_KEY) REFERENCES CUSTOMER (CUSTOMER_KEY),
	CONSTRAINT FK_ROLE_CUSTOMER FOREIGN KEY (ROLE_KEY) REFERENCES ROLE (ROLE_KEY)
) ENGINE=INNODB;

CREATE TABLE CAR (
	CAR_KEY BIGINT AUTO_INCREMENT NOT NULL,
	BRAND VARCHAR(20) NOT NULL,
	MODEL VARCHAR(20) NOT NULL,
	PURCHASE_DATE DATE,
	SALE_DATE DATE,
	CUSTOMER_KEY BIGINT NOT NULL,
	PRIMARY KEY (CAR_KEY),
	CONSTRAINT FK_CUSTOMER_CAR FOREIGN KEY (CAR_KEY) REFERENCES CUSTOMER (CUSTOMER_KEY)
) ENGINE=INNODB;

CREATE TABLE TRANSACTION_TYPE (
	TRANSACTION_TYPE_KEY BIGINT AUTO_INCREMENT NOT NULL,
	NAME VARCHAR(20) NOT NULL,
	PRIMARY KEY (TRANSACTION_TYPE_KEY)
) ENGINE=INNODB;

CREATE TABLE TRANSACTION (
	TRANSACTION_KEY BIGINT AUTO_INCREMENT NOT NULL,
	AMOUNT NUMERIC(18,2) NOT NULL,
	CURRENCY CHAR(3) NOT NULL,
	EXCHANGE_RATE DOUBLE,
	EXECUTION_DATE DATE NOT NULL,
	TRANSACTION_TYPE_KEY BIGINT NOT NULL,
	CAR_KEY BIGINT NOT NULL,
	CUSTOMER_KEY BIGINT NOT NULL,
	PRIMARY KEY (TRANSACTION_KEY),
	CONSTRAINT FK_TRANSACTION_CAR FOREIGN KEY (CAR_KEY) REFERENCES CAR (CAR_KEY),
	CONSTRAINT FK_TRANSACTION_TYPE FOREIGN KEY (TRANSACTION_TYPE_KEY) REFERENCES TRANSACTION_TYPE (TRANSACTION_TYPE_KEY),
	CONSTRAINT FK_TRANSACTION_CUSTOMER FOREIGN KEY (CUSTOMER_KEY) REFERENCES CUSTOMER (CUSTOMER_KEY)
) ENGINE=INNODB;

CREATE TABLE FUEL (
	FUEL_KEY BIGINT AUTO_INCREMENT NOT NULL,
	TYPE CHAR(4) NOT NULL,
	COST DOUBLE,
	QUANTITY DOUBLE,
	PRIMARY KEY (FUEL_KEY),
	CONSTRAINT FK_FUEL_TRANSACTION FOREIGN KEY (TRANSACTION_KEY) REFERENCES TRANSACTION (TRANSACTION_KEY),
) ENGINE=INNODB;