package co.edu.umanizales.appmusic.controller;

import co.edu.umanizales.appmusic.model.User;
import co.edu.umanizales.appmusic.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST para gestionar recursos de Usuarios
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    // Servicio de dominio que maneja la lógica de usuarios
    private final UserService userService;

    // GET /users - Lista todos los usuarios
    @GetMapping
    public ResponseEntity<List<User>> list() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET /users/{id} - Obtiene un usuario por su identificador
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /users - Crea un nuevo usuario
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody User user) {
        userService.addUser(user);
        return ResponseEntity.ok().build();
    }

    // PUT /users - Actualiza un usuario existente
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }

    // DELETE /users/{id} - Elimina un usuario por su identificador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
