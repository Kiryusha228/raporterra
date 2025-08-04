package ru.vldaislab.bekrenev.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.vldaislab.bekrenev.authservice.properties.ApiProperties;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private final ApiProperties apiProperties;

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder().baseUrl(apiProperties.getUrl()).build();
    }
}