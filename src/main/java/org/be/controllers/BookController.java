package org.be.controllers;

import org.be.dtos.BookDTO;
import org.be.dtos.common.ResponseDTO;
import org.be.services.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<BookDTO>> createBook(@RequestBody BookDTO bookDTO) {
        ResponseDTO<BookDTO> response = bookService.create(bookDTO);
        return ResponseEntity.status(Integer.parseInt(response.getStatus())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Object>> getBookById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeAuthor) {
        ResponseDTO<Object> response = bookService.findById(id, includeAuthor);
        return ResponseEntity.status(Integer.parseInt(response.getStatus())).body(response);
    }


    @GetMapping
    public ResponseEntity<ResponseDTO<List<BookDTO>>> getAllBooks(
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) LocalDate publishedDate,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        ResponseDTO<List<BookDTO>> response = bookService.findAll(authorId, publishedDate, title, minPrice, maxPrice);
        return ResponseEntity.status(Integer.parseInt(response.getStatus())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<BookDTO>> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        ResponseDTO<BookDTO> response = bookService.update(id, bookDTO);
        return ResponseEntity.status(Integer.parseInt(response.getStatus())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteBook(@PathVariable Long id) {
        ResponseDTO<Void> response = bookService.delete(id);
        return ResponseEntity.status(Integer.parseInt(response.getStatus())).body(response);
    }
}
