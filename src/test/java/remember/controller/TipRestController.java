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
import remember.domain.instances.Book;
import remember.domain.instances.Video;
import remember.domain.instances.Blogpost;
import remember.domain.Tip;
import remember.repository.TipRepository;

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
public class TipRestController {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<Tip> tips = new ArrayList<>();

    @Autowired
    private TipRepository tipRepository;

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

        this.tipRepository.deleteAllInBatch();

        this.tips.add(tipRepository.save(new Book("Tom", "Tom's adventure", "fine book")));
        this.tips.add(tipRepository.save(new Blogpost("Dana Shultz",
                "https://minimalistbaker.com/creamy-avocado-banana-green-smoothie/",
                "Minimalist Baker", "great recipe")));
        this.tips.add(tipRepository.save(new Video("https://www.youtube.com/watch?v=dQw4w9WgXcQ", "Rick Astley", "awesome video")));
    }

    @Test
    public void tipNotFound() throws Exception {
        this.mockMvc.perform(get("/api/v01/tips/1111")
                .content(this.json(new Book()))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void showSingleTip() throws Exception {
        this.mockMvc.perform(get("/api/v01/tips/"
                + this.tips.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(this.tips.get(0).getId().intValue())))
                .andExpect(jsonPath("$.author", is("Tom")))
                .andExpect(jsonPath("$.title", is("Tom's adventure")))
                .andExpect(jsonPath("$.comment", is("fine book")));
    }

    @Test
    public void showTips() throws Exception {
        this.mockMvc.perform(get("/api/v01/tips").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(this.tips.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].author", is("Tom")))
                .andExpect(jsonPath("$[0].title", is("Tom's adventure")))
                .andExpect(jsonPath("$[0].comment", is("fine book")))
                .andExpect(jsonPath("$[1].id", is(this.tips.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].author", is("Dana Shultz")))
                .andExpect(jsonPath("$[1].title", is("Minimalist Baker")))
                .andExpect(jsonPath("$[1].url", is("https://minimalistbaker.com/creamy-avocado-banana-green-smoothie/")))
                .andExpect(jsonPath("$[1].comment", is("great recipe")))
                .andExpect(jsonPath("$[2].id", is(this.tips.get(2).getId().intValue())))
                .andExpect(jsonPath("$[2].url", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ")))
                .andExpect(jsonPath("$[2].title", is("Rick Astley")))
                .andExpect(jsonPath("$[2].comment", is("awesome video")));
    }

    @Test
    public void deleteTip() throws Exception {
        this.mockMvc.perform(get("/api/v01/tips").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)));
        this.mockMvc.perform(delete("/api/v01/tips/" + this.tips.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
        this.mockMvc.perform(get("/api/v01/tips").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
