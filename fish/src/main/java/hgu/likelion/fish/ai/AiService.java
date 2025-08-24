package hgu.likelion.fish.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class AiService {
    private final WebClient aiClient;

    @Autowired
    AiService(WebClient.Builder builder) {
        // 타임아웃 예시(선택)
        HttpClient httpClient = HttpClient.create().responseTimeout(java.time.Duration.ofSeconds(30));
        this.aiClient = builder
                .baseUrl("http://127.0.0.1:8000")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public InferenceResult relayToFastApi(org.springframework.web.multipart.MultipartFile file) throws Exception {
        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
            @Override public String getFilename() { return file.getOriginalFilename(); }
        };

        return aiClient.post()
                .uri("/infer/json")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file", resource))
                .retrieve()
                .bodyToMono(InferenceResult.class)
                .block();
    }
}
