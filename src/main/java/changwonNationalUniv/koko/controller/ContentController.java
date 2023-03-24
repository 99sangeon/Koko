package changwonNationalUniv.koko.controller;

import changwonNationalUniv.koko.controller.dto.UploadFile;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.sound.sampled.AudioFileFormat.Type;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/content")
public class ContentController {

    @Value("${file.dir}")
    private String fileDir;
    public String getFullPath(String filename) {
        return fileDir + filename;
    }


    @GetMapping("/speaking/{speakingListId}")
    public String speakingPage(@PathVariable int speakingListId, Model model) {

        model.addAttribute("sentence", "안녕하세요?");

        return "/content/speakingPage";
    }

    @PostMapping("/speaking/{speakingListId}")
    public ResponseEntity<String> speakingEvaluation(@PathVariable int speakingListId,
                                                     @RequestParam("audio") MultipartFile audio) throws IOException, UnsupportedAudioFileException {


        //받은 아이디와 오디로데이터를 채점 서비스로 넘겨줌 -> 채점서비는 해당 아디의 원래문자와 해당 오디오파일을 AI api에 돌린 후
        //원래 오디오파일의 텍스트와 정답여부등등을 보내줌.

        //storeFile(audio);

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
                    .setAudioSampleRate(44100)
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            FFmpegProbeResult probeResult = ffprobe.probe(inputFile.getAbsolutePath());

            executor.createJob(builder).run();
        } catch (IOException e) {
            throw new RuntimeException("Error converting file to wav", e);
        }

        return ResponseEntity.ok("ㅎㅇ");
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



    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {


        }
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        Files.write(Path.of(getFullPath(storeFileName)), multipartFile.getBytes());
        return new UploadFile(originalFilename, storeFileName);
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + "webm";
    }
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}

