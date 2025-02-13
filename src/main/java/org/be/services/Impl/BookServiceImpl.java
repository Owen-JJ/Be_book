package org.be.services.Impl;

import org.be.dtos.AuthorDTO;
import org.be.dtos.BookDTO;
import org.be.dtos.BookWithAuthorDTO;
import org.be.dtos.common.ResponseDTO;
import org.be.entities.AuthorEntity;
import org.be.entities.BookEntity;
import org.be.repositories.AuthorRepository;
import org.be.repositories.BookRepository;
import org.be.services.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
    }

    //Add new books to the database.
    @Override
    public ResponseDTO<BookDTO> create(BookDTO bookDTO) {
        try {
            if (bookDTO.getPrice() == null || bookDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseDTO.<BookDTO>builder()
                        .status(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .message("Giá sách phải lớn hơn 0!")
                        .build();
            }
            if (bookRepository.findByIsbn(bookDTO.getIsbn()).isPresent()) {
                return ResponseDTO.<BookDTO>builder()
                        .status(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .message("ISBN đã tồn tại!")
                        .build();
            }
            Optional<AuthorEntity> optionalAuthor = authorRepository.findById(bookDTO.getAuthorId());
            if (optionalAuthor.isEmpty()) {
                return ResponseDTO.<BookDTO>builder()
                        .status(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .message("Tác giả không tồn tại!")
                        .build();
            }

            // Map DTO -> Entity
            BookEntity bookEntity = modelMapper.map(bookDTO, BookEntity.class);
            bookEntity.setAuthor(optionalAuthor.get());

            // Lưu vào database
            BookEntity savedBook = bookRepository.save(bookEntity);

            return ResponseDTO.<BookDTO>builder()
                    .status(String.valueOf(HttpStatus.CREATED.value()))
                    .message("Sách được tạo thành công!")
                    .data(modelMapper.map(savedBook, BookDTO.class))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();  // Ghi log lỗi
            return ResponseDTO.<BookDTO>builder()
                    .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .message("Lỗi khi tạo sách: " + e.getMessage())  // Thêm chi tiết lỗi
                    .build();
        }
    }


    //Get book details by ID, can get additional author information.
    @Override
    public ResponseDTO<Object> findById(Long id, boolean includeAuthor) {
        Optional<BookEntity> optionalBook = bookRepository.findById(id);

        if (optionalBook.isEmpty()) {
            return ResponseDTO.builder()
                    .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                    .message("Không tìm thấy sách!")
                    .build();
        }

        BookEntity bookEntity = optionalBook.get();
        BookDTO bookDTO = modelMapper.map(bookEntity, BookDTO.class);
        bookDTO.setAuthorId(bookEntity.getAuthor().getId());

        if (includeAuthor) {
            Optional<AuthorEntity> optionalAuthor = authorRepository.findById(bookEntity.getAuthor().getId());
            if (optionalAuthor.isPresent()) {
                AuthorDTO authorDTO = modelMapper.map(optionalAuthor.get(), AuthorDTO.class);
                return ResponseDTO.builder()
                        .status(String.valueOf(HttpStatus.OK.value()))
                        .message("Sách được tìm thấy!")
                        .data(new BookWithAuthorDTO(bookDTO, authorDTO))
                        .build();
            }
        }

        return ResponseDTO.builder()
                .status(String.valueOf(HttpStatus.OK.value()))
                .message("Sách được tìm thấy!")
                .data(bookDTO)
                .build();
    }

    //Filter the book list by authorId, publishedDate, title, price criteria.
    @Override
    public ResponseDTO<List<BookDTO>> findAll(Long authorId, LocalDate publishedDate, String title, Double minPrice, Double maxPrice) {
        List<BookEntity> books = bookRepository.findAll().stream()
                .filter(book -> (authorId == null || book.getAuthor().getId().equals(authorId)))
                .filter(book -> (publishedDate == null || book.getPublishedDate().isAfter(publishedDate)))
                .filter(book -> (title == null || book.getTitle().toLowerCase().contains(title.toLowerCase())))
                .filter(book -> (minPrice == null || book.getPrice().doubleValue() >= minPrice))
                .filter(book -> (maxPrice == null || book.getPrice().doubleValue() <= maxPrice))
                .toList();

        if (books.isEmpty()) {
            return ResponseDTO.<List<BookDTO>>builder()
                    .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                    .message("Không tìm thấy sách với điều kiện lọc!")
                    .build();
        }

        List<BookDTO> bookDTOs = books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());

        return ResponseDTO.<List<BookDTO>>builder()
                .status(String.valueOf(HttpStatus.OK.value()))
                .message("Danh sách sách được lấy thành công!")
                .data(bookDTOs)
                .build();
    }

    //Update book information.
    @Override
    public ResponseDTO<BookDTO> update(Long id, BookDTO bookDTO) {
        Optional<BookEntity> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            return ResponseDTO.<BookDTO>builder()
                    .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                    .message("Sách không tồn tại!")
                    .build();
        }

        BookEntity existingBook = optionalBook.get();
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setPublishedDate(bookDTO.getPublishedDate());
        existingBook.setPrice(bookDTO.getPrice());

        BookEntity updatedBook = bookRepository.save(existingBook);

        return ResponseDTO.<BookDTO>builder()
                .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .message("Sách đã được cập nhật!")
                .data(modelMapper.map(updatedBook, BookDTO.class))
                .build();
    }

    //Delete books by ID.
    @Override
    public ResponseDTO<Void> delete(Long id) {
        Optional<BookEntity> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            return ResponseDTO.<Void>builder()
                    .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                    .message("Sách không tồn tại!")
                    .build();
        }

        bookRepository.deleteById(id);

        return ResponseDTO.<Void>builder()
                .status(String.valueOf(HttpStatus.OK.value()))
                .message("Sách đã được xóa!")
                .build();
    }
}
