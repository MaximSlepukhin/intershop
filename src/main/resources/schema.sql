CREATE TABLE items (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       price DECIMAL(10, 2) NOT NULL,
                       img_path VARCHAR(255),
                       count INTEGER NOT NULL DEFAULT 0,
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO items (title, description, price, img_path, count) VALUES
                                                                   ('Ноутбук Lenovo', '15.6", 16 ГБ RAM, SSD 512 ГБ', 899.99, '/images/laptop1.jpg', 10),
                                                                   ('Смартфон Samsung', '6.5" AMOLED, 128 ГБ памяти', 699.50, '/images/phone1.jpg', 15),
                                                                   ('Наушники Sony', 'Беспроводные, шумоподавление', 199.99, '/images/headphones1.jpg', 20),
                                                                   ('Фотоаппарат Canon', 'Зеркальный, 24.2 МП', 1200.00, '/images/camera1.jpg', 5),
                                                                   ('Умные часы Apple', 'Watch Series 8, GPS', 429.99, '/images/watch1.jpg', 8);