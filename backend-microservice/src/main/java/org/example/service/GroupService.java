package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.exception.GroupNotFoundException;
import org.example.exception.UserNotFoundException;
import org.example.mapper.GroupMapper;
import org.example.mapper.UserMapper;
import org.example.model.dto.group.CreateGroupDto;
import org.example.model.dto.group.GroupDto;
import org.example.model.dto.group.GroupForUserDto;
import org.example.model.dto.group.UpdateGroupDto;
import org.example.model.dto.user.UserInfoResponseDto;
import org.example.model.dto.user.UserResponseDto;
import org.example.model.entity.user.Role;
import org.example.model.entity.user.UserInfo;
import org.example.model.entity.usergroup.UserGroup;
import org.example.model.entity.usergroup.UserGroupId;
import org.example.repository.GroupRepository;
import org.example.repository.UserGroupRepository;
import org.example.repository.UserInfoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserInfoRepository userRepository;

    private final GroupMapper groupMapper;
    private final UserMapper userMapper;

    public List<GroupDto> getAllGroups() {
        return groupMapper.toGroupDtoList(groupRepository.findAll());
    }

    public List<Long> getUsersInGroup(Long groupId) {
        var group = groupRepository.findById(groupId).orElseThrow(
                ()-> new GroupNotFoundException("Группа с данным id не найдена!")
        );

        return group.getUserGroups().stream().map(userGroup -> userGroup.getId().getUserId()).toList();
    }

    @Transactional
    public void createGroup(CreateGroupDto createGroupDto, String userMail) {
        var admin = userRepository.findByEmail(userMail).orElseThrow(
                () -> new NullPointerException("Пользователя не существует")
        );

        var group = groupMapper.toEntity(createGroupDto, admin);

        var users = new ArrayList<UserInfo>();
        for (Long id : createGroupDto.getUsers()) {
            users.add(userRepository.findById(id).orElseThrow(
                    () -> new UserNotFoundException("Пользователь не найден"))
            );
        }
        group = groupRepository.save(group);

        var userGroups = new HashSet<UserGroup>();
        for (var user : users) {
            userGroups.add(groupMapper.toUserGroup(user, group, admin));
        }
        group.setUserGroups(userGroups);
        userGroupRepository.saveAll(userGroups);
        groupRepository.save(group);
    }

    @Transactional
    public void updateGroup(UpdateGroupDto updateGroupDto) {
        var group = groupRepository.findById(updateGroupDto.getId()).orElseThrow(
                ()-> new GroupNotFoundException("Группа с данным id не найдена!")
        );

        group.setName(updateGroupDto.getName());
        group.setDescription(updateGroupDto.getDescription());
    }

    @Transactional
    public void addUserToGroup(UserGroupId userGroupId, String adminMail) {
        var group = groupRepository.findById(userGroupId.getGroupId()).orElseThrow(
                ()-> new GroupNotFoundException("Группа с данным id не найдена!")
        );

        var user = userRepository.findById(userGroupId.getUserId()).orElseThrow(
                () -> new NullPointerException("Пользователя не существует")
        );

        var admin = userRepository.findByEmail(adminMail).orElseThrow(
                () -> new NullPointerException("Пользователя не существует")
        );
        var userGroup = groupMapper.toUserGroup(user, group, admin);

        userGroupRepository.save(userGroup);

        var userGroups = group.getUserGroups();
        userGroups.add(userGroup);
        group.setUserGroups(userGroups);
        groupRepository.save(group);
    }

    @Transactional
    public void deleteGroup(Long groupId){
        var group = groupRepository.findById(groupId).orElseThrow(
                ()-> new GroupNotFoundException("Группа с данным id не найдена!")
        );
        userGroupRepository.deleteAll(group.getUserGroups());
        groupRepository.delete(group);
    }

    @Transactional
    public void deleteUserFromGroup(UserGroupId userGroupId) {
        var group = groupRepository.findById(userGroupId.getGroupId()).orElseThrow(
                ()-> new GroupNotFoundException("Группа с данным id не найдена!")
        );

        userRepository.findById(userGroupId.getUserId()).orElseThrow(
                () -> new NullPointerException("Пользователя не существует")
        );

        var userGroup = userGroupRepository.findById(userGroupId).orElseThrow(
                () -> new GroupNotFoundException("Такая связь пользователя и группы не найдена!")
        );

        group.getUserGroups().remove(userGroup);

        userGroupRepository.deleteById(userGroupId);
    }

    public List<GroupForUserDto> findInfo(String userMail){
        var user = userRepository.findByEmail(userMail).orElseThrow(
                ()->new UserNotFoundException("пользователь не найден!")
        );
        var groupForUserList = new ArrayList<GroupForUserDto>();
        var groups = user.getUserGroups().stream().map(UserGroup::getGroup).toList();

        for (var group : groups) {
            var users = group.getUserGroups().stream().map(UserGroup::getUser).filter(Objects::nonNull).toList();
            var usersInfoResponse = new ArrayList<UserInfoResponseDto>();
            for (var u : users) {
                usersInfoResponse.add(userMapper.toUserInfoResponseDto(new UserResponseDto(u.getId(), Role.USER), u));
            }
            var groupForUser = new GroupForUserDto(groupMapper.toGroupDto(group), usersInfoResponse);
            groupForUserList.add(groupForUser);
        }
        return groupForUserList;
    }


}
