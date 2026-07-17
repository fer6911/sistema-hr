# AtlasHR - Dashboard de Empleados, Beneficios y Datos Externos

Plataforma fullstack desarrollada como parte de la prueba técnica para **Symplifica**. AtlasHR es un dashboard unificado que permite gestionar empleados, asociar beneficios y enriquecer la información del empleado con datos geográficos provenientes de la API externa de **OpenStreetMap (Nominatim)**.

## Descripción del Proyecto

El sistema consta de dos partes principales:

- **Backend (Spring Boot)** — API REST construida bajo arquitectura **Hexagonal (Ports & Adapters)**. Gestiona el CRUD de empleados y beneficios. Consume la API de Nominatim en formato XML para obtener coordenadas geográficas (latitud/longitud) basadas en la ciudad del empleado.
- **Frontend (Nuxt 4 / Vue 3)** — Dashboard moderno que visualiza la lista de empleados, permite crear registros, y al ver el detalle muestra los beneficios asociados junto con la información geográfica obtenida del backend.

---

## Stack Tecnológico

### Backend

| Capa            | Tecnología              |
| --------------- | ----------------------- |
| Framework       | Spring Boot 4.1 / Java 21 |
| Build Tool      | Gradle                  |
| Arquitectura    | Hexagonal (Ports & Adapters) |
| Autenticación   | JWT (jjwt) + Spring Security |
| API Externa     | Nominatim (OpenStreetMap) — XML |
| Pruebas         | JUnit 5                 |

### Frontend

| Capa                | Tecnología                  |
| ------------------- | --------------------------- |
| Framework           | Nuxt 4 / Vue 3              |
| State Management    | Pinia + persistedstate      |
| Data Fetching       | TanStack Vue Query          |
| HTTP Client         | Axios                       |
| Styling             | Tailwind CSS v4 + daisyUI   |
| Validación          | Zod                         |
| Notificaciones      | vue-sonner                  |
| Package Manager     | pnpm                        |

---

## Requisitos Previos

### Generales

- **Git**

### Backend

- **Java 21** (JDK)
- **Gradle** (se incluye el wrapper `gradlew` en el proyecto)
- **PostgreSQL** (base de datos)

### Frontend

- **Node.js** >= 18.x
- **pnpm** >= 9.x

---

## Estructura del Proyecto

```
atlashr/
├── back/                          # Backend — Spring Boot (Hexagonal)
│   ├── build.gradle               # Dependencias y configuración de Gradle
│   ├── gradlew / gradlew.bat      # Wrapper de Gradle
│   └── src/
│       ├── main/java/com/atlashr/atlas_hr/
│       │   ├── AtlasHrApplication.java
│       │   ├── domain/
│       │   │   ├── model/         # Entidades de dominio
│       │   │   └── exception/     # Excepciones de dominio
│       │   ├── application/
│       │   │   ├── dto/           # Data Transfer Objects
│       │   │   ├── ports/in/      # Puertos de entrada (use cases)
│       │   │   ├── ports/out/     # Puertos de salida (persistencia, JWT, etc.)
│       │   │   └── service/       # Casos de uso
│       │   └── infrastructure/
│       │       ├── config/        # ApiResponse, GlobalResponseWrapper, etc.
│       │       ├── input/rest/    # Controllers REST
│       │       ├── output/        # Adapters (persistencia, API externa)
│       │       └── security/      # JWT, Security, Filtros
│       ├── main/resources/
│       │   └── application.properties
│       └── test/java/com/atlashr/atlas_hr/
│
├── front/                         # Frontend — Nuxt 4 (Vue 3)
│   ├── app/
│   │   ├── assets/css/main.css    # Estilos globales (Tailwind v4 + daisyUI)
│   │   ├── components/layout/     # Navbar, Footer
│   │   ├── features/              # Módulos por dominio (feature-sliced)
│   │   ├── layouts/               # Layouts de página
│   │   ├── middleware/             # Middleware de rutas
│   │   ├── pages/                 # Rutas de la aplicación
│   │   ├── plugins/               # Plugins de Nuxt
│   │   └── shared/                # Componentes, composables y utilidades compartidos
│   ├── public/                    # Archivos estáticos
│   ├── nuxt.config.ts             # Configuración de Nuxt
│   └── .env                       # Variables de entorno (no commitear)
│
└── README.md
```

### Arquitectura Hexagonal — Backend

El backend sigue el patrón **Hexagonal (Ports & Adapters)**, separando la lógica de negocio de los detalles de infraestructura:

- **`domain/`** — Entidades, value objects y puertos (interfaces) que definen los contratos del sistema. No depende de ninguna capa externa.
- **`application/`** — Casos de uso que orquestan la lógica del negocio, invocando los puertos definidos en el dominio.
- **`infrastructure/`** — Adaptadores que implementan los puertos: controladores REST, clientes HTTP (Nominatim), y repositorios de persistencia.

---

## Instalación y Ejecución

### 1. Clonar el repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd atlashr
```

### 2. Backend (Spring Boot)

```bash
cd back

# Crear la base de datos (requiere PostgreSQL corriendo)
psql -U postgres -c "CREATE DATABASE atlashr;"

# Ejecutar en modo desarrollo
./gradlew bootRun

# Ejecutar pruebas
./gradlew test
```

El servidor backend queda disponible en `http://localhost:8080`.

### 3. Frontend (Nuxt 4)

```bash
cd front

# Instalar dependencias
pnpm install

# Ejecutar en modo desarrollo (hot-reload)
pnpm dev
```

El servidor de desarrollo queda disponible en `http://localhost:3000`.

> **Nota:** El frontend necesita el backend corriendo en `http://localhost:8080` para funcionar correctamente.

---

## Variables de Entorno

### Frontend

Crea o edita el archivo `.env` en la raíz de `front/`:

```env
NUXT_API_URL=http://localhost:8080
VITE_API_URL=http://localhost:8080
```

| Variable       | Descripción                       | Default                 |
| -------------- | --------------------------------- | ----------------------- |
| `NUXT_API_URL` | URL base del backend API (server) | `http://localhost:8080` |
| `VITE_API_URL` | URL base del backend API (client) | `http://localhost:8080` |

### Backend

| Variable        | Descripción                       | Default                 |
| --------------- | --------------------------------- | ----------------------- |
| `DB_URL`        | URL de conexión a PostgreSQL      | `jdbc:postgresql://localhost:5432/atlashr` |
| `DB_USER`       | Usuario de la BD                  | `postgres`              |
| `DB_PASS`       | Contraseña de la BD               | `admin`                 |
| `JWT_SECRET_KEY`| Secreto para firmar tokens JWT (Base64) | Valor por defecto para desarrollo |

El archivo `application.properties` se encuentra en `back/src/main/resources/`. Por defecto el backend corre en el puerto `8080`.

---

## API Endpoints

El backend expone los siguientes endpoints REST:

### Autenticación (públicos)

| Método | Endpoint              | Descripción                                     |
| ------ | --------------------- | ----------------------------------------------- |
| `POST` | `/api/auth/register`  | Registrar nuevo usuario                         |
| `POST` | `/api/auth/login`     | Login — retorna JWT en header `Authorization`   |

### Empleados (requieren JWT)

| Método   | Endpoint                      | Descripción                          |
| -------- | ----------------------------- | ------------------------------------ |
| `GET`    | `/api/empleados`              | Listar todos los empleados           |
| `POST`   | `/api/empleados`              | Crear un nuevo empleado              |
| `GET`    | `/api/empleados/{id}`         | Obtener detalle de un empleado       |

### Beneficios (requieren JWT)

| Método   | Endpoint                       | Descripción                          |
| -------- | ------------------------------ | ------------------------------------ |
| `GET`    | `/api/beneficios/{empleadoId}` | Listar beneficios de un empleado     |
| `POST`   | `/api/beneficios`              | Asociar un beneficio a un empleado   |
| `DELETE` | `/api/beneficios/{id}`         | Eliminar un beneficio                |

### Integración con Nominatim

El backend consulta la API de **Nominatim (OpenStreetMap)** en formato XML utilizando la ciudad del empleado como parámetro de búsqueda. La respuesta se parsea para extraer las coordenadas geográficas (latitud/longitud) y el nombre completo del lugar, las cuales se incluyen en la respuesta de beneficios al frontend.

---

## Pruebas

### Backend

```bash
cd back
./gradlew test
```

Ejecuta las pruebas unitarias con JUnit 5. La cobertura incluye la lógica de negocio, el parseo del XML de Nominatim y los controladores.

### Frontend

```bash
cd front
pnpm test
```

---

## Docker (Opcional)

Si se encuentra disponible el `docker-compose.yml` en la raíz del proyecto:

```bash
docker-compose up --build
```

Esto levantará tanto el backend (puerto 8080) como el frontend (puerto 3000).

---

## Licencia

Proyecto privado — Prueba Técnica Symplifica.
