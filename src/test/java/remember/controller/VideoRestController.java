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
import remember.domain.instances.Video;
import remember.repository.inertances.VideoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@WebAppConfiguration
public class VideoRestController {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<Video> videos = new ArrayList<>();

    @Autowired
    private VideoRepository videoRepository;

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

        this.videoRepository.deleteAllInBatch();

        this.videos.add(videoRepository.save(
                new Video("https://www.youtube.com/watch?v=dQw4w9WgXcQ", "Rick Astley", "awesome video")));
        this.videos.add(videoRepository.save(
                new Video("https://www.youtube.com/watch?v=bWPMSSsVdPk", "Learn HTML", "no comment")));
        this.videos.add(videoRepository.save(
                new Video("https://www.youtube.com/watch?v=ZbZSe6N_BXs", "Happy", "makes me very happy")));
    }

    @Test
    public void videoNotFound() throws Exception {
        this.mockMvc.perform(get("/api/v01/videos/7777")
                .content(this.json(new Video()))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void showSingleVideo() throws Exception {
        this.mockMvc.perform(get("/api/v01/videos/"
                + this.videos.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(this.videos.get(0).getId().intValue())))
                .andExpect(jsonPath("$.url", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ")))
                .andExpect(jsonPath("$.title", is("Rick Astley")))
                .andExpect(jsonPath("$.comment", is("awesome video")));
    }

    @Test
    public void showAllVideos() throws Exception {
        this.mockMvc.perform(get("/api/v01/videos").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(this.videos.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].url", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ")))
                .andExpect(jsonPath("$[0].title", is("Rick Astley")))
                .andExpect(jsonPath("$[0].comment", is("awesome video")))
                .andExpect(jsonPath("$[1].id", is(this.videos.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].url", is("https://www.youtube.com/watch?v=bWPMSSsVdPk")))
                .andExpect(jsonPath("$[1].title", is("Learn HTML")))
                .andExpect(jsonPath("$[1].comment", is("no comment")))
                .andExpect(jsonPath("$[2].id", is(this.videos.get(2).getId().intValue())))
                .andExpect(jsonPath("$[2].url", is("https://www.youtube.com/watch?v=ZbZSe6N_BXs")))
                .andExpect(jsonPath("$[2].title", is("Happy")))
                .andExpect(jsonPath("$[2].comment", is("makes me very happy")));
    }

    @Test
    public void createVideo() throws Exception {
        String bookmarkJson = json(new Video("http://www.everywhereist.com/the-monkeys-of-gibraltar/", "sample", "sample"));
        this.mockMvc.perform(post("/api/v01/videos")
                .contentType(contentType)
                .content(bookmarkJson))
                .andExpect(status().isCreated());
    }
    
    @Test
    public void deleteVideo() throws Exception {
        this.mockMvc.perform(get("/api/v01/videos").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)));
        this.mockMvc.perform(delete("/api/v01/videos/" + this.videos.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
        this.mockMvc.perform(get("/api/v01/videos").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
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
