package remember.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import remember.domain.instances.Video;
import remember.repository.inertances.VideoRepository;

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
    public Video getVideo(@PathVariable Long id) {
        return videoRepository.findOne(id);
    }

    @PostMapping("/videos")
    @ResponseBody
    public Video addVideo(@RequestBody Map<String, String> request) {
        Video video = new Video(request.get("url"), request.get("title"), request.get("comment"));
        videoRepository.saveAndFlush(video);
        return video;
    }

    @DeleteMapping("/videos/{id}")
    @ResponseBody
    public void deleteVideo(@PathVariable Long id) {
        videoRepository.delete(id);
    }
}
