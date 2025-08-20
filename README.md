# ShopLite

## Descripción
ShopLite es una aplicación web desarrollada en Java con Jakarta EE, diseñada para gestionar productos en una tienda en línea. Permite a los usuarios registrarse, iniciar sesión, ver productos y realizar acciones administrativas como crear, editar y eliminar productos. La aplicación utiliza PostgreSQL como base de datos y se despliega en WildFly.

## Características
- **Autenticación**: Registro e inicio de sesión de usuarios con roles `USER` y `ADMIN`.
- **Gestión de Productos**: 
  - Usuarios: Visualizan la lista de productos con paginación.
  - Administradores: Crean, editan y eliminan productos mediante una interfaz con modales.
- **Seguridad**: Filtros (`AuthFilter`, `AdminFilter`) aseguran que solo usuarios autenticados y administradores accedan a ciertas funcionalidades.
- **Base de Datos**: PostgreSQL (`localhost:5433`, esquema `public`) con tablas `users` (username, password, role) y `products` (id, name, price, stock).


## Uso
- **Usuarios**: Pueden ver productos en `/home` con paginación.
- **Administradores**: Pueden acceder a `/admin` para crear productos, y en `/home` usar los botones "Editar" (modal con datos pre-cargados) y "Eliminar" para gestionar productos.

## Notas
- Los endpoints `/admin/*` están protegidos por `AdminFilter` (solo ADMIN).
- Los endpoints `/home` requieren autenticación (`AuthFilter`).

## Usuarios de Pruebas
-ADMIN: admin/admin123
-USER: user/user123
