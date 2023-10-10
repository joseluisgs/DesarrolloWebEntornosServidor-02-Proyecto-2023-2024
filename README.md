# DesarrolloWebEntornosServidor-02-Proyecto-2023-2024
Desarrollo Web en Entornos Servidor - 02 Programación de servicios y apps web en JVM Proyecto. 2DAW. Curso 2023-2024

# Proyecto
Este proyecto es una muestra de lo que se ha ido viendo en clase siguiendo los pasos del [repositorio del tema](https://github.com/joseluisgs/DesarrolloWebEntornosServidor-02-2023-2024).

Podrás seguir sus pasos commit a commit o en las ramas indicadas por clase.

# Problema
Vamos a crear una API REST y página web de una tienda de productos
- Tenemos una serie de productos con sus atributos
- Sabemos que todos los prodyctos pertenecen a una categoria
- Hay usuarios en el sistema, que pueden ser clientes o administradores
- Los clientes hacen pedidos
- Los pedidos esta formados por líeneas de pedidos
- Cada línea de pedido está formada por un producto.

## Elementos del dominio
Entidad: Usuario
- Atributos: ID_Usuario, Nombre, Dirección, Tipo, etc.
- Relaciones: Realiza uno o varios Pedidos

Entidad: Pedido
- Atributos: ID_Usiario, Fecha, Total, etc.
- Relaciones: Es realizado por un Cliente, Está compuesto por una o varias Líneas de pedido

Entidad: Línea de pedido
- Atributos: ID_Línea, Cantidad, Subtotal, etc.
- Relaciones: Está formada por un Producto

Entidad: Producto
- Atributos: ID_Producto, Nombre, Precio, etc.
- Relaciones: Pertenece a una Categoría

Entidad: Categoría
- Atributos: ID_Categoría, Nombre
- Relaciones: Tiene varios Productos

## Diagrama
Usuario 1 ----- * Pedido 1 -----> * Línea de Pedido * -----> 1 Producto * ----- Categoría

## Autor

Codificado con :sparkling_heart: por [José Luis González Sánchez](https://twitter.com/JoseLuisGS_)

[![Twitter](https://img.shields.io/twitter/follow/JoseLuisGS_?style=social)](https://twitter.com/JoseLuisGS_)
[![GitHub](https://img.shields.io/github/followers/joseluisgs?style=social)](https://github.com/joseluisgs)
[![GitHub](https://img.shields.io/github/stars/joseluisgs?style=social)](https://github.com/joseluisgs)

### Contacto

<p>
  Cualquier cosa que necesites házmelo saber por si puedo ayudarte 💬.
</p>
<p>
 <a href="https://joseluisgs.dev" target="_blank">
        <img src="https://joseluisgs.github.io/img/favicon.png" 
    height="30">
    </a>  &nbsp;&nbsp;
    <a href="https://github.com/joseluisgs" target="_blank">
        <img src="https://distreau.com/github.svg" 
    height="30">
    </a> &nbsp;&nbsp;
        <a href="https://twitter.com/JoseLuisGS_" target="_blank">
        <img src="https://i.imgur.com/U4Uiaef.png" 
    height="30">
    </a> &nbsp;&nbsp;
    <a href="https://www.linkedin.com/in/joseluisgonsan" target="_blank">
        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/LinkedIn_logo_initials.png/768px-LinkedIn_logo_initials.png" 
    height="30">
    </a>  &nbsp;&nbsp;
    <a href="https://g.dev/joseluisgs" target="_blank">
        <img loading="lazy" src="https://googlediscovery.com/wp-content/uploads/google-developers.png" 
    height="30">
    </a>  &nbsp;&nbsp;
<a href="https://www.youtube.com/@joseluisgs" target="_blank">
        <img loading="lazy" src="https://upload.wikimedia.org/wikipedia/commons/e/ef/Youtube_logo.png" 
    height="30">
    </a>  
</p>

## Licencia de uso

Este repositorio y todo su contenido está licenciado bajo licencia **Creative Commons**, si desea saber más, vea
la [LICENSE](https://joseluisgs.dev/docs/license/). Por favor si compartes, usas o modificas este proyecto cita a su
autor, y usa las mismas condiciones para su uso docente, formativo o educativo y no comercial.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Licencia de Creative Commons" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br /><span xmlns:dct="http://purl.org/dc/terms/" property="dct:title">
JoseLuisGS</span>
by <a xmlns:cc="http://creativecommons.org/ns#" href="https://joseluisgs.dev/" property="cc:attributionName" rel="cc:attributionURL">
José Luis González Sánchez</a> is licensed under
a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons
Reconocimiento-NoComercial-CompartirIgual 4.0 Internacional License</a>.<br />Creado a partir de la obra
en <a xmlns:dct="http://purl.org/dc/terms/" href="https://github.com/joseluisgs" rel="dct:source">https://github.com/joseluisgs</a>.
