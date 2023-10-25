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
});

// Nos conectamos a la base de datos world
db = db.getSiblingDB('tienda');

// Creamos la coleccion city
db.createCollection('pedidos');

// Insertamos los datos de la coleccion pedidos
db.pedidos.insertMany([
  {
    _id: ObjectId('6536518de9b0d305f193b5ef'),
    idUsuario: 1,
    cliente: {
      nombreCompleto: 'Juan Perez',
      email: 'juanperez@gmail.com',
      telefono: '+34123456789',
      direccion: {
        calle: 'Calle Mayor',
        numero: '10',
        ciudad: 'Madrid',
        provincia: 'Madrid',
        pais: 'España',
        codigoPostal: '28001',
      },
    },
    lineasPedido: [
      {
        idProducto: 2,
        precioProducto: 19.99,
        cantidad: 1,
        total: 19.99,
      },
      {
        idProducto: 3,
        precioProducto: 15.99,
        cantidad: 2,
        total: 31.98,
      },
    ],
    createdAt: '2023-10-23T12:57:17.3411925',
    updatedAt: '2023-10-23T12:57:17.3411925',
    isDeleted: false,
    totalItems: 3,
    total: 51.97,
    _class: 'Pedido',
  },
]);
