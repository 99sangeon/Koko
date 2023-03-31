package changwonNationalUniv.koko.service;

import changwonNationalUniv.koko.controller.dto.ProblemResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ContentService {
    void evaluate(Long id, MultipartFile audio) throws IOException;

    List<ProblemResponse> findProblems(int level);

    ProblemResponse findProblem(Long id);
}
