package changwonNationalUniv.koko.service;

import changwonNationalUniv.koko.controller.dto.ProblemResponse;
import changwonNationalUniv.koko.controller.dto.StepResponse;
import changwonNationalUniv.koko.entity.Problem;
import changwonNationalUniv.koko.entity.Step;
import changwonNationalUniv.koko.repository.ProblemRepository;
import changwonNationalUniv.koko.repository.StepRepository;
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
@RequiredArgsConstructor
@Transactional
public class ContentServiceImpl implements ContentService{

    private final ProblemRepository problemRepository;
    private final StepRepository stepRepository;

    @Value("${file.dir}")
    private String fileDir;

    @Override
    public List<ProblemResponse> findProblems(int level) {

        List<Problem> problems = problemRepository.findByLevel(level);
        List<ProblemResponse> problemResponses = new ArrayList<>();

        for (Problem problem: problems) {
            problemResponses.add(ProblemResponse.of(problem));
        }

        return problemResponses;
    }

    @Override
    public ProblemResponse findProblem(Long id) {

        Problem problem= problemRepository.findById(id).orElseThrow(() -> new NoSuchElementException());

        return ProblemResponse.of(problem);
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

    @Override
    public String findFilename(Long id) {
        return problemRepository.findFilename(id).orElseThrow(() -> new NoSuchElementException());
    }

    @Override
    public void evaluate(Long id, MultipartFile audio) throws IOException {

        saveMultipartFileToWavFile(audio);

    }

    private void saveMultipartFileToWavFile(MultipartFile audio) throws IOException {

        File inputFile = convertMultiPartFileToFile(audio);

        FFmpeg ffmpeg = new FFmpeg("/opt/homebrew/bin/ffmpeg"); // ffmpeg 실행 파일 경로
        FFprobe ffprobe = new FFprobe("/opt/homebrew/bin/ffprobe"); // ffprobe 실행 파일 경로
        File outputFile = new File(fileDir+"/output.wav");

        try {
            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(inputFile.getAbsolutePath())
                    .overrideOutputFiles(true)
                    .addOutput(outputFile.getAbsolutePath())
                    .setFormat("wav")
                    .setAudioChannels(2)
                    .setAudioCodec("pcm_s16le")
                    .setAudioSampleRate(48000)
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            FFmpegProbeResult probeResult = ffprobe.probe(inputFile.getAbsolutePath());
            executor.createJob(builder).run();

        } catch (IOException e) {
            throw new RuntimeException("Error converting file to wav", e);
        }
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
