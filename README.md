# Patrones Arquitectónicos y de Diseño

API REST desarrollada con **Spring Boot** que implementa patrones de diseño y arquitectura a través de un sistema de gestión de animales para un zoológico. El proyecto se ejecuta completamente en **Docker**.

| Patrón | Rol en el sistema |
|---|---|
| **Cliente–Servidor** | El cliente consume la API REST expuesta por el backend Spring Boot |
| **DTO** | `AnimalDTO` transporta datos entre cliente y servidor sin exponer lógica de dominio |
| **Factory Method** | `AnimalFactoryImpl` crea la subclase concreta de `Animal` según el tipo recibido |
| **Bridge** | `Animal` y `Habitat` son jerarquías independientes unidas por composición |
| **Chain of Responsibility** | 5 validadores encadenados procesan el DTO antes de registrar el animal |

---

## Arquitectura

```
Cliente (HTTP)
  └─ POST /api/animales (AnimalDTO en JSON)
       └─ AnimalController
            └─ AnimalService
                 ├─ Chain: ValidarNombre → ValidarEdad → ValidarTipo → ValidarHabitat → ValidarCompatibilidad
                 ├─ Factory: AnimalFactoryImpl → Leon | Elefante | Mono | Tiburon | Aguila | Cocodrilo
                 ├─ Bridge: Animal ──▶ Habitat (Sabana | Selva | Acuario | Rio | Desierto | Montana)
                 └─ Repositorio en memoria (List<Animal>)
```


## Requisitos

- [Docker](https://www.docker.com/) instalado

---

## Ejecutar con Docker

```bash
# 1. Clonar el repositorio
git clone https://github.com/div468/patrones-arquitectonicos-y-de-diseno.git
cd patrones-arquitectonicos-y-de-diseno

# 2. Construir la imagen
docker build -t zoologico .

# 3. Ejecutar el contenedor
docker run -p 8080:8080 zoologico
```

API disponible en `http://localhost:8080`

---

## Patrones explicados

### DTO — Data Transfer Object

`AnimalDTO` es un objeto plano sin lógica que viaja en el cuerpo del `POST`. Desacopla la representación de red del modelo de dominio.

```json
{
  "tipo": "LEON",
  "nombre": "Simba",
  "edad": 5,
  "habitat": "SABANA"
}
```

### Factory Method

`AnimalFactory` define el contrato. `AnimalFactoryImpl` decide qué subclase concreta instanciar sin que el `Service` lo sepa.

```
AnimalFactory (abstract)
  └── AnimalFactoryImpl
        ├── crearAnimal("LEON")      → new Leon(...)
        ├── crearAnimal("TIBURON")   → new Tiburon(...)
        └── crearAnimal("COCODRILO") → new Cocodrilo(...)
```

### Bridge

Separa la jerarquía de animales de la jerarquía de hábitats. Sin Bridge harían falta clases como `LeonSabana`, `TiburonRio`, etc. Con Bridge cualquier combinación válida se arma en tiempo de ejecución.

```
Animal (abstract)          Habitat (interface)
  ├── Leon                   ├── Sabana
  ├── Elefante               ├── Selva
  ├── Mono                   ├── Acuario
  ├── Tiburon      ──▶       ├── Rio
  ├── Aguila                 ├── Desierto
  └── Cocodrilo              └── Montana
```

### Chain of Responsibility

Cinco validadores encadenados. Cada uno conoce solo al siguiente. Si un eslabón falla, corta la cadena y lanza `IllegalArgumentException`.

```
ValidarNombre          → nombre no vacío, solo letras, máx 30 caracteres
  → ValidarEdad        → entre 1 y 80 años
    → ValidarTipo      → tipo reconocido por el sistema
      → ValidarHabitat → hábitat disponible en el zoológico
        → ValidarCompatibilidad ★ → ¿puede este animal vivir en este hábitat?
```

#### Tabla de compatibilidad

| Animal | Hábitats válidos |
|---|---|
| 🦁 León | Sabana, Desierto |
| 🐘 Elefante | Sabana, Selva |
| 🐒 Mono | Selva |
| 🦈 Tiburón | Acuario, Río |
| 🦅 Águila | Montaña |
| 🐊 Cocodrilo | Río, Selva |

---

## Diagramas UML

### UML de Clases

![UML de Clases](images/umlClases.png)

### UML de Secuencia

![UML de Secuencia](images/umlSecuencia.png)

---

## Estructura del proyecto

```
├── images/
│   ├── umlClases.png
│   └── umlSecuencia.png
├── src/main/java/com/zoo/
│   ├── controller/      # AnimalController — API REST (@RestController)
│   ├── service/         # AnimalService — orquesta chain, factory y repositorio
│   ├── dto/             # AnimalDTO — objeto de transferencia
│   ├── model/           # Animal (abstract) + Leon, Elefante, Mono, Tiburon, Aguila, Cocodrilo
│   ├── bridge/          # Habitat (interface) + Sabana, Selva, Acuario, Rio, Desierto, Montana
│   ├── factory/         # AnimalFactory (abstract) + AnimalFactoryImpl
│   └── chain/           # Validador (abstract) + 5 implementaciones
├── Dockerfile
└── pom.xml
```