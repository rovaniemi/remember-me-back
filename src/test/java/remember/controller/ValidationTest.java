package remember.controller;

import org.apache.commons.lang3.StringUtils;
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
import remember.domain.instances.Blogpost;
import remember.domain.instances.Book;
import remember.domain.instances.Video;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@WebAppConfiguration
public class ValidationTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).dispatchOptions(true).build();
    }

    @Test
    public void emptyTitle() throws Exception {
        Book book = new Book();
        book.setTitle("");
        book.setAuthor("author");
        String bookJson = json(book);
        this.mockMvc.perform(post("/api/v01/books")
                .contentType(contentType)
                .content(bookJson))
                .andExpect(status().is(400))
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].message", is("The title is a required field")))
                .andExpect(jsonPath("$[0].field", is("title")));
    }

    @Test
    public void emptyAuthor() throws Exception {
        Blogpost blogpost = new Blogpost("title", "", "", "https://www.google.fi");
        String blogpostJson = json(blogpost);
        this.mockMvc.perform(post("/api/v01/blogposts")
                .contentType(contentType)
                .content(blogpostJson))
                .andExpect(status().is(400))
                .andExpect(content().contentType(contentType))
                .andExpect((jsonPath("$", hasSize(1))))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].message", is("The author is a required field")))
                .andExpect(jsonPath("$[0].field", is("author")));
    }

    @Test
    public void emptyUrl() throws Exception {
        Video video = new Video("title", "", "");
        String videoJson = json(video);
        this.mockMvc.perform(post("/api/v01/videos")
                .contentType(contentType)
                .content(videoJson))
                .andExpect(status().is(400))
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].message", is("The url is a required field")))
                .andExpect(jsonPath("$[0].field", is("url")));
    }

    @Test
    public void invalidUrl() throws Exception {
        Video video = new Video("title", "", "url");
        String videoJson = json(video);
        this.mockMvc.perform(post("/api/v01/videos")
                .contentType(contentType)
                .content(videoJson))
                .andExpect(status().is(400))
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].message", is("The url should be valid")))
                .andExpect(jsonPath("$[0].field", is("url")));
    }

    @Test
    public void commentTooLong() throws Exception {
        String comment = StringUtils.repeat("x", 1001);
        Book book = new Book("title", comment, "author");
        String bookJson = json(book);
        this.mockMvc.perform(post("/api/v01/books")
                .contentType(contentType)
                .content(bookJson))
                .andExpect(status().is(400))
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].message", is("The comment must be under 1000 characters")))
                .andExpect(jsonPath("$[0].field", is("comment")));

    }

    @Test
    public void titleTooLong() throws Exception {
        String title = StringUtils.repeat("x", 101);
        Blogpost blogpost = new Blogpost(title, "", "author", "https://www.google.fi");
        String blogpostJson = json(blogpost);
        this.mockMvc.perform(post("/api/v01/blogposts")
                .contentType(contentType)
                .content(blogpostJson))
                .andExpect(status().is(400))
                .andExpect(content().contentType(contentType))
                .andExpect((jsonPath("$", hasSize(1))))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].message", is("The title should be between 1 and 100 characters")))
                .andExpect(jsonPath("$[0].field", is("title")));
    }

    @Test
    public void authorTooLong() throws Exception {
        String author = StringUtils.repeat("x", 101);
        Blogpost blogpost = new Blogpost("title", "", author, "https://www.google.fi");
        String blogpostJson = json(blogpost);
        this.mockMvc.perform(post("/api/v01/blogposts")
                .contentType(contentType)
                .content(blogpostJson))
                .andExpect(status().is(400))
                .andExpect(content().contentType(contentType))
                .andExpect((jsonPath("$", hasSize(1))))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].message", is("The author should be between 1 and 100 characters")))
                .andExpect(jsonPath("$[0].field", is("author")));
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}