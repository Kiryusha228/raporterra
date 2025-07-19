package org.example.mapper;

import org.example.model.dto.user.UserInfoResponseDto;
import org.example.model.dto.user.UserResponseDto;
import org.example.model.entity.user.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "id", source = "userInfo.id")
    @Mapping(target = "email", source = "userInfo.email")
    @Mapping(target = "firstName", source = "userInfo.firstName")
    @Mapping(target = "lastName", source = "userInfo.lastName")
    @Mapping(target = "role", source = "userResponseDto.role")
    UserInfoResponseDto toUserInfoResponseDto(UserResponseDto userResponseDto, UserInfo userInfo);
}
