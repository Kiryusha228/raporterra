package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.CreateUserInfoDto;
import org.example.model.entity.user.UserInfo;
import org.example.repository.UserInfoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void register(CreateUserInfoDto request) {

        UserInfo user = new UserInfo();
        user.setId(request.getId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        userRepository.save(user);
    }
}

