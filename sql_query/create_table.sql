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

);

CREATE TABLE user (
	id INT PRIMARY KEY,
	name VARCHAR(50),
	money INT
);



LOAD DATA LOCAL INFILE "initdata/user.csv"
	into table user
	fields terminated by ','
	lines terminated by '\r\n';

