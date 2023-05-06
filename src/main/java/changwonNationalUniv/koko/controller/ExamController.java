package changwonNationalUniv.koko.controller;

import changwonNationalUniv.koko.dto.response.ExamResponse;
import changwonNationalUniv.koko.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member/exam")
public class ExamController {

    private final ExamService examService;

    @GetMapping("/list")
    public String examList(Model model) {

        List<ExamResponse> examResponses = examService.findExamList();

        model.addAttribute("exams", examResponses);

        return "/exam/list";
    }
}
