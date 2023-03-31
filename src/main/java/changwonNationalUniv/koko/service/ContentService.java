package changwonNationalUniv.koko.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ContentService {
    void evaluate(Long id, MultipartFile audio) throws IOException;
}
