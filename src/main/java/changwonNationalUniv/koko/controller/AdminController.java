package changwonNationalUniv.koko.controller;

import changwonNationalUniv.koko.controller.dto.ProblemRequest;
import changwonNationalUniv.koko.controller.dto.StepRequest;
import changwonNationalUniv.koko.service.AdminService;
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

    private final AdminService adminService;

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

        Long id = adminService.saveStep(stepRequest);

        return "redirect:/content/step";
    }

    @GetMapping("/updateStep/{id}")
    public String updateStepForm(Model model, @PathVariable Long id) {

        StepRequest stepRequest = adminService.findStepRequest(id);

        model.addAttribute("stepRequest", stepRequest);

        return "/admin/updateStepForm";
    }

    @PostMapping("/updateStep/{id}")
    public String updateStep(@Validated @ModelAttribute StepRequest stepRequest
            , BindingResult bindingResult, @PathVariable Long id) {

        if(bindingResult.hasErrors()) {
            return "/admin/updateStepForm";
        }

        adminService.updateStep(id, stepRequest);

        return "redirect:/content/step";
    }

    @GetMapping("/updateProblem/{id}")
    public String updateProblemForm(Model model, @PathVariable Long id) {

        StepRequest stepRequest = adminService.findStepRequest(id);

        model.addAttribute("stepRequest", stepRequest);

        return "/admin/updateProblemForm";
    }

    @PostMapping("/updateProblem/{id}")
    public String updateProblem(@Validated @ModelAttribute ProblemRequest problemRequest
            , BindingResult bindingResult, @PathVariable Long id) {

        if(bindingResult.hasErrors()) {
            return "/admin/updateProblemForm";
        }

        adminService.updateProblem(id, problemRequest);

        return "redirect:/content/problem/" + id;
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

        Long id = adminService.saveProblem(problemRequest);

        return "redirect:/content/problem/" + id;
    }

    @RequestMapping ("/deleteProblem/{id}")
    public String deleteProblem(@PathVariable Long id) {

        int level = adminService.deleteProblem(id);

        return "redirect:/content/step/" + level;
    }

    @RequestMapping ("/deleteStep/{id}")
    public String deleteStep(@PathVariable Long id) {

        adminService.deleteStep(id);

        return "redirect:/content/step";
    }

    @ModelAttribute("levels")
    public List<Integer> levels() {

        List<Integer> levels = adminService.getLevels();

        if(levels != null) {
            return levels;
        }

        return new ArrayList<>();
    }
}
