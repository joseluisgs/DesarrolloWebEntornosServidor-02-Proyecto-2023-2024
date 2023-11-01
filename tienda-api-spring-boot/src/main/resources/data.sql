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
INSERT INTO CATEGORIAS (id, nombre)
VALUES ('d69cf3db-b77d-4181-b3cd-5ca8107fb6a9', 'DEPORTES'),
       ('6dbcbf5e-8e1c-47cc-8578-7b0a33ebc154', 'COMIDA'),
       ('9def16db-362b-44c4-9fc9-77117758b5b0', 'BEBIDA'),
       ('8c5c06ba-49d6-46b6-85cc-8246c0f362bc', 'COMPLEMENTOS'),
       ('bb51d00d-13fb-4b09-acc9-948185636f79', 'OTROS');


-- Datos de ejemplo PRODUCTOS
INSERT INTO PRODUCTOS (marca, modelo, descripcion, precio, imagen, stock, uuid, categoria_id)
VALUES ('Nike', 'Modelo1', 'Descripción1', 10.99, 'https://via.placeholder.com/150', 5,
        '19135792-b778-441f-871e-d6e6096e0ddc',
        'd69cf3db-b77d-4181-b3cd-5ca8107fb6a9'),
       ('Adidas', 'Modelo2', 'Descripción2', 19.99, 'https://via.placeholder.com/150', 10,
        '662ed342-de99-45c6-8463-446989aab9c8',
        '6dbcbf5e-8e1c-47cc-8578-7b0a33ebc154'),
       ('Nike', 'Modelo3', 'Descripción3', 15.99, 'https://via.placeholder.com/150', 2,
        'b79182ad-91c3-46e8-90b9-268164596a72',
        'd69cf3db-b77d-4181-b3cd-5ca8107fb6a9'),
       ('Nike', 'Modelo4', 'Descripción4', 25.99, 'https://via.placeholder.com/150', 8,
        '4fa72b3f-dca2-4fd8-b803-dffacf148c10',
        '6dbcbf5e-8e1c-47cc-8578-7b0a33ebc154'),
       ('Adidas', 'Modelo5', 'Descripción5', 12.99, 'https://via.placeholder.com/150', 3,
        '1e2584d8-db52-45da-b2d6-4203637ea78e',
        '6dbcbf5e-8e1c-47cc-8578-7b0a33ebc154');

-- Datos de ejemplo USUARIOS
-- Contraseña: Admin1
insert into USUARIOS (nombre, apellidos, username, email, password)
values ('Admin', 'Admin Admin', 'admin', 'admin@prueba.net',
        '$2a$10$vPaqZvZkz6jhb7U7k/V/v.5vprfNdOnh4sxi/qpPRkYTzPmFlI9p2');

insert into USER_ROLES (user_id, roles)
values (1, 'USER');
insert into USER_ROLES (user_id, roles)
values (1, 'ADMIN');

-- Contraseña: User1
insert into USUARIOS (nombre, apellidos, username, email, password)
values ('User', 'User User', 'user', 'user@prueba.net',
        '$2a$12$RUq2ScW1Kiizu5K4gKoK4OTz80.DWaruhdyfi2lZCB.KeuXTBh0S.');
insert into USER_ROLES (user_id, roles)
values (2, 'USER');

-- Contraseña: Test1
insert into USUARIOS (nombre, apellidos, username, email, password)
values ('Test', 'Test Test', 'test', 'test@prueba.net',
        '$2a$10$Pd1yyq2NowcsDf4Cpf/ZXObYFkcycswqHAqBndE1wWJvYwRxlb.Pu');
insert into USER_ROLES (user_id, roles)
values (2, 'USER');

-- Contraseña: Otro1
insert into USUARIOS (nombre, apellidos, username, email, password)
values ('otro', 'Otro Otro', 'otro', 'otro@prueba.net',
        '$2a$12$3Q4.UZbvBMBEvIwwjGEjae/zrIr6S50NusUlBcCNmBd2382eyU0bS');
insert into USER_ROLES (user_id, roles)
values (3, 'USER');