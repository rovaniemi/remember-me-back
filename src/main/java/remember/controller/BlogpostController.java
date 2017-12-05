package remember.controller;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import remember.domain.InstanceType;
import remember.domain.instances.Blogpost;
import remember.repository.inertances.BlogpostRepository;

import javax.servlet.http.HttpServletResponse;
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
    public Blogpost getBlogpost(@PathVariable Long id, HttpServletResponse response) {
        if (blogpostRepository.findOne(id) == null) {
            response.setStatus(HttpStatus.SC_NOT_FOUND);
        }
        return blogpostRepository.findOne(id);
    }

    @PostMapping("/blogposts")
    @ResponseBody
    public Blogpost addBlogpost(@Validated @RequestBody Blogpost blogpost, HttpServletResponse response) {
        blogpost.setType(InstanceType.BLOGPOST);
        blogpostRepository.saveAndFlush(blogpost);
        response.setStatus(HttpStatus.SC_CREATED);
        return blogpost;
    }

    @DeleteMapping("/blogposts/{id}")
    @ResponseBody
    public Blogpost deleteBlogpost(@PathVariable Long id) {
        Blogpost blogpost = blogpostRepository.findOne(id);
        blogpostRepository.delete(id);
        return blogpost;
    }

    @PutMapping("/blogposts/{id}")
    @ResponseBody
    public Blogpost modifyBlogpost(@Validated @RequestBody Blogpost blogpost,@PathVariable Long id, HttpServletResponse response) {
        Blogpost original = blogpostRepository.findOne(id);
        original.setTitle(blogpost.getTitle());
        original.setAuthor(blogpost.getAuthor());
        original.setUrl(blogpost.getUrl());
        original.setRead(blogpost.isRead());
        original.setComment(blogpost.getComment());
        blogpostRepository.flush();
        response.setStatus(HttpStatus.SC_OK);
        return original;
    }
}
