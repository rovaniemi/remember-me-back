/*package remember.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import remember.domain.inertances.Book;
import remember.repository.inertances.BookRepository;

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
    public Book getBook(@PathVariable Long id) {
        return bookRepository.findOne(id);
    }

    @PostMapping("/books")
    @ResponseBody
    public Book addBook(@RequestBody Map<String, String> request) {
        Book book = new Book();
        book.setTitle(request.get("name"));
        book.setAuthor(request.get("author"));
        bookRepository.saveAndFlush(book);
        return book;
    }

    @DeleteMapping("/books/{id}")
    @ResponseBody
    public void deleteBook(@PathVariable Long id) {
        bookRepository.delete(id);
    }
}

*/