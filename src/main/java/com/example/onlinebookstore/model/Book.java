package com.example.onlinebookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Currency;
import lombok.Data;
import lombok.NonNull;

@Entity
@Data
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
    private Currency price;
    @Column(name = "description")
    private String description;
    @Column(name = "cover_image")
    private String coverImage;

    public Book() {

    }
}
