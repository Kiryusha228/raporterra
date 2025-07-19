package org.example.client;

import lombok.RequiredArgsConstructor;
import org.example.config.WebClientConfig;
import org.example.model.dto.role.ChangeRoleDto;
import org.example.model.dto.role.RoleResponseDto;
import org.example.model.dto.user.UserResponseDto;
import org.example.model.entity.user.Role;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class UserClient {

    private final WebClientConfig webClient;

    public void changeUserRole(ChangeRoleDto changeRoleDto) {
        webClient
                .getWebClient()
                .put()
                .uri("user/role")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(changeRoleDto)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public RoleResponseDto getUserRole(Long userId) {
        return webClient
                .getWebClient()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("user/role")
                        .queryParam("userId", userId)
                        .build())
                .retrieve()
                .bodyToMono(RoleResponseDto.class)
                .block();
    }

    public List<UserResponseDto> getUsersByRole(Role role) {
        return webClient
                .getWebClient()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("user/get/by-role")
                        .queryParam("role", role.name())
                        .build())
                .retrieve()
                .bodyToFlux(UserResponseDto.class)
                .collectList()
                .block();
    }

    public List<UserResponseDto> getUsers() {
        return webClient
                .getWebClient()
                .get()
                .uri("user/get/all")
                .retrieve()
                .bodyToFlux(UserResponseDto.class)
                .collectList()
                .block();
    }

}
