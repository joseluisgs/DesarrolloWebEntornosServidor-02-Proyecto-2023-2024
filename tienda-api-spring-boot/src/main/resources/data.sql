/*

Ejemplo de si queremos hacer las tablas a mano y no con JPA
-- Borra la tabla si existe
DROP TABLE IF EXISTS PRODUCTOS;

-- Crea la tabla
CREATE TABLE IF NOT EXISTS PRODUCTOS (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   marca VARCHAR(255) NOT NULL,
   modelo VARCHAR(255) NOT NULL,
   descripcion VARCHAR(255) NOT NULL,
   precio DOUBLE DEFAULT 0 CHECK (precio >= 0),
   imagen TEXT DEFAULT 'https://via.placeholder.com/150',
   categoria VARCHAR(255) NOT NULL,
   stock INT DEFAULT 0 CHECK (stock >= 0),
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   uuid UUID NOT NULL,
   is_deleted BOOLEAN DEFAULT FALSE,
   UNIQUE (uuid)
);

*/

-- Datos de ejemplo CATEGORIAS
INSERT INTO CATEGORIAS (nombre)
VALUES ('DEPORTE'),
       ('COMIDA'),
       ('BEBIDA'),
       ('COMPLEMENTOS'),
       ('OTROS');


-- Datos de ejemplo PRODUCTOS
INSERT INTO PRODUCTOS (marca, modelo, descripcion, precio, imagen, categoria, stock, uuid)
VALUES ('Nike', 'Modelo1', 'Descripción1', 10.99, 'https://via.placeholder.com/150', 'DEPORTE', 5, UUID()),
       ('Adidas', 'Modelo2', 'Descripción2', 19.99, 'https://via.placeholder.com/150', 'OTROS', 10, UUID()),
       ('Nike', 'Modelo3', 'Descripción3', 15.99, 'https://via.placeholder.com/150', 'DEPORTE', 2, UUID()),
       ('Nike', 'Modelo4', 'Descripción4', 25.99, 'https://via.placeholder.com/150', 'OTROS', 8, UUID()),
       ('Adidas', 'Modelo5', 'Descripción5', 12.99, 'https://via.placeholder.com/150', 'OTROS', 3, UUID());