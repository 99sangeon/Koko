package changwonNationalUniv.koko.controller;


import changwonNationalUniv.koko.controller.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class LoginController {

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("login", new LoginDto());
        return "/oauth/login";
    }
}
