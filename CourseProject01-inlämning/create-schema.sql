set FOREIGN_KEY_CHECKS = 0;

-- Drop the tables if they already exist.

drop table if exists Storages;
drop table if exists Cookies;
drop table if exists Pallets;
drop table if exists Customers;
drop table if exists StorageUpdates;
drop table if exists IngredientInCookies;
drop table if exists Orders;
drop table if exists Pallet_Delivered;
drop table if exists OrderSpec;


-- Create the tables. The 'check' constraints are not effective in MySQL. 

CREATE TABLE Storages
(
  IngredientName VARCHAR(30) NOT NULL,
  StorageAmount INT NOT NULL,
  PRIMARY KEY (IngredientName)
);


CREATE TABLE Cookies
(
  Name VARCHAR(30) NOT NULL,
  PRIMARY KEY (Name)
);

CREATE TABLE Pallets
(
  Pallet_id INT NOT NULL AUTO_INCREMENT,
  productionDate DATE NOT NULL,
  Blocked BOOLEAN NOT NULL,
  Location VARCHAR(20) NOT NULL,
  Name VARCHAR(30) NOT NULL,
  PRIMARY KEY (Pallet_id),
  FOREIGN KEY (Name) REFERENCES Cookie(Name)
);

CREATE TABLE Customers
(
  Customer_id INT NOT NULL AUTO_INCREMENT,
  Name VARCHAR(30) NOT NULL,
  Address VARCHAR(50) NOT NULL,
  PRIMARY KEY (Customer_id)
);

CREATE TABLE StorageUpdates
(
  id INT NOT NULL AUTO_INCREMENT,
  UpdateTime DATE NOT NULL,
  UpdateAmount INT NOT NULL,
  IngredientName VARCHAR(30) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (IngredientName) REFERENCES Storage(IngredientNames)
);

CREATE TABLE IngredientInCookies
(
  Quantity INT NOT NULL,
  Unit VARCHAR(10) NOT NULL, 
  IngredientName VARCHAR(30) NOT NULL,
  Name VARCHAR(30) NOT NULL,
  FOREIGN KEY (IngredientName) REFERENCES Storage(IngredientName),
  FOREIGN KEY (Name) REFERENCES Cookie(Name)
);

CREATE TABLE Orders
(
  Order_id INT NOT NULL AUTO_INCREMENT,
  Customer_id INT NOT NULL,
  PRIMARY KEY (Order_id),
  FOREIGN KEY (Customer_id) REFERENCES Customers(Customer_id)
);

CREATE TABLE Pallet_Delivered
(
  Delivered_date DATE NOT NULL,
  Pallet_id INT NOT NULL,
  Order_id INT NOT NULL,
  FOREIGN KEY (Pallet_id) REFERENCES Pallet(Pallet_id),
  FOREIGN KEY (Order_id) REFERENCES Orders(Order_id)
);

CREATE TABLE OrderSpec
(
  Amount INT NOT NULL,
  Name VARCHAR(30) NOT NULL,
  Order_id INT NOT NULL,
  FOREIGN KEY (Name) REFERENCES Cookie(Name),
  FOREIGN KEY (Order_id) REFERENCES Orders(Order_id)
);