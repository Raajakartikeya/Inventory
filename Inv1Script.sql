create table CUSTOMERS
(
    customer_id   int auto_increment
        primary key,
    customer_name varchar(50)  not null,
    mobile_no     varchar(12)  null,
    address       varchar(255) null,
    email         varchar(50)  null,
    constraint unique_customer_email
        unique (email),
    constraint unique_customer_mobile_no
        unique (mobile_no)
);

create table INVOICES
(
    invoice_id      int auto_increment
        primary key,
    customer_id     int          not null,
    billing_name    varchar(50)  null,
    billing_address varchar(255) null,
    invoice_date    date         not null,
    due_date        date         not null,
    constraint invoices_ibfk_1
        foreign key (customer_id) references inv1.CUSTOMERS (customer_id)
);

create index idx_customer_id
    on INVOICES (customer_id);

create definer = root@localhost trigger FILL_BILLING_DETAILS
    before insert
    on INVOICES
    for each row
BEGIN
    DECLARE temp_customer_name VARCHAR(50);
    DECLARE temp_address VARCHAR(255);

    SELECT customer_name, address
    INTO temp_customer_name, temp_address
    FROM CUSTOMERS
    WHERE customer_id = NEW.customer_id;

    SET NEW.billing_name = temp_customer_name;
    SET NEW.billing_address = temp_address;
end;

create table INVOICE_ITEMS
(
    invoice_item_id   int auto_increment
        primary key,
    invoice_id        int            not null,
    item_id           int            not null,
    item_name         varchar(50)    not null,
    quantity          int            not null,
    price             int            not null,
    tax               int            not null,
    total_without_tax decimal(10, 2) not null,
    total_with_tax    decimal(10, 2) not null,
    constraint invoice_items_ibfk_1
        foreign key (invoice_id) references inv1.INVOICES (invoice_id),
    constraint invoice_items_ibfk_2
        foreign key (item_id) references inv1.ITEMS (item_id),
    constraint invoice_items_ibfk_3
        foreign key (tax) references inv1.TAX_PERCENTAGE (tax)
);

create index idx_invoice_id
    on INVOICE_ITEMS (invoice_id);

create index idx_item_id_invoice_items
    on INVOICE_ITEMS (item_id);

create index idx_tax_invoice_items
    on INVOICE_ITEMS (tax);

create table ITEMS
(
    item_id          int auto_increment
        primary key,
    item_name        varchar(255) not null,
    item_description varchar(255) null,
    tax              int          not null,
    constraint fk_item_tax
        foreign key (tax) references inv1.TAX_PERCENTAGE (tax)
);

create table PAYMENT_DETAILS
(
    invoice_id     int                                                  not null
        primary key,
    sub_total      decimal(10, 2)                                       not null,
    tax            decimal(10, 2)                                       not null,
    total          decimal(10, 2)                                       not null,
    payment_status enum ('PAID', 'UNPAID', 'PARTIALLY PAID', 'OVERDUE') not null,
    payment_made   decimal(10, 2)                                       not null,
    balance_due    decimal(10, 2)                                       not null,
    constraint payment_details_ibfk_1
        foreign key (invoice_id) references inv1.INVOICES (invoice_id)
);

create definer = root@localhost trigger paymentDetails
    before insert
    on PAYMENT_DETAILS
    for each row
BEGIN
    DECLARE temp_sub_total decimal(10,2);
    DECLARE temp_total decimal(10,2);
    DECLARE temp_tax decimal(10,2);
    SELECT SUM(total_without_tax) into temp_sub_total from INVOICE_ITEMS where invoice_id = new.invoice_id;
    SELECT SUM(total_with_tax) into temp_total from INVOICE_ITEMS where invoice_id = new.invoice_id;
    SET NEW.sub_total = temp_sub_total;
    SET NEW.total = temp_total;
    SET NEW.tax = NEW.total - NEW.sub_total;
    SET NEW.balance_due = temp_total;
    SET NEW.payment_made = 0;
    SET NEW.payment_status = 'UNPAID';
end;

create definer = root@localhost trigger updatePaymentDetails
    before update
    on PAYMENT_DETAILS
    for each row
BEGIN
    DECLARE temp_date DATE;
    SET NEW.balance_due = NEW.total - NEW.payment_made;
    SELECT due_date INTO temp_date from INVOICES where invoice_id = NEW.invoice_id;
    IF NEW.payment_made >= NEW.total THEN
        SET NEW.payment_status = 'PAID';
    ELSEIF(temp_date < CURDATE()) THEN
        set NEW.payment_status = 'OVERDUE';
    ELSEIF ((NEW.payment_made < NEW.total) AND (NEW.payment_made > 0)) THEN
        SET NEW.payment_status = 'PARTIALLY PAID';
    ELSE
        SET NEW.payment_status = 'UNPAID';
    END IF;
END;

create table PRICE_LIST
(
    price_list_id int            not null,
    item_id       int            not null,
    selling_price decimal(10, 2) not null,
    primary key (price_list_id, item_id),
    constraint price_list_ibfk_1
        foreign key (item_id) references inv1.ITEMS (item_id),
    constraint price_list_ibfk_2
        foreign key (price_list_id) references inv1.PRICE_LIST_NAME (price_list_id)
);

create index idx_item_id_price_list
    on PRICE_LIST (item_id);

create table PRICE_LIST_NAME
(
    price_list_id   int auto_increment
        primary key,
    price_list_name varchar(50) not null,
    constraint unique_price_list_name
        unique (price_list_name)
);

create table PURCHASES
(
    purchase_id int auto_increment
        primary key,
    vendor_id   int not null,
    constraint purchases_vendor_id
        foreign key (vendor_id) references inv1.VENDORS (vendor_id)
);

create table PURCHASE_LIST
(
    purchase_list_id  int auto_increment
        primary key,
    purchase_id       int not null,
    item_id           int not null,
    purchase_price    int not null,
    purchase_quantity int not null,
    constraint purchase_list_ibfk_1
        foreign key (item_id) references inv1.ITEMS (item_id),
    constraint purchase_list_ibfk_2
        foreign key (purchase_id) references inv1.PURCHASES (purchase_id)
);

create index item_id
    on PURCHASE_LIST (item_id);

create index purchase_id
    on PURCHASE_LIST (purchase_id);

create table STOCKS
(
    item_id        int not null
        primary key,
    total_quantity int not null,
    constraint stocks_ibfk_1
        foreign key (item_id) references inv1.ITEMS (item_id)
);

create table TAX_PERCENTAGE
(
    tax      int         not null
        primary key,
    tax_name varchar(25) null
);

create table VENDORS
(
    vendor_id   int auto_increment
        primary key,
    vendor_name varchar(50)  not null,
    mobile_no   varchar(12)  null,
    address     varchar(255) null,
    email       varchar(50)  null,
    constraint unique_vendor_email
        unique (email),
    constraint unique_vendor_mobile_no
        unique (mobile_no)
);

create
    definer = root@localhost procedure insert_invoice_item(IN p_invoice_id int, IN p_item_id int, IN p_quantity int,
                                                           IN p_price_list_id int)
BEGIN
    DECLARE temp_item_name VARCHAR(50);
    DECLARE temp_item_price DECIMAL(10, 2);
    DECLARE temp_tax INT;

    SELECT item_name, tax INTO temp_item_name, temp_tax
    FROM ITEMS
    WHERE item_id = p_item_id;

    SELECT selling_price INTO temp_item_price
    FROM PRICE_LIST
    WHERE (item_id = p_item_id AND price_list_id = p_price_list_id);

        INSERT INTO invoice_items (invoice_id,item_id, item_name, tax, price, quantity, total_without_tax, total_with_tax)
        VALUES (
            p_invoice_id,
            p_item_id,
            temp_item_name,
            temp_tax,
            temp_item_price,
            p_quantity,
            p_quantity * temp_item_price,
            (p_quantity * temp_item_price) + ((p_quantity * temp_item_price * temp_tax) / 100)
        );

END;

create definer = root@localhost event update_payment_status on schedule
    every '1' MINUTE
        starts '2024-09-05 12:20:23'
    enable
    do
    BEGIN
    UPDATE PAYMENT_DETAILS pd
    JOIN INVOICES i ON pd.invoice_id = i.invoice_id
    SET pd.payment_status = 'OVERDUE'
    WHERE i.due_date < CURDATE()
      AND pd.payment_status != 'PAID';
END;


