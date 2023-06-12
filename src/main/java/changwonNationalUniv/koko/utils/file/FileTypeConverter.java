package changwonNationalUniv.koko.utils.file;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class FileTypeConverter {

    @Value("${file.dir}")
    private static String fileDir;

    @Value("${ffmpeg/ffmpeg}")
    private static String mpeg;

    @Value("${ffmpeg/probe}")
    private static String probe;

    public static File saveMultipartFileToWavFile(MultipartFile audio) throws IOException {

        File inputFile = convertMultiPartFileToFile(audio);

        FFmpeg ffmpeg = new FFmpeg(mpeg); // ffmpeg 실행 파일 경로
        FFprobe ffprobe = new FFprobe(probe); // ffprobe 실행 파일 경로
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

    private static File convertMultiPartFileToFile(MultipartFile file) {
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
