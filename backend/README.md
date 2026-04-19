# Prueba Ufinet - Gestión de Autos (Backend)

API REST para registro y gestión de autos por usuario, construida con **Spring Boot 4**, **arquitectura hexagonal** y principios **SOLID**.

---

## Tecnologías

- Java 21
- Spring Boot 4.0.5
- Spring Security + OAuth2 Resource Server (JWT)
- Spring Data JPA + JPA Specifications
- SQL Server
- Lombok
- Maven

---

## Arquitectura Hexagonal

```
com.ufinet.autos
├── domain/                          # Núcleo de negocio (sin dependencias externas)
│   ├── model/                       # Entidades y value objects del dominio
│   ├── port/in/                     # Puertos de entrada (casos de uso)
│   ├── port/out/                    # Puertos de salida (repositorios, encoder, token)
│   └── exception/                   # Excepciones de dominio
│
├── application/                     # Orquestación de casos de uso
│   └── service/                     # AuthService, CarService
│
└── infrastructure/                  # Adaptadores externos
    ├── adapter/in/rest/             # Controllers, DTOs, mappers, exception handler
    ├── adapter/out/persistence/     # Entidades JPA, repositorios, specifications, adapters
    ├── security/                    # JWT (NimbusJwtEncoder), SecurityConfig, BCrypt
    └── config/                      # Inyección de beans de aplicación
```

### Principios SOLID

| Principio | Aplicación |
|---|---|
| **S** - Single Responsibility | Clases con una sola razón de cambio (AuthService, CarService, mappers separados) |
| **O** - Open/Closed | JPA Specifications permiten agregar filtros sin modificar código existente |
| **L** - Liskov Substitution | Los puertos (interfaces) son sustituibles por cualquier implementación |
| **I** - Interface Segregation | 4 use cases independientes en vez de un servicio monolítico |
| **D** - Dependency Inversion | El dominio define interfaces; la infraestructura las implementa |

---

## Requisitos previos

- Java 21+
- SQL Server (puerto 1433)
- Maven 3.9+ (incluido via wrapper `mvnw`)

---

## Configuración de la base de datos

1. Ejecutar el script `src/main/resources/schema.sql` en SQL Server para crear la base de datos y las tablas.

2. La conexión está configurada en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ufinet_autos_db;encrypt=true;trustServerCertificate=true
spring.datasource.username=ufinet_user
spring.datasource.password=${DB_PASSWORD:VE8#OpPbEG*EYXUCHkqD^D2BpDn67IWY}
```

> Se puede sobreescribir la contraseña con la variable de entorno `DB_PASSWORD`.

---

## Ejecución

```bash
./mvnw spring-boot:run
```

La API estará disponible en `http://localhost:8080/api/v1`.

---

## Endpoints

### Autenticación (públicos)

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/v1/auth/register` | Registrar usuario |
| POST | `/api/v1/auth/login` | Iniciar sesión |

**Body (ambos):**
```json
{
    "email": "demo@ufinet.com",
    "password": "password123"
}
```

**Respuesta:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "email": "demo@ufinet.com"
}
```

### Autos (requieren JWT)

Enviar header: `Authorization: Bearer <token>`

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/v1/cars` | Crear auto |
| GET | `/api/v1/cars` | Listar autos (paginado, con filtros) |
| GET | `/api/v1/cars/{id}` | Obtener auto por ID |
| PUT | `/api/v1/cars/{id}` | Actualizar auto |
| DELETE | `/api/v1/cars/{id}` | Eliminar auto |

**Body crear/actualizar:**
```json
{
    "brand": "Toyota",
    "model": "Corolla",
    "year": 2024,
    "licensePlate": "ABC-1234",
    "color": "Blanco",
    "photoUrl": "https://example.com/foto.jpg"
}
```

### Parámetros de búsqueda y filtrado

| Parámetro | Tipo | Descripción |
|---|---|---|
| `search` | String | Busca en placa y modelo |
| `brand` | String | Filtra por marca |
| `year` | Integer | Filtra por año |
| `page` | int | Número de página (default: 0) |
| `size` | int | Tamaño de página (default: 10) |
| `sortBy` | String | Campo de ordenamiento (default: id) |
| `sortDirection` | String | asc o desc (default: asc) |

**Ejemplo:** `GET /api/v1/cars?search=corolla&brand=Toyota&year=2024&page=0&size=10&sortBy=brand&sortDirection=asc`

**Respuesta paginada:**
```json
{
    "content": [ ... ],
    "page": 0,
    "size": 10,
    "totalElements": 5,
    "totalPages": 1,
    "last": true
}
```

---

## Colección Postman

Importar el archivo `postman/Ufinet-Autos-API.postman_collection.json` en Postman.

La colección guarda automáticamente el token JWT al ejecutar Register o Login, y lo usa en los demás requests.

---

## Estructura de la base de datos

```
users
├── id (BIGINT, PK, IDENTITY)
├── email (VARCHAR 255, UNIQUE)
├── password (VARCHAR 255)
├── created_at (DATETIME2)
└── updated_at (DATETIME2)

cars
├── id (BIGINT, PK, IDENTITY)
├── brand (VARCHAR 100)
├── model (VARCHAR 100)
├── year (INT)
├── license_plate (VARCHAR 20, UNIQUE)
├── color (VARCHAR 50)
├── photo_url (VARCHAR 500, nullable)
├── user_id (BIGINT, FK → users.id, CASCADE)
├── created_at (DATETIME2)
└── updated_at (DATETIME2)
```
