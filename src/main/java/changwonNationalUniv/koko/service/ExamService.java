package changwonNationalUniv.koko.service;

import changwonNationalUniv.koko.dto.response.ExamResponse;
import changwonNationalUniv.koko.entity.Member;

import java.util.List;

public interface ExamService {

    List<ExamResponse> findExamList();


}
