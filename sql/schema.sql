SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP DATABASE IF EXISTS biblieria;
CREATE DATABASE biblieria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'biblieria_app'@'localhost' IDENTIFIED BY 'biblieria123';
CREATE USER IF NOT EXISTS 'biblieria_app'@'127.0.0.1' IDENTIFIED BY 'biblieria123';
GRANT ALL PRIVILEGES ON biblieria.* TO 'biblieria_app'@'localhost';
GRANT ALL PRIVILEGES ON biblieria.* TO 'biblieria_app'@'127.0.0.1';

USE biblieria;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    username VARCHAR(60) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN','CLIENTE') NOT NULL DEFAULT 'CLIENTE',
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE libros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(180) NOT NULL,
    autor VARCHAR(140) NOT NULL,
    anio INT,
    precio DECIMAL(10,2) NOT NULL DEFAULT 0,
    stock INT NOT NULL DEFAULT 0,
    descripcion TEXT,
    imagen_ruta VARCHAR(500),
    imagen_mime VARCHAR(100),
    fecha_alta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE contactos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL,
    asunto VARCHAR(120) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    total DECIMAL(10,2) NOT NULL DEFAULT 0,
    estado ENUM('CONFIRMADO','CANCELADO') NOT NULL DEFAULT 'CONFIRMADO',
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pedidos_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE pedido_lineas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    libro_id INT NOT NULL,
    titulo VARCHAR(180) NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    cantidad INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_lineas_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE,
    CONSTRAINT fk_lineas_libro FOREIGN KEY (libro_id) REFERENCES libros(id)
);

INSERT INTO usuarios(nombre, email, username, password_hash, rol) VALUES
('Administrador', 'admin@biblieria.local', 'admin', '120000:QmlibGllcmlhU2FsdDIwMjY=:lsIYazV1qf5jyBCJBmjFpe9CdaPiBOdibZWOrqtxkAk=', 'ADMIN'),
('Cliente Demo', 'cliente@biblieria.local', 'cliente', '120000:QmlibGllcmlhU2FsdDIwMjY=:lsIYazV1qf5jyBCJBmjFpe9CdaPiBOdibZWOrqtxkAk=', 'CLIENTE');

INSERT INTO libros(titulo, autor, anio, precio, stock, descripcion, imagen_ruta, imagen_mime) VALUES
('1984', 'George Orwell', 1949, 15.90, 12, 'Novela distópica clásica.', NULL, NULL),
('Cien años de soledad', 'Gabriel García Márquez', 1967, 20.50, 24, 'Realismo mágico y saga familiar.', NULL, NULL),
('Don Quijote de la Mancha', 'Miguel de Cervantes', 1605, 25.00, 8, 'Obra cumbre de la literatura española.', NULL, NULL),
('La sombra del viento', 'Carlos Ruiz Zafón', 2001, 16.90, 15, 'Misterio literario ambientado en Barcelona.', NULL, NULL);
