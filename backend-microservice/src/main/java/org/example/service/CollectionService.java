package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.client.UserClient;
import org.example.exception.CollectionAccessDeniedException;
import org.example.exception.CollectionNotFoundException;
import org.example.exception.GroupNotFoundException;
import org.example.exception.ReportNotFoundException;
import org.example.mapper.CollectionMapper;
import org.example.mapper.GroupMapper;
import org.example.mapper.ReportMapper;
import org.example.mapper.UserMapper;
import org.example.model.dto.collection.CollectionDto;
import org.example.model.dto.collection.CreateCollectionDto;
import org.example.model.dto.collection.UpdateCollectionDto;
import org.example.model.dto.group.GroupDto;
import org.example.model.dto.report.AvailableReportsDto;
import org.example.model.dto.user.UserInfoResponseDto;
import org.example.model.dto.user.UserResponseDto;
import org.example.model.entity.collection.Collection;
import org.example.model.entity.collection.CollectionAccess;
import org.example.model.entity.collection.CollectionReport;
import org.example.model.entity.collection.CollectionReportId;
import org.example.model.entity.group.Group;
import org.example.model.entity.user.Role;
import org.example.model.entity.usergroup.UserGroup;
import org.example.repository.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionService {
    private final UserInfoRepository userInfoRepository;
    private final UserGroupRepository userGroupRepository;
    private final CollectionRepository collectionRepository;
    private final ReportRepository reportRepository;
    private final CollectionAccessRepository collectionAccessRepository;
    private final CollectionReportRepository collectionReportRepository;
    private final GroupRepository groupRepository;

    private final CollectionMapper collectionMapper;
    private final ReportMapper reportMapper;
    private final UserMapper userMapper;
    private final GroupMapper groupMapper;


    public Set<CollectionDto> getCollections(String userMail, String role) {
        var user = userInfoRepository.findByEmail(userMail).orElseThrow(
                () -> new NullPointerException("Пользователя не существует")
        );

        if (role.equals("ROLE_ADMIN")){
            return collectionMapper.toCollectionDtoSet(new HashSet<>(collectionRepository.findAll()));
        }
        else {
            List<UserGroup> userGroups = userGroupRepository.findByUser(user);
            List<Group> groups = userGroups.stream().map(UserGroup::getGroup).toList();

            var collectionAccess = collectionAccessRepository.findByUser(user);
            for (var group : groups) {
                collectionAccess.addAll(collectionAccessRepository.findByGroup(group));
            }

            var collections = new HashSet<Collection>();

            for (var access : collectionAccess) {
                collections.add(access.getCollection());
            }

            return collectionMapper.toCollectionDtoSet(collections);
        }
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
        for (var reportId : createCollectionDto.getReports()) {
            var report = reportRepository.findById(reportId).orElseThrow(
                    () -> new ReportNotFoundException("Отчет с id " + reportId + " не найден!")
            );

            var collectionReport = collectionReportRepository.save(
                    collectionMapper.toCollectionReport(collection, report)
            );

            collectionReports.add(collectionReport);
        }

        collection.setCollectionReports(collectionReports);
        collectionRepository.save(collection);
    }

    public void updateCollection(UpdateCollectionDto updateCollectionDto, Long collectionId) {
        var collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new CollectionNotFoundException("Соединение не найдено!")
        );
        collection.setName(updateCollectionDto.getName());
        collection.setDescription(updateCollectionDto.getDescription());
        collectionRepository.save(collection);
    }

    @Transactional
    public void addReportToCollection(Long collectionId, UUID reportId) {
        var collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new CollectionNotFoundException("Соединение не найдено!")
        );
        var report = reportRepository.findById(reportId).orElseThrow(
                () -> new ReportNotFoundException("Отчет с не найден!")
        );

        var collectionReport = collectionReportRepository.save(
                collectionMapper.toCollectionReport(collection, report)
        );

        var collectionReports = collection.getCollectionReports();

        collectionReports.add(collectionReport);
        collection.setCollectionReports(collectionReports);
        collectionRepository.save(collection);
    }

    @Transactional
    public void deleteReportFromCollection(Long collectionId, UUID reportId) {
        var collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new CollectionNotFoundException("Соединение не найдено!")
        );
        var report = reportRepository.findById(reportId).orElseThrow(
                () -> new ReportNotFoundException("Отчет с не найден!")
        );

        var collectionReport = collectionReportRepository.findById(new CollectionReportId(collectionId, reportId))
                .orElseThrow(
                        () -> new GroupNotFoundException("Такая связь коллекции и группы не найдена!")
                );

        collection.getCollectionReports().remove(collectionReport);

        collectionRepository.save(collection);
    }

    @Transactional
    public void addUserToCollection(Long collectionId, Long userId) {
        var collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new CollectionNotFoundException("Соединение не найдено!")
        );
        var user = userInfoRepository.findById(userId).orElseThrow(
                () -> new ReportNotFoundException("Пользователь не найден!")
        );

        var collectionAccess = collectionAccessRepository.save(
                collectionMapper.toCollectionAccess(collection, user)
        );

        var collectionAccesses = collection.getCollectionAccesses();

        collectionAccesses.add(collectionAccess);
        collection.setCollectionAccesses(collectionAccesses);
        collectionRepository.save(collection);
    }

    @Transactional
    public void addGroupToCollection(Long collectionId, Long groupId) { //todo возможно убрать дубляж
        var collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new CollectionNotFoundException("Соединение не найдено!")
        );
        var group = groupRepository.findById(groupId).orElseThrow(
                () -> new GroupNotFoundException("Группа не найдена!")
        );

        var collectionAccess = collectionAccessRepository.save(
                collectionMapper.toCollectionAccess(collection, group)
        );

        var collectionAccesses = collection.getCollectionAccesses();

        collectionAccesses.add(collectionAccess);
        collection.setCollectionAccesses(collectionAccesses);
        collectionRepository.save(collection);
    }

    @Transactional
    public void deleteUserFromCollection(Long collectionId, Long userId) {
        var collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new CollectionNotFoundException("Соединение не найдено!")
        );
        var user = userInfoRepository.findById(userId).orElseThrow(
                () -> new ReportNotFoundException("Пользователь не найден!")
        );

        var collectionAccess = collectionAccessRepository.findByUserAndCollection(user, collection)
                .orElseThrow(
                        () -> new GroupNotFoundException("Такая связь коллекции и пользователя не найдена!")
                );

        collection.getCollectionAccesses().remove(collectionAccess);

        collectionRepository.save(collection);
    }

    @Transactional
    public void deleteGroupFromCollection(Long collectionId, Long groupId) {//todo тоже убрать дубляж
        var collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new CollectionNotFoundException("Соединение не найдено!")
        );
        var group = groupRepository.findById(groupId).orElseThrow(
                () -> new GroupNotFoundException("Группа не найдена!")
        );

        var collectionAccess = collectionAccessRepository.findByGroupAndCollection(group, collection)
                .orElseThrow(
                        () -> new GroupNotFoundException("Такая связь коллекции и группы не найдена!")
                );

        collection.getCollectionAccesses().remove(collectionAccess);

        collectionRepository.save(collection);
    }

    public List<AvailableReportsDto> getReportsInCollection(Long collectionId, String userMail, String role) {
        var collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new CollectionNotFoundException("Соединение не найдено!")
        );

        var user = userInfoRepository.findByEmail(userMail).orElseThrow(
                () -> new NullPointerException("Пользователя не существует")
        );

        boolean hasDirectAccess = collectionAccessRepository.existsByCollectionAndUser(collection, user);
        boolean hasGroupAccess = user.getUserGroups().stream()
                .map(UserGroup::getGroup)
                .anyMatch(group -> collectionAccessRepository.existsByCollectionAndGroup(collection, group));

        if (!hasDirectAccess && !hasGroupAccess && !role.equals("ROLE_ADMIN")) {
            throw new CollectionAccessDeniedException("Пользователь не имеет доступа!");
        }


        var reports = new ArrayList<AvailableReportsDto>();

        for (var collectionReport : collection.getCollectionReports()) {
            reports.add(reportMapper.toAvailableReportsDto(collectionReport.getReport()));
        }

        return reports;
    }

    public List<UserInfoResponseDto> getUsersInCollection(Long collectionId) {
        var collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new CollectionNotFoundException("Соединение не найдено!")
        );


        var users = collection.getCollectionAccesses()
                .stream()
                .map(CollectionAccess::getUser)
                .filter(Objects::nonNull)
                .toList();

        var usersInfoResponse = new ArrayList<UserInfoResponseDto>();
        for (var user : users) {
            usersInfoResponse.add(userMapper.toUserInfoResponseDto(new UserResponseDto(user.getId(), Role.USER), user));
        }

        return usersInfoResponse;
    }

    public List<GroupDto> getGroupsInCollection(Long collectionId) {
        var collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new CollectionNotFoundException("Соединение не найдено!")
        );


        var groups = collection.getCollectionAccesses()
                .stream()
                .map(CollectionAccess::getGroup)
                .filter(Objects::nonNull)
                .toList();

        return groupMapper.toGroupDtoList(groups);
    }

    public void deleteCollection(Long collectionId){
        var collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new CollectionNotFoundException("Соединение не найдено!")
        );
        collectionRepository.delete(collection);
    }

}
