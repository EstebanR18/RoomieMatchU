# RoomieMatchU

RoomieMatchU es una plataforma compuesta por un **backend en Quarkus** y una **aplicación Android nativa** construida con Jetpack Compose. Su objetivo es facilitar la conexión entre personas que buscan un lugar para vivir y aquellas que tienen un espacio disponible, ofreciendo perfiles detallados, sugerencias basadas en compatibilidad y funcionalidades modernas como manejo de imágenes, autenticación segura y navegación fluida.

---

# 1. RoomieMatchU – Backend

## Descripción

RoomieMatchU Backend es una API REST desarrollada con **Quarkus**. Implementa un sistema de matching inteligente basado en características personales, preferencias de convivencia y atributos del lugar disponible.

Proporciona servicios para:
- Gestión de usuarios
- Creación y administración de perfiles
- Carga y manejo de imágenes mediante AWS S3
- Recuperación de contraseña por correo
- Algoritmo de compatibilidad para sugerencias de perfiles

## Tecnologías Principales
- Java 22  
- Quarkus  
- Jakarta EE  
- PostgreSQL  
- AWS S3  
- Jakarta Mail  
- BCrypt  
- Lombok  

## Arquitectura

El backend utiliza una arquitectura en capas:

    co.edu.ucentral/  
    ├── controller/ # Endpoints REST  
    ├── service/ # Lógica de negocio  
    ├── repository/ # Acceso a datos  
    ├── entity/ # Modelos JPA  
    ├── dto/ # Data Transfer Objects  
    └── storage/ # Gestión de almacenamiento (AWS S3)  


### Funcionalidades Principales

#### 1. Autenticación y Usuarios

- Registro con validación de email  
- Login seguro con contraseñas encriptadas  
- Obtención y edición de datos del usuario  
- Eliminación de usuario  
- Distinción entre perfiles "Busco Lugar" y "Tengo Lugar"

**Endpoints principales:**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/users/register` | Registrar usuario |
| POST | `/api/users/login` | Iniciar sesión |
| GET | `/api/users/{id}` | Obtener usuario |
| GET | `/api/users/email/{email}` | Buscar por email |
| PUT | `/api/users/{id}` | Actualizar usuario |
| DELETE | `/api/users/{id}` | Eliminar usuario |

#### 2. Gestión de Perfiles

El sistema soporta dos tipos de perfiles:

- **Perfil Busco Lugar**: Información del usuario interesado en encontrar un espacio.
- **Perfil Tengo Lugar**: Información detallada del lugar disponible y reglas de convivencia.

**Endpoints principales:**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/perfiles/busco-lugar` | Crear perfil búsqueda |
| POST | `/api/perfiles/tengo-lugar` | Crear perfil oferta |
| GET  | `/api/perfiles/user/{userId}` | Obtener perfil del usuario |
| PUT  | `/api/perfiles/{id}` | Editar perfil |
| DELETE | `/api/perfiles/{id}` | Eliminar perfil |
| PUT | `/api/perfiles/{userId}/cambiar-tipo` | Cambiar tipo de perfil |

#### 3. Sistema de Matching Inteligente

**Endpoint principal:** `GET /api/sugerencias/{userId}`

El algoritmo compara:
- Presupuesto y precio  
- Ubicación  
- Limpieza  
- Fumador / alcohol  
- Socialización  
- Mascotas  
- Horarios  
- Género  

Retorna una lista de perfiles ordenada por compatibilidad (score 0–100).

#### 4. Gestión de Imágenes (AWS S3)

El backend maneja:
- Foto de perfil
- Galerías de residencias
- Eliminación y reemplazo automático de imágenes antiguas

#### 5. Recuperación de Contraseña

- Generación de token temporal
- Envío por correo
- Validación del token para restablecer la contraseña

**Endpoints:**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/password-reset/request` | Solicitar reset |
| POST | `/api/password-reset/reset` | Restablecer contraseña |

### Modelos Principales

#### UserEntity
`id, nombre, email, contraseña (encriptada), tipoPerfil`

#### PerfilBuscoLugarEntity
`edad, género, ocupación, presupuesto, ubicación preferida, fumador, mascotas, horarios, socialización, limpieza, foto, descripción`

#### PerfilTengoLugarEntity
`dirección, precio, ubicación, tipo de residencia, reglas, servicios incluidos, habitaciones, fotos, descripción`

#### PasswordResetTokenEntity
`token, user, expirationDate`

### Variables de Entorno

La aplicación requiere un archivo `.env` con las siguientes categorías:

- Credenciales de Base de Datos  
- Credenciales de AWS S3  
- SMTP (correo)  
- Configuración de aplicación  

El contenido exacto no se publica por motivos de seguridad.

---

## 2. RoomieMatchU – Frontend (Android)

### Descripción

El frontend es una aplicación Android nativa desarrollada con **Kotlin + Jetpack Compose**. Permite a los usuarios registrarse, crear perfiles, explorar lugares disponibles y ver detalles de otros usuarios.

### Características

- Autenticación de usuarios  
- Creación de perfiles:  
  - Perfil "Busco Lugar"  
  - Perfil "Tengo Lugar"  
- Pantalla de inicio con lista de perfiles de "Tengo Lugar"  
- Pantalla de perfil con información detallada  
- Navegación fluida  

### Arquitectura del Frontend

Basada en **MVVM**:

- **Kotlin** — lógica y estructura  
- **Jetpack Compose** — UI moderna y declarativa  
- **ViewModel + StateFlow** — gestión de estado  
- **Retrofit** — consumo de la API REST  
- **Coil** — carga de imágenes  
- **Jetpack Navigation** — navegación entre pantallas  

### Estructura del Proyecto

    data/  
    ├── remote/ # Retrofit, DTOs, API  
    └── repository/ # Repositorios  
    ui/   
    ├── authentication/  
    ├── profile/  
    ├── profileform/  
    ├── HomeScreen.kt  
    ├── LandingScreen.kt  
    ├── ProfileScreen.kt  
    navigation/  
    ├── AppNavigation.kt  
    └── AppScreens.kt  
    viewmodel/  
    utils/  


### Flujo de Uso de la App

#### 1. Inicio
El usuario llega a la **LandingScreen**, donde puede iniciar sesión o registrarse.

#### 2. Autenticación
Registro o login mediante el backend. Manejo de errores y validaciones incluidos.

#### 3. Creación del Perfil
El usuario elige entre "Busco Lugar" o "Tengo Lugar" y llena la información necesaria.

#### 4. HomeScreen
Se muestran perfiles de personas que ofrecen un lugar.

#### 5. Ver Detalles
El usuario accede a la **ProfileScreen**, donde ve toda la información del perfil seleccionado.

---

## 3. Contribución

Las contribuciones son bienvenidas.  
Pasos sugeridos:  

    1. Abrir un issue  
    2. Crear una rama  
    3. Proponer cambios via Pull Request  
## 4. Equipo de Desarrollo

- [@Estebannn8](https://github.com/Estebannn8)
- [@Valerc07](https://github.com/Valerc07)
