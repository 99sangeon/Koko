package changwonNationalUniv.koko.service;

import changwonNationalUniv.koko.controller.dto.ProblemRequest;

import java.io.IOException;

public interface AdminService {

    Long saveProblem(ProblemRequest problemRequest) throws IOException;

}
