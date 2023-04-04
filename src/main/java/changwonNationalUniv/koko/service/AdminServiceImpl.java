package changwonNationalUniv.koko.service;

import changwonNationalUniv.koko.controller.dto.ProblemRequest;
import changwonNationalUniv.koko.controller.dto.StepRequest;
import changwonNationalUniv.koko.controller.dto.UploadFile;
import changwonNationalUniv.koko.entity.Problem;
import changwonNationalUniv.koko.entity.Step;
import changwonNationalUniv.koko.repository.ProblemRepository;
import changwonNationalUniv.koko.repository.StepRepository;
import changwonNationalUniv.koko.util.file.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final FileStore fileStore;
    private final ProblemRepository problemRepository;
    private final StepRepository stepRepository;

    @Override
    public Long saveProblem(ProblemRequest problemRequest) throws IOException {

        UploadFile uploadFile = fileStore.storeFile(problemRequest.getAudioFile());
        problemRequest.setUploadFile(uploadFile);

        Problem problem = problemRepository.save(problemRequest.toEntity());

        return problem.getId();
    }

    @Override
    public Long saveStep(StepRequest stepRequest) {
        Step step = stepRepository.save(stepRequest.toEntity());
        return step.getId();
    }

    @Override
    public StepRequest findStepRequest(Long id) {
        Step step = stepRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        return StepRequest.of(step);
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
    public int deleteProblem(Long id) {
        Problem problem= problemRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        int level = problem.getLevel();
        problemRepository.delete(problem);

        return level;
    }

    @Override
    public void deleteStep(Long id) {
        Step step = stepRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        stepRepository.delete(step);
    }

    @Override
    public void updateProblem(Long id, ProblemRequest problemRequest) {

    }
}
