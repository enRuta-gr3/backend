# backend
Este repositorio contiene el backend del sistema enRuta. Proyecto de carrera, Tecnólogo Informático Uruguay 2025

# En Ruta - Backend



## 🚀 Tecnologías utilizadas

- Spring Boot (versión 3.4.7)
- Java 17
- Maven
- Base de datos: PostgreSQL
- Otras tecnologías: Spring Data JPA, Spring Security
- IDEs utilizados:
  - Spring Tool Suite > https://spring.io/tools
  - Intellij IDEA
  
## Cómo comenzar

### Requisitos
- JDK 17+
- Maven 3.8+
- PostgreSQL

### Instalación

1. Clonar el repositorio:
   ```bash
   git clone git@github.com:enRuta-gr3/backend.git
   
2. Limpiar el proyecto
   ```bash
   mvn clean install
   
3. Correr la aplicación
   si tienes maven instalado globalmente
   ```bash
   ./mvnw spring-boot:run
   
También se puede ejecutar desde el runner del IDE.
   
## 🌐 Configuración de variables de entorno

Para ejecutar la aplicación correctamente y conectarse a la base de datos en la nube, es necesario definir las siguientes variables de entorno. Estas permiten mantener las credenciales seguras y evitar su exposición en el repositorio.

### 🔐 Variables requeridas:

| Variable          | Descripción                                       | Ejemplo                  |
|-------------------|---------------------------------------------------|--------------------------|
| API_KEY           | Key para conectarse con los servicios de supabase | `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inp2eW51d21yZm1rdHF3aGRqcG9lIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDQzMTM3OTMsImV4cCI6MjA1OTg4OTc5M30.T7zfUyRGDl7lctJyJ98TWrp1crjzlkx5VmX-r_x_t4c`         |
| DATABASE_URL      | URL de acceso a la base de datos                  | `jdbc:postgresql://db.zvynuwmrfmktqwhdjpoe.supabase.co:5432/postgres?sslmode\=require`              |
| DATABASE_USER     | Usuario de conexión con la base de datos          | `postgres`           |
| DATABASE_PASSWORD | Contraseña del usuario de la base de datos        | `8P8WFmTzu4VuDlW0`           |

### 📂 ¿Cómo definirlas?
1. Puedes definirlas en la configuración del Runner de tu aplicación.

2. También en windows a través del powershell:
   ```bash
   $env:API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
   $env:DATABASE_URL = "jdbc:postgresql://db.zvynuwmrfmktqwhdjpoe.supabase.co:5432/postgres?sslmode\=require"
   $env:DATABASE_USER = "postgres"
   $env:DATABASE_PASSWORD = "8P8WFmTzu4VuDlW0"
   

La clase que contiene el main es EnRutaBackendApplication.

✍️ Autores
- Franco Pirotto
- Matias Bidarte
- Paola Benedictti
