package com.example.onlinebookstore.service.impl;

import com.example.onlinebookstore.dto.BookDto;
import com.example.onlinebookstore.dto.CreateBookRequestDto;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.mapper.BookMapper;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.repository.BookRepository;
import com.example.onlinebookstore.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    @Override
    public BookDto save(CreateBookRequestDto createBookRequestDto) {
        Book book = bookRepository.save(bookMapper.toModel(createBookRequestDto));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto createBookRequestDto) {
        return bookRepository.findById(id).map(b -> {
            Book book = bookMapper.toModel(createBookRequestDto);
            book.setId(b.getId());
            bookRepository.save(book);

            return bookMapper.toDto(book);
        }).orElseThrow(() -> new EntityNotFoundException("Can't find book by id " + id));
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
