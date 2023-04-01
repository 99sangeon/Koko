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
}
