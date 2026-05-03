# Bibliería - JSP + Servlets + JDBC + MariaDB/MySQL

Aplicación Java Web académica para una librería online con:

- JSP para las vistas.
- Servlets para controladores.
- JDBC para acceso a MariaDB/MySQL.
- Catálogo de libros con portadas.
- Registro, login y cierre de sesión.
- Roles `ADMIN` y `CLIENTE`.
- Panel de administración para gestionar libros y clientes.
- Carrito de compra y pedidos.
- Formulario de contacto guardado en base de datos.
- Contraseñas hasheadas con PBKDF2 + salt.

## Requisitos

- Java 11 o superior.
- Maven.
- Apache Tomcat 10.1.x.
- MariaDB o MySQL compatible.

> Tomcat 10 usa paquetes `jakarta.*`. Con Tomcat 9 habría que cambiar imports `jakarta.servlet.*` por `javax.servlet.*` y ajustar dependencias.

## Instalación rápida en Windows

1. Crear la base de datos:

```powershell
cmd /c "mysql --default-character-set=utf8mb4 -u root -p < sql\schema.sql"
```

Evita importar el SQL con `Get-Content .\sql\schema.sql | mysql ...` en Windows, porque la tubería de PowerShell puede convertir mal las tildes antes de que lleguen a MariaDB.

2. Configurar variables de entorno, opcional pero recomendable:

```powershell
$env:DB_URL = "jdbc:mariadb://127.0.0.1:3306/biblieria?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
$env:DB_USER = "biblieria_app"
$env:DB_PASS = "biblieria123"
$env:APP_UPLOAD_DIR = "C:\uploads-biblieria"
```

`APP_UPLOAD_DIR` se usa solo para guardar las portadas de libros subidas desde el panel de administración.

Para guardar las variables para futuras sesiones:

```powershell
[Environment]::SetEnvironmentVariable("DB_URL", "jdbc:mariadb://127.0.0.1:3306/biblieria?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", "User")
[Environment]::SetEnvironmentVariable("DB_USER", "biblieria_app", "User")
[Environment]::SetEnvironmentVariable("DB_PASS", "biblieria123", "User")
[Environment]::SetEnvironmentVariable("APP_UPLOAD_DIR", "C:\uploads-biblieria", "User")
```

Después de guardarlas, abre una nueva terminal para que Windows cargue los cambios.

3. Compilar:

```powershell
mvn clean package
```

4. Copiar el WAR en Tomcat:

```powershell
Copy-Item .\target\biblieria.war "$env:CATALINA_HOME\webapps\" -Force
```

Si no tienes configurada `CATALINA_HOME`, usa la ruta real de Tomcat:

```powershell
Copy-Item .\target\biblieria.war "C:\Program Files\Apache Software Foundation\Tomcat 10.1\webapps" -Force
```

5. Abrir:

```text
http://localhost:8080/biblieria/home
```

## Usuarios demo

```text
admin / admin123
cliente / admin123
```

## Rutas principales

- `/home` página inicial.
- `/catalogo` catálogo público.
- `/registro` crear cuenta de cliente.
- `/login` acceso.
- `/carrito` carrito de compra.
- `/pedidos` pedidos del usuario.
- `/contacto` formulario de contacto.
- `/admin/libros` panel de administración para usuarios `ADMIN`.
