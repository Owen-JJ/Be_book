package org.be.services;

import org.be.dtos.AuthorDTO;
import org.be.dtos.BookDTO;
import org.be.dtos.BookWithAuthorDTO;
import org.be.dtos.common.ResponseDTO;
import org.be.entities.BookEntity;
import org.be.entities.AuthorEntity;
import org.be.repositories.BookRepository;
import org.be.repositories.AuthorRepository;
import org.be.services.Impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookEntity bookEntity;
    private BookDTO bookDTO;
    private AuthorDTO authorDTO;
    private AuthorEntity authorEntity;

    @BeforeEach
    void setUp() {
        // Khởi tạo AuthorEntity
        authorEntity = new AuthorEntity();
        authorEntity.setId(1L);
        authorEntity.setName("Nguyễn Nhật Ánh");
        authorEntity.setNationality("Vietnam");

        // Khởi tạo AuthorDTO
        authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setName("Nguyễn Nhật Ánh");
        authorDTO.setNationality("Vietnam");

        // Khởi tạo BookEntity
        bookEntity = new BookEntity();
        bookEntity.setId(1L);
        bookEntity.setTitle("Mắt Biếc");
        bookEntity.setAuthor(authorEntity);
        bookEntity.setPublishedDate(LocalDate.of(1990, 1, 1));
        bookEntity.setIsbn("9786042100247");
        bookEntity.setPrice(new BigDecimal("120000"));

        // Khởi tạo BookDTO
        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Mắt Biếc");
        bookDTO.setAuthorId(1L);
        bookDTO.setPublishedDate(LocalDate.of(1990, 1, 1));
        bookDTO.setIsbn("9786042100247");
        bookDTO.setPrice(new BigDecimal("120000"));
    }


    @Test
    void testFindById_WithAuthor() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity));
        when(modelMapper.map(bookEntity, BookDTO.class)).thenReturn(bookDTO);
        when(modelMapper.map(authorEntity, AuthorDTO.class)).thenReturn(authorDTO);

        ResponseDTO<Object> response = bookService.findById(1L, true);

        assertEquals(HttpStatus.OK.value(), Integer.parseInt(response.getStatus()));
        assertEquals("Sách được tìm thấy!", response.getMessage());

        assertTrue(response.getData() instanceof BookWithAuthorDTO);

        BookWithAuthorDTO bookWithAuthor = (BookWithAuthorDTO) response.getData();
        assertEquals("Mắt Biếc", bookWithAuthor.getBook().getTitle());
        assertEquals(1L, bookWithAuthor.getBook().getAuthorId());
        assertEquals("Nguyễn Nhật Ánh", bookWithAuthor.getAuthor().getName());

        verify(bookRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(bookEntity, BookDTO.class);
        verify(modelMapper, times(1)).map(authorEntity, AuthorDTO.class);
    }


    @Test
    void testFindById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseDTO<Object> response = bookService.findById(1L, false);

        assertEquals(HttpStatus.NOT_FOUND.value(), Integer.parseInt(response.getStatus()));
        assertNull(response.getData());
        verify(bookRepository, times(1)).findById(1L);
    }


    @Test
    void testFindAll_WithFilters() {
        BookEntity book2 = new BookEntity();
        book2.setId(2L);
        book2.setTitle("Toi Thay Hoa Vang Tren Co Xanh");
        book2.setAuthor(authorEntity);
        book2.setPublishedDate(LocalDate.of(2010, 5, 15));
        book2.setIsbn("9786042093333");
        book2.setPrice(new BigDecimal("95000"));

        List<BookEntity> bookList = Arrays.asList(bookEntity, book2);
        when(bookRepository.findAll()).thenReturn(bookList);
        when(modelMapper.map(any(BookEntity.class), eq(BookDTO.class))).thenAnswer(invocation -> {
            BookEntity book = invocation.getArgument(0);
            return new BookDTO(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getPublishedDate(), book.getIsbn(), book.getPrice());
        });

        ResponseDTO<List<BookDTO>> response = bookService.findAll(null, null, null, null, null);
        assertEquals(HttpStatus.OK.value(), Integer.parseInt(response.getStatus()));
        assertNotNull(response.getData());
        assertFalse(response.getData().isEmpty());

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_NotFound() {
        when(bookRepository.findAll()).thenReturn(List.of());
        ResponseDTO<List<BookDTO>> response = bookService.findAll(null, null, null, null, null);

        assertEquals(HttpStatus.NOT_FOUND.value(), Integer.parseInt(response.getStatus()));
        assertNull(response.getData());
        verify(bookRepository, times(1)).findAll();
    }
}
