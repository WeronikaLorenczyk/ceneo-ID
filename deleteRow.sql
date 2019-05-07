--usuwanie krotek
--zrobilam w osobnym pliku zeby sie latwiej szukalo, wkeisz to na koniec jak bedziesz wysylac ;)

CREATE OR REPLACE RULE del_discounts AS ON DELETE TO discounts DO ALSO
DELETE FROM discount_shop_product WHERE OLD.discount_id=discount_id;

CREATE OR REPLACE RULE del_shop_product AS ON DELETE TO shop_product DO ALSO
DELETE FROM discount_shop_product WHERE OLD.shop_product_id=discount_product_id;

CREATE OR REPLACE RULE del_shops AS ON DELETE TO shops DO ALSO
(DELETE FROM shop_product WHERE OLD.shop_id=shop_id;
DELETE FROM customers_shops WHERE OLD.shop_id=shop_id);

CREATE OR REPLACE RULE del_customers AS ON DELETE TO customers DO ALSO
(DELETE FROM product_customer WHERE OLD.customer_id=customer_id;
DELETE FROM customers_shops WHERE OLD.customer_id=customer_id);

CREATE OR REPLACE RULE del_products AS ON DELETE TO products DO ALSO
(DELETE FROM product_customer WHERE OLD.product_id=product_id;
DELETE FROM attribute_product WHERE OLD.product_id=product_id;
DELETE FROM shop_product WHERE OLD.product_id=product_id);

CREATE OR REPLACE RULE del_attributes AS ON DELETE TO attributes DO ALSO
DELETE FROM attribute_product WHERE OLD.attribute_id=attribute_id;

--to nie dziala i nie wiem co zrobic zeby sie nie petlilo
create or replace function del_categories()
returns trigger as
$$
begin
	CASE WHEN (SELECT count(*) FROM categories WHERE OLD.category_id=parent_category)>0 THEN
		DELETE FROM categories WHERE OLD.category_id=parent_category;
	END CASE;
	DELETE FROM attribute WHERE OLD.category_id=category_id;
	DELETE FROM products WHERE OLD.category_id=category_id;
      return OLD;
end;
$$
language plpgsql;

CREATE trigger del_categories BEFORE DELETE ON categories
FOR EACH ROW EXECUTE PROCEDURE del_categories();
