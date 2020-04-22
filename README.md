#Pedidos_Front-BackEnd

Aplicación Web Front/Back End para procesos CRUD con patrón MVC (JSF + IceFaces, Hibernate, Spring). No se han utilizado gestores de dependencias, todas las librerías están incluidas en el proyecto. 
Las estructura de la BD se corresponde con los DTOs de la aplicación, se compone de 4 tablas: Clientes, Articulos, Pedidos y Linea_Pedido. 
Para configurar la conexión a BD cambiar parámetros de conexión en com.spring.datos-conexion.properties y com.config.hibernate.cfg.xml
Tanto al borrar o modificar un pedido, como un artículo, se borran y modifican sus líneas asociadas. 
No se ha incluido la GUI para la tabla de Clientes aún, solo el DTO simplificado y el DAO. Se ha utilizado una plantilla para el menú y un CSS para el posicionamiento.

![Screenshot](Snap2.jpg)
![Screenshot](Snap3.jpg)
![Screenshot](Snap5.jpg)
![Screenshot](Snap6.jpg)
