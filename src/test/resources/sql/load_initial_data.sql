insert into shopping_basket (id, user_name, basket_status) values(1, 'testUser', 'OPEN');

insert into product (product_id) values('abc');
insert into product (product_id) values('def');

insert into product_order(id, basket_id, product_id, quantity) values(1,1,'abc',10);
insert into product_order(id, basket_id, product_id, quantity) values(2,1,'def',50);