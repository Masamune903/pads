/* $ "bin/mysql" -u root --local-infile=1 */

DROP DATABASE pads;
CREATE DATABASE pads;

USE pads;

CREATE TABLE model (
	code VARCHAR(20) PRIMARY KEY,
	name VARCHAR(50),
	category VARCHAR(50),
	price INT,
	manufacturer VARCHAR(50)
);

CREATE TABLE manufacturer (
	name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE product (
	code VARCHAR(20),
	model VARCHAR(20),
	warehouse VARCHAR(50),
	user INT,
	purchased_price INT,
	purchased_time DATETIME,
	receipt_location VARCHAR(50),
	PRIMARY KEY (code, model)
);

CREATE TABLE product_category (
	name VARCHAR(20)
);

CREATE TABLE location (
	name VARCHAR(50) PRIMARY KEY,
	address VARCHAR(50)
);

CREATE TABLE receipt_location (
	name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE trsp_hub (
	name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE warehouse (
	name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE delivery_member (
	code VARCHAR(20) PRIMARY KEY,
	name VARCHAR(50),
	state VARCHAR(20),
	belongs_to VARCHAR(50) /* 所属する運送拠点 */
);

CREATE TABLE route_plan (
	product VARCHAR(20) PRIMARY KEY,
	from_location VARCHAR(50),
	to_location VARCHAR(50),
	delivery_member VARCHAR(20)
);

CREATE TABLE delivery (
	product VARCHAR(20),
	delivery_member VARCHAR(20),
	start_time DATETIME,
	end_time DATETIME,
	from_location VARCHAR(50),
	to_location VARCHAR(50),
	PRIMARY KEY (product, delivery_member)
);

CREATE TABLE user (
	id INT PRIMARY KEY,
	name VARCHAR(50),
	money INT
);

CREATE TABLE location_register (
	user INT PRIMARY KEY,
	location VARCHAR(50)
);

/* データの初期化 */

LOAD DATA LOCAL INFILE "initdata/model.csv"
	INTO TABLE model
	FIELDS TERMINATED BY ','
	LINES TERMINATED BY '\r\n';

LOAD DATA LOCAL INFILE "initdata/manufacturer.csv"
	INTO TABLE manufacturer
	FIELDS TERMINATED BY ','
	LINES TERMINATED BY '\r\n';

LOAD DATA LOCAL INFILE "initdata/product.csv"
	INTO TABLE product
	FIELDS TERMINATED BY ','
	LINES TERMINATED BY '\r\n';

LOAD DATA LOCAL INFILE "initdata/location.csv"
	INTO TABLE location
	FIELDS TERMINATED BY ','
	LINES TERMINATED BY '\r\n';

LOAD DATA LOCAL INFILE "initdata/receipt_location.csv"
	INTO TABLE receipt_location
	FIELDS TERMINATED BY ','
	LINES TERMINATED BY '\r\n';

LOAD DATA LOCAL INFILE "initdata/trsp_hub.csv"
	INTO TABLE trsp_hub
	FIELDS TERMINATED BY ','
	LINES TERMINATED BY '\r\n';

LOAD DATA LOCAL INFILE "initdata/warehouse.csv"
	INTO TABLE warehouse
	FIELDS TERMINATED BY ','
	LINES TERMINATED BY '\r\n';

LOAD DATA LOCAL INFILE "initdata/delivery_member.csv"
	INTO TABLE delivery_member
	FIELDS TERMINATED BY ','
	LINES TERMINATED BY '\r\n';
	
LOAD DATA LOCAL INFILE "initdata/user.csv"
	INTO TABLE user
	FIELDS TERMINATED BY ','
	LINES TERMINATED BY '\r\n';
