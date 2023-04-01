package changwonNationalUniv.koko.service;

import changwonNationalUniv.koko.controller.dto.ProblemRequest;
import changwonNationalUniv.koko.controller.dto.StepRequest;

import java.io.IOException;

public interface AdminService {

    Long saveProblem(ProblemRequest problemRequest) throws IOException;

    Long saveStep(StepRequest stepRequest);

}
