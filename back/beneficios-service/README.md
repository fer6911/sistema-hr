# Microservicio de Beneficios - Ruby on Rails

Microservicio independiente que gestiona el CRUD del Módulo de Beneficios y la integración con la API externa de **Nominatim (OpenStreetMap)** para obtener coordenadas geográficas.

Este servicio fue extraído del backend principal (Java/Spring Boot) como parte del Bonus Track de la prueba técnica, convirtiéndolo en un servicio completamente independiente con su propia base de datos.

## Descripción

El microservicio expone una API REST que permite:

1. **Crear** beneficios asociados a un empleado
2. **Listar** los beneficios de un empleado específico
3. **Editar** un beneficio existente
4. **Eliminar** un beneficio
5. **Consultar ubicación** de la ciudad del empleado vía Nominatim (XML + caché 24h)

## Stack Tecnológico

| Capa            | Tecnología                              |
| --------------- | --------------------------------------- |
| Framework       | Ruby on Rails 8.1 (API-only)            |
| Ruby            | >= 3.3.x                                |
| Base de datos   | PostgreSQL (independiente)              |
| API Externa     | Nominatim (OpenStreetMap) — XML         |
| Parseo XML      | Nokogiri                                |
| HTTP Client     | Net::HTTP (stdlib de Ruby)              |
| Pruebas         | RSpec + FactoryBot + WebMock            |
| Puerto          | 3000                                    |

## Requisitos Previos

- **Ruby** >= 3.3.x ([RubyInstaller](https://rubyinstaller.org/) para Windows)
- **Rails** >= 8.1 (`gem install rails`)
- **PostgreSQL** corriendo localmente
- **Bundler** (`gem install bundler`)

## Estructura

```
beneficios-service/
├── .ruby-version                  # Versión de Ruby
├── .rspec                         # Configuración de RSpec
├── Gemfile                        # Dependencias Ruby
├── Rakefile
├── bin/
│   ├── rails                      # Script de Rails
│   └── setup                      # Script de setup
├── config/
│   ├── application.rb             # Configuración principal (API-only)
│   ├── database.yml               # Conexión PostgreSQL
│   ├── routes.rb                  # Rutas en español
│   ├── puma.rb                    # Servidor web
│   ├── environments/
│   │   ├── development.rb
│   │   └── test.rb
│   └── initializers/
│       ├── cors.rb                # CORS (permite todos los orígenes)
│       └── nominatim.rb           # Configuración de Nominatim (URL, User-Agent, TTL)
├── app/
│   ├── models/
│   │   ├── application_record.rb  # Base para modelos
│   │   └── beneficio.rb           # Modelo con validaciones
│   ├── controllers/
│   │   ├── application_controller.rb
│   │   ├── concerns/
│   │   │   └── api_response.rb    # Envoltorio ApiResponse (error, message, data, errors)
│   │   └── api/v1/
│   │       └── beneficios_controller.rb  # 4 endpoints
│   ├── services/
│   │   ├── crear_beneficio_service.rb
│   │   ├── listar_beneficios_service.rb
│   │   ├── editar_beneficio_service.rb
│   │   ├── eliminar_beneficio_service.rb
│   │   └── nominatim_service.rb   # Integración con Nominatim (XML + caché)
│   └── serializers/
│       └── beneficio_serializer.rb
├── db/
│   ├── migrate/
│   │   └── 001_crear_beneficios.rb
│   └── seeds.rb
└── spec/
    ├── factories/
    │   └── beneficios.rb
    ├── models/
    │   └── beneficio_spec.rb
    ├── services/
    │   ├── crear_beneficio_service_spec.rb
    │   ├── editar_beneficio_service_spec.rb
    │   ├── eliminar_beneficio_service_spec.rb
    │   ├── listar_beneficios_service_spec.rb
    │   └── nominatim_service_spec.rb
    ├── requests/api/v1/
    │   └── beneficios_spec.rb
    ├── rails_helper.rb
    └── spec_helper.rb
```

## Instalación y Ejecución

### 1. Clonar e instalar dependencias

```bash
cd back/beneficios-service
bundle install
```

### 2. Configurar la base de datos

Asegúrate de que PostgreSQL esté corriendo. Edita `config/database.yml` si tus credenciales son diferentes:

```yaml
default: &default
  adapter: postgresql
  encoding: unicode
  pool: 5
  host: localhost
  username: postgres
  password: postgres    # ← Ajustar según tu configuración
```

### 3. Crear base de datos y ejecutar migraciones

```bash
ruby bin\rails db:create db:migrate
```

En Linux/Mac:
```bash
bin/rails db:create db:migrate
```

### 4. Cargar datos de ejemplo (opcional)

```bash
ruby bin\rails db:seed
```

### 5. Levantar el servidor

```bash
ruby bin\rails server
```

El microservicio queda disponible en `http://localhost:3000`.

### 6. Cerrar el servidor

Presiona `Ctrl+C` en la terminal.

## API Endpoints

Todos los endpoints retornan el formato `ApiResponse`:

```json
{
  "error": false,
  "message": "Operación realizada exitosamente",
  "data": { ... },
  "errors": []
}
```

### Crear beneficio

```
POST /api/v1/beneficios
Content-Type: application/json

{
  "empleado_id": 1,
  "nombre_beneficio": "Seguro médico",
  "monto": 150000
}
```

**Respuesta:**
```json
{
  "error": false,
  "message": "Beneficio registrado exitosamente",
  "data": {
    "id": 1,
    "empleado_id": 1,
    "nombre_beneficio": "Seguro médico",
    "monto": 150000.0
  },
  "errors": []
}
```

### Listar beneficios por empleado

```
GET /api/v1/beneficios/empleado/:empleado_id?ciudad=Bogota
```

El parámetro `ciudad` es opcional. Si se proporciona, se consulta Nominatim para obtener latitud/longitud.

**Respuesta con ubicación:**
```json
{
  "error": false,
  "message": "Beneficios listados exitosamente",
  "data": {
    "beneficios": [
      {
        "id": 1,
        "empleado_id": 1,
        "nombre_beneficio": "Seguro médico",
        "monto": 150000.0
      }
    ],
    "ubicacion": {
      "latitud": "4.6533817",
      "longitud": "-74.0836331",
      "display_name": "Bogotá, Bogotá, Distrito Capital, ..."
    }
  },
  "errors": []
}
```

**Respuesta sin ubicación (ciudad no proporcionada o Nominatim falla):**
```json
{
  "data": {
    "beneficios": [ ... ],
    "ubicacion": null
  }
}
```

### Editar beneficio

```
PUT /api/v1/beneficios/:id
Content-Type: application/json

{
  "nombre_beneficio": "Seguro dental",
  "monto": 200000
}
```

### Eliminar beneficio

```
DELETE /api/v1/beneficios/:id
```

## Integración con Nominatim

El servicio consulta la API de **Nominatim (OpenStreetMap)** en formato **XML** (consistente con el requisito original del backend Java).

### Comportamiento

1. Recibe la ciudad como parámetro en el endpoint GET
2. Consulta `https://nominatim.openstreetmap.org/search?q=Ciudad&format=xml&limit=1`
3. Parsea la respuesta XML con **Nokogiri** para extraer latitud, longitud y nombre del lugar
4. Almacena el resultado en **caché en memoria** con TTL de 24 horas
5. Si la ciudad es null, vacía o Nominatim falla, retorna `ubicacion: null` (fallback graceful)

### Caché

- **Almacenamiento:** Hash en memoria (`NominatimService::CACHE`)
- **TTL:** 24 horas (configurable en `config/initializers/nominatim.rb`)
- **Normalización:** Las claves se normalizan (`strip.downcase`) para evitar duplicados

### Ejemplo de consulta Nominatim

```
GET https://nominatim.openstreetmap.org/search?q=Bogota&format=xml&limit=1
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<searchresults>
  <place lat="4.6533817" lon="-74.0836331"
    display_name="Bogotá, Bogotá, Distrito Capital, RAP (Especial) Central, Colombia" />
</searchresults>
```

## Pruebas

### Ejecutar todos los tests

```bash
# Crear BD de test (si no existe)
ruby bin\rails db:create db:migrate RAILS_ENV=test

# Ejecutar tests
bundle exec rspec
```

### Cobertura

| Tipo         | Archivo                                       | Tests |
| ------------ | --------------------------------------------- | ----- |
| Modelo       | `spec/models/beneficio_spec.rb`               | 7     |
| Service      | `spec/services/crear_beneficio_service_spec.rb`| 4     |
| Service      | `spec/services/editar_beneficio_service_spec.rb`| 2    |
| Service      | `spec/services/eliminar_beneficio_service_spec.rb`| 2   |
| Service      | `spec/services/listar_beneficios_service_spec.rb`| 2   |
| Service      | `spec/services/nominatim_service_spec.rb`      | 6     |
| Request      | `spec/requests/api/v1/beneficios_spec.rb`     | 11    |
| **Total**    |                                               | **34**|

### Ejecutar un solo archivo

```bash
bundle exec rspec spec/services/nominatim_service_spec.rb
```

### Ejecutar un solo test

```bash
bundle exec rspec spec/services/nominatim_service_spec.rb:12
```

## Variables de Entorno

| Variable                  | Descripción                          | Default                              |
| ------------------------- | ------------------------------------ | ------------------------------------ |
| `PORT`                    | Puerto del servidor                  | `3000`                               |
| `RAILS_ENV`               | Entorno de ejecución                 | `development`                        |
| `RAILS_MAX_THREADS`       | Hilos máximos de Puma                | `5`                                  |
| `BENEFICIOS_SERVICE_DATABASE_PASSWORD` | Contraseña BD producción | (requerido en producción) |

## Configuración de Nominatim

Editable en `config/initializers/nominatim.rb`:

```ruby
Rails.application.config.nominatim = {
  base_url: "https://nominatim.openstreetmap.org/search",
  user_agent: "BeneficiosService/1.0",
  cache_ttl: 24 * 60 * 60  # 24 horas en segundos
}
```

## Diseño Arquitectónico

### Independencia del microservicio

Este servicio es **completamente independiente** del backend Java:

- Tiene su propia base de datos PostgreSQL (`beneficios_service_development`)
- No tiene acceso a la tabla de empleados del backend Java
- La ciudad del empleado se recibe como query param (`?ciudad=Bogota`), no se busca internamente
- Esto evita acoplamiento entre servicios (principio de microservicios)

### Formato de respuesta ApiResponse

Mismo formato que el backend Java para mantener consistencia:

```json
{
  "error": boolean,
  "message": "string",
  "data": object|array|null,
  "errors": ["string"]
}
```

### Integración con el backend Java

El backend Java actúa como **proxy**:

1. El frontend llama a Java (`GET /api/beneficios/empleado/4`)
2. Java busca la ciudad del empleado 4 en su propia BD
3. Java reenvía la petición a Ruby (`GET /api/v1/beneficios/empleado/4?ciudad=Bogota`)
4. Ruby retorna beneficios + ubicación de Nominatim
5. Java reenvía la respuesta al frontend

```
Frontend → Java (proxy, :8080) → Ruby (lógica + BD propia, :3000) → Nominatim
```

## Licencia

Proyecto privado — Prueba Técnica AtlasHR.
