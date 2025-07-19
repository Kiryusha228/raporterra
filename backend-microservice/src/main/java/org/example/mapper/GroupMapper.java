package org.example.mapper;

import org.example.model.dto.group.CreateGroupDto;
import org.example.model.dto.group.GroupDto;
import org.example.model.entity.group.Group;
import org.example.model.entity.user.UserInfo;
import org.example.model.entity.usergroup.UserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.HashSet;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GroupMapper {

    @Mapping(target = "createdBy", source = "createdBy.id")
    GroupDto toGroupDto(Group group);

    List<GroupDto> toGroupDtoList(List<Group> group);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdBy", source = "user")
    @Mapping(target = "userGroups", ignore = true)
    @Mapping(target = "collectionAccesses", ignore = true)
    Group toEntity(CreateGroupDto dto, UserInfo user);

    @Mapping(target = "id", expression = "java(new org.example.model.entity.usergroup.UserGroupId(group.getId(), userInfo.getId()))")
    @Mapping(target = "group", source = "group")
    @Mapping(target = "user", source = "userInfo")
    @Mapping(target = "joinedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "addedBy", source = "addedBy")
    UserGroup toUserGroup(UserInfo userInfo, Group group, UserInfo addedBy);

    HashSet<UserGroup> toUserGroups(List<UserInfo> userInfos, Group group, UserInfo addedBy);
}
