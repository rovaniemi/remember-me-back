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

    @GetMapping("/tip")
    @ResponseBody
    public List<Tip> getAll() {
        return tipRepository.findAll();
    }

    @GetMapping("/tip/{id}")
    @ResponseBody
    public Tip getTip(@PathVariable Long id) {
        return tipRepository.findOne(id);
    }

    @DeleteMapping("/tip/{id}")
    @ResponseBody
    public void deleteTip(@PathVariable Long id) {
        tipRepository.delete(id);
    }
}
