DROP TABLE IF EXISTS public.products CASCADE ;
CREATE TABLE public.products (
                                 id serial NOT NULL PRIMARY KEY,
                                 product_name text NOT NULL,
                                 default_price float NOT NULL,
                                 currency text NOT NULL,
                                 description text NOT NULL,
                                 category_id integer NOT NULL,
                                 supplier_id integer NOT NULL
);

DROP TABLE IF EXISTS public.product_categories;
CREATE TABLE public.product_categories (
                                           id serial NOT NULL PRIMARY KEY,
                                           category_name text NOT NULL,
                                           category_department text NOT NULL,
                                           category_description text NOT NULL
);

DROP TABLE IF EXISTS public.suppliers;
CREATE TABLE public.suppliers (
                                  id serial NOT NULL PRIMARY KEY,
                                  supplier_name text NOT NULL,
                                  supplier_description text NOT NULL

);

DROP TABLE IF EXISTS public.users CASCADE ;
CREATE TABLE public.users (
                              id serial NOT NULL PRIMARY KEY,
                              name text NOT NULL

);


DROP TABLE IF EXISTS public.product_lines;
CREATE TABLE public.product_lines (
                                      id serial NOT NULL PRIMARY KEY,
                                      cart_id integer NOT NULL,
                                      product_id integer NOT NULL,
                                      quantity integer NOT NULL

);


DROP TABLE IF EXISTS public.carts;
CREATE TABLE public.carts (
                              id serial NOT NULL PRIMARY KEY,
                              user_id integer,
                              currency text NOT NULL,
                              order_id integer


);

DROP TABLE IF EXISTS public.orders;
CREATE TABLE public.orders (
                               id serial NOT NULL PRIMARY KEY,
                               user_id integer,
                               payment_method text,
                               payment_status integer,
                               currency text,
                               date_day text

);

DROP TABLE IF EXISTS public.shipping_info;
CREATE TABLE public.shipping_info (
    user_id integer NOT NULL PRIMARY KEY,
    first_name text,
    last_name text,
    email text,
    phone_number text,
    country text,
    city text,
    post_code text,
    street text
);






ALTER TABLE ONLY public.products
    ADD CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES public.product_categories(id);

ALTER TABLE ONLY public.products
    ADD CONSTRAINT fk_supplier_id FOREIGN KEY (supplier_id) REFERENCES public.suppliers(id);

ALTER TABLE ONLY public.product_lines
    ADD CONSTRAINT fk_product_id FOREIGN KEY (product_id) REFERENCES public.products(id);

ALTER TABLE ONLY public.product_lines
    ADD CONSTRAINT fk_cart_id FOREIGN KEY (cart_id) REFERENCES public.carts(id);

ALTER TABLE ONLY public.carts
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.carts
    ADD CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES public.orders(id);

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.shipping_info
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES public.users(id);

INSERT INTO product_categories (category_name, category_department, category_description) VALUES
    ('Tablet', 'Hardware', 'A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display');

INSERT INTO suppliers (supplier_name, supplier_description) VALUES
    ('Amazon', 'Digital content and services'),
    ('Lenovo', 'Computers');

INSERT INTO products (product_name, default_price, currency, description, category_id, supplier_id) VALUES
    ('Amazon Fire', '49.9', 'USD', 'Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.',
     (SELECT id FROM product_categories WHERE category_name = 'Tablet'), (SELECT id FROM suppliers WHERE supplier_name = 'Amazon')),
    ('Lenovo IdeaPad Miix 700', '479', 'USD', 'Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.',
     (SELECT id FROM product_categories WHERE category_name = 'Tablet'), (SELECT id FROM suppliers WHERE supplier_name = 'Lenovo')),
    ('Amazon Fire HD 8', '89', 'USD', 'Amazon"s latest Fire HD 8 tablet is a great value for media consumption.',
        (SELECT id FROM product_categories WHERE category_name = 'Tablet'), (SELECT id FROM suppliers WHERE supplier_name = 'Amazon'));

INSERT INTO users (id, name) VALUES (1,'Krzysiek'),(2,'Bob'), (3, 'Marcela');