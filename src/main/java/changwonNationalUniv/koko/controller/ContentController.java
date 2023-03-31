package changwonNationalUniv.koko.controller;

import changwonNationalUniv.koko.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/content")
public class ContentController {

    private final ContentService contentService;

    @GetMapping("/problem/{problemId}")
    public String problemPage(@PathVariable Long problemId, Model model) {

        model.addAttribute("sentence", "안녕하세요?");

        return "/content/problem";
    }

    @PostMapping("/problem/{problemId}")
    public ResponseEntity<String> problemEvaluation(@PathVariable Long problemId,
                                                     @RequestParam("audio") MultipartFile audio) throws IOException {


        //받은 아이디와 오디로데이터를 채점 서비스로 넘겨줌 -> 채점서비는 해당 아디의 원래문자와 해당 오디오파일을 AI api에 돌린 후
        //원래 오디오파일의 텍스트와 정답여부등등을 보내줌.
        contentService.evaluate(problemId, audio);

        return ResponseEntity.ok("ㅎㅇ");
    }

    @GetMapping("/step")
    public String stepPage(Model model) {

        return "/content/step";
    }


}
