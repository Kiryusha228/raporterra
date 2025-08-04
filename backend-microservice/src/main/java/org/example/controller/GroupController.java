package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.group.CreateGroupDto;
import org.example.model.dto.group.GroupDto;
import org.example.model.dto.group.UpdateGroupDto;
import org.example.model.entity.usergroup.UserGroupId;
import org.example.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService groupService;

    @GetMapping("")
    public List<GroupDto> getAllGroups(){
        return groupService.getAllGroups();
    }

    @GetMapping("/{groupId}")
    public List<Long> getUsersInGroup(@PathVariable Long groupId){
        return groupService.getUsersInGroup(groupId);
    }

    @PostMapping("")
    public void createGroup(Principal principal, @RequestBody CreateGroupDto createGroupDto) {
        groupService.createGroup(createGroupDto, principal.getName());
    }

    @PutMapping("")
    public void updateGroup(@RequestBody UpdateGroupDto updateGroupDto) {
        groupService.updateGroup(updateGroupDto);
    }

    @PostMapping("/add/")
    public void addUserToGroup(Principal principal, @RequestBody UserGroupId userGroupId) {
        groupService.addUserToGroup(userGroupId, principal.getName());
    }

    @DeleteMapping("/{groupId}")
    public void deleteGroup(@PathVariable Long groupId) {
        groupService.deleteGroup(groupId);
    }

    @DeleteMapping("/delete")
    public void deleteUserFromGroup(UserGroupId userGroupId) {
        groupService.deleteUserFromGroup(userGroupId);
    }

}
