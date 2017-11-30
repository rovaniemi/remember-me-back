package remember.controller;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import remember.domain.InstanceType;
import remember.domain.instances.Book;
import remember.domain.instances.Video;
import remember.repository.inertances.VideoRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
public class VideoController {

    @Autowired
    private VideoRepository videoRepository;

    @GetMapping("/videos")
    @ResponseBody
    public List<Video> getAll() {
        return videoRepository.findAll();
    }

    @GetMapping("/videos/{id}")
    @ResponseBody
    public Video getVideo(@PathVariable Long id, HttpServletResponse response) {
        if (videoRepository.findOne(id) == null) response.setStatus(HttpStatus.SC_NOT_FOUND);
        return videoRepository.findOne(id);
    }

    @PostMapping("/videos")
    @ResponseBody
    public Video addVideo(@Validated @RequestBody Video video, HttpServletResponse response) {
        video.setType(InstanceType.VIDEO);
        videoRepository.saveAndFlush(video);
        response.setStatus(HttpStatus.SC_CREATED);
        return video;
    }

    @DeleteMapping("/videos/{id}")
    @ResponseBody
    public Video deleteVideo(@PathVariable Long id) {
        Video video = videoRepository.findOne(id);
        videoRepository.delete(id);
        return video;
    }
}
