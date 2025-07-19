package ru.vldaislab.bekrenev.authservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import ru.vldaislab.bekrenev.authservice.config.WebClientConfig;
import ru.vldaislab.bekrenev.authservice.model.dto.CreateUserInfoDto;

@Component
@RequiredArgsConstructor
public class UserInfoClient {

    private final WebClientConfig webClient;

    public void addUserInfo(CreateUserInfoDto createUserInfoDto) {
        webClient
                .getWebClient()
                .post()
                .uri("user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createUserInfoDto)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

}
