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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import remember.domain.instances.Blogpost;
import remember.repository.inertances.BlogpostRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@WebAppConfiguration
public class BlogpostRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Blogpost blogpost;

    private List<Blogpost> blogpostList = new ArrayList<>();

    @Autowired
    private BlogpostRepository blogpostRepository;

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

        this.blogpostRepository.deleteAllInBatch();

        this.blogpost = blogpostRepository.save(
                new Blogpost("Minimalist Baker", "great recipe",
                        "Dana Shultz", "https://minimalistbaker.com/creamy-avocado-banana-green-smoothie/"));
        this.blogpostList.add(blogpostRepository.save(
                new Blogpost("Inspiring the Next Generation of Open Source", "linux",
                        "Linux Foundation", "https://www.linuxfoundation.org/blog/inspiring-next-generation-open-source/")));
        this.blogpostList.add(blogpostRepository.save(
                new Blogpost("Monkeys of Gibraltar", "monkeys",
                        "Geraldine DeRuiter", "http://www.everywhereist.com/the-monkeys-of-gibraltar/")));
    }

    @Test
    public void blogPostNotFound() throws Exception {
        this.mockMvc.perform(get("/api/v01/blogposts/1111")
                .content(this.json(new Blogpost()))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readSingleBlogpost() throws Exception {
        this.mockMvc.perform(get("/api/v01/blogposts/"
                + this.blogpost.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(this.blogpost.getId().intValue())))
                .andExpect(jsonPath("$.author", is("Dana Shultz")))
                .andExpect(jsonPath("$.title", is("Minimalist Baker")))
                .andExpect(jsonPath("$.url", is("https://minimalistbaker.com/creamy-avocado-banana-green-smoothie/")))
                .andExpect(jsonPath("$.comment", is("great recipe")));
    }

    @Test
    public void readBlogposts() throws Exception {
        this.mockMvc.perform(get("/api/v01/blogposts").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(this.blogpost.getId().intValue())))
                .andExpect(jsonPath("$[0].author", is("Dana Shultz")))
                .andExpect(jsonPath("$[0].title", is("Minimalist Baker")))
                .andExpect(jsonPath("$[0].url", is("https://minimalistbaker.com/creamy-avocado-banana-green-smoothie/")))
                .andExpect(jsonPath("$[0].comment", is("great recipe")))
                .andExpect(jsonPath("$[1].id", is(this.blogpostList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[1].author", is("Linux Foundation")))
                .andExpect(jsonPath("$[1].title", is("Inspiring the Next Generation of Open Source")))
                .andExpect(jsonPath("$[1].url", is("https://www.linuxfoundation.org/blog/inspiring-next-generation-open-source/")))
                .andExpect(jsonPath("$[1].comment", is("linux")))
                .andExpect(jsonPath("$[2].id", is(this.blogpostList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[2].author", is("Geraldine DeRuiter")))
                .andExpect(jsonPath("$[2].title", is("Monkeys of Gibraltar")))
                .andExpect(jsonPath("$[2].url", is("http://www.everywhereist.com/the-monkeys-of-gibraltar/")))
                .andExpect(jsonPath("$[2].comment", is("monkeys")));
    }

    @Test
    public void createBlogpost() throws Exception {
        String bookmarkJson = json(new Blogpost("sample", "sample", "sample", "http://www.everywhereist.com/the-monkeys-of-gibraltar/"));
        this.mockMvc.perform(post("/api/v01/blogposts")
                .contentType(contentType)
                .content(bookmarkJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteBlogpost() throws Exception {
        this.mockMvc.perform(get("/api/v01/blogposts").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)));
        this.mockMvc.perform(delete("/api/v01/blogposts/" + this.blogpost.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
        this.mockMvc.perform(get("/api/v01/blogposts").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void modifyBlogpost() throws Exception {
        String bookmarkJson = json(new Blogpost("test", "test", "test", "http://www.everywhereist.com/the-monkeys-of-gibraltar/"));
        this.mockMvc.perform(get("/api/v01/blogposts").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)));
        this.mockMvc.perform(put("/api/v01/blogposts/" + this.blogpost.getId())
                .contentType(contentType)
                .content(bookmarkJson))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/api/v01/blogposts").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(this.blogpost.getId().intValue())))
                .andExpect(jsonPath("$[0].author", is("test")));
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}