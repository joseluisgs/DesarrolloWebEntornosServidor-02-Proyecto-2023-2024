// Creamos el usuario administrador de la base de datos
// con sus daatos de conexion y los roles que tendra
db.createUser({
    user: 'admin',
    pwd: 'adminPassword123',
    roles: [
        {
            role: 'readWrite',
            db: 'tienda',
        },
    ],
})

// Nos conectamos a la base de datos world
db = db.getSiblingDB('tienda')

// Creamos la coleccion city
db.createCollection('pedidos')


// Insertamos los datos de la coleccion pedidos
db.pedidos.insertMany([
    {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "idUsuario": 1,
        "cliente": {
            "nombreCompleto": "Juan Perez",
            "email": "juanperez@gmail.com",
            "telefono": "+34123456789",
            "direccion": {
                "calle": "Calle Mayor",
                "numero": "10",
                "ciudad": "Madrid",
                "provincia": "Madrid",
                "pais": "España",
                "codigoPostal": "28001"
            }
        },
        "lineasPedido": [
            {
                "id": "123e4567-e89b-12d3-a456-426614174001",
                "idPedido": "123e4567-e89b-12d3-a456-426614174000",
                "idProducto": 2,
                "precioProducto": 19.99,
                "cantidad": 1
            },
            {
                "id": "123e4567-e89b-12d3-a456-426614174002",
                "idPedido": "123e4567-e89b-12d3-a456-426614174000",
                "idProducto": 3,
                "precioProducto": 15.99,
                "cantidad": 2
            }
        ]
    }
])