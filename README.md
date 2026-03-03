# patrones-arquitectonicos-y-de-diseno

Aplicación cliente-servidor con **Spring Boot + HTML/CSS/JS (Fetch API)** usando los patrones:

- Factory Method
- Bridge
- Chain of Responsibility
- DTO

## Arquitectura

Frontend (HTML + JS) → Controller (REST) → Service → Chain + Factory + Bridge → Repositorio en memoria (`List<Animal>`)

## Estructura

- `src/main/java/com/zoo/controller`: API REST
- `src/main/java/com/zoo/service`: lógica de negocio
- `src/main/java/com/zoo/dto`: objeto de transferencia (`AnimalDTO`)
- `src/main/java/com/zoo/model`: jerarquía de animales
- `src/main/java/com/zoo/bridge`: implementación de hábitats
- `src/main/java/com/zoo/factory`: creación de animales
- `src/main/java/com/zoo/chain`: validaciones encadenadas
- `frontend`: cliente estático

## Requisitos

- Java 17+
- Maven 3.9+

## Ejecutar backend

```bash
mvn spring-boot:run
```

Servidor en: `http://localhost:8080`

## Ejecutar frontend

Abre `frontend/index.html` en el navegador.

## Endpoints

Base URL: `http://localhost:8080/api/animales`

1. Registrar animal

`POST /api/animales`

```json
{
	"tipo": "LEON",
	"nombre": "Simba",
	"edad": 5,
	"habitat": "SABANA"
}
```

2. Listar animales

`GET /api/animales`

3. Ordenar animales

`GET /api/animales/ordenar?criterio=edad`

(`criterio` también acepta `nombre`)
