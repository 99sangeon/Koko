package changwonNationalUniv.koko.controller;

import changwonNationalUniv.koko.controller.dto.ProblemRequest;
import changwonNationalUniv.koko.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/adminPage")
    public String adminPage() {
        return "/admin/adminPage";
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

        adminService.saveProblem(problemRequest);

        return "redirect:/";
    }
}