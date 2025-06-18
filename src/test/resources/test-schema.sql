CREATE TABLE IF NOT EXISTS orders (
                                      id SERIAL PRIMARY KEY,
                                      total_sum NUMERIC(10,2) NOT NULL
    );

CREATE TABLE IF NOT EXISTS items (
                                     id BIGSERIAL PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    img_path VARCHAR(255)
    );

CREATE TABLE  IF NOT EXISTS orders_items (
                                             order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    item_id BIGINT NOT NULL REFERENCES items(id) ON DELETE CASCADE,
    count INTEGER NOT NULL CHECK (count > 0),
    PRIMARY KEY (order_id, item_id)
    );
INSERT INTO items (title, description, price, img_path) VALUES
                                                            ('Ноутбук Lenovo', '15.6", 16 ГБ RAM, SSD 512 ГБ', 80000, 'images/lenovo.jpeg'),
                                                            ('Смартфон Samsung', '6.5" AMOLED, 128 ГБ памяти', 90000, 'images/samsung.jpeg'),
                                                            ('Наушники Sony', 'Беспроводные, шумоподавление', 20000, 'images/sony.jpeg'),
                                                            ('Фотоаппарат Canon', 'Зеркальный, 24.2 МП', 120000, 'images/canon.jpeg'),
                                                            ('Умные часы Apple', 'Watch Series 8, GPS', 40000, 'images/apple.jpeg'),
                                                            ('Планшет iPad Pro', '12.9", 256 ГБ, M1', 95000, 'images/ipad.jpeg'),
                                                            ('Монитор Dell', '27", 4K, IPS', 65000, 'images/dell.jpeg'),
                                                            ('Клавиатура Logitech', 'Механическая, RGB', 12000, 'images/logitech.jpeg'),
                                                            ('Мышь Razer', 'Игровая, 16000 DPI', 8000, 'images/razer.jpeg'),
                                                            ('Колонка JBL', 'Портативная, waterproof', 15000, 'images/jbl.jpeg'),
                                                            ('Электронная книга Kindle', '10", 32 ГБ', 25000, 'images/kindle.jpeg'),
                                                            ('Роутер TP-Link', 'Wi-Fi 6, 3000 Мбит/с', 18000, 'images/tplink.jpeg');
INSERT INTO orders (total_sum) VALUES
                                   (76000.00),
                                   (185000.50),
                                   (95000.00);