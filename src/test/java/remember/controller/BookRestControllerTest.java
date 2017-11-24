package remember.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import remember.Main;
import remember.domain.inertances.Book;
import remember.repository.inertances.BookRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@WebAppConfiguration
public class BookRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Book book;

    private List<Book> bookList = new ArrayList<>();

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).dispatchOptions(true).build();

        this.bookRepository.deleteAllInBatch();

        this.book = bookRepository.save(new Book("Tom", "Tom's adventure", "fine book"));
        this.bookList.add(bookRepository.save(new Book("J. K. Rowling", "Harry Potter and the Cursed Child", "A description")));
        this.bookList.add(bookRepository.save(new Book("J. R. R. Tolkien", "The Lord of the Rings", "The Lord of the Rings is an epic high fantasy novel written by English author and scholar J. R. R. Tolkien.")));
    }

    @Test
    public void bookNotFound() throws Exception {
        mockMvc.perform(get("/books/1000")
                .content(this.json(new Book()))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readSingleBook() throws Exception {

        mockMvc.perform(get("/books/"
                + this.bookList.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(this.bookList.get(0).getId().intValue())))
                .andExpect(jsonPath("$.author", is("J. K. Rowling")))
                .andExpect(jsonPath("$.title", is("Harry Potter and the Cursed Child")))
                .andExpect(jsonPath("$.comment", is("A description")));
    }

    @Test
    public void readBooks() throws Exception {
        mockMvc.perform(get("/books").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(this.book.getId().intValue())))
                .andExpect(jsonPath("$[0].author", is("Tom")))
                .andExpect(jsonPath("$[0].title", is("Tom's adventure")))
                .andExpect(jsonPath("$[0].comment", is("fine book")))
                .andExpect(jsonPath("$[1].id", is(this.bookList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[1].author", is("J. K. Rowling")))
                .andExpect(jsonPath("$[1].title", is("Harry Potter and the Cursed Child")))
                .andExpect(jsonPath("$[1].comment", is("A description")))
                .andExpect(jsonPath("$[2].id", is(this.bookList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[2].author", is("J. R. R. Tolkien")))
                .andExpect(jsonPath("$[2].title", is("The Lord of the Rings")))
                .andExpect(jsonPath("$[2].comment", is("The Lord of the Rings is an epic high fantasy novel written by English author and scholar J. R. R. Tolkien.")));
    }

    @Test
    public void createBooks() throws Exception {
        String bookmarkJson = json(new Book("sample", "sample", "sample"));

        this.mockMvc.perform(post("/books")
                .contentType(contentType)
                .content(bookmarkJson))
                .andExpect(status().isCreated());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}