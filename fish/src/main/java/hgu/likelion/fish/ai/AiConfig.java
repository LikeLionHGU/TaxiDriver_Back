package hgu.likelion.fish.ai;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AiConfig {
    @Bean
    public WebClient aiClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://127.0.0.1:8000") // FastAPI 주소
                .build();
    }
}
