package remember.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import remember.domain.Tip;
import remember.repository.TipRepository;
import java.util.List;

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
    public Tip getTip(@PathVariable Long id) {
        return tipRepository.findOne(id);
    }

    @DeleteMapping("/tips/{id}")
    @ResponseBody
    public void deleteTip(@PathVariable Long id) {
        tipRepository.delete(id);
    }
}
