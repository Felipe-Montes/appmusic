# Guía de Importación - Colección Postman AppMusic

## Descripción
Este archivo contiene la colección de endpoints de la API AppMusic para ser utilizada en Postman.

## Archivo de Colección
- **Nombre**: `AppMusic_Postman_Collection.json`
- **Ubicación**: `/src/main/resources/`

## Pasos para Importar en Postman

### Opción 1: Importar desde archivo
1. Abre **Postman**
2. Haz clic en el botón **Import** (esquina superior izquierda)
3. Selecciona la pestaña **File**
4. Haz clic en **Upload Files**
5. Navega a `AppMusic_Postman_Collection.json`
6. Selecciona el archivo y haz clic en **Open**
7. Haz clic en **Import**

### Opción 2: Importar desde carpeta
1. Abre **Postman**
2. Haz clic en **Import**
3. Selecciona la pestaña **Folder**
4. Navega a la carpeta `/src/main/resources/`
5. Selecciona la carpeta y haz clic en **Select Folder**
6. Haz clic en **Import**

## Estructura de la Colección

La colección está organizada en las siguientes carpetas:

### 1. **Albums** - Gestión de Álbumes
- `GET /albums` - Listar todos los álbumes
- `GET /albums/{id}` - Obtener álbum por ID
- `POST /albums` - Crear nuevo álbum
- `DELETE /albums/{id}` - Eliminar álbum

### 2. **Artists** - Gestión de Artistas
- `GET /artists` - Listar todos los artistas
- `GET /artists/{id}` - Obtener artista por ID
- `POST /artists` - Crear nuevo artista
- `DELETE /artists/{id}` - Eliminar artista

### 3. **Songs** - Gestión de Canciones
- `GET /songs` - Listar todas las canciones
- `GET /songs/{id}` - Obtener canción por ID
- `POST /songs` - Crear nueva canción
- `DELETE /songs/{id}` - Eliminar canción

### 4. **Playlists** - Gestión de Playlists
- `GET /playlists` - Listar todas las playlists
- `GET /playlists/{id}` - Obtener playlist por ID
- `POST /playlists` - Crear nueva playlist
- `DELETE /playlists/{id}` - Eliminar playlist

### 5. **Users** - Gestión de Usuarios
- `GET /users` - Listar todos los usuarios
- `GET /users/{id}` - Obtener usuario por ID
- `POST /users` - Crear nuevo usuario
- `PUT /users` - Actualizar usuario
- `DELETE /users/{id}` - Eliminar usuario

### 6. **Play History** - Historial de Reproducción
- `GET /play-history` - Listar historial de reproducción
- `GET /play-history/{id}` - Obtener registro por ID
- `POST /play-history` - Crear nuevo registro
- `DELETE /play-history/{id}` - Eliminar registro

### 7. **Multimedia Content** - Contenido Multimedia
- `GET /contents` - Listar contenido multimedia
- `GET /contents/{id}` - Obtener contenido por ID

## Configuración Base

Todos los endpoints están configurados para:
- **URL Base**: `http://localhost:8080`
- **Content-Type**: `application/json` (para POST y PUT)

## Uso de Variables

Para cambiar la URL base, puedes:
1. Crear una variable de entorno en Postman
2. Reemplazar `http://localhost:8080` con `{{base_url}}`
3. Configurar la variable en el entorno

## Ejemplos de Payloads

### Album
```json
{
  "id": "1",
  "title": "Nombre del Álbum",
  "duration": 3600
}
```

### Artist
```json
{
  "idArtist": "1",
  "name": "Nombre del Artista"
}
```

### Song
```json
{
  "id": "1",
  "title": "Nombre de la Canción",
  "duration": 180,
  "musicGenre": "ROCK"
}
```

### User
```json
{
  "idUser": "1",
  "name": "Nombre del Usuario",
  "email": "usuario@example.com",
  "subscriptionType": "PREMIUM"
}
```

### Playlist
```json
{
  "idPlaylist": "1",
  "name": "Nombre de la Playlist"
}
```

### Play History
```json
{
  "idHistory": "1",
  "userId": "1",
  "songId": "1",
  "playDate": "2024-01-01T10:00:00"
}
```

## Notas Importantes

- Asegúrate de que la aplicación Spring Boot esté ejecutándose en `http://localhost:8080`
- Reemplaza los IDs de ejemplo con valores reales según sea necesario
- Los endpoints de eliminación no devuelven contenido en el body
- Los endpoints GET con ID devuelven 404 si el recurso no existe

## Soporte

Para más información sobre los modelos de datos, consulta la documentación de los modelos en:
- `/src/main/java/co/edu/umanizales/appmusic/model/`
