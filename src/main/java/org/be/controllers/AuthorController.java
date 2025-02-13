package org.be.controllers;

import org.be.dtos.AuthorDTO;
import org.be.dtos.common.ResponseDTO;
import org.be.services.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }
    @PostMapping
    public ResponseEntity<ResponseDTO<AuthorDTO>> createAuthor(@RequestBody AuthorDTO authorDTO) {
        ResponseDTO<AuthorDTO> response = authorService.create(authorDTO);
        return ResponseEntity.status(Integer.parseInt(response.getStatus())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<AuthorDTO>> getAuthorById(@PathVariable Long id) {
        ResponseDTO<AuthorDTO> response = authorService.findById(id);
        return ResponseEntity.status(Integer.parseInt(response.getStatus())).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<AuthorDTO>>> getAllAuthors() {
        ResponseDTO<List<AuthorDTO>> response = authorService.findAll();
        return ResponseEntity.status(Integer.parseInt(response.getStatus())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<AuthorDTO>> updateAuthor(@PathVariable Long id, @RequestBody AuthorDTO authorDTO) {
        ResponseDTO<AuthorDTO> response = authorService.update(id, authorDTO);
        return ResponseEntity.status(Integer.parseInt(response.getStatus())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteAuthor(@PathVariable Long id) {
        ResponseDTO<Void> response = authorService.delete(id);
        return ResponseEntity.status(Integer.parseInt(response.getStatus())).body(response);
    }
}
