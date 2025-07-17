package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.client.UserClient;
import org.example.exception.UserNotFoundException;
import org.example.mapper.UserMapper;
import org.example.model.dto.role.RoleResponseDto;
import org.example.model.dto.user.CreateUserInfoDto;
import org.example.model.dto.user.UserInfoResponseDto;
import org.example.model.dto.user.UserResponseDto;
import org.example.model.entity.user.Role;
import org.example.model.entity.user.UserInfo;
import org.example.repository.UserInfoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userRepository;
    private final UserClient userClient;
    private final UserMapper userMapper;

    public void register(CreateUserInfoDto request) {

        UserInfo user = new UserInfo();
        user.setId(request.getId());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        userRepository.save(user);
    }

    public UserInfoResponseDto getUserInfo(String userMail) {
        var user = userRepository.findByEmail(userMail).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        return new UserInfoResponseDto(
                user.getId(),
                userMail,
                user.getFirstName(),
                user.getLastName(),
                userClient.getUserRole(user.getId()).getRole()
        );
    }

    public List<UserInfoResponseDto> getUsersByRole(Role role) {
        var users = userClient.getUsersByRole(role);
        var userInfos = userRepository.findAllById(users.stream().map(UserResponseDto::getId).toList());

        return toUserInfoList(users, userInfos);
    }

    public List<UserInfoResponseDto> getUsers() {
        var users = userClient.getUsers();
        var userInfos = userRepository.findAllById(users.stream().map(UserResponseDto::getId).toList());

        return toUserInfoList(users, userInfos);
    }

    private List<UserInfoResponseDto> toUserInfoList(List<UserResponseDto> users, List<UserInfo> userInfos){
        var usersInfoResponse = new ArrayList<UserInfoResponseDto>();
        for (int i = 0; i < users.size(); i++) {
            usersInfoResponse.add(userMapper.toUserInfoResponseDto(users.get(i), userInfos.get(i)));
        }
        return usersInfoResponse;
    }



}

