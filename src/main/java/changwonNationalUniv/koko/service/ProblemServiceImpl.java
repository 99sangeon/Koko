package changwonNationalUniv.koko.service;

import changwonNationalUniv.koko.dto.request.ProblemRequest;
import changwonNationalUniv.koko.dto.request.UploadFile;
import changwonNationalUniv.koko.dto.response.ChallengedProblemHistoryResponse;
import changwonNationalUniv.koko.dto.response.ProblemResponse;
import changwonNationalUniv.koko.entity.*;
import changwonNationalUniv.koko.enums.ClearState;
import changwonNationalUniv.koko.repository.*;
import changwonNationalUniv.koko.utils.file.FileStore;
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

    @Value("${file.dir}")
    private String fileDir;
    
    private final MemberService memberService;
    private final ProblemRepository problemRepository;
    private final SecurityService securityService;
    private final ChallengedProblemRepository challengedProblemRepository;
    private final ChallengedProblemHistoryRepository challengedProblemHistoryRepository;
    private final StepRepository stepRepository;
    private final FileStore fileStore;
    private final RestTemplateService restTemplateService;

    @Override
    public Long saveProblem(ProblemRequest problemRequest) throws IOException {

        if(problemRequest.getAudioFile() != null) {
            UploadFile uploadFile = fileStore.storeFile(problemRequest.getAudioFile());
            problemRequest.setUploadFile(uploadFile);
        }

        Step step = stepRepository.findOneByLevel(problemRequest.getLevel())
                .orElseThrow(() -> new NoSuchElementException());

        Problem problem = problemRequest.toEntity();
        step.addProblem(problem);

        problemRepository.save(problem);

        return problem.getId();
    }

    @Override
    public int deleteProblem(Long id) {
        Problem problem= problemRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
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
    public List<ProblemResponse> findProblems(int level) {

        List<ProblemResponse> problemResponses = new ArrayList<>();

        if(securityService.isAuthenticated()) {
            Member member = memberService.getCurrentMember();
            problemResponses = problemRepository.findWithCpByMemberAndLevel(member, level);
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

        Problem problem= problemRepository.findById(id).orElseThrow(() -> new NoSuchElementException());

        return ProblemResponse.of(problem);
    }

    @Override
    public String findFilename(Long id) {
        return problemRepository.findFilename(id).orElseThrow(() -> new NoSuchElementException());
    }

    @Override
    public ChallengedProblemHistoryResponse evaluate(Long problem_id, MultipartFile audio) throws IOException {

            Member member = memberService.getCurrentMember();
            Problem problem = problemRepository.findById(problem_id).orElseThrow(() -> new NoSuchElementException());
            ChallengedProblem challengedProblem = challengedProblemRepository
                                                    .findChallengedProblemByMemberAndProblem(member, problem)
                                                    .orElseGet(() -> challengedProblemRepository.save(
                                                            ChallengedProblem
                                                            .builder()
                                                            .member(member)
                                                            .problem(problem)
                                                            .build()));

            File wavFile = saveMultipartFileToWavFile(audio);

            ChallengedProblemHistoryResponse challengedProblemHistoryResponse = runDenoiseAndAsrModel(wavFile);

            ChallengedProblemHistory challengedProblemHistory = ChallengedProblemHistory
                    .builder()
                    .score(challengedProblemHistoryResponse.getScore())
                    .korean(challengedProblemHistoryResponse.getKorean())
                    .build();

            problem.increaseChallengeCnt();  //문제 도전 횟수 1회증가
            member.increaseChallengeCnt();   //사용자 도전 횟수 1회증가

            //기존의 한글과 출력된 한글이 일치하면 문제 정답 처리, 일치하지 않으면 정답 처리X 및 피드백 제공
            if(problem.getKorean().equals(challengedProblemHistoryResponse.getKorean())) {

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
                challengedProblemHistory.setFeedback("아쉽네요. 다시 한번 도전해보세요!");
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

    private ChallengedProblemHistoryResponse runDenoiseAndAsrModel(File wavFile) {
        return restTemplateService.runModels(wavFile);
    }

    private File saveMultipartFileToWavFile(MultipartFile audio) throws IOException {

        File inputFile = convertMultiPartFileToFile(audio);

        //FFmpeg ffmpeg = new FFmpeg("/opt/homebrew/bin/ffmpeg"); // ffmpeg 실행 파일 경로
        //FFprobe ffprobe = new FFprobe("/opt/homebrew/bin/ffprobe"); // ffprobe 실행 파일 경로
        FFmpeg ffmpeg = new FFmpeg("C:/ffmpeg/bin/ffmpeg"); // ffmpeg 실행 파일 경로
        FFprobe ffprobe = new FFprobe("C:/ffmpeg/bin//ffprobe"); // ffprobe 실행 파일 경로
        File outputFile = new File(fileDir+"/output.wav");

        try {
            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(inputFile.getAbsolutePath())
                    .overrideOutputFiles(true)
                    .addOutput(outputFile.getAbsolutePath())
                    .setFormat("wav")
                    .setAudioChannels(1)
                    .setAudioCodec("pcm_s16le")
                    .setAudioSampleRate(16000)
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            FFmpegProbeResult probeResult = ffprobe.probe(inputFile.getAbsolutePath());
            executor.createJob(builder).run();

        } catch (IOException e) {
            throw new RuntimeException("Error converting file to wav", e);
        }

        return outputFile;
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(fileDir+file.getOriginalFilename());
        try {
            FileOutputStream fos = new FileOutputStream(convertedFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException("Error converting multipart file to file", e);
        }
        return convertedFile;
    }


}
