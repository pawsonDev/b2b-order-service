CREATE TABLE product
      (product_id           VARCHAR(255)   NOT NULL PRIMARY KEY
      )
;

CREATE TABLE shopping_basket
      (id                   BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY
      ,user_name            VARCHAR(255)
      ,basket_status        VARCHAR(50)
      )
;

CREATE TABLE product_order
      (id                   BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY
      ,basket_id            BIGINT          NOT NULL
      ,product_id           VARCHAR(255)
      ,quantity             BIGINT
      )
;

CREATE TABLE completed_order
      (id                   BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY
      ,basket_id            BIGINT          NOT NULL
      ,vat_number           VARCHAR(255)
      )
;