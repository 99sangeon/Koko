package changwonNationalUniv.koko.service;

import changwonNationalUniv.koko.dto.request.ProblemRequest;
import changwonNationalUniv.koko.dto.response.ChallengedProblemHistoryResponse;
import changwonNationalUniv.koko.dto.response.ProblemResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProblemService {

    Long saveProblem(ProblemRequest problemRequest) throws IOException;

    int deleteProblem(Long id);

    void updateProblem(Long id, ProblemRequest problemRequest) throws IOException;

    ChallengedProblemHistoryResponse evaluate(Long problem_id, MultipartFile audio) throws IOException;

    List<ProblemResponse> findProblems(int level);

    ProblemResponse findProblem(Long id);

    String findFilename(Long id);
}
