package changwonNationalUniv.koko.service;

import changwonNationalUniv.koko.dto.request.ProblemRequest;
import changwonNationalUniv.koko.dto.request.UploadFile;
import changwonNationalUniv.koko.dto.response.ChallengedProblemHistoryResponse;
import changwonNationalUniv.koko.dto.response.ProblemResponse;
import changwonNationalUniv.koko.dto.response.SuccessCntResponse;
import changwonNationalUniv.koko.entity.*;
import changwonNationalUniv.koko.enums.ClearState;
import changwonNationalUniv.koko.exception.FileNotFoundException;
import changwonNationalUniv.koko.exception.ProblemNotFoundException;
import changwonNationalUniv.koko.repository.*;
import changwonNationalUniv.koko.utils.file.FileStore;
import changwonNationalUniv.koko.utils.file.FileTypeConverter;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService{

    private final MemberService memberService;
    private final ProblemRepository problemRepository;
    private final SecurityService securityService;
    private final ChallengedProblemRepository challengedProblemRepository;
    private final ChallengedProblemHistoryRepository challengedProblemHistoryRepository;
    private final StepRepository stepRepository;
    private final FileStore fileStore;
    private final RestTemplateService restTemplateService;
    private final ChallengedProblemRepositoryCustom challengedProblemRepositoryCustom;

    @Override
    public Long saveProblem(ProblemRequest problemRequest) throws IOException {

        if(problemRequest.getAudioFile() != null) {
            UploadFile uploadFile = fileStore.storeFile(problemRequest.getAudioFile());
            problemRequest.setUploadFile(uploadFile);
        }

        Step step = stepRepository.findByLevel(problemRequest.getLevel())
                .orElseThrow(() -> new NoSuchElementException());

        Problem problem = problemRequest.toEntity();
        step.addProblem(problem);

        problemRepository.save(problem);

        return problem.getId();
    }

    @Override
    public int deleteProblem(Long id) {
        Problem problem= problemRepository.findById(id).orElseThrow(() -> new ProblemNotFoundException("해당 문제는 존재하지 않습니다."));
        int level = problem.getLevel();
        problemRepository.delete(problem);

        return level;
    }

    @Override
    public void updateProblem(Long id, ProblemRequest problemRequest) throws IOException {
        Problem problem= problemRepository.findById(id).orElseThrow(() -> new NoSuchElementException());

        problem.setLevel(problemRequest.getLevel());
        problem.setKorean(problemRequest.getKorean());
        problem.setEnglish(problemRequest.getEnglish());
        problem.setKoPronunciation(problemRequest.getKoPronunciation());
        problem.setEnPronunciation(problemRequest.getEnPronunciation());
        problem.setExp(problemRequest.getExp());

        if(problemRequest.getAudioFile() != null) {
            updateFile(problemRequest, problem);
        }
    }

    private void updateFile(ProblemRequest problemRequest, Problem problem) throws IOException {

        if(problem.getUploadFile() != null) {
            File file = new File(fileStore.getFullPath(problem.getUploadFile().getStoreFileName()));
            file.delete();
        }

        UploadFile uploadFile = fileStore.storeFile(problemRequest.getAudioFile());
        problem.setUploadFile(uploadFile);
    }

    @Override
    public List<ProblemResponse> findProblemResponses(int level) {

        List<ProblemResponse> problemResponses = new ArrayList<>();

        if(securityService.isAuthenticated()) {
            Member member = memberService.getCurrentMember();
            problemResponses = problemRepository.findProblemResponsesWithCpByMemberAndLevel(member, level);
        }
        else{
            List<Problem> problems = problemRepository.findByLevel(level);
            for (Problem p : problems) {
                problemResponses.add(ProblemResponse.of(p));
            }
        }
        return problemResponses;
    }

    @Override
    public ProblemResponse findProblem(Long id) {

        Problem problem= problemRepository.findById(id).orElseThrow(() -> new ProblemNotFoundException());

        return ProblemResponse.of(problem);
    }

    @Override
    public String findFilename(Long problemId) {
        return problemRepository.findFilename(problemId).orElseThrow(
                () -> new FileNotFoundException("해당 음성파일이 등록되지 않았습니다.\n 관리자에게 문의해 주세요."));
    }

    @Override
    public List<SuccessCntResponse> findSuccessCntForVisualChart(Member member) {
       return challengedProblemRepositoryCustom.findSuccessCntForVisualChart(member.getId());
    }

    @Override
    public ChallengedProblemHistoryResponse evaluate(Long problemId, MultipartFile audio) throws IOException {

            Member member = memberService.getCurrentMember();  //현재 로그인 중인 사용자의 정보를 가져온다.
            Problem problem = problemRepository.findById(problemId).orElseThrow(() -> new ProblemNotFoundException("해당 문제를 찾을 수 없습니다.")); //사용자가 체점을 요청한 문제의 정보를 가져온다.
            ChallengedProblem challengedProblem = challengedProblemRepository
                                                    .findChallengedProblemByMemberAndProblem(member, problem)
                                                    .orElseGet(() -> challengedProblemRepository.save(
                                                            ChallengedProblem
                                                            .builder()
                                                            .member(member)
                                                            .problem(problem)
                                                            .build())); // 만약 사용자가 해당문제를 처음 도전했다면 도전기록 엔티티를 생성하여 DB에 저장한다

            File wavFile = FileTypeConverter.saveMultipartFileToWavFile(audio); //사용자로부터 받은 오디오파일을 인코딩여 WAV파일로 저장한다.

            ChallengedProblemHistory challengedProblemHistory = runDenoiseAndAsrModel(); //WAV파일과 한글을 ASR 및 잡음제거 모델 API에 전송하고 점수와 피드백을 반환받는다.

            problem.increaseChallengedCnt();  //문제 도전 횟수를 1회증가 시키고 DB에 업데이트한다.
            member.increaseChallengeCnt();    //사용자 도전 횟수 1회증가 시키고 DB에 업데이트한다.

            //기존의 한글과 반환받은 한글이 일치하면 문제 정답 처리한다. 일치하지 않으면 정답 처리하지 않고 피드백을 반환한다.
            if(problem.getKorean().equals(challengedProblemHistory.getKorean())) {

                problem.increaseClearCnt();
                member.increaseSuccessCnt();
                challengedProblemHistory.setFeedback("잘하셨어요!");
                challengedProblemHistory.setClearState(ClearState.Y);

                //최초 클리어
                if(challengedProblem.getClearState() == null || challengedProblem.getClearState().equals(ClearState.N)) {

                    if(challengedProblem.getClearState() != null && challengedProblem.getClearState().equals(ClearState.N)) {
                        member.decreaseFailureProblemCnt();
                    }

                    member.increaseCumulativeExp(problem.getExp());
                    member.increaseSuccessProblemCnt();
                    challengedProblem.setClearState(ClearState.Y);
                }
            }

            else {

                member.increaseFailureCnt();
                
                String[] actualPronunciations = problem.getKorean().split(" ");
                String[] userPronunciations = challengedProblemHistory.getKorean().split(" ");
                String[] koPronunciations = problem.getKoPronunciation().split(" ");
                String[] enPronunciations = problem.getEnPronunciation().split(" ");
                
                String feedBack = "";

                if(actualPronunciations.length != userPronunciations.length) {

                    for(int i = 0; i < actualPronunciations.length; i++) {

                        feedBack +="\"" + actualPronunciations[i] + "\"" + "를(을) " +
                                    "\"" + koPronunciations[i] +  "(" + enPronunciations[i] + ")" + "\"" + "(으)로 발음해보세요.\n";
                    }
                }

                else {
                    for(int i = 0; i < actualPronunciations.length; i++) {

                        if(i >= userPronunciations.length) break;

                        if(!actualPronunciations[i].equals(userPronunciations[i])){
                            feedBack +="\"" + userPronunciations[i] + "\"" + "를(을) " +
                                    "\"" + koPronunciations[i] +  "(" + enPronunciations[i] + ")" + "\"" + "(으)로 발음해보세요.\n";
                        }
                    }
                }


                challengedProblemHistory.setFeedback(feedBack);
                challengedProblemHistory.setClearState(ClearState.N);

                //최초 실패
                if(challengedProblem.getClearState() == null) {
                    member.increaseFailureProblemCnt();
                    challengedProblem.setClearState(ClearState.N);
                }

            }

            challengedProblem.addChallengedProblemHistory(challengedProblemHistory);

            return ChallengedProblemHistoryResponse
                    .of(challengedProblemHistoryRepository.save(challengedProblemHistory));

    }

    private ChallengedProblemHistory runDenoiseAndAsrModel(File wavFile, String sentence) {
        return restTemplateService.runModels(wavFile, sentence);
    }

    private ChallengedProblemHistory runDenoiseAndAsrModel() {
        return ChallengedProblemHistory.builder()
                .korean("주말에는 공부 안 하고 쉬고 싶어요")
                .score(100f)
                .build();
    }


}
