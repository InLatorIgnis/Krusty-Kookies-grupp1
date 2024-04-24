-- SQL script to create the tables necessary for the procject in EDA216.
-- MySQL version.
--
-- Creates the tables Storage, Cookie, Pallet, customers, StorageUpdate, IngredientInCookie, orders
-- pallet_Delivered,  and orderSpec.
-- populates them with (simulated) data.
--
-- We disable foreign key checks temporarily so we can delete the
-- tables in arbitrary order, and so insertion is faster.

SET FOREIGN_KEY_CHECKS = 0;

-- Drop the tables if they already exist.
DROP TABLE IF EXISTS pallet_Delivered;
DROP TABLE IF EXISTS orderSpec;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS ingredientInCookies;
DROP TABLE IF EXISTS storageUpdates;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS pallets;
DROP TABLE IF EXISTS cookies;
DROP TABLE IF EXISTS storages;

-- Create the tables.

CREATE TABLE storages (
  ingredient_name VARCHAR(30) NOT NULL,
  storage_amount INT NOT NULL,
  storage_unit VARCHAR(10) NOT NULL,
  PRIMARY KEY (ingredient_name)
);

CREATE TABLE cookies (
  name VARCHAR(30) NOT NULL,
  PRIMARY KEY (name)
);

CREATE TABLE pallets (
  Pallet_id INT NOT NULL AUTO_INCREMENT,
  productionDate DATE NOT NULL,
  blocked BOOLEAN NOT NULL,
  location VARCHAR(20) NOT NULL,
  name VARCHAR(30) NOT NULL,
  PRIMARY KEY (Pallet_id),
  FOREIGN KEY (name) REFERENCES cookies(name)
);

CREATE TABLE customers (
  customer_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(30) NOT NULL,
  address VARCHAR(50) NOT NULL,
  PRIMARY KEY (customer_id)
);

CREATE TABLE storageUpdates (
  id INT NOT NULL AUTO_INCREMENT,
  updateTime DATE NOT NULL,
  updateAmount INT NOT NULL,
  ingredient_name VARCHAR(30) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (ingredient_name) REFERENCES storages(ingredient_name) ON DELETE CASCADE
);

CREATE TABLE ingredientInCookies (
  Quantity INT NOT NULL,
  Unit VARCHAR(10) NOT NULL, 
  ingredient_name VARCHAR(30) NOT NULL,
  cookie_name VARCHAR(30) NOT NULL,
  FOREIGN KEY (ingredient_name) REFERENCES storages(ingredient_name),
  FOREIGN KEY (cookie_name) REFERENCES cookies(name)
);

CREATE TABLE orders (
  Order_id INT NOT NULL AUTO_INCREMENT,
  customer_id INT NOT NULL,
  PRIMARY KEY (Order_id),
  FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE pallet_Delivered (
  Delivered_date DATE NOT NULL,
  Pallet_id INT NOT NULL,
  Order_id INT NOT NULL,
  FOREIGN KEY (Pallet_id) REFERENCES pallets(Pallet_id),
  FOREIGN KEY (Order_id) REFERENCES orders(Order_id)
);

CREATE TABLE orderSpec (
  Amount INT NOT NULL,
  cookie_name VARCHAR(30) NOT NULL,
  Order_id INT NOT NULL,
  FOREIGN KEY (cookie_name) REFERENCES cookies(name),
  FOREIGN KEY (Order_id) REFERENCES orders(Order_id)
);

-- We will do a lot of inserts, so we start a transaction to make it faster.

start transaction;

-- Populate the  customers table.

INSERT INTO customers (name, Address) VALUES 
('Bjudkakor AB', 'Ystad'),
('Finkakor AB', 'Helsingborg'),
('Gästkakor AB', 'Hässleholm'),
('Kaffebröd AB', 'Landskrona'),
('Kalaskakor AB', 'Trelleborg'),
('Partykakor AB', 'Kristianstad'),
('Skånekakor AB', 'Perstorp'),
('Småbröd AB', 'Malmö');

-- Populate the cookies table.

INSERT INTO cookies (name) VALUES
('Almond delight'),
('Amneris'),
('Berliner'),
('Nut cookie'),
('Nut ring'),
('Tango');


-- Populate the storages table
INSERT INTO storages (ingredient_name, storage_amount, storage_unit)
VALUES 
    ('Butter', 5000, 'g'),
    ('Chopped almonds', 5000, 'g'),
    ('Cinnamon', 5000, 'g'),
    ('Flour', 5000, 'g'),
    ('Sugar', 5000, 'g'),
    ('Eggs', 5000, 'g'),
    ('Marzipan', 5000, 'g'),
    ('Potato starch', 5000, 'g'),
    ('Wheat flour', 5000, 'g'),
    ('Chocolate', 5000, 'g'),
    ('Icing sugar', 5000, 'g'),
    ('Vanilla sugar', 5000, 'g'),
    ('Bread crumbs', 5000, 'g'),
    ('Egg whites', 5000, 'ml'),
    ('Fine-ground nuts', 5000, 'g'),
    ('Ground, roasted nuts', 5000, 'g'),
    ('Roasted, chopped nuts', 5000, 'g'),
    ('Sodium bicarbonate', 5000, 'g'),
    ('Vanilla', 5000, 'g');



-- Populate the IngredientInCookie table.

-- Ingredients for Almond delight
INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES
(400, 'g', 'Butter', 'Almond delight'),
(279, 'g', 'Chopped almonds', 'Almond delight'),
(10, 'g', 'Cinnamon', 'Almond delight'),
(400, 'g', 'Flour', 'Almond delight'),
(270, 'g', 'Sugar', 'Almond delight');

-- Ingredients for Amneris
INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES
(250, 'g', 'Butter', 'Amneris'),
(250, 'g', 'Eggs', 'Amneris'),
(750, 'g', 'Marzipan', 'Amneris'),
(25, 'g', 'Potato starch', 'Amneris'),
(25, 'g', 'Wheat flour', 'Amneris');

-- Ingredients for Berliner
INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES
(250, 'g', 'Butter', 'Berliner'),
(50, 'g', 'Chocolate', 'Berliner'),
(50, 'g', 'Eggs', 'Berliner'),
(350, 'g', 'Flour', 'Berliner'),
(100, 'g', 'Icing sugar', 'Berliner'),
(5, 'g', 'Vanilla sugar', 'Berliner');

-- Ingredients for Nut cookie
INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES
(125, 'g', 'Bread crumbs', 'Nut cookie'),
(50, 'g', 'Chocolate', 'Nut cookie'),
(350, 'ml', 'Egg whites', 'Nut cookie'),
(750, 'g', 'Fine-ground nuts', 'Nut cookie'),
(625, 'g', 'Ground, roasted nuts', 'Nut cookie'),
(375, 'g', 'Sugar', 'Nut cookie');

-- Ingredients for Nut ring
INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES
(450, 'g', 'Butter', 'Nut ring'),
(450, 'g', 'Flour', 'Nut ring'),
(190, 'g', 'Icing sugar', 'Nut ring'),
(225, 'g', 'Roasted, chopped nuts', 'Nut ring');

-- Ingredients for Tango
INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES
(200, 'g', 'Butter', 'Tango'),
(300, 'g', 'Flour', 'Tango'),
(4, 'g', 'Sodium bicarbonate', 'Tango'),
(250, 'g', 'Sugar', 'Tango'),
(2, 'g', 'Vanilla', 'Tango');

-- Populate the pallets table

INSERT INTO pallets
(`productionDate`, `blocked`, `location`, `name`) VALUES
(CURDATE(), 0, '07', 'Almond delight');

INSERT INTO pallets
(`productionDate`, `blocked`, `location`, `name`) VALUES
(CURDATE()-1, 1, '42', 'Tango');

-- Commit the transaction.

commit;

-- And re-enable foreign key checks.

set FOREIGN_KEY_CHECKS = 1;

