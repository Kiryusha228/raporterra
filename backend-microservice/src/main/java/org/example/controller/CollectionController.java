package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.collection.CollectionDto;
import org.example.model.dto.collection.CreateCollectionDto;
import org.example.model.dto.collection.UpdateCollectionDto;
import org.example.model.dto.report.AvailableReportsDto;
import org.example.service.CollectionService;
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
    public Set<CollectionDto> getCollections(Principal principal){
        return collectionService.getCollections(principal.getName());
    }

    @GetMapping("/get/reports/{collectionId}")
    public List<AvailableReportsDto> getReportsInCollection(Principal principal, @PathVariable Long collectionId){
        return collectionService.getReportsInCollection(collectionId, principal.getName());
    }

    @PostMapping("")
    public void createCollection(@RequestBody CreateCollectionDto createCollectionDto, Principal principal){
        collectionService.createCollection(createCollectionDto, principal.getName());
    }

    @PutMapping("/{collectionId}")
    public void updateCollection(@RequestBody UpdateCollectionDto updateCollectionDto, @PathVariable Long collectionId){
        collectionService.updateCollection(updateCollectionDto, collectionId);
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
