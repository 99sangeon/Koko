package changwonNationalUniv.koko.controller;

import changwonNationalUniv.koko.dto.response.StepResponse;
import changwonNationalUniv.koko.service.StepService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/content")
public class StepController {

    private final StepService stepService;

    @GetMapping("/step")
    public String step(Model model) {

        List<StepResponse> steps = stepService.findSteps();
        model.addAttribute("steps", steps);

        return "/content/step";
    }

}
