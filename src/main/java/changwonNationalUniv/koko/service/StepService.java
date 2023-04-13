package changwonNationalUniv.koko.service;

import changwonNationalUniv.koko.dto.request.StepRequest;
import changwonNationalUniv.koko.dto.response.StepResponse;

import java.util.List;

public interface StepService {

    Long saveStep(StepRequest stepRequest);

    StepResponse findStep(Long id);

    void updateStep(Long id, StepRequest stepRequest);

    List<Integer> getLevels();

    void deleteStep(Long id);

    List<StepResponse> findSteps();
}
