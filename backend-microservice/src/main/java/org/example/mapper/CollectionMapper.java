package org.example.mapper;


import org.example.model.dto.collection.CollectionDto;
import org.example.model.dto.collection.CreateCollectionDto;
import org.example.model.entity.collection.Collection;
import org.example.model.entity.collection.CollectionAccess;
import org.example.model.entity.collection.CollectionReport;
import org.example.model.entity.group.Group;
import org.example.model.entity.report.Report;
import org.example.model.entity.user.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CollectionMapper {
    @Mapping(target = "createdBy", source = "createdBy.id")
    CollectionDto toCollectionDto(Collection collection);

    Set<CollectionDto> toCollectionDtoSet(Set<Collection> collections);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "collectionReports", ignore = true)
    Collection toEntity(CreateCollectionDto dto, UserInfo createdBy);

    @Mapping(target = "id", expression = "java(new org.example.model.entity.collection.CollectionReportId(collection.getId(), report.getId()))")
    @Mapping(target = "collection", source = "collection")
    @Mapping(target = "report", source = "report")
    CollectionReport toCollectionReport(Collection collection, Report report);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "collection", source = "collection")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "group", expression = "java(null)")
    CollectionAccess toCollectionAccess(Collection collection, UserInfo user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "collection", source = "collection")
    @Mapping(target = "group", source = "group")
    @Mapping(target = "user", expression = "java(null)")
    CollectionAccess toCollectionAccess(Collection collection, Group group);
}
