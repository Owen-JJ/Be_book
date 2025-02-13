package org.be.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @JsonIgnoreProperties("books") // Ngăn vòng lặp JSON
    private AuthorEntity author;


    @Column(name = "published_date", nullable = false)
    private LocalDate publishedDate;

    @Column(name = "isbn", unique = true, nullable = false)
    private String isbn;

    @Column(name = "price", nullable = false)
    private BigDecimal price;
}
