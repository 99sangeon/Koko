package changwonNationalUniv.koko.utils.file;

import changwonNationalUniv.koko.dto.request.UploadFile;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class FileTypeConverter {

    @Value("${file.dir}")
    private String fileDir;

    @Value("${ffmpeg.mpeg}")
    private String mpeg;

    @Value("${ffmpeg.probe}")
    private String probe;

    private final FileStore fileStore;

    public UploadFile saveMultipartFileToWavFile(MultipartFile audio) throws IOException {

        File inputFile = convertMultiPartFileToFile(audio);

        FFmpeg ffmpeg = new FFmpeg(mpeg); // ffmpeg 실행 파일 경로
        FFprobe ffprobe = new FFprobe(probe); // ffprobe 실행 파일 경로

        UploadFile uploadFile = new UploadFile("output.wav", UUID.randomUUID().toString() +".wav");
        File outputFile = new File(fileDir+uploadFile.getStoreFileName());

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
        } finally {
            inputFile.delete();
        }

        return uploadFile;
    }

    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        UploadFile uploadFile = fileStore.storeFile(file);
        return new File(fileStore.getFullPath(uploadFile.getStoreFileName()));
    }
}
