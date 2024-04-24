INSERT INTO customers (name, Address) VALUES 
('Bjudkakor AB', 'Ystad'),
('Finkakor AB', 'Helsingborg'),
('Gästkakor AB', 'Hässleholm'),
('Kaffebröd AB', 'Landskrona'),
('Kalaskakor AB', 'Trelleborg'),
('Partykakor AB', 'Kristianstad'),
('Skånekakor AB', 'Perstorp'),
('Småbröd AB', 'Malmö');
INSERT INTO cookies (name) VALUES
('Almond delight'),
('Amneris'),
('Berliner'),
('Nut cookie'),
('Nut ring'),
('Tango');
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
INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES
(400, 'g', 'Butter', 'Almond delight'),
(279, 'g', 'Chopped almonds', 'Almond delight'),
(10, 'g', 'Cinnamon', 'Almond delight'),
(400, 'g', 'Flour', 'Almond delight'),
(270, 'g', 'Sugar', 'Almond delight');
INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES
(250, 'g', 'Butter', 'Amneris'),
(250, 'g', 'Eggs', 'Amneris'),
(750, 'g', 'Marzipan', 'Amneris'),
(25, 'g', 'Potato starch', 'Amneris'),
(25, 'g', 'Wheat flour', 'Amneris');
INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES
(250, 'g', 'Butter', 'Berliner'),
(50, 'g', 'Chocolate', 'Berliner'),
(50, 'g', 'Eggs', 'Berliner'),
(350, 'g', 'Flour', 'Berliner'),
(100, 'g', 'Icing sugar', 'Berliner'),
(5, 'g', 'Vanilla sugar', 'Berliner');
INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES
(125, 'g', 'Bread crumbs', 'Nut cookie'),
(50, 'g', 'Chocolate', 'Nut cookie'),
(350, 'ml', 'Egg whites', 'Nut cookie'),
(750, 'g', 'Fine-ground nuts', 'Nut cookie'),
(625, 'g', 'Ground, roasted nuts', 'Nut cookie'),
(375, 'g', 'Sugar', 'Nut cookie');
INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES
(450, 'g', 'Butter', 'Nut ring'),
(450, 'g', 'Flour', 'Nut ring'),
(190, 'g', 'Icing sugar', 'Nut ring'),
(225, 'g', 'Roasted, chopped nuts', 'Nut ring');
INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES
(200, 'g', 'Butter', 'Tango'),
(300, 'g', 'Flour', 'Tango'),
(4, 'g', 'Sodium bicarbonate', 'Tango'),
(250, 'g', 'Sugar', 'Tango'),
(2, 'g', 'Vanilla', 'Tango');
INSERT INTO pallets
(`productionDate`, `blocked`, `location`, `name`) VALUES
(CURDATE(), 0, '07', 'Almond delight');
INSERT INTO pallets
(`productionDate`, `blocked`, `location`, `name`) VALUES
(CURDATE()-1, 1, '42', 'Tango');