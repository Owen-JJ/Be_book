package org.be.repositories;

import org.be.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    //Find books by author ID.
    List<BookEntity> findByAuthorId(Long authorId);

    //Find books published after a specific date.
    List<BookEntity> findByPublishedDateAfter(LocalDate date);

    //Find books with titles containing case-insensitive keywords.
    List<BookEntity> findByTitleContainingIgnoreCase(String title);

    //Find books with a price greater than a specific value.
    List<BookEntity> findByPriceGreaterThan(BigDecimal price);

    //Find books with prices less than a specific value.
    List<BookEntity> findByPriceLessThan(BigDecimal price);

    Optional<BookEntity> findByIsbn(String isbn);
}
