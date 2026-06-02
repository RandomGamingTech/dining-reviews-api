# Dining Reviews API

REST API desarrollada con Spring Boot para la gestión de restaurantes y reseñas gastronómicas.

## Descripción

Este proyecto permite administrar restaurantes, usuarios y reseñas gastronómicas mediante una API REST.

Las reseñas pasan por un proceso de aprobación administrativa antes de ser consideradas para el cálculo de puntuaciones de los restaurantes.

## Tecnologías

* Java 21
* Spring Boot
* Spring Data JPA
* Maven
* H2 Database
* REST APIs

## Funcionalidades

### Usuarios

* Crear usuarios.
* Actualizar preferencias alimentarias.
* Consultar información de usuarios.

### Restaurantes

* Registrar restaurantes.
* Consultar restaurantes.
* Buscar restaurantes por código postal y alergias.

### Reseñas

* Crear reseñas gastronómicas.
* Aprobar o rechazar reseñas.
* Calcular puntuaciones de restaurantes a partir de reseñas aprobadas.

## Estructura del Proyecto

* Controller Layer
* Service Logic
* Repository Layer
* Entity Models
* Exception Handling

## Cómo ejecutar el proyecto

Clonar el repositorio:

```bash
git clone https://github.com/RandomGamingTech/dining-reviews-api.git
```

Entrar al directorio:

```bash
cd dining-reviews-api
```

Ejecutar:

```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en:

```text
http://localhost:8080
```

## Autor

David Daniel Cortinez Pescador
