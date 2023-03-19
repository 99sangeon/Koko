package changwonNationalUniv.koko.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/content")
public class ContentController {

    @GetMapping("/speaking/{speakingListId}")
    public String speakingPage(@PathVariable int speakingListId) {
        return "/content/speakingPage";
    }

    @PostMapping("/speaking/{speakingListId}")
    public String speakingEvaluation(@PathVariable int speakingListId) {
        return "/content/speakingPage";
    }
}
