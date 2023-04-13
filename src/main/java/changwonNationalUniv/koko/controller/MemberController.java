package changwonNationalUniv.koko.controller;

import changwonNationalUniv.koko.dto.request.MemberRequest;
import changwonNationalUniv.koko.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("member", new MemberRequest());
        return "/member/join";
    }

    @PostMapping("/join")
    public String join(@Validated @ModelAttribute("member") MemberRequest form, BindingResult bindingResult) {

        checkingPassword(form, bindingResult);

        if(bindingResult.hasErrors()) {
            return "/member/join";
        }


        memberService.save(form);

        return "redirect:/";
    }

    private static void checkingPassword(MemberRequest form, BindingResult bindingResult) {
        if(!form.getPassword().equals(form.getCheckingPassword())) {
            bindingResult.addError(new FieldError("MemberRequestDto", "checkingPassword", "입력하신 비밀번호가 일치하지 않습니다."));
        }
    }


}
