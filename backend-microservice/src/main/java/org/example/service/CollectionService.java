package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.exception.ReportNotFoundException;
import org.example.mapper.CollectionMapper;
import org.example.model.dto.collection.CollectionDto;
import org.example.model.dto.collection.CreateCollectionDto;
import org.example.model.entity.collection.Collection;
import org.example.model.entity.collection.CollectionReport;
import org.example.model.entity.collection.CollectionReportId;
import org.example.model.entity.group.Group;
import org.example.model.entity.usergroup.UserGroup;
import org.example.repository.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CollectionService {
    private final UserInfoRepository userInfoRepository;
    private final UserGroupRepository userGroupRepository;
    private final CollectionRepository collectionRepository;
    private final ReportRepository reportRepository;
    private final CollectionAccessRepository collectionAccessRepository;
    private final CollectionReportRepository collectionReportRepository;

    private final CollectionMapper collectionMapper;


    public Set<CollectionDto> getCollections(String userMail) {
        var user = userInfoRepository.findByEmail(userMail).orElseThrow(
                () -> new NullPointerException("Пользователя не существует")
        );

        List<UserGroup> userGroups = userGroupRepository.findByUser(user);
        List<Group> groups = userGroups.stream().map(UserGroup::getGroup).toList();

        var collectionAccess = collectionAccessRepository.findByUser(user);
        for (var group : groups) {
            collectionAccess.addAll(collectionAccessRepository.findByGroup(group));
        }

        var collections = new HashSet<Collection>();

        for (var access : collectionAccess){
            collections.add(access.getCollection());
        }

        return collectionMapper.toCollectionDtoSet(collections);
    }

    @Transactional
    public void createCollection(CreateCollectionDto createCollectionDto, String userMail) {
        var user = userInfoRepository.findByEmail(userMail).orElseThrow(
                () -> new NullPointerException("Пользователя не существует")
        );

        var collection = collectionRepository.save(
                collectionMapper.toEntity(createCollectionDto, user)
        );

        var collectionReports = new HashSet<CollectionReport>();
        for (var reportId : createCollectionDto.getReports()){
            var collectionReport = new CollectionReport();
            collectionReport.setId(new CollectionReportId(collection.getId(), reportId));
            collectionReport.setCollection(collection);
            collectionReport.setReport(
                    reportRepository.findById(reportId).orElseThrow(
                            () -> new ReportNotFoundException("Отчет с id " + reportId + " не найден!")
                    )
            );
            collectionReports.add(collectionReport);
            collectionReportRepository.save(collectionReport);
        }

        collection.setCollectionReports(collectionReports);
        collectionRepository.save(collection);
    }

}
