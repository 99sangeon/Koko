package changwonNationalUniv.koko.controller;

import changwonNationalUniv.koko.dto.request.MemberRequest;
import changwonNationalUniv.koko.dto.response.MemberResponse;
import changwonNationalUniv.koko.dto.response.SuccessCntResponse;
import changwonNationalUniv.koko.entity.Member;
import changwonNationalUniv.koko.service.MemberService;
import changwonNationalUniv.koko.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ProblemService problemService;

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

    @GetMapping("/member/myPage")
    public String myPage(Model model) {

        Member member = memberService.getCurrentMember();
        MemberResponse memberResponse = MemberResponse.of(member);
        memberResponse.setRank(memberService.getMyRank(member.getUserId()));

        model.addAttribute("memberResponse", memberResponse);
        
        return "/member/myPage";

    }

    @GetMapping("/member/visualization")
    public String visualization(Model model) {

        Member member = memberService.getCurrentMember();
        MemberResponse memberResponse = MemberResponse.of(member);
        memberResponse.setRank(memberService.getMyRank(member.getUserId()));

        List<SuccessCntResponse> successCnt= problemService.findSuccessCntForVisualChart(member);

        model.addAttribute("memberResponse", memberResponse);
        model.addAttribute("successCnt", successCnt);

        return "/member/visualization";
    }

    private static void checkingPassword(MemberRequest form, BindingResult bindingResult) {
        if(!form.getPassword().equals(form.getCheckingPassword())) {
            bindingResult.addError(new FieldError("MemberRequestDto", "checkingPassword", "입력하신 비밀번호가 일치하지 않습니다."));
        }
    }


}
