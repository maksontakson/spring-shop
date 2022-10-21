use my_db;
create table buckets (id integer not null auto_increment, user_id integer, primary key (id)) engine=InnoDB
create table buckets_products (bucket_id integer not null, product_id integer not null) engine=InnoDB
create table categories (id integer not null auto_increment, title varchar(255), primary key (id)) engine=InnoDB
create table orders (id integer not null auto_increment, address varchar(255), created datetime(6), status integer, sum decimal(19,2), updated datetime(6), user_id integer, primary key (id)) engine=InnoDB
create table orders_details (id integer not null auto_increment, amount decimal(19,2), price decimal(19,2), order_id integer, product_id integer, details_id integer not null, primary key (id)) engine=InnoDB
create table products (id integer not null auto_increment, price decimal(19,2), title varchar(255), primary key (id)) engine=InnoDB
create table products_categories (product_id integer not null, category_id integer not null) engine=InnoDB
create table users (id integer not null auto_increment, archive bit not null, email varchar(255), name varchar(255), password varchar(255), role varchar(255), bucket_id integer, primary key (id)) engine=InnoDB
alter table orders_details drop index UK_kk6y3pyhjt6kajomtjbhsoajo
alter table orders_details add constraint UK_kk6y3pyhjt6kajomtjbhsoajo unique (details_id)
alter table buckets add constraint FKnl0ltaj67xhydcrfbq8401nvj foreign key (user_id) references users (id)
alter table buckets_products add constraint FKloyxdc1uy11tayedf3dpu9lci foreign key (product_id) references products (id)
alter table buckets_products add constraint FKc49ah45o66gy2f2f4c3os3149 foreign key (bucket_id) references buckets (id)
alter table orders add constraint FK32ql8ubntj5uh44ph9659tiih foreign key (user_id) references users (id)
alter table orders_details add constraint FK5o977kj2vptwo70fu7w7so9fe foreign key (order_id) references orders (id)
alter table orders_details add constraint FKs0r9x49croribb4j6tah648gt foreign key (product_id) references products (id)
alter table orders_details add constraint FKgvp1k7a3ubdboj3yhnawd5m1p foreign key (details_id) references orders_details (id)
alter table products_categories add constraint FKqt6m2o5dly3luqcm00f5t4h2p foreign key (category_id) references categories (id)
alter table products_categories add constraint FKtj1vdea8qwerbjqie4xldl1el foreign key (product_id) references products (id)
alter table users add constraint FK8l2qc4c6gihjdyoch727guci foreign key (bucket_id) references buckets (id)