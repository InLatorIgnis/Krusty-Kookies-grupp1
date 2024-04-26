-- We will do a lot of inserts, so we start a transaction to make it faster.

start transaction;

-- Populate the  Customers table.

INSERT INTO Customers (Name, Address) VALUES 
('Bjudkakor AB', 'Ystad'),
('Finkakor AB', 'Helsingborg'),
('Gästkakor AB', 'Hässleholm'),
('Kaffebröd AB', 'Landskrona'),
('Kalaskakor AB', 'Trelleborg'),
('Partykakor AB', 'Kristianstad'),
('Skånekakor AB', 'Perstorp'),
('Småbröd AB', 'Malmö');

-- Populate the Cookies table.

INSERT INTO Cookies (Name) VALUES
('Almond delight'),
('Amneris'),
('Berliner'),
('Nut cookie'),
('Nut ring'),
('Tango');


-- Populate the IngredientInCookie table.

-- Ingredients for Almond delight
INSERT INTO IngredientInCookies (Quantity, Unit, IngredientName, Name) VALUES
(400, 'g', 'Butter', 'Almond delight'),
(279, 'g', 'Chopped almonds', 'Almond delight'),
(10, 'g', 'Cinnamon', 'Almond delight'),
(400, 'g', 'Flour', 'Almond delight'),
(270, 'g', 'Sugar', 'Almond delight');

-- Ingredients for Amneris
INSERT INTO IngredientInCookies (Quantity, Unit, IngredientName, Name) VALUES
(250, 'g', 'Butter', 'Amneris'),
(250, 'g', 'Eggs', 'Amneris'),
(750, 'g', 'Marzipan', 'Amneris'),
(25, 'g', 'Potato starch', 'Amneris'),
(25, 'g', 'Wheat flour', 'Amneris');

-- Ingredients for Berliner
INSERT INTO IngredientInCookies (Quantity, Unit, IngredientName, Name) VALUES
(250, 'g', 'Butter', 'Berliner'),
(50, 'g', 'Chocolate', 'Berliner'),
(50, 'g', 'Eggs', 'Berliner'),
(350, 'g', 'Flour', 'Berliner'),
(100, 'g', 'Icing sugar', 'Berliner'),
(5, 'g', 'Vanilla sugar', 'Berliner');

-- Ingredients for Nut cookie
INSERT INTO IngredientInCookies (Quantity, Unit, IngredientName, Name) VALUES
(125, 'g', 'Bread crumbs', 'Nut cookie'),
(50, 'g', 'Chocolate', 'Nut cookie'),
(350, 'ml', 'Egg whites', 'Nut cookie'),
(750, 'g', 'Fine-ground nuts', 'Nut cookie'),
(625, 'g', 'Ground, roasted nuts', 'Nut cookie'),
(375, 'g', 'Sugar', 'Nut cookie');

-- Ingredients for Nut ring
INSERT INTO IngredientInCookies (Quantity, Unit, IngredientName, Name) VALUES
(450, 'g', 'Butter', 'Nut ring'),
(450, 'g', 'Flour', 'Nut ring'),
(190, 'g', 'Icing sugar', 'Nut ring'),
(225, 'g', 'Roasted, chopped nuts', 'Nut ring');

-- Ingredients for Tango
INSERT INTO IngredientInCookies (Quantity, Unit, IngredientName, Name) VALUES
(200, 'g', 'Butter', 'Tango'),
(300, 'g', 'Flour', 'Tango'),
(4, 'g', 'Sodium bicarbonate', 'Tango'),
(250, 'g', 'Sugar', 'Tango'),
(2, 'g', 'Vanilla', 'Tango');


-- Commit the transaction.

commit;

-- And re-enable foreign key checks.

set FOREIGN_KEY_CHECKS = 1;