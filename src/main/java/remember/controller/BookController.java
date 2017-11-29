package remember.controller;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import remember.domain.instances.Book;
import remember.repository.inertances.BookRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/books")
    @ResponseBody
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @GetMapping("/books/{id}")
    @ResponseBody
    public Book getBook(@PathVariable Long id, HttpServletResponse response) {
        if(bookRepository.findOne(id) == null) response.setStatus(HttpStatus.SC_NOT_FOUND);
        return bookRepository.findOne(id);
    }

    @PostMapping("/books")
    @ResponseBody
    public Book addBook(@Validated @RequestBody Book book, HttpServletResponse response) {
        book.setType("book");
        bookRepository.saveAndFlush(book);
        response.setStatus(HttpStatus.SC_CREATED);
        return book;
    }

    @DeleteMapping("/books/{id}")
    @ResponseBody
    public Book deleteBook(@PathVariable Long id) {
        Book book = bookRepository.findOne(id);
        bookRepository.delete(id);
        return book;
    }
}
