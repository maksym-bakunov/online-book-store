package com.example.onlinebookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@SQLDelete(sql = "UPDATE books SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Column(name = "title")
    private String title;
    @NonNull
    @Column(name = "author")
    private String author;
    @Column(name = "isbn", unique = true)
    private String isbn;
    @Column(name = "price")
    @NonNull
    private BigDecimal price;
    @Column(name = "description")
    private String description;
    @Column(name = "cover_image")
    private String coverImage;
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
