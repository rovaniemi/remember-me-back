package remember.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import remember.domain.Tip;
import remember.repository.TipRepository;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;

@RestController
public class TipController {

    @Autowired
    private TipRepository tipRepository;

    @GetMapping("/tips")
    @ResponseBody
    public List<Tip> getAll() {
        return tipRepository.findAll();
    }

    @GetMapping("/tips/{id}")
    @ResponseBody
    public Tip getTip(@PathVariable Long id, HttpServletResponse response) {
        if (tipRepository.findOne(id) == null) response.setStatus(HttpStatus.SC_NOT_FOUND);
        return tipRepository.findOne(id);
    }

    @DeleteMapping("/tips/{id}")
    @ResponseBody
    public Tip deleteTip(@PathVariable Long id) {
        Tip tip = tipRepository.findOne(id);
        tipRepository.delete(id);
        return tip;
    }
}
