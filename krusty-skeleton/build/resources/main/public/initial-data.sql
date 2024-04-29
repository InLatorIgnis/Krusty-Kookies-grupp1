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
    ('Butter', 500000, 'g'),
    ('Chopped almonds', 500000, 'g'),
    ('Cinnamon', 500000, 'g'),
    ('Flour', 500000, 'g'),
    ('Sugar', 500000, 'g'),
    ('Eggs', 500000, 'g'),
    ('Marzipan', 500000, 'g'),
    ('Potato starch', 500000, 'g'),
    ('Wheat flour', 500000, 'g'),
    ('Chocolate', 500000, 'g'),
    ('Icing sugar', 500000, 'g'),
    ('Vanilla sugar', 500000, 'g'),
    ('Bread crumbs', 500000, 'g'),
    ('Egg whites', 500000, 'ml'),
    ('Fine-ground nuts', 500000, 'g'),
    ('Ground, roasted nuts', 500000, 'g'),
    ('Roasted, chopped nuts', 500000, 'g'),
    ('Sodium bicarbonate', 500000, 'g'),
    ('Vanilla', 500000, 'g');
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
