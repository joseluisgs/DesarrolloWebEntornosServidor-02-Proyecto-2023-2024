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
VALUES ('DEPORTES'),
       ('COMIDA'),
       ('BEBIDA'),
       ('COMPLEMENTOS'),
       ('OTROS');


-- Datos de ejemplo PRODUCTOS
INSERT INTO PRODUCTOS (marca, modelo, descripcion, precio, imagen, categoria_id, stock, uuid)
VALUES ('Nike', 'Modelo1', 'Descripción1', 10.99, 'https://via.placeholder.com/150', 1, 5, UUID()),
       ('Adidas', 'Modelo2', 'Descripción2', 19.99, 'https://via.placeholder.com/150', 5, 10, UUID()),
       ('Nike', 'Modelo3', 'Descripción3', 15.99, 'https://via.placeholder.com/150', 1, 2, UUID()),
       ('Nike', 'Modelo4', 'Descripción4', 25.99, 'https://via.placeholder.com/150', 5, 8, UUID()),
       ('Adidas', 'Modelo5', 'Descripción5', 12.99, 'https://via.placeholder.com/150', 5, 3, UUID());

-- Datos de ejemplo USUARIO
-- Contraseña: Admin1
insert into USUARIOS (nombre, apellidos, username, email, password)
values ('Admin', 'AdminAdmin', 'admin', 'admin@prueba.net',
        '$2a$10$vPaqZvZkz6jhb7U7k/V/v.5vprfNdOnh4sxi/qpPRkYTzPmFlI9p2');

insert into USER_ROLES (user_id, roles)
values (1, 'USER');
insert into USER_ROLES (user_id, roles)
values (1, 'ADMIN');