package changwonNationalUniv.koko.service;

import changwonNationalUniv.koko.dto.request.StepRequest;
import changwonNationalUniv.koko.dto.response.StepResponse;
import changwonNationalUniv.koko.entity.Step;
import changwonNationalUniv.koko.repository.StepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class StepServiceImpl implements StepService{

    private final StepRepository stepRepository;


    @Override
    public Long saveStep(StepRequest stepRequest) {
        Step step = stepRepository.save(stepRequest.toEntity());
        return step.getId();
    }

    @Override
    public StepResponse findStep(Long id) {
        Step step = stepRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        return StepResponse.of(step);
    }

    @Override
    public StepResponse findStep(int level) {
        Step step = stepRepository.findByLevel(level).orElseThrow(() -> new NoSuchElementException());
        return StepResponse.of(step);
    }

    @Override
    public void updateStep(Long id, StepRequest stepRequest) {
        Step step = stepRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        step.setLevel(stepRequest.getLevel());
        step.setTitle(stepRequest.getTitle());
        step.setDescription(stepRequest.getDescription());
    }

    @Override
    public List<Integer> getLevels() {
        return stepRepository.findLevelsSortedAsc();
    }

    @Override
    public void deleteStep(Long id) {
        Step step = stepRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        stepRepository.delete(step);
    }

    @Override
    public List<StepResponse> findSteps() {
        List<Step> steps = stepRepository.findAllByOrderByLevelAsc();
        List<StepResponse> stepResponses = new ArrayList<>();

        for (Step step: steps) {
            stepResponses.add(StepResponse.of(step));
        }

        return stepResponses;
    }

}
