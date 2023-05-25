/* $ "bin/mysql" -u root --local-infile=1 */

DROP DATABASE pads;
CREATE DATABASE pads;

USE pads;

CREATE TABLE model (
	code VARCHAR(20) PRIMARY KEY,
	name VARCHAR(50),
	price INT,
	manufacturer_name VARCHAR(50)
);

CREATE TABLE manufacturer (
	name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE product (
	code VARCHAR(20),
	model_code VARCHAR(20),
	user_id INT,
	receipt_location_name VARCHAR(50),
	PRIMARY KEY (code, model_code)
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
	product_code VARCHAR(20) PRIMARY KEY,
	from_location_name VARCHAR(50),
	to_location_name VARCHAR(50)
);

CREATE TABLE delivery (
	product_code VARCHAR(20),
	delivery_member_code VARCHAR(20),
	start_time DATETIME,
	end_time DATETIME,
	from_location_name VARCHAR(50),
	end_location_name VARCHAR(50),
	PRIMARY KEY (product_code, delivery_member_code)
);

CREATE TABLE user (
	id INT PRIMARY KEY,
	name VARCHAR(50),
	money INT
);

CREATE TABLE location_register (
	id INT PRIMARY KEY,
	name VARCHAR(50),
	money INT
);

/* データの初期化 */

LOAD DATA LOCAL INFILE "initdata/model.csv"
	into table model
	fields terminated by ','
	lines terminated by '\r\n';

LOAD DATA LOCAL INFILE "initdata/manufacturer.csv"
	into table manufacturer
	fields terminated by ','
	lines terminated by '\r\n';

LOAD DATA LOCAL INFILE "initdata/product.csv"
	into table product
	fields terminated by ','
	lines terminated by '\r\n';

LOAD DATA LOCAL INFILE "initdata/location.csv"
	into table location
	fields terminated by ','
	lines terminated by '\r\n';

LOAD DATA LOCAL INFILE "initdata/receipt_location.csv"
	into table receipt_location
	fields terminated by ','
	lines terminated by '\r\n';

LOAD DATA LOCAL INFILE "initdata/trsp_hub.csv"
	into table trsp_hub
	fields terminated by ','
	lines terminated by '\r\n';

LOAD DATA LOCAL INFILE "initdata/warehouse.csv"
	into table warehouse
	fields terminated by ','
	lines terminated by '\r\n';

LOAD DATA LOCAL INFILE "initdata/delivery_member.csv"
	into table delivery_member
	fields terminated by ','
	lines terminated by '\r\n';
UPDATE delivery_member
	set state = NULL
	where state = '';
	
LOAD DATA LOCAL INFILE "initdata/user.csv"
	into table user
	fields terminated by ','
	lines terminated by '\r\n';
