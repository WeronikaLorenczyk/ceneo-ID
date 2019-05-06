--CREATE SCHEMA ceneo; nie wiem co to robi ani czy tego chcemy

CREATE TABLE categories ( 
	category_id          integer  PRIMARY KEY,
	name                 varchar(100)  unique not NULL,
	parent_category      integer,
	min_age              integer DEFAULT 0,
	CONSTRAINT fk_categories_categories FOREIGN KEY ( parent_category ) REFERENCES categories( category_id )  
 );

--uzywam tej seq zeby pozniej nie trzeba bylo sprawdzac jakie jest najwieksze id
CREATE SEQUENCE seq_categories_id INCREMENT BY 1 START WITH 1;

INSERT INTO categories VALUES
(nextval('seq_categories_id'), 'wyposazenie', null, null),
(nextval('seq_categories_id'), 'meble', 1, null),
(nextval('seq_categories_id'), 'krzesla', 2, null),
(nextval('seq_categories_id'), 'szafy', 2, null),
(nextval('seq_categories_id'), 'szafki kuchenne', 4, null),
(nextval('seq_categories_id'), 'jedzenie', null, null),
(nextval('seq_categories_id'), 'napoje', 6, null),
(nextval('seq_categories_id'), 'alkohol', 7, 18);


CREATE TABLE customers ( 
	customer_id          integer  PRIMARY KEY,
	login                varchar(50) unique not NULL,
	"password"           varchar(50) not NULL,
	age                  integer,
	"location"           varchar(50)  
 );

CREATE SEQUENCE seq_customer_id INCREMENT BY 1 START WITH 1;

--bedzie hash potem, mozemy go napisac juz w javie bo te w sql mi nie chca dzialac :/
INSERT INTO customers VALUES
(nextval('seq_customer_id'),'a','a',1,'Krakow'),
(nextval('seq_customer_id'),'b','b',1,'Katowice'),
(nextval('seq_customer_id'),'c','c',20,'Warszawa'),
(nextval('seq_customer_id'),'d','d',25,'Krakow');

CREATE TABLE discounts ( 
	discount_id          integer  PRIMARY KEY,
	date_from            date not NULL,
	date_to              date,
	percent              numeric(4,2) DEFAULT 0, 
	CHECK (date_from < date_to),
	CHECK (percent >=0)
 );

CREATE SEQUENCE seq_discounts_id INCREMENT BY 1 START WITH 1;

INSERT INTO discounts VALUES
(nextval('seq_discounts_id'),'2011-03-12','2011-03-30',5),
(nextval('seq_discounts_id'),'2015-03-12','2015-03-30',10),(nextval('seq_discounts_id'),'2014-03-12','2014-03-30',50);



CREATE TABLE shops ( 
	shop_id              integer  PRIMARY KEY,
	"location"           varchar(50) ,
	name                 varchar(100) not NULL
 );

CREATE SEQUENCE seq_shop_id INCREMENT BY 1 START WITH 1;

INSERT INTO shops VALUES
(nextval('seq_shop_id'),'Krakow','sklep a'),
(nextval('seq_shop_id'),'Krakow','sklep b'),
(nextval('seq_shop_id'),'Warszawa','sklep c');


CREATE TABLE attributes ( 
	attribute_id         integer  PRIMARY KEY ,
	name                 varchar(100) not NULL,
	category_id          integer not NULL,
	unit                 varchar(50),
	CONSTRAINT uni_att UNIQUE (name, category_id),
	CONSTRAINT fk_attribute_categories FOREIGN KEY ( category_id ) REFERENCES categories( category_id )  
 );

CREATE SEQUENCE seq_attribute_id INCREMENT BY 1 START WITH 1;

INSERT INTO attributes VALUES
(nextval('seq_attribute_id'), 'material',1, null),
(nextval('seq_attribute_id'), 'kolor',1, null),
(nextval('seq_attribute_id'), 'ile drzwi',3, null),
(nextval('seq_attribute_id'), 'pojemnosc',7, 'ml');

CREATE TABLE customers_shops ( 
	customer_id          integer not NULL,
	shop_id              integer not NULL,
	rating               integer not NULL,
	CONSTRAINT uni_cus_shop UNIQUE(customer_id,shop_id),
	CONSTRAINT fk_customers_shops_customers FOREIGN KEY ( customer_id ) REFERENCES customers( customer_id )  ,
	CONSTRAINT fk_customers_shops_shops FOREIGN KEY ( shop_id ) REFERENCES shops( shop_id ),
	CHECK (rating<=5 AND rating>=0)
 );

INSERT INTO customers_shops VALUES
(1,1,1),(1,2,4),(2,3,4),(2,1,4);


CREATE TABLE products ( 
	product_id           integer  PRIMARY KEY,
	name                 varchar(100) not NULL unique,
	description          varchar(200),
	category_id          integer,
	CONSTRAINT fk_products_categories FOREIGN KEY ( category_id ) REFERENCES categories( category_id ) 
 );

CREATE SEQUENCE seq_product_id INCREMENT BY 1 START WITH 1;

INSERT INTO products VALUES
(nextval('seq_product_id'), 'krzeslo a',null,3),
(nextval('seq_product_id'), 'krzeslo b',null,3),
(nextval('seq_product_id'), 'szafa a',null,4),
(nextval('seq_product_id'), 'szafka kuchenna a',null,5),
(nextval('seq_product_id'), 'jablko',null,6),
(nextval('seq_product_id'), 'sok',null,7),
(nextval('seq_product_id'), 'wino',null,8),
(nextval('seq_product_id'), 'piwo',null,8),
(nextval('seq_product_id'), 'szafka kuchenna b',null,5);

CREATE TABLE attribute_product ( 
	product_id           integer not NULL,
	attribute_id         integer not NULL,
	"value"              varchar(100) not NULL,
	CONSTRAINT uni_att_pro UNIQUE (product_id,attribute_id),
	CONSTRAINT fk_attribute_product_attribute FOREIGN KEY ( attribute_id ) REFERENCES attribute( attribute_id )  ,
	CONSTRAINT fk_attribute_product_products FOREIGN KEY ( product_id ) REFERENCES products( product_id )  
 );

INSERT INTO attribute_product VALUES
(1,1,'drewno'),
(1,2,'braz'),
(3,3,'3'),
(3,1,'drewno'),
(4,1,'metal'),
(4,3,'5'),
(6,4,'1000'),
(9,1,'metal');

CREATE TABLE shop_product ( 
	shop_product_id		integer PRIMARY KEY,
	shop_id              integer not NULL,
	product_id           integer not NULL,
	price                numeric(10,2) CHECK (price>=0) not NULL,
	amount               numeric CHECK (amount>=0),
	CONSTRAINT uni_sho_pro UNIQUE (shop_id,product_id),
	CONSTRAINT fk_shop_product_discount_product FOREIGN KEY ( product_id ) REFERENCES products( product_id )  ,
	CONSTRAINT fk_shop_product_shops FOREIGN KEY ( shop_id ) REFERENCES shops( shop_id )  
 );

CREATE SEQUENCE seq_sho_pro INCREMENT 1 START WITH 1;

INSERT INTO shop_product VALUES
(nextval('seq_sho_pro'),1,1,3.5,100),
(nextval('seq_sho_pro'),2,1,5,200),
(nextval('seq_sho_pro'),3,1,7,100),
(nextval('seq_sho_pro'),1,2,3.5,100),
(nextval('seq_sho_pro'),1,3,3.5,100),
(nextval('seq_sho_pro'),2,4,3.5,100),
(nextval('seq_sho_pro'),1,5,3.5,100),
(nextval('seq_sho_pro'),1,6,3.5,100),
(nextval('seq_sho_pro'),3,7,3.5,100),
(nextval('seq_sho_pro'),3,8,3.5,100);


CREATE TABLE discount_shop_product ( 
	discount_id          integer not NULL,
	discount_product_id  integer not NULL,
	CONSTRAINT uni_dis_sho_pro UNIQUE (discount_id,discount_product_id),
	CONSTRAINT fk_discount_product_discounts FOREIGN KEY ( discount_id ) REFERENCES discounts( discount_id ) ,
	CONSTRAINT fk_discount_product_shop_product FOREIGN KEY ( discount_product_id ) REFERENCES shop_product( shop_product_id ) 
 );

INSERT INTO discount_shop_product VALUES
(1,1),(1,3),(2,5),(3,6),(2,4);


CREATE TABLE product_customer ( 
	product_id           integer  NOT NULL,
	customer_id          integer  NOT NULL,
	rating               integer  NOT NULL,
	CONSTRAINT uni_pro_cus UNIQUE (product_id,customer_id),
	CHECK (rating<=5 AND rating>=0),
	CONSTRAINT fk_product_customer_products FOREIGN KEY ( product_id ) REFERENCES products( product_id )  ,
	CONSTRAINT fk_product_customer_customers FOREIGN KEY ( customer_id ) REFERENCES customers( customer_id )  
 );

INSERT INTO product_customer VALUES
(1,1,4),(1,3,2),(2,3,1),(2,2,1);

