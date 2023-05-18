/* $ "bin/mysql" -u root --local-infile=1 */

CREATE TABLE model (
	code INT PRIMARY KEY,
	name VARCHAR(50),
	price INT,
	manufacturer_name VARCHAR(50)
);

CREATE TABLE manufacturer (
	name VARCHAR(50) INT PRIMARY KEY
);

CREATE TABLE product (
	code INT PRIMARY KEY,
	model_code INT PRIMARY KEY,
	user_id INT,
	receipt_location_name VARCHAR(50)
);

CREATE TABLE location (
	name VARCHAR(50) PRIMARY KEY,
	address VARCHAR
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
	code INT PRIMARY KEY,
	name VARCHAR(50),
	state VARCHAR(20)
);

CREATE TABLE route_plan (
	product_code INT PRIMARY KEY,
	from_location_name VARCHAR(50),
	to_location_name VARCHAR(50)
);

CREATE TABLE delivery (
	product_code INT PRIMARY KEY,
	delivery_member_code INT PRIMARY KEY,
	start_time DATETIME,
	end_time DATETIME,
	from_location_name VARCHAR(50),
	end_location_name VARCHAR(50)
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
	into table user
	fields terminated by ','
	lines terminated by '\r\n';

LOAD DATA LOCAL INFILE "initdata/manufacturer.csv"
	into table user
	fields terminated by ','
	lines terminated by '\r\n';

LOAD DATA LOCAL INFILE "initdata/product.csv"
	into table user
	fields terminated by ','
	lines terminated by '\r\n';

LOAD DATA LOCAL INFILE "initdata/location.csv"
	into table user
	fields terminated by ','
	lines terminated by '\r\n';

LOAD DATA LOCAL INFILE "initdata/receipt_location.csv"
	into table user
	fields terminated by ','
	lines terminated by '\r\n';

LOAD DATA LOCAL INFILE "initdata/trsp_hub.csv"
	into table user
	fields terminated by ','
	lines terminated by '\r\n';

LOAD DATA LOCAL INFILE "initdata/warehouse.csv"
	into table user
	fields terminated by ','
	lines terminated by '\r\n';

LOAD DATA LOCAL INFILE "initdata/delivery_member.csv"
	into table user
	fields terminated by ','
	lines terminated by '\r\n';
	
LOAD DATA LOCAL INFILE "initdata/user.csv"
	into table user
	fields terminated by ','
	lines terminated by '\r\n';