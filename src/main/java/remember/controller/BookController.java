package remember.controller;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import remember.domain.inertances.Book;
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
    public Book addBook(@RequestBody Map<String, String> request, HttpServletResponse response) {
        Book book = new Book();
        book.setTitle(request.get("name"));
        book.setAuthor(request.get("author"));
        bookRepository.saveAndFlush(book);
        response.setStatus(HttpStatus.SC_CREATED);
        return book;
    }

    @DeleteMapping("/books/{id}")
    @ResponseBody
    public void deleteBook(@PathVariable Long id) {
        bookRepository.delete(id);
    }
}
