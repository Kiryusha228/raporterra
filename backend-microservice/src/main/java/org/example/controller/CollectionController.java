package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.collection.CollectionDto;
import org.example.model.dto.collection.CreateCollectionDto;
import org.example.model.dto.collection.UpdateCollectionDto;
import org.example.model.dto.group.GroupDto;
import org.example.model.dto.report.AvailableReportsDto;
import org.example.model.dto.user.UserInfoResponseDto;
import org.example.service.CollectionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/collections")
public class CollectionController {
    private final CollectionService collectionService;

    @GetMapping("/get")
    public Set<CollectionDto> getCollections(Authentication authentication){
        var role = authentication.getAuthorities().stream().toList().get(0).toString();
        var mail = authentication.getName();
        return collectionService.getCollections(mail, role);
    }

    @GetMapping("/get/reports/{collectionId}")
    public List<AvailableReportsDto> getReportsInCollection(Authentication authentication, @PathVariable Long collectionId){
        var role = authentication.getAuthorities().stream().toList().get(0).toString();
        var mail = authentication.getName();

        return collectionService.getReportsInCollection(collectionId, mail, role);
    }

    @GetMapping("/get/users/{collectionId}")
    public List<UserInfoResponseDto> getUsersInCollection(@PathVariable Long collectionId){
        return collectionService.getUsersInCollection(collectionId);
    }

    @GetMapping("/get/groups/{collectionId}")
    public List<GroupDto> getGroupsInCollection(@PathVariable Long collectionId){
        return collectionService.getGroupsInCollection(collectionId);
    }

    @PostMapping("")
    public void createCollection(@RequestBody CreateCollectionDto createCollectionDto, Principal principal){
        collectionService.createCollection(createCollectionDto, principal.getName());
    }

    @PutMapping("/{collectionId}")
    public void updateCollection(@RequestBody UpdateCollectionDto updateCollectionDto, @PathVariable Long collectionId){
        collectionService.updateCollection(updateCollectionDto, collectionId);
    }

    @DeleteMapping("/{collectionId}")
    public void deleteCollection(@PathVariable Long collectionId){
        collectionService.deleteCollection(collectionId);
    }

    @PostMapping("/{collectionId}/reports/{reportId}")
    public void addReportToCollection(@PathVariable Long collectionId, @PathVariable UUID reportId){
        collectionService.addReportToCollection(collectionId, reportId);
    }

    @DeleteMapping("/{collectionId}/reports/{reportId}")
    public void deleteReportFromCollection(@PathVariable Long collectionId, @PathVariable UUID reportId){
        collectionService.deleteReportFromCollection(collectionId, reportId);
    }

    @PostMapping("/{collectionId}/access/{userId}")
    public void addUserToCollection(@PathVariable Long collectionId, @PathVariable Long userId){
        collectionService.addUserToCollection(collectionId, userId);
    }

    @DeleteMapping("/{collectionId}/access/{userId}")
    public void deleteUserFromCollection(@PathVariable Long collectionId, @PathVariable Long userId){
        collectionService.deleteUserFromCollection(collectionId, userId);
    }

    @PostMapping("/{collectionId}/access/group/{groupId}")
    public void addGroupToCollection(@PathVariable Long collectionId, @PathVariable Long groupId){
        collectionService.addGroupToCollection(collectionId, groupId);
    }

    @DeleteMapping("/{collectionId}/access/group/{groupId}")
    public void deleteGroupFromCollection(@PathVariable Long collectionId, @PathVariable Long groupId){
        collectionService.deleteGroupFromCollection(collectionId, groupId);
    }







}
