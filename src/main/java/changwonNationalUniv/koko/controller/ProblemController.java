package changwonNationalUniv.koko.controller;

import changwonNationalUniv.koko.dto.response.ChallengedProblemHistoryResponse;
import changwonNationalUniv.koko.dto.response.ProblemResponse;
import changwonNationalUniv.koko.service.ProblemService;
import changwonNationalUniv.koko.utils.file.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/content")
public class ProblemController {

    private final FileStore fileStore;
    private final ProblemService problemService;

    @GetMapping("/problem/{problemId}")
    public String problem(@PathVariable Long problemId, Model model) {

        ProblemResponse problem = problemService.findProblem(problemId);
        model.addAttribute("problem", problem);

        return "/content/problem";
    }

    @PostMapping("/problem/{problemId}")
    public ResponseEntity<ChallengedProblemHistoryResponse> problemEvaluation(@PathVariable Long problemId,
                                                                              @RequestParam("audio") MultipartFile audio) throws IOException {

        //받은 아이디와 오디오데이터를 채점 서비스로 넘겨줌 -> 채점서비는 해당 아디의 원래문자와 해당 오디오파일을 AI api에 돌린 후
        //원래 오디오파일의 텍스트와 정답여부등등을 보내줌.
        ChallengedProblemHistoryResponse evaluateResult = problemService.evaluate(problemId, audio);

        return ResponseEntity.ok(evaluateResult);
    }


    @GetMapping("/step/{level}")
    public String problemList(@PathVariable int level ,Model model) {

        List<ProblemResponse> problems = problemService.findProblems(level);
        model.addAttribute("problems", problems);

        return "/content/problemList";

    }

    @ResponseBody
    @GetMapping("/audio/{id}")
    public Resource audio(@PathVariable Long id) throws MalformedURLException {
        String filename = problemService.findFilename(id);
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

}
