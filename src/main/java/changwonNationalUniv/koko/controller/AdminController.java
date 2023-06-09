package changwonNationalUniv.koko.controller;

import changwonNationalUniv.koko.dto.request.ProblemRequest;
import changwonNationalUniv.koko.dto.request.StepRequest;
import changwonNationalUniv.koko.dto.response.ProblemResponse;
import changwonNationalUniv.koko.dto.response.StepResponse;
import changwonNationalUniv.koko.service.ProblemService;
import changwonNationalUniv.koko.service.StepService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProblemService problemService;
    private final StepService stepService;

    @GetMapping("/adminPage")
    public String adminPage() {
        return "/admin/adminPage";
    }

    @GetMapping("/stepForm")
    public String saveStepForm(Model model) {

        model.addAttribute("stepRequest", new StepRequest());

        return "/admin/stepForm";
    }

    @PostMapping("/stepForm")
    public String saveStep(@Validated @ModelAttribute StepRequest stepRequest
            , BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "/admin/stepForm";
        }

        Long id = stepService.saveStep(stepRequest);

        return "redirect:/";
    }

    @GetMapping("/updateStep/{id}")
    public String updateStepForm(Model model, @PathVariable Long id) {

        StepResponse stepResponse = stepService.findStep(id);

        model.addAttribute("step", stepResponse);

        return "/admin/updateStepForm";
    }

    @PostMapping("/updateStep/{id}")
    public String updateStep(@Validated @ModelAttribute("step") StepRequest stepRequest
            , BindingResult bindingResult, @PathVariable Long id) {

        if(bindingResult.hasErrors()) {
            return "/admin/updateStepForm";
        }

        stepService.updateStep(id, stepRequest);

        return "redirect:/content/step";
    }

    @GetMapping("/updateProblem/{id}")
    public String updateProblemForm(Model model, @PathVariable Long id) {

        ProblemResponse problem = problemService.findProblem(id);
        model.addAttribute("problem", problem);

        return "/admin/updateProblemForm";
    }

    @PostMapping("/updateProblem/{problemId}")
    public String updateProblem(@Validated @ModelAttribute ProblemRequest problemRequest
            , BindingResult bindingResult, @PathVariable Long problemId) throws IOException {

        if(bindingResult.hasErrors()) {
            return "/admin/updateProblemForm";
        }

        problemService.updateProblem(problemId, problemRequest);

        return "redirect:/content/problem/" + problemId;
    }

    @GetMapping("/problemForm")
    public String saveProblemForm(Model model) {

        model.addAttribute("problemRequest", new ProblemRequest());

        return "/admin/problemForm";
    }

    @PostMapping("/problemForm")
    public String saveProblem(@Validated @ModelAttribute ProblemRequest problemRequest
            , BindingResult bindingResult) throws IOException {

        if(bindingResult.hasErrors()) {
            return "/admin/problemForm";
        }

        Long id = problemService.saveProblem(problemRequest);

        return "redirect:/content/problem/" + id;
    }

    @RequestMapping ("/deleteProblem/{problemId}")
    public String deleteProblem(@PathVariable Long problemId) {

        int level = problemService.deleteProblem(problemId);

        return "redirect:/content/step/" + level;
    }

    @RequestMapping ("/deleteStep/{id}")
    public String deleteStep(@PathVariable Long id) {

        stepService.deleteStep(id);

        return "redirect:/content/step";
    }

    @ModelAttribute("levels")
    public List<Integer> levels() {

        List<Integer> levels = stepService.getLevels();

        if(levels != null) {
            return levels;
        }

        return new ArrayList<>();
    }
}
