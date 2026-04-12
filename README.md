# CatAPI

REST API que consume [TheCatAPI](https://thecatapi.com/) con autenticación JWT, paginación y gestión de razas e imágenes de gatos.

---

## Arranque del proyecto

### Con Docker (recomendado)

1. Reemplaza `THE_CAT_API_KEY` en el `docker-compose.yml` con tu clave de TheCatAPI para obtener mejores resultados (sin clave, la cantidad de imágenes por consulta estará limitada a 10).

2. Desde la raíz del proyecto ejecuta:

```bash
docker-compose build
docker-compose up
```

La API quedará disponible en `http://localhost:8080`.

---

### De manera local

Configura las siguientes variables de entorno antes de correr la aplicación:

```properties
thecatapi.base-url=${THE_CAT_API_BASE_URL}
thecatapi.api-key=${THE_CAT_API_KEY}
jwt.secret-key=${SECRET_KEY}

spring.datasource.url=${DATA_BASE_URL}
spring.datasource.username=${DATA_BASE_USERNAME}
spring.datasource.password=${DATA_BASE_PASSWORD}
```

---

## Decisiones técnicas

- **Campos de breed sin conversión a booleano:** TheCatAPI no documenta claramente qué campos son booleanos y cuáles son escalas numéricas, por lo que se optó por no asumir conversiones y mantener los valores originales.

- **@JsonProperty para Snake Case:** Como TheCatAPI usa `snake_case` en sus respuestas, se utilizó `@JsonProperty` para mapear correctamente los campos al modelo de la aplicación.

- **Paginación incluida:** Todos los endpoints que devuelven listas incluyen paginación. La respuesta tiene el siguiente formato:


```json
{
    "content": [
        {
            "height": "1350",
            "id": "J2PmlIizw",
            "url": "https://cdn2.thecatapi.com/images/J2PmlIizw.jpg",
            "width": "1080"
        }
    ],
    "page": 1,
    "size": 3
}
```

Los parámetros `page` y `size` son opcionales en todos los endpoints. El tamaño de página por defecto es **10**.

- **Límite de imágenes sin API Key:** El endpoint `images?breed_id` siempre devolverá 10 imágenes a menos que se configure `THE_CAT_API_KEY`.

- **Autenticación por email:** Se decidió usar el email como identificador único para el login.

- **Autenticación Bearer requerida:** Todos los endpoints excepto `/register` y `/login` requieren un token JWT en el header `Authorization: Bearer <token>`.

- **Validaciones en register:** Ningún campo puede ser nulo. El email debe tener formato válido y la contraseña debe tener al menos 8 caracteres.

- **Cobertura con Jacoco:** Para la cobertura decidio usar Jacoco utilizando el comando:
```properties
mvn test
```
---

## Endpoints

### Auth

#### Register
**POST** `http://localhost:8080/api/v1/users/auth/register`

Body:
```json
{
    "password": "password1",
    "email": "email@example.com",
    "firstName": "example",
    "lastName": "example"
}
```

Devuelve un token de autenticación JWT.

---

#### Login
**POST** `http://localhost:8080/api/v1/users/auth/login`

Body:
```json
{
    "email": "pepe1@gmail.com",
    "password": "password"
}
```

Devuelve un token de autenticación JWT.

---

### Breeds

> Todos los endpoints de breeds requieren `Authorization: Bearer <token>`

#### Get breed by ID
**GET** `http://localhost:8080/api/v1/breeds/beng`

Respuesta de ejemplo:
```json
{
    "adaptability": 5,
    "affectionLevel": 5,
    "bidability": 3,
    "catFriendly": 4,
    "cfaUrl": "http://cfa.org/Breeds/BreedsAB/Bengal.aspx",
    "childFriendly": 4,
    "countryCode": "US",
    "countryCodes": "US"
}
```

---

#### Get all breeds
**GET** `http://localhost:8080/api/v1/breeds?page=0&size=4`

`page` y `size` son opcionales.

---

#### Search breeds by query
**GET** `http://localhost:8080/api/v1/breeds/search?query=a&size=2`

`page` y `size` son opcionales.

---

### Images

> Requiere `Authorization: Bearer <token>`

#### Get images by breed ID
**GET** `http://localhost:8080/api/v1/images?breed_id=beng&size=3&page=1`

`page` y `size` son opcionales.

Respuesta:
```json
{
    "content": [
        {
            "height": "1350",
            "id": "J2PmlIizw",
            "url": "https://cdn2.thecatapi.com/images/J2PmlIizw.jpg",
            "width": "1080"
        }
    ],
    "page": 1,
    "size": 3
}
```
