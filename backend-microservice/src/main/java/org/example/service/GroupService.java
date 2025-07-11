package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.exception.GroupNotFoundException;
import org.example.exception.UserNotFoundException;
import org.example.mapper.GroupMapper;
import org.example.model.dto.group.CreateGroupDto;
import org.example.model.dto.group.GroupDto;
import org.example.model.dto.group.UpdateGroupDto;
import org.example.model.entity.user.UserInfo;
import org.example.model.entity.usergroup.UserGroupId;
import org.example.repository.GroupRepository;
import org.example.repository.UserGroupRepository;
import org.example.repository.UserInfoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserInfoRepository userRepository;

    private final GroupMapper groupMapper;

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
        var user = userRepository.findByEmail(userMail).orElseThrow(
                () -> new NullPointerException("Пользователя не существует")
        );

        var group = groupMapper.toEntity(createGroupDto, user);

        var users = new ArrayList<UserInfo>();
        for (Long id : createGroupDto.getUsers()) {
            users.add(userRepository.findById(id).orElseThrow(
                    () -> new UserNotFoundException("Пользователь не найден"))
            );
        }

        var userGroups = groupMapper.toUserGroups(users, group, user);

        group.setUserGroups(userGroups);
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
    public void addUserToGroup(Long groupId, Long userId, String adminMail) {
        var group = groupRepository.findById(groupId).orElseThrow(
                ()-> new GroupNotFoundException("Группа с данным id не найдена!")
        );

        var user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("Пользователя не существует")
        );

        var admin = userRepository.findByEmail(adminMail).orElseThrow(
                () -> new NullPointerException("Пользователя не существует")
        );

        group.getUserGroups().add(groupMapper.toUserGroup(user, group, admin));
    }

    public void deleteGroup(Long groupId){
        groupRepository.deleteById(groupId);
    }

    @Transactional
    public void deleteUserFromGroup(Long groupId, Long userId) {
        var group = groupRepository.findById(groupId).orElseThrow(
                ()-> new GroupNotFoundException("Группа с данным id не найдена!")
        );

        var user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("Пользователя не существует")
        );

        var userGroupId = new UserGroupId(groupId,userId);
        var userGroup = userGroupRepository.findById(userGroupId).orElseThrow(
                () -> new GroupNotFoundException("Такая связь пользователя и группы не найдена!")
        );

        group.getUserGroups().remove(userGroup);

        userGroupRepository.deleteById(userGroupId);
    }


}
