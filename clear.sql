DROP TABLE IF EXISTS product_customer CASCADE; 
DROP TABLE IF EXISTS discount_shop_product CASCADE; 
DROP TABLE IF EXISTS shop_product CASCADE; 
DROP TABLE IF EXISTS attribute_product CASCADE; 
DROP TABLE IF EXISTS products CASCADE; 
DROP TABLE IF EXISTS customers_shops CASCADE; 
DROP TABLE IF EXISTS attributes CASCADE; 
DROP TABLE IF EXISTS shops CASCADE; 
DROP TABLE IF EXISTS discounts CASCADE; 
DROP TABLE IF EXISTS customers CASCADE; 
DROP TABLE IF EXISTS categories CASCADE; 

DROP SEQUENCE IF EXISTS seq_sho_pro;
DROP SEQUENCE IF EXISTS seq_product_id;
DROP SEQUENCE IF EXISTS seq_attribute_id;
DROP SEQUENCE IF EXISTS seq_shop_id;
DROP SEQUENCE IF EXISTS seq_discounts_id;
DROP SEQUENCE IF EXISTS seq_customer_id;
DROP SEQUENCE IF EXISTS seq_categories_id;