package co.edu.umanizales.appmusic.controller;

import co.edu.umanizales.appmusic.model.User;
import co.edu.umanizales.appmusic.model.Payment;
import co.edu.umanizales.appmusic.model.SubscriptionType;
import co.edu.umanizales.appmusic.service.UserService;
import co.edu.umanizales.appmusic.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

// Controlador REST para gestionar recursos de Usuarios
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    // Servicio de dominio que maneja la lógica de usuarios
    private final UserService userService;
    private final PaymentService paymentService;

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

    // POST /users/register - Crea usuario y registra pago si es PREMIUM o FAMILY
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserRequest req) {
        SubscriptionType st = SubscriptionType.valueOf(req.subscriptionType());
        userService.addUser(new User(req.idUser(), req.name(), req.email(), st, null));
        if (st == SubscriptionType.PREMIUM || st == SubscriptionType.FAMILY) {
            if (req.idPayment() == null || req.idPayment().isBlank() || req.amount() == null || req.amount() <= 0 || req.paymentDate() == null || req.paymentDate().isBlank()) {
                return ResponseEntity.badRequest().body("Se requieren idPayment, amount > 0 y paymentDate (AAAA-MM-DD) para planes PREMIUM o FAMILY");
            }
            LocalDate date;
            try { date = LocalDate.parse(req.paymentDate()); } catch (Exception e) {
                return ResponseEntity.badRequest().body("Fecha de pago inválida, formato AAAA-MM-DD");
            }
            paymentService.addPayment(new Payment(req.idPayment(), req.amount(), date, userService.getUserById(req.idUser())));
        }
        return ResponseEntity.ok().build();
    }

    public record RegisterUserRequest(
            @NotBlank(message = "idUser es obligatorio") String idUser,
            @NotBlank(message = "name es obligatorio") String name,
            @NotBlank(message = "email es obligatorio") @Email(message = "email inválido") String email,
            @NotBlank(message = "subscriptionType es obligatorio") @Pattern(regexp = "FREE|PREMIUM|FAMILY", message = "subscriptionType debe ser FREE, PREMIUM o FAMILY") String subscriptionType,
            String idPayment,
            @Positive(message = "amount debe ser > 0") Double amount,
            String paymentDate
    ) {}

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
