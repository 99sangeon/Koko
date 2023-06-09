package changwonNationalUniv.koko.controller;

import changwonNationalUniv.koko.dto.response.ChallengedProblemHistoryResponse;
import changwonNationalUniv.koko.dto.response.ProblemResponse;
import changwonNationalUniv.koko.dto.response.StepResponse;
import changwonNationalUniv.koko.service.ProblemService;
import changwonNationalUniv.koko.service.StepService;
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
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/content")
public class ProblemController {

    private final FileStore fileStore;
    private final ProblemService problemService;
    private final StepService stepService;

    @GetMapping("/problem/{problemId}")
    public String problem(@PathVariable Long problemId, Model model) {

        ProblemResponse problem = problemService.findProblem(problemId);
        model.addAttribute("problem", problem);

        return "/content/problem";
    }

    //매핑된 주소로 사용자가 녹음하여 음성데이터를 전송한다.
    @PostMapping("/problem/{problemId}")
    public ResponseEntity<ChallengedProblemHistoryResponse> problemEvaluation
            (@PathVariable Long problemId,
             @RequestParam("audio") MultipartFile audio) throws IOException {

        //사용자에게 받은 문제 아이디와 음성데이터를 채점API로 넘겨주고 점수와 피드백을 반환받는다.
        ChallengedProblemHistoryResponse evaluateResult = problemService.evaluate(problemId, audio);

        //사용자에게 점수와 피드백을 전송한다.
        return ResponseEntity.ok(evaluateResult);
    }


    @GetMapping("/step/{level}")
    public String problemList(@PathVariable int level ,Model model) {

        StepResponse stepResponse = stepService.findStep(level);
        List<ProblemResponse> problemsResponses = problemService.findProblemResponses(level);

        model.addAttribute("step", stepResponse);
        model.addAttribute("problems", problemsResponses);

        return "/content/step";
    }

    @ResponseBody
    @GetMapping("/audio/{id}")
    public Resource audio(@PathVariable Long id) throws MalformedURLException {
        String filename = problemService.findFilename(id);
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @ResponseBody
    @GetMapping("/noise")
    public Resource noiseImg() throws MalformedURLException {

        return new UrlResource("file:" + "C:\\Users\\User\\Desktop\\img\\Original.jpg");
    }

    @ResponseBody
    @GetMapping("/deNoise")
    public Resource deNoiseImg() throws MalformedURLException {

        return new UrlResource("file:" + "C:\\Users\\User\\Desktop\\img\\Denoised.jpg");
    }

}
