package changwonNationalUniv.koko.service;

import changwonNationalUniv.koko.controller.dto.ProblemRequest;
import changwonNationalUniv.koko.controller.dto.StepRequest;

import java.io.IOException;
import java.util.List;

public interface AdminService {

    Long saveProblem(ProblemRequest problemRequest) throws IOException;

    Long saveStep(StepRequest stepRequest);

    List<Integer> getLevels();

    int deleteProblem(Long id);


    void deleteStep(Long id);
}
