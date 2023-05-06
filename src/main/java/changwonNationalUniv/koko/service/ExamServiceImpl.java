package changwonNationalUniv.koko.service;

import changwonNationalUniv.koko.dto.response.ExamResponse;
import changwonNationalUniv.koko.entity.Member;
import changwonNationalUniv.koko.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService{

    private final ExamRepository examRepository;
    private final MemberService memberService;

    @Override
    public List<ExamResponse> findExamList() {

        Member member = memberService.getCurrentMember();
        List<ExamResponse> examResponses = examRepository.findExamWithStepByMember(member);

        return examResponses;
    }
}
