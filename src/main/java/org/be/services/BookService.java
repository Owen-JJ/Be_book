package org.be.services;

import org.be.dtos.BookDTO;
import org.be.dtos.common.ResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface BookService {

    //Add new books to the database.
    ResponseDTO<BookDTO> create(BookDTO bookDTO);

    //Get book details by ID, can get additional author information.
    ResponseDTO<Object> findById(Long id, boolean includeAuthor);

    //Filter the book list by authorId, publishedDate, title, price criteria.
    ResponseDTO<List<BookDTO>> findAll(Long authorId, LocalDate publishedDate, String title, Double minPrice, Double maxPrice);

    //Update book information.
    ResponseDTO<BookDTO> update(Long id, BookDTO bookDTO);

    //Delete books by ID.
    ResponseDTO<Void> delete(Long id);
}
