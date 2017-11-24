package remember.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import remember.domain.inertances.Blogpost;
import remember.repository.inertances.BlogpostRepository;

import java.util.List;
import java.util.Map;

@RestController
public class BlogpostController {

    @Autowired
    private BlogpostRepository blogpostRepository;

    @GetMapping("/blogposts")
    @ResponseBody
    public List<Blogpost> getAll() {
        return blogpostRepository.findAll();
    }

    @GetMapping("/blogposts/{id}")
    @ResponseBody
    public Blogpost getBlogpost(@PathVariable Long id) {
        return blogpostRepository.findOne(id);
    }

    @PostMapping("/blogposts")
    @ResponseBody
    public Blogpost addBlogpost(@RequestBody Map<String, String> request) {
        Blogpost blogpost = new Blogpost();
        blogpost.setTitle(request.get("name"));
        blogpost.setUrl(request.get("url"));
        blogpost.setAuthor(request.get("author"));
        blogpostRepository.saveAndFlush(blogpost);
        return blogpost;
    }

    @DeleteMapping("/blogposts/{id}")
    @ResponseBody
    public void deleteBlogpost(@PathVariable Long id) {
        blogpostRepository.delete(id);
    }
}
