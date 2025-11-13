package co.edu.umanizales.appmusic.controller;

import co.edu.umanizales.appmusic.model.MultimediaContent;
import co.edu.umanizales.appmusic.service.MultimediaContentService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contents")
@RequiredArgsConstructor
public class MultimediaContentController {
    private final MultimediaContentService contentService;

    @GetMapping
    public ResponseEntity<List<MultimediaContent>> listar() {
        return ResponseEntity.ok(contentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MultimediaContent> buscarPorId(@PathVariable String id) {
        MultimediaContent content = contentService.findById(id);
        if (content != null) {
            return ResponseEntity.ok(content);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
