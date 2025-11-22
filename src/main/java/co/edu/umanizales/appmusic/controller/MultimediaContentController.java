package co.edu.umanizales.appmusic.controller;

import co.edu.umanizales.appmusic.model.MultimediaContent;
import co.edu.umanizales.appmusic.service.MultimediaContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST para gestionar contenidos multimedia generales
@RestController
@RequestMapping("/contents")
@RequiredArgsConstructor
public class MultimediaContentController {
    // Servicio de dominio que maneja la lógica de contenidos multimedia
    private final MultimediaContentService contentService;

    // Lista todos los contenidos multimedia
    @GetMapping
    public ResponseEntity<List<MultimediaContent>> list() {
        return ResponseEntity.ok(contentService.findAll());
    }

    // Obtiene un contenido multimedia por su identificador
    @GetMapping("/{id}")
    public ResponseEntity<MultimediaContent> findById(@PathVariable String id) {
        MultimediaContent content = contentService.findById(id);
        if (content != null) {
            return ResponseEntity.ok(content);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
