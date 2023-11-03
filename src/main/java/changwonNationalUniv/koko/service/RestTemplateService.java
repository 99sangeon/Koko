package changwonNationalUniv.koko.service;

import changwonNationalUniv.koko.dto.response.ChallengedProblemHistoryResponse;
import changwonNationalUniv.koko.entity.ChallengedProblemHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestTemplateService {

    // http://localhost:9090/api/server/hello 로 요청해서 response를 받아오기
    public ChallengedProblemHistory runModels(File wavFile, String sentence, boolean denosieModeState){

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        try {
            body.add("file", new UrlResource("file:" + wavFile.getAbsolutePath()));
            log.info(wavFile.getAbsolutePath() + "-----------------------------------------------------------------");

            body.add("sentence", sentence);
        }
        catch (Exception e) {
            body.add("wavFile", "error");
        }
        body.add("hi", "안녕하세요");


        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);

        String serverUrl = "http://localhost:5000/runModels";

        if(denosieModeState) {
            serverUrl = "http://localhost:5000/runModels/deNoiseMode";
        }



        ResponseEntity<ChallengedProblemHistory> response = restTemplate
                .postForEntity(serverUrl, requestEntity, ChallengedProblemHistory.class);

        return response.getBody();
    }
}
