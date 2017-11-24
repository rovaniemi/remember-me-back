package remember.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import remember.domain.inertances.Video;
import remember.repository.inertances.VideoRepository;

import java.util.List;
import java.util.Map;

@RestController
public class VideoController {

    @Autowired
    private VideoRepository videoRepository;

    @GetMapping("/video")
    @ResponseBody
    public List<Video> getAll() {
        return videoRepository.findAll();
    }

    @GetMapping("/video/{id}")
    @ResponseBody
    public Video getVideo(@PathVariable Long id) {
        return videoRepository.findOne(id);
    }

    @PostMapping("/video")
    @ResponseBody
    public Video addVideo(@RequestBody Map<String, String> request) {
        Video video = new Video();
        video.setTitle(request.get("name"));
        video.setUrl(request.get("url"));
        videoRepository.saveAndFlush(video);
        return video;
    }

    @DeleteMapping("/video/{id}")
    @ResponseBody
    public void deleteVideo(@PathVariable Long id) {
        videoRepository.delete(id);
    }
}